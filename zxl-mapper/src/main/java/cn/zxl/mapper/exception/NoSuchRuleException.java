package cn.zxl.mapper.exception;

public class NoSuchRuleException extends RuntimeException {

	private static final long serialVersionUID = -5151219139575320048L;

	public NoSuchRuleException() {
		super();
	}

	public NoSuchRuleException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoSuchRuleException(String message) {
		super(message);
	}

	public NoSuchRuleException(Throwable cause) {
		super(cause);
	}

}
