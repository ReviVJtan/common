package cn.modoumama.common.response;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import cn.modoumama.common.ErrDetailInfo;

/**
 * 响应返回对象
 * 
 * @author ouyangjian
 * 
 */
@XStreamAlias("response")
public class Response implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6030910386334021804L;

	/*** 成功记录数 */
	private Integer successCount = 1;

	/** 错误数 */
	private Integer errorCount = 0;

	/** 错误信息列表 */
	private List<ErrDetailInfo> errInfoList = null;

	/** 异常错误信息 */
	private String exception = null;

	/**
	 * 累积添加错误信息，同时增长errorCount值
	 * 
	 * @param response
	 *            返回对象
	 * @param errorCode
	 *            错误编码
	 * @param errorDes
	 *            错误描述
	 * @param pkInfo
	 *            关键词
	 */
	public void addErrInfo(String errorCode, String errorDes, String pkInfo) {

		if (errInfoList == null) {
			errInfoList = new ArrayList<ErrDetailInfo>();
		}

		ErrDetailInfo detailInfo = new ErrDetailInfo(errorCode, errorDes,
				pkInfo);
		errInfoList.add(detailInfo);

		// 设置错误个数
		errorCount = errInfoList.size();
		
		successCount = 0;
	}

	public void setException(Exception e) {
		try {
			if (e != null) {
				StringWriter sw = new StringWriter();
				e.printStackTrace(new PrintWriter(sw, true));
				String str = sw.toString();
				exception = str;
			}
		} catch (Exception ex) {
		}
	}

	public Integer getSuccessCount() {
		return successCount;
	}

	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}

	public void setSuccessCount(Integer successCount) {
		this.successCount = successCount;
	}

	public Integer getErrorCount() {
		return errorCount;
	}

	public void setErrorCount(Integer errorCount) {
		this.errorCount = errorCount;
	}

	public List<ErrDetailInfo> getErrInfoList() {
		return errInfoList;
	}

	public void setErrInfoList(List<ErrDetailInfo> errInfoList) {
		this.errInfoList = errInfoList;
	}

}
