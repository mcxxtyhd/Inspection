package com.inspect.service.impl;

import java.io.File;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.SQLQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.inspect.constant.Constant;
import com.inspect.dao.BaseDaoI;
import com.inspect.model.baseinfo.TBaseInfo;
import com.inspect.model.basis.TEquipment;
import com.inspect.model.basis.TGroup;
import com.inspect.model.basis.TPlanTask;
import com.inspect.model.monitor.TInspectItemDetailReport;
import com.inspect.model.monitor.TInspectItemReport;
import com.inspect.model.problem.TInspectProblem;
import com.inspect.model.problem.TRepairTask;
import com.inspect.service.InspectItemServiceI;
import com.inspect.service.ProblemServiceI;
import com.inspect.util.common.DateUtils;
import com.inspect.util.common.QueryResult;
import com.inspect.util.common.StringUtils;
import com.inspect.util.excel.Eoip;
import com.inspect.util.excel.Eoiprule;
import com.inspect.vo.problem.ProblemVo;
import com.inspect.vo.problem.RepairTaskVo;
import com.inspect.vo.summary.SummaryFormVo;
import com.itextpdf.text.pdf.PdfStructTreeController.returnType;
@Service("problemService")
@Transactional(propagation = Propagation.REQUIRED,readOnly = false,rollbackFor = Exception.class)
public class ProblemServiceImpl implements ProblemServiceI {
	@Resource
	private BaseDaoI baseDao;

	public BaseDaoI getBaseDao() {
		return baseDao;
	}

	public void setBaseDao(BaseDaoI baseDao) {
		this.baseDao = baseDao;
	}

	@Resource
	private InspectItemServiceI inspectItemService;
	@Override
	public void addProblem(ProblemVo problemVo) {
		TInspectProblem problem=new TInspectProblem();
	    BeanUtils.copyProperties(problemVo,problem);
	    problem.setEntid(problemVo.getEntid());
	    problem.setCreatetime(DateUtils.formatDateTime(new Date()));
	    baseDao.save(problem);
	}

	@Override
	public void editProblem(ProblemVo problemVo) {
		TInspectProblem problem=baseDao.get(TInspectProblem.class,problemVo.getId());
	    BeanUtils.copyProperties(problemVo,problem,new String[]{"id","entid","createtime"});
	    baseDao.update(problem);
	}

	@Override
	public Map<String, Object> findProblemDatagrid(ProblemVo problemVo,int page, int rows, String qsql,String buf1) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer buf = new StringBuffer(qsql);
		if (problemVo.getProitaskid()!=0) {
			buf.append(" and proitaskid =").append(problemVo.getProitaskid());
		}
		if (problemVo.getIuserid()!=0) {
			buf.append(" and iuserid =").append(problemVo.getIuserid());
		}
		if (problemVo.getEntid()!=0) {
			buf.append(" and entid =").append(problemVo.getEntid());
		}
		if (StringUtils.isNotEmpty(problemVo.getProtype())) {
			buf.append(" and protype like'%").append(problemVo.getProtype()).append("%'");
		}
		if (StringUtils.isNotEmpty(problemVo.getProcycle())) {
			buf.append(" and procycle='").append(problemVo.getProcycle()).append("'");
		}
		
