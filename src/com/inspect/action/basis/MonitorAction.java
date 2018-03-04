package com.inspect.action.basis;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.BeanUtils;

import com.inspect.action.common.BaseAction;
import com.inspect.model.basis.TInspectUser;
import com.inspect.model.monitor.TInspectItemDetailReport;
import com.inspect.model.monitor.TInspectItemRaltionReport;
import com.inspect.model.monitor.TInspectItemReport;
import com.inspect.service.BaseInfoServiceI;
import com.inspect.service.InspectItemServiceI;
import com.inspect.service.InspectMonitorServiceI;
import com.inspect.service.InspectUserServiceI;
import com.inspect.service.SystemServiceI;
import com.inspect.vo.basis.InspectUserVo;
import com.inspect.vo.basis.PointVo;
import com.inspect.vo.monitor.EquipLocateMap;
import com.inspect.vo.monitor.InspectItemReportVo;
import com.inspect.vo.monitor.LineLocateMap;
import com.inspect.vo.monitor.LocateReportMap;
import com.inspect.vo.monitor.SearchLocateMap;

@Namespace("/monitor")
@Action(value="monitorAction",results={
		@Result(name="showusermap",location="/webpage/monitor/showUserLocateMap.jsp"),
		@Result(name="showlinemap",location="/webpage/monitor/showLineLocateMap.jsp"),
		@Result(name="showEquipmap",location="/webpage/monitor/showEquipLocateMap.jsp"),
		@Result(name="equtMsgView",location="/webpage/monitor/equtMsgView.jsp")
		})
public class MonitorAction extends BaseAction {

	private static final long serialVersionUID = -8865072676300248248L;
	
	@Resource
	private InspectMonitorServiceI inspectMonitorService;
	@Resource
	private InspectUserServiceI inspectUserService;
	@Resource
	private InspectItemServiceI inspectItemService;
	@Resource
	private BaseInfoServiceI baseInfoService;
	@Resource
	private SystemServiceI systemService;
	private String rpgroupid;
	
	public String getRpgroupid() {
		return rpgroupid;
	}

	public void setRpgroupid(String rpgroupid) {
		this.rpgroupid = rpgroupid;
	}

	/**
	 * 加载人员监控地图页面
	 */
	public String showusermap(){
		String basePath=getRequest().getScheme()+"://"+getRequest().getServerName()+":"+getRequest().getServerPort()+getRequest().getContextPath()+"/";
	    getRequest().setAttribute("activeImageUrl",basePath + "images/icon_green.gif");
	    //获取企业列表
		getRequest().setAttribute("EnterpriseList",systemService.comboboxEnterprise(queryEnterpriseByWhere()));
		//加载巡检员列表
	//	getRequest().setAttribute("GroupList",inspectUserService.getGroupList(queryEnterpriseByWhere()));
		getRequest().setAttribute("InspectUserList",inspectUserService.getInspectUserList(queryEnterpriseByWhere()));
		//巡检员、终端数量统计
		getRequest().setAttribute("UserAllCount",inspectUserService.getInspectUserCount(queryEnterpriseByWhere()));
		getRequest().setAttribute("TerminalAllCount",inspectItemService.getInspectTerminalCount(queryEnterpriseByWhere()));
		SearchLocateMap umap=new SearchLocateMap();
		umap.setEntId(getSessionUserName().getEntid());
//		String tdate=DateUtils.dateToString(new Date(),0);
//		umap.setStartDate(tdate);//默认为当前时间
		//今日在线人数
		getRequest().setAttribute("UserOnlineCount",inspectMonitorService.getUserLocateReportLIst(umap,queryEnterpriseByWhere(),0).size());
		//今日在线终端数
		getRequest().setAttribute("TerminalOnlineCount",inspectMonitorService.getUserLocateReportLIst(umap,queryEnterpriseByWhere(),1).size());

		return "showusermap";
	}
	
