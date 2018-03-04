package com.inspect.model.system;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.inspect.model.BaseModel;

/**
 * 系统用户实体类
 * @author wzs
 * @version 1.0
 */
@Entity
@Table(name="t_s_user")
public class TSUser extends BaseModel implements Serializable {

	private static final long serialVersionUID = -6994999499813258857L;

	private String username; //用户名称
	private String realname; //真实姓名
	private String password; //用户密码
	private String sex;	//用户性别
	private String mobile;   //用户手机
	private String email;    //用户邮箱
	private String udesc;    //用户描述
	
	
	public TSUser() {
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUdesc() {
		return udesc;
	}

	public void setUdesc(String udesc) {
		this.udesc = udesc;
	}
}
