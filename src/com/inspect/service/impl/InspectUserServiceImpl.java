package com.inspect.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.inspect.dao.BaseDaoI;
import com.inspect.model.basis.TGroup;
import com.inspect.model.basis.TInspectUser;
import com.inspect.model.basis.TTerminal;
import com.inspect.model.monitor.TTerminateStatusReport;
import com.inspect.model.system.Enterprise;
import com.inspect.service.InspectUserServiceI;
import com.inspect.util.common.QueryResult;
import com.inspect.util.common.StringUtils;
import com.inspect.vo.basis.GroupVo;
import com.inspect.vo.basis.InspectUserVo;
import com.inspect.vo.basis.TerminalVo;

@Service("inspectUserService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
public class InspectUserServiceImpl implements InspectUserServiceI {

	@Resource
	private BaseDaoI baseDao;

	public BaseDaoI getBaseDao() {
		return baseDao;
	}

	public void setBaseDao(BaseDaoI baseDao) {
		this.baseDao = baseDao;
	}

	@Override
	public void addGroup(GroupVo groupvo) {
		TGroup group = new TGroup();
		BeanUtils.copyProperties(groupvo, group);
		group.setEntid(groupvo.getEntid());
		baseDao.save(group);
	}

	@Override
	public void editGroup(GroupVo groupvo) {
		TGroup group = baseDao.get(TGroup.class, groupvo.getId());
		BeanUtils.copyProperties(groupvo, group, new String[] { "id", "entid" });
		baseDao.save(group);
	}

	public TGroup getGroup(String id) {
		if (StringUtils.isEmpty(String.valueOf(id))) {
			return null;
		}
		return baseDao.get(TGroup.class, id);
	}

	@Override
	public void removeGroup(String ids) {
		if (!StringUtils.isEmpty(ids)) {
			for (String id : ids.split(",")) {
				baseDao.delete(TGroup.class, Integer.parseInt(id));
			}
		}
	}

	@Override
	public Map<String, Object> findGroupterDatagrid(GroupVo groupvo, int page,
			int rows, String qsql) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer buf = new StringBuffer(qsql);
		if (!StringUtils.isEmpty(groupvo.getGname())) {
			buf.append(" and gname like '%").append(groupvo.getGname()).append("%'");
		}
		if (groupvo.getEntid()!=0) {
			buf.append(" and entid=").append(groupvo.getEntid());
		}
		buf.append(" order by id desc ");
		QueryResult<TGroup> queryResult = baseDao.getQueryResult(TGroup.class,buf.toString(), (page - 1) * rows, rows, null, null);
		List<GroupVo> gvolist = new ArrayList<GroupVo>();
		if (queryResult != null && queryResult.getResultList().size() > 0) {
			for (TGroup tu : queryResult.getResultList()) {
				GroupVo u = new GroupVo();
				BeanUtils.copyProperties(tu, u);
				u.setPcomname(baseDao.get(Enterprise.class, tu.getEntid())!=null?baseDao.get(Enterprise.class, tu.getEntid()).getEntname():"");
				gvolist.add(u);
			}
		}
		map.put("total", queryResult.getTotalRecord());
		map.put("rows", gvolist);
		return map;
	}

	@Override
	public void addInspectUser(InspectUserVo inspectuservo) {
		TInspectUser iuser = new TInspectUser();
		BeanUtils.copyProperties(inspectuservo, iuser);
		if (!StringUtils.isEmpty(inspectuservo.getGroupid())) {
			iuser.setTgroup(baseDao.get(TGroup.class, Integer.parseInt(inspectuservo.getGroupid())));
		}
		baseDao.save(iuser);
	}

	@Override
	public void editInspectUser(InspectUserVo inspectuservo) {
		TInspectUser iuser = baseDao.get(TInspectUser.class, inspectuservo.getId());
		BeanUtils.copyProperties(inspectuservo, iuser, new String[] { "id","entid" });
		if (!StringUtils.isEmpty(inspectuservo.getGroupid())) {
			iuser.setTgroup(baseDao.get(TGroup.class, Integer.parseInt(inspectuservo.getGroupid())));
		}
		baseDao.update(iuser);
	}

	@Override
	public TInspectUser getInspectUser(String id) {
		if (StringUtils.isEmpty(String.valueOf(id))) {
			return null;
		}
		return baseDao.get(TInspectUser.class,Integer.parseInt(id));
	}

	@Override
	public void removeInspectUser(String ids) {
		if (!StringUtils.isEmpty(ids)) {
			for (String id : ids.split(",")) {
				baseDao.delete(TInspectUser.class, Integer.parseInt(id));
			}
		}
	}

	@Override
	public Map<String, Object> findInspectUserDatagrid(
			InspectUserVo inspectuservo, int page, int rows, String qsql) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer buf = new StringBuffer(qsql);
		if (!StringUtils.isEmpty(inspectuservo.getIuname())) {
			buf.append(" and iuname like '%").append(inspectuservo.getIuname()).append("%'");
		}
		if (!StringUtils.isEmpty(inspectuservo.getIrealname())) {
			buf.append(" and irealname like '%").append(inspectuservo.getIrealname().trim()).append("%'");
		}
		if (!StringUtils.isEmpty(inspectuservo.getIunumber())) {
			buf.append(" and iunumber ='").append(inspectuservo.getIunumber()).append("'");
		}
		if (inspectuservo.getEntid()!=0) {
			buf.append(" and entid=").append(inspectuservo.getEntid());
		}
		if (StringUtils.isNotEmpty(inspectuservo.getGroupid())&&!inspectuservo.getGroupid().equals("0")) {
			buf.append(" and tgroup.id=").append(inspectuservo.getGroupid());
		}
		buf.append(" order by id desc ");
		QueryResult<TInspectUser> queryResult = baseDao.getQueryResult(TInspectUser.class, buf.toString(), (page - 1) * rows, rows,null, null);
		List<InspectUserVo> uvolist = new ArrayList<InspectUserVo>();
		if (queryResult != null && queryResult.getResultList().size() > 0) {
			for (TInspectUser tu : queryResult.getResultList()) {
				InspectUserVo u = new InspectUserVo();
				BeanUtils.copyProperties(tu, u);
				if (tu.getTgroup() != null) {
					u.setGroupname(tu.getTgroup().getGname());
					u.setGroupid(String.valueOf(tu.getTgroup().getId()));
					u.setPcomname(baseDao.get(Enterprise.class, tu.getEntid())!=null?baseDao.get(Enterprise.class, tu.getEntid()).getEntname():"");
				}
				uvolist.add(u);
			}
		}
		map.put("total", queryResult.getTotalRecord());
		map.put("rows", uvolist);
		return map;
	}
	@Override
	public Long count(String uname,int entid,int type) {
		Long ct=null;
		if(type==0){
			ct=baseDao.count("select count(*) from TGroup t where t.gname = ? and t.entid=?", new Object[] { uname,entid });
		}
		if(type==1){
			ct=baseDao.count("select count(*) from TInspectUser t where t.iuname = ? ", new Object[] { uname });
		}
		return ct;
	}

	@SuppressWarnings("unchecked")
	public List<TGroup> getGroupList(int qsql) {
		StringBuffer buf=new StringBuffer("select id,gname from t_group where ");
		if(qsql==0){
			buf.append(" 1=1");
		}else{
			buf.append(" entid in(0,").append(qsql).append(")");
		}
		buf.append(" order by id desc");
		List<TGroup> list = baseDao.getJdbcTemplate().queryForList(buf.toString());
		return list;
	}
	@SuppressWarnings("unchecked")
	public List<TGroup> getGroupList1(int qsql) {
		StringBuffer buf=new StringBuffer(" from TGroup where ");
		if(qsql==0){
			buf.append(" 1=1");
		}else{
			buf.append(" entid =").append(qsql);
		}
		buf.append(" order by id desc");
		List<TGroup> list = baseDao.find(buf.toString());
		return list;
	}

	public Long findcountBygid(int gid, int entid) {
		long cot = 0;
		cot = baseDao.count("select count(*) from TInspectUser t where t.groupid = ? and t.entid=?",new Object[] { gid, entid });
		return cot;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TInspectUser> getInspectUserList(int qsql) {
		StringBuffer buf=new StringBuffer("select id,iuname,irealname from t_inspect_user where");
		if(qsql==0){
			buf.append(" 1=1");
		}else{
			buf.append(" entid in(0,").append(qsql).append(")");
		}
		List<TInspectUser> list=baseDao.getJdbcTemplate().queryForList(buf.toString());
		return list;
	}

	@Override
	public void addTerminal(TerminalVo terminalvo) {
		TTerminal terminal = new TTerminal();
		BeanUtils.copyProperties(terminalvo, terminal);
		terminal.setEntid(terminalvo.getEntid());
		baseDao.save(terminal);
	}

	@Override
	public void editTerminal(TerminalVo terminalvo) {
		TTerminal terminal = baseDao.get(TTerminal.class, terminalvo.getId());
		BeanUtils.copyProperties(terminalvo, terminal, new String[] { "id", "entid" });
		baseDao.save(terminal);
	}

	@Override
	public TTerminal getTerminal(String id) {
		if (StringUtils.isEmpty(String.valueOf(id))) {
			return null;
		}
		return baseDao.get(TTerminal.class, id);
	}
	@Override
	public TTerminal getTerminalByTermno(String termno) {
		List<TTerminal> tlist=baseDao.find(" from TTerminal where termno='"+termno+"'");
		TTerminal t=null;
		if(tlist!=null&&tlist.size()>0){
			t=tlist.get(0);
		}
		
		return t;
	}
	@Override
	public void removeTerminal(String ids) {
		if (!StringUtils.isEmpty(ids)) {
			for (String id : ids.split(",")) {
				baseDao.delete(TTerminal.class, Integer.parseInt(id));
			}
		}
	}

	@Override
	public Map<String, Object> findTerminalterDatagrid(TerminalVo terminalvo,
			int page, int rows, String qsql) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer buf = new StringBuffer(qsql);
		if (!StringUtils.isEmpty(terminalvo.getTname())) {
			buf.append(" and tname like '%").append(terminalvo.getTname()).append("%'");
		}
		if (!StringUtils.isEmpty(terminalvo.getTermno())) {
			buf.append(" and termno like '%").append(terminalvo.getTermno()).append("%'");
		}
		if (terminalvo.getEntid()!=0) {
			buf.append(" and entid=").append(terminalvo.getEntid());
		}
		buf.append(" order by id desc ");
		QueryResult<TTerminal> queryResult = baseDao.getQueryResult(TTerminal.class, buf.toString(), (page - 1) * rows, rows, null, null);
		List<TerminalVo> tvolist = new ArrayList<TerminalVo>();
		if (queryResult != null && queryResult.getResultList().size() > 0) {
			for (TTerminal tu : queryResult.getResultList()) {
				TerminalVo u = new TerminalVo();
				BeanUtils.copyProperties(tu, u);
				tvolist.add(u);
			}
		}
		map.put("total", queryResult.getTotalRecord());
		map.put("rows", tvolist);
		return map;
	}

	public List<TTerminal> getTerminalList(int entid) {
		List<TTerminal> list = new ArrayList<TTerminal>();
		String hql = "from TTerminal where entid=" + entid;
		List<TTerminal> l = baseDao.findByHqlAll(TTerminal.class, hql);
		if (l != null && l.size() > 0) {
			for (TTerminal a : l) {
				TTerminal ginfo = new TTerminal();
				ginfo.setId(a.getId());
				ginfo.setTname(a.getTname());
				list.add(ginfo);
			}
		}
		return list;
	}

	public Long getInspectUserCount(int eid) {
		long count;
		if(eid==0){
			count=baseDao.count("select count(*) from TInspectUser");
		}
		else{
			count=baseDao.count("select count(*) from TInspectUser t where t.entid = ?", new Object[] { eid });
		}
		return count;
	}
	/**
	 * @author liao
	 * 验证用户是否存在
	 * @param user
	 * @return
	 */
	@Override
	public TInspectUser checkTInspectUser(String iuname,String iupwd) {
		
		System.out.println(iuname+"-------"+iupwd);
		  List<TInspectUser> inspectUsers = baseDao.find("from TInspectUser u where u.iuname = '" + iuname
				  + "' and u.iupwd='" + iupwd+ "'");
		    if ((inspectUsers != null) && (inspectUsers.size() > 0)){
		    	System.out.println("用户名和密码匹配成功！");
		    	return ((TInspectUser)inspectUsers.get(0));
		    }
		    return null;
	}

	/**
	 * @author liao
	 */
	@Override
	public void addTerminateStatusReport(TTerminateStatusReport terminateStatusReport) {
				baseDao.save1(terminateStatusReport);
			
	}
	/**
	 * @author liao
	 */
	@Override
	public void editTerminateStatusReport(TTerminateStatusReport terminateStatusReport) {
		baseDao.update(terminateStatusReport);
	}
	/**
	 * @author liao
	 */
	@Override
	public TTerminateStatusReport findTerminateStatusReport(String rpuserid) {
		String hql=" from TTerminateStatusReport rp where rp.rpuserid= '"+rpuserid+"' order by rp.id desc limit 0,1 " ;
		List<TTerminateStatusReport> list=baseDao.find(hql);
		if(list!=null&&list.size()!=0){
			TTerminateStatusReport terminateStatusReport=list.get(0);
			return terminateStatusReport;
		}
		return null;
	}

	@Override
	public void editInspectUserPwd(TInspectUser inspectUser) {
		baseDao.update(inspectUser);
	}
	/**
	 * true 表示所有组里面含有组员
	 * false 表示所有组里面没有成员
	 */
	@Override
	public boolean isGroupHaveUsers(String groupids) {
		boolean flag=false;
		for(String groupid:groupids.split(",")){
			List<TInspectUser> list=baseDao.find("from TInspectUser where groupid="+groupid);
			if(list!=null&&list.size()>0){
				flag=true;
				break;
			}
		}
		
		return flag;
	}

	@Override
	public List<TInspectUser> findInspectUserByGroupid(String groupid) {
		List<TInspectUser> list=baseDao.find("from TInspectUser where groupid="+groupid);
		return list;
	}
	@Override
	public Long terminateTotalCount(int entid){
		return baseDao.count("select count(*) from TTerminal t where t.entid = ?", new Object[] { entid });
	}
	
	@Override
	public Long count1(String termno,int entid,int type) {
		Long ct=null;
		if(type==0){
			ct=baseDao.count("select count(*) from TTerminal t where t.termno = ? and t.entid=?", new Object[] { termno,entid });
		}
		if(type==1){
			ct=baseDao.count("select count(*) from TTerminal t where t.termno = ? ", new Object[] { termno });
		}
		return ct;
	}
	@Override
	public void deleteTerminateStatusReport(String termid) {
		String hql="from  TTerminateStatusReport where  rpterminateid='"+termid+"' and flag=1";
		List list=baseDao.find(hql);
		if(list!=null&&list.size()>0){
			for (int i = 0; i < list.size(); i++) {
				TTerminateStatusReport term=(TTerminateStatusReport) list.get(i);
				baseDao.delete(TTerminateStatusReport.class, term.getId());
			}
		}
	}
	//获得最近的一条记录
	@Override 
	public TTerminateStatusReport geTerminateStatusReport(String termid) {
		// TODO Auto-generated method stub
		String hql="from TTerminateStatusReport where rpterminateid='"+termid+"' ORDER BY id DESC LIMIT 1";
		List<TTerminateStatusReport> list=baseDao.find(hql);
		if(list!=null&&list.size()>0){
			TTerminateStatusReport term=list.get(0);
			return term;
		}
		return null;
	}
	
	/**
	 * 终端登陆时判断是否存在终端
	 */
	@Override
	public boolean getTerminalByTermid(String termid) {
		// TODO Auto-generated method stub
		
		boolean flag=false;
		String hql="select count(*) from TTerminal where termno='"+termid+"'";
		//	System.out.println("termid==========="+termid);
		//	System.out.println(hql);
		Long count=baseDao.count(hql);
		if(count>0){
			System.out.println("终端存在！");
		  flag=true;	
		}
		else{
			flag=false;
		}
		//System.out.println("flag==========="+flag+"\t count="+count);
		return flag;
	}

	@Override
	public List<TInspectUser> getInspectUsers() {
		// TODO Auto-generated method stub
		List<TInspectUser>list=baseDao.find("from TInspectUser");
		return list;
	}

	
}
