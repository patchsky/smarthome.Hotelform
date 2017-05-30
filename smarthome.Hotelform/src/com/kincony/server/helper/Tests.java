package com.kincony.server.helper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Tests {
	
	public static String getWeekOfDate(Date dt) {
		String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0)
		w = 0;
		return weekDays[w];
	}
	
	public static boolean isInDate(Date date, String strDateBegin,  
	        String strDateEnd,String s) {  
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
	    String strDate = sdf.format(date);  
	    // 截取当前时间时分秒  
	    int strDateH = Integer.parseInt(strDate.substring(11, 13));  
	    
	    // 截取开始时间时分秒  
	    int strDateBeginH = Integer.parseInt(strDateBegin);  
	    
	    // 截取结束时间时分秒  
	    int strDateEndH = Integer.parseInt(strDateEnd);
	   
	    
	    int ds  = Integer.parseInt(s);
	    if(ds==1){
	    	if (strDateH >= strDateBeginH && strDateH <= strDateEndH ) { 
		        // 当前时间小时数在开始时间和结束时间小时数之间   
		        return true;
		    } else {
		        return false;  
		    }  
	    }else{
	    	 return false; 
	    }
	    
	}  
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		boolean inDate = isInDate(new Date(), "10", "4", "1");
		String weekOfDate = getWeekOfDate(new Date());
		if(weekOfDate.equals("星期三")){
			if(inDate){
				System.err.println("1");
			}else{
				System.err.println("2");
			}
		}else{
			System.err.println("星期不对");
		}
		
	}

}
