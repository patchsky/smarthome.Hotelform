package com.kincony.server.packets;

import org.jboss.netty.buffer.ChannelBuffer;

import com.kincony.server.packets.PacketParseException;
import com.kincony.server.support.Protocol;
import com.kincony.server.util.Util;

public abstract class OutPacket extends Packet{
	/** 包起始序列号 */
	protected static int seq = Protocol.SERVER_INIT_SEQUENCE;
	
	/** 超时截止时间，单位ms */
	private long timeout;
	/** 重发计数器 */
	private int resendCountDown;
	
	/**
	 * 
	 * @param command
	 * @throws PacketParseException 
	 */
	public OutPacket(char command, String devId){
		super(command, getNextSeq(), devId);
		this.resendCountDown = Protocol.MAX_RESEND;
	}

	public OutPacket(char command, int sequence, String devId){
		super(command, sequence, devId);
	}
	
	/**
	 * 填充
	 * @param buffer
	 */
	public void fill(ChannelBuffer buffer){
		putHead(buffer);
		putBody(buffer);
		putTail(buffer);
		
		int bodyLength = buffer.readableBytes() - Protocol.PACKET_HEADER_SIZE;
		//buffer.setChar(4, Util.getChar(Integer.toHexString(bodyLength), 0));
		buffer.setChar(4, bodyLength);
	}
	
	/**
	 * 初始化包头
	 * 
	 * @param buf
	 *
	 */
	protected void putHead(ChannelBuffer buf){
		buf.writeByte(mark);
		buf.writeByte(version);
		buf.writeChar(command);
		buf.writeChar(bodyLength);
		buf.writeBytes(getDevIdBytes());
		buf.writeBytes(ext);
	}
	
	
	/**
	 * 将包尾部转化为字节流
	 * 
	 */
	protected  void putTail(ChannelBuffer buf){
	}
	
	@Override
    protected void parseHeader(ChannelBuffer buffer) throws PacketParseException {
	}
	
	@Override
    protected void parseBody(ChannelBuffer buffer) throws PacketParseException {
    }
	
	@Override
	protected void parseTail(ChannelBuffer buf) throws PacketParseException {
		
	}
	
	@Override
    protected byte[] decryptBody(byte[] body, int offset, int length) {
		return body;
    }
	
	/* (non-Javadoc)
	 * @see com.ycmsoft.qqserver.qq.packets.OutPacket#encryptBody(byte[], int, int)
	 */
	@Override
	protected byte[] encryptBody(byte[] body, int offset, int length) {
		return body;
	}
	
	 /**
	 * @return
	 * 		下一个可用的序列号
	 */
	public static int getNextSeq() {
		if(seq >= Integer.MAX_VALUE){
			seq = 0;
	    }
	    seq++;
	    return seq;
	}
	
	/**
	 * 是否需要重发.
	 * 
	 * @return 需要重发返回true, 否则返回false.
	 */
	public final boolean needResend() {
		return (resendCountDown--) > 0;
	}
	
	public long getTimeout() {
		return timeout;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}
}
