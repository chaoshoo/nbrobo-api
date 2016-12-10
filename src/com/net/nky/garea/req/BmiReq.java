package com.net.nky.garea.req;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import com.net.nky.garea.vo.Bmi;
import com.net.nky.garea.vo.DeviceInfo;

/**
 * 创建医生或者居民用户
 * @author Ken
 * @version 2016年9月19日
 */
public class BmiReq extends BaseReq {

	private String deviceId;
	
	private Bmi bmi;

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public Bmi getBmi() {
		return bmi;
	}

	public void setBmi(Bmi bmi) {
		this.bmi = bmi;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
