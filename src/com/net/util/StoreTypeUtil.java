package com.net.util;


import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

public class StoreTypeUtil {
	/**
	 * 
	 * @param dpId
	 * @return 0:管理员  1：总店  2：分店 3：单店，后期再放入单利中
	 */
	public static int getType(String dpId){
		if("1".equals(dpId)){
			return 0;
		}
		String sql="select parent from department where dp_id='"+dpId+"'";
	    Record re = Db.findFirst(sql); 
		String parent = re.getStr("parent")+"";
		if(parent.equals("yes")){
		    //有分店为总店，无分店为单店
			String ss="select count(1) as num from department where parent_id='"+dpId+"'";
			Record res = Db.findFirst(ss);
			if("0".equals(res.get("num")+"")){
				return 3;
			}else{
				return 1;
			}
		}else if(parent.equals("no")){
			return 2;
		}
		return 2;
	}
	/**
	 * 通过分店dpid获取该分店的总店dpid
	 * @param dpId
	 * @return
	 */
	public static String getDpId(String dpId){
		String sql="select parent_id from department where dp_id='"+dpId+"'";
	    Record re = Db.findFirst(sql); 
	    String did = re.get("parent_id")+"";
		return did;
	}
}
