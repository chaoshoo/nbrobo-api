package com.net.util;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.jfinal.plugin.activerecord.Db;

public class DataSqlUtil {
	
	/**
	 * 根据Map的key值和value值动态插入数据
	 * @param tableName
	 * @param data
	 * @return
	 */
	public static int insertDate(String tableName, Map<String, String> data) {
		String keys = "";
		String values = "";
		Iterator iter = data.entrySet().iterator();
		while (iter.hasNext()) {
			Entry entry = (Entry) iter.next();
			String key = entry.getKey() + "";
			String val = entry.getValue() + "";
			keys += key + ",";
			values += "'" + val + "',";
		}
		if(keys.endsWith(",")) {
			keys = keys.substring(0, keys.length()-1);
			values = values.substring(0, values.length()-1);
		}
		String sqlInsert = "insert into "+tableName+" ("+keys+") values ("+values+")";
		return Db.update(sqlInsert);
	}

}
