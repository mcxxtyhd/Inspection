package com.inspect.action.system;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;

import com.inspect.action.common.BaseAction;
import com.inspect.annotation.LogAnnotation;
import com.inspect.model.system.TSFunction;
import com.inspect.model.system.TSRole;
import com.inspect.model.system.TSRoleFunction;
import com.inspect.service.SystemServiceI;
import com.inspect.util.common.ExceptionUtil;
import com.inspect.util.common.StringUtils;
import com.inspect.util.system.CriteriaQuery;
import com.inspect.vo.comon.ComboTree;
import com.inspect.vo.comon.ComboTreeModel;
import com.inspect.vo.comon.Json;
import com.inspect.vo.system.PageRole;
import com.opensymphony.xwork2.ModelDriven;

@Namespace("/system")
@Action(value="roleAction",results={
		@Result(name="roleList",location="/webpage/system/role/roleList.jsp"),
		@Result(name="roleAdd",location="/webpage/system/role/roleAdd.jsp"),
		@Result(name="roleEdit",location="/webpage/system/role/roleEdit.jsp"),
		@Result(name="fun",location="/webpage/system/role/roleSet.jsp")})
public class RoleAction extends BaseAction implements ModelDriven<ComboTree> {
	
	private static final long serialVersionUID = -7212734618000252022L;
	
	private static final Logger logger = Logger.getLogger(RoleAction.class);

	@Resource
	private SystemServiceI systemService;
	
	public SystemServiceI getSystemService() {
		return systemService;
	}

	public void setSystemService(SystemServiceI systemService) {
		this.systemService = systemService;
	}

	private PageRole pageRole=new PageRole();
	
	private ComboTree comboTree = new ComboTree();

	private int page;// 当前页
	private int rows;// 每页显示记录数
	private String rolename;
	private String roledesc;
	private int id;
	private int entid;
	
	public int getEntid() {
		return entid;
	}

