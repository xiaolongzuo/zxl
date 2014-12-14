package cn.zxl.common.security;

import java.security.Key;
import java.security.KeyFactory;
import java.security.spec.PKCS8EncodedKeySpec;

import javax.crypto.Cipher;

public class PrivateRSACipher extends AbstractRSACipher {

	private byte[] privateKeyBytes;

	public PrivateRSACipher(String privateKey, int keySize) throws Exception {
		super(keySize);
		this.privateKeyBytes = BASE64.decrypt(privateKey);
	}

	@Override
	protected Cipher initCipher(int mode) throws Exception {
		PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key key = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
		Cipher decryptCipher = Cipher.getInstance(keyFactory.getAlgorithm());
		decryptCipher.init(mode, key);
		return decryptCipher;
	}

}