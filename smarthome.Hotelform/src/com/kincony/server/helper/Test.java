package com.kincony.server.helper;

import com.kincony.server.util.Util;

public class Test {

	public static void main(String[] args) {
		byte[] b= new byte[]{0,0,0x000,0x0,0x0,0x0,0x0,0x0,0x0,0x0};
		System.out.println("d = "+Util.getString(b));
	}

}
