package com.inspect.model.monitor;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.inspect.model.BaseModel;

/**
 * 巡检人员实时上报信息实体类
 * @author wzs
 */
@Entity
@Table(name = "t_terminate_status_report")
public class TTerminateStatusReport extends BaseModel implements Serializable {

	private static final long serialVersionUID = -7201980714660996957L;

	private String rpterminateid; // 终端编号
	private int rpuserid; // 巡检员id
	private int rpgroupid;//班组ID
	private int rplineid; // 线路id
	private String rplogintime; // 登录时间
	private String rplogouttime; // 退出时间
	private int flag; // 登录状态 0：未登录 1：已登录
	private String  rmark;
	

	public String getRmark() {
		return rmark;
	}

	public void setRmark(String rmark) {
		this.rmark = rmark;
	}

	public TTerminateStatusReport() {
	}

	public TTerminateStatusReport(String rpterminateid, int rpuserid,
			int rplineid, String rplogintime, String rplogouttime, int flag,int rpgroupid) {
		this.rpterminateid = rpterminateid;
		this.rpuserid = rpuserid;
		this.rplineid = rplineid;
		this.rplogintime = rplogintime;
		this.rplogouttime = rplogouttime;
		this.flag = flag;
		this.rpgroupid = rpgroupid;
	}

	public String getRpterminateid() {
		return rpterminateid;
	}

	public void setRpterminateid(String rpterminateid) {
		this.rpterminateid = rpterminateid;
	}

	public int getRpuserid() {
		return rpuserid;
	}

	public void setRpuserid(int rpuserid) {
		this.rpuserid = rpuserid;
	}
	
	public int getRpgroupid() {
		return rpgroupid;
	}

	public void setRpgroupid(int rpgroupid) {
		this.rpgroupid = rpgroupid;
	}

	public int getRplineid() {
		return rplineid;
	}

	public void setRplineid(int rplineid) {
		this.rplineid = rplineid;
	}

	public String getRplogintime() {
		return rplogintime;
	}

	public void setRplogintime(String rplogintime) {
		this.rplogintime = rplogintime;
	}

	public String getRplogouttime() {
		return rplogouttime;
	}

	public void setRplogouttime(String rplogouttime) {
		this.rplogouttime = rplogouttime;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}
}
