package com.net.nky.controller;

import java.util.List;
import java.util.Map;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.net.nky.service.AppInterfaceService;
import com.net.nky.service.RunBatchService;
import com.net.nky.service.impl.AppInterfaceServiceImpl;
import com.net.util.JsonUtil;

@Controller
@RequestMapping("/cancelorder")
public class cancelOrderContoller {

	
private static final Logger LOGGER = Logger.getLogger(cancelOrderContoller.class);
	
 @Autowired		
 private AppInterfaceServiceImpl appInterfaceServiceImpl;
 
 
 @Autowired		
 private RunBatchService runBatchService;
 
 
	/**
	 *定时取消未支付订单
	 *http://www.localhost.com:8080/nkyapi/cancelorder/cancel.do?
	 */
 	@RequestMapping(value = "/cancelOrder.do")
	public void cancelOrder() {
 		AppInterfaceService appInterfaceService = new AppInterfaceServiceImpl();
 		JSONObject messageObj = new JSONObject();
 		JSONObject result = new JSONObject();
		List<Record> calOrderLst=Db.find("select * from vip_reg where status='2' and TO_DAYS(NOW()) - TO_DAYS(create_time) > 1 / 24 / 60*30");
		for(Record record:calOrderLst){
			String orderid=record.getStr("orderid");
//			超期未支付，订单状态及流程相关服务
			try {
//				String inputStr="{'type':'cancelorder','cancelreason':'test2','operator':'13638631787','orderid':'2002286'}";
				messageObj.put("cancelreason", "支付超时,系统自动取消");
				messageObj.put("operator", "SYS");
				messageObj.put("orderid", orderid);
				appInterfaceService.cancelorder(messageObj, result);
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}
 	}
}
