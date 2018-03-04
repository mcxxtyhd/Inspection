package com.inspect.action.webservice;

import java.io.BufferedInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.hibernate.SQLQuery;
import org.springframework.beans.BeanUtils;

import com.alibaba.fastjson.JSON;
import com.hp.hpl.sparta.xpath.ThisNodeTest;
import com.inspect.action.common.BaseAction;
import com.inspect.constant.Constant;
import com.inspect.dao.BaseDaoI;
import com.inspect.model.baseinfo.TBaseInfoEquipment;
import com.inspect.model.basis.TEnumParameter;
import com.inspect.model.basis.TEquipment;
import com.inspect.model.basis.TEquipmentProjectGroup;
import com.inspect.model.basis.TInspectUser;
import com.inspect.model.basis.TLine;
import com.inspect.model.basis.TLinePoint;
import com.inspect.model.basis.TPlanTask;
import com.inspect.model.basis.TPoint;
import com.inspect.model.basis.TPointEquipment;
import com.inspect.model.basis.TProject;
import com.inspect.model.basis.TProjectGroup;
import com.inspect.model.basis.TVersion;
import com.inspect.model.monitor.TInspectItemDetailReport;
import com.inspect.model.monitor.TInspectItemReport;
import com.inspect.model.monitor.TTerminateStatusReport;
import com.inspect.model.monitor.TUserLocateReport;
import com.inspect.model.problem.TRepairTask;
import com.inspect.model.system.Parameter;
import com.inspect.service.HttpServiceI;
import com.inspect.service.InspectItemServiceI;
import com.inspect.service.InspectMonitorServiceI;
import com.inspect.service.InspectUserServiceI;
import com.inspect.service.ProblemServiceI;
import com.inspect.service.SystemServiceI;
import com.inspect.service.VersionServiceI;
import com.inspect.util.common.DateUtils;
import com.inspect.util.common.StringUtils;
import com.inspect.vo.HttpVo.TermStatus;
import com.inspect.vo.basis.StationVo;
import com.inspect.vo.basis.TermEnumParameterVo;
import com.inspect.vo.basis.TermEquipmentVo;
import com.inspect.vo.basis.TermInspectUserVo;
import com.inspect.vo.basis.TermPlanVo;
import com.inspect.vo.basis.TermPointVo;
import com.inspect.vo.basis.TermProjectGroupVo;
import com.inspect.vo.basis.TermProjectVo;
import com.inspect.vo.monitor.InspectItemDetailReportVo1;
import com.inspect.vo.monitor.TermInspectItemReportVo1;
import com.inspect.vo.problem.ProblemVo;
import com.inspect.vo.problem.RepairTaskVo;
import com.itextpdf.text.pdf.PdfStructTreeController.returnType;

/**
 * 巡检平台与终端通信交互流程控制类
 * 
 * @version 1.0
 * @since 2013-07-02
 */
@Namespace("/web")
@Action(value = "webAction", results = { @Result(name = "interetest", location = "/webpage/login/test.jsp") })
public class HttpAction extends BaseAction {
	private static final long serialVersionUID = -1087979206488280646L;
	private InspectItemServiceI inspectItemService;
	private SystemServiceI systemService;
	private InspectUserServiceI inspectUserService;
	private HttpServiceI httpService;
	private ProblemServiceI problemService;
	private InspectMonitorServiceI inspectMonitorService;
	private File uploadFile;// 得到上传的文件
	private String uploadFileContentType;// 得到文件的类型
	private String uploadFileFileName;// 得到文件的名称

	@Resource
	private BaseDaoI baseDao;

	@Resource
	private VersionServiceI versionService;

	@Resource
	public void setHttpService(HttpServiceI httpService) {
		this.httpService = httpService;
	}

	@Resource
	public void setProblemService(ProblemServiceI problemService) {
		this.problemService = problemService;
	}

	@Resource
	public void setInspectItemService(InspectItemServiceI inspectItemService) {
		this.inspectItemService = inspectItemService;
	}

	@Resource
	public void setSystemService(SystemServiceI systemService) {
		this.systemService = systemService;
	}

	@Resource
	public void setInspectUserService(InspectUserServiceI inspectUserService) {
		this.inspectUserService = inspectUserService;
	}

	@Resource
	public void setInspectMonitorService(
			InspectMonitorServiceI inspectMonitorService) {
		this.inspectMonitorService = inspectMonitorService;
	}

	public File getUploadFile() {
		return uploadFile;
	}

	public void setUploadFile(File uploadFile) {
		this.uploadFile = uploadFile;
	}

	public String getUploadFileContentType() {
		return uploadFileContentType;
	}

	public void setUploadFileContentType(String uploadFileContentType) {
		this.uploadFileContentType = uploadFileContentType;
	}

	public String getUploadFileFileName() {
		return uploadFileFileName;
	}

	public void setUploadFileFileName(String uploadFileFileName) {
		this.uploadFileFileName = uploadFileFileName;
	}

	/**
	 * 终端用户登录 参数：用户编号，密码 登录成功：返回 inspectUserVo对象的内容 登录失败：返回用户名称为""
	 */
	public void interlogin() {
		Map<String, Object> map = new HashMap<String, Object>();
		String iuname1 = getRequest().getParameter("iuname");
		String iupwd = getRequest().getParameter("iupwd");
		String termid = getRequest().getParameter("termid");
		try {
			String iuname = new String(iuname1.getBytes("ISO-8859-1"), "utf-8");
			if (StringUtils.isNotEmpty(iuname) && StringUtils.isNotEmpty(iupwd)
					&& StringUtils.isNotEmpty(termid)) {
				TInspectUser inspectUser = new TInspectUser();
				// 判断终端是否存在，false表示不存在，true表示存在
				boolean flag1 = inspectUserService.getTerminalByTermid(termid);
				if (flag1 == false) {
					map.put("reason", "终端不存在");
					map.put("result", 1);
					writeJson(map);
					return;
				}
				// 检测用户名是否存在
				inspectUser = inspectUserService.checkTInspectUser(iuname,
						iupwd);
				TermInspectUserVo inspectUserVo = new TermInspectUserVo();
				// 用户密码正确
				if (inspectUser != null) {
					// 判断是否已经登陆
					BeanUtils.copyProperties(inspectUser, inspectUserVo);
					if (inspectUser.getTgroup() != null) {
						inspectUserVo.setGroupid(String.valueOf(inspectUser
								.getTgroup().getId()));
					}
					if (inspectUser.getTgroup() != null
							&& inspectUser.getTgroup().getGname() != null) {
						inspectUserVo.setGroupname(inspectUser.getTgroup()
								.getGname());
					}
					Double rmark = Math.random();  //标志，作为是否重新登陆的唯一标志
					// 将标识码放入用户中，一同传给终端
					inspectUserVo.setImark(String.valueOf(rmark));
					// 保存终端状态信息
					TTerminateStatusReport terminateStatusReport = new TTerminateStatusReport();
					// //获取终端范围
					Parameter param = null;
					param = systemService.getParameter("终端半径"); //?为啥要设置终端半径
					if (param == null) {
						// map.put("reason",
						// "请在参数管理中设置终端覆盖范围，并将名称写为'终端半径'，单位为米");
						map.put("reason", "登陆失败，请联系管理员");
						map.put("result", 1);
						writeJson(map);
						return;
					} else {

						inspectUserVo.setRadius(param.getPvalue());
					}
					Parameter param1 = null;
					// 获取室内图片数量
					param1 = systemService.getParameter("铁塔图片数量");
					if (param1 == null) {
						// map.put("reason", "请在参数管理中设置铁塔图片数量（参数名称即为铁塔图片数量）");
						map.put("reason", "登陆失败，请联系管理员");
						map.put("result", 1);
						writeJson(map);
						return;
					} else {
						inspectUserVo.setTietapicnum(param1.getPvalue());
					}
					Parameter param2 = null;
					// 获取室内图片数量
					param2 = systemService.getParameter("室内图片数量");
					if (param2 == null) {
						// map.put("reason", "请在参数管理中设置室内图片数量（参数名称即为室内图片数量）");
						map.put("reason", "登陆失败，请联系管理员");
						map.put("result", 1);
						writeJson(map);
						return;
					} else {
						inspectUserVo.setShineipicnum(param2.getPvalue());
					}
					// 随机生成一个标志码
					terminateStatusReport.setRmark(String.valueOf(rmark));  //巡检人员实时上报信息实体类
					String date = DateUtils.getFormatSecondDate();
					// 设置终端状态为1，表示在线（因为终端与人是同时登录同时退出的，不存在单独的登录退出，所以公用一个标志位），0表示不在线
					terminateStatusReport.setFlag(1);
					terminateStatusReport.setEntid(inspectUserVo.getEntid());
					terminateStatusReport.setRpgroupid(Integer
							.parseInt(inspectUserVo.getGroupid()));
					terminateStatusReport.setRplogintime(date);
					terminateStatusReport.setRpuserid(inspectUserVo.getId());
					terminateStatusReport.setRpterminateid(termid);
					// 将终端最后一条记录的状态改为0，然后新增一条终端状态
					TTerminateStatusReport term = inspectUserService
							.geTerminateStatusReport(termid);   //获得该终端的最后一条记录
					int flag = httpService.addTerminateStatusReport(term,
							terminateStatusReport);    //如果该终端第一次使用就保存terminateStatusReport，否则保存term。
					// 数据保存异常
					if (flag == 1) {
						map.put("reason", "登陆异常，请联系平台");
						map.put("result", 1);
						writeJson(map);
						return;
					}
					map.put("result", 0);
					map.put("inspectUserVo", inspectUserVo);
					writeJson(map);
				} else {
					map.put("reason", "用户名或密码错误");
					map.put("result", 1);
					writeJson(map);
					return;
				}
			} else {
				map.put("reason", "用户名、密码和终端编号不能为空");
				map.put("result", 1);
				writeJson(map);
				return;
			}
		} catch (Exception e) {
			map.put("reason", "请输入合适的值");
			map.put("result", 1);
			writeJson(map);
			return;
		}
	}

