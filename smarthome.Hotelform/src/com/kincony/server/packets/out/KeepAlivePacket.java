package com.kincony.server.packets.out;

import org.jboss.netty.buffer.ChannelBuffer;

import com.kincony.server.packets.OutPacket;
import com.kincony.server.support.Protocol;

public class KeepAlivePacket extends OutPacket{
	public KeepAlivePacket(String devId, int sequence, int ret) {
		super(Protocol.MSG_DEV_HEART_BEAT_RE, sequence, devId);
		this.ext[0] = (byte)ret;
	}

	@Override
	protected void putBody(ChannelBuffer buf) {
		
	}
}
