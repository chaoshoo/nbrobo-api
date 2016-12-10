package com.net.util;

import java.util.HashMap;
import java.util.Map;


public class MapUitl {

	public static String getMapBykey(Map map,String key) {
		String result="";
		if(map==null){
			return result;
		}
		if(map.containsKey(key)){
			result=map.get(key).toString();
		}else {
			return "";
		}
		
		return result;
	}
	
	
	
	public static void main(String[] args) {
		Map map = new HashMap();
		map.put("a", 1111);
		System.out.println(getMapBykey(map, "b"));
	}
}
