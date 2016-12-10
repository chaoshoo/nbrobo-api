package com.net.nky.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.apache.http.impl.cookie.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.net.nky.service.RemoteRefuseService;
@Service
public class RemoteRefuseServiceImpl implements RemoteRefuseService {
	private Logger LOG = LoggerFactory.getLogger(RemoteRefuseServiceImpl.class);
	@Override
	public String refuseTime() {
		LOG.info("--------start refuseTime------------");
		String sql = "select v.id,d.id as did,d.name,DATE_FORMAT(r.order_time,\"%Y-%m-%d %H:%s\") as yutime from remote_inspect r,t_vip v,doctor d "
				+ " where r.vip_code=v.vip_code and r.doctor_code=d.code and r.order_time>=? and r.order_time <= ?";
		long time = new Date().getTime();
		Date start = new Date(time-20*60*1000);
		Date end = new Date(time-10*60*1000);
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss "); 
		  sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai")); 
		LOG.debug("start "+sdf.format(new Date())+"------------");
		LOG.debug("start "+sdf.format(start)+"------------");
		LOG.debug("end "+sdf.format(end)+"------------");
		List<Record> list =  Db.find(sql,sdf.format(start),sdf.format(end));
		if(!list.isEmpty()){
			for (Record r: list) {				
				saveMessage(r.get("did")+"", r.get("id")+"", "你预约"+r.get("name")+"时间"+r.get("yutime")+"快到了，请做好准备。");
			}
		}
		return "ok";
	}
	

	private boolean saveMessage(String sender,String reciver,String content){
		try {
			Record r1 = new Record();
			//msg_type 为1 表示文本
			r1.set("msg_type", "1").set("creator", sender).set("title", "提示").set("content", content)
			.set("create_time", new Date());
			boolean flag = Db.save("message", r1);
			if(flag){
				Record r2 = new Record();
				r2.set("msg_type", "1").set("message_id", r1.get("id")).set("reciver", reciver).set("create_time", new Date());
				flag = Db.save("message_center", r2);
			}
			return flag;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
	}

}
