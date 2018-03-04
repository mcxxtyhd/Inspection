package com.inspect.action.system;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;

import com.inspect.action.common.BaseAction;
import com.inspect.annotation.LogAnnotation;
import com.inspect.constant.Constant;
import com.inspect.model.system.TSFunction;
import com.inspect.model.system.TSRole;
import com.inspect.model.system.TSRoleFunction;
import com.inspect.model.system.TSRoleUser;
import com.inspect.model.system.TSUser;
import com.inspect.service.SystemServiceI;
import com.inspect.util.system.ListtoMenu;
import com.inspect.util.system.NumberComparator;
import com.inspect.vo.comon.Json;
import com.inspect.vo.comon.SessionInfo;
import com.inspect.vo.system.PageUser;
import com.opensymphony.xwork2.ModelDriven;
@Namespace("/system")
@Action(value="loginAction",results={
		@Result(name="main",location="/webpage/login/main.jsp"),
		@Result(name="left",location="/webpage/login/left.jsp"),
		@Result(name="login",location="/webpage/login/login.jsp")})
public class LoginAction extends BaseAction implements ModelDriven<PageUser> {

	private static final long serialVersionUID = 2135600388108680249L;
	
	private static final Logger logger = Logger.getLogger(LoginAction.class);
	
	@Resource
	private SystemServiceI systemService;

	public SystemServiceI getSystemService() {
		return systemService;
	}

	public void setSystemService(SystemServiceI systemService) {
		this.systemService = systemService;
	}

	private PageUser pageUser=new PageUser();
	
	@Override
	public PageUser getModel() {
		return pageUser;
	}
	
	//用户登录
	public String login(){
		TSUser user = this.getSessionUserName();
		if (user != null) {
			getRequest().setAttribute("userName", user.getRealname());
			return "main";
		} else {
			return "login";
		}
	}
	
	//用户验证
	@LogAnnotation(event="用户登录",tablename="t_s_user")
	public void checkuser(){
		Json j = new Json();
		TSUser u = systemService.checkUserExits(pageUser);
		if (u != null) {
			SessionInfo sessionInfo = new SessionInfo();
			sessionInfo.setUser(u);
			getSession().setMaxInactiveInterval(60 * 40);
			getSession().setAttribute(Constant.USER_SESSION, sessionInfo);
			j.setMsg("用户: " + u.getUsername() + "登录成功");
			j.setSuccess(true);
		} else {
			setOperstatus(1);
			j.setMsg("用户名或密码错误!");
			j.setSuccess(false);
		}
		writeJson(j);
	}
	
	
	//系统注销
	@LogAnnotation(event="用户注销",tablename="t_s_user")
	public String logout(){
		TSUser user= getSessionUserName();
		try {
			//左侧菜单信息放入到session中
			List<TSRoleUser> rUsers = systemService.findByProperty(TSRoleUser.class, "TSUser.id", user.getId());
			for (TSRoleUser ru : rUsers) {
				TSRole role = ru.getTSRole();
				getSession().removeAttribute(String.valueOf(role.getId()));
			}
			// 判断用户是否为空不为空则清空session中的用户object
			getSession().removeAttribute(Constant.USER_SESSION);// 注销该操作用户
			setEntid(user.getEntid());
			setOperater(user.getUsername());
		} catch (Exception e) {
			setEntid(user.getEntid());
			setOperater(user.getUsername());
			setOperstatus(1);
			logger.info( e.getMessage() ) ;
		}
		return this.login();
	}
	
	//系统加载菜单功能列表
	public String left(){
		TSUser user = this.getSessionUserName();
		//System.out.println("44444444444444");
		String roles = "";
		// 登陆者的权限
		Set<TSFunction> loginActionlist = new HashSet<TSFunction>();// 已有权限菜单
		//左侧菜单信息放入到session中
		List<TSRoleUser> rUsers = systemService.findByProperty(TSRoleUser.class, "TSUser.id",user.getId());
		for (TSRoleUser ru : rUsers) {
			TSRole role = ru.getTSRole();
			roles += role.getRolename()+ ",";
			List<TSRoleFunction> roleFunctionList =this.getSessionTSRoleFunction(String.valueOf(role.getId()));
			if (roleFunctionList == null) {
				getSession().setMaxInactiveInterval(60 * 40);
				roleFunctionList = systemService.findByProperty(TSRoleFunction.class, "TSRole.id", role.getId());
				getSession().setAttribute(String.valueOf(role.getId()), roleFunctionList);
			}
			for (TSRoleFunction roleFunction : roleFunctionList) {
				TSFunction function = (TSFunction) roleFunction.getTSFunction();
				loginActionlist.add(function);
			}
		}
		List<TSFunction> bigActionlist = new ArrayList<TSFunction>();// 一级权限菜单
		List<TSFunction> smailActionlist = new ArrayList<TSFunction>();// 二级权限菜单
		if (loginActionlist.size() > 0) {
			for (TSFunction function : loginActionlist) {
				if (function.getFunctionLevel() == 0) {
					bigActionlist.add(function);
				} else if (function.getFunctionLevel() == 1) {
					smailActionlist.add(function);
				}
			}
		}
		
		// 菜单栏排序
		Collections.sort(bigActionlist, new NumberComparator());
		Collections.sort(smailActionlist, new NumberComparator());
		String logString = ListtoMenu.getEasyuiMenu(bigActionlist, smailActionlist);
		getRequest().setAttribute("loginMenu", logString);
		getRequest().setAttribute("parentFun", bigActionlist);
		getRequest().setAttribute("roleName", roles);
		getRequest().setAttribute("childFun", smailActionlist);
		return "left";
	}
	
	//根据角色ＩＤ获取拥有权限
	 @SuppressWarnings("unchecked")
	public final List<TSRoleFunction> getSessionTSRoleFunction(String roleId){
		 getSession().setMaxInactiveInterval(-1);
		if (getSession().getAttributeNames().hasMoreElements()) {
			List TSRoleFunctionList = (List) getSession().getAttribute(roleId);
			if (TSRoleFunctionList != null){
				return TSRoleFunctionList;
			}else{
				return null;
			}
		}
		return null;
	}
}
