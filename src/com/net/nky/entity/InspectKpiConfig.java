package com.net.nky.entity;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * inspect_kpi_config 检测数据指标.
 * @author Ken
 * @version 2016年8月22日
 */
public class InspectKpiConfig {
	@JsonIgnore
	private Long id;
	private String code;
	private String name;
	private Integer isfz;
	private String unit;
	private String inspectCode;
	private String des;
	private String kpiMax;
	private String kpiMin;
	private String kpiPic;

	private Set<InspectKpiConfigFz> fzSet = new HashSet<InspectKpiConfigFz>();

	public InspectKpiConfig() {

	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return this.id;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCode() {
		return this.code;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setIsfz(Integer isfz) {
		this.isfz = isfz;
	}

	public Integer getIsfz() {
		return this.isfz;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getUnit() {
		return this.unit;
	}

	public void setInspectCode(String inspectCode) {
		this.inspectCode = inspectCode;
	}

	public String getInspectCode() {
		return this.inspectCode;
	}

	public void setDes(String des) {
		this.des = des;
	}

	public String getDes() {
		return this.des;
	}

	public void setKpiMax(String kpiMax) {
		this.kpiMax = kpiMax;
	}

	public String getKpiMax() {
		return this.kpiMax;
	}

	public void setKpiMin(String kpiMin) {
		this.kpiMin = kpiMin;
	}

	public String getKpiMin() {
		return this.kpiMin;
	}

	public void setKpiPic(String kpiPic) {
		this.kpiPic = kpiPic;
	}

	public String getKpiPic() {
		return this.kpiPic;
	}

	public Set<InspectKpiConfigFz> getFzSet() {
		return fzSet;
	}

	public void setFzSet(Set<InspectKpiConfigFz> fzSet) {
		this.fzSet = fzSet;
	}

	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
