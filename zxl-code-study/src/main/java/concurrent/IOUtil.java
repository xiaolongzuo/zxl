package concurrent;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

public abstract class IOUtil {

	private static final String CHARSET = "utf-8";

	public static byte[] readBytes(InputStream inputStream, int bufSize) throws IOException {
		byte[] buf = new byte[bufSize];
		byte[] data = new byte[0];
		int length = 0;
		while ((length = inputStream.read(buf)) > 0) {
			int oldLength = data.length;
			byte[] temp = new byte[oldLength];
			System.arraycopy(data, 0, temp, 0, oldLength);
			data = new byte[length + oldLength];
			System.arraycopy(temp, 0, data, 0, oldLength);
			System.arraycopy(buf, 0, data, oldLength, length);
		}
		return data;
	}

	public static String readString(InputStream inputStream, int bufSize) throws IOException {
		return new String(readBytes(inputStream, bufSize), CHARSET);
	}

	public static void write(OutputStream outputStream, String message) throws UnsupportedEncodingException, IOException {
		outputStream.write(message.getBytes(CHARSET));
		outputStream.flush();
	}

}
