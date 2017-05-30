package com.kincony.server.support;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import com.kincony.server.packets.in.KeepAlivePacket;

public class KeepAliveTrigger<T> implements Callable<T> {
	private PacketProcessor packetProcessor;
    
	// 超时队列
    private Queue<KeepAlivePacket> aliveQueue;

	
	public KeepAliveTrigger(PacketProcessor packetProcessor) {
        this.packetProcessor = packetProcessor;
        aliveQueue = new LinkedList<KeepAlivePacket>();
       packetProcessor.executorScheduled.schedule(this, Protocol.TIMEOUT_ALIVE, TimeUnit.MILLISECONDS);
	}
	
    /**
     * 添加一个队列
     * 
     * @param packet
     * 		发送包对象
     * @param name
     * 		port名称
     */
    public synchronized void add(KeepAlivePacket packet) {
    	packet.setTimeout(System.currentTimeMillis() + Protocol.TIMEOUT_ALIVE);
    	this.remove(packet);
    	
    	aliveQueue.offer(packet);
    }
    
    /**
     * 清空重发队列
     */
    public synchronized void clear() {
    	aliveQueue.clear();
    }
    
    /**
     * 得到超时队列的第一个包，不把它从队列中删除
     * 
     * @return 
     * 		超时队列的第一个包，如果没有，返回null
     */
    public synchronized KeepAlivePacket get() {
		return aliveQueue.peek();
    }
    
    /**
     * 得到第一个包，并把它从队列中删除
     * 
     * @return 
     * 		超时队列的第一个包，如果没有，返回null
     */
    public synchronized KeepAlivePacket remove() {
    	return aliveQueue.poll();
    }
    
    /**
     * 删除
     * 
     * @param ack
     */
    public synchronized void remove(KeepAlivePacket ack) {
        for(Iterator<KeepAlivePacket> i = aliveQueue.iterator(); i.hasNext(); ) {
        	KeepAlivePacket packet = i.next();
        	if(packet.equals(ack)){
        		i.remove();
        		break;
        	}
        }
    }
    
    /**
     * 得到下一个包的超时时间
     * 
     * @return 
     * 		下一个包的超时时间，如果队列为空，返回一个固定值
     */
    private long getTimeoutLeft() {
        KeepAlivePacket packet = get();
        if(packet == null)
            return Protocol.TIMEOUT_ALIVE;
        else
            return packet.getTimeout() - System.currentTimeMillis();
    }    

	/* (non-Javadoc)
	 * @see java.util.concurrent.Callable#call()
	 */
	public T call() throws Exception {
    	long t = getTimeoutLeft();
		while(t <= 0) {
			KeepAlivePacket packet = remove();      
			
			if(packet != null) { // 超时离线
				packetProcessor.getPacketProcessHelper().procesKeepAliveLost(packet);
			} 
			
			t = getTimeoutLeft();
		}
		packetProcessor.executorScheduled.schedule(this, 10000, TimeUnit.MILLISECONDS);
    	return null;
	}
}
