package com.net.nky.garea.req;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * /Garea-GT1000-业务数据上传规范v0.2.pdf
 * 公共参数作为所有接口都需要提供的参数，除非有接口特殊说明的可不用提供公共参数。
 * @author Ken
 * @version 2016年9月19日
 */
public class BaseReq {

	protected String appCode;

	protected String custCode;// 企业客户编码 4 位字符
	protected String pIdCard;//居民用户身份证号 创建医生时可为空
	protected String dIdCard;// 医生身份证号

	protected String deviceId;
	protected String signData;
	protected String sign;

	public BaseReq() {
		super();
	}

	public BaseReq(String appCode, String custCode, String pIdCard, String dIdCard, String deviceId, String signData,
			String sign) {
		super();
		this.appCode = appCode;
		this.custCode = custCode;
		this.pIdCard = pIdCard;
		this.dIdCard = dIdCard;
		this.deviceId = deviceId;
		this.signData = signData;
		this.sign = sign;
	}

	public String getAppCode() {
		return appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	public String getSignData() {
		return signData;
	}

	public void setSignData(String signData) {
		this.signData = signData;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getCustCode() {
		return custCode;
	}

	public void setCustCode(String custCode) {
		this.custCode = custCode;
	}

	public String getpIdCard() {
		return pIdCard;
	}

	public void setpIdCard(String pIdCard) {
		this.pIdCard = pIdCard;
	}

	public String getdIdCard() {
		return dIdCard;
	}

	public void setdIdCard(String dIdCard) {
		this.dIdCard = dIdCard;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
