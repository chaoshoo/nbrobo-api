package com.net.util;

import com.net.ServiceConstants;

/**
 * 好医网 参数拼接
 * 
 * @author zw
 *
 */
public class HttpParamUtil {

	public static void main(String[] args) {
		try {
			System.out.println(getUrlForDocSchedule("8610000713", "4200271143", "860270216", "0","10"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String getUrlForAllHospital(String rowstart, String rowcount) {
		String paramvalue = "appid=" + ServiceConstants.appid + "&hosid=0&nowstp=" + DateUtil.getNowStp() + "&rowcount="
				+ rowcount + "&rowstart=" + rowstart + "";
		String rquestUrl = genSignedGetUrl(ServiceConstants.HY_URL_HOSPITAL, paramvalue);
		return rquestUrl;
	}

	public static String getUrlForDeptInHospital(String hosid, String rowstart, String rowcount) {
		String paramvalue = "appid=" + ServiceConstants.appid + "&hosid=" + hosid + "&nowstp=" + DateUtil.getNowStp()
				+ "&rowcount=" + rowcount + "&rowstart=" + rowstart + "";
		String rquestUrl = genSignedGetUrl(ServiceConstants.HY_URL_DEPARTMENT, paramvalue);
		return rquestUrl;
	}

	public static String getUrlForDoctorInDept(String deptid, String hosid, String rowstart, String rowcount) {
		String paramvalue = "appid=" + ServiceConstants.appid + "&deptid=" + deptid + "&hosid=" + hosid + "&nowstp="
				+ DateUtil.getNowStp() + "&rowcount=" + rowcount + "&rowstart=" + rowstart + "";
		String rquestUrl = genSignedGetUrl(ServiceConstants.HY_URL_DOCTOR, paramvalue);
		return rquestUrl;
	}

	public static String getUrlForDocSchedule(String docid, String deptid, String hosid, String rowstart,
			String rowcount) {
		String paramvalue = "appid=" + ServiceConstants.appid + "&deptid=" + deptid + "&docid=" + docid + "&hosid="
				+ hosid + "&nowstp=" + DateUtil.getNowStp() + "&rowcount=" + rowcount + "&rowstart=" + rowstart + "";
		String rquestUrl = genSignedGetUrl(ServiceConstants.HY_URL_DOCTOR_SHCEDULE, paramvalue);
		return rquestUrl;

	}

	public static String getUrlForDocParttimeSchedule(String doctorno, String hosid, String scheduleid, String rowstart,
			String rowcount) {
		String paramvalue = "appid=" + ServiceConstants.appid + "&doctorno=" + doctorno + "&hosid=" + hosid + "&nowstp="
				+ DateUtil.getNowStp() + "&rowcount=" + rowcount + "&rowstart=" + rowstart + "&scheduleid="
				+ scheduleid;
		String rquestUrl = genSignedGetUrl(ServiceConstants.HY_URL_DOCTOR_PARTTIME_SHCEDULE, paramvalue);
		return rquestUrl;
	}

	public static String getUrlForGhLock(String hosid, String partscheduleid, String scheduleid) {
		String paramvalue = "appid=" + "HYGH000002" + "&hosid=" + hosid + "&nowstp=" + DateUtil.getNowStp()
				+ "&partscheduleid=" + partscheduleid + "&scheduleid=" + scheduleid;
		String rquestUrl = genSignedGetUrl(ServiceConstants.HY_URL_LOCK, paramvalue);
		return rquestUrl;

	}

	public static String getUrlForPlaceOrder(String orderid) {
		String paramvalue = "appid=" + ServiceConstants.appid + "&nowstp=" + DateUtil.getNowStp() + "&orderid="
				+ orderid;
		String rquestUrl = genSignedGetUrl(ServiceConstants.HY_URL_PLACE_ORDER, paramvalue);
		return rquestUrl;
	}

	public static String getUrlForCancleOrder(String cancelreason, String operator, String orderid) {
		String paramvalue = "appid=" + ServiceConstants.appid + "&cancelreason=" + cancelreason + "&nowstp="
				+ DateUtil.getNowStp() + "&operator=" + operator + "&orderid=" + orderid;
		String rquestUrl = genSignedGetUrl(ServiceConstants.HY_URL_CANCEL_ORDER, paramvalue);
		return rquestUrl;
	}

	public static String getUrlForDoctorStop(String bstp, String estp, String hosid, String rowcount, String rowstart) {
		String paramvalue = "appid=" + ServiceConstants.appid + "&bstp=" + bstp + "&estp=" + estp + "&hosid=" + hosid
				+ "&nowstp=" + DateUtil.getNowStp() + "&rowcount=" + rowcount + "&rowstart=" + rowstart;
		String rquestUrl = genSignedGetUrl(ServiceConstants.HY_URL_DOCTOR_STOP, paramvalue);
		return rquestUrl;

	}

	/**
	 * 获取加密参数
	 * 
	 * @throws Exception
	 */
	public static String getCiphByString(String param) throws Exception {
		String url = ServiceConstants.HY_URL_CIPHER + param;
		String ciph = HttpClient.doGet(url, "utf-8");
		return ciph.substring(ciph.indexOf("是\"") + 4, ciph.length() - 2);
	}

	private static String genSignedGetUrl(String url, String param) {
		String a = param + "&" + ServiceConstants.key;
		String sign = MD5Util.encryption(a);
		String newparam = param + "&" + "sign=" + sign;
		String signedUrl = url + "?" + newparam;
		System.out.println("发送请求参数===" + "url==" + signedUrl);
		return signedUrl;
	}

}
