package com.kincony.server.packets.in.report;

import org.jboss.netty.buffer.ChannelBuffer;

import com.kincony.server.packets.PacketParseException;
import com.kincony.server.packets.in.ReportPacket;

/**
 * 帧类型：0x08
b、设备类型：0x4002
c、设备地址：2字节0
d、长度：2
e、帧内容：工作模式（1字节）：0-正常工作，1-异常工作，2-关机
		   运行状态（1字节）：0-正常，1-漏水，2缺水，3-滤芯寿命到

a、帧类型：0x03
b、设备类型：0x4002
c、设备地址：2字节0
d、长度：7
e、帧内容：进水TDS（2字节），出水TDS（2字节），1级滤芯寿命（1字节0-100%），2级滤芯寿命（1字节0-100%），3级滤芯寿命（1字节0-100%），4级滤芯寿命（1字节0-100%），5级滤芯寿命（1字节0-100%），
累计水量（2字节65536L水），

 * @author joey
 *
 */
public class Report0240Packet extends ReportPacket{

	private int frameWork;
	private int frameStatus;
	
	private int inTDS;
	private int outTDS;
	private int life1,life2,life3,life4,life5;
	private int totalWater;
	
	public Report0240Packet(ChannelBuffer buf) throws PacketParseException {
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
				inTDS = buf.readChar();
				outTDS = buf.readChar();
				life1 = buf.readByte();
				life2 = buf.readByte();
				life3 = buf.readByte();
				life4 = buf.readByte();
				life5 = buf.readByte();
				totalWater = buf.readChar();
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
	
	public int getInTDS() {
		return inTDS;
	}

	public void setInTDS(int inTDS) {
		this.inTDS = inTDS;
	}

	public int getOutTDS() {
		return outTDS;
	}

	public void setOutTDS(int outTDS) {
		this.outTDS = outTDS;
	}

	public int getTotalWater() {
		return totalWater;
	}

	public void setTotalWater(int totalWater) {
		this.totalWater = totalWater;
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
