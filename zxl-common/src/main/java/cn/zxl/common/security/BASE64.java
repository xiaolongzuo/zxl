package cn.zxl.common.security;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

@SuppressWarnings("restriction")
public abstract class BASE64 {

	public static byte[] decrypt(String data) throws Exception {
		return (new BASE64Decoder()).decodeBuffer(data);
	}

	public static String encrypt(byte[] data) throws Exception {
		return (new BASE64Encoder()).encodeBuffer(data);
	}

}
