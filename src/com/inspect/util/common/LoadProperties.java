package com.inspect.util.common;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 加载 配置属性文件
 * 
 */
public class LoadProperties {
	private static Log logger = LogFactory.getLog(LoadProperties.class);

	private static LoadProperties instance;

	private Properties properties;

	private LoadProperties() {
		super();
		init();
	}

	public static synchronized LoadProperties getInstance() {
		if (instance == null) {
			instance = new LoadProperties();
		}
		return instance;
	}
	
	public boolean isContains(String key,String value) {
		boolean result = false;
		String valueStr = getExcepitonMsgByKey(key);
		if (StringUtils.isEmpty(valueStr))
			return result;
		String[] valueArr = valueStr.split("###");
		for (String str : valueArr) {
			if (StringUtils.equals(str, value)) {
				result = true;
				break;
			}
		}
		return result;
	}

	public String getExcepitonMsgByKey(String actionName) {
		if (actionName == null || actionName.length() == 0)
			return "";
		return properties.getProperty(actionName);
	}

	private void init() {
		properties = new Properties();
		Reader reader = new InputStreamReader(LoadProperties.class.getResourceAsStream("/resources/config.properties"));
		try {
			properties.load(reader);
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

	public void reload() {
		init();
	}
}
