package com.kincony.server.event;

/**
 * 包事件监听器
 * @author JOEY
 *
 */
public interface IPacketListener {
	/**
     * 包到达时调用此方�?
     * 
     * @param e
     */
    public void packetArrived(PacketEvent e);
}