	/**
	 * 加载线路监控地图页面
	 */
	public String showlinemap(){
		String basePath=getRequest().getScheme()+"://"+getRequest().getServerName()+":"+getRequest().getServerPort()+getRequest().getContextPath()+"/";
	    getRequest().setAttribute("activeImageUrl",basePath + "images/icon_green.gif");
	    getRequest().setAttribute("deactiveImageUrl",basePath +  "images/icon_grey.gif");
	    getRequest().setAttribute("warnigImageUrl",basePath +  "images/icon_red.gif");
		getRequest().setAttribute("LineList",inspectItemService.getlineList(getSessionUserName().getEntid()));
		getRequest().setAttribute("GroupList",inspectUserService.getGroupList(getSessionUserName().getEntid()));
		getRequest().setAttribute("InspectUserList",inspectUserService.getInspectUserList(getSessionUserName().getEntid()));
		return "showlinemap";
	}
	/**
	 * 人员监控地图展现 
	 *  点击查询或初始化地图时
	 *  初始化地图 qtype为0
	 *  查询时 qtype为1
	 *  查询轨迹也是此方法
	 */
	@SuppressWarnings("unchecked")
	public String showUserLocateMap() throws JSONException, IOException {
		String qType=getRequest().getParameter("QType");
		SearchLocateMap umap=new SearchLocateMap();
		List userPosxyList=null;
		umap.setEntId(queryEnterpriseByWhere());
		//qType 1 表示通过查询条件查询  0表示查询所有
		if(qType.equals("1")){
			umap.setIuserName(getRequest().getParameter("RpUserId"));
			umap.setGroupName(getRequest().getParameter("RpGroupId"));
			umap.setEntId(Integer.parseInt(getRequest().getParameter("entid")));
			umap.setStartDate(getRequest().getParameter("Sdate"));
			//umap.setEndDate(getRequest().getParameter("Edate"));
		 //   userPosxyList=inspectMonitorService.getUserLocateReportLIstByUid(umap);//查询某天人员GPS上报位置信的集合,只有在查询轨迹时需要此数据
		}
		//List termIdsList= inspectMonitorService.getTerminateStatusReportList(umap,queryEnterpriseByWhere()); //在登陆状态表查询某天终端id的集合,一个终端只对应一个人，
		List<LocateReportMap> terms = inspectMonitorService.getAvailableTerms_liao(umap,queryEnterpriseByWhere());//用于标注人员坐标点的位置和相应信息
		List<LocateReportMap> userreports=null;
		if(userPosxyList!=null&&userPosxyList.size()>0 ){
		//	 userreports = inspectMonitorService.getAvailableUsers(userPosxyList);//巡检路线轨迹时，用于标注各个坐标点的位置和相应信息
		}
		JSONObject json = new JSONObject();
		json.put("result", terms);//地图显示的基础信息，信息参考类LocateReportMap，当选中查询轨迹时，此数据用不着
		//json.put("rstUsers",userreports);//只有当选中查询轨迹时，用到此数据
//		json.put("rstPoints", termpoints);
//		json.put("rstLines",lineIds);
		return this.outDates(json);
	
	}
	
	/**
	 * 不再用
	 * 人员监控地图展现
	 */
	@SuppressWarnings("unchecked")
	public String showUserLocateMap_zhansheng() throws JSONException, IOException {
		String qType=getRequest().getParameter("QType");
		SearchLocateMap umap=new SearchLocateMap();
		List userPosxyList=null;
		umap.setEntId(queryEnterpriseByWhere());
		//qType 1 表示通过查询条件查询  0表示查询所有
		if(qType.equals("1")){
			umap.setIuserName(getRequest().getParameter("RpUserId"));
			umap.setGroupName(getRequest().getParameter("RpGroupId"));
			umap.setStartDate(getRequest().getParameter("Sdate"));
			umap.setEntId(Integer.parseInt(getRequest().getParameter("entid")));
			//umap.setEndDate(getRequest().getParameter("Edate"));
		    userPosxyList=inspectMonitorService.getUserLocateReportLIstByUid(umap);//查询某天人员GPS上报位置信的集合,只有在查询轨迹时需要此数据
		}
		List termIdsList= inspectMonitorService.getTerminateStatusReportList(umap,queryEnterpriseByWhere()); //在登陆状态表查询某天终端id的集合,一个终端只对应一个人，
		//获取巡检员所巡线路
		String lineIds="";
		String termIds="";
//		lineIds=inspectMonitorService.getLineIdS(umap);
//		if(lineIds!=null&& !lineIds.isEmpty()){
//		    termIds =inspectMonitorService.getPointIdsByLineId(lineIds);//逗号分隔开
//		}
		return disposeShowMap(termIdsList,termIds,lineIds,userPosxyList);
	}
	
