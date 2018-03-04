package com.inspect.service;

import java.util.List;

import java.util.Map;

import com.inspect.model.baseinfo.TBaseInfo;
import com.inspect.model.monitor.TInspectItemDetailReport;
import com.inspect.model.monitor.TInspectItemRaltionReport;
import com.inspect.model.monitor.TInspectItemReport;
import com.inspect.model.monitor.TInspectPointReport;
import com.inspect.model.monitor.TTerminateStatusReport;
import com.inspect.model.monitor.TUserLocateReport;
import com.inspect.vo.monitor.InspectItemReportVo;
import com.inspect.vo.monitor.LineLocateMap;
import com.inspect.vo.monitor.LocateReportMap;
import com.inspect.vo.monitor.SearchLocateMap;

/**
 * 巡检监控管理业务处理Service
 * @author wzs
 */
public interface InspectMonitorServiceI {
	
	List<TTerminateStatusReport>  getUserLocateReportLIst(SearchLocateMap userlocatereport,int qsql,int flag);
	
    List<LocateReportMap> getAvailableTerms(List<String> termIds);
    
    /**
     * 人员轨迹回放
     * @param rptermId 巡检终端ID
     */
    List<String> showTermMove(String rptermId);
    
    /**
     * 获取线路ID
     */
    String getLineIds(int eid);
    
    String getLineIdS(SearchLocateMap smap);
    
    @SuppressWarnings("unchecked")
	List getLineId(int eid);
    
    /**
     * 获取线路下所有巡检点ID
     */
    String getPointIdsByLineId(String lineId);

    List<LineLocateMap> getAvailablePoint(String termIds);
    
    List<String> showTermPoints(String lineid);
    /**gps数据上报
     * 用于
     * 添加TUserLocateReport内容
     * @return 
     */
    void addTUserLocateReport(TUserLocateReport userLocateReport);
    
	/**添加TInspectItemDetailReport信息，用于终端数据提交
	 * @author liao
	 * @param inspectItemDetailReport
	 */
	void addTInspectItemDetailReport(TInspectItemDetailReport inspectItemDetailReport);
	
	
	TInspectItemReport getTInspectItemReport(int id);
	
	int isComplete(int psize,int baseid,int taskid);
	
	/**
	 * 
	 * 判断设备是否已经被巡检过
	 * @param taskid
	 * @param equid
	 * @return false表示未巡检，true表示已巡检
	 */
	List<TInspectItemReport> isEquipInspectStatus(int taskid,int equid);
	
	/**
	 * 通过制定任务id和设备id找到对应的集合，用来或许此对象和判断此任务此设备是否被提交
	 * @param taskid
	 * @param equid
	 * @return  
	 */
	TInspectItemReport getEquipInspectStatus(int taskid,int xequid);
	/**
	 * 通过制定任务id和巡检项id找到对应的集合，用来查询此对象
	 * @param taskid
	 * @param equid
	 * @return  
	 */
	TInspectItemDetailReport getDetailInspectBytaskidAndproid(int taskid,int xproid);
	
	/**
	 * 
	 * 通过制定任务id和巡检项id找到对应的集合，用来查询此巡检项是否被提交
	 * @param taskid
	 * @param equid
	 * @returnfalse表示未巡检，true表示已巡检
	 */
	boolean isDetailInspectStatus(int taskid,int xproid);
	
	/**用于终端数据提交
	 * @author liao
	 * @param inspectItemReport
	 */
	void addTInspectItemReport(TInspectItemReport inspectItemReport); 
	
	/**添加TInspectItemRaltionReport信息，用于终端数据提交
	 * @author liao
	 * @param inspectItemRaltionReport
	 */
	void addTInspectItemRaltionReport(TInspectItemRaltionReport inspectItemRaltionReport); 
    
    /**
     * 提交终端数据时，若出现异常，则运行此方法
     * @author liao
     * @param inspectPointReport
     */
	void addTInspectPointReport(TInspectPointReport inspectPointReport);
	
    /**
     * 线路监控 查看设备巡检上报信息
     */
    Map<String, Object> findInspectMessageDatagridByEId(InspectItemReportVo imessageVo,int page, int rows, String qsql);
    
    List<TUserLocateReport> getUserLocateReportLIstByUid(SearchLocateMap userlocatereport);
    
    List<LocateReportMap> getAvailableUsers(List<String> userIds);
    
    List<String> showTermUserLocate(SearchLocateMap userlocatereport);
    
    Map<String, Object> findEquimentDatagridByPid(InspectItemReportVo imessageVo,String qsql) ;
    
    List<TInspectItemReport> getMessageByEnumberList(InspectItemReportVo msgVo);
    
    List<InspectItemReportVo> getMessageByEnumber(InspectItemReportVo msgVo);

	List<TTerminateStatusReport> getTerminateStatusReportList(
			SearchLocateMap userlocatereport, int qsql);

	void editEntity(Object entity);
	boolean editEntity1(Object entity);

	void editEntity(TInspectItemReport entity);

	void editEntity(Object entity, List list);

	List<TBaseInfo> findEquipBybregion(String bregion);

	List<LocateReportMap> getAvailableTerms_liao(
			SearchLocateMap userlocatereport, int qsql);
}
