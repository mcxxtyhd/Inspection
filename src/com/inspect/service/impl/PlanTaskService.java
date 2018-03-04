package com.inspect.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.inspect.dao.BaseDaoI;
import com.inspect.model.basis.TEquipment;
import com.inspect.model.basis.TEquipmentProjectGroup;
import com.inspect.model.basis.TGroup;
import com.inspect.model.basis.TInspectUser;
import com.inspect.model.basis.TLine;
import com.inspect.model.basis.TLinePoint;
import com.inspect.model.basis.TPlan;
import com.inspect.model.basis.TPlanTask;
import com.inspect.model.basis.TPoint;
import com.inspect.model.basis.TPointEquipment;
import com.inspect.model.basis.TProject;
import com.inspect.model.basis.TProjectGroup;
import com.inspect.model.monitor.TInspectItemReport;
import com.inspect.service.PlanTaskServiceI;
import com.inspect.util.common.DateUtils;
import com.inspect.util.common.QueryResult;
import com.inspect.util.common.StringUtils;
import com.inspect.vo.basis.PlanQueryVo;
import com.inspect.vo.basis.PlanTaskVo;
import com.inspect.vo.basis.PlanVo;
import com.itextpdf.text.pdf.PdfStructTreeController.returnType;

@Service("planTaskService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
public class PlanTaskService implements PlanTaskServiceI {
	@Resource
	private BaseDaoI baseDao;

	public BaseDaoI getBaseDao() {
		return baseDao;
	}

	public void setBaseDao(BaseDaoI baseDao) {
		this.baseDao = baseDao;
	}

	@Override
	public void addProject(PlanVo plantaskvo, TPlan plan) {
		TPlanTask tplantask = new TPlanTask();
		tplantask.setTplan(plan);
		BeanUtils.copyProperties(plantaskvo, tplantask);
		tplantask.setEntid(plantaskvo.getEntid());
		baseDao.save(tplantask);
	}
	
// 查询所有的巡检任务zuixin

	public Map<String, Object> findPlanTaskDatagrid1(PlanTaskVo plantaskvo, int page, int rows, String hql) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer buf = new StringBuffer(hql);
		if (!StringUtils.isEmpty(plantaskvo.getPname())) {
			buf.append(" and pname like '%").append(plantaskvo.getPname()).append("%'");
		}
		if (plantaskvo.getPlineid() != 0) {
			buf.append(" and plineid = ").append(plantaskvo.getPlineid());
		}
		if (plantaskvo.getPgid() != 0) {
			buf.append(" and pgid = ").append(plantaskvo.getPgid());
		}
		if (plantaskvo.getPuid() != 0) {
			buf.append(" and puid = ").append(plantaskvo.getPuid());
		}
//		if (plantaskvo.getPtype() >= 0) {
//			buf.append(" and ptype = ").append(plantaskvo.getPtype());
//		}
		if (!StringUtils.isEmpty(plantaskvo.getPstartdate())) {
			buf.append(" and pstartdate = '").append(plantaskvo.getPstartdate()).append("'");
		}
//		if (!StringUtils.isEmpty(plantaskvo.getPenddate())) {
//			buf.append(" and penddate <= '").append(plantaskvo.getPenddate()).append("'");
//		}
		if (plantaskvo.getPstatus() != 0) {
			buf.append(" and pstatus = ").append(plantaskvo.getPstatus());
		}
		if (plantaskvo.getEntid()!=0) {
			buf.append(" and entid=").append(plantaskvo.getEntid());
		}
		buf.append(" order by id desc");
		
		System.out.println("PlanTaskService: "+ buf.toString());
		
		QueryResult<TPlanTask> queryResult = baseDao.getQueryResult(TPlanTask.class, buf.toString(), (page - 1) * rows, rows, null, null);
		
		List<PlanTaskVo> docvolist = new ArrayList<PlanTaskVo>();
		
		for (TPlanTask te : queryResult.getResultList()) {
			System.out.println("遍历结果集的计划名称: "+te.getPname());
		}
		
		if (queryResult != null && queryResult.getResultList().size() > 0) {
			
			System.out.println("queryresult数量："+queryResult.getResultList().size());
			for (TPlanTask te : queryResult.getResultList()) {
				
				System.out.println("计划名称: "+te.getPname());
				boolean taskflag=true;
				PlanTaskVo plantaskvos = new PlanTaskVo();
				BeanUtils.copyProperties(te, plantaskvos);
				TLine line = baseDao.get(TLine.class, te.getPlineid());
				if (line != null) {
					plantaskvos.setPlinename(line.getLname());
					System.out.println("line.getLname(): "+ line.getLname());
				}
				TInspectUser inspectuser = baseDao.get(TInspectUser.class, te.getPuid());
				if (inspectuser != null) {
					plantaskvos.setUsername(inspectuser.getIuname());
					System.out.println("inspectuser.getIuname(): "+ inspectuser.getIuname());
				}
				TGroup g=baseDao.getEntityById(TGroup.class,te.getPgid());
				if(g!=null){
					plantaskvos.setGroupname(g.getGname());
					 System.out.println("g.getGname()： " + g.getGname());
				}
				////判断是否超期
				{
					List<TLinePoint> tlp= line.getLinepoints();
					
					System.out.println("tlp: "+tlp.size());
					long ecount=0;
					if(tlp!=null && tlp.size()>0){
						for(int i=0;i<tlp.size()&&taskflag==true;i++){
							TLinePoint lp=tlp.get(i);
							TPoint tp=lp.getTpoint();
							if(tp!=null){
								Set<TPointEquipment> tpe=tp.getPointequipments();
								System.out.println("tpe: "+tpe.size());
								ecount+=tpe.size();
								//计算巡检项数目
								if(tpe!=null&&tpe.size()>0){
								taskflag=this.isComplete(tpe, te.getId());  //te:tplantask
								}
								//TEquipment equip=
							}
						}
					}
					System.out.println("select count(*) from TInspectItemReport t where t.xtaskid="+te.getId());
					long qecount=baseDao.count("select count(*) from TInspectItemReport t where t.xtaskid="+te.getId());
					long count=ecount-qecount;
				    
					 System.out.println("qecount--------" + qecount  + ", xtaskid: " + te.getId());
				    String nowdate=DateUtils.getFormatMonthDate();
				    //所有巡检项未完成
				    if(!nowdate.equals(plantaskvos.getPstartdate().substring(0,7))&&taskflag==false){
				    	plantaskvos.setPcomflag(0);
				    }
				    //至少有一个设备未巡检
				    else if(!nowdate.equals(plantaskvos.getPstartdate().substring(0,7))&&count>0){
				    	plantaskvos.setPcomflag(0);
				    }
				    else{
				    	plantaskvos.setPcomflag(1);
				    }
				}
				
				System.out.println("plantaskvos: " + plantaskvos.getPname());
				docvolist.add(plantaskvos);
				
			}
			
			System.out.println("docvolist:  " + docvolist.size());
		}
		
		
		//System.out.println("docvolist:  " + docvolist.size());
		map.put("total", queryResult.getTotalRecord());
		map.put("rows", docvolist);
		return map;
	}
	/**
	 * 判断某一个巡检设备的所有巡检项是否都被巡检过。
	 * 若都巡检过，则返回true，否则返回false
	 * @param tpe
	 * @return
	 */
	public boolean isComplete(Set<TPointEquipment> tpe ,int taskid){
		boolean taskflag=true;
			Iterator<TPointEquipment> te11=tpe.iterator();
			while(te11.hasNext()){
				TPointEquipment te1=te11.next();
				//统计一个设备下所需要巡检的巡检项的数目
				int procount=0;
				TEquipment equip=te1.getTequipment();
				if(equip!=null){
					  //获取设备下面的巡检项组
					  Set<TEquipmentProjectGroup> epGroupList=equip.getEquipmentprojectgroups();
					  //将设备下的巡检项组id保存起来
					  if(epGroupList!=null&&epGroupList.size()!=0){
						  
						  for(TEquipmentProjectGroup epGroup:epGroupList){
							
							  
							  TProjectGroup pGroup=epGroup.getTprojectgroup();
							  if(pGroup!=null){
								  Set<TProject> pro= pGroup.getTprojects();
								  if(pro!=null&&pro.size()>0){
									  procount=pro.size()+procount;
								  }
							  }
						  }
					  }
				}
				
				System.out.println("from t_report_message t where t.xtaskid="+taskid+" and t.xequid='"+equip.getId()+"'");
				TInspectItemReport t=(TInspectItemReport) baseDao.find1(" from TInspectItemReport t where t.xtaskid="+taskid+" and t.xequid='"+equip.getId()+"'");
				//System.out.println("t.getInspectreportdetailmsgs().size(): "+ t.getInspectreportdetailmsgs().size());
				//实际上传的巡检项数目
				int procount1=0;
				if(t!=null){      //t_report_message
					procount1=t.getInspectreportdetailmsgs().size();  //t_report_message_detai
				}
				
				//System.out.println("procount>procount1?  "+procount+"  "+procount1);
				//若巡检未完成则为false,完成则为true
				if(procount>procount1){
					taskflag=false;
					return taskflag;
				}
			
		}
	
		return taskflag;
	}
	// 查询所有的巡检任务
	@Override
	public Map<String, Object> findPlanTaskDatagrid(PlanTaskVo plantaskvo, int page, int rows, String hql) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer buf = new StringBuffer(hql);
		if (!StringUtils.isEmpty(plantaskvo.getPname())) {
			buf.append(" and pname like '%").append(plantaskvo.getPname()).append("%'");
		}
		if (plantaskvo.getPlineid() != 0) {
			buf.append(" and plineid = ").append(plantaskvo.getPlineid());
		}
		if (plantaskvo.getPgid() != 0) {
			buf.append(" and pgid = ").append(plantaskvo.getPgid());
		}
		if (plantaskvo.getPuid() != 0) {
			buf.append(" and puid = ").append(plantaskvo.getPuid());
		}
//		if (plantaskvo.getPtype() >= 0) {
//			buf.append(" and ptype = ").append(plantaskvo.getPtype());
//		}
		if (!StringUtils.isEmpty(plantaskvo.getPstartdate())) {
			buf.append(" and pstartdate = '").append(plantaskvo.getPstartdate()).append("'");
		}
//		if (!StringUtils.isEmpty(plantaskvo.getPenddate())) {
//			buf.append(" and penddate <= '").append(plantaskvo.getPenddate()).append("'");
//		}
		if (plantaskvo.getPstatus() != 0) {
			buf.append(" and pstatus = ").append(plantaskvo.getPstatus());
		}
		buf.append(" order by id desc");
		QueryResult<TPlanTask> queryResult = baseDao.getQueryResult(TPlanTask.class, buf.toString(), (page - 1) * rows, rows, null, null);
		List<PlanTaskVo> docvolist = new ArrayList<PlanTaskVo>();
		if (queryResult != null && queryResult.getResultList().size() > 0) {
			for (TPlanTask te : queryResult.getResultList()) {
				PlanTaskVo plantaskvos = new PlanTaskVo();
				BeanUtils.copyProperties(te, plantaskvos);
				TLine line = baseDao.get(TLine.class, te.getPlineid());
				if (line != null) {
					plantaskvos.setPlinename(line.getLname());
				}
				TInspectUser inspectuser = baseDao.get(TInspectUser.class, te.getPuid());
				if (inspectuser != null) {
					plantaskvos.setUsername(inspectuser.getIuname());
				}
				TGroup g=baseDao.getEntityById(TGroup.class,te.getPgid());
				if(g!=null){
					plantaskvos.setGroupname(g.getGname());
				}
				////判断是否超期
				{
					List<TLinePoint> tlp= line.getLinepoints();
					long ecount=0;
					if(tlp!=null && tlp.size()>0){
						for(TLinePoint lp:tlp){
							TPoint tp=lp.getTpoint();
							if(tp!=null){
								Set<TPointEquipment> tpe=tp.getPointequipments();
								ecount+=tpe.size();
							}
						}
					}
					long qecount=baseDao.count("select count(*) from TInspectItemReport t where t.xtaskid="+te.getId());
					long count=ecount-qecount;
				    ////
				    String nowdate=DateUtils.getFormatMonthDate();
				    if(!nowdate.equals(plantaskvos.getPstartdate().substring(0,7))&&count>0){
				    	plantaskvos.setPcomflag(0);
				    }
				    else{
				    	plantaskvos.setPcomflag(1);
				    }
				}
				docvolist.add(plantaskvos);
				
			}
		}
		map.put("total", queryResult.getTotalRecord());
		map.put("rows", docvolist);
		return map;
	}

	// 删除任务信息
	public void deleteTask(String ids) {
		if (!StringUtils.isEmpty(ids)) {
			baseDao.delete(TPlanTask.class, Integer.parseInt(ids));
		}
	}

	public TPlanTask QueryPlantask(String id) {
		return baseDao.get(TPlanTask.class, Integer.parseInt(id));
	}

	// 修改任务信息
	public void updateTask(PlanTaskVo planTasks) {
		TPlanTask plantask = baseDao.getEntityById(TPlanTask.class, planTasks
				.getId());
		BeanUtils.copyProperties(planTasks, plantask, new String[] { "id" });
		baseDao.update(plantask);
	}

	@Override
	public void deleteTaskByUser(String puids) {
		// TODO Auto-generated method stub
		if (!StringUtils.isEmpty(puids)) {
			for (String puid : puids.split(",")) {
				List<TPlanTask> list = baseDao
						.find("from TPlanTask where puid=" + puid);
				if(list!=null&&list.size()>0){
					for (TPlanTask planTask : list) {
						baseDao.delete(TPlanTask.class, planTask.getId());
					}
				}
			}
		}
	}
}
