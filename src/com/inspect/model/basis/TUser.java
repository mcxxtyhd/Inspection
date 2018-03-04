package com.inspect.model.basis;

import java.io.Serializable;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.inspect.model.BaseModel;
import com.inspect.model.system.Enterprise;

/***
 * 
 * information :用户信息表
 *
 */

//@Entity
//@Table(name="t_user")
public class TUser extends BaseModel implements Serializable {
	private String username ;
	private String password ;
	private String phoneNumber ;
	private String sex ;
	private String email ;
	private String pdesc ;
	//所属单位
	private Enterprise enterprise ;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPdesc() {
		return pdesc;
	}
	public void setPdesc(String pdesc) {
		this.pdesc = pdesc;
	}
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "enterpriseid")
	public Enterprise getEnterprise() {
		return enterprise;
	}
	
	public void setEnterprise(Enterprise enterprise) {
		this.enterprise = enterprise;
	}
	
	
	
}
