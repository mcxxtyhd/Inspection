package com.inspect.action.system;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;

import com.inspect.action.common.BaseAction;
import com.inspect.annotation.LogAnnotation;
import com.inspect.service.SystemServiceI;
import com.inspect.util.common.ExceptionUtil;
import com.inspect.util.common.StringUtils;
import com.inspect.vo.comon.Json;
import com.inspect.vo.system.EnterpriseVo;
import com.opensymphony.xwork2.ModelDriven;

@Namespace("/system")
@Action(value="enterpriseAction",results={
		@Result(name="enterpriseList",location="/webpage/system/enterprise/enterpriseList.jsp"),
		@Result(name="enterpriseAdd",location="/webpage/system/enterprise/enterpriseAdd.jsp"),
		@Result(name="enterpriseEdit",location="/webpage/system/enterprise/enterpriseEdit.jsp")})
public class EnterpriseAction extends BaseAction implements ModelDriven<EnterpriseVo> {

	private static final long serialVersionUID = -4594038427642734716L;
	
	private static final Logger logger = Logger.getLogger(EnterpriseAction.class);
	
	@Resource(name="systemService")
	private SystemServiceI systemService;
	
	private EnterpriseVo enterprisevo;
	
	public SystemServiceI getSystemService() {
		return systemService;
	}

	public void setSystemService(SystemServiceI systemService) {
		this.systemService = systemService;
	}

	@Override
	public EnterpriseVo getModel() {
		if(enterprisevo==null){
			enterprisevo=new EnterpriseVo();
		}
		return enterprisevo;
	}
	
	public String entpriseList(){
		return "enterpriseList";
	}
	
	public String entpriseAdd(){
		return "enterpriseAdd";
	}
	
	public String entpriseEdit(){
		return "enterpriseEdit";
	}
	
	public void entpriseDatagrid(){
		int page =Integer.parseInt(ServletActionContext.getRequest().getParameter("page") );
		int rows =Integer.parseInt(ServletActionContext.getRequest().getParameter("rows") );
		Map<String, Object> map = systemService.findEnterpriseDatagrid(enterprisevo,page,rows,this.queryEnterpriseByWhere());
		writeJson(map);
	}
	
	@LogAnnotation(event="添加单位信息",tablename="t_s_enterprise")
	public void addEnterprise(){
		Json j=new Json();
		try{
			systemService.addEnterprise(enterprisevo);
			j.setMsg("添加成功！");
			j.setSuccess(true);
		}catch(Exception e){
			setOperstatus(1);
			j.setMsg("添加失败！");
			logger.error(ExceptionUtil.getExceptionMessage(e));
		}
		writeJson(j);
	}
	
	@LogAnnotation(event="修改单位信息",tablename="t_s_enterprise")
	public void editEnterprise(){
		Json j=new Json();
		try{
			systemService.editEnterprise(enterprisevo);
			j.setMsg("修改成功！");
			j.setSuccess(true);
		}catch(Exception e){
			setOperstatus(1);
			j.setMsg("修改失败！");
			logger.error(ExceptionUtil.getExceptionMessage(e));
		}
		writeJson(j);
	}
	
	@LogAnnotation(event="删除单位信息",tablename="t_s_enterprise")
	public void deleteEnterprise(){
		Json j=new Json();
		try{
			if(!StringUtils.isEmpty(enterprisevo.getIds())){
				systemService.removeEnterprise(enterprisevo.getIds());
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
}
