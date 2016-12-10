package com.net.nky.garea.vo;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 温度.
 * @author Ken
 * @version 2016年9月19日
 */
public class Temperature {

	private String detectDate;

	private Float temperature;//体温 *10

	public String getDetectDate() {
		return detectDate;
	}

	public void setDetectDate(String detectDate) {
		this.detectDate = detectDate;
	}

	public Float getTemperature() {
		return temperature;
	}

	public void setTemperature(Float temperature) {
		this.temperature = temperature;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

}
