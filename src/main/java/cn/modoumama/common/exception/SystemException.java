package cn.modoumama.common.exception;

public class SystemException extends BaseException {
	private static final long serialVersionUID = 3787730660315875183L;

	

	public SystemException(Throwable exception) {
		super(exception);
	}

	public SystemException(String message) {
		super("SYSTEM.ERROR",message);
	}
	
	public SystemException(String message, Throwable exception) {
		super(message, exception);
	}
	
	public SystemException(String code, String message, Throwable exception) {
		super(code, message, exception);
	}

}