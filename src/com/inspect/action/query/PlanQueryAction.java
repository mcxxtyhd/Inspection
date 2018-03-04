package com.inspect.action.query;


import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;

import com.inspect.action.common.BaseAction;
import com.inspect.service.InspectItemServiceI;
import com.inspect.service.InspectQueryServiceI;
import com.inspect.service.InspectUserServiceI;
import com.inspect.service.SystemServiceI;
import com.inspect.vo.basis.PlanQueryVo;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 巡检数据统计查询业务流程控制Action
 * @version 1.0
 */
@Namespace("/query")
@Action(value="planQueryAction",results={
		@Result(name="linestatusList",location="/webpage/query/linestatusList.jsp")})
public class PlanQueryAction extends BaseAction implements ModelDriven<PlanQueryVo> {

	private static final long serialVersionUID = -2804063737837336268L;
	
	PlanQueryVo planQueryvo=new PlanQueryVo();
	
	@Resource
	private InspectItemServiceI inspectItemService;
	@Resource
	private InspectUserServiceI inspectUserService;
	@Resource
	private SystemServiceI systemService;
	@Resource
	private InspectQueryServiceI inspectQueryService;

	@Override
	public PlanQueryVo getModel() {
		return planQueryvo;
	}

	public InspectItemServiceI getInspectItemService() {
		return inspectItemService;
	}
	public void setInspectItemService(InspectItemServiceI inspectItemService) {
		this.inspectItemService = inspectItemService;
	}
	public InspectUserServiceI getInspectUserService() {
		return inspectUserService;
	}
	public void setInspectUserService(InspectUserServiceI inspectUserService) {
		this.inspectUserService = inspectUserService;
	}

	/**
	 * 加载线路状态查询页面
	 */
	public String linestatusList(){
		getRequest().setAttribute("EnterpriseList",systemService.comboboxEnterprise(queryEnterpriseByWhere()));
		getRequest().setAttribute("TaskList",inspectItemService.getPlanTaskList(queryEnterpriseByWhere()));
		getRequest().setAttribute("GroupList",inspectUserService.getGroupList(queryEnterpriseByWhere()));
		//getRequest().setAttribute("InspectUserList",inspectUserService.getInspectUserList(queryEnterpriseByWhere()));
		//getRequest().setAttribute("EquipmentList",inspectItemService.getEquipmentList(getSessionUserName().getEntid(),"","a"));
		return "linestatusList";
	}
	/**
	 * 根据条件查询线路状态
	 */
	public void terstatusDatagrid(){
		int page =Integer.parseInt(ServletActionContext.getRequest().getParameter("page") );
		int rows =Integer.parseInt(ServletActionContext.getRequest().getParameter("rows") );
		Map<String, Object> map = inspectItemService.findPlanQueryVoDatagrid(planQueryvo, page, rows,querySql());
		writeJson(map);
	}
	
	/**
	 * 巡检数据统计
	 */
	public void tasksummaryDatagrid(){
		int page =Integer.parseInt(ServletActionContext.getRequest().getParameter("page") );
		int rows =Integer.parseInt(ServletActionContext.getRequest().getParameter("rows") );
		Map<String, Object> map = inspectQueryService.findInspectSummaryInfoDatagrid(planQueryvo, page, rows,querySql());
		writeJson(map);
	}
	/**
	 * 查询不同时间范围内的巡检数据
	 */
	public void summarySituaDatagrid(){
		int page =Integer.parseInt(ServletActionContext.getRequest().getParameter("page") );
		int rows =Integer.parseInt(ServletActionContext.getRequest().getParameter("rows") );
		Map<String, Object> map = inspectQueryService.findInspectSummaryInfoDatagridByDate(planQueryvo, page, rows," 1=1 ");
		writeJson(map);
	}
	/**
	 * 首页统计一季度巡检完成情况
	 */
	public void tasksummaryDatagrid1(){
		int page =Integer.parseInt(ServletActionContext.getRequest().getParameter("page") );
		int rows =Integer.parseInt(ServletActionContext.getRequest().getParameter("rows") );
		Map<String, Object> map = inspectQueryService.findInspectSummaryInfoDatagrid1(planQueryvo, page, rows," 1=1 ");
		writeJson(map);
	}

}
