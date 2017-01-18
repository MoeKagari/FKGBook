package tool;

import java.io.ByteArrayOutputStream;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class ZLibUtils {

	public static byte[] compress(byte[] data) {
		try (ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length)) {
			Deflater compresser = new Deflater();
			compresser.reset();
			compresser.setInput(data);
			compresser.finish();

			byte[] buf = new byte[1024];
			while (!compresser.finished()) {
				int i = compresser.deflate(buf);
				bos.write(buf, 0, i);
			}
			compresser.end();

			return bos.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
			return data;
		}
	}

	public static byte[] decompress(byte[] data) {
		try (ByteArrayOutputStream o = new ByteArrayOutputStream(data.length)) {
			Inflater decompresser = new Inflater();
			decompresser.reset();
			decompresser.setInput(data);

			byte[] buf = new byte[1024];
			while (!decompresser.finished()) {
				int i = decompresser.inflate(buf);
				o.write(buf, 0, i);
			}
			decompresser.end();

			return o.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
			return data;
		}
	}

}
