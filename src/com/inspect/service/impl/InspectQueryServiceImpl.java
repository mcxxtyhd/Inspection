package com.inspect.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.hibernate.SQLQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.inspect.dao.BaseDaoI;
import com.inspect.model.baseinfo.TBaseInfo;
import com.inspect.model.basis.TEquipment;
import com.inspect.model.basis.TGroup;
import com.inspect.model.basis.TInspectUser;
import com.inspect.model.basis.TLine;
import com.inspect.model.basis.TLinePoint;
import com.inspect.model.basis.TPlanTask;
import com.inspect.model.basis.TPoint;
import com.inspect.model.basis.TPointEquipment;
import com.inspect.model.basis.TProject;
import com.inspect.model.basis.TProjectGroup;
import com.inspect.model.monitor.TInspectItemDetailReport;
import com.inspect.model.monitor.TInspectItemRaltionReport;
import com.inspect.model.monitor.TInspectItemReport;
import com.inspect.model.monitor.TTerminateStatusReport;
import com.inspect.model.system.Enterprise;
import com.inspect.model.system.Parameter;
import com.inspect.service.InspectItemServiceI;
import com.inspect.service.InspectQueryServiceI;
import com.inspect.service.SystemServiceI;
import com.inspect.util.common.DateUtils;
import com.inspect.util.common.QueryResult;
import com.inspect.util.common.StringUtils;
import com.inspect.vo.basis.PlanQueryVo;
import com.inspect.vo.monitor.InspectItemReportVo;
import com.inspect.vo.monitor.TerminaStatusReportVo;
import com.itextpdf.text.log.SysoCounter;

@Service("inspectQueryService")
@Transactional(propagation = Propagation.REQUIRED,readOnly = false,rollbackFor = Exception.class)
public class InspectQueryServiceImpl implements InspectQueryServiceI {
	@Resource
	private BaseDaoI baseDao;
	@Resource
	private SystemServiceI systemService;
	@Resource
	private InspectItemServiceI inspectItemService;
	public BaseDaoI getBaseDao() {
		return baseDao;
	}
	public void setBaseDao(BaseDaoI baseDao) {
		this.baseDao = baseDao;
	}
	
	//巡检终端状态查询
	public Map<String, Object> findStatusDatagrid(TerminaStatusReportVo terminastatusrepvo, int page, int rows,String qsql) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer buf = new StringBuffer(qsql);
		if (terminastatusrepvo.getRpgroupid()!=0) {
			buf.append(" and rpgroupid =").append(terminastatusrepvo.getRpgroupid());
		}
		if (terminastatusrepvo.getRpuserid()!=0) {
			buf.append(" and rpuserid =").append(terminastatusrepvo.getRpuserid());
		}
		if(!StringUtils.isEmpty(terminastatusrepvo.getRpterminateid())){
			buf.append(" and rpterminateid like'%").append(terminastatusrepvo.getRpterminateid()).append("%'");
		}
		if(!StringUtils.isEmpty(terminastatusrepvo.getRpsdate())){
			buf.append(" and rplogintime >='").append(terminastatusrepvo.getRpsdate()).append("'");
		}
		if(!StringUtils.isEmpty(terminastatusrepvo.getRpedate())){
			buf.append(" and rplogintime <='").append(terminastatusrepvo.getRpedate()).append("'");
		}
		buf.append(" order by rplogintime desc");
		QueryResult<TTerminateStatusReport> queryResult = baseDao.getQueryResult(TTerminateStatusReport.class, buf.toString(), (page - 1) * rows, rows,null, null);
		List<TerminaStatusReportVo> evolist=new ArrayList<TerminaStatusReportVo>();
		if (queryResult != null && queryResult.getResultList().size() > 0) {
			for (TTerminateStatusReport te : queryResult.getResultList()) {
				TerminaStatusReportVo evo = new TerminaStatusReportVo();
				BeanUtils.copyProperties(te,evo);
				evo.setRpgroupname(baseDao.get(TGroup.class,te.getRpgroupid())!=null?baseDao.get(TGroup.class,te.getRpgroupid()).getGname():"");
				evo.setRpusername(baseDao.get(TInspectUser.class,te.getRpuserid())!=null?baseDao.get(TInspectUser.class,te.getRpuserid()).getIuname():"");
				evo.setRplinename(baseDao.get(TLine.class,te.getRplineid())!=null?baseDao.get(TLine.class,te.getRplineid()).getLname():"");
				evolist.add(evo);
			}
		}
		map.put("total", queryResult.getTotalRecord());
		map.put("rows", evolist);
		return map;
	}

	//巡检数据查询
	@Override
	public Map<String, Object> findInspectInfoDatagrid(InspectItemReportVo inspectitempvo, int page, int rows,String qsql,String buf1) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer buf = new StringBuffer(qsql);
		if(StringUtils.isEmpty(inspectitempvo.getQyerytype())){
			buf.append(" and 1=0");
		}
		if (inspectitempvo.getEntid()!=0) {
			buf.append(" and entid =").append(inspectitempvo.getEntid());
		}
		if(StringUtils.isNotEmpty(inspectitempvo.getXstatus())){
			buf.append(" and xstatus='").append(inspectitempvo.getXstatus()).append("'");
		}
		if (inspectitempvo.getItaskid()!=0) {
			buf.append(" and xtaskid =").append(inspectitempvo.getItaskid());
		}
		if (inspectitempvo.getXequid()!=null) {
			buf.append(" and xequid =").append(inspectitempvo.getXequid());
		}
		if (!StringUtils.isEmpty(inspectitempvo.getPstartdate())) {
			String tid=getTaskIdByDate1(inspectitempvo.getPstartdate());
			if(!StringUtils.isEmpty(tid)){
				buf.append(" and xtaskid in (").append(tid).append(")");
			}else{
				buf.append(" and xtaskid =0");
			}
		}
		if (inspectitempvo.getXgid()!=0) {
			buf.append(" and xgid =").append(inspectitempvo.getXgid());
		}
