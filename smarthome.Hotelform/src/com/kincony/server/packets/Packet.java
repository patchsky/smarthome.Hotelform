package com.kincony.server.packets;

import java.net.SocketAddress;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;

import com.kincony.server.packets.PacketParseException;
import com.kincony.server.support.Protocol;
import com.kincony.server.util.Util;

public abstract class Packet{
	
	/**
	 * 标记位
	 */
	protected byte mark = Protocol.MARK;
	
	/**
	 * 版本号
	 */
	protected byte version = 0x01;
	/**
	 * 包命令, 0x00~0x01
	 */
	protected char command;
	
	protected int sequence;
	
	/**
	 * 数据长度
	 */
	protected int bodyLength;
	
	/**
	 * 设备号
	 */
	protected String devId;
	
	protected byte[] ext = new byte[]{0,0,0,0,0,0,0,0,0,0};
	
	protected boolean duplicated;
	
	protected String hostName;
	
	protected int port;
	
	protected Packet(){
	}
	
	public Packet(char command, int sequence, String devId){
		this.command = command;
		this.sequence = sequence;
		this.devId = devId;
	}
	
	protected Packet(ChannelBuffer buf) throws PacketParseException{
		// 解析头部
		parseHeader(buf);
		
		// 解析包体
		parseBody(buf);
		
		// 解析底部
		parseTail(buf);
	}
	
	/**
	 * 初始化包头
	 * 
	 * @param buf
	 *
	 */
	protected abstract void putHead(ChannelBuffer buf);
	
	/**
	 * 初始化包体
	 * 
	 * @param buf
	 * 			ByteBuffer
	 */
	protected abstract void putBody(ChannelBuffer buf);
	
	/**
	 * 将包尾部转化为字节流
	 * 
	 */
	protected abstract void putTail(ChannelBuffer buf);	
	
	/**
	 * 解析包头
	 * 
	 * @param buf
	 * 		ByteBuffer
	 * @throws PacketParseException
	 * 		如果解析出错
	 */
	protected abstract void parseHeader(ChannelBuffer buf) throws PacketParseException;
	
	/**
	 * 解析包体
	 * 
	 * @param buf
	 * 			ChannelBuffer
	 * @throws PacketParseException
	 * 			如果解析出错
	 */
	protected abstract void parseBody(ChannelBuffer buf) throws PacketParseException;
	
	/**
	 * 解析底部
	 * 
	 * @param buf
	 * 			ChannelBuffer
	 * @throws PacketParseException
	 * 			如果解析出错
	 */
	protected abstract void parseTail(ChannelBuffer buf) throws PacketParseException;
	
	/**
	 * 加密包体
	 * 
	 * @param b 
	 * 		未加密的字节数组
	 * @param offset
	 * 		包体开始的偏移
	 * @param length
	 * 		包体长度
	 * @return
	 * 		加密的包体
	 */
	protected abstract byte[] encryptBody(byte[] b, int offset, int length);
	
	/**
	 * 解密包体
	 * 
     * @param body
     * 			包体字节数组
     * @param offset
     * 			包体开始偏移
     * @param length
     * 			包体长度
     * @return 解密的包体字节数组
     */
	protected abstract byte[] decryptBody(byte[] body, int offset, int length);
	
	public final boolean equals(Object obj) {
		if (obj instanceof Packet)
			return equals((Packet) obj);
		else
			return super.equals(obj);
	}
	
	/**
	 * 是否与另一个包相等. 必须command、序列号、posId都相等才能认为两个包相等. 即使这两个包并非相同类型.
	 * 
	 * @param packet
	 *                   被比较的包.
	 * @return 相等返回true, 否则返回false.
	 */
	public final boolean equals(Packet packet) {
		return 
			command == packet.command && devId.equals(packet.devId);
	}

	/**
	 * 把序列号和命令拼起来作为哈希码.
	 */
	public final int hashCode(){
		return (sequence << 16) | command;
	}
	
	public char getCommand() {
		return command;
	}

	public int getSequence() {
		return sequence;
	}
	
	public int getBodyLength() {
		return bodyLength;
	}

	public void setBodyLength(char bodyLength) {
		this.bodyLength = bodyLength;
	}

	public byte[] getExt() {
		return ext;
	}

	public void setExt(byte[] ext) {
		this.ext = ext;
	}

	public byte getMark() {
		return mark;
	}

	public byte getVersion() {
		return version;
	}

	public String getDevId() {
		return devId;
	}
	
	public byte[] getDevIdBytes() {
		return Util.getBytes(devId, 28);
	}

	public boolean isDuplicated() {
		return duplicated;
	}

	public void setDuplicated(boolean duplicated) {
		this.duplicated = duplicated;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
}
