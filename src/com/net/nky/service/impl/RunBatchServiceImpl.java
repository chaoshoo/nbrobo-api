package com.net.nky.service.impl;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.jfinal.plugin.activerecord.Db;
import com.net.nky.service.RunBatchService;
import com.net.util.MapUitl;

@Service
public class RunBatchServiceImpl implements RunBatchService {

	public int insertHospital(JSONObject result) {
		JSONObject retObj = JSONObject.fromObject(result);
		JSONObject json=(JSONObject)retObj.get("message");
		if(json.getString("li")!=null){
			List<Map> hosList=(List<Map>)json.get("li");
			String sql="insert into gh_hospital_All  (hosid,hosname,ispay,mdt,parenthosid) values ";
			for(Map map:hosList){
				sql=sql+","+"('"+MapUitl.getMapBykey(map, "hosid")
						+"','"+MapUitl.getMapBykey(map, "hosname")
						+"','"+MapUitl.getMapBykey(map, "ispay")
						+"','"+MapUitl.getMapBykey(map, "mdt")
						+"','"+MapUitl.getMapBykey(map, "parenthosid")+"')";
			}
			sql=sql.replace("values ,(", "values (");
			return Db.update(sql);
		
		}
		return 0;

	}

	public int insertDeptalllist(String hosid,JSONObject result) {
		JSONObject retObj = JSONObject.fromObject(result);
		JSONObject json=(JSONObject)retObj.get("message");
		if(json.getString("li")!=null){
			List<Map> hosList=(List<Map>)json.get("li");
			String sql="insert into gh_dept_All  (hostid,deptname,deptid,parentid,level,mdt) values ";
			for(Map map:hosList){
				sql=sql+","+"('"+hosid+"','"+MapUitl.getMapBykey(map, "deptname")+"','"+MapUitl.getMapBykey(map, "deptid")+"','"+MapUitl.getMapBykey(map, "parentid")
						+"','"+MapUitl.getMapBykey(map, "level")+"',now())";
			}
			sql=sql.replace("values ,(", "values (");
			
			return Db.update(sql);
		
		}
		return 0;
		
	}

	@Override
	public int insertDoctoralllist(String hosid, String deptid,JSONObject resultdoctor) {
		JSONObject retObj = JSONObject.fromObject(resultdoctor);
		JSONObject json=(JSONObject)retObj.get("message");
		try {
			if(json.getString("li")!=null){
				List<Map> hosList=(List<Map>)json.get("li");
				String sql="insert into gh_doctor_All  (GH_COUNT,hostid,deptid,docid,docname,deptname,scheduleflag,doctitle,docdes,smallpicurl,mdt) values ";
				for(Map map:hosList){
					sql=sql+","+"('"+MapUitl.getMapBykey(map, "GH_COUNT")+"','"+hosid+"','"+MapUitl.getMapBykey(map, "deptid")+"','"+MapUitl.getMapBykey(map, "docid")+"','"
							+MapUitl.getMapBykey(map, "docname")+"','"+MapUitl.getMapBykey(map, "deptname")+"','','"+MapUitl.getMapBykey(map, "doctitle")+"'" +
							",'"+MapUitl.getMapBykey(map, "docdes")+"','"+MapUitl.getMapBykey(map, "smallpicurl")+"',now())";
				}
				sql=sql.replace("values ,(", "values (");
				return Db.update(sql);
			}
			return 0;
		} catch (Exception e) {
		}
		return 0;
	
	}

	@Override
	public void deleteTable() {
		String desql1="delete from gh_hospital_All ";
		String desql2="delete from gh_dept_All ";
		String desql3="delete from gh_doctor_All ";
		//先清空表
		Db.update(desql1);
		Db.update(desql2);
		Db.update(desql3);
	}
	
}
