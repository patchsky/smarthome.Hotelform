package com.kincony.server.support;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import com.kincony.server.packets.ErrorPacket;
import com.kincony.server.packets.InPacket;
import com.kincony.server.packets.OutPacket;
import com.kincony.server.packets.PacketHistory;

public class ResendTrigger<T> implements Callable<T> {
	private PacketProcessor packetProcessor;

	// 检查应答包是否到达的类
    private PacketHistory history;
    
	// 超时队列
    private Queue<OutPacket> timeoutQueue;

    // temp variable
    private String portName;
	
	public ResendTrigger(PacketProcessor packetProcessor) {
        this.packetProcessor = packetProcessor;
        history = packetProcessor.getHistory();
        timeoutQueue = new LinkedList<OutPacket>();
       packetProcessor.executorScheduled.schedule(this, Protocol.TIMEOUT_SEND, TimeUnit.MILLISECONDS);
	}
	
    /**
     * 添加一个包到超时队列
     * 
     * @param packet
     * 		发送包对象
     * @param name
     * 		port名称
     */
    public synchronized void add(OutPacket packet) {
        timeoutQueue.offer(packet);
    }
    
    /**
     * 清空重发队列
     */
    public synchronized void clear() {
    	timeoutQueue.clear();
    }
    
    /**
     * 得到超时队列的第一个包，不把它从队列中删除
     * 
     * @return 
     * 		超时队列的第一个包，如果没有，返回null
     */
    public synchronized OutPacket get() {
		return timeoutQueue.peek();
    }
    
    /**
     * 得到超时队列的第一个包，并把它从队列中删除
     * 
     * @return 
     * 		超时队列的第一个包，如果没有，返回null
     */
    public synchronized OutPacket remove() {
    	return timeoutQueue.poll();
    }
    
    /**
     * 删除ack对应的请求包
     * 
     * @param ack
     */
    public synchronized void remove(OutPacket ack) {
        int hash = ack.hashCode();
        for(Iterator<OutPacket> i = timeoutQueue.iterator(); i.hasNext(); ) {
        	OutPacket packet = i.next();
        	if(packet.hashCode() == hash){
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
        OutPacket packet = get();
        if(packet == null)
            return Protocol.TIMEOUT_SEND;
        else
            return packet.getTimeout() - System.currentTimeMillis();
    }    
    
    /**
     * 触发超时事件
     * @param p
     */
    private void fireOperationTimeOutEvent(OutPacket packet) {
    	ErrorPacket error = new ErrorPacket(ErrorPacket.ERROR_TIMEOUT, packet.getDevId());
    	error.timeoutPacket = packet;
    	packetProcessor.addIncomingPacket(error);
    }

	/* (non-Javadoc)
	 * @see java.util.concurrent.Callable#call()
	 */
	public T call() throws Exception {
    	long t = getTimeoutLeft();
		while(t <= 0) {
			OutPacket packet = remove();      
			
			// 发送
			if(packet != null && !history.check(packet, false)) {
				if(packet.needResend()) {
					// 重发次数未到最大，重发
					packetProcessor.send(packet);
				} else {
					// 触发操作超时事件
					fireOperationTimeOutEvent(packet);
				}   
			} 
			
			t = getTimeoutLeft();
		}
		packetProcessor.executorScheduled.schedule(this, t, TimeUnit.MILLISECONDS);

    	return null;
	}
}
