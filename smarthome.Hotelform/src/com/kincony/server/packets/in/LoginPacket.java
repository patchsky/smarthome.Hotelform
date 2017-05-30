package com.kincony.server.packets.in;

import org.jboss.netty.buffer.ChannelBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kincony.server.packets.InPacket;
import com.kincony.server.packets.PacketParseException;
import com.kincony.server.util.Util;

public class LoginPacket extends InPacket{
	private static Logger logger = LoggerFactory.getLogger(LoginPacket.class);
	private String devData;

	public LoginPacket(ChannelBuffer buf) throws PacketParseException {
		super(buf);
	}

	@Override
	protected void parseBody(ChannelBuffer buf) throws PacketParseException {
		int bufferLength = this.bodyLength;
		byte[] bytes = new byte[bufferLength];
		buf.readBytes(bytes);
		
		devData = Util.getString(bytes);
	}

	public String getDevData() {
		return devData;
	}
}
