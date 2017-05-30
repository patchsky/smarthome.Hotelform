package com.kincony.server;

public class Testysa {
	public static void main(String[] args) {
		String das = "ZIGBEE_SCAN-DEVEICE-4,32836,0;1,33021,2;2,56194,0;1,61978,3;1,61967,3;8192,65535,0";
		String[] split = das.split("-");
		for (int i = 0; i < split.length; i++) {
			System.err.println("截取 "+split[i]);
		}
	}
}
