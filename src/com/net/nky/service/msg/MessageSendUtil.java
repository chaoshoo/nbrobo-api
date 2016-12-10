package com.net.nky.service.msg;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.net.nky.service.cache.MessageCacheSingleton;
import com.taobao.api.ApiException;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;

/**
 * 短信验证码.
 * @author Ken
 * @version 2016年8月18日
 */
public class MessageSendUtil {

	static final Logger LOG = LoggerFactory.getLogger(MessageSendUtil.class);

	/**
	 * 发送短信验证码 注册.
	 * @param tel 接受短信的号码.
	 * @return  对象中:
	 * 		 success; //是否发送成功  
	 * 		 verifycode; //发送成功的验证吗 
	 * 		message;//发送失败的原因，和发送成功时为：短信发送成功
	 * <p><b>示例</b>
	 *  <p>短信：【注册验证】验证码491395，您正在注册成为沃博健康用户，感谢您的支持！ 
	 *  <p>对象返回：[success=true,verifycode=491395,message=短信发送成功]
	 *  <p>示例:System.out.println(sendRegistMessage("13367241859") );
	 */
	public static MessageResultVo sendRegistMessage(String tel) {
		MessageResultVo vo = new MessageResultVo(false, getRandom6());//"短信发送成功"
		try {
			//对1分钟内发出多条，和一条内超过指定条数处理
			if (!MessageCacheSingleton.getSingleton().getPhonegap(tel)) {
				vo.setSuccess(false);
				vo.setMessage(MessageMetaData.ERROR_FREQ);
	            LOG.warn("mobile send message gap 10 seconds: {}" ,tel );
	            return vo;
	        } else if (!MessageCacheSingleton.getSingleton().getPhonedayCache(tel)) {
				vo.setSuccess(false);
				vo.setMessage(MessageMetaData.ERROR_FREQ_DAY);
	            LOG.warn("mobile send message 10 times one day :{} " , tel);
	            return vo;
	        }
			
			AlibabaAliqinFcSmsNumSendRequest req = MessageMetaData.getRequest(tel);
			req.setSmsFreeSignName(MessageMetaData.register.FREESIGNNAME);
			req.setSmsParamString("{code:'" + vo.getVerifycode() + "',product:'" + MessageMetaData.SIGN + "'}");
			req.setSmsTemplateCode(MessageMetaData.register.SMSTEMPLATECODE);
			AlibabaAliqinFcSmsNumSendResponse rsp = MessageMetaData.getMessageClient().execute(req);

			LOG.error("{}短信发送状态:{}", tel, rsp.getBody());
			if (rsp.getResult().getSuccess()) {
				vo.setSuccess(true);
				return vo;
			} else {
				LOG.error(tel+"短信发送失败:{}", rsp.getBody());
			}
		} catch (ApiException e) {
			LOG.error(tel+"短信发送异常,API调用异常:{}", e);
		} catch (Exception e) {
			LOG.error(tel+"短信发送异常:{}", e);
		}
		return vo;
	}

