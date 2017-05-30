package com.kincony.server.helper;

import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Date;

import org.jboss.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.kincony.server.bean.DockUser;
import com.kincony.server.packets.ErrorPacket;
import com.kincony.server.packets.InPacket;
import com.kincony.server.packets.OutPacket;
import com.kincony.server.packets.in.AlarmPacket;
import com.kincony.server.packets.in.CDataPacket;
import com.kincony.server.packets.in.DDataPacket;
import com.kincony.server.packets.in.LoginPacket;
import com.kincony.server.packets.in.QueryPacket;
import com.kincony.server.packets.in.ReportPacket;
import com.kincony.server.packets.in.SendDDataReplyPacket;
import com.kincony.server.packets.in.alarm.Alarm0140Packet;
import com.kincony.server.packets.in.alarm.Alarm0150Packet;
import com.kincony.server.packets.in.alarm.Alarm0240Packet;
import com.kincony.server.packets.in.report.Report0150Packet;
import com.kincony.server.packets.in.report.Report0140Packet;
import com.kincony.server.packets.in.report.Report0240Packet;
import com.kincony.server.packets.out.DataReplyPacket;
import com.kincony.server.packets.out.KeepAlivePacket;
import com.kincony.server.packets.out.LoginReplyPacket;
import com.kincony.server.packets.out.QueryReplyPacket;
import com.kincony.server.packets.out.SendDDataPacket;
import com.kincony.server.support.PacketProcessor;
import com.kincony.server.support.Protocol;
import com.kincony.server.util.NettyUtils;
import com.kincony.server.util.Util;


public class BoProcessHelper{

	private static Logger logger = LoggerFactory.getLogger(BoProcessHelper.class);
	
	private PacketProcessor packetProcessor;
	
	private UserManager userManager;
	
	public BoProcessHelper(PacketProcessor packetProcessor){
		this.packetProcessor = packetProcessor;
		this.userManager = packetProcessor.getUserManager();
	}
	
	/**
	 * 服务器主动发给设备
	 * @param devId
	 * @param devType 设备类型
	 * @param frameType 帧类型
	 * @param frameBody 帧内容
	 */
	public void processSendDData(String devId, char devType, byte frameType, byte[] frameBody){
		logger.info("发送 "+devId+" processSendDData");
		SendDDataPacket packet = new SendDDataPacket(devId);
		packet.setFrameType(frameType);
		packet.setDevType(devType);
		packet.setFrameBody(frameBody);
		//packet.setFrameLen(frameBody.length);
		
		String[] address = packetProcessor.getSocketAddress(devId);
		
		if(address == null){
			logger.error(devId + " 设备IP 不存在");
			return;
		}
		
		packet.setHostName(address[0]);
		packet.setPort(Integer.valueOf(address[1]));
		
		packetProcessor.sendStrategy(packet);
	}
	
	/**
	 * 服务器主动发给设备
	 * @param devId
	 * @param devType 设备类型
	 * @param frameType 帧类型
	 * @param frameBody 帧内容
	 */
	public void processSend0Packet(String devId, char devType, byte frameType){
		SendDDataPacket packet = new SendDDataPacket(devId);
		int req = OutPacket.getNextSeq();
		logger.info("发送清0命令 " + devId + " frameType = " + frameType + " rand=" + req);
		
		packet.setFrameType(frameType);
		packet.setDevType(devType);
		byte[] b = new byte[1];
		b[0] = (byte)req;
		
		packet.setFrameBody(b);
		
		String[] address = packetProcessor.getSocketAddress(devId);
		
		if(address == null){
			logger.error(devId + " 设备IP 不存在");
			return;
		}
		
		packet.setHostName(address[0]);
		packet.setPort(Integer.valueOf(address[1]));
		
		packetProcessor.send(packet);
	}
}
