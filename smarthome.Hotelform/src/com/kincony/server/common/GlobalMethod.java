package com.kincony.server.common;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public final class GlobalMethod {

	public static String removeLeadingChar(String strSource, char leadChar) {
		if (strSource != null) {
			if (strSource.startsWith(String.valueOf(leadChar))) {
				int length = strSource.length();
				for (int i = 0; i < length; i++) {
					if (strSource.charAt(i) != leadChar) {
						return strSource.substring(i, length);
					}
				}
				return GlobalConst.CONST_STRING_EMPTY;
			}
		}
		return strSource;
	}

	public static String removeLeadingZero(String strSource) {
		return removeLeadingChar(strSource, '0');
	}

	public static String addLeadingZero(int source, int intTotalCount) {
		String strSource = String.valueOf(source);
		return addLeadingZero(strSource, intTotalCount);
	}
	
	public static String addLeadingZero(String strSource, int intTotalCount) {
		int intCount = intTotalCount - strSource.length();
		String strFormat = strSource;
		for (int i = 0; i < intCount; i++) {
			strFormat = "0" + strFormat;
		}
		return strFormat;
	}

	/**
     * Description : Return the 0 if the parameter is null, otherwise, return
     * the parameter in Double
     * 
     * @param objValue
     *            Object
     */
    public static BigDecimal ifNullBigDec(Object objValue) {
        BigDecimal bdValue = new BigDecimal(0);
        try {
            if (!isNullorEmpty(objValue)) {
                bdValue = (java.math.BigDecimal) DataUtil.convertValue(
                        java.math.BigDecimal.class, objValue);
            }
            return bdValue;
        } catch (Exception e) {
            System.out.println(e);
            return bdValue;
        }
    }
    
	/**
	 * Description : Return true if input object is null
	 * 
	 * @param objValue
	 *            Object
	 * @return boolean
	 */
	public static boolean isNull(Object objValue) {
		return (objValue == null);
	}

	/**
	 * Description : Return true if input object is null or empty
	 * 
	 * @param objValue
	 *            Object
	 * @return boolean
	 */
	public static boolean isNullorEmpty(Object objValue) {
		if (objValue == null) {
			return true;
		} else if (objValue instanceof String) {
			return ((String) objValue).length() == 0;
		} else if (objValue instanceof List) {
			return ((List) objValue).isEmpty();
		} else if (objValue instanceof Map) {
			return ((Map) objValue).isEmpty();
		} else {
			return (objValue.toString().length() == 0);
		}
	}

	/**
	 * Description : Return true if object type is numeric
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isNumericType(Object value) {
		boolean result = false;
		if (value instanceof Integer) {
			result = true;
		} else if (value instanceof Long) {
			result = true;
		} else if (value instanceof Double) {
			result = true;
		} else if (value instanceof Float) {
			result = true;
		} else if (value instanceof BigDecimal) {
			result = true;
		}
		return result;
	}
	
	public static boolean isNumericString(Object obj,
            boolean baReturnValueIfNull) {
        if (isNullorEmpty(obj)) {
            return baReturnValueIfNull;
        }
        boolean bResult = true;
        String str = (obj instanceof String ? (String) obj : obj.toString());
        int intLength = str.length();
        for (int i = 0; bResult && i < intLength; i++) {
            char c = str.charAt(i);
            bResult = Character.isDigit(c);
        }
        return bResult;
    }
	
	/**
     * Get the current date and time, return VerifyPhoneNum a formatted string
     * 
     * @param strFormat
     *            the string format e.g. yyyy-MM-dd
     */
    public static String getCurrentDateTime(String strFormat) {
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        return SyncSimpleDateFormat.format(strFormat, ts);
    }
    
	public static String formatDate(Date date, String format , Locale locale) {
        if (date == null) {
            return GlobalConst.CONST_STRING_EMPTY;
        }
        if (format == null) {
            format = GlobalConst.FORMAT_DATE;
        }
       
        SimpleDateFormat df = new SimpleDateFormat(format, locale);
        return df.format(date);
    }
    
	public static Date format(Date date){
		String defaultFormatString = GlobalConst.FORMAT_DATE;
    	return format(date, defaultFormatString);
    }
	
    public static Date format(Date date, String format){
    	return formatDate(formatDate(date, format));
    }

    public static String formatDate(Date date) {
        String defaultFormatString = GlobalConst.FORMAT_DATE;
        return formatDate(date, defaultFormatString);
    }

    public static Date formatDate(String data) {
        String defaultFormatString = GlobalConst.FORMAT_DATE;
        return formatDate(data, defaultFormatString);
    }
    
    public static String formatDate(Date date, String format) {
        if (date == null) {
            return GlobalConst.CONST_STRING_EMPTY;
        }
        if (format == null) {
            format = GlobalConst.FORMAT_DATE;
        }
        return SyncSimpleDateFormat.format(format, date);
    }

    public static Date formatDate(String data, String format){

        if (data == null) {
            return null;
        }
        if (format == null) {
            format = GlobalConst.FORMAT_DATE;
        }
        Date dt = null;
		try {
			dt = SyncSimpleDateFormat.parse(format, data);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return dt;
    }
    
    /**
     * Description : get max date from two input date
     * 
     * @param dt1
     *            Date
     * @param dt2
     *            Date
     * @return Date
     */
    public static Date getMaxDate(Date dt1, Date dt2) {
        if (dt1 == null)
            return dt2;
        if (dt2 != null) {
            return ((GlobalMethod.getDateDiff(dt1, dt2) > 0) ? dt2 : dt1);
        }
        return null;
    }

    /**
     * Description : get min date from two input date
     * 
     * @param dt1
     *            Date
     * @param dt2
     *            Date
     * @return Date
     */
    public static Date getMinDate(Date dt1, Date dt2) {
        if (dt1 == null)
            return dt2;
        if (dt2 != null) {
            return ((GlobalMethod.getDateDiff(dt1, dt2) < 0) ? dt2 : dt1);
        }
        return null;
    }

    /**
     * Get the day difference between two calendar dates
     * 
     * @param dtStartDate
     *            start date (util.date)
     * @param dtEndDate
     *            end date (util.date)
     */
    public static int getDateDiff(Date dtStartDate, Date dtEndDate) {
        long StartTime = dtStartDate.getTime(); // gets milliseconds of start
                                                // date
        long EndTime = dtEndDate.getTime(); // gets milliseconds of end date

        // base on fact that 86400000 milliseconds per day
        // date.getTime() which gives you the number of milliseconds since Jan 1
        // 1970, the Epoch
        int StartDay = Integer.parseInt(String
                .valueOf((StartTime - (StartTime % 86400000)) / 86400000)); // number
                                                                            // of
                                                                            // days
                                                                            // of
                                                                            // start
                                                                            // date
        int EndDay = Integer.parseInt(String
                .valueOf((EndTime - (EndTime % 86400000)) / 86400000)); // number
                                                                        // of
                                                                        // days
                                                                        // of
                                                                        // end
                                                                        // date
        int difference = EndDay - StartDay; // difference in days between two
                                            // dates

        return difference;
    }
    
    public static int getDayDiff(Date dtStartDate, Date dtEndDate) throws Exception{
    	Date dStartDate = format(dtStartDate);
    	Date dEndDate = format(dtEndDate);
    	return ((Long)((dEndDate.getTime() - dStartDate.getTime()) / (24 * 60 * 60 * 1000))).intValue();
    }
    
    public static boolean isValidDate(String straDate, String straFormat) throws Exception {

		java.util.Date dtValidDate = null;
		try {
			dtValidDate = SyncSimpleDateFormat.parse(straFormat, straDate, false, 0);
			if (dtValidDate == null) {
				return false;
			}
			return true;
		} catch (Exception ex) {
			return false;
		}
	}
    
    public static Date addDate(Date dtStartDate, int intField, int intNum) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dtStartDate);
        cal.add(intField, intNum);
        Date dt = new Date(cal.getTime().getTime());
        return dt;
    }
    
    public static int returnHour(Timestamp straDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new java.sql.Date(straDate.getTime()));
        return cal.get(Calendar.HOUR);
    }

    public static int returnMin(Timestamp straDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new java.sql.Date(straDate.getTime()));
        return cal.get(Calendar.MINUTE);
    }

    public static int returnHourOfDay(Timestamp straDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new java.sql.Date(straDate.getTime()));
        return cal.get(Calendar.HOUR_OF_DAY);
    }

    public static int returnAmPm(Timestamp straDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new java.sql.Date(straDate.getTime()));
        return cal.get(Calendar.AM_PM);
    }

    public static int returnYear(Date straDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new java.sql.Date(straDate.getTime()));
        return cal.get(Calendar.YEAR);
    }

    public static int returnMonth(Date straDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new java.sql.Date(straDate.getTime()));
        return cal.get(Calendar.MONTH) + 1;
    }

    public static int returnDate(Date straDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new java.sql.Date(straDate.getTime()));
        return cal.get(Calendar.DATE);
    }

    public static int returnDayOfMonth(Date straDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new java.sql.Date(straDate.getTime()));
        return cal.get(Calendar.DAY_OF_MONTH);
    }
    
    public static String returnStirng(Object obj){
    	if(null == obj){
    		return "";
    	}else{
    		return obj.toString();
    	}
    }
    
    public static String trim(String str){
    	if(str == null){
    		return null;
    	}
    	return str.trim();
    }
    
	/**
	 * 鍙栧綋鍓嶅勾浠�
	 * 
	 * @param
	 * @return
	 */
	public static Integer currentYear() {
		Integer currentYear = (Calendar.getInstance()).get(Calendar.YEAR);
		return currentYear;
	}

	/**
	 * 鑾峰彇褰撳墠骞翠唤鍙婁笅涓�勾浠藉垪琛�
	 * 
	 * @param
	 * @return
	 */
	public static List<Integer> getThisAndNextYearList() {
		Integer currentYear = currentYear();
		List<Integer> yearList = new ArrayList<Integer>();
		yearList.add(currentYear);
		yearList.add(currentYear + 1);
		return yearList;
	}
    
}