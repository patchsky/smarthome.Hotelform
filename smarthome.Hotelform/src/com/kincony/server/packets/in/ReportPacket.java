package com.kincony.server.packets.in;

import org.jboss.netty.buffer.ChannelBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kincony.server.packets.InPacket;
import com.kincony.server.packets.PacketParseException;
import com.kincony.server.util.Util;

/**
 * 设备报警包
 * @author joey
 * 
 * 帧头	帧类型	设备类型	设备地址	长度	帧内容	校检	帧尾
0x02	0x08	0x0140	0x0000	0x0200	2字节	1字节	0x03

帧类型：0x03
b、设备类型：0x4001
c、设备地址：2字节0
d、长度：7
e、帧内容： TDS（2字节），1级滤芯寿命（1字节0-100%），2级滤芯寿命（1字节0-100%），3级滤芯寿命（1字节0-100%），4级滤芯寿命（1字节0-100%），5级滤芯寿命（1字节0-100%），
接收：
帧头	帧类型	设备类型	设备地址	长度	帧内容	校检	帧尾
0x02	0x03	0x0140	0x0000	0x0700	7字节	1字节	0x03

 *
 */
public abstract class ReportPacket extends InPacket{
	private static Logger logger = LoggerFactory.getLogger(ReportPacket.class);
	
	protected byte frameHead;
	protected byte frameType;
	protected char devType;
	protected char devAddr;
	protected int frameLen;
	
	protected byte check;
	protected byte frameTail;
	
	protected byte[] devData;
	
	public ReportPacket(ChannelBuffer buf) throws PacketParseException {
		super(buf);
	}

	@Override
	protected void parseBody(ChannelBuffer buf) throws PacketParseException {
		int length = buf.readableBytes();
		
		devData = new byte[length];
		buf.getBytes(buf.readerIndex(), devData);
		
		/*if(length != 12 && length != 17){
			return;
		}*/
		
		frameHead = buf.readByte();
		frameType = buf.readByte();
		devType = buf.readChar();
		devAddr = buf.readChar();
		frameLen = buf.readChar();
		parseFrame(buf, frameLen);
		check = buf.readByte();
		frameTail = buf.readByte();
	}
	
	protected abstract void parseFrame(ChannelBuffer buf, int frameLen) throws PacketParseException;

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
}
