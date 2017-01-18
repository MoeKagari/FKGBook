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
		String header = "";
		for (int i = 0; i < 2; i++) {
			if (bytes[i] < 0) header += (bytes[i] + 256);
			else header += bytes[i];
		}
		return header;
	}

	public static String getExten(String header) {
		String exten = "";
		switch (header) {
			case "13780":
				exten = "png";
				break;
			case "6787":
				exten = "swf";
				break;
			case "7368":
				exten = "mp3";
				break;
			case "255216":
				exten = "jpg";
				break;
			default:
				exten = "txt";
		}
		return exten;
	}

	public static File create(String filename) throws IOException {
		File file = new File(filename);

		create(file);

		return file;
	}

	public static void create(File file) throws IOException {
		file = file.getAbsoluteFile();
		File parent = file.getParentFile();
		if (!parent.exists()) {
			parent.mkdirs();
		}

		if (!file.exists() || file.isDirectory()) {
			file.createNewFile();
		}
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
		file = file.getAbsoluteFile();
		try {
			create(file);
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
