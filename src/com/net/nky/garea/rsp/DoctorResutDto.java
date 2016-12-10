package com.net.nky.garea.rsp;

import java.util.List;

/**
 * 同步医生接口返回.
 * @author Ken
 * @version 2016年11月18日
 */
public class DoctorResutDto extends ResultDto {

	private static final long serialVersionUID = -2553397501489314627L;

	private List<Doctor> results;

	public List<Doctor> getResults() {
		return results;
	}

	public void setResults(List<Doctor> results) {
		this.results = results;
	}

}
