package com.kincony.server.common;

import java.text.ParseException;

public class DataUtil {

	/**
	* Description : Convert the value object type into the corresponding object type
	* @param	      aClass Class
	* @param	      aValue Object
	* @return	      Object
	*/
	public static Object convertValue(Class aClass, Object aValue) throws Exception {
		if (aValue == null || aClass == null) {
			return null;
		}
		if (aValue.getClass().equals(aClass)
			&& aClass != java.sql.Timestamp.class
			&& aClass != java.sql.Date.class
			&& aClass != java.util.Date.class) {
			return aValue;
		}

		String strValue = aValue.toString();
		int iValueLen = strValue.length();
		if (iValueLen == 0) {
			return null;
		}
		
		try {
			
			// Convert To String
			if (aClass == java.lang.String.class) {
				if (aValue instanceof byte[]) {
					return new String((byte[]) aValue);
				} else if (aValue instanceof char[]) {
					return new String((char[]) aValue);
				} else {
					return formatToString(aValue);
				}
			}
			// Convert To Boolean
			else if (aClass == java.lang.Boolean.class) {
				strValue = removeNumStrComma(strValue);
				if (strValue.lastIndexOf(GlobalConst.CONST_STRING_DOT) != -1){
					strValue = strValue.substring(0, strValue.lastIndexOf(GlobalConst.CONST_STRING_DOT));
				}
				if ((strValue.equals(GlobalConst.CONST_STRING_true)) || (strValue.equals(GlobalConst.CONST_STRING_false))) {
					return Boolean.valueOf(strValue);
				} else if (Long.valueOf(strValue).longValue() == 0) {
					return Boolean.valueOf(GlobalConst.CONST_STRING_false);
				}
				return Boolean.valueOf(GlobalConst.CONST_STRING_true);
			}
			// Convert To Double
			else if (aClass == java.lang.Double.class) {
				strValue = removeNumStrComma(strValue);
				return Double.valueOf(SyncDecimalFormat.format("#.########", Double.valueOf(strValue)));
			}
			// Convert To Float
			else if (aClass == java.lang.Float.class) {
				strValue = removeNumStrComma(strValue);
				return Float.valueOf(strValue);
			}
			// Convert To Byte
			else if (aClass == java.lang.Byte.class) {
				strValue = removeNumStrComma(strValue);
				int iValueLastIndex = strValue.lastIndexOf(GlobalConst.CONST_STRING_DOT);
				if (iValueLastIndex != -1) {
					return Byte.valueOf(strValue.substring(0, iValueLastIndex));
				} else {
					return Byte.valueOf(strValue);
				}
			}
			// Convert To Short
			else if (aClass == java.lang.Short.class) {
				strValue = removeNumStrComma(strValue);
				if (strValue.lastIndexOf(GlobalConst.CONST_STRING_DOT) != -1) {
					return Short.valueOf(strValue.substring(0, strValue.lastIndexOf(GlobalConst.CONST_STRING_DOT)));
				} else {
					return Short.valueOf(strValue);
				}
			}
			// Convert To Integer
			else if (aClass == java.lang.Integer.class) {
				strValue = removeNumStrComma(strValue);
				if (strValue.lastIndexOf(GlobalConst.CONST_STRING_DOT) != -1) {
					return Integer.valueOf(strValue.substring(0, strValue.lastIndexOf(GlobalConst.CONST_STRING_DOT)));
				} else {
					return Integer.valueOf(strValue);
				}
			}
			// Convert To Long
			else if (aClass == java.lang.Long.class) {
				strValue = removeNumStrComma(strValue);
				if (strValue.lastIndexOf(GlobalConst.CONST_STRING_DOT) != -1) {
					return Long.valueOf(
						strValue.substring(0, strValue.lastIndexOf(GlobalConst.CONST_STRING_DOT)));
				} else {
					return Long.valueOf(strValue);
				}
			}
			// Convert To java.math.BigDecimal
			else if (aClass == java.math.BigDecimal.class) {
				strValue = removeNumStrComma(strValue);
				return (new java.math.BigDecimal(strValue));
			}
			// Convert To java.math.BigInteger
			else if (aClass == java.math.BigInteger.class) {
				strValue = removeNumStrComma(strValue);
				if (strValue.lastIndexOf(GlobalConst.CONST_STRING_DOT) != -1)
					return (new java.math.BigInteger(strValue.substring(0, strValue.lastIndexOf(GlobalConst.CONST_STRING_DOT))));
				else
					return (new java.math.BigInteger(strValue));
			}
			// Convert To java.util.Date
			else if (aClass == java.util.Date.class) {
				if (aValue instanceof java.util.Date) {
					return new java.sql.Date(((java.util.Date) aValue).getTime());
				}
				if (aValue.getClass() == java.lang.String.class) {
					return new java.sql.Date(SyncSimpleDateFormat.parse(GlobalConst.FORMAT_DATE, strValue).getTime());
				}
			}
			// Convert To java.sql.Date
			else if (aClass == java.sql.Date.class) {
				if (aValue instanceof java.util.Date) {
					return new java.sql.Date(((java.util.Date) aValue).getTime());
				}
				if (aValue.getClass() == java.lang.String.class) {
					return new java.sql.Date(SyncSimpleDateFormat.parse(GlobalConst.FORMAT_DATE, strValue).getTime());
				}
			}
			// Convert To java.sql.Timestamp
			else if (aClass == java.sql.Timestamp.class) {
				if (aValue instanceof java.util.Date) {
					return new java.sql.Timestamp(((java.util.Date) aValue).getTime());
				} else if (aValue.getClass() == java.lang.String.class) {
					//DateFormat objDateFormat = null;
					if (aValue.toString().length() >= GlobalConst.FORMAT_DATETIME.length()) {
						return new java.sql.Timestamp(SyncSimpleDateFormat.parse(GlobalConst.FORMAT_DATETIME, strValue).getTime());
					} else {
						//return new java.sql.Timestamp(SyncSimpleDateFormat.parse(GlobalConst.FORMAT_DATE, strValue).getTime());
						if (aValue.toString().length() >= GlobalConst.FORMAT_DATETIME.length()) {
							
							return new java.sql.Timestamp(SyncSimpleDateFormat.parse(GlobalConst.FORMAT_DATETIME, strValue).getTime());
						} else {
							
							try{
							return new java.sql.Timestamp(SyncSimpleDateFormat.parse(GlobalConst.FORMAT_DATE, strValue).getTime());
							}catch (Exception e){   
							}
						}
					}
				}
			}
			// Convert To Others
			else {
			}
		} catch (ParseException e) {
			String strMsg = GlobalConst.CONST_STRING_EMPTY;
			if (aClass == java.util.Date.class || aClass == java.sql.Date.class){
			  strMsg = "Incorrect data format. Date value required.";
			}
			else if (aClass == java.sql.Timestamp.class){
			  strMsg = "Incorrect data format. Datetime value required.";
			}
		} catch (NumberFormatException e) {
			String strMsg = GlobalConst.CONST_STRING_EMPTY;
			if (aClass == java.lang.Boolean.class) {
				strMsg = "Incorrect data format. 0 or 1 required.";
			} else {
				strMsg = "Incorrect data format. Numeric value required.";
			}
		}
		return aValue;
	}

