package com.net.nky.service.push;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.yun.core.log.YunLogEvent;
import com.baidu.yun.core.log.YunLogHandler;
import com.baidu.yun.push.auth.PushKeyPair;
import com.baidu.yun.push.client.BaiduPushClient;
import com.baidu.yun.push.constants.BaiduPushConstants;
import com.baidu.yun.push.model.AddDevicesToTagRequest;
import com.baidu.yun.push.model.AddDevicesToTagResponse;
import com.baidu.yun.push.model.CreateTagRequest;
import com.baidu.yun.push.model.CreateTagResponse;
import com.baidu.yun.push.model.DeleteDevicesFromTagRequest;
import com.baidu.yun.push.model.DeleteDevicesFromTagResponse;
import com.baidu.yun.push.model.DeleteTagRequest;
import com.baidu.yun.push.model.DeleteTagResponse;
import com.baidu.yun.push.model.DeviceInfo;
import com.baidu.yun.push.model.MsgSendInfo;
import com.baidu.yun.push.model.PushMsgToSingleDeviceRequest;
import com.baidu.yun.push.model.PushMsgToSingleDeviceResponse;
import com.baidu.yun.push.model.PushMsgToTagRequest;
import com.baidu.yun.push.model.PushMsgToTagResponse;
import com.baidu.yun.push.model.QueryDeviceNumInTagRequest;
import com.baidu.yun.push.model.QueryDeviceNumInTagResponse;
import com.baidu.yun.push.model.QueryMsgStatusRequest;
import com.baidu.yun.push.model.QueryMsgStatusResponse;
import com.baidu.yun.push.model.QueryTagsRequest;
import com.baidu.yun.push.model.QueryTagsResponse;
import com.baidu.yun.push.model.TagInfo;
import com.huilet.util.HttpUtil;
import com.net.ServiceConstants;
import com.net.util.HttpClient;
import com.net.util.HttpParamUtil;
import com.net.util.MD5Util;

import net.sf.json.JSONObject;

/**
 * 消息推送工具类.
 * 用于tag组的CURD,单个、tag的消息推送.
 * @author Ken
 * @version 2016年8月22日
 */
public class PushUtil {

	private static final Logger LOG = LoggerFactory.getLogger(PushUtil.class);

	/**
	 * 获取推送客户端.
	 */
	private static BaiduPushClient getPushClient() {
		PushKeyPair pair = new PushKeyPair(PushMetaData.API_KEY, PushMetaData.SECRET_KEY);
		// 2. build a BaidupushClient object to access released interfaces
		BaiduPushClient pushClient = new BaiduPushClient(pair, BaiduPushConstants.CHANNEL_REST_URL);

		// 3. register a YunLogHandler to get detail interacting information
		// in this request.
		pushClient.setChannelLogHandler(new YunLogHandler() {
			@Override
			public void onHandle(YunLogEvent event) {
				LOG.debug(event.getMessage());
			}
		});
		return pushClient;
	}

	/**
	 * 添加标签，标签即为小组.
	 */
	public static boolean createTag(String tagName) {
		try {
			// 4. specify request arguments
			CreateTagRequest request = new CreateTagRequest().addTagName(tagName).addDeviceType(3);
			// 5. http request  // 0 -- tag successfully build, 1 -- fail to build tag
			CreateTagResponse response = getPushClient().createTag(request);
			LOG.debug(String.format("tagName: %s, result: %d", response.getTagName(), response.getResult()));
			if (response.getResult() == 0) {
				return true;
			}
		} catch (Exception e) {
			LOG.error("添加推送Tag异常", e);
		}
		return false;
	}

	/**
	 * 删除标签，标签即为小组.
	 */
	public static boolean deleteTag(String tagName) {
		try {
			DeleteTagRequest request = new DeleteTagRequest().addTagName(tagName).addDeviceType(new Integer(3));
			// 5. http request
			DeleteTagResponse response = getPushClient().deleteTag(request);
			LOG.debug(String.format("tagName: %s, result: %d", response.getTagName(), response.getResult()));
			if (response.getResult() == 0) {
				return true;
			}
		} catch (Exception e) {
			LOG.error("删除标签异常", e);
		}
		return false;
	}

