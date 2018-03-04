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
import com.inspect.model.basis.TProjectGroup;
import com.inspect.service.InspectItemServiceI;
import com.inspect.service.SystemServiceI;
import com.inspect.util.common.ExceptionUtil;
import com.inspect.util.common.StringUtils;
import com.inspect.vo.basis.TProjectGroupVo;
import com.inspect.vo.comon.Json;
import com.opensymphony.xwork2.ModelDriven;
/**
 * 终端配置流程控制Action
 * @version 1.0
 */
@Namespace("/basis")
@Action(value="projectGroup",results={
		@Result(name="projectGroupList",location="/webpage/basis/projectGroupList.jsp"),
		@Result(name="projectGroupAdd",location="/webpage/basis/projectGroupAdd.jsp"),
		@Result(name="projectGroupEdit",location="/webpage/basis/projectGroupEdit.jsp")})
public class TProjectGroupAction extends BaseAction implements ModelDriven<TProjectGroupVo> {
	private static final long serialVersionUID = -9011086728759375544L;

	private static final Logger logger = Logger.getLogger(TProjectGroupAction.class);
	@Resource
	private InspectItemServiceI inspectItemService;
	@Resource
	private SystemServiceI systemService;
	
	private TProjectGroupVo projectGroupVo=new TProjectGroupVo();
	@Resource
	private BaseDaoI baseDao;
	
	@Override
	public TProjectGroupVo getModel() {
		return projectGroupVo;
	}
	
	public String projectGroupList(){
		getRequest().setAttribute("EnterpriseList",systemService.comboboxEnterprise(queryEnterpriseByWhere()));
		return "projectGroupList";
	}
	
	public String projectGroupAdd(){
		return "projectGroupAdd";
	}
	
	public String projectGroupEdit(){
		return "projectGroupEdit";
	}
	
	public void projectGroupDatagrid(){
		int page =Integer.parseInt(ServletActionContext.getRequest().getParameter("page") );
		int rows =Integer.parseInt(ServletActionContext.getRequest().getParameter("rows") );
		Map<String, Object> map = inspectItemService.findProjectGroupDatagrid(projectGroupVo, page, rows, querySql());
		writeJson(map);
	}
	
	@LogAnnotation(event="添加巡检项组",tablename="t_project_group")
	public void addProjectGroup(){
		Json j=new Json();
		try{
			long cnumber=inspectItemService.isExistProjectGroup(projectGroupVo.getPgname(),queryEnterpriseByWhere());
			if(cnumber>0){
				 j.setMsg("名称已存在,不能重复添加!");
			}else{
				projectGroupVo.setEntid(getSessionUserName().getEntid());
				inspectItemService.addProblemGroup(projectGroupVo);
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
	
	@LogAnnotation(event="修改巡检项组信息",tablename="t_project_group")
	public void editProjectGroup(){
		Json j=new Json();
		try{
			inspectItemService.editProjectGroup(projectGroupVo);
			j.setMsg("修改成功！");
			j.setSuccess(true);
		}catch(Exception e){
			setOperstatus(1);
			j.setMsg("修改失败！");
			logger.error(ExceptionUtil.getExceptionMessage(e));
		}
		writeJson(j);
	}
	@LogAnnotation(event="删除巡检项组",tablename="t_project_group")
	public void removeProjectGroup(){
		Json j=new Json();
		try{
			if(!StringUtils.isEmpty(projectGroupVo.getIds())){
				inspectItemService.removeProjectGroup(projectGroupVo.getIds());
				j.setSuccess(true);
				j.setMsg("删除成功！");
			}
		}	
		catch(Exception e){
			setOperstatus(1);
			j.setMsg("删除失败！");
			logger.error(ExceptionUtil.getExceptionMessage(e));
		}
		writeJson(j);
	}

	public void isadmin(){
		Json j=new Json();
		boolean flag=false;
		String ids=getRequest().getParameter("ids");
		flag=baseDao.isadmin(TProjectGroup.class,ids,queryEnterpriseByWhere());
		j.setSuccess(flag);
		writeJson(j);
	}

}
