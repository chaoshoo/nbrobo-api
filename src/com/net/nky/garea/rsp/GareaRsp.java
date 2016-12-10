package com.net.nky.garea.rsp;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 返回接口对象定义.
 * @author Ken
 * @version 2016年9月20日
 */
public class GareaRsp {

	private Boolean success;//true:创建成功false:创建失败
	private String errorCode;//保存失败时的错误码
	private String errorDesc;//错误信息描述

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorDesc() {
		return errorDesc;
	}

	public void setErrorDesc(String errorDesc) {
		this.errorDesc = errorDesc;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
