package com.net.nky.service;

import net.sf.json.JSONObject;

/**
 * 接口服务类.
 * @author Ken
 * @version 2016年8月19日
 */
public interface AppInterfaceService {
	
	/**
	 * http://localhost:8080/nkyapi/mobile/interface.do?content={type:%27login%27}
	 * @param messageObj
	 * @param result
	 */
	void login(JSONObject messageObj, JSONObject result);
	
	/**
	 * http://localhost:8080/nkyapi/mobile/interface.do?content={%27type%27:%27registMessage%27,%27tel%27:%2713367241859%27}
	 * @param messageObj
	 * @param result json:{"success":false,"message":"短信发送失败"}
	 */
	void registmessage(JSONObject messageObj, JSONObject result);
	
	/**
	 * 挂号短信.
	 * http://localhost:8080/nkyapi/mobile/interface.do?content={'type':'ghmessage','tel':'13367241859','hospital':'同济','depart':'普珍','doctor':'郑医生','date':'2016-9-22','num':'2'}
	 */
	void ghmessage(JSONObject messageObj, JSONObject result);
	
	/**
	 * http://localhost:8080/nkyapi/mobile/interface.do?content={%27type%27:%27pushmessage%27,%27title%27:%27abc%27,%27content%27:%27def%27,%27channeid%27:%27channeid%27}   
	 * @param messageObj
	 * @param result json:{"success":false,"message":"消息发送失败"}
	 */
	void pushmessage(JSONObject messageObj, JSONObject result);
	
	/**
	 * http://localhost:8080/nkyapi/mobile/interface.do?content={%27type%27:%27changepwdmessage%27,%27tel%27:%2713367241859%27}
	 * @param messageObj
	 * @param result  json:{"success":true,"message":"短信发送成功"}
	 */
	void changepwdmessage(JSONObject messageObj, JSONObject result);
	
	/**
	 * localhost:8080/nkyapi/mobile/interface.do?content={'type':'checkmessage','tel':'13367241859','code':'123456'}
	 * @param messageObj 其中包含2个参数  tel,code code为输入的验证码 
	 * @param result   json:{"success":false,"message":"验证码验证失败"}
	 */
	void checkmessage(JSONObject messageObj, JSONObject result);
	
	/**用户登陆
	 * http://localhost:8080/nkyapi/mobile/interface.do?content={type:}
	 * @param messageObj
	 * @param result
	 */
	void userlogin(JSONObject messageObj, JSONObject result);
	
	/**用户注册
	 * http://localhost:8080/nkyapi/mobile/interface.do?content={type:'useregist'}
	 * @param messageObj
	 * @param result
	 */
	void useregist(JSONObject messageObj, JSONObject result);
	
	/**医院查询
	 * http://localhost:8080/nkyapi/mobile/interface.do?content={type:}
	 * @param messageObj
	 * @param result
	 */
	void hospitals(JSONObject messageObj, JSONObject result);
	
	/**科室查询
	 * http://localhost:8080/nkyapi/mobile/interface.do?content={type:}
	 * @param messageObj
	 * @param result
	 */
	void offices(JSONObject messageObj, JSONObject result);
	
	/**医生查询
	 * http://localhost:8080/nkyapi/mobile/interface.do?content={type:}
	 * @param messageObj
	 * @param result
	 */
	void doctors(JSONObject messageObj, JSONObject result);
	
	/**设备操作指南
	 * http://localhost:8080/nkyapi/mobile/interface.do?content={type:}
	 * @param messageObj
	 * @param result
	 */
	void syspara(JSONObject messageObj, JSONObject result);
	
	/**消息列表
	 * http://localhost:8080/nkyapi/mobile/interface.do?content={type:}
	 * @param messageObj
	 * @param result
	 */
	void messagelist(JSONObject messageObj, JSONObject result);
	
	/**消息详情
	 * http://localhost:8080/nkyapi/mobile/interface.do?content={type:}
	 * @param messageObj
	 * @param result
	 */
	void messageinfo(JSONObject messageObj, JSONObject result);
	
