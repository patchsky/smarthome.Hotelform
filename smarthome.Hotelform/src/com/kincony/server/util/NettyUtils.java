package com.kincony.server.util;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyUtils {
	private static Logger logger = LoggerFactory.getLogger(NettyUtils.class);
			
	public static String getIp(SocketAddress socketAddress){
		String ip = "";
		
		if(socketAddress != null){
			InetSocketAddress isa = (InetSocketAddress) socketAddress;
			
			logger.debug("isa.getHostName() => " + isa.getHostName());
			
			ip = isa.getHostName();
		}
		
		return ip;
	}
}
