package com.kincony.server.handler;

import java.util.ArrayList;
import java.util.List;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kincony.server.packets.InPacket;
import com.kincony.server.packets.PacketParseException;
import com.kincony.server.packets.in.AlarmPacket;
import com.kincony.server.packets.in.ReportPacket;
import com.kincony.server.support.PacketProcessor;
import com.kincony.server.support.Protocol;


public class PacketDecoder extends FrameDecoder{
	private static Logger logger = LoggerFactory.getLogger(PacketDecoder.class);
			
	private PacketProcessor packetProcessor;
	
	public PacketDecoder(PacketProcessor packetProcessor){
		this.packetProcessor = packetProcessor;
	}
	
	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel,
			ChannelBuffer buf) throws Exception {
		byte[] array = buf.array();
		StringBuffer sqs = new StringBuffer();
		for (int i = 0; i < array.length; i++) {
			if(i > 0) {
				sqs.append(',');
			    }
			sqs.append(array[i]);
		}
		logger.error("数据包 " + sqs.toString());
	
		int length = buf.readableBytes();
		
		if (length > 4) {
			try{
				List<InPacket> ret = new ArrayList<InPacket>();
				char command = buf.getChar(2);
				
				while(length > 4 && accept(command)){
					InPacket packet = this.decodePacket(buf);
					if(packet != null){
						ret.add(packet);
					}
					length = buf.readableBytes();
					command = length > 4 ? buf.getChar(2) : 0xFFFF;
				}
				int alen = buf.readableBytes();
				if(alen > 0 ){
					int readerIndex = buf.readerIndex();
					buf.skipBytes(alen);
					buf.readerIndex(alen + readerIndex);
					logger.error("异常"+packetProcessor.getPacketProcessHelper().getDeviceCode());
					logger.error("异常数据 " + alen + "字节");
					StringBuffer sq = new StringBuffer();
					for (int i = 0; i < array.length; i++) {
						if(i > 0) {
							 sq.append(',');
						    }
						 sq.append(array[i]);
					}
					logger.error("异常数据包 " + sq.toString());
				}
				return ret;
			}catch(Exception e){
				int alen = buf.readableBytes();
				if(alen > 0 ){
					int readerIndex = buf.readerIndex();
					buf.skipBytes(alen);
					buf.readerIndex(alen + readerIndex);
					logger.error("异常"+packetProcessor.getPacketProcessHelper().getDeviceCode());
					logger.error("异常数据 " + alen + "字节");
					StringBuffer sq = new StringBuffer();
					for (int i = 0; i < array.length; i++) {
						if(i > 0) {
							 sq.append(',');
						    }
						 sq.append(array[i]);
					}
					logger.error("异常数据包 " + sq.toString());
					
				}
				e.printStackTrace();
				throw e;
			}
		}
		return null;
	}
	
	private InPacket decodePacket(ChannelBuffer buf) throws PacketParseException{
		int length = buf.readableBytes();
		int packetLength = Protocol.PACKET_HEADER_SIZE + (int)buf.getChar(4);
		byte[] array = buf.array();
		if(length < packetLength){
			buf.skipBytes(length);
			buf.readerIndex(length);
			logger.error("异常数据 " + length + "字节, 数据长度不对");
			StringBuffer sq = new StringBuffer();
			for (int i = 0; i < array.length; i++) {
				if(i > 0) {
					 sq.append(',');
				    }
				 sq.append(array[i]);
			}
			logger.error("异常数据包 " + sq.toString());
			return null;
		}
		
		int readerIndex = buf.readerIndex();
		
		ChannelBuffer frame = buf.factory().getBuffer(packetLength);
		frame.writeBytes(buf, 0, packetLength);
		buf.skipBytes(packetLength);
		buf.readerIndex(readerIndex + packetLength);
		InPacket packet = packetProcessor.getPacketHelper().processIn(frame);
	
		if(packet != null){
			return packet;
		}
		return null;
	}
	
	private boolean accept(char command){
		switch(command){
		case Protocol.MSG_DEV_REG:
			return true;
		case Protocol.MSG_DEV_HEART_BEAT:
			return true;
		case Protocol.MSG_CLI_QUERY:
			return true;
		default:
			if(command >= Protocol.MSG_C2D_DATA_START && command <= Protocol.MSG_C2D_DATA_END){
				return true;
			}
			if(command >= Protocol.MSG_D2C_DATA_START && command <= Protocol.MSG_D2C_DATA_END){
				return true;
			}
			if(command >= Protocol.MSG_D2S_DATA_START && command <= Protocol.MSG_D2S_DATA_END){
				return true;
			}
			if(command >= Protocol.MSG_D2S_ALARM_START && command <= Protocol.MSG_D2S_ALARM_END){
				return true;
			}
			break;
		}
		return false;
	}

}
