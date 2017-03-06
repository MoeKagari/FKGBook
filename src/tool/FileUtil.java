package tool;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileUtil {

	public static String getHeader(byte[] bytes) {
		return Byte.toUnsignedInt(bytes[0]) + "" + Byte.toUnsignedInt(bytes[1]);
	}

	public static String getExten(String header) {
		switch (header) {
			case "13780":
				return "png";
			case "6787":
				return "swf";
			case "7368":
				return "mp3";
			case "255216":
				return "jpg";
			default:
				return "txt";
		}
	}

	public static File create(String filename) throws IOException {
		return create(new File(filename));
	}

	public static File create(File file) throws IOException {
		file = file.getAbsoluteFile();
		File parent = file.getParentFile();
		if (!parent.exists()) parent.mkdirs();
		if (!file.exists()) file.createNewFile();

		return file;
	}

	public static byte[] read(InputStream is) {
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			int len;
			byte[] buffer = new byte[1024];
			while ((len = is.read(buffer)) != -1) {
				baos.write(buffer, 0, len);
			}
			return baos.toByteArray();
		} catch (IOException e) {
			return null;
		}
	}

	public static byte[] read(String filename) {
		try {
			return read(new FileInputStream(filename));
		} catch (FileNotFoundException e) {
			return null;
		}
	}

	public static byte[] read(File file) {
		try {
			return read(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			return null;
		}
	}

	public static void save(String filename, byte[] bytes) {
		save(new File(filename), bytes);
	}

	public static void save(File file, byte[] bytes) {
		try {
			file = create(file);
		} catch (IOException e) {
			e.printStackTrace();
		}

		try (FileOutputStream fos = new FileOutputStream(file)) {
			fos.write(bytes);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
