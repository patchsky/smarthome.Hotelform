package com.kincony.server;

import java.net.InetSocketAddress;

public interface Server {
	void startServer() throws Exception;
	
	void startServer(int port) throws Exception;;
	
	void startServer(InetSocketAddress socketAddress) throws Exception;
	
	void stopServer() throws Exception;
	
	InetSocketAddress getSocketAddress();
}
