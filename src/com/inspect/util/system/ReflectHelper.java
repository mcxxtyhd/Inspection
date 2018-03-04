package com.inspect.util.system;

import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

public class ReflectHelper {
	private static final Logger logger = Logger.getLogger(ReflectHelper.class);
	 @SuppressWarnings("unchecked")
	  private Class cls;
	  private Object obj;
	  private Hashtable<String, Method> getMethods;
	  private Hashtable<String, Method> setMethods;

	  public ReflectHelper(Object o){
	    this.getMethods = null;
	    this.setMethods = null;
	    this.obj = o;
	    initMethods();
	  }

	  @SuppressWarnings("unchecked")
	public void initMethods(){
	    this.getMethods = new Hashtable();
	    this.setMethods = new Hashtable();
	    this.cls = this.obj.getClass();
	    Method[] methods = this.cls.getMethods();
	    String gs = "get(\\w+)";
	    Pattern getM = Pattern.compile(gs);
	    String ss = "set(\\w+)";
	    Pattern setM = Pattern.compile(ss);
	    String rapl = "$1";
	    for (int i = 0; i < methods.length; ++i){
	      String param;
	      Method m = methods[i];
	      String methodName = m.getName();
	      if (Pattern.matches(gs, methodName)){
	        param = getM.matcher(methodName).replaceAll(rapl).toLowerCase();
	        this.getMethods.put(param, m);
	      }else if (Pattern.matches(ss, methodName)){
	        param = setM.matcher(methodName).replaceAll(rapl).toLowerCase();
	        this.setMethods.put(param, m);
	      }
	    }
	  }

	  public boolean setMethodValue(String property, Object object){
	    Method m = (Method)this.setMethods.get(property.toLowerCase());
	    if (m != null);
	    try{
	      m.invoke(this.obj, new Object[]{ object });
	      return true;
	    }catch (Exception ex){
	      System.out.println("invoke getter on " + property + " error: " + ex.toString());
	      logger.error("invoke getter on " + property + " error: " + ex.toString());
	      return false;
	    }
	  }

	  public Object getMethodValue(String property){
	    Object value = null;
	    Method m = (Method)this.getMethods.get(property.toLowerCase());
	    if (m != null)
	      try{
	        value = m.invoke(this.obj, new Object[0]);
	      }catch (Exception ex){
	        System.out.println("invoke getter on " + property + " error: " + ex.toString());
	        logger.error("invoke getter on " + property + " error: " + ex.toString());
	      }
	    return value;
	  }
}
