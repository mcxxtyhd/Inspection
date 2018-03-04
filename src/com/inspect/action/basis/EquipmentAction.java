package com.inspect.action.basis;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;

import com.inspect.action.common.BaseAction;
import com.inspect.annotation.LogAnnotation;
import com.inspect.dao.BaseDaoI;
import com.inspect.model.baseinfo.TBaseInfo;
import com.inspect.model.basis.TEnumParameter;
import com.inspect.model.basis.TEquipment;
import com.inspect.model.basis.TEquipmentProjectGroup;
import com.inspect.model.basis.TProject;
import com.inspect.model.basis.TProjectGroup;
import com.inspect.service.InspectItemServiceI;
import com.inspect.service.SystemServiceI;
import com.inspect.util.common.ExceptionUtil;
import com.inspect.util.common.StringUtils;
import com.inspect.util.excel.Eoip;
import com.inspect.util.excel.Eoiprule;
import com.inspect.vo.basis.EquipmentVo;
import com.inspect.vo.comon.Json;
import com.inspect.vo.summary.EquipmentSummaryVo;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 巡检设备业务流程控制Action
 * @version 1.0
 */
@Namespace("/basis")
@Action(value="equipmentAction",results={
		@Result(name="equipmentList",location="/webpage/basis/equipmentList.jsp"),
		@Result(name="equipmentAdd",location="/webpage/basis/equipmentAdd.jsp"),
		@Result(name="equipmentEdit",location="/webpage/basis/equipmentEdit.jsp"),
		@Result(name="equipmentExcel",location="/webpage/basis/equipmentExcel.jsp"),
		@Result(name="ebaseinfoEdit",location="/webpage/basis/ebaseinfoEdit.jsp")})
public class EquipmentAction extends BaseAction implements ModelDriven<EquipmentVo> {

	private static final long serialVersionUID = 6136954790875343573L;
	
	private static final Logger logger = Logger.getLogger(EquipmentAction.class);
	
	private InspectItemServiceI inspectItemService;
	
	private SystemServiceI systemService;
	
	private EquipmentVo equipmentVo=new EquipmentVo();

	public InspectItemServiceI getInspectItemService() {
		return inspectItemService;
	}
	@Resource
	private BaseDaoI baseDao;
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
	public EquipmentVo getModel() {
		return equipmentVo;
	}
	
	public String equipmentList(){
		getRequest().setAttribute("EnterpriseList",systemService.comboboxEnterprise(queryEnterpriseByWhere()));
		return "equipmentList";
	}
	public String equipmentExcel() {
		return "equipmentExcel";
	}
	public String equipmentAdd(){
		List<TProjectGroup> pjt=this.inspectItemService.getProjectGroupList(getSessionUserName().getEntid(),"","a");
		getRequest().setAttribute("ProjectList",pjt);
		getRequest().setAttribute("TwoList",inspectItemService.getTwoDimensionCodeList(getSessionUserName().getEntid()));
		getRequest().setAttribute("RfidList",inspectItemService.getRfidList(getSessionUserName().getEntid()));
		return "equipmentAdd";
	}
	
	public String equipmentEdit(){
		//已选巡检项列表
		StringBuffer pids=new StringBuffer();
		TEquipment prot=inspectItemService.getEquipment(getRequest().getParameter("EQID"));  
		Set<TEquipmentProjectGroup> equipmentprojectlist=prot.getEquipmentprojectgroups();
		List<TProjectGroup> list=new ArrayList<TProjectGroup>();
		List<TProjectGroup> projectList=new ArrayList<TProjectGroup>();
		boolean b=false;
		if(equipmentprojectlist!=null && equipmentprojectlist.size()>0){
			for(TEquipmentProjectGroup a:equipmentprojectlist){
				TProjectGroup einfo=new TProjectGroup();
				einfo.setId(a.getTprojectgroup().getId());
				einfo.setPgname(a.getTprojectgroup().getPgname());
				list.add(einfo);
				//获取已选巡检点ID
				if(b){
					pids.append(",");
				}
				pids.append(a.getTprojectgroup().getId());
				b=true;
			}
		}
		getRequest().setAttribute("EquipmentSelectProjectList",list);
		
		//可选巡检点列表
		if(!StringUtils.isEmpty(pids.toString())){
			projectList=inspectItemService.getProjectGroupList(getSessionUserName().getEntid(),pids.toString(),"u");
		}else{
			projectList=inspectItemService.getProjectGroupList(getSessionUserName().getEntid(),"","u");
		}
		getRequest().setAttribute("ProjectList",projectList);
		getRequest().setAttribute("TwoList",inspectItemService.getTwoDimensionCodeList(getSessionUserName().getEntid()));
		getRequest().setAttribute("RfidList",inspectItemService.getRfidList(getSessionUserName().getEntid()));
		return "equipmentEdit";
	}
	
