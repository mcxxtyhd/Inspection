package com.inspect.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.inspect.dao.BaseDaoI;
import com.inspect.model.basis.TGroup;
import com.inspect.model.basis.TInspectUser;
import com.inspect.model.basis.TLine;
import com.inspect.model.basis.TPlan;
import com.inspect.model.basis.TPlanTask;
import com.inspect.service.PlanServiceI;
import com.inspect.service.PlanTaskServiceI;
import com.inspect.util.common.QueryResult;
import com.inspect.util.common.StringUtils;
import com.inspect.vo.basis.PlanVo;

@SuppressWarnings("all")
@Service("planService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
public class PlanServiceImpl implements PlanServiceI {
	@Resource
	private BaseDaoI baseDao;
	@Resource
	private PlanTaskServiceI plaTaskService;

	public PlanTaskServiceI getPlaTaskService() {
		return plaTaskService;
	}

	public void setPlaTaskService(PlanTaskServiceI plaTaskService) {
		this.plaTaskService = plaTaskService;
	}

	public BaseDaoI getBaseDao() {
		return baseDao;
	}

	public void setBaseDao(BaseDaoI baseDao) {
		this.baseDao = baseDao;
	}

	// 增加巡检计划
	@Override
	public void addProject(PlanVo planvo) {
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
		TPlan plan = new TPlan();
		BeanUtils.copyProperties(planvo, plan);
		plan.setEntid(planvo.getEntid());
		baseDao.save(plan);
		// 每天一次
		if (planvo.getPtype() == 0) {
			int total = (int) getQuot(planvo.getPstartdate(), planvo.getPenddate())+1;
			try {
				Date date3 = ft.parse(planvo.getPstartdate());
				for (int i = 0; i < total; i++) {
					if (i != 0) {
						Calendar calendar = new GregorianCalendar();
						calendar.setTime(date3);
						calendar.add(calendar.DATE, 1);// 把日期往后增加一天.整数往后推,负数往前移动
						date3 = calendar.getTime(); // 这个时间就是日期往后推一天的结果
					}
					// planvo.setPweekday(ft.format(date3));
					planvo.setPstartdate(ft.format(date3));
					planvo.setPenddate(ft.format(date3));
					plaTaskService.addProject(planvo, plan);
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		// 每周一次
		if (planvo.getPtype() == 1) {
			int total = (int) getQuot(planvo.getPstartdate(), planvo
					.getPenddate()+1);
			try {
				Date date3 = ft.parse(planvo.getPstartdate());

				for (int i = 0; i < total; i++) {
					if (i != 0) {
						Calendar calendar = new GregorianCalendar();
						calendar.setTime(date3);
						calendar.add(calendar.DATE, 1);// 把日期往后增加一天.整数往后推,负数往前移动
						date3 = calendar.getTime(); // 这个时间就是日期往后推一天的结果
					}
					String week = String.valueOf(date3.getDay());
					for (String each : planvo.getPweekday().split(",")) {
						if (week.equals(each.trim())) {
							// planvo.setPweekday(ft.format(date3));
							planvo.setPstartdate(ft.format(date3));
							planvo.setPenddate(ft.format(date3));
							plaTaskService.addProject(planvo, plan);
							break;
						}
					}
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// 每月一次
		if (planvo.getPtype() == 2) {

			int total = (int) getQuot(planvo.getPstartdate(), planvo
					.getPenddate())+1;
			try {
				Date date3 = ft.parse(planvo.getPstartdate());
				Date date4 = ft.parse(planvo.getPinspecttime());
				for (int i = 0; i < total; i++) {
					if (i != 0) {
						Calendar calendar = new GregorianCalendar();
						calendar.setTime(date3);
						calendar.add(calendar.DATE, 1);// 把日期往后增加一天.整数往后推,负数往前移动
						date3 = calendar.getTime(); // 这个时间就是日期往后推一天的结果
					}
					String week = String.valueOf(date3.getDate());
					String day = String.valueOf(date4.getDate());
					if (week.equals(day.trim())) {
						planvo.setPstartdate(ft.format(date3));
						planvo.setPenddate(ft.format(date3));
						plaTaskService.addProject(planvo, plan);
					}
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	public static long getQuot(String time1, String time2) {
		long quot = 0;
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date date1 = ft.parse(time1);
			Date date2 = ft.parse(time2);
			quot = date2.getTime() - date1.getTime();
			quot = quot / 1000 / 60 / 60 / 24;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return quot;
	}

	// 查询所有的巡检任务
	@Override
	public Map<String, Object> findPlanDatagrid(PlanVo planvo, int page,int rows, String hql) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer buf = new StringBuffer(hql);
		if (!StringUtils.isEmpty(planvo.getPname())) {
			buf.append(" and pname like '%").append(planvo.getPname()).append("%'");
		}
		if (planvo.getPlineid() != 0) {
			buf.append(" and plineid = ").append(planvo.getPlineid());
		}
		if (planvo.getPgid() != 0) {
			buf.append(" and pgid = ").append(planvo.getPgid());
		}
		if (planvo.getPuid() != 0) {
			buf.append(" and puid = ").append(planvo.getPuid());
		}
//		if (planvo.getPtype() >= 0) {
//			buf.append(" and ptype = ").append(planvo.getPtype());
//		}
		if (!StringUtils.isEmpty(planvo.getPstartdate())) {
			buf.append(" and pstartdate = '").append(planvo.getPstartdate()).append("'");
		}
//		if (!StringUtils.isEmpty(planvo.getPenddate())) {
//			buf.append(" and penddate <= '").append(planvo.getPenddate()).append("'");
//		}
		if (planvo.getPstatus() != 0) {
			buf.append(" and pstatus = ").append(planvo.getPstatus());
		}
		if (planvo.getEntid()!=0) {
			buf.append(" and entid=").append(planvo.getEntid());
		}
		buf.append(" order by id desc");
		
		System.out.println("PlanServiceImpl查询语句： "+buf.toString());
		
		QueryResult<TPlan> queryResult = baseDao.getQueryResult(TPlan.class,buf.toString(), (page - 1) * rows, rows, null, null);
		List<PlanVo> docvolist = new ArrayList<PlanVo>();
		if (queryResult != null && queryResult.getResultList().size() > 0) {
			for (TPlan te : queryResult.getResultList()) {
				PlanVo planvos = new PlanVo();
				BeanUtils.copyProperties(te, planvos);
				TLine line = baseDao.get(TLine.class, te.getPlineid());
				if (line != null) {
					planvos.setPlinename(line.getLname());
				}
				TInspectUser inspectuser = baseDao.get(TInspectUser.class, te.getPuid());
				if (inspectuser != null) {
					planvos.setUsername(inspectuser.getIuname());
				}
				TGroup g=baseDao.getEntityById(TGroup.class,te.getPgid());
				if(g!=null){
					planvos.setGroupname(g.getGname());
				}
				docvolist.add(planvos);
			}
		}
		map.put("total", queryResult.getTotalRecord());
		map.put("rows", docvolist);
		return map;
	}

	// 验证任务名称不能重复
	public boolean isExist(String planName,int entid) {
		long number = baseDao.count("SELECT COUNT(*) FROM TPlan WHERE pname=? and entid=?",new Object[] { planName,entid });
		return number == 1 ? true : false;
	}

	// 修改巡检计划
	public void editPlan(PlanVo planvo) {
		TPlan tplan = baseDao.getEntityById(TPlan.class, planvo.getId());
		BeanUtils.copyProperties(planvo, tplan, new String[] { "id", "plineid","puid" });
		baseDao.update(tplan);
	}

	// 删除巡检计划
	public void deletePlan(String ids) {
		if (!StringUtils.isEmpty(ids)) {
			TPlan tp = baseDao.getEntityById(TPlan.class, Integer.parseInt(ids));
			Set<TPlanTask> list = tp.getTplantasks();
			if (list != null && list.size() > 0) {
				for (TPlanTask tpt : list) {
					// 删除任务
					baseDao.delete(TPlanTask.class, tpt.getId());
				}
			}
			baseDao.delete(TPlan.class, Integer.parseInt(ids));

		}
	}

	public TPlan QueryTplan(String id) {
		return baseDao.get(TPlan.class, Integer.parseInt(id));
	}

	@Override
	public long taskcount(int id) {
		long number = baseDao.count(
				"SELECT COUNT(*) FROM TPlanTask WHERE pstatus=1 and pid=?",
				new Object[] { id });
		return number;
	}

	@Override
	public boolean isUserHavePlans(int puid) {
		boolean flag = false;
		String hql = " from TPlan where puid=" + puid;
		List list = baseDao.find(hql);
		if (list != null && list.size() > 0) {
			flag = true;
		} else {
			flag = false;
		}
		return flag;
	}

	// 删除巡检计划
	@Override
	public void deletePlanByUser(String puids) {
		if (!StringUtils.isEmpty(puids)) {
			for (String puid : puids.split(",")) {
				List<TPlan> list = baseDao.find("from TPlan where puid=" + puid);
				if(list!=null&&list.size()>0){
					for (TPlan plan : list) {
						baseDao.delete(TPlan.class, plan.getId());
					}
				}
			}
		}
	}

	@Override
	public List<TPlan> findPlanList(int lineid) {
		// TODO Auto-generated method stub
		String hql=" from TPlan where plineid="+lineid;
		List list=baseDao.find(hql);
		return list;
	}
	
	@Override
	public void addPlanLT(PlanVo planvo){
		TPlan plan = new TPlan();
		BeanUtils.copyProperties(planvo, plan);
		TGroup tg=baseDao.getEntityById(TGroup.class,planvo.getPgid());
		Set<TInspectUser> us= tg.getIusers();
		if(us!=null && us.size()>0){
			for(TInspectUser u:us){
				plan.setPuid(u.getId());
			}
		}
		baseDao.save(plan);
		//根据计划生成任务信息
		TPlanTask tplantask = new TPlanTask();
		tplantask.setTplan(plan);
		BeanUtils.copyProperties(planvo, tplantask);
		baseDao.save(tplantask);
	}
	@Override
	public void editPlanLT(PlanVo planvo){
		TPlan tplan=baseDao.getEntityById(TPlan.class,planvo.getId());
		BeanUtils.copyProperties(planvo,tplan,new String[]{"id","entid"});
		baseDao.update(tplan);
		//修改任务信息
		Set<TPlanTask> tplantasks=tplan.getTplantasks();
		if(tplantasks!=null && tplantasks.size()>0){
			for(TPlanTask task:tplantasks){
				BeanUtils.copyProperties(planvo,task,new String[]{"id","entid"});
				baseDao.update(task);
			}
		}
	}
}
