package com.kincony.server.packets.in.report;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.jboss.netty.buffer.ChannelBuffer;

import com.kincony.server.packets.PacketParseException;
import com.kincony.server.packets.in.ReportPacket;

/**
 * 运行状态（1字节）：0-正常，1-异常，
  温度：1字节
  湿度：1字节

 * @author joey
 *
 */
public class Report0150Packet extends ReportPacket{
	private int frameStatus;
	private int temperature;//温度
	private int humidity; //湿度
	private int electric; //电量
	
	private String imgPath;
	
	public Report0150Packet(ChannelBuffer buf) throws PacketParseException {
		super(buf);
	}

	@Override
	protected void parseFrame(ChannelBuffer buf, int frameLen) throws PacketParseException {
		switch(frameType){
			case 0x08:
				frameStatus = buf.readByte();
				temperature = buf.readByte();
				humidity = buf.readByte();
				electric = buf.readByte();
			break;
			case 0x03:
				byte[] imgByte = new byte[frameLen];
				buf.readBytes(imgByte);
				
				String root = System.getProperty("webapp.root");
				
				imgPath = "\\pic\\" + System.currentTimeMillis() + ".jpg";
				
				File imgFile = new File(root + imgPath);
				
				try {
					FileUtils.writeByteArrayToFile(imgFile, imgByte);
				} catch (IOException e) {
					e.printStackTrace();
				}
			break;
		}
	}

	public int getFrameStatus() {
		return frameStatus;
	}

	public void setFrameStatus(int frameStatus) {
		this.frameStatus = frameStatus;
	}

	public int getTemperature() {
		return temperature;
	}

	public void setTemperature(int temperature) {
		this.temperature = temperature;
	}

	public int getHumidity() {
		return humidity;
	}

	public void setHumidity(int humidity) {
		this.humidity = humidity;
	}

	public String getImgPath() {
		return imgPath;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}

	public int getElectric() {
		return electric;
	}

	public void setElectric(int electric) {
		this.electric = electric;
	}
}
