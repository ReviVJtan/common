package cn.modoumama.common;

import java.io.Serializable;

public class FileItem implements Serializable{

	/**
	  * @Fields serialVersionUID : 序列化
	  */
	
	private static final long serialVersionUID = 5312992139596283817L;

	private String fileName;

	private byte[] content;

	private String mimeType;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

}
