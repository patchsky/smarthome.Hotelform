package com.kincony.server.support;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kincony.server.bean.SocketChannel;
import com.kincony.server.common.GlobalConst;
import com.kincony.server.common.GlobalMethod;
import com.kincony.server.event.IPacketListener;
import com.kincony.server.event.PacketEvent;
import com.kincony.server.helper.MessageHelper;
import com.kincony.server.helper.PacketProcessHelper;
import com.kincony.server.helper.UserManager;
import com.kincony.server.packets.InPacket;
import com.kincony.server.packets.OutPacket;
import com.kincony.server.packets.PacketHelper;
import com.kincony.server.packets.PacketHistory;
import com.kincony.server.packets.in.AlarmPacket;
import com.kincony.server.packets.in.ReportPacket;
import com.kincony.server.util.Util;

public class PacketProcessor implements IPacketListener {

	private static Logger logger = LoggerFactory
			.getLogger(PacketProcessor.class);
	private PacketHelper packetHelper;
	/** 接收队列 */
	private Queue<InPacket> receiveQueue;
	/** 线程执行器 */
	public Executor executor = null;
	/** 单线程执行器 */
	public static final ScheduledExecutorService executorScheduled = Executors.newSingleThreadScheduledExecutor();
	/** 包事件触发过程 */
	protected Callable<Object> packetEventTrigger;
	/** 包处理器路由器 */
	private ProcessorRouter router;
	/** 包处理器数目 */
	protected static final int PROCESSOR_COUNT = 3;

	/** 重发过程 */
	protected ResendTrigger<Object> resendTrigger;
	
	protected KeepAliveTrigger<Object> keepAliveTrigger;
	
	private PacketProcessHelper packetProcessHelper;
	
	private MessageHelper messageHelper;
	
	private UserManager userManager;

	/** 表示是否于主机连接 */
	private boolean connctioned;
	
	private Channel channel;
	
	private final ChannelGroup allChannels = new DefaultChannelGroup("vlghs-server");
	
	private final ConcurrentHashMap<String, String[]> allKeysChannelId = new ConcurrentHashMap<String, String[]>();

	public PacketProcessor() {
		executor = Executors.newCachedThreadPool();
		
		router = new ProcessorRouter(PROCESSOR_COUNT);

		packetEventTrigger = new PacketEventTrigger<Object>(this);

		packetHelper = new PacketHelper();
		receiveQueue = new LinkedList<InPacket>();
		resendTrigger = new ResendTrigger<Object>(this);
		
		userManager = new UserManager();
		
		keepAliveTrigger = new KeepAliveTrigger<Object>(this);
		
		if(packetProcessHelper == null){
			packetProcessHelper = new PacketProcessHelper(this);
		}
		messageHelper = new MessageHelper();
		
		router.installProcessor(this);
	}

	public void release() {
		resendTrigger.clear();
		//keepAliveTrigger.clear();
		executorScheduled.shutdownNow();
		packetHelper.getHistory().clear();
	}
	
	public void processPacket(final InPacket packet){
		/*executor.execute(new Runnable() {
			@Override
			public void run() {
				PacketEvent e = new PacketEvent(packet);
	            e.type = packet.getCommand();
	            packetArrived(e);
			}
		});*/
		PacketEvent e = new PacketEvent(packet);
        e.type = packet.getCommand();
        packetArrived(e);
	}

	/**
	 * 发送包到指定的channel，重发机制
	 * 
	 * @param packet
	 */
	public void send(OutPacket packet) {
		//Channel channel = packet.getChannel();


	

		if(channel != null){
			
			channel.write(packet, new InetSocketAddress(packet.getHostName(), packet.getPort()));
			logger.debug("发送包" + Util.getCommandString(packet.getCommand()) + "　devId：" + packet.getDevId() + " ip:"+packet.getHostName());
			
			/*packet.setTimeout(Protocol.TIMEOUT_SEND);
			packet.setTimeout(Protocol.MAX_RESEND);
			this.addResendPacket(packet);*/
			
			
			
		}
	}
	
	/**
	 * 发送包到指定的channel，无重发机制
	 * 
	 * @param packet
	 */
	public void sendStrategy(OutPacket packet) {
		//Channel channel = packet.getChannel();

		// 判断是否连接
		if(channel != null){
			System.err.println("进了");
			channel.write(packet, new InetSocketAddress(packet.getHostName(), packet.getPort()));
			logger.info("发送包" + Util.getCommandString(packet.getCommand()) + "　devId：" + packet.getDevId() + " ip:"+packet.getHostName()+ " port:"+packet.getPort());
			//packet.setTimeout(System.currentTimeMillis() + Protocol.TIMEOUT_SEND);
			//this.addResendPacket(packet);
		}
	}
	
	/**
	 * 发送包到指定的channel，无重发机制
	 * 
	 * @param packet
	 */
	public void sends(OutPacket packet) {
		//Channel channel = packet.getChannel();

		// 判断是否连接
		if(channel != null){
			channel.write(packet, new InetSocketAddress(packet.getHostName(), packet.getPort()));
			logger.info("发送包" + Util.getCommandString(packet.getCommand()) + "　devId：" + packet.getDevId() + " ip:"+packet.getHostName()+ " port:"+packet.getPort());
			//packet.setTimeout(System.currentTimeMillis() + Protocol.TIMEOUT_SEND);
			//this.addResendPacket(packet);
		}
	}
	
