package com.net.nky.service.push;

/**
 * 消息推送的基本包.
 * @author Ken
 * @version 2016年8月22日
 */
public class PushMetaData {

	/*确保使用的javaSE-1.6及兼容版本。 将libs和output下的jar包添加到工程路径。
	 * 支持的域名为：“api.tuisong.baidu.com”，“api.push.baidu.com”。
	 */
//	protected static final String API_ID = "4956964";
	
	//API key (AK) : 一个应用的公钥及唯一标识。用于在使用百度开发者服务时标记一个应用，创建后不能修改。
	public static final String API_KEY = "iO2Kd9I9Sv2Cn4Djmm8YFBaD";
	
//	//Secret key (SK) : 对应一个AK的密钥,用于在使用百度开发者服务时生成签名或加密数据,开发者可以随时进行重置. 请务必确认SK的内容不会泄露给第三方,否则可能产生安全问题,如确认SK被泄露,请立即在开发者中心进行重置。
	public static final String SECRET_KEY = "cvvLSHGwBRsXzIkqGrTnTm0qcfMtLrFw";
	
//	public static final String API_KEY = "FWV9lIEHpw7VgSNxLWaux3Q9";
//	public static final String SECRET_KEY = "dxKYVWeCGvWPglkSjXd0pm6cUGsA33oC";
	
	//channelId: 设备的唯一标识, 在推送消息时,用于指定消息的目标接收设备。
	
	// appid=4956964 userId=1080462447920975324 channelId=3830725647211086088 requestId=3503859319
	
}
