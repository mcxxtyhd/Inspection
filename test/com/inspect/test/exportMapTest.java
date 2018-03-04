package com.inspect.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.inspect.AbstractBaseTransactionalSpringContextTests;
import com.inspect.dao.BaseDaoI;
import com.inspect.model.baseinfo.TBaseInfo;
import com.inspect.model.basis.TEquipment;
import com.inspect.model.basis.TTestCase;
import com.inspect.model.monitor.TInspectItemDetailReport;
import com.inspect.model.monitor.TInspectItemReport;
import com.inspect.service.InspectItemServiceI;
import com.inspect.service.SummaryServiceI;
import com.inspect.util.common.StringUtils;
import com.inspect.util.excel.Eoip;
import com.inspect.util.excel.Eoiprule;
import com.inspect.vo.basis.EquipmentVo;
import com.inspect.vo.summary.SummaryFormVo;


public class exportMapTest extends AbstractBaseTransactionalSpringContextTests {

	@Autowired
	private BaseDaoI baseDao;
	
	@Resource
	private SummaryServiceI summaryService;
	

	public SummaryServiceI getSummaryService() {
		return summaryService;
	}

	@Resource
	public void setSummaryService(SummaryServiceI summaryService) {
		this.summaryService = summaryService;
	}


	

	public List<Object> db2list(){ 
		List<Object> queryResult = new ArrayList<Object>();
		String hql = "from TTestCase where 1=1";
		queryResult = baseDao.find(hql);
		
		return queryResult;
		
	}

	@Test
	public void testexport() throws SecurityException, IllegalArgumentException, IOException, NoSuchFieldException, IllegalAccessException{
		List<Object> db = new ArrayList<Object>();
		List<String> listfile = new ArrayList<String>();
		
		//date prepare:
		db = db2list();
		
		//导出规则设置
		 Eoiprule rule = new Eoiprule();
		 //绝对模式：
		 rule.setExpd2e("iunumber,c1;iuname,g1;idesc,c2;iresult,C6;idesc2,b11;iresult2,D11");
		 rule.setExpfilename("E:\\temp\\testpoi\\testcase_");
		 rule.setExptemplatefile("E:\\temp\\testpoi\\testcase.xls");


		 String specialfields = "C6,cm;D11,cm";
		 rule.setSpecialFields(specialfields);

 
		 //绝对模式:true;列模式：false
		 Boolean mode = true;
		 rule.setExpmode(mode);
		 rule.setExppostfixrule("name,iunumber");
		 rule.setExpnumsheetperwb(20);


		 Eoip eoip = new Eoip();
		 eoip.setErule(rule);
		 
		 listfile = eoip.db2excel(db);
		 

		
	}
	
	public void testimport() throws Exception{
		List<Object> db = new ArrayList<Object>();
		
		  //得到源数据文件
		  File file = new File("E:\\temp\\testpoi\\规划管理系统测试用例.xls");
		  if(!file.exists()){
		   throw new Exception("文件路径不存在！");
		  }
		  
		//规则设置
		 Eoiprule rule = new Eoiprule();
		 
		 rule.setSheetnumber((short)2);
		 rule.setRowcontentspos((short)1);
		 String nametocol = new String("iunumber,3;iuname,8;idesc,9;iresult,4;idesc2,5;iresult2,4");
		 rule.setNametocol(nametocol);
		 
		 Eoip eoip = new Eoip();
		 eoip.setErule(rule);
		 
		 TTestCase testcase = new TTestCase();
		 
		 db = eoip.excel2db(testcase, file);
		 
		  //加入到数据库中
		  for(int i=0;i<db.size();i++){
			  baseDao.save(db.get(i));
		  }
		 

	}

