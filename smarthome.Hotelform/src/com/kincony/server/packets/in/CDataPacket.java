package com.kincony.server.packets.in;

import org.jboss.netty.buffer.ChannelBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kincony.server.packets.InPacket;
import com.kincony.server.packets.PacketParseException;
import com.kincony.server.util.Util;

public class CDataPacket extends InPacket{
	private static Logger logger = LoggerFactory.getLogger(CDataPacket.class);
	private byte[] devData;
	
	private String clientId;

	public CDataPacket(ChannelBuffer buf) throws PacketParseException {
		super(buf);
		
		clientId = Util.getString2(this.ext);
	}

	@Override
	protected void parseBody(ChannelBuffer buf) throws PacketParseException {
		//int bufferLength = this.bodyLength;
		devData= new byte[buf.readableBytes()];
		buf.readBytes(devData);
	}

	
	public byte[] getDevData() {
		return devData;
	}

	public String getClientId() {
		return clientId;
	}	
}
