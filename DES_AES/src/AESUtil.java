import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

public class AESUtil implements Cryptography{
	
	private final int KEY_SIZE = 128;
	private final int DATA_LENGTH = 128;
	private SecretKey secretKey;
	private KeyGenerator keyGenerator;
	private SecureRandom random;
	private Cipher encryptionCipher;
	private Cipher decryptionCipher;
	private Base64Coder base64Coder;
	
	public AESUtil() {
		init();
	}
	
	private void init() {
		
		base64Coder = new Base64Coder();
		
		try {
			keyGenerator = KeyGenerator.getInstance("AES");
			keyGenerator.init(KEY_SIZE);
			secretKey = keyGenerator.generateKey();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public String encrypt(String plain) {
		// TODO Auto-generated method stub
		try {
			encryptionCipher = Cipher.getInstance("AES/GCM/NoPadding");
			encryptionCipher.init(Cipher.ENCRYPT_MODE, secretKey);
			byte[] encryptedBytes = encryptionCipher.doFinal(plain.getBytes());
			return base64Coder.encode(encryptedBytes);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String decrtpt(String content) {
		// TODO Auto-generated method stub
		try {
			decryptionCipher = Cipher.getInstance("AES/GCM/NoPadding");
			GCMParameterSpec spec = new GCMParameterSpec(DATA_LENGTH, encryptionCipher.getIV());
			decryptionCipher.init(Cipher.DECRYPT_MODE, secretKey, spec);
			byte[] decrptedBytes = decryptionCipher.doFinal(base64Coder.decode(content));
			return new String(decrptedBytes);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
