package com.net.nky.singleton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.google.common.collect.Maps;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.net.nky.service.RedisService;
import com.net.util.Contants;
import com.net.util.JsonUtil;
import com.net.util.SpringUtil;

/**
 * 下拉列表字典--单例模式
 * 
 * @author yc 2011-3-28
 * @Version CECTManegeServer
 */
public class DicSingleton {

	private static DicSingleton instance = null;

	private  RedisService redisService = null;

	public static DicSingleton getInstance() {
		if (instance == null) {
			instance = new DicSingleton();
		}
		return instance;
	}

	private DicSingleton() {
		redisService = (RedisService) SpringUtil.getBean("redisService");
		loadData(0);
	}

	/**
	 * 加载数据
	 */
	private void loadData(int flag) {
		if (flag == 0 && redisService.exists(Contants.KEY_DIC)) {
			Object obj = redisService.getObj(Contants.KEY_DIC.getBytes());
			if ("error".equals(obj)) {
				setData();
			}
		} else {
			setData();
		}

	}

	/**
	 * 赋值
	 */
	public void setData() {
		String sql = "select *  from  dic";
		List<Record> list = Db.find(sql);

		redisService.setObj(Contants.KEY_DIC.getBytes(), JsonUtil.getJsonObjByjfinalList(list).toString());
	}

	//	public Map<String,Object> getObj(String... keys){
	//		Map<String,Object> map = new HashMap<String,Object>();
	//		RedisService redisService = (RedisService) SpringUtil.getBean("redisService");
	//		for(String key:keys){
	//			if(redisService.exists(key)){
	//				map.put(key,redisService.getObj(key.getBytes()));
	//			}
	//		}
	//		
	//		if(map.size()<keys.length){
	//			map=null;
	//		}
	//		return map;
	//	}

	/**
	 * 重新加载
	 */
	public void reloadData() {

		loadData(1);
	}

	/**
	 * 根据type获取下拉列表
	 * 
	 * @param type
	 * @return
	 */
	public List getDic(String type) {
		JSONArray dic_array = JSONArray.fromObject(redisService.getObj(Contants.KEY_DIC.getBytes()));
		List list = new ArrayList();
		for (int i = 0; i < dic_array.size(); i++) {
			JSONObject o = dic_array.getJSONObject(i);
			if (type.equals(o.getString("dic_type"))) {
				HashMap map = new HashMap();
				map.put("dic_name", o.getString("dic_name"));
				map.put("dic_value", o.getString("dic_value"));
				map.put("dic_remark", o.getString("dic_remark"));
				list.add(map);
			}
		}
		return list;
	}

	public Map<String, String> getDicMap(String type) {
		JSONArray dic_array = JSONArray.fromObject(redisService.getObj(Contants.KEY_DIC.getBytes()));
		Map<String, String> map = Maps.newHashMap();
		for (int i = 0; i < dic_array.size(); i++) {
			JSONObject o = dic_array.getJSONObject(i);
			if (type.equals(o.getString("dic_type"))) {
				map.put(o.get("dic_name").toString(), o.get("dic_value").toString());
			}
		}
		return map;
	}

	/**
	 * 根据dic类型，加key值获取value
	 * 
	 * @param dicType
	 *            类型
	 * @param keyValue
	 *            键值
	 * @return huilet 2013-4-17
	 * @author yuanc
	 */
	public String getValueBykeyDic(String dicType, String keyValue) {
		String value = "";
		List<HashMap> list = getDic(dicType);
		for (HashMap map : list) {
			String key = (String) map.get("dic_name");
			if (keyValue.equals(key)) {
				value = (String) map.get("dic_value");
			}
		}
		return value;
	}
}