	/**
	 * 发送短信验证码 登录密码.
	 * @param tel 接受短信的号码.
	 * @return  对象中:
	 * 		 success; //是否发送成功  
	 * 		 verifycode; //发送成功的验证吗 
	 * 		message;//发送失败的原因，和发送成功时为：短信发送成功
	 * <p><b>示例</b>
	 *<p> 短信示例：【身份验证】验证码189733，您正在尝试修改沃博健康登录密码，请妥善保管账户信息。
	 *<p> 对象返回示例：[success=true,verifycode=189733,message=短信发送成功]
	 *<p> 示例:System.out.println(sendChangePwdMessage("13367241859") );
	 */
	public static MessageResultVo sendChangePwdMessage(String tel) {
		MessageResultVo vo = new MessageResultVo(false, getRandom6());
		try {
			//对1分钟内发出多条，和一条内超过指定条数处理
			if (!MessageCacheSingleton.getSingleton().getPhonegap(tel)) {
				vo.setSuccess(false);
				vo.setMessage(MessageMetaData.ERROR_FREQ);
	            LOG.warn("mobile send message gap 10 seconds: {}" ,tel );
	            return vo;
	        } else if (!MessageCacheSingleton.getSingleton().getPhonedayCache(tel)) {
				vo.setSuccess(false);
				vo.setMessage(MessageMetaData.ERROR_FREQ_DAY);
	            LOG.warn("mobile send message 10 times one day :{} " , tel);
	            return vo;
	        }
			
			AlibabaAliqinFcSmsNumSendRequest req = MessageMetaData.getRequest(tel);
			req.setSmsFreeSignName(MessageMetaData.resetPwd.FREESIGNNAME);
			req.setSmsParamString("{code:'" + vo.getVerifycode() + "',product:'" + MessageMetaData.SIGN + "'}");
			req.setSmsTemplateCode(MessageMetaData.resetPwd.SMSTEMPLATECODE);
			AlibabaAliqinFcSmsNumSendResponse rsp = MessageMetaData.getMessageClient().execute(req);

			LOG.error("{}短信发送状态:{}", tel, rsp.getBody());
			if (rsp.getResult().getSuccess()) {
				vo.setSuccess(true);
				return vo;
			} else {
				LOG.error(tel+"短信发送失败:{}", rsp.getBody());
			}
		} catch (ApiException e) {
			LOG.error(tel+"短信发送异常,API调用异常:{}",e);
		} catch (Exception e) {
			LOG.error(tel+"短信发送异常:{}", e);
		}
		return vo;
	}
	
	/**
	 * 发送短信挂号.
	 */
	public static MessageResultVo sendGh(String tel,String  hospital , String  depart,String  doctor,String  date,String  num) {
		MessageResultVo vo = new MessageResultVo(false, getRandom6());
		try {
			AlibabaAliqinFcSmsNumSendRequest req = MessageMetaData.getRequest(tel);
			req.setSmsFreeSignName("健康管理服务平台");
			String reqparam  = "{code:'" + vo.getVerifycode() + "',product:'" + MessageMetaData.SIGN + "'"+
					",hospital:'" + hospital + "'"+ 
					",depart:'" + depart + "'"+ 
					",doctor:'" + doctor + "'"+ 
					",date:'" + date + "'"+ 
					",num:'" + num + "'"+ 
					"}";
			req.setSmsParamString(reqparam);
			
			req.setSmsTemplateCode("SMS_15475693");
			AlibabaAliqinFcSmsNumSendResponse rsp = MessageMetaData.getMessageClient().execute(req);
			
			LOG.error("{}短信发送状态:{}", tel, rsp.getBody());
			if (rsp.getResult().getSuccess()) {
				vo.setSuccess(true);
				return vo;
			} else {
				LOG.error(tel+"短信发送失败:{}", rsp.getBody());
			}
		} catch (ApiException e) {
			LOG.error(tel+"短信发送异常,API调用异常:{}",e);
		} catch (Exception e) {
			LOG.error(tel+"短信发送异常:{}", e);
		}
		return vo;
	}

	/**
	 * 获取6位随机验证码.
	 */
	public static String getRandom6() {
		Random random = new Random();
		String code6 = String.valueOf(100000 + random.nextInt(899999));
		return code6;
	}

	/**
	 * 手机号验证
	 * @param  str
	 * @return 验证通过返回true
	 */
	public static boolean isMobile(String str) {
		if (StringUtils.isEmpty(str) || str.length() != 11) {
			return false;
		}
		Pattern p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$"); // 验证手机号
		Matcher m = p.matcher(str);
		return m.matches();
	}

	public static void main(String[] args) {
		//短信：【注册验证】验证码491395，您正在注册成为沃博健康用户，感谢您的支持！ 
		//对象返回：[success=true,verifycode=491395,message=短信发送成功]
		//System.out.println(sendRegistMessage("13367241859") );
		System.out.println(sendGh("13367241859","同济" , "普通", "正","2016-09-22", "2") );

		//短信：【身份验证】验证码189733，您正在尝试修改沃博健康登录密码，请妥善保管账户信息。
		//对象返回：[success=true,verifycode=189733,message=短信发送成功]
		//System.out.println(sendChangePwdMessage("13367241859") );
	}

}
