package star16m.utils.toapp.commons.errors;

public class ToAppException extends Exception {

	private static final long serialVersionUID = -1657495417084987754L;

	public ToAppException() {
		super();
	}

	public ToAppException(String message, Throwable throwable, boolean arg2, boolean arg3) {
		super(message, throwable, arg2, arg3);
	}

	public ToAppException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public ToAppException(String message) {
		super(message);
	}

	public ToAppException(Throwable throwable) {
		super(throwable);
	}

}
