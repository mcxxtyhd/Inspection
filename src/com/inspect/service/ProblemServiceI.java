package com.inspect.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.inspect.model.basis.TGroup;
import com.inspect.model.problem.TRepairTask;
import com.inspect.vo.problem.ProblemVo;
import com.inspect.vo.problem.RepairTaskVo;
import com.inspect.vo.summary.SummaryFormVo;

public interface ProblemServiceI {
	/**
	 *  添加巡检项对象
	 */
	void addProblem(ProblemVo problemVo);
	
	
	boolean isAddorEdit(String termid,String equipnum, int entid);
	/**
	 * 修改巡检项对象
	 */
	void editProblem(ProblemVo problemVo);

	/**
	 * 删除巡检项对象
	 */
	void removeProblem(String ids);
	

	/**
	 * 查询巡检项对象 分页
	 */
	Map<String, Object> findProblemDatagrid(ProblemVo problemVo,int page, int rows,String qsql,String buf1);

	/**
	 * 查询巡检项对象 分页
	 */
	Map<String, Object> findProblemDatagrid1(ProblemVo problemVo,int page, int rows,String qsql,String buf1,String buf2 );
	
	/**
	 * 通过终端标号获取信息
	 * @param termid
	 * @return
	 */
	TRepairTask getRepairTask(String termid);
	/**
	 * 通过突发任务主键
	 * @param termid
	 * @return
	 */
	TRepairTask getRepairTask(int id);
	
	public void addRepairTask(RepairTaskVo repairTaskVo);
	
	public void editRepairTask(RepairTaskVo repairTaskVo);
	
	public void removeRepairTask(String ids);
	
	public Map<String, Object> findRepairTaskDatagrid(RepairTaskVo repairTaskVo,int page, int rows,String qsql);

	List<TGroup> getGroupOnLineList(int entid);
	 String problemtoexcel(ProblemVo problemvo,String qsql,int entid);
	 String repairTaskVotoexcel(RepairTaskVo repairTaskVo,String qsql);

}
