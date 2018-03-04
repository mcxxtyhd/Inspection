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
import com.inspect.constant.Constant;
import com.inspect.dao.BaseDaoI;
import com.inspect.model.basis.TEnumParameter;
import com.inspect.model.basis.TTerminal;
import com.inspect.service.InspectUserServiceI;
import com.inspect.service.SystemServiceI;
import com.inspect.util.common.ExceptionUtil;
import com.inspect.util.common.StringUtils;
import com.inspect.vo.basis.TerminalVo;
import com.inspect.vo.comon.Json;
import com.opensymphony.xwork2.ModelDriven;
/**
 * 终端配置流程控制Action
 * @version 1.0
 */
@Namespace("/basis")
@Action(value="terminalAction",results={
		@Result(name="terminalList",location="/webpage/basis/terminalList.jsp"),
		@Result(name="terminalAdd",location="/webpage/basis/terminalAdd.jsp"),
		@Result(name="terminalEdit",location="/webpage/basis/terminalEdit.jsp")})
public class TerminalAction extends BaseAction implements ModelDriven<TerminalVo> {
	private static final long serialVersionUID = -9011086728759375544L;

	private static final Logger logger = Logger.getLogger(TerminalAction.class);
	
	private InspectUserServiceI inspectUserService;
	
	private SystemServiceI systemService;
	
	private TerminalVo terminalVo=new TerminalVo();
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
	public TerminalVo getModel() {
		return terminalVo;
	}
	
	public String terminalList(){
		getRequest().setAttribute("EnterpriseList",systemService.comboboxEnterprise(queryEnterpriseByWhere()));
		return "terminalList";
	}
	
	public String terminalAdd(){
		return "terminalAdd";
	}
	
	public String terminalEdit(){
		return "terminalEdit";
	}
	
	public void terminalDatagrid(){
		int page =Integer.parseInt(ServletActionContext.getRequest().getParameter("page") );
		int rows =Integer.parseInt(ServletActionContext.getRequest().getParameter("rows") );
		Map<String, Object> map = inspectUserService.findTerminalterDatagrid(terminalVo,page,rows,querySql());
		writeJson(map);
	}
	
	@LogAnnotation(event="添加终端",tablename="t_terminal")
	public void addTerminal(){
		Json j=new Json();
		try{
			long tcount=this.inspectUserService.terminateTotalCount(getSessionUserName().getEntid());
			if(tcount>=Constant.TERMINATE_COUNT){
				j.setMsg("终端数量已超过规定个数，不能再添加！");
			}
			long tcount1=this.inspectUserService.count(terminalVo.getTermno(),queryEnterpriseByWhere(),0);
			if(tcount1>=Constant.TERMINATE_COUNT){
				j.setMsg("终端编号已存在，不能重复添加！");
			}else{
				terminalVo.setEntid(getSessionUserName().getEntid());
				inspectUserService.addTerminal(terminalVo);
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
	
	@LogAnnotation(event="修改终端",tablename="t_terminal")
	public void editTerminal(){

		Json j=new Json();
		try{
			inspectUserService.editTerminal(terminalVo);
			j.setMsg("修改成功！");
			j.setSuccess(true);
		}catch(Exception e){
			setOperstatus(1);
			j.setMsg("修改失败！");
			logger.error(ExceptionUtil.getExceptionMessage(e));
		}
		writeJson(j);
	}
	@LogAnnotation(event="删除终端",tablename="t_terminal")
	public void deleteTerminal(){
		Json j=new Json();
		try{
			if(!StringUtils.isEmpty(terminalVo.getIds())){
				inspectUserService.removeTerminal(terminalVo.getIds());
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
		flag=baseDao.isadmin(TTerminal.class,ids,queryEnterpriseByWhere());
		j.setSuccess(flag);
		writeJson(j);
	}
}
