package com.kincony.server.util;

import java.util.HashMap;
import java.util.Map;

import javax.print.DocFlavor.STRING;

public class StaticUtil {
	
	public static Map<String, String[]> NETWORK = new HashMap<String, String[]>(); //网络号
	
	public static Map<String, String[]> CHANNEL = new HashMap<String, String[]>(); //信道
	
	//双向灯
	public static Map<String, String[]> QUERYSTATE = new HashMap<String, String[]>();
	//窗帘
	public static Map<String, String[]> CURTAIN = new HashMap<String, String[]>();
}
