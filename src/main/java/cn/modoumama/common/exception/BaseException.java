package cn.modoumama.common.exception;

import java.io.Serializable;

public abstract class BaseException extends RuntimeException implements Serializable {
    private static final long serialVersionUID = 1311416870635146320L;
    protected Exception exception;
    protected String code;
    
    public BaseException() {
        super();
    }

    public BaseException(String message) {
        super(message);
    }

    public BaseException(String code, String message) {
		super(message);
		this.code = code;
	}
    
    public BaseException(Throwable cause) {
        super(cause);
    }
    
    public BaseException(String message,Throwable cause) {
        super(message,cause);
    }
    
    public BaseException(String code, String message, Throwable cause) {
		super(message,cause);
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Exception getException() {
		return exception;
	}

	public void setException(Exception exception) {
		this.exception = exception;
	}
}