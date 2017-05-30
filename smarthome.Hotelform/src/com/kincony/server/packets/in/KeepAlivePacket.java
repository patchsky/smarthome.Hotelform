package com.kincony.server.packets.in;

import org.jboss.netty.buffer.ChannelBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kincony.server.packets.InPacket;
import com.kincony.server.packets.PacketParseException;
import com.kincony.server.support.Protocol;

public class KeepAlivePacket extends InPacket{
	/** keep alive 时间，单位ms */
	private long timeout;

	public KeepAlivePacket(ChannelBuffer buf) throws PacketParseException {
		super(buf);
	}

	@Override
	protected void parseBody(ChannelBuffer buf) throws PacketParseException {
		
	}

	public long getTimeout() {
		return timeout;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}
}
