package com.inspect.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.inspect.model.baseinfo.TBaseInfo;
import com.inspect.model.basis.TEnumParameter;
import com.inspect.model.basis.TEquipment;
import com.inspect.model.basis.TLine;
import com.inspect.model.basis.TPlan;
import com.inspect.model.basis.TPlanTask;
import com.inspect.model.basis.TPoint;
import com.inspect.model.basis.TProject;
import com.inspect.model.basis.TProjectGroup;
import com.inspect.model.basis.TRfid;
import com.inspect.model.basis.TTwoDimensionCode;
import com.inspect.model.monitor.TInspectItemDetailReport;
import com.inspect.model.monitor.TInspectItemRaltionReport;
import com.inspect.model.monitor.TInspectItemReport;
import com.inspect.model.monitor.TInspectPointReport;
import com.inspect.model.system.Forms;
import com.inspect.vo.basis.EnumParameterVo;
import com.inspect.vo.basis.EquipmentVo;
import com.inspect.vo.basis.FormsVo;
import com.inspect.vo.basis.LineVo;
import com.inspect.vo.basis.PlanQueryVo;
import com.inspect.vo.basis.PointVo;
import com.inspect.vo.basis.ProjectVo;
import com.inspect.vo.basis.RfidVo;
import com.inspect.vo.basis.StationVo;
import com.inspect.vo.basis.TProjectGroupVo;
import com.inspect.vo.basis.TwoDimensionCodeVo;
import com.inspect.vo.basis.UnicomVo;

/**
 * 巡检基础数据管理业务处理Service
 * @author wzs
 */
public interface InspectItemServiceI {
	/**
	 *  添加基站对象
	 */
	void addStation(StationVo stationvo);
	
	/**
	 * 修改基站对象
	 */
	void editStation(StationVo stationvo);
	
	/**
	 * 删除基站对象
	 */
	void removeStation(String ids);
	
	Long countStationName(String pname);
	/**
	 * 查询基站对象 分页
	 */
	Map<String, Object> findStationDatagrid(StationVo stationvo,int page, int rows,String qsql);

	/**
	 *  添加巡检项对象
	 */
	void addProject(ProjectVo projectvo);
	
	/**
	 * 修改巡检项对象
	 */
	void editProject(ProjectVo projectvo);

	/**
	 * 删除巡检项对象
	 */
	void removeProject(String ids);
	/**
	 * 获取巡检项名称集合
	 * @param hql
	 * @return
	 */
	String getProjectName(String hql);

	/**
	 * 查询巡检项对象
	 */
	TProject getProject(String id);
	/**
	 * 通过查询巡检项组id查询
	 */
	 List<TProject> getProjectByPgroupid(String pgroupid);

	/**
	 * 查询巡检项对象 分页
	 */
	Map<String, Object> findProjectDatagrid(ProjectVo projectvo,int page, int rows,String qsql);

	/**
	 *  添加巡检设备对象
	 */
	void addEquipment(EquipmentVo equipmentvo);
	
	
	void addForms(FormsVo formsvo);
	/**
	 * 修改巡检设备对象
	 */
	void editEquipment(EquipmentVo equipmentvo);
	/**
	 * 
	 * 判断设备是否已经被巡检过
	 * @param taskid
	 * @param equid
	 * @return
	 */
	boolean isEquipInspectStatus(int taskid,int equid);
	/**
	 * 通过制定任务id和巡检项id找到对应的集合，用来或许此对象和判断此任务此巡检项是否被提交
	 * @param taskid
	 * @param equid
	 * @return  
	 */
	List<TInspectItemDetailReport> getDetailInspectStatus(int taskid,int xproid);

	/**
	 * 删除巡检设备对象
	 */
	void removeEquipment(String ids);

	/**
	 * 查询巡检设备对象
	 */
	TEquipment getEquipment(String id);
	
	
	Forms getForms(String id);
	/**
	 * 
	 */
	TEquipment getEquipmentByequtnum(String xequtnum,int entid);

	/**
	 * 
	 */
	String getEquipmentByename(String ename,int entid);
	
	TEquipment getEquipmentByEtwocodeid(String etwocodeid);
	
	TInspectItemReport getTInspectItemReport(int id);

	/**
	 * 查询巡检设备对象 分页
	 */
	Map<String, Object> findEquipmentDatagrid(EquipmentVo equipmentvo,int page, int rows,String qsql);
	
