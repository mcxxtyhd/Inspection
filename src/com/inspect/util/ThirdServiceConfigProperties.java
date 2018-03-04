package com.inspect.util;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 加载第三方转发终端信息 配置属性文件
 * 
 */
public class ThirdServiceConfigProperties {
	private static Log logger = LogFactory.getLog(ThirdServiceConfigProperties.class);

	private static ThirdServiceConfigProperties instance;

	private Properties properties;
	public static final String Special_Appid="special_appId";
	public static final String Send_AppId="send_appId";
	public static final String rest_url_16="rest_url_16";
	public static final String analytical_type_16="analytical_type_16";
	public static final String appDoorBusinessWs="appDoorBusinessWs"; /*事件规则接收端业务处理WS的API服务的URL */

	private ThirdServiceConfigProperties() {
		super();
		init();
	}

	public static synchronized ThirdServiceConfigProperties getInstance() {
		if (instance == null) {
			instance = new ThirdServiceConfigProperties();
		}

		return instance;

	}
	
	public boolean isContains(String key,String value) {
		boolean result = false;
		String valueStr = getExcepitonMsgByKey(key);

		if (StringUtils.isEmpty(valueStr))
			return result;

		/* 在thirdServiceConfig.properties 属性文件中 send_appId  和 special_appId 的值以 ### 分隔 */
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
		Reader reader = new InputStreamReader(ThirdServiceConfigProperties.class.getResourceAsStream("/thirdServiceConfig.properties"));
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
