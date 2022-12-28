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
		desCryptography.encrypt("C:\\Users\\user\\Desktop\\網路安全\\FileEncryptionSystem\\DES_AES\\src\\1.txt", "C:/Users/user/Desktop/test/2.txt");
		desCryptography.decrtpt("C:/Users/user/Desktop/test/2.txt", "C:/Users/user/Desktop/test/3.txt");
		
		Cryptography aesCryptography = new AESUtil();
		aesCryptography.encrypt("C:\\Users\\user\\Desktop\\網路安全\\FileEncryptionSystem\\DES_AES\\src\\1.txt", "C:/Users/user/Desktop/test/4.txt");
		aesCryptography.decrtpt("C:/Users/user/Desktop/test/4.txt", "C:/Users/user/Desktop/test/5.txt");
	}

}
