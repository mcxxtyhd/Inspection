package com.inspect.util;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/***
 * 
 * This class is used for ...   
 * @author liux  
 * @version   
 *       1.0, 2013-4-18 上午11:05:49   
 * information : 
 * 		 properties配置文件的读写
 *
 */
public class ConfigReadOrWrite {
	private static Log logger = LogFactory.getLog(ConfigReadOrWrite.class);

	private static String FILe_PATH ;
	private static ConfigReadOrWrite configReadOrWrite ;
	private Properties properties;
	
	private ConfigReadOrWrite(){
		super();
		init();
	}
	
	public synchronized static ConfigReadOrWrite getInstance(){
		if( configReadOrWrite == null ){
			configReadOrWrite =new ConfigReadOrWrite() ;
		}
		return configReadOrWrite ;
	}

	/***
	 * 读取配置文件
	 * @author liux
	 */
	public String readProperties( String actionName ) {
		if (actionName == null || actionName.length() == 0)
			return "";
		return properties.getProperty(actionName);
	}
	
	private void init() {
		properties = new Properties();
		Reader reader = new InputStreamReader(ThirdServiceConfigProperties.class.getResourceAsStream("/resources/config.properties"));
		try {
			properties.load(reader);
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}



}
