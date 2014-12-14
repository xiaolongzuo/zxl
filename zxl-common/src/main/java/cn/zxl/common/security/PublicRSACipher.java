package cn.zxl.common.security;

import java.security.Key;
import java.security.KeyFactory;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

public class PublicRSACipher extends AbstractRSACipher {

	private byte[] publicKeyBytes;

	public PublicRSACipher(String publicKey, int keySize) throws Exception {
		super(keySize);
		this.publicKeyBytes = BASE64.decrypt(publicKey);
	}

	@Override
	protected Cipher initCipher(int mode) throws Exception {
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(publicKeyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key publicKey = keyFactory.generatePublic(x509KeySpec);

		Cipher decryptCipher = Cipher.getInstance(keyFactory.getAlgorithm());
		decryptCipher.init(mode, publicKey);
		return decryptCipher;
	}

}