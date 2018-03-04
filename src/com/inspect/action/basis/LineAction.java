package com.inspect.action.basis;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
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

import com.alibaba.fastjson.JSON;
import com.inspect.action.common.BaseAction;
import com.inspect.annotation.LogAnnotation;
import com.inspect.dao.BaseDaoI;
import com.inspect.model.basis.TEnumParameter;
import com.inspect.model.basis.TEquipment;
import com.inspect.model.basis.TLine;
import com.inspect.model.basis.TLinePoint;
import com.inspect.model.basis.TPlan;
import com.inspect.model.basis.TPoint;
import com.inspect.model.basis.TPointEquipment;
import com.inspect.service.InspectItemServiceI;
import com.inspect.service.PlanServiceI;
import com.inspect.service.SystemServiceI;
import com.inspect.util.common.ExceptionUtil;
import com.inspect.util.common.StringUtils;
import com.inspect.util.excel.Eoip;
import com.inspect.util.excel.Eoiprule;
import com.inspect.vo.basis.EquipmentVo;
import com.inspect.vo.basis.LineVo;
import com.inspect.vo.basis.PointVo;
import com.inspect.vo.comon.Json;
import com.inspect.vo.summary.LineSummaryVo;
import com.inspect.vo.summary.PointSummaryVo;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 巡检线路业务流程控制Action
 * @version 1.0
 */
@Namespace("/basis")
@Action(value="lineAction",results={
		@Result(name="lineList",location="/webpage/basis/lineList.jsp"),
		@Result(name="lineAdd",location="/webpage/basis/lineAdd.jsp"),
		@Result(name="lineEdit",location="/webpage/basis/lineEdit.jsp")})
public class LineAction extends BaseAction implements ModelDriven<LineVo> {

	private static final long serialVersionUID = -8401567083697554491L;

	private static final Logger logger = Logger.getLogger(LineAction.class);
	
	private InspectItemServiceI inspectItemService;
	
	private SystemServiceI systemService;
	@Resource
	private PlanServiceI planService;
	
	private LineVo lineVo=new LineVo();
	
	public InspectItemServiceI getInspectItemService() {
		return inspectItemService;
	}
	@Resource
	private BaseDaoI baseDao;
	
	@Resource
	public void setInspectItemService(InspectItemServiceI inspectItemService) {
		
		System.out.println("1,4,8setInspectItemService");
		this.inspectItemService = inspectItemService;
	}
	
	public SystemServiceI getSystemService() {
		System.out.println("getSystemService");
		return systemService;
	}

	@Resource
	public void setSystemService(SystemServiceI systemService) {
		System.out.println("2,4,9setSystemService");
		this.systemService = systemService;
	}
	
	@Override
	public LineVo getModel() {
		System.out.println("3,5,10getModel()");
		return lineVo;
	}
	
	public String lineList(){
		getRequest().setAttribute("EnterpriseList",systemService.comboboxEnterprise(queryEnterpriseByWhere()));
		return "lineList";
	}
	
	@SuppressWarnings("unchecked")
	public String lineAdd(){
//		List pointList=inspectItemService.getPointList(getSessionUserName().getEntid(),"","a");
//		getRequest().setAttribute("PointList",pointList);
		List<TEquipment> bs=this.inspectItemService.getRetionList(queryEnterpriseByWhere(),"","a");
		getRequest().setAttribute("eList",bs);
		return "lineAdd";
	}
	
	public String lineEdit(){
		//显示区县列表
		List<TEquipment> bs=this.inspectItemService.getRetionList(queryEnterpriseByWhere(),"","a");
		getRequest().setAttribute("eList",bs);
		//已选巡检点列表
		StringBuffer pids=new StringBuffer("");
		TLine line=inspectItemService.getLine(getRequest().getParameter("LID"));
		List<TLinePoint> linepointlist=line.getLinepoints();
		List<TPoint> list=new ArrayList<TPoint>();
		List<PointVo> pointList=new ArrayList<PointVo>();
		String region="";
		//巡检点集合
		if(linepointlist!=null && linepointlist.size()>0){
			for(TLinePoint a:linepointlist){
				TPoint linfo=new TPoint();
				linfo.setId(a.getTpoint().getId());
				linfo.setPoname(a.getTpoint().getPoname());
				list.add(linfo);
				//获取已选巡检点ID
				if (linepointlist.size() - 1 == linepointlist.lastIndexOf(a)) {
					pids.append(a.getTpoint().getId());
					Set<TPointEquipment> pe=a.getTpoint().getPointequipments();
					Iterator<TPointEquipment> iter=pe.iterator();
					if(iter.hasNext()){
						TPointEquipment pe1=iter.next();
						region=pe1.getTequipment().getEregion();
					}
				} else {
					pids.append(a.getTpoint().getId() + ",");
				}
			}
		}
		getRequest().setAttribute("LineSelectPointList",list);
		//可选巡检点列表
		//可选巡检点列表
		//pointList=inspectItemService.getPointList(region, "", pids.toString(), "u", getSessionUserName().getEntid());
		getRequest().setAttribute("PointList",pointList);
		
		return "lineEdit";
	}
	
