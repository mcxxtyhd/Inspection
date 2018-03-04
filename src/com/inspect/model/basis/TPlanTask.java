package com.inspect.model.basis;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.inspect.model.BaseModel;
/**
 * 巡检计划生成的任务实体类
 * @author wzs
 */
@Entity
@Table(name="t_plan_task")
public class TPlanTask extends BaseModel implements Serializable {

	private static final long serialVersionUID = -6521702560767034272L;
	private String pname;	//计划名称
	private String pstartdate; //计划开始日期
	private String penddate;   //计划结束日期
	private int plineid;      //巡检线路
	private String pstarttime; // 计划开始时间
	private String pendtime;	//计划结束时间
	private int ptype;         //巡检方式 1：每天一次  2：每天多次  3：按周巡检  4：按月巡检
	private String pinspecttime; //巡检时间
	private String presttime; 	 //休息时间
	private String pweekday;	//巡检日期
	private int pgid;           //巡检班组
	private int puid;			//巡检人员
	private int pstatus;        //计划状态  0:未巡检 1：已巡检
	private String pdesc;		//计划描述
	private TPlan tplan;        //计划对象
	
	public TPlanTask() {
		super();
	}
	public TPlanTask(String pname, int plineid, int puid) {
		super();
		this.pname = pname;
		this.plineid = plineid;
		this.puid = puid;
	}
	public TPlanTask(String pname, String pstartdate, String penddate, int plineid, String pstarttime, String pendtime, int ptype, String pinspecttime, String presttime, String pweekday, int pgid, int puid, int pstatus, String pdesc) {
		super();
		this.pname = pname;
		this.pstartdate = pstartdate;
		this.penddate = penddate;
		this.plineid = plineid;
		this.pstarttime = pstarttime;
		this.pendtime = pendtime;
		this.ptype = ptype;
		this.pinspecttime = pinspecttime;
		this.presttime = presttime;
		this.pweekday = pweekday;
		this.pgid = pgid;
		this.puid = puid;
		this.pstatus = pstatus;
		this.pdesc = pdesc;
	}
	@Column(length=100)
	public String getPname() {
		return pname;
	}
	public void setPname(String pname) {
		this.pname = pname;
	}
	@Column(length=40)
	public String getPstartdate() {
		return pstartdate;
	}
	public void setPstartdate(String pstartdate) {
		this.pstartdate = pstartdate;
	}
	@Column(length=40)
	public String getPenddate() {
		return penddate;
	}
	public void setPenddate(String penddate) {
		this.penddate = penddate;
	}
	public int getPlineid() {
		return plineid;
	}
	public void setPlineid(int plineid) {
		this.plineid = plineid;
	}
	@Column(length=40)
	public String getPstarttime() {
		return pstarttime;
	}
	public void setPstarttime(String pstarttime) {
		this.pstarttime = pstarttime;
	}
	@Column(length=40)
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
	@Column(length=40)
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
	@Column(length=40)
	public String getPweekday() {
		return pweekday;
	}
	public void setPweekday(String pweekday) {
		this.pweekday = pweekday;
	}
	public int getPgid() {
		return pgid;
	}
	public void setPgid(int pgid) {
		this.pgid = pgid;
	}
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
	@Column(length=100)
	public String getPdesc() {
		return pdesc;
	}
	public void setPdesc(String pdesc) {
		this.pdesc = pdesc;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="pid")
	public TPlan getTplan() {
		return tplan;
	}
	public void setTplan(TPlan tplan) {
		this.tplan = tplan;
	}
}
