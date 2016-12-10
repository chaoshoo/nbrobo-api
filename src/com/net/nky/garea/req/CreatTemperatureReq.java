package com.net.nky.garea.req;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import com.net.nky.garea.vo.DeviceInfo;
import com.net.nky.garea.vo.Temperature;

/**
 * 创建医生或者居民用户
 * @author Ken
 * @version 2016年9月19日
 */
public class CreatTemperatureReq extends BaseReq{

	private DeviceInfo deviceInfo;
	private Temperature temperature;

	public DeviceInfo getDeviceInfo() {
		return deviceInfo;
	}

	public void setDeviceInfo(DeviceInfo deviceInfo) {
		this.deviceInfo = deviceInfo;
	}

	public Temperature getTemperature() {
		return temperature;
	}

	public void setTemperature(Temperature temperature) {
		this.temperature = temperature;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
