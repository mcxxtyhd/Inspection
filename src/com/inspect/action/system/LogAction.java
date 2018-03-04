package com.inspect.action.system;

import java.util.Map;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import com.inspect.action.common.BaseAction;
import com.inspect.service.BaseServiceI;
import com.inspect.service.SystemServiceI;
import com.inspect.vo.system.PageLog;
import com.opensymphony.xwork2.ModelDriven;

@Namespace("/system")
@Action(value="logAction",results={
		@Result(name="logList",location="/webpage/system/log/logList.jsp")})
public class LogAction extends BaseAction implements ModelDriven<PageLog> {

	private static final long serialVersionUID = -2616514454080655203L;
	
	private BaseServiceI baseService;
	
	private SystemServiceI systemService;
	
	private PageLog pageLog=new PageLog();

	public BaseServiceI getBaseService() {
		return baseService;
	}
   @Autowired
	public void setBaseService(BaseServiceI baseService) {
		this.baseService = baseService;
	}
   
   public SystemServiceI getSystemService() {
		return systemService;
	}
   
   @Autowired
	public void setSystemService(SystemServiceI systemService) {
		this.systemService = systemService;
	}
 
	public PageLog getModel() {
		return pageLog;
	}
	
	public String logList(){
		return "logList";
	}
	
	public void logDatagrid(){
		int page =Integer.parseInt(ServletActionContext.getRequest().getParameter("page") );
		int rows =Integer.parseInt(ServletActionContext.getRequest().getParameter("rows") );
		Map<String, Object> map = baseService.findLogDatagrid(pageLog,page,rows,this.querySql());
		writeJson(map);
	}
}
