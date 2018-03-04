package com.inspect.action.basis;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.inspect.util.common.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;

import com.inspect.action.common.BaseAction;
import com.inspect.annotation.LogAnnotation;
import com.inspect.model.basis.TEquipment;
import com.inspect.model.basis.TPoint;
import com.inspect.model.basis.TPointEquipment;
import com.inspect.service.InspectItemServiceI;
import com.inspect.service.SystemServiceI;
import com.inspect.vo.basis.PointVo;
import com.inspect.vo.basis.StationVo;
import com.inspect.vo.comon.Json;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 巡检点业务流程控制Action
 * @version 1.0
 */
@Namespace("/basis")
@Action(value="stationAction",results={
		@Result(name="stationList",location="/webpage/basis/stationList.jsp"),
		@Result(name="stationAdd",location="/webpage/basis/stationAdd.jsp"),
		@Result(name="stationEdit",location="/webpage/basis/stationEdit.jsp")})
public class StationAction extends BaseAction implements ModelDriven<StationVo> {

	private static final long serialVersionUID = 112L;
	
	private static final Logger logger = Logger.getLogger(StationAction.class);
	
	private InspectItemServiceI inspectItemService;
	
	private SystemServiceI systemService;
	
	private StationVo stationVo=new StationVo();

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
	public StationVo getModel() {
		return stationVo;
	}
	
	public String stationList(){
		//getRequest().setAttribute("EnterpriseList",systemService.comboboxEnterprise(queryEnterpriseByWhere()));
		return "stationList";
	}
	
	public String stationAdd(){
//		List<TEquipment> eqs=this.inspectItemService.getEquipmentList(getSessionUserName().getEntid(),"","a");
//		getRequest().setAttribute("EquipmentList",eqs);
		return "stationAdd";
	}
	
	public String stationEdit(){

		return "stationEdit";
	}
	
	public void stationDatagrid(){
		
		System.out.println("^^^^^^^^stationDatagrid()^^^^^^^");
		
		
		int page =Integer.parseInt(ServletActionContext.getRequest().getParameter("page") );
		int rows =Integer.parseInt(ServletActionContext.getRequest().getParameter("rows") );
		Map<String, Object> map = inspectItemService.findStationDatagrid(stationVo,page,rows,querySql());
		writeJson(map);
   
		System.out.println("^^^^^^^^stationDatagrid()^^^^^map^^^^^^^"+map);
	
	
	}
	
	@LogAnnotation(event="新增基站",tablename="t_station")
	public void addStation(){
		Json j=new Json();
		try{
			long sname=inspectItemService.countStationName(stationVo.getStname());
			if(sname>0){
				 j.setMsg("名称：【"+stationVo.getStname()+"】已存在,不能重复添加!");
				 setOperstatus(1);
			}else{
				stationVo.setEntid(getSessionUserName().getEntid());
				inspectItemService.addStation(stationVo);
				j.setMsg("基站添加成功！");
				j.setSuccess(true);
			}
		}catch(Exception e){
			setOperstatus(1);
			j.setMsg("基站添加失败！");
		//	logger.error(ExceptionUtil.getExceptionMessage(e));
		}
		writeJson(j);
	}
	
	@LogAnnotation(event="修改基站",tablename="t_station")
	public void editStation(){
		Json j=new Json();
		try{
			inspectItemService.editStation(stationVo);
			j.setMsg("基站修改成功！");
			j.setSuccess(true);
		}catch(Exception e){
			setOperstatus(1);
			j.setMsg("基站修改失败！");
			//logger.error(ExceptionUtil.getExceptionMessage(e));
		}
		writeJson(j);
	}
	@LogAnnotation(event="删除基站",tablename="t_station")
	public void deleteStation(){
		Json j=new Json();
		try{
			if(!StringUtils.isEmpty(stationVo.getIds())){
				inspectItemService.removeStation(stationVo.getIds());
				j.setSuccess(true);
				j.setMsg("删除成功！");
			}
		}catch(Exception e){
			setOperstatus(1);
			j.setMsg("信息删除失败！");
			//logger.error(ExceptionUtil.getExceptionMessage(e));
		}
		writeJson(j);
	}

	public void pequipmentList(){
		List<TEquipment> but=inspectItemService.getEquipmentList(getSessionUserName().getEntid(),"","a");
		writeJson(but);
	}

	
}
