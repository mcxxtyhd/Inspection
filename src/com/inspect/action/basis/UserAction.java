package com.inspect.action.basis;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;

import com.inspect.action.common.BaseAction;
import com.inspect.annotation.LogAnnotation;
import com.inspect.dao.BaseDaoI;
import com.inspect.model.basis.TGroup;
import com.inspect.model.basis.TInspectUser;
import com.inspect.model.basis.TProjectGroup;
import com.inspect.service.InspectUserServiceI;
import com.inspect.service.PlanServiceI;
import com.inspect.service.PlanTaskServiceI;
import com.inspect.service.SystemServiceI;

import com.inspect.util.common.ExceptionUtil;
import com.inspect.util.common.StringUtils;

import com.inspect.vo.basis.InspectUserVo;
import com.inspect.vo.comon.Json;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 巡检员业务流程控制Action
 * @version 1.0
 */
@Namespace("/basis")
@Action(value="userAction",results={
		@Result(name="userList",location="/webpage/basis/userList.jsp"),
		@Result(name="userAdd",location="/webpage/basis/userAdd.jsp"),
		@Result(name="userEdit",location="/webpage/basis/userEdit.jsp")})
public class UserAction extends BaseAction implements ModelDriven<InspectUserVo> {
	
	private static final long serialVersionUID = -6793095455825164157L;

	private static final Logger logger = Logger.getLogger(UserAction.class);
	
	
	private InspectUserServiceI inspectUserService;
	
	private SystemServiceI systemService;
	
	private InspectUserVo inspectuserVo= new InspectUserVo();
	@Resource
	private PlanTaskServiceI planTaskService;
	@Resource
	private  PlanServiceI planService;
	public InspectUserServiceI getInspectUserService() {
		return inspectUserService;
	}
	@Resource
	private BaseDaoI baseDao;
	@Resource
	public void setInspectUserService(InspectUserServiceI inspectUserService) {
		this.inspectUserService = inspectUserService;
	}
	
	public SystemServiceI getSystemService() {
		return systemService;
	}

	@Resource
	public void setSystemService(SystemServiceI systemService) {
		this.systemService = systemService;
	}

	@Override
	public InspectUserVo getModel() {
		return inspectuserVo;
	}
	
	public String userList(){
		getRequest().setAttribute("GroupList",inspectUserService.getGroupList(queryEnterpriseByWhere()));
		getRequest().setAttribute("EnterpriseList",systemService.comboboxEnterprise(queryEnterpriseByWhere()));
		return "userList";
	}
	
	public String userAdd(){
		getRequest().setAttribute("GroupList",inspectUserService.getGroupList(queryEnterpriseByWhere()));
		return "userAdd";
	}
	
	public String userEdit(){
		getRequest().setAttribute("GroupList",inspectUserService.getGroupList(queryEnterpriseByWhere()));
		return "userEdit";
	}
	
	public void userDatagrid(){
		int page =Integer.parseInt(ServletActionContext.getRequest().getParameter("page") );
		int rows =Integer.parseInt(ServletActionContext.getRequest().getParameter("rows") );
		Map<String, Object> map = inspectUserService.findInspectUserDatagrid(inspectuserVo,page,rows,querySql1());
		writeJson(map);
	}
	
	@LogAnnotation(event="添加巡检员",tablename="t_inspect_user")
	public void addInspectUser(){
		Json j=new Json();
		try{
			long cnumber=inspectUserService.count(inspectuserVo.getIuname(),0,1);
			if(cnumber>0){
				 j.setMsg("名称已存在,不能重复添加!");
			}else{
				inspectuserVo.setEntid(super.getSessionUserName().getEntid());
				inspectUserService.addInspectUser(inspectuserVo);
				j.setMsg("添加成功！");
				j.setSuccess(true);
			}
		}catch(Exception e){
			setOperstatus(1);
			j.setMsg("添加失败！");
			logger.error(ExceptionUtil.getExceptionMessage(e));
		}
		writeJson(j);
	}
	
	@LogAnnotation(event="修改巡检员信息",tablename="t_inspect_user")
	public void editInspectUser(){
		Json j=new Json();
		try{
			/*long cnumber=inspectUserService.count(inspectuserVo.getIuname(),0,1);
			if(cnumber>0){
				 j.setMsg("名称已存在,不能重复添加!");
			}else{*/
				inspectUserService.editInspectUser(inspectuserVo);
				j.setMsg("修改成功！");
				j.setSuccess(true);
		//	}
			
		
		}catch(Exception e){
			setOperstatus(1);
			j.setMsg("修改失败！");
			logger.error(ExceptionUtil.getExceptionMessage(e));
		}
		writeJson(j);
	}
	
	@LogAnnotation(event="删除巡检员信息",tablename="t_inspect_user")
	public void deleteInspectUser(){
		Json j=new Json();
		try{
			if(!StringUtils.isEmpty(inspectuserVo.getIds())){
				inspectUserService.removeInspectUser(inspectuserVo.getIds());
				j.setSuccess(true);
				j.setMsg("删除成功！");
			}
		}catch(Exception e){
			setOperstatus(1);
			j.setMsg("删除失败！");
			logger.error(ExceptionUtil.getExceptionMessage(e));
		}
		writeJson(j);
	}
	
	
	@LogAnnotation(event="删除巡检员信息",tablename="t_inspect_user")
	public void deleteInspectUser1(){
		Json j=new Json();
		try{
			if(!StringUtils.isEmpty(inspectuserVo.getIds())){
				inspectUserService.removeInspectUser(inspectuserVo.getIds());
				//planTaskService.deleteTaskByUser(inspectuserVo.getIds());
			//	planService.deletePlanByUser(inspectuserVo.getIds());
				j.setSuccess(true);
				j.setMsg("删除成功！");
			}
		}catch(Exception e){
			setOperstatus(1);
			j.setMsg("删除失败！");
			logger.error(ExceptionUtil.getExceptionMessage(e));
		}
		writeJson(j);
	}
	
	public void groupList(){
		List<TGroup> but=inspectUserService.getGroupList(super.getSessionUserName().getEntid());
		writeJson(but);
	}
	
	public void userList1(){
		List<TInspectUser> list=inspectUserService.getInspectUsers();
		if(list!=null&&list.size()>0){
			for(TInspectUser user:list){
				user.setIrealname(user.getIuname());
				user.setIrealname(user.getIuname());
				systemService.save(user);
			}
	}
	}

	public void isadmin(){
		Json j=new Json();
		boolean flag=false;
		String ids=getRequest().getParameter("ids");
		flag=baseDao.isadmin(TInspectUser.class,ids,queryEnterpriseByWhere());
		j.setSuccess(flag);
		writeJson(j);
	}
}
