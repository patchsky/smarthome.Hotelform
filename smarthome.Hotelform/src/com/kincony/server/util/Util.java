package com.kincony.server.util;

import java.io.UnsupportedEncodingException;
import java.util.StringTokenizer;

import com.kincony.server.packets.in.CDataPacket;
import com.kincony.server.packets.in.DDataPacket;
import com.kincony.server.packets.in.KeepAlivePacket;
import com.kincony.server.packets.in.LoginPacket;
import com.kincony.server.packets.in.QueryPacket;
import com.kincony.server.packets.in.UnknownInPacket;
import com.kincony.server.support.Protocol;

public class Util {
	// string buffer
    private static StringBuilder sb = new StringBuilder();
    
    // 16进制字符数组
    private static char[] hex = new char[] { 
            '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
    };
    
	/**
     * 比较两个字节数组的内容是否相等
     * 
     * @param b1
     * 		字节数组1
     * @param b2
     * 		字节数组2
     * @return
     * 		true表示相等
     */
    public static boolean isByteArrayEqual(byte[] b1, byte[] b2) {
        if(b1.length != b2.length)
            return false;
        
        for(int i = 0; i < b1.length; i++) {
            if(b1[i] != b2[i])
                return false;
        }
        return true;
    }
    
    /**
	 * 判断IP是否全0
	 * @param ip
	 * @return true表示IP全0
	 */
	public static boolean isIpZero(byte[] ip) {
		for(int i = 0; i < ip.length; i++) {
			if(ip[i] != 0)
				return false;			
		}
		return true;
	}
	
	public static byte[] int2byte(int res) {
		byte[] targets = new byte[4];

		targets[0] = (byte) (res & 0xff);// 最低位 
		targets[1] = (byte) ((res >> 8) & 0xff);// 次低位 
		targets[2] = (byte) ((res >> 16) & 0xff);// 次高位 
		targets[3] = (byte) (res >>> 24);// 最高位,无符号右移。 
		return targets; 
	} 
	
	/**
     * 根据某种编码方式得到字符串的字节数组形式
     * @param s 字符串
     * @param encoding 编码方式
     * @return 特定编码方式的字节数组，如果encoding不支持，返回一个缺省编码的字节数组
     */
    public static byte[] getBytes(String s, String encoding) {
        try {
            return s.getBytes(encoding);
        } catch (UnsupportedEncodingException e) {
            return s.getBytes();
        }
    }
    
    /**
     * 根据缺省编码得到字符串的字节数组形式
     * 
     * @param s
     * @return
     */
    public static byte[] getBytes(String s) {
        return getBytes(s, Protocol.CHARSET_DEFAULT);
    }
    
    public static byte[] getBytes(String s, int maxLength){
    	byte[] tp = new byte[maxLength];
    	
    	byte[] b = getBytes(s);
    	int len = b.length>maxLength?maxLength:b.length;
		System.arraycopy(b, 0, tp, 0, len);
		/*if(len > maxLength){
			for(int i=len; i<maxLength; i++){
				tp[i] = 0x0;
			}
		}*/
		
		return tp;
    }
    
    /**
     * 对原始字符串进行编码转换，如果失败，返回原始的字符串
     * @param s 原始字符串
     * @param srcEncoding 源编码方式
     * @param destEncoding 目标编码方式
     * @return 转换编码后的字符串，失败返回原始字符串
     */
    public static String getString(String s, String srcEncoding, String destEncoding) {
        try {
            return new String(s.getBytes(srcEncoding), destEncoding);
        } catch (UnsupportedEncodingException e) {
            return s;
        }
    }
    
    /**
     * 根据某种编码方式将字节数组转换成字符串
     * @param b 字节数组
     * @param encoding 编码方式
     * @return 如果encoding不支持，返回一个缺省编码的字符串
     */
    public static String getString2(byte[] b, String encoding) {
        try {
        	int count = 0;
        	for(int i=0; i<b.length; i++){
        		if(b[i] == 0){
        			break;
        		}
        		count++;
        	}
        	byte[] b2 = new byte[count];
        	System.arraycopy(b, 0, b2, 0, count);
        	
            return new String(b2, encoding);
        } catch (UnsupportedEncodingException e) {
            return new String(b);
        }
    }
    
    /**
     * 根据某种编码方式将字节数组转换成字符串
     * @param b 字节数组
     * @param encoding 编码方式
     * @return 如果encoding不支持，返回一个缺省编码的字符串
     */
    public static String getString(byte[] b, String encoding) {
        try {
            return new String(b, encoding);
        } catch (UnsupportedEncodingException e) {
            return new String(b);
        }
    }
    