	/**
	 * 删除一个重发包
	 * 
	 * @param packet
	 */
	public void removeResendPacket(OutPacket packet) {
		resendTrigger.remove(packet);
	}

	/**
	 * 添加一个包到重发队列
	 * 
	 * @param packet
	 * @param port
	 */
	public void addResendPacket(OutPacket packet) {
		resendTrigger.add(packet);
	}

	/**
	 * 添加一个包到接收队列
	 * 
	 * @param packet
	 */
	public synchronized void addIncomingPacket(InPacket packet) {
		if (packet == null)
			return;
		receiveQueue.offer(packet);
		((ExecutorService) executor).submit(packetEventTrigger);
	}

	/**
	 * 从接收队列中得到第一个包，并且把这个包从队列中移除
	 * 
	 * @return 接收队列的第一个包，没有返回null
	 */
	public synchronized InPacket removeIncomingPacket() {
		return receiveQueue.poll();
	}

	/**
	 * @return true表示接收队列为空
	 */
	public synchronized boolean isEmpty() {
		return receiveQueue.isEmpty();
	}

	/**
	 * 通知包处理器包到达事件
	 * 
	 * @param e
	 */
	public void firePacketArrivedEvent(PacketEvent e) {
		router.packetArrived(e);
	}

	@Override
	public void packetArrived(PacketEvent e) {
		InPacket in = (InPacket) e.getSource();

		/*if (in instanceof UnknownInPacket) {
			logger.error("收到一个未知格式包 " + in.getMessage());
			return;
		}*/

		// 显示调试信息
		/*if (in instanceof ErrorPacket) {
			logger.debug("开始处理错误通知包，错误类型："
					+ Util.getErrorString(((ErrorPacket) in).errorCode));
		} else {
			logger.debug("开始处理" + in.toString());

		}*/
		
		logger.info("开始处理" + in.toString() + "　devId:" + in.getDevId() + " commond：" + Util.getCommandString(in.getCommand()));
		
		//removeResendPacket(in);

		// 现在开始判断包的类型，作出相应的处理
		switch (in.getCommand()) {
			case Protocol.MSG_DEV_REG: 
			try {
				packetProcessHelper.processLoginSuccess(in);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} 
				break;
			case Protocol.MSG_DEV_HEART_BEAT:
			try {
				packetProcessHelper.procesKeepAliveSuccess(in);
			} catch (Exception e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
				break;
			case Protocol.MSG_CLI_QUERY:
			try {
				packetProcessHelper.processQuerySuccess(in);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} 
				break;
			case Protocol.CMD_UNKNOWN:
				packetProcessHelper.processUnknown(in);
				break;
		}
		
		if(in.getCommand() >= Protocol.MSG_C2D_DATA_START && in.getCommand() <= Protocol.MSG_C2D_DATA_END){
			packetProcessHelper.processCDataSuccess(in);
		}
		if(in.getCommand() >= Protocol.MSG_D2C_DATA_START && in.getCommand() <= Protocol.MSG_D2C_DATA_END){
			packetProcessHelper.processDDataSuccess(in);
		}
		if(in.getCommand() >= Protocol.MSG_D2S_DATA_START && in.getCommand() <= Protocol.MSG_D2S_DATA_END){
			packetProcessHelper.processReportSuccess(in);
		}
		if(in.getCommand() >= Protocol.MSG_D2S_ALARM_START && in.getCommand() <= Protocol.MSG_D2S_ALARM_END){
			packetProcessHelper.processAlarmSuccess(in);
		}
	}
	
	/**
	 * @return Returns the monitor.
	 */
	public PacketHistory getHistory() {
		return packetHelper.getHistory();
	}

	public PacketHelper getPacketHelper() {
		return packetHelper;
	}

	public MessageHelper getMessageHelper() {
		return messageHelper;
	}

	public PacketProcessHelper getPacketProcessHelper() {
		return packetProcessHelper;
	}

	public ProcessorRouter getRouter() {
		return router;
	}
	
	public ResendTrigger<Object> getResendTrigger() {
		return resendTrigger;
	}

	public KeepAliveTrigger<Object> getKeepAliveTrigger() {
		return keepAliveTrigger;
	}

	public boolean isConnctioned() {
		return connctioned;
	}

	public void setConnctioned() {
		this.connctioned = true;
	}
	
	public void setDisConnctioned() {
		/*if(this.connctioned && !SystemConfig.getInstance().isClosed()){
				main.getDisplay().syncExec(new Runnable() {
					public void run() {
						if(main.getDisplay().getActiveShell() != null){
							MessageBoxHelper.openWarning(main.getDisplay().getActiveShell(), "与主机断开连接！");
						}
					}
				});
		}*/
		resendTrigger.clear();
		this.connctioned = false;
	}

	public ChannelGroup getAllChannels() {
		return allChannels;
	}
	
	public void addSocketAddress(String dockUser, String[] ipPort){
		if(channel == null) return;
		
		allKeysChannelId.put(dockUser,ipPort);
	}
	
	public String[] getSocketAddress(String dockUser){
		return allKeysChannelId.get(dockUser);
	}
	
	public void removeSocketAddress(String dockUser){
		allKeysChannelId.remove(dockUser);
	}
	
	public UserManager getUserManager() {
		return userManager;
	}

	public void setPacketProcessHelper(PacketProcessHelper packetProcessHelper) {
		this.packetProcessHelper = packetProcessHelper;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}
}