	/**
	 * 查询巡检项列表
	 */
	List<TProject> getProjectList(int entid);
	
	List<TProject> getProjectList(int entid,String eids,String qtype);
	
	/**
	 *  添加巡检点对象
	 */
	void addPoint(PointVo pointvo);
	
	/**
	 * 修改巡检点对象
	 */
	void editPoint(PointVo pointvo);

	/**
	 * 删除巡检点对象
	 */
	void removePoint(String ids);

	/**
	 * 查询巡检点对象
	 */
	TPoint getPoint(String id);
	
	TPoint getPointByName(String poname,int entid);
	

	/**
	 * 查询巡检点对象 分页
	 */
	Map<String, Object> findPointDatagrid(PointVo pointvo,int page, int rows,String qsql);
	
	
	/**
	 * 联通报表对象 分页
	 */
	Map<String, Object> findUnicomDatagrid(UnicomVo unicomvo,int page, int rows,String qsql);
	
	
	/**
	 * 联通报表对象
	 */
	Map<String, Object> findFormsDatagrid(FormsVo formsvo,int page, int rows,String qsql);
	
	
	/**
	 * 查询巡设备项列表
	 */
	List<TEquipment> getEquipmentList(int entid,String eids,String qtype);
	
	TLine findLineByName(String pointname, int entid);
	
	/**
	 *  添加巡检线路对象
	 */
	void addLine(LineVo linevo);
	
	/**
	 * 修改巡检线路对象
	 */
	void editLine(LineVo linevo);

	/**
	 * 删除巡检线路对象
	 */
	void removeLine(String ids);

	/**
	 * 查询巡检线路对象
	 */
	TLine getLine(String id);

	 boolean isHavePlanAndTask(int lineid);
	/**
	 * 查询巡检线路对象 分页
	 */
	Map<String, Object> findLineDatagrid(LineVo linevo,int page, int rows,String qsql);
	
	/**
	 * 查询巡检计划对象 分页  用于线路状态查询
	 */
	Map<String, Object> findPlanQueryVoDatagrid(PlanQueryVo planQueryvo,int page, int rows,String hql);
	
	/**
	 * 查询巡检点列表
	 */
	List<TPoint> getPointList(int entid,String pids,String qtype);
	
	/**
	 * 查询巡检线路列表
	 */
	List<TLine> getlineList(int entid);
	
	
	/**
	 * 查询巡检线路列表
	 */
	List<TLine> getlineList1(int entid);
	
	
	List<TPlanTask> gettasklist(int entid);
	/**通过用户主键查找任务
	 * @author liao
	 * @param puid
	 * @return
	 */
	List<TPlanTask> getPlanTask(String puid);
	
	/**
	 *  添加二维码对象
	 */
	void addTwoDimensionCode(TwoDimensionCodeVo twodimensioncodevo);
	
	/**
	 * 修改二维码对象
	 */
	void editTwoDimensionCode(TwoDimensionCodeVo twodimensioncodevo);

	/**
	 * 删除二维码对象
	 */
	void removeTwoDimensionCode(String ids);

	/**
	 * 查询二维码对象
	 */
	TTwoDimensionCode getTwoDimensionCode(String id);
	


	/**
	 * 查询二维码对象 分页
	 */
	Map<String, Object> findTwoDimensionCodeDatagrid(TwoDimensionCodeVo twodimensioncodevo,int page, int rows,String qsql);

	/**
	 * 查询二维码列表
	 */
	List<TTwoDimensionCode> getTwoDimensionCodeList(int entid);
	
	/**
	 *  添加RFID对象
	 */
	void addRfid(RfidVo rfidvo);
	
	/**
	 * 修改RFID对象
	 */
	void editRfid(RfidVo rfidvo);

	/**
	 * 删除RFID对象
	 */
	void removeRfid(String ids);

	/**
	 * 查询RFID对象
	 */
	TRfid getRfid(String id);

	/**
	 * 查询RFID对象 分页
	 */
	Map<String, Object> findRfidDatagrid(RfidVo rfidvo,int page, int rows,String qsql);
	
