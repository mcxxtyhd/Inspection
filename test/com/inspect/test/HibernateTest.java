package com.inspect.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.inspect.AbstractBaseTransactionalSpringContextTests;
import com.inspect.dao.BaseDaoI;
import com.inspect.model.system.Enterprise;
import com.inspect.util.excel.Eoip;
import com.inspect.util.excel.Eoiprule;
import com.inspect.vo.basis.EquipmentVo;
import com.inspect.vo.basis.PlanQueryVo;


public class HibernateTest extends AbstractBaseTransactionalSpringContextTests {

	@Autowired
	private BaseDaoI baseDao;
	
	@Test
	public void testSave(){
		
//		User u=new User();
//		u.setName("aaaaaaaaa");
//		baseDao.save(u);
		
	}
//	@Test 
	public void testEoipExport() throws Exception{
		List<Object> db = new ArrayList<Object>();
		List<String> listfile = new ArrayList<String>();
		
		//测试数据初始化
		for(int i=0;i<9;i++){
		  EquipmentVo vo = new EquipmentVo();
		  if(i/2 ==0)
			  vo.setBeqcount("ooo");
		  else
			  vo.setBeqcount("yyy");
		  vo.setPointid(i);
		  vo.setEposx(23.22+i);
		  vo.setEaddress(i+ "address");
		
		  db.add(vo);
		}
		  
		//导出规则设置
		 Eoiprule rule = new Eoiprule();
		 rule.setExpd2e("beqcount,B3;pointid,c2;eposx,d5;eaddress,a2");
		 rule.setExpfilename("E:\\temp\\testpoi\\equipment_");
		 rule.setExptemplatefile("E:\\temp\\testpoi\\equipment_template_abs.xls");
		 rule.setExpstartcol(3);

		 //枚举型数据设置
		 Map<String,String> enummap = new HashMap<String,String>();
		 enummap.put("pointid", "0,abc;1,def;2,ghk");
		 enummap.put("beqcount", "yyy,□是    □否;ooo,女");
		 
		 rule.setExpenumstr(enummap);
		 
		 Boolean mode = true;
		 rule.setExpmode(mode);
		 rule.setExppostfixrule("name,eaddress");
		 rule.setExpnumsheetperwb(3);

		 Eoip eoip = new Eoip();
		 eoip.setErule(rule);
		 
		 listfile = eoip.db2excel(db);
		 

	}
//	@Test
	public void test(){
			String sql="  SELECT COUNT(*) FROM t_plan_task a "+ 
			"  LEFT JOIN t_line b ON (a.plineid=b.id) "+ 
			"  LEFT JOIN t_line_point c ON (b.id=c.lineid) "+ 
			"  LEFT JOIN t_point d ON (c.pointid=d.id) "+ 
			"  LEFT JOIN t_point_equipment e ON (d.id=e.pointid) "+ 
			"  WHERE a.entid="+13+" AND a.pstartdate<'2014-10'AND a.pstartdate>'2014-05' ";
			System.out.println(sql);
			SQLQuery query = baseDao.getHibernaSession().createSQLQuery(sql);
			List<Object[]> list=null;
			Object object = null;
			try {
				 list = query.list();
				 if(list!=null&&list.size()>0){
					  object = (Object) list.get(0);
					// Integer pggroup = (Integer) object[0];
				 }
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			System.out.println(object);
	}
  
	@Test
	public void test1(){
		int piclimit=13;
		Map<Integer, String> mapent = new HashMap<Integer, String>();
		
	//mapent 用于保存单位id和任务编号
		mapent.put(13,"180,181,182,183,184,185,186,187");
		//下面是为了获取计划巡检设备的数量
		Iterator iter=mapent.entrySet().iterator();
		List<PlanQueryVo> evolist=new ArrayList<PlanQueryVo>();
			if(iter.hasNext()){
				Map.Entry enter=(Map.Entry) iter.next();
				int entid=(Integer) enter.getKey();
				String taskids=(String) enter.getValue();
				/*String sql="  SELECT COUNT(*) FROM t_plan_task a "+ 
				"  LEFT JOIN t_line b ON (a.plineid=b.id) "+ 
				"  LEFT JOIN t_line_point c ON (b.id=c.lineid) "+ 
				"  LEFT JOIN t_point d ON (c.pointid=d.id) "+ 
				"  LEFT JOIN t_point_equipment e ON (d.id=e.pointid) "+ 
				"  WHERE a.entid="+entid+" and  a.id in ("+taskids+")";
				System.out.println(sql);
				SQLQuery query = baseDao.getHibernaSession().createSQLQuery(sql);
				List<Object[]> list=null;
				Object object = null;
				try {
					 list = query.list();
					 if(list!=null&&list.size()>0){
						  object = (Object) list.get(0);
						// Integer pggroup = (Integer) object[0];
					 }
					 else{
						 object=0;
					 }	 
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}*/
				Integer plansum=2222;//计划巡检设备的数量
				
				
			//下面是为了获取图片异常个数的数量及已巡检的设备的数量
			HashMap<Integer,PlanQueryVo> mapvo=new HashMap<Integer,PlanQueryVo>();
		
				String sql1=" select ximgname FROM t_report_message  where xtaskid in ("+taskids +") and xequtype=1";
				SQLQuery query1 = baseDao.getHibernaSession().createSQLQuery(sql1);
				List<Object> list1=null;
				Object object1 = null;
				Integer querycount=null;//已经巡检的设备数量
				int yichang=0;//图片异常设备数量
				try {
					 list1 = query1.list();
					 if(list1!=null&&list1.size()>0){
						 querycount=list1.size();
						 for(int i=0;i<list1.size();i++){
								int picnum=0;
								Object obj=list1.get(i);
								//铁塔类型
								String imagename=(String) obj;
								if(imagename!=null){
									picnum=imagename.split(",").length;
								}
								else{
									picnum=0;
								}	
								//大于0，表示图片没有上传完成,即异常
								if((piclimit-picnum)>0){
									yichang++;
								}
						}
					}
					 else{
						 querycount=0;
					 }
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				
				//下面是为了获取公司名称
				String entname=baseDao.getEntityById(Enterprise.class,entid)!=null?baseDao.getEntityById(Enterprise.class,entid).getEntname():"  ";
					//问题上报数量
					long errorcount=baseDao.count("select count(*) from TInspectProblem t where t.proitaskid in ("+taskids +")");
					//未巡检的设备数量
					int unquerycount=plansum-querycount;
					//获取某单位下设备总数量
					long sum=baseDao.count("select count(*) from TEquipment t where  t.etype=1 and t.entid="+entid);
			{	
					//汇总所有的信息
				PlanQueryVo evo=new PlanQueryVo();
				evo.setPointCount(plansum);  //巡检点个数//巡检设备总数
				evo.setQueryCount(querycount);  //已寻点个数
				evo.setUnqueryCount(plansum-querycount);//未寻点个数
				evo.setErrqueryCount((int)errorcount); //有问题个数
				evo.setPicexcepnum(yichang);  //图片异常个数
				evo.setEntid(entid);//所属单位id
				evo.setEsum(3*(int)sum);//设备总数量
				evo.setEntname(entname);//所属单位名称
				//小数点后保留两位数
				//DecimalFormat df2 = new DecimalFormat("###.00");
				//计划率
				double planrate=0;
				if(evo.getEsum()>0){
				 planrate=(double)(Math.round(((evo.getPointCount()*10000)/(double)evo.getEsum()))/100.0);
					if(planrate>=0&&planrate<=100){
						evo.setPlanrate(String.valueOf(planrate)+"%");
					}
					else{
						evo.setPicrate("空");
					}
				}else
				{
					evo.setPlanrate("空");
				}
				
				
				//巡检合格率
				double picrate;
				if(evo.getPointCount()>0){
					picrate=(double)(Math.round(((evo.getPicexcepnum())*10000/(double)evo.getQueryCount()))/100.0);
					//判断，如果没有，则rate>=0且rate<=100
					if(picrate>=0&&picrate<=100){
						evo.setPicrate(String.valueOf(picrate)+"%");
					}
					else{
						evo.setPicrate("空");
					}
				}else
				{
					evo.setPicrate("空");
				}
				
				
				//完成率
				double rate=0;
				//判断巡检线路里是否有设备
				if(evo.getPointCount()>0){
					rate=(double)(Math.round((evo.getQueryCount()*10000/(double)evo.getPointCount()))/100.0);
						;
					//判断巡检线路中的设备是否被删除过，如果没有，则rate>=0且rate<=100
					if(rate>=0&&rate<=100){
						evo.setRate(String.valueOf(rate)+"%");
						evo.setComrate(rate);
					}
					else{
						evo.setPicrate("空");
						evo.setComrate(0.00);
					}
					evo.setComrate(rate);
				}else
				{
					evo.setRate("空");
					evo.setComrate(100.00);
				}
				evolist.add(evo);
			}
		}
		com.inspect.vo.basis.ComparatorPlanQueryVo comparator = new com.inspect.vo.basis.ComparatorPlanQueryVo();
		Collections.sort(evolist,comparator);
		for(PlanQueryVo v:evolist){
			System.out.println("总数量："+v.getEsum());
			System.out.println("计划数量："+v.getPointCount());
			System.out.println("计划率："+v.getPlanrate());
			System.out.println("已巡检数量："+v.getQueryCount());
			System.out.println("未巡检数量："+v.getUnqueryCount());
			System.out.println("图片异常个数："+v.getPicexcepnum());
			System.out.println("问题上报数量："+v.getErrqueryCount());
			System.out.println("完成率："+v.getRate());
			System.out.println("合格率："+v.getPicrate());
			System.out.println("所属单位："+v.getEntname());
		}
	}
}
