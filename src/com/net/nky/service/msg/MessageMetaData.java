package com.net.nky.service.msg;

import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;

/**
 * 短信验证码基本数据.
 * @author Ken
 * @version 2016年8月18日
 */
public class MessageMetaData {

	private static final String URL = "http://gw.api.taobao.com/router/rest";
	private static final String APPKEY = "23436123";
	private static final String SECRET = "5c406f52c9fc657c5a5151944b4eef0e";
	static final String SIGN = "沃博健康";
	static final String OK = "短信发送成功";
	static final String ERROR = "短信发送失败";
	static final String ERROR_FREQ = "短信发送失败，请稍后再试！";
	static final String ERROR_FREQ_DAY = "短信发送失败，号码短信发送超过限定条数！";

	/**
	 * 获取发送短信对象.
	 */
	static TaobaoClient getMessageClient() {
		return new DefaultTaobaoClient(URL, APPKEY, SECRET);
	}

	/**
	 * 获取发送短信request的对象.
	 * @param tel 接受短信的号码.
	 */
	static AlibabaAliqinFcSmsNumSendRequest getRequest(String tel) {
		AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
		req.setExtend("");
		req.setSmsType("normal");
		req.setRecNum(tel);
		return req;
	}

	/**
	 * 注册.
	 * @author Ken
	 * @version 2016年8月18日
	 */
	interface register {
		static final String FREESIGNNAME = "注册验证";
		static final String SMSTEMPLATECODE = "SMS_13180418";
	}

	/**
	 * 修改密码
	 * @author Ken
	 * @version 2016年8月18日
	 */
	interface resetPwd {
		static final String FREESIGNNAME = "身份验证";
		static final String SMSTEMPLATECODE = "SMS_13180416";
	}

}
