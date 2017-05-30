package com.kincony.server.packets;

import org.jboss.netty.buffer.ChannelBuffer;

import com.kincony.server.support.Protocol;

public final class PacketHelper {
	private IParser parser;
	
	private PacketHistory history;
	
	public PacketHelper(){
		history = new PacketHistory();
		parser = new PacketParser(history);
	}
	
	public InPacket processIn(ChannelBuffer buf) throws PacketParseException{
		// 解析包
        try {
        	if(!parser.accept(buf))
        		return null;
        	boolean duplicated = parser.isDuplicated(buf);
	        if(duplicated){
	        	 return null;
	        }
            InPacket ret = parser.parseIncoming(buf, 0, 0);
	        return ret;
        } catch (PacketParseException e) {
            throw e;
		} finally {
        }
	}
	
	public IParser getParser() {
		return parser;
	}
	
	/**
	 * @return Returns the history.
	 */
	public PacketHistory getHistory() {
		return history;
	}
}
