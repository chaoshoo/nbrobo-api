package com.net.util;

/**
 * 系统参数配置
 * 
 * huilet 2013-4-11
 * 
 * @author yuanc
 */
public class Contants {
	
	/**
	 * redis中存放字典列表的key值,DicSingleton
	 */
	public static final String KEY_DIC ="dic";
	/**
	 * redis中存放字典列表的key值,SysSingleton
	 */
	public static final String SYS_PARA ="sysPara";
	/**
	 * redis中存放department的key值,DepartmentSingleton
	 */
	public static final String KEY_DPLIST ="dpList";
	/**
	 * redis中存放department的key值,DepartmentSingleton
	 */
	public static final String KEY_DPMAP ="dpMap";
	/**
	 * redis中存放department的key值,DepartmentSingleton
	 */
	public static final String KEY_SYSLIST ="sysList";
	/**
	 * redis中存放t_device_attr的key值,DeviceAttrSingleton
	 */
	public static final String KEY_ATTR ="listAttr";

	/**
	 * 计算短信所需费用
	 * @param price
	 * @param content
	 * @return
	 */
	public static float getPrice(float price, String content) {
		int length = content.length();
		if(length<=61) {
			return price;
		}else if(length>61 && length <=130){
			return price*2;
		}else if(length>130 && length <=200) {
			return price*3;
		}
		return price*(length/60+1);
	}
	
}