//		if (inspectitempvo.getXuid()!=0) {
//			buf.append(" and xuid =").append(inspectitempvo.getXuid());
//		}
		if(!StringUtils.isEmpty(inspectitempvo.getRpsdate())){
			buf.append(" and xreptime >='").append(inspectitempvo.getRpsdate()).append("'");
		}
		if(!StringUtils.isEmpty(inspectitempvo.getRpedate())){
			buf.append(" and xreptime <='").append(inspectitempvo.getRpedate()).append("'");
		}
		//当设备名称没有值时
		if(StringUtils.isEmpty(inspectitempvo.getXename())){
			buf.append(" and 1=1 ");//inspectmessageVo.getXename()
		}
		//当设备名称作为查询条件时
		else{
			//当输入设备名称并找到对应的设备时,buf1.length()>0,后则buf1.length()=0
			 if(buf1.length()>0){
				buf.append(" and id in (").append(buf1.toString()).append(")");
			}
			
			else {
				buf.append(" and 1=0 ");//inspectmessageVo.getXename()
			}
		}
		buf.append(" order by xreptime desc");
		QueryResult<TInspectItemReport> queryResult = baseDao.getQueryResult(TInspectItemReport.class, buf.toString(), (page - 1) * rows, rows,null, null);
		List<InspectItemReportVo> evolist=new ArrayList<InspectItemReportVo>();
		Parameter param1=systemService.getParameter("铁塔图片数量");
		Parameter param2=systemService.getParameter("室内图片数量");
		if (queryResult != null && queryResult.getResultList().size() > 0) {
			for (TInspectItemReport te : queryResult.getResultList()) {
				InspectItemReportVo evo = new InspectItemReportVo();
				TPlanTask ptsk=null;
				try {
					BeanUtils.copyProperties(te,evo);
					 ptsk=baseDao.getEntityById(TPlanTask.class,te.getXtaskid());
				} catch (Exception e) {
					e.printStackTrace();
					// TODO: handle exception
				}
			
				if(ptsk!=null){
					evo.setXtaskname(ptsk.getPname());
					evo.setPstartdate(ptsk.getPstartdate());
					evo.setPenddate(ptsk.getPenddate());
				}
				//组名称
				evo.setXgname(baseDao.get(TGroup.class,te.getXgid())!=null?baseDao.get(TGroup.class,te.getXgid()).getGname():"");
//				evo.setXuname(baseDao.get(TInspectUser.class,te.getXuid())!=null?baseDao.get(TInspectUser.class,te.getXuid()).getIuname():"");
				//设备名称
				evo.setXequname(baseDao.getEntityById(TBaseInfo.class,te.getXequid())!=null?baseDao.getEntityById(TBaseInfo.class,te.getXequid()).getBname():"");
				//计算问题上报个数
				long errorcount=baseDao.count("select count(*) from TInspectProblem t where t.proitaskid="+te.getXtaskid()+" and prosite="+te.getXequid());
				int picsum=0;//参数里面图片的数量
				int picnum=0;//上报表里面上传的图片数量
				//铁塔类型
				if("1".equals(te.getXequtype())){
					picsum=Integer.parseInt(param1.getPvalue());
				}
				//室内类型
				else if("2".equals(te.getXequtype())){
					picsum=Integer.parseInt(param2.getPvalue());
				}
				if(te.getXimgname()!=null){
					picnum=te.getXimgname().split(",").length;
				}
				int excepflag=picsum-picnum;//大于0，表示图片没有上传完成
				if(excepflag>0||errorcount>0){
					evo.setFlag(true);
				}
				else {
					evo.setFlag(false);
				}
				evolist.add(evo);
			}
		}
		map.put("total", queryResult.getTotalRecord());
		map.put("rows", evolist);
		return map;
	}
	
	/**
	 * 用于地图中显示设备的所有信息（包括设备下的巡检项）
	 */
	@Override
	public Map<String, Object> findInspectInfoDatagrid1(InspectItemReportVo inspectitempvo, int page, int rows,String qsql) {

		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer buf = new StringBuffer(qsql);
		if (!StringUtils.isEmpty(inspectitempvo.getXequtnum())) {
			buf.append(" and xequtnum ='").append(inspectitempvo.getXequtnum()).append("'");
		}
		if (inspectitempvo.getXgid()!=0) {
			buf.append(" and xgid =").append(inspectitempvo.getXgid());
		}
		if (inspectitempvo.getXuid()!=0) {
			buf.append(" and xuid =").append(inspectitempvo.getXuid());
		}
		if(!StringUtils.isEmpty(inspectitempvo.getRpsdate())){
			buf.append(" and xreptime >='").append(inspectitempvo.getRpsdate()).append(" 00:00:00'");
		}
		if(!StringUtils.isEmpty(inspectitempvo.getRpedate())){
			buf.append(" and xreptime <='").append(inspectitempvo.getRpedate()).append("23:59:59'");
		}
		QueryResult<TInspectItemReport> queryResult = baseDao.getQueryResult(TInspectItemReport.class, buf.toString(), (page - 1) * rows, rows,null, null);
		List<InspectItemReportVo> evolist=new ArrayList<InspectItemReportVo>();
		if (queryResult != null && queryResult.getResultList().size() > 0) {
			for (TInspectItemReport te : queryResult.getResultList()) {
				List<TInspectItemRaltionReport> inspectmsgdetailreports=te.getInspectmsgdetailreports();
				if(inspectmsgdetailreports!=null && inspectmsgdetailreports.size()>0){
					for(TInspectItemRaltionReport t:inspectmsgdetailreports){
						InspectItemReportVo evo = new InspectItemReportVo();
						BeanUtils.copyProperties(te,evo);
						TGroup g=baseDao.get(TGroup.class,te.getXgid());
						if(g!=null){
							evo.setXgname(g.getGname());
						}
						TInspectUser inspectUser=baseDao.get(TInspectUser.class,te.getXuid());
						if(inspectUser!=null&&inspectUser.getIuname()!=null){
							evo.setXuname(inspectUser.getIuname());
						}
						TLine line=baseDao.get(TLine.class,te.getXlid());
						if(line!=null&&line.getLname()!=null){
							evo.setXlname(line.getLname());
						}
						TEquipment e=getEquipmentByEnum(te.getXequtnum());
						if(e!=null){
							evo.setXequname(e.getEname());
						}
						evo.setId(t.getTinspectitemdetailrport().getId());
						evo.setXmaxvalue(t.getTinspectitemdetailrport().getXmaxvalue());
						evo.setXminvalue(t.getTinspectitemdetailrport().getXminvalue());
						evo.setXpvalue(t.getTinspectitemdetailrport().getXvalue());
						evo.setXproname(t.getTinspectitemdetailrport().getXproname());
						evo.setXreptime(t.getTinspectitemdetailrport().getXreptime());
						if(te.getXstatus()!=null&&te.getXstatus().equals("0")){
							evo.setXstatus("告警");
						}
						if(te.getXstatus()!=null&&te.getXstatus().equals("1")){
							evo.setXstatus("正常");
						}
						if(t.getTinspectitemdetailrport().getXimgname()!=null){
							evo.setXimgname(t.getTinspectitemdetailrport().getXimgname());
						}
						if(t.getTinspectitemdetailrport().getXimgurl()!=null){
							evo.setXimgurl(t.getTinspectitemdetailrport().getXimgurl());
						}
						evolist.add(evo);
					}
				}
			}
		}
		map.put("total", evolist.size());
		map.put("rows", evolist);
		return map;
}
	private TEquipment getEquipmentByEnum(String eqnum){
		TEquipment equip=null;
		String hql="from TEquipment where enumber='"+ eqnum+"'";
		equip=baseDao.findByHql(TEquipment.class,hql);
		return equip;
	}
	
	@Override
	public void editReportMsgStatus(TInspectItemReport rmsg){
		String hql="update t_report_message set xstatus='"+rmsg.getXstatus() +"' where id="+rmsg.getId();
		baseDao.getJdbcTemplate().execute(hql);
	}
	

	@Override
	public TInspectItemReport getItemReport(String mid){
		if (StringUtils.isEmpty(String.valueOf(mid))) {
			return null;
		}
		return baseDao.get(TInspectItemReport.class, Integer.parseInt(mid));
	}
	
	//巡检数据详细查询
	public Map<String, Object> findMessageInfoDatagrid(InspectItemReportVo inspectitempvo, int page, int rows,String qsql) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer buf = new StringBuffer(qsql);
		buf.append(" and msgid=").append(inspectitempvo.getXmid());
		buf.append(" order by xreptime DESC, xprogid ,xproid ");
		QueryResult<TInspectItemDetailReport> queryResult = baseDao.getQueryResult(TInspectItemDetailReport.class, buf.toString(), (page - 1) * rows, rows,null, null);
		List<InspectItemReportVo> evolist=new ArrayList<InspectItemReportVo>();
		if (queryResult != null && queryResult.getResultList().size() > 0) {
			for (TInspectItemDetailReport te : queryResult.getResultList()) {
				InspectItemReportVo evo = new InspectItemReportVo();
				BeanUtils.copyProperties(te, evo);
				evo.setXpvalue(te.getXvalue());
				evo.setXptype(te.getXtype());
				evo.setXreptime(te.getXreptime());
				evo.setXpgname(baseDao.getEntityById(TProjectGroup.class,te.getXprogid())!=null?baseDao.getEntityById(TProjectGroup.class,te.getXprogid()).getPgname():"");
				evolist.add(evo);
			}
		}
		map.put("total",queryResult.getTotalRecord());
		map.put("rows", evolist);
		return map;
	}
	public List<InspectItemReportVo> findInspectDetailInfo(InspectItemReportVo inspectitempvo) {
		String hql=" from TInspectItemReport where 1=1";
		StringBuffer buf=new StringBuffer(hql);
		buf.append(" and id=").append(inspectitempvo.getXmid());
		List<TInspectItemReport> queryResult =baseDao.find(buf.toString());
		List<InspectItemReportVo> evolist=new ArrayList<InspectItemReportVo>();
		if (queryResult != null && queryResult.size() > 0) {
			for (TInspectItemReport te : queryResult) {
				List<TInspectItemRaltionReport> inspectmsgdetailreports=te.getInspectmsgdetailreports();
				if(inspectmsgdetailreports!=null && inspectmsgdetailreports.size()>0){
					for(TInspectItemRaltionReport t:inspectmsgdetailreports){
						InspectItemReportVo evo = new InspectItemReportVo();
						BeanUtils.copyProperties(te,evo);
						evo.setXmaxvalue(t.getTinspectitemdetailrport().getXmaxvalue());
						evo.setXminvalue(t.getTinspectitemdetailrport().getXminvalue());
						evo.setXpvalue(t.getTinspectitemdetailrport().getXvalue());
						evo.setXproname(t.getTinspectitemdetailrport().getXproname());
						evo.setXreptime(t.getTinspectitemdetailrport().getXreptime());
						if(te.getXstatus()!=null&&te.getXstatus().equals("0")){
							evo.setXstatus("告警");
						}
						if(te.getXstatus()!=null&&te.getXstatus().equals("1")){
							evo.setXstatus("正常");
						}
						evo.setXuname(baseDao.get(TInspectUser.class,te.getXuid()).getIuname());
						TEquipment e=getEquipmentByEnum(te.getXequtnum());
						if(e!=null){
							evo.setXequname(e.getEname());
						}
						evo.setId( t.getTinspectitemdetailrport().getId() ) ;
						evolist.add(evo);
					}
				}
			}
		}
		return evolist;
	}

	@Override
	public TInspectItemDetailReport getTInspectItemDetailReport( Integer id  ) {
		return baseDao.get( TInspectItemDetailReport.class , id ) ;
	}
	//巡检数据统计
	@Override
	public Map<String, Object> findInspectSummaryInfoDatagrid(PlanQueryVo planQueryvo, int page, int rows,String qsql) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer buf = new StringBuffer(qsql);
		if(StringUtils.isEmpty(planQueryvo.getQyerytype())){
			buf.append(" and 1=0");
		}
		if (planQueryvo.getEntid()!=0) {
			buf.append(" and entid =").append(planQueryvo.getEntid());
		}
		if (planQueryvo.getItaskid()!=0) {
			buf.append(" and id =").append(planQueryvo.getItaskid());
		}
		if (planQueryvo.getPgid()!=0) {
			buf.append(" and pgid =").append(planQueryvo.getPgid());
		}
		if(!StringUtils.isEmpty(planQueryvo.getPstartdate())){
			buf.append(" and pstartdate >='").append(planQueryvo.getPstartdate()).append("'");
		}
		if(!StringUtils.isEmpty(planQueryvo.getPenddate())){
			buf.append(" and pstartdate <='").append(planQueryvo.getPenddate()).append("'");
		}
		buf.append(" order by id desc");
		QueryResult<TPlanTask> queryResult = baseDao.getQueryResult(TPlanTask.class, buf.toString(), (page - 1) * rows, rows,null, null);
		List<PlanQueryVo> evolist=new ArrayList<PlanQueryVo>();
		if (queryResult != null && queryResult.getResultList().size() > 0) {
			for (TPlanTask te : queryResult.getResultList()) {
				PlanQueryVo evo = new PlanQueryVo();
				BeanUtils.copyProperties(te,evo);
				TLine line=baseDao.getEntityById(TLine.class,te.getPlineid());
				List<TLinePoint> tlp= line.getLinepoints();
				int ecount=0;//巡检线路下设备数量
				if(tlp!=null && tlp.size()>0){
					for(TLinePoint lp:tlp){
						TPoint tp=lp.getTpoint();
						if(tp!=null){
							Set<TPointEquipment> tpe=tp.getPointequipments();
							ecount+=tpe.size();
						}
					}
					evo.setPointCount(ecount);
				}
				long qecount=baseDao.count("select count(*) from TInspectItemReport t where t.xtaskid="+te.getId());
				long errorcount=baseDao.count("select count(*) from TInspectProblem t where t.proitaskid="+te.getId());
				evo.setQueryCount((int)qecount);
				//为巡检设备数量
				//ecount=0;//巡检线路下设备数量
				evo.setUnqueryCount(ecount-(int)qecount);
				//设备异常个数(指的是问题上报的个数)
				evo.setErrqueryCount((int)errorcount);
			//	long   poor;
				double rate=0;
				//判断巡检线路里是否有设备
				//完成率
				if(ecount>0){
					rate=qecount*100/ecount;
					//判断巡检线路中的设备是否被删除过，如果没有，则rate>=0且rate<=100
					if(rate>=0){
						evo.setRate(String.valueOf(rate)+"%");
					}
					else{
						evo.setRate("空");
					}
				}else
				{
					evo.setRate("空");
				}
				
				//	 System.out.println(rate);
				evo.setGroupname(baseDao.getEntityById(TGroup.class,te.getPgid())!=null?baseDao.getEntityById(TGroup.class,te.getPgid()).getGname():"");
				//图片异常的个数
				int count=this.getPicnum(te.getId());
				//异常图片数量指的巡检过的设备中图片不达标的数量
				evo.setPicexcepnum(count);
				
				//巡检合格率
				double picrate=0;
				//判断巡检线路里是否有设备
				if(qecount>0){
					picrate=(qecount-count)*100/qecount;
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
				
				
//				evo.setUsername(baseDao.getEntityById(TInspectUser.class,te.getPuid())!=null?baseDao.getEntityById(TInspectUser.class,te.getPuid()).getIuname():"");
				evolist.add(evo);
			}
		}
		map.put("total", queryResult.getTotalRecord());
		map.put("rows", evolist);
		return map;
	}
	
	
	/**
	 * 首页统计一季度巡检完成情况
	 */
	@Override
	public Map<String, Object> findInspectSummaryInfoDatagrid1(PlanQueryVo planQueryvo, int page, int rows,String qsql) {
	//	long a11=System.currentTimeMillis();
		Parameter param1=systemService.getParameter("铁塔图片数量");
		//long a=System.currentTimeMillis();       
		int piclimit=Integer.parseInt(param1.getPvalue());
		Map<String, Object> map = new HashMap<String, Object>();
		List<PlanQueryVo> evolist=new ArrayList<PlanQueryVo>();
		
		//统计总计
		int totalcontractbasenumber =0;
		int totalplanbasenumber =0;
		int totaluninspectbasenumber =0;
		double  totalplanrate =0;
		double  totalpercentofpass = 0;
		double  totalfinishrate = 0;
		double  totaltotalfinishrate = 0;
		int totalplanfinished = 0;
		int totalerrornumber =0;
		int totalpicerrornumber =0;
		
		
		try {
			StringBuffer buf = new StringBuffer(qsql);
		//	buf.append(DateUtils.thisQuarter());
			buf.append(" order by entid asc");
			System.out.println("buf.toString():  "+buf.toString());
			QueryResult<TPlanTask> queryResult = baseDao.getQueryResult(TPlanTask.class, buf.toString(), -1, -1,null, null);
			//mapent 用于保存单位id和任务编号
			HashMap<Integer,String> mapent=new HashMap<Integer,String>();
			if (queryResult != null && queryResult.getResultList().size() > 0) {
				for (TPlanTask te : queryResult.getResultList()) {
					
					if(mapent.containsKey(te.getEntid())){
						StringBuffer taskid=new StringBuffer(mapent.get(te.getEntid()));
						mapent.put(te.getEntid(),taskid.append(","+te.getId()).toString());
					}
					else{
						mapent.put(te.getEntid(),""+te.getId());
					}
				}
			}
		//	System.out.println("111111111执行保存单位id和任务编号耗时 : "+(System.currentTimeMillis()-a)/1000f+" 秒 ");//0.453miao
			
			Calendar currentTime = Calendar.getInstance();
			Date date = currentTime.getTime();
			String currentTime2 = new SimpleDateFormat("yyyy-MM").format(date);
			//System.out.println("当前年月-->" + currentTime2);
			String[] t = new String[2];
			t =currentTime2.split("-");
			String startym=currentTime2,endym=currentTime2;
			
			int t1 = Integer.parseInt(t[1]);
			//System.out.println("当前月份-->" + t1);
			if(t1>0&&t1<4){
				startym=t[0]+"-01";
				endym=t[0]+"-03";
			}
			else if(t1>3&&t1<7){
				startym=t[0]+"-04";
				endym=t[0]+"-06";
			}
			else if(t1>6&&t1<10){
				startym=t[0]+"-07";
				endym=t[0]+"-09";
			}
			else if(t1>9&&t1<13){
				startym=t[0]+"-10";
				endym=t[0]+"-12";
			}

			System.out.println("起止月份-->" + startym+"---"+endym);
				

			
			
			
			Iterator iter=mapent.entrySet().iterator();
			while(iter.hasNext()){
				//下面是为了获取计划巡检设备的数量
				//long a1=System.currentTimeMillis();  
				

				
				
				
				
				Map.Entry enter=(Map.Entry) iter.next();
				int entid=(Integer) enter.getKey();

				String taskids=(String) enter.getValue();
	           // System.out.println("entid: " + entid +"; taskids: " + taskids); 
				
				String sql="  SELECT COUNT(*) FROM t_plan_task a "+ 
				"  LEFT JOIN t_line b ON (a.plineid=b.id) "+ 
				"  LEFT JOIN t_line_point c ON (b.id=c.lineid) "+ 
				"  LEFT JOIN t_point d ON (c.pointid=d.id) "+ 
				"  LEFT JOIN t_point_equipment e ON (d.id=e.pointid) "+ 
				"  WHERE a.entid="+entid+" and  a.id in ("+taskids+")"
				 +"  and a.pstartdate>='"+startym+"' AND a.pstartdate<='"+endym+"'"
				;
				
				
				//A左连接B－－》A left join B,结果就是A表所有记录与B表中与A表关联的记录。
				//获取当前年份与月份，得出当前季度
				
				//SELECT COUNT(*) FROM t_plan_task a LEFT JOIN t_line b ON (a.plineid=b.id) LEFT JOIN t_line_point c ON (b.id=c.lineid) LEFT JOIN t_point d ON (c.pointid=d.id) LEFT JOIN t_point_equipment e ON (d.id=e.pointid) 
				//WHERE a.entid=11  and  a.id in (523,803,548,549,298,832,1366,1367,1368,1369,1370,1372,1373,350,359,617,880,630,389,927,1442,1443,1444,1445,1459,438,744) and a.pstartdate>'2015-01' AND a.pstartdate<='2015-05'
				
				//where条件加上pstartdate>季度开始月份并且小于季度结束月份。如：WHERE pstartdate>'2014-01' AND pstartdate<='2014-03'
				
				//System.out.println(sql);
				SQLQuery query = baseDao.getHibernaSession().createSQLQuery(sql);
				List<Object[]> list=null;
				Object object = null;
				try {
					 list = query.list();
					 
					 //遍历list
					 
//					 Iterator it = list.iterator();	
//					 System.out.println("list数量： " + list.size());
//                     while(it.hasNext()){			
//                    	 System.out.println("query.list(): "+it.next());		
//                    	 }
					 
					 
					 
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
				}
				Integer plansum=Integer.parseInt(object.toString());//计划巡检设备的数量
				
		//		System.out.println("2222222执行查找设备总数量耗时 : "+(System.currentTimeMillis()-a1)/1000f+" 秒 ");//0.328秒
			//下面是为了获取图片异常个数的数量及已巡检的设备的数量
			HashMap<Integer,PlanQueryVo> mapvo=new HashMap<Integer,PlanQueryVo>();
		//	long a2=System.currentTimeMillis();  
				//String sql1=" select ximgname FROM t_report_message  where xtaskid in ("+taskids +") and xequtype=1";
			String sql1=" select ximgname FROM t_report_message  where xtaskid in ("+taskids +") and xequtype=1 and "+ "xreptime>='"+startym+"-01' AND xreptime<='"+endym+"-30'";
			
			SQLQuery query1 = baseDao.getHibernaSession().createSQLQuery(sql1);
				List<String> list1=null;
				Object object1 = null;
				Integer querycount=null;//已经巡检的设备数量
				int yichang=0;//图片异常设备数量
				try {
					 list1 = query1.list();
					 if(list1!=null&&list1.size()>0){
						 querycount=list1.size();
						// System.out.println("已巡检个数： " +querycount);
//		 System.out.println("333333333执行查找异常图片sql数量耗时 : "+(System.currentTimeMillis()-a2)/1000f+" 秒 ");////13.75秒
							long a0=System.currentTimeMillis();  
							//对图片名称拆分，以便统计图片异常数量
						 for(int i=0;i<list1.size();i++){
								int picnum=0;
								String str=list1.get(i);
								//铁塔类型
								if( str!=null){
									String imagename=str;
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
			//			 System.out.println("444444执行查找异常图片name拆分数量耗时 : "+(System.currentTimeMillis()-a0)/1000f+" 秒 ");
					}
					 else{
						 querycount=0;
					 }
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			//	long a3=System.currentTimeMillis();
				//下面是为了获取公司名称
				String entname=baseDao.getEntityById(Enterprise.class,entid)!=null?baseDao.getEntityById(Enterprise.class,entid).getEntname():"  ";
					//问题上报数量
					//long errorcount=baseDao.count("select count(*) from TInspectProblem t where t.proitaskid in ("+taskids +") ");
					
				//0518修改
				String errorsql = "select count(*) from TInspectProblem t where t.proitaskid in ("+taskids +") " + "  and t.createtime>='"+startym+"' AND t.createtime<='"+endym+"'";
				long errorcount=baseDao.count(errorsql);

					//获取某单位下设备总数量，合同站点数
					long sum=baseDao.count("select count(*) from TEquipment t where  t.etype=1 and t.entid="+entid);
				//	System.out.println("555555555执行查找问题上报数量及本单位总设备数量耗时 : "+(System.currentTimeMillis()-a3)/1000f+" 秒 ");//0.969秒
					
					//比较总完成率
					double totrate = (double)(Math.round(querycount*10000/(double)sum)/100.0);
					//总完成率
					String totalrate=(double)(Math.round(querycount*10000/(double)sum)/100.0)+"";
					
					
					//汇总所有的信息
			{	
				//汇总所有的信息
				PlanQueryVo evo=new PlanQueryVo();
				evo.setPointCount(plansum);  //巡检点个数//巡检设备总数
				evo.setQueryCount(querycount);  //已寻点个数
				evo.setUnqueryCount(plansum-querycount);//未寻点个数
				evo.setErrqueryCount((int)errorcount); //有问题个数
				evo.setPicexcepnum(yichang);  //图片异常个数
				evo.setEntid(entid);//所属单位id
				evo.setEsum((int)sum);//设备总数量
				evo.setEntname(entname);//所属单位名称
				
				evo.setTotalrate(totalrate+"%");
				
				
				if(totrate>=0){
					evo.setTotrate(totrate);
				}
				else{
					evo.setTotrate(0.00);
				}
				
				
				
				//统计总计
				totalcontractbasenumber = totalcontractbasenumber + (int)sum; //合同站点数
				totalplanbasenumber = totalplanbasenumber + plansum;   //计划巡检站点数
				totalplanfinished = totalplanfinished + querycount;    //已巡检个数
			    totalerrornumber = totalerrornumber + (int)errorcount;  //问题上报个数
			    totalpicerrornumber =totalpicerrornumber + yichang;    //图片异常个数
				
				
				//小数点后保留两位数
				//DecimalFormat df2 = new DecimalFormat("###.00");
				//计划率
				double planrate=0;
				if(evo.getEsum()>0){
					// pointCount;   //巡检点个数,任务下巡检设备总数
					//esum;       //单位下基础设备的总数目；
				 planrate=(double)(Math.round(((evo.getPointCount()*10000)/(double)evo.getEsum()))/100.0);
				 
				// planrate=(double)(Math.round(((evo.getPointCount()*10000)/(double)evo.getEsum()))/100.0);
					if(planrate>=0){
						evo.setPlanrate(String.valueOf(planrate)+"%");
					}
					else{
						evo.setPlanrate("空");
					}
				}else
				{
					evo.setPlanrate("空");
				}
				
				
				//巡检合格率
				double picrate;
				if(evo.getPointCount()>0){
					
					//（已巡检个数-图片异常个数）/已巡检个数
					picrate=(double)(Math.round(((evo.getQueryCount()-evo.getPicexcepnum())*10000/(double)evo.getQueryCount()))/100.0);
					//判断，如果没有，则rate>=0且rate<=100
					if(picrate>=0){
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
					
					//已巡检个数/计划数
					rate=(double)(Math.round((evo.getQueryCount()*10000/(double)evo.getPointCount()))/100.0);
					//判断巡检线路中的设备是否被删除过，如果没有，则rate>=0且rate<=100
					if(rate>=0){
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
			
			/*
			System.out.println("记录条数："+evolist.size());
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
				System.out.println("所属单位id："+v.getEntid());
			}
			*/
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		//List<PlanQueryVo> evolist
		//对evllist遍历求和
//		totalcontractbasenumber = totalcontractbasenumber + (int)sum; //合同站点数
//		totalplanbasenumber = totalplanbasenumber + plansum;   //计划巡检站点数
//		totalplanfinished = totalplanfinished + querycount;    //已巡检个数
//	    totalerrornumber = totalerrornumber + (int)errorcount;  //问题上报个数
//	    totalpicerrornumber =totalpicerrornumber + yichang;    //图片异常个数
		
		totaluninspectbasenumber = totalplanbasenumber - totalplanfinished;  //未巡检个数
		
		//计划率
		totalplanrate =(double)(Math.round((totalplanbasenumber*10000)/(double)totalcontractbasenumber)/100.0);  //计划率
		 
		//合格率 = （已巡检个数-图片异常个数）/已巡检个数
		totalpercentofpass=(double)(Math.round((totalplanfinished-totalpicerrornumber)*10000/(double)totalplanfinished)/100.0);
		
		//完成率 = 已巡检个数/计划数
		totalfinishrate=(double)(Math.round(totalplanfinished*10000/(double)totalplanbasenumber)/100.0);
		
		
		//总计总的完成率
		totaltotalfinishrate = (double)(Math.round(totalplanfinished*10000/(double)totalcontractbasenumber)/100.0);
		
				
				
		PlanQueryVo totalevo=new PlanQueryVo();
		totalevo.setPointCount(totalplanbasenumber);  //计划巡检数
		totalevo.setQueryCount(totalplanfinished);  //已寻点个数
		totalevo.setUnqueryCount(totalplanbasenumber-totalplanfinished);//未寻点个数
		totalevo.setErrqueryCount((int)totalerrornumber); //有问题个数
		totalevo.setPicexcepnum(totalpicerrornumber);  //图片异常个数
		totalevo.setEntid(0);//所属单位id
		totalevo.setEsum((int)totalcontractbasenumber);//设备总数量
		totalevo.setEntname("总计");//所属单位名称
		totalevo.setRate(String.valueOf(totalfinishrate)+"%"); //完成率
		totalevo.setPicrate(String.valueOf(totalpercentofpass)+"%"); //合格率
		totalevo.setPlanrate(String.valueOf(totalplanrate)+"%");  //计划率
		totalevo.setTotalrate(totaltotalfinishrate+"%");  //总计总完成率
		
		
		evolist.add(totalevo);
		
		//完成率排序
		//com.inspect.vo.basis.ComparatorPlanQueryVo comparator2 = new com.inspect.vo.basis.ComparatorPlanQueryVo();
		//Collections.sort(evolist,comparator2);
		
		
		//System.out.println("111111111执行保存单位id和任务编号耗时 : "+(System.currentTimeMillis()-a11)/1000f+" 秒 ");//0.453miao
		map.put("total", evolist.size());
		map.put("rows", evolist);
		return map;
	}
	
	/**
	 * 统计分析-情况统计,根据日期检索巡检完成情况
	 */
	@Override
	public Map<String, Object> findInspectSummaryInfoDatagridByDate(PlanQueryVo planQueryvo, int page, int rows,String qsql) {
	//	long a11=System.currentTimeMillis();
		Parameter param1=systemService.getParameter("铁塔图片数量");
		//long a=System.currentTimeMillis();       
		int piclimit=Integer.parseInt(param1.getPvalue());
		Map<String, Object> map = new HashMap<String, Object>();
		List<PlanQueryVo> evolist=new ArrayList<PlanQueryVo>();
		
		//统计总计
		int totalcontractbasenumber =0;
		int totalplanbasenumber =0;
		int totaluninspectbasenumber =0;
		double  totalplanrate =0;
		double  totalpercentofpass = 0;
		double  totalfinishrate = 0;
		double  totaltotalfinishrate = 0;
		int totalplanfinished = 0;
		int totalerrornumber =0;
		int totalpicerrornumber =0;
		
		
		try {
			StringBuffer buf = new StringBuffer(qsql);
		//	buf.append(DateUtils.thisQuarter());
			buf.append(" order by entid asc");
			System.out.println("buf.toString():  "+buf.toString());
			QueryResult<TPlanTask> queryResult = baseDao.getQueryResult(TPlanTask.class, buf.toString(), -1, -1,null, null);
			//mapent 用于保存单位id和任务编号
			HashMap<Integer,String> mapent=new HashMap<Integer,String>();
			if (queryResult != null && queryResult.getResultList().size() > 0) {
				for (TPlanTask te : queryResult.getResultList()) {
					
					if(mapent.containsKey(te.getEntid())){
						StringBuffer taskid=new StringBuffer(mapent.get(te.getEntid()));
						mapent.put(te.getEntid(),taskid.append(","+te.getId()).toString());
					}
					else{
						mapent.put(te.getEntid(),""+te.getId());
					}
				}
			}
			Calendar currentTime = Calendar.getInstance();
			Date date = currentTime.getTime();
			String currentTime2 = new SimpleDateFormat("yyyy-MM").format(date);
			//System.out.println("当前年月-->" + currentTime2);
			//默认查询当前月
			String startym=currentTime2,endym=currentTime2;
			
			if(planQueryvo.getPstartdate()!=null&&planQueryvo.getPstartdate().length()>0)
			{
				startym = planQueryvo.getPstartdate();
			}
			if(planQueryvo.getPenddate()!=null&&planQueryvo.getPenddate().length()>0)
			{
				endym = planQueryvo.getPenddate();
			}
			System.out.println("起止月份-->" + startym+"---"+endym);
			Iterator iter=mapent.entrySet().iterator();
			while(iter.hasNext()){
				//下面是为了获取计划巡检设备的数量
				//long a1=System.currentTimeMillis();  
				
				Map.Entry enter=(Map.Entry) iter.next();
				int entid=(Integer) enter.getKey();
				String taskids=(String) enter.getValue();
				String sql="  SELECT COUNT(*) FROM t_plan_task a "+ 
				"  LEFT JOIN t_line b ON (a.plineid=b.id) "+ 
				"  LEFT JOIN t_line_point c ON (b.id=c.lineid) "+ 
				"  LEFT JOIN t_point d ON (c.pointid=d.id) "+ 
				"  LEFT JOIN t_point_equipment e ON (d.id=e.pointid) "+ 
				"  WHERE a.entid="+entid+" and  a.id in ("+taskids+")"
				 +"  and a.pstartdate>='"+startym+"' AND a.pstartdate<='"+endym+"'"
				;
				//System.out.println(sql);
				SQLQuery query = baseDao.getHibernaSession().createSQLQuery(sql);
				List<Object[]> list=null;
				Object object = null;
				try {
					 list = query.list();
					 if(list!=null&&list.size()>0){
						  object = (Object) list.get(0);
					 }
					 else{
						 object=0;
					 }	 
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				Integer plansum=Integer.parseInt(object.toString());//计划巡检设备的数量
		//		System.out.println("2222222执行查找设备总数量耗时 : "+(System.currentTimeMillis()-a1)/1000f+" 秒 ");//0.328秒
			//下面是为了获取图片异常个数的数量及已巡检的设备的数量
			HashMap<Integer,PlanQueryVo> mapvo=new HashMap<Integer,PlanQueryVo>();
		//	long a2=System.currentTimeMillis();  
				//String sql1=" select ximgname FROM t_report_message  where xtaskid in ("+taskids +") and xequtype=1";
			String sql1=" select ximgname FROM t_report_message  where xtaskid in ("+taskids +") and xequtype=1 and "+ "xreptime>='"+startym+"-01' AND xreptime<='"+endym+"-30'";
			SQLQuery query1 = baseDao.getHibernaSession().createSQLQuery(sql1);
				List<String> list1=null;
				Object object1 = null;
				Integer querycount=null;//已经巡检的设备数量
				int yichang=0;//图片异常设备数量
				try {
					 list1 = query1.list();
					 if(list1!=null&&list1.size()>0){
						 querycount=list1.size();
						// System.out.println("已巡检个数： " +querycount);
//		 System.out.println("333333333执行查找异常图片sql数量耗时 : "+(System.currentTimeMillis()-a2)/1000f+" 秒 ");////13.75秒
							long a0=System.currentTimeMillis();  
							//对图片名称拆分，以便统计图片异常数量
						 for(int i=0;i<list1.size();i++){
								int picnum=0;
								String str=list1.get(i);
								//铁塔类型
								if( str!=null){
									String imagename=str;
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
			//			 System.out.println("444444执行查找异常图片name拆分数量耗时 : "+(System.currentTimeMillis()-a0)/1000f+" 秒 ");
					}
					 else{
						 querycount=0;
					 }
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			//	long a3=System.currentTimeMillis();
				//下面是为了获取公司名称
				String entname=baseDao.getEntityById(Enterprise.class,entid)!=null?baseDao.getEntityById(Enterprise.class,entid).getEntname():"  ";
					//问题上报数量
					//long errorcount=baseDao.count("select count(*) from TInspectProblem t where t.proitaskid in ("+taskids +") ");
				String errorsql = "select count(*) from TInspectProblem t where t.proitaskid in ("+taskids +") " + "  and t.createtime>='"+startym+"' AND t.createtime<='"+endym+"'";
				long errorcount=baseDao.count(errorsql);
					//获取某单位下设备总数量，合同站点数
					long sum=baseDao.count("select count(*) from TEquipment t where  t.etype=1 and t.entid="+entid);
				//	System.out.println("555555555执行查找问题上报数量及本单位总设备数量耗时 : "+(System.currentTimeMillis()-a3)/1000f+" 秒 ");//0.969秒
					
					//比较总完成率
					double totrate = (double)(Math.round(querycount*10000/(double)sum)/100.0);
					//总完成率
					String totalrate=(double)(Math.round(querycount*10000/(double)sum)/100.0)+"";
					
					//汇总所有的信息
				{	
					//汇总所有的信息
					PlanQueryVo evo=new PlanQueryVo();
					evo.setPointCount(plansum);  //巡检点个数//巡检设备总数
					evo.setQueryCount(querycount);  //已寻点个数
					evo.setUnqueryCount(plansum-querycount);//未寻点个数
					evo.setErrqueryCount((int)errorcount); //有问题个数
					evo.setPicexcepnum(yichang);  //图片异常个数
					evo.setEntid(entid);//所属单位id
					evo.setEsum((int)sum);//设备总数量
					evo.setEntname(entname);//所属单位名称
					
					evo.setTotalrate(totalrate+"%");
					
					
					if(totrate>=0){
						evo.setTotrate(totrate);
					}
					else{
						evo.setTotrate(0.00);
					}
					
					
					
					//统计总计
					totalcontractbasenumber = totalcontractbasenumber + (int)sum; //合同站点数
					totalplanbasenumber = totalplanbasenumber + plansum;   //计划巡检站点数
					totalplanfinished = totalplanfinished + querycount;    //已巡检个数
				    totalerrornumber = totalerrornumber + (int)errorcount;  //问题上报个数
				    totalpicerrornumber =totalpicerrornumber + yichang;    //图片异常个数
					
					
					//小数点后保留两位数
					//DecimalFormat df2 = new DecimalFormat("###.00");
					//计划率
					double planrate=0;
					if(evo.getEsum()>0){
						// pointCount;   //巡检点个数,任务下巡检设备总数
						//esum;       //单位下基础设备的总数目；
					 planrate=(double)(Math.round(((evo.getPointCount()*10000)/(double)evo.getEsum()))/100.0);
					 
					// planrate=(double)(Math.round(((evo.getPointCount()*10000)/(double)evo.getEsum()))/100.0);
						if(planrate>=0){
							evo.setPlanrate(String.valueOf(planrate)+"%");
						}
						else{
							evo.setPlanrate("空");
						}
					}else
					{
						evo.setPlanrate("空");
					}
					
					
					//巡检合格率
					double picrate;
					if(evo.getPointCount()>0){
						
						//（已巡检个数-图片异常个数）/已巡检个数
						picrate=(double)(Math.round(((evo.getQueryCount()-evo.getPicexcepnum())*10000/(double)evo.getQueryCount()))/100.0);
						//判断，如果没有，则rate>=0且rate<=100
						if(picrate>=0){
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
						
						//已巡检个数/计划数
						rate=(double)(Math.round((evo.getQueryCount()*10000/(double)evo.getPointCount()))/100.0);
						//判断巡检线路中的设备是否被删除过，如果没有，则rate>=0且rate<=100
						if(rate>=0){
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
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		totaluninspectbasenumber = totalplanbasenumber - totalplanfinished;  //未巡检个数
		
		//计划率
		totalplanrate =(double)(Math.round((totalplanbasenumber*10000)/(double)totalcontractbasenumber)/100.0);  //计划率
		 
		//合格率 = （已巡检个数-图片异常个数）/已巡检个数
		totalpercentofpass=(double)(Math.round((totalplanfinished-totalpicerrornumber)*10000/(double)totalplanfinished)/100.0);
		
		//完成率 = 已巡检个数/计划数
		totalfinishrate=(double)(Math.round(totalplanfinished*10000/(double)totalplanbasenumber)/100.0);
		
		//总计总的完成率
		totaltotalfinishrate = (double)(Math.round(totalplanfinished*10000/(double)totalcontractbasenumber)/100.0);
				
		PlanQueryVo totalevo=new PlanQueryVo();
		totalevo.setPointCount(totalplanbasenumber);  //计划巡检数
		totalevo.setQueryCount(totalplanfinished);  //已寻点个数
		totalevo.setUnqueryCount(totalplanbasenumber-totalplanfinished);//未寻点个数
		totalevo.setErrqueryCount((int)totalerrornumber); //有问题个数
		totalevo.setPicexcepnum(totalpicerrornumber);  //图片异常个数
		totalevo.setEntid(0);//所属单位id
		totalevo.setEsum((int)totalcontractbasenumber);//设备总数量
		totalevo.setEntname("总计");//所属单位名称
		totalevo.setRate(String.valueOf(totalfinishrate)+"%"); //完成率
		totalevo.setPicrate(String.valueOf(totalpercentofpass)+"%"); //合格率
		totalevo.setPlanrate(String.valueOf(totalplanrate)+"%");  //计划率
		totalevo.setTotalrate(totaltotalfinishrate+"%");  //总计总完成率
		evolist.add(totalevo);
		
		//完成率排序
		//com.inspect.vo.basis.ComparatorPlanQueryVo comparator2 = new com.inspect.vo.basis.ComparatorPlanQueryVo();
		//Collections.sort(evolist,comparator2);
		//System.out.println("111111111执行保存单位id和任务编号耗时 : "+(System.currentTimeMillis()-a11)/1000f+" 秒 ");//0.453miao
		map.put("total", evolist.size());
		map.put("rows", evolist);
		return map;
	}
	
	
	
	//巡检数据比对及核查
	@Override
	public Map<String, Object> findInspectInfoCompareDatagrid1(InspectItemReportVo inspectitempvo, int page, int rows,String qsql,String buf1) {
		
		
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer buf = new StringBuffer(qsql);
		if(StringUtils.isEmpty(inspectitempvo.getQyerytype())){
			buf.append(" and 1=0");
		}
		if (inspectitempvo.getEntid()!=0) {
			buf.append(" and entid =").append(inspectitempvo.getEntid());
		}
		if (inspectitempvo.getItaskid()!=0) {
			buf.append(" and xtaskid =").append(inspectitempvo.getItaskid());
		}
		if (inspectitempvo.getXequid()!=null) {
			buf.append(" and tinspectitemreport.xequid =").append(inspectitempvo.getXequid());
		}
		if (inspectitempvo.getXproid()!=null) {
			buf.append(" and xproid =").append(inspectitempvo.getXproid());
		}
		if(!StringUtils.isEmpty(inspectitempvo.getXstatus())){
			buf.append(" and xstatus='").append(inspectitempvo.getXstatus()).append("'");
		}
		if (!StringUtils.isEmpty(inspectitempvo.getPstartdate())||!StringUtils.isEmpty(inspectitempvo.getPenddate())) {
			String tid=getTaskIdByDate(inspectitempvo.getPstartdate(),inspectitempvo.getPenddate());
			if(!StringUtils.isEmpty(tid)){
				buf.append(" and xtaskid in (").append(tid).append(")");
			}else{
				buf.append(" and xtaskid =0");
			}
		}
		if (inspectitempvo.getXgid()!=0) {
			buf.append(" and tinspectitemreport.xgid =").append(inspectitempvo.getXgid());
		}
		if(!StringUtils.isEmpty(inspectitempvo.getXproname())){
			buf.append("  and xproname like  '%" ).append(inspectitempvo.getXproname().trim()).append("%'");
		}
		//设备名称的模糊查询    当设备名称没有值时
		if(StringUtils.isEmpty(inspectitempvo.getXename())){
			buf.append(" and 1=1 ");//inspectmessageVo.getXename()
		}
		//当设备名称作为查询条件时
		else{
			//当输入设备名称并找到对应的设备时,buf1.length()>0,后则buf1.length()=0
			 if(buf1.length()>0){
				buf.append(" and msgid in (").append(buf1.toString()).append(")");
			}
			
			else {
				buf.append(" and 1=0 ");//inspectmessageVo.getXename()
			}
		}
		
		buf.append(" order by xreptime desc");
		QueryResult<TInspectItemDetailReport> queryResult = baseDao.getQueryResult(TInspectItemDetailReport.class, buf.toString(), (page - 1) * rows, rows,null, null);
		List<InspectItemReportVo> evolist=new ArrayList<InspectItemReportVo>();
		if (queryResult != null && queryResult.getResultList().size() > 0) {
			for (TInspectItemDetailReport te : queryResult.getResultList()) {
				InspectItemReportVo evo = new InspectItemReportVo();
				BeanUtils.copyProperties(te,evo);
				TPlanTask ptsk=baseDao.getEntityById(TPlanTask.class,te.getXtaskid());
				if(ptsk!=null){
					evo.setXtaskname(ptsk.getPname());
					evo.setPstartdate(ptsk.getPstartdate());
					evo.setXgname(baseDao.get(TGroup.class,ptsk.getPgid())!=null?baseDao.get(TGroup.class,ptsk.getPgid()).getGname():"");
				}
				//设备名称
				evo.setXequname(baseDao.getEntityById(TBaseInfo.class,te.getTinspectitemreport().getXequid())!=null?baseDao.getEntityById(TBaseInfo.class,te.getTinspectitemreport().getXequid()).getBname():"");
				//巡检项名称
				evo.setXpgname(baseDao.getEntityById(TProject.class,te.getXproid())!=null?baseDao.getEntityById(TProject.class,te.getXproid()).getTprojectgroup().getPgname():"");
				evo.setXpvalue(te.getXvalue());
				evolist.add(evo);
			}
		}
		map.put("total", queryResult.getTotalRecord());
		map.put("rows", evolist);
		return map;
	}
	//巡检数据比对及核查
	@Override
	public Map<String, Object> findInspectInfoCompareDatagrid(InspectItemReportVo inspectitempvo, int page, int rows,String qsql) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer buf = new StringBuffer(qsql);
		if(StringUtils.isEmpty(inspectitempvo.getQyerytype())){
			buf.append(" and 1=0");
		}
		if (inspectitempvo.getEntid()!=0) {
			buf.append(" and entid =").append(inspectitempvo.getEntid());
		}
		if (inspectitempvo.getItaskid()!=0) {
			buf.append(" and xtaskid =").append(inspectitempvo.getItaskid());
		}
		if (inspectitempvo.getXequid()!=null) {
			buf.append(" and tinspectitemreport.xequid =").append(inspectitempvo.getXequid());
		}
		if (inspectitempvo.getXproid()!=null) {
			buf.append(" and xproid =").append(inspectitempvo.getXproid());
		}
		if(!StringUtils.isEmpty(inspectitempvo.getXstatus())){
			buf.append(" and xstatus='").append(inspectitempvo.getXstatus()).append("'");
		}
		if (!StringUtils.isEmpty(inspectitempvo.getPstartdate())||!StringUtils.isEmpty(inspectitempvo.getPenddate())) {
			String tid=getTaskIdByDate(inspectitempvo.getPstartdate(),inspectitempvo.getPenddate());
			if(!StringUtils.isEmpty(tid)){
				buf.append(" and xtaskid in (").append(tid).append(")");
			}else{
				buf.append(" and xtaskid =0");
			}
		}
		if (inspectitempvo.getXgid()!=0) {
			buf.append(" and tinspectitemreport.xgid =").append(inspectitempvo.getXgid());
		}
		if(!StringUtils.isEmpty(inspectitempvo.getXproname())){
			buf.append("  and xproname like  '%" ).append(inspectitempvo.getXproname().trim()).append("%'");
		}
		buf.append(" order by xreptime desc");
		QueryResult<TInspectItemDetailReport> queryResult = baseDao.getQueryResult(TInspectItemDetailReport.class, buf.toString(), (page - 1) * rows, rows,null, null);
		List<InspectItemReportVo> evolist=new ArrayList<InspectItemReportVo>();
		if (queryResult != null && queryResult.getResultList().size() > 0) {
			for (TInspectItemDetailReport te : queryResult.getResultList()) {
				InspectItemReportVo evo = new InspectItemReportVo();
				BeanUtils.copyProperties(te,evo);
				TPlanTask ptsk=baseDao.getEntityById(TPlanTask.class,te.getXtaskid());
				if(ptsk!=null){
					evo.setXtaskname(ptsk.getPname());
					evo.setPstartdate(ptsk.getPstartdate());
					evo.setXgname(baseDao.get(TGroup.class,ptsk.getPgid())!=null?baseDao.get(TGroup.class,ptsk.getPgid()).getGname():"");
				}
				evo.setXequname(baseDao.getEntityById(TEquipment.class,te.getTinspectitemreport().getXequid())!=null?baseDao.getEntityById(TEquipment.class,te.getTinspectitemreport().getXequid()).getEname():"");
				evo.setXpgname(baseDao.getEntityById(TProject.class,te.getXproid())!=null?baseDao.getEntityById(TProject.class,te.getXproid()).getTprojectgroup().getPgname():"");
				evo.setXpvalue(te.getXvalue());
				evolist.add(evo);
			}
		}
		map.put("total", queryResult.getTotalRecord());
		map.put("rows", evolist);
		return map;
	}
	@Override
	@SuppressWarnings("unchecked")
	public Map<String, Object> findProjectByEidDatagrid(InspectItemReportVo inspectitempvo,String qsql) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer buf = new StringBuffer("SELECT j.id,j.pname FROM t_equipment AS e,t_equipment_project_group AS eg,t_project as j WHERE e.id=eg.eid and eg.pgid=j.pgroupid ");
		if(inspectitempvo.getXequid()!=0){
			buf.append(" and e.id=").append(inspectitempvo.getXequid());
		}
		if (inspectitempvo.getEntid()!=0) {
			buf.append(" and entid =").append(inspectitempvo.getEntid());
		}
		String totalRecordSQL = buf.toString();
		int  rows = (inspectitempvo.getPage() - 1) * inspectitempvo.getRows();
		int offset = inspectitempvo.getRows();
		buf.append(" LIMIT "+rows+", "+offset);
		String rowsSQL = buf.toString();
		Integer totalRecord = baseDao.getJdbcTemplate().queryForList(totalRecordSQL).size();
		List resultList = baseDao.getJdbcTemplate().queryForList(rowsSQL);
		map.put("total", totalRecord);
		map.put("rows", resultList);
		return map;
	}
	
	@SuppressWarnings("unchecked")
	private String getTaskIdByDate(String psdate,String pedate){
		StringBuffer buf=new StringBuffer("select id from TPlanTask where 1=1");
		if(!StringUtils.isEmpty(psdate)){
			buf.append(" and pstartdate >='").append(psdate).append("'");
		}
		if(!StringUtils.isEmpty(pedate)){
			buf.append(" and pstartdate <='").append(pedate).append("'");
		}
		List list= baseDao.find(buf.toString());
		String rst="";
		boolean b = false;
		if(list!=null&& list.size()>0){
			for(int i=0;i<list.size();i++){
				if (b) {
					rst += ",";
				}
				rst+=list.get(i);
				b = true;
			}
		}
		return rst;
		 
	}
	
	
	private String getTaskIdByDate1(String psdate){
		StringBuffer buf=new StringBuffer("select id from TPlanTask where 1=1");
		if(!StringUtils.isEmpty(psdate)){
			buf.append(" and pstartdate like'%").append(psdate.trim()).append("%'");
		}
	
		List list= baseDao.find(buf.toString());
		String rst="";
		boolean b = false;
		if(list!=null&& list.size()>0){
			for(int i=0;i<list.size();i++){
				if (b) {
					rst += ",";
				}
				rst+=list.get(i);
				b = true;
			}
		}
		return rst;
		 
	}
	/**
	 * 图片异常的设备数目
	 * @param taskid
	 * @return
	 */
	public int getPicnum(Integer taskid){
		int count=0;//图片正常设备数量
		Parameter param1=systemService.getParameter("铁塔图片数量");
		Parameter param2=systemService.getParameter("室内图片数量");
		List<TInspectItemReport> teList=baseDao.find("from  TInspectItemReport where  xtaskid="+taskid);
		int size=teList.size();//已巡检的设备数量
		if(teList!=null&&teList.size()>0){
			for(TInspectItemReport te:teList){
				int picsum=0, picnum=0;
				//铁塔类型
				if("1".equals(te.getXequtype())){
					picsum=Integer.parseInt(param1.getPvalue());
				}
				//室内类型
				else if("2".equals(te.getXequtype())){
					picsum=Integer.parseInt(param2.getPvalue());
				}
				if(te.getXimgname()!=null){
					picnum=te.getXimgname().split(",").length;
				}
				int excepflag=picsum-picnum;//大于0，表示图片没有上传完成
				if(excepflag<=0){
					count++;
				}
			}
		}
		int yichang=size-count;
		return yichang;
	}
	@Override
	public Map<String, Object> inspectmessageModifyDatagrid(
			InspectItemReportVo inspectitempvo, int page, int rows, int entid) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer buf = new StringBuffer();
		if(entid==0){
			buf.append(" 1=1");
		}else{
			buf.append(" a.entid in (0,").append(entid).append(")");
		}
		if(StringUtils.isEmpty(inspectitempvo.getQyerytype())){
			buf.append(" and 1=0");
		}
		if (inspectitempvo.getEntid()!=0) {
			buf.append(" and a.entid =").append(inspectitempvo.getEntid());
		}
	
		if (StringUtils.isNotEmpty(inspectitempvo.getXequtnum())) {
			buf.append(" and a.xequtnum like '%").append(inspectitempvo.getXequtnum().trim()).append("%' ");
		}
		if (StringUtils.isNotEmpty(inspectitempvo.getXequname())) {
			String ids=inspectItemService.getEquipment1Byenameornum(inspectitempvo.getXequname(), inspectitempvo.getEntid(), 0);
			//当输入设备名称并找到对应的设备时,buf1.length()>0,后则buf1.length()=0
			 if(ids.length()>0){
				buf.append(" and a.xequid in (").append(ids.toString()).append(") ");
			}
			else {
				buf.append(" and 1=0 ");//inspectmessageVo.getXename()
			}
		}
		String pname="";
		QueryResult<TInspectItemReport> queryResult = baseDao.getQueryResult11(TInspectItemReport.class, buf.toString(), (page - 1) * rows, rows,null, null);
		List<InspectItemReportVo> evolist=new ArrayList<InspectItemReportVo>();
		if (queryResult != null && queryResult.getResultList().size() > 0) {
			for (TInspectItemReport te : queryResult.getResultList()) {
				InspectItemReportVo evo = new InspectItemReportVo();
				evo.setXequtnum(te.getXequtnum());
				evo.setId(te.getId());
				evo.setXequid(te.getXequid());
				TBaseInfo binfo=baseDao.getEntityById(TBaseInfo.class,te.getXequid());
				if(binfo!=null){
					//设备名称
					evo.setXequname(binfo.getBname());
					evo.setBcity(binfo.getBcity());
					evo.setBregion(binfo.getBregion());
					evo.setBposx(binfo.getBposx());
					evo.setBposy(binfo.getBposy());
				}
				
				evolist.add(evo);
			}
		}
		map.put("total",queryResult.getTotalRecord());
		map.put("rows", evolist);
		return map;
	}
	@Override
	public void editDetailReport(InspectItemReportVo detail) {
		// TODO Auto-generated method stub
		TInspectItemDetailReport detail1=(TInspectItemDetailReport) baseDao.find1(" from TInspectItemDetailReport where id="+detail.getId());
		if(detail1!=null){
			detail1.setXvalue(detail.getXvalue());
			//	System.out.println(detail.getXpvalue());
		//	baseDao.save(detail1);
		}
		
	}
}
