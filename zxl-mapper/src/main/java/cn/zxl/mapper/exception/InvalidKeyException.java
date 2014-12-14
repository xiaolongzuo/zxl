package cn.zxl.mapper.exception;

public class InvalidKeyException extends RuntimeException {

	private static final long serialVersionUID = -6349311857466052163L;

	public InvalidKeyException() {
		super();
	}

	public InvalidKeyException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidKeyException(String message) {
		super(message);
	}

	public InvalidKeyException(Throwable cause) {
		super(cause);
	}

}
