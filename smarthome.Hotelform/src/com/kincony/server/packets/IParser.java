package com.kincony.server.packets;

import org.jboss.netty.buffer.ChannelBuffer;

/**
 * 包解析器
 * 
 * @author JOEY
 */
public interface IParser {
	/**
	 * 判断此parser是否可以处理这个包
	 * @return
	 */
	public boolean accept(ChannelBuffer buf);
	
	/**
	 * 解析出一个输入包对象
	 * @return
	 * @throws PacketParseException
	 */
	public InPacket parseIncoming(ChannelBuffer buf) throws PacketParseException;
	
	/**
	 * 解析出一个输出包对象
	 * @return
	 * @throws PacketParseException
	 */
	public OutPacket parseOutcoming(ChannelBuffer buf) throws PacketParseException;
	
	/**
	 * 解析出一个输出包对象
	 * @return
	 * @throws PacketParseException
	 */
	public OutPacket parseOutcoming(ChannelBuffer buf, int offset, int length) throws PacketParseException;
	
	/**
	 * 解析出一个输入包对象
	 * @return
	 * @throws PacketParseException
	 */
	public InPacket parseIncoming(ChannelBuffer buf, int offset, int length) throws PacketParseException;
	
	/**
	 * 判断buf中的包是否是输入包
	 * 
	 * @return
	 */
	public boolean isIncoming(ChannelBuffer buf);
	
	/**
	 * 检查包是否重复
	 * @return
	 */
	public boolean isDuplicated(ChannelBuffer buf);
	
	/**
	 * 
	 * @return
	 * 		true表示即使这个包是重复包也要回复
	 */
	public boolean isDuplicatedNeedReply();
	
}
