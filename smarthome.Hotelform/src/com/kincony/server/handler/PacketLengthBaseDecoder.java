package com.kincony.server.handler;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.LengthFieldBasedFrameDecoder;

import com.kincony.server.packets.InPacket;
import com.kincony.server.support.PacketProcessor;


public class PacketLengthBaseDecoder extends LengthFieldBasedFrameDecoder{
	private PacketProcessor packetProcessor;
	
	public PacketLengthBaseDecoder(PacketProcessor packetProcessor, int maxFrameLength, int lengthFieldOffset,
			int lengthFieldLength) {
		super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
		this.packetProcessor = packetProcessor;
	}

	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel,
			ChannelBuffer buf) throws Exception {
		Object buffer = super.decode(ctx, channel, buf);
		if(buffer != null){
			InPacket packet = packetProcessor.getPacketHelper().processIn((ChannelBuffer)buffer);
			if(packet != null){
				//packet.setChannelId(channel.getId());
			}
			return packet;
		}
		return null;
	}
	

}