	/***
	 * 添加巡检项组
	 * 
	 * @param projectGroupVo
	 */
	void addProblemGroup(TProjectGroupVo projectGroupVo);
	/*
	 * 
	 */
	Long isExistProjectGroup(String pgname,int entid);
	/**
	 * 删除巡检项组
	 */
	void removeProjectGroup(String ids);
	/**
	 * 获取巡检项
	 */
	TProjectGroup getProjectGroup(int id);
	/**
	 * 修改巡检项组
	 */
	void editProjectGroup(TProjectGroupVo projectGroupVo);
	/**
	 * 查询TProjectGroup对象 分页
	 */
	Map<String, Object> findProjectGroupDatagrid(TProjectGroupVo projectGroupVo,int page, int rows,String qsql);
	/**
	 * @author liao
	 * @param id
	 * @return
	 */
	//查找巡检项
	TLine findLine(int id);
	
	TPoint findPointByName(String pointname,int entid);
	/**
	 * @author liao
	 * @param id
	 * @return
	 */
   //查询巡检点
	TPoint findPoint(int id);
	/**
	 * @author liao
	 * @param id
	 * @return
	 */
	//查询设备
	TEquipment findEquipment(int id);
	/**
	 * @author liao
	 * @param id
	 * @return
	 */
	//查询巡检项
	TProject findProject(int id);
	/**
	 * @author liao
	 * @param id
	 * @return
	 */
	//通过cname获取二维码信息
	TTwoDimensionCode getTwoDimensionCodeByCname(String cname);
	

	/**
	 * 查询RFID列表
	 */
	List<TRfid> getRfidList(int entid);
	
	Long getInspectTerminalCount(int eid);
	
	Long countProjectName(String pname,int entid);

	Long countEquipmentNumber(String pnumber,int entid);
	
	Long countFormsNumber(String flicense);

	Long countPointName(String pname,int entid);

	Long countLineName(String lname,int entid);

	/**
	 * 添加图片
	 * @param inspectItemDetailReport
	 * @param inspectItemReport
	 */
	int addPicture(TInspectItemDetailReport inspectItemDetailReport,
			TInspectItemReport inspectItemReport,TInspectItemRaltionReport inspectItemRaltionReport);

	void addData(TInspectItemDetailReport inspectItemDetailReport,
			TInspectItemReport inspectItemReport,TInspectItemRaltionReport inspectItemRaltionReport,TInspectPointReport inspectPointReport,String status);
	List<TProjectGroup> getProjectGroupList(String sqhl);

	List<TProjectGroup> getProjectGroupList(int entid, String eids, String qtype);

	public <T> T getEntityById(Class<T> paramClass, Serializable id);

	void addEnumParameter(EnumParameterVo enumParameterVo);

	void editEnumParameter(EnumParameterVo enumParameterVo);

	void removeEnumParameter(String ids);

	Map<String, Object> findEnumParameterDatagrid(EnumParameterVo enumParameterVo, int page, int rows, String qsql);

	/**
	 * 根据枚举名称查询枚举值列表
	 */
	List<TEnumParameter> getEnumParameListByParameName(String hql, String pname);

	/**
	 * 根据参数类型查询参数列表
	 */
	List<TEnumParameter> getEnumParameListByParameType(String hql, String ptype);


	boolean isInspect(int projectid,int itaskid);

	List<TPlanTask> getPlanTaskList(int qsql);

	Map<String, Object> findEquipmentInfoDatagrid(EquipmentVo equipmentvo, int page, int rows, String qsql);

	Long countEnumName(String pname, int entid);

	Map<String, Object> findEquipmentInfoDatagrid(EquipmentVo equipmentvo, String qsql);

	String saveEquipmentProjectGroupList(List<Object> db, int entid);
	String savePointList(List<Object> db, int entid);
	String saveLineList(List<Object> db, int entid);

	String getEquipment1Byenameornum(String ename, int entid,int flag);
	//通过设备续签分类级联查询设备区县，用于巡检线点的增加修改
	List<TEquipment> getRetionList(int entid, String eids, String qtype);
	
	//通过设备续签分类级联查询设备详细信息，用于巡检线点的增加修改
	List<TEquipment> getEquipListByRegion(int entid, String region,String eids, String qtype);
	//通过设备续签分类查询，用于设备地图监控
	List<TBaseInfo> getCityList(int entid);
	List<TBaseInfo> getRegionList(int entid);

	List<PointVo> getPointListByRegion(int entid, String region,
			String eids, String qtype);
	
	String test(String ename, int entid,String flag);

	List<TEquipment> getEquipList(String region,String ename,String eids,String qtype,int entid);

	List<PointVo> getPointList(String region, String poname, String eids,String qtype, int entid);
}
