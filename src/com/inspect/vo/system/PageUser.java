package com.inspect.vo.system;

import java.io.Serializable;

import com.inspect.vo.comon.BaseVo;

public class PageUser extends BaseVo implements Serializable {
	
	private static final long serialVersionUID = -7166350553203519799L;
	
	private String email;    //用户邮箱
	private String mobile;   //用户手机
	private String password; //用户密码
	private String realname; //真实姓名
	private String udesc;    //用户描述
	private String username; //用户名称
	private String roleid;   //角色编号
	private String rolename; //角色名称
	private String checkpassword; //重复密码
	private String newpassword;//新密码
	private String oldpassword;//原密码
	private String sex;	//用户性别
	private String entname;
	public String getEntname() {
		return entname;
	}
	public void setEntname(String entname) {
		this.entname = entname;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getOldpassword() {
		return oldpassword;
	}
	public void setOldpassword(String oldpassword) {
		this.oldpassword = oldpassword;
	}
	public String getNewpassword() {
		return newpassword;
	}
	public void setNewpassword(String newpassword) {
		this.newpassword = newpassword;
	}
	public String getCheckpassword() {
		return checkpassword;
	}
	public void setCheckpassword(String checkpassword) {
		this.checkpassword = checkpassword;
	}
	public String getRoleid() {
		return roleid;
	}
	public void setRoleid(String roleid) {
		this.roleid = roleid;
	}
	public String getRolename() {
		return rolename;
	}
	public void setRolename(String rolename) {
		this.rolename = rolename;
	}
	public String getEmail() {
		return email;
	}
	public String getMobile() {
		return mobile;
	}
	public String getPassword() {
		return password;
	}
	public String getRealname() {
		return realname;
	}
	public String getUdesc() {
		return udesc;
	}
	public String getUsername() {
		return username;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setRealname(String realname) {
		this.realname = realname;
	}
	public void setUdesc(String udesc) {
		this.udesc = udesc;
	}
	public void setUsername(String username) {
		this.username = username;
	}
}
