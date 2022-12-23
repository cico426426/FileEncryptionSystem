
public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String key = "12345678";
		Cryptography desCryptography = new DESUtil(key);
		String encrypt = desCryptography.encrypt("¤j®a¦n");
		System.out.println(encrypt);
		String decrtptString = desCryptography.decrtpt(encrypt);
		System.out.println(decrtptString);
		
		Cryptography aesCryptography = new AESUtil();
		String aesEncrypt = aesCryptography.encrypt("Hi i addadw");
		System.out.println(aesEncrypt);
		String aesDecryptString = aesCryptography.decrtpt(aesEncrypt);
		System.out.println(aesCryptography);
	
	}

}
