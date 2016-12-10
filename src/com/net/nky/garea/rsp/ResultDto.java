package com.net.nky.garea.rsp;

import java.io.Serializable;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 查询接口封装类
 * @author yangling
 */
public class ResultDto implements Serializable {
	
	private static final long serialVersionUID = 626205010898309857L;
	
	private boolean success;
	private IError error;
	
	public ResultDto() {
		this(true);
	}

	public ResultDto(boolean success) {
		this.success = success;
	}
	
	public ResultDto(boolean success, IError error) {
		this.success = success;
		this.error = error;
	}

	/**
	 * @return the success
	 */
	public boolean isSuccess() {
		return success;
	}

	/**
	 * @param success the success to set
	 */
	public void setSuccess(boolean success) {
		this.success = success;
	}

	/**
	 * @deprecated
	 * @param errorCode the errorCode to set
	 * 
	 */
	public void setErrorCode(IError error) {
		setError(error);
	}

	/**
	 * 
	 * @param error
	 */
	public void setError(IError error) {
		this.error = error;
	}
	
	public void setError(IError error,  boolean successFlag){
		this.error = error;
		this.success = successFlag;
	}
	/**
	 * @return the errorCode
	 */
	public String getErrorCode() {
		if(this.error == null) {
			return "";
		}
		return error.getCode();
	}
	
	/**
	 * @return
	 * @see com.garea.cloud.exception.IError#getDesc()
	 */
	public String getErrorDesc() {
		if(this.error == null) {
			return "";
		}
		return error.getDesc();
	}

	/**
	 * 
	 * @return
	 */
	public String getErrorExtDesc() {
		if(this.error == null) {
			return "";
		}
		return error.getExtDesc();
	}

	public IError getError() {
		return error;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}	
}
