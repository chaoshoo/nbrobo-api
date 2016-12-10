package com.net.nky.service;

import org.springframework.stereotype.Service;

import net.sf.json.JSONObject;

@Service
public interface RunBatchService {

	
	int insertHospital(JSONObject result);

	int insertDeptalllist(String hosid,JSONObject result);

	int insertDoctoralllist(String hosid, String deptid, JSONObject resultdoctor);

	void deleteTable();
	
}
