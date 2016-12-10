package com.net.nky.garea.vo;

/**
 * 7种类型"空腹","早餐餐前","早餐餐后","午餐餐前","午餐餐后","晚餐餐前","晚餐餐后" 的对应代码发我
 * 0到6
 * @author Ken
 * @version 2016年9月21日
 */
public class C02 {

	private String detectDate;//检测时间    时间格式字 符串

	private Float glu;

	private Integer beforeOrAtter;
	
	//GLU0 随机血糖    GLU1	餐前血糖    GLU2		餐后血糖
	public String getType(){
		if(beforeOrAtter == 0){
			return "GLU0";
		}else if (beforeOrAtter == 1 || beforeOrAtter == 3 || beforeOrAtter == 5){
			return "GLU1";
		}else if (beforeOrAtter == 2 || beforeOrAtter == 4 || beforeOrAtter == 6){
			return "GLU2";
		}
		return "";
	}
	
	public String getDetectDate() {
		return detectDate;
	}

	public void setDetectDate(String detectDate) {
		this.detectDate = detectDate;
	}

	public Float getGlu() {
		return glu;
	}

	public void setGlu(Float glu) {
		this.glu = glu;
	}

	public Integer getBeforeOrAtter() {
		return beforeOrAtter;
	}

	public void setBeforeOrAtter(Integer beforeOrAtter) {
		this.beforeOrAtter = beforeOrAtter;
	}

}