	public void setEntid(int entid) {
		this.entid = entid;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public String getRolename() {
		return rolename;
	}

	public void setRolename(String rolename) {
		this.rolename = rolename;
	}

	public String getRoledesc() {
		return roledesc;
	}

	public void setRoledesc(String roledesc) {
		this.roledesc = roledesc;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ComboTree getModel() {
		return comboTree;
	}
	
	public String roleList(){
		getRequest().setAttribute("EnterpriseList",systemService.comboboxEnterprise(this.queryEnterpriseByWhere()));
		return "roleList";
	}
	
	public String roleAdd(){
		getRequest().setAttribute("RoleList",systemService.combobox(queryEnterpriseByWhere()));
		getRequest().setAttribute("EnterpriseList",systemService.comboboxEnterprise(this.queryEnterpriseByWhere()));
		return "roleAdd";
	}
	
	public String roleEdit(){
		getRequest().setAttribute("RoleList",systemService.combobox(queryEnterpriseByWhere()));
		getRequest().setAttribute("EnterpriseList",systemService.comboboxEnterprise(this.queryEnterpriseByWhere()));
		return "roleEdit";
	}
	
	public void roleDatagrid(){
		pageRole.setRolename(rolename);
		pageRole.setEntid(entid);
		int page =Integer.parseInt(ServletActionContext.getRequest().getParameter("page") );
		int rows =Integer.parseInt(ServletActionContext.getRequest().getParameter("rows") );
		Map<String, Object> map = systemService.findRoleDatagrid(pageRole,page,rows,querySql1());
		writeJson(map);
	}
	
	@LogAnnotation(event="添加角色",tablename="t_role")
	public void addRole(){
		Json j=new Json();
		try{
			pageRole.setRolename(rolename);
			pageRole.setRoledesc(roledesc);
			pageRole.setEntid(entid);
			long rname=systemService.countByRoleName(rolename,queryEnterpriseByWhere());
			if(rname>0){
				j.setMsg("角色：【"+rolename+"】已存在,不能重复添加!");
				j.setSuccess(false);
				setOperstatus(1);
			}else{
				systemService.addRole(pageRole);
				j.setMsg("添加成功！");
				j.setSuccess(true);
			}
		}catch(Exception e){
			setOperstatus(1);
			j.setMsg("添加失败！");
			logger.error(ExceptionUtil.getExceptionMessage(e));
		}
		writeJson(j);
	}
	
	@LogAnnotation(event="修改角色",tablename="t_role")
	public void editRole(){
		Json j=new Json();
		try{
			String rid=getRequest().getParameter("RoleId");
			pageRole.setId(Integer.parseInt(rid));
			pageRole.setRolename(rolename);
			pageRole.setRoledesc(roledesc);
			pageRole.setEntid(entid);
			systemService.editRole(pageRole);
			j.setMsg("修改成功！");
			j.setSuccess(true);
		}catch(Exception e){
			setOperstatus(1);
			j.setMsg("修改失败！");
			logger.error(ExceptionUtil.getExceptionMessage(e));
		}
		writeJson(j);
	}
	
	@LogAnnotation(event="删除角色",tablename="t_role")
	public void deleteRole(){
		Json j=new Json();
		try{
			String roleid=getRequest().getParameter("RoleId");
			if(roleid.equals("1")){
				setOperstatus(1);
				j.setMsg("此角色不能删除！");
				
			}
			else{
			if(!StringUtils.isEmpty(roleid)){
				systemService.deleteRole(roleid);
				j.setSuccess(true);
				j.setMsg("删除成功！");
			}
			}
		}catch(Exception e){
			setOperstatus(1);
			j.setMsg("删除失败！");
			logger.error(ExceptionUtil.getExceptionMessage(e));
		}
		writeJson(j);
	}
	
	//功能菜单页面
	public String fun(){
		String roleId = getRequest().getParameter("roleId");
		getRequest().setAttribute("roleId", roleId);
		return "fun";
	}
	
	//加载功能权限列表
	public void setAuthority() {
		CriteriaQuery cq = new CriteriaQuery(TSFunction.class);
		if (comboTree.getId() != null) {
			cq.eq("TSFunction.id", Integer.parseInt(comboTree.getId()));
		}
		if (comboTree.getId() == null) {
			cq.isNull("TSFunction");
		}
		cq.notEq("functionLevel", Short.parseShort("-1"));
		cq.add();
		List<TSFunction> functionList = systemService.getListByCriteriaQuery(cq,false);
		List<ComboTree> comboTrees = new ArrayList<ComboTree>();
		String roleId = getRequest().getParameter("roleId");
		List<TSFunction> loginActionlist = new ArrayList<TSFunction>();// 已有权限菜单
		TSRole role = this.systemService.getRole(roleId);
		if (role != null) {
			List<TSRoleFunction> roleFunctionList=systemService.findByProperty(TSRoleFunction.class, "TSRole.id", role.getId());
			if (roleFunctionList.size() > 0) {
				for (TSRoleFunction roleFunction : roleFunctionList) {
					TSFunction function = (TSFunction) roleFunction.getTSFunction();
					loginActionlist.add(function);
				}
			}
		}
		ComboTreeModel comboTreeModel = new ComboTreeModel("id", "functionName", "TSFunctions");
		comboTrees = systemService.ComboTree(functionList,comboTreeModel,loginActionlist);
		writeJson(comboTrees);
	}
	
	//修改功能权限
	@LogAnnotation(event="修改功能权限",tablename="t_s_role_function")
	public void updateAuthority() {
		Json j = new Json();
		try{
			String roleId =getRequest().getParameter("roleId");
			String rolefunction = getRequest().getParameter("rolefunctions");
			TSRole role = this.systemService.getRole(roleId);
			List<TSRoleFunction> roleFunctionList=systemService.findByProperty(TSRoleFunction.class, "TSRole.id", role.getId());
			systemService.deleteAllEntitie(roleFunctionList);
			String[] roleFunctions = null;
			List<TSRoleFunction> entitys = new ArrayList<TSRoleFunction>();
			if (rolefunction != "") {
				roleFunctions = rolefunction.split(",");
				for (String s : roleFunctions) {
					TSRoleFunction rf = new TSRoleFunction();
					TSFunction f = this.systemService.getEntityById(s);
					rf.setTSFunction(f);
					rf.setTSRole(role);
					rf.setEntid(role.getEntid());
					//性能优化，改成批量插入
					entitys.add(rf);
				}
			}
			this.systemService.batchSave(entitys);
			j.setSuccess(true);
			j.setMsg("权限更新成功");			
		}catch (Exception e){
			setOperstatus(1);
            logger.error(ExceptionUtil.getExceptionMessage(e));    
			j.setMsg("权限更新失败");			
		}
		writeJson(j);
	}
}
