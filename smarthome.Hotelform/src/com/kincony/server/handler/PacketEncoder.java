package com.kincony.server.handler;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

import com.kincony.server.packets.OutPacket;
import com.kincony.server.support.Protocol;

public class PacketEncoder extends OneToOneEncoder{

	@Override
	protected Object encode(ChannelHandlerContext ctx, Channel channel,
			Object obj) throws Exception {
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		
		OutPacket packet = (OutPacket) obj;
		packet.fill(buffer);
		//buffer.setInt(0, buffer.readableBytes() - Protocol.LENGTH_FIELD_LENGTH);
		return buffer;
	}
}
