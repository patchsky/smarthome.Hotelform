package com.kincony.server.helper;

import java.util.Properties;

import com.kincony.server.bean.DockUserStatus;

public class DockUserStatusHelper {
	private Properties properites = new Properties();
	
	public void setDockUserLogin(String dockUser, String loginTime){
		properites.setProperty(dockUser +  "_id", dockUser);
		properites.setProperty(dockUser +  "_status", "登陆");
		properites.setProperty(dockUser +  "_logintime", loginTime);
	}
	
	public void setDockUserLogout(String dockUser, String logoutTime){
		properites.setProperty(dockUser +  "_id", dockUser);
		properites.setProperty(dockUser +  "_status", "退出");
		properites.setProperty(dockUser +  "_logouttime", logoutTime);
	}
	
	public DockUserStatus getDockUserStatus(String dockUser){
		DockUserStatus dockUserStatus = new DockUserStatus();
		dockUserStatus.setDockUser(dockUser);
		dockUserStatus.setStatus(properites.getProperty(dockUser +  "_status", "未连接"));
		if("登陆".equals(dockUserStatus.getStatus())){
			dockUserStatus.setStatusTime(properites.getProperty(dockUser +  "_logintime", ""));
		}else{
			dockUserStatus.setStatusTime(properites.getProperty(dockUser +  "_logouttime", ""));
		}
		
		return dockUserStatus;
	}
}
