package cn.modoumama.common;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 错误信息封装对象，用于保存自定义异常信息
 * 
 * @author ouyangjian
 * 
 */
@XStreamAlias("errDetailInfo")
public class ErrDetailInfo implements Serializable {

	private static final long serialVersionUID = 4049522523279414519L;

	/** 错误编码 */
	private String errorCode;

	/** 错误描述 */
	private String errorDes;

	/** 主键信息 */
	private String pkInfo;

	public ErrDetailInfo() {
	}

	public ErrDetailInfo(String errorCode, String errorDes, String pkInfo) {
		this.errorCode = errorCode;
		this.errorDes = errorDes;
		this.pkInfo = pkInfo;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorDes() {
		return errorDes;
	}

	public void setErrorDes(String errorDes) {
		this.errorDes = errorDes;
	}

	public String getPkInfo() {
		return pkInfo;
	}

	public void setPkInfo(String pkInfo) {
		this.pkInfo = pkInfo;
	}

}
