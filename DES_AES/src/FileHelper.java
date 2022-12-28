import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.InvalidKeyException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;

public class FileHelper {
	public void encryptFile(String in, String out, Cipher cipher) {
		byte[] buffer = new byte[1024];
		try {
			FileInputStream inputStream = new FileInputStream(in);
			OutputStream outputStream = new FileOutputStream(out);
			CipherInputStream cin = new CipherInputStream(inputStream, cipher);
			
			int i;
			while((i = cin.read(buffer)) != -1) {
				outputStream.write(buffer, 0 , i);
			}
			outputStream.close();
			cin.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void decryptFile(String in, String out, Cipher cipher) {
		byte[] buffer = new byte[1024];
		try {
			FileInputStream inputStream = new FileInputStream(in);
			OutputStream outputStream = new FileOutputStream(out);
			
			CipherOutputStream cout = new CipherOutputStream(outputStream, cipher);
			int i;
			while((i=inputStream.read(buffer)) != -1) {
				cout.write(buffer ,0 , i);
			}
			cout.close();
			inputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
}
