package com.net.nky.garea.vo;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * Ecg 配置.
 * @author Ken
 * @version 2016年9月23日
 */
public class EcgConfig {

	private Integer highPassFilter;//":7,"
	private Integer lowPassFilter;//":5,"
	private Integer notchFilter;//":0;

	public Integer getHighPassFilter() {
		return highPassFilter;
	}

	public void setHighPassFilter(Integer highPassFilter) {
		this.highPassFilter = highPassFilter;
	}

	public Integer getLowPassFilter() {
		return lowPassFilter;
	}

	public void setLowPassFilter(Integer lowPassFilter) {
		this.lowPassFilter = lowPassFilter;
	}

	public Integer getNotchFilter() {
		return notchFilter;
	}

	public void setNotchFilter(Integer notchFilter) {
		this.notchFilter = notchFilter;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
