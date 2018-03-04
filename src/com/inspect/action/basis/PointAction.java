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
import org.springframework.beans.BeanUtils;

import com.inspect.action.common.BaseAction;
import com.inspect.annotation.LogAnnotation;
import com.inspect.dao.BaseDaoI;
import com.inspect.model.baseinfo.TBaseInfo;
import com.inspect.model.basis.TEnumParameter;
import com.inspect.model.basis.TEquipment;
import com.inspect.model.basis.TInspectUser;
import com.inspect.model.basis.TPoint;
import com.inspect.model.basis.TPointEquipment;
import com.inspect.service.InspectItemServiceI;
import com.inspect.service.SystemServiceI;
import com.inspect.util.common.ExceptionUtil;
import com.inspect.util.common.StringUtils;
import com.inspect.util.excel.Eoip;
import com.inspect.util.excel.Eoiprule;
import com.inspect.vo.basis.EquipmentVo;
import com.inspect.vo.basis.InspectUserVo;
import com.inspect.vo.basis.PointVo;
import com.inspect.vo.basis.TBaseInfoVo;
import com.inspect.vo.comon.Json;
import com.inspect.vo.summary.EquipmentSummaryVo;
import com.inspect.vo.summary.PointSummaryVo;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 巡检点业务流程控制Action
 * @version 1.0
 */
@Namespace("/basis")
@Action(value="pointAction",results={
		@Result(name="pointList",location="/webpage/basis/pointList.jsp"),
		@Result(name="pointAdd",location="/webpage/basis/pointAdd.jsp"),
		@Result(name="pointEdit",location="/webpage/basis/pointEdit.jsp")})
public class PointAction extends BaseAction implements ModelDriven<PointVo> {

	private static final long serialVersionUID = 1L;
	
	private static final Logger logger = Logger.getLogger(PointAction.class);
	
	private InspectItemServiceI inspectItemService;
	
	private SystemServiceI systemService;
	@Resource
	private BaseDaoI baseDao;
	private PointVo pointVo=new PointVo();

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
	public PointVo getModel() {
		return pointVo;
	}
	
	public String pointList(){
		getRequest().setAttribute("EnterpriseList",systemService.comboboxEnterprise(queryEnterpriseByWhere()));
		return "pointList";
	}
	
	public String pointAdd(){
//		List<TEquipment> eqs=this.inspectItemService.getEquipmentList(queryEnterpriseByWhere(),"","a");
//		getRequest().setAttribute("EquipmentList",eqs);
		List<TEquipment> bs=this.inspectItemService.getRetionList(queryEnterpriseByWhere(),"","a");
		getRequest().setAttribute("eList",bs);
		return "pointAdd";
	}
	
	public String pointEdit(){
		//显示区县列表
		List<TEquipment> bs=this.inspectItemService.getRetionList(queryEnterpriseByWhere(),"","a");
		getRequest().setAttribute("eList",bs);
		//已选巡检设备列表
		StringBuffer eids=new StringBuffer("");
		String region="";
		TPoint point=inspectItemService.getPoint(getRequest().getParameter("PID"));
		//flag为1 表示是从list页面过来的
		Set<TPointEquipment> pointequipmentlist=point.getPointequipments();
		List<TEquipment> list=new ArrayList<TEquipment>();
		List<TEquipment> equipmentList=new ArrayList<TEquipment>();
		boolean b=false;
		if(pointequipmentlist!=null && pointequipmentlist.size()>0){
			for(TPointEquipment a:pointequipmentlist){
				TEquipment einfo=new TEquipment();
				einfo.setId(a.getTequipment().getId());
				einfo.setEname(a.getTequipment().getEname());
				einfo.setEregion(a.getTequipment().getEregion());
				//已选巡检点ID
				list.add(einfo);
				if(b){
					eids.append(",");
				}
				else{
					region=einfo.getEregion();
				}
				eids.append(a.getTequipment().getId());
				b=true;
			}
		}
		//显示已选设备名称
		getRequest().setAttribute("pointSelectEquipmentList",list);
		//可选巡检点列表
	//	equipmentList=inspectItemService.getEquipList(region, "", eids.toString(), "u", queryEnterpriseByWhere());
		//显示还可选设备列表
		getRequest().setAttribute("EquipmentList",equipmentList);
		
		return "pointEdit";
	}
	
	public void pointDatagrid(){
		int page =Integer.parseInt(ServletActionContext.getRequest().getParameter("page") );
		int rows =Integer.parseInt(ServletActionContext.getRequest().getParameter("rows") );
		Map<String, Object> map = inspectItemService.findPointDatagrid(pointVo,page,rows,queryEnterpriseByWhere()+"");
		writeJson(map);
	}
	
