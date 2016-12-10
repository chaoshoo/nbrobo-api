package com.net.util;


/**
 * 获取时间戳
 * @author zw
 *
 */
public class DateUtil {

	
	public static String getNowStp(){
		long nowstp = System.currentTimeMillis();
		return Long.toString(nowstp); 
	}
}
