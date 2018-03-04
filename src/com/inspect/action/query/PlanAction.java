package com.inspect.action.query;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.BeanUtils;

import com.inspect.action.common.BaseAction;
import com.inspect.annotation.LogAnnotation;
import com.inspect.dao.BaseDaoI;
import com.inspect.model.basis.TGroup;
import com.inspect.model.basis.TLine;
import com.inspect.model.basis.TPlan;
import com.inspect.model.basis.TPlanTask;
import com.inspect.service.InspectItemServiceI;
import com.inspect.service.InspectUserServiceI;
import com.inspect.service.PlanServiceI;
import com.inspect.service.SystemServiceI;
import com.inspect.util.common.ExceptionUtil;
import com.inspect.util.common.StringUtils;
import com.inspect.vo.basis.CascodeVo;
import com.inspect.vo.basis.GroupVo;
import com.inspect.vo.basis.LineVo;
import com.inspect.vo.basis.PlanVo;
import com.inspect.vo.basis.TPlanTaskVo;
import com.inspect.vo.comon.Json;
import com.opensymphony.xwork2.ModelDriven;
/**
 * 设备巡检计划Action
 * @version 1.0
 */
@Namespace("/query")
@Action(value = "planAction", results = {
		@Result(name = "planList", location = "/webpage/query/planlist.jsp"),
		@Result(name = "planAdd", location = "/webpage/query/planAdd.jsp") ,
		@Result(name = "editPlan", location = "/webpage/query/planEdit.jsp"),
		@Result(name = "planAddLT", location = "/webpage/query/planAddLT.jsp"), 
		@Result(name = "planEditLT", location = "/webpage/query/planEditLT.jsp") })
public class PlanAction extends BaseAction implements ModelDriven<PlanVo> {

	private static final long serialVersionUID = -704018141269575000L;
	
	private static final Logger logger = Logger.getLogger(PlanAction.class);
	@Resource
	private BaseDaoI baseDao;
	
	@Resource
	private InspectUserServiceI inspectUserService; 	
	@Resource
	private InspectItemServiceI inspectItemService;
	@Resource
    private  PlanServiceI  planService;    	
	@Resource
	private SystemServiceI systemService;
	
	private PlanVo   planvo   = new PlanVo();
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

	public PlanServiceI getPlanService() {
		return planService;
	}

	public void setPlanService(PlanServiceI planService) {
		this.planService = planService;
	}
	
	@Override
	public PlanVo getModel() {
		return planvo;
	}

	public String queryList() {
		getRequest().setAttribute("EnterpriseList",systemService.comboboxEnterprise(queryEnterpriseByWhere()));
		getRequestValue();
		return "planList";
	}
	public String  planAdd(){
		getRequestValue();
		return "planAdd";
	}
	
	public String EditPlan(){
		getRequestValue();
		return "editPlan";
	}
	
	public String planAddLT(){
		getRequestValue();
		return "planAddLT";
	}
	
	public String planEditLT(){
		getRequestValue();
		return "planEditLT";
	}
	//查询巡检计划
	public void findPlanList(){
		System.out.println("findPlanList()");
		int page =Integer.parseInt(ServletActionContext.getRequest().getParameter("page") );
		int rows =Integer.parseInt(ServletActionContext.getRequest().getParameter("rows") );
		Map<String, Object> map  = planService.findPlanDatagrid(planvo, page, rows,querySql());
		writeJson(map);
	}
	
	//新增巡检计划信息
	@LogAnnotation(event="添加巡检计划",tablename="t_plan")
	public void AddPlan() {
		Json json = new Json();
		try {
			if (planService.isExist(planvo.getPname(),queryEnterpriseByWhere())) {
				json.setMsg(planvo.getPname() + "任务名称已存在!");
			} else {
				planvo.setEntid(getSessionUserName().getEntid());
				planService.addPlanLT(planvo);
				json.setSuccess(true);
				json.setMsg("添加成功!");
			}
		} catch (Exception e) {
			setOperstatus(1);
			json.setMsg("添加失败！");
			logger.error(ExceptionUtil.getExceptionMessage(e));
		}
		writeJson(json);
	}
	