	/**咨询录入
	 * http://localhost:8080/nkyapi/mobile/interface.do?content={type:}
	 * @param messageObj
	 * @param result
	 */
	void questionsave(JSONObject messageObj, JSONObject result);
	
	/**咨询回复录入
	 * http://localhost:8080/nkyapi/mobile/interface.do?content={type:}
	 * @param messageObj
	 * @param result
	 */
	void questionlogsave(JSONObject messageObj, JSONObject result);
	
	/**咨询列表
	 * http://localhost:8080/nkyapi/mobile/interface.do?content={type:}
	 * @param messageObj
	 * @param result
	 */
	void questionlist(JSONObject messageObj, JSONObject result);
	
	/**咨询详情
	 * http://localhost:8080/nkyapi/mobile/interface.do?content={type:}
	 * @param messageObj
	 * @param result
	 */
	void questioninfo(JSONObject messageObj, JSONObject result);
	
	/**远程咨询列表
	 * http://localhost:8080/nkyapi/mobile/interface.do?content={type:}
	 * http://localhost:8080/nkyapi/mobile/interface.do?content={'type':'remotelist','vip_id':'V10102'}
	 * @param messageObj
	 * @param result
	 */
	void remotelist(JSONObject messageObj, JSONObject result);
	
	/**远程咨询详情
	 * http://localhost:8080/nkyapi/mobile/interface.do?content={type:}
	 * @param messageObj
	 * @param result
	 */
	void remoteinfo(JSONObject messageObj, JSONObject result);
	
	/**远程咨询录入
	 * http://localhost:8080/nkyapi/mobile/interface.do?content={type:}
	 * @param messageObj
	 * @param result
	 */
	void remotesave(JSONObject messageObj, JSONObject result);
	
	/**远程咨询取消
	 * http://localhost:8080/nkyapi/mobile/interface.do?content={type:}
	 * @param messageObj
	 * @param result
	 */
	void remotecancel(JSONObject messageObj, JSONObject result);
	
	/**远程咨询录入
	 * http://localhost:8080/nkyapi/mobile/interface.do?content={type:}
	 * @param messageObj
	 * @param result
	 */
	void remotescoresave(JSONObject messageObj, JSONObject result);
	
	/**远程咨询录入
	 * http://localhost:8080/nkyapi/mobile/interface.do?content={type:}
	 * @param messageObj
	 * @param result
	 */
	void remotelogsave(JSONObject messageObj, JSONObject result);

	/**
	 *  http://localhost:8080/nkyapi/mobile/interface.do?content={'type':'measure','inspect_code':'C01','SYS':'137','DIA':'78','PR':'67','card_code':'420222198101010001','device_sn':'2131231321321','inspect_time':'2016-08-23 09:49:35'}
	 *  
	 * 指标检测
	 * @param messageObj
	 * @param result
	 */
	void measure(JSONObject messageObj, JSONObject result);

	/**
http://localhost:8080/nkyapi/mobile/interface.do?content={%27type%27:%27querymeasuredatalist%27,%27inspect_code%27:%27C01%27,%27card_code%27:%27420222198101010001%27,page:%271%27}
	 * 查询指标数据列表
	 * @param messageObj
	 * @param result
	 */
	void querymeasuredatalist(JSONObject messageObj, JSONObject result);
	
	/**
	 *  http://localhost:8080/nkyapi/mobile/interface.do?content={'type':'recentmeasuredata','card_code':'420222198101010001'}
	 * @param messageObj
	 * @param result
	 */
	void  recentmeasuredata(JSONObject messageObj, JSONObject result); 
	
	/**
	 *  http://localhost:8080/nkyapi/mobile/interface.do?content={'type':'savevipfamily',}
	 * @param messageObj
	 * @param result
	 */
	void  savevipfamily(JSONObject messageObj, JSONObject result); 
	
	/**
	 *  http://localhost:8080/nkyapi/mobile/interface.do?content={'type':'vipfamilylist',}
	 * @param messageObj
	 * @param result
	 */
	void  vipfamilylist(JSONObject messageObj, JSONObject result);
	
