package com.inspect.interceptor;

import javax.servlet.http.HttpSession;
import org.apache.struts2.ServletActionContext;
import com.inspect.vo.comon.SessionInfo;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;

/**
 * 系统权限拦截器，拦截无效登录或SESSION失效
 * @author wzs
 * @version 1.0
 */
public class SessionInterceptor extends MethodFilterInterceptor{

	private static final long serialVersionUID = 8461944682286747036L;
	
	@Override
	protected String doIntercept(ActionInvocation actionInvocation) throws Exception {
		HttpSession session = ServletActionContext.getRequest().getSession();
	    SessionInfo sessioninfo = (SessionInfo)session.getAttribute("USER_SESSION");
	    if (sessioninfo == null) {
			ServletActionContext.getRequest().setAttribute("msg", "您还没有登录或登录已超时，请重新登录，然后再刷新本功能！");
			return "noSession";
			
		}
		return actionInvocation.invoke();
	}

}
