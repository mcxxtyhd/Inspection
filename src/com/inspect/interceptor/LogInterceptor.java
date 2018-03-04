package com.inspect.interceptor;

import java.lang.reflect.Method;
import java.util.Date;

import org.apache.struts2.ServletActionContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.inspect.action.common.BaseAction;
import com.inspect.annotation.LogAnnotation;
import com.inspect.model.system.TLog;
import com.inspect.service.BaseServiceI;
import com.inspect.util.common.DateUtils;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

/**
 * 日志拦截器
 */
public class LogInterceptor extends AbstractInterceptor {

	private static final long serialVersionUID = 6494740054390047981L;

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		String methodName = invocation.getProxy().getMethod();
		Method method = invocation.getAction().getClass().getMethod(methodName);
		TLog log=null;
		if(method != null && method.isAnnotationPresent(LogAnnotation.class)) {
			log=new TLog();
			// 获取当前执行的Action上的注解
			LogAnnotation logannotation = method.getAnnotation(LogAnnotation.class);
			String event = logannotation.event();//操作事件
			String tablename=logannotation.tablename();//操作表名
			log.setOpevent(event);  //操作事件
			log.setOptable(tablename);//操作表名
			log.setOptime(new Date());//操作时间
		}
		String reString = "";
		try {
			reString = invocation.invoke();
		} catch (Exception e) {
			log.setOpstatus(1);
		}
		if(log !=null){
			BaseAction baseAction =(BaseAction) invocation.getAction();
			log.setOpstatus(baseAction.getOperstatus());//0：成功 1：失败
			String username="";
			try {
				username=baseAction.getSessionUserName().getUsername();
				log.setOpuser(username==null?"":username);	
				log.setEntid(baseAction.getSessionUserName().getEntid());
			} catch (Exception e) {
				// TODO: handle exception
				log.setOpuser(baseAction.getOperater());
				log.setEntid(baseAction.getEntid());
			}
	
			BaseServiceI service = (BaseServiceI) WebApplicationContextUtils.getWebApplicationContext(ServletActionContext.getServletContext()).getBean("baseService");
			service.addLog(log);
		}
		return reString;
	}

}
