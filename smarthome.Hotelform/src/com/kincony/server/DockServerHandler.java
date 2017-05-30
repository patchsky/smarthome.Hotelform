package com.kincony.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ChildChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kincony.server.packets.InPacket;
import com.kincony.server.support.PacketProcessor;

public class DockServerHandler extends SimpleChannelUpstreamHandler{
	 private static Logger logger = LoggerFactory.getLogger(DockServerHandler.class);
	
	private PacketProcessor packetProcessor;
	
	public DockServerHandler(PacketProcessor packetProcessor){
		this.packetProcessor = packetProcessor;
	}
	
    @Override
    public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e) throws Exception {
        if (e instanceof ChannelStateEvent) {
        	logger.debug(e.toString());
        }
        super.handleUpstream(ctx, e);
    }
    
    public void channelOpen(
            ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        ctx.sendUpstream(e);
    }
    
    public void channelBound(
            ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        ctx.sendUpstream(e);
    }

    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
    	Channel channel = e.getChannel();
    	//packetProcessor.removeChannel(packetProcessor.getChannel(channel.getId()));
    	//packetProcessor.getAllChannels().add(channel);
    	
    	super.channelConnected(ctx, e);
    }


    /** 
     * channel关闭触发事件 
     * @param ctx 
     * @param e 
     * @throws Exception 
     */ 
    @Override 
    public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception { 
    		//packetProcessor.removeChannel(ctx.getChannel());
    		super.channelClosed(ctx, e);
	}

   
    @Override
	public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
    	//packetProcessor.removeChannel(ctx.getChannel());
    	
    	super.channelDisconnected(ctx, e);
    }
    
    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
    	List<InPacket> list = (List<InPacket>)e.getMessage();
    	if(list != null){
    		for(InPacket packet : list){
    			 if(packet != null) {
		        	//packet.setChannel(e.getChannel());
		        	//packet.setSocketAddress(e.getRemoteAddress());
		        	
		        	InetSocketAddress isa = (InetSocketAddress)e.getRemoteAddress();
		        	
		        	//packet.setSocketAddress(new InetSocketAddress(isa.getHostName(), isa.getPort()));
		        	packet.setHostName(isa.getAddress().getHostAddress());
		        	packet.setPort(isa.getPort());
		        	
		        	//放入接收队列
		        	//packetProcessor.processPacket(packet);
		        	packetProcessor.addIncomingPacket(packet);
		        }
        	}
    	}
    	
    	
        /*InPacket packet = (InPacket) e.getMessage();
        if(packet != null) {
        	//packet.setChannel(e.getChannel());
        	//packet.setSocketAddress(e.getRemoteAddress());
        	
        	InetSocketAddress isa = (InetSocketAddress)e.getRemoteAddress();
        	
        	//packet.setSocketAddress(new InetSocketAddress(isa.getHostName(), isa.getPort()));
        	packet.setHostName(isa.getHostName());
        	packet.setPort(isa.getPort());
        	
        	//放入接收队列
        	//packetProcessor.processPacket(packet);
        	packetProcessor.addIncomingPacket(packet);
        }*/
        //super.messageReceived(ctx, e);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
    	//logger.error("Unexpected exception from downstream.", e.getCause());
        
    	// 处理IOException，主动关闭channel
    	
    	//packetProcessor.removeChannel(ctx.getChannel());
    	
 		if (e.getCause() != null && e.getCause() instanceof IOException) {
 			e.getChannel().close();
 		}
    }
    
    public void childChannelOpen(
            ChannelHandlerContext ctx, ChildChannelStateEvent e) throws Exception {
        ctx.sendUpstream(e);
    }
}
