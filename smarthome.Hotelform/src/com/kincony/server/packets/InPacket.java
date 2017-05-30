package com.kincony.server.packets;

import org.jboss.netty.buffer.ChannelBuffer;

import com.kincony.server.packets.PacketParseException;
import com.kincony.server.support.Protocol;
import com.kincony.server.util.Util;

public abstract class InPacket extends Packet{
	
	public InPacket(ChannelBuffer buf) throws PacketParseException{
		super(buf);
	}

	public InPacket(char command, String devId) {
		super(command, 0, devId);
	}

	@Override
	protected void parseHeader(ChannelBuffer buf) throws PacketParseException {
		
		mark = buf.readByte();
		version = buf.readByte();
		command = buf.readChar();
		//bodyLength = Integer.parseInt(""+(int)buf.readChar(), 16);
		bodyLength = (int)buf.readChar();
		
		byte[] devIdByte = new byte[28];
		buf.readBytes(devIdByte);
		devId = Util.getString2(devIdByte);
		buf.readBytes(ext);
	}
	
	@Override
	protected void parseTail(ChannelBuffer buf) throws PacketParseException {
		
	}
	
	@Override
    protected byte[] encryptBody(byte[] body, int offset, int len) {
		return body;
    }
	
	@Override
	protected byte[] decryptBody(byte[] body, int offset, int len) {
		return body;
	}
	
	@Override
	protected void putHead(ChannelBuffer buf) {
	}

	@Override
	protected void putBody(ChannelBuffer buf) {
	}

	@Override
	protected void putTail(ChannelBuffer buf) {
	}
}