	public void testEoipExport() throws Exception{
		List<Object> db = new ArrayList<Object>();
		List<String> listfile = new ArrayList<String>();
		
		//date prepare:
		db = listprepare();
		
		//导出规则设置
		 Eoiprule rule = new Eoiprule();
		 
		 /* 列模式：
		 rule.setExpd2e("xequtnum,b5;xequid,c5;xequtype,j5");
		 rule.setExpfilename("E:\\temp\\testpoi\\equipment_.xls");
		 rule.setExptemplatefile("E:\\temp\\testpoi\\铁塔、天馈线维护台账（最新）.xls");
		 rule.setExpstartcol(4);

		 //枚举型数据设置
		 Map<String,String> enummap = new HashMap<String,String>();
		 enummap.put("pointid", "0,abc;1,def;2,ghk");
		 enummap.put("beqcount", "yyy,□是    □否;ooo,女");
		 //rule.setExpenumstr(enummap);
		 
		 //set type:
		 Map<String,String> setdemap = new HashMap<String,String>();
		 setdemap.put("com.inspect.model.monitor.TInspectItemDetailReport", "inspectreportdetailmsgs");
		 //如果
		 //rule.setSetdemap(setdemap);
		 String setfieldmap = "温度 ,m10;湿度,n11;高度,o12";
		 String setfieldvalue = "xproname,xvalue";

		 rule.setSetfieldmap(setfieldmap);
		 rule.setSetfieldvalue(setfieldvalue);
		 
		 //绝对模式:true;列模式：false
		 Boolean mode = false;
		 rule.setExpmode(mode);
		 rule.setExppostfixrule("name,xequtnum");
		 rule.setExpnumsheetperwb(3);

		  */
		 
		 //绝对模式：
		 rule.setExpd2e("xequtnum,d3;xequid,l3;xequtype,j3");
		 rule.setExpfilename("E:\\temp\\testpoi\\equipment_");
		 rule.setExptemplatefile("E:\\temp\\testpoi\\铁塔、天馈线系统检查维护记录-单页模板.xls");
		 rule.setExpstartcol(4);

		 //枚举型数据设置
		 Map<String,String> enummap = new HashMap<String,String>();
		 enummap.put("pointid", "0,abc;1,def;2,ghk");
		 enummap.put("温度", "男男,□是;女女,□否");
		 rule.setExpenumstr(enummap);
		 
		 //set type:
		 Map<String,String> setdemap = new HashMap<String,String>();
		 setdemap.put("com.inspect.model.monitor.TInspectItemDetailReport", "inspectreportdetailmsgs");
		 //如果
		 rule.setSetdemap(setdemap);
		 String setfieldmap = "调整前测量结果偏南 ,AB14;湿度,d14;高度,d15";
		 String setfieldvalue = "xproname,xvalue";
		 
		 String specialfields = "AB14,cm";
		 rule.setSpecialFields(specialfields);

		 rule.setSetfieldmap(setfieldmap);
		 rule.setSetfieldvalue(setfieldvalue);
		 
		 //绝对模式:true;列模式：false
		 Boolean mode = true;
		 rule.setExpmode(mode);
		 rule.setExppostfixrule("name,xequtnum");
		 rule.setExpnumsheetperwb(1);


		 Eoip eoip = new Eoip();
		 eoip.setErule(rule);
		 
		 listfile = eoip.db2excel(db);
		 

	}
	
	public List<Object> listprepare() 
	{
		
		List<Object> queryResult = new ArrayList<Object>();
		
		for(int i=0;i<5;i++){
			TInspectItemReport te= new TInspectItemReport();
			te.setXequtnum("铁塔" + i);
			te.setXequtype("铁塔");
			te.setXequid(i*1000+i);
			List<TInspectItemDetailReport> datailsSet= new ArrayList<TInspectItemDetailReport> ();
			for(int k=0;k<3;k++){
				TInspectItemDetailReport iidr = new TInspectItemDetailReport();
				iidr.setXprogid(1);
				if(k==0) iidr.setXproname("调整前测量结果偏南");
				if(k==1) iidr.setXproname("湿度");
				if(k==2) iidr.setXproname("高度");
				String v = null;
				if(i%2 == 0) v = "12"; else v = "35";
				iidr.setXvalue(v);
				datailsSet.add(iidr);
			}
			te.setInspectreportdetailmsgs(datailsSet);
			queryResult.add((Object)te);
		}
		
		/*
		int startRow ;
		String sql="from TInspectItemReport where 1=1 ";

    	List<Object> queryResult=baseDao.find(sql);
				if (queryResult != null && queryResult.size() > 0) {
					for (int i=0;i<queryResult.size();i++) {
						//设备信息
						TInspectItemReport te= (TInspectItemReport)queryResult.get(i);
						TEquipment equipment=new TEquipment();
						//获取设备信息
						equipment=baseDao.getEntityById(TEquipment.class,te.getXequid());
						if(equipment!=null){
							//因为设备信息与基础信息一对一关联，所以通过设备信息找到基础信息
							List<TBaseInfo> bInfoList=baseDao.find("from TBaseInfo where bnumber='"+te.getXequtnum()+"'");
							TBaseInfo bInfo=null;
							if(bInfoList.size() > 0){
								//将基础信息放入excel表中
								bInfo=bInfoList.get(0);
							}
							
						}
						Set<TInspectItemDetailReport> datailsSet= new HashSet<TInspectItemDetailReport> ();
						for(int k=0;k<3;k++){
							TInspectItemDetailReport iidr = new TInspectItemDetailReport();
							iidr.setXprogid(1);
							if(k==0) iidr.setXproname("馈线窗处密封情况良好");
							if(k==1) iidr.setXproname("馈线接地线固定符合规范");
							if(k==2) iidr.setXproname("馈线窗处接地系统符合安装规范");
							
							iidr.setXvalue(te.getXequtnum()+"x" +k);
							datailsSet.add(iidr);
						}
						 te.setInspectreportdetailmsgs(datailsSet);
						 
						 System.out.println("ddd");

						 }
						
					}
			*/
		return queryResult;
	}
	
}