    /**
     * 根据缺省编码将字节数组转换成字符串
     * 
     * @param b
     * 		字节数组
     * @return
     * 		字符串
     */
    public static String getString2(byte[] b) {
        return getString2(b, Protocol.CHARSET_DEFAULT);
    }
    
    /**
     * 根据缺省编码将字节数组转换成字符串
     * 
     * @param b
     * 		字节数组
     * @return
     * 		字符串
     */
    public static String getString(byte[] b) {
        return getString(b, Protocol.CHARSET_DEFAULT);
    }
    
    /**
     * 根据某种编码方式将字节数组转换成字符串
     * @param b 字节数组
     * @param offset 要转换的起始位置
     * @param len 要转换的长度
     * @param encoding 编码方式
     * @return 如果encoding不支持，返回一个缺省编码的字符串
     */
    public static String getString(byte[] b, int offset, int len, String encoding) {
        try {
            return new String(b, offset, len, encoding);
        } catch (UnsupportedEncodingException e) {
            return new String(b, offset, len);
        }
    }
    
    /**
     * 根据缺省编码方式将字节数组转换成字符串
     * @param b 字节数组
     * @param offset 要转换的起始位置
     * @param len 要转换的长度
     * @return
     */
    public static String getString(byte[] b, int offset, int len) {
        return getString(b, offset, len, Protocol.CHARSET_DEFAULT);
    }
    
