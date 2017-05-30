package com.kincony.server.packets.in;

import org.jboss.netty.buffer.ChannelBuffer;

import com.kincony.server.packets.InPacket;
import com.kincony.server.packets.PacketParseException;

/**
 * 发给设备
帧头	帧类型	设备类型	设备地址	长度	帧内容	校检	帧尾
0x02	0x02	0x0240	0x0000	0x0100	1/0	1字节	0x03
 * @author joey
 *
 */
public class SendDDataReplyPacket extends InPacket{
	
	protected byte frameHead;
	protected byte frameType;
	protected char devType;
	protected char devAddr;
	protected int frameLen;
	protected int frameStatus;
	protected byte check;
	protected byte frameTail;
	
	protected byte[] devData;
	/**
	 * 成功，失败 1，0
	 */
	protected byte[] frameBody;
	
	public SendDDataReplyPacket(ChannelBuffer buf) throws PacketParseException {
		super(buf);
	}

	@Override
	protected void parseBody(ChannelBuffer buf) throws PacketParseException {
		int length = buf.readableBytes();
		
		devData = new byte[length];
		buf.getBytes(buf.readerIndex(), devData);
		
		/*if(length != 11){
			return;
		}*/
		
		frameHead = buf.readByte();
		frameType = buf.readByte();
		devType = buf.readChar();
		devAddr = buf.readChar();
		frameLen = buf.readChar();
		
		length = buf.readableBytes();
		
		frameBody = new byte[length - 2];
		
		buf.readBytes(frameBody);
		
		check = buf.readByte();
		frameTail = buf.readByte();
	}

	public byte getFrameHead() {
		return frameHead;
	}

	public void setFrameHead(byte frameHead) {
		this.frameHead = frameHead;
	}

	public byte getFrameType() {
		return frameType;
	}

	public void setFrameType(byte frameType) {
		this.frameType = frameType;
	}

	public char getDevType() {
		return devType;
	}

	public void setDevType(char devType) {
		this.devType = devType;
	}

	public char getDevAddr() {
		return devAddr;
	}

	public void setDevAddr(char devAddr) {
		this.devAddr = devAddr;
	}

	public int getFrameLen() {
		return frameLen;
	}

	public void setFrameLen(int frameLen) {
		this.frameLen = frameLen;
	}

	public int getFrameStatus() {
		return frameStatus;
	}

	public void setFrameStatus(int frameStatus) {
		this.frameStatus = frameStatus;
	}

	public byte getCheck() {
		return check;
	}

	public void setCheck(byte check) {
		this.check = check;
	}

	public byte getFrameTail() {
		return frameTail;
	}

	public void setFrameTail(byte frameTail) {
		this.frameTail = frameTail;
	}

	public byte[] getDevData() {
		return devData;
	}

	public void setDevData(byte[] devData) {
		this.devData = devData;
	}

	public byte[] getFrameBody() {
		return frameBody;
	}

	public void setFrameBody(byte[] frameBody) {
		this.frameBody = frameBody;
	}
}
