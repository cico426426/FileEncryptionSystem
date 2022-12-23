import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;


public class DESUtil implements Cryptography{
	
	private String key;
	private SecureRandom random;
	private SecretKey secretKey;
	private Cipher cipher;
	private Base64Coder base64Coder;
	
	public DESUtil(String key) {
		this.key = key;
		init();
	}
	
	private void init() {
		base64Coder = new Base64Coder();
		random = new SecureRandom();
		try {
			DESKeySpec desKeySpec = new DESKeySpec(key.getBytes());
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			secretKey = keyFactory.generateSecret(desKeySpec);
			cipher = Cipher.getInstance("DES");
		} catch (InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public String encrypt(String plain) {
		// TODO Auto-generated method stub
		try {
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, random);
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
			cipher.init(Cipher.DECRYPT_MODE, secretKey, random);
			byte[] result = cipher.doFinal(base64Coder.decode(content));
			return new String(result);
		} catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