    /**
     * 把字符串转换成int
     * @param s 字符串
     * @param faultValue 如果转换失败，返回这个值
     * @return 如果转换失败，返回faultValue，成功返回转换后的值
     */
    public static int getInt(String s, int faultValue) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return faultValue;
        }
    }
    
    /**
     * 把字符串转换成long
     * @param s 字符串
     * @param faultValue 如果转换失败，返回这个值
     * @return 如果转换失败，返回faultValue，成功返回转换后的值
     */
    public static long getLong(String s, int radix, long faultValue) {
        try {
            return Long.parseLong(s, radix);
        } catch (NumberFormatException e) {
            return faultValue;
        }
    }
    
    /**
     * 把字符串转换成int
     * @param s 字符串
     * @param radix
     * 		基数
     * @param faultValue 如果转换失败，返回这个值
     * @return 如果转换失败，返回faultValue，成功返回转换后的值
     */
    public static int getInt(String s, int radix, int faultValue) {
        try {
            return Integer.parseInt(s, radix);
        } catch (NumberFormatException e) {
            return faultValue;
        }
    }
    
    public static int byteArrayToInt(byte[] b, int offset) {
        int value= 0;
        for (int i = 0; i < 4; i++) {
            int shift= (4 - 1 - i) * 8;
            value +=(b[i + offset] & 0x000000FF) << shift;//往高位游
        }
        return value;
    }
    
    public static int byte2ToInt(byte[] b) {
    	 int value= 0;
         for (int i = 0; i < 2; i++) {
             int shift= (2 - 1 - i) * 8;
             value +=(b[i] & 0x000000FF) << shift;//往高位游
         }
         return value;
    }
    
    /**
     * 检查字符串是否是整数格式
     * 
     * @param s
     * 		字符串
     * @return
     * 		true表示可以解析成整数
     */
    public static boolean isInt(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch(NumberFormatException e) {
            return false;
        }
    }
    
    /**
     * 把字符串转换成char类型的无符号数
     * @param s 字符串
     * @param faultValue 如果转换失败，返回这个值
     * @return 如果转换失败，返回faultValue，成功返回转换后的值
     */
    public static char getChar(String s, int faultValue) {
        return (char)(getInt(s, faultValue) & 0xFFFF);
    }
    
    /**
     * 把字符串转换成byte
     * @param s 字符串
     * @param faultValue 如果转换失败，返回这个值
     * @return 如果转换失败，返回faultValue，成功返回转换后的值
     */
    public static byte getByte(String s, int faultValue) {
        return (byte)(getInt(s, faultValue) & 0xFF);
    }
    
    /**
     * @param ip ip的字节数组形式
     * @return 字符串形式的ip
     */
    public static String getIpStringFromBytes(byte[] ip) {
	    sb.delete(0, sb.length());
    	sb.append(ip[0] & 0xFF);
    	sb.append('.');   	
    	sb.append(ip[1] & 0xFF);
    	sb.append('.');   	
    	sb.append(ip[2] & 0xFF);
    	sb.append('.');   	
    	sb.append(ip[3] & 0xFF);
    	return sb.toString();
    }
    
    /**
     * 从ip的字符串形式得到字节数组形式
     * @param ip 字符串形式的ip
     * @return 字节数组形式的ip
     */
    public static byte[] getIpByteArrayFromString(String ip) {
        byte[] ret = new byte[4];
        StringTokenizer st = new StringTokenizer(ip, ".");
        try {
            ret[0] = (byte)(Integer.parseInt(st.nextToken()) & 0xFF);
            ret[1] = (byte)(Integer.parseInt(st.nextToken()) & 0xFF);
            ret[2] = (byte)(Integer.parseInt(st.nextToken()) & 0xFF);
            ret[3] = (byte)(Integer.parseInt(st.nextToken()) & 0xFF);
        } catch (Exception e) {
           // log.error(e.getMessage());
        }
        return ret;
    }
    
    /**
     * 判断IP是否相等
     * @param ip1 IP的字节数组形式
     * @param ip2 IP的字节数组形式
     * @return true如果两个IP相等
     */
    public static boolean isIpEquals(byte[] ip1, byte[] ip2) {
        return (ip1[0] == ip2[0] && ip1[1] == ip2[1] && ip1[2] == ip2[2] && ip1[3] == ip2[3]);
    }
    
    /** 
     * 将char转为低字节在前，高字节在后的byte数组 
     */ 
     public static byte[] toLH(char n) { 
 	    byte[] b = new byte[2]; 
 	    b[0] = (byte) (n & 0xff); 
 	    b[1] = (byte) (n >> 8 & 0xff); 
 	    return b; 
     } 
     
    /** 
    * 将int转为低字节在前，高字节在后的byte数组 
    */ 
    public static byte[] toLH(int n) { 
	    byte[] b = new byte[4]; 
	    b[0] = (byte) (n & 0xff); 
	    b[1] = (byte) (n >> 8 & 0xff); 
	    b[2] = (byte) (n >> 16 & 0xff); 
	    b[3] = (byte) (n >> 24 & 0xff); 
	    return b; 
    } 
    /** 
    * 将float转为低字节在前，高字节在后的byte数组 
    */ 
    public static byte[] toLH(float f) { 
    	return toLH(Float.floatToRawIntBits(f)); 
    } 
    
	/**
	 * 返回命令字符串
	 * @param cmd
	 * @return
	 */
	public static String getCommandString(char command){
		switch(command){
			case Protocol.MSG_DEV_REG:
				return "MSG_DEV_REG";
			case Protocol.MSG_DEV_REG_RE:
				System.err.println("发了");
				return "MSG_DEV_REG_RE";
			case Protocol.MSG_DEV_HEART_BEAT:
				return "MSG_DEV_HEART_BEAT";
			case Protocol.MSG_DEV_HEART_BEAT_RE:
				return "MSG_DEV_HEART_BEAT_RE";
			case Protocol.MSG_CLI_QUERY:
				return "MSG_CLI_QUERY";
			case Protocol.MSG_CLI_QUERY_RE:
				return "MSG_CLI_QUERY_RE";
			default:
				if(command >= Protocol.MSG_C2D_DATA_START && command <= Protocol.MSG_C2D_DATA_END){
					return "MSG_C2D_DATA";
				}
				if(command >= Protocol.MSG_D2C_DATA_START && command <= Protocol.MSG_D2C_DATA_END){
					return "MSG_D2C_DATA";
				}
				if(command >= Protocol.MSG_D2S_DATA_START && command <= Protocol.MSG_D2S_DATA_END){
					return "MSG_D2S_DATA";
				}
				if(command >= Protocol.MSG_D2S_ALARM_START && command <= Protocol.MSG_D2S_ALARM_END){
					return "MSG_D2S_ALARM";
				}
				return "Unknown";
		}
	}
	
	public static byte getValidateByte(byte[] sendData) {
		int sum = 0;
		for (int i = 0; i < sendData.length; i++) {
			sum += getInt(sendData[i]);
		}
		return (byte) sum;
	}
	
	public static int getInt(byte b) {
		return b & 0xFF;
	}
	
	public String getDevTypeName(char devType){
		switch(devType){
			case 0x0110:
				return "PM2.5传感器";
			case 0x0210:
				return "人体红外传感器";
			case 0x0310:
				return "温湿度传感器";
			case 0x0120:
				return "U-air(wifi)传感器";
			case 0x0130:
				return "水阀(wifi)";
			case 0x0140:
				return "东磁净水器(wifi)";
			default:
				return "未知设备";
		}
	}
}
