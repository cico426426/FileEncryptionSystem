package utils

import org.jetbrains.skia.impl.Log
import java.io.File
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class Crypt {
    companion object {
        const val AES = "AES"
        const val DES = "DES"

        const val AES_CBC_NO_PADDING = "AES/CBC/NoPadding"
        const val AES_CBC_PKCS5_PADDING = "AES/CBC/PKCS5Padding"
        const val AES_ECB_NO_PADDING = "AES/ECB/NoPadding"
        const val AES_ECB_PKCS5_PADDING = "AES/ECB/PKCS5Padding"
        const val DES_CBC_NO_PADDING = "DES/CBC/NoPadding"
        const val DES_CBC_PKCS5_PADDING = "DES/CBC/PKCS5Padding"
        const val DES_ECB_NO_PADDING = "DES/ECB/NoPadding"
        const val DES_ECB_PKCS5_PADDING = "DES/ECB/PKCS5Padding"

        const val DES_SUPPORTING_TEXT = "Token length should be 8 characters long."
        const val AES_SUPPORTING_TEXT = "Token length should be 16 characters long."
    }

    private var files = mutableListOf<File>()
    private var encryptionMethodMismatchFiles = mutableListOf<String>()

    fun encrypt(type: String, path: List<String>, token: String): String {
        path.forEach {
            this.files.addAll(getFilesUnderPath(it))
        }

        val cipher = getCipher(Cipher.ENCRYPT_MODE, type, token)

        files.forEach {
            val content = it.readText().toByteArray()
            val encrypt = cipher.doFinal(content)
            it.writeText("$type:${String(Base64.getEncoder().encode(encrypt))}")
        }

        return getMessages()
    }

    fun decrypt(type: String, path: List<String>, token: String): String {
        path.forEach {
            this.files.addAll(getFilesUnderPath(it))
        }

        val cipher = getCipher(Cipher.DECRYPT_MODE, type, token)

        files.forEach {
            var content = it.readText()

            if (!content.startsWith("$type:")) {
                this.encryptionMethodMismatchFiles.add(it.path)
                return@forEach
            }

            content = content.removePrefix("$type:")
            Log.debug(content)
            val decrypt = cipher.doFinal(Base64.getDecoder().decode(content))
            it.writeText(decrypt.decodeToString())
        }

        return getMessages()
    }

    private fun getCipher(mode: Int, type: String, token: String): Cipher {
        return when (type) {
            AES, AES_ECB_NO_PADDING, AES_ECB_PKCS5_PADDING -> {
                val cipher = Cipher.getInstance(type)
                val secretKeySpec = SecretKeySpec(token.toByteArray(), AES)
                cipher.init(mode, secretKeySpec)
                cipher
            }

            AES_CBC_NO_PADDING, AES_CBC_PKCS5_PADDING -> {
                val cipher = Cipher.getInstance(type)
                val secretKeySpec = SecretKeySpec(token.toByteArray(), AES)
                val ivParameterSpec = IvParameterSpec(token.toByteArray())
                cipher.init(mode, secretKeySpec, ivParameterSpec)
                cipher
            }

            DES, DES_ECB_NO_PADDING, DES_ECB_PKCS5_PADDING -> {
                val cipher = Cipher.getInstance(type)
                val secretKeySpec = SecretKeySpec(token.toByteArray(), DES)
                cipher.init(mode, secretKeySpec)
                cipher
            }

            DES_CBC_NO_PADDING, DES_CBC_PKCS5_PADDING -> {
                val cipher = Cipher.getInstance(type)
                val secretKeySpec = SecretKeySpec(token.toByteArray(), DES)
                val ivParameterSpec = IvParameterSpec(token.toByteArray())
                cipher.init(mode, secretKeySpec, ivParameterSpec)
                cipher
            }

            else -> throw Exception("Not supported encryption method")
        }
    }

    private fun getFilesUnderPath(path: String): List<File> {
        val p = File(path)

        return if (p.isDirectory) {
            p.walkTopDown().filter { it.isFile }.toList()
        } else {
            listOf(p)
        }
    }

    private fun getMessages(): String {
        val processedCount = this.files.count() - this.encryptionMethodMismatchFiles.count()
        var message = "$processedCount file(s) processed.\n"

        if (this.encryptionMethodMismatchFiles.isNotEmpty()) {
            message += "\nThese files cannot be decrypted because these files are not encrypted or the decryption method does not match the encryption method:\n"

            this.encryptionMethodMismatchFiles.forEach { message += it + "\n" }
        }

        return message
    }
}
