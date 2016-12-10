package com.net.nky.pay.http;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.net.nky.pay.util.GetWxOrderno;
import com.net.nky.pay.util.RequestHandler;
import com.net.nky.pay.util.RequestHandlerForQrcode;
import com.net.nky.pay.util.WxQrcodeUtil;

public class WxQrcode {

	//微信支付商户开通后 微信会提供appid和appsecret和商户号partner
		private static String appid = "wxfd65d5401333bc07";
		private static String appsecret = "9ce209909b58457286c1c1fe8cf7f22b";
		private static String partner = "1376090302";
		//这个参数partnerkey是在商户后台配置的一个32位的key,微信商户平台-账户设置-安全设置-api安全
		private static String partnerkey = "woboqianwoboqianwoboqianwoboqian";
		//openId 是微信用户针对公众号的标识，授权的部分这里不解释
		private static String openId = "oqPYuv0-ROUqYLzmUmFg9liHC8X0";
		//微信支付成功后通知地址 必须要求80端口并且地址不能带参数
		private static String notifyurl = "http://api.nbrobo.com/success.do";	
		
		
		public static String getCodeurl(WxPayDto tpWxPayDto){
		
		// 1 参数
		// 订单号
		String orderId = tpWxPayDto.getOrderId();
		// 附加数据 原样返回
		String attach = "";
		// 总金额以分为单位，不带小数点
		String totalFee = WxQrcodeUtil.getMoney(tpWxPayDto.getTotalFee());
		
		// 订单生成的机器 IP
		String spbill_create_ip = tpWxPayDto.getSpbillCreateIp();
		// 这里notify_url是 支付完成后微信发给该链接信息，可以判断会员是否支付成功，改变订单状态等。
		String notify_url = notifyurl;
		String trade_type = "NATIVE";

		// 商户号
		String mch_id = partner;
		// 随机字符串
		String nonce_str = WxQrcodeUtil.getNonceStr();

		// 商品描述根据情况修改
		String body = tpWxPayDto.getBody();

		// 商户订单号
		String out_trade_no = orderId;

		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		packageParams.put("appid", appid);
		packageParams.put("mch_id", mch_id);
		packageParams.put("nonce_str", nonce_str);
		packageParams.put("body", body);
		packageParams.put("attach", attach);
		packageParams.put("out_trade_no", out_trade_no);

		// 这里写的金额为1 分到时修改
		packageParams.put("total_fee", totalFee);
		packageParams.put("spbill_create_ip", spbill_create_ip);
		packageParams.put("notify_url", notify_url);

		packageParams.put("trade_type", trade_type);

		RequestHandlerForQrcode reqHandler = new RequestHandlerForQrcode(null, null);
		reqHandler.init(appid, appsecret, partnerkey);

		String sign = reqHandler.createSign(packageParams);
		String xml = "<xml>" + "<appid>" + appid + "</appid>" + "<mch_id>"
				+ mch_id + "</mch_id>" + "<nonce_str>" + nonce_str
				+ "</nonce_str>" + "<sign>" + sign + "</sign>"
				+ "<body><![CDATA[" + body + "]]></body>" 
				+ "<out_trade_no>" + out_trade_no
				+ "</out_trade_no>" + "<attach>" + attach + "</attach>"
				+ "<total_fee>" + totalFee + "</total_fee>"
				+ "<spbill_create_ip>" + spbill_create_ip
				+ "</spbill_create_ip>" + "<notify_url>" + notify_url
				+ "</notify_url>" + "<trade_type>" + trade_type
				+ "</trade_type>" + "</xml>";
		String code_url = "";
		String createOrderURL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
		
		
		code_url = new GetWxOrderno().getCodeUrl(createOrderURL, xml);
		System.out.println("code_url----------------"+code_url);
		return code_url;
	}
}
