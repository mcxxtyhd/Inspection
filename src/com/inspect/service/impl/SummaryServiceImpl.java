package com.inspect.service.impl;

import java.awt.Button;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.persistence.Transient;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.tools.ant.types.CommandlineJava.SysProperties;
import org.hibernate.SQLQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.inspect.constant.Constant;
import com.inspect.dao.BaseDaoI;
import com.inspect.model.baseinfo.TBaseInfo;
import com.inspect.model.baseinfo.TBaseInfoEquipment;
import com.inspect.model.basis.TEnumParameter;
import com.inspect.model.basis.TEquipment;
import com.inspect.model.basis.TEquipmentProjectGroup;
import com.inspect.model.basis.TGroup;
import com.inspect.model.basis.TLine;
import com.inspect.model.basis.TLinePoint;
import com.inspect.model.basis.TPlanTask;
import com.inspect.model.basis.TPoint;
import com.inspect.model.basis.TPointEquipment;
import com.inspect.model.basis.TProject;
import com.inspect.model.basis.TProjectGroup;
import com.inspect.model.basis.TSummaryConfig;
import com.inspect.model.monitor.TInspectItemDetailReport;
import com.inspect.model.monitor.TInspectItemReport;
import com.inspect.service.InspectItemServiceI;
import com.inspect.service.InspectMonitorServiceI;
import com.inspect.service.SummaryServiceI;
import com.inspect.util.ResBundleUtil;
import com.inspect.util.beanutil.MyBeanUtils;
import com.inspect.util.common.QueryResult;
import com.inspect.util.common.StringUtils;
import com.inspect.util.excel.Eoip;
import com.inspect.util.excel.Eoiprule;
import com.inspect.vo.basis.SummaryConfigVo;
import com.inspect.vo.basis.TermEnumParameterVo;
import com.inspect.vo.basis.TermEquipmentVo;
import com.inspect.vo.basis.TermPlanVo;
import com.inspect.vo.basis.TermPointVo;
import com.inspect.vo.basis.TermProjectGroupVo;
import com.inspect.vo.basis.TermProjectVo;
import com.inspect.vo.summary.SummaryFormVo;
import com.itextpdf.text.log.SysoCounter;
import com.itextpdf.text.pdf.PdfStructTreeController.returnType;
@Service("summaryService")
public class SummaryServiceImpl implements SummaryServiceI {
	@Resource
	private BaseDaoI baseDao;
	@Resource
	private InspectItemServiceI inspectItemService;
	@Resource
	private  InspectMonitorServiceI inspectMonitorService;
	/***总帐表导出（new）
	 * 实现思路：根据总表的数据，将sheet名称为键，sheet id为值放进map,然后根据得到的ID,
	 * @return            		返回导出数据之后的临时文件地址
	 * @author liao
	 * @throws IllegalAccessException 
	 * @throws NoSuchFieldException 
	 * @throws IOException 
	 * @throws IllegalArgumentException 
	 * @throws SecurityException 
	 */
	@SuppressWarnings("unchecked")
	public String allSummary1(String ids,SummaryFormVo summaryFormvo,String btype,int entid,HashMap<String, Integer> summaryMap)
	{		String sql=" ";
			StringBuffer buf = new StringBuffer(" 1=1 ");
		 if (summaryFormvo.getItaskid()!=0) {//
				buf.append(" and a.xtaskid =").append(summaryFormvo.getItaskid());
			}
		
		 if (summaryFormvo.getXgid()!=0) {
				buf.append(" and a.xgid =").append(summaryFormvo.getXgid());
			}
	
		if (StringUtils.isNotEmpty(summaryFormvo.getRpsdate())) {
			buf.append(" and a.xreptime >='").append(summaryFormvo.getRpsdate()).append("'");
		}
		if (StringUtils.isNotEmpty(summaryFormvo.getRpedate())) {
			buf.append(" and a.xreptime <='").append(summaryFormvo.getRpedate()).append("'");
		}

		
		//针对设备的查询条件
		StringBuffer buf1=new StringBuffer();
		{
			if(entid==0){
				buf1.append("  1=1 ");
			}else{
				buf1.append(" b.entid in (0,").append(entid).append(")");
			}
			if (summaryFormvo.getEntid()!=0) {//////
				buf1.append(" and b.entid =").append(summaryFormvo.getEntid());
			}
			if (StringUtils.isNotEmpty(summaryFormvo.getEcity())) {
				buf1.append(" and b.bcity like '%").append(summaryFormvo.getEcity().trim()).append("%'");
			}
			if (StringUtils.isNotEmpty(summaryFormvo.getEregion())) {
				buf1.append(" and b.bregion like '%").append(summaryFormvo.getEregion().trim()).append("%'");
			}
			 if (StringUtils.isNotEmpty(summaryFormvo.getXequtnum())) {
					buf1.append(" and b.bnumber like '%").append(summaryFormvo.getXequtnum().trim()).append("%'");
			}
			//btype为2表示室内 ；1表示铁塔
			 if (StringUtils.isNotEmpty(summaryFormvo.getBtype())) {
				buf1.append(" and b.btype ='").append(summaryFormvo.getBtype().trim()).append("'");
			}
		}
	
	/////--巡检周期没做
		
		sql = "SELECT   b.id,b.entid,b.baddress,b.bdesc,b.beqcount,b.bfactory,b.blevel,b.bname,b.bnumber,b.bposx,b.bposy,b.bregion,b.btower,b.btype,b.bfixcycle,b.btowertype,b.bcity,b.bwlnumber,b.bheight," +
				"a.id as ids,a.entid,a.xequid,a.xequtnum,a.xequtype,a.xgid,a.ximgname,a.ximgurl,a.xlid,a.xpid,a.xreptime,a.xstatus,a.xtaskid,a.xterid,a.xuid" +
				", c.entname , c.entname  FROM t_base_info b LEFT JOIN t_report_message a ON(a.xequid=b.id and " +buf.toString()+" ) LEFT JOIN t_s_enterprise c ON (a.entid=c.id) " +
				" where "+buf1.toString();
			System.out.println(sql);
		long a=System.currentTimeMillis();
		SQLQuery query = baseDao.getHibernaSession().createSQLQuery(sql);
		
		//获取符合条件的message
		List<Object[]> rowlist = query.list();
		System.out.println("执行左连接查询耗时 : "+(System.currentTimeMillis()-a)/1000f+" 秒 ");
		//将结果封装到listresult中
		List<Object> listresult= list2object1(rowlist);
			if(listresult==null){
				return null;
			}
		Iterator itr = listresult.iterator();
		String bname="";
		int index=1;
		long a1=System.currentTimeMillis();
		this.getMessageDetail(listresult);
		while (itr.hasNext()) {
			TInspectItemReport te = (TInspectItemReport)itr.next();
			te.setXimgurl(String.valueOf(index));
			index++;
		}
		//while循环作用：通过引用传递将TInspectItemDetailReport的值赋给TInspectItemReport，并过滤掉重复的巡检项，只记录最后一次提交的结果
		/*while (itr.hasNext()) {
			TInspectItemReport te = (TInspectItemReport)itr.next();
			te.setXimgurl(String.valueOf(index));
			if("4413".equals(te.getXequtnum())){
				//	System.out.println("ddddd");
			}
			index++;
			 bname=te.bname;//设备名称
			 //	 System.out.println(te.getId());//te.getId()为某次任某个设备在message表中的主键id
			 if(te.getId()!=0){
					String sqldetail="from TInspectItemDetailReport where msgid =" + te.getId() + " order by msgid, id";
					List<TInspectItemDetailReport> detaillist=baseDao.find(sqldetail);
					
					//重复数据的处理：按照id的大小判断
					//得到每个不同巡检项的最新值列表
					Map<String,TInspectItemDetailReport> newest = new HashMap<String,TInspectItemDetailReport>();
					for(int i=0;i< detaillist.size();i++){
						TInspectItemDetailReport t = detaillist.get(i);
						String item = t.getXproname();
						if(newest.containsKey(item)){
							if(newest.get(item).getId() < t.getId()){
								newest.remove(item);
								newest.put(item, t);
							}
						}
						else{
							newest.put(item, t);
						}	
					}
					List<TInspectItemDetailReport> detailsingle= new ArrayList<TInspectItemDetailReport>();
					Collection vs=newest.values(); 
					Iterator it=vs.iterator(); 
					while(it.hasNext()){ 
						detailsingle.add((TInspectItemDetailReport)it.next());
					}
					te.setInspectreportdetailmsgs(detailsingle);
			 }
		}			
		*/	System.out.println("执行detail耗时 : "+(System.currentTimeMillis()-a1)/1000f+" 秒 ");
		List<String> url=null;
		try{
			int flag=0;
			try {
				long a2=System.currentTimeMillis();
				url=eoipexport(listresult,btype,flag,bname);
				System.out.println("执行excel导出耗时 : "+(System.currentTimeMillis()-a2)/1000f+" 秒 ");
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		
			//	System.out.println( url.get(0));
			return url.get(0);
		
		}
		catch(Exception e){
			//e.printStackTrace();
		}
		
		return null;
	}
	/**
	 * 将detail表内容放入message中
	 * @param listresult
	 */
	public void getMessageDetail(List<Object> listresult){
		Map<Integer,TInspectItemReport> map=new HashMap<Integer,TInspectItemReport>();
		StringBuffer ids1=new StringBuffer("");
		String ids2="";
		if(listresult!=null&&listresult.size()>0){
			for(int i=0;i<listresult.size();i++){
				TInspectItemReport te=(TInspectItemReport) listresult.get(i);
				if(te.getId()!=0){
					ids1.append(te.getId()).append(",");
					map.put(te.getId(), te);//将TInspectItemReport内容放入map，方便在后面detail中通过msgid直接定位到massage
				
				}
			}
			 ids2=ids1.substring(0,ids1.length()-1);
			System.out.println(ids2);
		}
			
		if(ids2.length()>0){
			String sqldetail="select xproid,xproname,penumvalue,xvalue ,msgid from t_report_message_detail where msgid in (" +ids2 + ") order by msgid, id";
			System.out.println(sqldetail);
			SQLQuery query = baseDao.getHibernaSession().createSQLQuery(sqldetail);
			List<Object[]> list=query.list();
			if(list!=null&&list.size()>0){
				for(Object[] obj:list){
					TInspectItemDetailReport tedail=new TInspectItemDetailReport();
					tedail.setXproid((Integer)obj[0]);
					tedail.setXproname((String)obj[1]);
					tedail.setPenumvalue((String)obj[2]);
					tedail.setXvalue((String)obj[3]);
					tedail.msgid=(Integer)obj[4];
					map.get(tedail.msgid).getInspectreportdetailmsgs().add(tedail);
				}
				
			}
		}
	}
	
	
	/***总帐表导出（old）
	 * 实现思路：根据总表的数据，将sheet名称为键，sheet id为值放进map,然后根据得到的ID,
	 * @return            		返回导出数据之后的临时文件地址
	 * @author liao
	 * @throws IllegalAccessException 
	 * @throws NoSuchFieldException 
	 * @throws IOException 
	 * @throws IllegalArgumentException 
	 * @throws SecurityException 
	 */
	@SuppressWarnings("unchecked")
	public String allSummary(String templatePath,SummaryFormVo summaryFormvo,String btype,int entid,HashMap<String, Integer> summaryMap)
	{
		String sql=" ";
		StringBuffer buf = new StringBuffer();
		if(entid==0){
			buf.append(" 1=1 ");
		}else{
			buf.append(" a.entid in (0,").append(entid).append(")");
		}
		//entid矛盾
		 if (summaryFormvo.getEntid()!=0) {//////
				buf.append(" and a.entid =").append(summaryFormvo.getEntid());
			}
		 if (summaryFormvo.getItaskid()!=0) {//
				buf.append(" and a.xtaskid =").append(summaryFormvo.getItaskid());
			}
		 if (summaryFormvo.getXuid()!=0) {
				buf.append(" and a.xuid =").append(summaryFormvo.getXuid());
			}
		 if (summaryFormvo.getXgid()!=0) {
				buf.append(" and a.xgid =").append(summaryFormvo.getXgid());
			}
		 if (StringUtils.isNotEmpty(summaryFormvo.getXequtnum())) {
				buf.append(" and xequtnum like '%").append(summaryFormvo.getXequtnum().trim()).append("%'");
			}
		 
		if (StringUtils.isNotEmpty(summaryFormvo.getRpsdate())) {
			buf.append(" and a.xreptime >='").append(summaryFormvo.getRpsdate()).append("'");
		}
		if (StringUtils.isNotEmpty(summaryFormvo.getRpedate())) {
			buf.append(" and a.xreptime <='").append(summaryFormvo.getRpedate()).append("'");
		}
		//btype为2表示室内 ；1表示铁塔
		if (!StringUtils.isEmpty(btype)&&!"0".equals(btype)) {
			buf.append(" and a.xequtype ='").append(btype).append("'");
		}
	/////--巡检周期没做
		
		sql = "SELECT  a.*, b.*, c.entname , c.entname  FROM t_report_message a LEFT JOIN t_base_info b ON(a.xequid=b.id) LEFT JOIN t_s_enterprise c ON (a.entid=c.id) where "+buf.toString();
		//	System.out.println(sql);
		SQLQuery query = baseDao.getHibernaSession().createSQLQuery(sql);

		//获取符合条件的message
		List<Object[]> rowlist = query.list();
		//将结果封装到listresult中
		List<Object> listresult= list2object(rowlist);
			if(listresult==null){
				return null;
			}
		Iterator itr = listresult.iterator();
		String bname="";
		int index=1;
		//while循环作用：通过引用传递将TInspectItemDetailReport的值赋给TInspectItemReport，并过滤掉重复的巡检项，只记录最后一次提交的结果
		while (itr.hasNext()) {
			TInspectItemReport te = (TInspectItemReport)itr.next();
			te.setXimgurl(String.valueOf(index));
			index++;
			 bname=te.bname;//设备名称
			 // System.out.println(te.getId());
			String sqldetail="from TInspectItemDetailReport where msgid =" + te.getId() + " order by msgid, id";
			List<TInspectItemDetailReport> detaillist=baseDao.find(sqldetail);
			
			//重复数据的处理：按照id的大小判断
			//得到每个不同巡检项的最新值列表
			Map<String,TInspectItemDetailReport> newest = new HashMap<String,TInspectItemDetailReport>();
			for(int i=0;i< detaillist.size();i++){
				TInspectItemDetailReport t = detaillist.get(i);
				String item = t.getXproname();
				if(newest.containsKey(item)){
					if(newest.get(item).getId() < t.getId()){
						newest.remove(item);
						newest.put(item, t);
					}
				}
				else{
					newest.put(item, t);
				}	
			}
			List<TInspectItemDetailReport> detailsingle= new ArrayList<TInspectItemDetailReport>();
			Collection vs=newest.values(); 
			Iterator it=vs.iterator(); 
			while(it.hasNext()){ 
				detailsingle.add((TInspectItemDetailReport)it.next());
			}

			te.setInspectreportdetailmsgs(detailsingle);

		}			

		try{
			int flag=0;
			List<String> url=eoipexport(listresult,btype,flag,bname);
			//	System.out.println( url.get(0));
			return url.get(0);
		
		}
		catch(Exception e){
			//e.printStackTrace();
		}
		
		return null;
	}
	
	
	/**
	 * 维护表导出
	 */
	@Override
	public String baseinfoListSummary(String pic,String btype,String btaskid,String id,int flag)
	{
		String sql=" ";
	
		StringBuffer buf=new StringBuffer(" 1=1 ");
		//btype为2表示室内 ；1表示铁塔
		if (!StringUtils.isEmpty(btype)&&!"0".equals(btype)) {
			buf.append(" and a.xequtype ='").append(btype).append("'");
		}
		buf.append(" and a.xtaskid='").append(btaskid).append("' and a.xequid=").append(id);
	/////--巡检周期没做
		
		
/*		sql = "SELECT   b.id,b.entid,b.baddress,b.bdesc,b.beqcount,b.bfactory,b.blevel,b.bname,b.bnumber,b.bposx,b.bposy,b.bregion,b.btower,b.btype,b.bfixcycle,b.btowertype,b.bcity,b.bwlnumber,b.bheight," +
		"a.id as ids,a.entid,a.xequid,a.xequtnum,a.xequtype,a.xgid,a.ximgname,a.ximgurl,a.xlid,a.xpid,a.xreptime,a.xstatus,a.xtaskid,a.xterid,a.xuid" +
		", c.entname , c.entname  FROM t_base_info b LEFT JOIN t_report_message a ON(a.xequid=b.id and " +buf.toString()+" ) LEFT JOIN t_s_enterprise c ON (a.entid=c.id) " +
		" where "+buf1.toString();*/
		sql = "SELECT    b.id,b.entid,b.baddress,b.bdesc,b.beqcount,b.bfactory,b.blevel,b.bname,b.bnumber,b.bposx,b.bposy,b.bregion,b.btower,b.btype,b.bfixcycle,b.btowertype,b.bcity,b.bwlnumber,b.bheight," +
		"a.id as ids,a.entid,a.xequid,a.xequtnum,a.xequtype,a.xgid,a.ximgname,a.ximgurl,a.xlid,a.xpid,a.xreptime,a.xstatus,a.xtaskid,a.xterid,a.xuid" +
		", c.entname , d.irealname  FROM t_report_message a LEFT JOIN t_base_info b ON(a.xequtnum=b.bnumber and a.entid=b.entid and a.xequid=b.id) LEFT JOIN t_s_enterprise c ON (a.entid=c.id)  LEFT JOIN t_inspect_user d ON (a.xuid=d.id)  where "+buf.toString();
	//	sql = "SELECT *  FROM t_report_message a LEFT JOIN t_base_info b ON(a.xequtnum=b.bnumber)  where "+buf.toString();
	    System.out.println(sql);
		SQLQuery query = baseDao.getHibernaSession().createSQLQuery(sql);

		//获取符合条件的message
		List<Object[]> rowlist = query.list();
		
		List<Object> listresult= list2object1(rowlist);
			if(listresult==null){
				return null;
			}
		Iterator itr = listresult.iterator();
		String bname="";
		int bid=0;
		while (itr.hasNext()) {
			TInspectItemReport te = (TInspectItemReport)itr.next();
			bname=te.bname;
			String sqldetail="from TInspectItemDetailReport where msgid =" + te.getId() + " order by msgid, id";
			List<TInspectItemDetailReport> detaillist=baseDao.find(sqldetail);
			//		System.out.println(te.getXequtnum());
			bid=te.getId();
			//重复数据的处理：按照id的大小判断
			//得到每个不同巡检项的最新值列表
			Map<String,TInspectItemDetailReport> newest = new HashMap<String,TInspectItemDetailReport>();
			for(int i=0;i< detaillist.size();i++){
				TInspectItemDetailReport t = detaillist.get(i);
				String item = t.getXproname();
				if(newest.containsKey(item)){
					if(newest.get(item).getId() < t.getId()){
						newest.remove(item);
						newest.put(item, t);
					}
				}
				else{
					newest.put(item, t);
				}	
			}
			List<TInspectItemDetailReport> detailsingle= new ArrayList<TInspectItemDetailReport>();
			Collection vs=newest.values(); 
			Iterator it=vs.iterator(); 
			while(it.hasNext()){ 
				detailsingle.add((TInspectItemDetailReport)it.next());
			}

			te.setInspectreportdetailmsgs(detailsingle);

		}			

		try{
			List<String> url=null;
			if(pic!=null&&pic.equals("pic")){
				url=eoipprint(listresult,btype,flag,bid);
			}
			else{
				
			 url=eoipexport(listresult,btype,flag,bname);
			}
		//	System.out.println( url.get(0));
			return url.get(0);
		
		}
		catch(Exception e){}
		
		return null;
	}
	
	
	/**用于打印图片，获取临时路径地址
	 * shezhi guiz
	 * @param listdb
	 * @param btype
	 * @return
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws IOException
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 */
	public List<String> eoipprint(List<Object> listdb,String btype,int flag,int bid) throws SecurityException, IllegalArgumentException, IOException, NoSuchFieldException, IllegalAccessException{
		
		//导出的文件名列表
		List<String> listfile = new ArrayList<String>();
		String urlString="";
		
		//导出规则设置
		 Eoiprule rule = new Eoiprule();
		 String summaryConfig="";
		 Boolean mode=false;
		 //字段位置
		 String setfieldmap ="";
		 //枚举型数据设置
		 Map<String,String> enummap = new HashMap<String,String>();
		 //绝对模式:true;列模式：false
		 //列模式开始
		 // 列模式：
		 if(flag==0){
			 
		 }
		
		 //绝对模式(即导出详情)
		 else{
			  mode = true;
				 ////btype为2表示室内 ；1表示铁塔
			  if("1".equals(btype)){
				  //  rule.setExpd2e("bname,d3;bposx,l3;bposy,n3");
				  rule.setExpd2e("bname,d3;entname,c2;btowertype,q3;bposx,l3;bposy,n3;iuname,m2;xreptime,v2;btower,w3");
				  rule.setExpfilename(Constant.TEMP_FILE_URL+File.separator+bid);
				  rule.setExptemplatefile(Constant.MODEL_FILE_URL+File.separator+"铁塔、天馈线系统检查维护记录打印.xls");
				  String temp="是,■是    □否;否,□是    ■否";
				  //铁塔辅助系统
				 enummap.put("航标灯、避雷器工作是否正常", temp);
				 enummap.put("天线抱杆（包括GPS抱杆）是否紧固牢固", temp);
				 enummap.put("铁塔警示牌（广告牌）是否固定紧固", temp);
				 enummap.put("铁塔和平台无锈蚀铁丝、螺丝、螺栓等杂物", temp);
				 enummap.put("铁塔避雷系统下引线连接是否可靠、是否锈蚀、丢失", temp);
				 enummap.put("检查爬梯、平台、过桥是否牢固", temp);
				 //天馈系统
				 enummap.put("天线与抱杆固定是否牢固", temp);
				 enummap.put("天线紧固件有无缺损、锈蚀", temp);
				 enummap.put("天线、馈线外观有无损伤", temp);
				 enummap.put("馈线与天线接口处密封是否良好", temp);
				 enummap.put("馈线接地处密封是否良好", temp);
				 enummap.put("平台处的馈线、接地线走线是否规范", temp);
				 enummap.put("馈线窗处密封是否良好", temp);
				 enummap.put("馈线卡子紧固情况是否良好", temp);
				 enummap.put("馈线卡子有无损缺有无锈蚀", temp);
				 //铁塔
				 enummap.put("铁塔镀锌观感质量、有无锈蚀现象", temp);
				 enummap.put("铁塔周围及塔体上无杂物、垃圾、废料", temp);
				 enummap.put("铁塔螺栓无缺损、锈蚀、松动", temp);
				 enummap.put("铁塔螺母无缺损、锈蚀、松动、开裂", temp);
				 enummap.put("铁塔基础回填夯实、无下陷", temp);
				 enummap.put("测量铁塔接地电阻", temp);
				 enummap.put("铁塔平台数", temp);
				 
						setfieldmap= "航标灯、避雷器工作是否正常,j4;天线抱杆（包括GPS抱杆）是否紧固牢固,j5;铁塔警示牌（广告牌）是否固定紧固,j6;铁塔和平台无锈蚀铁丝、螺丝、螺栓等杂物,j7;铁塔避雷系统下引线连接是否可靠、是否锈蚀、丢失,j8;检查爬梯、平台、过桥是否牢固,j9;天线与抱杆固定是否牢固,q4;天线紧固件有无缺损、锈蚀,q5;天线、馈线外观有无损伤,q6;馈线与天线接口处密封是否良好,q7;馈线接地处密封是否良好,q8;平台处的馈线、接地线走线是否规范,q9;馈线窗处密封是否良好,q11;馈线卡子紧固情况是否良好,q13;馈线卡子有无损缺有无锈蚀,q14;铁塔镀锌观感质量、有无锈蚀现象,ab4;铁塔周围及塔体上无杂物、垃圾、废料,ab5;铁塔螺栓无缺损、锈蚀、松动,ab6;铁塔螺母无缺损、锈蚀、松动、开裂,ab7;铁塔基础回填夯实、无下陷,ab8;铁塔基础回填夯实、无下陷,ab9;测量铁塔接地电阻,ab10;铁塔平台数,ab11;GSM900M天线方位角TX1,D13;GSM900M天线俯仰角TX1,D14;GSM900M抱杆垂直度TX1,D15;GSM900M驻波比TX1,D16;GSM900M天线方位角RX1,D18;GSM900M天线俯仰角RX1,D19;GSM900M抱杆垂直度RX1,D20;GSM900M驻波比RX1,D21;GSM1800M天线方位角TX1,D24;GSM1800M天线俯仰角TX1,D25;GSM1800M抱杆垂直度TX1,D26;GSM1800M驻波比TX1,D27;GSM1800M天线方位角RX1,D29;GSM1800M天线俯仰角RX1,D30;GSM1800M抱杆垂直度RX1,D31;GSM1800M驻波比RX1,D32;GSM900M天线方位角TX2,F13;GSM900M天线俯仰角TX2,F14;GSM900M抱杆垂直度TX2,F15;GSM900M驻波比TX2,F16;GSM900M天线方位角RX2,F18;GSM900M天线俯仰角RX2,F19;GSM900M抱杆垂直度RX2,F20;GSM900M驻波比RX2,F21;GSM1800M天线方位角TX2,F24;GSM1800M天线俯仰角TX2,F25;GSM1800M抱杆垂直度TX2,F26;GSM1800M驻波比TX2,F27;GSM1800M天线方位角RX2,F29;GSM1800M天线俯仰角RX2,F30;GSM1800M抱杆垂直度RX2,F31;GSM1800M驻波比RX2,F32;GSM900M天线方位角TX3,H13;GSM900M天线俯仰角TX3,H14;GSM900M抱杆垂直度TX3,H15;GSM900M驻波比TX3,H16;GSM900M天线方位角RX3,H18;GSM900M天线俯仰角RX3,H19;GSM900M抱杆垂直度RX3,H20;GSM900M驻波比RX3,H21;GSM1800M天线方位角TX3,H24;GSM1800M天线俯仰角TX3,H25;GSM1800M抱杆垂直度TX3,H26;GSM1800M驻波比TX3,H27;GSM1800M天线方位角RX3,H29;GSM1800M天线俯仰角RX3,H30;GSM1800M抱杆垂直度RX3,H31;GSM1800M驻波比RX3,H32";
			  }
			  else{
				  rule.setExpd2e("entname,e2;bname,c3;baddress,g3;blevel,j3;bposx,n3;bposy,t3;iuname,x3;xreptime,t2");
				  rule.setExpfilename(Constant.TEMP_FILE_URL+File.separator+bid);
				  rule.setExptemplatefile(Constant.MODEL_FILE_URL+File.separator+"室内分布及WLAN维护巡检表打印.xls");
				  String temp="是,是■  否□  未配备□;否,是□  否■  未配备□;未配备,是□  否□  未配备■";
				  String temp1="优,优■  良□  一般□  差□;良,优□  良■  一般□  差□;一般,优□  良□  一般■  差□;差,优□  良□  一般□  差■";
				  String temp2="满足,满足■  不满足□;不满足,满足□  不满足■";
				  //有源设备维护情况
				  enummap.put("设备维护有源设备维护情况信源（RRU、微蜂窝、直放站和干线放大器等）及传输设备运行状态是否正常", temp);
				  enummap.put("设备维护有源设备维护情况信源机和传输设备外部和内部是否标签正确清晰", temp);
				  enummap.put("设备维护有源设备维护情况信源设备尾纤是否过渡弯曲", temp);
				  enummap.put("设备维护有源设备维护情况有源设备接入电源是否稳定安全", temp);
				  enummap.put("设备维护有源设备维护情况设备周围是否有安全隐患", temp);
				  enummap.put("设备维护有源设备维护情况设备周围是否有安全隐患", temp);
				  enummap.put("设备维护有源设备维护情况设备接地线是否合格", temp);
				//无源设备维护情况
				  enummap.put("设备维护无源设备维护情况室分系统的驻波比是否满足要求", temp);
				  enummap.put("设备维护无源设备维护情况避雷接地部件是否做防水防锈处理", temp);
				  enummap.put("设备维护无源设备维护情况检查施主天线的方位角是否正常", temp);
				  enummap.put("设备维护无源设备维护情况施主天线接地电阻及接地线是否正常", temp);
				//网线、馈线、光纤、电源线维护
				  enummap.put("线路器件网线、馈线、光纤、电源线维护线路布放是否符合设计，并走线整齐、美观，不得有扭曲、空中飞线等情况", temp);
				  enummap.put("线路器件网线、馈线、光纤、电源线维护检查是否当跳线或馈线需要弯曲部分时，弯曲角圆滑，弯曲曲率半径符合相关标准要求", temp);
				  enummap.put("线路器件网线、馈线、光纤、电源线维护馈线头是否松动", temp);
				  enummap.put("线路器件网线、馈线、光纤、电源线维护与设备相连的跳线是否牢固固定", temp);
				//天线、合路器、耦合器、功分器维护
				  enummap.put("线路器件天线、合路器、耦合器、功分器维护器件安装是否固定，并且垂直、牢固，不允许悬空放置，不应放置室外（如特殊情况需室外放置，必须做好防水处理）", temp);
				  enummap.put("线路器件天线、合路器、耦合器、功分器维护器件是否有标签，并正确表明线路方向", temp);
				//信源机房主机机房卫生
				  enummap.put("信源机房信源机房主机机房卫生信源机房主机机房卫生", temp1);
				//信源机房主机机房安全
				  enummap.put("信源机房信源机房主机机房安全门、窗、锁是否完好", temp);
				  enummap.put("信源机信源机房信源机房主机机房安全墙面是否无裂痕", temp);
				  enummap.put("信源机房信源机房主机机房安全孔洞是否密封", temp);
				  enummap.put("信源机房信源机房主机机房安全灭火设施是否正常", temp);
				  enummap.put("信源机房信源机房主机机房安全机房周围是否有杂物", temp);
				  enummap.put("信源机房信源机房主机机房安全是否有消防隐患和安全隐患", temp);
				//配套设备维护情况
				  enummap.put("信源机房配套设备维护情况机房空调电压、电流是否正常", temp);
				  enummap.put("信源机房配套设备维护情况空调冷凝器、蒸发器、过滤网是否清洁", temp);
				  enummap.put("信源机房配套设备维护情况配套UPS电源是否工作正常", temp);
				//日常测试
				  enummap.put("PPPOE用户认证通过所需时间≤10s", temp2);
				  enummap.put("室分覆盖的建筑物周围一定距离（通常定义10米）外的地面，室分系统的信号强度不得高于-90dBm", temp2);
				  enummap.put("实际拨打测试中应感觉良好，无断续、杂音等现象", temp2);
				  enummap.put("室内分布系统覆盖范围内，手机不得切换到其它小区", temp2);
				  enummap.put("WCDMA数据业务接通率不低于96%；实测HSDPA吞吐率不低于2.5M；实测HSUPA吞吐率不低于1.2M", temp2);
				  enummap.put("覆盖区内同频接收干扰信号强度<-90dBm，邻频接收信号强度<-70dBm", temp2);
				  enummap.put("室内分布系统总体的驻波比不应大于1.5%", temp2);
				  setfieldmap="设备维护有源设备维护情况信源（RRU、微蜂窝、直放站和干线放大器等）及传输设备运行状态是否正常,j4;设备维护有源设备维护情况信源机和传输设备外部和内部是否标签正确清晰,j5;设备维护有源设备维护情况信源设备尾纤是否过渡弯曲,j6;设备维护有源设备维护情况有源设备接入电源是否稳定安全,j7;设备维护有源设备维护情况设备周围是否有安全隐患,j8;设备维护有源设备维护情况设备安装是否牢固,j9;设备维护有源设备维护情况设备接地线是否合格,j10;设备维护无源设备维护情况室分系统的驻波比是否满足要求,j11;设备维护无源设备维护情况避雷接地部件是否做防水防锈处理,j12;设备维护无源设备维护情况检查施主天线的方位角是否正常,j13;设备维护无源设备维护情况施主天线接地电阻及接地线是否正常,j14;线路器件网线、馈线、光纤、电源线维护线路布放是否符合设计，并走线整齐、美观，不得有扭曲、空中飞线等情况,j15;线路器件网线、馈线、光纤、电源线维护检查是否当跳线或馈线需要弯曲部分时，弯曲角圆滑，弯曲曲率半径符合相关标准要求,j16;线路器件网线、馈线、光纤、电源线维护馈线头是否松动,j17;线路器件网线、馈线、光纤、电源线维护与设备相连的跳线是否牢固固定,j18;线路器件天线、合路器、耦合器、功分器维护器件安装是否固定，并且垂直、牢固，不允许悬空放置，不应放置室外（如特殊情况需室外放置，必须做好防水处理）,j19;线路器件天线、合路器、耦合器、功分器维护器件是否有标签，并正确表明线路方向,j20;信源机房信源机房主机机房卫生信源机房主机机房卫生,j21信源机房信源机房主机机房安全门、窗、锁是否完好,j22;信源机房信源机房主机机房安全墙面是否无裂痕,j23;信源机房信源机房主机机房安全孔洞是否密封,j24;信源机房信源机房主机机房安全灭火设施是否正常,j25;信源机房信源机房主机机房安全机房周围是否有杂物,j26;信源机房信源机房主机机房安全是否有消防隐患和安全隐患,j27;信源机房配套设备维护情况机房空调电压、电流是否正常,j28;信源机房配套设备维护情况空调冷凝器、蒸发器、过滤网是否清洁,j29;信源机房配套设备维护情况配套UPS电源是否工作正常,j30;业务类型,aa4;PPPOE用户认证通过所需时间≤10s,aa5;室分覆盖的建筑物周围一定距离（通常定义10米）外的地面，室分系统的信号强度不得高于-90dBm,aa6;实际拨打测试中应感觉良好，无断续、杂音等现象,aa7;室内分布系统覆盖范围内，手机不得切换到其它小区,aa8;WCDMA数据业务接通率不低于96%；实测HSDPA吞吐率不低于2.5M；实测HSUPA吞吐率不低于1.2M,aa9;覆盖区内同频接收干扰信号强度<-90dBm，邻频接收信号强度<-70dBm,aa10;室内分布系统总体的驻波比不应大于1.5%,aa11;PSC_RSSIA点,o18;PSC_RSSIB点,o19;PSC_RSSIC点,o20;PSC_RSSID点,o21;PSC_RSSIE点,o22;PSC_EC-IOA点,u18;PSC_EC-IOB点,u19;CI_EC-IOC点,u20;CI_EC-IOD点,u21;CI_EC-IOE点,u22;CI_上传速率A点,y18;CI_上传速率B点,y19;CI_上传速率C点,y20;CI_上传速率D点,y21;CI_上传速率E点,y22;CI_下载速率A点,ad18;CI_下载速率B点,ad19;CI_下载速率C点,ad20;CI_下载速率D点,ad21;CI_下载速率E点,ad22;BCCH_RXA点,o25;BCCH_RXB点,o26;BCCH_RXC点,o27;BCCH_RXD点,o28;BCCH_RXE点,o29;BSIC_TXA点,s25;BSIC_TXB点,s26;BSIC_TXC点,s27;BSIC_TXD点,s28;BSIC_TXE点,s29;CID_RQA点,w25;CID_RQB点,w26;CID_RQC点,w27;CID_RQD点,w28;CID_RQE点,w29;CID_上传速率A点,y25;CID_上传速率B点,y26;CID_上传速率C点,y27;CID_上传速率D点,y28;CID_上传速率E点,y29;CID_下载速率A点,ad25;CID_下载速率B点,ad26;CID_下载速率C点,ad27;CID_下载速率D点,ad28;CID_下载速率E点,ad29;频点A点,o31;频点B点,o32;频点C点,o33;频点D点,o34;频点E点,o35;场强A点,u31;场强B点,u32;场强C点,u33;场强D点,u34;场强E点,u35;上传速率A点,y31;上传速率B点,y32;上传速率C点,y33;上传速率D点,y34;上传速率E点,y35;下载速率A点,ad31;下载速率B点,ad32;下载速率C点,ad33;下载速率D点,ad34;下载速率E点,ad35";
			  }
		
		
			 rule.setExpenumstr(enummap);
			 
			 //set type:
			 Map<String,String> setdemap = new HashMap<String,String>();
			 setdemap.put("com.inspect.model.monitor.TInspectItemDetailReport", "inspectreportdetailmsgs");
			 //如果
			
			 //rule.setSetdemap(setdemap);
			
			 String setfieldvalue = "xproname,xvalue";

			 rule.setSetfieldmap(setfieldmap);
			 rule.setSetfieldvalue(setfieldvalue);
			 
		 }

		 
		 //绝对模式结束
	
		 rule.setExpmode(mode);
		 rule.setExppostfixrule("name,bname");
		 rule.setExpnumsheetperwb(1);


		 Eoip eoip = new Eoip();
		 eoip.setErule(rule);
		 
		 listfile = eoip.db2excel(listdb);
		 
		 return listfile;
		
	}
	
	/**导出excel
	 * shezhi guiz
	 * @param listdb
	 * @param btype
	 * @return
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws IOException
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 */
	public List<String> eoipexport(List<Object> listdb,String btype,int flag,String bname) throws SecurityException, IllegalArgumentException, IOException, NoSuchFieldException, IllegalAccessException{
		long a=System.currentTimeMillis();
		//导出的文件名列表
		List<String> listfile = new ArrayList<String>();
		String urlString="";
		
		//导出规则设置
		 Eoiprule rule = new Eoiprule();
		 String summaryConfig="";
		 Boolean mode=false;
		 StringBuffer buffer=new StringBuffer();
		 String setfieldmap ="";
		 //枚举型数据设置
		 Map<String,String> enummap = new HashMap<String,String>();
		 //绝对模式:true;列模式：false
		 //列模式开始
		 // 列模式：
		 if(flag==0){
			  mode = false;
			 ////btype为2表示室内 ；1表示铁塔
			 if("1".equals(btype)){
				 summaryConfig= this.getSConfig(1);//巡检项名称和列的对应关系名称名称必须与数据库的巡检项名称相同
				 // System.out.println(summaryConfig);
				 rule.setExpd2e("ximgurl,a5;xequtnum,b5;bcity,c5;bregion,d5;bname,e5;baddress,f5;bposx,g5;bposy,h5;" +
			 		"bfactory,i4;btowertype,j5;bheight,k5;btower,l2;xreptime,ht5"); 
				 rule.setExpfilename(Constant.TEMP_FILE_URL+File.separator+"铁塔、天馈线.xlsx");/////////临时文件文件名
				 rule.setExptemplatefile(Constant.MODEL_FILE_URL+File.separator+"铁塔、天馈线台账（最新）.xlsx");//模版名称
				 rule.setExpstartcol(4);//从某某行开始
				 rule.setMaxcolnum(228);
			 }
			 else{
				 summaryConfig= this.getSConfig(2);//巡检项名称和列的对应关系名称名称必须与数据库的巡检项名称相同
				 //	 System.out.println(summaryConfig);
				/* rule.setExpd2e("bcity,a5;xequtnum,b5;bname,c5;baddress,d5;bfactory,e5;bposx,f5;bposy,g5;blevel,h5;" +
			 		"beqcount,i4");*/ 
				 rule.setExpd2e("ximgurl,a5;xequtnum,b5;bcity,c5;bregion,d5;bname,e5;baddress,f5;bfactory,g5;bposx,h5;bposy,i5;blevel,j5;beqcount,k5;" +
			 		"beqcount,i4;xreptime,dt5"); 
				 rule.setExpfilename(Constant.TEMP_FILE_URL+File.separator+"室内.xlsx");/////////临时文件文件名
				 rule.setExptemplatefile(Constant.MODEL_FILE_URL+File.separator+"室内分布及WLAN台账.xlsx");//模版名称
				 rule.setExpstartcol(4);//从某某行开始
				 rule.setMaxcolnum(124);
			 }
			/*	 //枚举型数据设置
			 * 
				 Map<String,String> enummap = new HashMap<String,String>();
				 enummap.put("pointid", "0,abc;1,def;2,ghk");
				 enummap.put("beqcount", "yyy,□是    □否;ooo,女");
				 //rule.setExpenumstr(enummap);
		*/		 
				 //set type:
				 Map<String,String> setdemap = new HashMap<String,String>();
				 setdemap.put("com.inspect.model.monitor.TInspectItemDetailReport", "inspectreportdetailmsgs");
				 //如果要取消set数据的导出功能，将setdemap字段置空即可
				 rule.setSetdemap(setdemap);
				
				 String setfieldvalue = "xproname,xvalue";

				 rule.setSetfieldmap(summaryConfig);
				 rule.setSetfieldvalue(setfieldvalue);
		 }
		
		 //绝对模式(即导出详情)
		 else{
			  mode = true;
				 ////btype为2表示室内 ；1表示铁塔
			  if("1".equals(btype)){
				    rule.setExpd2e("bname,d3;entname,c2;btowertype,q3;bposx,l3;bposy,n3;iuname,m2;xreptime,v2;btower,w3");
			//	  rule.setExpd2e("bname,d3;btowertype,j3;bposx,l3;bposy,n3;btower,t3;xreptime,x3");
				  rule.setExpfilename(Constant.TEMP_FILE_URL+File.separator+"铁塔"+bname);
				  rule.setExptemplatefile(Constant.MODEL_FILE_URL+File.separator+"铁塔、天馈线系统检查维护记录.xls");
			/*		 buffer.append("航标灯、避雷器工作是否正常,j4;天线抱杆（包括GPS抱杆）是否紧固牢固,j5;铁塔警示牌（广告牌）是否固定紧固,j6;铁塔和平台无锈蚀铁丝、螺丝、螺栓等杂物,j7;铁塔避雷系统下引线连接是否可靠、是否锈蚀、丢失,j8;检查爬梯、平台、过桥是否牢固,j9;")//铁塔辅助系统
		.append("天线与抱杆固定是否牢固,q4;天线紧固件有无缺损、锈蚀,q5;天线、馈线外观有无损伤,q6;馈线与天线接口处密封是否良好,q7;馈线接地处密封是否良好,q8;平台处的馈线、接地线走线是否规范,q9;馈线窗处密封是否良好,q11;馈线卡子紧固情况是否良好,q13;馈线卡子有无损缺有无锈蚀,q14")//天馈系统
		.append("铁塔镀锌观感质量、有无锈蚀现象,ab4;铁塔周围及塔体上无杂物、垃圾、废料,ab5;铁塔螺栓无缺损、锈蚀、松动,ab6;铁塔螺母无缺损、锈蚀、松动、开裂,ab7;铁塔基础回填夯实、无下陷,ab8;铁塔基础回填夯实、无下陷,ab9;测量铁塔接地电阻,ab10;铁塔平台数,ab11;")//铁塔
		.append("GSM900M天线方位角TX1,D13;GSM900M天线俯仰角TX1,D14;GSM900M抱杆垂直度TX1,D15;GSM900M驻波比TX1,D16;GSM900M天线方位角RX1,D18;GSM900M天线俯仰角RX1,D19;GSM900M抱杆垂直度RX1,D20;GSM900M驻波比RX1,D21;GSM1800M天线方位角TX1,D24;GSM1800M天线俯仰角TX1,D25;GSM1800M抱杆垂直度TX1,D26;GSM1800M驻波比TX1,D27;GSM1800M天线方位角RX1,D29;GSM1800M天线俯仰角RX1,D30;GSM1800M抱杆垂直度RX1,D31;GSM1800M驻波比RX1,D32;")//B　T　S　天线系统D列
		.append("GSM900M天线方位角TX2,F13;GSM900M天线俯仰角TX2,F14;GSM900M抱杆垂直度TX2,F15;GSM900M驻波比TX2,F16;GSM900M天线方位角RX2,F18;GSM900M天线俯仰角RX2,F19;GSM900M抱杆垂直度RX2,F20;GSM900M驻波比RX2,F21;GSM1800M天线方位角TX2,F24;GSM1800M天线俯仰角TX2,F25;GSM1800M抱杆垂直度TX2,F26;GSM1800M驻波比TX2,F27;GSM1800M天线方位角RX2,F29;GSM1800M天线俯仰角RX2,F30;GSM1800M抱杆垂直度RX2,F31;GSM1800M驻波比RX2,F32;")//B　T　S　天线系统F列
		.append("GSM900M天线方位角TX3,H13;GSM900M天线俯仰角TX3,H14;GSM900M抱杆垂直度TX3,H15;GSM900M驻波比TX3,H16;GSM900M天线方位角RX3,H18;GSM900M天线俯仰角RX3,H19;GSM900M抱杆垂直度RX3,H20;GSM900M驻波比RX3,H21;GSM1800M天线方位角TX3,H24;GSM1800M天线俯仰角TX3,H25;GSM1800M抱杆垂直度TX3,H26;GSM1800M驻波比TX3,H27;GSM1800M天线方位角RX3,H29;GSM1800M天线俯仰角RX3,H30;GSM1800M抱杆垂直度RX3,H31;GSM1800M驻波比RX3,H32");//B　T　S　天线系统H列
					*/ 
				  String temp="是,■是    □否;否,□是    ■否";
				  //铁塔辅助系统
				 enummap.put("航标灯、避雷器工作是否正常", temp);
				 enummap.put("天线抱杆（包括GPS抱杆）是否紧固牢固", temp);
				 enummap.put("铁塔警示牌（广告牌）是否固定紧固", temp);
				 enummap.put("铁塔和平台无锈蚀铁丝、螺丝、螺栓等杂物", temp);
				 enummap.put("铁塔避雷系统下引线连接是否可靠、是否锈蚀、丢失", temp);
				 enummap.put("检查爬梯、平台、过桥是否牢固", temp);
				 //天馈系统
				 enummap.put("天线与抱杆固定是否牢固", temp);
				 enummap.put("天线紧固件有无缺损、锈蚀", temp);
				 enummap.put("天线、馈线外观有无损伤", temp);
				 enummap.put("馈线与天线接口处密封是否良好", temp);
				 enummap.put("馈线接地处密封是否良好", temp);
				 enummap.put("平台处的馈线、接地线走线是否规范", temp);
				 enummap.put("馈线窗处密封是否良好", temp);
				 enummap.put("馈线卡子紧固情况是否良好", temp);
				 enummap.put("馈线卡子有无损缺有无锈蚀", temp);
				 //铁塔
				 enummap.put("铁塔镀锌观感质量、有无锈蚀现象", temp);
				 enummap.put("铁塔周围及塔体上无杂物、垃圾、废料", temp);
				 enummap.put("铁塔螺栓无缺损、锈蚀、松动", temp);
				 enummap.put("铁塔螺母无缺损、锈蚀、松动、开裂", temp);
				 enummap.put("铁塔基础回填夯实、无下陷", temp);
				 enummap.put("测量铁塔接地电阻", temp);
				 enummap.put("铁塔平台数", temp);
				 
						setfieldmap= "航标灯、避雷器工作是否正常,j4;天线抱杆（包括GPS抱杆）是否紧固牢固,j5;铁塔警示牌（广告牌）是否固定紧固,j6;铁塔和平台无锈蚀铁丝、螺丝、螺栓等杂物,j7;铁塔避雷系统下引线连接是否可靠、是否锈蚀、丢失,j8;检查爬梯、平台、过桥是否牢固,j9;天线与抱杆固定是否牢固,q4;天线紧固件有无缺损、锈蚀,q5;天线、馈线外观有无损伤,q6;馈线与天线接口处密封是否良好,q7;馈线接地处密封是否良好,q8;平台处的馈线、接地线走线是否规范,q9;馈线窗处密封是否良好,q11;馈线卡子紧固情况是否良好,q13;馈线卡子有无损缺有无锈蚀,q14;铁塔镀锌观感质量、有无锈蚀现象,ab4;铁塔周围及塔体上无杂物、垃圾、废料,ab5;铁塔螺栓无缺损、锈蚀、松动,ab6;铁塔螺母无缺损、锈蚀、松动、开裂,ab7;铁塔基础回填夯实、无下陷,ab8;铁塔基础回填夯实、无下陷,ab9;测量铁塔接地电阻,ab10;铁塔平台数,ab11;GSM900M天线方位角TX1,D13;GSM900M天线俯仰角TX1,D14;GSM900M抱杆垂直度TX1,D15;GSM900M驻波比TX1,D16;GSM900M天线方位角RX1,D18;GSM900M天线俯仰角RX1,D19;GSM900M抱杆垂直度RX1,D20;GSM900M驻波比RX1,D21;GSM1800M天线方位角TX1,D24;GSM1800M天线俯仰角TX1,D25;GSM1800M抱杆垂直度TX1,D26;GSM1800M驻波比TX1,D27;GSM1800M天线方位角RX1,D29;GSM1800M天线俯仰角RX1,D30;GSM1800M抱杆垂直度RX1,D31;GSM1800M驻波比RX1,D32;GSM900M天线方位角TX2,F13;GSM900M天线俯仰角TX2,F14;GSM900M抱杆垂直度TX2,F15;GSM900M驻波比TX2,F16;GSM900M天线方位角RX2,F18;GSM900M天线俯仰角RX2,F19;GSM900M抱杆垂直度RX2,F20;GSM900M驻波比RX2,F21;GSM1800M天线方位角TX2,F24;GSM1800M天线俯仰角TX2,F25;GSM1800M抱杆垂直度TX2,F26;GSM1800M驻波比TX2,F27;GSM1800M天线方位角RX2,F29;GSM1800M天线俯仰角RX2,F30;GSM1800M抱杆垂直度RX2,F31;GSM1800M驻波比RX2,F32;GSM900M天线方位角TX3,H13;GSM900M天线俯仰角TX3,H14;GSM900M抱杆垂直度TX3,H15;GSM900M驻波比TX3,H16;GSM900M天线方位角RX3,H18;GSM900M天线俯仰角RX3,H19;GSM900M抱杆垂直度RX3,H20;GSM900M驻波比RX3,H21;GSM1800M天线方位角TX3,H24;GSM1800M天线俯仰角TX3,H25;GSM1800M抱杆垂直度TX3,H26;GSM1800M驻波比TX3,H27;GSM1800M天线方位角RX3,H29;GSM1800M天线俯仰角RX3,H30;GSM1800M抱杆垂直度RX3,H31;GSM1800M驻波比RX3,H32";
			   }
			  else{
				  rule.setExpd2e("entname,e2;bname,c3;baddress,g3;blevel,j3;bposx,n3;bposy,t3;iuname,x3;xreptime,t2");
				  rule.setExpfilename(Constant.TEMP_FILE_URL+File.separator+"室内"+bname);
				  rule.setExptemplatefile(Constant.MODEL_FILE_URL+File.separator+"室内分布及WLAN维护巡检表.xls");
				  /*	  buffer.append("设备维护有源设备维护情况信源（RRU、微蜂窝、直放站和干线放大器等）及传输设备运行状态是否正常,j4;设备维护有源设备维护情况信源机和传输设备外部和内部是否标签正确清晰,j5;设备维护有源设备维护情况信源设备尾纤是否过渡弯曲,j6;设备维护有源设备维护情况有源设备接入电源是否稳定安全,j7;设备维护有源设备维护情况设备周围是否有安全隐患,j8;设备维护有源设备维护情况设备安装是否牢固,j9;设备维护有源设备维护情况设备接地线是否合格,j10;")//有源设备维护情况
			        .append("设备维护无源设备维护情况室分系统的驻波比是否满足要求,j11;设备维护无源设备维护情况避雷接地部件是否做防水防锈处理,j12;设备维护无源设备维护情况检查施主天线的方位角是否正常,j13;设备维护无源设备维护情况施主天线接地电阻及接地线是否正常,j14;")//无源设备维护情况
			        .append("线路器件网线、馈线、光纤、电源线维护线路布放是否符合设计，并走线整齐、美观，不得有扭曲、空中飞线等情况,j15;线路器件网线、馈线、光纤、电源线维护检查是否当跳线或馈线需要弯曲部分时，弯曲角圆滑，弯曲曲率半径符合相关标准要求,j16;线路器件网线、馈线、光纤、电源线维护馈线头是否松动,j17;线路器件网线、馈线、光纤、电源线维护与设备相连的跳线是否牢固固定,j18;")//网线、馈线、光纤、电源线维护
			        .append("线路器件天线、合路器、耦合器、功分器维护器件安装是否固定，并且垂直、牢固，不允许悬空放置，不应放置室外（如特殊情况需室外放置，必须做好防水处理）,j19;线路器件天线、合路器、耦合器、功分器维护器件是否有标签，并正确表明线路方向,j20;")//天线、合路器、耦合器、功分器维护
			        .append("信源机房信源机房主机机房卫生信源机房主机机房卫生,j21")//信源机房主机机房卫生
			  		.append("信源机房信源机房主机机房安全门、窗、锁是否完好,j22;信源机房信源机房主机机房安全墙面是否无裂痕,j23;信源机房信源机房主机机房安全孔洞是否密封,j24;信源机房信源机房主机机房安全灭火设施是否正常,j25;信源机房信源机房主机机房安全机房周围是否有杂物,j26;信源机房信源机房主机机房安全是否有消防隐患和安全隐患,j27;")//信源机房主机机房安全
			  		.append("信源机房配套设备维护情况机房空调电压、电流是否正常,j28;信源机房配套设备维护情况空调冷凝器、蒸发器、过滤网是否清洁,j29;信源机房配套设备维护情况配套UPS电源是否工作正常,j30;")//配套设备维护情况
			  		.append("业务类型,aa4;PPPOE用户认证通过所需时间≤10s,aa5;室分覆盖的建筑物周围一定距离（通常定义10米）外的地面，室分系统的信号强度不得高于-90dBm,aa6;实际拨打测试中应感觉良好，无断续、杂音等现象,aa7;室内分布系统覆盖范围内，手机不得切换到其它小区,aa8;WCDMA数据业务接通率不低于96%；实测HSDPA吞吐率不低于2.5M；实测HSUPA吞吐率不低于1.2M,aa9;覆盖区内同频接收干扰信号强度<-90dBm，邻频接收信号强度<-70dBm,aa10;室内分布系统总体的驻波比不应大于1.5%,aa11;")//日常测试
			  		.append("PSC_RSSIA点,o18;PSC_RSSIB点,o19;PSC_RSSIC点,o20;PSC_RSSID点,o21;PSC_RSSIE点,o22;")//WCDMA  PRC
			  		.append("PSC_EC-IOA点,u18;PSC_EC-IOB点,u19;CI_EC-IOC点,u20;CI_EC-IOD点,u21;CI_EC-IOE点,u22;")//WCDMA IO
			  		.append("CI_上传速率A点,y18;CI_上传速率B点,y19;CI_上传速率C点,y20;CI_上传速率D点,y21;CI_上传速率E点,y22;")//WCDMA上传速率
			  		.append("CI_下载速率A点,ad18;CI_下载速率B点,ad19;CI_下载速率C点,ad20;CI_下载速率D点,ad21;CI_下载速率E点,ad22;")//WCDMA 下载速率
			  		.append("BCCH_RXA点,o25;BCCH_RXB点,o26;BCCH_RXC点,o27;BCCH_RXD点,o28;BCCH_RXE点,o29;")//GSM网络BCCH
			  		.append("BSIC_TXA点,s25;BSIC_TXB点,s26;BSIC_TXC点,s27;BSIC_TXD点,s28;BSIC_TXE点,s29;")//GSM网络 BSIC_TX
			  		.append("CID_RQA点,w25;CID_RQB点,w26;CID_RQC点,w27;CID_RQD点,w28;CID_RQE点,w29;")//GSM网络  CID_RQ
			  		.append("CID_上传速率A点,y25;CID_上传速率B点,y26;CID_上传速率C点,y27;CID_上传速率D点,y28;CID_上传速率E点,y29;")//GSM网络上传速率
			  		.append("CID_下载速率A点,ad25;CID_下载速率B点,ad26;CID_下载速率C点,ad27;CID_下载速率D点,ad28;CID_下载速率E点,ad29;")//gsm网络下载速率
			  		.append("频点A点,o31;频点B点,o32;频点C点,o33;频点D点,o34;频点E点,o35;")//WLAN网络频点
			  		.append("场强A点,u31;场强B点,u32;场强C点,u33;场强D点,u34;场强E点,u35;")//WLAN网络场强
			  		.append("上传速率A点,y31;上传速率B点,y32;上传速率C点,y33;上传速率D点,y34;上传速率E点,y35;")//WLAN网络上传速率
			  		.append("下载速率A点,ad31;下载速率B点,ad32;下载速率C点,ad33;下载速率D点,ad34;下载速率E点,ad35");//WLAN网络 下载速率
*/	
				  String temp="是,是■  否□  未配备□;否,是□  否■  未配备□;未配备,是□  否□  未配备■";
				  String temp1="优,优■  良□  一般□  差□;良,优□  良■  一般□  差□;一般,优□  良□  一般■  差□;差,优□  良□  一般□  差■";
				  String temp2="满足,满足■  不满足□;不满足,满足□  不满足■";
				  //有源设备维护情况
				  enummap.put("设备维护有源设备维护情况信源（RRU、微蜂窝、直放站和干线放大器等）及传输设备运行状态是否正常", temp);
				  enummap.put("设备维护有源设备维护情况信源机和传输设备外部和内部是否标签正确清晰", temp);
				  enummap.put("设备维护有源设备维护情况信源设备尾纤是否过渡弯曲", temp);
				  enummap.put("设备维护有源设备维护情况有源设备接入电源是否稳定安全", temp);
				  enummap.put("设备维护有源设备维护情况设备周围是否有安全隐患", temp);
				  enummap.put("设备维护有源设备维护情况设备周围是否有安全隐患", temp);
				  enummap.put("设备维护有源设备维护情况设备接地线是否合格", temp);
				//无源设备维护情况
				  enummap.put("设备维护无源设备维护情况室分系统的驻波比是否满足要求", temp);
				  enummap.put("设备维护无源设备维护情况避雷接地部件是否做防水防锈处理", temp);
				  enummap.put("设备维护无源设备维护情况检查施主天线的方位角是否正常", temp);
				  enummap.put("设备维护无源设备维护情况施主天线接地电阻及接地线是否正常", temp);
				//网线、馈线、光纤、电源线维护
				  enummap.put("线路器件网线、馈线、光纤、电源线维护线路布放是否符合设计，并走线整齐、美观，不得有扭曲、空中飞线等情况", temp);
				  enummap.put("线路器件网线、馈线、光纤、电源线维护检查是否当跳线或馈线需要弯曲部分时，弯曲角圆滑，弯曲曲率半径符合相关标准要求", temp);
				  enummap.put("线路器件网线、馈线、光纤、电源线维护馈线头是否松动", temp);
				  enummap.put("线路器件网线、馈线、光纤、电源线维护与设备相连的跳线是否牢固固定", temp);
				//天线、合路器、耦合器、功分器维护
				  enummap.put("线路器件天线、合路器、耦合器、功分器维护器件安装是否固定，并且垂直、牢固，不允许悬空放置，不应放置室外（如特殊情况需室外放置，必须做好防水处理）", temp);
				  enummap.put("线路器件天线、合路器、耦合器、功分器维护器件是否有标签，并正确表明线路方向", temp);
				//信源机房主机机房卫生
				  enummap.put("信源机房信源机房主机机房卫生信源机房主机机房卫生", temp1);
				//信源机房主机机房安全
				  enummap.put("信源机房信源机房主机机房安全门、窗、锁是否完好", temp);
				  enummap.put("信源机信源机房信源机房主机机房安全墙面是否无裂痕", temp);
				  enummap.put("信源机房信源机房主机机房安全孔洞是否密封", temp);
				  enummap.put("信源机房信源机房主机机房安全灭火设施是否正常", temp);
				  enummap.put("信源机房信源机房主机机房安全机房周围是否有杂物", temp);
				  enummap.put("信源机房信源机房主机机房安全是否有消防隐患和安全隐患", temp);
				//配套设备维护情况
				  enummap.put("信源机房配套设备维护情况机房空调电压、电流是否正常", temp);
				  enummap.put("信源机房配套设备维护情况空调冷凝器、蒸发器、过滤网是否清洁", temp);
				  enummap.put("信源机房配套设备维护情况配套UPS电源是否工作正常", temp);
				//日常测试
				  enummap.put("PPPOE用户认证通过所需时间≤10s", temp2);
				  enummap.put("室分覆盖的建筑物周围一定距离（通常定义10米）外的地面，室分系统的信号强度不得高于-90dBm", temp2);
				  enummap.put("实际拨打测试中应感觉良好，无断续、杂音等现象", temp2);
				  enummap.put("室内分布系统覆盖范围内，手机不得切换到其它小区", temp2);
				  enummap.put("WCDMA数据业务接通率不低于96%；实测HSDPA吞吐率不低于2.5M；实测HSUPA吞吐率不低于1.2M", temp2);
				  enummap.put("覆盖区内同频接收干扰信号强度<-90dBm，邻频接收信号强度<-70dBm", temp2);
				  enummap.put("室内分布系统总体的驻波比不应大于1.5%", temp2);
			//	  setfieldmap="设备维护有源设备维护情况信源（RRU、微蜂窝、直放站和干线放大器等）及传输设备运行状态是否正常,j4;设备维护有源设备维护情况信源机和传输设备外部和内部是否标签正确清晰,j5;设备维护有源设备维护情况信源设备尾纤是否过渡弯曲,j6;设备维护有源设备维护情况有源设备接入电源是否稳定安全,j7;设备维护有源设备维护情况设备周围是否有安全隐患,j8;设备维护有源设备维护情况设备安装是否牢固,j9;设备维护有源设备维护情况设备接地线是否合格,j10;设备维护无源设备维护情况室分系统的驻波比是否满足要求,j11;设备维护无源设备维护情况避雷接地部件是否做防水防锈处理,j12;设备维护无源设备维护情况检查施主天线的方位角是否正常,j13;设备维护无源设备维护情况施主天线接地电阻及接地线是否正常,j14;线路器件网线、馈线、光纤、电源线维护线路布放是否符合设计，并走线整齐、美观，不得有扭曲、空中飞线等情况,j15;线路器件网线、馈线、光纤、电源线维护检查是否当跳线或馈线需要弯曲部分时，弯曲角圆滑，弯曲曲率半径符合相关标准要求,j16;线路器件网线、馈线、光纤、电源线维护馈线头是否松动,j17;线路器件网线、馈线、光纤、电源线维护与设备相连的跳线是否牢固固定,j18;线路器件天线、合路器、耦合器、功分器维护器件安装是否固定，并且垂直、牢固，不允许悬空放置，不应放置室外（如特殊情况需室外放置，必须做好防水处理）,j19;线路器件天线、合路器、耦合器、功分器维护器件是否有标签，并正确表明线路方向,j20;信源机房信源机房主机机房卫生信源机房主机机房卫生,j21;信源机房信源机房主机机房安全门、窗、锁是否完好,j22;信源机房信源机房主机机房安全墙面是否无裂痕,j23;信源机房信源机房主机机房安全孔洞是否密封,j24;信源机房信源机房主机机房安全灭火设施是否正常,j25;信源机房信源机房主机机房安全机房周围是否有杂物,j26;信源机房信源机房主机机房安全是否有消防隐患和安全隐患,j27;信源机房配套设备维护情况机房空调电压、电流是否正常,j28;信源机房配套设备维护情况空调冷凝器、蒸发器、过滤网是否清洁,j29;信源机房配套设备维护情况配套UPS电源是否工作正常,j30;业务类型,aa4;PPPOE用户认证通过所需时间≤10s,aa5;室分覆盖的建筑物周围一定距离（通常定义10米）外的地面，室分系统的信号强度不得高于-90dBm,aa6;实际拨打测试中应感觉良好，无断续、杂音等现象,aa7;室内分布系统覆盖范围内，手机不得切换到其它小区,aa8;WCDMA数据业务接通率不低于96%；实测HSDPA吞吐率不低于2.5M；实测HSUPA吞吐率不低于1.2M,aa9;覆盖区内同频接收干扰信号强度<-90dBm，邻频接收信号强度<-70dBm,aa10;室内分布系统总体的驻波比不应大于1.5%,aa11;PSC_RSSIA点,o18;PSC_RSSIB点,o19;PSC_RSSIC点,o20;PSC_RSSID点,o21;PSC_RSSIE点,o22;PSC_EC-IOA点,u18;PSC_EC-IOB点,u19;CI_EC-IOC点,u20;CI_EC-IOD点,u21;CI_EC-IOE点,u22;CI_上传速率A点,y18;CI_上传速率B点,y19;CI_上传速率C点,y20;CI_上传速率D点,y21;CI_上传速率E点,y22;CI_下载速率A点,ad18;CI_下载速率B点,ad19;CI_下载速率C点,ad20;CI_下载速率D点,ad21;CI_下载速率E点,ad22;BCCH_RXA点,o25;BCCH_RXB点,o26;BCCH_RXC点,o27;BCCH_RXD点,o28;BCCH_RXE点,o29;BSIC_TXA点,s25;BSIC_TXB点,s26;BSIC_TXC点,s27;BSIC_TXD点,s28;BSIC_TXE点,s29;CID_RQA点,w25;CID_RQB点,w26;CID_RQC点,w27;CID_RQD点,w28;CID_RQE点,w29;CID_上传速率A点,y25;CID_上传速率B点,y26;CID_上传速率C点,y27;CID_上传速率D点,y28;CID_上传速率E点,y29;CID_下载速率A点,ad25;CID_下载速率B点,ad26;CID_下载速率C点,ad27;CID_下载速率D点,ad28;CID_下载速率E点,ad29;频点A点,o31;频点B点,o32;频点C点,o33;频点D点,o34;频点E点,o35;场强A点,u31;场强B点,u32;场强C点,u33;场强D点,u34;场强E点,u35;上传速率A点,y31;上传速率B点,y32;上传速率C点,y33;上传速率D点,y34;上传速率E点,y35;下载速率A点,ad31;下载速率B点,ad32;下载速率C点,ad33;下载速率D点,ad34;下载速率E点,ad35";
		setfieldmap="设备维护有源设备维护情况信源（RRU、微蜂窝、直放站和干线放大器等）及传输设备运行状态是否正常,j4;设备维护有源设备维护情况信源机和传输设备外部和内部是否标签正确清晰,j5;设备维护有源设备维护情况信源设备尾纤是否过渡弯曲,j6;设备维护有源设备维护情况有源设备接入电源是否稳定安全,j7;设备维护有源设备维护情况设备周围是否有安全隐患,j8;设备维护有源设备维护情况设备安装是否牢固,j9;设备维护有源设备维护情况设备接地线是否合格,j10;设备维护无源设备维护情况室分系统的驻波比是否满足要求,j11;设备维护无源设备维护情况避雷接地部件是否做防水防锈处理,j12;设备维护无源设备维护情况检查施主天线的方位角是否正常,j13;设备维护无源设备维护情况施主天线接地电阻及接地线是否正常,j14;线路器件网线、馈线、光纤、电源线维护线路布放是否符合设计，并走线整齐、美观，不得有扭曲、空中飞线等情况,j15;线路器件网线、馈线、光纤、电源线维护检查是否当跳线或馈线需要弯曲部分时，弯曲角圆滑，弯曲曲率半径符合相关标准要求,j16;线路器件网线、馈线、光纤、电源线维护馈线头是否松动,j17;线路器件网线、馈线、光纤、电源线维护与设备相连的跳线是否牢固固定,j18;线路器件天线、合路器、耦合器、功分器维护器件安装是否固定，并且垂直、牢固，不允许悬空放置，不应放置室外（如特殊情况需室外放置，必须做好防水处理）,j19;线路器件天线、合路器、耦合器、功分器维护器件是否有标签，并正确表明线路方向,j20;信源机房信源机房主机机房卫生信源机房主机机房卫生,j21;信源机房信源机房主机机房安全门、窗、锁是否完好,j22;信源机房信源机房主机机房安全墙面是否无裂痕,j23;信源机房信源机房主机机房安全孔洞是否密封,j24;信源机房信源机房主机机房安全灭火设施是否正常,j25;信源机房信源机房主机机房安全机房周围是否有杂物,j26;信源机房信源机房主机机房安全是否有消防隐患和安全隐患,j27;信源机房配套设备维护情况机房空调电压、电流是否正常,j28;信源机房配套设备维护情况空调冷凝器、蒸发器、过滤网是否清洁,j29;信源机房配套设备维护情况配套UPS电源是否工作正常,j30;业务类型,aa4;PPPOE用户认证通过所需时间≤10s,aa5;室分覆盖的建筑物周围一定距离（通常定义10米）外的地面，室分系统的信号强度不得高于-90dBm,aa6;实际拨打测试中应感觉良好，无断续、杂音等现象,aa7;室内分布系统覆盖范围内，手机不得切换到其它小区,aa8;WCDMA数据业务接通率不低于96%；实测HSDPA吞吐率不低于2.5M；实测HSUPA吞吐率不低于1.2M,aa9;覆盖区内同频接收干扰信号强度<-90dBm，邻频接收信号强度<-70dBm,aa10;室内分布系统总体的驻波比不应大于1.5%,aa11;PSC_RSSIA点,o18;PSC_RSSIB点,o19;PSC_RSSIC点,o20;PSC_RSSID点,o21;PSC_RSSIE点,o22;PSC_EC-IOA点,u18;PSC_EC-IOB点,u19;CI_EC-IOC点,u20;CI_EC-IOD点,u21;CI_EC-IOE点,u22;CI_上传速率A点,y18;CI_上传速率B点,y19;CI_上传速率C点,y20;CI_上传速率D点,y21;CI_上传速率E点,y22;CI_下载速率A点,ad18;CI_下载速率B点,ad19;CI_下载速率C点,ad20;CI_下载速率D点,ad21;CI_下载速率E点,ad22;BCCH_RXA点,o25;BCCH_RXB点,o26;BCCH_RXC点,o27;BCCH_RXD点,o28;BCCH_RXE点,o29;BSIC_TXA点,s25;BSIC_TXB点,s26;BSIC_TXC点,s27;BSIC_TXD点,s28;BSIC_TXE点,s29;CID_RQA点,w25;CID_RQB点,w26;CID_RQC点,w27;CID_RQD点,w28;CID_RQE点,w29;CID_上传速率A点,y25;CID_上传速率B点,y26;CID_上传速率C点,y27;CID_上传速率D点,y28;CID_上传速率E点,y29;CID_下载速率A点,ad25;CID_下载速率B点,ad26;CID_下载速率C点,ad27;CID_下载速率D点,ad28;CID_下载速率E点,ad29;频点A点,o31;频点B点,o32;频点C点,o33;频点D点,o34;频点E点,o35;场强A点,u31;场强B点,u32;场强C点,u33;场强D点,u34;场强E点,u35;上传速率A点,y31;上传速率B点,y32;上传速率C点,y33;上传速率D点,y34;上传速率E点,y35;下载速率A点,ad31;下载速率B点,ad32;下载速率C点,ad33;下载速率D点,ad34;下载速率E点,ad35";

			  }
			
	
			 rule.setExpenumstr(enummap);
			 
			 //set type:
			 Map<String,String> setdemap = new HashMap<String,String>();
			  setdemap.put("com.inspect.model.monitor.TInspectItemDetailReport", "inspectreportdetailmsgs");
			 //setdemap.put("com.inspect.model.monitor.TInspectItemDetailReport",  inspectreportdetailmsgs);
			 
			 //如果
			 rule.setSetdemap(setdemap);
		
			 	

			 
			 String setfieldvalue = "xproname,xvalue";

			 rule.setSetfieldmap(setfieldmap);
			 rule.setSetfieldvalue(setfieldvalue);
			 //绝对模式结束
		 }

		 
		
	
		 rule.setExpmode(mode);
		 rule.setExppostfixrule("name,bname");
		 rule.setExpnumsheetperwb(1);//sheet导出数目


		 Eoip eoip = new Eoip();
		 eoip.setErule(rule);
		 // System.out.println("执行配置规则耗时 : "+(System.currentTimeMillis()-a)/1000f+" 秒 ");
		 // System.out.println("2");
			long a1=System.currentTimeMillis();
			try {
				 listfile = eoip.db2excel(listdb);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				//	System.out.println("eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
				System.out.println(e.getMessage());
			}
		
			// System.out.println("执行耗时 : "+(System.currentTimeMillis()-a1)/1000f+" 秒 ");
		 
		 return listfile;
		
	}
	public List<Object> list2object1(List list){
		   if(list == null || list.size()==0)
			   return null;
			   

		    List<Object> listobj = new ArrayList<Object>();
		    

		    for(int i = 0; i < list.size();i++){
			 TInspectItemReport ir = new TInspectItemReport();
		     Object[] object = (Object[])list.get(i);// 每行记录不在是一个对象 而是一个数组
		     int id 		= 0;
		     if(object[19]!=null){
		    	 
		    	id= (Integer)object[19];
		    	//	System.out.println(id);
		    }
		/*     if(object[0]!=null){
		    	 
			    	System.out.println((Integer)object[0]);
			    }*/
		     int entid 	=0;
		     if(object[1]!=null){
		    	   entid 	= (Integer)object[1];
		     }
		     int xtaskid =0;
		    if(object[31]!=null){
		    	 xtaskid = (Integer)object[31]; //任务id 
		     }
		     int xlid  	=0;
		 	if(object[27]!=null){
		 		 xlid  	= (Integer)object[27];;   //线路id 
		     }
		 	int xpid  	=0;
			if(object[28]!=null){
				 xpid  	= (Integer)object[28];  //巡检点id
		     }
			int xequid 	=0;
			if(object[21]!=null){
				 xequid 	= (Integer)object[21]; //设备ID
		     }
			int xgid 	= 0;
			if(object[24]!=null){
				 xgid 	= (Integer)object[24]; //巡检班组 
		     }
			int xuid  	=0;
			if(object[33]!=null){
				 xuid  	= (Integer)object[33]; ; //巡检员 
		     }
		
			String xequtnum = (String)object[22]; //设备编号
			String xterid   = (String)object[32];; //巡检终端编号
			String xreptime = (String)object[29]; ; //上报时间
			String xstatus  = (String)object[30]; ;  //状态0表示正常 1表示异常，即审核不合格，需要重新巡检
			String ximgname = (String)object[25]; //图评名称
			String ximgurl  = (String)object[26];;  //图评路径
			String xequtype = (String)object[23];//设备类型

			String bfixcycle=(String)object[14];
			String btype	=(String)object[13];;   //基础设备类型
			String bnumber	=(String)object[8];;  //编号
			String bname	=(String)object[7];;   // 名称
			String bcity	=(String)object[16];
			String bregion	=(String)object[11];;//区县
			String baddress		=(String)object[2];;//地址
			double bposx=0;
			double bposy=0;
			if(object[9]!=null){
				 bposx	=Double.parseDouble(String.valueOf(object[9]));;     //经度
			}
			if(object[10]!=null){
				 bposy	=Double.parseDouble(String.valueOf(object[10]));;    //纬度
			}
			String btowertype=(String)object[15];;//铁塔类型
			String btower	=(String)object[12];;  //自建（共享）塔
			String bfactory	=(String)object[5];; // 集成厂家 
			String blevel	=(String)object[6];; //基站维护等级
			String beqcount	=(String)object[4];; // 有源设备数量
			String bwlnumber=(String)object[17];;//物理编号（无效，等同于设备编号，在T_base_info里面都为null）
			String bdesc	=(String)object[3];;  // 描述
			double bheight=0;
			if(object[18]!=null){
				bheight	=Double.parseDouble(String.valueOf(object[18]));    //纬度
			}
			String entname = (String)object[34];
			String iuname = (String)object[35];
			
			ir.setId(id);
			ir.setEntid(entid);
			ir.setXtaskid(xtaskid);
			ir.setXlid(xlid);
			ir.setXpid(xpid);
			ir.setXequid(xequid);
			ir.setXgid(xgid);
			ir.setXuid(xuid);
			ir.setXequtnum(bnumber);
			ir.setXterid(xterid);
			ir.setXreptime(xreptime);
			ir.setXstatus(xstatus);
			ir.setXimgname(ximgname);
			ir.setXimgurl(ximgurl);
			ir.setXequtype(btype);

			
			ir.btype   			=	btype   ;
			//ir.bnumber  		=	bnumber ;
			ir.bname   			=	bname  ;
			ir.bcity 			=	bcity 	;
			ir.bregion			=	bregion	;
			ir.baddress			=	baddress;
			ir.bposx     		=	bposx   ;
			ir.bposy    		=	bposy    	  	;
			ir.btowertype		=	btowertype		;
			ir.btower  			=	btower  			;
			ir.bfactory 		=	bfactory 	  	;
			ir.blevel 			=	blevel 		  	;
			ir.beqcount 		=	beqcount 	  	;
			ir.bwlnumber		=	bwlnumber	  	;
			ir.bdesc  			=	bdesc  		  	;
			ir.bheight     		= bheight       ;
			ir.entname 		    = entname;
			ir.iuname			= iuname;

			
	        listobj.add(ir); // 最终封装在list中 传到前台。
		    }
		    return listobj;
	}
	//老版台帐List转成object
	public List<Object> list2object(List list){
		   if(list == null || list.size()==0)
			   return null;
			   

		    List<Object> listobj = new ArrayList<Object>();
		    

		    for(int i = 0; i < list.size();i++){
			 TInspectItemReport ir = new TInspectItemReport();
		     Object[] object = (Object[])list.get(i);// 每行记录不在是一个对象 而是一个数组
		     
		    int id 		=  (Integer)object[0];
		    int entid 	= (Integer)object[1];
		    int xtaskid = (Integer)object[12]; //任务id
			int xlid  	= (Integer)object[8];;   //线路id
			int xpid  	= (Integer)object[9];  //巡检点id
			int xequid 	= (Integer)object[2]; //设备ID
			int xgid 	= (Integer)object[5]; //巡检班组
			int xuid  	= (Integer)object[14]; ; //巡检员
			String xequtnum = (String)object[3]; //设备编号
			String xterid   = (String)object[13];; //巡检终端编号
			String xreptime = (String)object[10]; ; //上报时间
			String xstatus  = (String)object[11]; ;  //状态0表示正常 1表示异常，即审核不合格，需要重新巡检
			String ximgname = (String)object[6]; //图评名称
			String ximgurl  = (String)object[7];;  //图评路径
			String xequtype = (String)object[4];//设备类型

			String bfixcycle=(String)object[29];
			String btype	=(String)object[28];;   //基础设备类型
			String bnumber	=(String)object[23];;  //编号
			String bname	=(String)object[22];;   // 名称
			String bcity	=(String)object[31];
			String bregion	=(String)object[26];;//区县
			String baddress		=(String)object[17];;//地址
			double bposx=0;
			double bposy=0;
			if(object[24]!=null){
				 bposx	=Double.parseDouble(String.valueOf(object[24]));;     //经度
			}
			if(object[25]!=null){
				 bposy	=Double.parseDouble(String.valueOf(object[25]));;    //纬度
			}
			String btowertype=(String)object[30];;//铁塔类型
			String btower	=(String)object[27];;  //自建（共享）塔
			String bfactory	=(String)object[20];; // 集成厂家 
			String blevel	=(String)object[21];; //基站维护等级
			String beqcount	=(String)object[19];; // 有源设备数量
			String bwlnumber=(String)object[32];;//物理编号
			String bdesc	=(String)object[18];;  // 描述
			double bheight=0;
			if(object[33]!=null){
				bheight	=Double.parseDouble(String.valueOf(object[33]));    //纬度
			}
			String entname = (String)object[34];
			String iuname = (String)object[35];
			
			ir.setId		(id       );
			ir.setEntid(entid);
			ir.setXtaskid   (xtaskid  );
			ir.setXlid      (xlid     );
			ir.setXpid      (xpid     );
			ir.setXequid    (xequid   );
			ir.setXgid      (xgid     );
			ir.setXuid      (xuid     );
			ir.setXequtnum  (xequtnum );
			ir.setXterid    (xterid   );
			ir.setXreptime  (xreptime );
			ir.setXstatus   (xstatus  );
			ir.setXimgname  (ximgname );
			ir.setXimgurl   (ximgurl  );
			ir.setXequtype  (xequtype );

			
			ir.btype   			=	btype   ;
			//ir.bnumber  		=	bnumber ;
			ir.bname   			=	bname  ;
			ir.bcity 			=	bcity 	;
			ir.bregion			=	bregion	;
			ir.baddress			=	baddress;
			ir.bposx     		=	bposx   ;
			ir.bposy    		=	bposy    	  	;
			ir.btowertype		=	btowertype		;
			ir.btower  			=	btower  			;
			ir.bfactory 		=	bfactory 	  	;
			ir.blevel 			=	blevel 		  	;
			ir.beqcount 		=	beqcount 	  	;
			ir.bwlnumber		=	bwlnumber	  	;
			ir.bdesc  			=	bdesc  		  	;
			ir.bheight     		= bheight       ;
			ir.entname 		    = entname;
			ir.iuname			= iuname;

			
 	        listobj.add(ir); // 最终封装在list中 传到前台。
		    }
		    return listobj;
	}
	
	/***
	 * 
	 * 得到模版单元格的格式，然后将数据转换为对应的数据格式置换进去。
	 * @param cell  	具体excel单元格
	 * @param value     需要置换进去的数据值
	 * @author liao
	 */
	private void cellSetValue(Cell cell,String value){
		//int type = cell.getCellType();
		Double cell_value = null ;
		if( !org.apache.commons.lang.StringUtils.isEmpty(value) ){
			try {
			//	cell_value = Double.parseDouble(value) ;
				cell.setCellValue( value ) ;
			} catch (NumberFormatException e) {
			}
		}
	
	}
	/**
	 * 获取统计配置表的配置信息
	 * @param ids
	 * @return
	 */
	public HashMap<String, String> getSummaryConfigMap(String ids,int flag){
		HashMap<String, String> sconfigMap=new HashMap<String, String>();
		//excel导出部分信息
		if(flag==0){
			if(!StringUtils.isEmpty(ids)){
					for(String id : ids.split(",")){
						TSummaryConfig sconfig=baseDao.get(TSummaryConfig.class,Integer.parseInt(id));
						sconfigMap.put(sconfig.getSname(),sconfig.getScell());
					}
			}
		
		}
		//excel表全部导出
		else if(flag==1){
			List<TSummaryConfig> sconfigList=baseDao.find("from TSummaryConfig");
			if(sconfigList!=null&&sconfigList.size()>0){
				for(TSummaryConfig sconfig:sconfigList){
					sconfigMap.put(sconfig.getSname(),sconfig.getScell());
				}
			}	
		}
		return sconfigMap;
	}
	
	/**
	 * 获取统计配置表的配置信息
	 * @param ids
	 * @return
	 */
	public HashMap<String, String> getSummaryConfigMap(String ids,int flag,int sConfigFlag){
		HashMap<String, String> sconfigMap=new HashMap<String, String>();
		//excel导出部分信息
		if(flag==0){
			if(!StringUtils.isEmpty(ids)){
					for(String id : ids.split(",")){
						String hql="from TSummaryConfig where id="+id +" and flag="+sConfigFlag;
						List<TSummaryConfig> sconfigList=baseDao.find(hql);
						if(sconfigList!=null&&sconfigList.size()>0){
							TSummaryConfig sconfig=sconfigList.get(0);
							sconfigMap.put(sconfig.getSname(),sconfig.getScell());
						}
					}
			}
		
		}
		//excel表全部导出
		else if(flag==1){
			List<TSummaryConfig> sconfigList=baseDao.find("from TSummaryConfig where flag="+sConfigFlag);
			if(sconfigList!=null&&sconfigList.size()>0){
				for(TSummaryConfig sconfig:sconfigList){
					sconfigMap.put(sconfig.getSname(),sconfig.getScell());
				}
			}	
		}
		return sconfigMap;
	}
	/**	//统计报表查询
	 * @author liao
	 */
	@Override
	public Map<String, Object> findSummaryFormDatagrid(
			SummaryFormVo summaryFormvo, int page, int rows, String qsql) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer buf = new StringBuffer(qsql);
			//StringBuffer ids=new StringBuffer("");
		if (summaryFormvo.getXgid()!=0) {
			buf.append(" and xgid =").append(summaryFormvo.getXgid());
		}
		if (StringUtils.isNotEmpty(summaryFormvo.getXequtnum())) {
			buf.append(" and xequtnum like '%").append(summaryFormvo.getXequtnum().trim()).append("%'");
		}
		if (StringUtils.isNotEmpty(summaryFormvo.getBtype())) {
			buf.append(" and xequtype ='").append(summaryFormvo.getBtype()).append("'");
		}
		if (summaryFormvo.getEntid()!=0) {
			buf.append(" and entid =").append(summaryFormvo.getEntid());
		}
		if (summaryFormvo.getItaskid()!=0) {
			buf.append(" and xtaskid =").append(summaryFormvo.getItaskid());
		}
		if (summaryFormvo.getXuid()!=0) {
			buf.append(" and xuid =").append(summaryFormvo.getXuid());
		}
		if (StringUtils.isNotEmpty(summaryFormvo.getRpsdate())) {
			buf.append(" and xreptime >='").append(summaryFormvo.getRpsdate()).append("'");
		}
		if (StringUtils.isNotEmpty(summaryFormvo.getRpedate())) {
			buf.append(" and xreptime <='").append(summaryFormvo.getRpedate()).append("'");
		}
		if (StringUtils.isNotEmpty(summaryFormvo.getEcity())) {
			buf.append(" and ecity like '%").append(summaryFormvo.getEcity().trim()).append("%'");
		}
		if (StringUtils.isNotEmpty(summaryFormvo.getEregion())) {
			buf.append(" and eregion like '%").append(summaryFormvo.getEregion().trim()).append("%'");
		}
		//有城市或区县条件时
/*		if(StringUtils.isNotEmpty(summaryFormvo.getBcity())||StringUtils.isNotEmpty(summaryFormvo.getBregion())){
			StringBuffer add=new StringBuffer("from TBaseInfo where  1=1 ");
			//判断是否有城市条件和区县
			if (StringUtils.isNotEmpty(summaryFormvo.getBcity())) {
				add.append(" and bcity  like '%"+summaryFormvo.getBcity().trim()+"%'");
			}
			//判断是否有区县条件
			if(StringUtils.isNotEmpty(summaryFormvo.getBregion())){
				add.append(" and bregion like '%"+summaryFormvo.getBregion().trim()+"%'" );
			}
			List<TBaseInfo> bInfoList=baseDao.find(add.toString());
			if(bInfoList!=null&&bInfoList.size()>0){
				for(int i=0;i<bInfoList.size();i++){
					TBaseInfo binfo=bInfoList.get(i);
					if(i<bInfoList.size()-1){
						ids.append(binfo.getId()).append(",");
					}
					else{
						ids.append(binfo.getId());
					}
				}
				
			}
			
			if(StringUtils.isNotEmpty(ids.toString())){
				buf.append("and xequid in ("+ids.toString()+")");
			}
			else{
				buf.append(" and 1=0 ");
			}
		}*/
		
		buf.append(" order by id desc");
		
		
		
		
		System.out.println("findSummaryFormDatagrid查询： "+buf.toString());
		
		ResBundleUtil resourceBundle=new ResBundleUtil();

		
		//	HashMap<String, String> summaryMap=resourceBundle.getSummaryMap();
		//Set<String> mapSet =  summaryMap.keySet();	//获取所有的key值 为set的集合
		QueryResult<TInspectItemReport> queryResult = baseDao.getQueryResult(TInspectItemReport.class, buf.toString(), (page - 1) * rows, rows,null, null);
		List<SummaryFormVo> sfvoList=new ArrayList<SummaryFormVo>();
		
			try {
			
				if (queryResult != null && queryResult.getResultList().size() > 0) {
					for (int i=0;i<queryResult.getResultList().size();i++) {
						//设备上报信息
						TInspectItemReport te= queryResult.getResultList().get(i);
						 TPlanTask task=(TPlanTask) baseDao.find1(" from TPlanTask where id="+te.getXtaskid());
							List<TBaseInfo> bInfoList=baseDao.find("from TBaseInfo where id="+te.getXequid());
							TGroup g=null;
							if(task!=null){
								g=(TGroup) baseDao.find1(" from TGroup where id="+task.getPgid());
							}
					
							//如果设备没有删除，即在设备表里面有对应的编号，则设备信息赋值给SummaryFormVo
					if (bInfoList !=null&&bInfoList.size()>0){
								TBaseInfo bInfo=bInfoList.get(0);
								SummaryFormVo sfvo=new SummaryFormVo();
								BeanUtils.copyProperties(bInfo,sfvo);
								sfvo.setItaskid(te.getXtaskid());
								if(task!=null){
									sfvo.setPname(task.getPname());
								}
								if(g!=null){
									sfvo.setXgname(g.getGname());
								}
								sfvo.setXreptime(te.getXreptime());
								//	System.out.println(sfvo.getId());
								sfvoList.add(sfvo);
							}
							//如果设备被删除，即在设备表里面没有对应的编号，则设备信息放入空
						/*	else{
								SummaryFormVo sfvo=new SummaryFormVo();
								if(task!=null){
									sfvo.setPname(task.getPname());
								}
								
								sfvoList.add(sfvo);
							}*/
					
						
					}
				} 
		
			
			}catch (Exception e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			} finally {}
			map.put("total",queryResult.getTotalRecord());
			map.put("come", (page - 1) * rows);
			map.put("to", rows * page);
			map.put("rows", sfvoList);
			return map;
	}
	/**
	 * 	统计报表查找某次任务某个设备的所有巡检项信息
	 * @author liao
	 */
	@Override
	public Map<String, Object> findSummaryFormDetialsDatagrid(SummaryFormVo summaryFormvo,
			int page, int rows, String qsql) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer buf = new StringBuffer(qsql);
		if (!StringUtils.isEmpty(summaryFormvo.getBnumber())) {
			buf.append(" and xequtnum ='").append(summaryFormvo.getBnumber()).append("'");
		}
		if (summaryFormvo.getItaskid()!=0) {
			buf.append(" and xtaskid =").append(summaryFormvo.getItaskid());
		}
		if (StringUtils.isNotEmpty(summaryFormvo.getBtype())) {
			buf.append(" and xequtype ='").append(summaryFormvo.getBtype()).append("'");
		}
		//获取指定设备指定任务下的设备上报信息
		QueryResult<TInspectItemReport> queryResult = baseDao.getQueryResult(TInspectItemReport.class, buf.toString(), (page - 1) * rows, rows,null, null);
		
