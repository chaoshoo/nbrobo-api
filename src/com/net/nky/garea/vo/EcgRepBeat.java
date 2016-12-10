package com.net.nky.garea.vo;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

public class EcgRepBeat {

	private String domData;///67/rP+q/6f/p/+k/6X/pv+n/6j/r/+w/7P/tf+7/7//uf+2/7L/sP+t/6z/rv+x/7P/sP+w/7P/uf+4/7r/vf/D/83/zP/O/83/zv/O/9D/1P/T/9L/0//N/8b/w//G/8H/uv+1/7H/tv+0/7

	//"domLeadLength":387,"pEnd":109,"pStart":58,"qrsEnd":201,"qrsStart":140,"tEnd":308
	private Integer domLeadLength;
	private Integer pEnd;
	private Integer pStart;
	private Integer qrsEnd;
	private Integer qrsStart;
	private Integer tEnd;

	public Integer getpEnd() {
		return pEnd;
	}

	public void setpEnd(Integer pEnd) {
		this.pEnd = pEnd;
	}

	public Integer getpStart() {
		return pStart;
	}

	public void setpStart(Integer pStart) {
		this.pStart = pStart;
	}

	public Integer getQrsEnd() {
		return qrsEnd;
	}

	public void setQrsEnd(Integer qrsEnd) {
		this.qrsEnd = qrsEnd;
	}

	public Integer getQrsStart() {
		return qrsStart;
	}

	public void setQrsStart(Integer qrsStart) {
		this.qrsStart = qrsStart;
	}

	public Integer gettEnd() {
		return tEnd;
	}

	public void settEnd(Integer tEnd) {
		this.tEnd = tEnd;
	}

	public String getDomData() {
		return domData;
	}

	public void setDomData(String domData) {
		this.domData = domData;
	}

	public Integer getDomLeadLength() {
		return domLeadLength;
	}

	public void setDomLeadLength(Integer domLeadLength) {
		this.domLeadLength = domLeadLength;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
