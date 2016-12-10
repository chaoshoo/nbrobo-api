package com.net.nky.garea.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.net.nky.garea.req.BaseReq;
import com.net.nky.garea.rsp.Doctor;
import com.net.nky.garea.rsp.DoctorResutDto;
import com.net.nky.garea.rsp.ResultDto;
import com.net.nky.garea.util.EcgUtil;
import com.net.nky.garea.vo.Bmi;
import com.net.nky.garea.vo.C01;
import com.net.nky.garea.vo.C02;
import com.net.nky.garea.vo.C05;
import com.net.nky.garea.vo.C06;
import com.net.nky.garea.vo.Ecg;
import com.net.nky.garea.vo.Temperature;
import com.net.nky.seq.SysId;
import com.net.nky.service.AppInterfaceService;
import com.net.util.MD5Util;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

import net.sf.json.JSONObject;

/**
 * 盖睿服务类.
 * @author Ken
 * @version 2016年9月20日
 */
@Component
@Path("/api")
public class GareaService {

	@Autowired
	protected AppInterfaceService appInterfaceService;

	/**
	 * 3.1.1 设备上传保存个人或者医生
	 * @param appCode 系统代码
	 * @param signData 签名数据
	 * @param sign	签名信息
	 * 
	 * @param custCode 企业客户编码 4 位字符
	 * @param deviceId  设备编号 50 位以内字符
	 * @param userType  用户类型 0：居民用户 1：医生
	 * @param pIdCard		居民用户身份证号  创建医生时可为空		允许为空
	 * @param dIdCard   医生身份证号
	 * @param name 创建信息对应的姓名
	 * @param address 身份证上住址
	 * @param nativeplace	身份证上民族
	 * @param phone	手机号码
	 * @param inputStream	身份证头像
	 * @param imageDetail
	 * @return
	 * @throws Exception
	 */
	@POST
	@Path("/em/info/create")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces({ MediaType.APPLICATION_JSON + ";charset=utf-8" })
	public ResultDto createInfo(@FormDataParam("appCode") String appCode, @FormDataParam("signData") String signData,
			@FormDataParam("sign") String sign, @FormDataParam("custCode") String custCode,
			@FormDataParam("deviceId") String deviceId, @FormDataParam("userType") String userType,
			@FormDataParam("pIdCard") String pIdCard, @FormDataParam("dIdCard") String dIdCard,
			@FormDataParam("name") String name, @FormDataParam("address") String address,
			@FormDataParam("nativeplace") String nativeplace, @FormDataParam("phone") String phone,
			@FormDataParam("file") InputStream inputStream,
			@FormDataParam("file") FormDataContentDisposition imageDetail) throws Exception {
		BaseReq req = new BaseReq(appCode, custCode, pIdCard, dIdCard, deviceId, signData,sign) ;
		Log.info("rsq [info]: name:{}, basereq :{}", name,req);//pid 421125199210164314, did 420106197702160011
		try{
			if (StringUtils.isEmpty(name) || StringUtils.isEmpty(deviceId) || (StringUtils.isEmpty(pIdCard) && StringUtils.isEmpty(dIdCard))) {
				Log.info("rsq [info]: {},{} 数据不合法，不保存", name, req);
				return new ResultDto(false);
			}
			
			checkInsertDevice(deviceId);//保存一体机设备

			//居民用户身份证
			if (StringUtils.isNotEmpty(pIdCard)) {
				Record vipIds = Db.findFirst("select id from t_vip where  papers_num=? ", new Object[] { pIdCard });
				if (vipIds != null) {
					return new ResultDto();//用户已经注册 , true
				}
				String headPicUrl = pIdCard + ".png";
				if (StringUtils.isEmpty(localFilePath)
						|| !inputStreamToPng(inputStream, new File(localFilePath + headPicUrl))) {
					headPicUrl = "";
				}
				int count = Db.update(
						"insert into t_vip (vip_code,card_code,login_account,mobile,login_password,heard_img_url,isvalid,"
								+ "papers_num,nick_name,real_name,sex,area,address,birthday,age,phone,create_time) "
								+ " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,NOW())",
						new Object[] { SysId.getInstance().getNextVipCode(), pIdCard,
								(StringUtils.isEmpty(phone)) ? pIdCard : phone, phone,
								MD5Util.MD5("123456", "UTF-8").toLowerCase(), headPicUrl, 1, pIdCard, name, name,
								getIdCardSex(pIdCard), address, address, getIdCardBirthDay(pIdCard), getIdCardAge(pIdCard),
								phone });
				if (count > 0) {
					Log.info("rsq [info]: {},{} 保存成功", name, req);
					return new ResultDto();//用户已经注册 , true
				}
			} else {
				//doctor ,现在不需要搞
			}
		}catch(Exception e){
			Log.error("一体机存入居民信息异常",e);
		}
		Log.warn("rsq [info]: {},{} 保存失败", name, req);
		return new ResultDto(false);//用户已经注册 , true 
	}

