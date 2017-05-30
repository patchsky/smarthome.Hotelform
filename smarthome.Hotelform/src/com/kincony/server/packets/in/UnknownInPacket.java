package com.kincony.server.packets.in;

import org.jboss.netty.buffer.ChannelBuffer;

import com.kincony.server.packets.InPacket;
import com.kincony.server.packets.PacketParseException;

public class UnknownInPacket extends InPacket{
	
	public UnknownInPacket(ChannelBuffer buf) throws PacketParseException{
		super(buf);
	}
	
	@Override
	protected void parseBody(ChannelBuffer buf) throws PacketParseException {
		
	}

}
