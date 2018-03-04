package com.inspect.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 日志注解类
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface LogAnnotation {
	String  operator() default "";  //操作人
	String  event() default "";     //操作事件
	String  tablename() default "";	//操作表名
	String  classname() default ""; //操作ACTION
/*	String  entid() default ""; //操作ACTION
	String  name() default ""; //操作ACTION
*/}