	/**
	 * zhansheng 不再用
	 * 地图显示人员信息
	 */
	private String disposeShowMap(List<String> termIdsList,String tremidepoints,String lineIds,List<String> userPosxyList) throws IOException {
		List<LocateReportMap> terms = inspectMonitorService.getAvailableTerms(termIdsList);//用于标注各个坐标点的位置和相应信息
		List<LocateReportMap> userreports=null;
		if(userPosxyList!=null&&userPosxyList.size()>0 ){
			 userreports = inspectMonitorService.getAvailableUsers(userPosxyList);//巡检路线轨迹时，用于标注各个坐标点的位置和相应信息
		}
		JSONObject json = new JSONObject();
		json.put("result", terms);//地图显示的基础信息，信息参考类LocateReportMap，当选中查询轨迹时，此数据用不着
		json.put("rstUsers",userreports);//只有当选中查询轨迹时，用到此数据
//		json.put("rstPoints", termpoints);
//		json.put("rstLines",lineIds);
		return this.outDates(json);
	}
	
	/**
	 * 人员轨迹回放
	 */
	public String showTermMoveMap() throws IOException{
		String rpterminateId = getRequest().getParameter("RpTerminateId");
		List<String> datas = inspectMonitorService.showTermMove(rpterminateId);
		JSONObject json = new JSONObject();
		json.put("result", datas);//巡检人员信息点
		//巡检员轨迹坐标点
		return this.outDates(json);
	}
	
	/**
	 * 线路巡检点监控地图展示
	 */
	public String showLineLocateMap() throws JSONException, IOException {
		//获取所有线路ID
		String searchType=getRequest().getParameter("SearchType");
		String lineIds="";
		String termIds="";
		SearchLocateMap smap=new SearchLocateMap();
		smap.setEntId(getSessionUserName().getEntid());
		//获取符合条件 的所有线路id
		if(searchType.equals("1")){//查询操作
			smap.setLineName(getRequest().getParameter("LineId"));//巡检线路
			smap.setGroupName(getRequest().getParameter("RpGroupId"));
			smap.setIuserName(getRequest().getParameter("RpUserId"));
			smap.setStartDate(getRequest().getParameter("Sdate"));// 开始时间
			smap.setEndDate(getRequest().getParameter("Edate"));//结束时间
			lineIds=inspectMonitorService.getLineIdS(smap);
		}else{
			lineIds=inspectMonitorService.getLineIdS(smap);
		}
		//获取线路id下所有巡检点id
		if(lineIds!=null&& !lineIds.isEmpty()){
		    termIds =inspectMonitorService.getPointIdsByLineId(lineIds);//逗号分隔开
		}
		return disposeShowMapAllLines(termIds,lineIds);
	}
	
	private String disposeShowMapAllLines(String tremides,String lineIds) throws IOException {
		List<LineLocateMap> terms=new ArrayList<LineLocateMap>(StringUtils.isEmpty(tremides) ? 0 : tremides.split(",").length);
		String[] splits = tremides.split(",");
		for(int i = 0; i < splits.length; i++) {
			//获取对应点下面的所有LineLocateMap信息
			List<LineLocateMap> pointid = inspectMonitorService.getAvailablePoint(splits[i]);
			terms.addAll(pointid);
		}
		//此时terms的内部机构是：terms本身是一个List ，其内部还是一个且只有一个对象的LineLocateMap的List，
		JSONObject json = new JSONObject();
		json.put("result", terms);
		//lineIds是符合条件的线路id号
		json.put("rstLines",lineIds);
		return this.outDates(json);
	}
	
	/**不再用
	 * 巡检线路地图展示
	 */
	public String showDrawLineLocateMap() throws JSONException, IOException {
		String rplineId = getRequest().getParameter("LineId");
		JSONObject json = new JSONObject();
		if(!StringUtils.isEmpty(rplineId)){
			List<String> datas = inspectMonitorService.showTermPoints(rplineId);
			json.put("result", datas);
		}
		return this.outDates(json);
	}
	
	//查看人员轨迹标注点信息
	public String showDrawLineUserLocateMap() throws JSONException, IOException {
		SearchLocateMap smap=new SearchLocateMap();
		smap.setEntId(queryEnterpriseByWhere());
		smap.setGroupName(getRequest().getParameter("RpGroupId"));
		smap.setIuserName(getRequest().getParameter("RpUserId"));
		smap.setStartDate(getRequest().getParameter("Sdate"));// 开始时间
		//smap.setEndDate(getRequest().getParameter("Edate"));//结束时间
		JSONObject json = new JSONObject();
		List<String> data = inspectMonitorService.showTermUserLocate(smap);
		json.put("result", data);
		return this.outDates(json);
	}
	
	private String outDates(JSONObject json) throws IOException {
		PrintWriter out = getWebWriter();
		out.write(json.toString());
		out.flush();
		out.close();
		return NONE;
	}
	
