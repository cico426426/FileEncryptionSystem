import java.util.Base64;

public class Base64Coder {
	public String encode(byte[] data) {
		return Base64.getEncoder().encodeToString(data);
	}
	public byte[] decode(String data) {
		return Base64.getDecoder().decode(data);
	}
}
