package tool;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
	public static String getMD5(String sourceStr) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(sourceStr.getBytes());
			byte bytes[] = md5.digest();
			StringBuilder buf = new StringBuilder("");
			for (int i = 0; i < bytes.length; i++) {
				int b = bytes[i];
				if (b < 0) b += 256;
				if (b < 16) buf.append("0");
				buf.append(Integer.toHexString(b));
			}
			return buf.toString();
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}
}