	@LogAnnotation(event="修改巡检计划",tablename="t_plan")
	public void  PlanEdit(){
		Json j=new Json();
		try{
			planService.editPlanLT(planvo);
			j.setMsg("修改成功！");
			j.setSuccess(true);
		}catch(Exception e){
			setOperstatus(1);
			j.setMsg("修改失败！");
			logger.error(ExceptionUtil.getExceptionMessage(e));
		}
		writeJson(j);
	}
	@LogAnnotation(event="删除巡检计划",tablename="t_plan")
	public void deletePlan(){
		Json j=new Json();
		try{
			String ids=getRequest().getParameter("ids");
			if(!StringUtils.isEmpty(ids)){
				for(String id:ids.split(",")){	
					TPlan plan  = planService.QueryTplan(id);
					if(planService.taskcount(plan.getId())==0){
						if(plan.getPstatus()==0){
							planService.deletePlan(id);
							j.setSuccess(true);
							j.setMsg("删除成功！");
						}else{
							j.setMsg("不能删除已巡检的计划！");
						}
					}else{
						j.setMsg("不能删除已巡检的计划！");
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
	/**判断巡检员是否有计划
	 */
	 public void isUserHavePlans(){
		 String userId=getRequest().getParameter("id");
		 Json j=new Json();
		 int puid=Integer.parseInt(userId);
		 boolean flag=false;
		  flag=planService.isUserHavePlans(puid);
			//j.setMsg("此用户已有线路计划和任务！");
			j.setSuccess(flag);
			writeJson(j);
	 }
	 
	 private void getRequestValue(){
		getRequest().setAttribute("LineList",inspectItemService.getlineList(getSessionUserName().getEntid()));
		getRequest().setAttribute("GroupList",inspectUserService.getGroupList(getSessionUserName().getEntid()));
		getRequest().setAttribute("InspectUserList",inspectUserService.getInspectUserList(getSessionUserName().getEntid()));
	 }
	
		public void isadmin(){
			Json j=new Json();
			boolean flag=false;
			String ids=getRequest().getParameter("ids");
			flag=baseDao.isadmin(TPlan.class,ids,queryEnterpriseByWhere());
			j.setSuccess(flag);
			writeJson(j);
		}
		/*
		 * 通过单位级联查询到维护队和任务
		 * 
		 */
		public void cascodeToTaskAndGroup(){
			CascodeVo c=new CascodeVo();
			int entid1=getSessionUserName().getEntid();
			String  entid=(String)getRequest().getParameter("entid");
			List<GroupVo> glist=new ArrayList();
			List<TPlanTaskVo> tlist=new ArrayList();
			
			//总公司权限
			if(entid1==0){
				
				//添加维护队
				List<TGroup> gl=inspectUserService.getGroupList1(Integer.parseInt(entid));
					if(gl!=null&&gl.size()>0){
						for(TGroup g:gl){
							GroupVo gvo=new GroupVo();
							BeanUtils.copyProperties(g,gvo);
							glist.add(gvo);
						}
					}
				//添加线路
				List<TPlanTask> ll=inspectItemService.gettasklist(Integer.parseInt(entid));
				if(ll!=null&&ll.size()>0){
					for(TPlanTask t:ll){
						TPlanTaskVo tvo=new TPlanTaskVo();
						BeanUtils.copyProperties(t,tvo);
						tlist.add(tvo);
					}
				}
			//	System.out.println(glist.size());
				//System.out.println(tlist.size());
				c.setGlist(glist);
				c.setTlist(tlist);
				c.setFlag(0);
			}
			
			//	非总公司权限
			else{
				c.setFlag(1);
				
			}
			writeJson(c);
			
		}
		/*
		 * 通过单位级联查询到维护队和线路
		 * 
		 */
		public void cascodeToLineAndGroup(){
			CascodeVo c=new CascodeVo();
			int entid1=getSessionUserName().getEntid();
			String  entid=(String)getRequest().getParameter("entid");
			List<GroupVo> glist=new ArrayList();
			List<LineVo> llist=new ArrayList();
			
			//总公司权限
			if(entid1==0){
				
				//添加维护队
				List<TGroup> gl=inspectUserService.getGroupList1(Integer.parseInt(entid));
					if(gl!=null&&gl.size()>0){
						for(TGroup g:gl){
							GroupVo gvo=new GroupVo();
							BeanUtils.copyProperties(g,gvo);
							glist.add(gvo);
						}
					}
				//添加线路
				List<TLine> ll=inspectItemService.getlineList1(Integer.parseInt(entid));
				if(ll!=null&&ll.size()>0){
					for(TLine t:ll){
						LineVo lvo=new LineVo();
						BeanUtils.copyProperties(t,lvo);
						llist.add(lvo);
					}
				}
				//System.out.println(glist.size());
				//System.out.println(llist.size());
				c.setGlist(glist);
				c.setLlist(llist);
				c.setFlag(0);
			}
			
			//	非总公司权限
			else{
				c.setFlag(1);
				
			}
			writeJson(c);
			
		}
}
