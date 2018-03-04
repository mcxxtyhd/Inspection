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
import com.inspect.service.InspectItemServiceI;
import com.inspect.service.SystemServiceI;
import com.inspect.util.common.ExceptionUtil;
import com.inspect.util.common.StringUtils;
import com.inspect.vo.basis.RfidVo;
import com.inspect.vo.comon.Json;
import com.opensymphony.xwork2.ModelDriven;

/**
 * RFID业务流程控制Action
 * @version 1.0
 */
@Namespace("/basis")
@Action(value="rfidAction",results={
		@Result(name="rfidList",location="/webpage/basis/rfidList.jsp"),
		@Result(name="rfidAdd",location="/webpage/basis/rfidAdd.jsp"),
		@Result(name="rfidEdit",location="/webpage/basis/rfidEdit.jsp")})
public class RfidAction extends BaseAction implements ModelDriven<RfidVo> {

	private static final long serialVersionUID = -413243087061168992L;
	
	private static final Logger logger = Logger.getLogger(RfidAction.class);
	
	
	private InspectItemServiceI inspectItemService;
	
	private SystemServiceI systemService;
	
	private RfidVo rfidVo=new RfidVo();

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
	public RfidVo getModel() {
		return rfidVo;
	}
	
	public String rfidList(){
		getRequest().setAttribute("EnterpriseList",systemService.comboboxEnterprise(queryEnterpriseByWhere()));
		return "rfidList";
	}
	
	public String rfidAdd(){
		return "rfidAdd";
	}
	
	public String rfidEdit(){
		return "rfidEdit";
	}
	
	public void rfidDatagrid(){
		int page =Integer.parseInt(ServletActionContext.getRequest().getParameter("page") );
		int rows =Integer.parseInt(ServletActionContext.getRequest().getParameter("rows") );
		Map<String, Object> map = inspectItemService.findRfidDatagrid(rfidVo,page,rows,querySql());
		writeJson(map);
	}
	
	@LogAnnotation(event="添加RFID",tablename="t_rfid")
	public void addRfid(){
		Json j=new Json();
		try{
			rfidVo.setEntid(getSessionUserName().getEntid());
			inspectItemService.addRfid(rfidVo);
			j.setMsg("添加成功！");
			j.setSuccess(true);
		}catch(Exception e){
			setOperstatus(1);
			j.setMsg("添加失败！");
			logger.error(ExceptionUtil.getExceptionMessage(e));
		}
		writeJson(j);
	}
	
	@LogAnnotation(event="修改RFID",tablename="t_rfid")
	public void editRfid(){
		Json j=new Json();
		try{
			inspectItemService.editRfid(rfidVo);
			j.setMsg("修改成功！");
			j.setSuccess(true);
		}catch(Exception e){
			setOperstatus(1);
			j.setMsg("修改失败！");
			logger.error(ExceptionUtil.getExceptionMessage(e));
		}
		writeJson(j);
	}
	@LogAnnotation(event="删除RFID",tablename="t_rfid")
	public void deleteRfid(){
		Json j=new Json();
		try{
			if(!StringUtils.isEmpty(rfidVo.getIds())){
				inspectItemService.removeRfid(rfidVo.getIds());
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
