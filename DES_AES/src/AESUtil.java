import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

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
	private Cipher encryptionCipher;
	private Cipher decryptionCipher;
	private Base64Coder base64Coder;
	private FileHelper fileHelper;
	
	public AESUtil() {
		init();
	}
	
	private void init() {
		
		base64Coder = new Base64Coder();
		fileHelper = new FileHelper();
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
	public void encrypt(String inputFile, String outputFile) {
		// TODO Auto-generated method stub
		try {
			encryptionCipher = Cipher.getInstance("AES/GCM/NoPadding");
			encryptionCipher.init(Cipher.ENCRYPT_MODE, secretKey);
			fileHelper.encryptFile(inputFile, outputFile, encryptionCipher);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void decrtpt(String inputFile, String outputFile) {
		// TODO Auto-generated method stub
		try {
			decryptionCipher = Cipher.getInstance("AES/GCM/NoPadding");
			GCMParameterSpec spec = new GCMParameterSpec(DATA_LENGTH, encryptionCipher.getIV());
			decryptionCipher.init(Cipher.DECRYPT_MODE, secretKey, spec);
			fileHelper.decryptFile(inputFile, outputFile, decryptionCipher);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
}