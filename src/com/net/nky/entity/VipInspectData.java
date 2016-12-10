package com.net.nky.entity;

import java.math.BigDecimal;
import java.util.Date;

//vip_inspect_data
public class VipInspectData {
	private Long id;
	private String cardCode;
	private String inspectCode;
	private String deviceSn;
	private Date inspectTime;
	private BigDecimal gy;
	private BigDecimal dy;
	private BigDecimal ml;
	private BigDecimal ns;
	private BigDecimal height;
	private BigDecimal weight;
	private BigDecimal bmi;
	private Date createTime;

	public VipInspectData() {

	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return this.id;
	}

	public void setCardCode(String cardCode) {
		this.cardCode = cardCode;
	}

	public String getCardCode() {
		return this.cardCode;
	}

	public void setInspectCode(String inspectCode) {
		this.inspectCode = inspectCode;
	}

	public String getInspectCode() {
		return this.inspectCode;
	}

	public void setDeviceSn(String deviceSn) {
		this.deviceSn = deviceSn;
	}

	public String getDeviceSn() {
		return this.deviceSn;
	}

	public void setInspectTime(Date inspectTime) {
		this.inspectTime = inspectTime;
	}

	public Date getInspectTime() {
		return this.inspectTime;
	}

	public void setGy(BigDecimal gy) {
		this.gy = gy;
	}

	public BigDecimal getGy() {
		return this.gy;
	}

	public void setDy(BigDecimal dy) {
		this.dy = dy;
	}

	public BigDecimal getDy() {
		return this.dy;
	}

	public void setMl(BigDecimal ml) {
		this.ml = ml;
	}

	public BigDecimal getMl() {
		return this.ml;
	}

	public void setNs(BigDecimal ns) {
		this.ns = ns;
	}

	public BigDecimal getNs() {
		return this.ns;
	}

	public void setHeight(BigDecimal height) {
		this.height = height;
	}

	public BigDecimal getHeight() {
		return this.height;
	}

	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}

	public BigDecimal getWeight() {
		return this.weight;
	}

	public void setBmi(BigDecimal bmi) {
		this.bmi = bmi;
	}

	public BigDecimal getBmi() {
		return this.bmi;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getCreateTime() {
		return this.createTime;
	}
}
