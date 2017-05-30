package com.kincony.server.helper;

public class MessageIDGenerator {
	private static int messageId = 0;
	
	public static int getNext(){
		return messageId++;
	}
}