	/**
	* Description : Change the value input into a String
	* @param	      Value Object
	* @return	      Object
	*/
	public static String formatToString(Object Value) {
		if (Value == null) {
			return GlobalConst.CONST_STRING_EMPTY;
		}
		//if (Value instanceof java.lang.String) {
		/*if (Value.getClass() == java.lang.String.class) {
			return ((String) Value).replace('\0', ' ').trim();
		}*/
		if (Value.getClass() == java.lang.String.class) {
			return ((String) Value);
		}
		if (Value instanceof java.lang.Number) {
			return Value.toString();
		}
		if ((Value.getClass() == java.util.Date.class) || (Value.getClass() == java.sql.Date.class)) {
			return SyncSimpleDateFormat.format(GlobalConst.FORMAT_DATE, (java.util.Date) Value);
		}
		if (Value.getClass() == java.sql.Timestamp.class) {
			return SyncSimpleDateFormat.format(GlobalConst.FORMAT_DATETIME, (java.util.Date) Value);
		}
		return Value.toString().trim();
	}

	/**
	* Description : Change the value input into a String
	* @param	      Value Object
	* @return	      Object
	*/
	public static String formatToNullOrString(Object Value) {
		if (Value == null) {
			return null;
		}
		//if (Value instanceof java.lang.String) {
		if (Value.getClass() == java.lang.String.class) {
			//return ((String) Value).replace('\0', ' ').trim();
		    if(((String) Value).length() == 0 ){
		        return null;
		    }
		    else{
		        return ((String) Value).replace('\0', ' ').trim();
		    }
		}
		if (Value instanceof java.lang.Number) {
			return Value.toString();
		}
		if ((Value.getClass() == java.util.Date.class) || (Value.getClass() == java.sql.Date.class)) {
			return SyncSimpleDateFormat.format(GlobalConst.FORMAT_DATE, (java.util.Date) Value);
		}
		if (Value.getClass() == java.sql.Timestamp.class) {
			return SyncSimpleDateFormat.format(GlobalConst.FORMAT_DATETIME, (java.util.Date) Value);
		}
		return Value.toString().trim();
	}

	/**
	* Description : Remove comma in number string b4 convert to number 
	* @param	      straValue String
	* @return	      String
	*/
	public static String removeNumStrComma(String straValue) {
		return straValue.replaceAll(",", "");
	}
	
//	public static void main(String[] argv) {
//		try {
//			java.util.HashMap hmtest = new java.util.HashMap();
//			hmtest.put("A", "1");
//			hmtest.put("B", "2");
//			System.out.println(hmtest.entrySet());
//			java.util.Set stest = hmtest.keySet();
//			System.out.println("stest="+stest);
//			Object[] objtest = hmtest.keySet().toArray();
//			int intlen = objtest.length;
//			for (int i=0; i< intlen; i++){
//				System.out.println(i + "=" + objtest[i]);
//				System.out.println(objtest[i] + "=" + hmtest.get(objtest[i]));
//			}
//		} catch (Exception e) {
//
//			System.out.println(e.toString());
//		}
//	}
}