	/**
	 * 巡检点下的所有巡检设备消息
	 */
	public void equipmentLoadDatagrid(){
		InspectItemReportVo imessageVo=new InspectItemReportVo();
		imessageVo.setXpid(Integer.parseInt(getRequest().getParameter("PointId")));
		Map<String, Object> map = inspectMonitorService.findEquimentDatagridByPid(imessageVo,querySql());
		writeJson(map);
	}
	
	/**
	 * 巡检点巡检设备消息查看
	 */
	public String equtMsgView(){
		InspectItemReportVo imessageVo=new InspectItemReportVo();
		imessageVo.setXequtnum(getRequest().getParameter("EquNumber"));
		
		List<TInspectItemReport> emsglist=inspectMonitorService.getMessageByEnumberList(imessageVo);
		if(emsglist!=null &&emsglist.size()>0){
			getRequest().setAttribute("EquimentMsgList",emsglist);
			for(TInspectItemReport rep:emsglist){
				List<TInspectItemRaltionReport> reps=rep.getInspectmsgdetailreports();
				if(reps!=null && reps.size()>0){
					for(TInspectItemRaltionReport repdel:reps){
						TInspectItemDetailReport msg=repdel.getTinspectitemdetailrport();
						getRequest().setAttribute("EquimentReportMsg",msg);
					}
				}
			}
		}
		return "equtMsgView";
	}
	/**
	 * 通过区县级联对应的设备信息
	 */
	public void getEquipBybregion(){
		String  bregion=getRequest().getParameter("bregion");
		List<TInspectUser> inspectUserList = new ArrayList<TInspectUser>();
		@SuppressWarnings("unused")
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
		inspectUserList=inspectUserService.findInspectUserByGroupid(bregion);
		List<InspectUserVo> voList=new ArrayList<InspectUserVo>();
		//方法一
		if(inspectUserList!=null&&inspectUserList.size()>0){
			for(int i=0;i<inspectUserList.size();i++){
				InspectUserVo inspectUserVo=new InspectUserVo();
			    BeanUtils.copyProperties(inspectUserList.get(i), inspectUserVo);
			    voList.add(inspectUserVo);
			}
		/*	方法二
		 * for(int i=0;i<inspectUserList.size();i++){
				Map<String, Object> mapClazz = new HashMap<String, Object>();
				mapClazz.put("id", inspectUserList.get(i).getId()+"");
				mapClazz.put("iuname", inspectUserList.get(i).getIuname());
				mapClazz.put("iuname1", inspectUserList.get(i));
				mapList.add(mapClazz);
			}*/
		}
	//	writeJson(mapList);
		writeJson(voList);
	}
	
	
	
	/**
	 * 加载设备监控地图页面
	 */
	public String showEquipmap(){
		String basePath=getRequest().getScheme()+"://"+getRequest().getServerName()+":"+getRequest().getServerPort()+getRequest().getContextPath()+"/";
	    getRequest().setAttribute("tietaImageUrl",basePath + "images/icon_tieta.jpg");
	      getRequest().setAttribute("shineiImageUrl",basePath + "images/icon_shinei.jpg");
	    //区县名称
	    getRequest().setAttribute("regionList",inspectItemService.getRegionList(queryEnterpriseByWhere()));
	    //城市名称
	    getRequest().setAttribute("cityList",inspectItemService.getCityList(queryEnterpriseByWhere()));
		SearchLocateMap umap=new SearchLocateMap();
		umap.setEntId(getSessionUserName().getEntid());

		return "showEquipmap";
	}
	
	
	/**
	 * 设备监控地图展示
	 */
	public String showEquipLocateMap() throws JSONException, IOException {
		SearchLocateMap smap=new SearchLocateMap();
		smap.setEntId(queryEnterpriseByWhere());
		smap.setRpEquipCity(getRequest().getParameter("bcity"));//城市
		smap.setRpEquipRegion(getRequest().getParameter("bregion"));//区县
		smap.setRpEquipAddress(getRequest().getParameter("baddress"));//地址
		smap.setRpEquipName(getRequest().getParameter("rpEquipName"));//区县
		smap.setBtype(getRequest().getParameter("btype"));//区县
		List<EquipLocateMap> terms=new ArrayList<EquipLocateMap>();
		//获取符合条件的设备信息
		terms=baseInfoService.getLineLocateMap(smap);
		JSONObject json = new JSONObject();
		json.put("result", terms);
		return this.outDates(json);
	}
}
