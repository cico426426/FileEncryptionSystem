import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.*;
import javax.imageio.stream.FileCacheImageInputStream;


public class DESUtil implements Cryptography{
	
	private SecretKey secretKey;
	private Cipher cipher;
	private Base64Coder base64Coder;
	private KeyGenerator keyGenerator;
	private FileHelper fileHelper;
	
	public DESUtil() {
		init();
	}
	
	private void init() {
		base64Coder = new Base64Coder();
		fileHelper = new FileHelper();
		try {
			keyGenerator = KeyGenerator.getInstance("DES");
			secretKey = keyGenerator.generateKey();
			cipher = Cipher.getInstance("DES");
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	@Override
	public String encrypt(String plain) {
		// TODO Auto-generated method stub
		try {
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			byte[] result = cipher.doFinal(plain.getBytes());
			return base64Coder.encode(result);
		} catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String decrtpt(String content) {
		// TODO Auto-generated method stub
		try {
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			byte[] result = cipher.doFinal(base64Coder.decode(content));
			return new String(result);
		} catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void encrypt(String inputFile, String outputFile) {
		// TODO Auto-generated method stub
		try {
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			fileHelper.encryptFile(inputFile, outputFile, cipher);
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void decrtpt(String inputFile, String outputFile) {
		// TODO Auto-generated method stub
		try {
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			fileHelper.decryptFile(inputFile, outputFile, cipher);
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
}
