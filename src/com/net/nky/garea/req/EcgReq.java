package com.net.nky.garea.req;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import com.net.nky.garea.vo.Bmi;
import com.net.nky.garea.vo.Ecg;

/**
 * 创建医生或者居民用户
 * @author Ken
 * @version 2016年9月19日
 */
public class EcgReq extends BaseReq {

	private String deviceId;

	private Ecg ecg;

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public Ecg getEcg() {
		return ecg;
	}

	public void setEcg(Ecg ecg) {
		this.ecg = ecg;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
