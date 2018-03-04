package com.inspect.model.basis;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.inspect.model.BaseModel;

/**
 * 巡检人员实体类
 * @author wzs
 * @version 1.0
 */
@Entity
@Table(name="t_inspect_user")
public class TInspectUser extends BaseModel implements Serializable {
	private static final long serialVersionUID = 5197794356053851300L;
	private String iunumber;//编号
	private String iuname;//巡检员登录名称
	private String iupwd;//密码
	private String iusex;//性别
	private String iumobile;//手机
	private String iudesc;//描述
	private TGroup tgroup;//所属班组
	private String  imark;//标志，作为是否重新登陆的唯一标志，属于随机产生
//	private int    istatus;//判断是否登录，0表示没有登录，1表示已经登陆过
	private String irealname;//真实姓名（用于在数据统计中显示，代替之前的iuname）
	
	
	public String getIrealname() {
		return irealname;
	}
	public void setIrealname(String irealname) {
		this.irealname = irealname;
	}
	public String getImark() {
		return imark;
	}
	public void setImark(String imark) {
		this.imark = imark;
	}
/*	public int getIstatus() {
		return istatus;
	}
	public void setIstatus(int istatus) {
		this.istatus = istatus;
	}*/
	public TInspectUser() {
	}
	public TInspectUser(String iunumber, String iuname, String iupwd) {
		this.iunumber = iunumber;
		this.iuname = iuname;
		this.iupwd = iupwd;
	}
	public TInspectUser(String iunumber, String iuname, String iupwd,
			String iusex, String iumobile,String iudesc,TGroup tgroup) {
		this.iunumber = iunumber;
		this.iuname = iuname;
		this.iupwd = iupwd;
		this.iusex = iusex;
		this.iumobile = iumobile;
		this.iudesc = iudesc;
		this.tgroup= tgroup;
	}
	public String getIunumber() {
		return iunumber;
	}
	public void setIunumber(String iunumber) {
		this.iunumber = iunumber;
	}
	public String getIuname() {
		return iuname;
	}
	public void setIuname(String iuname) {
		this.iuname = iuname;
	}
	public String getIupwd() {
		return iupwd;
	}
	public void setIupwd(String iupwd) {
		this.iupwd = iupwd;
	}
	public String getIusex() {
		return iusex;
	}
	public void setIusex(String iusex) {
		this.iusex = iusex;
	}
	public String getIumobile() {
		return iumobile;
	}
	public void setIumobile(String iumobile) {
		this.iumobile = iumobile;
	}
	public String getIudesc() {
		return iudesc;
	}
	public void setIudesc(String iudesc) {
		this.iudesc = iudesc;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="groupid",nullable=true)
	public TGroup getTgroup() {
		return tgroup;
	}
	public void setTgroup(TGroup tgroup) {
		this.tgroup = tgroup;
	}
	
}
