package utils;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
public class HmacSHA256
{
	public static void main(String[] args)
	{
			String secret = "7ad3773142a6692b25b8"; // "secret";
			String message = "1234.1234:private-foobar"; // "Message";
			getHmacSHA256HexDigest(secret, message);
			
	}
	/**
	 * Calculates a HMAC SHA256 hex digest.
	 * @param secret
	 * @param message
	 * @return
	 */
	public static String getHmacSHA256HexDigest(String secret, String message)
	{	
		StringBuffer hash = new StringBuffer();
		
		try
		{
			Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
	
			SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
			sha256_HMAC.init(secret_key);
			byte[] bytes = sha256_HMAC.doFinal(message.getBytes());
			
			for (int i = 0; i < bytes.length; i++) 
			{	String hex = Integer.toHexString(0xFF & bytes[i]);
				if (hex.length() == 1) 
				{	hash.append('0');
				}
				hash.append(hex);
			}			
		}
		catch (InvalidKeyException e) 
		{			
		} 
		catch (NoSuchAlgorithmException e)
		{			
		}
		
		return hash.toString();
	}
}