	/**
	 * 3.2.1  体重指数接口
	 */
	@POST
	@Path("/dt/bmi/create")
	@Produces({ MediaType.APPLICATION_JSON + ";charset=utf-8" })
	public ResultDto bmi(@FormParam("appCode") String appCode, @FormParam("signData") String signData,
			@FormParam("sign") String sign, @FormParam("custCode") String custCode,
			@FormParam("deviceId") String deviceId, @FormParam("bmi") String bmiStr,
			@FormParam("pIdCard") String pIdCard, @FormParam("dIdCard") String dIdCard) throws Exception {
		ResultDto rsp = new ResultDto();
		BaseReq req = new BaseReq(appCode, custCode, pIdCard, dIdCard, deviceId, signData,sign) ;
		Log.info("rsq [bmi]: bmi:{}, basereq :{}", bmiStr,req);//pid 421125199210164314, did 420106197702160011
		try{
			//checkInsertDevice(deviceId);//保存一体机设备
			
			JSONObject jsonObject = JSONObject.fromObject(bmiStr);     
			Bmi bmi = (Bmi) JSONObject.toBean(jsonObject, Bmi.class);
			JSONObject messageObj = new JSONObject();
			messageObj.put("inspect_code", "C03");
			messageObj.put("type", "measure");
			messageObj.put("WEIGHT", new BigDecimal(bmi.getWeight()).setScale(0, BigDecimal.ROUND_HALF_UP));
			messageObj.put("HEIGHT", new BigDecimal(bmi.getHeight()).setScale(0, BigDecimal.ROUND_HALF_UP));
			messageObj.put("BMI",  new BigDecimal(bmi.getWeight()*10000/bmi.getHeight()/bmi.getHeight()).setScale(2, BigDecimal.ROUND_HALF_UP));
			messageObj.put("device_sn",deviceId);
			messageObj.put("card_code", pIdCard);
			messageObj.put("inspect_time", bmi.getDetectDate());
			JSONObject result = new JSONObject();
			appInterfaceService.measure(messageObj, result);
			//体质指数（BMI）=体重（kg）÷身高^2（m） EX：70kg÷（1.75×1.75）=22.86
			Log.info("一体机存入BMI信息结果：{}",result);
			if(!(result != null && result.getString("flag") != null &&  result.getString("flag").equals("success"))){
				rsp.setSuccess(false);
			}
		}catch(Exception e){
			Log.error("一体机存入BMI信息异常",e);
		}
		
		Log.info("rsp : " + rsp);
		return rsp;
	}

