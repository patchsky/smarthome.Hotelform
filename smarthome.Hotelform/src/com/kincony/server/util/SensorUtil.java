package com.kincony.server.util;

import java.util.Calendar;
import java.util.Date;

public class SensorUtil {
	public static boolean getIndexFromArr(String val,String week)
	{
		boolean isR = false;
		String[] arrayStr = new String[] {};// 字符数组  
		arrayStr = week.split(",");// 字符串转字符数组  
		int i = 0;
		for (; i < arrayStr.length; i++) {
			if (val.equals(arrayStr[i])){
				isR = true;
				break;
			}
		}
		return isR;
	}
	
	
	public static String getWeekOfDate(Date dt) {
		String[] weekDays = {"星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日"};
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		int w = cal.get(Calendar.DAY_OF_WEEK) - 2;
		if (w < 0)
		w = 0;
		return weekDays[w];
	}
	
	public static String getWeekOfDates(Date dt) {
		String[] weekDays = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		int w = cal.get(Calendar.DAY_OF_WEEK) - 2;
		if (w < 0)
		w = 0;
		return weekDays[w];
	}
	
	public static void main(String[] args) {
		String weekOfDate = SensorUtil.getWeekOfDate(new Date());
		boolean indexFromArr = SensorUtil.getIndexFromArr(weekOfDate, "星期一,星期二,星期三,星期四,星期五,星期六,星期日,");
		if(indexFromArr)
			System.err.println("1");
		else
			System.err.println("2");
		
		
	}
}
