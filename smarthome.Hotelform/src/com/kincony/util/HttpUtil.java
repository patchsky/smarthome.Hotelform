package com.kincony.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class HttpUtil {
	  public static String sendGet(String url, String charset, int timeout)
      {
        String result = "";
        try
        {
          URL u = new URL(url);
          try
          {
            URLConnection conn = u.openConnection();
            conn.connect();
            conn.setConnectTimeout(timeout);
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), charset));
            String line="";
            while ((line = in.readLine()) != null)
            {
              
              result = result + line;
            }
            in.close();
          } catch (IOException e) {
            return result;
          }
        }
        catch (MalformedURLException e)
        {
          return result;
        }
        
        return result;
      }
	  
	  
	  public static String sendPost(String requrl,String param){
	         URL url;
	          String sTotalString="";  
	        try {
	            url = new URL(requrl);
	             URLConnection connection = url.openConnection(); 
	               
	             connection.setRequestProperty("accept", "*/*");
	             connection.setRequestProperty("connection", "Keep-Alive");
	             connection.setRequestProperty("Content-Type", "text/xml");
	            // connection.setRequestProperty("Content-Length", body.getBytes().length+"");
	             connection.setRequestProperty("User-Agent",
	                     "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)");
	               
	               
	                connection.setDoOutput(true);  
	                OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "utf-8");  
	                out.write(param); // 向页面传递数据。post的关键所在！  
	                out.flush();  
	                out.close();  
	                // 一旦发送成功，用以下方法就可以得到服务器的回应：  
	                String sCurrentLine;  
	                
	                sCurrentLine = "";  
	                sTotalString = "";  
	                InputStream l_urlStream;  
	                l_urlStream = connection.getInputStream();  
	                // 传说中的三层包装阿！  
	                BufferedReader l_reader = new BufferedReader(new InputStreamReader(  
	                        l_urlStream));  
	                while ((sCurrentLine = l_reader.readLine()) != null) {  
	                    sTotalString += sCurrentLine + "\r\n";  
	            
	                }  
	                  
	        } catch (Exception e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }  
	             
	            System.out.println(sTotalString);  
	            return sTotalString;
	     }
}
