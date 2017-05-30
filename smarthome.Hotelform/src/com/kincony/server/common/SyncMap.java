/**
 * com.excelhk.wucommon.common
 *
 * SyncMap.java
 * 
 * Company : Excel Technology International (Hong Kong) Limited
 * Team    : WMS
 * 
 * The contents of this file are confidential and proprietary to Excel.
 * Copying is explicitly prohibited without the express permission of Excel.
 * 
 * Revision History
 * Date             By        Version Reason
 * ---------------- --------- ------- ----------------------------------------------
 * Feb 20, 2004     garywong   1.0.0    Created.
 *  
 */

/**
 * SyncMap -
 * extends HashMap with synchronized put method.
 * It is the only 1 differect between HashMap and SyncMap
 * 
 * <DT><B>Copyright:</B>
 * <DD>The contents of this file are confidential and proprietary to Excel.</DD>
 * <DD>Copying is explicitly prohibited without the express permission of Excel.</DD>
 * 
 * @author garywong
 * @version 1.0.0
 *
 */

package com.kincony.server.common;

import java.util.HashMap;
import java.util.Map;

public class SyncMap extends HashMap{
	
	public SyncMap() {
		super();
	}
	
	public SyncMap(Map map) {
		super(map);
	}
	
	public synchronized Object put(Object key, Object value) {		
		return super.put(key,value); 
	}
	
	public static void main(String[] args) {
	}
}
