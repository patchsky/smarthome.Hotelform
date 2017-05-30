package com.kincony.server.packets;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kincony.server.packets.in.AlarmPacket;
import com.kincony.server.packets.in.CDataPacket;
import com.kincony.server.packets.in.DDataPacket;
import com.kincony.server.packets.in.KeepAlivePacket;
import com.kincony.server.packets.in.LoginPacket;
import com.kincony.server.packets.in.QueryPacket;
import com.kincony.server.packets.in.ReportPacket;
import com.kincony.server.packets.in.SendDDataReplyPacket;
import com.kincony.server.packets.in.UnknownInPacket;
import com.kincony.server.packets.in.alarm.Alarm0140Packet;
import com.kincony.server.packets.in.alarm.Alarm0240Packet;
import com.kincony.server.packets.in.alarm.Alarm0150Packet;
import com.kincony.server.packets.in.report.Report0150Packet;
import com.kincony.server.packets.in.report.Report0140Packet;
import com.kincony.server.packets.in.report.Report0240Packet;
import com.kincony.server.support.Protocol;
import com.kincony.server.util.Util;

public class PacketParser implements IParser{
	private static Logger logger = LoggerFactory.getLogger(PacketParser.class);

	private PacketHistory history;

	public PacketParser(PacketHistory history) {
		this.history = history;
	}
	
	@Override
	public boolean accept(ChannelBuffer buf) {
		int bufferLength = buf.readableBytes();
		if(bufferLength <= 0){
			return false;
		}
		//length = buf.getInt(0); // 读取包长
		byte mark = buf.getByte(0);
		byte version = buf.getByte(1);
		if(mark != Protocol.MARK){
			return false;
		}
		/*command = buf.getChar(2);
		length = buf.getInt(4);*/
		//dockUser = buf.getInt(10);
		return true;
	}

	@Override
	public boolean isDuplicated(ChannelBuffer buf) {
		/*boolean add = true;
		switch(command){
			case Protocol.CMD_LOGIN:
			case Protocol.CMD_KEEP_ALIVE:
				add = false;
			break;
		}
		
		boolean duplicated = history.check(dockUser, getHash(), add);
			
	    if(duplicated)
	        logger.info("包" + command + " 序号 " + (int)sequence + "重复到达，忽略");
	    return duplicated;*/
		return false;
	}

	@Override
	public boolean isDuplicatedNeedReply() {
		return false;
	}

	@Override
	public boolean isIncoming(ChannelBuffer buf) {
		return false;
	}

	@Override
	public InPacket parseIncoming(ChannelBuffer buf)
			throws PacketParseException {
		return null;
	}

	@Override
	public OutPacket parseOutcoming(ChannelBuffer buf)
			throws PacketParseException {
		return null;
	}
	
	@Override
	public InPacket parseIncoming(ChannelBuffer buf, int offset, int length)
			throws PacketParseException {
			char command = buf.getChar(2);
		try{
			switch(command){
				case Protocol.MSG_DEV_REG:
					return new LoginPacket(buf);
				case Protocol.MSG_DEV_HEART_BEAT:
					return new KeepAlivePacket(buf);
				case Protocol.MSG_CLI_QUERY:
					return new QueryPacket(buf);
				default:
					byte frameType = buf.getByte(45);
					char devType = buf.getChar(46);
					
					if(command >= Protocol.MSG_C2D_DATA_START && command <= Protocol.MSG_C2D_DATA_END){
						switch(frameType){
							case 0x02:
							case 0x10:
								return new SendDDataReplyPacket(buf);
						}
						return new CDataPacket(buf);
					}
					if(command >= Protocol.MSG_D2C_DATA_START && command <= Protocol.MSG_D2C_DATA_END){
						switch(frameType){
							case 0x02:
							case 0x10:
								return new SendDDataReplyPacket(buf);
						}
						return new DDataPacket(buf);
					}
					if(command >= Protocol.MSG_D2S_DATA_START && command <= Protocol.MSG_D2S_DATA_END){
						switch(devType){
							
							case Protocol.MSG_TYPE_HEBEI:
								return new Report0240Packet(buf);
							case Protocol.MSG_TYPE_BAOXIAN:
								return new Report0150Packet(buf);
							case Protocol.MSG_TYPE_DONGCHE:
								return new Report0140Packet(buf);
						}
					}
					if(command >= Protocol.MSG_D2S_ALARM_START && command <= Protocol.MSG_D2S_ALARM_END){
						switch(devType){
							case Protocol.MSG_TYPE_HEBEI:
								return new Alarm0240Packet(buf);
							case Protocol.MSG_TYPE_BAOXIAN:
								return new Alarm0150Packet(buf);
							case Protocol.MSG_TYPE_DONGCHE:
								return new Alarm0140Packet(buf);
						}
					}
					return new UnknownInPacket(buf);
			}
					
		} catch (Exception e) {
            // 如果解析失败，返回null
			logger.error(e.getMessage(), e);
			return new UnknownInPacket(buf);
        }
	}

	@Override
	public OutPacket parseOutcoming(ChannelBuffer buf, int offset, int length)
			throws PacketParseException {
		return null;
	}
}
