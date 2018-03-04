package com.inspect.vo.monitor;

import java.io.Serializable;

import com.inspect.vo.comon.BaseVo;

public class TerminaStatusReportVo extends BaseVo implements Serializable {

	private static final long serialVersionUID = -3118675083713269528L;
	private int flag; // 登录状态 0：未登录 1：已登录
	private String rpedate;
	private int rpgroupid;// 班组ID
	private String rpgroupname;
	private int rplineid; // 线路id
	private String rplinename;
	private String rplogintime; // 登录时间
	private String rplogouttime; // 退出时间
	private String rpsdate;
	private String rpterminateid; // 终端编号
	private String rptermintename;
	private int rpuserid; // 巡检员id
	private String rpusername;

	public int getFlag() {
		return flag;
	}

	public String getRpedate() {
		return rpedate;
	}

	public int getRpgroupid() {
		return rpgroupid;
	}

	public String getRpgroupname() {
		return rpgroupname;
	}

	public int getRplineid() {
		return rplineid;
	}

	public String getRplinename() {
		return rplinename;
	}

	public String getRplogintime() {
		return rplogintime;
	}

	public String getRplogouttime() {
		return rplogouttime;
	}

	public String getRpsdate() {
		return rpsdate;
	}

	public String getRpterminateid() {
		return rpterminateid;
	}

	public String getRptermintename() {
		return rptermintename;
	}

	public int getRpuserid() {
		return rpuserid;
	}

	public String getRpusername() {
		return rpusername;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public void setRpedate(String rpedate) {
		this.rpedate = rpedate;
	}

	public void setRpgroupid(int rpgroupid) {
		this.rpgroupid = rpgroupid;
	}

	public void setRpgroupname(String rpgroupname) {
		this.rpgroupname = rpgroupname;
	}

	public void setRplineid(int rplineid) {
		this.rplineid = rplineid;
	}

	public void setRplinename(String rplinename) {
		this.rplinename = rplinename;
	}

	public void setRplogintime(String rplogintime) {
		this.rplogintime = rplogintime;
	}

	public void setRplogouttime(String rplogouttime) {
		this.rplogouttime = rplogouttime;
	}

	public void setRpsdate(String rpsdate) {
		this.rpsdate = rpsdate;
	}

	public void setRpterminateid(String rpterminateid) {
		this.rpterminateid = rpterminateid;
	}

	public void setRptermintename(String rptermintename) {
		this.rptermintename = rptermintename;
	}

	public void setRpuserid(int rpuserid) {
		this.rpuserid = rpuserid;
	}

	public void setRpusername(String rpusername) {
		this.rpusername = rpusername;
	}

}
