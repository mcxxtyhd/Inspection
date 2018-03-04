package com.inspect.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.hibernate.SQLQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.inspect.dao.BaseDaoI;
import com.inspect.model.baseinfo.TBaseInfo;
import com.inspect.model.basis.TInspectUser;
import com.inspect.model.basis.TLine;
import com.inspect.model.basis.TLinePoint;
import com.inspect.model.basis.TPlanTask;
import com.inspect.model.basis.TPoint;
import com.inspect.model.basis.TPointEquipment;
import com.inspect.model.basis.TTerminal;
import com.inspect.model.monitor.TInspectItemDetailReport;
import com.inspect.model.monitor.TInspectItemRaltionReport;
import com.inspect.model.monitor.TInspectItemReport;
import com.inspect.model.monitor.TInspectPointReport;
import com.inspect.model.monitor.TTerminateStatusReport;
import com.inspect.model.monitor.TUserLocateReport;
import com.inspect.service.InspectMonitorServiceI;
import com.inspect.service.InspectUserServiceI;
import com.inspect.util.common.MaapDateFormatter;
import com.inspect.util.common.QueryResult;
import com.inspect.util.common.StringUtils;
import com.inspect.vo.monitor.InspectItemReportVo;
import com.inspect.vo.monitor.LineLocateMap;
import com.inspect.vo.monitor.LocateReportMap;
import com.inspect.vo.monitor.SearchLocateMap;