	/**
	 *  http://localhost:8080/nkyapi/mobile/interface.do?content={'type':'getsystemtime',}
	 * @param messageObj
	 * @param result
	 */
	void getsystemtime(JSONObject messageObj, JSONObject result);
	
	
	/**
	 * 查询医院 860270007
	 * http://localhost:8080/nkyapi/mobile/interface.do?content={'type':'hospitalAllList','hosid':'0','rowstart':'0','rowcount':'0','hosname':'协和','flag':'0'}
	 * * flag＝0表示从数据库查，搜索必须送0
	 * @param messageObj
	 * @param result
	 */
	void  hospitalalllist(JSONObject messageObj, JSONObject result);
	
	
	/**
	 *  860270007 860270129
	 * 查询科室
	 * http://localhost:8080/nkyapi/mobile/interface.do?content={'type':'deptalllist','hosid':'860270007','rowstart':'1','rowcount':'300','deptname':'儿科','flag':'0'}
	 * * flag＝0表示从数据库查，搜索必须送0
	 * @param messageObj
	 * @param result
	 */
	void  deptalllist(JSONObject messageObj, JSONObject result);
	
	
	/**
	 * http://localhost:8080/nkyapi/mobile/interface.do?content={'type':'doctor','deptid':'4200270386','hosid':'860270129','rowstart':'0','rowcount':'20','docname':'张','flag':'0'}
	 * flag＝0表示从数据库查，搜索必须送0
	 * 4200270142
	 * http://localhost:8080/nkyapi/mobile/interface.do?content={'type':'doctor','deptid':'4200270102','hosid':'860270007','rowstart':'0','rowcount':'20'}
	 * 4200270115 
	 * 查询医生
	 * http://localhost:8080/nkyapi/mobile/interface.do?content={'type':'doctor','deptid':'4200270386','hosid':'860270129','rowstart':'0','rowcount':'20'}
	 * @param messageObj
	 * @param result
	 */
	void doctor(JSONObject messageObj, JSONObject result);
	
	
	/**
	 * 
	 * 4200270102  861006747  860270007
	 * http://localhost:8080/nkyapi/mobile/interface.do?content={'type':'doctorschedule','deptid':'4200270102','docid':'861006747','hosid':'860270007','rowstart':'1','rowcount':'10'}
	 * 查询排班
	 * http://localhost:8080/nkyapi/mobile/interface.do?content={'type':'doctorschedule','deptid':'4200270386','docid':'861005481','hosid':'860270129','rowstart':'1','rowcount':'10'}
	 * @param messageObj
	 * @param result
	 */
	void doctorschedule(JSONObject messageObj, JSONObject result);
	
	/**
	 * 860270007
	 * 
	 * 
	* http://localhost:8080/nkyapi/mobile/interface.do?content={'type':'parttime','doctorno':'861006747','hosid':'860270007','rowcount':'0','rowstart':'0','scheduleid':'99246'}
	 * 5.1.5 	查询分时排班
	 * @param messageObj
	 * @param result
	 */
	void parttime(JSONObject messageObj, JSONObject result);
	
	
	/**
	 * 443294
	 * 107755
	* http://localhost:8080/nkyapi/mobile/interface.do?content=
	* {'type':'ghlock','hosid':'860270007','partscheduleid':'411314','scheduleid':'100019','certtypeno':'1','idcard':'421002199102075015',
	* 'patientname':'刘立','patientsex':'1','patientbirthday':'1991-02-07','contactphone':'18907183215','familyaddress':'武汉市东西湖新华医院','vipcode':'V1001','docid':'861000031','outpdate':'2016-09-10 AM','deptid':'4200271144'}
	 * 5.1.6 	锁定号源
	 * @param messageObj
	 * @param result
	 */
	void ghlock(JSONObject messageObj, JSONObject result);
	
