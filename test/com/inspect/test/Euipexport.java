package com.inspect.test;

import java.io.File;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.struts2.ServletActionContext;
import org.hibernate.SQLQuery;
import org.junit.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.inspect.AbstractBaseTransactionalSpringContextTests;
import com.inspect.annotation.LogAnnotation;
import com.inspect.dao.BaseDaoI;
import com.inspect.model.baseinfo.TBaseInfo;
import com.inspect.model.basis.TEnumParameter;
import com.inspect.model.basis.TEquipment;
import com.inspect.model.basis.TInspectUser;
import com.inspect.model.basis.TProject;
import com.inspect.model.basis.TProjectGroup;

import com.inspect.model.monitor.TInspectItemDetailReport;
import com.inspect.model.monitor.TInspectItemReport;
import com.inspect.service.InspectItemServiceI;
import com.inspect.service.ProblemServiceI;
import com.inspect.service.SummaryServiceI;
import com.inspect.util.common.DateUtils;
import com.inspect.util.common.StringUtils;
import com.inspect.util.excel.Eoip;
import com.inspect.util.excel.Eoiprule;
import com.inspect.vo.basis.EquipmentVo;
import com.inspect.vo.basis.TermEnumParameterVo;
import com.inspect.vo.basis.TermProjectGroupVo;
import com.inspect.vo.basis.TermProjectVo;
import com.inspect.vo.comon.Json;
import com.inspect.vo.problem.ProblemVo;
import com.inspect.vo.problem.RepairTaskVo;
import com.inspect.vo.summary.SummaryFormVo;


public class Euipexport extends AbstractBaseTransactionalSpringContextTests {

	@Autowired
	private BaseDaoI baseDao;
	
	@Resource
	private SummaryServiceI summaryService;
	
	@Resource
	private ProblemServiceI problemService;
	@Resource
	private InspectItemServiceI inspectItemService;
	
	public SummaryServiceI getSummaryService() {
		return summaryService;
	}

	@Resource
	public void setSummaryService(SummaryServiceI summaryService) {
		this.summaryService = summaryService;
	}
	//@Test
	public  void intergetpvalue() {
		Map<String, Object> map = new HashMap<String, Object>();
		List<TermProjectGroupVo> tpGroupList1 = new ArrayList<TermProjectGroupVo>();
		try {
			String eid = "43605";
			String taskid ="515";
			String bid = "102567";
			//从detail表中获取已经提交的巡检项的信息
			List<TermProjectVo> plist=new ArrayList();
			StringBuffer buf1=new StringBuffer();
			buf1.append(" SELECT b.xproid,b.xvalue FROM t_report_message  a LEFT JOIN t_report_message_detail b ON(a.id=b.msgid) WHERE  a.xequid="+bid+" AND a.xtaskid="+taskid);
			SQLQuery query1 = baseDao.getHibernaSession().createSQLQuery(buf1.toString());
			List<Object[]> pglist1 = query1.list();
			Map<Integer,String > map1=new HashMap<Integer,String >();
			if(pglist1!=null&&pglist1.size()>0){
				for (int i = 0; i < pglist1.size(); i++) {
					Object[] obj=pglist1.get(i);
					map1.put((Integer)obj[0], (String)obj[1]);//将提交的巡检项id和值放入map中
				}
			}
			//从巡检项表中获取巡检项信息
			StringBuffer buf=new StringBuffer();
			buf.append(" select pgid from t_equipment_project_group where eid="+eid);
			SQLQuery query = baseDao.getHibernaSession().createSQLQuery(buf.toString());
			List<Object[]> pglist = query.list();
			Integer gid=0;
			if(pglist!=null&&pglist.size()>0){
				for (int i = 0; i < pglist.size(); i++) {
					Object gproject=pglist.get(i);
					gid=(Integer) gproject;
					TProjectGroup tpGroup = new TProjectGroup();
					TermProjectGroupVo tpGroupVo = new TermProjectGroupVo();
					tpGroup = inspectItemService.getProjectGroup(gid);
					BeanUtils.copyProperties(tpGroup, tpGroupVo);
		
					if (tpGroup.getTprojects() != null
							&& tpGroup.getTprojects().size() > 0) {
						for (TProject project : tpGroup.getTprojects()) {
							TermProjectVo tprojectVo = new TermProjectVo();
							BeanUtils.copyProperties(project, tprojectVo);
							String pvalue=map1.get(tprojectVo.getId());
							if(pvalue!=null){
								tprojectVo.setPvalue(pvalue);
							}
							// 如果巡检项类型为1，则表示为枚举值，那必须获取枚举值对应的内容
							if (tprojectVo.getPtype().equals("1")) {
								List<TEnumParameter> evList = null;
								List<TermEnumParameterVo> evList1 = new ArrayList<TermEnumParameterVo>();
								evList = inspectItemService
										.getEnumParameListByParameName(" 1=1 ",
												tprojectVo.getPenumvalue());
								if (evList != null) {
									for (TEnumParameter enums : evList) {
										TermEnumParameterVo teParameter = new TermEnumParameterVo();
										BeanUtils.copyProperties(enums,
												teParameter);
										evList1.add(teParameter);
									}
								}
								if (evList != null) {
									tprojectVo.setEvList(evList1);
								}
							}
							tpGroupVo.getTprojectList().add(tprojectVo);
						}
						com.inspect.vo.basis.ComparatorProject comparator = new com.inspect.vo.basis.ComparatorProject();
						Collections.sort(tpGroupVo.getTprojectList(),
								comparator);
		
					}
					tpGroupList1.add(tpGroupVo);
			}
		}	
		
			
			
	}catch (Exception e) {
		// TODO: handle exception
		e.printStackTrace();
	}		
	map.put("tpGroupList1", tpGroupList1);
	writeJson(map);
	}
	
