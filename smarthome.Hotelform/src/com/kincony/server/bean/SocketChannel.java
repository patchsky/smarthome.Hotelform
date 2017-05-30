package com.kincony.server.bean;

import java.net.SocketAddress;

import org.jboss.netty.channel.Channel;

public class SocketChannel {
	private Channel channel;
	private SocketAddress socketAddress;
	
	public SocketChannel(Channel channel, SocketAddress socketAddress){
		this.channel = channel;
		this.socketAddress = socketAddress;
	}
	

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public SocketAddress getSocketAddress() {
		return socketAddress;
	}

	public void setSocketAddress(SocketAddress socketAddress) {
		this.socketAddress = socketAddress;
	}
}
