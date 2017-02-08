package patch.other.unpack;

import java.io.File;
import java.io.IOException;

import tool.FileUtil;
import tool.ZLibUtils;

class ZlibUnpacker {
	/**
	 * 
	 * @param file
	 *            the file or directory need to decompress
	 * @param target
	 *            the target directory,if null,decompress to ..\\unpack
	 * @return the thread going to decompress file,please start thread
	 */
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
	/*----------------------------------*/

	private static void unpackDirectory(String dir, File file) {
		for (File f : file.listFiles()) {
			if (f.isDirectory()) unpackDirectory(dir + "\\" + f.getName(), f);
			else unpackFile(dir + "\\" + f.getName(), f);
		}
	}

	private static void unpackFile(String filename, File file) {
		byte[] bytes = FileUtil.read(file);
		bytes = ZLibUtils.decompress(bytes);
		if (bytes == null || bytes.length == 0) {
			System.out.println("bytes == null");
			return;
		}

		String header = FileUtil.getHeader(bytes);
		String exten = FileUtil.getExten(header);
		File newFile = new File(filename + "." + exten);

		try {
			FileUtil.create(newFile);
			FileUtil.save(newFile, bytes);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