	@LogAnnotation(event="添加巡检点",tablename="t_point")
	public void addPoint(){
		Json j=new Json();
		try{
			long cname=inspectItemService.countPointName(pointVo.getPoname(),queryEnterpriseByWhere());
			if(cname>0){
				 j.setMsg("名称：【"+pointVo.getPoname()+"】已存在,不能重复添加!");
				 setOperstatus(1);
			}else{
				pointVo.setEntid(getSessionUserName().getEntid());
				inspectItemService.addPoint(pointVo);
				j.setMsg("添加成功！");
				j.setSuccess(true);
			}
		}catch(Exception e){
			setOperstatus(1);
			j.setMsg("添加巡检点失败！");
			logger.error(ExceptionUtil.getExceptionMessage(e));
		}
		writeJson(j);
	}
	
	@LogAnnotation(event="修改巡检点",tablename="t_point")
	public void editPoint(){
		Json j=new Json();
		try{
			inspectItemService.editPoint(pointVo);
			j.setMsg("修改成功！");
			j.setSuccess(true);
		}catch(Exception e){
			setOperstatus(1);
			j.setMsg("修改失败！");
			logger.error(ExceptionUtil.getExceptionMessage(e));
		}
		writeJson(j);
	}
	@LogAnnotation(event="删除巡检点",tablename="t_point")
	public void deletePoint(){
		Json j=new Json();
		try{
			if(!StringUtils.isEmpty(pointVo.getIds())){
				inspectItemService.removePoint(pointVo.getIds());
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

	public void pequipmentList(){
		List<TEquipment> but=inspectItemService.getEquipmentList(queryEnterpriseByWhere(),"","a");
		writeJson(but);
	}
	
	public void uploadFile() throws Exception {
		String realPath = ServletActionContext.getServletContext().getRealPath("webpage/upload");

		if (pointVo.getExcelFile() != null) {
			File uploadfile = new File(new File(realPath),pointVo.getExcelFileFileName());
			if (!uploadfile.getParentFile().exists()) {
				uploadfile.getParentFile().mkdir();
			}
			if (uploadfile.exists()) {
				uploadfile.delete();
			}
			FileUtils.copyFile(pointVo.getExcelFile(),uploadfile);
		}
	}
	@LogAnnotation(event="导入巡检点",tablename="t_point")
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
				rule.setNametocol("enumber,1;pointname,2");	//映射三个字段，
			  Eoip eoip = new Eoip();
			  eoip.setErule(rule);
			  
			  //指定导入的数据库表
			  PointSummaryVo mirror = new PointSummaryVo();  
			//  uploadFile();
			  //获得bean list，excle每行对应一个bean对象
			  db = eoip.excel2db(mirror,pointVo.getExcelFile());
			  //加入到数据库中
			  int entid=getSessionUserName().getEntid();
			String count1=inspectItemService.savePointList(db,entid);
				j.setMsg("导入成功！excel总行数："+eoip.getTotalExcelColumns()+",excel有效数目："+eoip.getTotalExcelDataLines()+",实际保存行数："+count1);
				j.setSuccess(true);
		} catch (Exception e) {
			// TODO: handle exception
			j.setMsg("导入失败！");
			j.setSuccess(true);
		}
		writeJson(j);
	}
	
	/**
	 * 级联查询
	 * 通过区县名称
	 */
	public void getEquipByRegion(){

		String  region=getRequest().getParameter("bregion");
		String  ename=getRequest().getParameter("ename");
		List<TEquipment> equipList = new ArrayList<TEquipment>();
		equipList=inspectItemService.getEquipList(region, ename, "", "a", queryEnterpriseByWhere());
		List<EquipmentVo> binfoList=new ArrayList<EquipmentVo>();
		//方法一
		if(equipList!=null&&equipList.size()>0){
			for(int i=0;i<equipList.size();i++){
				EquipmentVo binfoVo=new EquipmentVo();
			    BeanUtils.copyProperties(equipList.get(i), binfoVo);
			    binfoList.add(binfoVo);
			}
		/*	方法二
		 * List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
		 * for(int i=0;i<inspectUserList.size();i++){
				Map<String, Object> mapClazz = new HashMap<String, Object>();
				mapClazz.put("id", inspectUserList.get(i).getId()+"");
				mapClazz.put("iuname", inspectUserList.get(i).getIuname());
				mapClazz.put("iuname1", inspectUserList.get(i));
				mapList.add(mapClazz);
			}*/
		}
	//	writeJson(mapList);
		writeJson(binfoList);
	}
	
	public void isadmin(){
		Json j=new Json();
		boolean flag=false;
		String ids=getRequest().getParameter("ids");
		flag=baseDao.isadmin(TPoint.class,ids,queryEnterpriseByWhere());
		j.setSuccess(flag);
		writeJson(j);
	}
}