	/**
	 * 3.2.2 心电数据上传
	 */
	@POST
	@Path("/dt/ecgManage/create")
	@Produces({ MediaType.APPLICATION_JSON + ";charset=utf-8" })
	public ResultDto ecg(@FormParam("appCode") String appCode, @FormParam("signData") String signData,
			@FormParam("sign") String sign, @FormParam("custCode") String custCode,
			@FormParam("deviceId") String deviceId, @FormParam("ecg") String ecgStr,
			@FormParam("pIdCard") String pIdCard, @FormParam("dIdCard") String dIdCard,
			@FormParam("doctorId") String doctorIdStr, @FormParam("orgCode") String orgCode) throws Exception {
		//{"analyzeResult":"9110;1100;7102;-1;-1;-1;-1;-1;-1;-1","config":{"highPassFilter":7,"lowPassFilter":5,"notchFilter":0},"data":"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AM4AzwDQANAA1ADaAN0A3gDgAOIA4wDiAN8A4ADgAN8A3gDeAOMA4wDkAOUA5QDiAOEA4ADcANoA2QDYANoA2wDcAOAA4wDlAOcA6QDpAOkA6QDnAOYA4wDgAN8A3QDhAOUA7QDyAPgBAAEJAQ8BFQEcASEBIgEoASwBMgE5AT8BQwFKAUwBTAFMAUYBQAE5AS8BJAEaARIBCwEGAQQBAwEBAQUBBgEKAQsBCQEEAP4A9wDsAOMA1wDNAMQAwAC7ALkAuQC6ALoAugC6ALwAvgC8AL0AvQC/AMEAwADAAMAAwAC+ALsAuQC3ALUAtACzALUAtAC3ALYAtwC4ALYAsQCtAKUAngCZAJAAhwCBAHwAewB/AIQAjgCkAMsA+gE3AYMB2gI8AqADAgNf//8AAwAKABIAGAAcACEAIgAiACIAIgAhAB8AHwAfACEAJAAnAC0AMAAxADQAOQA6AD0AQABAAEAAPwA8ADkAMwAwACoAJwAkACIAIgAkACoALQAxADcAOgA8ADkANAAxACsAJQAfABsAGAAYABkAGQAeACEAJAAoACoAKgAuAC0ALQAuAC4AMQAxADMANgA3ADkAOgA6ADwAPAA8ADoANwA0ADEALQAnACQAHwAbABYAEgAMAAcAAf/9//P/6v/i/9n/0P/G/77/u/+4/7r/vf/B/8f/zP/Q/9P/1v/Z/9n/3P/i/+j/8f/9AAkAGAAoADkARgBRAFgAXgBjAGcAaQBtAHAAcwB1AHYAeAB4AHUAdQByAHAAbwBvAHIAcwB2AHwAhACIAIsAkACRAJEAkwCUAJYAmQCZAJwAnwCgAKMApgCpAKsArgCvALEArgCrAKMAmQCEAGcAPwAK/8//h/89/vL+rP50/k3+Qf5S/nr+v/8Z/4T/+QBpANgBOwGKAcgB8gIKAhMCDQH8AeQByAGoAYoBbAFLAS8BFQEAAO4A3wDQAMQAuACuAKUAmgCOAIQAewBwAGkAYQBdAFgAVABPAEwASQBFAEMAQgBCAEIAQwBFAEUARQBFAEMAPwA5ADAAJwAeABMADAAB//3/9P/w/+r/5f/f/9v/2P/S/8//yf/G/8H/vf+4/7T/rv+o/6D/lv+K/3//df9p/17/Vf9O/0v/S/9L/0z/T/9R/1L/VP9S/1L/Uf9O/07/T/9R/1T/Wv9h/2r/c/98/4r/k/+c/6X/rP+1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...
		ResultDto rsp = new ResultDto();
		rsp.setSuccess(false);
		BaseReq req = new BaseReq(appCode, custCode, pIdCard, dIdCard, deviceId, signData,sign) ;
		Log.info("rsq [ecg]: ecg:{}, basereq :{}", ecgStr,req);//pid 421125199210164314, did 420106197702160011
		//bmiStr {"detectDate":"2016-09-21 11:14:04","height":170,"weight":60}
		try{
			//checkInsertDevice(deviceId);//保存一体机设备
			JSONObject jsonObject = JSONObject.fromObject(ecgStr);     
			Ecg ecg = (Ecg) JSONObject.toBean(jsonObject, Ecg.class);
			Log.info("rsq [ecg]: ecgData:{}", ecg.getData());

			StringBuilder sb = new StringBuilder();
			try{
				String [] resultArr = ecg.getAnalyzeResult().split(";");
				for (String tmp : resultArr) {
					String tmpStr = EcgUtil.getResultText(Integer.valueOf(tmp));
					if(StringUtils.isNotEmpty(tmpStr) && !tmpStr.equals("null")){
						sb.append(tmpStr);
						sb.append(". ");
					}
				}
			}catch(Exception e){
				sb.append("解析异常. ");
			}
			Log.info("{}心电图数据结果:{}",dIdCard,sb.toString());	
			
			try{
				StringBuilder sqlBuilder = new StringBuilder();
				sqlBuilder.append("INSERT INTO vip_inspect_data_ecg(card_code,inspect_code,device_sn,create_time,inspect_time,analyzeResult,leadType,type,");
				sqlBuilder.append("params,highPassFilter,lowPassFilter, notchFilter, domLeadLength,pEnd,pStart,qrsEnd,qrsStart,tEnd,data,domData,analyzeResultStr) ");
				sqlBuilder.append("VALUES (?,?,?,now(),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
				
				int count = Db.update(sqlBuilder.toString(),new Object[]{pIdCard,"C07",deviceId, ecg.getDetectDate(),ecg.getAnalyzeResult(),ecg.getLeadType(),ecg.getType(),
						ecg.getParams(),ecg.getConfig().getHighPassFilter(),ecg.getConfig().getLowPassFilter(),ecg.getConfig().getNotchFilter(),
						ecg.getRepBeat().getDomLeadLength(),ecg.getRepBeat().getpEnd(),ecg.getRepBeat().getpStart(),ecg.getRepBeat().getQrsEnd(),
						ecg.getRepBeat().getQrsStart(),ecg.getRepBeat().gettEnd(),ecg.getData(),ecg.getRepBeat().getDomData(),sb.toString()});
				Log.info("写入心电图数据详情:{}",count);
				if (count > 0) {
					return new ResultDto(); 
				}
				//com.jfinal.plugin.activerecord.ActiveRecordException: java.sql.SQLException: Parameter index out of range (18 > number of parameters, which is 17).
			}catch(Exception e){
				//Data truncation: Data too long for column 'data' at row 1
				Log.error("写入心电图数据详情出错，现只写入简单的检查数据:",e);
				StringBuilder sqlBuilder = new StringBuilder();
				sqlBuilder.append("INSERT INTO vip_inspect_data_ecg(card_code,inspect_code,device_sn,create_time,inspect_time,analyzeResult,leadType,type,data,analyzeResultStr) ");
				sqlBuilder.append("VALUES (?,?,?,now(),?,?,?,?,?,?)");
				
				int count = Db.update(sqlBuilder.toString(),new Object[]{pIdCard,"C07",deviceId, ecg.getDetectDate(),ecg.getAnalyzeResult(),ecg.getLeadType(),ecg.getType(),
						ecg.getData(),sb.toString()});
				Log.info("写入心电图数据简单数据:{}",count);
				if (count > 0) {
					return new ResultDto(); 
				}
			} 
		}catch(Exception e){
			Log.error("一体机存入心电图信息异常",e);
		}
		Log.info("rsp : " + rsp);
		return rsp;
	}

	/**
	 * 3.2.3 保存血糖数据
	 */
	@POST
	@Path("/dt/glu/create")
	@Produces({ MediaType.APPLICATION_JSON + ";charset=utf-8" })
	public ResultDto createGlu(@FormParam("appCode") String appCode, @FormParam("signData") String signData,
			@FormParam("sign") String sign, @FormParam("custCode") String custCode,
			@FormParam("deviceId") String deviceId, @FormParam("glu") String gluStr,
			@FormParam("pIdCard") String pIdCard, @FormParam("dIdCard") String dIdCard) {
		
		//{"beforeOrAtter":5,"detectDate":"2016-09-21 16:41:53","glu":26.2}
		ResultDto rsp = new ResultDto();
		BaseReq req = new BaseReq(appCode, custCode, pIdCard, dIdCard, deviceId, signData,sign) ;
		Log.info("rsq [C02]: 血糖:{}, basereq :{}", gluStr,req);
		try{
			//checkInsertDevice(deviceId);//保存一体机设备
			
			JSONObject jsonObject = JSONObject.fromObject(gluStr);     
			C02 data = (C02) JSONObject.toBean(jsonObject, C02.class);
			JSONObject messageObj = new JSONObject();
			messageObj.put("inspect_code", "C02");
			messageObj.put("type", "measure");
			if(data.getType().equals("GLU0")){
				messageObj.put("GLU0", new BigDecimal(data.getGlu()).setScale(1, BigDecimal.ROUND_HALF_UP));//随机血糖
			}else if(data.getType().equals("GLU1")){
				messageObj.put("GLU1", new BigDecimal(data.getGlu()).setScale(1, BigDecimal.ROUND_HALF_UP));//餐前血糖
			}else if(data.getType().equals("GLU2")){
				messageObj.put("GLU2", new BigDecimal(data.getGlu()).setScale(1, BigDecimal.ROUND_HALF_UP)); //餐后血糖
			}else {
				rsp.setSuccess(false);
				Log.error("一体机存入血糖信息不合法");
				return rsp;
			}
			messageObj.put("device_sn",deviceId);
			messageObj.put("card_code", pIdCard);
			messageObj.put("inspect_time", data.getDetectDate());
			JSONObject result = new JSONObject();
			appInterfaceService.measure(messageObj, result);
			Log.info("一体机存入血糖信息结果：{}",result);
			if(!(result != null && result.getString("flag") != null &&  result.getString("flag").equals("success"))){
				rsp.setSuccess(false);
			}
		}catch(Exception e){
			Log.error("一体机存入血糖信息异常",e);
		}
		
		Log.info("rsp : " + rsp);
		return rsp;
	 
	}

	/**
	 * 3.2.4 保存血酮数据
	 */
	@POST
	@Path("/dt/ketone/create")
	@Produces({ MediaType.APPLICATION_JSON + ";charset=utf-8" })
	public ResultDto createKetone(@FormParam("appCode") String appCode, @FormParam("signData") String signData,
			@FormParam("sign") String sign, @FormParam("custCode") String custCode,
			@FormParam("deviceId") String deviceId, @FormParam("ketone") String ketoneStr,
			@FormParam("pIdCard") String pIdCard, @FormParam("dIdCard") String dIdCard) throws Exception {

		ResultDto rsp = new ResultDto();
		rsp.setSuccess(false);
		Log.info("rsp : " + rsp);
		return rsp;
	}

	/**
	 * 保存血压数据 
	 */
	@POST
	@Path("/dt/nibp/create")
	@Produces({ MediaType.APPLICATION_JSON + ";charset=utf-8" })
	public ResultDto createNibp(@FormParam("appCode") String appCode, @FormParam("signData") String signData,
			@FormParam("sign") String sign, @FormParam("custCode") String custCode,
			@FormParam("deviceId") String deviceId, @FormParam("nibp") String nibpStr,
			@FormParam("pIdCard") String pIdCard, @FormParam("dIdCard") String dIdCard) throws Exception {
		
		//{"detectDate":"2016-09-21 15:54:54","nibpDia":77,"nibpMea":90,"nibpSys":126,"pr":99}
		ResultDto rsp = new ResultDto();
		BaseReq req = new BaseReq(appCode, custCode, pIdCard, dIdCard, deviceId, signData,sign) ;
		Log.info("rsq [C01]: 血压:{}, basereq :{}", nibpStr,req);
		try{
			//checkInsertDevice(deviceId);//保存一体机设备
			
			JSONObject jsonObject = JSONObject.fromObject(nibpStr);     
			C01 data = (C01) JSONObject.toBean(jsonObject, C01.class);
			JSONObject messageObj = new JSONObject();
			messageObj.put("inspect_code", "C01");
			messageObj.put("type", "measure");
			messageObj.put("SYS", data.getNibpSys());//收缩压
			messageObj.put("DIA", data.getNibpDia());//舒张压
			messageObj.put("PR", data.getPr()); //"nibpDia":77,"nibpMea":90,"nibpSys":126,"pr":99
			messageObj.put("device_sn",deviceId);
			messageObj.put("card_code", pIdCard);
			messageObj.put("inspect_time", data.getDetectDate());
			JSONObject result = new JSONObject();
			appInterfaceService.measure(messageObj, result);
			Log.info("一体机存入血压信息结果：{}",result);
			if(!(result != null && result.getString("flag") != null &&  result.getString("flag").equals("success"))){
				rsp.setSuccess(false);
			}
		}catch(Exception e){
			Log.error("一体机存入血压信息异常",e);
		}
		
		Log.info("rsp : " + rsp);
		return rsp;
	 
	}

	/**
	 * 3.2.6 保存血氧数据
	 */
	@POST
	@Path("/dt/spo2/create")
	@Produces({ MediaType.APPLICATION_JSON + ";charset=utf-8" })
	public ResultDto createSpo2(@FormParam("appCode") String appCode, @FormParam("signData") String signData,
			@FormParam("sign") String sign, @FormParam("custCode") String custCode,
			@FormParam("deviceId") String deviceId, @FormParam("spo2") String spo2Str,
			@FormParam("pIdCard") String pIdCard, @FormParam("dIdCard") String dIdCard) throws Exception {
		ResultDto rsp = new ResultDto();
		BaseReq req = new BaseReq(appCode, custCode, pIdCard, dIdCard, deviceId, signData,sign) ;
		Log.info("rsq [SPO2]: 血氧:{}, basereq :{}", spo2Str,req);//pid 421125199210164314, did 420106197702160011
		//bmiStr {"detectDate":"2016-09-21 11:14:04","height":170,"weight":60}
		try{
			//checkInsertDevice(deviceId);//保存一体机设备
			//{"detectDate":"2016-09-21 15:44:37","temperature":36.44444274902344}
			JSONObject jsonObject = JSONObject.fromObject(spo2Str);     
			C05 temp = (C05) JSONObject.toBean(jsonObject, C05.class);
			JSONObject messageObj = new JSONObject();
			messageObj.put("inspect_code", "C05");
			messageObj.put("type", "measure");
			messageObj.put("PR2", temp.getPr());
			messageObj.put("SPO2", temp.getSpo2());
			messageObj.put("device_sn",deviceId);
			messageObj.put("card_code", pIdCard);
			messageObj.put("inspect_time", temp.getDetectDate());
			JSONObject result = new JSONObject();
			appInterfaceService.measure(messageObj, result);
			Log.info("一体机存入血氧信息结果：{}",result);
			if(!(result != null && result.getString("flag") != null &&  result.getString("flag").equals("success"))){
				rsp.setSuccess(false);
			}
		}catch(Exception e){
			Log.error("一体机存入血氧信息异常",e);
		}
		
		Log.info("rsp : " + rsp);
		return rsp;
	}

	/**
	 * 3.2.7 保存体温数据 
	 */
	@POST
	@Path("/dt/temperature/create")
	@Produces({ MediaType.APPLICATION_JSON + ";charset=utf-8" })
	public ResultDto createTemperature(@FormParam("appCode") String appCode, @FormParam("signData") String signData,
			@FormParam("sign") String sign, @FormParam("custCode") String custCode,
			@FormParam("deviceId") String deviceId, @FormParam("temperature") String temperatureStr,
			@FormParam("pIdCard") String pIdCard, @FormParam("dIdCard") String dIdCard) throws Exception {
		ResultDto rsp = new ResultDto();
		BaseReq req = new BaseReq(appCode, custCode, pIdCard, dIdCard, deviceId, signData,sign) ;
		Log.info("rsq [TEMP]: TEMP:{}, basereq :{}", temperatureStr,req);//pid 421125199210164314, did 420106197702160011
		//bmiStr {"detectDate":"2016-09-21 11:14:04","height":170,"weight":60}
		try{
			//checkInsertDevice(deviceId);//保存一体机设备
			//{"detectDate":"2016-09-21 15:44:37","temperature":36.44444274902344}
			JSONObject jsonObject = JSONObject.fromObject(temperatureStr);     
			Temperature temp = (Temperature) JSONObject.toBean(jsonObject, Temperature.class);
			JSONObject messageObj = new JSONObject();
			messageObj.put("inspect_code", "C04");
			messageObj.put("type", "measure");
			messageObj.put("TEMP", new BigDecimal(temp.getTemperature()).setScale(1, BigDecimal.ROUND_HALF_UP));
			messageObj.put("device_sn",deviceId);
			messageObj.put("card_code", pIdCard);
			messageObj.put("inspect_time", temp.getDetectDate());
			JSONObject result = new JSONObject();
			appInterfaceService.measure(messageObj, result);
			Log.info("一体机存入TEMP信息结果：{}",result);
			if(!(result != null && result.getString("flag") != null &&  result.getString("flag").equals("success"))){
				rsp.setSuccess(false);
			}
		}catch(Exception e){
			Log.error("一体机存入TEMP信息异常",e);
		}
		
		Log.info("rsp : " + rsp);
		return rsp;
	}

	/**
	 * 保存尿常规数据
	 */
	@POST
	@Path("/dt/urine/create")
	@Produces({ MediaType.APPLICATION_JSON + ";charset=utf-8" })
	public ResultDto createUrine(@FormParam("appCode") String appCode, @FormParam("signData") String signData,
			@FormParam("sign") String sign, @FormParam("custCode") String custCode,
			@FormParam("deviceId") String deviceId, @FormParam("urine") String urineStr,
			@FormParam("pIdCard") String pIdCard, @FormParam("dIdCard") String dIdCard) throws Exception {
		
		//rsq [C06]: 尿常规:{"bil":0,"bld":1,"detectDate":"2016-09-22 10:22:28","glu":0,"ket":0,"leu":0,"nit":0,"ph":6,"pro":2,"sg":1.025,"ubg":0,"vc":3}, 
		ResultDto rsp = new ResultDto();
		BaseReq req = new BaseReq(appCode, custCode, pIdCard, dIdCard, deviceId, signData,sign) ;
		Log.info("rsq [C06]: 尿常规:{}, basereq :{}", urineStr,req);
		try{
			//checkInsertDevice(deviceId);//保存一体机设备
			
			JSONObject jsonObject = JSONObject.fromObject(urineStr);     
			C06 data = (C06) JSONObject.toBean(jsonObject, C06.class);
			JSONObject messageObj = new JSONObject();
			messageObj.put("inspect_code", "C06");
			messageObj.put("type", "measure"); 
			messageObj.put("LEU", data.getLeu()); //白细胞
			messageObj.put("NIT", data.getNit()); //亚硝酸盐
			messageObj.put("UBG", data.getUbg()); //尿胆原
			messageObj.put("PH",  new BigDecimal(data.getPh()).setScale(1, BigDecimal.ROUND_HALF_UP)); //酸碱度
			messageObj.put("BLD", data.getBld()); //红细胞
			messageObj.put("GLU", data.getGlu()); //葡萄糖
			messageObj.put("KET", data.getKet()); //酮体
			messageObj.put("PRO", data.getPro()); //蛋白质
			messageObj.put("BIL", data.getBil()); //胆红素
			messageObj.put("VC", data.getVc()); //维生素
			messageObj.put("SG", new BigDecimal(data.getSg()).setScale(3, BigDecimal.ROUND_HALF_UP)); //比重
			messageObj.put("device_sn",deviceId);
			messageObj.put("card_code", pIdCard);
			messageObj.put("inspect_time", data.getDetectDate());
			JSONObject result = new JSONObject();
			appInterfaceService.measure(messageObj, result);
			Log.info("一体机存入尿常规信息结果：{}",result);
			if(!(result != null && result.getString("flag") != null &&  result.getString("flag").equals("success"))){
				rsp.setSuccess(false);
			}
		}catch(Exception e){
			Log.error("一体机存入尿常规信息异常",e);
		}
		
		Log.info("rsp : " + rsp);
		return rsp;
	 
	}

	/**
	 * 根据设备编号同步医生信息
	 */
	@GET
	@Path("/em/info/syncInfo")
	@Produces({ MediaType.APPLICATION_JSON + ";charset=utf-8" })
	public ResultDto syncInfoByDeviceId(@QueryParam("appCode") String appCode, @QueryParam("signData") String signData,
			@QueryParam("sign") String sign, @QueryParam("custCode") String custCode,
			@QueryParam("deviceId") String deviceId) {
		//device_doctor   deviceId
		DoctorResutDto result = new DoctorResutDto();
		result.setSuccess(false);
		List<Doctor> results = new ArrayList<Doctor>();
		try{
			List<Record> checkRecords = Db.find("select  d.tel,d.password,d.idCard  from device_doctor dd,doctor d  where dd.doctorid = d.id   and dd.deviceid  in (select id from device where device_id = ?)", deviceId);
			if(null != checkRecords && checkRecords.size()>0){
				for(Record r : checkRecords){
					String name = r.getStr("TEL");
					String password = r.getStr("PASSWORD");
					String idCard = r.getStr("IDCARD");
					Doctor d = new Doctor();
					d.setUserLoginName(name);
					d.setUserPassword(password);
					d.setIdCard(idCard);
					results.add(d);
				}
				result.setSuccess(true);
				result.setResults(results);
			}
		}catch(Exception e){
			Log.error("同步医生失败",e);
			result.setSuccess(false);
			return result;
		}
		return result;
	}

	/**
	 * 3.1.2 体检报告管理 
	 */
	@POST
	@Path("/dt/detectReport/create")
	@Produces({ MediaType.APPLICATION_JSON + ";charset=utf-8" })
	public ResultDto detectReport(@FormParam("appCode") String appCode, @FormParam("signData") String signData,
			@FormParam("sign") String sign, @FormParam("custCode") String custCode,
			@FormParam("pIdCard") String pIdCard, @FormParam("dIdCard") String dIdCard,
			@FormParam("deviceInfo") String deviceInfoStr, @FormParam("reportNo") String reportNo,
			@FormParam("reportTime") String reportTimeStr, @FormParam("reportData") String reportDataStr,
			@FormParam("influence") String influence, @FormParam("ecgDetectDate") String ecgDetectDateStr,
			@FormParam("flupId") String flupIdStr) throws Exception {
		//TODO 待做，现在应该不用
		ResultDto rsp = new ResultDto();
		rsp.setSuccess(false);
		Log.info("rsp : " + rsp);
		return rsp;
	}

	@GET
	@Path("/hello")
	public String sayHello() {
		return "sayHello";
	}

	Logger Log = LoggerFactory.getLogger(this.getClass());

	/**
	 * 获取身份证的性别.0 男 ， 1 女  //第17位数字表示性别：奇数表示男性，偶数表示女性；
	 */
	private String getIdCardSex(String idCard) {
		if (StringUtils.isEmpty(idCard) || idCard.length() != 18) {
			return "";
		} else {
			String sex17 = idCard.substring(16, 17);
			if (Integer.valueOf(sex17) % 2 == 0) {
				return "1";
			} else {
				return "0";
			}
		}
	}

	/**
	 * 获取身份证的生日 //第7~14位数字表示：出生年、月、日； 
	 */
	private String getIdCardBirthDay(String idCard) {
		if (StringUtils.isEmpty(idCard) || idCard.length() != 18) {
			return "";
		} else {
			return idCard.substring(6, 14);
		}
	}

	/**
	 * 获取身份证的年龄 //第7~14位数字表示：出生年、月、日； 
	 */
	private Integer getIdCardAge(String idCard) {
		try {
			if (StringUtils.isEmpty(idCard) || idCard.length() != 18) {
				return null;
			} else {
				Integer byear = Integer.valueOf(idCard.substring(6, 10));
				Calendar c = Calendar.getInstance();
				return c.get(Calendar.YEAR) - byear;
			}
		} catch (Exception e) {
			Log.error("身份证解析年龄异常", e);
			return null;
		}
	}

	static Properties conf_pros = new Properties();
	static String localFilePath = null; //本地图片地址

	static {
		try {
			URL url = GareaService.class.getClassLoader().getResource("conf.properties");
			File conf = new File(url.getFile());
			InputStream in = new FileInputStream(conf);
			conf_pros.load(in);
			localFilePath = conf_pros.getProperty("local_pic_path").trim();
		} catch (Exception e) {
			LoggerFactory.getLogger("GareaServiceStatic").error("读取配置文件conf.properties出错了 ", e);
		}
	}

	/**
	 * 根据Key读取Value,如果没有，就返回null
	 */
	public String getConfigByKey(String key) {
		try {
			String value = conf_pros.getProperty(key);
			return value.trim();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 图像流转换为图片.
	 */
	public boolean inputStreamToPng(InputStream ins, File file) {
		try {
			OutputStream os = new FileOutputStream(file);
			int bytesRead = 0;
			byte[] buffer = new byte[8192];
			while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
				os.write(buffer, 0, bytesRead);
			}
			os.close();
			ins.close();
			return true;
		} catch (Exception e) {
			Log.error("转换图片失败.", e);
			return false;
		}
	}

	/**
	 * 保存一体机设备.
	 * @param deviceId 一体机设备SN.
	 * @return  如果已经存在或者保存成功返回true.
	 */
	private boolean checkInsertDevice(String deviceId){
		//用线程，队列来处理 //如果考虑并发的话		
		try{
			Record record = Db.findFirst("select SN from device where sn=?",deviceId);
			if(record != null && StringUtils.isNotEmpty(record.getStr("SN"))){
				Log.debug("盖瑞一体机设备已存在:{}",deviceId);
				return true;
			}else{
				int count = Db.update("insert into device (device_id,device_type,sn,create_time,remark) values(?,?,?,NOW(),?)",
							new Object[] { deviceId, "GAREA",deviceId,"盖睿一体机" }); 
				
				if (count > 0) {
					Log.info("盖瑞一体机设备保存成功:{}", deviceId);
					return true;
				}
			}	
		}catch(Exception e){
			Log.info("盖瑞一体机设备保存失败,"+deviceId, e);
		}
		return false;
	}

	public static void main(String[] args) {
		String idCard = "420001198011224123";
		System.out.println(new GareaService().getIdCardSex(idCard));
		System.out.println(new GareaService().getIdCardBirthDay(idCard));
		System.out.println(new GareaService().getIdCardAge(idCard));
	}

}