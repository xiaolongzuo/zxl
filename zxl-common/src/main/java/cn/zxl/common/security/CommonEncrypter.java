package cn.zxl.common.security;

import java.security.MessageDigest;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public abstract class CommonEncrypter {

	private static final String CHARSET = "UTF-8";

	private static final String KEY_MAC = "HmacMD5";

	private static final String KEY_MD5 = "MD5";

	private static final String KEY_SHA = "SHA";

	public static String initMacKey() throws Exception {
		KeyGenerator keyGenerator = KeyGenerator.getInstance(KEY_MAC);
		SecretKey secretKey = keyGenerator.generateKey();
		return BASE64.encrypt(secretKey.getEncoded());
	}

	public static byte[] encryptHMAC(byte[] data, String key) throws Exception {
		SecretKey secretKey = new SecretKeySpec(BASE64.decrypt(key), KEY_MAC);
		Mac mac = Mac.getInstance(secretKey.getAlgorithm());
		mac.init(secretKey);
		return mac.doFinal(data);
	}

	public static String encryptMD5(String data) {
		MessageDigest md5;
		try {
			md5 = MessageDigest.getInstance(KEY_MD5);
			md5.update(data.getBytes(CHARSET));
			return new String(md5.digest(), CHARSET);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static byte[] encryptSHA(byte[] data) throws Exception {
		MessageDigest sha = MessageDigest.getInstance(KEY_SHA);
		sha.update(data);
		return sha.digest();
	}

}
