
public interface Cryptography {
	public String encrypt(String plain);
	public String decrtpt(String content);
	
	public void encrypt(String inputFile, String outputFile);
	public void decrtpt(String inputFile, String outputFile);
}