	/**
	 * 查询标签，标签即为小组.TagInfo:[ tagId tagName info type  createTime]
	 * @param maxNum 是最大的数据，不填默认是100
	 * @return null 如果没有. 
	 */
	public static List<TagInfo> queryTags(String tagName, Integer maxNum) {
		try {
			if (maxNum == null || maxNum < 1) {
				maxNum = 100;
			}
			// 4. specify request arguments
			QueryTagsRequest request = new QueryTagsRequest().addTagName(tagName).addStart(0).addLimit(10)
					.addDeviceType(maxNum);
			QueryTagsResponse response = getPushClient().queryTags(request);
			if (null == response) {
				LOG.debug(tagName + " 没有查询到数据: ");
				return null;
			}
			// Http请求结果解析打印
			LOG.debug("totalNum: " + response.getTotalNum());
			return response.getTagsInfo();
		} catch (Exception e) {
			LOG.error(" 查询标签异常", e);
		}
		return null;
	}

	/**
	 * 查询标签下的设备数.
	 * @return null 如果异常. 
	 */
	public static Integer queryDeviceNumInTag(String tagName) {
		try {
			// 4. specify request arguments
			QueryDeviceNumInTagRequest request = new QueryDeviceNumInTagRequest().addTagName(tagName).addDeviceType(3);
			// 5. http request
			QueryDeviceNumInTagResponse response = getPushClient().queryDeviceNumInTag(request);
			if (null != response) {
				LOG.debug(String.format("deviceNum: %d", response.getDeviceNum()));
			}
			return response.getDeviceNum();
		} catch (Exception e) {
			LOG.error("查询标签下的设备数异常", e);
		}
		return null;
	}

	/**
	 * 标签中添加设备
	 * @return List<String> 返回没有添加成功的数据. 
	 */
	public static List<String> addDevicesToTag(String tagName, String channelIds[]) {
		List<String> errorChannelIds = new ArrayList<String>();
		if (channelIds == null || channelIds.length < 1) {
			return errorChannelIds;
		}
		errorChannelIds.addAll(Arrays.asList(channelIds));
		try {
			// 4. specify request arguments
			AddDevicesToTagRequest request = new AddDevicesToTagRequest().addTagName(tagName).addChannelIds(channelIds)
					.addDeviceType(3);
			// 5. http request
			AddDevicesToTagResponse response = getPushClient().addDevicesToTag(request);
			// Http请求结果解析打印
			if (null != response) {
				List<DeviceInfo> devicesInfo = response.getDevicesInfoAfterAdded();
				for (DeviceInfo device : devicesInfo) {
					if (device.getResult() == 0) {
						errorChannelIds.remove(device.getChannelId());
					}
				}
			}
		} catch (Exception e) {
			LOG.error("查询标签下的设备异常", e);
		}
		return errorChannelIds;
	}

	/**
	 * 标签中移除设备
	 * @return List<String> 返回没有移除成功的数据. 
	 */
	public static List<String> deleteDevicesFromTag(String tagName, String channelIds[]) {
		List<String> errorChannelIds = new ArrayList<String>();
		if (channelIds == null || channelIds.length < 1) {
			return errorChannelIds;
		}
		errorChannelIds.addAll(Arrays.asList(channelIds));
		try {
			// 4. specify request arguments
			DeleteDevicesFromTagRequest request = new DeleteDevicesFromTagRequest().addTagName(tagName)
					.addChannelIds(channelIds).addDeviceType(3);
			// 5. http request
			DeleteDevicesFromTagResponse response = getPushClient().deleteDevicesFromTag(request);
			// Http请求结果解析打印
			if (null != response) {
				List<DeviceInfo> devicesInfo = response.getDevicesInfoAfterDel();
				for (DeviceInfo device : devicesInfo) {
					if (device.getResult() == 0) {
						errorChannelIds.remove(device.getChannelId());
					}
				}
			}
		} catch (Exception e) {
			LOG.error("移除标签下的设备异常", e);
		}
		return errorChannelIds;
	}

	/**
	 * 标签中推送消息
	 * @return List<String> 返回消息id. 
	 */
	public static String pushMsgToTag(String tagName, String message) {
		try {
			// 4. specify request arguments
			// pushTagTpye = 1 for common tag pushing
			PushMsgToTagRequest request = new PushMsgToTagRequest().addTagName(tagName).addMsgExpires(new Integer(3600))
					.addMessageType(0) // 添加透传消息
					// .addSendTime(System.currentTimeMillis() / 1000 + 120) //设置定时任务
					.addMessage(message).addDeviceType(3);
			// 5. http request
			PushMsgToTagResponse response = getPushClient().pushMsgToTag(request);
			// Http请求结果解析打印
			LOG.debug("msgId: " + response.getMsgId() + ",sendTime: " + response.getSendTime() + ",timerId: "
					+ response.getTimerId());
			return response.getMsgId();
		} catch (Exception e) {
			LOG.error("标签中推送消息异常", e);
		}
		return null;
	}

