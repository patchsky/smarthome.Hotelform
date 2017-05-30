package com.kincony.util;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class MapUtil {
	public static Map<String, Object> maps(){
		 Map<String, Object> map = new TreeMap<String, Object>(new Comparator<String>() {
	            public int compare(String obj1, String obj2) {
	                // 降序排序
	                return obj1.compareTo(obj2);
	            }
	        });
		 map.put("date", null);
         map.put("message", "");
         map.put("page", 0);
         map.put("success", true);
         map.put("total", 0);
         map.put("totalPages", 0);
		return map;
	}
}
