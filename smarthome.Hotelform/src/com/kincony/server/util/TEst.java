package com.kincony.server.util;

public class TEst {

	public static void main(String[] args) {
		char c = 0x0a;
		
		int i = 34;
		
		///System.out.println('0'+c);
		
		//byte b = Byte.parseByte('0'+c, 16);
		
		//System.out.println(b);
		
		System.out.println((int)c);
		/*char cc =toHexDigit(22);
		System.out.println(cc);
		
		System.out.println(fromHexDigit(c));*/
	}

	
	 public static byte getLow(int i) {
        return (byte) (i & 0xFF);
    }

    public static byte getHigh(int i) {
        i = i >> 8;
        return (byte) (i & 0xFF);
    }

}
