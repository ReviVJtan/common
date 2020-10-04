package cn.modoumama.common.request;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 请求对象request
 * 
 * @author ouyangjian
 */
public class Request implements Serializable {

	

	/**
	 * 
	 */
	private static final long serialVersionUID = 8029099338063271641L;

	/** 用户Id */
	private Long userId;

	/** 客户端IP */
	private String ip;
	
	/**用户类型*/
	private Integer appId;

	/** 语言设置默认1 （1：中文、2：英文、3：韩文） */
	private Integer language;
	
	/**appKey*/
	private String appKey;
	
	/**appUserTable*/
	private String appUserTable;

	/** 参数params */
	private Map<String, String> headerMap = new HashMap<String, String>();

	/**  当前页  */
	private Integer pageNum;
	
	/**  每页显示多少条  */
	private Integer pageSize;
	
	
	public Integer getPageNum() {
		return pageNum;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Integer getLanguage() {
		return language;
	}

	public void setLanguage(Integer language) {
		this.language = language;
	}

	public Map<String, String> getHeaderMap() {
		return headerMap;
	}

	public void setHeaderMap(Map<String, String> headerMap) {
		this.headerMap = headerMap;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Integer getAppId() {
		return appId;
	}

	public void setAppId(Integer appId) {
		this.appId = appId;
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public String getAppUserTable() {
		return appUserTable;
	}

	public void setAppUserTable(String appUserTable) {
		this.appUserTable = appUserTable;
	}
	
}
