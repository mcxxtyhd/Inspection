package com.inspect.interceptor;

import org.apache.struts2.ServletActionContext;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

/**
 * 字符集编码拦截器
 */
public class EncodingInterceptor extends AbstractInterceptor {
	private static final long serialVersionUID = 5264666670609156416L;

	public String intercept(ActionInvocation actionInvocation) throws Exception {
		@SuppressWarnings("unused")
		ActionContext actionContext = actionInvocation.getInvocationContext();
		ServletActionContext.getResponse().setCharacterEncoding("utf-8");
		ServletActionContext.getRequest().setCharacterEncoding("utf-8");
		return actionInvocation.invoke();
	}

}
