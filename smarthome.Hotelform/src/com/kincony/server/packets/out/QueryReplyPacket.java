package com.kincony.server.packets.out;

import org.jboss.netty.buffer.ChannelBuffer;

import com.kincony.server.packets.OutPacket;
import com.kincony.server.support.Protocol;

public class QueryReplyPacket extends OutPacket{
	
	public QueryReplyPacket(String devId, int ret) {
		super(Protocol.MSG_CLI_QUERY_RE, devId);
		this.ext[0] = (byte)ret;
	}

	@Override
	protected void putBody(ChannelBuffer buf) {
	}
}
