package com.inspect.service.impl;
import java.awt.Button;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.inspect.dao.BaseDaoI;
import com.inspect.model.system.Enterprise;
import com.inspect.model.system.Forms;
import com.inspect.model.system.Parameter;
import com.inspect.model.system.TLog;
import com.inspect.model.system.TSFunction;
import com.inspect.model.system.TSRole;
import com.inspect.model.system.TSRoleFunction;
import com.inspect.model.system.TSRoleUser;
import com.inspect.model.system.TSUser;
import com.inspect.service.SystemServiceI;
import com.inspect.util.common.ConvertObjectUtils;
import com.inspect.util.common.DateUtils;
import com.inspect.util.common.QueryResult;
import com.inspect.util.common.StringUtils;
import com.inspect.util.system.CriteriaQuery;
import com.inspect.util.system.ReflectHelper;
import com.inspect.vo.basis.UnicomVo;
import com.inspect.vo.comon.ComboTree;
import com.inspect.vo.comon.ComboTreeModel;
import com.inspect.vo.system.EnterpriseVo;
import com.inspect.vo.system.PageRole;
import com.inspect.vo.system.PageUser;

@Service("systemService")
@Transactional(propagation = Propagation.REQUIRED,readOnly = false,rollbackFor = Exception.class)
public class SystemServiceImpl implements SystemServiceI {
	
	@Resource
	private BaseDaoI baseDao;

	public BaseDaoI getBaseDao() {
		return baseDao;
	}

	public void setBaseDao(BaseDaoI baseDao) {
		this.baseDao = baseDao;
	}

	/**
	 * 添加单位
	 */
	public void addEnterprise(EnterpriseVo enterprise) {
       Enterprise ent=new Enterprise();
       BeanUtils.copyProperties(enterprise,ent);
       baseDao.save(ent);
	}

	/**
	 * 修改单位
	 */
	public void editEnterprise(EnterpriseVo enterprise) {
		Enterprise ent=baseDao.get(Enterprise.class,enterprise.getId());
	    BeanUtils.copyProperties(enterprise,ent,new String[]{"id"});
	    baseDao.save(ent);
		
	}
	
	/**
	 * 删除单位
	 */
	public void removeEnterprise(String ids) {
		if(!StringUtils.isEmpty(ids)){
			for(String id : ids.split(",")){
				baseDao.delete(Enterprise.class,Integer.parseInt(id));
			}
		}
	}

	/**
	 * 查询单位对象
	 */
	public Enterprise findEnterpriseById(int id) {
		if(StringUtils.isEmpty(String.valueOf(id))){
			return null;
		}
		return baseDao.get(Enterprise.class,id);
	}

	/**
	 * 查询单位列表
	 */
	public Map<String, Object> findEnterpriseDatagrid(EnterpriseVo enterprise,int page, int rows,int qsql) {
		  Map<String, Object> map = new HashMap<String, Object>();
	      StringBuffer buf=new StringBuffer();
	        if(qsql==0){
				buf.append(" 1=1");
			}else{
				buf.append(" id=").append(qsql);
			}
			if(!StringUtils.isEmpty(enterprise.getEntname())){
				buf.append(" and entname like '%").append(enterprise.getEntname()).append("%'");
			}
			if(!StringUtils.isEmpty(enterprise.getEntlinkperson())){
				buf.append(" and entlinkperson ='").append(enterprise.getEntlinkperson()).append("'");
			}
			buf.append("order by id desc");
			QueryResult<Enterprise> queryResult = baseDao.getQueryResult(Enterprise.class,buf.toString(),(page-1)*rows, rows,null,null );
			map.put("total", queryResult.getTotalRecord() );				
			map.put("come", (page - 1)* rows );
			map.put("to", rows* page );
			map.put("rows", queryResult.getResultList() );
			return map;
	}
	
	@SuppressWarnings("unchecked")
	public TSUser checkUserExits(PageUser user) {
	    List users = baseDao.find("from TSUser u where u.username = '" + user.getUsername() + "' and u.password='" + user.getPassword() + "'");
	    if ((users != null) && (users.size() > 0)){
	    	return ((TSUser)users.get(0));
	    }
	    return null;
	}
	
