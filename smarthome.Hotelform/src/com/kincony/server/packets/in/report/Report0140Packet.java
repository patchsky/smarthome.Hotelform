package com.kincony.server.packets.in.report;

import org.jboss.netty.buffer.ChannelBuffer;

import com.kincony.server.packets.PacketParseException;
import com.kincony.server.packets.in.ReportPacket;

/**
 * 
a、帧类型：0x08
b、设备类型：0x4001
c、设备地址：2字节0
d、长度：2
e、帧内容： 工作模式（1字节）：1-制水，2-冲洗，3-正常工作，
		   运行状态（1字节）：0-滤芯寿命到，1-漏水，2-缺水，2-正常

a、帧类型：0x03
b、设备类型：0x4001
c、设备地址：2字节0
d、长度：7
e、帧内容： TDS（2字节），1级滤芯寿命（1字节0-100%），2级滤芯寿命（1字节0-100%），3级滤芯寿命（1字节0-100%），4级滤芯寿命（1字节0-100%），5级滤芯寿命（1字节0-100%），
 * @author joey
 *
 */
public class Report0140Packet extends ReportPacket{

	private int frameWork;
	private int frameStatus;
	
	private int TDS;
	private int life1,life2,life3,life4,life5;
	
	public Report0140Packet(ChannelBuffer buf) throws PacketParseException {
		super(buf);
	}

	@Override
	protected void parseFrame(ChannelBuffer buf, int frameLen) throws PacketParseException {
		switch(frameType){
			case 0x08:
				frameWork = buf.readByte();
				frameStatus = buf.readByte();
			break;
			case 0x03:
				TDS = buf.readChar();
				life1 = buf.readByte();
				life2 = buf.readByte();
				life3 = buf.readByte();
				life4 = buf.readByte();
				life5 = buf.readByte();
			break;
		}	
	}
	
	public int getFrameWork() {
		return frameWork;
	}

	public void setFrameWork(int frameWork) {
		this.frameWork = frameWork;
	}

	public int getFrameStatus() {
		return frameStatus;
	}

	public void setFrameStatus(int frameStatus) {
		this.frameStatus = frameStatus;
	}
	
	public int getTDS() {
		return TDS;
	}

	public void setTDS(int tDS) {
		TDS = tDS;
	}

	public int getLife1() {
		return life1;
	}

	public void setLife1(int life1) {
		this.life1 = life1;
	}

	public int getLife2() {
		return life2;
	}

	public void setLife2(int life2) {
		this.life2 = life2;
	}

	public int getLife3() {
		return life3;
	}

	public void setLife3(int life3) {
		this.life3 = life3;
	}

	public int getLife4() {
		return life4;
	}

	public void setLife4(int life4) {
		this.life4 = life4;
	}

	public int getLife5() {
		return life5;
	}

	public void setLife5(int life5) {
		this.life5 = life5;
	}
}
