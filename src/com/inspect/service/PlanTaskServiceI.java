package com.inspect.service;

import java.util.Map;

import com.inspect.model.basis.TPlan;
import com.inspect.model.basis.TPlanTask;
import com.inspect.vo.basis.PlanTaskVo;
import com.inspect.vo.basis.PlanVo;

public interface PlanTaskServiceI {
   //增加巡检任务
	public void addProject(PlanVo plantaskvo,TPlan plan);
	//查询所有的巡检任务
	public Map<String, Object> findPlanTaskDatagrid1(PlanTaskVo plantaskvo,int page, int rows,String hql);
	
	//查询所有的巡检任务
	public Map<String, Object> findPlanTaskDatagrid(PlanTaskVo plantaskvo,int page, int rows,String hql);
	//删除任务信息
	 public void deleteTask(String ids);
	 //修改任务信息
	 public  void  updateTask(PlanTaskVo planTasks);
	 public TPlanTask  QueryPlantask(String id);
		//删除任务信息
	 public void deleteTaskByUser(String puid);
	 
}