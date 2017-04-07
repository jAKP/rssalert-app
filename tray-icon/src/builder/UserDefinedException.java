package builder;

public class UserDefinedException extends RuntimeException {
	private static final long serialVersionUID = 13216733298798717L;
	private final String userId;

	public UserDefinedException(final String message, final String userId) {
		super(message);
		this.userId = userId;
	}

	public UserDefinedException(final String message, final Throwable error, final String userId) {
		super(message, error);
		this.userId = userId;
	}

	public UserDefinedException(final Throwable e, final String userId) {
		super(e);
		this.userId = userId;
	}

	public String getUserId() {
		return userId;
	}
}