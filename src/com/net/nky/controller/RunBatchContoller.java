package com.net.nky.controller;

import java.util.List;
import java.util.Map;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.net.nky.service.RunBatchService;
import com.net.nky.service.impl.AppInterfaceServiceImpl;
import com.net.util.JsonUtil;

@Controller
@RequestMapping("/runbatch")
public class RunBatchContoller {

	
private static final Logger LOGGER = Logger.getLogger(RunBatchContoller.class);
	
 @Autowired		
 private AppInterfaceServiceImpl appInterfaceServiceImpl;
 
 
 @Autowired		
 private RunBatchService runBatchService;

 
	/**
	 *查询医生入库，并根據id循環便利科室，在根據科室循環便利醫生
	 */
 	@RequestMapping(value = "/searchHospital.do")
	public void searchHospital() {
 		runBatchService.deleteTable();
 		int json2 =0;
 		int json3=0;
		try {
			//拼装json数据
			JSONObject messageObj = new JSONObject();
			JSONObject resulthos = new JSONObject();
			JSONObject resultdept = new JSONObject();
			JSONObject resultdoctor = new JSONObject();
			
			messageObj.put("hosid", "0");
			messageObj.put("rowstart", "0");
			messageObj.put("rowcount", "0");
			LOGGER.info("------------------------------------定时查询医院信息------------------------------------");
			appInterfaceServiceImpl.hospitalalllist(messageObj, resulthos);
			int json=runBatchService.insertHospital(resulthos);
			LOGGER.info("------------------------------------插入數據條數------------------------------------"+json);
			if(json>0){
				//根據result裡面的hostidl來查科室的信息
				List<Map> list = JsonUtil.getListForResult(resulthos);
				if(list.size()>0){
					for(Map map :list){
						String hosid=map.get("hosid").toString();
						messageObj.put("hosid", hosid);
						appInterfaceServiceImpl.deptalllist(messageObj, resultdept);
						json2=runBatchService.insertDeptalllist(hosid,resultdept);
						LOGGER.info("------------------------------------插入數據條數------------------------------------"+json2);
						if(json2 > 0){
							List<Map> list2= JsonUtil.getListForResult(resultdept);
							if(list2.size()>0){
								for(Map map2 :list2){
									String deptid=map2.get("deptid").toString();
									messageObj.put("deptid", deptid);
									appInterfaceServiceImpl.doctor(messageObj, resultdoctor);
									json3=runBatchService.insertDoctoralllist(hosid,deptid,resultdoctor);
									LOGGER.info("------------------------------------插入數據條數------------------------------------"+json3);
								}
							}
						
						}
					
							
					}
					
					
					
				}
			}
			
			
		} catch (Exception e) {
			LOGGER.error("定时跑批失败"+e);
			e.printStackTrace();
		}

	}
}
