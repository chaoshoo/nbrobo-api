package com.net.util;

import com.net.ServiceConstants;


/**
 * 好医网 参数拼接
 * @author zw
 *
 */
public class HttpParamUtil {
	
	
	public static String getUrl(String hosid,String rowstart,String rowcount,String url){
		String paramvalue="appid="+ServiceConstants.appid+"&hosid="+hosid+"&nowstp="+DateUtil.getNowStp()+"&rowcount="+rowcount+"&rowstart="+rowstart+"";
		String a =paramvalue+"&"+ServiceConstants.key;
//		System.out.println("加密的参数为=="+a);
		String sign = MD5Util.encryption(a);
		String newparam=paramvalue+"&"+"sign="+sign;
		//添加参数
		String rquestUrl = ServiceConstants.HOSPITALURL.replace("flag", url)+newparam;
		System.out.println("发送请求参数==="+"url=="+rquestUrl);
		return rquestUrl;
	}
	
	
	public static String getUrlForDep(String deptid,String hosid,String rowstart,String rowcount,String url){
		String paramvalue="appid="+ServiceConstants.appid+"&deptid="+deptid+"&hosid="+hosid+"&nowstp="+DateUtil.getNowStp()+"&rowcount="+rowcount+"&rowstart="+rowstart+"";
		String a =paramvalue+"&"+ServiceConstants.key;
//		System.out.println("加密的参数为=="+a);
		String sign = MD5Util.encryption(a);
		String newparam=paramvalue+"&"+"sign="+sign;
		//添加参数
		String rquestUrl = ServiceConstants.HOSPITALURL.replace("flag", url)+newparam;
		System.out.println("发送请求参数==="+"url=="+rquestUrl);
		return rquestUrl;
	}


	public static String getUrlForDoc(String deptid, String docid,String hosid, String rowstart, String rowcount, String url) {
		String paramvalue="appid="+ServiceConstants.appid+"&deptid="+deptid+"&docid="+docid+"&hosid="+hosid+"&nowstp="+DateUtil.getNowStp()+"&rowcount="+rowcount+"&rowstart="+rowstart+"";
		String a =paramvalue+"&"+ServiceConstants.key;
//		System.out.println("加密的参数为=="+a);
		String sign = MD5Util.encryption(a);
		String newparam=paramvalue+"&"+"sign="+sign;
		//添加参数
		String rquestUrl = ServiceConstants.DOCTORURL.replace("flag", url)+newparam;
		System.out.println("发送请求参数==="+"url=="+rquestUrl);
		return rquestUrl;
		
	}


	public static String getUrlForPart(String doctorno,String hosid,String scheduleid, String rowstart,String rowcount, String url) {
		String paramvalue="appid="+ServiceConstants.appid+"&doctorno="+doctorno+"&hosid="+hosid+"&nowstp="+DateUtil.getNowStp()+"&rowcount="+rowcount+"&rowstart="+rowstart+"&scheduleid="+scheduleid;
		String a =paramvalue+"&"+ServiceConstants.key;
//		System.out.println("加密的参数为=="+a);
		String sign = MD5Util.encryption(a);
		String newparam=paramvalue+"&"+"sign="+sign;
		//添加参数
		String rquestUrl = ServiceConstants.HOSPITALURL.replace("flag", url)+newparam;
		System.out.println("发送请求参数==="+"url=="+rquestUrl);
		return rquestUrl;
	}


	public static String getUrlForghlock(String hosid, String partscheduleid,String scheduleid, String url) {
		String paramvalue="appid="+"HYGH000002"+"&hosid="+hosid+"&nowstp="+DateUtil.getNowStp()+"&partscheduleid="+partscheduleid+"&scheduleid="+scheduleid;
		String a =paramvalue+"&"+ServiceConstants.key;
//		System.out.println("加密的参数为=="+a);
		String sign = MD5Util.encryption(a);
		String newparam=paramvalue+"&"+"sign="+sign;
		//添加参数
		String rquestUrl = ServiceConstants.CENTERGH.replace("flag", url)+newparam;
		System.out.println("发送请求参数==="+"url=="+rquestUrl);
		return rquestUrl;
		
	}


	public static String getUrlFororder(String orderid,String url) {
		String paramvalue="appid="+ServiceConstants.appid+"&nowstp="+DateUtil.getNowStp()+"&orderid="+orderid;
		String a =paramvalue+"&"+ServiceConstants.key;
//		System.out.println("加密的参数为=="+a);
		String sign = MD5Util.encryption(a);
		String newparam=paramvalue+"&"+"sign="+sign;
		//添加参数
		String rquestUrl = ServiceConstants.CENTERGH.replace("flag", url)+newparam;
		System.out.println("发送请求参数==="+"url=="+rquestUrl);
		return rquestUrl;
	}


	public static String getUrlForCancle(String cancelreason, String operator,String orderid, String url) {
		String paramvalue="appid="+ServiceConstants.appid+"&cancelreason="+cancelreason+"&nowstp="+DateUtil.getNowStp()+"&operator="+operator+"&orderid="+orderid;
		String a =paramvalue+"&"+ServiceConstants.key;
//		System.out.println("加密的参数为=="+a);
		String sign = MD5Util.encryption(a);
		String newparam=paramvalue+"&"+"sign="+sign;
		//添加参数
		String rquestUrl = ServiceConstants.CENTERGH.replace("flag", url)+newparam;
		System.out.println("发送请求参数==="+"url=="+rquestUrl);
		return rquestUrl;
	}


	public static String getUrlForCancle(String bstp, String estp,
			String hosid, String rowcount, String rowstart, String url) {
		String paramvalue="appid="+ServiceConstants.appid+"&bstp="+bstp+"&estp="+estp+"&hosid="+hosid+"&nowstp="+DateUtil.getNowStp()+"&rowcount="+rowcount+"&rowstart="+rowstart;
		String a =paramvalue+"&"+ServiceConstants.key;
//		System.out.println("加密的参数为=="+a);
		String sign = MD5Util.encryption(a);
		String newparam=paramvalue+"&"+"sign="+sign;
		//添加参数
		String rquestUrl = ServiceConstants.DOCTORURL.replace("flag", url)+newparam;
		System.out.println("发送请求参数==="+"url=="+rquestUrl);
		return rquestUrl;
		
	}
	
	/**
	 * 获取加密参数
	 * @throws Exception 
	 */
	public static String getCiphByString(String param) throws Exception{
		String url =ServiceConstants.CIPHERURL+param;
		String ciph =  HttpClient.doGet(url, "utf-8");
		return ciph.substring(ciph.indexOf("是\"")+4, ciph.length()-2);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
