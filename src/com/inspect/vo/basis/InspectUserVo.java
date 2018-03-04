package com.inspect.vo.basis;

import java.io.Serializable;
import com.inspect.vo.comon.BaseVo;

public class InspectUserVo extends BaseVo implements Serializable {
	private static final long serialVersionUID = -9035991864830453023L;
	private String iunumber;//编号
	private String iuname;//名称
	private String iupwd;//密码
	private String iusex;//性别
	private String iumobile;//手机
	private String iudesc;//描述
	private String groupid;//所属班组
	private String groupname;//班组名称
	private int    istatus;//判断是否登录，0表示没有登录，1表示已经登陆过
	private String  imark;//标志，作为是否重新登陆的唯一标志，属于随机产生（存放在终端状态表t_terminate_status_report中）
	private String irealname;//真实姓名（用于在数据统计中显示，代替之前的iuname）
	private String pcomname; //公司名称
	
	public String getPcomname() {
		return pcomname;
	}
	public void setPcomname(String pcomname) {
		this.pcomname = pcomname;
	}
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
	public int getIstatus() {
		return istatus;
	}
	public void setIstatus(int istatus) {
		this.istatus = istatus;
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
	public String getGroupid() {
		return groupid;
	}
	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}
	public String getGroupname() {
		return groupname;
	}
	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}
}
