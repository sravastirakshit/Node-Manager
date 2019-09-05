package dk.nodemanager.exception;

public class NodeManagerException extends Exception {

	private String message;

	public NodeManagerException(String message) {
		super(message);
		this.message = message;
	}

	public NodeManagerException(Throwable th) {
		super(th);
		this.message = th.getMessage();
	}

	public NodeManagerException(String message, Throwable throwable) {
		super(message, throwable);
		this.message = message;
	}

	public String getMessage() {
		return this.message;
	}
}
