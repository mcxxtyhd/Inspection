package com.inspect.service;

import java.util.List;
import java.util.Map;

import com.inspect.model.monitor.TInspectItemDetailReport;
import com.inspect.model.monitor.TInspectItemReport;
import com.inspect.vo.basis.PlanQueryVo;
import com.inspect.vo.monitor.InspectItemReportVo;
import com.inspect.vo.monitor.TerminaStatusReportVo;

public interface InspectQueryServiceI {
	
	/**
	 * 巡检员状态查询
	 */
	Map<String, Object> findStatusDatagrid(TerminaStatusReportVo terminastatusrepvo,int page, int rows,String qsql);
	
	/**
	 * 巡检数据查询
	 */
	Map<String, Object> findInspectInfoDatagrid(InspectItemReportVo inspectitempvo,int page, int rows,String qsql,String buf1);

	TInspectItemReport getItemReport(String mid);
	
	Map<String, Object> findMessageInfoDatagrid(InspectItemReportVo inspectitempvo, int page, int rows,String qsql);
	/**
	 * 查找巡检项信息，只有一条数据返回
	 */
	List<InspectItemReportVo> findInspectDetailInfo(InspectItemReportVo inspectitempvo);

	Map<String, Object> findInspectInfoDatagrid1(InspectItemReportVo inspectitempvo, int page, int rows, String qsql);
	
	Map<String, Object> findInspectSummaryInfoDatagridByDate(PlanQueryVo planQueryvo, int page, int rows,String qsql);
	
	TInspectItemDetailReport getTInspectItemDetailReport( Integer id  ) ;

	/**
	 *巡检数据统计
	 */
	Map<String, Object> findInspectSummaryInfoDatagrid(PlanQueryVo planQueryvo, int page, int rows, String qsql);

	/**
	 * 巡检数据比对
	 */
	Map<String, Object> findInspectInfoCompareDatagrid(InspectItemReportVo inspectitempvo, int page, int rows, String qsql);
	
	Map<String, Object> findInspectInfoCompareDatagrid1(InspectItemReportVo inspectitempvo, int page, int rows, String qsql,String buf);
	
	/**
	 * 巡检数据修改数据查询
	 * @param inspectitempvo
	 * @param page
	 * @param rows
	 * @param qsql
	 * @return
	 */
	Map<String, Object> inspectmessageModifyDatagrid(InspectItemReportVo inspectitempvo, int page, int rows, int entid);
	//巡检数据修改
	void editDetailReport(InspectItemReportVo detail);
	/**
	 * 根据巡检设备查询关联巡检项
	 */
	Map<String, Object> findProjectByEidDatagrid(InspectItemReportVo inspectitempvo,String qsql);

	void editReportMsgStatus(TInspectItemReport rmsg);
	/**
	 * 首页统计一季度巡检完成情况
	 */
	Map<String, Object> findInspectSummaryInfoDatagrid1(
			PlanQueryVo planQueryvo, int page, int rows, String qsql);
	
}
