package com.inspect.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.inspect.vo.basis.SummaryConfigVo;
import com.inspect.vo.summary.SummaryFormVo;
//统计报表

public interface SummaryServiceI {
	//导出excel表
	String allSummary(String templatePath,SummaryFormVo summaryFormvo,String btype, int entid,HashMap<String, Integer> sconfigMap) ;
	String allSummary1(String ids,SummaryFormVo summaryFormvo,String btype, int entid,HashMap<String, Integer> sconfigMap) ;
	//统计报表查询
	Map<String, Object> findSummaryFormDatagrid(SummaryFormVo summaryFormvo,int page, int rows,String qsql);
	//统计报表查找某次任务某个设备的所有巡检项信息
	Map<String, Object>  findSummaryFormDetialsDatagrid(SummaryFormVo summaryFormvo,int page, int rows,String qsql);
	//修改导出excel表时调用
	Map<String, Object> editExcel(SummaryConfigVo summaryConfigvo,int page, int rows,String qsql);
	HashMap<String, String> getSummaryConfigMap(String ids,int flag);
	HashMap<String, String> getSummaryConfigMap(String ids,int flag,int sConfigFlag);
	String baseinfoListSummary(String templatePath, String btype,String btaskid, String id,int flag);
	//将铁塔配置文件放入t_summary_config表中
	void Test();
	String saveConfigList(List<Object> db, int entid, int flag);
	String getSConfig(int flag);
	String getbids(SummaryFormVo sfVo, String sql);
	
}
