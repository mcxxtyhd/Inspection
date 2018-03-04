package com.inspect.util;

import java.io.BufferedReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;

public class ResBundleUtil {
	private static final String MESSAGE_RES_CONFIG_FILE = "MessageResources";
	private static final String APP_RES_CONFIG_FILE = "ApplicationResources";

	public static String getString(String key) {
		Locale l = Locale.getDefault();
		ResourceBundle rb = ResourceBundle.getBundle(MESSAGE_RES_CONFIG_FILE, l);
		return rb.getString(key);
	}
	
	public static String getAppString(String key) {
		ResourceBundle rb = ResourceBundle.getBundle(APP_RES_CONFIG_FILE);
		return rb.getString(key);
	}
	public HashMap<String, String> getSummaryMap(){

		ResourceBundle rb=ResourceBundle.getBundle("summary");
		//将文件的key取出  
		Enumeration<String> em=rb.getKeys();
		//存放属性文件的键值对 
		HashMap<String, String> hm=new HashMap<String, String>();
		//根据key取值  
		while(em.hasMoreElements()){ 
			String key=em.nextElement();  
			String value=rb.getString(key);  
			hm.put(key, value);
			}//属性文件的内容全部在map中了
		return hm;
		
	}
	
	public static String getSummaryList(int flag){
		ResourceBundle rb=null;
		if(flag==1){
			rb=ResourceBundle.getBundle("tietasummary");
		}
		else{
			 rb=ResourceBundle.getBundle("shineisummary");
		}
		//将文件的key取出  
		Enumeration<String> em=rb.getKeys();
		//存放属性文件的键值对 
		HashMap<String, String> hm=new HashMap<String, String>();
		StringBuffer buf=new StringBuffer();
		//根据key取值  
		while(em.hasMoreElements()){ 
			String key=em.nextElement();  
			String value=rb.getString(key);  
			buf.append(key).append(",").append(value).append(";");
			}//属性文件的内容全部在map中了
		String str=buf.toString().substring(0,buf.toString().length()-1);
		//System.out.println(str);
		return str;
		
	}
}
