/**
 * Package	: com.excelhk.wucommon.common
 * File	: SyncDecimalFormat.java
 *
 * Company 	: Excel Technology International (Hong Kong) Limited
 * Team    	: UTS
 * Description 	: 
 *
 * The contents of this file are confidential and proprietary to Excel.
 * Copying is explicitly prohibited without the express permission of Excel.
 *
   * Create Date	: 20050503
   * Create By	: Terence Tse
 */

package com.kincony.server.common;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Map;

public class SyncDecimalFormat {

	private static Map mapDecimalFormat = new SyncMap(); 
	
	private SyncDecimalFormat() {} //Protected to new
			
	/**
	 * format - 
	 * Format a number in strFormat
	 * @param strFormat
	 * @param number
	 * @return
	 */
	public static String format(String strFormat, Object object) {
		DecimalFormat objDecimalFormat = getFormatObject(strFormat);
	 	synchronized(SyncDecimalFormat.class) { 
			return objDecimalFormat.format(object);
	 	}
	}	
	
	public static String format(String strFormat, Number number) {
		DecimalFormat objDecimalFormat = getFormatObject(strFormat);
	 	synchronized(SyncDecimalFormat.class) { 
			return objDecimalFormat.format(number);
	 	}
	}	
	
	public static String format(String strFormat, long lNumber) {
		DecimalFormat objDecimalFormat = getFormatObject(strFormat);
	 	synchronized(SyncDecimalFormat.class) { 
			return objDecimalFormat.format(lNumber);
	 	}
	}	
	
	public static String format(String strFormat, double dblNumber) {
		DecimalFormat objDecimalFormat = getFormatObject(strFormat);
	 	synchronized(SyncDecimalFormat.class) { 
			return objDecimalFormat.format(dblNumber);
	 	}
	}	
	
	/**
	 * parse -
	 * parse a text to be a number using the format
	 * @param strFormat - java format e.g yyyy-MM-dd
	 * @param text - text to be format to be a number
	 * @param bLenient : it will call DecimalFormat.setLenient(bLenient);
	 * @return
	 */
	public static Number parse(String strFormat, String text) throws Exception  {
		try {		
			DecimalFormat objDecimalFormat = getFormatObject(strFormat);			
			synchronized (SyncDecimalFormat.class) { 
				return objDecimalFormat.parse(text);
			}
		} catch (ParseException pe) {
			throw pe;
		}				
	}
		 
	/**
	 * getFormatObject
	 * - get the DecimalFormat from the mapDecimalFormat
	 * @param strFormat
	 * @return
	 */
	private static DecimalFormat getFormatObject(String strFormat) {
		DecimalFormat objDecimalFormat = (DecimalFormat) mapDecimalFormat.get(strFormat);
		if(objDecimalFormat == null) {
			//not found in cache, so create one.
			objDecimalFormat = new DecimalFormat(strFormat);
			mapDecimalFormat.put(strFormat, objDecimalFormat);
		}
		return objDecimalFormat;
	}

}


/**
 * $Revision: 1.1 $
 * $History: SyncDecimalFormat.java $
 * 
 * *****************  Version 1  *****************
 * User: Aleung       Date: 11/04/06   Time: 14:47
 * Created in $/UTS3.5/CORE/Source/src/com/excelhk/wucommon/common
 * 
 * *****************  Version 2  *****************
 * User: Terence      Date: 3/05/05    Time: 16:24
 * Updated in $/COREMERGE_DEV/Source/src/com/excelhk/wucommon/common
 * 
 * *****************  Version 1  *****************
 * User: Terence      Date: 3/05/05    Time: 15:43
 * Created in $/COREMERGE_DEV/Source/src/com/excelhk/wucommon/common
 */