	public void equipmentDatagrid(){
		int page =Integer.parseInt(ServletActionContext.getRequest().getParameter("page") );
		int rows =Integer.parseInt(ServletActionContext.getRequest().getParameter("rows") );
		Map<String, Object> map = inspectItemService.findEquipmentDatagrid(equipmentVo,page,rows,querySql());
		writeJson(map);
	}
	
	public void equipmentInfoDatagrid(){
		int page =Integer.parseInt(ServletActionContext.getRequest().getParameter("page") );
		int rows =Integer.parseInt(ServletActionContext.getRequest().getParameter("rows") );
		Map<String, Object> map = inspectItemService.findEquipmentInfoDatagrid(equipmentVo,page,rows,querySql());
		writeJson(map);
	}
	
	public void equipmentInfoDatagridNotPage(){
		Map<String, Object> map = inspectItemService.findEquipmentInfoDatagrid(equipmentVo,querySql());
		writeJson(map);
	}
	
	@LogAnnotation(event="添加设备",tablename="t_equipment")
	public void addEquipment(){
		Json j=new Json();
		try{
			//TBaseInfo bf=(TBaseInfo)inspectItemService.getEntityById(TBaseInfo.class,Integer.parseInt(equipmentVo.getEpid()));
			TBaseInfo bf=inspectItemService.getEntityById(TBaseInfo.class,Integer.parseInt(equipmentVo.getEpid()));

			long cnumber=inspectItemService.countEquipmentNumber(bf.getBnumber(),queryEnterpriseByWhere());
			if(cnumber>0){
				 j.setMsg("设备已存在,不能重复添加!");
				 setOperstatus(1);
			}else{
				equipmentVo.setEntid(getSessionUserName().getEntid());
				inspectItemService.addEquipment(equipmentVo);
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
	
	@LogAnnotation(event="修改巡检设备",tablename="t_equipment")
	public void editEquipment(){
		Json j=new Json();
		try{
			inspectItemService.editEquipment(equipmentVo);
			j.setMsg("修改成功！");
			j.setSuccess(true);
		}catch(Exception e){
			setOperstatus(1);
			j.setMsg("修改失败！");
			logger.error(ExceptionUtil.getExceptionMessage(e));
		}
		writeJson(j);
	}
	@LogAnnotation(event="删除巡检设备",tablename="t_equipment")
	public void deleteEquipment(){
		Json j=new Json();
		try{
			if(!StringUtils.isEmpty(equipmentVo.getIds())){
				inspectItemService.removeEquipment(equipmentVo.getIds());
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

	public void eprojectList(){
		List<TProject> but=inspectItemService.getProjectList(getSessionUserName().getEntid());
		writeJson(but);
	}
	
	public void uploadFile() throws Exception {
		String realPath = ServletActionContext.getServletContext().getRealPath("webpage/upload");

		if (equipmentVo.getExcelFile() != null) {
			File uploadfile = new File(new File(realPath),equipmentVo.getExcelFileFileName());
			if (!uploadfile.getParentFile().exists()) {
				uploadfile.getParentFile().mkdir();
			}
			if (uploadfile.exists()) {
				uploadfile.delete();
			}
			FileUtils.copyFile(equipmentVo.getExcelFile(),uploadfile);
		}
	}
	@LogAnnotation(event="导入设备巡检项",tablename="t_equipment")
	public void testExel() throws Exception{
		Json j=new Json();
		List<Object> db = new ArrayList<Object>();
		  //设置导入规则
		try {
			 Eoiprule rule = new Eoiprule();
			  rule.setSheetnumber((short)0);		//读第一个sheet
			  /*
			   * entity对象字段名与excle列值的映射
			   * 名称,列值;名称,列值;名称,列值;
			   */
				rule.setRowcontentspos((short)1);		//数据内容从第2行开始
				rule.setNametocol("enumber,1;epGroupName,2");	//映射三个字段，
			  Eoip eoip = new Eoip();
			  eoip.setErule(rule);
			  
			  //指定导入的数据库表
			  EquipmentSummaryVo mirror = new EquipmentSummaryVo();  
			// uploadFile();
			  //获得bean list，excle每行对应一个bean对象
			  db = eoip.excel2db(mirror,equipmentVo.getExcelFile());
			  //加入到数据库中
			  int entid=getSessionUserName().getEntid();
			String count1=inspectItemService.saveEquipmentProjectGroupList(db,entid);
				j.setMsg("导入成功！excel总行数："+eoip.getTotalExcelColumns()+",excel有效数目："+eoip.getTotalExcelDataLines()+",实际保存行数："+count1);
				j.setSuccess(true);
		} catch (Exception e) {
			// TODO: handle exception
			j.setMsg("导入失败！");
			j.setSuccess(true);
		}
		writeJson(j);
	}
	public void isadmin(){
		Json j=new Json();
		boolean flag=false;
		String ids=getRequest().getParameter("ids");
		flag=baseDao.isadmin(TEquipment.class,ids,queryEnterpriseByWhere());
		j.setSuccess(flag);
		writeJson(j);
	}
}