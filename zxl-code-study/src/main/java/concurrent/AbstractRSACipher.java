package concurrent;

import javax.crypto.Cipher;

public abstract class AbstractRSACipher {

	protected static final String KEY_ALGORITHM = "RSA";

	protected static final String SIGNATURE_ALGORITHM = "MD5withRSA";

	private int maxEncryptLength;

	private int maxDecryptLength;

	public AbstractRSACipher(int keySize) {
		maxDecryptLength = keySize / 8;
		maxEncryptLength = maxDecryptLength - 11;
	}

	public byte[] decrypt(byte[] data) throws Exception {
		Cipher cipher = initCipher(Cipher.DECRYPT_MODE);
		return segmented(data, cipher, maxDecryptLength);
	}

	public byte[] encrypt(byte[] data) throws Exception {
		Cipher cipher = initCipher(Cipher.ENCRYPT_MODE);
		return segmented(data, cipher, maxEncryptLength);
	}

	protected abstract Cipher initCipher(int mode) throws Exception;

	protected byte[] segmented(byte[] data, Cipher cipher, int maxLength) throws Exception {
		int times = (data.length % maxLength == 0) ? (data.length / maxLength) : (data.length / maxLength + 1);
		byte[] bytes = new byte[0];
		byte[] temp = null;
		for (int i = 0; i < times; i++) {
			byte[] decrypted = cipher.doFinal(data, i * maxLength, ((i == times - 1) ? (data.length - i * maxLength) : (maxLength)));
			int oldLength = bytes.length;
			temp = new byte[oldLength];
			System.arraycopy(bytes, 0, temp, 0, oldLength);
			bytes = new byte[oldLength + decrypted.length];
			System.arraycopy(temp, 0, bytes, 0, temp.length);
			System.arraycopy(decrypted, 0, bytes, oldLength, decrypted.length);
		}
		return bytes;
	}

}
