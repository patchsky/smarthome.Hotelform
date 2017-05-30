package com.kincony.server.packets.out;

import org.jboss.netty.buffer.ChannelBuffer;

import com.kincony.server.packets.OutPacket;
import com.kincony.server.support.Protocol;
import com.kincony.server.util.Util;

public class DataReplyPacket extends OutPacket{
	private byte[] devData;
	
	public DataReplyPacket(char command, String devId) {
		super(command, devId);
		
	}

	@Override
	protected void putBody(ChannelBuffer buf) {
		buf.writeBytes(devData);
	}

	public void setDevData(byte[] devData) {
		this.devData = devData;
	}
}
