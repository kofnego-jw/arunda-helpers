package at.ac.uibk.fiba.arunda.odb.api;

/**
 * Exception class for odb preparation
 * @author totoro
 *
 */
public class OdbException extends Exception {
	
	private static final long serialVersionUID = 201503091517L;
	
	public OdbException() {
		super();
	}
	
	public OdbException(String message) {
		super(message);
	}
	
	public OdbException(Throwable cause) {
		super(cause);
	}
	
	public OdbException(String message, Throwable cause) {
		super(message, cause);
	}

}
