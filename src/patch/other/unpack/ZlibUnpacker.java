package patch.other.unpack;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import tool.ZLibUtils;

class ZlibUnpacker {

	public static Thread getThread(File file, File target) {
		return new Thread() {
			@Override
			public void run() {
				String dir;
				if (target == null) dir = file.getParent() + "\\unpack\\" + file.getName();
				else dir = target.getAbsolutePath();

				if (file.isDirectory()) unpackDirectory(dir, file);
				else unpackFile(dir, file);
			}
		};
	}

	private static void unpackDirectory(String dir, File file) {
		for (File f : file.listFiles()) {
			if (f.isDirectory()) unpackDirectory(dir + "\\" + f.getName(), f);
			else unpackFile(dir + "\\" + f.getName(), f);
		}
	}

	private static void unpackFile(String filename, File file) {
		try {
			byte[] bytes = FileUtils.readFileToByteArray(file);
			bytes = ZLibUtils.decompress(bytes);

			String header = String.format("%d%d", Byte.toUnsignedInt(bytes[0]), Byte.toUnsignedInt(bytes[1]));
			String exten = getExten(header);
			File newFile = new File(filename + "." + exten);
			FileUtils.writeByteArrayToFile(newFile, bytes);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static String getExten(String header) {
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
}