	/**
	 * 终端用户注销 参数：用户编号 成功：返回结果0和用户编号 失败：返回1和用户编号
	 */
	public void interloginOut() {
		String imark = getRequest().getParameter("imark");
		String puid = getRequest().getParameter("puid");
		String termid = getRequest().getParameter("termid");
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			if (StringUtils.isNotEmpty(puid) && StringUtils.isNotEmpty(termid)) {
				String date = DateUtils.getFormatSecondDate();  //yyyy-MM-dd HH:mm:ss
				TTerminateStatusReport term = inspectUserService
						.geTerminateStatusReport(termid);
				TermStatus tStatus = new TermStatus();
				tStatus = httpService.judgeValid(term, imark);
				if (tStatus.getFlag() != 0) {
					map.put("reason", tStatus.getReason());
					map.put("result", tStatus.getFlag());
					writeJson(map);
					return;
				}
				term.setRplogouttime(date);
				term.setFlag(0);
				// 注销
				inspectUserService.addTerminateStatusReport(term);//保存状态
				map.put("result", 0);
				writeJson(map);
			} else {
				map.put("reason", "请输入用户编号、终端编号和标志码");
				map.put("result", 1);
				writeJson(map);
				return;
			}
		} catch (Exception e) {
			map.put("reason", "请输入正确的用户编号和终端编号");
			map.put("result", 1);
			writeJson(map);
			return;
		}
	}

	/**
	 * interfindTask的更新版
	 * 
	 * 主要目的在于提高执行效率
	 * 
	 * 
	 */

	/*
	 * 
	 * 获取本次登录的任务清单，以及对应的设备清单，包括设备中的巡检项组清单 SELECT GROUP_CONCAT(epg.pgid),
	 * pt.Pstartdate ,lp.lineid,pt.id AS taskid, l.lname AS lname,lp.pointid,
	 * e.* FROM t_point_equipment pe LEFT JOIN t_equipment e
	 * ON(pe.equipmentid=e.id) LEFT JOIN t_equipment_project_group epg ON
	 * (e.id=epg.eid) LEFT JOIN t_line_point lp ON (pe.pointid = lp.pointid)
	 * LEFT JOIN T_Plan_Task pt ON (lp.lineid = pt.plineid) LEFT JOIN t_line l
	 * ON (lp.lineid = l.id) WHERE pt.pgid='185' AND ( pt.pstartdate='2014-05'
	 * OR pt.pstartdate='2014-04') GROUP BY e.id;
	 */

	/*
	 * 
	 * 获取本次登录中，所有可能的巡检项组的id SELECT epg.pgid FROM t_point_equipment pe LEFT JOIN
	 * t_equipment e ON(pe.equipmentid=e.id) LEFT JOIN t_equipment_project_group
	 * epg ON (e.id=epg.eid) LEFT JOIN t_line_point lp ON (pe.pointid =
	 * lp.pointid) LEFT JOIN T_Plan_Task pt ON (lp.lineid = pt.plineid) LEFT
	 * JOIN t_line l ON (lp.lineid = l.id) WHERE pt.pgid='185' AND (
	 * pt.pstartdate='2014-05' OR pt.pstartdate='2014-04') GROUP BY epg.pgid;
	 */

	public void interfindTask() {
		
		Map<String, Object> map = new HashMap<String, Object>();
		String pgid = getRequest().getParameter("pgid");// 巡检项组id
		String termid = getRequest().getParameter("termid");
		String imark = getRequest().getParameter("imark");
		String lastmonth = getRequest().getParameter("lastmonth");// 上月
		boolean lastmonthflag = StringUtils.isNotEmpty(lastmonth);// 看是否是查询上月的，若是上月，则为true,本月为false
		
		if (StringUtils.isEmpty(imark) || StringUtils.isEmpty(termid)
				|| StringUtils.isEmpty(pgid)) {
			map.put("reason", "标志码、终端编号和班组编号不能为空");
			map.put("result", 1);
			writeJson(map);
			return;
		}
		// 检测是否过期和验证码是否匹配
		TTerminateStatusReport term = inspectUserService
				.geTerminateStatusReport(termid);
		TermStatus tStatus = new TermStatus();
		tStatus = httpService.judgeValid(term, imark);
		if (tStatus.getFlag() != 0) {
			map.put("reason", tStatus.getReason());
			map.put("result", tStatus.getFlag());
			writeJson(map);
			return;
		}

		StringBuffer equipmentSql = new StringBuffer();
		String lastmonthSql = lastMonth(pgid);

		equipmentSql

				.append("SELECT GROUP_CONCAT(cast(epg.pgid as char)),	pt.Pstartdate ,lp.lineid,pt.id AS taskid, l.lname AS lname,lp.pointid,e.*,be.bid");

		equipmentSql
				.append(" FROM t_point_equipment pe 	LEFT JOIN  t_equipment e ON(pe.equipmentid=e.id)  ");
		equipmentSql.append(" 	LEFT JOIN t_base_info_equipment be ON ( e.id = be.eid)");
		equipmentSql
				.append(" LEFT JOIN  t_equipment_project_group epg ON (e.id=epg.eid) LEFT JOIN t_line_point lp ON (pe.pointid = lp.pointid)");
		equipmentSql
				.append(" LEFT JOIN  T_Plan_Task pt ON (lp.lineid = pt.plineid) LEFT JOIN t_line l ON (lp.lineid = l.id) ");
		equipmentSql.append(lastmonthSql);
		equipmentSql.append(" GROUP BY e.id,pt.id order by pt.Pstartdate desc");

		//System.out.println(equipmentSql.toString());

		try{
		SQLQuery query = baseDao.getHibernaSession().createSQLQuery(
				equipmentSql.toString());
		List list=null;
		try {
			 list = query.list();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		List<Object[]> rowlist = list;


		List<TermPlanVo> taskGroup = list2object(rowlist);

		if(taskGroup == null || taskGroup.size() == 0){
			map.put("result", 1);
			map.put("reason", "本用户没有任务或任务内容为空");
			writeJson(map);
			return;
		}
		StringBuffer projectgroupSql = new StringBuffer();
		projectgroupSql.append("SELECT epg.pgid, epg.id");
		projectgroupSql
				.append(" FROM t_point_equipment pe 	LEFT JOIN  t_equipment e ON(pe.equipmentid=e.id) ");
		projectgroupSql
				.append(" LEFT JOIN  t_equipment_project_group epg ON (e.id=epg.eid) LEFT JOIN t_line_point lp ON (pe.pointid = lp.pointid)");
		projectgroupSql
				.append(" LEFT JOIN  T_Plan_Task pt ON (lp.lineid = pt.plineid) LEFT JOIN t_line l ON (lp.lineid = l.id)");
		projectgroupSql.append(lastmonthSql);
		projectgroupSql.append(" GROUP BY epg.pgid");

	//	System.out.println(projectgroupSql.toString());

		SQLQuery pgquery = baseDao.getHibernaSession().createSQLQuery(
				projectgroupSql.toString());
		List<Object[]> pglist = pgquery.list();

		List<TermProjectGroupVo> pgGroup = list2pg(pglist);
		
		if(pgGroup == null || pgGroup.size() == 0){
			map.put("result", 1);
			map.put("reason", "本任务没有设置巡检项");
			writeJson(map);
			return;			
		}
		
		map.put("result", 0);
		map.put("tpGroupList1", pgGroup);
		map.put("planVoList", taskGroup);
		writeJson(map);

		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public List<TermProjectGroupVo> list2pg(List<Object[]> list) {

		if (list == null || list.size() == 0)
			return null;

		List<TermProjectGroupVo> pgGroup = new ArrayList<TermProjectGroupVo>();

		List<Object> listobj = new ArrayList<Object>();

		for (int i = 0; i < list.size(); i++) {
			Object[] object = (Object[]) list.get(i);// 每行记录不在是一个对象 而是一个数组
				if(object[0]==null){
					continue;
				}
			int id = (Integer) object[0];

			TProjectGroup tpGroup = new TProjectGroup();
			TermProjectGroupVo tpGroupVo = new TermProjectGroupVo();
			tpGroup = inspectItemService.getProjectGroup(id);
			BeanUtils.copyProperties(tpGroup, tpGroupVo);

			if (tpGroup.getTprojects() == null
					|| tpGroup.getTprojects().size() == 0) {
				continue;
			}
			for (TProject project : tpGroup.getTprojects()) {
				TermProjectVo tprojectVo = new TermProjectVo();  
				BeanUtils.copyProperties(project, tprojectVo);
				// 如果巡检项类型为1，则表示为枚举值，那必须获取枚举值对应的内容
				if (tprojectVo.getPtype().equals("1")) {
					List<TEnumParameter> evList = null;  //枚举参数类实体类
					List<TermEnumParameterVo> evList1 = new ArrayList<TermEnumParameterVo>();
					evList = inspectItemService.getEnumParameListByParameName(
							" 1=1 ", tprojectVo.getPenumvalue());    //penumvalue;//关联枚举值
					if (evList != null) {
						for (TEnumParameter enums : evList) {
							TermEnumParameterVo teParameter = new TermEnumParameterVo();
							BeanUtils.copyProperties(enums, teParameter);
							evList1.add(teParameter);
						}
					}
					if (evList != null) {
						tprojectVo.setEvList(evList1);
					}
				}
				tpGroupVo.getTprojectList().add(tprojectVo);
			}
			com.inspect.vo.basis.ComparatorProject comparator = new com.inspect.vo.basis.ComparatorProject();
			Collections.sort(tpGroupVo.getTprojectList(),
					comparator);
			pgGroup.add(tpGroupVo);
		}
	
		return pgGroup;
	}

	public List<TermPlanVo> list2object(List list) {
		if (list == null || list.size() == 0)
			return null;

		List<TermPlanVo> taskGroup = new ArrayList<TermPlanVo>();

		List<Object> listobj = new ArrayList<Object>();

		for (int i = 0; i < list.size(); i++) {
			Object[] object = (Object[]) list.get(i);// 每行记录不在是一个对象 而是一个数组

			String pggroup = (String) object[0];
			String pstartdate = (String) object[1];
			int lineid = (Integer)object[2];
			int taskid = (Integer) object[3];
			String linename = (String) object[4];
			int pointid = (Integer)object[5];
			if(object[6]==null){  //点集合，点中包含设备
				continue;
			}
			int eid = (Integer) object[6];//equipment的id
			if(object[20]==null){
				continue;
			}
			int bid = (Integer) object[20];//baseinfo的id
			int entid = (Integer) object[7];
			String eaddress = (String) object[8];
			// efactory [9]
			String ename = (String) object[10];
			String enumber = (String) object[11];
			double eposx = (Double)object[12];
			double eposy = (Double) object[13];
			// erfid
			// etwocodeid
			String etype = (String) object[16];
			// sdesc
			String ecity = (String) object[18];
			String eregion = (String) object[19];

			// task、line在这里等同，因为是一对的关系
			TermPlanVo task = gettask(taskGroup, taskid);
			if (task == null) {
				TermPlanVo t = new TermPlanVo();
				t.setId(lineid);
				t.setItaskid(taskid);
				t.setEntid(entid);
				
				//历史月份的增加其日期，提醒用户
				if(islastmonth( pstartdate)){
					t.setLname(linename + "(" + pstartdate + ")");
				}else{
					t.setLname(linename);
				}
					
				t.setPstartdate(pstartdate);

				taskGroup.add(t);

				task = t;
			}

			TermPointVo point = getpoint(task, pointid);
			if (point == null) {
				TermPointVo p = new TermPointVo();
				p.setPointid(pointid);

				task.getTpointVoList().add(p);

				point = p;
			}

			if (!inpoint(point, bid)) {
				TermEquipmentVo e = new TermEquipmentVo();

				List reportlist = inspectMonitorService.isEquipInspectStatus(
						taskid, bid);
				e.setInspectStatus(false);
				if (reportlist != null && reportlist.size() > 0) {
					TInspectItemReport itemReport = (TInspectItemReport) reportlist
							.get(reportlist.size() - 1);
					e.setXstatus(itemReport.getXstatus());
					e.setInspectStatus(true);

					// 是否是上月的计划，需要排除已经巡检完毕的设备 且 该设备已经是巡检完成状态，则不添加入列表
					if (islastmonth(pstartdate)
							&& itemReport.getLastmonthflag() == 1) {
						continue;
					}
				}
				e.setEaddress(eaddress);
				e.setEcity(ecity);
				e.setEname(ename);
				e.setEnumber(enumber);
				e.setEposx(eposx);
				e.setEposy(eposy);
				e.setEregion(eregion);
				e.setEtype(etype);
				e.setId(bid);
				e.setEid(eid);
				e.setTpgroupids(pggroup);

				point.getEquipmentList().add(e);
			}
		}
	
		return taskGroup;
	}
	
	
	
	
	/**
	 * 返回终端已巡检和为巡检的巡检项
	 * @param list
	 * @return
	 */
	
	public  void intergetpvalue() {
		Map<String, Object> map = new HashMap<String, Object>();
		List<TermProjectGroupVo> tpGroupList1 = new ArrayList<TermProjectGroupVo>();
		try {
			String eid = getRequest().getParameter("eid");
			String taskid = getRequest().getParameter("taskid");
			String bid = getRequest().getParameter("bid");
			//从detail表中获取已经提交的巡检项的信息
			List<TermProjectVo> plist=new ArrayList();
			StringBuffer buf1=new StringBuffer();
			buf1.append(" SELECT b.xproid,b.xvalue FROM t_report_message  a LEFT JOIN t_report_message_detail b ON(a.id=b.msgid) WHERE  a.xequid="+bid+" AND a.xtaskid="+taskid);
			SQLQuery query1 = baseDao.getHibernaSession().createSQLQuery(buf1.toString());
			List<Object[]> pglist1 = query1.list();
			Map<Integer,String > map1=new HashMap<Integer,String >();
			if(pglist1!=null&&pglist1.size()>0){
				for (int i = 0; i < pglist1.size(); i++) {
					Object[] obj=pglist1.get(i);
					map1.put((Integer)obj[0], (String)obj[1]);//将提交的巡检项id和值放入map中
				}
			}
			//从巡检项表中获取巡检项信息
			StringBuffer buf=new StringBuffer();
			buf.append(" select pgid from t_equipment_project_group where eid="+eid);
			SQLQuery query = baseDao.getHibernaSession().createSQLQuery(buf.toString());
			List<Object[]> pglist = query.list();
			Integer gid=0;
			if(pglist!=null&&pglist.size()>0){
				for (int i = 0; i < pglist.size(); i++) {
					Object gproject=pglist.get(i);
					gid=(Integer) gproject;
					TProjectGroup tpGroup = new TProjectGroup();
					TermProjectGroupVo tpGroupVo = new TermProjectGroupVo();
					tpGroup = inspectItemService.getProjectGroup(gid);
					BeanUtils.copyProperties(tpGroup, tpGroupVo);
		
					if (tpGroup.getTprojects() != null
							&& tpGroup.getTprojects().size() > 0) {
						for (TProject project : tpGroup.getTprojects()) {
							TermProjectVo tprojectVo = new TermProjectVo();
							BeanUtils.copyProperties(project, tprojectVo);
							String pvalue=map1.get(tprojectVo.getId());
							if(pvalue!=null){
								tprojectVo.setPvalue(pvalue);
							}
							/*else{
								tprojectVo.setPvalue(" 11");
							}*/
							// 如果巡检项类型为1，则表示为枚举值，那必须获取枚举值对应的内容
							if (tprojectVo.getPtype().equals("1")) {
								List<TEnumParameter> evList = null;
								List<TermEnumParameterVo> evList1 = new ArrayList<TermEnumParameterVo>();
								evList = inspectItemService
										.getEnumParameListByParameName(" 1=1 ",
												tprojectVo.getPenumvalue());
								if (evList != null) {
									for (TEnumParameter enums : evList) {
										TermEnumParameterVo teParameter = new TermEnumParameterVo();
										BeanUtils.copyProperties(enums,
												teParameter);
										evList1.add(teParameter);
									}
								}
								if (evList != null) {
									tprojectVo.setEvList(evList1);
								}
							}
							tpGroupVo.getTprojectList().add(tprojectVo);
						}
						com.inspect.vo.basis.ComparatorProject comparator = new com.inspect.vo.basis.ComparatorProject();
						Collections.sort(tpGroupVo.getTprojectList(),
								comparator);
		
					}
					tpGroupList1.add(tpGroupVo);
			}
		}	
		
			
			
	}catch (Exception e) {
		// TODO: handle exception
		e.printStackTrace();
	}		
	map.put("tpGroupList1", tpGroupList1);
	writeJson(map);
	}

	/**
	 * 删除掉空的任务，包括当前月
	 * 
	 * @param taskGroup
	 */
	public void remove0task(List<TermPlanVo> taskGroup){
		if(taskGroup == null || taskGroup.size() == 0){
			taskGroup = null;
			return;
		}
		
		for(int i=taskGroup.size()-1 ;i >= 0;i--){
			TermPlanVo task = taskGroup.get(i);
			if(task.getTpointVoList() == null || task.getTpointVoList().size()== 0){
				taskGroup.remove(task);
				continue;
			}
			
			for(int j=task.getTpointVoList().size()-1;j>=0;j--){
				TermPointVo point = task.getTpointVoList().get(j);
				if(point.getEquipmentList() == null || point.getEquipmentList().size() == 0){
					task.getTpointVoList().remove(point);
				}
			}
			//原来有值的task可能已经被过滤为空
			if(task.getTpointVoList().size() ==0){
				taskGroup.remove(task);
			}
		}
		
		if(taskGroup.size() == 0){
			taskGroup = null;
		}
		
	}
	/**
	 * 
	 * @param taskGroup
	 * @param taskid
	 * @return
	 */
	public TermPlanVo gettask(List<TermPlanVo> taskGroup, int taskid) {

		for (int i = 0; i < taskGroup.size(); i++) {
			if (taskGroup.get(i).getItaskid() == taskid) {
				return taskGroup.get(i);
			}
		}
		return null;
	}

	public TermPointVo getpoint(TermPlanVo task, int pointid) {

		for (int i = 0; i < task.getTpointVoList().size(); i++) {
			if (task.getTpointVoList().get(i).getPointid() == pointid) {
				return task.getTpointVoList().get(i);
			}
		}
		return null;
	}

	public Boolean inpoint(TermPointVo point, int equipmentid) {
		for (int i = 0; i < point.getEquipmentList().size(); i++) {
			if (point.getEquipmentList().get(i).getId() == equipmentid) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 只是比较是否与当前月相同，如不同，认为是过去的任务
	 * 
	 * @param pstartdate
	 * @return
	 */
	public Boolean islastmonth(String pstartdate) {

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
		Date now = new Date();
		String strnow = (simpleDateFormat).format(now);

		if (strnow.compareTo(pstartdate) == 0) {
			return false;
		}

		return true;
	}

	/**
	 * 获取本季度中本月以及之前的月份
	 * 
	 * @param pgid
	 * @return
	 */
	public String lastMonth(String pgid) {
		StringBuffer buf = new StringBuffer("where  pt.pgid='" + pgid + "'");
		java.util.Calendar c = Calendar.getInstance();// 今天的时间
		int month = c.get(Calendar.MONTH) + 1; // 获取月份，0表示1月份
		int yu = month % 3;
	//	System.out.println("取余：" + yu);
		//System.out.println("当月=" + month);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
		java.util.Date date0 = c.getTime();
		String date1 = (simpleDateFormat).format(date0);
		c.add(Calendar.MONTH, -1);// 今天的时间月份-1支持1月的上月
		java.util.Date date = c.getTime();
		String date2 = (simpleDateFormat).format(date);
	//	System.out.println("上月=" + date2);
		c.add(Calendar.MONTH, -1);// 今天的时间月份-1支持1月的上月
		date = c.getTime();
		String date3 = (simpleDateFormat).format(date);
		//System.out.println("上上月=" + date3);

		// buf.append(" and  pt.pstartdate='"+date1+"'");
		if (yu == 0) {
			buf.append(" and ( pt.pstartdate='" + date1
					+ "' or pt.pstartdate='" + date2 + "' or pt.pstartdate='"
					+ date3 + "')");
		} else if (yu == 2) {
			buf.append(" and ( pt.pstartdate='" + date1
					+ "' or pt.pstartdate='" + date2 + "')");
		} else { // yu == 1: 不做处理，按季度进行查询，上季度的月份不列入清单
			buf.append(" and  pt.pstartdate='" + date1 + "'");
		}

		return buf.toString();
	}

	/**
	 * 巡检任务查询 参数：用户编号 成功：返回任务集合 失败：返回1和用户编号
	 * 
	 */
	public void interfindTask_liao() {

		Map<String, Object> map = new HashMap<String, Object>();
		String pgid = getRequest().getParameter("pgid");// 巡检项组id
		String termid = getRequest().getParameter("termid");
		String imark = getRequest().getParameter("imark");
		String lastmonth = getRequest().getParameter("lastmonth");// 上月
		boolean lastmonthflag = StringUtils.isNotEmpty(lastmonth);// 看是否是查询上月的，若是上月，则为true,本月为false
		if (StringUtils.isEmpty(imark) || StringUtils.isEmpty(termid)
				|| StringUtils.isEmpty(pgid)) {
			map.put("reason", "标志码、终端编号和班组编号不能为空");
			map.put("result", 1);
			writeJson(map);
			return;
		}
		// 检测是否过期和验证码是否匹配
		TTerminateStatusReport term = inspectUserService
				.geTerminateStatusReport(termid);
		TermStatus tStatus = new TermStatus();
		tStatus = httpService.judgeValid(term, imark);
		if (tStatus.getFlag() != 0) {
			map.put("reason", tStatus.getReason());
			map.put("result", tStatus.getFlag());
			writeJson(map);
			return;
		}
		TermPlanVo planVo = new TermPlanVo();
		// 获取任务
		List<TPlanTask> ptasklist = null;
		String sql = DateUtils.getLastMonth(pgid, lastmonthflag);

		ptasklist = inspectItemService.getPlanTask(sql);

		List<TermProjectGroupVo> tpGroupList1 = new ArrayList<TermProjectGroupVo>();
		Set<Integer> pgroudIdSet = new TreeSet();
		List<TermPlanVo> planVoList = new ArrayList<TermPlanVo>();
		try {
			if (ptasklist != null && ptasklist.size() > 0) {
				// 查询巡检计划
				for (TPlanTask planTask : ptasklist) {
					TermPlanVo planVo2 = new TermPlanVo();
					planVo2.setPstartdate(planTask.getPstartdate());
					int lineId1 = planTask.getPlineid();
					String lineId = String.valueOf(lineId1);
					// 获取任务计划中的线路实体
					if (lineId != null) {
						TLine line = inspectItemService.getLine(lineId);
						// 将line复制到lineVo中
						if (line != null) {
							// 获取线路中的巡检点
							List<TLinePoint> linepoints = line.getLinepoints();
							planVo2.setId(line.getId());
							planVo2.setItaskid(planTask.getId());
							planVo2.setEntid(line.getEntid());
							planVo2.setLname(line.getLname());
							if (linepoints != null && linepoints.size() != 0) {
								for (TLinePoint tlinePoint : linepoints) {
									// List<TermPointVo> tpointVoList=new
									// ArrayList<TermPointVo>();
									TPoint point = tlinePoint.getTpoint();
									// 给巡检点Vo对象赋值
									TermPointVo tpointVo = new TermPointVo();
									tpointVo.setPointid(point.getId());
									// 获取巡检点下面的设备
									Set<TPointEquipment> pointequipments = point
											.getPointequipments();
									if (pointequipments != null
											&& pointequipments.size() != 0) {
										for (TPointEquipment pointEquipment : pointequipments) {

											TEquipment equipment = pointEquipment
													.getTequipment();
											int baseid = 0;
											if (equipment != null) {

												Set<TBaseInfoEquipment> eSet = equipment
														.getBaseinfoequipments();

												Iterator<TBaseInfoEquipment> beset = eSet
														.iterator();
												if (beset.hasNext()) {
													TBaseInfoEquipment be = (TBaseInfoEquipment) beset
															.next();
													// 将t_base_info装进来
													baseid = be.getTbaseinfo()
															.getId();
													// equipment.setId(equipid);
												}

												TermEquipmentVo tequipmentVo = new TermEquipmentVo();
												tequipmentVo.setId(baseid);
												// List
												// list=inspectMonitorService.isEquipInspectStatus(planTask.getId(),
												// equipment.getId());
												List list = inspectMonitorService
														.isEquipInspectStatus(
																planTask
																		.getId(),
																baseid);
												boolean flag = false;
												if (list != null
														&& list.size() > 0) {
													flag = true;
													TInspectItemReport itemReport = (TInspectItemReport) list
															.get(list.size() - 1);
													// 状态0表示巡检审核正常
													// 1表示异常，即审核不合格，需要重新巡检
													tequipmentVo
															.setXstatus(itemReport
																	.getXstatus());
													if (itemReport
															.getLastmonthflag() == 1) {
														continue;
													}
												}
												// 设备是否被巡检过，即设备表在message表中是否存在记录，而不需要管巡检项是否全部完成//false表示未巡检，true表示已巡检
												tequipmentVo
														.setInspectStatus(flag);
												// 将equipment复制到equipmentVo中
												BeanUtils.copyProperties(
														equipment,
														tequipmentVo,
														new String[] { "id",
																"xstatus" });
												if (tequipmentVo.getEtype() == null) {
													tequipmentVo.setEtype("");
												}
												String pgroupid = "";
												// 获取设备下面的巡检项组
												Set<TEquipmentProjectGroup> epGroupList = equipment
														.getEquipmentprojectgroups();
												// 将设备下的巡检项组id保存起来
												if (epGroupList != null
														&& epGroupList.size() != 0) {
													int i = 0;
													for (TEquipmentProjectGroup epGroup : epGroupList) {
														i++;
														TProjectGroup pGroup = epGroup
																.getTprojectgroup();
														if (i < epGroupList
																.size()) {
															pgroupid += pGroup
																	.getId()
																	+ ",";
														} else {
															pgroupid += pGroup
																	.getId();
														}
														pgroudIdSet.add(pGroup
																.getId());
													}
												}
												tequipmentVo
														.setTpgroupids(pgroupid);
												// 添加设备信息
												// equipmentList.add(equipmentVo);
												tpointVo.getEquipmentList()
														.add(tequipmentVo);
											}
										}
									}
									planVo2.getTpointVoList().add(tpointVo);
								}
							}
						}
					}
					planVoList.add(planVo2);
				}
				// 获取任务下面的设备信息
				Iterator<Integer> it2 = pgroudIdSet.iterator();
				while (it2.hasNext()) {
					int id = (Integer) it2.next();
					TProjectGroup tpGroup = new TProjectGroup();
					TermProjectGroupVo tpGroupVo = new TermProjectGroupVo();
					tpGroup = inspectItemService.getProjectGroup(id);
					BeanUtils.copyProperties(tpGroup, tpGroupVo);

					if (tpGroup.getTprojects() != null
							&& tpGroup.getTprojects().size() > 0) {
						for (TProject project : tpGroup.getTprojects()) {
							TermProjectVo tprojectVo = new TermProjectVo();
							BeanUtils.copyProperties(project, tprojectVo);
							// 如果巡检项类型为1，则表示为枚举值，那必须获取枚举值对应的内容
							if (tprojectVo.getPtype().equals("1")) {
								List<TEnumParameter> evList = null;
								List<TermEnumParameterVo> evList1 = new ArrayList<TermEnumParameterVo>();
								evList = inspectItemService
										.getEnumParameListByParameName(" 1=1 ",
												tprojectVo.getPenumvalue());
								if (evList != null) {
									for (TEnumParameter enums : evList) {
										TermEnumParameterVo teParameter = new TermEnumParameterVo();
										BeanUtils.copyProperties(enums,
												teParameter);
										evList1.add(teParameter);
									}
								}
								if (evList != null) {
									tprojectVo.setEvList(evList1);
								}
							}
							tpGroupVo.getTprojectList().add(tprojectVo);
						}
						com.inspect.vo.basis.ComparatorProject comparator = new com.inspect.vo.basis.ComparatorProject();
						Collections.sort(tpGroupVo.getTprojectList(),comparator);

					}
					tpGroupList1.add(tpGroupVo);
				}
			} else {
				map.put("reason", "本月没有任务");
				map.put("result", 1);
				writeJson(map);
				return;
			}

		} catch (Exception e) {
			map.put("reason", "数据异常，请咨寻服务器");
			map.put("result", 1);
			writeJson(map);
			e.printStackTrace();
			return;
		}
		map.put("result", 0);
		map.put("tpGroupList1", tpGroupList1);
		map.put("planVoList", planVoList);
		writeJson(map);
	}

	/**
	 * 终端巡检数据提交 参数：用户编号(userNo)，时间（time），经度（longitude），纬度（latitude）,
	 * 终端编号(terminalNo)，巡检项名称（inspectionitemName），
	 * 巡检项类型（inspectionItemType），巡检描述
	 * (inspectionDescribe)，状态(status)，异常状态(exceptionstatus),
	 * 成功：结果（result），用户名称(username)，用户编号(userno)， 终端编号(terminalno) 失败：返回1和用户编号
	 * 
	 */
	public void interterminalData() {

		String json = getRequest().getParameter("json");

		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> classMap = new HashMap<String, Object>();
		classMap.put("inspectreportdetailmsgs",
				InspectItemDetailReportVo1.class);
		String date = DateUtils.getFormatSecondDate();
		int psize = 0;// 设备下的巡检项总数
		int baseid = 0;// 设备主键id
		int taskid = 0;// 任务id
		try {
			TermInspectItemReportVo1 diyBean = (TermInspectItemReportVo1) JSONObject
					.toBean(JSONObject.fromObject(json),
							TermInspectItemReportVo1.class, classMap);
			TInspectItemReport equipReport1 = new TInspectItemReport();
			// 获取设备下的巡检项信息
			Set<InspectItemDetailReportVo1> detailList = diyBean
					.getInspectreportdetailmsgs();
			Iterator<InspectItemDetailReportVo1> iter = detailList.iterator();
			List<TInspectItemDetailReport> detailList1 = new ArrayList<TInspectItemDetailReport>();
			int index = 0;
			TInspectItemReport equip = new TInspectItemReport();
			String retrans;
			String imark;
			String termid;
			while (iter.hasNext()) {
				index++;
				InspectItemDetailReportVo1 euqipDetail = iter.next();
				psize = euqipDetail.getPsize(); //设备下巡检项总数
				baseid = euqipDetail.getXequid();
				taskid = euqipDetail.getXtaskid();
				// 只在传过来的集合中保存一次设备信息
				if (index == 1) {
					retrans = euqipDetail.getRetrains();  ////重传标志
					imark = euqipDetail.getImark();
					termid = euqipDetail.getXterid();

					// 校验是否重传和是否超过周期
					if (StringUtils.isEmpty(retrans)
							|| StringUtils.isEmpty(imark)
							|| StringUtils.isEmpty(termid)) {
						map.put("reason", "重传标志、标志码和终端标号不能为空");
						map.put("result", 1);
						writeJson(map);
						return;
					}
					// 无需检测是否过期和验证码是否匹配
					if ("1".equals(retrans)) {
					}
					// 检测是否过期和验证码是否匹配
					else {
						TTerminateStatusReport term = inspectUserService
								.geTerminateStatusReport(termid);
						TermStatus tStatus = new TermStatus();
						tStatus = httpService.judgeValid(term, imark);
						if (tStatus.getFlag() != 0) {
							map.put("reason", tStatus.getReason());
							map.put("result", tStatus.getFlag());
							writeJson(map);
							return;
						}
					}
					boolean flag = false;
					BeanUtils.copyProperties(euqipDetail, equip);
					List list = inspectMonitorService.isEquipInspectStatus(
							euqipDetail.getXtaskid(), euqipDetail.getXequid());
					if (list != null && list.size() > 0) {
						flag = true;
					}
					// 若提交过，则修改设备记录
					if (flag == true) {
						// 将设备记录信息修改掉
						equipReport1 = inspectMonitorService
								.getEquipInspectStatus(
										euqipDetail.getXtaskid(), euqipDetail
												.getXequid());
						// 提交数据接口不上传图片。故复制对象时将ximgurl和ximgname忽略掉
						// 只要不改变主键id，当调用save（）时，则属于修改，若改变了主键id，则属于新增一条数据了
						BeanUtils.copyProperties(equip, equipReport1,
								new String[] { "id", "ximgurl", "ximgname",
										"entid" });
						equipReport1.setXreptime(date);
						equipReport1.setXstatus("0");
					}
					// 若没提交，则保存设备记录
					else {
						BeanUtils.copyProperties(equip, equipReport1,
								new String[] { "ximgurl", "ximgname" });
						equipReport1.setXstatus("0");
						equipReport1.setXreptime(date);
					}
				}
				TInspectItemDetailReport detail = new TInspectItemDetailReport();
				BeanUtils.copyProperties(euqipDetail, detail, new String[] {
						"ximgurl", "ximgname" });
				detail.setTinspectitemreport(equipReport1);
				detail.setXreptime(date);
				detailList1.add(detail);
			}
			inspectMonitorService.editEntity(equipReport1, detailList1);
			inspectMonitorService.isComplete(psize, baseid, taskid);
			map.put("result", 0);
			writeJson(map);
		} catch (Exception e) {
			map.put("reason", "输入的数据有问题");
			map.put("result", 1);
			writeJson(map);
			e.printStackTrace();
			return;

		}

	}

	/**
	 * 上传图片
	 */
	public void interterminalPicture() {
		String imark = getRequest().getParameter("imark");
		String termid = getRequest().getParameter("xterid");
		Map<String, Object> map = new HashMap<String, Object>();
		if (StringUtils.isEmpty(imark) || StringUtils.isEmpty(termid)) {
			map.put("reason", "重传状态和终端id不能为空");
			map.put("result", 1);
			writeJson(map);
			return;
		}

		// 检测是否过期和验证码是否匹配
		TermStatus tStatus = new TermStatus();
		TTerminateStatusReport term = inspectUserService
				.geTerminateStatusReport(termid);
		tStatus = httpService.judgeValid(term, imark);
		if (tStatus.getFlag() != 0) {
			map.put("reason", tStatus.getReason());
			map.put("result", tStatus.getFlag());
			writeJson(map);
			return;
		}
		// 设备上传图片
		String xtaskid = StringUtils.notNull(getRequest().getParameter(
				"xtaskid"));
		String xlid = StringUtils.notNull(getRequest().getParameter("xlid"));
		String xpid = StringUtils.notNull(getRequest().getParameter("pointid"));
		String xequid = StringUtils
				.notNull(getRequest().getParameter("xequid"));
		String xgid = StringUtils.notNull(getRequest().getParameter("xgid"));
		String xuid = StringUtils.notNull(getRequest().getParameter("xuid"));
		String xequtnum = StringUtils.notNull(getRequest().getParameter(
				"xequtnum"));
		String xterid = termid;// 终端编号
		String xequtype = StringUtils.notNull(getRequest().getParameter(
				"xequtype"));
		String entid = StringUtils.notNull(getRequest().getParameter("entid"));
		String ecity = StringUtils.notNull(getRequest().getParameter("ecity"));// 城市
		String eregion = StringUtils.notNull(getRequest().getParameter(
				"eregion"));// 区县
		int xequid1 = 0;
		int xtaskid1 = 0;
		int entid1 = 0;
		if (StringUtils.isNotEmpty(xlid) && StringUtils.isNotEmpty(xgid)
				&& StringUtils.isNotEmpty(xuid)
				&& StringUtils.isNotEmpty(xequid)
				&& StringUtils.isNotEmpty(xtaskid)
				&& StringUtils.isNotEmpty(entid)
				&& StringUtils.isNotEmpty(xterid)) {
			xequid1 = Integer.parseInt(xequid);
			xtaskid1 = Integer.parseInt(xtaskid);
			entid1 = Integer.parseInt(entid);
		} else {
			map.put("reason", "请填写线路、巡检班组、人员、任务、设备、代维商和终端编号");
			map.put("result", 1);
			writeJson(map);
			return;
		}
		String fileName = uploadFileFileName;
		String suffix = fileName.substring(fileName.lastIndexOf("."));
		String date2 = DateUtils.getFormatMinSecondDate();
		String newname = date2 + suffix;
		fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
		String fielPath = Constant.IMG_PATH;

		if (uploadFileFileName == null) {
			map.put("reason", "请上传图片");
			map.put("result", 1);
			writeJson(map);
			return;
		}

		File dir = new File(fielPath);
		File f = null;
		try {
			if (!dir.exists()) {
				// 生成目录
				dir.mkdirs();
			}
			// 删除服务器已存在的文件
			f = new File(fielPath, uploadFileFileName);

			File mm = new File(fielPath, newname);
			f.renameTo(mm);
			if (f.exists()) {
				newname = date2 + "_1" + suffix;
				mm = new File(fielPath, newname);
			}
			f.renameTo(mm);
			BufferedOutputStream bos = null;
			BufferedInputStream bis = null;
			FileInputStream fis;
			FileOutputStream fos = null;
			try {
				fis = new FileInputStream(uploadFile);
				bis = new BufferedInputStream(fis);
				fos = new FileOutputStream(new File(dir, newname));
				bos = new BufferedOutputStream(fos);
			} catch (FileNotFoundException e) {
				map.put("result", 1);
				writeJson(map);
				e.printStackTrace();
				return;
			}

			byte[] buf = new byte[4096];
			int len = -1;
			try {
				while ((len = bis.read(buf)) != -1) {
					bos.write(buf, 0, len);
					bos.flush();
				}
			} catch (IOException e) {
				map.put("reason", "图片有问题，请检查是否过大");
				map.put("result", 1);
				writeJson(map);
				e.printStackTrace();
				return;
			} finally {
				bis.close();
				fis.close();
				bos.close();
				fos.close();
			}
			String date = DateUtils.getFormatSecondDate();
			boolean flag1 = false;
			// 判断设备有没有提交过
			List list = inspectMonitorService.isEquipInspectStatus(xtaskid1,
					xequid1);
			if (list != null && list.size() > 0) {
				flag1 = true;
			}
			TInspectItemReport equipReport = new TInspectItemReport();
			// 若提交过，则修改图片
			if (flag1 == true) {
				TInspectItemReport equipReport1 = null;
				// 将设备记录信息修改掉
				equipReport1 = inspectMonitorService.getEquipInspectStatus(
						xtaskid1, xequid1);
				// 如果有图片，则合并图片的名称
				if (equipReport1.getXimgname() != null
						&& !equipReport1.getXimgname().trim().equals("")) {
					String oldName = equipReport1.getXimgname();
					equipReport1.setXimgname(oldName + "," + newname);
				}
				// 如果没有图片，则增加进来
				else {
					equipReport1.setXimgname(newname);
				}
				int hasPicNum = equipReport1.getXimgname().split(",").length;

				equipReport1.setXreptime(date);
				equipReport1.setXimgurl(fielPath);
				equipReport1.setXstatus("0");
				boolean flag = false;
				flag = inspectMonitorService.editEntity1(equipReport1);
				if (flag == false) {
					f.delete();
					map.put("result", 1);
					map.put("reason", "上传失败");
					writeJson(map);
					return;
				}
				map.put("hasPicNum", hasPicNum);
				map.put("result", 0);
				writeJson(map);
			}
			// 若没提交，则保存设备记录
			else {
				equipReport.setXreptime(date);
				equipReport.setXtaskid(Integer.parseInt(xtaskid));
				if (StringUtils.isNotEmpty(xlid)) {
					equipReport.setXlid(Integer.parseInt(xlid));
				}
				if (StringUtils.isNotEmpty(xpid)) {
					equipReport.setXpid(Integer.parseInt(xpid));
				}

				equipReport.setXequid(Integer.parseInt(xequid));

				if (StringUtils.isNotEmpty(xgid)) {
					equipReport.setXgid(Integer.parseInt(xgid));
				}
				equipReport.setXuid(Integer.parseInt(xuid));
				if (StringUtils.isNotEmpty(xequtnum)) {
					equipReport.setXequtnum(xequtnum);
				}
				equipReport.setXterid(xterid);
				equipReport.setXimgurl(fielPath);
				equipReport.setEntid(Integer.parseInt(entid));
				equipReport.setEcity(ecity);
				equipReport.setEregion(eregion);
				equipReport.setXequtype(xequtype);
				equipReport.setXimgname(newname);
				equipReport.setXreptime(date);
				equipReport.setXstatus("0");
				boolean flag = false;
				flag = inspectMonitorService.editEntity1(equipReport);
				if (flag == false) {
					f.delete();
					map.put("result", 1);
					map.put("reason", "上传失败");
					writeJson(map);
					return;
				}
				map.put("hasPicNum", 1);
				map.put("result", 0);
				writeJson(map);
			}
		} catch (Exception e) {
			map.put("result", 1);
			map.put("reason", "上传图片失败");
			writeJson(map);
			e.printStackTrace();
			return;
		}
	}

	/**
	 * 紧急任务下发
	 */
	public void interterminalgetTask() {
		Map<String, Object> map = new HashMap<String, Object>();
		String termid = getRequest().getParameter("termid");
		String imark = getRequest().getParameter("imark");
		if (StringUtils.isEmpty(imark) || StringUtils.isEmpty(termid)) {
			map.put("reason", "重传状态和终端id不能为空");
			map.put("result", 1);
			writeJson(map);
			return;
		}
		try {
			// System.out.println("11");

			// 检测是否过期和验证码是否匹配
			TermStatus tStatus = new TermStatus();
			TTerminateStatusReport term = inspectUserService
					.geTerminateStatusReport(termid);
			tStatus = httpService.judgeValid(term, imark);
			if (tStatus.getFlag() != 0) {
				map.put("reason", tStatus.getReason());
				map.put("result", tStatus.getFlag());
				writeJson(map);
				return;
			}
			// System.out.println("12");
			// 通过终端id查询是否有突发任务
			TRepairTask repair = problemService.getRepairTask(termid);
			if (repair != null) {
				// System.out.println("13");
				RepairTaskVo repairTaskVo = new RepairTaskVo();
				BeanUtils.copyProperties(repair, repairTaskVo);
				repairTaskVo.setRflag(1);
				problemService.editRepairTask(repairTaskVo);
				map.put("rcategory", repair.getRcategory());
				map.put("rendtime", repair.getRendtime());
				map.put("content", repair.getRcontent());
				map.put("repairid", repair.getId());
				map.put("result", 0);
				writeJson(map);
				return;
			} else {
				// System.out.println("14");
				map.put("result", 1);
				writeJson(map);
				return;
			}
		} catch (Exception e) {
			// TODO: handle exception
			map.put("reason", "数据异常");
			map.put("result", 1);
			writeJson(map);
			return;
		}

	}

	public void interterminalgetTaskReply() {
		Map<String, Object> map = new HashMap<String, Object>();
		String repairid = getRequest().getParameter("repairid");

		if (StringUtils.isEmpty(repairid)) {
			map.put("reason", "紧急任务编号不能为空");
			map.put("result", 1);
			writeJson(map);
			return;
		}

		try {
			TRepairTask repair = problemService.getRepairTask(Integer
					.parseInt(repairid));
			repair.setRflag(2);
			RepairTaskVo repairTaskVo = new RepairTaskVo();
			BeanUtils.copyProperties(repair, repairTaskVo);
			problemService.editRepairTask(repairTaskVo);
			map.put("result", 0);
			writeJson(map);
		} catch (Exception e) {
			map.put("reason", "确认失败");
			map.put("result", 1);
			writeJson(map);
			return;
		}
	}

	/**
	 * 终端GPS数据上报 参数：用户编号(userNo)，时间（time），经度（longitude），纬度（latitude） 成功：返回0，用户编号
	 * 失败：返回1和用户编号
	 */
	public void interterminalGPS() {
		Map<String, Object> map = new HashMap<String, Object>();
		boolean flag=this.compare_date();
		if(flag==true){
		try {
			
			String rpuserid = getRequest().getParameter("rpuserid");
			String rpterminalid = getRequest().getParameter("rpterminalid");
			String rpposx = getRequest().getParameter("rpposx");
			String rpposy = getRequest().getParameter("rpposy");
			String rplineid = getRequest().getParameter("itaskid");// rplineid
			String rpgroupid = getRequest().getParameter("rpgroupid");
			String entid = getRequest().getParameter("entid");
			TUserLocateReport locate = new TUserLocateReport();
			if (StringUtils.isEmpty(entid) || StringUtils.isEmpty(rpuserid)
					|| StringUtils.isEmpty(rplineid)
					|| StringUtils.isEmpty(rpterminalid)
					|| StringUtils.isEmpty(rpposx)
					|| StringUtils.isEmpty(rpgroupid)) {
				map.put("reason", "分公司、用户编号、线路编号、终端编号、经纬度和班组编号不能为空");
				map.put("result", 1);
				writeJson(map);
				return;
			}
			if(Double.parseDouble(rpposy)==0){
				map.put("reason", "GPS无法获取数据，请稍候再试");
				map.put("result", 1);
				writeJson(map);
				return;
			
			}
			locate.setRpuserid(Integer.parseInt(rpuserid));
			locate.setRpterminalid(rpterminalid);
			locate.setRpposx(Double.parseDouble(rpposy));
			locate.setRpposy(Double.parseDouble(rpposx));
			locate.setRplineid(Integer.parseInt(rplineid));
			locate.setRpgroupid(Integer.parseInt(rpgroupid));
			String date = DateUtils.getFormatSecondDate();
			locate.setRptime(date);
			locate.setEntid(Integer.parseInt(entid));
			inspectMonitorService.addTUserLocateReport(locate);
		} catch (Exception e) {
			map.put("reason", "数据异常，请稍后处理");
			map.put("result", 1);
			writeJson(map);
			return;
		}
		}
		else{
			map.put("reason", "不在时间内数据不保存");
			map.put("result", 1);
			writeJson(map);
			return;
		}
		map.put("result", 0);
		writeJson(map);
	}

	/**
	 * 突发任务提交
	 */
	public void interterminalRepairTask() {
		Map<String, Object> map = new HashMap<String, Object>();
		String retrans = getRequest().getParameter("retrains");// 0表示正常上传，1表示重传
		String repairid = getRequest().getParameter("repairid");
		String termid = getRequest().getParameter("rternumber");
		String imark = getRequest().getParameter("imark");
		String rdesc = getRequest().getParameter("rdesc");
		String ruid = getRequest().getParameter("ruid");

		if (StringUtils.isEmpty(imark) || StringUtils.isEmpty(termid)
				|| StringUtils.isEmpty(retrans)
				|| StringUtils.isEmpty(repairid)) {
			map.put("reason", "重传状态、标志码和、抢修编号终端编号不能为空");
			map.put("result", 1);
			writeJson(map);
			return;
		}

		try {
			// 无需检测是否过期和验证码是否匹配
			if ("1".equals(retrans)) {
			}
			// 检测是否过期和验证码是否匹配
			else {
				TermStatus tStatus = new TermStatus();
				TTerminateStatusReport term = inspectUserService
						.geTerminateStatusReport(termid);
				tStatus = httpService.judgeValid(term, imark);
				if (tStatus.getFlag() != 0) {
					map.put("reason", tStatus.getReason());
					map.put("result", tStatus.getFlag());
					writeJson(map);
					return;
				}
			}

			// rdesc=new String(rdesc.getBytes("ISO-8859-1"),"utf-8");
			// TInspectUser user=inspectUserService.getInspectUser(ruid);

			TRepairTask repair = problemService.getRepairTask(Integer
					.parseInt(repairid));
			/*
			 * if(user!=null){
			 * repair.setRgid(user.getTgroup()==null?0:user.getTgroup
			 * ().getId()); }
			 */
			repair.setRdesc(rdesc);
			repair.setRflag(3);
			repair.setRuid(Integer.parseInt(ruid));
			String date = DateUtils.gettimeOnlyOwnByGPS();
			repair.setRrepdate(date);
			RepairTaskVo repairTaskVo = new RepairTaskVo();
			BeanUtils.copyProperties(repair, repairTaskVo);
			problemService.editRepairTask(repairTaskVo);
			map.put("result", 0);
			writeJson(map);
		} catch (Exception e) {
			map.put("reason", "提交失败");
			map.put("result", 1);
			writeJson(map);
			return;
		}
	}

	/**
	 * 终端登录密码修改 参数：用户编号(userNo)，新密码 成功：返回0，用户编号 失败：返回1和用户编号 注意：此处传递的是userId
	 */
	//1是后添加为测试station
	public void intereditTerminalPassword1() {
		System.out.println("intereditTerminalPassword1()");
		String imark = getRequest().getParameter("imark");
		String termid = getRequest().getParameter("termid");
		// System.out.println("333333333333333333333333333333333333333333333333");
		Map<String, Object> map = new HashMap<String, Object>();
		if (StringUtils.isEmpty(imark) || StringUtils.isEmpty(termid)) {
			map.put("reason", "标志码和终端id不能为空");
			map.put("result", 1);
			writeJson(map);
			return;
		}
		// 检测是否过期和验证码是否匹配
		TTerminateStatusReport term = inspectUserService
				.geTerminateStatusReport(termid);
		TermStatus tStatus = new TermStatus();
		tStatus = httpService.judgeValid(term, imark);
		if (tStatus.getFlag() != 0) {
			map.put("reason", tStatus.getReason());
			map.put("result", tStatus.getFlag());
			writeJson(map);
			return;
		}
		String puid = getRequest().getParameter("puid");
		String iupwd = getRequest().getParameter("iupwd");
		try {
			if (StringUtils.isNotEmpty(puid) && StringUtils.isNotEmpty(iupwd)) {
				TInspectUser inspectUser = inspectUserService
						.getInspectUser(puid);
				inspectUser.setIupwd(iupwd);
				inspectUserService.editInspectUserPwd(inspectUser);
				map.put("result", 0);
				writeJson(map);
			} else {
				map.put("reason", "用户编号和密码不能为空");
				map.put("result", 1);
				writeJson(map);
				return;
			}
		} catch (Exception e) {
			map.put("reason", "修改异常");
			map.put("result", 1);
			writeJson(map);
			return;
		}
	}

	public String intertest() {
		return "interetest";
	}

	/***
	 * 巡检问题上报
	 */
	public void interProblem() {
		String retrans = getRequest().getParameter("retrains");// 0表示正常上传，1表示重传
		String imark = getRequest().getParameter("imark");
		String entid = getRequest().getParameter("entid");// 分公司编号
		String iuserid = getRequest().getParameter("iuserid");// 问题提交人
		String itaskid = getRequest().getParameter("itaskid");// 任务id
		String protype = getRequest().getParameter("protype");// 问题类型
		String prodesc = getRequest().getParameter("prodesc");// 问题描述
		String procycle = getRequest().getParameter("procycle");// 巡检周期
		String prosite = getRequest().getParameter("prosite");// 巡检站点（设备表equipment的主键id）
		String ternumber = getRequest().getParameter("ternumber");// 终端编号
		Map<String, Object> map = new HashMap<String, Object>();
		if (StringUtils.isEmpty(imark) || StringUtils.isEmpty(retrans)
				|| StringUtils.isEmpty(entid) || StringUtils.isEmpty(iuserid)
				|| StringUtils.isEmpty(itaskid) || StringUtils.isEmpty(prodesc)
				|| StringUtils.isEmpty(procycle)
				|| StringUtils.isEmpty(prosite)
				|| StringUtils.isEmpty(ternumber)) {
			map.put("reason",
					"重传状态、标志码和终端编号、分公司编号、提交人、任务编号、问题描述、巡检周期、巡检站点和终端编号不能为空");
			map.put("result", 1);
			writeJson(map);
			return;
		}
		try {
			// 无需检测是否过期和验证码是否匹配
			if ("1".equals(retrans)) {
			}
			// 检测是否过期和验证码是否匹配
			else {
				TTerminateStatusReport term = inspectUserService
						.geTerminateStatusReport(ternumber);
				TermStatus tStatus = new TermStatus();
				tStatus = httpService.judgeValid(term, imark);
				if (tStatus.getFlag() != 0) {
					map.put("reason", tStatus.getReason());
					map.put("result", tStatus.getFlag());
					writeJson(map);
					return;
				}
			}
			ProblemVo problemvo = new ProblemVo();
			if (StringUtils.isNotEmpty(entid)) {
				problemvo.setEntid(Integer.parseInt(entid));
			}
			if (StringUtils.isNotEmpty(iuserid)) {
				TInspectUser u = this.inspectUserService
						.getInspectUser(iuserid);
				problemvo.setIuserid(u.getTgroup().getId());// 保存的是组id
			}
			if (StringUtils.isNotEmpty(itaskid)) {
				problemvo.setProitaskid(Integer.parseInt(itaskid));
			}
			problemvo.setProtype(protype);
			problemvo.setProdesc(prodesc);
			problemvo.setProcycle(procycle);
			problemvo.setProsite(prosite);
			problemvo.setTernumber(ternumber);
			problemService.addProblem(problemvo);
			map.put("result", 0);
			writeJson(map);
		} catch (Exception e) {
			map.put("reason", "请输入合适的值");
			map.put("result", 1);
			writeJson(map);
			e.printStackTrace();
			return;
		}
	}

	/***
	 * 检查是否为最新版本
	 */
	public void interVersion() {
		Map<String, Object> map = new HashMap<String, Object>();
		TVersion ver = versionService.getVersion();
		if (ver == null) {
			map.put("result", 0);
			writeJson(map);
		} else {
			map.put("vupdate", ver.getVupdate());
			map.put("vaddress", ver.getVaddress());
			map.put("vnum", ver.getVnum());
			map.put("vdesc", ver.getVdesc());
			map.put("result", 1);
			writeJson(map);
		}
	}

	/***
	 * 检查是否为最新版本
	 */
	public void interRevertVersion() {
		Map<String, Object> map = new HashMap<String, Object>();
		TVersion ver = versionService.getRevertVersion();
		if (ver == null) {
			map.put("result", 0);
			writeJson(map);
		} else {
			map.put("vupdate", ver.getVupdate());
			map.put("vaddress", ver.getVaddress());
			map.put("vnum", ver.getVnum());
			map.put("vdesc", ver.getVdesc());
			map.put("result", 1);
			writeJson(map);
		}
	}
	public static boolean compare_date() {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd ");
		String bf = df.format(new Date());
		String bf1;
		bf1 = bf + " 20:00:00";
		Date date = new Date();// 取时间
//		Calendar calendar = new GregorianCalendar();
//		calendar.setTime(date);
//		calendar.add(calendar.DATE, 1);// 把日期往后增加一天.整数往后推,负数往前移动
//		date = calendar.getTime(); // 这个时间就是日期往后推一天的结果
//		String bf2 = df.format(date) + " 05:00:00";
		String bf2 = bf+ " 05:00:00";
		//System.out.println("bf1=" + bf1 + "\tbf2=" + bf2);
		boolean flag=false;
		try {
			DateFormat form = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Date dt1 = form.parse(bf1);
			Date dt2;

			dt2 = form.parse(bf2);

			Date dt = form.parse(form.format(new Date()));

//			System.out.println("dt.getTime()" + dt.getTime() + "\t\t\t\t"+ form.format(new Date()));
//			System.out.println("dt1.getTime()" + dt1.getTime() + "\t\t\t\t"+ bf1);
//			System.out.println("dt2.getTime()" + dt2.getTime() + "\t\t\t\t"+ bf2);
			if (dt.getTime() < dt1.getTime() && dt.getTime() >dt2.getTime()) {
				//System.out.println("当前时间在" + dt1.getTime() + "与"+ dt2.getTime() + "之间");
				flag= true;
			} else {
				System.out.println("时间不允许");
				flag= false;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return flag;
	}
	

	
	//@LogAnnotation(event="添加基站信息",tablename="t_station")
	//public void modifyaddstationinfoweb(){
		public void intereditTerminalPassword2(){	
		
		System.out.println("+++++++++intereditTerminalPassword2()+++++++++++");
		
		//stnumber,stname,stantenna,sttower,stposx,stposy,staddress,stdate,stnet,stfre,stshare,stprocedure,stvalidaty;
		String stnumber1 =  getRequest().getParameter("stnumber");
		String stname1 =  getRequest().getParameter("stname");
		String stantenna1 =  getRequest().getParameter("stantenna");
		String sttower1 =  getRequest().getParameter("sttower");
		String stposx1 =  getRequest().getParameter("stposx");
		String stposy1 =  getRequest().getParameter("stposy");
		String staddress1 =  getRequest().getParameter("staddress");
		String stdate1 =  getRequest().getParameter("stdate");
		String stnet1 =  getRequest().getParameter("stnet");
		String stfre1 =  getRequest().getParameter("stfre");
		String stshare1 =  getRequest().getParameter("stshare");
		String stprocedure1 =  getRequest().getParameter("stprocedure");
		String stvalidaty1 =  getRequest().getParameter("stvalidaty");
	
	    if(!stantenna1.matches("\\d+")){  //不是数字则默认为0
	    	stantenna1="0";
	    }
	    if(!sttower1.matches("\\d+")){
	    	sttower1="0";
	    }
	    if(!stposx1.matches("\\d+")){
	    	stposx1="0";
	    }
	    if(!stposy1.matches("\\d+")){
	    	stposy1="0";
	    }
		Double stantenna11 =  Double.valueOf(stantenna1);
		Double sttower11 =  Double.valueOf(sttower1);
		Double stposx11 =  Double.valueOf(stposx1);
		Double stposy11 =  Double.valueOf(stposy1);
 
		if(stnumber1!=null&&stname1!=null){
			
			StationVo stationvo=new StationVo();
			stationvo.setStname(stname1);
			stationvo.setStnumber(stnumber1);
			stationvo.setStantenna(stantenna11);
			stationvo.setSttower(sttower11);
			stationvo.setStposx(stposx11);
			stationvo.setStposy(stposy11);
			stationvo.setStaddress(staddress1);
			stationvo.setStdate(stdate1);
			stationvo.setStnet(stnet1);
			stationvo.setStfre(stfre1);
			stationvo.setStshare(stshare1);
			stationvo.setStprocedure(stprocedure1);
			stationvo.setStvalidaty(stvalidaty1);
			

			inspectItemService.addStation(stationvo);		 
			
		Map<String,Object>  map =new HashMap<String, Object>();
		map.put("result",0);
	
		writeJson(map);
		}
		else{
			Map<String,Object>  map =new HashMap<String, Object>();
			map.put("result",1);
			writeJson(map);
		}
		
	}
	
	
	
	//@LogAnnotation(event="添加基站信息",tablename="t_station")
		public void intereditTerminalPassword(){
			
		System.out.println("+++++++++intereditTerminalPassword()+++++++++++");
		
		String op =  getRequest().getParameter("operation");
		
		System.out.println("++++++++++++++++++++" + op);
		if(op.equals("list"))
		{
		
		Connection con=null;//声明Connection引用
		Statement stat=null;//声明Statement引用
		ResultSet rs=null;//声明ResultSet引用
			try{
			Class.forName("com.mysql.jdbc.Driver");
			String url="jdbc:mysql://localhost/inspect";
			con = DriverManager.getConnection(url,"root","123");
			}catch(ClassNotFoundException e){
				e.printStackTrace();
				}catch(SQLException e1){
					e1.printStackTrace();
					}
			
			Vector<String[]> v =new Vector<String[]>();//创建返回向量对象
			try{
				 stat = con.createStatement();//创建语句对象
				 String sql = "select id,stnumber,stname,stantenna,sttower,staddress from t_station";
				 rs = stat.executeQuery(sql);
				 while(rs.next()){//遍历结果集得到分组信息		    
				    String group[] = new String[6];
				    for(int i=0;i<group.length;i++){
				      group[i] = rs.getString(i+1)+"";
				      
				      System.out.print(group[i]+" ");
				    }	
				    System.out.println(" ");
					v.add(group);//将信息数组添加到返回的向量里
				}
			}
			catch(Exception e)
			{e.printStackTrace();}
			finally
			{ try {
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			  try {
				stat.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			  try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			  }	
		  System.out.println("***daxiao*******  "+v.size());
 
		  
		Map<String,Object>  map =new HashMap<String, Object>();
		map.put("result",v);
 
		writeJson(map);
 
		}
		 
		else if(op.equals("add"))
		{
			System.out.println("添加基站信息！");
			String stnumber1 =  getRequest().getParameter("stnumber").trim();
			System.out.println("*****stname1******"+stnumber1);
			
			String stname1 =  getRequest().getParameter("stname").trim();
			System.out.println("*****stname1******"+stname1);
			String stantenna1 =  getRequest().getParameter("stantenna").trim();
			String sttower1 =  getRequest().getParameter("sttower").trim();
			String stposx1 =  getRequest().getParameter("stposx").trim();
			String stposy1 =  getRequest().getParameter("stposy").trim();
			System.out.println("*****stposy1******"+stposy1);
			String staddress1 =  getRequest().getParameter("staddress").trim();
			String stdate1 =  getRequest().getParameter("stdate").trim();
			String stnet1 =  getRequest().getParameter("stnet").trim();
			String stfre1 =  getRequest().getParameter("stfre").trim();
			String stshare1 =  getRequest().getParameter("stshare").trim();
			String stprocedure1 =  getRequest().getParameter("stprocedure").trim();
			String stvalidaty1 =  getRequest().getParameter("stvalidaty").trim();
			String stpic =  getRequest().getParameter("stpic").trim();
			System.out.println("*****stpic******"+stpic);
			
			
			Double stposx11,stposy11,stantenna11,sttower11;
			
			if(stposx1==null||stposx1.length()==0){
				System.out.println("*****stposx1为空和长度为0******" );
				  stposx11 = 0.0;
			}else
			{
				  stposx11 =  Double.valueOf(stposx1);
			}
			
			
			if(stposy1==null||stposy1.length()==0){
				System.out.println("*****stposy1为空和长度为0******");
				 stposy11 = 0.0;
			}else
			{
				  stposy11 =  Double.valueOf(stposy1);
			}
			
			
			if(stantenna1==null||stantenna1.length()==0){
				System.out.println("*****stantenna1为空和长度为0******");
				 stantenna11 = 0.0;
			}else
			{
				 stantenna11 =  Double.valueOf(stantenna1);
			}
			
			
			if(sttower1==null||sttower1.length()==0){
				System.out.println("*****sttower1为空和长度为0******");
				 sttower11 = 0.0;
			}else
			{
				 sttower11 =  Double.valueOf(sttower1);
			}
 

			
			if(stnumber1!=null&&stname1!=null){
				
				System.out.println("****进入stnumber1!=null&&stname1!=null*******");
				
				StationVo stationvo=new StationVo();
				stationvo.setStname(stname1);
				stationvo.setStnumber(stnumber1);
				stationvo.setStantenna(stantenna11);
				stationvo.setSttower(sttower11);
				stationvo.setStposx(stposx11);
				stationvo.setStposy(stposy11);
				stationvo.setStaddress(staddress1);
				stationvo.setStdate(stdate1);
				stationvo.setStnet(stnet1);
				stationvo.setStfre(stfre1);
				stationvo.setStshare(stshare1);
				stationvo.setStprocedure(stprocedure1);
				stationvo.setStvalidaty(stvalidaty1);
				stationvo.setStpic(stpic);

				inspectItemService.addStation(stationvo);		 
				System.out.println("添加基站信息成功！");
			Map<String,Object>  map =new HashMap<String, Object>();
			map.put("result",0);
		
			writeJson(map);
			}
			else{
				Map<String,Object>  map =new HashMap<String, Object>();
				map.put("result",1);
				writeJson(map);
			} 	
		}
		else if(op.equals("detail"))
		{
			int id =  Integer.parseInt(getRequest().getParameter("id"));
			
			Connection con=null;//声明Connection引用
			Statement stat=null;//声明Statement引用
			ResultSet rs=null;//声明ResultSet引用
				try{
				Class.forName("com.mysql.jdbc.Driver");
				String url="jdbc:mysql://localhost/inspect";
				con = DriverManager.getConnection(url,"root","123");
				}catch(ClassNotFoundException e){
					e.printStackTrace();
					}catch(SQLException e1){
						e1.printStackTrace();
						}
				
				Vector<String[]> v =new Vector<String[]>();//创建返回向量对象
				try{
					 stat = con.createStatement();//创建语句对象
					 String sql = "select * from t_station where id="+id;
					 rs = stat.executeQuery(sql);
					 String group[] = new String[16];
					if(rs.next()){
					 for(int i=0;i<16;i++){
						 group[i] = rs.getString(i+1)+"";
						 System.out.println(group[i]+" "); 
					 }
					 v.add(group);//将信息数组添加到返回的向量里
					}
	
				}
				catch(Exception e)
				{e.printStackTrace();}
				finally
				{ try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				  try {
					stat.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				  try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				  }	
			   
	 
			  
			Map<String,Object>  map =new HashMap<String, Object>();
			map.put("result",v);
	 
			writeJson(map);
		}
		else if(op.equals("modify"))
		{
			int id =  Integer.parseInt(getRequest().getParameter("id"));
			String stnumber1 =  getRequest().getParameter("stnumber");
			String stname1 =  getRequest().getParameter("stname");
			String stantenna11 =  getRequest().getParameter("stantenna");
			String sttower11 =  getRequest().getParameter("sttower");
			String stposx11 =  getRequest().getParameter("stposx");
			String stposy11 =  getRequest().getParameter("stposy");
			String staddress1 =  getRequest().getParameter("staddress");
			String stdate1 =  getRequest().getParameter("stdate");
			String stnet1 =  getRequest().getParameter("stnet");
			String stfre1 =  getRequest().getParameter("stfre");
			String stshare1 =  getRequest().getParameter("stshare");
			String stprocedure1 =  getRequest().getParameter("stprocedure");
			String stvalidaty1 =  getRequest().getParameter("stvalidaty");
			
			Double stantenna1 =  Double.valueOf(stantenna11);
			Double sttower1 =  Double.valueOf(sttower11);
			Double stposx1 =  Double.valueOf(stposx11);
			Double stposy1 =  Double.valueOf(stposy11);
			
			
			Connection con=null;//声明Connection引用
			Statement stat=null;//声明Statement引用
			ResultSet rs=null;//声明ResultSet引用
				try{
				Class.forName("com.mysql.jdbc.Driver");
				String url="jdbc:mysql://localhost/inspect";
				con = DriverManager.getConnection(url,"root","123");
				}catch(ClassNotFoundException e){
					e.printStackTrace();
					}catch(SQLException e1){
						e1.printStackTrace();
						}
				
				//Vector<String[]> v =new Vector<String[]>();//创建返回向量对象
				try{
					 stat = con.createStatement();//创建语句对象
					// String sql = "select * from t_station where id="+id;
					 
					String sqla =  "update t_station set stnumber ='"+stnumber1+"', stname='"+stname1+"',stantenna='"+stantenna1+"',sttower='"+sttower1+"',stposx='"+stposx1+"',stposy='"+stposy1+"',staddress='"+staddress1+"',stdate='"+stdate1+"',stnet='"+stnet1+"',stfre='"+stfre1+"',stshare='"+stshare1+"',stprocedure='"+stprocedure1+"',stvalidaty='"+stvalidaty1+"' where id="+id ;
					 
					String sql = new String(sqla.getBytes(),"iso8859-1");
					
					int result = stat.executeUpdate(sql);	
//					 rs = stat.executeQuery(sql);
//					 String group[] = new String[15];
//					if(rs.next()){
//					 for(int i=0;i<15;i++){
//						 group[i] = rs.getString(i+1)+"";
//						 System.out.print(group[i]+" "); 
//					 }
//					 v.add(group);//将信息数组添加到返回的向量里
//					}
	
				}
				catch(Exception e)
				{e.printStackTrace();}
				finally
				{ try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				  try {
					stat.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				  try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				  }	
			   
	 
			  
			Map<String,Object>  map =new HashMap<String, Object>();
			map.put("result",1);
	 
			writeJson(map);
		}
		else if(op.equals("delete"))
		{
			System.out.println("***********delete");
			
			int id =  Integer.parseInt(getRequest().getParameter("id"));
			
			System.out.println("***********delete id ="+id);
			
			Connection con=null;//声明Connection引用
			Statement stat=null;//声明Statement引用
			ResultSet rs=null;//声明ResultSet引用
				try{
				Class.forName("com.mysql.jdbc.Driver");
				String url="jdbc:mysql://localhost/inspect";
				con = DriverManager.getConnection(url,"root","123");
				System.out.println("连接数据库成功！");
				
				}catch(ClassNotFoundException e){
					e.printStackTrace();
					}catch(SQLException e1){
						e1.printStackTrace();
						}
				
				//Vector<String[]> v =new Vector<String[]>();//创建返回向量对象
				try{
					 stat = con.createStatement();//创建语句对象
				    String sqla = "delete from t_station where id="+id;
					 
					//String sql = new String(sqla.getBytes(),"iso8859-1");
					
					int result = stat.executeUpdate(sqla);	
					if(result>0){
					System.out.println("删除数据库成功！");
					}
	
				}
				catch(Exception e)
				{e.printStackTrace();}
				finally
				{ try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				  try {
					stat.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				  try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				  }	
			   
	 
			  
			Map<String,Object>  map =new HashMap<String, Object>();
			map.put("result",2);
	 
			writeJson(map);
		} 
		else if(op.equals("query"))
		{
			System.out.println("***********query");
			
			String stnamevalue;
			try {
				stnamevalue = new String(getRequest().getParameter("stnamevalue").trim().getBytes("iso-8859-1"),"utf-8");
	
				System.out.println("stnamevalue***********" +stnamevalue);
		
			Connection con=null;//声明Connection引用
			Statement stat=null;//声明Statement引用
			ResultSet rs=null;//声明ResultSet引用
				try{
				Class.forName("com.mysql.jdbc.Driver");
				String url="jdbc:mysql://localhost/inspect";
				con = DriverManager.getConnection(url,"root","123");
				}catch(ClassNotFoundException e){
					e.printStackTrace();
					}catch(SQLException e1){
						e1.printStackTrace();
						}
				
				Vector<String[]> v =new Vector<String[]>();//创建返回向量对象
				try{
					 stat = con.createStatement();//创建语句对象
					 String sqla = "select id,stnumber,stname,stantenna,sttower,staddress from t_station WHERE stname like \"%"+ stnamevalue +"%\" order by  id desc ";
					// String sql = new String(sqla.getBytes(),"iso8859-1");//转码
					 rs = stat.executeQuery(sqla);
					 while(rs.next()){//遍历结果集得到分组信息		    
					    String group[] = new String[6];
					    for(int i=0;i<group.length;i++){
					      group[i] = rs.getString(i+1)+"";
					      
					      System.out.print(group[i]+" ");
					    }	
					    System.out.println(" ");
						v.add(group);//将信息数组添加到返回的向量里
					}
				}
				catch(Exception e)
				{e.printStackTrace();}
				finally
				{ try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				  try {
					stat.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				  try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				  }	
				

			  System.out.println("***daxiao*******  "+v.size());
	 
			  
			Map<String,Object>  map =new HashMap<String, Object>();
			map.put("result",v);
	 
			writeJson(map);

			} catch (UnsupportedEncodingException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
		 
		}
		else if(op.equals("update"))
		{
			String stpic =  getRequest().getParameter("stpic");
			
			
			try {	
			
			String stnumber1 =  new String(getRequest().getParameter("stnumber").trim().getBytes("iso-8859-1"),"utf-8");
			String stname1 =   new String(getRequest().getParameter("stname").trim().getBytes("iso-8859-1"),"utf-8");
			String stantenna1 =   getRequest().getParameter("stantenna").trim();
			String sttower1 =   getRequest().getParameter("sttower").trim();
			String stposx1 =   getRequest().getParameter("stposx").trim();
			String stposy1 =   getRequest().getParameter("stposy").trim();
			String staddress1 =  new String( getRequest().getParameter("staddress").trim().getBytes("iso-8859-1"),"utf-8");
			String stdate1 =   new String(getRequest().getParameter("stdate").trim().getBytes("iso-8859-1"),"utf-8");
			String stnet1 =   new String(getRequest().getParameter("stnet").trim().getBytes("iso-8859-1"),"utf-8");
			String stfre1 =   new String(getRequest().getParameter("stfre").trim().getBytes("iso-8859-1"),"utf-8");
			String stshare1 =   new String(getRequest().getParameter("stshare").trim().getBytes("iso-8859-1"),"utf-8");
			String stprocedure1 =   new String(getRequest().getParameter("stprocedure").trim().getBytes("iso-8859-1"),"utf-8");
			String stvalidaty1 =   new String(getRequest().getParameter("stvalidaty").trim().getBytes("iso-8859-1"),"utf-8");
			//String stpic =  getRequest().getParameter("stpic").trim();
			
			if(stantenna1.equals("")){
				stantenna1 = "0";
			}
			if(sttower1.equals("")){
				sttower1= "0";
			}
			if(stposx1.equals("")){
				stposx1 = "0";
			}
			if(stposy1.equals("")){
				stposy1 = "0";
			}
			Double stantenna11 =  Double.valueOf(stantenna1);
			Double sttower11 =  Double.valueOf(sttower1);
			Double stposx11 =  Double.valueOf(stposx1);
			Double stposy11 =  Double.valueOf(stposy1);
			
			
			
			 System.out.print("***update****stname1************"+stname1); 
			
			
			
			Connection con=null;//声明Connection引用
			Statement stat=null;//声明Statement引用
			ResultSet rs=null;//声明ResultSet引用
				try{
				Class.forName("com.mysql.jdbc.Driver");
				//String url="jdbc:mysql://localhost/inspect";
				String url="jdbc:mysql://localhost/inspect";
				con = DriverManager.getConnection(url,"root","123");
				}catch(ClassNotFoundException e){
					e.printStackTrace();
					}catch(SQLException e1){
						e1.printStackTrace();
						}
				
				//Vector<String[]> v =new Vector<String[]>();//创建返回向量对象
				try{
					 stat = con.createStatement();//创建语句对象
					// String sql = "select * from t_station where id="+id;
					 
					String sqla =  "update t_station set stnumber ='"+stnumber1+"', stname='"+stname1+"',stantenna='"+stantenna11+"',sttower='"+sttower11+"',stposx='"+stposx11+"',stposy='"+stposy11+"',staddress='"+staddress1+"',stdate='"+stdate1+"',stnet='"+stnet1+"',stfre='"+stfre1+"',stshare='"+stshare1+"',stprocedure='"+stprocedure1+"',stvalidaty='"+stvalidaty1+"' where stpic="+stpic ;
					 
					//String sql = new String(sqla.getBytes(),"iso8859-1");
					
					stat.executeUpdate(sqla);	
					
					
					  
					Map<String,Object>  map =new HashMap<String, Object>();
					map.put("result",0);
			 
					String jsontest = JSON.toJSONStringWithDateFormat(map, "yyyy-MM-dd HH:mm:ss");
					System.out.println("****jsontest*******"+jsontest);
					
					
					writeJson(map);
					
					
 
	
				}
				catch(Exception e)
				{e.printStackTrace();}
				finally
				{ 
					
					try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				  try {
					stat.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				  try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				  }	
			   
	 

			
			
			
//			Map<String,Object>  map =new HashMap<String, Object>();
//			map.put("result",0);
//		
//			writeJson(map);
//			}
//			else{
//				Map<String,Object>  map =new HashMap<String, Object>();
//				map.put("result",1);
//				writeJson(map);
//			} 	
//			
//			} catch (UnsupportedEncodingException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			
			
			
			
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
		//图片上传
		  // 上传文件域
	    private File image;
	    // 上传文件类型
	    private String imageContentType;
	    // 封装上传文件名
	    private String imageFileName;
	    // 接受依赖注入的属性
	    private String savePath;
	    private String stpic;
	    @Override
	    public String execute() {
	    	  System.out.println("execute()");
	        HttpServletRequest request=ServletActionContext.getRequest();
	        FileOutputStream fos = null;
	        FileInputStream fis = null;
	      
	        stpic = request.getParameter("stpic");
	        try {
	            System.out.println("获取Android端传过来的普通信息：");
	            System.out.println("用户名："+request.getParameter("username"));
	            System.out.println("密码："+request.getParameter("pwd"));
	            System.out.println("年龄："+request.getParameter("age"));
	            System.out.println("文件名："+request.getParameter("fileName"));
	            System.out.println("文件夹名："+request.getParameter("stpic"));
	            System.out.println("获取Android端传过来的文件信息：");
	            System.out.println("文件存放目录: "+getSavePath());
	            System.out.println("文件名称: "+imageFileName);
	            System.out.println("文件大小: "+image.length());
	            System.out.println("文件类型: "+imageContentType);
	            
	            
	            
	            String  picpath  = getSavePath() + "/" + stpic;
	            System.out.println("picpath  " + picpath);
	            
	            
	            File file =new File(picpath);    
	          //如果文件夹不存在则创建    
	          if  (!file.exists()  && !file .isDirectory())      
	          {       
	              System.out.println("//不存在");  
	              file.mkdir();    
	          }
	          else   
	          {  
	              System.out.println("//目录存在");  
	          } 
	            
	            
	            
	            //fos = new FileOutputStream(getSavePath() + "/" + getImageFileName());
	            
	          fos = new FileOutputStream(picpath  + "/" + getImageFileName()); 
	            
	          System.out.println("getImageFileName() "+ getImageFileName());  
	            
	            
	            
	            fis = new FileInputStream(getImage());
	            byte[] buffer = new byte[1024];
	            int len = 0;
	            while ((len = fis.read(buffer)) != -1) {
	                fos.write(buffer, 0, len);
	            }
	            System.out.println("文件上传成功");
	            

	            
	        } catch (Exception e) {
	            System.out.println("文件上传失败");
	            e.printStackTrace();
	        } finally {
	            close(fos, fis);
	        }
	        return SUCCESS;
	    }

	    /**
	     * 文件存放目录
	     * 
	     * @return
	     */
	    public String getSavePath() throws Exception{
	        return ServletActionContext.getServletContext().getRealPath(savePath); 
	    }

	    public void setSavePath(String savePath) {
	        this.savePath = savePath;
	    }

	    public File getImage() {
	        return image;
	    }

	    public void setImage(File image) {
	        this.image = image;
	    }

	    public String getImageContentType() {
	        return imageContentType;
	    }

	    public void setImageContentType(String imageContentType) {
	        this.imageContentType = imageContentType;
	    }

	    public String getImageFileName() {
	        return imageFileName;
	    }

	    public void setImageFileName(String imageFileName) {
	        this.imageFileName = imageFileName;
	    }

	    private void close(FileOutputStream fos, FileInputStream fis) {
	        if (fis != null) {
	            try {
	                fis.close();
	                fis=null;
	            } catch (IOException e) {
	                System.out.println("FileInputStream关闭失败");
	                e.printStackTrace();
	            }
	        }
	        if (fos != null) {
	            try {
	                fos.close();
	                fis=null;
	            } catch (IOException e) {
	                System.out.println("FileOutputStream关闭失败");
	                e.printStackTrace();
	            }
	        }
	    }
		
		//图片上传结束
}
