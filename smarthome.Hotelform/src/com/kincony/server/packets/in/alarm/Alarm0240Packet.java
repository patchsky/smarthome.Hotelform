package com.kincony.server.packets.in.alarm;

import org.jboss.netty.buffer.ChannelBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kincony.server.packets.InPacket;
import com.kincony.server.packets.PacketParseException;
import com.kincony.server.packets.in.AlarmPacket;
import com.kincony.server.util.Util;

/**
 * 设备报警包
 * 
 * a、帧类型：0x07
b、设备类型：0x4002
c、设备地址：2字节0
d、长度：1
e、帧内容：1-漏水报警、2-缺水报警、3-滤芯报警
报警：
帧头	帧类型	设备类型	设备地址	长度	帧内容	校检	帧尾
0x02	0x07	0x0240	0x0000	0x0100	1/2/3	1字节	0x03

 * @author joey
 *
 */
public class Alarm0240Packet extends AlarmPacket{
	private static Logger logger = LoggerFactory.getLogger(Alarm0240Packet.class);
	private int frameStatus;
	
	public Alarm0240Packet(ChannelBuffer buf) throws PacketParseException {
		super(buf);
	}
	
	@Override
	protected void parseFrame(ChannelBuffer buf, int frameLen)
			throws PacketParseException {
		frameStatus = buf.readByte();
	}

	public int getFrameStatus() {
		return frameStatus;
	}

	public void setFrameStatus(int frameStatus) {
		this.frameStatus = frameStatus;
	}
}