@Service("inspectMonitorService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
public class InspectMonitorServiceImpl implements InspectMonitorServiceI {

	private BaseDaoI baseDao;

	@Resource
	public void setBaseDao(BaseDaoI baseDao) {
		this.baseDao = baseDao;
	}

	@Resource
	private InspectUserServiceI inspectUserService;

	/**
	 * 查询在线人数和在线终端数
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<TTerminateStatusReport> getUserLocateReportLIst(SearchLocateMap userlocatereport, int qsql, int flag) {
		Date date = new Date();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String date1 = (simpleDateFormat).format(date);
		StringBuffer buf = new StringBuffer("select rpterminateid,rpuserid,rpgroupid,rplineid from t_terminate_status_report where flag =1");
		if (qsql != 0) {
			buf.append(" and entid='").append(qsql).append("'");
		}
		// 查询在线人数
		if (flag == 0) {
			buf.append(" and rplogintime like '" + date1 + "%' and id in(select max(id) from t_terminate_status_report   group by rpuserid )");
		}
		// 查询在线终端数
		else if (flag == 1) {
			buf.append(" and rplogintime like '" + date1 + "%' and id in(select max(id) from t_terminate_status_report   group by rpterminateid )");
		}
		// System.out.println(buf.toString());
		SQLQuery sqlquery = baseDao.getHibernaSession().createSQLQuery(buf.toString());
		List resultset = sqlquery.list();
		return resultset;
	}

	/**
	 * 查询终端某天巡检人员信息
	 * 
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<TTerminateStatusReport> getTerminateStatusReportList(SearchLocateMap userlocatereport, int qsql) {
		Date date = new Date();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String date1 = (simpleDateFormat).format(date);
		StringBuffer buf = new StringBuffer("select rpterminateid,rpuserid,rpgroupid,rplineid from t_terminate_status_report where flag=1 ");
		if (qsql != 0) {
			buf.append(" and entid=").append(qsql);
		}
		if (!StringUtils.isEmpty(userlocatereport.getIuserName())) {
			buf.append(" and rpuserid =").append(userlocatereport.getIuserName());
		}
		if (!StringUtils.isEmpty(userlocatereport.getGroupName())) {
			buf.append(" and rpgroupid =").append(userlocatereport.getGroupName());
		}
		if (!StringUtils.isEmpty(userlocatereport.getStartDate())) {
			buf.append(" and rplogintime like'%").append(userlocatereport.getStartDate()).append("%'");
			buf.append(" and id in(select max(id) from t_terminate_status_report where rplogintime like'%").append(userlocatereport.getStartDate()).append("%' group by rpterminateid )");
		} else {
			buf.append(" and rplogintime like '%" + date1 + "%'");
			buf.append(" and id in(select max(id) from t_terminate_status_report where rplogintime like'%").append(date1).append("%' group by rpterminateid )");
		}
		SQLQuery sqlquery = baseDao.getHibernaSession().createSQLQuery(buf.toString());
		List resultset = sqlquery.list();
		return resultset;
	}

	/**
	 * 在地图上获取人员坐标等信息
	 */
	@Override
	public List<LocateReportMap> getAvailableTerms_liao(SearchLocateMap userlocatereport, int qsql) {

		Date date = new Date();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String date1 = (simpleDateFormat).format(date);
		StringBuffer buf2 = new StringBuffer();
		if (qsql != 0) {
			buf2.append(" and entid=").append(qsql);
		}
		if (!StringUtils.isEmpty(userlocatereport.getIuserName())) {
			buf2.append(" and rpuserid =").append(userlocatereport.getIuserName());
		}
		if (!StringUtils.isEmpty(userlocatereport.getGroupName())) {
			buf2.append(" and rpgroupid =").append(userlocatereport.getGroupName());
		}
		if (userlocatereport.getEntId() != 0) {
			buf2.append(" and entid =" + userlocatereport.getEntId());
		}
		StringBuffer buf1 = new StringBuffer("SELECT DISTINCT tt.ID,tt.rpterminalid,tt.rpuserid,u.irealname ,tt.rpposx,tt.rpposy,tt.rplineid,tt.rptime FROM  ");

		buf1.append(" (select max(id) as id,a.rpterminalid  from t_user_locate_report a where 1=1 ").append(buf2.toString() + " group by a.rpterminalid )t ");

		buf1.append(" INNER JOIN t_user_locate_report tt ON ( t.id = tt.ID");
		if (StringUtils.isNotEmpty(userlocatereport.getStartDate())) {
			buf1.append("  and  tt.rptime like '" + userlocatereport.getStartDate() + "%' ) ");
		} else {
			buf1.append("  and  tt.rptime like '" + date1 + "%' ) ");
		}
		buf1.append(" LEFT JOIN t_terminate_status_report ts ON (t.rpterminalid = ts.rpterminateid)").append(" LEFT JOIN t_inspect_user u ON (u.id=tt.rpuserid)");
		// 如果时间条件为空
		if (StringUtils.isEmpty(userlocatereport.getStartDate())) {
			buf1.append(" WHERE ts.flag =1  ");
		} else {
			buf1.append(" WHERE ts.rplogintime LIKE '").append(userlocatereport.getStartDate()).append("%'");
		}
		//System.out.println(buf1.toString());
		SQLQuery sqlquery = baseDao.getHibernaSession().createSQLQuery(buf1.toString());
		List resultset = sqlquery.list();
		List<LocateReportMap> terms = new ArrayList<LocateReportMap>();
		if (resultset == null || resultset.size() == 0) {
			return terms;
		}
		try {
			if (resultset != null && !resultset.isEmpty()) {
				for (int i = 0; i < resultset.size(); i++) {
					Object[] obj = (Object[]) resultset.get(i);
					// tt.ID,tt.rpterminalid,tt.rpuserid,u.irealname ,tt.rpposx,tt.rpposy,tt.rplineid,tt.rptime

					LocateReportMap term = new LocateReportMap(); // 地图显示提示信息
					term.setRpTerminalNumber((String) obj[1]);// 终端编号
					term.setRpUserId(String.valueOf((Integer) obj[2]));// 巡检员id
					term.setRpUserName((String) obj[3]); // 巡检员名称
					term.setRpPosX((Double) obj[4]);// 经度
					term.setRpPosY((Double) obj[5]);// 纬度
					term.setRpRepoetTime((String) obj[7]);// 上报时间
					term.setRpLastTimestamp((String) obj[7]);// 最后上报时间
					// 获取巡检员信息
					TInspectUser iuser = baseDao.get(TInspectUser.class, (Integer) obj[2]);
					TTerminal t = inspectUserService.getTerminalByTermno(term.getRpTerminalNumber());
					term.setRpUserMobile(t == null ? "" : t.getTtelnumber());// 终端手机号
					if (iuser != null) {
						term.setRpGroupName(iuser.getTgroup().getGname());
					}
					TPlanTask line = baseDao.getEntityById(TPlanTask.class, (Integer) obj[6]);
					if (line != null) {
						term.setRpLineName(line.getPname());
					}
					terms.add(term);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return terms;
	}

	@SuppressWarnings("unchecked")
	private Map<String, LocateReportMap> getAvailableTermMap_liao(List resultset) {
		Map<String, LocateReportMap> termMap = new HashMap<String, LocateReportMap>();
		LocateReportMap[] terms = new LocateReportMap[resultset.size()];
		/** *************** 终端集合消息对象 ************ */
		for (int i = 0; i < terms.length; i++) {
			terms[i] = new LocateReportMap();
			Object[] obj = (Object[]) resultset.get(i);
			terms[i].setRpTerminalNumber((String) obj[0]); // 终端编号 获取当前处于工作状态中的巡检终端编号
			// int aa=((Integer) obj[1]).intValue();
			// terms[i].setRpUserNumber(String.valueOf(aa)); //巡检员id 主键
			termMap.put(terms[i].getRpTerminalNumber(), terms[i]); // 保存巡检巡检终端编号
		}
		/** *************** 终端实时上报消息对象 ************ */
		try {
			Map<String, Object> detailMap = new HashMap<String, Object>();
			StringBuffer buffer = new StringBuffer("id,rpuserid,rpterminalid,rpposx,rpposy,rptime,rplineid,rpgroupid");
			List<String> paramList = new ArrayList<String>();
			paramList.add("id");
			paramList.add("rpuserid");
			paramList.add("rpterminalid");
			paramList.add("rpposx");
			paramList.add("rpposy");
			paramList.add("rptime");
			paramList.add("rplineid");
			paramList.add("rpgroupid");
			String limitTime = MaapDateFormatter.getStoreFormatter().format(new Date(System.currentTimeMillis() - 10 * 3600 * 24 * 1000));
			SQLQuery sqlQuery = this.baseDao.getHibernaSession().createSQLQuery(// where rptime>'" + limitTime + "'
					"SELECT " + buffer + " FROM (SELECT * FROM  t_user_locate_report  ORDER BY rptime DESC) as availablemessage GROUP BY rpterminalid");
			List resultSet = sqlQuery.list();
			if (resultSet != null && !resultSet.isEmpty()) {
				for (int i = 0; i < resultSet.size(); i++) {
					Object[] result = (Object[]) resultSet.get(i);
					for (int j = 0; j < paramList.size(); j++) {
						detailMap.put(paramList.get(j), result[j]);
					}
					String termId = (String) detailMap.get("rpterminalid");
					// 将terms[]里面的终端id与对象detailMap里面的终端id配对，如果有，将此终端id的经纬度等信息放入进来
					if (termMap.containsKey(termId)) {
						LocateReportMap term = termMap.get(termId); // 地图显示提示信息
						term.setRpPosX((Double) detailMap.get("rpposx"));// 经度
						term.setRpPosY((Double) detailMap.get("rpposy"));// 纬度
						// 获取巡检员信息
						TInspectUser iuser = baseDao.get(TInspectUser.class, (Integer) detailMap.get("rpuserid"));
						TTerminal t = inspectUserService.getTerminalByTermno(termId);// 终端手机号
						term.setRpUserMobile(t == null ? "" : t.getTtelnumber());
						if (iuser != null) {
							term.setRpUserName(iuser.getIrealname()); // 巡检员名称
							// term.setRpUserMobile(iuser.getIumobile());//巡检员手机号
							term.setRpGroupName(iuser.getTgroup().getGname());
						}
						// term.setRpUserId1(String.valueOf((Integer)detailMap.get("rpuserid")));
						// TLine line=baseDao.get(TLine.class,(Integer)detailMap.get("rplineid"));
						TPlanTask line = baseDao.getEntityById(TPlanTask.class, (Integer) detailMap.get("rplineid"));
						if (line != null) {
							term.setRpLineName(line.getPname());
						}
						String timestamp = (String) detailMap.get("rptime");
						term.setRpRepoetTime(timestamp);
						term.setRpLastTimestamp(timestamp);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return termMap;
	}

	/**
	 * 在地图上获取人员坐标等信息
	 */
	@Override
	public List<LocateReportMap> getAvailableTerms(List<String> termIds) {
		List<LocateReportMap> terms = new ArrayList<LocateReportMap>();
		if (termIds == null || termIds.size() == 0)
			return terms;
		Map<String, LocateReportMap> termMap = getAvailableTermMap(termIds);
		terms.addAll(termMap.values());
		termMap.clear();
		return terms;
	}

	@SuppressWarnings("unchecked")
	private Map<String, LocateReportMap> getAvailableTermMap(List resultset) {
		Map<String, LocateReportMap> termMap = new HashMap<String, LocateReportMap>();
		LocateReportMap[] terms = new LocateReportMap[resultset.size()];
		/** *************** 终端集合消息对象 ************ */
		for (int i = 0; i < terms.length; i++) {
			terms[i] = new LocateReportMap();
			Object[] obj = (Object[]) resultset.get(i);
			terms[i].setRpTerminalNumber((String) obj[0]); // 终端编号 获取当前处于工作状态中的巡检终端编号
			// int aa=((Integer) obj[1]).intValue();
			// terms[i].setRpUserNumber(String.valueOf(aa)); //巡检员id 主键
			termMap.put(terms[i].getRpTerminalNumber(), terms[i]); // 保存巡检巡检终端编号
		}
		/** *************** 终端实时上报消息对象 ************ */
		try {
			Map<String, Object> detailMap = new HashMap<String, Object>();
			StringBuffer buffer = new StringBuffer("id,rpuserid,rpterminalid,rpposx,rpposy,rptime,rplineid,rpgroupid");
			List<String> paramList = new ArrayList<String>();
			paramList.add("id");
			paramList.add("rpuserid");
			paramList.add("rpterminalid");
			paramList.add("rpposx");
			paramList.add("rpposy");
			paramList.add("rptime");
			paramList.add("rplineid");
			paramList.add("rpgroupid");
			String limitTime = MaapDateFormatter.getStoreFormatter().format(new Date(System.currentTimeMillis() - 10 * 3600 * 24 * 1000));
			SQLQuery sqlQuery = this.baseDao.getHibernaSession().createSQLQuery(// where rptime>'" + limitTime + "'
					"SELECT " + buffer + " FROM (SELECT * FROM  t_user_locate_report  ORDER BY rptime DESC) as availablemessage GROUP BY rpterminalid");
			List resultSet = sqlQuery.list();
			if (resultSet != null && !resultSet.isEmpty()) {
				for (int i = 0; i < resultSet.size(); i++) {
					Object[] result = (Object[]) resultSet.get(i);
					for (int j = 0; j < paramList.size(); j++) {
						detailMap.put(paramList.get(j), result[j]);
					}
					String termId = (String) detailMap.get("rpterminalid");
					// 将terms[]里面的终端id与对象detailMap里面的终端id配对，如果有，将此终端id的经纬度等信息放入进来
					if (termMap.containsKey(termId)) {
						LocateReportMap term = termMap.get(termId); // 地图显示提示信息
						term.setRpPosX((Double) detailMap.get("rpposx"));// 经度
						term.setRpPosY((Double) detailMap.get("rpposy"));// 纬度
						// 获取巡检员信息
						TInspectUser iuser = baseDao.get(TInspectUser.class, (Integer) detailMap.get("rpuserid"));
						TTerminal t = inspectUserService.getTerminalByTermno(termId);// 终端手机号
						term.setRpUserMobile(t == null ? "" : t.getTtelnumber());
						if (iuser != null) {
							term.setRpUserName(iuser.getIrealname()); // 巡检员名称
							// term.setRpUserMobile(iuser.getIumobile());//巡检员手机号
							term.setRpGroupName(iuser.getTgroup().getGname());
						}
						// term.setRpUserId1(String.valueOf((Integer)detailMap.get("rpuserid")));
						// TLine line=baseDao.get(TLine.class,(Integer)detailMap.get("rplineid"));
						TPlanTask line = baseDao.getEntityById(TPlanTask.class, (Integer) detailMap.get("rplineid"));
						if (line != null) {
							term.setRpLineName(line.getPname());
						}
						String timestamp = (String) detailMap.get("rptime");
						term.setRpRepoetTime(timestamp);
						term.setRpLastTimestamp(timestamp);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return termMap;
	}

	@SuppressWarnings("unchecked")
	public List<String> showTermMove(String rptermId) {
		List<String> datas = new ArrayList<String>();
		datas.add(rptermId);
		if (rptermId == null)
			return datas;
		SQLQuery sqlQuery = this.baseDao.getHibernaSession().createSQLQuery("select distinct(CONCAT(rpposx, rpposy)) , rpposx, rpposy from t_user_locate_report  where rpterminalid='" + rptermId + "' order by rptime desc  limit 10");
		List resultSet = sqlQuery.list();
		if (resultSet != null && resultSet.size() > 0) {
			for (int i = resultSet.size() - 1; i >= 0; i--) {
				Object[] result = (Object[]) resultSet.get(i);
				if (result.length == 3) {
					datas.add(result[1].toString());
					datas.add(result[2].toString());
				}
			}
		}
		return datas;
	}

	public String getLineIds(int eid) {
		StringBuffer lineIds = new StringBuffer();
		String hql = "from TLine where entid= '" + eid + "' ";
		List<TLine> resultset = baseDao.find(hql);
		if (resultset != null && resultset.size() > 0) {
			for (TLine base : resultset) {
				if (resultset.size() - 1 == resultset.lastIndexOf(base)) {
					lineIds.append(base.getId());
				} else {
					lineIds.append(base.getId() + ",");
				}
			}
		}
		return lineIds.toString();
	}

	@SuppressWarnings("unchecked")
	public String getLineIdS(SearchLocateMap smap) {
		StringBuffer lineIds = new StringBuffer();
		StringBuffer buf = new StringBuffer("select distinct(plineid) from t_plan where 1=1 ");
		if (smap.getEntId() == 0) {
		} else {
			buf.append(" and  entid=").append(smap.getEntId());
		}
		if (!StringUtils.isEmpty(smap.getLineName())) {
			buf.append(" and plineid =").append(smap.getLineName());
		}
		if ((!StringUtils.isEmpty(smap.getGroupName()) && (!StringUtils.isEmpty(smap.getIuserName())))) {
			buf.append(" and id in (select pid from t_plan_user where gid=" + smap.getGroupName() + " and uid=" + smap.getGroupName() + ") ");
		} else {
			if (!StringUtils.isEmpty(smap.getGroupName())) {
				buf.append(" and id in (select pid from t_plan_user where gid=" + smap.getGroupName() + ") ");
			}

			if (!StringUtils.isEmpty(smap.getIuserName())) {
				buf.append(" and id in (select pid from t_plan_user where uid=" + smap.getIuserName() + ") ");
			}
		}

		if (!StringUtils.isEmpty(smap.getStartDate())) {
			String beginDate = smap.getStartDate().replaceAll("-", "").replaceAll(" ", "").replaceAll(":", "");
			beginDate += "000";
			buf.append(" and pstartdate >= '").append(beginDate).append("'");
		}
		if (!StringUtils.isEmpty(smap.getEndDate())) {
			String endDate = smap.getEndDate().replaceAll("-", "").replaceAll(" ", "").replaceAll(":", "");
			endDate += "999";
			buf.append(" and penddate <= '").append(endDate).append("'");
		}
		// System.out.println("buf==:"+buf.toString());

		SQLQuery sqlquery = baseDao.getHibernaSession().createSQLQuery(buf.toString());
		List<TLine> resultset = sqlquery.list();
		boolean b = false;
		if (resultset != null && resultset.size() > 0) {
			for (int i = 0; i < resultset.size(); i++) {
				TLine point = baseDao.get(TLine.class, resultset.get(i));
				if (b) {
					lineIds.append(",");
				}
				if (point != null) {
					lineIds.append(point.getId());
				}
				b = true;
			}
		}
		// System.out.println("eeeeeeeeeeeee");
		// System.out.println("11111111111"+lineIds.toString());

		return lineIds.toString();
	}

	@SuppressWarnings("unchecked")
	public List getLineId(int eid) {
		List<TLine> list = new ArrayList<TLine>();
		String hql = "select distinct(plineid) from t_plan ";
		SQLQuery sqlquery = baseDao.getHibernaSession().createSQLQuery(hql);
		List<TLine> l = sqlquery.list();
		for (int i = 0; i < l.size(); i++) {
			TLine pinfo = new TLine();
			TLine ll = baseDao.get(TLine.class, l.get(i));
			if (ll != null) {

				pinfo.setId(ll.getId());
			}
			list.add(pinfo);
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	public String getPointIdsByLineId(String lineId) {
		StringBuffer pointIds = new StringBuffer();
		String hql = "select distinct(pointid) from t_line_point o where o.lineid in (" + lineId + ") ";
		SQLQuery sqlquery = baseDao.getHibernaSession().createSQLQuery(hql);
		List<TLinePoint> resultset = sqlquery.list();
		boolean b = false;
		if (resultset != null && resultset.size() > 0) {
			for (int i = 0; i < resultset.size(); i++) {
				TPoint point = baseDao.get(TPoint.class, resultset.get(i));
				if (b) {
					pointIds.append(",");
				}
				if (point != null) {

					pointIds.append(point.getId());
				}
				b = true;
			}
		}
		return pointIds.toString();
	}

	/**
	 * 返回值是只有一个且只有一个LineLocateMap对象的List集合
	 */
	public List<LineLocateMap> getAvailablePoint(String pointId) {
		List<LineLocateMap> terms = new ArrayList<LineLocateMap>();
		if (pointId.length() == 0)
			return terms;
		String hql = "from TPoint where id=" + pointId;
		List<TPoint> resultset = baseDao.findByHqlAll(TPoint.class, hql);
		Map<String, LineLocateMap> termMap = getAvailableTermPointMap(resultset);
		// getAvailableTermPointMap方法逻辑比较繁琐，其实可以直接返回一个LineLocateMap对象，记为A
		// getAvailablePoint直接返回对象A即可
		terms.addAll(termMap.values());
		termMap.clear();
		return terms;
	}

	@SuppressWarnings("unchecked")
	private Map<String, LineLocateMap> getAvailableTermPointMap(List resultset) {
		Map<String, LineLocateMap> termMap = new HashMap<String, LineLocateMap>();
		LineLocateMap[] terms = new LineLocateMap[resultset.size()];// resultset.size()肯定为1，即循环一次
		for (int i = 0; i < terms.length; i++) {
			terms[i] = new LineLocateMap();
			TPoint obj = (TPoint) resultset.get(i);
			if (obj != null) {
				terms[i].setRpPointId(String.valueOf(obj.getId())); // 巡检点Id
				terms[i].setRpPointNumber(obj.getPonumber()); // 巡检点编号
				terms[i].setRpPointName(obj.getPoname()); // 点名称
				terms[i].setRpPosX(obj.getPoposx()); // 经度
				terms[i].setRpPosY(obj.getPoposy()); // 纬度

				// 巡检设备
				Set<TPointEquipment> list = obj.getPointequipments();
				String eNames = "";
				boolean b = false;
				if (list != null && list.size() > 0) {
					for (TPointEquipment ept : list) {
						if (ept.getTequipment() != null) {
							if (b) {
								eNames += ",";
							}
							eNames += ept.getTequipment().getEnumber();
							b = true;
						}
					}
				}
				if (!StringUtils.isEmpty(eNames)) {
					String[] names = eNames.split(",");
					terms[i].setRpEquipments(Arrays.asList(names));
				}

				Set<TLinePoint> lps = obj.getLinepoints();
				if (lps != null && lps.size() > 0) {
					for (TLinePoint lp : lps) {
						if (lp.getTline() != null) {
							terms[i].setRpLineName(lp.getTline().getLname());
						}
					}
				}

				/** *******巡检点是否巡检告警处理******* */
				String sql = "select flag,xreptime from t_report_point_message WHERE xpnum='" + obj.getPonumber() + "' ORDER BY xreptime DESC LIMIT 1";
				SQLQuery sqlQuery = baseDao.getHibernaSession().createSQLQuery(sql);
				List rstSet = sqlQuery.list();
				if (!rstSet.isEmpty()) {
					for (int ii = 0; ii < rstSet.size(); ii++) {
						Object[] result = (Object[]) rstSet.get(ii);
						int warningFlag = ((Integer) result[0]).intValue();
						terms[i].setRpWarningFlag(warningFlag);
					}
				}
				/** *********************** */
				termMap.put(terms[i].getRpPointId(), terms[i]); // 将巡检点信息保存到MAP中 key：巡检点ID
			}
		}
		return termMap;
	}

	@SuppressWarnings("unchecked")
	public List<String> showTermPoints(String lineid) {
		List<String> datas = new ArrayList<String>();
		datas.add(lineid);
		if (lineid == null)
			return datas;
		String pointid = this.getPointIdsByLineId(lineid);
		if (pointid != null && pointid.length() > 0) {
			SQLQuery sqlQuery = this.baseDao.getHibernaSession().createSQLQuery("select distinct(CONCAT(poposx, poposy)) ,poposx,poposy from t_point where id in ( " + pointid + " ) ");
			List resultSet = sqlQuery.list();
			if (resultSet != null && resultSet.size() > 0) {
				for (int i = resultSet.size() - 1; i >= 0; i--) {
					Object[] result = (Object[]) resultSet.get(i);
					if (result.length == 3) {
						datas.add(result[1].toString());
						datas.add(result[2].toString());
					}
				}
			}
		}
		return datas;
	}

	public Map<String, Object> findInspectMessageDatagridByEId(InspectItemReportVo imessageVo, int page, int rows, String qsql) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer buf = new StringBuffer(qsql);
		if (!StringUtils.isEmpty(imessageVo.getXequtnum())) {
			buf.append(" and xequtnum = '").append(imessageVo.getXequtnum()).append("'");
		}
		QueryResult<TInspectItemReport> queryResult = baseDao.getQueryResult(TInspectItemReport.class, buf.toString(), (page - 1) * rows, rows, null, null);
		List<InspectItemReportVo> evolist = new ArrayList<InspectItemReportVo>();
		if (queryResult != null && queryResult.getResultList().size() > 0) {
			for (TInspectItemReport te : queryResult.getResultList()) {
				InspectItemReportVo evo = new InspectItemReportVo();
				BeanUtils.copyProperties(te, evo);

				TInspectUser u = baseDao.get(TInspectUser.class, te.getXuid());
				if (u != null) {
					evo.setXuname(u.getIuname());
				}
				if (te.getXstatus() != null && te.getXstatus().equals("0")) {
					// evo.setXstatusname(te.getXstatus().equals("0")?"正常":"告警");
					evo.setXstatus("告警");
				}
				if (te.getXstatus() != null && te.getXstatus().equals("1")) {
					// evo.setXstatusname(te.getXstatus().equals("0")?"正常":"告警");
					evo.setXstatus("正常");
				}

				evolist.add(evo);
			}
		}
		map.put("total", queryResult.getTotalRecord());
		map.put("come", (page - 1) * rows);
		map.put("to", rows * page);
		map.put("rows", evolist);
		return map;
	}

	/**
	 * 查询巡检人员上报信息，在图片上显示位置
	 */
	public List<TUserLocateReport> getUserLocateReportLIstByUid(SearchLocateMap userlocatereport) {
		StringBuffer buf = new StringBuffer("from TUserLocateReport where 1=1");
		if (userlocatereport.getEntId() != 0) {
			buf.append(" and entid=").append(userlocatereport.getEntId());
		}
		if (userlocatereport.getGroupName() != null && !userlocatereport.getGroupName().trim().equals("")) {
			buf.append(" and rpgroupid=").append(userlocatereport.getGroupName());
		}
		if (!StringUtils.isEmpty(userlocatereport.getIuserName())) {
			buf.append(" and rpuserid ='").append(userlocatereport.getIuserName()).append("'");
		}
		if (userlocatereport.getEntId() != 0) {
			buf.append(" and entid =" + userlocatereport.getEntId());
		}
		if (!StringUtils.isEmpty(userlocatereport.getStartDate())) {
			String beginDate = userlocatereport.getStartDate();
			buf.append(" and rptime like '%").append(beginDate).append("%' order by rptime desc");
		} else {
			Date date = new Date();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			buf.append(" and rptime like '%").append(simpleDateFormat.format(date)).append("%' order by rptime desc");
		}

		// System.out.println("dddddd="+buf.toString());
		List<TUserLocateReport> resultset = baseDao.find(buf.toString());
		return resultset;
	}

	public List<LocateReportMap> getAvailableUsers(List<String> userIds) {
		List<LocateReportMap> terms = new ArrayList<LocateReportMap>();
		if (userIds.size() == 0)
			return terms;
		Map<String, LocateReportMap> termMap = getAvailableUserMap(userIds);
		terms.addAll(termMap.values());
		termMap.clear();
		return terms;
	}

	@SuppressWarnings("unchecked")
	private Map<String, LocateReportMap> getAvailableUserMap(List resultset) {
		Map<String, LocateReportMap> termMap = new HashMap<String, LocateReportMap>();
		LocateReportMap[] terms = new LocateReportMap[resultset.size()];
		/** *************** 终端集合消息对象 ************ */
		for (int i = 0; i < terms.length; i++) {
			terms[i] = new LocateReportMap();
			// Object[] obj = (Object[]) resultset.get(i);
			TUserLocateReport obj = (TUserLocateReport) resultset.get(i);
			terms[i].setRpTerminalNumber((String) obj.getRpterminalid()); // 终端编号 获取当前处于工作状态中的巡检终端编号
			terms[i].setRpUserNumber(String.valueOf(obj.getRpuserid())); // 巡检员id 编号
			terms[i].setRpUserId(String.valueOf(obj.getId()));
			terms[i].setRpPosX(obj.getRpposx());
			terms[i].setRpPosY(obj.getRpposy());
			terms[i].setRpRepoetTime(obj.getRptime());

			// 获取巡检员信息
			TInspectUser iuser = baseDao.get(TInspectUser.class, obj.getRpuserid());
			if (iuser != null) {
				terms[i].setRpUserName(iuser.getIuname()); // 巡检员名称
				terms[i].setRpUserMobile(iuser.getIumobile());// 巡检员手机号
				terms[i].setRpUserId1(String.valueOf(iuser.getId()));
				terms[i].setRpGroupName(iuser.getTgroup().getGname());
			}

			TLine line = baseDao.get(TLine.class, obj.getRplineid());
			if (line != null) {
				terms[i].setRpLineName(line.getLname());
			}
			termMap.put(terms[i].getRpUserId(), terms[i]); // 保存巡检巡检终端编号
		}
		return termMap;
	}

	/**
	 * 人员监控查询轨迹
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<String> showTermUserLocate(SearchLocateMap userlocatereport) {
		List<String> datas = new ArrayList<String>();
		datas.add(userlocatereport.getIuserName());
		if (userlocatereport.getIuserName() == null)
			return datas;
		StringBuffer buf = new StringBuffer("select distinct(CONCAT(rpposx, rpposy)) , rpposx, rpposy, id from t_user_locate_report  where 1=1 ");
		if (!StringUtils.isEmpty(userlocatereport.getIuserName())) {
			buf.append(" and rpuserid =").append(userlocatereport.getIuserName());
		}
		if (!StringUtils.isEmpty(userlocatereport.getStartDate())) {
			String beginDate = userlocatereport.getStartDate();
			buf.append(" and rptime like '%").append(beginDate).append("%'");
		} else {
			Date date = new Date();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			buf.append(" and rptime like '%").append(simpleDateFormat.format(date)).append("%'");
		}

		if (!StringUtils.isEmpty(userlocatereport.getTerminatenumber())) {
			buf.append(" and rpterminalid ='").append(userlocatereport.getTerminatenumber()).append("'");
		}
		//buf.append(" and (id LIKE '%00' OR id LIKE '%50') ");
		buf.append(" order by rptime desc");

		// System.out.println("buf=="+buf.toString());

		SQLQuery sqlQuery = this.baseDao.getHibernaSession().createSQLQuery(buf.toString());
		List resultSet = sqlQuery.list();
		int first = 0;
		if (resultSet != null && resultSet.size() > 0) {
			for (int i = resultSet.size() - 1; i >= 0; i--) {
				try {
					Object[] result = (Object[]) resultSet.get(i);
					// 过滤掉GPS上报时没有经纬度的数据
					if (result[1] == null || (Double) result[1] < 1) {
						continue;
					}
					String id = result[3].toString();
					// 为了放置在地图上显示密集，数据量巨大，导致加载失败，顾当id后两位是00或50时保存一次
					if(first==0){
						datas.add(result[1].toString());
						datas.add(result[2].toString());
						first++;
						continue;
					}
					
					else if ((first!= 0) && (first%10==0)) {
						datas.add(result[1].toString());
						datas.add(result[2].toString());
						first++;
					}
					else{
						first++;
					}

				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}

			}
		}
		return datas;
	}

	public Map<String, Object> findEquimentDatagridByPid(InspectItemReportVo imessageVo, String qsql) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer buf = new StringBuffer(qsql);
		if (imessageVo.getXpid() != 0) {
			buf.append(" and id= ").append(imessageVo.getXpid());
		}
		QueryResult<TPoint> queryResult = baseDao.getQueryResultNotPage(TPoint.class, buf.toString(), null, null);
		List<InspectItemReportVo> evolist = new ArrayList<InspectItemReportVo>();
		if (queryResult != null && queryResult.getResultList().size() > 0) {
			for (TPoint te : queryResult.getResultList()) {
				Set<TPointEquipment> pointequipmentlist = te.getPointequipments();
				if (pointequipmentlist != null && pointequipmentlist.size() > 0) {
					for (TPointEquipment a : pointequipmentlist) {
						InspectItemReportVo evo = new InspectItemReportVo();
						evo.setXeid(a.getTequipment().getId());
						evo.setXename(a.getTequipment().getEname());
						evo.setXenumber(a.getTequipment().getEnumber());
						evolist.add(evo);
					}
				}
				// evo.setXstatusname(te.getXstatus().equals("0")?"正常":"告警");
				// evolist.add(evo);
			}
		}
		map.put("total", queryResult.getTotalRecord());
		map.put("rows", evolist);
		return map;
	}

	public List<TInspectItemReport> getMessageByEnumberList(InspectItemReportVo msgVo) {
		StringBuffer buf = new StringBuffer("from TInspectItemReport where 1=1");
		if (!StringUtils.isEmpty(msgVo.getRpsdate())) {
			buf.append(" and xreptime >='").append(msgVo.getRpsdate()).append("'");
		}
		if (!StringUtils.isEmpty(msgVo.getRpedate())) {
			buf.append(" and xreptime <='").append(msgVo.getRpedate()).append("'");
		}
		if (msgVo.getXuid() != 0) {
			buf.append(" and xuid =").append(msgVo.getXuid());
		}
		if (!StringUtils.isEmpty(msgVo.getXequtnum())) {
			buf.append(" and xequtnum ='").append(msgVo.getXequtnum()).append("'");
		}
		List<TInspectItemReport> list = baseDao.findByHqlAll(TInspectItemReport.class, buf.toString());
		return list;

	}

	public List<InspectItemReportVo> getMessageByEnumber(InspectItemReportVo msgVo) {
		StringBuffer buf = new StringBuffer("from TInspectItemReport where 1=1");
		if (!StringUtils.isEmpty(msgVo.getRpsdate())) {
			buf.append(" and xreptime >='").append(msgVo.getRpsdate()).append("'");
		}
		if (!StringUtils.isEmpty(msgVo.getRpedate())) {
			buf.append(" and xreptime <='").append(msgVo.getRpedate()).append("'");
		}
		if (msgVo.getXuid() != 0) {
			buf.append(" and xuid =").append(msgVo.getXuid());
		}
		if (!StringUtils.isEmpty(msgVo.getXenumber())) {
			buf.append(" and xequtnum ='").append(msgVo.getXenumber()).append("'");
		}
		List<TInspectItemReport> list = baseDao.findByHqlAll(TInspectItemReport.class, buf.toString());
		List<InspectItemReportVo> l = new ArrayList<InspectItemReportVo>();
		if (list != null && list.size() > 0) {
			for (TInspectItemReport tp : list) {
				InspectItemReportVo rvo = new InspectItemReportVo();
				BeanUtils.copyProperties(tp, rvo);
				rvo.setXlname(baseDao.getEntityById(TLine.class, tp.getXlid()).getLname());
				l.add(rvo);
			}
		}
		return l;
	}

	@Override
	public void addTInspectItemDetailReport(TInspectItemDetailReport inspectItemDetailReport) {
		baseDao.save1(inspectItemDetailReport);
	}

	@Override
	public void addTInspectItemReport(TInspectItemReport inspectItemReport) {
		baseDao.save1(inspectItemReport);
	}

	@Override
	public void addTInspectItemRaltionReport(TInspectItemRaltionReport inspectItemRaltionReport) {
		baseDao.save(inspectItemRaltionReport);
	}

	@Override
	public void addTInspectPointReport(TInspectPointReport inspectPointReport) {
		baseDao.save1(inspectPointReport);
	}

	@Override
	public void addTUserLocateReport(TUserLocateReport userLocateReport) {
		baseDao.save(userLocateReport);
	}

	@Override
	public TInspectItemReport getTInspectItemReport(int id) {
		TInspectItemReport tReport = null;
		tReport = baseDao.get(TInspectItemReport.class, id);
		return tReport;
	}

	@Override
	public List<TInspectItemReport> isEquipInspectStatus(int taskid, int equid) {
		List<TInspectItemReport> list = new ArrayList<TInspectItemReport>();
		boolean flag = false;
		String hql = " from TInspectItemReport where xtaskid=" + taskid + "and xequid=" + equid;  //xequid; //baseinfo表中的ID
		long a = 0;
		list = baseDao.find(hql);

		return list;
	}

	@Override
	public TInspectItemReport getEquipInspectStatus(int taskid, int xequid) {
		List<TInspectItemReport> list = null;
		TInspectItemReport equip = new TInspectItemReport();
		String hql = "from TInspectItemReport where xtaskid=" + taskid + "and xequid=" + xequid;
		long a = 0;
		list = baseDao.find(hql);
		if (list != null && list.size() > 0) {
			equip = list.get(0);
		}
		return equip;
	}

	@Override
	public boolean isDetailInspectStatus(int taskid, int xproid) {
		// TODO Auto-generated method stub
		boolean flag = false;
		String hql = "select count(*) from TInspectItemDetailReport where xtaskid=" + taskid + "and xequid=" + xproid;
		long a = 0;
		a = baseDao.count(hql);
		if (a > 0) {
			flag = true;
		}
		return flag;
	}

	@Override
	public TInspectItemDetailReport getDetailInspectBytaskidAndproid(int taskid, int xproid) {
		// TODO Auto-generated method stub
		List<TInspectItemDetailReport> list = null;
		TInspectItemDetailReport detail = new TInspectItemDetailReport();
		String hql = "from TInspectItemDetailReport where xtaskid=" + taskid + "and xproid=" + xproid;
		long a = 0;
		list = baseDao.find(hql);
		if (list != null && list.size() > 0) {
			detail = list.get(0);
		}
		return detail;
	}

	@Override
	public void editEntity(Object entity) {
		baseDao.save(entity);
	}

	@Override
	public boolean editEntity1(Object entity) {
		boolean flag = false;
		try {
			baseDao.save(entity);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			flag = false;
		}
		return flag;
	}

	@Override
	public void editEntity(TInspectItemReport entity) {
		baseDao.saveOrUpdate(entity);
	}

	@Override
	public void editEntity(Object entity, List list) {
		try {
			baseDao.save(entity);
			if (list != null && list.size() > 0) {
				for (int j = 0; j < list.size(); j++) {
					baseDao.save(list.get(j));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}

	@Override
	public List<TBaseInfo> findEquipBybregion(String bregion) {
		List<TBaseInfo> list = baseDao.find("from TBaseInfo where bregion=" + bregion);
		return list;
	}

	@Override
	public int isComplete(int psize, int baseid, int taskid) {
		// TODO Auto-generated method stub
		TInspectItemReport me = (TInspectItemReport) baseDao.find1("from TInspectItemReport where xequid=" + baseid + " and xtaskid=" + taskid);
		if (me != null) {
			int meid = me.getId();
			List<TInspectItemDetailReport> list = baseDao.find(" from TInspectItemDetailReport where msgid=" + meid + "group by xproid");
			if (list != null) {
				if (psize == list.size()) {
					me.setLastmonthflag(1);
					baseDao.save(me);
				}
			}

		}
		return 0;
	}
}
