package com.kincony.server.helper;

import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import com.kincony.server.bean.DockUser;

/**
 * 在线用户管理
 * @author joey
 *
 */
public class UserManager {

	private HashMap<String, DockUser> userMap;
	
	public UserManager(){
		userMap = new HashMap<String, DockUser>();
	}
	
	public synchronized void addUser(String userId, Date registerDate, String ip, int port){
		DockUser user = new DockUser();
		user.setUserId(userId);
		user.setRegisterTime(registerDate);
		user.setKeepAliveTime(registerDate);
		user.setIp(ip);
		user.setPort(port);
		user.setStatus(1);
		
		userMap.put(userId, user);
	}
	
	public synchronized void logoutUser(String userId, Date logoutTime, String logoutIp){
		DockUser user = userMap.get(userId);
		if(user != null){
			user.setIp(logoutIp);
			user.setLogoutTime(logoutTime);
			user.setStatus(0);
		}
	}
	
	public synchronized void keepAliveUser(String userId, Date keepAliveTime, String ip, int port){
		DockUser user = userMap.get(userId);
		if(user == null){
			user = new DockUser();
			userMap.put(userId, user);
		}
		user.setKeepAliveTime(keepAliveTime);
		user.setIp(ip);
		user.setPort(port);
		user.setStatus(1);
	}
	
	public synchronized DockUser getUser(String userId){
		return userMap.get(userId);
	}
}
