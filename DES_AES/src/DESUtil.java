import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.*;


public class DESUtil implements Cryptography{
	
	private SecretKey secretKey;
	private Cipher cipher;
	private Base64Coder base64Coder;
	private KeyGenerator keyGenerator;
	
	public DESUtil() {
		init();
	}
	
	private void init() {
		base64Coder = new Base64Coder();
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

}