	/**
	 * 标签中推送消息结果查询.
	 * @return List<String> 返回消息推送成功的个数. 
	 */
	public static Integer queryMsgStatus(String messageId) {
		try {
			// 4. specify request arguments
			String[] msgIds = { messageId };
			QueryMsgStatusRequest request = new QueryMsgStatusRequest().addMsgIds(msgIds).addDeviceType(3);
			// 5. http request
			QueryMsgStatusResponse response = getPushClient().queryMsgStatus(request);
			// Http请求结果解析打印
			LOG.debug("totalNum: " + response.getTotalNum() + " " + "result:");
			if (null != response) {
				List<MsgSendInfo> list = response.getMsgSendInfos();
				for (MsgSendInfo msgSendInfo : list) {
					LOG.debug("{" + "msgId = " + msgSendInfo.getMsgId() + ",status = " + msgSendInfo.getMsgStatus()
							+ ",sendTime = " + msgSendInfo.getSendTime() + ",success = "
							+ msgSendInfo.getSuccessCount());
					return msgSendInfo.getSuccessCount();
				}
			}
		} catch (Exception e) {
			LOG.error("标签中推送消息查询异常", e);
		}
		return null;
	}

	/**
	 * 推送消息.
	 * @param channelId 
	 * @param 消息标题
	 * @param description 消息详情
	 * @return List<String> 返回messgeId. 
	 */
	public static String pushMsgToSingleDevice(String channelId, String title, String description,Integer msgType) {
		try {
			// 4. specify request arguments
			//创建 Android的通知
			JSONObject notification = new JSONObject();
			notification.put("title", title);
			notification.put("description", description);
			notification.put("notification_builder_id", 0);
			notification.put("notification_basic_style", 4);
			notification.put("open_type", 1);
			notification.put("url", "http://push.baidu.com");
			JSONObject jsonCustormCont = new JSONObject();
			jsonCustormCont.put("msgType", msgType); //自定义内容，key-value
			notification.put("custom_content", jsonCustormCont);

			PushMsgToSingleDeviceRequest request = new PushMsgToSingleDeviceRequest().addChannelId(channelId)
					.addMsgExpires(new Integer(3600)). // message有效时间
//					addMessageType(1).// 1：通知,0:透传消息. 默认为0 注：IOS只有通知.
					addMessage(notification.toString()).addDeviceType(3);// deviceType => 3:android, 4:ios
			// 5. http request
			PushMsgToSingleDeviceResponse response = getPushClient().pushMsgToSingleDevice(request);
			// Http请求结果解析打印
			LOG.debug("msgId: " + response.getMsgId() + ",sendTime: " + response.getSendTime());
			return response.getMsgId();
		} catch (Exception e) {
			LOG.error("推送消息到单台设备异常", e);
		}
		return null;
	}

	/**
	 * 推送消息批量.
	 * @return List<String> 返回消息推送成功的个数. 
	 * @return null表示返回有失败的情况
	 */
	public static Map<String, String> pushMsgToManyDevice(List<String> channelIds, String title, String description,Integer msgType) {
		Map<String, String> cIdMsgId = new HashMap<String, String>();
		try {
			for (String channelId : channelIds) {
				String msgId = pushMsgToSingleDevice(channelId, title, description,msgType);
				if (StringUtils.isNotEmpty(msgId)) {
					cIdMsgId.put(channelId, msgId);
				}
			}
		} catch (Exception e) {
			LOG.error("标签中批量推送消息异常", e);
		}
		if(cIdMsgId == null || cIdMsgId.size()<1){
			return null;
		}
		return cIdMsgId;
	}
	
	public static String pushMsgToWeiXin(String openids,String msg){
		Map<String,String> map = new HashMap<String,String>();
		map.put("wxid", openids);
		map.put("message", msg);
		String token = ServiceConstants.WEIXIN_MD5KEY+openids+msg;		
		map.put("token", MD5Util.MD5(token, "utf-8"));
		String str = "false:推送异常";
		try {
			LOG.info("send wx map:::"+map);
			str = HttpClient.httpPost(ServiceConstants.WEIXIN_PUSH, map);
			LOG.info("get wx result:::"+str);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str;
	}

}
