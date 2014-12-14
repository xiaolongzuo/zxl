package cn.zxl.common.security;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public abstract class DES {

	public static final String KEY_DES = "DES";

	public static String generateRandomPrivateKey() {
		try {
			KeyGenerator keyGenerator = KeyGenerator.getInstance(KEY_DES);
			SecretKey key = keyGenerator.generateKey();
			return BASE64.encrypt(key.getEncoded());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static byte[] encrypt(byte[] data, String privateKey) {
		try {
			SecureRandom random = new SecureRandom();
			DESKeySpec desKey = new DESKeySpec(BASE64.decrypt(privateKey));
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(KEY_DES);
			SecretKey securekey = keyFactory.generateSecret(desKey);
			Cipher cipher = Cipher.getInstance(KEY_DES);
			cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
			return cipher.doFinal(data);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static byte[] decrypt(byte[] data, String privateKey) {
		try {
			SecureRandom random = new SecureRandom();
			DESKeySpec desKey = new DESKeySpec(BASE64.decrypt(privateKey));
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(KEY_DES);
			SecretKey securekey = keyFactory.generateSecret(desKey);
			Cipher cipher = Cipher.getInstance(KEY_DES);
			cipher.init(Cipher.DECRYPT_MODE, securekey, random);
			return cipher.doFinal(data);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
