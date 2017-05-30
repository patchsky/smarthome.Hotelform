package com.kincony.server.common;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SyncSimpleDateFormat {
	
	//Store up SimpleDateFormat
	//Key=format, Value=object of DateFormat
	private static Map mapSimDateFormat = new ConcurrentHashMap(); 
	
	private SyncSimpleDateFormat() {} //Protected to new
			
	/**
	 * format - 
	 * Format a date in strFormat
	 * @param strFormat
	 * @param date
	 * @return
	 */
	public static String format(String strFormat, Date date) {
				
		DateFormat objDateFormat=getFormatObject(strFormat);
	 	synchronized(SyncSimpleDateFormat.class) { 
			return objDateFormat.format(date);
	 	}
		
	}	
	
	public static Date parse(String strFormat, String text, int intParsePosition) throws Exception {
		return SyncSimpleDateFormat.parse(strFormat, text, false, intParsePosition);
	}
	
	public static Date parse(String strFormat, String text, boolean bLenient,
														int intParsePosition) throws Exception {
		try {
			DateFormat objDateFormat=getFormatObject(strFormat);
			synchronized(SyncSimpleDateFormat.class) {
				objDateFormat.setLenient(bLenient);
	      ParsePosition pos = new ParsePosition(intParsePosition);
				return objDateFormat.parse(text, pos);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * parse -
	 * parse a text to be a date using the format
	 * @param strFormat - java format e.g yyyy-MM-dd
	 * @param text - text to be format to be a date
	 * @param bLenient : it will call DateFormat.setLenient(bLenient);
	 * @return
	 */
	public static Date parse(String strFormat, String text, boolean bLenient) throws Exception  {
		try {		
			DateFormat objDateFormat=getFormatObject(strFormat);			
			synchronized(SyncSimpleDateFormat.class) { 
				objDateFormat.setLenient(bLenient);
				return objDateFormat.parse(text);
			}
		}catch(ParseException pe) {
			throw pe;
		}				
	}
	
	/**parse -
	 * Similar to the parse(String strFormat, String text, boolean bLenient) except
	 * default value of Lenient - false
	 * @param strFormat
	 * @param text
	 * @return
	 */
	public static Date parse(String strFormat, String text) throws Exception {
		return SyncSimpleDateFormat.parse(strFormat,text,false);
	}
	
	
	 
	/**
	 * getFormatObject
	 * - get the SimpleDateFormat from the mapSimDateFormat
	 * @param strFormat
	 * @return
	 */
	private static DateFormat getFormatObject(String strFormat) {
		DateFormat objDateFormat = (DateFormat) mapSimDateFormat.get(strFormat);
		if(objDateFormat ==null) {
			//not found in cache, so create one.
			objDateFormat = new SimpleDateFormat(strFormat);
			mapSimDateFormat.put(strFormat,objDateFormat);
		}
		return objDateFormat;
	}

		 
}
