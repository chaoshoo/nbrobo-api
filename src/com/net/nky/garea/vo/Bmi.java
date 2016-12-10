package com.net.nky.garea.vo;

/**
 * 
 * @author Ken
 * @version 2016年9月19日
 */
public class Bmi {
	//bmiStr {"detectDate":"2016-09-21 11:14:04","height":170,"weight":60}
	private String detectDate;//检测时间    时间格式字 符串
	private Float weight;//体重
	private Float height;//身高
	private Float bmi;//bmi指数

	public String getDetectDate() {
		return detectDate;
	}

	public void setDetectDate(String detectDate) {
		this.detectDate = detectDate;
	}

	public Float getWeight() {
		return weight;
	}

	public void setWeight(Float weight) {
		this.weight = weight;
	}

	public Float getHeight() {
		return height;
	}

	public void setHeight(Float height) {
		this.height = height;
	}

	public Float getBmi() {
		return bmi;
	}

	public void setBmi(Float bmi) {
		this.bmi = bmi;
	}

}
