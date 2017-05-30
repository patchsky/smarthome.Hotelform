package com.kincony.server;

import java.io.Serializable;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import org.jboss.netty.bootstrap.ConnectionlessBootstrap;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.group.ChannelGroupFuture;
import org.jboss.netty.channel.socket.DatagramChannel;
import org.jboss.netty.channel.socket.nio.NioDatagramChannelFactory;
import org.jboss.netty.util.HashedWheelTimer;
import org.jboss.netty.util.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.kincony.server.helper.PacketProcessHelper;
import com.kincony.server.support.PacketProcessor;
import com.kincony.service.system.host.HostService;


public class DockServer {

	private static Logger logger = LoggerFactory.getLogger(DockServer.class);
	private ChannelFactory factory;
	private boolean isStarted;
	@Resource(name="hostService")
	private HostService hostService;
	private PacketProcessor packetProcessor;

	public DockServer() {

		System.out.println("");
		// packetProcessor = new PacketProcessor();
		// this.startServer();
	}

	/**
	 * 服务端主程序
	 */
	public void startServer() {

		logger.debug("服务器启动中...");

		if (packetProcessor == null)
			packetProcessor = new PacketProcessor();

		Timer timer = new HashedWheelTimer();

		factory = new NioDatagramChannelFactory(Executors.newCachedThreadPool());

		ConnectionlessBootstrap bootstrap = new ConnectionlessBootstrap(factory);

		// Configure the pipeline factory.
		bootstrap.setPipelineFactory(new DockServerPipelineFactory(
				packetProcessor));

		// bootstrap.setOption("receiveBufferSizePredictorFactory", new
		// FixedReceiveBufferSizePredictorFactory(Protocol.MAX_PACKET_SIZE));
		bootstrap.setOption("tcpNoDelay", true);
		bootstrap.setOption("keepAlive", true);
		// bootstrap.setOption("reuseAddress", true);
		// Bind and start to accept incoming connections.
		
		/*DatagramChannel channel = (DatagramChannel) bootstrap
				.bind(new InetSocketAddress("192.168.1.178", 8888));*/
		DatagramChannel channel = (DatagramChannel) bootstrap
				.bind(new InetSocketAddress(8888));
		packetProcessor.setChannel(channel);
		packetProcessor.getAllChannels().add(channel);
		isStarted = true;

		logger.debug("服务器启动成功！");
	}

	public void start() {
		try {
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						System.err.println("服务15秒后启动");
						logger.info("服务15秒后启动");
						hostService.editAllStatus();
						Thread.sleep(15000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					startServer();
				}
			});
			t.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void stop() {
		if (isStarted) {
			if (packetProcessor.getAllChannels() != null) {
				ChannelGroupFuture future = packetProcessor.getAllChannels()
						.close();
				future.awaitUninterruptibly();
			}
			if (factory != null) {
				factory.releaseExternalResources();
				factory = null;
			}
			if (packetProcessor != null) {
				packetProcessor.release();
			}

			isStarted = false;
			logger.debug("服务器停止!");
			if (factory != null) {
				factory.releaseExternalResources();
				factory = null;
			}
		}
	}

	public boolean isStarted() {
		return isStarted;
	}

	public void setPacketProcessor(PacketProcessor packetProcessor) {
		this.packetProcessor = packetProcessor;
	}

	public static void main(String args[]) {

	}
}
