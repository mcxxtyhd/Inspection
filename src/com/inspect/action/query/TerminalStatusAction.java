package com.inspect.action.query;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;

import com.inspect.action.common.BaseAction;
import com.inspect.service.InspectQueryServiceI;
import com.inspect.service.InspectUserServiceI;
import com.inspect.vo.monitor.TerminaStatusReportVo;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 终端状态查询业务流程控制Action
 * @version 1.0
 */
@Namespace("/query")
@Action(value="terminalstatusAction",results={
		@Result(name="terstatusList",location="/webpage/query/terstatusList.jsp")})
public class TerminalStatusAction extends BaseAction implements ModelDriven<TerminaStatusReportVo> {

	private static final long serialVersionUID = -2804063737837336267L;
	
	@Resource
	private InspectQueryServiceI inspectQueryService;
	
	@Resource
	private InspectUserServiceI inspectUserService;
	
	private TerminaStatusReportVo terminastatusVo=new TerminaStatusReportVo();
	
	public InspectQueryServiceI getInspectQueryService() {
		return inspectQueryService;
	}

	public InspectUserServiceI getInspectUserService() {
		return inspectUserService;
	}

	@Override
	public TerminaStatusReportVo getModel() {
		return terminastatusVo;
	}
	
	public String terstatusList(){
		//加载巡检员列表
		getRequest().setAttribute("GroupList",inspectUserService.getGroupList(queryEnterpriseByWhere()));
		getRequest().setAttribute("InspectUserList",inspectUserService.getInspectUserList(queryEnterpriseByWhere()));
		return "terstatusList";
	}
	
	public void terstatusDatagrid(){
		int page =Integer.parseInt(ServletActionContext.getRequest().getParameter("page") );
		int rows =Integer.parseInt(ServletActionContext.getRequest().getParameter("rows") );
		Map<String, Object> map = inspectQueryService.findStatusDatagrid(terminastatusVo,page,rows,querySql());
		writeJson(map);
	}
	

}
