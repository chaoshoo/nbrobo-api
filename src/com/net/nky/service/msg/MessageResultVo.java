package com.net.nky.service.msg;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 短信发送返回.
 * @author Ken
 * @version 2016年8月18日
 */
public class MessageResultVo {

	private boolean success; //是否发送成功

	private String verifycode; //发送成功的验证吗

	private String message;//发送失败的原因，和发送成功时为：短信发送成功

	private MessageResultVo() {

	}

	/**
	 * 短信发送成功构造类
	 * @param success
	 * @param verifycode
	 */
	public MessageResultVo(boolean success, String verifycode) {
		this();
		setSuccess(success);
		this.verifycode = verifycode;
	}

	public String getVerifycode() {
		return verifycode;
	}

	public void setVerifycode(String verifycode) {
		this.verifycode = verifycode;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
		if (this.success) {
			this.message = MessageMetaData.OK;
		} else {
			this.message = MessageMetaData.ERROR;
		}
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
