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
		desCryptography.encrypt("C:\\Users\\user\\Desktop\\網路安全\\FileEncryptionSystem\\DES_AES\\src\\1.txt", "C:/Users/user/Desktop/test/2.txt");
		desCryptography.decrtpt("C:/Users/user/Desktop/test/2.txt", "C:/Users/user/Desktop/test/3.txt");
		
		Cryptography aesCryptography = new AESUtil();
		String aesEncrypt = aesCryptography.encrypt("Hi i addadw");
		System.out.println(aesEncrypt);
		String aesDecryptString = aesCryptography.decrtpt(aesEncrypt);
		System.out.println(aesDecryptString);
		aesCryptography.encrypt("C:\\Users\\user\\Desktop\\網路安全\\FileEncryptionSystem\\DES_AES\\src\\1.txt", "C:/Users/user/Desktop/test/4.txt");
		aesCryptography.decrtpt("C:/Users/user/Desktop/test/4.txt", "C:/Users/user/Desktop/test/5.txt");
	}

}
