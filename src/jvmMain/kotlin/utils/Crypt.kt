package utils

import java.io.File
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

class Crypt {
    companion object {
        const val AES = "AES"
        const val DES = "DES"

        const val AES_CBC_NO_PADDING = "AES/CBC/NoPadding"
        const val AES_CBC_PKCS5_PADDING = "AES/CBC/PKCS5Padding"
        const val AES_ECB_NO_PADDING = "AES/ECB/NoPadding"
        const val AES_ECB_PKCS5_PADDING = "AES/ECB/PKCS5Padding"
        const val AES_GCM_No_PADDING = "AES/GCM/NoPadding"
        const val DES_CBC_NO_PADDING = "DES/CBC/NoPadding"
        const val DES_CBC_PKCS5_PADDING = "DES/CBC/PKCS5Padding"
        const val DES_ECB_NO_PADDING = "DES/ECB/NoPadding"
        const val DES_ECB_PKCS5_PADDING = "DES/ECB/PKCS5Padding"

        const val DES_SUPPORTING_TEXT = "Token length should be 8 characters long."
        const val AES_SUPPORTING_TEXT = "Token length should be 16 characters long."
    }

    private var files = mutableListOf<File>()
    private var encryptionMethodMismatchFiles = mutableListOf<String>()

    private val ByteArray.asHexUpper: String
        inline get() {
            return this.joinToString(separator = "") {
                String.format("%02X", it)
            }
        }


    private val String.hexAsByteArray: ByteArray
        inline get() {
            return this.chunked(2).map {
                it.uppercase(Locale.US).toInt(16).toByte()
            }.toByteArray()
        }

    fun encrypt(type: String, path: List<String>, token: String): String {
        path.forEach {
            this.files.addAll(getFilesUnderPath(it))
        }

        val cipher = Cipher.getInstance(type)
        val keySpec = getKey(type, token)

        cipher.init(Cipher.ENCRYPT_MODE, keySpec)

        files.forEach {
            val f = it.readBytes()
            val encrypt = cipher.doFinal(f)
            it.writeText("$type:${encrypt.asHexUpper}")
        }

        return getMessages()
    }

    fun decrypt(type: String, path: List<String>, token: String): String {
        path.forEach {
            this.files.addAll(getFilesUnderPath(it))
        }

        val cipher = Cipher.getInstance(type)
        val keySpec = getKey(type, token)

        cipher.init(Cipher.DECRYPT_MODE, keySpec)

        files.forEach {
            var content = it.readText()

            if (!content.startsWith("$type:")) {
                this.encryptionMethodMismatchFiles.add(it.path)
                return@forEach
            }

            content = content.removePrefix("$type:")
            val decrypt = cipher.doFinal(content.hexAsByteArray)
            it.writeText(String(decrypt))
        }

        return getMessages()
    }

    private fun getKey(type: String, token: String): SecretKeySpec {
        return when (type) {
            DES -> SecretKeySpec(token.toByteArray(), DES)
            AES -> SecretKeySpec(token.toByteArray(), AES)
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
        val processedCount =
            this.files.count() - this.encryptionMethodMismatchFiles.count()
        var message = "$processedCount file(s) processed.\n"

        if (this.encryptionMethodMismatchFiles.isNotEmpty()) {
            message += "\nThese files cannot be decrypted because these files are not encrypted or the decryption method does not match the encryption method:\n"

            this.encryptionMethodMismatchFiles.forEach { message += it + "\n" }
        }

        return message
    }
}