	public <T> List<T> findByProperty(Class<T> paramClass, String paramString,Object paramObject) {
		return this.baseDao.findByProperty(paramClass, paramString, paramObject);
	}
	
	
	public void addRole(PageRole pageRole){
		TSRole role=new TSRole();
	    BeanUtils.copyProperties(pageRole,role);
	    baseDao.save(role);
	}
	
	public void editRole(PageRole pageRole) {
		TSRole role=baseDao.getEntityById(TSRole.class,pageRole.getId());
	    BeanUtils.copyProperties(pageRole,role,new String[]{"id"});
	    baseDao.update(role);
	}
	
	public TSRole getRole(String id) {
		if(StringUtils.isEmpty(String.valueOf(id))){
			return null;
		}
		return baseDao.getEntityById(TSRole.class,Integer.parseInt(id));
	}
	
	public TSFunction getEntityById(String id) {
		if(StringUtils.isEmpty(String.valueOf(id))){
			return null;
		}
		return baseDao.getEntityById(TSFunction.class,Integer.parseInt(id));
	}
	public <T> T get(Class<T> type, Serializable id) {
		return baseDao.get(type, id);
	}

	public void deleteRole(String ids) {
		if(!StringUtils.isEmpty(ids)){
			TSRole role=this.getRole(ids);
			//删除角色权限
			delRoleFunction(role);
			//删除角色
			baseDao.delete(TSRole.class,Integer.parseInt(ids));
		}
	}
	
	private void delRoleFunction(TSRole role){
		List<TSRoleFunction> roleFunctions=baseDao.findByProperty(TSRoleFunction.class,"TSRole.id",role.getId());	
		if (roleFunctions.size()>0) {
			for (TSRoleFunction tsRoleFunction : roleFunctions) {
			//删除角色权限关联表
				this.deleteRoleFunction(tsRoleFunction);	
			}
		}
		List<TSRoleUser> roleUsers=baseDao.findByProperty(TSRoleUser.class,"TSRole.id",role.getId());
	    for (TSRoleUser tsRoleUser : roleUsers) {
	    	//删除角色用户关联表
	    	this.deleteRoleUser(tsRoleUser);
		}	
	}
	
	
	public void deleteRoleFunction(TSRoleFunction rolefunction){
		baseDao.delete(TSRoleFunction.class,rolefunction.getId());
	}
	
	public void deleteRoleUser(TSRoleUser roleuser){
		baseDao.delete(TSRoleUser.class,roleuser.getId());
	}
	
	public void save(Object entity){
		baseDao.save(entity);
	}

	public <T> void batchSave(List<T> entitys){
	    this.baseDao.saveBatch(entitys);
	  }
	
