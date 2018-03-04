package com.inspect.service.impl;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.annotation.Resource;

import org.apache.tools.ant.filters.TokenFilter.Trim;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.inspect.dao.BaseDaoI;
import com.inspect.model.baseinfo.TBaseInfo;
import com.inspect.model.baseinfo.TBaseInfoEquipment;
import com.inspect.model.basis.TEnumParameter;
import com.inspect.model.basis.TEquipment;
import com.inspect.model.basis.TEquipmentProjectGroup;
import com.inspect.model.basis.TLine;
import com.inspect.model.basis.TLinePoint;
import com.inspect.model.basis.TPlan;
import com.inspect.model.basis.TPlanTask;
import com.inspect.model.basis.TPoint;
import com.inspect.model.basis.TPointEquipment;
import com.inspect.model.basis.TProject;
import com.inspect.model.basis.TProjectGroup;
import com.inspect.model.basis.TRfid;
import com.inspect.model.basis.TStation;
import com.inspect.model.basis.TTwoDimensionCode;
import com.inspect.model.monitor.TInspectItemDetailReport;
import com.inspect.model.monitor.TInspectItemRaltionReport;
import com.inspect.model.monitor.TInspectItemReport;
import com.inspect.model.monitor.TInspectPointReport;
import com.inspect.model.problem.TInspectProblem;
import com.inspect.model.system.Enterprise;
import com.inspect.model.system.Forms;
import com.inspect.service.InspectItemServiceI;
import com.inspect.service.InspectMonitorServiceI;
import com.inspect.util.common.QueryResult;
import com.inspect.util.common.StringUtils;
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
import com.inspect.vo.basis.TermProjectGroupVo;
import com.inspect.vo.basis.TwoDimensionCodeVo;
import com.inspect.vo.basis.UnicomVo;
import com.inspect.vo.summary.EquipmentSummaryVo;
import com.inspect.vo.summary.LineSummaryVo;
import com.inspect.vo.summary.PointSummaryVo;

@Service("inspectItemService")
@Transactional(propagation = Propagation.REQUIRED,readOnly = false,rollbackFor = Exception.class)
public class InspectItemServiceImpl implements InspectItemServiceI {
	@Resource
	private BaseDaoI baseDao;
	@Resource
	private  InspectMonitorServiceI inspectMonitorServiceI;
	
	public InspectMonitorServiceI getInspectMonitorServiceI() {
		return inspectMonitorServiceI;
	}

	public void setInspectMonitorServiceI(
			InspectMonitorServiceI inspectMonitorServiceI) {
		this.inspectMonitorServiceI = inspectMonitorServiceI;
	}

	public BaseDaoI getBaseDao() {
		return baseDao;
	}

	public void setBaseDao(BaseDaoI baseDao) {
		this.baseDao = baseDao;
	}
	@Override
	public <T> T getEntityById(Class<T> paramClass, Serializable id) {
		return baseDao.getEntityById(paramClass, id);
	}
	
	@Override
	public void addProject(ProjectVo projectvo){
		TProject project=new TProject();
	    BeanUtils.copyProperties(projectvo,project);
	    project.setEntid(projectvo.getEntid());
	    project.setTprojectgroup(baseDao.getEntityById(TProjectGroup.class,projectvo.getPgroupid()));
	    baseDao.save(project);
	}
	
	@Override
	public void editProject(ProjectVo projectvo) {
		TProject project=baseDao.get(TProject.class,projectvo.getId());
	    BeanUtils.copyProperties(projectvo,project,new String[]{"id","entid"});
	    if(projectvo.getPtype().equals("0")){
	    	project.setPenumvalue("");
	    }
	    if(projectvo.getPtype().equals("1")){
	    	project.setPminvalue("");
	    	project.setPmaxvalue("");
	    }
	    if(projectvo.getPtype().equals("2")){
	    	project.setPenumvalue("");
	    	project.setPminvalue("");
	    	project.setPmaxvalue("");
	    }
	    project.setTprojectgroup(baseDao.getEntityById(TProjectGroup.class,projectvo.getPgroupid()));
	    baseDao.update(project);
	}

	@Override
	public TProject getProject(String id) {
		if(StringUtils.isEmpty(String.valueOf(id))){
			return null;
		}
		return baseDao.get(TProject.class,Integer.parseInt(id));
	}

	@Override
	public void removeProject(String ids) {
		if(!StringUtils.isEmpty(ids)){
			for(String id : ids.split(",")){
				baseDao.delete(TProject.class,Integer.parseInt(id));
			}
		}
	}
	
	@Override
	public Map<String, Object> findProjectDatagrid(ProjectVo projectvo,int page, int rows, String qsql) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer buf = new StringBuffer(qsql);
		System.out.println(" findProjectDatagrid: " + buf);
		
