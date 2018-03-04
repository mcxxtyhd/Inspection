package com.inspect.vo.basis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.inspect.model.BaseModel;
import com.inspect.model.basis.TPlan;
/**
 * 巡检计划生成的任务实体类
 * @author wzs
 */

public class TPlanTaskVo extends BaseModel implements Serializable {

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
	
	List <LineVo> lineVoList=new ArrayList<LineVo>();//LineVolist集合
	
	public TPlanTaskVo() {
		super();
	}
	public TPlanTaskVo(String pname, int plineid, int puid) {
		super();
		this.pname = pname;
		this.plineid = plineid;
		this.puid = puid;
	}
	public TPlanTaskVo(String pname, String pstartdate, String penddate, int plineid, String pstarttime, String pendtime, int ptype, String pinspecttime, String presttime, String pweekday, int pgid, int puid, int pstatus, String pdesc) {
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
	public int getPlineid() {
		return plineid;
	}
	public void setPlineid(int plineid) {
		this.plineid = plineid;
	}
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
	public String getPdesc() {
		return pdesc;
	}
	public void setPdesc(String pdesc) {
		this.pdesc = pdesc;
	}


	public List<LineVo> getLineVoList() {
		return lineVoList;
	}
	public void setLineVoList(List<LineVo> lineVoList) {
		this.lineVoList = lineVoList;
	}
	
}
