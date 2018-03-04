package com.inspect.service;
import com.inspect.model.system.Parameter;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.inspect.model.system.Enterprise;
import com.inspect.model.system.Forms;
import com.inspect.model.system.TSFunction;
import com.inspect.model.system.TSRole;
import com.inspect.model.system.TSRoleFunction;
import com.inspect.model.system.TSRoleUser;
import com.inspect.model.system.TSUser;
import com.inspect.util.system.CriteriaQuery;
import com.inspect.vo.basis.UnicomVo;
import com.inspect.vo.comon.ComboTree;
import com.inspect.vo.comon.ComboTreeModel;
import com.inspect.vo.system.EnterpriseVo;
import com.inspect.vo.system.PageRole;
import com.inspect.vo.system.PageUser;

public interface SystemServiceI {

	//单位管理
	void addEnterprise(EnterpriseVo enterprise);
	
	void editEnterprise(EnterpriseVo enterprise);

	void removeEnterprise(String ids);

	Enterprise findEnterpriseById(int id);

	Map<String, Object> findEnterpriseDatagrid(EnterpriseVo enterprise,int page, int rows,int qsql);
	
	

	 //验证用户是否存在
	TSUser checkUserExits(PageUser user);
	
	//查询对象类列表
	<T> List<T> findByProperty(Class<T> paramClass, String paramString, Object paramObject);
	
	//添加角色
	void addRole(PageRole pageRole);
	
	//修改角色
   void editRole(PageRole pageRole);
	
   //获取角色对象
	TSRole getRole(String id);

	//删除角色
	void deleteRole(String ids);
	
	//查询角色列表
   Map<String, Object> findRoleDatagrid(PageRole pageRole,int page, int rows,String qsql) ;
	
   //添加用户
   void addUser(PageUser pageUser);
   
   <T> void batchSave(List<T> entitys);
   
   void editUser(TSUser user);
	
   //修改用户
   void editUser(PageUser pageUser);
	
   //获取用户对象
	TSUser getUser(String id);

	//删除用户
	void deleteUser(String ids);
	
	//查询用户列表
	Map<String, Object> findUserDatagrid(PageUser pageUser,int page, int rows,String qsql);
	
	Parameter getParameter(String name);
	
	@SuppressWarnings("unchecked")
	List<ComboTree> ComboTree(List all,ComboTreeModel comboTreeModel, List in);
	
	<T> List<T> getListByCriteriaQuery();
	
	<T> List<T> getListByCriteriaQuery(CriteriaQuery cq, Boolean ispage);
	
	<T> void deleteAllEntitie(Collection<T> entities);
	
	TSFunction getEntityById(String id);
	
	void save(Object entity);
	
   void deleteRoleFunction(TSRoleFunction rolefunction);
	
	void deleteRoleUser(TSRoleUser roleuser);
	
	List<TSRole> combobox(int qsql);
	
	List<TSUser> comboboxUser();
	
	List<UnicomVo> comboboxUnicom();
	
	Long countByUserName(String username,int entid);
	
	Long countByRoleName(String rolename,int entid);

	//获取企业列表
    List<Enterprise> comboboxEnterprise(int qsql);
    
    
    List<Forms>  comboboxAdministrativedivision();
    
    List<UnicomVo> comboboxUnicom(int qsql);
    int onlineCount(int hsql);
}
