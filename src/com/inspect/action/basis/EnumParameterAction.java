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
import com.inspect.service.InspectItemServiceI;
import com.inspect.service.SystemServiceI;
import com.inspect.util.common.ExceptionUtil;
import com.inspect.util.common.StringUtils;
import com.inspect.vo.basis.EnumParameterVo;
import com.inspect.vo.comon.Json;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 巡检内容枚举类型值业务流程控制Action
 * @version 1.0
 */
@Namespace("/basis")
@Action(value="enumAction",results={
		@Result(name="enumparameterList",location="/webpage/basis/enumparameterList.jsp"),
		@Result(name="enumparameterAdd",location="/webpage/basis/enumparameterAdd.jsp"),
		@Result(name="enumparameterEdit",location="/webpage/basis/enumparameterEdit.jsp")})
public class EnumParameterAction extends BaseAction implements ModelDriven<EnumParameterVo> {

	private static final long serialVersionUID = -4664913577035047375L;
	
	private static final Logger logger = Logger.getLogger(EnumParameterAction.class);
	
	private EnumParameterVo enumParameterVo=new EnumParameterVo();
	
	private InspectItemServiceI inspectItemService;
	
	private SystemServiceI systemService;
	@Resource
	private BaseDaoI baseDao;
	
	public InspectItemServiceI getInspectItemService() {
		return inspectItemService;
	}
	@Resource
	public void setInspectItemService(InspectItemServiceI inspectItemService) {
		this.inspectItemService = inspectItemService;
	}
	
	public SystemServiceI getSystemService() {
		return systemService;
	}

	@Resource
	public void setSystemService(SystemServiceI systemService) {
		this.systemService = systemService;
	}

	@Override
	public EnumParameterVo getModel() {
		return enumParameterVo;
	}
	
	public String enumparameterList(){
		getRequest().setAttribute("EnterpriseList",systemService.comboboxEnterprise(queryEnterpriseByWhere()));
		return "enumparameterList";
	}
	
	public String enumparameterAdd(){
		return "enumparameterAdd";
	}
	
	public String enumparameterEdit(){
		return "enumparameterEdit";
	}
	public void enumparameterDatagrid(){
		int page =Integer.parseInt(ServletActionContext.getRequest().getParameter("page") );
		int rows =Integer.parseInt(ServletActionContext.getRequest().getParameter("rows") );
		Map<String, Object> map = inspectItemService.findEnumParameterDatagrid(enumParameterVo,page,rows,querySql());
		writeJson(map);
	}
	
	@LogAnnotation(event="添加枚举类型参数",tablename="t_enum_parameter")
	public void addEnumParameter(){
		Json j=new Json();
		try{
//			long cnumber=inspectItemService.countEnumName(enumParameterVo.getPname(),queryEnterpriseByWhere());
//			if(cnumber>0){
//				 j.setMsg("名称已存在,不能重复添加!");
//			}else{
				enumParameterVo.setEntid(getSessionUserName().getEntid());
				inspectItemService.addEnumParameter(enumParameterVo);
				j.setMsg("添加成功！");
				j.setSuccess(true);
//			}
		}catch(Exception e){
			setOperstatus(1);
			j.setMsg("添加失败！");
			logger.error(ExceptionUtil.getExceptionMessage(e));
		}
		writeJson(j);
	}
	
	@LogAnnotation(event="修改枚举类型参数",tablename="t_enum_parameter")
	public void editEnumParameter(){
		Json j=new Json();
		try{
			inspectItemService.editEnumParameter(enumParameterVo);
			j.setMsg("修改成功！");
			j.setSuccess(true);
		}catch(Exception e){
			setOperstatus(1);
			j.setMsg("修改失败！");
			logger.error(ExceptionUtil.getExceptionMessage(e));
		}
		writeJson(j);
	}
	@LogAnnotation(event="删除枚举类型参数",tablename="t_enum_parameter")
	public void deleteEnumParameter(){
		Json j=new Json();
		try{
			if(!StringUtils.isEmpty(enumParameterVo.getIds())){
				inspectItemService.removeEnumParameter(enumParameterVo.getIds());
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
		flag=baseDao.isadmin(TEnumParameter.class,ids,queryEnterpriseByWhere());
		j.setSuccess(flag);
		writeJson(j);
	}
}