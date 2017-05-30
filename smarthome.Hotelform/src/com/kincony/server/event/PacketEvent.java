package com.kincony.server.event;

import java.util.EventObject;

/**
 * Packet事件�?
 * 
 * @author JOEY
 *
 */
public class PacketEvent extends EventObject{

	private static final long serialVersionUID = -8746662947115974374L;
	public int type;

	public PacketEvent(Object source) {
		super(source);
	}

}
