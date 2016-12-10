package com.net.nky.garea.vo;

/**
 * 血压
 * @author Ken
 * @version 2016年9月19日
 */
public class C01 {
	private String detectDate;//检测时间    时间格式字 符串
	private Integer nibpDia;// 
	private Integer nibpMea;// 
	private Integer nibpSys;// 
	private Integer pr;// 
	//{"detectDate":"2016-09-21 15:54:54","nibpDia":77,"nibpMea":90,"nibpSys":126,"pr":99}

	public String getDetectDate() {
		return detectDate;
	}

	public void setDetectDate(String detectDate) {
		this.detectDate = detectDate;
	}

	public Integer getNibpDia() {
		return nibpDia;
	}

	public void setNibpDia(Integer nibpDia) {
		this.nibpDia = nibpDia;
	}

	public Integer getNibpMea() {
		return nibpMea;
	}

	public void setNibpMea(Integer nibpMea) {
		this.nibpMea = nibpMea;
	}

	public Integer getNibpSys() {
		return nibpSys;
	}

	public void setNibpSys(Integer nibpSys) {
		this.nibpSys = nibpSys;
	}

	public Integer getPr() {
		return pr;
	}

	public void setPr(Integer pr) {
		this.pr = pr;
	}

}
