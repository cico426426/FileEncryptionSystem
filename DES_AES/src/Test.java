import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class Test {

	public static void main(String[] args) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException {
		// TODO Auto-generated method stub
		Cryptography desCryptography = new DESUtil();
		String encrypt = desCryptography.encrypt("大家好");
		System.out.println(encrypt);
		String decrtptString = desCryptography.decrtpt(encrypt);
		System.out.println(decrtptString);
		
		Cryptography aesCryptography = new AESUtil();
		String aesEncrypt = aesCryptography.encrypt("Hi i addadw");
		System.out.println(aesEncrypt);
		String aesDecryptString = aesCryptography.decrtpt(aesEncrypt);
		System.out.println(aesDecryptString);
	}

}
