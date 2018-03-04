package com.inspect.action.query;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;

import com.inspect.action.common.BaseAction;
import com.inspect.annotation.LogAnnotation;
import com.inspect.model.basis.TPlanTask;
import com.inspect.service.InspectItemServiceI;
import com.inspect.service.InspectUserServiceI;
import com.inspect.service.PlanTaskServiceI;
import com.inspect.service.SystemServiceI;
import com.inspect.util.common.ExceptionUtil;
import com.inspect.util.common.StringUtils;
import com.inspect.vo.basis.PlanTaskVo;
import com.inspect.vo.comon.Json;
import com.opensymphony.xwork2.ModelDriven;
/**
 * 任务管理Action
 * 
 * @version 1.0
 */
@Namespace("/query")
@Action(value = "plantaskAction", results = {
		@Result(name = "plantaskList", location = "/webpage/query/tasklist.jsp"),
		@Result(name = "editasktPlan", location = "/webpage/query/taskedit.jsp") })
public class PlanTaskAction extends BaseAction implements ModelDriven<PlanTaskVo> {
	private static final long serialVersionUID = 4756849234142057706L;
	private static final Logger logger = Logger.getLogger(PlanAction.class);
	@Resource
	private InspectUserServiceI inspectUserService; 	
	@Resource
	private InspectItemServiceI inspectItemService;
	@Resource
	private PlanTaskServiceI  plantaskService;
	
	@Resource
	private SystemServiceI systemService;
	
	private PlanTaskVo  plantask  =  new  PlanTaskVo();
	@Override
	public PlanTaskVo getModel() {
		return plantask;
	}
	public InspectUserServiceI getInspectUserService() {
		return inspectUserService;
	}
	public void setInspectUserService(InspectUserServiceI inspectUserService) {
		this.inspectUserService = inspectUserService;
	}
	public InspectItemServiceI getInspectItemService() {
		return inspectItemService;
	}
	public void setInspectItemService(InspectItemServiceI inspectItemService) {
		this.inspectItemService = inspectItemService;
	}
	public PlanTaskServiceI getPlantaskService() {
		return plantaskService;
	}
	public void setPlantaskService(PlanTaskServiceI plantaskService) {
		this.plantaskService = plantaskService;
	}
   public String PlanTaskList(){
	   getRequest().setAttribute("EnterpriseList",systemService.comboboxEnterprise(queryEnterpriseByWhere()));
		getRequestValue();
	   return "plantaskList";
   }
 //查询巡检任务
	public void findPlanTaskList(){
		
		System.out.println("findPlanTaskList()");
		
		int page =Integer.parseInt(ServletActionContext.getRequest().getParameter("page") );
		int rows =Integer.parseInt(ServletActionContext.getRequest().getParameter("rows") );
		Map<String, Object> map  = plantaskService.findPlanTaskDatagrid1(plantask, page, rows,querySql());
		writeJson(map);
	}
  public String EditPlanTask(){
	   getRequestValue();
	   return "editasktPlan";
  }
  //修改巡检任务
	@LogAnnotation(event="修改巡检任务",tablename="t_plantask")
	public void  PlanTaskEdit(){
		Json j=new Json();
		try{
			plantaskService.updateTask(plantask);
			j.setMsg("修改成功！");
			j.setSuccess(true);
		}catch(Exception e){
			setOperstatus(1);
			j.setMsg("修改失败！");
			logger.error(ExceptionUtil.getExceptionMessage(e));
		}
		writeJson(j);
	}
	@LogAnnotation(event="删除巡检任务",tablename="t_plantask")
	public void deletePlanTask(){
		Json j=new Json();
		try{
			String ids=getRequest().getParameter("ids");
			if(!StringUtils.isEmpty(ids)){
				 if(!StringUtils.isEmpty(ids)){
						for(String id:ids.split(",")){
							 TPlanTask task = plantaskService.QueryPlantask(id);
							 if(task.getPstatus()== 0 ){
								 plantaskService.deleteTask(id);
								j.setSuccess(true);
								j.setMsg("删除成功！");
							 }else{
								 setOperstatus(1);
								 j.setSuccess(false);
								 j.setMsg("不能删除已巡检的任务！");
							 }
						} 
					 }
				
			}
		}catch(Exception e){
			setOperstatus(1);
			j.setMsg("删除失败！");
			logger.error(ExceptionUtil.getExceptionMessage(e));
		}
		writeJson(j);
	}

	private void getRequestValue() {
		getRequest().setAttribute("LineList", inspectItemService.getlineList(getSessionUserName().getEntid()));
		getRequest().setAttribute("GroupList", inspectUserService.getGroupList(getSessionUserName().getEntid()));
		getRequest().setAttribute("InspectUserList", inspectUserService.getInspectUserList(getSessionUserName().getEntid()));
	}
}
