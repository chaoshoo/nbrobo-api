package com.net.nky.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.net.nky.service.push.PushUtil;

/**
 * 推送消息线程.
 * @author Ken
 * @version 2016年9月29日
 */
public class PushMessageThread implements Runnable {

	Logger Log = LoggerFactory.getLogger(this.getClass());

	private Object lock = new Object();
	final static String checkSql = " select m.title,m.content,v.android_tv_channel_id,v.wxopenid,v.vip_code,v.real_name,c.id,c.msg_type"
		+ " from message_center c "
		+ " left join message m  on c.message_id = m.id "
		+ " left join t_vip v on c.reciver = v.id"
		+ " where c.send_time is null order by c.create_time asc limit 100 ";
	@Override
	public void run() {
//		Log.debug("推消息 线程");
		synchronized (lock) {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			List<Record> toBePushedList = Db.find(checkSql);
			if(null != toBePushedList && toBePushedList.size()>0){
//				Log.debug("有需要推的消息:{}",toBePushedList.size());
				//遍历，把消息和接受者信息都查询出来
				for(Record r : toBePushedList){
					String channeId = r.getStr("ANDROID_TV_CHANNEL_ID");//android_tv_token_id
					String wxopenid = r.getStr("WXOPENID");
					String vipCode = r.getStr("VIP_CODE");
					String title = r.getStr("TITLE");
					String content = r.getStr("CONTENT");
					Integer msgtype = r.getInt("MSG_TYPE");
					Long messageId = r.getLong("ID");
					//android push
					boolean pushed = false;
					if(StringUtils.isNotEmpty(channeId)){
						Log.debug("{} 推送消息请求到用户android ：{}",vipCode,channeId);
//						PushUtil.addDevicesToTag("testTag", new String[] {channeId });
						String msgId = PushUtil.pushMsgToSingleDevice(channeId, title, content,msgtype);
						if(StringUtils.isNotEmpty(msgId)){
							pushed = true; 
						}
					}
					//wx push
					if(StringUtils.isNotEmpty(wxopenid)){
						if(wxopenid.endsWith("-")){
							wxopenid = wxopenid.substring(0, wxopenid.length()-1);
						}
							Log.debug("{} 推送消息请求到用户微信:{}",vipCode,wxopenid);
							String str = PushUtil.pushMsgToWeiXin(wxopenid, content);
							if(str.startsWith("true")){
								pushed = true; 
							}
					}
					if(pushed){
						int flag = Db.update("update message_center set send_time = now() where id = ? ",messageId);
						Log.debug("{} 推送消息请求到用户:{}",vipCode,flag);
					}
				}
			}else{
				Log.debug("推消息-没有需要推送的消息");
			}
			
		}
	}
}
