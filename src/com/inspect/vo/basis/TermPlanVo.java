package com.inspect.vo.basis;
import java.util.ArrayList;
import java.util.List;
public class TermPlanVo  {
	private int entid; // 企业ID
	private String planname;//计划名称
	private int id;//线路id
	private int itaskid;//任务id
	private String lname; // 线路名称
	private String pstartdate; //任务周期
	private  List<TermPointVo> tpointVoList=new ArrayList<TermPointVo>();//点的集合
	
	
	public String getPstartdate() {
		return pstartdate;
	}
	public void setPstartdate(String pstartdate) {
		this.pstartdate = pstartdate;
	}
	public int getItaskid() {
		return itaskid;
	}
	public void setItaskid(int itaskid) {
		this.itaskid = itaskid;
	}
	public String getLname() {
		return lname;
	}
	public void setLname(String lname) {
		this.lname = lname;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public List<TermPointVo> getTpointVoList() {
		return tpointVoList;
	}
	public void setTpointVoList(List<TermPointVo> tpointVoList) {
		this.tpointVoList = tpointVoList;
	}

	public String getPlanname() {
		return planname;
	}
	public void setPlanname(String planname) {
		this.planname = planname;
	}
	public int getEntid() {
		return entid;
	}
	public void setEntid(int entid) {
		this.entid = entid;
	}
	
	 


	
	
}
