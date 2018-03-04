package com.inspect.action.system;

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
import com.inspect.model.system.TSRole;
import com.inspect.model.system.TSUser;
import com.inspect.service.SystemServiceI;
import com.inspect.util.common.ExceptionUtil;
import com.inspect.util.common.StringUtils;
import com.inspect.vo.comon.Json;
import com.inspect.vo.system.PageUser;
import com.opensymphony.xwork2.ModelDriven;

@Namespace("/system")
@Action(value="userAction",results={
		@Result(name="userList",location="/webpage/system/user/userList.jsp"),
		@Result(name="userAdd",location="/webpage/system/user/userAdd.jsp"),
		@Result(name="userEdit",location="/webpage/system/user/userEdit.jsp"),
		@Result(name="changepassword",location="/webpage/system/user/changepassword.jsp"),
		@Result(name="userinfo",location="/webpage/system/user/userinfo.jsp")})
public class UserAction extends BaseAction implements ModelDriven<PageUser> {

	private static final long serialVersionUID = 3401924975978073968L;

	private static final Logger logger = Logger.getLogger(UserAction.class);
	
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
	
	public String userList(){
		getRequest().setAttribute("EnterpriseList",systemService.comboboxEnterprise(queryEnterpriseByWhere()));
		return "userList";
	}
	
	public String userAdd(){
		getRequest().setAttribute("RoleList",systemService.combobox(queryEnterpriseByWhere()));
		getRequest().setAttribute("EnterpriseList",systemService.comboboxEnterprise(queryEnterpriseByWhere()));
		return "userAdd";
	}
	
	public String userEdit(){
		getRequest().setAttribute("RoleList",systemService.combobox(queryEnterpriseByWhere()));
		getRequest().setAttribute("EnterpriseList",systemService.comboboxEnterprise(queryEnterpriseByWhere()));
		return "userEdit";
	}
	
	public void userDatagrid(){
		int page =Integer.parseInt(ServletActionContext.getRequest().getParameter("page") );
		int rows =Integer.parseInt(ServletActionContext.getRequest().getParameter("rows") );
		Map<String, Object> map = systemService.findUserDatagrid(pageUser,page,rows,querySql1());
		writeJson(map);
	}
	
	@LogAnnotation(event="添加用户",tablename="t_s_user")
	public void addUser(){
		Json j=new Json();
		try{
			long namecount=systemService.countByUserName(pageUser.getUsername(),queryEnterpriseByWhere());
			if(namecount>0){
			   j.setMsg("用户：【"+pageUser.getUsername()+"】已存在,不能重复添加!");
			   j.setSuccess(false);
			   setOperstatus(1);
			}else{
				systemService.addUser(pageUser);
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
	
	@LogAnnotation(event="修改用户",tablename="t_s_user")
	public void editUser(){
		Json j=new Json();
		try{
			systemService.editUser(pageUser);
			j.setMsg("修改成功！");
			j.setSuccess(true);
		}catch(Exception e){
			setOperstatus(1);
			j.setMsg("修改失败！");
			logger.error(ExceptionUtil.getExceptionMessage(e));
		}
		writeJson(j);
	}
	
	@LogAnnotation(event="删除用户",tablename="t_s_user")
	public void deleteUser(){
		Json j=new Json();
		try{
			if(!StringUtils.isEmpty(pageUser.getIds())){
				systemService.deleteUser(pageUser.getIds());
				j.setSuccess(true);
				j.setMsg("删除成功！");
			}
		}catch(Exception e){
			setOperstatus(1);
			j.setMsg("删除失败！");
			logger.error(ExceptionUtil.getExceptionMessage(e));
		}
		writeJson(j);
	}
	
	public void roleNmaeList(){
		List<TSRole> r=systemService.combobox(queryEnterpriseByWhere());
		writeJson(r);
	}
	
	//修改密码
	public String changepassword() {
		TSUser user = getSessionUserName();
		getRequest().setAttribute("user", user);
		return "changepassword";
	}
	
	@LogAnnotation(event="修改密码",tablename="t_s_user")
	public void changeSavePassword(){
		Json j=new Json();
		try{
			TSUser user = this.getSessionUserName();
			if (!pageUser.getOldpassword().equals(user.getPassword())) {
				j.setMsg("原密码不正确");
				j.setSuccess(false);
				setOperstatus(1);
			} else {
				user.setPassword(pageUser.getNewpassword());
				systemService.editUser(user);
				j.setMsg("修改成功！");
				j.setSuccess(true);
			}
		}catch(Exception e){
			setOperstatus(1);
			j.setMsg("修改失败！");
			logger.error(ExceptionUtil.getExceptionMessage(e));
		}
		writeJson(j);
	}
	
	//用户信息
	public String userinfo() {
		TSUser user = getSessionUserName();
		getRequest().setAttribute("user", user);
		return "userinfo";
	}
}
