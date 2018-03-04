package com.inspect.util.beanutil;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
  
/**  
 *  
 * @author XZB  
 */  
public class MyBeanUtils extends BeanUtils {   
	/**
	 * 注册时间转换类型
	 */
    static {   
        ConvertUtils.register(new DateConvert(), java.util.Date.class);   
        ConvertUtils.register(new DateConvert(), java.sql.Date.class);   
    } 
    /**
     * 多属性拷贝
     * @param bean
     * @param name
     * @param value
     */
    public static void copyProperty(Object bean,String name,Object value){
    	try {
			BeanUtils.copyProperty(bean, name, value);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
    }
    /**
     * 单属性拷贝
     * @param dest
     * @param orig
     */
    public static void copyProperties(Object dest, Object orig) {   
        try {   
            BeanUtils.copyProperties(dest, orig);   
        } catch (IllegalAccessException ex) {   
            ex.printStackTrace();   
        } catch (InvocationTargetException ex) {   
            ex.printStackTrace();   
        }   
    } 

}  
