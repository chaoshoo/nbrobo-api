
package com.net;

import java.util.ResourceBundle;

/**
 * 服务常量
 * 
 * @author hiveview
 *
 */
public class ServiceConstants {
	private static ResourceBundle conf = ResourceBundle.getBundle("conf");

	public static String appid = "HYGH000004";
	public static String key = "7b9cbbfe355d97cc5de5cb9d3f3ac62f";
	public static String jdbc_driver = conf.getString("jdbc.driver");
	public static String jdbc_url = conf.getString("jdbc.url");
	public static String jdbc_user = conf.getString("jdbc.user");
	public static String jdbc_password = conf.getString("jdbc.password");

	public static final String URL_DOMAIN = "http://www.nky.com/";

	public static final String URL_UPLOAD = "";

	public static final String HY_URL_DOMAIN = "http://27.17.40.149:9000/hy_ghservice";
	public static final String HY_URL_DOMAIN_API = HY_URL_DOMAIN + "/ghapi";
	public static final String HY_URL_DOMAIN_DOCTOR = HY_URL_DOMAIN + "/ghdoctor";
	public static final String HY_URL_DOMAIN_CENTER = HY_URL_DOMAIN + "/centergh";

	public static final String HY_URL_HOSPITAL = HY_URL_DOMAIN_API + "/gethospital";
	public static final String HY_URL_DEPARTMENT = HY_URL_DOMAIN_API + "/getdept";
	public static final String HY_URL_DOCTOR = HY_URL_DOMAIN_API + "/Ghgetdoctor";
	public static final String HY_URL_DOCTOR_PARTTIME_SHCEDULE = HY_URL_DOMAIN_API + "/GetPartTime";

	public static final String HY_URL_DOCTOR_SHCEDULE = HY_URL_DOMAIN_DOCTOR + "/getDoctorSchedule";
	public static final String HY_URL_DOCTOR_STOP = HY_URL_DOMAIN_DOCTOR + "/getDoctorstop";

	public static final String HY_URL_LOCK = HY_URL_DOMAIN_CENTER + "/ghlock";
	public static final String HY_URL_PLACE_ORDER = HY_URL_DOMAIN_CENTER + "/confirmOrder";
	public static final String HY_URL_CANCEL_ORDER = HY_URL_DOMAIN_CENTER + "/cancelorder";
	public static final String HY_URL_CIPHER = HY_URL_DOMAIN_CENTER + "/jiami?miwen=";

	public static final String HY_URL_CENTERGH = HY_URL_DOMAIN + "/centergh/flag?";
	public static final String HY_URL_CIPHERURL = HY_URL_DOMAIN + "/centergh/jiami?miwen=";

	public static final String WEIXIN_PUSH = "http://wx.nbrobo.com/wxPush/send.json?";
	public static final String WEIXIN_MD5KEY = "nky_0654321";// 如果要修改 对应微信也要修改

	public static final String APP_KEY = conf.getString("APP_KEY");;
	public static final String APP_SECRET = conf.getString("APP_SECRET");;
	public static final String TOKEN_SERVER = conf.getString("TOKEN_SERVER");;
	public static final String CREATE_SERVER = conf.getString("CREATE_SERVER");;
	public static final String DELETE_SERVER = conf.getString("DELETE_SERVER");;
}
