package com.net.nky.pay.action;

import java.io.PrintStream;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.net.nky.pay.util.CommonUtil;
import com.net.nky.pay.util.GetWxOrderno;
import com.net.nky.pay.util.RequestHandler;
import com.net.nky.pay.util.Sha1Util;
import com.net.nky.pay.util.TenpayUtil;
import com.net.util.MD5Util;

import net.sf.json.JSONObject;

public class TopayServlet {
	public static void getPackage(String orderNo, JSONObject result) {
		String goods_name = "挂号费";
		String body = goods_name;
		Record orderRecord = Db.findFirst(
				"select t.orderfee,s.wxopenid,s.wx_code from vip_reg t,t_vip s where TIMESTAMPDIFF(SECOND,t.create_time,NOW()) <(15*60) and s.vip_code=t.vip_code and t.orderId=?", orderNo);
		if(orderRecord==null||orderRecord.getStr("wxopenid")==null||"".equals(orderRecord.getStr("wxopenid"))){
			result.put("code", "1");
			result.put("message", "挂号订单已超时");
			return;
		}
		String orderfee = orderRecord.getStr("orderfee");
		String openId = orderRecord.getStr("wxopenid");
//		if(openId==null||"".equals(openId)){
//			result.put("code", "1");
//			result.put("message", "挂号订单已超时");
//			return;
//		}
		String wxcode = orderRecord.getStr("wx_code");
		float money = (float) 0.01;
		int price = (int) (money * 100.0F);
		String totalFee = String.valueOf(price);
		System.out.println("totalFee==" + totalFee);

		// 微信支付商户号 1376090302
		// 商户平台登录帐号 1376090302@1376090302
		// 商户平台登录密码 610068
		// 申请对应的公众号 居民健康管理服务平台（）
		// 公众号APPID wxfd65d5401333bc07
		String appid = "wxfd65d5401333bc07";
		String appsecret = "9ce209909b58457286c1c1fe8cf7f22b";
		String partner = "1376090302";
		String partnerkey = "woboqianwoboqianwoboqianwoboqian";
		// String openId = "";//给钱的那个人
		String code = wxcode;
		String notify_url = "http://api.nbrobo.com/success.do";
		String URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
				+ appid + "&secret=" + appsecret + "&code=" + code
				+ "&grant_type=authorization_code";

		JSONObject jsonObject = CommonUtil.httpsRequest(URL, "GET", null);

		String currTime = TenpayUtil.getCurrTime();

		String strTime = currTime.substring(8, currTime.length());

		String strRandom = String.valueOf(TenpayUtil.buildRandom(4));

		String strReq = strTime + strRandom;

		String mch_id = partner;

		String device_info = "";

		String nonce_str = strReq;

		String attach = "";

		String out_trade_no = orderNo;

		// String spbill_create_ip = request.getRemoteAddr();
		String spbill_create_ip = "";

		String trade_type = "JSAPI";
		String openid = openId;

		SortedMap packageParams = new TreeMap();
		packageParams.put("appid", appid);
		packageParams.put("mch_id", mch_id);
		packageParams.put("nonce_str", nonce_str);
		packageParams.put("body", body);
		packageParams.put("attach", attach);
		packageParams.put("out_trade_no", out_trade_no);

		packageParams.put("total_fee", totalFee);

		packageParams.put("spbill_create_ip", spbill_create_ip);
		packageParams.put("notify_url", notify_url);
		packageParams.put("trade_type", trade_type);
		packageParams.put("openid", openid);

		RequestHandler reqHandler = new RequestHandler();
		reqHandler.init(appid, appsecret, partnerkey);

		String sign = reqHandler.createSign(packageParams);
		String xml = "<xml><appid>" + appid + "</appid>" + "<mch_id>" + mch_id
				+ "</mch_id>" + "<nonce_str>" + nonce_str + "</nonce_str>"
				+ "<sign>" + sign + "</sign>" + "<body><![CDATA[" + body
				+ "]]></body>" + "<attach>" + attach + "</attach>"
				+ "<out_trade_no>" + out_trade_no + "</out_trade_no>"
				+ "<total_fee>" + totalFee + "</total_fee>"
				+ "<spbill_create_ip>" + spbill_create_ip
				+ "</spbill_create_ip>" + "<notify_url>" + notify_url
				+ "</notify_url>" + "<trade_type>" + trade_type
				+ "</trade_type>" + "<openid>" + openid + "</openid>"
				+ "</xml>";

		String createOrderURL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
		String prepay_id = "";
		try {
			new GetWxOrderno();
			prepay_id = GetWxOrderno.getPayNo(createOrderURL, xml);
			if (prepay_id.equals("")) {
				result.put("code", "1");
				result.put("message", "预支付失败");
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		SortedMap finalpackage = new TreeMap();
		String appid2 = appid;
		String timestamp = Sha1Util.getTimeStamp();
		String nonceStr2 = nonce_str;
		String prepay_id2 = "prepay_id=" + prepay_id;
		String packages = prepay_id2;
		finalpackage.put("appId", appid2);
		finalpackage.put("timeStamp", timestamp);
		finalpackage.put("nonceStr", nonceStr2);
		finalpackage.put("package", packages);
		finalpackage.put("signType", "MD5");

		String finalsign = reqHandler.createSign(finalpackage);

//		String packageStr = "\"appid\":\"" + appid2 + "\","
//				+ "\"timeStamp\":\"" + timestamp + "\"," + "\"nonceStr\":\""
//				+ nonceStr2 + "\"," + "\"package\":\"" + packages + "\","
//				+ "\"signType\":\"" + "MD5\"" + "," + "\"paySign\":\""
//				+ finalsign + "\"";
		result.put("appid", appid2);
		result.put("timeStamp",timestamp);
		result.put("nonceStr",nonceStr2);
		result.put("package",packages);
		result.put("signType","MD5");
		result.put("paySign",finalsign);
		result.put("code", "0");
		result.put("message", "成功");
//		return packageStr;
	}
}

/*
 * Location: Qualified Name: com.yq.weixin.pay.action.TopayServlet JD-Core
 * Version: 0.6.2
 */