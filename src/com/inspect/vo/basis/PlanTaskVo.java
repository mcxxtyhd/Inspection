package com.inspect.vo.basis;

import java.io.Serializable;

import com.inspect.vo.comon.BaseVo;

public class PlanTaskVo extends BaseVo implements Serializable {

	private static final long serialVersionUID = 7441295449640961913L;
    
	private String pname;	//计划名称
	private String pstartdate; //计划开始日期
	private String penddate;   //计划结束日期
	
	private String pstarttime; // 计划开始时间
	private String pendtime;	//计划结束时间
	private int ptype;         //巡检方式 1：每天一次  2：每天多次  3：按周巡检  4：按月巡检
	private String pinspecttime; //巡检时间
	private String presttime; 	 //休息时间
	private String pweekday;	//巡检日期
	
	private int puid;			//巡检人员
	private int pstatus;        //计划状态  1:未巡检 2：已巡检
	private String pdesc;		//计划描述
	
	private String plinename;
	private String username;
	private String groupname;
	
	private int pcomflag;//一个任务中未巡检的设备总数 //判断是否完成1表示未超时 0表示超时
	
	private int pgid;           //巡检班组
	private int plineid;      //巡检线路
	
	public int getPgid() {
		return pgid;
	}
	public void setPgid(int pgid) {
		this.pgid = pgid;
	}
	public int getPlineid() {
		return plineid;
	}
	public void setPlineid(int plineid) {
		this.plineid = plineid;
	}
	
	
//	private String plineid;      //巡检线路
//	private String pgid;           //巡检班组
//	
//	public String getPlineid() {
//		return plineid;
//	}
//	public void setPlineid(String plineid) {
//		this.plineid = plineid;
//	}
//	public String getPgid() {
//		return pgid;
//	}
//	public void setPgid(String pgid) {
//		this.pgid = pgid;
//	}

	

	public int getPcomflag() {
		return pcomflag;
	}
	public void setPcomflag(int pcomflag) {
		this.pcomflag = pcomflag;
	}
	public String getGroupname() {
		return groupname;
	}
	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}
	public String getPlinename() {
		return plinename;
	}
	public void setPlinename(String plinename) {
		this.plinename = plinename;
	}
	public String getPname() {
		return pname;
	}
	public void setPname(String pname) {
		this.pname = pname;
	}
	public String getPstartdate() {
		return pstartdate;
	}
	public void setPstartdate(String pstartdate) {
		this.pstartdate = pstartdate;
	}
	public String getPenddate() {
		return penddate;
	}
	public void setPenddate(String penddate) {
		this.penddate = penddate;
	}
//	public int getPlineid() {
//		return plineid;
//	}
//	public void setPlineid(int plineid) {
//		this.plineid = plineid;
//	}
	public String getPstarttime() {
		return pstarttime;
	}
	public void setPstarttime(String pstarttime) {
		this.pstarttime = pstarttime;
	}
	public String getPendtime() {
		return pendtime;
	}
	public void setPendtime(String pendtime) {
		this.pendtime = pendtime;
	}
	public int getPtype() {
		return ptype;
	}
	public void setPtype(int ptype) {
		this.ptype = ptype;
	}
	public String getPinspecttime() {
		return pinspecttime;
	}
	public void setPinspecttime(String pinspecttime) {
		this.pinspecttime = pinspecttime;
	}
	public String getPresttime() {
		return presttime;
	}
	public void setPresttime(String presttime) {
		this.presttime = presttime;
	}
	public String getPweekday() {
		return pweekday;
	}
	public void setPweekday(String pweekday) {
		this.pweekday = pweekday;
	}
//	public int getPgid() {
//		return pgid;
//	}
//	public void setPgid(int pgid) {
//		this.pgid = pgid;
//	}
	public int getPuid() {
		return puid;
	}
	public void setPuid(int puid) {
		this.puid = puid;
	}
	public int getPstatus() {
		return pstatus;
	}
	public void setPstatus(int pstatus) {
		this.pstatus = pstatus;
	}
	public String getPdesc() {
		return pdesc;
	}
	public void setPdesc(String pdesc) {
		this.pdesc = pdesc;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
}
