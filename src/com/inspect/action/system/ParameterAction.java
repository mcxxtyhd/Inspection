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
import com.inspect.dao.BaseDaoI;
import com.inspect.model.basis.TEnumParameter;
import com.inspect.model.system.Parameter;
import com.inspect.service.BaseServiceI;
import com.inspect.service.SystemServiceI;
import com.inspect.util.common.ExceptionUtil;
import com.inspect.util.common.StringUtils;
import com.inspect.vo.comon.Json;
import com.inspect.vo.system.ParameterVo;
import com.opensymphony.xwork2.ModelDriven;

@Namespace("/system")
@Action(value="parameterAction",results={
		@Result(name="parameterList",location="/webpage/system/parameter/parameterList.jsp"),
		@Result(name="parameterAdd",location="/webpage/system/parameter/parameterAdd.jsp"),
		@Result(name="parameterEdit",location="/webpage/system/parameter/parameterEdit.jsp")})
public class ParameterAction extends BaseAction implements ModelDriven<ParameterVo> {

	private static final long serialVersionUID = 140822744870300061L;
	
	private static final Logger logger = Logger.getLogger(ParameterAction.class);
	
	@Resource
	private BaseServiceI baseService;
	@Resource
	private BaseDaoI baseDao;
	@Resource
	private SystemServiceI systemService;
	
	private ParameterVo parameterVo=new ParameterVo();
	

	public BaseServiceI getBaseService() {
		return baseService;
	}

	public void setBaseService(BaseServiceI baseService) {
		this.baseService = baseService;
	}

	public SystemServiceI getSystemService() {
		return systemService;
	}

	public void setSystemService(SystemServiceI systemService) {
		this.systemService = systemService;
	}
	@Override
	public ParameterVo getModel() {
		return parameterVo;
	}
	
	public String parameterList(){
		getRequest().setAttribute("EnterpriseList",systemService.comboboxEnterprise(queryEnterpriseByWhere()));
		return "parameterList";
	}
	
	public String parameterAdd(){
		return "parameterAdd";
	}
	
	public String parameterEdit(){
		return "parameterEdit";
	}
	
	public void parameterDatagrid(){
		int page =Integer.parseInt(ServletActionContext.getRequest().getParameter("page") );
		int rows =Integer.parseInt(ServletActionContext.getRequest().getParameter("rows") );
		Map<String, Object> map = baseService.findParameterDatagrid(parameterVo,page,rows,querySql());
		writeJson(map);
	}
	
	@LogAnnotation(event="添加参数",tablename="t_parameter")
	public void addParameter(){
		Json j=new Json();
		try{
			parameterVo.setEntid(super.queryEnterpriseByWhere());
			baseService.addParameter(parameterVo);
			j.setMsg("添加成功！");
			j.setSuccess(true);
		}catch(Exception e){
			setOperstatus(1);
			j.setMsg("添加失败！");
			logger.error(ExceptionUtil.getExceptionMessage(e));
		}
		writeJson(j);
	}
	
	@LogAnnotation(event="修改参数",tablename="t_parameter")
	public void editParameter(){
		Json j=new Json();
		try{
			baseService.editParameter(parameterVo);
			j.setMsg("修改成功！");
			j.setSuccess(true);
		}catch(Exception e){
			setOperstatus(1);
			j.setMsg("修改失败！");
			logger.error(ExceptionUtil.getExceptionMessage(e));
		}
		writeJson(j);
	}
	@LogAnnotation(event="删除参数",tablename="t_parameter")
	public void deleteParameter(){
		Json j=new Json();
		try{
			if(!StringUtils.isEmpty(parameterVo.getIds())){
				baseService.removeParameter(parameterVo.getIds());
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

	public void isadmin(){
		Json j=new Json();
		boolean flag=false;
		String ids=getRequest().getParameter("ids");
		flag=baseDao.isadmin(Parameter.class,ids,queryEnterpriseByWhere());
		j.setSuccess(flag);
		writeJson(j);
	}
}
