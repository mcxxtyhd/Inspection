package com.inspect.action.basis;

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
import com.inspect.model.basis.TEnumParameter;
import com.inspect.model.basis.TGroup;
import com.inspect.service.InspectUserServiceI;
import com.inspect.service.SystemServiceI;
import com.inspect.util.common.ExceptionUtil;
import com.inspect.util.common.StringUtils;
import com.inspect.vo.basis.GroupVo;
import com.inspect.vo.comon.Json;
import com.opensymphony.xwork2.ModelDriven;
/**
 * 巡检班组业务流程控制Action
 * @version 1.0
 */
@Namespace("/basis")
@Action(value="groupAction",results={
		@Result(name="groupList",location="/webpage/basis/groupList.jsp"),
		@Result(name="groupAdd",location="/webpage/basis/groupAdd.jsp"),
		@Result(name="groupEdit",location="/webpage/basis/groupEdit.jsp")})
public class GroupAction extends BaseAction implements ModelDriven<GroupVo> {

	private static final long serialVersionUID = -6790313540205529721L;
	
	private static final Logger logger = Logger.getLogger(GroupAction.class);
	
	private InspectUserServiceI inspectUserService;
	
	private SystemServiceI systemService;
	
	private GroupVo groupVo=new GroupVo();
	@Resource
	private BaseDaoI baseDao;
	public InspectUserServiceI getInspectUserService() {
		return inspectUserService;
	}

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
	public GroupVo getModel() {
		return groupVo;
	}
	
	public String groupList(){
		getRequest().setAttribute("EnterpriseList",systemService.comboboxEnterprise(queryEnterpriseByWhere()));
		return "groupList";
	}
	
	public String groupAdd(){
		return "groupAdd";
	}
	
	public String groupEdit(){
		return "groupEdit";
	}
	
	public void groupDatagrid(){
		int page =Integer.parseInt(ServletActionContext.getRequest().getParameter("page") );
		int rows =Integer.parseInt(ServletActionContext.getRequest().getParameter("rows") );
		Map<String, Object> map = inspectUserService.findGroupterDatagrid(groupVo,page,rows,querySql());
		writeJson(map);
	}
	
	@LogAnnotation(event="添加维护队",tablename="t_group")
	public void addGroup(){
		Json j=new Json();
		try{
			long cnumber=inspectUserService.count(groupVo.getGname(),queryEnterpriseByWhere(),0);
			if(cnumber>0){
				 j.setMsg("维护队已存在,不能重复添加!");
			}else{
				groupVo.setEntid(getSessionUserName().getEntid());
				inspectUserService.addGroup(groupVo);
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
	
	@LogAnnotation(event="修改维护队",tablename="t_group")
	public void editGroup(){
		Json j=new Json();
		try{
			inspectUserService.editGroup(groupVo);
			j.setMsg("修改成功！");
			j.setSuccess(true);
		}catch(Exception e){
			setOperstatus(1);
			j.setMsg("修改失败！");
			logger.error(ExceptionUtil.getExceptionMessage(e));
		}
		writeJson(j);
	}
	
	@LogAnnotation(event="删除维护队",tablename="t_group")
	public void deleteGroup(){
		Json j=new Json();
		try{
			if(!StringUtils.isEmpty(groupVo.getIds())){
				inspectUserService.removeGroup(groupVo.getIds());
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
	
	public void isGroupHaveUsers(){
		Json j=new Json();
		boolean flag=false;
		String ids=getRequest().getParameter("ids");
		flag=baseDao.isadmin(TGroup.class,ids,queryEnterpriseByWhere());
		if(flag==false){
			flag=inspectUserService.isGroupHaveUsers(ids);
		}
		j.setSuccess(flag);
		writeJson(j);
	}
}