	public void lineDatagrid(){
		int page =Integer.parseInt(ServletActionContext.getRequest().getParameter("page") );
		int rows =Integer.parseInt(ServletActionContext.getRequest().getParameter("rows") );
		Map<String, Object> map = inspectItemService.findLineDatagrid(lineVo,page,rows,queryEnterpriseByWhere()+"");
		writeJson(map);
	}
	@LogAnnotation(event="添加巡检线路",tablename="t_line")
	public void addLine(){
		Json j=new Json();
		try{
			if(lineVo.getLpIds()!=null&&lineVo.getLpIds().length()>0){
				long lname=inspectItemService.countLineName(lineVo.getLname(),getSessionUserName().getEntid());
				if(lname>0){
					 j.setMsg("名称：【"+lineVo.getLname()+"】已存在,不能重复添加!");
					 setOperstatus(1);
				}else{
					lineVo.setEntid(getSessionUserName().getEntid());
					inspectItemService.addLine(lineVo);
					j.setMsg("添加成功！");
					j.setSuccess(true);
				}
			}else{
				j.setMsg("请选择线路巡检点！");
				j.setSuccess(false);
			}
		}catch(Exception e){
			setOperstatus(1);
			j.setMsg("添加失败！");
			logger.error(ExceptionUtil.getExceptionMessage(e));
		}
		writeJson(j);
	}
	
	@LogAnnotation(event="修改巡检线路",tablename="t_line")
	public void editLine(){
		Json j=new Json();
		try{
			//if(lineVo.getLpIds()!=null&&lineVo.getLpIds().length()>0){
				inspectItemService.editLine(lineVo);
				j.setMsg("修改成功！");
				j.setSuccess(true);
			/*}else{
				j.setMsg("请选择线路巡检点！");
				j.setSuccess(false);
			}*/
		}catch(Exception e){
			setOperstatus(1);
			j.setMsg("修改失败！");
			logger.error(ExceptionUtil.getExceptionMessage(e));
		}
		writeJson(j);
	}
	@LogAnnotation(event="删除巡检线路",tablename="t_line")
	public void deleteLine(){
		Json j=new Json();
		try{
			if(!StringUtils.isEmpty(lineVo.getIds())){
				//删除线路
				inspectItemService.removeLine(lineVo.getIds());
				int lineid;
				for(String id:lineVo.getIds().split(",")){
					lineid=Integer.parseInt(id);
					//找出线路相关的计划
					List<TPlan>planList= planService.findPlanList(lineid);
					if(planList!=null&&planList.size()>0){
						for(TPlan plan:planList){
							//删除计划和下面的任务
							planService.deletePlan(String.valueOf(plan.getId()));
						}
					}
				}
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
	
	public void isHavePlanAndTask(){
		String ids=getRequest().getParameter("ids");
		boolean flag=false;
		Json j=new Json();
		for(String id:ids.split(",")){
			int lineid=Integer.parseInt(id);
			try{
				flag=inspectItemService.isHavePlanAndTask(lineid);
				if(flag==true){
					break;
				}
			}catch(Exception e){
				flag=false;
			}
			
		}
		j.setSuccess(flag);
		writeJson(j);
	}
	public void uploadFile() throws Exception {
		String realPath = ServletActionContext.getServletContext().getRealPath("webpage/upload");

		if (lineVo.getExcelFile() != null) {
			File uploadfile = new File(new File(realPath),lineVo.getExcelFileFileName());
			if (!uploadfile.getParentFile().exists()) {
				uploadfile.getParentFile().mkdir();
			}
			if (uploadfile.exists()) {
				uploadfile.delete();
			}
			FileUtils.copyFile(lineVo.getExcelFile(),uploadfile);
		}
	}
	@LogAnnotation(event="导入巡检线路",tablename="t_line")
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
				rule.setNametocol("linename,1;pointname,2");	//映射三个字段，
			  Eoip eoip = new Eoip();
			  eoip.setErule(rule);
			  
			  //指定导入的数据库表
			  LineSummaryVo mirror = new LineSummaryVo();  
			//  uploadFile();
			  //获得bean list，excle每行对应一个bean对象
			  db = eoip.excel2db(mirror,lineVo.getExcelFile());
			  //加入到数据库中
			  int entid=getSessionUserName().getEntid();
			String count1=inspectItemService.saveLineList(db,entid);
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
		String  poname=getRequest().getParameter("poname");
		List<PointVo> pvoList = new ArrayList<PointVo>();
		//getPointList
		pvoList=inspectItemService.getPointList(region, poname, "", "a", queryEnterpriseByWhere());
		writeJson(pvoList);
	}
	public void isadmin(){
		Json j=new Json();
		boolean flag=false;
		String ids=getRequest().getParameter("ids");
		flag=baseDao.isadmin(TLine.class,ids,queryEnterpriseByWhere());
		j.setSuccess(flag);
		writeJson(j);
	}
}
