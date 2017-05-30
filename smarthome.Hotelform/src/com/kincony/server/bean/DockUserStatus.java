package com.kincony.server.bean;

import java.util.Date;

public class DockUserStatus {
	private String dockUser;
	private String status;
	private String statusTime;
	
	public String getDockUser() {
		return dockUser;
	}
	public void setDockUser(String dockUser) {
		this.dockUser = dockUser;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStatusTime() {
		return statusTime;
	}
	public void setStatusTime(String statusTime) {
		this.statusTime = statusTime;
	}
}
