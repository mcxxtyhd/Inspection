
package com.inspect;

import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.ApplicationContext;

 

/**
 * note: 建议不要使用SpringLocator进行bean获取，尽量使用注入方式
 * SpringLocator.java
 * 2009-5-8
 */
public abstract class SpringLocator {

	private static ApplicationContext applicationContext;
	
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name) {
		return (T) applicationContext.getBean(name);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getBean(Class<T> clazz) {
		return (T) BeanFactoryUtils.beanOfType(applicationContext, clazz);
	}

	public static void setApplicationContext(ApplicationContext applicationContext) {
		SpringLocator.applicationContext = applicationContext;
	}

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}
	
	
}