	public Map<String, Object> findRoleDatagrid(PageRole pageRole,int page, int rows,String qsql) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer buf = new StringBuffer(qsql);
		if (!StringUtils.isEmpty(pageRole.getRolename())) {
			buf.append(" and rolename like '%").append(pageRole.getRolename()).append("%'");
		}
		if (pageRole.getEntid()!=0) {
			buf.append(" and entid=").append(pageRole.getEntid());
		}
		buf.append(" order by id desc");
		QueryResult<TSRole> queryResult = baseDao.getQueryResult(TSRole.class, buf.toString(), (page - 1) * rows, rows,null, null);
		List<PageRole> pvolist=new ArrayList<PageRole>();
		if (queryResult != null && queryResult.getResultList().size() > 0) {
			for (TSRole trole : queryResult.getResultList()) {
				PageRole pvo = new PageRole();
				BeanUtils.copyProperties(trole,pvo);
				Enterprise ent=this.findEnterpriseById(trole.getEntid());
				if(ent!=null){
					pvo.setEntname(ent.getEntname());
				}
				pvolist.add(pvo);
			}
		}
		map.put("total", queryResult.getTotalRecord());
		map.put("come", (page - 1) * rows);
		map.put("to", rows * page);
		map.put("rows", pvolist);
		return map;
	}
	
	public void addUser(PageUser pageUser){
		TSUser user=new TSUser();
	    BeanUtils.copyProperties(pageUser,user);
	    baseDao.save(user);
	    //添加用户角色表
	    if(!StringUtils.isEmpty(pageUser.getRoleid())){
	    	addRoleUser(user,pageUser.getRoleid());
	    }
	}
	
	private void addRoleUser(TSUser user, String roleidstr) {
		//先删除用户角色表
		baseDao.executeHql("delete TSRoleUser t where t.TSUser = ?", new Object[] { user });
		TSRoleUser rUser = new TSRoleUser();
		TSRole role =  baseDao.getEntityById(TSRole.class,Integer.parseInt(roleidstr));
		rUser.setTSRole(role);
		rUser.setTSUser(user);
		rUser.setEntid(user.getEntid());
		baseDao.save(rUser);
	}
	
	public void editUser(PageUser pageUser) {
		TSUser user=baseDao.getEntityById(TSUser.class,pageUser.getId());
	    BeanUtils.copyProperties(pageUser,user,new String[]{"id","password"});
	    baseDao.update(user);
	    //添加用户角色表
	    if(!StringUtils.isEmpty(pageUser.getRoleid())){
	    	addRoleUser(user,pageUser.getRoleid());
	    }
	}
	
	public void editUser(TSUser user){
		 baseDao.update(user);
	}
	
	public TSUser getUser(String id) {
		if(StringUtils.isEmpty(String.valueOf(id))){
			return null;
		}
		return baseDao.getEntityById(TSUser.class,Integer.parseInt(id));
	}

	public void deleteUser(String ids) {
		if(!StringUtils.isEmpty(ids)){
			for(String id : ids.split(",")){
				TSUser user=this.getUser(id);
				deleteRoleUser(user);
				baseDao.delete(TSUser.class,Integer.parseInt(id));
			}
		}
	}
	
	private void deleteRoleUser(TSUser user){
		List<TSRoleUser> roleUsers=baseDao.findByProperty(TSRoleUser.class,"TSUser.id",user.getId());
		if (roleUsers.size()>0) {
			for (TSRoleUser tsRoleUser : roleUsers) {
				// 删除角色用户关联表
				deleteRoleUser(tsRoleUser);
			}	
		}
	}
	
	public Long countByUserName(String username,int entid) {
		return baseDao.count("select count(*) from TSUser t where t.username = ? ", new Object[] { username });
	}
	
	public Long countByRoleName(String rolename,int entid) {
		return baseDao.count("select count(*) from TSRole t where t.rolename = ? and entid=?", new Object[] { rolename,entid });
	}
	
	public Map<String, Object> findUserDatagrid(PageUser pageUser,int page, int rows,String qsql) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer buf = new StringBuffer(qsql);
		if (!StringUtils.isEmpty(pageUser.getUsername())) {
			buf.append(" and username like '%").append(pageUser.getUsername()).append("%'");
		}
		if (!StringUtils.isEmpty(pageUser.getRealname())) {
			buf.append(" and realname like '%").append(pageUser.getRealname()).append("%'");
		}
		if (pageUser.getEntid()!=0) {
			buf.append(" and entid=").append(pageUser.getEntid());
		}
		buf.append(" order by id desc");
		QueryResult<TSUser> queryResult = baseDao.getQueryResult(TSUser.class, buf.toString(), (page - 1) * rows, rows,null, null);
		List<PageUser> pvolist=new ArrayList<PageUser>();
		if (queryResult != null && queryResult.getResultList().size() > 0) {
			for (TSUser tuser : queryResult.getResultList()) {
				PageUser pvo = new PageUser();
				BeanUtils.copyProperties(tuser,pvo);
				List<TSRoleUser> roleUsers = baseDao.findByProperty(TSRoleUser.class, "TSUser.id", tuser.getId());
				String roleId = "";
				String roleName = "";
				if (roleUsers.size() > 0) {
					for (TSRoleUser tRoleUser : roleUsers) {
						roleId += tRoleUser.getTSRole().getId();
						roleName += tRoleUser.getTSRole().getRolename();
					}
				}
				Enterprise ent=this.findEnterpriseById(tuser.getEntid());
				if(ent!=null){
					pvo.setEntname(ent.getEntname());
				}
				pvo.setRoleid(roleId);
				pvo.setRolename(roleName);
				pvolist.add(pvo);
			}
		}
		map.put("total", queryResult.getTotalRecord());
		map.put("come", (page - 1) * rows);
		map.put("to", rows * page);
		map.put("rows", pvolist);
		return map;
	}
	
	@SuppressWarnings("unchecked")
	public List<ComboTree> ComboTree(List all,ComboTreeModel comboTreeModel, List in){
	    List trees = new ArrayList();
	    Iterator localIterator = all.iterator();
	    while (localIterator.hasNext()){
	      Object obj = localIterator.next();
	      trees.add(comboTree(obj, comboTreeModel, in, false));
	    }
	    return trees;
	  }
	

	@SuppressWarnings("unchecked")
	private ComboTree comboTree(Object obj, ComboTreeModel comboTreeModel, List in, boolean recursive) {
		ComboTree tree = new ComboTree();
		Map attributes = new HashMap();
		ReflectHelper reflectHelper = new ReflectHelper(obj);
		String id = ConvertObjectUtils.getString(reflectHelper.getMethodValue(comboTreeModel.getIdField()));
		tree.setId(id);
		tree.setText(ConvertObjectUtils.getString(reflectHelper.getMethodValue(comboTreeModel.getTextField())));
		if (comboTreeModel.getSrcField() != null) {
			attributes.put("href", ConvertObjectUtils.getString(reflectHelper.getMethodValue(comboTreeModel.getSrcField())));
			tree.setAttributes(attributes);
		}
		if ((in != null) && (in.size() > 0)) {
			Iterator localIterator = in.iterator();
			while (localIterator.hasNext()) {
				Object inobj = localIterator.next();
				ReflectHelper reflectHelper2 = new ReflectHelper(inobj);
				String inId = ConvertObjectUtils.getString(reflectHelper2.getMethodValue(comboTreeModel.getIdField()));
				if (inId.equals(id)) {
					tree.setChecked(Boolean.valueOf(true));
				}
			}
		}
		List tsFunctions = (List) reflectHelper.getMethodValue(comboTreeModel.getChildField());
		if ((tsFunctions != null) && (tsFunctions.size() > 0)) {
			tree.setState("closed");
			tree.setChecked(Boolean.valueOf(false));
		}
		return tree;
	}

	public <T> List<T> getListByCriteriaQuery() {
		ComboTree comboTree = new ComboTree();
		CriteriaQuery cq = new CriteriaQuery(TSFunction.class);
		if (comboTree.getId() != null) {
			cq.eq("TSFunction.id", comboTree.getId());
		}
		if (comboTree.getId() == null) {
			cq.isNull("TSFunction");
		}
		cq.notEq("functionLevel", Short.parseShort("-1"));
		cq.add();
		return baseDao.getListByCriteriaQuery(cq, false);
	}

	public <T> List<T> getListByCriteriaQuery(CriteriaQuery cq, Boolean ispage) {
		return this.baseDao.getListByCriteriaQuery(cq, ispage);
	}

	public <T> void deleteAllEntitie(Collection<T> entities) {
		this.baseDao.deleteAllEntitie(entities);
	}

	public List<TSRole> combobox(int qsql) {
		List<TSRole> rl = new ArrayList<TSRole>();
		StringBuffer buf=new StringBuffer("from TSRole where ");
		if(qsql==0){
			buf.append("1=1");
		}else{
			buf.append(" entid=").append(qsql);
		}
		List<TSRole> l = baseDao.find(buf.toString());
		if (l != null && l.size() > 0) {
			for (TSRole t : l) {
				TSRole r = new TSRole();
				r.setId(t.getId());
				r.setRolename(t.getRolename());
				rl.add(r);
			}
		}
		return rl;
	}
	  
	public List<TSUser> comboboxUser() {
		List<TSUser> rl = new ArrayList<TSUser>();
		List<TSUser> l = baseDao.find("from TSUser");
		if (l != null && l.size() > 0) {
			for (TSUser t : l) {
				TSUser r = new TSUser();
				r.setId(t.getId());
				r.setUsername(t.getUsername());
				rl.add(r);
			}
		}
		return rl;
	}
	
	//获取企业列表
	@SuppressWarnings("unchecked")
	public List<Enterprise> comboboxAdministrativedivision2(int qsql) {
		StringBuffer buf=new StringBuffer("select id,administrativedivision from unicominfo where ");
		if(qsql==0){
			buf.append("1=1");
		}else{
			buf.append(" id=").append(qsql);
		}
		List<Enterprise> l = baseDao.getJdbcTemplate().queryForList(buf.toString());
		return l;
	}

	public List<Forms> comboboxAdministrativedivision() {
		StringBuffer buf=new StringBuffer("select id,administrativedivision from unicominfo where 1=1");
		List<Forms> l = baseDao.getJdbcTemplate().queryForList(buf.toString());
		return l;
	}
	
	
	//获取联通区县列表
	@SuppressWarnings("unchecked")
	public List<UnicomVo> comboboxUnicom(int qsql) {
		System.out.println("466comboboxUnicom(int qsql): "+qsql);
		
		StringBuffer buf=new StringBuffer("select administrativedivision,basename from unicominfo where ");
		if(qsql==0){
			buf.append("1=1");
		}else{
			buf.append(" id=").append(qsql);
		}
		//而spring的jdbcTemplate.queryForList( sql );
		//先返回一个object，这个object里面是一个map，对应的key就是数据库里面的字段名，value就是我们要取的值了！
		List<UnicomVo> l = baseDao.getJdbcTemplate().queryForList(buf.toString());
		
		System.out.println("476systemserviceimpl list长度: "+l.size());
		return l;
	}
	
	
	@Override
	public Parameter getParameter(String name) {
		// TODO Auto-generated method stub
		String hql="from Parameter where pname='"+name+"'";
		List<Parameter> list=baseDao.find(hql);
		Parameter param=null;
		if(list!=null&&list.size()>0){
			param=list.get(0);
		}
		return param;
	}

	@Override
	public int onlineCount(int qsql) {
		// TODO Auto-generated method stub
		StringBuffer onbuf=new StringBuffer("");
		StringBuffer offbuf=new StringBuffer("");
		String startdate=DateUtils.getFormatDayDate()+" 00:00:00";
		String entdate=DateUtils.getFormatDayDate()+" 23:59:59";
		//异常退出不做处理
		//查出登录的用户
		onbuf.append("select count(*) from TLog where optime >='").append(startdate).append("'")
				  .append(" and optime <='").append(entdate).append("'")
				  .append(" and opevent ='用户登录'").append(" and opstatus=0");
		
		if(qsql!=0){
			onbuf.append(" and entid='").append(qsql).append("'");
		}
		Long online=baseDao.count(onbuf.toString());
		//查出登录的用户
		offbuf.append("select count(*) from TLog where optime >='").append(startdate).append("'")
				  .append(" and optime <='").append(entdate).append("'")
				  .append(" and opevent ='用户注销'").append(" and opstatus=0");
		
		if(qsql!=0){
			offbuf.append(" and entid='").append(qsql).append("'");
		}
		
		//System.out.println(offbuf.toString());
		Long offline=baseDao.count(offbuf.toString());
		
		return (int) (online-offline);
	}

	@Override
	public List<UnicomVo> comboboxUnicom() {
		StringBuffer buf=new StringBuffer("select administrativedivision,basename from unicominfo");
		
		List<UnicomVo> l = baseDao.getJdbcTemplate().queryForList(buf.toString());
		return l;
	}

	@Override
	//获取企业列表
	public List<Enterprise> comboboxEnterprise(int qsql) {
		List<Enterprise> rl = new ArrayList<Enterprise>();
		StringBuffer buf=new StringBuffer("from Enterprise where ");
		if(qsql==0){
			buf.append("1=1");
		}else{
			buf.append(" id=").append(qsql);
		}
		List<Enterprise> l = baseDao.find(buf.toString());
		if (l != null && l.size() > 0) {
			for (Enterprise e : l) {
				Enterprise r = new Enterprise();
				r.setId(e.getId());
				r.setEntname(e.getEntname());
				rl.add(r);
			}
		}
		return rl;
	}

}
