package com.net.util;

import java.util.Random;


/**
 * 生成随机数,编码,序列
 * @author YC
 *
 */
public class CodeUtil {
	
	/**
	 * 获得指定长度的随机码
	 * @param len
	 * @return
	 */
	public static String getRandomCode(int len){
		StringBuffer rntStr=new StringBuffer();
		Random r=new Random();
		for(int i=0;i<len;i++){
			rntStr.append(r.nextInt(10));
		}
		return rntStr.toString();
	}
	
}
