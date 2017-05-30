package com.kincony.controller.app.appuser;

import java.util.ArrayList;
import java.util.List;

public class Test {
	public static void main(String[] args) {
		String num = "ZIGBEE_LOCK-NUM-1,12176,2,0,0,0,0,0,0,0,0,0,64,0,4,0";
		String[] split = num.split(",");
		StringBuffer ssd = new StringBuffer();
		
		for (int i = 3; i < split.length; i++) {
			String s =Integer.toBinaryString(Integer.valueOf(split[i].trim().toString()));
			String sasd = "";
			if(s.length()==1){
				sasd= "0000000"+s;
			}else if(s.length()==2){
				sasd= "000000"+s;
			}else if(s.length()==3){
				sasd= "00000"+s;
			}else if(s.length()==4){
				sasd= s+"0000";
			}else if(s.length()==5){
				sasd= "000"+s;
			}else if(s.length()==6){
				sasd= "00"+s;
			}else if(s.length()==7){
				sasd= "0"+s;
			}else if(s.length()==8){
				sasd= s;
			}
			ssd.append(sasd);
		}
		/*System.err.println(ssd.toString());*/
		
		List<String> ar = new ArrayList<String>();
		int sssd = 0;
		if(ssd.toString().length()-1<=0){
			sssd = 1;
		}else{
			sssd = ssd.toString().length()-1;
		}
		for (int i = 0; i < ssd.toString().length(); i++) {
			String substring = ssd.substring(ssd.toString().length(), sssd);
			ar.add(substring);
			
		}
		for (int i = 0; i < ar.size(); i++) {
			System.err.println(ar.get(i));
		}
	}
}
