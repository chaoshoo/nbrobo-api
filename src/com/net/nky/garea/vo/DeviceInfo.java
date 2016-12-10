package com.net.nky.garea.vo;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

public class DeviceInfo {

	private String deviceId; //设备编号
	private String deviceName;//设备名称
	private String deviceModel;//设备类型 1: gp102 	2:gp102plus   3:gt1000

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getDeviceModel() {
		return deviceModel;
	}

	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

}
