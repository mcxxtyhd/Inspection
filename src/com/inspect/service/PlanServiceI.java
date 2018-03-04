package com.inspect.service;

import java.util.List;
import java.util.Map;

import com.inspect.model.basis.TPlan;
import com.inspect.vo.basis.PlanVo;

public interface PlanServiceI {
	//增加巡检计划
	public void addProject(PlanVo planvo);
	//查询所有的巡检计划信息
	public Map<String, Object> findPlanDatagrid(PlanVo planvo,int page, int rows,String hql);
	//验证巡检名称是否重复
	 public  boolean  isExist(String planName,int entid);
	 //修改巡检计划
	 public void editPlan(PlanVo planvo);
	 //删除巡检计划
	 public void deletePlan(String ids);
	 public TPlan QueryTplan(String id);
	 
	 public long taskcount(int id);
	 //判断巡检员是否有计划和任务
	 public boolean isUserHavePlans(int puid);
	 
	void deletePlanByUser(String puids);
	public List<TPlan> findPlanList(int lineid);
	//添加计划任务
	void addPlanLT(PlanVo planvo);
	//修改计划任务信息
	void editPlanLT(PlanVo planvo);
}