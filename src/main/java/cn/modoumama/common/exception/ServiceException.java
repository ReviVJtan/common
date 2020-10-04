package cn.modoumama.common.exception;

import cn.modoumama.common.spring.MessageUtils;

public class ServiceException  extends BaseException {
	private static final long serialVersionUID = 1L;


	public ServiceException(String code, Throwable cause) {
		super(MessageUtils.getMessage(code),cause);
		this.code = code;
	}

	public ServiceException(String code) {
		super(MessageUtils.getMessage(code));
		this.code = code;
	}

	public ServiceException(Throwable cause) {
		super(cause);
	}


}
