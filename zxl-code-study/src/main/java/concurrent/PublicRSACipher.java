package concurrent;

import java.security.Key;
import java.security.KeyFactory;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import sun.misc.BASE64Decoder;

public class PublicRSACipher extends AbstractRSACipher {

	private byte[] publicKeyBytes;

	public PublicRSACipher(String publicKey, int keySize) throws Exception {
		super(keySize);
		this.publicKeyBytes = new BASE64Decoder().decodeBuffer(publicKey);
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