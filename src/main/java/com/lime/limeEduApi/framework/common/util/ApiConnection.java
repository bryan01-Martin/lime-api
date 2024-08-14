package com.lime.limeEduApi.framework.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

public class ApiConnection {
	
	
    public static String request(String apiPath, String methodType,Map<String, Object> property,Map<String, Object> params) {

    	
        HttpURLConnection conn ;
        OutputStreamWriter writer = null;
        BufferedReader reader = null;
        InputStreamReader isr = null;
        StringBuffer buffer = new StringBuffer();
        
        try {
        	final URL url = new URL(apiPath);
            conn = (HttpURLConnection) url.openConnection();
            // conn.setRequestMethod(methodType.toString());
            
            Iterator<String> propsItr = property.keySet().iterator();
            conn.setRequestMethod(methodType);
            while(propsItr.hasNext()) {
            	String key = propsItr.next();
            	conn.setRequestProperty(key,(String) property.get(key));
            }
            
            
            Iterator<String> paramItr = params.keySet().iterator();
            
            StringBuffer sb = new StringBuffer();
            
            while(paramItr.hasNext()) {
            	String key = paramItr.next();
            	sb.append(key).append("=").append((String)params.get(key)).append("&");
            }
            

            
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setAllowUserInteraction(true);
            
            writer = new OutputStreamWriter(conn.getOutputStream(),"UTF-8");
            writer.write(sb.toString());
            writer.flush();


            final int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                isr = new InputStreamReader(conn.getInputStream(),"UTF-8");
            }else {
                isr = new InputStreamReader(conn.getErrorStream(),"UTF-8");
            }
            
            reader = new BufferedReader(isr);
            
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            
            return buffer.toString();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) try { writer.close(); } catch (Exception ignore) { }
            if (reader != null) try { reader.close(); } catch (Exception ignore) { }
            if (isr != null) try { isr.close(); } catch (Exception ignore) { }
        }
		return buffer.toString();

    }		
	
	
}
