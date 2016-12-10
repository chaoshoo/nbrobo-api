package com.net.nky.entity;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * dic字典表. inspect_code dic_type.
 * @author Ken
 * @version 2016年8月22日
 */
public class InspectDic {
	private String dicName;//C01
	private String dicValue;//血压

	private Map<String, InspectKpiConfig> inspectMap = new HashMap<String,InspectKpiConfig>();

	public InspectDic() {

	}

	public String getDicName() {
		return dicName;
	}

	public void setDicName(String dicName) {
		this.dicName = dicName;
	}

	public String getDicValue() {
		return dicValue;
	}

	public void setDicValue(String dicValue) {
		this.dicValue = dicValue;
	}

	public Map<String, InspectKpiConfig> getInspectMap() {
		return inspectMap;
	}

	public void setInspectMap(Map<String, InspectKpiConfig> inspectMap) {
		this.inspectMap = inspectMap;
	}

	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
