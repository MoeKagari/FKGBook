package tool;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class GameUtils {
	private static final String pass = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";

	private static int byteToInteger(byte b) {
		return b < 0 ? (b + 128) : b;
	}

	//字符序列  ->(indexOf)  长度4分组  ->  变换  ->  组合
	public static byte[] decompress(byte[] source) {
		int len = 4;
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			for (int index = 0; index < source.length; index += len) {
				int[] a = new int[len];
				int i = 0;
				while (i < len && index + i < source.length) {
					a[i] = pass.indexOf(byteToInteger(source[index + i]));
					i++;
				}

				int[] b = decompress(a);
				int j = 0;
				while (j < b.length) {
					if (a[j + 1] == 64) break;
					baos.write(b[j]);
					j++;
				}
			}
			return baos.toByteArray();
		} catch (IOException e) {
			return null;
		}
	}

	private static int[] decompress(int[] a) {
		int[] b = new int[3];

		b[0] = (a[0] << 2) + ((a[1] & 48) >> 4);
		b[1] = ((a[1] & 15) << 4) + ((a[2] & 60) >> 2);
		b[2] = ((a[2] & 3) << 6) + a[3];

		return b;
	}

	private static int[] compress(int[] b) {
		int[] a = new int[4];

		a[0] = (b[0] & 252) >> 2;
		a[1] = ((b[0] & 3) << 4) | (b[1] >> 4);
		a[2] = ((b[1] & 15) << 2) | (b[2] >> 6);
		a[3] = b[2] & 63;

		return a;
	}

	//数据序列  ->  长度3分组  ->  变换  ->(charAt)  组合
	public static byte[] compress(byte[] source) {
		int len = 3;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		for (int index = 0; index < source.length; index += len) {
			int[] a = new int[len];
			int i = 0;
			while (i < len && index + i < source.length) {
				a[i] = byteToInteger(source[index + i]);
				i++;
			}

			int b[] = compress(a);
			switch (i) {
				case 0:
					b[1] = 64;
				case 1:
					b[2] = 64;
				case 2:
					b[3] = 64;
				case 3:
			}
			for (int oneint : b) {
				baos.write(pass.charAt(oneint));
			}
		}
		return baos.toByteArray();
	}

	public static byte[] getBody(byte[] bytes, boolean chunked) {
		int count = -1;
		for (int i = 0; i <= bytes.length - 4;) {
			if (bytes[i] != '\r') {
				i++;
				continue;
			}

			i++;
			if (bytes[i] != '\n') continue;
			i++;
			if (bytes[i] != '\r') continue;
			i++;
			if (bytes[i] != '\n') continue;

			count = i + 1;
			break;
		}

		if (count == -1) return null;

		if (chunked == false) return Arrays.copyOfRange(bytes, count, bytes.length - 1);

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

}
