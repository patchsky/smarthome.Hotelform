package com.kincony.server.packets;

import org.jboss.netty.buffer.ChannelBuffer;

import com.kincony.server.packets.PacketParseException;
import com.kincony.server.support.Protocol;

public class ErrorPacket extends InPacket{

    /** 远端已经关闭连接 */
    public static final int ERROR_REMOTE_CLOSED = 0;
    /** 操作超时 */
    public static final int ERROR_TIMEOUT = 1;
    
    public int errorCode;
    public String portName;
    
    // 用在超时错误中
    public OutPacket timeoutPacket;
    
    public ErrorPacket(int errorCode, String devId){
        super(Protocol.CMD_UNKNOWN, devId);
        this.errorCode = errorCode;
    }
    
	@Override
	protected void parseBody(ChannelBuffer buf) throws PacketParseException {
		
	}

}