package com.kincony.entity.system;

public class HostDevice {
	private String HOSTDEVICE_ID;
	private String DEVICE_CODE;
	private String GUEST_ROOM;
	private String DEVICE_TYPE;
	private String NICK_NAME;
	private String DEVICE_ADDRESS;
	public String getHOSTDEVICE_ID() {
		return HOSTDEVICE_ID;
	}
	public void setHOSTDEVICE_ID(String hOSTDEVICE_ID) {
		HOSTDEVICE_ID = hOSTDEVICE_ID;
	}
	public String getDEVICE_CODE() {
		return DEVICE_CODE;
	}
	public void setDEVICE_CODE(String dEVICE_CODE) {
		DEVICE_CODE = dEVICE_CODE;
	}
	public String getGUEST_ROOM() {
		return GUEST_ROOM;
	}
	public void setGUEST_ROOM(String gUEST_ROOM) {
		GUEST_ROOM = gUEST_ROOM;
	}
	public String getDEVICE_TYPE() {
		return DEVICE_TYPE;
	}
	public void setDEVICE_TYPE(String dEVICE_TYPE) {
		DEVICE_TYPE = dEVICE_TYPE;
	}
	public String getNICK_NAME() {
		return NICK_NAME;
	}
	public void setNICK_NAME(String nICK_NAME) {
		NICK_NAME = nICK_NAME;
	}
	public String getDEVICE_ADDRESS() {
		return DEVICE_ADDRESS;
	}
	public void setDEVICE_ADDRESS(String dEVICE_ADDRESS) {
		DEVICE_ADDRESS = dEVICE_ADDRESS;
	}
	public HostDevice() {
		super();
		// TODO Auto-generated constructor stub
	}
	public HostDevice(String hOSTDEVICE_ID, String dEVICE_CODE,
			String gUEST_ROOM, String dEVICE_TYPE, String nICK_NAME,
			String dEVICE_ADDRESS) {
		super();
		HOSTDEVICE_ID = hOSTDEVICE_ID;
		DEVICE_CODE = dEVICE_CODE;
		GUEST_ROOM = gUEST_ROOM;
		DEVICE_TYPE = dEVICE_TYPE;
		NICK_NAME = nICK_NAME;
		DEVICE_ADDRESS = dEVICE_ADDRESS;
	}
	
	
	
}