		if (!StringUtils.isEmpty(projectvo.getPname())) {
			buf.append(" and pname like '%").append(projectvo.getPname()).append("%'");
		}
		if (!StringUtils.isEmpty(projectvo.getPtype())) {
			buf.append(" and ptype ='").append(projectvo.getPtype()).append("'");
		}
		if (projectvo.getPgroupid()!=0) {
			buf.append(" and tprojectgroup.id =").append(projectvo.getPgroupid());
		}
		if (projectvo.getEntid()!=-1) {
			buf.append(" and entid=").append(projectvo.getEntid());
		}
		buf.append(" order by id desc ");
		QueryResult<TProject> queryResult = baseDao.getQueryResult(TProject.class, buf.toString(), (page - 1) * rows, rows,null, null);
		List<ProjectVo> pvolist=new ArrayList<ProjectVo>();
		if (queryResult != null && queryResult.getResultList().size() > 0) {
			for (TProject te : queryResult.getResultList()) {
				ProjectVo pvo = new ProjectVo();
				BeanUtils.copyProperties(te,pvo,new String[]{"equipmentprojects"});
				if(te.getTprojectgroup()!=null){
					
					pvo.setPgroupid(te.getTprojectgroup().getId());
					pvo.setPgroupname(te.getTprojectgroup().getPgname());
					pvo.setPcomname(baseDao.get(Enterprise.class, pvo.getEntid())!=null?baseDao.get(Enterprise.class, pvo.getEntid()).getEntname():"");
			
//					private String pmaxvalue;// 最大值
//					private String pminvalue;// 最小值
					pvo.setPmaxvalue(te.getPmaxvalue());
					pvo.setPminvalue(te.getPminvalue());
				}
				pvolist.add(pvo);
			}
		}
		map.put("total", queryResult.getTotalRecord());
		map.put("rows", pvolist);
		return map;
	}

	@Override
	public void addEquipment(EquipmentVo equipmentvo) {
		TEquipment equipment=new TEquipment();
//	    BeanUtils.copyProperties(equipmentvo,equipment);
	    TBaseInfo tbf=baseDao.getEntityById(TBaseInfo.class,Integer.parseInt(equipmentvo.getEpid()));
	    if(tbf!=null){
	    	equipment.setEnumber(tbf.getBnumber());//设备编号
	    	equipment.setEname(tbf.getBname());//设备名称
	    	equipment.setEposx(tbf.getBposx());//经度
	    	equipment.setEposy(tbf.getBposy());//纬度
	    	equipment.setEaddress(tbf.getBaddress());//设备地址
	    	equipment.setEtype(tbf.getBtype());//设备类型
	    	equipment.setEcity(tbf.getBcity());//设备市区
	    	equipment.setEregion(tbf.getBregion());//设备县区
	    }
	    equipment.setEntid(equipmentvo.getEntid());
	    baseDao.save(equipment);
	    addBaseInfoEquipment(tbf,equipment);
	    if (equipmentvo.getEpids() != null&&equipmentvo.getEpids().length()>0) {
	    	 this.addEquipmentProject(equipmentvo, equipment);
	    }
	    
	}
	
	
	@Override
	public void addForms(FormsVo formsvo) {
		Forms forms=new Forms();
//	    BeanUtils.copyProperties(equipmentvo,equipment);
	    Forms f=baseDao.getEntityById(Forms.class,Integer.parseInt(formsvo.getLicense()));
	    if(f!=null){
	    	forms.setOperator(f.getOperator());
	    	forms.setLicense(f.getLicense());
	    	forms.setDatasheet(f.getDatasheet());
	    	forms.setBaseid(f.getBaseid());
	    	forms.setBasename(f.getBasename());
	    	forms.setAdministrativedivision(f.getAdministrativedivision());
	    	forms.setAddress(f.getAddress());
	    	forms.setRadius(f.getRadius());
	    	forms.setLongitute(f.getLongitute());
	    	forms.setLatitude(f.getLatitude());
	    	forms.setCityx(f.getCityx());
	    	forms.setCityy(f.getCityy());
	    	forms.setHigh(f.getHigh());
	    	forms.setStartdate(f.getStartdate());
	    	forms.setRecognitionbookid(f.getRecognitionbookid());
	    	forms.setNetwork1(f.getNetwork1());
	    	forms.setNetwork1sectorid(f.getNetwork1sectorid());
	    	forms.setNetwork1sectornum(f.getNetwork1sectornum());
	    	forms.setNetwork1angle(f.getNetwork1angle());
	    	forms.setNetwork1identificationcode(f.getNetwork1identificationcode());
	    	forms.setNetwork1receiveangle(f.getNetwork1receiveangle());
	    	forms.setNetwork1sendangle(f.getNetwork1sendangle());
	    	forms.setNetwork1loss(f.getNetwork1loss());
	    	forms.setNetwork1sendfreq(f.getNetwork1sendfreq());
	    	forms.setNetwork1receivefreq(f.getNetwork1receivefreq());
	    	forms.setNetwork1equipmentmodel(f.getNetwork1equipmentmodel());
	    	forms.setNetwork1cmiitid(f.getNetwork1cmiitid());
	    	forms.setNetwork1equipmentfactory(f.getNetwork1equipmentfactory());
	    	forms.setNetwork1equipmentnum(f.getNetwork1equipmentnum());
	    	forms.setNetwork1transmitpower(f.getNetwork1transmitpower());
	    	forms.setNetwork1powerunit(f.getNetwork1powerunit());
	    	forms.setNetwork1antennatype(f.getNetwork1antennatype());
	    	forms.setNetwork1antennamodel(f.getNetwork1antennamodel());
	    	forms.setNetwork1polarization(f.getNetwork1polarization());
	    	forms.setNetwork13db(f.getNetwork13db());
	    	forms.setNetwork1dbi(f.getNetwork1dbi());
	    	forms.setNetwork1dbiunit(f.getNetwork1dbiunit());
	    	forms.setNetwork1antennafactory(f.getNetwork1antennafactory());
	    	forms.setNetwork1antennahigh(f.getNetwork1antennahigh());
	    	forms.setNetwork1startdate(f.getNetwork1startdate());
	    	forms.setNetwork2(f.getNetwork2());
	    	forms.setNetwork2sectorid(f.getNetwork2sectorid());
	    	forms.setNetwork2sectornum(f.getNetwork2sectornum());
	    	forms.setNetwork2angle(f.getNetwork2angle());
	    	forms.setNetwork2identificationcode(f.getNetwork2identificationcode());
	    	forms.setNetwork2receiveangle(f.getNetwork2receiveangle());
	    	forms.setNetwork2sendangle(f.getNetwork2sendangle());
	    	forms.setNetwork2loss(f.getNetwork2loss());
	    	forms.setNetwork2sendfreq(f.getNetwork2sendfreq());
	    	forms.setNetwork2receivefreq(f.getNetwork2receivefreq());
	    	forms.setNetwork2equipmentmodel(f.getNetwork2equipmentmodel());
	    	forms.setNetwork2cmiitid(f.getNetwork2cmiitid());
	    	forms.setNetwork2equipmentfactory(f.getNetwork2equipmentfactory());
	    	forms.setNetwork2equipmentnum(f.getNetwork2equipmentnum());
	    	forms.setNetwork2transmitpower(f.getNetwork2transmitpower());
	    	forms.setNetwork2powerunit(f.getNetwork2powerunit());
	    	forms.setNetwork2antennatype(f.getNetwork2antennatype());
	    	forms.setNetwork2antennamodel(f.getNetwork2antennamodel());
	    	forms.setNetwork2polarization(f.getNetwork2polarization());
	    	forms.setNetwork23db(f.getNetwork23db());
	    	forms.setNetwork2dbi(f.getNetwork2dbi());
	    	forms.setNetwork2dbiunit(f.getNetwork2dbiunit());
	    	forms.setNetwork2antennafactory(f.getNetwork2antennafactory());
	    	forms.setNetwork2antennahigh(f.getNetwork2antennahigh());
	    	forms.setNetwork2startdate(f.getNetwork2startdate());
	    	forms.setNetwork3(f.getNetwork3());
	    	forms.setNetwork3sectorid(f.getNetwork3sectorid());
	    	forms.setNetwork3angle(f.getNetwork3angle());
	    	forms.setNetwork3identificationcode(f.getNetwork3identificationcode());
	    	forms.setNetwork3loss(f.getNetwork3loss());
	    	forms.setNetwork3sendfreq(f.getNetwork3sendfreq());
	    	forms.setNetwork3receivefreq(f.getNetwork3receivefreq());
	    	forms.setNetwork3equipmentmodel(f.getNetwork3equipmentmodel());
	    	forms.setNetwork3cmiitid(f.getNetwork3cmiitid());
	    	forms.setNetwork3equipmentfactory(f.getNetwork3equipmentfactory());
	    	forms.setNetwork3transmitpower(f.getNetwork3transmitpower());
	    	forms.setNetwork3powerunit(f.getNetwork3powerunit());
	    	forms.setNetwork3antennatype(f.getNetwork3antennatype());
	    	forms.setNetwork3antennamodel(f.getNetwork3antennamodel());
	    	forms.setNetwork3polarization(f.getNetwork3polarization());
	    	forms.setNetwork3dbi(f.getNetwork3dbi());
	    	forms.setNetwork3dbiunit(f.getNetwork3dbiunit());
	    	forms.setNetwork3antennafactory(f.getNetwork3antennafactory());
	    	forms.setNetwork3antennahigh(f.getNetwork3antennahigh());
	    	forms.setNetwork3startdate(f.getNetwork3startdate());
	    	forms.setNetwork4(f.getNetwork4());
	    	forms.setNetwork4sectorid(f.getNetwork4sectorid());
	    	forms.setNetwork4sectornum(f.getNetwork4sectornum());
	    	forms.setNetwork4angle(f.getNetwork4angle());
	    	forms.setNetwork4identificationcode(f.getNetwork4identificationcode());
	    	forms.setNetwork4receiveangle(f.getNetwork4receiveangle());
	    	forms.setNetwork4sendangle(f.getNetwork4sendangle());
	    	forms.setNetwork4loss(f.getNetwork4loss());
	    	forms.setNetwork4sendfreq(f.getNetwork4sendfreq());
	    	forms.setNetwork4receivefreq(f.getNetwork4receivefreq());
	    	forms.setNetwork4equipmentmodel(f.getNetwork4equipmentmodel());
	    	forms.setNetwork4cmiitid(f.getNetwork4cmiitid());
	    	forms.setNetwork4equipmentfactory(f.getNetwork4equipmentfactory());
	    	forms.setNetwork4equipmentnum(f.getNetwork4equipmentnum());
	    	forms.setNetwork4transmitpower(f.getNetwork4transmitpower());
	    	forms.setNetwork4powerunit(f.getNetwork4powerunit());
	    	forms.setNetwork4antennatype(f.getNetwork4antennatype());
	    	forms.setNetwork4antennamodel(f.getNetwork4antennamodel());
	    	forms.setNetwork4polarization(f.getNetwork4polarization());
	    	forms.setNetwork43db(f.getNetwork43db());
	    	forms.setNetwork4dbi(f.getNetwork4dbi());
	    	forms.setNetwork4dbiunit(f.getNetwork4dbiunit());
	    	forms.setNetwork4antennafactory(f.getNetwork4antennafactory());
	    	forms.setNetwork4antennahigh(f.getNetwork4antennahigh());
	    	forms.setNetwork4startdate(f.getNetwork4startdate());
	    	forms.setPrintdate(f.getPrintdate());
	    	forms.setValidity(f.getValidity());
	    	forms.setRemark(f.getRemark());

	    }
	    baseDao.save(forms);
	   
	    
	}
	
	
	/**
	 * 保存设备与巡检项组的关联关系
	 * @param equipmentvo 
	 * @param equipment
	 */
	private void addEquipmentProject(EquipmentVo equipmentvo,TEquipment equipment){
		baseDao.executeHql("delete TEquipmentProjectGroup t where t.tequipment = ?", new Object[] { equipment });
		if (equipmentvo.getEpids() != null) {
			for (String id : equipmentvo.getEpids().split(",")) {
				TEquipmentProjectGroup tep = new TEquipmentProjectGroup();
				tep.setTequipment(equipment);
				tep.setTprojectgroup(baseDao.get(TProjectGroup.class,Integer.parseInt(id.trim())));
				tep.setEntid(equipment.getEntid());
				baseDao.save(tep);
			}
		}
	}
	
	/**在导入设备巡检项时用到
	 * 保存设备与巡检项组的关联关系
	 * @param equipment
	 * 1 表示保存 0表示为保存
	 */
	private int addEquipmentProject(TEquipment equipment,TProjectGroup pGroup){
		int flag=0;
		TEquipmentProjectGroup tep = new TEquipmentProjectGroup();
		tep.setTequipment(equipment);
			tep.setTprojectgroup(baseDao.get(TProjectGroup.class,pGroup.getId()));
			tep.setEntid(equipment.getEntid());
			String hql1="select count(*) from  TEquipmentProjectGroup t where t.tequipment ="+equipment.getId()+" and t.tprojectgroup="+pGroup.getId()+" and entid="+equipment.getEntid();
			long count=baseDao.count(hql1);
			if(count>0){
				flag=0;
			}
			else{
			flag=1;
			baseDao.save(tep);
			}
	  return flag;
	}
	/**
	 * 保存设备与基础信息的关联关系
	 * @param baseinfo
	 * @param equipment
	 */
	private void addBaseInfoEquipment(TBaseInfo baseinfo,TEquipment equipment){
		TBaseInfoEquipment tbe=new TBaseInfoEquipment();
		tbe.setTbaseinfo(baseinfo);
		tbe.setTequipment(equipment);
		tbe.setEntid(equipment.getEntid());
		baseDao.save(tbe);
	}

	@Override
	public void editEquipment(EquipmentVo equipmentvo) {
		TEquipment equipment=baseDao.get(TEquipment.class,equipmentvo.getId());
//	    BeanUtils.copyProperties(equipmentvo,equipment,new String[]{"id","entid"});
//	    baseDao.update(equipment);
	   // if (equipmentvo.getEpids() != null&&equipmentvo.getEpids().length()>0) {
	    	 this.addEquipmentProject(equipmentvo, equipment);
	    //}
	}

	@Override
	public TEquipment getEquipment(String id) {
		if(StringUtils.isEmpty(String.valueOf(id))){
			return null;
		}
		return baseDao.get(TEquipment.class,Integer.parseInt(id));
	}

	
	public Forms getForms(String id) {
		if(StringUtils.isEmpty(String.valueOf(id))){
			return null;
		}
		return baseDao.get(Forms.class,Integer.parseInt(id));
	}
	
	
	@Override
	public void removeEquipment(String ids) {
		if(!StringUtils.isEmpty(ids)){
			for(String id : ids.split(",")){
				TEquipment e =baseDao.get(TEquipment.class,Integer.parseInt(id.trim()));
				if (e != null) {
					//先删除设备与巡检项关联关系
					baseDao.executeHql("delete from TEquipmentProject t where t.tequipment = ?", new Object[] { e });
					//删除设备与基础信息的关系
					baseDao.delete(TEquipment.class,Integer.parseInt(id));
				}
			}
		}
	}
	
	@Override
	public Map<String, Object> findEquipmentDatagrid(EquipmentVo equipmentvo,int page, int rows, String qsql) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer buf = new StringBuffer(qsql);  //1=1 or  entid in (0,").append(entid).append(")");
		if (!StringUtils.isEmpty(equipmentvo.getEname())) {
			buf.append(" and ename like '%").append(equipmentvo.getEname().trim()).append("%'");
		}
		if (!StringUtils.isEmpty(equipmentvo.getEnumber())) {
			buf.append(" and enumber like '%").append(equipmentvo.getEnumber().trim()).append("%'");
		}
		if (!StringUtils.isEmpty(equipmentvo.getEtype())) {
			buf.append(" and etype  ='").append(equipmentvo.getEtype().trim()).append("'");
		}
		if (equipmentvo.getEntid()!=0) {
			buf.append(" and entid=").append(equipmentvo.getEntid());
		}
		buf.append(" order by id desc ");
		QueryResult<TEquipment> queryResult = baseDao.getQueryResult(TEquipment.class, buf.toString(), (page - 1) * rows, rows,null, null);
		List<EquipmentVo> evolist=new ArrayList<EquipmentVo>();
		if (queryResult != null && queryResult.getResultList().size() > 0) {
			for (TEquipment te : queryResult.getResultList()) {
				EquipmentVo evo = new EquipmentVo();
				BeanUtils.copyProperties(te,evo);
				
				Set<TEquipmentProjectGroup> equipmentprojects = te.getEquipmentprojectgroups();
				String epIds = "";
				String epNames = "";
				boolean b = false;
				//tprojectgroup;//巡检项组信息
				if (equipmentprojects != null && equipmentprojects.size() > 0) {
					for (TEquipmentProjectGroup equipmentproject : equipmentprojects) {
						if (equipmentproject.getTprojectgroup() != null) {
							if (b) {
								epIds += ",";
								epNames += ",";
							}
							epIds += equipmentproject.getTprojectgroup().getId();
							epNames += equipmentproject.getTprojectgroup().getPgname();
							b = true;
						}
					}
				}
				evo.setEpids(epIds);
				evo.setEpnames(epNames);
				evolist.add(evo);
			}
		}
		map.put("total", queryResult.getTotalRecord());
		map.put("rows", evolist);
		return map;
	}
	@Override
	public Map<String, Object> findEquipmentInfoDatagrid(EquipmentVo equipmentvo,int page, int rows, String qsql) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer buf = new StringBuffer(qsql);
		if (equipmentvo.getEntid()!=0) {
			buf.append(" and entid=").append(equipmentvo.getEntid());
		}
		QueryResult<TEquipment> queryResult = baseDao.getQueryResult(TEquipment.class, buf.toString(),(page - 1) * rows, rows,null, null);
		List<EquipmentVo> evolist=new ArrayList<EquipmentVo>();
		if (queryResult != null && queryResult.getResultList().size() > 0) {
			for (TEquipment te : queryResult.getResultList()) {
				EquipmentVo evo = new EquipmentVo();
				BeanUtils.copyProperties(te,evo);
				evolist.add(evo);
			}
		}
		map.put("total", queryResult.getTotalRecord());
		map.put("rows", evolist);
		return map;
	}
	@Override
	public Map<String, Object> findEquipmentInfoDatagrid(EquipmentVo equipmentvo, String qsql) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer buf = new StringBuffer(qsql);
		if (equipmentvo.getEntid()!=0) {
			buf.append(" and entid=").append(equipmentvo.getEntid());
		}
		QueryResult<TEquipment> queryResult = baseDao.getQueryResultNotPage(TEquipment.class, buf.toString(),null, null);
		List<EquipmentVo> evolist=new ArrayList<EquipmentVo>();
		if (queryResult != null && queryResult.getResultList().size() > 0) {
			for (TEquipment te : queryResult.getResultList()) {
				EquipmentVo evo = new EquipmentVo();
				BeanUtils.copyProperties(te,evo);
				evolist.add(evo);
			}
		}
		map.put("total", queryResult.getTotalRecord());
		map.put("rows", evolist);
		return map;
	}
	
	@SuppressWarnings("unchecked")
	public List<TProject> getProjectList(int entid){
		String hql="select id,pname from t_project where entid="+entid;
		List<TProject> list=baseDao.getJdbcTemplate().queryForList(hql);
		return list;
	}
	@Override
	public List<TProject> getProjectList(int entid,String eids,String qtype) {
		List<TProject> list=new ArrayList<TProject>();
		String hql="from TProject where entid='"+entid+"'";
		if(qtype.equals("u")){//修改
			if(eids!="" && !eids.equals("")){
				hql+=" and id not in ("+eids +" )";
			}
		}
		hql+=" order by id asc";
		List<TProject> l=baseDao.findByHqlAll(TProject.class, hql);
		if(l!=null && l.size()>0){
			for(TProject a:l){
				TProject einfo=new TProject();
				einfo.setId(a.getId());
				einfo.setPname(a.getPname());
				list.add(einfo);
			}
		}
		return list;
	}
	@Override
	// 巡检项组信息实体类
	// pgname;//巡检项组名称
	
	public List<TProjectGroup> getProjectGroupList(int entid,String eids,String qtype) {
		List<TProjectGroup> list=new ArrayList<TProjectGroup>();
		String hql="from TProjectGroup where entid in(0,"+entid+")";
		if(qtype.equals("u")){//修改
			if(eids!="" && !eids.equals("")){
				hql+=" and id not in ("+eids +" )";
			}
		}
		hql+=" order by id asc";
		List<TProjectGroup> l=baseDao.findByHqlAll(TProjectGroup.class, hql);
		if(l!=null && l.size()>0){
			for(TProjectGroup a:l){
				TProjectGroup einfo=new TProjectGroup();
				einfo.setId(a.getId());
				einfo.setPgname(a.getPgname());
				list.add(einfo);
			}
		}
		return list;
	}

	@Override
	public void addPoint(PointVo pointvo) {
		TPoint point=new TPoint();
	    BeanUtils.copyProperties(pointvo,point);
	    point.setEntid(pointvo.getEntid());
	    baseDao.save(point);
	    this.addPointEquipment(pointvo, point);
	}
	
	public void addPoint1(TPoint point) {
	    baseDao.save(point);
	}
	public void addLine1(TLine line) {
	    baseDao.save(line);
	}
	/**
	 * 保存巡检点与巡检设备的关联关系
	 * @param pointvo 
	 * @param point
	 */
	private void addPointEquipment(PointVo pointvo,TPoint point){
		baseDao.executeHql("delete from TPointEquipment t where t.tpoint = ?", new Object[] { point });
		if (pointvo.getPoeIds() != null) {
			for (String id : pointvo.getPoeIds() .split(",")) {
				TPointEquipment tpe = new TPointEquipment();
				tpe.setTpoint(point);
				tpe.setTequipment(baseDao.get(TEquipment.class,Integer.parseInt(id.trim())));
				Long count=baseDao.count("select count(*) from TPointEquipment where tpoint="+point.getId()+" and tequipment="+id);
				if(count==0){
					baseDao.save(tpe);
				}
			}
		}
	}
	
	/**在导入巡检点时用到
	 * 保存巡检点与巡检设备的关联关系
	 * 1表示保存 0表示为保存
	 */
	private int addPointEquipment(TPoint point,TEquipment equip){
		int flag=0;
		TPointEquipment pEquipment=new TPointEquipment();
		pEquipment.setTpoint(point);
		pEquipment.setTequipment(equip);
		long count=0;
		try {
			//String hql1="select count(*) from  TEquipmentProjectGroup t where t.tequipment ="+equipment.getId()+" and t.tprojectgroup="+pGroup.getId()+"and entid="+equipment.getEntid();
			String hql1="select count(*) from  TPointEquipment t where t.tequipment ="+equip.getId()+" and t.tpoint="+point.getId();
			count=baseDao.count(hql1);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	if(count>0){
			flag=0;
		}
		else{
		flag=1;
		baseDao.save(pEquipment);
		}
		
	  return flag;
	}
	
	
	/**在导入巡检线路时用到
	 * 保存巡检点与巡检线路的关联关系
	 * 1表示保存 0表示为保存
	 */
	private int addLinePoint(TLine line,TPoint point){
		int flag=0;
		TLinePoint linePoint=new TLinePoint();
		linePoint.setTpoint(point);
		linePoint.setTline(line);
		long count=0;
		try {
			String hql1="select count(*) from  TLinePoint t where t.tline ="+line.getId()+" and t.tpoint="+point.getId();
			 count=baseDao.count(hql1);
			// System.out.println(point.getPoname());
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		if(count>0){
			flag=0;
		}
		else{
		flag=1;
		baseDao.save(linePoint);
		}
		
	  return flag;
	}
	@Override
	public void editPoint(PointVo pointvo) {
		TPoint point=baseDao.get(TPoint.class,pointvo.getId());
	    BeanUtils.copyProperties(pointvo,point,new String[]{"id","entid"});
	    baseDao.update(point);
	    this.addPointEquipment(pointvo, point);
	}

	@Override
	public TPoint getPoint(String id) {
		if(StringUtils.isEmpty(String.valueOf(id))){
			return null;
		}
		return baseDao.get(TPoint.class,Integer.parseInt(id));
	}

	@Override
	public void removePoint(String ids) {
		if(!StringUtils.isEmpty(ids)){
			for(String id : ids.split(",")){
				TPoint p =baseDao.get(TPoint.class,Integer.parseInt(id.trim()));
				if (p != null) {
					//先删除巡检点与设备关联关系
					baseDao.executeHql("delete from TPointEquipment t where t.tpoint = ?", new Object[] { p });
					baseDao.delete(TPoint.class,Integer.parseInt(id));
				}
			}
		}
	}
	
	@Override
	public Map<String, Object> findPointDatagrid(PointVo pointvo, int page,int rows, String entid) {
		Map<String, Object> map = new HashMap<String, Object>();

		StringBuffer buf = new StringBuffer();
		buf.append(" SELECT b.id,GROUP_CONCAT(CAST(c.ename AS CHAR)),a.pointid ,b.poname "+
					" FROM t_point b   LEFT JOIN        t_point_equipment  a   ON (a.pointid=b.id )      LEFT JOIN  t_equipment c    ON (a.equipmentid=c.id )") ;
		 
		if("0".equals(entid)){
			buf.append(" where 1=1");
		}else{
			buf.append(" where  b.entid in (0,").append(entid).append(")");
		}
		if (!StringUtils.isEmpty(pointvo.getPoname())) {
			buf.append(" and b.poname like '%").append(pointvo.getPoname()).append("%'");
		}
		if (!StringUtils.isEmpty(pointvo.getPonumber())) {
			buf.append(" and b.ponumber ='").append(pointvo.getPonumber()).append("'");
		}
		if (pointvo.getEntid()!=0) {
			buf.append(" and b.entid=").append(pointvo.getEntid());
		}
	
		buf.append("  GROUP BY b.id order by b.id desc ");
	//	System.out.println(buf.toString());
		
		String sql=buf.toString()+ "LIMIT "+(page - 1) * rows+","+rows ;
		SQLQuery pgquery = baseDao.getHibernaSession().createSQLQuery(sql);
		List<Object[]> lvolist = pgquery.list();

		List<PointVo> list = toPointObj(lvolist);
		if(list==null){
			list=new ArrayList<PointVo>();
		}
		SQLQuery listsize = baseDao.getHibernaSession().createSQLQuery(buf.toString());
		map.put("total", listsize.list().size());
		map.put("rows", list);
		return map;
		
		/*
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer buf = new StringBuffer(qsql);
		if (!StringUtils.isEmpty(pointvo.getPoname())) {
			buf.append(" and poname like '%").append(pointvo.getPoname()).append("%'");
		}
		if (!StringUtils.isEmpty(pointvo.getPonumber())) {
			buf.append(" and ponumber ='").append(pointvo.getPonumber()).append("'");
		}
		if (pointvo.getEntid()!=0) {
			buf.append(" and entid=").append(pointvo.getEntid());
		}
		buf.append(" order by id desc ");
		QueryResult<TPoint> queryResult = baseDao.getQueryResult(TPoint.class, buf.toString(), (page - 1) * rows, rows,null, null);
		List<PointVo> pvolist=new ArrayList<PointVo>();
		if (queryResult != null && queryResult.getResultList().size() > 0) {
			for (TPoint tp : queryResult.getResultList()) {
				PointVo pvo = new PointVo();
				BeanUtils.copyProperties(tp,pvo);
				Set<TPointEquipment> pointequipments = tp.getPointequipments();
				String poIds = "";
				String poNames = "";
				boolean b = false;
				if (pointequipments != null && pointequipments.size() > 0) {
					for (TPointEquipment pointequipment : pointequipments) {
						if (pointequipment.getTequipment()!= null) {
							if (b) {
								poIds += ",";
								poNames += ",";
							}
							poIds += pointequipment.getTequipment().getId();
							poNames += pointequipment.getTequipment().getEname();
							b = true;
						}
					}
				}
				pvo.setPoeIds(poIds);
				pvo.setPoeNames(poNames);
				pvolist.add(pvo);
			}
		}
		map.put("total", queryResult.getTotalRecord());
		map.put("rows", pvolist);
		return map;
	*/}
	
	private List<PointVo> toPointObj(List<Object[]> lvolist) {

		// TODO Auto-generated method stub
		List<PointVo> list=new ArrayList();
		if (lvolist == null || lvolist.size() == 0){
			return null;
		}

		for(int i=0;i<lvolist.size();i++){
			try {
				PointVo p=new PointVo();
				Object[] obj=(Object[])lvolist.get(i);
	
				
				//设备名称
				if(obj[1]==null){
					p.setPoeNames("");
				}
				else{
					p.setPoeNames((String)obj[1]);
				}
				//巡检点id
				p.setId((Integer)obj[0]);
				//巡检点名称
				if(obj[3]==null){
					p.setPoname("");
				}
				else{
					p.setPoname((String)obj[3]);
				}
				list.add(p);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
		}
		return list;
	
	}

	/**  
	 * 	//   通过设备区县和巡检点名称查找巡检点信息，
	 *         用于巡检线线路的增加和修改
	 */
	@Override
	public List<PointVo> getPointList(String region,String poname, String eids,String qtype,int entid) {
		StringBuffer hql=new StringBuffer("");
		//判断是否有区县
		if(StringUtils.isNotEmpty(region)){
			hql.append("SELECT a.id,a.poname FROM t_point a,t_point_equipment b,t_equipment c WHERE a.id=b.pointid AND b.equipmentid=c.id");
			hql.append(" and c.eregion like '%"+region+"%'");
			
			if(entid!=0){
				hql.append(" and a.entid in(0,"+entid+") and c.entid in(0,"+entid+")");
			}
			else{
				hql.append(" and 1=1 ");
			}
		
		}
		else{
			hql.append("SELECT a.id,a.poname FROM t_point a where ");	
			
			if(entid!=0){
				hql.append(" a.entid in(0,"+entid+")");
			}
			else{
				hql.append(" 1=1 ");
			}
			
		}
		 if(StringUtils.isNotEmpty(poname)){
				hql.append(" and a.poname like '%"+poname.trim()+"%'");
			}
		 hql.append(" group by a.id  ORDER BY a.id ASC");
		//System.out.println(hql.toString());
		 Query query=baseDao.getHibernaSession().createSQLQuery(hql.toString());
		 List<Object[]> polist = query.list();
		 List<PointVo> pVoList=toEquipObj(polist);
		// System.out.println(pVoList.size());
		 return pVoList;
		/*
		StringBuffer hql=new StringBuffer("from TPoint ");
		if(entid!=0){
			hql.append("where entid in(0,"+entid+")");
		}
		else{
			hql.append("where 1=1 ");
		}
		if(qtype.equals("u")){//修改
			if(eids!="" && !eids.equals("")){
				hql.append(" and id not in ("+eids +" )");
			}
		}
		if(StringUtils.isNotEmpty(poname)){
			hql.append(" and poname like '%"+poname.trim()+"%'");
		}
		hql.append("order by id asc");
		List<TPoint> pList=baseDao.findByHqlAll(TPoint.class, hql.toString());
		List<PointVo> pVoList=new ArrayList<PointVo>();
		//巡检点集合
		if(StringUtils.isNotEmpty(region)){
			if(pList!=null&&pList.size()>0){
				for(int i=0;i<pList.size();i++){
					Set<TPointEquipment> pe=pList.get(i).getPointequipments();
					Iterator iter=pe.iterator();
					//找出符合区县条件巡检点
					//之后判断一下巡检点下面没有设备时，是否报错
					while(iter.hasNext()){
						TPointEquipment pe1=(TPointEquipment) iter.next();
						//在巡检点下Equipment中存在对应区县，则将此巡检点加入，然后查询下一个巡检点；
						if(region.equals(pe1.getTequipment().getEregion())){
							PointVo pVo=new PointVo();
							BeanUtils.copyProperties(pList.get(i), pVo);
							pVoList.add(pVo);
							break;
						}
					}
				}
			}
		}
		else{
			if(pList!=null&&pList.size()>0){
				for(int i=0;i<pList.size();i++){
					Set<TPointEquipment> pe=pList.get(i).getPointequipments();
					Iterator iter=pe.iterator();
					//找出符合区县条件巡检点
					//之后判断一下巡检点下面没有设备时，是否报错
					 //此处是不对的，既然不判断区县，那么就不用进入TPointEquipment里面
					while(iter.hasNext()){
						TPointEquipment pe1=(TPointEquipment) iter.next();
							PointVo pVo=new PointVo();
							BeanUtils.copyProperties(pList.get(i), pVo);
							pVoList.add(pVo);
							break;
					}
				}
			}
		}
//南郊259      长青336
		System.out.println(pVoList.size());
		return pVoList;
	*/}
	private List<PointVo> toEquipObj(List<Object[]> polist) {
		List<PointVo> list=new ArrayList();
		  if(polist!=null&&polist.size()>0){
			  for(int i=0;i<polist.size();i++){
				  PointVo pvo=new PointVo();
				  Object[] obj=polist.get(i);
				  pvo.setId((Integer)obj[0]);
				  if(obj[1]==null){
					  pvo.setPoname("");
				  }
				  else{
					  pvo.setPoname((String)obj[1]);
				  }
				  list.add(pvo);
			  }
		  }
		  return list;
	}

	/**   动态级联查询
	 * 	//通过设备区县查找巡检点信息，
	 *         用于巡检线线路的增加和修改
	 */
	@Override
	public List<PointVo> getPointListByRegion(int entid,String region,String eids,String qtype) {
		String hql="from TPoint ";
		if(entid!=0){
			hql+="where entid in(0,"+entid+")";
		}
		else{
			hql+="where 1=1 ";
		}
		if(qtype.equals("u")){//修改
			if(eids!="" && !eids.equals("")){
				hql+=" and id not in ("+eids +" )";
			}
			
		}
		hql+="order by id asc";
		List<TPoint> pList=baseDao.findByHqlAll(TPoint.class, hql);
		List<PointVo> pVoList=new ArrayList<PointVo>();
		//巡检点集合
		if(pList!=null&&pList.size()>0){
			for(int i=0;i<pList.size();i++){
				Set<TPointEquipment> pe=pList.get(i).getPointequipments();
				Iterator iter=pe.iterator();
				//找出符合区县条件巡检点
				//之后判断一下巡检点下面没有设备时，是否报错
				while(iter.hasNext()){
					TPointEquipment pe1=(TPointEquipment) iter.next();
					//在巡检点下Equipment中存在对应区县，则将此巡检点加入，然后查询下一个巡检点；
				//	System.out.println(pe1.getTequipment().getEregion());
					if(region.equals(pe1.getTequipment().getEregion())){
						PointVo pVo=new PointVo();
						BeanUtils.copyProperties(pList.get(i), pVo);
						pVoList.add(pVo);
						break;
					}
				}
			}
		}	
		return pVoList;
	}
	/**  级联查询
	 * //通过设备分类级联查询设备区县
	 * 用于巡检线点的增加
	 */
	@Override
	public List<TEquipment> getRetionList(int entid,String eids,String qtype) {
		List<TEquipment> list=new ArrayList<TEquipment>();
		String hql="from TEquipment ";
		if(entid!=0){
			hql+="where entid in(0,"+entid+")";
		}
		else{
			hql+="where 1=1 ";
		}
		if(qtype.equals("u")){//修改
			if(eids!="" && !eids.equals("")){
				hql+=" and id not in ("+eids +" )";
			}
		}
		hql+="group by eregion  order by id asc";
		List<TEquipment> l=baseDao.findByHqlAll(TEquipment.class, hql);
		if(l!=null && l.size()>0){
			for(TEquipment a:l){
				TEquipment equip=new TEquipment();
				equip.setId(a.getId());
				equip.setEregion(a.getEregion());
				list.add(equip);
			}
		}
		return list;
	}
	/**
	 * 巡检点增加或修改时查询可以设备
	 */
	@Override
	public List<TEquipment> getEquipList(String region,String ename,String eids,String qtype,int entid) {
		String hql="from TEquipment ";
		if(entid!=0){
			hql+="where entid in(0,"+entid+")";
		}
		else{
			hql+="where 1=1 ";
		}
	/*	if(qtype.equals("u")){//修改
			if(eids!="" && !eids.equals("")){
				hql+=" and id not in ("+eids +" )";
			}
		}*/
		//设备区县
		if(StringUtils.isNotEmpty(region)){
			hql+=" and eregion like'%"+region.trim()+"%'";
		}
		//设备名称
		if(StringUtils.isNotEmpty(ename)){
			hql+=" and ename like'%"+ename.trim()+"%'";
		}
		hql+="order by id asc";
		List<TEquipment> list=baseDao.findByHqlAll(TEquipment.class, hql);
	//	System.out.println(list.size());
		return list;
	}
	/**   动态级联查询
	 *         用于巡检线点的增加和修改
	 */
	@Override
	public List<TEquipment> getEquipListByRegion(int entid,String region,String eids,String qtype) {
		String hql="from TEquipment ";
		if(entid!=0){
			hql+="where entid in(0,"+entid+")";
		}
		else{
			hql+="where 1=1 ";
		}
		if(qtype.equals("u")){//修改
			if(eids!="" && !eids.equals("")){
				hql+=" and id not in ("+eids +" )";
			}
			
			hql+=" and eregion='"+region+"'";
		}
		//可选的设备区县条件
		if(StringUtils.isNotEmpty(region)){
			hql+=" and eregion='"+region+"'";
		}
		hql+="order by id asc";
		List<TEquipment> list=baseDao.findByHqlAll(TEquipment.class, hql);
		return list;
	}
	/**
	 * 在进入List页面时调用此方法
	 * 查询巡设备项列表
	 *
	 */
	@Override
	public List<TEquipment> getEquipmentList(int entid,String eids,String qtype) {
		List<TEquipment> list=new ArrayList<TEquipment>();
		String hql="from TEquipment ";
		if(entid!=0){
			hql+="where entid in(0,"+entid+")";
		}
		else{
			hql+="where 1=1 ";
		}
		if(qtype.equals("u")){//修改
			if(eids!="" && !eids.equals("")){
				hql+=" and id not in ("+eids +" )";
			}
		}
		hql+=" order by id asc";
		List<TEquipment> l=baseDao.findByHqlAll(TEquipment.class, hql);
		if(l!=null && l.size()>0){
			for(TEquipment a:l){
				TEquipment einfo=new TEquipment();
				einfo.setId(a.getId());
				einfo.setEname(a.getEname());
				einfo.setEnumber(a.getEnumber());
				list.add(einfo);
			}
		}
		return list;
	}

	@Override
	public void addLine(LineVo linevo) {
		TLine line=new TLine();
	    BeanUtils.copyProperties(linevo,line);
	    line.setEntid(linevo.getEntid());
	    baseDao.save(line);
	    this.addLinePoint(linevo, line);
	}

	/**
	 * 保存巡检线路与巡检点的关联关系
	 * @param linevo 
	 * @param line
	 */
	private void addLinePoint(LineVo linevo,TLine line){
		baseDao.executeHql("delete from TLinePoint t where t.tline = ?", new Object[] { line });
		if (linevo.getLpIds() != null) {
			int i=0;
			for (String id : linevo.getLpIds() .split(",")) {
				i=i+1;
				TLinePoint tlp = new TLinePoint();
				tlp.setTline(line);
				tlp.setTpoint(baseDao.get(TPoint.class,Integer.parseInt(id.trim())));
				tlp.setLporder(i);
				Long count=baseDao.count("select count(*) from TLinePoint where tline="+line.getId()+" and tpoint="+id);
				if(count==0){
					baseDao.save(tlp);
				}
			}
		}
	}
	
	@Override
	public void editLine(LineVo linevo) {
		TLine line=baseDao.get(TLine.class,linevo.getId());
	    BeanUtils.copyProperties(linevo,line,new String[]{"id","entid"});
	    baseDao.update(line);
	    this.addLinePoint(linevo,line);
	}

	@Override
	public TLine getLine(String id) {
		if(StringUtils.isEmpty(String.valueOf(id))){
			return null;
		}
		return baseDao.get(TLine.class,Integer.parseInt(id));
	}

	@Override
	public void removeLine(String ids) {
		if(!StringUtils.isEmpty(ids)){
			for(String id : ids.split(",")){
				TLine l =baseDao.get(TLine.class,Integer.parseInt(id.trim()));
				if (l != null) {
					//先删除巡检线路与巡检点关联关系
					baseDao.executeHql("delete from TLinePoint t where t.tline = ?", new Object[] { l });
					baseDao.delete(TLine.class,Integer.parseInt(id));
				}
			}
		}
	}
	
	@Override
	public Map<String, Object> findLineDatagrid(LineVo linevo, int page,int rows, String entid) {
		
		Map<String, Object> map = new HashMap<String, Object>();

		StringBuffer buf = new StringBuffer();
		buf.append("SELECT b.id, GROUP_CONCAT(CAST(c.poname AS CHAR)),a.lineid ,b.lname "+
		 " FROM t_line b  LEFT JOIN  t_line_point a ON ( a.lineid=b.id ) LEFT JOIN  t_point c  ON (a.pointid=c.id ) ") ;
		 
		if("0".equals(entid)){
			buf.append(" where 1=1");
		}else{
			buf.append(" where  b.entid in (0,").append(entid).append(")");
		}
		if (!StringUtils.isEmpty(linevo.getLname())) {
			buf.append(" and b.lname like '%").append(linevo.getLname()).append("%'");
		}
		if (!StringUtils.isEmpty(linevo.getLnumber())) {
			buf.append(" and b.lnumber ='").append(linevo.getLnumber()).append("'");
		}
		if (linevo.getEntid()!=0) {
			buf.append(" and b.entid=").append(linevo.getEntid());
		}
		
		buf.append(" group by b.id order by b.id desc ");
		
		String sql=buf.toString()+ "LIMIT "+(page - 1) * rows+","+rows ;
		//System.out.println(buf.toString());
		SQLQuery pgquery = baseDao.getHibernaSession().createSQLQuery(sql);
		List<Object[]> lvolist = pgquery.list();

		List<LineVo> list = toObj(lvolist);
		
		SQLQuery listsum = baseDao.getHibernaSession().createSQLQuery(buf.toString());
		if(list==null){
			list=new ArrayList<LineVo>();
		}
		map.put("total", listsum.list().size());
		map.put("rows", list);
		return map;
		/*
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer buf = new StringBuffer(qsql);
		if (!StringUtils.isEmpty(linevo.getLname())) {
			buf.append(" and lname like '%").append(linevo.getLname()).append("%'");
		}
		if (!StringUtils.isEmpty(linevo.getLnumber())) {
			buf.append(" and lnumber ='").append(linevo.getLnumber()).append("'");
		}
		if (linevo.getEntid()!=0) {
			buf.append(" and entid=").append(linevo.getEntid());
		}
		buf.append(" order by id desc ");
		QueryResult<TLine> queryResult = baseDao.getQueryResult(TLine.class, buf.toString(), (page - 1) * rows, rows,null, null);
		List<LineVo> lvolist=new ArrayList<LineVo>();
		if (queryResult != null && queryResult.getResultList().size() > 0) {
			for (TLine tl : queryResult.getResultList()) {
				LineVo lvo = new LineVo();
				BeanUtils.copyProperties(tl,lvo);
//				Set<TLinePoint> linepoints = tl.getLinepoints();
				List<TLinePoint> linepoints = tl.getLinepoints();
				StringBuffer lpIds = new StringBuffer("");
				StringBuffer  lpNames = new StringBuffer("");
				boolean b = false;
				if (linepoints != null && linepoints.size() > 0) {
					for (TLinePoint linepoint : linepoints) {
						if (linepoint.getTpoint()!= null) {
							if (b) {
								lpIds.append(",");
								lpNames.append(",");
							}
							lpIds.append(linepoint.getTpoint().getId());
							lpNames.append(linepoint.getTpoint().getPoname());
							b = true;
						}
					}
				}
				lvo.setLpIds(lpIds.toString());
				lvo.setLpNames(lpNames.toString());
				lvolist.add(lvo);
			}
		}
		map.put("total", queryResult.getTotalRecord());
		map.put("rows", lvolist);
		return map;
	*/}
	
	private List<LineVo> toObj(List<Object[]> lvolist) {
		// TODO Auto-generated method stub
		List<LineVo> list=new ArrayList();
		if (lvolist == null || lvolist.size() == 0){
			return null;
		}
		for(int i=0;i<lvolist.size();i++){
			try {
				LineVo l=new LineVo();
				Object[] obj=(Object[])lvolist.get(i);
	
				//巡检点名称
				if(obj[1]==null){
					l.setLpNames("");
				}
				else{
					l.setLpNames((String)obj[1]);
				}
				//巡检线路id
				l.setId((Integer)obj[0]);
				//巡检线路名称
				if(obj[3]==null){
					l.setLname("");
				}
				else{
					l.setLname((String)obj[3]);
				}
				list.add(l);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
		}
		return list;
	}

	//unicomvo
//    id : rows[0].id,
//	operator : rows[0].ponumber,
//	applyid : rows[0].poname,
//	baseid : rows[0].poposx,
//	basename : rows[0].poposy,
//	baseaddress : rows[0].poaddress
	
	private List<UnicomVo> toUnicomVoObj(List<Object[]> lvolist) {
		// TODO Auto-generated method stub
		List<UnicomVo> list=new ArrayList();
		if (lvolist == null || lvolist.size() == 0){
			return null;
		}
		for(int i=0;i<lvolist.size();i++){
			try {
				UnicomVo l=new UnicomVo();
				Object[] obj=(Object[])lvolist.get(i);
	
				l.setId((Integer)obj[0]);
				//巡检线路名称
				
				
				//巡检点名称
				if(obj[1]==null){
					l.setOperator("");
				}
				else{
					l.setOperator((String)obj[1]);
				}
				//巡检线路id
				
				if(obj[2]==null){
					l.setLicense("");
				}
				else{
					l.setLicense((String)obj[2]);
				}
				
				
				if(obj[3]==null){
					l.setBaseid("");
				}
				else{
					l.setBaseid((String)obj[3]);
				}
				
				if(obj[4]==null){
					l.setBasename("");
				}
				else{
					l.setBasename((String)obj[4]);
				}
				
				if(obj[5]==null){
					l.setAddress("");
				}
				else{
					l.setAddress((String)obj[5]);
				}
				
				
				
				
				
				list.add(l);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
		}
		return list;
	}
	
	
	private List<FormsVo> toFormsVoObj(List<Object[]> lvolist) {
		// TODO Auto-generated method stub
		List<FormsVo> list=new ArrayList();
		if (lvolist == null || lvolist.size() == 0){
			return null;
		}
		for(int i=0;i<lvolist.size();i++){
			try {
				FormsVo l=new FormsVo();
				Object[] obj=(Object[])lvolist.get(i);
	
				l.setId((Integer)obj[0]);
				//巡检线路名称
				
				
				//巡检点名称
				if(obj[1]==null){
					l.setOperator("");
				}
				else{
					l.setOperator((String)obj[1]);
				}
				//巡检线路id
				
				if(obj[2]==null){
					l.setAdministrativedivision("");
				}
				else{
					l.setAdministrativedivision((String)obj[2]);
				}
				
				
				if(obj[3]==null){
					l.setLicense("");
				}
				else{
					l.setLicense((String)obj[3]);
				}
				
				
				if(obj[4]==null){
					l.setBaseid("");
				}
				else{
					l.setBaseid((String)obj[4]);
				}
				
				if(obj[5]==null){
					l.setBasename("");
				}
				else{
					l.setBasename((String)obj[5]);
				}
				
				if(obj[6]==null){
					l.setAddress("");
				}
				else{
					l.setAddress((String)obj[6]);
				}
				
				    String url =  "jdbc:mysql://127.0.0.1:3306/inspect",password = "123";
			        String user = "root";
			        Connection conn = null;
			        
			        int xpicsnum=0,mappicnum=0;
			        Class.forName("com.mysql.jdbc.Driver"); 
		        	conn = DriverManager.getConnection(url, user, password);
 
		        	Statement stmt = conn.createStatement();
		        	String sqlpic = "select * from t_report_message where xequtnum = '" + (String)obj[3]+"'";
		    		ResultSet rs = stmt.executeQuery(sqlpic); 
		        	//防止查询空。	
		    		if(rs.next()) {
		    			
		        		if(rs.getString("ximgname")!=null&&rs.getString("ximgname").trim().length()!=0){
		        			xpicsnum = 	rs.getString("ximgname").trim().split(",").length;
		        			System.out.println("现场照片: "+rs.getString("ximgname"));
		        		}else{
		        			xpicsnum = 0;
		        		} 
		        		
		        		
		        		if(rs.getString("mappic")!=null&&rs.getString("mappic").trim().length()!=0){
		        		    mappicnum = 1;	
		        		}else
		        		{
		        			mappicnum = 0;		
		        		}
		        	}
		        	
		        	String remarkstr = "现场照片"+xpicsnum+"张，平面图"+mappicnum+"张";
				
		        	l.setRemark(remarkstr);
//				if(obj[7]==null){
//					l.setRemark("");
//				}
//				else{
//					l.setRemark((String)obj[7]);
//				}
				
				
				list.add(l);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
		}
		return list;
	}
	
	
	
	
	@Override
	public List<TLine> getlineList(int entid) {
		List<TLine> list=new ArrayList<TLine>();
		String hql="";
		if(entid==0){
			hql="from TLine";
		}
		else{
			hql="from TLine where entid in(0,"+entid+")";
		}
		List<TLine> l=baseDao.findByHqlAll(TLine.class, hql);
		if(l!=null && l.size()>0){
			for(TLine a:l){
				TLine pinfo=new TLine();
				pinfo.setId(a.getId());
				pinfo.setLname(a.getLname());
				list.add(pinfo);
			}
		}
		return list;
	}
	
	@Override
	public List<TPlanTask> gettasklist(int entid) {
		String hql="";
		if(entid==0){
			hql="from TPlanTask";
		}
		else{
			hql="from TPlanTask where entid="+entid;
		}
		List<TPlanTask> list=baseDao.find(hql);

		return list;
	}

	@Override
	public List<TLine> getlineList1(int entid) {
		String hql="";
		if(entid==0){
			hql="from TLine";
		}
		else{
			hql="from TLine where entid="+entid;
		}
		List<TLine> list=baseDao.find(hql);
	
		return list;
	}
	
	@Override
	public List<TPoint> getPointList(int entid,String pids,String qtype) {
			List<TPoint> list=new ArrayList<TPoint>();
		
			String hql="from TPoint ";
			if(entid!=0){
				hql+="where entid in(0,"+entid+")";
			}
			else{
				hql+="where 1=1 ";
			}
			if(qtype.equals("u")){//修改
				if(pids!="" && !pids.equals("")){
					hql+=" and id not in ("+pids +" )";
				}
			}
			hql+=" order by id asc";
			List<TPoint> l=baseDao.findByHqlAll(TPoint.class, hql);
			if(l!=null && l.size()>0){
				for(TPoint a:l){
					TPoint linfo=new TPoint();
					linfo.setId(a.getId());
					linfo.setPoname(a.getPoname());
					list.add(linfo);
				}
			}
			return list;
		}

	@Override
	public void addTwoDimensionCode(TwoDimensionCodeVo twodimensioncodevo) {
		TTwoDimensionCode twocode=new TTwoDimensionCode();
	    BeanUtils.copyProperties(twodimensioncodevo,twocode);
	    twocode.setEntid(twodimensioncodevo.getEntid());
	    baseDao.save(twocode);
	}

	@Override
	public void editTwoDimensionCode(TwoDimensionCodeVo twodimensioncodevo) {
		TTwoDimensionCode twocode=baseDao.get(TTwoDimensionCode.class,twodimensioncodevo.getId());
	    BeanUtils.copyProperties(twodimensioncodevo,twocode,new String[]{"id","entid"});
	    baseDao.update(twocode);
	}

	@Override
	public void removeTwoDimensionCode(String ids) {
		if(!StringUtils.isEmpty(ids)){
			for(String id : ids.split(",")){
				baseDao.delete(TTwoDimensionCode.class,Integer.parseInt(id));
			}
		}
	}
	@Override
	public TTwoDimensionCode getTwoDimensionCode(String id) {
		if(StringUtils.isEmpty(String.valueOf(id))){
			return null;
		}
		return baseDao.get(TTwoDimensionCode.class,Integer.parseInt(id));
	}
	@Override
	public TTwoDimensionCode getTwoDimensionCodeByCname(String cname){
	
		TTwoDimensionCode twoDimensionCode=null;
		String hql="from TTwoDimensionCode where cname='"+ cname+"'";
		twoDimensionCode=baseDao.findByHql(TTwoDimensionCode.class,hql);
		return twoDimensionCode;
	}
	@Override
	public Map<String, Object> findTwoDimensionCodeDatagrid(TwoDimensionCodeVo twodimensioncodevo, int page, int rows,String qsql) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer buf = new StringBuffer(qsql);
		if (!StringUtils.isEmpty(twodimensioncodevo.getCname())) {
			buf.append(" and cname like '%").append(twodimensioncodevo.getCname()).append("%'");
		}
		if (twodimensioncodevo.getEntid()!=0) {
			buf.append(" and entid=").append(twodimensioncodevo.getEntid());
		}
		QueryResult<TTwoDimensionCode> queryResult = baseDao.getQueryResult(TTwoDimensionCode.class, buf.toString(), (page - 1) * rows, rows,null, null);
		List<TwoDimensionCodeVo> cvolist=new ArrayList<TwoDimensionCodeVo>();
		if (queryResult != null && queryResult.getResultList().size() > 0) {
			for (TTwoDimensionCode te : queryResult.getResultList()) {
				TwoDimensionCodeVo cvo = new TwoDimensionCodeVo();
				BeanUtils.copyProperties(te,cvo);
				cvolist.add(cvo);
			}
		}
		map.put("total", queryResult.getTotalRecord());
		map.put("rows", cvolist);
		return map;
	}

	@Override
	public List<TTwoDimensionCode> getTwoDimensionCodeList(int entid) {
		List<TTwoDimensionCode> list=new ArrayList<TTwoDimensionCode>();
		String hql="from TTwoDimensionCode where entid="+entid;
		List<TTwoDimensionCode> l=baseDao.findByHqlAll(TTwoDimensionCode.class, hql);
		if(l!=null && l.size()>0){
			for(TTwoDimensionCode a:l){
				TTwoDimensionCode pinfo=new TTwoDimensionCode();
				pinfo.setId(a.getId());
				pinfo.setCname(a.getCname());
				list.add(pinfo);
			}
		}
		return list;
	}

	@Override
	public void addRfid(RfidVo rfidvo) {
		TRfid r=new TRfid();
	    BeanUtils.copyProperties(rfidvo,r);
	    rfidvo.setEntid(rfidvo.getEntid());
	    baseDao.save(r);
	}

	@Override
	public void editRfid(RfidVo rfidvo) {
		TRfid r=baseDao.get(TRfid.class,rfidvo.getId());
	    BeanUtils.copyProperties(rfidvo,r,new String[]{"id","entid"});
	    baseDao.update(r);
	}

	@Override
	public TRfid getRfid(String id) {
		if(StringUtils.isEmpty(String.valueOf(id))){
			return null;
		}
		return baseDao.get(TRfid.class,Integer.parseInt(id));
	}

	@Override
	public void removeRfid(String ids) {
		if(!StringUtils.isEmpty(ids)){
			for(String id : ids.split(",")){
				baseDao.delete(TRfid.class,Integer.parseInt(id));
			}
		}
	}
	
	@Override
	public Map<String, Object> findRfidDatagrid(RfidVo rfidvo, int page,int rows, String qsql) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer buf = new StringBuffer(qsql);
		if (!StringUtils.isEmpty(rfidvo.getRname())) {
			buf.append(" and rname like '%").append(rfidvo.getRname()).append("%'");
		}
		if (rfidvo.getEntid()!=0) {
			buf.append(" and entid=").append(rfidvo.getEntid());
		}
		QueryResult<TRfid> queryResult = baseDao.getQueryResult(TRfid.class, buf.toString(), (page - 1) * rows, rows,null, null);
		List<RfidVo> rvolist=new ArrayList<RfidVo>();
		if (queryResult != null && queryResult.getResultList().size() > 0) {
			for (TRfid tr : queryResult.getResultList()) {
				RfidVo cvo = new RfidVo();
				BeanUtils.copyProperties(tr,cvo);
				rvolist.add(cvo);
			}
		}
		map.put("total", queryResult.getTotalRecord());
		map.put("rows", rvolist);
		return map;
	}
	
	@Override
	public List<TRfid> getRfidList(int entid) {
		List<TRfid> list=new ArrayList<TRfid>();
		String hql="from TRfid where entid="+entid;
		List<TRfid> l=baseDao.findByHqlAll(TRfid.class, hql);
		if(l!=null && l.size()>0){
			for(TRfid a:l){
				TRfid pinfo=new TRfid();
				pinfo.setId(a.getId());
				pinfo.setRname(a.getRname());
				list.add(pinfo);
			}
		}
		return list;
	}
	
	public Long getInspectTerminalCount(int eid) {
		long count;
		if(eid==0){
			count=baseDao.count("select count(*) from TTerminal");
		}
		else{
			count=baseDao.count("select count(*) from TTerminal t where t.entid = ?", new Object[] { eid });
		}
		return count;
	}
	@Override
	public Long countEnumName(String pname,int entid) {
		return baseDao.count("select count(*) from TEnumParameter t where t.pname = ? and t.entid=?", new Object[] { pname,entid });
	}
	public Long countProjectName(String pname,int entid) {
		return baseDao.count("select count(*) from TProject t where t.pname = ? and t.entid=?", new Object[] { pname,entid });
	}
	
	public Long countEquipmentNumber(String pnumber,int entid) {
		return baseDao.count("select count(*) from TEquipment t where t.enumber = ? and t.entid=?", new Object[] { pnumber,entid });
	}
	
	public Long countFormsNumber(String flicense) {
		return baseDao.count("select count(*) from unicominfo t where t.license = ?", new Object[] { flicense });
	}
	
	public Long countPointName(String pname,int entid) {
		return baseDao.count("select count(*) from TPoint t where t.poname = ? and t.entid=?", new Object[] { pname,entid });
	}
	
	public Long countLineName(String lname,int entid) {
		return baseDao.count("select count(*) from TLine t where t.lname = ? and t.entid=?", new Object[] { lname,entid });
	}

	/**
	 * 任务查询时查找线路
	 */
	@Override
	public TLine findLine(int id) {
		if(StringUtils.isEmpty(String.valueOf(id))){
			return null;
		}
		return  baseDao.get(TLine.class,id);
	}
	/**
	 * 任务查询时查找巡检点
	 */
	@Override
	public TPoint findPoint(int id) {
		if(StringUtils.isEmpty(String.valueOf(id))){
			return null;
		}
		return  baseDao.get(TPoint.class,id);
	}
	/**
	 * 任务查询时查找设备
	 */
	@Override
	public TEquipment findEquipment(int id) {
		if(StringUtils.isEmpty(String.valueOf(id))){
			return null;
		}
		return baseDao.get(TEquipment.class,id);
	}
	
	/**
	 * 任务查询时查找巡检项
	 */
	@Override
	public TProject findProject(int id) {
		if(StringUtils.isEmpty(String.valueOf(id))){
			return null;
		}
		return  baseDao.get(TProject.class,id);
	}

	@Override
	public List<TPlanTask> getPlanTask(String sql) {
		 List<TPlanTask> list=baseDao.find(sql);
		return list;
	}

	@Override
	public TEquipment getEquipmentByequtnum(String xequtnum,int entid) {
		String hql="from TEquipment  ir where  ir.enumber='"+xequtnum+"' and ir.entid="+entid;
		List<TEquipment> tEquipmenlist=baseDao.find(hql);
		if(tEquipmenlist!=null&&tEquipmenlist.size()>0){
			TEquipment equipment=new TEquipment();
			equipment=tEquipmenlist.get(0);
			return equipment;
		}
		return null;
	}
	
	/**
	 * 通过设备名称找到message表中主键id的集合
	 */
		@Override
		public String getEquipment1Byenameornum(String ename, int entid,int flag) {
			String hql="";
			//根据名称
			if(flag==0){
				if(entid==0){
					hql="from TBaseInfo  ir where  ir.bname like'%"+ename.trim()+"%'";
				}
				else{
					hql="from TBaseInfo  ir where  ir.bname like'%"+ename.trim()+"%' and ir.entid in(0,"+entid+")";
				}
			}
			//根据编号
			else{
				if(entid==0){
					hql="from TBaseInfo  ir where  ir.bnumber like'%"+ename.trim()+"%'";
				}
				else{
					hql="from TBaseInfo  ir where  ir.bnumber like'%"+ename.trim()+"%' and ir.entid="+entid;
				}
			}
			StringBuffer buf=new StringBuffer();
			List<TBaseInfo> tEquipmenlist=baseDao.find(hql);
			int i=1;
			//设备表主键id集合
			if(tEquipmenlist!=null&&tEquipmenlist.size()>0){
				for(TBaseInfo equipment:tEquipmenlist){
					if(i!=tEquipmenlist.size()){
					buf.append(equipment.getId()).append(",");
					}
					else{
						buf.append(equipment.getId());
					}
					i++;
				}
				
			}
			//	System.out.println(buf.toString());
			
			return buf.toString();
		}
	
	
/**模糊查询设备名称时用到
 * 通过设备名称找到message表中主键id的集合
 */
	@Override
	public String getEquipmentByename(String ename, int entid) {
		String hql="";
		if(entid==0){
			hql="from TBaseInfo  ir where  ir.bname like'%"+ename.trim()+"%'";
		}
		else{
			hql="from TBaseInfo  ir where  ir.bname like'%"+ename.trim()+"%' and ir.entid in(0,"+entid+")";
		}
		String idString="";
		StringBuffer buf=new StringBuffer();
		List<TBaseInfo> tEquipmenlist=baseDao.find(hql);
		int i=1;
		//设备表主键id集合
		if(tEquipmenlist!=null&&tEquipmenlist.size()>0){
			for(TBaseInfo equipment:tEquipmenlist){
				if(i!=tEquipmenlist.size()){
				buf.append(equipment.getId()).append(",");
				}
				else{
					buf.append(equipment.getId());
				}
				i++;
			}
			
		}
		//System.out.println(buf.toString());
		
		StringBuffer buf1=new StringBuffer();
		//通过设备表主键id在message表中找到对应 的记录，放到buf1中。bu1是message表主键id的集合，用分隔符，隔开
		if(buf.toString().length()>0){
			String hql1="";
			if(entid==0){
				hql1="from TInspectItemReport  ir where  ir.xequid in ("+buf.toString()+")";
			}
			else{
				 hql1="from TInspectItemReport  ir where  ir.xequid in ("+buf.toString()+") and ir.entid="+entid;
			}
			List<TInspectItemReport> itemReportlist=baseDao.find(hql1);
			int k=1;
			if(itemReportlist!=null&&itemReportlist.size()>0){
				for(TInspectItemReport itemReport:itemReportlist){
					if(k!=itemReportlist.size()){
					buf1.append(itemReport.getId()).append(",");
					}
					else{
						buf1.append(itemReport.getId());
					}
					k++;
				}
				
			}
			//	System.out.println(buf1.toString());
		}
		return buf1.toString();
	}
	@Override
	public TPoint findPointByName(String linename, int entid) {
		String hql="from TPoint  t where  t.poname='"+linename+"' and t.entid="+entid;
		List<TPoint> tEquipmenlist=baseDao.find(hql);
		if(tEquipmenlist!=null&&tEquipmenlist.size()>0){
			TPoint point=new TPoint();
			point=tEquipmenlist.get(0);
			return point;
		}
		return null;
	}
	
	@Override
	public TLine findLineByName(String pointname, int entid) {
		String hql="from TLine  t where  t.lname='"+pointname+"' and t.entid="+entid;
		List<TLine> linelist=baseDao.find(hql);
		if(linelist!=null&&linelist.size()>0){
			TLine line=new TLine();
			line=linelist.get(0);
			return line;
		}
		return null;
	}
	@Override
	public TEquipment getEquipmentByEtwocodeid(String etwocodeid) {
		String hql="from TEquipment  ir where  ir.etwocodeid='"+etwocodeid+"'";
		List<TEquipment> tEquipmenlist=baseDao.find(hql);
		if(tEquipmenlist!=null&&tEquipmenlist.size()>0){
			TEquipment equipment=new TEquipment();
			equipment=tEquipmenlist.get(0);
			return equipment;
		}
		return null;
	}
	/**
	 * 巡检计划查询
	 */
	@Override
	public Map<String, Object> findPlanQueryVoDatagrid(PlanQueryVo planQueryvo,
			int page, int rows,String shql) {
		String qsql=shql;
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer buf = new StringBuffer(qsql);
		if (planQueryvo.getPgid()!=0) {
			buf.append(" and pgid =").append(planQueryvo.getPgid());
		}
		if (planQueryvo.getPuid()!=0) {
			buf.append(" and puid =").append(planQueryvo.getPuid());
		}
	/*	if (planQueryvo.getPlineid()!=0) {
			buf.append(" and plineid =").append(planQueryvo.getPlineid());
		}*/
		if (!StringUtils.isEmpty(planQueryvo.getPstartdate())) {
			buf.append(" and pstartdate >= '").append(planQueryvo.getPstartdate()).append("'");
		}
		if (!StringUtils.isEmpty(planQueryvo.getPenddate())) {
			buf.append(" and penddate <='").append(planQueryvo.getPenddate()).append("'");
		}

		if(StringUtils.isNotEmpty(planQueryvo.getXreptime())){
			buf.append(" and pstartdate >= '").append(planQueryvo.getXreptime()).append("'");
			buf.append(" and penddate <='").append(planQueryvo.getXreptime()).append("'");
		}
		
		QueryResult<TPlanTask> queryResult = baseDao.getQueryResult(TPlanTask.class, buf.toString(), (page - 1) * rows, rows,null, null);

		List<PlanQueryVo> planQueryvolist=new ArrayList<PlanQueryVo>();
		HashMap<Integer, Integer> map1=new HashMap<Integer, Integer>();
		if (queryResult != null && queryResult.getResultList().size() > 0) {
			for (TPlanTask pTask : queryResult.getResultList()) {
				PlanQueryVo planQueryvo1 = new PlanQueryVo();
				BeanUtils.copyProperties(pTask,planQueryvo1);
				planQueryvo1.setItaskid(pTask.getId());
				TLine tline=this.findLine(planQueryvo1.getPlineid());
//				Set<TLinePoint> linepoints = tl.getLinepoints();
				List<TLinePoint> linepoints = tline.getLinepoints();
				planQueryvo1.setPlinename(tline.getLname());
				planQueryvo1.setPointCount(linepoints.size());//巡检点数目
				//用于计算已寻点数目
				String hql="select count(*) from  TInspectItemReport where  "+shql;
				
				StringBuffer buf1=new StringBuffer(hql);
				if (!StringUtils.isEmpty(planQueryvo1.getPstartdate())) {
					buf1.append(" and xreptime >='").append(planQueryvo1.getPstartdate()).append("'");
				}
				if (!StringUtils.isEmpty(planQueryvo1.getPenddate())) {
					buf1.append(" and xreptime<='").append(planQueryvo1.getPenddate()).append(" 23:59:59").append("'");
				}
				if(planQueryvo1.getPlineid()!=0){
					buf1.append(" and xlid= ").append(planQueryvo1.getPlineid());
				}
				buf1.append(" and xtaskid= ").append(planQueryvo1.getItaskid());//任务id
				buf1.append("  group by xpid");//一个点只能计算一次
				List list=baseDao.find(buf1.toString());
				if(list!=null&&list.size()>0){
					planQueryvo1.setQueryCount(list.size());
				}

				//计算未寻点数目
				int unqueryCount=planQueryvo1.getPointCount()-planQueryvo1.getQueryCount();
				planQueryvo1.setUnqueryCount(unqueryCount);
				if(unqueryCount>0){
					planQueryvo1.setLineStatus("异常");
				}
				else{
					planQueryvo1.setLineStatus("正常");
				}
				//判断时候以站点进行查询
				if(planQueryvo.getPequid()!=0){
					String hql1=" from  TInspectItemReport where  "+shql +" and  xequid="+planQueryvo.getPequid();
					List<TInspectItemReport> iTaskList=baseDao.find(hql1);
					if(iTaskList!=null&&iTaskList.size()>0){
						for(int i=0;i<iTaskList.size();i++){
							map1.put(i,iTaskList.get(i).getXtaskid());//找出于此站点有关的任务id
						}
					}
				}
				//判断时候存在以站点名称进行的查询
				if(planQueryvo.getPequid()!=0){
					//判断任务表中的任务id是否在记录表中指定设备id中也存在
					if(map1.containsValue(planQueryvo1.getItaskid())){
						planQueryvolist.add(planQueryvo1);
					}
				}
				else{
					planQueryvolist.add(planQueryvo1);
				}
				
			}
		}
		map.put("total", queryResult.getTotalRecord());
		map.put("rows", planQueryvolist);
		return map;
	}
	@Override
	/**
	 * 终端上传图片
	 */
	public int addPicture(TInspectItemDetailReport inspectItemDetailReport,TInspectItemReport inspectItemReport,TInspectItemRaltionReport inspectItemRaltionReport){
		int i = 0 ;
		try{
			//保存至设备表记录中
			inspectMonitorServiceI.addTInspectItemReport(inspectItemReport);
			//保存是项目记录表中
			inspectMonitorServiceI.addTInspectItemDetailReport(inspectItemDetailReport);
			//保存至关联表
			inspectMonitorServiceI.addTInspectItemRaltionReport(inspectItemRaltionReport);
			i = 1 ;
		}catch (Exception e) {}
		return i;
	}
	/**
	 * 终端提交数据
	 * @param inspectItemDetailReport
	 * @param inspectItemReport
	 */
	@Override
	public void addData(TInspectItemDetailReport inspectItemDetailReport,TInspectItemReport inspectItemReport,TInspectItemRaltionReport inspectItemRaltionReport,TInspectPointReport inspectPointReport,String status){
		//保存是项目记录表中
		inspectMonitorServiceI.addTInspectItemDetailReport(inspectItemDetailReport);
		//若包含异常状态，则执行此语句
		if (status!=null&&!status.equals("1")) {
			
			inspectMonitorServiceI.addTInspectPointReport(inspectPointReport);
		}
		//保存至设备表记录中
		inspectMonitorServiceI.addTInspectItemReport(inspectItemReport);
		//保存至关联表
		inspectMonitorServiceI.addTInspectItemRaltionReport(inspectItemRaltionReport);
	}

	@Override
	public boolean isHavePlanAndTask(int lineid) {
		String hql="from TPlan where plineid="+lineid;
		List list=baseDao.find(hql);
		boolean flag=false;
		if(list!=null&&list.size()>0){
			flag=true;
		}
		
		return flag;
	}

	@Override
	public Map<String, Object> findProjectGroupDatagrid(
			TProjectGroupVo projectGroupVo, int page, int rows, String qsql) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer buf = new StringBuffer(qsql);
		if (!StringUtils.isEmpty(projectGroupVo.getPgname())) {
			buf.append(" and pgname like '%").append(projectGroupVo.getPgname()).append("%'");
		}
		if (projectGroupVo.getEntid()!=-1) {
			buf.append(" and entid =").append(projectGroupVo.getEntid());
		}
		buf.append(" order by id desc ");
		QueryResult<TProjectGroup> queryResult = baseDao.getQueryResult(TProjectGroup.class, buf.toString(), (page - 1) * rows, rows,null, null);
		List<TProjectGroupVo> pGroupvolist=new ArrayList<TProjectGroupVo>();
		if (queryResult != null && queryResult.getResultList().size() > 0) {
			for (TProjectGroup pGroup : queryResult.getResultList()) {
				TProjectGroupVo pGroupvo = new TProjectGroupVo();
				BeanUtils.copyProperties(pGroup,pGroupvo,new String[]{"tprojects"});
				pGroupvo.setPcomname(baseDao.get(Enterprise.class, pGroupvo.getEntid())!=null?baseDao.get(Enterprise.class, pGroupvo.getEntid()).getEntname():"");
				pGroupvolist.add(pGroupvo);
			}
		}
		map.put("total", queryResult.getTotalRecord());
		map.put("rows", pGroupvolist);
		return map;
	}

	@Override
	public void addProblemGroup(TProjectGroupVo projectGroupVo) {
		TProjectGroup projectGroup=new TProjectGroup();
		BeanUtils.copyProperties(projectGroupVo,projectGroup);
		baseDao.save(projectGroup);
	}

	@Override
	public void removeProjectGroup(String ids) {
		for (String id : ids.split(",")) {
			baseDao.delete(TProjectGroup.class, Integer.parseInt(id));
		}
	}
	
	@Override
	public void editProjectGroup(TProjectGroupVo projectGroupVo) {
		TProjectGroup projectGroup=baseDao.get(TProjectGroup.class, projectGroupVo.getId());
		BeanUtils.copyProperties(projectGroupVo, projectGroup,new String[]{"id","entid"});
		baseDao.update(projectGroup);
		
	}

	@Override
	public TProjectGroup getProjectGroup(int id) {
		TProjectGroup projectGroup=	baseDao.get(TProjectGroup.class,id);
		return projectGroup;
	}

	@Override
	public Long isExistProjectGroup(String pgname,int entid) {
		return baseDao.count("select count(*) from TProjectGroup t where t.pgname = ? and t.entid=?", new Object[] { pgname,entid });
	}
	
	@Override
	public List<TProjectGroup> getProjectGroupList(String sqhl) {
		List<TProjectGroup> list=new ArrayList<TProjectGroup>();
		String hql="from TProjectGroup where "+sqhl;
		List<TProjectGroup> l=baseDao.findByHqlAll(TProjectGroup.class, hql);
		if(l!=null && l.size()>0){
			for(TProjectGroup a:l){
				TProjectGroup pinfo=new TProjectGroup();
				pinfo.setId(a.getId());
				pinfo.setPgname(a.getPgname());
				list.add(pinfo);
			}
		}
		return list;
	}
	
	@Override
	public void addEnumParameter(EnumParameterVo enumParameterVo) {
		TEnumParameter tep=new TEnumParameter();
		BeanUtils.copyProperties(enumParameterVo,tep);
		baseDao.save(tep);
	}
	@Override
	public void editEnumParameter(EnumParameterVo enumParameterVo) {
		TEnumParameter tep=baseDao.get(TEnumParameter.class, enumParameterVo.getId());
		BeanUtils.copyProperties(enumParameterVo, tep,new String[]{"id","entid"});
		baseDao.update(tep);
	}
	@Override
	public void removeEnumParameter(String ids) {
		if(!StringUtils.isEmpty(ids)){
			for(String id : ids.split(",")){
				baseDao.delete(TEnumParameter.class,Integer.parseInt(id));
			}
		}
	}
	@Override
	public Map<String, Object> findEnumParameterDatagrid(EnumParameterVo enumParameterVo,int page, int rows, String qsql) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer buf = new StringBuffer(qsql);
		if (!StringUtils.isEmpty(enumParameterVo.getPname())) {
			buf.append(" and pname like '%").append(enumParameterVo.getPname()).append("%'");
		}
		if (!StringUtils.isEmpty(enumParameterVo.getPvalue())) {
			buf.append(" and pvalue like '%").append(enumParameterVo.getPvalue()).append("%'");
		}
		if (!StringUtils.isEmpty(enumParameterVo.getPtype())) {
			buf.append(" and ptype ='").append(enumParameterVo.getPtype()).append("'");
		}
		if (enumParameterVo.getEntid()!=-1) {
			buf.append(" and entid=").append(enumParameterVo.getEntid());
		}
		buf.append(" order by pname");
		QueryResult<TEnumParameter> queryResult = baseDao.getQueryResult(TEnumParameter.class, buf.toString(), (page - 1) * rows, rows,null, null);
		List<EnumParameterVo> pvolist=new ArrayList<EnumParameterVo>();
		if (queryResult != null && queryResult.getResultList().size() > 0) {
			for (TEnumParameter te : queryResult.getResultList()) {
				EnumParameterVo pvo = new EnumParameterVo();
				BeanUtils.copyProperties(te,pvo);
			//	System.out.println(baseDao.get(Enterprise.class, pvo.getEntid()).getEntname());
				pvo.setPcomname(baseDao.get(Enterprise.class, pvo.getEntid())!=null?baseDao.get(Enterprise.class, pvo.getEntid()).getEntname():"");
			    pvolist.add(pvo);
			}
		}
		map.put("total", queryResult.getTotalRecord());
		map.put("rows", pvolist);
		return map;
	}
	
	@Override
	public List<TEnumParameter> getEnumParameListByParameName(String hql,String pname){
		StringBuffer buf=new StringBuffer("from TEnumParameter ");
		if(!StringUtils.isEmpty(hql)){
			buf.append(" where ").append(hql);
		}else{
			buf.append(" where 1=1");
		}
		List<TEnumParameter> list=null;
		if(!StringUtils.isEmpty(pname)){
			buf.append(" and pname ='").append(pname).append("'");
		}
		list=baseDao.findByHqlAll(TEnumParameter.class, buf.toString());
		return list;
	}
	@Override
	public List<TEnumParameter> getEnumParameListByParameType(String hql,String ptype){
		List<TEnumParameter> list=new ArrayList<TEnumParameter>();
		StringBuffer buf=new StringBuffer("select distinct(pname) from TEnumParameter where ").append(hql);
//		if(!StringUtils.isEmpty(ptype)){
//			buf.append(" and ptype ='").append(ptype).append("'");
//		}
		//System.out.println(buf.toString());
		List<String> l=baseDao.find(buf.toString());
		if(l!=null && l.size()>0){
			for(String a:l){
				TEnumParameter p=new TEnumParameter();
//			    p.setPvalue(a.getPvalue());
				p.setPname(a);
				list.add(p);
			}
		}
		return list;
	}

	@Override
	public boolean isInspect(int projectid, int taskid) {
		boolean status=false;
		String hql="from  TInspectItemDetailReport where xtaskid="+taskid+" and xproid="+projectid;
		List list=baseDao.find(hql);
		if(list!=null&&list.size()>0){
			status=true;
		}
		return status;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<TPlanTask> getPlanTaskList(int qsql) {
		StringBuffer buf=new StringBuffer("select id,pname from t_plan_task where ");
		if(qsql==0){
			buf.append(" 1=1");
		}else{
			buf.append(" entid=").append(qsql);
		}
		List<TPlanTask> list = baseDao.getJdbcTemplate().queryForList(buf.toString());
		return list;
	}
	/**
	 * 通过巡检项组id查询
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<TProject> getProjectByPgroupid(String pgroupid) {
		String hql="from  TProject where pgroupid="+pgroupid;
		List list=baseDao.find(hql);
		return  list;
	}

	@Override
	public boolean isEquipInspectStatus(int taskid, int equid) {
		boolean flag=false;
		String hql="select count(*) from TInspectItemReport where xtaskid="+taskid+"and xequid="+equid;
		long a=0;
		a=baseDao.count(hql);
		if(a>0){
			flag=true;
		}
		return flag;
	}

	@Override
	public TInspectItemReport getTInspectItemReport(int id) {
		TInspectItemReport tReport=null;
		tReport=baseDao.get(TInspectItemReport.class, id);
		return tReport;
	}

	@Override
	public List<TInspectItemDetailReport> getDetailInspectStatus(int taskid, int xproid) {
		List list=null;
		String hql="from TInspectItemDetailReport where xtaskid="+taskid+"and xproid="+xproid;
		list=baseDao.find(hql);
		return list;
	}
	public TBaseInfo getBaseInfobyNumber(String bnumber,int entid) {
		List<TBaseInfo>bInfoList= baseDao.find("from TBaseInfo t where t.bnumber = '"+bnumber +"' "+" and t.entid="+entid);
		TBaseInfo bInfo=null;
		if(bInfoList!=null&&bInfoList.size()>0){
			bInfo=bInfoList.get(0);
		}
		return bInfo;
	}
	@Override
	public TPoint getPointByName(String poname,int entid) {
		// TODO Auto-generated method stub
		String hql="from TPoint t where t.poname = '"+poname+"' and t.entid="+entid;
		TPoint point=null;
		List<TPoint> pointList=baseDao.find(hql);
		if(pointList!=null&&pointList.size()>0){
			point=pointList.get(0);
		}
		return point;
	}
	@Override
	public String saveEquipmentProjectGroupList(List<Object> db,int entid){
		int count1=0;
		  for(int i=0;i<db.size();i++){
			  EquipmentSummaryVo epgVo= (EquipmentSummaryVo) db.get(i);
			  //Long count=countEquipmentNumber(epgVo.getEnumber(), entid);
				String hql="from TProjectGroup where  pgname='"+epgVo.getEpGroupName()+"' and entid in(0,"+entid+")";
				List <TProjectGroup> pGroupList=baseDao.find(hql);
				TProjectGroup pGroup=null; 
				//当巡检项名称存在时，保存
				if(pGroupList!=null&&pGroupList.size()>0){
					pGroup=pGroupList.get(0);
				}
				if(pGroup!=null){
				     TBaseInfo bInfo=null;
					 TEquipment equip=getEquipmentByequtnum(epgVo.getEnumber(),entid);
					 //判断在设备表t_equipment表中是否已经存在指定设备编号的信息，如果没有，则保存值设备表和保存设备与基础信息的关联表中
					 //如果有，则用已有的设备
					 if(equip!=null){
					 }
					 else{
						 bInfo=getBaseInfobyNumber(epgVo.getEnumber(),entid);
							//只有在基础信息表里面存在此设备编号，才保存，否则直接无视
						 if(bInfo!=null){
							 equip=new TEquipment();
							 equip.setEnumber(bInfo.getBnumber());//设备编号
							 equip.setEname(bInfo.getBname());//设备名称
							 equip.setEposx(bInfo.getBposx());//经度
							 equip.setEposy(bInfo.getBposy());//纬度
							 equip.setEaddress(bInfo.getBaddress());//设备地址
							 equip.setEtype(bInfo.getBtype());//设备类型
							 equip.setEntid(entid);
							 equip.setEregion(bInfo.getBregion());//设备区县
							 equip.setEcity(bInfo.getBcity());//设备区县
							 //当巡检项组存在时保存设备和关联表信息
								//保存设备
								 baseDao.save(equip);
							    //保存设备与基础信息的关联表
							    addBaseInfoEquipment(bInfo,equip); 
						 }
						 else{
							 continue;
						 }
					 }
					 //当此信息已经在关联表中存在时，不保存，且返回0；如果不存在，则保存，返回1
					   int flag= addEquipmentProject(equip, pGroup);
					   if(flag==1){
					    count1++;
					   }
				}
					
		  }
		  String dd=String.valueOf(count1);
		  return dd;
	}

	@Override
	public String savePointList(List<Object> db, int entid) {
		// TODO Auto-generated method stub
		int count1=0;
		  for(int i=0;i<db.size();i++){
				PointSummaryVo psVo= (PointSummaryVo) db.get(i);
				TPoint pVo=findPointByName(psVo.getPointname(),entid);
				TEquipment equip=getEquipmentByequtnum(psVo.getEnumber(),entid);
				if(equip==null){
					continue;
				}
				//判断巡检点是否存在，若存在，则使用已存在的，若不存在，则保存此巡检点信息
				if(pVo!=null){
				}
				else{
					 pVo=new TPoint();
					 pVo.setEntid(entid);
					 pVo.setPoname(psVo.getPointname());
					 this.addPoint1(pVo);//莒南老汽车站（嵋山路信用社）_CELL_3\4莒南环保局家属楼

				 }
				TPoint point= this.getPointByName(pVo.getPoname(),pVo.getEntid());
				//判断在设备表t_equipment表中是否已经存在指定设备编号的信息，如果有，则保存值巡检点表和保存设备与巡检点的关联表中
				 //如果没有，则跳过此数据
			//	System.out.println("巡检点名称"+point.getPoname()+"\t设备编号+"+equip.getEnumber());
				int flag=addPointEquipment(point,equip);
				if(flag==1){
				    count1++;
				}
		  }
	
		return String.valueOf(count1);
	}
/**
 * 导入线路
 */
	@Override
	public String saveLineList(List<Object> db, int entid) {
		// TODO Auto-generated method stub
		int count1=0;
		  for(int i=0;i<db.size();i++){
			  LineSummaryVo lsVo= (LineSummaryVo) db.get(i);
			  //知道线路
				TLine line=findLineByName(lsVo.getLinename(),entid);
				//找到巡检点
				TPoint point=findPointByName(lsVo.getPointname(),entid);
				if(point==null){
					continue;
				}
				//判断巡检线路是否存在，若存在，则使用已存在的，若不存在，则保存此巡检点信息
				if(line!=null){
				}
				else{
					line=new TLine();
					//判断巡检点中是后为空，如果否，则保存值巡检点表和保存设备与巡检点的关联表中
					 //如果是，则跳过此数据
					 line.setEntid(entid);
					 line.setLname(lsVo.getLinename());
					 this.addLine1(line);
				}
				//判断巡检点中是后为空，如果否，则保存值巡检点表和保存设备与巡检点的关联表中
				 //如果是，则跳过此数据
					int flag=addLinePoint(line,point);
					if(flag==1){
					    count1++;
					}
		  }
		return String.valueOf(count1);
	}

@Override
public String getProjectName(String hql) {/*
	Map<String, Object> map = new HashMap<String, Object>();
	StringBuffer buf = new StringBuffer(qsql);
	if (!StringUtils.isEmpty(projectvo.getPname())) {
		buf.append(" and pname like '%").append(projectvo.getPname()).append("%'");
	}
	if (!StringUtils.isEmpty(projectvo.getPtype())) {
		buf.append(" and ptype ='").append(projectvo.getPtype()).append("'");
	}
	if (projectvo.getPgroupid()!=0) {
		buf.append(" and tprojectgroup.id =").append(projectvo.getPgroupid());
	}
	if (projectvo.getEntid()!=0) {
		buf.append(" and entid=").append(projectvo.getEntid());
	}
	buf.append(" order by id desc ");
	List<ProjectVo> pvolist=new ArrayList<ProjectVo>();
	if (queryResult != null && queryResult.getResultList().size() > 0) {
		for (TProject te : queryResult.getResultList()) {
			ProjectVo pvo = new ProjectVo();
			BeanUtils.copyProperties(te,pvo,new String[]{"equipmentprojects"});
			if(te.getTprojectgroup()!=null){
				pvo.setPgroupid(te.getTprojectgroup().getId());
				pvo.setPgroupname(te.getTprojectgroup().getPgname());
			}
			pvolist.add(pvo);
		}
	}
	map.put("total", queryResult.getTotalRecord());
	map.put("rows", pvolist);
	return map;
*/
		return null;
}

@Override
public List<TBaseInfo> getCityList(int entid) {
	StringBuffer buf=new StringBuffer(" from TBaseInfo where ");
	if(entid==0){
		buf.append(" 1=1");
	}else{
		buf.append(" entid in(0,").append(entid).append(")");
	}
	buf.append(" group by bcity ");
	List<TBaseInfo> cityList = baseDao.find(buf.toString());
	return cityList;
}


@Override
public List<TBaseInfo> getRegionList(int entid) {
	StringBuffer buf=new StringBuffer(" from TBaseInfo where ");
	if(entid==0){
		buf.append(" 1=1");
	}else{
		buf.append(" entid in(0,").append(entid).append(")");
	}
	buf.append(" group by  bregion");
	List<TBaseInfo> bregionList = baseDao.find(buf.toString());
	return bregionList;
}

@Override
public String test(String ename, int entid, String flag1) {
	String flag="1";
	System.err.println(flag1);
	//将巡检上报信息表中的设备主键改成基础信息主键
	if("1".equals(flag1)){
		int i=0;
		
			List<TInspectItemReport> itemList = baseDao
					.find(" from TInspectItemReport");
			if (itemList != null && itemList.size() > 0) {
				for (TInspectItemReport item : itemList) {
					if(item.getId()==10){
						//			System.out.println("ddddddddd");
					}
					int eid = item.getXequid();
					TEquipment equip = (TEquipment) baseDao
							.find1(" from TEquipment where id=" + eid +" and entid="+item.getEntid());
					int baseid = 0;
					//	System.out.println(eid);
					i++;
					try {
					if(eid==13563){
						//		System.out.println("i"+i+"\tsize="+itemList.size());
					}
					
						if (equip != null) {
							Set<TBaseInfoEquipment> eSet = equip
									.getBaseinfoequipments();

							Iterator<TBaseInfoEquipment> beset = eSet.iterator();
							if (beset.hasNext()) {
								TBaseInfoEquipment be = (TBaseInfoEquipment) beset.next();
								// 将t_base_info装进来
								baseid = be.getTbaseinfo().getId();
								if(baseid==13561){
								//	System.out.println("dddddddddddddddd");
								}
							//	System.out.println(baseid);
								item.setXequid(baseid);
								baseDao.save(item);
							}
						}
						else{
							List<TInspectItemDetailReport> detailList=item.getInspectreportdetailmsgs();
							if(detailList!=null&&detailList.size()>0){
								for(TInspectItemDetailReport detail:detailList){
									baseDao.delete(TInspectItemDetailReport.class,detail.getId());
								}
								baseDao.delete(TInspectItemReport.class,item.getId());
							}
							else{
								baseDao.delete(TInspectItemReport.class,item.getId());
							}
							
						}
					} catch (Exception e) {
						// TODO: handle exception
						flag="2";
						e.printStackTrace();
						System.out.println(e.getMessage());
					}
					
				}
			}
	}
	//将巡检问题中的设备主键改成基础信息主键
	else if("2".equals(flag1)){
		try {
			List<TInspectProblem>  proList = baseDao.find(" from TInspectProblem");
			if(proList!=null&&proList.size()>0){
				for(TInspectProblem pro:proList){
					String id=pro.getProsite();
					TEquipment equip = (TEquipment) baseDao.find1(" from TEquipment where id="+Integer.parseInt(id) +" and entid="+pro.getEntid());
					int baseid=0;
					if (equip != null) {
						Set<TBaseInfoEquipment> eSet = equip.getBaseinfoequipments();
						Iterator<TBaseInfoEquipment> beset = eSet.iterator();
						if (beset.hasNext()) {
							TBaseInfoEquipment be = (TBaseInfoEquipment) beset.next();
							baseid = be.getTbaseinfo().getId();
							pro.setProsite(String.valueOf(baseid));
							baseDao.save(pro);
							
						}
					}
					else{
						baseDao.delete(TInspectProblem.class,pro.getId());
					}
		
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			flag="2";
			e.printStackTrace();
			System.out.println(e.getMessage());
		}

	}
	else{
		
	}

	return flag;
}

@Override
public Map<String, Object> findUnicomDatagrid(UnicomVo unicomvo, int page,
		int rows, String qsql) {
	Map<String, Object> map = new HashMap<String, Object>();

	StringBuffer buf = new StringBuffer();
//	buf.append("SELECT b.id, GROUP_CONCAT(CAST(c.poname AS CHAR)),a.lineid ,b.lname "+
//	 " FROM t_line b  LEFT JOIN  t_line_point a ON ( a.lineid=b.id ) LEFT JOIN  t_point c  ON (a.pointid=c.id ) ") ;
//	 
//	if("0".equals(entid)){
//		buf.append(" where 1=1");
//	}else{
//		buf.append(" where  b.entid in (0,").append(entid).append(")");
//	}
//	if (!StringUtils.isEmpty(linevo.getLname())) {
//		buf.append(" and b.lname like '%").append(linevo.getLname()).append("%'");
//	}
//	if (!StringUtils.isEmpty(linevo.getLnumber())) {
//		buf.append(" and b.lnumber ='").append(linevo.getLnumber()).append("'");
//	}
//	if (linevo.getEntid()!=0) {
//		buf.append(" and b.entid=").append(linevo.getEntid());
//	}
	
//	    id : rows[0].id,
//		company : rows[0].ponumber,
//		applyid : rows[0].poname,
//		baseid : rows[0].poposx,
//		basename : rows[0].poposy,
//		baseaddress : rows[0].poaddress
		
	buf.append(" select  id,company,applyid,baseid,basename,baseaddress from unicominfo ");
	
	String sql=buf.toString()+ "LIMIT "+(page - 1) * rows+","+rows ;
	//System.out.println(buf.toString());
	SQLQuery pgquery = baseDao.getHibernaSession().createSQLQuery(sql);
	List<Object[]> lvolist = pgquery.list();

	List<UnicomVo> list = toUnicomVoObj(lvolist);
	System.out.println("inspectitemserviceimpl++++: "+ list.size());
	
	
	SQLQuery listsum = baseDao.getHibernaSession().createSQLQuery(buf.toString());
	
	System.out.println("listsum.list().size(): "+listsum.list().size());
	if(list==null){
		list=new ArrayList<UnicomVo>();
	}
	map.put("total", listsum.list().size());
	map.put("rows", list);
	return map;
}

//froms报表
@Override    
public Map<String, Object> findFormsDatagrid(FormsVo formsvo, int page,
		int rows, String qsql) {

	Map<String, Object> map = new HashMap<String, Object>();

	StringBuffer buf = new StringBuffer();

		
	buf.append("select id,operator,administrativedivision,license,baseid,basename,address,remark from unicominfo ");
	
	
	//模糊查询
	//System.out.println("运营商： "+formsvo.getOperator());
	if (!StringUtils.isEmpty(formsvo.getOperator())) {
		buf.append(" where operator like '%").append(formsvo.getOperator().replace(",", "")).append("%'");
	}else
	{
		buf.append(" where 1=1 ");
	}
	
	//System.out.println("区县名： "+formsvo.getAdministrativedivision());
	if (!StringUtils.isEmpty(formsvo.getAdministrativedivision())) {
		buf.append(" and administrativedivision like '%").append(formsvo.getAdministrativedivision().replace(",", "")).append("%'");
	}
	
	//System.out.println("申请编号： "+formsvo.getLicense());
	if (!StringUtils.isEmpty(formsvo.getLicense())) {
		buf.append(" and license like '%").append(formsvo.getLicense().replace(",", "")).append("%' ");
	}
	
	
	String sql=buf.toString()+ "LIMIT "+(page - 1) * rows+","+rows ;
	
	System.out.println("(InspectItemServiceImpl.java2732)sql查询语句:"+sql);
	
	SQLQuery pgquery = baseDao.getHibernaSession().createSQLQuery(sql);
	List<Object[]> lvolist = pgquery.list();

	List<FormsVo> list = toFormsVoObj(lvolist);

	
	SQLQuery listsum = baseDao.getHibernaSession().createSQLQuery(buf.toString());
	
	if(list==null){
		list=new ArrayList<FormsVo>();
	}
	map.put("total", listsum.list().size());
	map.put("rows", list);
	return map;
}

//forms查询
public Map<String, Object> findFormsDatagrid2(FormsVo formsvo, int page,
		int rows, String qsql) {

	Map<String, Object> map = new HashMap<String, Object>();

	StringBuffer buf = new StringBuffer();

		
	buf.append(" select id,operator,administrativedivision,license,baseid,basename,address,remark from unicominfo ");
	
	String sql=buf.toString()+ "LIMIT "+(page - 1) * rows+","+rows ;
	
	System.out.println(page+"  页  "+rows+"  行。 "+sql);
	
	SQLQuery pgquery = baseDao.getHibernaSession().createSQLQuery(sql);
	List<Object[]> lvolist = pgquery.list();

	List<FormsVo> list = toFormsVoObj(lvolist);

	
	SQLQuery listsum = baseDao.getHibernaSession().createSQLQuery(buf.toString());
	
	if(list==null){
		list=new ArrayList<FormsVo>();
	}
	map.put("total", listsum.list().size());
	map.put("rows", list);
	return map;
}



@Override
public void removeStation(String ids) {
	if(!StringUtils.isEmpty(ids)){
		for(String id : ids.split(",")){
			baseDao.delete(TStation.class,Integer.parseInt(id));
		}
	}
}


@Override
public Map<String, Object> findStationDatagrid(StationVo stationvo, int page,int rows, String qsql) {
	
	System.out.println("############findStationDatagrid实现类###########");
	
	
	Map<String, Object> map = new HashMap<String, Object>();
	StringBuffer buf = new StringBuffer(qsql);
	if (!StringUtils.isEmpty(stationvo.getStname())) {
		buf.append(" and stname like '%").append(stationvo.getStname()).append("%'");
	}
	if (!StringUtils.isEmpty(stationvo.getStnumber())) {
		buf.append(" and stnumber ='").append(stationvo.getStnumber()).append("'");
	}
//	if (stationvo.getEntid()!=0) {
//		buf.append(" and entid=").append(stationvo.getEntid());
//	}
	QueryResult<TStation> queryResult = baseDao.getQueryResult(TStation.class, buf.toString(), (page - 1) * rows, rows,null, null);
	List<StationVo> svolist=new ArrayList<StationVo>();
	if (queryResult != null && queryResult.getResultList().size() > 0) {
		for (TStation ts : queryResult.getResultList()) {
			StationVo svo = new StationVo();
			BeanUtils.copyProperties(ts,svo);
			svolist.add(svo);
		}
	}
	map.put("total", queryResult.getTotalRecord());
	map.put("come", (page - 1) * rows);
	map.put("to", rows * page);
	map.put("rows", svolist);
	
	System.out.println("############svolist###########"+svolist);
	return map;
}



public Map<String, Object> findStationDatagridNoPage(StationVo stationvo,String qsql) {
	Map<String, Object> map = new HashMap<String, Object>();
	StringBuffer buf = new StringBuffer(qsql);
	if (!StringUtils.isEmpty(stationvo.getStname())) {
		buf.append(" and stname like '%").append(stationvo.getStname()).append("%'");
	}
	if (!StringUtils.isEmpty(stationvo.getStnumber())) {
		buf.append(" and stnumber ='").append(stationvo.getStnumber()).append("'");
	}
//	if (stationvo.getEntid()!=0) {
//		buf.append(" and entid=").append(stationvo.getEntid());
//	}
	QueryResult<TStation> queryResult = baseDao.getQueryResultNotPage(TStation.class, buf.toString(),null, null);
	List<StationVo> svolist=new ArrayList<StationVo>();
	if (queryResult != null && queryResult.getResultList().size() > 0) {
		for (TStation ts : queryResult.getResultList()) {
			StationVo svo = new StationVo();
			BeanUtils.copyProperties(ts,svo);

			svolist.add(svo);
		}
	}
	map.put("total", queryResult.getTotalRecord());
	map.put("rows", svolist);
	return map;
}

public Long countStationName(String sname) {
	return baseDao.count("select count(*) from TStation t where t.stname = ?", new Object[] { sname });
}

@Override
public void addStation(StationVo stationvo) {
	TStation station=new TStation();
    BeanUtils.copyProperties(stationvo,station);
    station.setEntid(stationvo.getEntid());
    baseDao.save(station);
    //this.addPointEquipment(pointvo, point);
}

public void editStation(StationVo stationvo) {
	TStation station=baseDao.get(TStation.class,stationvo.getId());
   // BeanUtils.copyProperties(stationvo,station,new String[]{"id","entid"});
	 BeanUtils.copyProperties(stationvo,station);
    baseDao.update(station);
}




	
}
