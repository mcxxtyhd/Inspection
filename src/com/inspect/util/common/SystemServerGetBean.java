package com.inspect.util.common;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SystemServerGetBean {
	private static SystemServerGetBean systemServerGetBean ;
	private ApplicationContext context ;
	
	private SystemServerGetBean(){
		context = new  ClassPathXmlApplicationContext("resources/spring-hibernate.xml") ;
	}
	
	public synchronized static SystemServerGetBean  getInstance(){
		if( systemServerGetBean == null ){
			systemServerGetBean = new SystemServerGetBean() ;
		}
		return systemServerGetBean ;
	}
	
	/***
	 * 根据类的注解名称返回一个抽象的实例
	 * @param classNameString
	 * @return
	 */
	public Object returnClass( String classNameString ){
		return context.getBean( classNameString ) ;
	}
}
