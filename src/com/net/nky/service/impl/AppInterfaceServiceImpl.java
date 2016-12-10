package com.net.nky.service.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.huilet.util.PubMethod;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.net.ServiceConstants;
import com.net.nky.entity.InspectDic;
import com.net.nky.entity.InspectKpiConfig;
import com.net.nky.entity.InspectKpiConfigFz;
import com.net.nky.nrtc.CheckSumBuilder;
import com.net.nky.pay.action.TopayServlet;
import com.net.nky.seq.IdCoderService;
import com.net.nky.service.AppInterfaceService;
import com.net.nky.service.cache.MessageCacheSingleton;
import com.net.nky.service.msg.MessageResultVo;
import com.net.nky.service.msg.MessageSendUtil;
import com.net.nky.service.push.PushUtil;
import com.net.nky.singleton.InspectConfigData;
import com.net.nky.singleton.SysSingleton;
import com.net.util.CodeUtil;
import com.net.util.DateUtil;
import com.net.util.HttpClient;
import com.net.util.HttpParamUtil;
import com.net.util.JsonUtil;
import com.net.util.MD5Util;
import com.net.util.MapUitl;
import com.net.util.SpringUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class AppInterfaceServiceImpl implements AppInterfaceService {

	private Logger LOG = LoggerFactory.getLogger(AppInterfaceServiceImpl.class);
	
	@Autowired
	IdCoderService idCoderService = (IdCoderService) SpringUtil.getBean("idCoderService");
	
	static String[] format = {"yyyy-MM-dd HH:mm:ss"};

	public static final String MSG_TEL_ERROR = "手机号码不正确";

	public static final String REQ_ERROR = "请求不合法";

	public static final String MSG_REQ_ERROR = REQ_ERROR;
	
	public static final String MSG_SEND_ERROR = "短信发送失败";
	
	public static final String MSG_SEND_OK = "短信发送成功";
	
	public static final String MSG_CHECK_OK = "验证码验证成功";

	public static final String MSG_CHECK_ERROR = "验证码验证失败";
	
	public static final String MSG_CHECK_INVALID = "验证码失效";
	
	public static final String LOGIN_SUCCESS = "登陆成功";
	
	public static final String LOGIN_FAIL_PASSWORD = " 密码错误";
	
	public static final String NO_REGISTER = "未注册";

	public static final String LOGIN_LACK = "缺少参数";
	
	public static final String OK = "执行成功";
	
	public static final String ERROR = "执行失败";
	
	/**
	 * APP登录
	 * @param messageObj
	 * @param result
	 */
	public void login(JSONObject messageObj, JSONObject result) {
		result.put("flag", "fail");
		result.put("remark", "idnotexist");
	}

	@Override
	public void registmessage(JSONObject messageObj, JSONObject result) {
		String tel = (String) messageObj.getString("tel").toLowerCase();
		if (!MessageSendUtil.isMobile(tel)) {
			result.put("success", false);
			result.put("message", MSG_TEL_ERROR);
			return;
		}
		clearCache(tel); 
		MessageResultVo vo = MessageSendUtil.sendRegistMessage(tel);
		if(vo.isSuccess()){
			MessageCacheSingleton.getSingleton().saveOrUpdateVerifyCode(tel, vo.getVerifycode());
//			MessageCacheSingleton.getSingleton().set(tel, vo.getVerifycode());
			result.put("success", true);
			result.put("message", MSG_SEND_OK);
		}else{
			MessageCacheSingleton.getSingleton().removeVerifyCode(tel);
//			MessageCacheSingleton.getSingleton().del(tel);
			result.put("success", false);
			result.put("message", vo.getMessage());
		}
	}

	@Override
	public void changepwdmessage(JSONObject messageObj, JSONObject result) {
		String tel = (String) messageObj.getString("tel").toLowerCase();
		if (!MessageSendUtil.isMobile(tel)) {
			result.put("success", false);
			result.put("message", MSG_TEL_ERROR);
			return;
		}
		clearCache(tel);
		MessageResultVo vo = MessageSendUtil.sendChangePwdMessage(tel);
		if(vo.isSuccess()){
			MessageCacheSingleton.getSingleton().saveOrUpdateVerifyCode(tel, vo.getVerifycode());
//			MessageCacheSingleton.getSingleton().set(tel, vo.getVerifycode());
			result.put("success", true);
			result.put("message", MSG_SEND_OK);
		}else{
			MessageCacheSingleton.getSingleton().removeVerifyCode(tel);
//			MessageCacheSingleton.getSingleton().del(tel);
			result.put("success", false);
			result.put("message", vo.getMessage());
		}
	}

	@Override
	public void checkmessage(JSONObject messageObj, JSONObject result) {
		String tel = (String) messageObj.getString("tel").toLowerCase();
		String ccode = (String) messageObj.getString("code").toLowerCase();
		if (!MessageSendUtil.isMobile(tel) || StringUtils.isEmpty(ccode)) {
			result.put("success", false);
			result.put("message", MSG_REQ_ERROR);
			return;
		}
		String code = MessageCacheSingleton.getSingleton().getCacheVerifyCode(tel);
		if (code == null) {
			result.put("success", false);
			result.put("message", MSG_CHECK_INVALID);
			return;
		} else if (ccode.equals(code)) {
			result.put("code", code);
			result.put("success", true);
			result.put("message", MSG_CHECK_OK);
			clearCache(tel);
		} else {
			result.put("success", false);
			result.put("message", MSG_CHECK_ERROR);
			return;
		}
	}
	@Override
	public void ghmessage(JSONObject messageObj, JSONObject result) {
		String tel = (String) messageObj.getString("tel").toLowerCase();
		String hospital = (String) messageObj.getString("hospital").toLowerCase();
		String depart = (String) messageObj.getString("depart").toLowerCase();
		String doctor = (String) messageObj.getString("doctor").toLowerCase();
		String date = (String) messageObj.getString("date").toLowerCase();
		String num = (String) messageObj.getString("num").toLowerCase();
		try { 
			hospital = new String(hospital.getBytes("ISO8859-1"), Charset.forName("UTF-8"));
			depart = new String(depart.getBytes("ISO8859-1"), Charset.forName("UTF-8")); 
			doctor = new String(doctor.getBytes("ISO8859-1"), Charset.forName("UTF-8")); 
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} 
		
		if (!MessageSendUtil.isMobile(tel)) {
			result.put("success", false);
			result.put("message", MSG_TEL_ERROR);
			return;
		}
		clearCache(tel);
		MessageResultVo vo = MessageSendUtil.sendGh(tel, hospital, depart, doctor, date, num);
		if(vo.isSuccess()){
			MessageCacheSingleton.getSingleton().saveOrUpdateVerifyCode(tel, vo.getVerifycode());
//			MessageCacheSingleton.getSingleton().set(tel, vo.getVerifycode());
			result.put("success", true);
			result.put("message", MSG_SEND_OK);
		}else{
			MessageCacheSingleton.getSingleton().removeVerifyCode(tel);
//			MessageCacheSingleton.getSingleton().del(tel);
			result.put("success", false);
			result.put("message", vo.getMessage());
		}
	}
	/**
	 * 清理掉{@code tel}手机号码的缓存.
	 */
	private void clearCache(String tel){
		boolean deleteFlag = MessageCacheSingleton.getSingleton().removeVerifyCode(tel);
		LOG.debug("清"+tel+"缓存:" + deleteFlag);
	}

	@Override
	public void userlogin(JSONObject messageObj, JSONObject result){
		Object num=messageObj.get("num");//手机号
		//Object papers_num=messageObj.get("papers_num");//证件号
		String password=messageObj.getString("password");//密码
		Object android_tv_channel_id=messageObj.get("android_tv_channel_id");//证件号
		
		if((num==null||"".equals(num.toString())))
		{
			result.put("code", "0");
			result.put("message", LOGIN_LACK);
			return;
		}
		
		
		String sql=" SELECT * FROM  t_vip WHERE isvalid='1' and( mobile = ? or papers_num = ? or card_code = ?)";
		
		List<Object> paraList = new ArrayList<Object>();
		paraList.add(num);
		paraList.add(num);
		paraList.add(num);
		
		Record vipInfo = Db.findFirst(sql, paraList.toArray());
		if(vipInfo == null){
			result.put("code", "0");
			result.put("message", NO_REGISTER);
			return;
		}
		
		password = MD5Util.MD5(password.toString(),"UTF-8").toLowerCase();
		String login_password = vipInfo.getStr("login_password").toLowerCase();
		if(!login_password.equals(password)){
			result.put("code", "0");
			result.put("message", LOGIN_FAIL_PASSWORD);
			return;
		}
		result.put("vip_info",JsonUtil.getMapByJfinalRecord(vipInfo));
		
		if(android_tv_channel_id!=null && !"".equals(android_tv_channel_id.toString()))
		{
			vipInfo.set("android_tv_channel_id", android_tv_channel_id);
			Db.update("t_vip", vipInfo);
		}
		
		result.put("code", "1");
		result.put("message", LOGIN_SUCCESS);
		
	}

	@Override
	public void useregist(JSONObject messageObj, JSONObject result)
	{
		String mobile = messageObj.getString("mobile");
		try{
			
			String vip_id =  idCoderService.getSupplyClientCode();
			Object login_account = messageObj.get("login_account");
			if(login_account == null){
				login_account = mobile;
			}			
			Object login_password = messageObj.get("login_password");
			if(login_password!=null&&!"".equals(login_password))
			{
				login_password = MD5Util.MD5(login_password.toString(),"UTF-8").toLowerCase();
			}
			
			Object card_code = messageObj.get("card_code");
			Object heard_img_url = messageObj.get("heard_img_url");
			Object papers_type = messageObj.get("papers_type");
			Object papers_num = messageObj.get("papers_num");
			Object nick_name = messageObj.get("nick_name");
			Object real_name = messageObj.get("real_name");
			Object account_mail = messageObj.get("account_mail");
			Object weight = messageObj.get("weight");
			Object height =  messageObj.get("height");
			Object sex =  messageObj.get("sex");
			if(sex == null || "null".equals(sex)||"".equals(sex)){
				sex="0";
			}
			Object area =  messageObj.get("area");
			Object address =  messageObj.get("address");
			Object birthday =  messageObj.get("birthday");
			Object post_code =  messageObj.get("post_code");
			Object phone =  messageObj.get("phone");
			Object ill_history =  messageObj.get("ill_history");
			Object gm =  messageObj.get("gm");
			Object qq =  messageObj.get("qq");
			Object wxopenid =  messageObj.get("wxopenid");
			Object android_tv_token_id =  messageObj.get("android_tv_token_id");
			Object android_tv_channel_id =  messageObj.get("android_tv_channel_id");
			
			if(card_code==null || "".equals(card_code.toString()))
			{
				card_code = papers_num;
			}
			
			Record rec  = Db.findFirst("select * from t_vip where (card_code=? or papers_num=? or mobile=? ) and isvalid = '1' ",
										new Object[]{card_code,papers_num,mobile});
			if(rec != null)
			{
				result.put("code", "0");
				result.put("message", "您已经注册过了，请直接登录！");
				return ;
			}
			
			Db.update("insert into t_vip (vip_code,card_code,login_account,mobile,login_password,heard_img_url,isvalid,papers_type,"+
												"papers_num,nick_name,real_name,account_mail,weight,height,sex,area,address,birthday,post_code,phone,"+
												"ill_history,gm,qq,wxopenid,android_tv_token_id,android_tv_channel_id,modify_time,create_time) "+
					" values(?,?,?,?,?,?,'1',?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,NOW(),NOW())",
					new Object[]{vip_id,card_code,login_account,mobile,login_password,heard_img_url,papers_type,
							papers_num,nick_name,real_name,account_mail,weight,height,sex,area,address,birthday,post_code,phone,
							ill_history,gm,qq,wxopenid,android_tv_token_id,android_tv_channel_id});
			
			result.put("code", "1");
			result.put("message", "注册成功");	
			
		}catch(Exception e)
		{
			e.printStackTrace();
			result.put("code", "0");
			result.put("message", "注册失败！");
		}
		
		
	}

	@Override
	public void hospitals(JSONObject messageObj, JSONObject result)
	{
		Object name = messageObj.get("name");
		Object code = messageObj.get("code");
		Object pageIndex = messageObj.get("pageIndex");
		Object pageSize = messageObj.get("pageSize");		
		
		String qrySql = "select * from hospital where 1=1 ";
		List<Object> paraList = new ArrayList<Object>();
		String conSql = "";		
		
		if(code!=null && !"".equals(code.toString()))
		{
			conSql+= " and code = ? ";
			paraList.add(code);
		}
		
		if(name!=null && !"".equals(name.toString()))
		{
			conSql+=" and name like '%"+name+"%' ";
		}
		
		String qryCountSql = "select count(*) total from hospital where 1=1 " +conSql;
		Record totalRec = Db.findFirst(qryCountSql,paraList.toArray());
		int total = Integer.parseInt(totalRec.get("total").toString());
		
		if(pageIndex!= null && !"".equals(pageIndex.toString()))
		{
			conSql+=" limit ?,? ";
			paraList.add(Integer.parseInt(pageIndex.toString())-1);
			paraList.add(Integer.parseInt(pageSize.toString()));
		}		
		qrySql = qrySql+conSql;
		List<Record> hosList = Db.find(qrySql,paraList.toArray());
		if(hosList==null || hosList.size()<1)
		{
			result.put("code", "0");
			return;
		}
		result.put("hospitals", JsonUtil.getJsonObjByjfinalList(hosList));
		result.put("code", "1");		
	}

	@Override
	public void offices(JSONObject messageObj, JSONObject result)
	{
		
		Object name = messageObj.get("name");
		Object code = messageObj.get("code");
		Object pageIndex = messageObj.get("pageIndex");
		Object pageSize = messageObj.get("pageSize");		
		
		String qrySql = "select * from office where 1=1 ";
		List<Object> paraList = new ArrayList<Object>();
		String conSql = "";		
		
		if(code!=null && !"".equals(code.toString()))
		{
			conSql+= " and code = ? ";
			paraList.add(code);
		}
		
		if(name!=null && !"".equals(name.toString()))
		{
			conSql+=" and name like '%"+name+"%' ";
		}
		
		String qryCountSql = "select count(*) total from office where 1=1 " +conSql;
		Record totalRec = Db.findFirst(qryCountSql,paraList.toArray());
		int total = Integer.parseInt(totalRec.get("total").toString());
		
		if(pageIndex!= null && !"".equals(pageIndex.toString()))
		{
			conSql+=" limit ?,? ";
			paraList.add((Integer.parseInt(pageIndex.toString())-1) * Integer.parseInt(pageSize.toString()));
			paraList.add(Integer.parseInt(pageSize.toString()));
		}		
		qrySql = qrySql+conSql;
		List<Record> officeList = Db.find(qrySql,paraList.toArray());
		if(officeList==null || officeList.size()<1)
		{
			result.put("code", "0");
			return;
		}
		result.put("offices", JsonUtil.getJsonObjByjfinalList(officeList));
		result.put("code", "1");
	}

	@Override
	public void doctors(JSONObject messageObj, JSONObject result)
	{
		Object name = messageObj.get("name");
		Object code = messageObj.get("code");
		Object officeCode = messageObj.get("office_code");
		Object hospitalCode = messageObj.get("hospital_code");
		Object pageIndex = messageObj.get("pageIndex");
		Object pageSize = messageObj.get("pageSize");
		
		
		String qrySql = "select * from doctor where 1=1 ";
		List<Object> paraList = new ArrayList<Object>();
		String conSql = "";		
		
		if(code!=null && !"".equals(code.toString()))
		{
			conSql+= " and code = ? ";
			paraList.add(code);
		}
		if(hospitalCode!=null && !"".equals(hospitalCode.toString()))
		{
			conSql+= " and hospital_code = ? ";
			paraList.add(hospitalCode);
		}
		if(officeCode!=null && !"".equals(officeCode.toString()))
		{
			conSql+= " and office_code = ? ";
			paraList.add(officeCode);
		}
		
		if(name!=null && !"".equals(name.toString()))
		{
			conSql+=" and name like '%"+name+"%' ";
		}
		
		String qryCountSql = "select count(*) total from doctor where 1=1 " +conSql;
		Record totalRec = Db.findFirst(qryCountSql,paraList.toArray());
		int total = Integer.parseInt(totalRec.get("total").toString());
		
		if(pageIndex!= null && !"".equals(pageIndex.toString()))
		{
			conSql+=" limit ?,? ";
			paraList.add((Integer.parseInt(pageIndex.toString())-1) * Integer.parseInt(pageSize.toString()));
			paraList.add(Integer.parseInt(pageSize.toString()));
		}		
		qrySql = qrySql+conSql;
		List<Record> doctorList = Db.find(qrySql,paraList.toArray());
		if(doctorList==null || doctorList.size()<1)
		{
			result.put("code", "0");
			return;
		}
		result.put("doctors", JsonUtil.getJsonObjByjfinalList(doctorList));
		result.put("code", "1");
		
	}

	@Override
	public void syspara(JSONObject messageObj, JSONObject result) {		
		String code = messageObj.getString("code");		
		result.put("value", SysSingleton.getInstance().getValueByCode(code));
	}
   /**
    *id   title content  create_time
    */
	@Override
	public void messagelist(JSONObject messageObj, JSONObject result) {
		
		Object vip_id = messageObj.get("vip_id");
		Object pageIndex = messageObj.get("pageIndex");
		Object pageSize = messageObj.get("pageSize");		
		
		String qrySql = "select mc.id,m.title,m.content,mc.send_time as create_time from message m,message_center mc where m.id=mc.message_id and send_time>0 "; 
//		String qrySql = "select * from message where 1=1 ";
		List<Object> paraList = new ArrayList<Object>();
		String conSql = "";		
		if(vip_id!=null && !"".equals(vip_id.toString()))
		{
			conSql+= "  and mc.reciver= ? ";
			paraList.add(vip_id);
		}else{
			result.put("code", "0");
			return;
		}
		conSql+=" order by mc.send_time desc  ";
		
		if(pageIndex!= null && !"".equals(pageIndex.toString()))
		{
			conSql+=" limit ?,? ";
			paraList.add((Integer.parseInt(pageIndex.toString())-1) * Integer.parseInt(pageSize.toString()));
			paraList.add(Integer.parseInt(pageSize.toString()));
		}		
		
		qrySql = qrySql+conSql;
		List<Record> messageList = Db.find(qrySql,paraList.toArray());
		if(messageList==null || messageList.size()<1)
		{
			result.put("code", "0");
			return;
		}
		result.put("messages", JsonUtil.getJsonObjByjfinalList(messageList));
		result.put("code", "1");
	}

	@Override
	public void messageinfo(JSONObject messageObj, JSONObject result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void questionlist(JSONObject messageObj, JSONObject result) {
		
		Object vip_code = messageObj.get("vip_code");
		Object doctor_code = messageObj.get("doctor_code");
		Object pageIndex = messageObj.get("pageIndex");
		Object pageSize = messageObj.get("pageSize");		
		
		String qrySql = "select * from vip_questions where 1=1 ";
		List<Object> paraList = new ArrayList<Object>();
		String conSql = "";		
		
		if(vip_code!=null && !"".equals(vip_code.toString()))
		{
			conSql+= " and vip_code = ? ";
			paraList.add(vip_code);
		}
		
		if(doctor_code!=null && !"".equals(doctor_code.toString()))
		{
			conSql+= " and doctor_code = ? ";
			paraList.add(doctor_code);
		}
		
		String qryCountSql = "select count(*) total from vip_questions where 1=1 " +conSql;
		Record totalRec = Db.findFirst(qryCountSql,paraList.toArray());
		int total = Integer.parseInt(totalRec.get("total").toString());
		
		if(pageIndex!= null && !"".equals(pageIndex.toString()))
		{
			conSql+=" limit ?,? ";
			paraList.add((Integer.parseInt(pageIndex.toString())-1) * Integer.parseInt(pageSize.toString()));
			paraList.add(Integer.parseInt(pageSize.toString()));
		}		
		
		qrySql = qrySql+conSql;
		List<Record> questionList = Db.find(qrySql,paraList.toArray());
		if(questionList==null || questionList.size()<1)
		{
			result.put("code", "0");
			return;
		}
		result.put("questions", JsonUtil.getJsonObjByjfinalList(questionList));
		result.put("code", "1");
		
	}

	@Override
	public void questioninfo(JSONObject messageObj, JSONObject result) {

		int ques_id = messageObj.getInt("vip_questions_id");		
		String qrySql = "select * from vip_questions_log where vip_questions_id=? order by create_time asc ";
		
		List<Record> questionInfoList = Db.find(qrySql,new Object[]{ques_id});
		if(questionInfoList==null || questionInfoList.size()<1)
		{
			result.put("code", "0");
			return;
		}
		
		result.put("questions", JsonUtil.getJsonObjByjfinalList(questionInfoList));
		result.put("code", "1");		
	}

	@Override
	public void remotelist(JSONObject messageObj, JSONObject result) {
		
		Object vip_code = messageObj.get("vip_code");
		Object pageIndex = messageObj.get("pageIndex");
		Object pageSize = messageObj.get("pageSize");		
		
		String qrySql = "select a.*,b.name from remote_inspect a ,doctor b "
				+ " where a.doctor_code = b.code ";
		List<Object> paraList = new ArrayList<Object>();
		String conSql = "";	
		
		if(vip_code!=null && !"".equals(vip_code.toString()))
		{
			conSql+= " and a.vip_code = ? ";
			paraList.add(vip_code);
		}
		
		conSql+=" order by a.create_time desc ";
		
		if(pageIndex!= null && !"".equals(pageIndex.toString()))
		{
			conSql+=" limit ?,? ";
			paraList.add((Integer.parseInt(pageIndex.toString())-1) * Integer.parseInt(pageSize.toString()));
			paraList.add(Integer.parseInt(pageSize.toString()));
		}		
		
		qrySql = qrySql+conSql;
		List<Record> remoteList = Db.find(qrySql,paraList.toArray());
		if(remoteList==null || remoteList.size()<1)
		{
			result.put("code", "0");
			return;
		}
		result.put("remotes", JsonUtil.getJsonObjByjfinalList(remoteList));
		result.put("code", "1");
		
	}

	@Override
	public void remoteinfo(JSONObject messageObj, JSONObject result) {
		
	}

	@Override
	public void questionsave(JSONObject messageObj, JSONObject result) {
		
		String vip_code = messageObj.getString("vip_code");			
		Object doctor_code = messageObj.get("doctor_code");
		Object title = messageObj.get("title");
		Object content = messageObj.get("content");
		Object attachement = messageObj.get("attachement");
		
		Db.update("insert into vip_questions (vip_code,doctor_code,title,content,attachement,status,create_time) "+
				" values(?,?,?,?,?,'0',NOW())",
				new Object[]{vip_code,doctor_code,title,content,attachement});
		
		Record r = Db.findFirst("select * from doctor_vip where vip_code = ? and doctor_code = ? ",new Object[]{vip_code,doctor_code});
		if(r==null)
		{
			Db.update("insert into doctor_vip (vip_code,doctor_code,create_time) "+
					" values(?,?,NOW())",
					new Object[]{vip_code,doctor_code});
		}
		
		result.put("code", "1");
		result.put("message", "咨询提交成功");	
	}

	@Override
	public void questionlogsave(JSONObject messageObj, JSONObject result)
	{
		String answer_code = messageObj.getString("answer_code");			
		Object vip_questions_id = messageObj.get("vip_questions_id");
		Object answer_content = messageObj.get("answer_content");
		
		Db.update("insert into vip_questions_log (answer_code,vip_questions_id,answer_content,create_time) "+
				" values(?,?,?,NOW())",
				new Object[]{answer_code,vip_questions_id,answer_content});
		
		result.put("code", "1");
		result.put("message", "留言提交成功");
	}
	
	
	@Override
	public void remotesave(JSONObject messageObj, JSONObject result) {

		String vip_code = messageObj.getString("vip_code");			
		Object doctor_code = messageObj.get("doctor_code");
		Object hospital_code = messageObj.get("hospital_code");
		Object order_time = messageObj.get("order_time");
		Object remark = messageObj.get("remark");
		String code = "R"+CodeUtil.getRandomCode(5);
		
		Db.update("insert into remote_inspect (code,vip_code,doctor_code,hospital_code,order_time,remark,isDeal,create_time) "+
				" values(?,?,?,?,?,?,0,NOW())",
				new Object[]{code,vip_code,doctor_code,hospital_code,order_time,remark});
		
		Record r = Db.findFirst("select * from doctor_vip where vip_code = ? and doctor_code = ? ",new Object[]{vip_code,doctor_code});
		if(r==null)
		{
			Db.update("insert into doctor_vip (vip_code,doctor_code,create_time) "+
					" values(?,?,NOW())",
					new Object[]{vip_code,doctor_code});
		}
		
		
		result.put("code", "1");
		result.put("message", "远程咨询预约提交成功");	
		
	}
	
	@Override
	public void remotecancel(JSONObject messageObj, JSONObject result)
	{
		String remote_code = messageObj.getString("remote_code");	
		Db.update(" update remote_inspect set isDeal=-1 where code = ? ",
				new Object[]{remote_code});
		
		result.put("code", "1");
		result.put("message", "远程咨询取消成功");	
		
	}

	@Override
	public void remotescoresave(JSONObject messageObj, JSONObject result) {
		
		String remote_inspect_code = messageObj.getString("remote_inspect_code");			
		Object vip_code = messageObj.get("vip_code");
		Object content = messageObj.get("content");
		int score = messageObj.getInt("score");
		
		Db.update("insert into remote_inspect_score (remote_inspect_code,vip_code,content,score,create_time) "+
				" values(?,?,?,?,NOW())",
				new Object[]{remote_inspect_code,vip_code,content,score});
		
		result.put("code", "1");
		result.put("message", "远程咨询评分提交成功");	
	}

	@Override
	public void remotelogsave(JSONObject messageObj, JSONObject result) {
		
		String remote_inspect_code = messageObj.getString("remote_inspect_code");			
		Object vip_or_doctor = messageObj.get("vip_or_doctor");
		Object des = messageObj.get("des");
		
		Db.update("insert into remote_inspect_log (remote_inspect_code,vip_or_doctor,des,create_time) "+
				" values(?,?,?,NOW())",
				new Object[]{remote_inspect_code,vip_or_doctor,des});
		
		result.put("code", "1");
		result.put("message", "远程咨询轨迹提交成功");
		
	}
	
		/**
		 * 检查指标入库.
		 *http://localhost:8080/nkyapi/mobile/interface.do?content={'type':'measure','inspect_code':'C01','SYS':'137','DIA':'78','PR':'67','card_code':'123456','device_sn':'2131231321321','inspect_time':'2016-08-23 09:49:35'}
		 */
		@Override
		public void measure(JSONObject messageObj, JSONObject result) {
			try {
				List<String> sqlList = new ArrayList<String>();
				
				String  inspect_code = (String) messageObj.getString("inspect_code");
				String  card_code = (String) messageObj.getString("card_code");
				String  device_sn = (String) messageObj.getString("device_sn");
				String  inspect_time = (String) messageObj.getString("inspect_time");
				String sqltitle = "INSERT INTO vip_inspect_data(card_code,`inspect_code`,`device_sn`,`create_time`,`inspect_time`,";
				String sqldata = "  VALUES ('"+card_code+"','"+inspect_code+"','"+device_sn+"',now(),'"+inspect_time+"', ";
				
				//判断 指标阈值
				String inspect_is_normal = "0";
				//Record r = Db.findFirst("select * from t_vip where card_code = '"+card_code+"' or papers_num = '"+card_code+"' ");
				Record r = Db.findFirst("select * from t_vip where card_code = ? or papers_num = ? ",card_code,card_code);
				String sex = r.get("sex")+"";	
				if(sex ==null || "null".equals(sex)||"".equals(sex)){
					sex = "0";
				}
//				int age  = r.getInt("age");
				Integer age  = r.getInt("age");
				if(age == null){
					age = 0;
				}
				
				InspectDic   inspectDic =  InspectConfigData.getInstance().getInspect(inspect_code);
				Map<String, InspectKpiConfig>   map = inspectDic.getInspectMap();
				
				for(String key  : map.keySet()){
					String desc = "";
					InspectKpiConfig  inspectKpiConfig  =  (InspectKpiConfig)map.get(key);
					desc += inspectKpiConfig.getName();
					String v = "";
					try {
						 v  = messageObj.get(key)+"";
						 if(v.equals("null")){
							 continue;
						 }
					} catch (Exception e) {
						// TODO: handle exception
					}

					sqltitle+=key+",";
					sqldata+="'"+v+"',";
					
					BigDecimal _v=new BigDecimal(v);  
					
					//如果不等于0  跳出
//					if(!"0".equals(inspect_is_normal)){
//						continue;
//					}
					//判断指标是否正常
					String name = inspectKpiConfig.getName();
					Set<InspectKpiConfigFz> fzSet = inspectKpiConfig.getFzSet();
					for(InspectKpiConfigFz  inspectKpiConfigFz  : fzSet){
						String _sex = inspectKpiConfigFz.getSex();
						int _agemax  = inspectKpiConfigFz.getAgeMax();
						int _agemin = inspectKpiConfigFz.getAgeMin();
						
						BigDecimal _fzmax = new BigDecimal(inspectKpiConfigFz.getFzMax());
						BigDecimal _fzmin = new BigDecimal(inspectKpiConfigFz.getFzMin());
						
						if(PubMethod.isEmpty(_fzmax)&&PubMethod.isEmpty(_fzmin)){
							continue;
						}
						
						if(!_sex.equals(sex)){
							continue;
						}
						if(age>_agemax ||age<_agemin){
							continue;
						}
						desc+= _fzmin +"~"+_fzmax;
						if(_v.compareTo(_fzmin)==-1){
							inspect_is_normal = "-1";
						}
						if(_v.compareTo(_fzmax)==1){
							inspect_is_normal = "1";
						}
						
					}
					
					//插入最近一次检测指标
					sqlList.add(" DELETE FROM   vip_inspect_latest  WHERE card_code = '"+card_code+"' AND inspect_code = '"+inspect_code+"' and kpi_code = '"+key+"'");
					
					
					sqlList.add("INSERT INTO `vip_inspect_latest`(`inspect_code`,kpi_code,`inspect_name`,`inspect_time`,`inspect_value`,`card_code`,`create_time`,`inspect_is_normal`,inspect_desc) " +
							"VALUES ( '"+inspect_code+"','"+key+"','"+name+"','"+inspect_time+"','"+v+"','"+card_code+"',now(),'"+inspect_is_normal+"','"+desc+"')");
					
				}
				String sql = sqltitle+"INSPECT_IS_NORMAL)"+ sqldata+"'"+inspect_is_normal+"')";
				
				sqlList.add(sql);
				Db.batch(sqlList, sqlList.size());
				result.put("flag", "success");
				result.put("inspect_is_normal", inspect_is_normal);
				
			} catch (Exception e) {
				LOG.error( "数据解析异常",e);
				result.put("flag", "fail");
				result.put("remark", "数据解析异常");
			}
			
			
		}

		@Override
		public void querymeasuredatalist(JSONObject messageObj, JSONObject result) {
			// TODO Auto-generated method stub
			try {
				String  inspect_code = (String) messageObj.getString("inspect_code");
				String  card_code = (String) messageObj.getString("card_code");
				int  page = Integer.parseInt( messageObj.get("page")+"");
				
				String filed = "";
				
				InspectDic   inspectDic =  InspectConfigData.getInstance().getInspect(inspect_code);
				Map<String, InspectKpiConfig>   map = inspectDic.getInspectMap();
				for(String key  : map.keySet()){
					filed+=key+",";
				}
				String sql = "select "+filed.substring(0, filed.lastIndexOf(","))+" from  vip_inspect_data  where card_code = '"+card_code+"'  ORDER  BY  inspect_time DESC  limit "+0*page+","+5*page;
				List<Record>  li =Db.find(sql);
				JSONArray  A = JsonUtil.getJsonObjByjfinalList(li);
				
				result.put("flag", "success");
				
				result.put("array", A);
			
			} catch (Exception e) {
				// TODO: handle exception
				result.put("flag", "fail");
				result.put("remark", "异常");
			}
		}

		@Override
		public void recentmeasuredata(JSONObject messageObj, JSONObject result) {
			// TODO Auto-generated method stub
			try {
				String  card_code = (String) messageObj.getString("card_code");
				String sql = "SELECT * FROM  vip_inspect_latest WHERE  card_code = '"+card_code+"'  AND  inspect_time = " +
						"(SELECT  inspect_time  FROM  `vip_inspect_latest`  WHERE inspect_code in ('C01','C02') and  card_code = '"+card_code+"'  ORDER BY inspect_time DESC LIMIT 1)";
				List<Record>  li =Db.find(sql);
				JSONArray  A = JsonUtil.getJsonObjByjfinalList(li);
				
				result.put("flag", "success");
				
				result.put("array", A);

			} catch (Exception e) {
				// TODO: handle exception
				result.put("flag", "fail");
				result.put("remark", "异常");
			}
		}

		@Override
		public void savevipfamily(JSONObject messageObj, JSONObject result) {
			
			String vip_code = messageObj.getString("vip_code");			
			Object family_account = messageObj.get("family_account");
			Object family_pwd = messageObj.get("family_pwd");
			
			if(family_pwd!=null&&!"".equals(family_pwd))
			{
				family_pwd = MD5Util.MD5(family_pwd.toString(),"UTF-8").toLowerCase();
			}
			
			String sql=" SELECT * FROM  t_vip WHERE (card_code = ? or papers_num = ? or mobile=?)and login_password=? and isvalid='1' ";
			Record vipInfo = Db.findFirst(sql, new Object[]{family_account,family_account,family_account,family_pwd});
			if(vipInfo == null){
				result.put("code", "0");
				result.put("message", NO_REGISTER);
				return;
			}
			
			String  bind_card_code = vipInfo.getStr("card_code");

			Db.update("delete   from vip_family  where vip_code = '"+vip_code+"'  and family_account='"+bind_card_code+"'");
			
			Db.update("insert into vip_family (vip_code,family_account,family_pwd,create_time) "+
					" values(?,?,?,NOW())",
					new Object[]{vip_code,bind_card_code,family_pwd});
			
			String v = SysSingleton.getInstance().getValueByCode("SC10001");
			if("yes".equals(v)){
				String  _vip_code = vipInfo.getStr("vip_code");
				sql = "select *  from  t_vip WHERE vip_code = '"+vip_code+"'";
				Record r = Db.findFirst(sql);
				String _family_account = r.getStr("card_code");
				String _family_pwd =  r.getStr("login_password");
				Db.update("delete   from vip_family  where vip_code = '"+_vip_code+"'  and family_account='"+_family_account+"'");
				Db.update("insert into vip_family (vip_code,family_account,family_pwd,create_time) "+
						" values(?,?,?,NOW())",
						new Object[]{_vip_code,_family_account,_family_pwd});
			}
			
			result.put("code", "1");
			result.put("message", "成员绑定成功");
			
		}
		
		/**
		 * vip_code   当前账号vip_code
		 * vip_card   当前卡号 
		 * bind_account  接触绑定卡号 
		 * @param messageObj
		 * @param result
		 */
		public void  deletebind(JSONObject messageObj, JSONObject result){
			try {
				String vip_code = messageObj.getString("vip_code");	
				String vip_card = messageObj.getString("vip_card");	
				Object bind_account = messageObj.get("bind_account");
				Db.update("delete   from vip_family  where vip_code = '"+vip_code+"'  and family_account='"+bind_account+"'");
				
				Record r = Db.findFirst("SELECT * FROM  t_vip WHERE  card_code = '"+bind_account+"'");
				if(r!=null){
					String bind_vip_code = r.get("vip_code")+"";
					Db.update("delete   from vip_family  where vip_code = '"+bind_vip_code+"'  and family_account='"+vip_card+"'");
				}
				result.put("code", "1");
				result.put("message", "解绑成功");	
				
			}catch(Exception e)
			{
				e.printStackTrace();
				result.put("code", "0");
				result.put("message", "解绑失败！");
			}
		}

		@Override
		public void vipfamilylist(JSONObject messageObj, JSONObject result) {
			
			Object vip_code = messageObj.get("vip_code");	
			
			String qrySql = "select a.family_account,a.vip_code family_vip_code ,b.* "
					+ " from vip_family a,t_vip b "
					+ " where a.family_account = b.card_code and isvalid='1' and a.vip_code=? ";
			
			List<Record> vflist = Db.find(qrySql,vip_code);
			if(vflist==null || vflist.size()<1)
			{
				result.put("code", "0");
				return;
			}
			result.put("familys", JsonUtil.getJsonObjByjfinalList(vflist));
			result.put("code", "1");
		}

		@Override
		//creator,creatorrole,msgtype,title,content,recivercardcode,recivergroupid
		public void pushmessage(JSONObject messageObj, JSONObject result) {
//			try{
//				LOG.debug("推送消息请求：{}",messageObj);
//				Integer msgtype = (Integer) messageObj.getInt("msgtype");
//				String title = (String) messageObj.getString("title");
//				String content = (String) messageObj.getString("content");
////				String channelId = (String) messageObj.getString("channeid");
//				Integer creator = (Integer) messageObj.getInt("creator");
//				String creatorrole = (String) messageObj.getString("creatorrole");
//				
//				String recivercardcode = "";
//				if(StringUtils.isNotEmpty(messageObj.getString("recivercardcode"))){
//					recivercardcode =  messageObj.getString("recivercardcode");//推一个
//				}
//				Integer recivergroupid = null;
//				if(null != messageObj.get("recivergroupid")){
//					try{
//						recivergroupid = (Integer) messageObj.getInt("recivergroupid");//推一组
//					}catch(Exception e){
//						recivergroupid = null;
//					}
//				}
//				
//				if (null == msgtype || StringUtils.isEmpty(title) || StringUtils.isEmpty(content) || null == creator || StringUtils.isEmpty(creatorrole)||
//						(recivergroupid == null && StringUtils.isEmpty(recivercardcode))) {
//					result.put("success", false);
//					result.put("message", REQ_ERROR);
//					return;
//				} 
//				try { 
//					title = new String(title.getBytes("ISO8859-1"), Charset.forName("UTF-8"));
//					content = new String(content.getBytes("ISO8859-1"), Charset.forName("UTF-8")); 
//				} catch (UnsupportedEncodingException e) {
//					e.printStackTrace();
//				} 
//				boolean pushFlag = false;
//				Set<Long> vipIds = new HashSet<Long>();//已推送的用户群
//				//区分是单推还是多推，还有推Android消息还是微信，还是都推。
//				if(StringUtils.isNotEmpty(recivercardcode) && !recivercardcode.equals("null") && !recivercardcode.equals("\"null\"")){
//					LOG.debug("推送消息请求到单个用户：{}",recivercardcode);
//					Record pushMetaRecord = Db.findFirst("select id,android_tv_channel_id,WXOPENID from t_vip   where card_code = ?", recivercardcode);
//					String channeId = pushMetaRecord.getStr("ANDROID_TV_CHANNEL_ID");//android_tv_token_id
//					String wxopenid = pushMetaRecord.getStr("WXOPENID");
//					//android push
//					if(StringUtils.isNotEmpty(channeId)){
//						LOG.debug("推送消息请求到用户 ：{}",channeId);
//						String msgId = PushUtil.pushMsgToSingleDevice(channeId, title, content,msgtype);
//						if(StringUtils.isNotEmpty(msgId)){
//							vipIds.add(pushMetaRecord.getLong("ID"));
//							pushFlag = true; 
//						}
//					}
//					//wx push
//					if(StringUtils.isNotEmpty(wxopenid)){
//						if(wxopenid.endsWith("-")){
//							wxopenid = wxopenid.substring(0, wxopenid.length()-1);
//						}
//						LOG.debug("推送消息请求到用户:{}",wxopenid);
//						String str = PushUtil.pushMsgToWeiXin(wxopenid, content);
//						if(str.startsWith("true")){
//							pushFlag = true; 
//						}
//					}
//				}else if(null != recivergroupid){
//					LOG.debug("推送消息请求到用户组 ：{}",recivergroupid);
//					List<Record> pushMetaRecords = Db.find("select id,android_tv_channel_id,WXOPENID from t_vip   where vip_code in (select vip_code from doctor_vip where group_id = ?)", recivergroupid);
//					List<String> channerIds = new ArrayList<String>();
//					String openids = ",";
//					if(null != pushMetaRecords && pushMetaRecords.size()>0){
//						for(Record r : pushMetaRecords){
//							vipIds.add(r.getLong("ID"));
//							channerIds.add(r.getStr("ANDROID_TV_CHANNEL_ID"));
//							if(r.get("WXOPENID") != null && !StringUtils.isEmpty(r.get("WXOPENID").toString())){
//								String wxid = r.get("WXOPENID")+"";
//								if(wxid.endsWith("-")){
//									wxid = wxid.substring(0, wxid.length()-1);
//								}
//								openids += wxid;
//							}
//						}
//					}
//					//android push
//					if(channerIds !=null && channerIds.size()>0){
//						LOG.debug("推送消息请求到用户组 ：{}",Arrays.asList(channerIds));
//						Map<String, String>  msgIds = PushUtil.pushMsgToManyDevice(channerIds, title, content,msgtype);
//						if(msgIds != null && msgIds.size() > 0){
//							pushFlag = true;
//						}
//					}
//					
//					//wx push
//					//LOG.warn("待开发推送微信:");//msgtype TODO 2016.9.9
//					if(openids.startsWith(",")){
//						openids = openids.substring(1);
//					}
//					if(StringUtils.isNotEmpty(openids)){
//						LOG.debug("推送消息请求到用户:{}",openids);
//						String str = PushUtil.pushMsgToWeiXin(openids, content);
//						if(str.startsWith("true")){
//							pushFlag = true; 
//						}
//					}
//				}
//				
//				LOG.debug("推送消息结果 ：{}",pushFlag);
//				if(pushFlag){
//					boolean dbFlag = false;
//					if(vipIds != null && vipIds.size()>0){
//						//写入数据库
//						Date createTime = new Date();
//						Record message = new Record().set("msg_type",msgtype).set("creator", creator).set("title", title).set("content", content)
//								.set("isvalid", 1).set("create_time", createTime);
//						boolean flag = Db.save("message", "id", message);
//						LOG.debug("推送消息写入数据表message结果 ：{}",flag);
//						if(flag){
//							Long messageId = message.getLong("id");
//							//写入详情表
//							for(Long rId : vipIds){
//								Record rr = new Record().set("msg_type",msgtype).set("message_id", messageId).set("send_time", new Date())
//										.set("reciver", rId).set("create_time", createTime);
//								boolean cFlag = Db.save("message_center", "id", rr);
//								LOG.debug("推送消息写入数据表message_center结果 ：{}",cFlag);
//								if(cFlag){
//									dbFlag = true;//有推送成功有写入到数据库就成功
//								}
//								
//							}
//						}
//					} 
//					
//					if(dbFlag){
//						result.put("success", true);
//						result.put("message", OK);
//					}
//				} 
//			}catch(Exception e){
//				result.put("success", false);
//				result.put("message",ERROR);
//			}
//			
//			//如果不成功就返回失败
//			if(result.get("message")==null || StringUtils.isEmpty(result.getString("message"))){
//				result.put("success", false);
//				result.put("message",ERROR);
//			}
//			LOG.debug("推送消息最终结果 ：{}",result);
		}
		
		public void getsystemtime(JSONObject messageObj, JSONObject result) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd.HHmmss");
			result.put("code", "1");
			result.put("datetime", dateFormat.format(new Date()));
		}

		/**
		 *查询医院
		 *hosid 医院ID，“0”即所有
		 *rowstart 查询起始行（从1开始），“0”即所有／／分页时为查询行数
		 *rowcount 查询行数（具体返回行数由服务器决定），“0”即所有
		 *通过名字搜索
		 */
		public void hospitalalllist(JSONObject messageObj, JSONObject result) {
			List<Record> list = new ArrayList();
			String returnMessage="";
			String flag="";
			String url="gethospital";
			String hosid = (String) messageObj.getString("hosid");
			String rowstart = (String) messageObj.getString("rowstart");
			String rowcount = (String) messageObj.getString("rowcount");
			Object hosname = messageObj.get("hosname");
			
			if(messageObj.containsKey("flag")){
				flag = (String) messageObj.getString("flag");
			}
			
			if(flag!=""&&"0".equals(flag)){
				//查數據庫
				String sql="select * from gh_hospital_All where 1=1 ";
				if("0".equals(hosid)&&"0".equals(rowstart) && "0".equals(rowcount)){
					if(hosname!=null&&!"".equals(hosname.toString())){
						sql +=" and hosname like '%"+hosname+"%'";
					}
					list = Db.find(sql);
				}else {
					if(!"0".equals(hosid)){
						sql +=" and hosid = '"+hosid+"'";
					}
					if(hosname!=null&&!"".equals(hosname.toString())){
						sql +=" and hosname like '%"+hosname+"%'";
					}
					if(!"0".equals(rowcount)){
						int pageIndex=Integer.parseInt(rowstart)-1;
						int pageSize=Integer.parseInt(rowcount);
						sql +=" limit "+(pageIndex*pageSize)+","+pageSize;
					}
					list =Db.find(sql);
				}
				JSONObject json = new JSONObject();
				json.put("li", JsonUtil.getJsonObjByjfinalList(list).toString());
				json.put("msg", "");
				json.put("nowstp", DateUtil.getNowStp());
				json.put("ret", "0");
				json.put("rowcount", rowcount);
				json.put("rowstart", rowstart);
				json.put("rowtotalcount", list.size());
				returnMessage=json.toString();
				System.out.println(returnMessage);
			}
			else {
				String rquestUrl=HttpParamUtil.getUrl(hosid, rowstart, rowcount,url);
				try {
					 returnMessage=HttpClient.doGet(rquestUrl, "utf-8");
				} catch (Exception e) {
					result.put("success", false);
					result.put("message",ERROR);
					e.printStackTrace();
				}
			}
			result.put("success", true);
			result.put("code",1);
			result.put("message", returnMessage);
			
		}

		/**
		 * 查询科室
		 * *hosid 医院ID，
		 *rowstart 查询起始行（从1开始），“0”即所有
		 *rowcount 查询行数（具体返回行数由服务器决定），“0”即所有
		 */
		public void deptalllist(JSONObject messageObj, JSONObject result) {
			List<Record> list = new ArrayList();
			String returnMessage="";
			String url="getdept";
			String flag="";
			String hosid = (String) messageObj.getString("hosid");
			String rowstart = (String) messageObj.getString("rowstart");
			String rowcount = (String) messageObj.getString("rowcount");
			Object deptname = messageObj.get("deptname");
			if(messageObj.containsKey("flag")){
				flag = (String) messageObj.getString("flag");
			}
				
			if(flag!=""&&"0".equals(flag)){
				//查數據庫
				String sql="select * from gh_dept_All where 1=1 ";
				if("0".equals(hosid)&&"0".equals(rowstart) && "0".equals(rowcount)){
					if(deptname!=null&&!"".equals(deptname.toString())){
						sql +=" and deptname like '%"+deptname+"%'";
					}
					list = Db.find(sql);
				}else {
					if(!"0".equals(hosid)){
						sql +=" and hostid = '"+hosid+"'";
					}
					if(deptname!=null&&!"".equals(deptname.toString())){
						sql +=" and deptname like '%"+deptname+"%'";
					}
					if(!"0".equals(rowcount)){
						int pageIndex=Integer.parseInt(rowstart)-1;
						int pageSize=Integer.parseInt(rowcount);
						sql +=" limit "+(pageIndex*pageSize)+","+pageSize;
					}
					list =Db.find(sql);
				}
				JSONObject json = new JSONObject();
				json.put("li", JsonUtil.getJsonObjByjfinalList(list).toString());
				json.put("msg", "");
				json.put("nowstp", DateUtil.getNowStp());
				json.put("ret", "0");
				json.put("rowcount", rowcount);
				json.put("rowstart", rowstart);
				json.put("rowtotalcount", list.size());
				returnMessage=json.toString();
			}
			else {
				try {
					String rquestUrl=HttpParamUtil.getUrl(hosid, rowstart, rowcount,url);
					returnMessage=HttpClient.doGet(rquestUrl, "utf-8");
				} catch (Exception e) {
					result.put("success", false);
					result.put("message",ERROR);
					e.printStackTrace();
				}
			}
			result.put("success", true);
			result.put("code",1);
			result.put("message", returnMessage);
		}

		/**
		 * 查询医生
		 * deptid 部门id
		 * *hosid 医院ID，“0”即所有
		 *rowstart 查询起始行（从1开始），“0”即所有
		 *rowcount 查询行数（具体返回行数由服务器决定），“0”即所有
		 */
		public void doctor(JSONObject messageObj, JSONObject result) {
			String url="Ghgetdoctor";
			List<Record> list = new ArrayList();
			String returnMessage="";
			String flag="";
			
			String hosid = (String) messageObj.getString("hosid");
			String rowstart = (String) messageObj.getString("rowstart");
			String rowcount = (String) messageObj.getString("rowcount");
			String deptid = (String) messageObj.getString("deptid");
			Object docname = messageObj.get("docname");
			if(messageObj.containsKey("flag")){
				flag = (String) messageObj.getString("flag");
			}
			if(flag!=""&&"0".equals(flag)){
				//查數據庫
				String sql="select * from gh_doctor_All where 1=1 ";
				if("0".equals(hosid)&&"0".equals(rowstart) && "0".equals(rowcount)&&"0".equals(deptid)){
					if(docname!=null&&!"".equals(docname.toString())){
						sql +=" and docname like '%"+docname+"%'";
					}
					list = Db.find(sql);
				}else {
					if(!"0".equals(hosid)){
						sql +=" and hostid = '"+hosid+"'";
					}
					if(docname!=null&&!"".equals(docname.toString())){
						sql +=" and docname like '%"+docname+"%'";
					}
					if(!"0".equals(deptid)){
						sql +=" and deptid = '"+deptid+"'";
					}
					if(!"0".equals(rowcount)){
						int pageIndex=Integer.parseInt(rowstart)-1;
						int pageSize=Integer.parseInt(rowcount);
						sql +=" limit "+(pageIndex*pageSize)+","+pageSize;
					}
					
					list =Db.find(sql);
				}
				JSONObject json = new JSONObject();
				json.put("li", JsonUtil.getJsonObjByjfinalList(list).toString());
				json.put("msg", "");
				json.put("nowstp", DateUtil.getNowStp());
				json.put("ret", "0");
				json.put("rowcount", rowcount);
				json.put("rowstart", rowstart);
				json.put("rowtotalcount", list.size());
				returnMessage=json.toString();
			}
			else{
				String rquestUrl=HttpParamUtil.getUrlForDep(deptid,hosid, rowstart, rowcount,url);
				try {
					returnMessage=HttpClient.doGet(rquestUrl, "utf-8");
				} catch (Exception e) {
					result.put("success", false);
					result.put("message",ERROR);
					e.printStackTrace();
				}
			}
			result.put("success", true);
			result.put("code",1);
			result.put("message", returnMessage);
			
			
		
		}

		
		/**
		 * 查询排班
		 */
		public void doctorschedule(JSONObject messageObj, JSONObject result) {
			String url="getDoctorSchedule";
			String hosid = (String) messageObj.getString("hosid");
			String rowstart = (String) messageObj.getString("rowstart");
			String rowcount = (String) messageObj.getString("rowcount");
			String deptid = (String) messageObj.getString("deptid");
			String docid=(String) messageObj.getString("docid");
			
			String rquestUrl=HttpParamUtil.getUrlForDoc(deptid,docid,hosid, rowstart, rowcount,url);
			try {
				String returnMessage=HttpClient.doGet(rquestUrl, "utf-8");
				
				result.put("success", true);
				result.put("code",1);
				result.put("message", returnMessage);
			} catch (Exception e) {
				result.put("success", false);
				result.put("message",ERROR);
				e.printStackTrace();
			}
			
			
		}

		/**
		 * 查询分时排班
		 */
		public void parttime(JSONObject messageObj, JSONObject result) {
			String url="GetPartTime";
			String doctorno = (String) messageObj.getString("doctorno");
			String rowstart = (String) messageObj.getString("rowstart");
			String rowcount = (String) messageObj.getString("rowcount");
			String scheduleid= (String) messageObj.getString("scheduleid");
			String hosid= (String) messageObj.getString("hosid");
			
			String rquestUrl=HttpParamUtil.getUrlForPart(doctorno,hosid,scheduleid, rowstart, rowcount,url);
			try {
				String returnMessage=HttpClient.doGet(rquestUrl, "utf-8");
				
				result.put("success", true);
				result.put("code",1);
				result.put("message", returnMessage);
			} catch (Exception e) {
				result.put("success", false);
				result.put("message",ERROR);
				e.printStackTrace();
			}
			
			
		}

		/**
		 * 锁定号源
		 */
		public void ghlock(JSONObject messageObj, JSONObject result) {
			String url="ghlock";
			String returnMessage="";
			String newidcard="";
			String newpatientname="";
			String newcontactphone="";
			String newfamilyaddress="";
			
			String hosid= (String) messageObj.getString("hosid");
			String vipcode= (String) messageObj.getString("vipcode");
			String docid= (String) messageObj.getString("docid");
			String outpdate= (String) messageObj.getString("outpdate");
			String depId= (String) messageObj.getString("deptid");
			String scheduleid= (String) messageObj.getString("scheduleid");
			String partscheduleid= (String) messageObj.getString("partscheduleid");
			
			
			String certtypeno=(String) messageObj.getString("certtypeno");//证件类型
			String idcard=(String) messageObj.getString("idcard");//证件号 加密
			String patientname=(String) messageObj.getString("patientname");//病人姓名  加密
			String patientsex=(String) messageObj.getString("patientsex");//病人性别（1：男；2：女）
			String patientbirthday=(String) messageObj.getString("patientbirthday");//病人出生日期，格式：2015-01-01
			String contactphone=(String) messageObj.getString("contactphone");//联系电话 加密
			String familyaddress=(String) messageObj.getString("familyaddress");//家庭地址 加密
			String familyaddressInsert=(String) messageObj.getString("familyaddress");//家庭地址 加密
			
			
			
			
			try {
				familyaddress=new String(familyaddress.getBytes("ISO-8859-1"),"utf-8");
				patientname=new String(patientname.getBytes("ISO-8859-1"),"utf-8");
				
				
				newidcard=HttpParamUtil.getCiphByString(idcard);
				newpatientname=HttpParamUtil.getCiphByString(patientname);
				newcontactphone=HttpParamUtil.getCiphByString(contactphone);
				newfamilyaddress=HttpParamUtil.getCiphByString(familyaddress);
				
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
			JSONObject json  = new JSONObject();
			json.put("certtypeno", certtypeno);
			json.put("idcard", newidcard);
			json.put("patientname", newpatientname);
			json.put("patientsex", patientsex);
			json.put("patientbirthday", patientbirthday);
			json.put("contactphone", newcontactphone);
			json.put("familyaddress",newfamilyaddress);
			//传入map
//			JSONObject json = JSONObject.fromObject(map);
			System.out.println("json参数"+json.toString());
			String newjson=StringEscapeUtils.unescapeJava(json.toString());
			
			System.out.println("newjson参数"+newjson);
			
			String rquestUrl=HttpParamUtil.getUrlForghlock(hosid,partscheduleid,scheduleid,url);
			try {
				returnMessage=HttpClient.postForJson(rquestUrl, newjson);
				JSONObject retObj = JSONObject.fromObject(returnMessage);
				if(retObj.get("ret")!=null&&"0".equals(retObj.get("ret").toString())){
//					"ispay": "1",
					String ispay=retObj.getString("ispay");
//					  "msg": "锁号成功",
					String msg=retObj.getString("msg");
//					  "nowstp": "20160406111506",
					String nowstp=retObj.getString("nowstp");
//					  "orderfee": "6.50",
					String orderfee=retObj.getString("orderfee");
//					  "orderid": "14599125062335f5a5d1b-c97a-451a-be72-d29e203e80d1",
					String orderid=retObj.getString("orderid");
//					  "ordertime": "2016-04-06 11:15:59",
					String ordertime=retObj.getString("ordertime");
//					  "ret": 0
					String ret=retObj.getString("ret");
					result.put("success", true);
					result.put("code",1);
					result.put("message", returnMessage);
				}else{
					result.put("success", false);
					result.put("message",returnMessage);
					return;
				}
			} catch (Exception e) {
				result.put("success", false);
				result.put("message",ERROR);
				e.printStackTrace();
			}
			
				Map map =JsonUtil.getMap4Json(returnMessage);
				String ispay=MapUitl.getMapBykey(map, "ispay");
				String orderfee=MapUitl.getMapBykey(map, "orderfee");
				if("".equals(orderfee)){
					orderfee="0";
				}
				String status="1";//1  锁号成功  2 确认成功  3 支付成功 4 支付失败 5 取消订单
//				是否需要扣费（1:是;0:否）
//				if("0".equals(ispay)){
//					status="3";
//				}
				String orderid=MapUitl.getMapBykey(map, "orderid");
				String ordertime=MapUitl.getMapBykey(map, "ordertime");
				String msg=MapUitl.getMapBykey(map, "msg");
//				docid
//				Record docRedord=Db.findFirst("select * from gh_doctor_All where docid=? ",docid);
//				if(docRedord!=null){
//					depId=docRedord.getStr("deptid");
//				}
//				,'"+idcard+"'
				String sql="insert into vip_reg  (orderid,orderfee,status,certtypeno," +
						"idcard,patientname,patientsex,patientbirthday,contactphone,familyaddress,hosid,scheduleid,partscheduleid,msg,vip_code,deptid,docid,outpdate,ordertime,create_time) VALUES (" +
						" '"+orderid+"','"+orderfee+"','"+status+"','"+certtypeno+"' ,'"+idcard+"','"+messageObj.getString("patientname")+"'," +
								"'"+patientsex+"','"+patientbirthday+"','"+contactphone+"','"+familyaddressInsert+"','"+hosid+"','"+scheduleid+"','"+partscheduleid+"','"+msg+"','"+vipcode+"','"+depId+"','"+docid+"','"+outpdate+"','"+ordertime+"',NOW() )";
				Db.update(sql);
			
		}

		/**
		 * 5.1.7 	确认订单
		 */
		public void confirmorder(JSONObject messageObj, JSONObject result) {
			String url="confirmOrder";
			String orderid= (String) messageObj.getString("orderid");
			
			String rquestUrl=HttpParamUtil.getUrlFororder(orderid,url);
			try {
				String returnMessage=HttpClient.doGet(rquestUrl, "utf-8");
				returnMessage.replace("?","");
				JSONObject retObj = JSONObject.fromObject(returnMessage);
				if(retObj.get("ret")!=null&&"0".equals(retObj.get("ret"))){
//					outpdate
					Object outpdate=retObj.get("outpdate");
					if(outpdate!=null&&!"".equals(outpdate.toString())){
						String outpdateInt=retObj.getString("outpdate");
						String serial=retObj.getString("serial");
						Db.update("update vip_reg set outpdate=? ,create_time=NOW(),status='2',serial=? where orderid=?",outpdateInt,serial,orderid);
//						Db.update("update vip_reg set status=’4‘ where orderid=?",orderid);
					}
					
				}
				result.put("success", true);
				result.put("code",1);
				result.put("message", returnMessage);
			} catch (Exception e) {
				result.put("success", false);
				result.put("message",ERROR);
				e.printStackTrace();
			}
		}

		/**
		 * 取消订单
		 */
		public void cancelorder(JSONObject messageObj, JSONObject result) {
			String url="cancelorder";
			
			String cancelreason= (String) messageObj.getString("cancelreason");
			String operator= (String) messageObj.getString("operator");
			String orderid= (String) messageObj.getString("orderid");
			
			String rquestUrl=HttpParamUtil.getUrlForCancle("111",operator,orderid,url);
			try {
				String returnMessage=HttpClient.doGet(rquestUrl, "utf-8");
				Db.update("update vip_reg set status='5',canceltime=NOW(),cancelreason=? where orderid=?",cancelreason,orderid);
				result.put("success", true);
				result.put("code",1);
				result.put("message", returnMessage);
			} catch (Exception e) {
				result.put("success", false);
				result.put("message",ERROR);
				e.printStackTrace();
			}
			
			
			
		}

		
		public void doctorstop(JSONObject messageObj, JSONObject result) {
			String url="getDoctorstop";
			
			String bstp= (String) messageObj.getString("bstp");
			String estp= (String) messageObj.getString("estp");
			String hosid= (String) messageObj.getString("hosid");
			String rowcount=  (String) messageObj.getString("rowcount");
			String rowstart=  (String) messageObj.getString("rowstart");
			
			String rquestUrl=HttpParamUtil.getUrlForCancle(bstp,estp,hosid,rowcount,rowstart,url);
			try {
				String returnMessage=HttpClient.doGet(rquestUrl, "utf-8");
				
				result.put("success", true);
				result.put("code",1);
				result.put("message", returnMessage);
			} catch (Exception e) {
				result.put("success", false);
				result.put("message",ERROR);
				e.printStackTrace();
			}
			
		}
		public void getappversion(JSONObject messageObj, JSONObject result) {
			
			try {
				String version_type = messageObj.get("version_type")+"";
				String sql = "SELECT version_code,version_url   FROM t_version WHERE isvalid = 'yes' ";
				if(!PubMethod.isEmpty(version_type)){
					sql += " AND version_type='"+version_type+"' ";
				}else{
					sql += " AND version_type='hezi' ";
				}
				sql += "  ORDER BY create_time DESC  ";
				Record appRecord = Db.findFirst(sql);
				if(appRecord!=null)
				{
					result.put("version_code", appRecord.get("version_code")+"");
					result.put("version_url", appRecord.get("version_url")+"");
				}
				
				
				result.put("success", true);
				result.put("code","1");
			} catch (Exception e) {
				result.put("success", false);
				result.put("code","1");
				e.printStackTrace();
			}
			
		}
		/**
		 * 挂号订单查询
		 * 
		 */
		public void getghorderlst(JSONObject messageObj, JSONObject result) {
			
			Object orderId= messageObj.get("orderId");
			Object status= messageObj.get("status");
			Object hosid= messageObj.get("hosid");
			Object vipcode=  messageObj.get("vipcode");
			Object docid=  messageObj.get("docid");
			Object depId=  messageObj.get("deptid");
			Object patientname=  messageObj.get("patientname");
			Object pageIndex = messageObj.get("pageIndex");
			Object pageSize = messageObj.get("pageSize");
			
			String qrySql = "select t.*,(select hosname from `gh_hospital_All` where hosid=t.`hosid`) hosname,(select deptname from `gh_dept_All` where deptid=t.`deptid`) deptname,(select docname from `gh_doctor_All` where docid=t.docid) docname from vip_reg t where 1=1 ";
			String conSql = "";		
			
			if(hosid!=null && !"".equals(hosid.toString()))
			{
				conSql+= " and hosid = '"+hosid.toString()+"' ";
			}
			if(orderId!=null && !"".equals(orderId.toString()))
			{
				conSql+= " and orderId = '"+orderId.toString()+"' ";
			}
			if(status!=null && !"".equals(status.toString()))
			{
				conSql+= " and status = '"+status.toString()+"' ";
			}
			if(vipcode!=null && !"".equals(vipcode.toString()))
			{
				conSql+= " and vip_code = '"+vipcode.toString()+"' ";
			}
			if(docid!=null && !"".equals(docid.toString()))
			{
				conSql+= " and docid = '"+docid.toString()+"' ";
			}
			if(depId!=null && !"".equals(depId.toString()))
			{
				conSql+= " and deptid = '"+depId.toString()+"' ";
			}
			if(patientname!=null && !"".equals(patientname.toString()))
			{
				conSql+= " and patientname = '"+patientname.toString()+"' ";
			}
			conSql+=" order by id desc ";
			Integer pageIndexInt=0;
			Integer pageSizeInt=0;
			if(pageIndex!= null && !"".equals(pageIndex.toString()))
			{
				conSql+=" limit ?,? ";
				pageIndexInt=(Integer.valueOf(pageIndex.toString())-1)*Integer.valueOf(pageSize.toString());
				pageSizeInt=(Integer.parseInt(pageSize.toString()));
			}		
			qrySql = qrySql+conSql;
			List<Record> orderList = Db.find(qrySql,pageIndexInt.intValue(),pageSizeInt.intValue());
			if(orderList==null || orderList.size()<1)
			{
				result.put("code", "0");
				return;
			}
			for(Record record:orderList){
				Date dd=record.getDate("create_time");
				DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String ddd=df1.format(dd);
				System.out.println(ddd);
			}
			result.put("orders", JsonUtil.getJsonObjByjfinalList(orderList));
			result.put("code", "1");
		}
		public void getwxpreorder(JSONObject messageObj, JSONObject result) {
			
			Object orderid= messageObj.get("orderid");
			if(orderid==null || "".equals(orderid.toString()))
			{
				result.put("code", "1");
				result.put("message", "orderId不能为空");
				return;
			}
			TopayServlet.getPackage(orderid.toString(),result);
		}

		@Override
		public void doctorlogin(JSONObject messageObj, JSONObject result) {
			// TODO Auto-generated method stub
			Object tel=messageObj.get("tel");//手机号
			String password=messageObj.getString("password");//密码
			Object android_tv_channel_id=messageObj.get("android_tv_channel_id");//证件号
			
			if((tel==null||"".equals(tel.toString())))
			{
				result.put("code", "0");
				result.put("message", LOGIN_LACK);
				return;
			}
			
			
			String sql=" SELECT * FROM  doctor WHERE isvalid='1' and tel=?";
			
			List<Object> paraList = new ArrayList<Object>();
			paraList.add(tel);
			
			Record doctorInfo = Db.findFirst(sql, paraList.toArray());
			if(doctorInfo == null){
				result.put("code", "0");
				result.put("message", NO_REGISTER);
				return;
			}
			
			password = MD5Util.MD5(password.toString(),"UTF-8").toLowerCase();
			String _password = doctorInfo.getStr("password").toLowerCase();
			if(!_password.equals(password)){
				result.put("code", "0");
				result.put("message", LOGIN_FAIL_PASSWORD);
				return;
			}
			result.put("doctor_info",JsonUtil.getMapByJfinalRecord(doctorInfo));
			
			if(android_tv_channel_id!=null && !"".equals(android_tv_channel_id.toString()))
			{
				doctorInfo.set("android_tv_channel_id", android_tv_channel_id);
				Db.update("t_vip", doctorInfo);
			}
			
			result.put("code", "1");
			result.put("message", LOGIN_SUCCESS);
		}

		@Override
		public void doctorremotelist(JSONObject messageObj, JSONObject result) {
			// TODO Auto-generated method stub
			String doctor_code=messageObj.getString("doctor_code");
			String sql = "SELECT m.*,n.`real_name`,n.card_code,n.`mobile` FROM (SELECT * FROM  remote_inspect WHERE  doctor_code = '"+doctor_code+"') m LEFT JOIN `t_vip` n  ON m.vip_code = n.`vip_code`  ORDER BY order_time DESC ";
			List<Record> list = Db.find(sql);
			if(list==null || list.size()<1)
			{
				result.put("code", "0");
				return;
			}
			result.put("remotelist", JsonUtil.getJsonObjByjfinalList(list));
			result.put("code", "1");
			
		}

		@Override
		public void doctorremotewychannel(JSONObject messageObj, JSONObject result) {
			// TODO Auto-generated method stub
			String remote_inspect_id=messageObj.getString("remote_inspect_id");
			String channel_id=messageObj.getString("channel_id");
			
			String sql = "INSERT INTO `remote_inspect_wy_vedio`(`remote_inspect_id`,`channel_id`,`create_time`) VALUES ('"+remote_inspect_id+"','"+channel_id+"',now())";
			Db.update(sql);
			
			//push 消息给盒子   消息内容 包含     视频+channel_id        盒子收到消息后 弹出视频接收程序 默认 channel_id
			//查询远程视频对应客户的 android_tv_channel_id  进行push
			sql= "SELECT android_tv_channel_id FROM t_vip WHERE vip_code = (SELECT  vip_code    FROM  `remote_inspect`   WHERE id = '"+remote_inspect_id+"')";
			Record r = Db.findFirst(sql);
			if(r!=null){
				String android_tv_channel_id = r.getStr("android_tv_channel_id");
				//PUSH
				String msgId = PushUtil.pushMsgToSingleDevice(android_tv_channel_id, "vedio", channel_id,1);
			}
			
			
			result.put("code", "1");
			result.put("message", LOGIN_SUCCESS);
		}
		
		
		public void doctorexitvedio(JSONObject messageObj, JSONObject result) {
			
			String remote_inspect_id=messageObj.getString("remote_inspect_id");
			String channel_id=messageObj.getString("channel_id");
			
			String sql = "UPDATE `remote_inspect_wy_vedio`  SET state = 'finish' WHERE remote_inspect_id = '"+remote_inspect_id+"' AND channel_id = '"+channel_id+"' ";
			Db.update(sql);
			
			
			Db.update(" update remote_inspect set isDeal=1 where id = ? ",new Object[]{remote_inspect_id});

			result.put("code", "1");
			result.put("message", LOGIN_SUCCESS);
			
		}
		
		@Override
		public void getvediotoken(JSONObject messageObj, JSONObject result) {
			
			String uidStr = messageObj.getString("uid");
			Long uid = null;
			try {
				uid = Long.valueOf(uidStr);
			} catch (Exception e) {
				e.printStackTrace();
				result.put("code",0);
				result.put("msg", "uid必须是Long类型");
				return;
			} 
			
			
			DefaultHttpClient client = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(ServiceConstants.TOKEN_SERVER);
			
			String nonce = new Random().nextInt(999999) + "";
			String curTime = String.valueOf((new Date()).getTime() / 1000L);
			String checkSum = CheckSumBuilder.getCheckSum(ServiceConstants.APP_SECRET, nonce, curTime);
			
			httpPost.addHeader("AppKey", ServiceConstants.APP_KEY);
			httpPost.addHeader("Nonce", nonce);
			httpPost.addHeader("CurTime", curTime);
			httpPost.addHeader("CheckSum", checkSum);
			httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
			
			StringEntity stringEntity = new StringEntity("uid=" + uid,Consts.UTF_8);
			httpPost.setEntity(stringEntity);
			
			
			try {
				HttpResponse response = client.execute(httpPost);
				if (response != null && response.getEntity() != null) {
					JSONObject resultJson = JSONObject.fromObject(EntityUtils.toString(response.getEntity(), "utf-8"));
					if ("200".equals(resultJson.getString("code"))) {
						result.put("code", 1);
						result.put("token", resultJson.getString("token"));
						return;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			result.put("code", 0);
			result.put("msg", "token获取失败");
		}

		@Override
		public void makechannelname(JSONObject messageObj, JSONObject result) {
			
			String tel = messageObj.getString("tel");
			String remote_inspect_id = messageObj.getString("remote_inspect_id");
			String dotorName = messageObj.getString("dotorName");
			
			
			DefaultHttpClient client = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(ServiceConstants.CREATE_SERVER);
			
			String nonce = new Random().nextInt(999999) + "";
			String curTime = String.valueOf((new Date()).getTime() / 1000L);
			String checkSum = CheckSumBuilder.getCheckSum(ServiceConstants.APP_SECRET, nonce, curTime);
			
			httpPost.addHeader("AppKey", ServiceConstants.APP_KEY);
			httpPost.addHeader("Nonce", nonce);
			httpPost.addHeader("CurTime", curTime);
			httpPost.addHeader("CheckSum", checkSum);
			httpPost.addHeader("Content-Type", "application/json;charset=utf-8");
			
			//房间号
			String channelname = tel+nonce;
			
			StringEntity stringEntity = new StringEntity("{\"name\":\""+channelname+"\",\"type\":0}",Consts.UTF_8);
			httpPost.setEntity(stringEntity);
			
			try {
				HttpResponse response = client.execute(httpPost);
				if (response != null && response.getEntity() != null) {
					
					JSONObject resultJson = JSONObject.fromObject(EntityUtils.toString(response.getEntity(), "utf-8"));
					
					if ("200".equals(resultJson.getString("code"))) {
						JSONObject retJson = resultJson.getJSONObject("ret");
						String pushUrl = retJson.getString("pushUrl");
						String cid = retJson.getString("cid");
						
						String sql = "INSERT INTO `remote_inspect_wy_vedio`(`remote_inspect_id`,`channel_id`,`create_time`) VALUES ('"+remote_inspect_id+"','"+channelname+"',now())";
						Db.update(sql);
						
						//push 消息给盒子   消息内容 包含     视频+channel_id        盒子收到消息后 弹出视频接收程序 默认 channel_id
						//查询远程视频对应客户的 android_tv_channel_id  进行push
						sql= "SELECT android_tv_channel_id FROM t_vip WHERE vip_code = (SELECT  vip_code    FROM  `remote_inspect`   WHERE id = '"+remote_inspect_id+"')";
						Record r = Db.findFirst(sql);
						if(r!=null){
							String android_tv_channel_id = r.getStr("android_tv_channel_id");
							//PUSH 房间号 咨询id 医生名字
							PushUtil.pushMsgToSingleDevice(android_tv_channel_id, "vedio", channelname + "," + remote_inspect_id + "," + dotorName,1);
						}
						result.put("code", 1);
						result.put("channelname", channelname);
						result.put("pushUrl", pushUrl);
						result.put("cid", cid);
						return;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			result.put("code", "0");
		}

		@Override
		public void deletechannelname(JSONObject messageObj, JSONObject result) {
			String cid = messageObj.getString("cid");
			if (cid == null || "".equals(cid)) {
				result.put("code",0);
				result.put("msg", "cid不能为空");
				return;
			}
			
			DefaultHttpClient client = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(ServiceConstants.DELETE_SERVER);
			
			String nonce = new Random().nextInt(999999) + "";
			String curTime = String.valueOf((new Date()).getTime() / 1000L);
			String checkSum = CheckSumBuilder.getCheckSum(ServiceConstants.APP_SECRET, nonce, curTime);
			
			httpPost.addHeader("AppKey", ServiceConstants.APP_KEY);
			httpPost.addHeader("Nonce", nonce);
			httpPost.addHeader("CurTime", curTime);
			httpPost.addHeader("CheckSum", checkSum);
			httpPost.addHeader("Content-Type", "application/json;charset=utf-8");
			
			StringEntity stringEntity = new StringEntity("{\"cid\":\"" + cid + "\"}",Consts.UTF_8);
			httpPost.setEntity(stringEntity);
			
			
			try {
				HttpResponse response = client.execute(httpPost);
				if (response != null && response.getEntity() != null) {
					JSONObject resultJson = JSONObject.fromObject(EntityUtils.toString(response.getEntity(), "utf-8"));
					if ("200".equals(resultJson.getString("code"))) {
						result.put("code", 1);
						result.put("token", "删除成功");
						return;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			result.put("code", 0);
			result.put("msg", "删除失败");
		}
		
		
}
