package cn.zxl.mapper.exception;

public class NoSuchMapException extends RuntimeException {

	private static final long serialVersionUID = -5477607497118422741L;

	public NoSuchMapException() {
		super();
	}

	public NoSuchMapException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoSuchMapException(String message) {
		super(message);
	}

	public NoSuchMapException(Throwable cause) {
		super(cause);
	}

}