		List<SummaryFormVo> evolist=new ArrayList<SummaryFormVo>();
		if (queryResult != null && queryResult.getResultList().size() > 0) {
			//巡检项记录
			for (TInspectItemReport te : queryResult.getResultList()) {
				List<TInspectItemDetailReport> detailsList=te.getInspectreportdetailmsgs();
						if(detailsList!=null&&detailsList.size()>0)
							for(TInspectItemDetailReport details:detailsList){
								SummaryFormVo evo = new SummaryFormVo();
								BeanUtils.copyProperties(details,evo);
								evo.setItaskid(te.getXtaskid());
								evolist.add(evo);
							}
					
					}
			}
		map.put("total", evolist.size());
		map.put("come", (page - 1) * rows);
		map.put("to", rows * page);
		map.put("rows", evolist);
		return map;
	}
	/**	//获取导出excel表的配置属性
	 * @author liao
	 */
	@Override
	public Map<String, Object> editExcel(SummaryConfigVo summaryConfigvo, int page,
			int rows, String qsql) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer buf = new StringBuffer(qsql);
			buf.append(" and flag =").append(summaryConfigvo.getFlag());
		//excel表内容
		QueryResult<TSummaryConfig> queryResult = baseDao.getQueryResult(TSummaryConfig.class, buf.toString(), (page - 1) * rows, rows,null, null);
		List<SummaryConfigVo> evolist=new ArrayList<SummaryConfigVo>();
		if (queryResult != null && queryResult.getResultList().size() > 0) {
			for (TSummaryConfig sc : queryResult.getResultList()) {
				SummaryConfigVo scVo = new SummaryConfigVo();
				//BeanUtils.copyProperties(sc,scVo);
				MyBeanUtils.copyProperties(scVo, sc);
				evolist.add(scVo);
			}
		}
		map.put("total", evolist.size());
		map.put("rows", evolist);
		return map;
	}
	@Override
	public void Test(){/*
		String config="地排（块）,HI1;RRU数量以及所属系统数量,GO1;RRU数量以及所属系统所在平台,GM1;GSM900驻波比TX1,CZ1;GSM900驻波比TX2,DA1;GSM900驻波比TX3,DB1;馈线WCDMA厂家,CM1;其他,HH1;天线抱杆（包括GPS抱杆）是否紧固牢固,o4;天线半功率角GSM900TX2,GD1;RRU数量以及所属系统供电模式2,GT1;天线半功率角GSM900TX3,GE1;RRU数量以及所属系统供电模式1,GP1;天线GSM900厂家,BO1;调整前测量结果偏南,AV1;螺丝,HM1;铁塔塔件无缺损、锈蚀、松动,BH1;馈线WCDMA长度,CP1;GSM1800M驻波比TX3,DZ1;天线半功率角WCDMATX2,GJ1;GSM1800M驻波比TX2,DY1;天线半功率角WCDMATX3,GK1;GSM1800M驻波比TX1,DX1;天线半功率角WCDMATX1,GI1;天线WCDMA厂家所在平台,CB1;抱杆占用情况统计空闲数量2,ab1;抱杆占用情况统计空闲数量1,y4;GSM900M天线俯仰角TX2,CU1;抱杆占用情况统计所属平台1,w4;GSM900M天线俯仰角TX3,CV1;天线GSM900电调/机械,BS1;铁塔螺栓无缺损、锈蚀、松动,BF1;单/双极化天线WCDMATX2,FR1;馈线接地（根）,HJ1;单/双极化天线WCDMATX1,FQ1;回水弯状态,m4;抱杆占用情况统计所属平台2,z4;WCDMA天线方位角RX3,FA1;WCDMA天线方位角RX2,EZ1;GSM900M天线俯仰角TX1,CT1;WCDMA天线方位角RX1,EY1;馈线GSM1800厂家,CI1;RRU数量以及所属系统所在平台1,GQ1;RRU数量以及所属系统所在平台2,GU1;RRU数量以及所属系统供电模式,GL1;调整前测量结果偏北,AW1;天线GSM1800厂家,BT1;天线半功率角GSM900TX1,GC1;WCDMA天线方位角TX1,EM1;铁塔垂直度指标（1/1500）,AU1;WCDMA天线方位角TX3,EO1;RRU维护电源线是否完好,GY1;WCDMA天线方位角TX2,EN1;馈线GSM1800长度,CL1;胶泥（卷）,HE1;RRU数量以及所属系统系统2,GV1;RRU数量以及所属系统系统1,GR1;单/双极化天线WCDMATX3,FS1;GSM900M天线方位角TX3,CS1;GSM1800M抱杆垂直度TX3,DW1;GSM1800M抱杆垂直度TX2,DV1;GSM1800M抱杆垂直度TX1,DU1;GSM900M天线方位角TX1,CQ1;GSM900M天线方位角TX2,CR1;硬化地面,HB1;天线GSM1800电调/机械,BX1;调整前测量结果倾东,AX1;天线GSM900型号,BP1;天线GSM900数量,BQ1;GSM1800M天线方位角RX2,EB1;GSM1800M天线方位角RX1,EA1;GSM1800M天线方位角RX3,EC1;维护记录第二季度维护记录,HQ1;塔基础,HA1;馈线GSM900数量,CG1;测量铁塔接地电阻,BK1;天线与抱杆固定是否牢固,ac1;维护记录第三季度维护记录,HR1;维护记录第四季度维护记录,HS1;GPS天线无阻挡；无漏水现,AN1;油漆（kg）,HG1;塔放供电系统是否正常,AS1;天线GSM1800所在平台,BW1;天线WCDMA厂家数量,CA1;馈线接地线固定是否良好,AI1;天线紧固件有无缺损、锈蚀,AD1;铁塔镀锌观感质量、有无锈蚀现象,BD1;铁塔避雷系统下引线连接是否可靠、是否锈蚀、丢失,r4;RRU维护1/2馈线是否完好,GX1;天线WCDMA厂家电调/机械,CC1;WCDMA天线俯仰角TX2,EQ1;GSM900M驻波比RX1,DL1;GSM900M驻波比RX3,DN1;GSM900M驻波比RX2,DM1;是否已经二次浇注,BJ1;馈线GSM900型号,CF1;塔放运行状态是否正常,AT1;黄油（kg）,HF1;WCDMA天线俯仰角TX3,ER1;馈线与天线接口处密封是否良好,AF1;馈线GSM1800型号,CJ1;抱杆占用情况统计占用数量,u4;RRU维护光纤是否完好,GZ1;天线增益GSM1800TX3,FY1;天线WCDMA厂家型号,BZ1;抱杆占用情况统计空闲数量,v4;天线增益GSM1800TX2,FX1;主接地（米、根）,HK1;调整后测量结果倾西,BC1;天线增益GSM1800TX1,FW1;天线半功率角GSM1800TX1,GF1;角铁,HN1;天线GSM1800数量,BV1;天线半功率角GSM1800TX3,GH1;天线半功率角GSM1800TX2,GG1;拉线（米、根）,HL1;清理天线周围杂物、障碍物,AQ1;WCDMA驻波比TX1,EV1;WCDMA驻波比TX2,EW1;WCDMA驻波比TX3,EX1;天线紧固件齐全，无缺损、锈蚀等现象,AO1;馈线接地处密封是否良好,AG1;抱杆占用情况统计占用数量2,aa1;抱杆占用情况统计占用数量1,x4;扎带（根）,HC1;铁塔建成（验收）日期,BN1;铁塔周围及塔体上无杂物、垃圾、废料,BE1;航标灯、避雷器工作是否正常,n4;GSM900M抱杆垂直度RX2,DJ1;维护记录第一季度维护记录,HP1;GSM900M抱杆垂直度RX3,DK1;GSM900M抱杆垂直度RX1,DI1;天线GSM900所在平台,BR1;天线WCDMA厂家厂家,BY1;GSM900M抱杆垂直度TX1,CW1;调整后测量结果偏北,BA1;GSM900M抱杆垂直度TX3,CY1;GSM900M抱杆垂直度TX2,CX1;馈线GSM1800数量,CK1;RRU数量以及所属系统系统,GN1;GSM900M天线俯仰角RX3,DH1;馈线WCDMA型号,CN1;GSM900M天线俯仰角RX2,DG1;天线GSM1800型号,BU1;GSM900M天线俯仰角RX1,DF1;单/双极化天线GSM1800TX3,FP1;馈线卡子有无损缺有无锈蚀,AM1;铁塔基础回填夯实、无下陷,BI1;单/双极化天线GSM1800TX1,FN1;单/双极化天线GSM1800TX2,FO1;天线增益WCDMATX2,GA1;天线增益WCDMATX3,GB1;铁塔螺母无缺损、锈蚀、松动、开裂,BG1;胶带（卷）,HD1;天线天线挂高,CD1;天线增益WCDMATX1,FZ1;WCDMA驻波比RX1,FH1;WCDMA驻波比RX2,FI1;WCDMA驻波比RX3,FJ1;GSM1800M天线俯仰角TX3,DT3;GSM1800M天线俯仰角TX2,DS1;铁塔和平台无锈蚀铁丝、螺丝、螺栓等杂物,q4;抱杆数量,BM1;塔放外、内部是否标签正确，有无腐蚀,AR1;平台处的馈线、接地线走线是否规范,AH1;GSM1800M天线俯仰角TX1,DR1;调整后测量结果偏南,AZ1;GSM1800M天线俯仰角RX2,EE1;GSM900M天线方位角RX3,DE1;GSM1800M天线俯仰角RX3,EF1;GSM1800M抱杆垂直度RX1,EG1;GSM900M天线方位角RX2,DD1;GSM1800M抱杆垂直度RX2,EH1;GSM900M天线方位角RX1,DC1;GSM1800M天线俯仰角RX1,ED1;抱杆占用情况统计所属平台,t4;GSM1800M抱杆垂直度RX3,EI1;RRU数量以及所属系统数量1,GS1;天线增益GSM900TX1,FT1;天线增益GSM900TX2,FU1;天线增益GSM900TX3,FV1;WCDMA天线俯仰角TX,EP1;WCDMA天线俯仰角RX2,FC1;WCDMA天线俯仰角RX1,FB1;WCDMA天线俯仰角RX3,FD1;供电系统（市电，远供电源，市电加远供电源）,HO1;馈线窗处密封是否良好,AJ1;RRU数量以及所属系统数量2,GW1;WCDMA抱杆垂直度TX1,ES1;WCDMA抱杆垂直度TX2,ET1;WCDMA抱杆垂直度TX3,EU1;WCDMA抱杆垂直度RX1,FE1;WCDMA抱杆垂直度RX2,FF1;WCDMA抱杆垂直度RX3,FG1;GSM1800M天线方位角TX3,DQ1;GSM1800M天线方位角TX1,DO1;馈线GSM900厂家,CE1;GSM1800M天线方位角TX2,DP1;馈线GSM900长度,CH1;天线、馈线外观有无损伤,AE1;铁塔平台数,BL1;铁塔警示牌（广告牌）是否固定紧固,p4;GSM1800M驻波比RX2,EK1;GSM1800M驻波比RX3,EL1;GSM1800M驻波比RX1,EJ1;馈线窗处接地系统是否良好,AK1;调整后测量结果倾东,BB1;调整前测量结果倾西,AY1;馈线卡子紧固情况是否良好,AL1;检查爬梯、平台、过桥是否牢固,s4;馈线WCDMA数量,CO1;单/双极化天线GSM900TX1,FK1;天线无漏水，天线各端口的密封程良好,AP1;单/双极化天线GSM900TX2,FL1;单/双极化天线GSM900TX3,FM1";
		//总共215条数据
		String[] c=config.split(";");
		for(int i=0;i<c.length;i++){
			String[] c1=c[i].split(",");
			TSummaryConfig sc=new TSummaryConfig();
			sc.setFlag(1);
			sc.setSname(c1[0]);
			sc.setScell(c1[1]);
			baseDao.save(sc);
		}
	*/}
	
	
	
	@Override
	public String saveConfigList(List<Object> db,int entid,int flag){
		int count1=0;
		  for(int i=0;i<db.size();i++){
			  TSummaryConfig con= (TSummaryConfig) db.get(i);
			  con.setEntid(entid);
			  String hql="";
				  baseDao.save(db.get(i));
				  count1++;
		  }
		  return String.valueOf(count1);
	}
	
	/**
	 * 获取统计配置表的配置信
	 * @param flag//1表示铁塔总账，2表示室内总账，3表示铁塔详情，4表示室内详情
	 * @return
	 */
	@Override
	public String getSConfig(int flag){
		HashMap<String, String> sconfigMap=new HashMap<String, String>();
	
			List<TSummaryConfig> sconfigList=baseDao.find("from TSummaryConfig where flag="+flag);
			StringBuffer buf=new StringBuffer();
			String str=null;
			if(sconfigList!=null&&sconfigList.size()>0){
				for(TSummaryConfig sconfig:sconfigList){
					buf.append(sconfig.getSname()).append(",").append(sconfig.getScell()).append(";");
				}
				str=buf.toString().substring(0,buf.toString().length()-1);
			}
		return str;
	}
	
	
	/**用于数据统计的总账导出
	 * 获取查询base_info表中的主键的主键为getbidsSet和getbidsSet1集合的交集
	 */
	@Override
	public String getbids(SummaryFormVo summaryFormvo,String qsql){
		Map<String, Object> map = new HashMap<String, Object>();
		//根据巡检任务id，周期，维护队，设备类型查询的语句
		StringBuffer buf = new StringBuffer(qsql);
		
		boolean flag=true;
		if(summaryFormvo.getItaskid()==0||StringUtils.isEmpty(summaryFormvo.getRpsdate())||StringUtils.isEmpty(summaryFormvo.getRpedate())||StringUtils.isEmpty(summaryFormvo.getRpedate())||summaryFormvo.getXgid()==0){
			flag=false;
		}
		if (summaryFormvo.getItaskid()!=0) {
			buf.append(" and id =").append(summaryFormvo.getItaskid());
		}
		if (summaryFormvo.getEntid()!=0) {
			buf.append(" and entid =").append(summaryFormvo.getEntid());
		}
		if (StringUtils.isNotEmpty(summaryFormvo.getRpsdate())) {
			buf.append(" and pstartdate >='").append(summaryFormvo.getRpsdate()).append("'");
		}
		if (StringUtils.isNotEmpty(summaryFormvo.getRpedate())) {
			buf.append(" and pstartdate <='").append(summaryFormvo.getRpedate()).append("'");
		}
		if (summaryFormvo.getXgid()!=0) {
			buf.append(" and pgid =").append(summaryFormvo.getXgid());
		}
		buf.append(" order by bcity,bregion desc");
		//System.out.println("buf="+buf.toString());
		//根据巡检单位，城市和设备编号查询的语句
		StringBuffer buf1 = new StringBuffer(qsql);
		Set<Integer> set=null;
		if(flag==true){
			//当没有条件时，不执行此方法
		//通过所属班组或任务id或巡检周期获取对应的baseinfo主键
			set=this.getbidsSet(summaryFormvo, buf.toString());
		}
		if (summaryFormvo.getEntid()!=0) {
			buf1.append(" and entid =").append(summaryFormvo.getEntid());
		}
		
		if (StringUtils.isNotEmpty(summaryFormvo.getXequtnum())) {
			buf1.append(" and bnumber like '%").append(summaryFormvo.getXequtnum().trim()).append("%'");
		}
		if (StringUtils.isNotEmpty(summaryFormvo.getBcity())) {
			buf1.append(" and bcity like '%").append(summaryFormvo.getBcity().trim()).append("%'");
		}
		if(StringUtils.isNotEmpty(summaryFormvo.getBregion())){
			buf1.append(" and bregion like '%").append(summaryFormvo.getBregion().trim()).append("%'");
		}
		if (StringUtils.isNotEmpty(summaryFormvo.getBtype())) {
			buf1.append(" and btype ='").append(summaryFormvo.getBtype()).append("'");
		}
		buf1.append(" order by bcity,bregion desc");
//		System.out.println("buf1="+buf1.toString());
		
		//通过所属单位，城市，设备编号查询beaseinfo表的主键
		Set<Integer> set1=this.getbidsSet1(summaryFormvo, buf1.toString());
		
		StringBuffer buf2=new StringBuffer();
		//如果没有巡检任务id，周期，维护队，设备类型中的任何一项，则表示不需要通过巡检计划来查询base_info的主键id，只需根据set1获取base_info的主键id即可
		
		if(flag==false){
			int index=0;
			Iterator<Integer> iter=set1.iterator();
			int size=set1.size();
			while(iter.hasNext()){
				index++;
			Integer id=iter.next();
			if(size==index){
				buf2.append(id);
			}
			else{
				buf2.append(id).append(",");
			}
			}
		}
		else{
			Iterator<Integer> iter=set.iterator();
			//获取其中一个set的size
			int size=set.size();
			int index=0;
			//获取两个set集合的交集
			while(iter.hasNext()){
				index++;
				Integer id=iter.next();
				if(set1.contains(id)){
					if(size==index){
						buf2.append(id);
					}
					else{
						buf2.append(id).append(",");
					}
				}
			}
		}
//	System.out.println("ids="+buf2.toString());
		return buf2.toString();
	}
	/**（此时通过所属班组或任务id或巡检周期获取对应的baseinfo主键）
	 * 通过任务或巡检周期导出总账
	 * @return
	 */
	public Set<Integer> getbidsSet(SummaryFormVo sfVo,String sql){
		Set<Integer> bidsSet=new HashSet<Integer>();
		StringBuffer buf=new StringBuffer(sql);
		 //获取任务
	
		buf.append(" order by id desc");
		//System.out.println(" from TplanTask where "+buf.toString());
		 List<TPlanTask> ptasklist=baseDao.find(" from TPlanTask where "+buf.toString());
		 if(ptasklist!=null&&ptasklist.size()>0){
			 //查询巡检计划
			 for(TPlanTask planTask:ptasklist){
				TermPlanVo planVo2=new TermPlanVo();
				int lineId1=planTask.getPlineid();
			    String lineId=String.valueOf(lineId1);
			    //获取任务计划中的线路实体
					if(lineId!=null){
					    TLine line=inspectItemService.getLine(lineId);
					    //line
						if(line!=null){
							//获取线路中的巡检点
								 List<TLinePoint> linepoints=line.getLinepoints();
//								 planVo2.setId(line.getId());
//								 planVo2.setItaskid(planTask.getId());
//								 planVo2.setEntid(line.getEntid());
//								 planVo2.setLname(line.getLname());
								 if(linepoints!=null&&linepoints.size()!=0){
									 for(TLinePoint tlinePoint:linepoints){
										// List<TermPointVo> tpointVoList=new ArrayList<TermPointVo>();
										 TPoint point=tlinePoint.getTpoint();
										 //TermPointVo tpointVo=new TermPointVo();
										// tpointVo.setPointid(point.getId());
										 //获取巡检点下面的设备
										 Set<TPointEquipment> pointequipments =point.getPointequipments();
										 if(pointequipments!=null&&pointequipments.size()!=0){ 
											  for(TPointEquipment pointEquipment:pointequipments){
												  	//通过设备信息找到对应的基础信息主键
												  TEquipment equipment=pointEquipment.getTequipment();
												  if(equipment!=null){
													  Set<TBaseInfoEquipment> eSet=  equipment.getBaseinfoequipments();
													 
														  Iterator<TBaseInfoEquipment> beset=eSet.iterator();
														  if(beset.hasNext()){
															  TBaseInfoEquipment be=(TBaseInfoEquipment)beset.next();
															  //将t_base_info装进来
															 bidsSet.add(be.getTbaseinfo().getId());
														  }
													  
												  }
											  }
										 }
									
									 }
								 }
							}
					}
		   }
		
		}
		 return bidsSet;
	}
	/**（通过所属单位，城市，设备编号查询beaseinfo表的主键）
	 * 通过任务或巡检周期导出总账
	 * @param sfVo
	 * @param sql
	 * @return
	 */
	public Set<Integer> getbidsSet1(SummaryFormVo sfVo,String sql){
		StringBuffer buf=new StringBuffer(sql);
		Set<Integer> bidsSet=new HashSet<Integer>();
		buf.append(" order by bcity,bregion,id");
		//System.out.println(" from TBaseInfo where "+buf.toString());
		List<TBaseInfo> blist=baseDao.find(" from TBaseInfo where "+buf.toString());
		if(blist!=null&&blist.size()>0){
			for(int i=0;i<blist.size();i++){
				bidsSet.add(blist.get(i).getId());
			}
		}
		return bidsSet;
	}
}