	public void writeJson(Object object) {
		try {
			String json = JSON.toJSONStringWithDateFormat(object, "yyyy-MM-dd HH:mm:ss");
			System.out.println(json);
			ServletActionContext.getResponse().setContentType("text/html;charset=utf-8");
			ServletActionContext.getResponse().getWriter().write(json);
			ServletActionContext.getResponse().getWriter().flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//@Test
	public void setcity(){
	List<TBaseInfo> l=	(List) baseDao.find("select b.bcity,b.bregion, b.id  from TBaseInfo b ,TInspectItemReport a where b.id=a.xequid ");
	System.out.println("22222222222222222222222222222="+l.size());
	for(TBaseInfo b:l){
		System.out.println(b.getId()+"\t"+b.getBcity()+"\t "+b.getBregion());
	}
	}
	/**
	 * 巡检问题导出
	 */
//巡检问题导出@Test
	public void problemtoExcel(){

		try{
			ProblemVo problemVo=new ProblemVo();
			System.out.println("212");
			String filePath= problemService.problemtoexcel(problemVo," 1=1 ",0);
			
			System.out.println("dddd"+filePath);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
//抢修任务导出   @Test
public void repairtasktoExcel(){

	try{
		RepairTaskVo repairTaskVo=new RepairTaskVo();
		System.out.println("212");
		String filePath= problemService.repairTaskVotoexcel(repairTaskVo," 1=1 ");
		
		System.out.println("dddd"+filePath);
	}catch(Exception e){
		e.printStackTrace();
	}
}

//统计导出测试@Test
public void summarytoExcel() throws UnsupportedEncodingException{
		String flag ="1";
		SummaryFormVo summaryFormvo=new SummaryFormVo();
	if(!StringUtils.isNotEmpty("bcity")){
		String bcity="bcity";
		String bcity1=new String(bcity.getBytes("ISO-8859-1"),"utf-8");
		summaryFormvo.setBcity(bcity1);
	}
	
		String btype ="1";// getRequest().getParameter("btype");//表示设备类型 铁塔和室内
		System.out.println(summaryFormvo.getEntid());
		//获取excel数据表的配置内容，1表示获取表里面全部内容
		HashMap<String, Integer> sconfigMap=null;// =summaryService.getSummaryConfigMap("", 1,sConfigFlag);
	//	String filePath = summaryService.allSummary1("",summaryFormvo,btype," 1=1 ",sconfigMap);
		//System.out.println("dd"+filePath);

}
//@Test
public void getids(){
	SummaryFormVo sfVo=new SummaryFormVo();
	//获取符合条件的base_info的主键id集合
	String ids=	summaryService.getbids(sfVo, " 1=1 ");
	
	System.out.println(ids);
}

	public static void main(String[] args) {
		Euipexport d=new Euipexport();
	//	d.problemtoExcel();
	}
	
	
	@Test
	public void updateUserRealname(){/*
		List<TInspectUser>list=baseDao.find(" from TInspectUser");
		if(list!=null&&list.size()>0){
			for(TInspectUser user:list){
				user.setIrealname(user.getIuname());
				baseDao.save(user);
			}
	}
	*/
	System.out.println("ddd");	
	}

		
}
