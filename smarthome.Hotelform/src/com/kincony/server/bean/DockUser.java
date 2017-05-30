package com.kincony.server.bean;

import java.util.Date;

public class DockUser {
	private String userId;
	private Date registerTime;
	
	private Date keepAliveTime;
	private String ip;
	
	private int port;
	
	private Date logoutTime; // 离线时间
	
	private int status; // 1：在线, 0：离线

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Date getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(Date registerTime) {
		this.registerTime = registerTime;
	}


	public Date getKeepAliveTime() {
		return keepAliveTime;
	}

	public void setKeepAliveTime(Date keepAliveTime) {
		this.keepAliveTime = keepAliveTime;
	}

	public Date getLogoutTime() {
		return logoutTime;
	}

	public void setLogoutTime(Date logoutTime) {
		this.logoutTime = logoutTime;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
}
