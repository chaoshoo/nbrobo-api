package com.net.nky.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.net.nky.pay.http.WxPayDto;
import com.net.nky.pay.http.WxQrcode;
import com.net.nky.pay.util.CommonUtil;
import com.net.nky.pay.util.EncoderHandler;
import com.net.nky.pay.util.GetWxOrderno;
import com.net.nky.pay.util.Token;
import com.net.nky.pay.util.WxQrcodeUtil;

@Controller
@RequestMapping("/pay")
public class PayController {

	private static Logger log = LoggerFactory.getLogger(PayController.class);
	CommonUtil commonUtil = new CommonUtil();

	
	@RequestMapping(value = "/getwxqrcode.do")
	@ResponseBody
	public void getwxqrcode(HttpServletRequest request,HttpServletResponse response) throws IOException {
		String orderid=request.getParameter("orderid");
		Record orderRecord = Db.findFirst(
				"select t.orderfee,s.wxopenid,s.wx_code from vip_reg t,t_vip s where s.vip_code=t.vip_code and t.orderId=?", orderid);
		String orderfee =orderRecord.getStr("orderfee");
		 //扫码支付
	    WxPayDto tpWxPay1 = new WxPayDto();
	    tpWxPay1.setBody("挂号费");
	    tpWxPay1.setOrderId(WxQrcodeUtil.getNonceStr());
	    tpWxPay1.setSpbillCreateIp("127.0.0.1");
	    tpWxPay1.setTotalFee(orderfee);
		String codeurl =WxQrcode.getCodeurl(tpWxPay1);
		EncoderHandler encoder= new EncoderHandler();
		OutputStream output = response.getOutputStream();
		encoder.encoderQRCode(codeurl, output);
	}     
	
	
	
	
	@RequestMapping(value = "/success.do")
	@ResponseBody
	public void success(HttpServletRequest request) {
		String xmlStr = getWxXml(request);
	     Map map2 = GetWxOrderno.doXMLParse(xmlStr);
	     String return_code = (String)map2.get("return_code");
	     if (return_code.equals("SUCCESS")){
		     String order_id = (String)map2.get("out_trade_no");
		     Db.update("update vip_reg set status=3,payrtime=NOW() where orderid=?",order_id);
	     }else{
	    	 String order_id = (String)map2.get("out_trade_no");
		     Db.update("update vip_reg set status=4 where orderid=?",order_id);
	     }
//	     Record orderRecord = Db.findFirst(
//					"select t.orderfee,s.wxopenid from vip_reg t,t_vip s where s.vip_code=t.vip_code and t.orderId=?", order_id);
//		 int orderfee = orderRecord.getInt("orderfee");
//		 String openId = orderRecord.getStr("wxopenid");
//	     Map map=new HashMap();
//	     map.put("order_id", order_id);
//	     map.put("status", Integer.valueOf(1));
//	     if (return_code.equals("SUCCESS")){
//	       map.put("result", "订单支付成功");
//	       map.put("body", "挂号费");
//	       map.put("price", orderfee);
//	       map.put("oppen_id", openId);
//	       pushMessage(map);
//	     }
	}
	public static String getWxXml(HttpServletRequest request)
	   {
	     try
	     {
	       BufferedReader br = new BufferedReader(new InputStreamReader(
	         request
	         .getInputStream()));
	 
	       String line = null;
	       StringBuilder sb = new StringBuilder();
	       while ((line = br.readLine()) != null) {
	         sb.append(line);
	       }
	       System.out.println(sb.toString());
	       return sb.toString();
	     }
	     catch (IOException e)
	     {
	       e.printStackTrace();
	       return e.getMessage();
	     }
	   }
	 
}
