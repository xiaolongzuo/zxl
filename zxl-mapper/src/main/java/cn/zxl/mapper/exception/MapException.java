package cn.zxl.mapper.exception;

public class MapException extends RuntimeException {

	private static final long serialVersionUID = 2950815439443779192L;

	public MapException() {
		super();
	}

	public MapException(String message, Throwable cause) {
		super(message, cause);
	}

	public MapException(String message) {
		super(message);
	}

	public MapException(Throwable cause) {
		super(cause);
	}

}