	/**
	* http://localhost:8080/nkyapi/mobile/interface.do?content={'type':'confirmorder','orderid':'2002454'}
	 *5.1.7 	确认订单
	 * @param messageObj
	 * @param result
	 */
	void confirmorder(JSONObject messageObj, JSONObject result);
	
	
	/**
	* http://localhost:8080/nkyapi/mobile/interface.do?content={'type':'cancelorder','cancelreason':'test2','operator':'13638631787','orderid':'2002286'}
	 *5.1.8 	取消订单
	 * @param messageObj
	 * @param result
	 */
	void cancelorder(JSONObject messageObj, JSONObject result);
	
	/**
	  * http://localhost:8080/nkyapi/mobile/interface.do?content={'type':'doctorstop','bstp':'0','estp':'0','hosid':'860270007','rowcount':'20','rowstart':'1'}
	 * 5.1.9 	获取停诊订单
	 */
	void doctorstop(JSONObject messageObj, JSONObject result);
	/**
	 * http://localhost:8080/nkyapi/mobile/interface.do?content={'type':'getghorderLst','orderId':'2002498','hosid':'860270007','patientname':'刘立','vipcode':'V2169','docid':'861000031','deptid':'4200271144','status':'1','pageIndex':'1','pageSize':'10'}
	 * 5.1.9 	获取挂号订单列表
	 * status  //1  锁号成功  2 确认成功  3 支付成功 4 支付失败 5 取消订单
	 */
	void getghorderlst(JSONObject messageObj, JSONObject result);
	/**
	 * http://localhost:8080/nkyapi/mobile/interface.do?content={'type':'getwxpreOrder','orderid':'2002498','openid':'oenWDs0oFjJ1q6VuRk-Gr3W1eeOw'}
	 * 5.1.9 	微信支付，预付订单
	 * 
	 */
	void getwxpreorder(JSONObject messageObj, JSONObject result);
	/**
	 * http://localhost:8080/nkyapi/mobile/interface.do?content={'type':'getappversion'}
	 * 5.1.9 	app版本
	 * 
	 */
	void getappversion(JSONObject messageObj, JSONObject result);
	/**
	 * 接触绑定
	 * 
	 * http://localhost:8080/nkyapi/mobile/interface.do?content={'type':'deletebind','vip_code':'V10607','vip_card':'429001198710264633','bind_account':'420106197702160011'}
	 * @param messageObj
	 * @param result
	 */
	void deletebind(JSONObject messageObj, JSONObject result);
	
	/**
	 * http://localhost:8080/nkyapi/mobile/interface.do?content={'type':'doctorlogin','tel':'V10607','password':'429001198710264633','android_tv_channel_id':'420106197702160011'}
	 * 医生登录
	 * @param messageObj
	 * @param result
	 */
	void doctorlogin(JSONObject messageObj, JSONObject result);
	
	/**
	 *  http://localhost:8080/nkyapi/mobile/interface.do?content={'type':'doctorremotelist','doctor_code':'V10607'}
	 * 医生视频列表
	 * @param messageObj
	 * @param result
	 */
	void doctorremotelist(JSONObject messageObj, JSONObject result);
	
	/**
	 * http://localhost:8080/nkyapi/mobile/interface.do?content={'type':'doctorremotewychannel','remote_inspect_id':'V10607','channel_id':'V10607'}
	 * 医生发起视频回填房间号  并push 消息给vip
	 * @param messageObj
	 * @param result
	 */
	void doctorremotewychannel(JSONObject messageObj, JSONObject result);
	
	
	/**
	 * 远程视频 结束回调
	 * @param messageObj
	 * @param result
	 */
	void doctorexitvedio(JSONObject messageObj, JSONObject result);

	/**
	 * 获取视频用到的token
	 * @param messageObj
	 * @param result
	 */
	void getvediotoken(JSONObject messageObj, JSONObject result);
	
	/**
	 * 创建房间
	 * @param messageObj
	 * @param result
	 */
	void makechannelname(JSONObject messageObj, JSONObject result);
	
	/**
	 * 创建房间
	 * @param messageObj
	 * @param result
	 */
	void deletechannelname(JSONObject messageObj, JSONObject result);
}