		//巡检站点（设备名称）
		if (StringUtils.isEmpty(problemVo.getProsite())) {
			
		}
		//当设备名称作为查询条件时
		else{
			//当输入设备名称并找到对应的设备时,buf1.length()>0,后则buf1.length()=0
			 if(buf1.length()>0){
				buf.append(" and prosite in (").append(buf1.toString()).append(")");
			}
			
			else {
				buf.append(" and 1=0 ");//inspectmessageVo.getXename()
			}
		}
		buf.append(" order by id desc");
		//	System.out.println(buf.toString());
		QueryResult<TInspectProblem> queryResult = baseDao.getQueryResult(TInspectProblem.class, buf.toString(), (page - 1) * rows, rows,null, null);
		List<ProblemVo> problemVolist = new ArrayList<ProblemVo>();
		if (queryResult != null && queryResult.getResultList().size() > 0) {
			for (TInspectProblem problem : queryResult.getResultList()) {
				ProblemVo proVo = new ProblemVo();
				BeanUtils.copyProperties(problem, proVo);
				proVo.setIusername(baseDao.getEntityById(TGroup.class,problem.getIuserid())!=null?baseDao.getEntityById(TGroup.class,problem.getIuserid()).getGname():"");
				proVo.setProitaskname(baseDao.getEntityById(TPlanTask.class,problem.getProitaskid())!=null?baseDao.getEntityById(TPlanTask.class,problem.getProitaskid()).getPname():"");
			//	baseDao.getEntityById(TEquipment.class,Integer.parseInt(problem.getProsite()))!=null?;
				TEquipment equip=baseDao.getEntityById(TEquipment.class,Integer.parseInt(problem.getProsite()));
				if(equip!=null){
					proVo.setPrositename(equip.getEname());
					proVo.setPrositenum(equip.getEnumber());
				}
				
				problemVolist.add(proVo);
			}
		}
		map.put("total", queryResult.getTotalRecord());
		map.put("rows", problemVolist);
		return map;
	}

	@Override
	public Map<String, Object> findProblemDatagrid1(ProblemVo problemVo,int page, int rows, String qsql,String buf1,String buf2) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer buf = new StringBuffer(qsql);
		if (problemVo.getProitaskid()!=0) {
			buf.append(" and proitaskid =").append(problemVo.getProitaskid());
		}
		if (problemVo.getIuserid()!=0) {
			buf.append(" and iuserid =").append(problemVo.getIuserid());
		}
		if (problemVo.getEntid()!=0) {
			buf.append(" and entid =").append(problemVo.getEntid());
		}
		if (StringUtils.isNotEmpty(problemVo.getProtype())) {
			buf.append(" and protype like'%").append(problemVo.getProtype()).append("%'");
		}
		if (StringUtils.isNotEmpty(problemVo.getProcycle())) {
			buf.append(" and procycle='").append(problemVo.getProcycle()).append("'");
		}
		
		//巡检站点（设备名称）
		if (StringUtils.isEmpty(problemVo.getProsite())) {
			
		}
		//当设备名称作为查询条件时
		else{
			//当输入设备名称并找到对应的设备时,buf1.length()>0,后则buf1.length()=0
			 if(buf1.length()>0){
				buf.append(" and prosite in (").append(buf1.toString()).append(")");
			}
			
			else {
				buf.append(" and 1=0 ");//inspectmessageVo.getXename()
			}
		}
		//巡检站点（设备名称）
		if (StringUtils.isEmpty(problemVo.getPrositenum())) {
			
		}
		//当设备名称作为查询条件时
		else{
			//当输入设备名称并找到对应的设备时,buf1.length()>0,后则buf1.length()=0
			 if(buf2.length()>0){
				buf.append(" and prosite in (").append(buf2.toString()).append(")");
			}
			
			else {
				buf.append(" and 1=0 ");//inspectmessageVo.getXename()
			}
		}
		buf.append(" order by id desc");
		//	System.out.println(buf.toString());
		QueryResult<TInspectProblem> queryResult = baseDao.getQueryResult(TInspectProblem.class, buf.toString(), (page - 1) * rows, rows,null, null);
		List<ProblemVo> problemVolist = new ArrayList<ProblemVo>();
		if (queryResult != null && queryResult.getResultList().size() > 0) {
			for (TInspectProblem problem : queryResult.getResultList()) {
				ProblemVo proVo = new ProblemVo();
				BeanUtils.copyProperties(problem, proVo);
				proVo.setIusername(baseDao.getEntityById(TGroup.class,problem.getIuserid())!=null?baseDao.getEntityById(TGroup.class,problem.getIuserid()).getGname():"");
				proVo.setProitaskname(baseDao.getEntityById(TPlanTask.class,problem.getProitaskid())!=null?baseDao.getEntityById(TPlanTask.class,problem.getProitaskid()).getPname():"");
			//	baseDao.getEntityById(TEquipment.class,Integer.parseInt(problem.getProsite()))!=null?;
				TBaseInfo binfo=baseDao.getEntityById(TBaseInfo.class,Integer.parseInt(problem.getProsite()));
				if(binfo!=null){
					proVo.setPrositename(binfo.getBname());
					proVo.setPrositenum(binfo.getBnumber());
				}
				
				problemVolist.add(proVo);
			}
		}
		map.put("total", queryResult.getTotalRecord());
		map.put("rows", problemVolist);
		return map;
	}
	@Override
	public void removeProblem(String ids) {
		if(!StringUtils.isEmpty(ids)){
			for(String id : ids.split(",")){
				baseDao.delete(TInspectProblem.class,Integer.parseInt(id));
			}
		}
	}

	@Override
	public void addRepairTask(RepairTaskVo repairTaskVo) {
		TRepairTask trepair=new TRepairTask();
		BeanUtils.copyProperties(repairTaskVo,trepair);
		trepair.setRsenddate(DateUtils.gettimeOnlyOwnByGPS());
		trepair.setRflag(1);//未处理
		trepair.setRternumber(getTerniateByGid(repairTaskVo.getRgid()));
		baseDao.save(trepair);
	}

	@Override
	public void editRepairTask(RepairTaskVo repairTaskVo) {
		TRepairTask trepair=baseDao.getEntityById(TRepairTask.class,repairTaskVo.getId());
		BeanUtils.copyProperties(repairTaskVo,trepair,new String[]{"id","entid","rsenddate"});
		trepair.setRternumber(getTerniateByGid(repairTaskVo.getRgid()));//终端编号
		baseDao.update(trepair);
	}
	
	private String getTerniateByGid(int gid){
		String hql="select rpterminateid from TTerminateStatusReport where flag=1 and rpgroupid="+gid +"order by id desc";
		return (String) baseDao.find(hql).get(0);
	}

	@Override
	public Map<String, Object> findRepairTaskDatagrid(RepairTaskVo repairTaskVo, int page, int rows, String qsql) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer buf = new StringBuffer(qsql);
		if (repairTaskVo.getRgid()!=0) {
			buf.append(" and rgid=").append(repairTaskVo.getRgid());
		}
		if (repairTaskVo.getEntid()!=0) {
			buf.append(" and entid =").append(repairTaskVo.getEntid());
		}
		if (repairTaskVo.getRflag()!=0) {
			buf.append(" and rflag =").append(repairTaskVo.getRflag());
		}
		if(!StringUtils.isEmpty(repairTaskVo.getRcontent())){
			buf.append(" and rcontent like '%").append(repairTaskVo.getRcontent()).append("%'");
		}
		buf.append(" order by id desc");
		QueryResult<TRepairTask> queryResult = baseDao.getQueryResult(TRepairTask.class, buf.toString(), (page - 1) * rows, rows, null, null);
		List<RepairTaskVo> repairVolist = new ArrayList<RepairTaskVo>();
		if (queryResult != null && queryResult.getResultList().size() > 0) {
			for (TRepairTask repair : queryResult.getResultList()) {
			
				RepairTaskVo proVo = new RepairTaskVo();
				BeanUtils.copyProperties(repair, proVo);
				//判断是否存在截至日期，若存在，则比较是否超期，否则统一改成为超期
				if(StringUtils.isNotEmpty(proVo.getRendtime())){
						SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH-mm");
						//判断处理日期是否为空，如果不是，则与截止日期比较
						if(StringUtils.isNotEmpty(proVo.getRrepdate())){
							try {
								Date nowdate1 = ft.parse(proVo.getRrepdate());
								Date enddate1 = ft.parse(proVo.getRendtime());
								long diff = nowdate1.getTime() - enddate1.getTime();
								//如果diff大于0，表示超时，小于0，表示未超时
								 if(diff>0){
									 proVo.setRcomflag(0);//超时
								 }
								 else{
									 proVo.setRcomflag(1);//未超时
								 }
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
						}
						else{
							try {
								Date nowdate1 = ft.parse(ft.format(new Date()));
								Date enddate1 = ft.parse(proVo.getRendtime());
								long diff = nowdate1.getTime() - enddate1.getTime();
								//如果diff大于0，表示超时，小于0，表示未超时
								 if(diff>0){
									 proVo.setRcomflag(0);//超时
								 }
								 else{
									 proVo.setRcomflag(1);//未超时
								 }
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
						}
				}
				else{
					 proVo.setRcomflag(1);//未超时
				}
				proVo.setRgname(baseDao.getEntityById(TGroup.class,repair.getRgid())!=null?baseDao.getEntityById(TGroup.class,repair.getRgid()).getGname():"");
				repairVolist.add(proVo);
			}
		}
		map.put("total", queryResult.getTotalRecord());
		map.put("rows", repairVolist);
		return map;
	}

	@Override
	public void removeRepairTask(String ids) {
		if(!StringUtils.isEmpty(ids)){
			for(String id : ids.split(",")){
				baseDao.delete(TRepairTask.class,Integer.parseInt(id));
			}
		}
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<TGroup> getGroupOnLineList(int entid){
		StringBuffer buf=new StringBuffer("SELECT DISTINCT g.id,g.gname FROM t_group AS g,t_terminate_status_report AS z WHERE g.id=z.rpgroupid AND z.flag=1");
		buf.append(" AND z.rplogintime LIKE '%").append(DateUtils.formatDate(new Date())).append("%'");
		if(entid==0){
			buf.append(" and 1=1");
		}else{
			buf.append(" and z.entid in(0,").append(entid).append(")");
		}
		//buf.append(" LIMIT 1");
		List<TGroup>  l=baseDao.getJdbcTemplate().queryForList(buf.toString());
		return l;
	}

	/**
	 * 在GPS中发送突发任务
	 */
	@Override
	public TRepairTask getRepairTask(String termid) {
		String date=DateUtils.getFormatDayDate();
		String hql="from TRepairTask where rternumber='"+termid+"' and rsenddate like '"+ date+"%' and ( rflag=1 or rflag=2)";
		List<TRepairTask> repairList=baseDao.find(hql);
		if(repairList!=null&&repairList.size()>0){
			TRepairTask repairTask=repairList.get(0);
			return repairTask;
		}
		return null;
	}
/**
 * 上报突发任务
 */
	@Override
	public TRepairTask getRepairTask(int id) {
		TRepairTask repairTask=baseDao.get(TRepairTask.class, id);
		return repairTask;
	}

	@SuppressWarnings("unchecked")
	public String problemtoexcel(ProblemVo problemVo,String qsql,int entid)
	{	
		List<Object> problemVolist=getproblemVolist(problemVo, qsql, entid) ;
		if(problemVolist==null){
			return null;
		}
		try{
			//设置规则
			Eoip eoip=setProblemeoip();
			//获取路径
			List<String> url = eoip.db2excel(problemVolist);
			//	System.out.println( url.get(0));
			return url.get(0);
		}
		catch(Exception e){
			//e.printStackTrace();
		}
		
		return null;
	}
	/**
	 * 导出时获取数据
	 * @param problemVo
	 * @param qsql
	 * @param entid
	 * @return
	 */
	public List<Object> getproblemVolist(ProblemVo problemVo,String qsql,int entid) {
		//总的查询sql
		String sql=" ";
		String buf1="";
		String buf2="";
		StringBuffer buf = new StringBuffer(qsql);
		//通过设备名称查找equipment主键id
		if(!StringUtils.isEmpty(problemVo.getProsite())){
			buf1=inspectItemService.getEquipment1Byenameornum(problemVo.getProsite().trim(), entid,0);
		}
		//通过设备编号查找equipment主键id
		if(!StringUtils.isEmpty(problemVo.getPrositenum())){
			buf2=inspectItemService.getEquipment1Byenameornum(problemVo.getPrositenum().trim(), entid,1);
		}
		if (problemVo.getProitaskid()!=0) {
			buf.append(" and proitaskid =").append(problemVo.getProitaskid());
		}
		if (problemVo.getIuserid()!=0) {
			buf.append(" and iuserid =").append(problemVo.getIuserid());
		}
		if (problemVo.getEntid()!=0) {
			buf.append(" and entid =").append(problemVo.getEntid());
		}
		if (StringUtils.isNotEmpty(problemVo.getProtype())) {
			buf.append(" and protype like'%").append(problemVo.getProtype()).append("%'");
		}
		if (StringUtils.isNotEmpty(problemVo.getProcycle())) {
			buf.append(" and procycle='").append(problemVo.getProcycle()).append("'");
		}
		
		//巡检站点（设备名称）
		if (StringUtils.isEmpty(problemVo.getProsite())) {
			
		}
		//当设备名称作为查询条件时
		else{
			//当输入设备名称并找到对应的设备时,buf1.length()>0,后则buf1.length()=0
			 if(buf1.length()>0){
				buf.append(" and prosite in (").append(buf1.toString()).append(")");
			}
			
			else {
				buf.append(" and 1=0 ");//inspectmessageVo.getXename()
			}
		}
		//巡检站点（设备名称）
		if (StringUtils.isEmpty(problemVo.getPrositenum())) {
			
		}
		//当设备名称作为查询条件时
		else{
			//当输入设备名称并找到对应的设备时,buf1.length()>0,后则buf1.length()=0
			 if(buf2.length()>0){
				buf.append(" and prosite in (").append(buf2.toString()).append(")");
			}
			
			else {
				buf.append(" and 1=0 ");//inspectmessageVo.getXename()
			}
		}
		buf.append(" order by id desc");
		
		sql = "from TInspectProblem where "+buf.toString();
		//System.out.println(sql);
		List<TInspectProblem> rowlist = baseDao.find(sql);
		List<Object> problemVolist = new ArrayList<Object>();
		int index=1;
		if (rowlist != null && rowlist.size() > 0) {
			for (TInspectProblem problem :rowlist) {
				ProblemVo proVo = new ProblemVo();
				BeanUtils.copyProperties(problem, proVo);
				//设置导出的序号
				proVo.setIndex(index);
				index++;
				proVo.setIusername(baseDao.getEntityById(TGroup.class,problem.getIuserid())!=null?baseDao.getEntityById(TGroup.class,problem.getIuserid()).getGname():"");
				proVo.setProitaskname(baseDao.getEntityById(TPlanTask.class,problem.getProitaskid())!=null?baseDao.getEntityById(TPlanTask.class,problem.getProitaskid()).getPname():"");
				TBaseInfo binfo=baseDao.getEntityById(TBaseInfo.class,Integer.parseInt(problem.getProsite()));
				if(binfo!=null){
					proVo.setPrositename(binfo.getBname());
					proVo.setPrositenum(binfo.getBnumber());
				}
				
				problemVolist.add(proVo);
			}
		}
		return problemVolist;
	}
	/**
	 * 设置巡检问题导出规则
	 * @param problemVolist
	 * @return
	 */
	public Eoip setProblemeoip(){
		//导出规则设置
		 Eoiprule rule = new Eoiprule();
		 Boolean mode=false;
		 //枚举型数据设置
		 //绝对模式:true;列模式：false
		 if(0==0){
			  mode = false;
				 rule.setExpd2e("index,a1;proitaskname,b1;procycle,c1;prositenum,d1;prositename,e1;protype,f1;prodesc,g1;iusername,h1;createtime,i1;"); 
				 rule.setExpfilename(Constant.TEMP_FILE_URL+File.separator+"巡检问题.xlsx");/////////临时文件文件名
				 rule.setExptemplatefile(Constant.MODEL_FILE_URL+File.separator+"巡检问题模版.xlsx");//模版名称
				 rule.setExpstartcol(1);//从某某行开始
				 //set type:
				 String setfieldvalue = "pname,pvalue";
				 rule.setSetfieldvalue(setfieldvalue);
		 }
		
		 rule.setExpmode(mode);
		 rule.setExppostfixrule("name,bname");
		 rule.setExpnumsheetperwb(1);//sheet导出数目
		 Eoip eoip = new Eoip();
		 eoip.setErule(rule);
		 return eoip;
		
	
	}

	@Override
	public String repairTaskVotoexcel(RepairTaskVo repairTaskVo, String qsql) {	
		List<Object> repairTaskVolist=getrepairTaskVolist(repairTaskVo, qsql) ;
		if(repairTaskVolist==null){
			return null;
		}
		try{
			//设置规则
			Eoip eoip=setrepairtaskeoip();
			//获取路径
			List<String> url = eoip.db2excel(repairTaskVolist);
			//	System.out.println( url.get(0));
			return url.get(0);
		}
		catch(Exception e){
			//e.printStackTrace();
		}
		
		return null;
	}
	/**
	 * 导出时获取数据
	 * @param problemVo
	 * @param qsql
	 * @param entid
	 * @return
	 */
	public List<Object> getrepairTaskVolist(RepairTaskVo repairTaskVo,String qsql) {
		//总的查询sql
		StringBuffer buf = new StringBuffer(qsql);
		if (repairTaskVo.getRgid()!=0) {
			buf.append(" and rgid=").append(repairTaskVo.getRgid());
		}
		if (repairTaskVo.getEntid()!=0) {
			buf.append(" and entid =").append(repairTaskVo.getEntid());
		}
		if (repairTaskVo.getRflag()!=0) {
			buf.append(" and rflag =").append(repairTaskVo.getRflag());
		}
		if(!StringUtils.isEmpty(repairTaskVo.getRcontent())){
			buf.append(" and rcontent like '%").append(repairTaskVo.getRcontent()).append("%'");
		}
		buf.append(" order by id desc");
		String sql = "from TRepairTask where "+buf.toString();
		
		List<TRepairTask> rowlist = baseDao.find(sql);
		List<Object> repairTaskVolist = new ArrayList<Object>();
		int index=1;
		if (rowlist != null && rowlist.size() > 0) {
			for (TRepairTask r :rowlist) {
				RepairTaskVo rVo = new RepairTaskVo();
				BeanUtils.copyProperties(r, rVo);
				//设置导出的序号
				rVo.setIndex(index);
				index++;
				//设置紧急任务状态（从数字改成对应描述）
				 if(rVo.getRflag()==2){
					rVo.setRflag1("已下发");
				}
				else if(rVo.getRflag()==3){
					rVo.setRflag1("已回复");
				}
				else{
					rVo.setRflag1("未处理");
				}
				 //根据巡检组id获取巡检组名称
				TGroup g=baseDao.getEntityById(TGroup.class,rVo.getRgid());
				if(g!=null){
				rVo.setRgname(g.getGname());
				}
				repairTaskVolist.add(rVo);
			}
		}
		return repairTaskVolist;
	}
	/**
	 * 设置巡检问题导出规则
	 * @param problemVolist
	 * @return
	 */
	public Eoip setrepairtaskeoip(){
		//导出规则设置
		 Eoiprule rule = new Eoiprule();
		 Boolean mode=false;
		 //绝对模式:true;列模式：false
	
		 if(0==0){
			  mode = false;
				 rule.setExpd2e("index,a1;rcategory,b1;rcontent,c1;rgname,d1;rflag1,e1;rdesc,f1;rsenddate,g1;rrepdate,h1;rendtime,i1;"); 
				 rule.setExpfilename(Constant.TEMP_FILE_URL+File.separator+"抢修任务.xlsx");/////////临时文件文件名
				 rule.setExptemplatefile(Constant.MODEL_FILE_URL+File.separator+"抢修任务模版.xlsx");//模版名称
				 rule.setExpstartcol(1);//从某某行开始
				 //set type:
				 String setfieldvalue = "pname,pvalue";
				 rule.setSetfieldvalue(setfieldvalue);
		 }
		
		 rule.setExpmode(mode);
		 rule.setExppostfixrule("name,bname");
		 rule.setExpnumsheetperwb(1);//sheet导出数目
		 Eoip eoip = new Eoip();
		 eoip.setErule(rule);
		 return eoip;
		
	
	}
	/**
	 * 添加或修改问题上报信息时，判断是否存在终端和编号
	 * true 存在终端和编号  false 不存在终端或编号
	 */
	@Override
	public boolean isAddorEdit(String termid,String equipnum, int entid) {
		// TODO Auto-generated method stub
		
		boolean flag=false;
		String hql="select count(*) from t_terminal a,t_equipment b where a.termno='"+termid+"'and b.enumber='"+equipnum+"' and a.entid=b.entid and a.entid in(0,"+entid+")";
		//	System.out.println(hql);
		SQLQuery query=baseDao.getHibernaSession().createSQLQuery(hql);
		
		if(query!=null&&query.list().size()>0){
			//	System.out.println(query.list().get(0));
			BigInteger c=(BigInteger) query.list().get(0);
			String c1=String.valueOf(c);
			if(!"0".equals(c1)){
				flag=true;
			}
		}
		return flag;
	}
}
