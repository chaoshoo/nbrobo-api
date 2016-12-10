package com.net.nky.garea.req;

import java.util.Date;

/**
 * 3.1.2上传查体报告
 * @author Ken
 * @version 2016年9月19日
 */
public class DetectReportReq extends BaseReq {

	private Long flupId;//随访编号 为空时是体检报告

	private String deviceInfo;//设备信息  deviceId: 设备编号

	private String reportNo;//报告编号  17 位是体检报告  18 位是随访报告

	private Date reportTime;//查体报告生成时间

	private Integer influence;//溶血 :1  黄疸 	:2    脂血 :4

	private Object reportData;//报告内容  与心电检测时间只能一个为空

	private Date ecgDetectDate;//心电检测时间     与报告数据只能一个为空

	public String getDeviceInfo() {
		return deviceInfo;
	}

	public void setDeviceInfo(String deviceInfo) {
		this.deviceInfo = deviceInfo;
	}

	public Long getFlupId() {
		return flupId;
	}

	public void setFlupId(Long flupId) {
		this.flupId = flupId;
	}

	public String getReportNo() {
		return reportNo;
	}

	public void setReportNo(String reportNo) {
		this.reportNo = reportNo;
	}

	public Date getReportTime() {
		return reportTime;
	}

	public void setReportTime(Date reportTime) {
		this.reportTime = reportTime;
	}

	public Integer getInfluence() {
		return influence;
	}

	public void setInfluence(Integer influence) {
		this.influence = influence;
	}

	public Object getReportData() {
		return reportData;
	}

	public void setReportData(Object reportData) {
		this.reportData = reportData;
	}

	public Date getEcgDetectDate() {
		return ecgDetectDate;
	}

	public void setEcgDetectDate(Date ecgDetectDate) {
		this.ecgDetectDate = ecgDetectDate;
	}

}
