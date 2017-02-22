package tool;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class HTTPUtil {
	public static byte[] getBody(byte[] bytes, boolean chunked) {
		int count = -1;
		for (int i = 0; i < bytes.length; i++) {
			if (bytes[i] != '\r') continue;
			i++;
			if (bytes[i] != '\n') continue;
			i++;
			if (bytes[i] != '\r') continue;
			i++;
			if (bytes[i] != '\n') continue;
			i++;

			count = i;
			break;
		}

		if (count == -1) return null;
		if (chunked == false) {
			try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
				while (count < bytes.length) {
					baos.write(bytes[count] & 0xff);
					count++;
				}
				return baos.toByteArray();
			} catch (IOException e) {
				return null;
			}
		}

		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			while (count < bytes.length) {
				String str = "";
				while (!Character.isWhitespace(bytes[count])) {
					str += (char) bytes[count];
					count++;
				}

				int len = Integer.valueOf(str, 16);

				count += 2;

				for (int i = 0; i < len; i++) {
					baos.write(bytes[count] & 0xff);
					count++;
				}

				count += 2;
			}
			return baos.toByteArray();
		} catch (IOException e) {
			return null;
		}
	}

	public static byte[] getHeader(byte[] bytes) {
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			for (int i = 0; i < bytes.length; i++) {
				baos.write(bytes[i] & 0xff);
				if (bytes[i] != '\r') continue;

				i++;
				baos.write(bytes[i] & 0xff);
				if (bytes[i] != '\n') continue;

				i++;
				baos.write(bytes[i] & 0xff);
				if (bytes[i] != '\r') continue;

				i++;
				baos.write(bytes[i] & 0xff);
				if (bytes[i] != '\n') continue;

				break;
			}

			return baos.toByteArray();
		} catch (IOException e) {
			return null;
		}
	}

}
