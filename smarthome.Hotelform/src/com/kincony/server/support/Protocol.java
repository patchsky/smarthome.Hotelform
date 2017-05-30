package com.kincony.server.support;

public interface Protocol {
	public static final String MK = "cityclud";
	
	/** 包常量 - 包起始序列号, 服务端*/
	public static final int SERVER_INIT_SEQUENCE = 1000;
	
	/** 包常量 - 包起始序列号, 客户端*/
	public static final int CLIENT_INIT_SEQUENCE = 10000;
	
	/** 单位: ms */
	public static final long TIMEOUT_SEND = 800;
	
	/** 单位: ms */
	public static final long TIMEOUT_ALIVE= 60000;
	
	/** 最大重发次数 */
	public static final int MAX_RESEND = 2;
	
	public static final String CHARSET_DEFAULT = "UTF-8";
	
	/** 包最大大小 */
	public static final int MAX_PACKET_SIZE = 65535;
	//public static final int MAX_PACKET_SIZE = 1024;
	
	public static final int LENGTH_FIELD_OFFSET = 0;
	
	public static final int LENGTH_FIELD_LENGTH = 4;
	
	public static final int PACKET_HEADER_SIZE = 44;
	
	public static final int PACKET_BODY_SIZE = 980;
	
	public static final int DEVID_LENGTH = 28;
	
	/** 密钥长度 */
	public static final int LENGTH_KEY = 8;
	
	public static final byte MARK = 0x5E;
	
	/**
	 * 设备向服务器注册消息
	 */
	public static final char MSG_DEV_REG = 0xA000;
	/**
	 * 服务器回复设备注册消息
	 */
	public static final char MSG_DEV_REG_RE = 0xA001;
	
	/**
	 * 设备向服务器发送心跳消息
	 */
	public static final char MSG_DEV_HEART_BEAT = 0xA002;
	/**
	 * 服务器回复设备心跳消息
	 */
	public static final char MSG_DEV_HEART_BEAT_RE = 0xA003;
	
	/**
	 * 客户端向服务器查询设备状态
	 */
	public static final char MSG_CLI_QUERY = 0xB000;
	/**
	 * 服务器回复客户端查询设备
	 */
	public static final char MSG_CLI_QUERY_RE = 0xB001;
	/**
	 * 客户端向设备发送数据
	 */
	public static final char MSG_C2D_DATA_START = 0xC000;
	
	public static final char MSG_C2D_DATA_END = 0xCFFF;
	
	/**
	 * 设备向客户端发送数据
	 */
	public static final char MSG_D2C_DATA_START = 0xD000;
	
	public static final char MSG_D2C_DATA_END = 0xDFFF;
	
	/**
	 * 设备向服务器上报数据
	 */
	public static final char MSG_D2S_DATA_START = 0xE000;
	
	public static final char MSG_D2S_DATA_END = 0xEFFF;
	
	/**
	 * 设备向服务器发送报警，服务器向手机推送报警
	 */
	public static final char MSG_D2S_ALARM_START = 0xF000;
	
	public static final char MSG_D2S_ALARM_END = 0xFFFF;
	
	/** 类型常量 - 未知命令 */
	public static final char CMD_UNKNOWN = 0x0000;
	
	
	public static final char MSG_TYPE_DONGCHE = 0x0140;
	public static final char MSG_TYPE_HEBEI = 0x0240;
	public static final char MSG_TYPE_BAOXIAN = 0x0150;
}
