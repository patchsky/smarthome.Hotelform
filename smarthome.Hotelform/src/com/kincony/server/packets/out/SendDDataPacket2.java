package com.kincony.server.packets.out;

import org.jboss.netty.buffer.ChannelBuffer;

import com.kincony.server.packets.OutPacket;
import com.kincony.server.support.Protocol;
import com.kincony.server.util.Util;

/**
 * 发给设备
 * 帧头	帧类型	设备类型	设备地址	长度	帧内容	校检	帧尾
0x02	0x02	0x0240	0x0000	0x0100	0、1	1字节	0x03
 * @author joey
 *
 */
public class SendDDataPacket2 extends OutPacket{
	
	protected byte frameHead = 0x02;
	protected byte frameType = 0x02;
	protected char devType = 0x0240;
	protected char devAddr = 0x0000;
	protected int frameLen = 0x0100;
	protected byte check = 0x00;
	protected byte frameTail = 0x03;
	
	private byte[] frameBody;
	
	public SendDDataPacket2(String devId) {
		super(Protocol.MSG_C2D_DATA_START, devId);
	}

	@Override
	protected void putBody(ChannelBuffer buf) {
		buf.writeByte(frameHead);
		buf.writeByte(frameType);
		buf.writeChar(devType);
		buf.writeChar(devAddr);
		buf.writeChar(frameLen);
		
		buf.writeBytes(frameBody);
		byte[] cb = new byte[frameBody.length + 7];
		
		cb[0] = frameType;
		cb[1] = 0x02;
		cb[2] = 0x04;
		cb[3] = 0x00;
		cb[4] = 0x00;
		cb[5] = 0x01;
		cb[6] = 0x00;
		
		System.arraycopy(frameBody, 0, cb, 7, frameBody.length);
		check = Util.getValidateByte(cb);
		
		buf.writeByte(check);
		buf.writeByte(frameTail);
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

	public void setFrameLen(int frameLen) {
		this.frameLen = Util.byte2ToInt(Util.toLH(frameLen));
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

	public byte[] getFrameBody() {
		return frameBody;
	}

	public void setFrameBody(byte[] frameBody) {
		this.frameBody = frameBody;
	}
}
