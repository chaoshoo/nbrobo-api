package com.net.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 判断时间
 * 
 * @author Administrator
 * 
 */
public class TimeJudge {

	public static void main(String[] args) {
		try {
			System.out.println(">>>>>>>>" + dayJudge("2015-08-04 23:59:59", 2));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 判断时间是否在系统时间numDay天内
	 * 
	 * @param date
	 *            传入的时间
	 * @param numDay
	 *            传入的天数
	 * @throws Exception
	 */
	public static boolean dayJudge(String date, int numDay)
			throws ParseException {
		if (numDay < 0) {
			return false;
		}
		SimpleDateFormat adf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat adfNow = new SimpleDateFormat("yyyy-MM-dd");
		// 得到毫秒
		Long day = numDay * 24 * 60 * 60 * 1000L;

		// 传入的Date
		Long time = adf.parse(date).getTime();

		// 当前系统年月日
		Long nowTime = adfNow.parse(nowDay()).getTime();
		// 当前最后的时间
		Long endTime = nowTime + 24 * 60 * 60 * 1000L;

		Long startDay = endTime - day;

		if (time > startDay && time < endTime) {
			return true;
		}
		return false;
	}

	/**
	 * 得到当前系统的年月日
	 * 
	 * @return
	 */
	public static String nowDay() {
		SimpleDateFormat adf = new SimpleDateFormat("yyyy-MM-dd");
		adf.format(System.currentTimeMillis());
		return adf.format(System.currentTimeMillis());
	}
	
	/**
	 * 计算两个时间差
	 * 返回为秒数
	 * @return
	 */
	public static long dateAndDate(Date beginDate, Date endDate) {
		
		return (endDate.getTime()-beginDate.getTime())/1000;
	}
	
	/**
	 * 计算两个时间差
	 * 返回为秒数
	 * @return
	 */
	public static long dateAndDate(String beginDate, String endDate) {
		SimpleDateFormat adf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date begin = new Date();
		Date end = new Date();
		try {
			begin = adf.parse(beginDate);
			end = adf.parse(endDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return (end.getTime()-begin.getTime())/1000;
	}
	
	/**
	 * 计算两个时间差
	 * 返回为秒数
	 * @return
	 */
	public static long dateAndDate(String beginDate, Date endDate) {
		SimpleDateFormat adf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date begin = new Date();
		try {
			begin = adf.parse(beginDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return (endDate.getTime()-begin.getTime())/1000;
	}
}
