package com.inspect.vo.basis;

public class TermInspectUserVo {
	private int id;//主键
	private int entid; // 企业ID
	private String iunumber;//编号
	private String iuname;//名称
	private String iupwd;//密码
	private String iusex;//性别
	private String iumobile;//手机
	//private String iudesc;//描述
	private String groupid;//所属班组
	private String groupname;//班组名称
	private String   radius;//半径
	private String  imark;//标志，作为是否重新登陆的唯一标志，属于随机产生（存放在终端状态表t_terminate_status_report中）
	private String tietapicnum;//铁塔图片数量
	private String  shineipicnum;//室内图片数量
	
	
	

	public String getTietapicnum() {
		return tietapicnum;
	}
	public void setTietapicnum(String tietapicnum) {
		this.tietapicnum = tietapicnum;
	}
	public String getShineipicnum() {
		return shineipicnum;
	}
	public void setShineipicnum(String shineipicnum) {
		this.shineipicnum = shineipicnum;
	}
	public String getRadius() {
		return radius;
	}
	public void setRadius(String radius) {
		this.radius = radius;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getEntid() {
		return entid;
	}
	public void setEntid(int entid) {
		this.entid = entid;
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

	public String getImark() {
		return imark;
	}
	public void setImark(String imark) {
		this.imark = imark;
	}
	
}

