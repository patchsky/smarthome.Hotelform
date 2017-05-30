package com.kincony.server.support;

import java.util.concurrent.Callable;

import com.kincony.server.event.PacketEvent;
import com.kincony.server.packets.InPacket;

/**
 * 触发PacketEvent
 * @author JOEY
 *
 * @param <T>
 */
public class PacketEventTrigger <T> implements Callable<T> {

	private PacketProcessor packetProcessor;
	
	public PacketEventTrigger(PacketProcessor packetProcessor){
		this.packetProcessor = packetProcessor;
	}
	
	@Override
	public T call() throws Exception {
		InPacket packet = packetProcessor.removeIncomingPacket();
        while(packet != null) {
            // 通知包事件监听器
            PacketEvent e = new PacketEvent(packet);
            e.type = packet.getCommand();
            packetProcessor.firePacketArrivedEvent(e);
            // 得到下一个包
            packet = packetProcessor.removeIncomingPacket();
        }
		return null;
	}
}
