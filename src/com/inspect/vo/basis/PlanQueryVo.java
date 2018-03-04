package com.inspect.vo.basis;

import java.io.Serializable;

import com.inspect.vo.comon.BaseVo;

public class PlanQueryVo extends BaseVo implements Serializable {
	private static final long serialVersionUID = 219829299182428912L;
	
	private String pname;	//计划名称
	private String pstartdate; //计划开始日期
	private String penddate;   //计划结束日期
	private int plineid;      //巡检线路id
	private String pstarttime; // 计划开始时间
	private String pendtime;	//计划结束时间
	private int ptype;         //巡检方式 1：每天一次  2：每天多次  3：按周巡检  4：按月巡检
	private String pinspecttime; //巡检时间
	private String presttime; 	 //休息时间
	private String pweekday;	//巡检日期
	private int pstatus;        //计划状态  0:未巡检 1：已巡检
	private int pgid;           //巡检班组
	private int puid;			//巡检人员
	private int pid;
	
	private String plinename;
	private String username;
	private String groupname;
	private String entname;   //公司名称
	private int    esum;       //单位下基础设备的总数目；
	private int   pointCount;   //巡检点个数//任务下巡检设备总数
	private int queryCount;    //已寻点个数
	private int unqueryCount;   //未寻点个数
	private int  errqueryCount;//有问题个数
	private  String  lineStatus;  //线路状态
	private int itaskid;   //任务id
	private String xreptime;  //巡检提交时间
	private int pequid;     //设备id
	private String qyerytype;
	private String  rate;  //设备巡检完成比率 （某任务下已巡检的设备数量除以此任务下设备的总数）
	private String  picrate;  //巡检合格比例（已巡检的设备中图片正常的比例）
	private int picexcepnum;//图片异常的个数
	private String  planrate; //计划完成率（当季度任务下的所有设备除以本市设备总数 的3倍）
	private  double  comrate;//此跟rate一个意思，方便对合格率进行排序巡检完成比率（某任务下已巡检的设备数量除以此任务下设备的总数）
	private  String taskids;   // 任务id的集合 
	private String  totalrate;  //  总完成率  已巡检个数/合同站点数
	private double totrate;  //用于比较
	
	public double getTotrate() {
		return totrate;
	}
	public void setTotrate(double totrate) {
		this.totrate = totrate;
	}
	public String getTotalrate() {
		return totalrate;
	}
	public void setTotalrate(String totalrate) {
		this.totalrate = totalrate;
	}
	
	
	public String getTaskids() {
		return taskids;
	}
	public void setTaskids(String taskids) {
		this.taskids = taskids;
	}
	public double getComrate() {
		return comrate;
	}
	public void setComrate(double comrate) {
		this.comrate = comrate;
	}
	public String getPlanrate() {
		return planrate;
	}
	public void setPlanrate(String planrate) {
		this.planrate = planrate;
	}
	public int getEsum() {
		return esum;
	}
	public void setEsum(int esum) {
		this.esum = esum;
	}
	public String getEntname() {
		return entname;
	}
	public void setEntname(String entname) {
		this.entname = entname;
	}
	public String getPicrate() {
		return picrate;
	}
	public void setPicrate(String picrate) {
		this.picrate = picrate;
	}
	public int getPicexcepnum() {
		return picexcepnum;
	}
	public void setPicexcepnum(int picexcepnum) {
		this.picexcepnum = picexcepnum;
	}
	public String getRate() {
		return rate;
	}
	public void setRate(String rate) {
		this.rate = rate;
	}
	public String getQyerytype() {
		return qyerytype;
	}
	public void setQyerytype(String qyerytype) {
		this.qyerytype = qyerytype;
	}
	public String getGroupname() {
		return groupname;
	}
	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}
	public int getErrqueryCount() {
		return errqueryCount;
	}
	public void setErrqueryCount(int errqueryCount) {
		this.errqueryCount = errqueryCount;
	}
	public int getPequid() {
		return pequid;
	}
	public void setPequid(int pequid) {
		this.pequid = pequid;
	}
	public String getXreptime() {
		return xreptime;
	}
	public void setXreptime(String xreptime) {
		this.xreptime = xreptime;
	}
	public int getItaskid() {
		return itaskid;
	}
	public void setItaskid(int itaskid) {
		this.itaskid = itaskid;
	}
	public int getPointCount() {
		return pointCount;
	}
	public void setPointCount(int pointCount) {
		this.pointCount = pointCount;
	}
	public int getQueryCount() {
		return queryCount;
	}
	public void setQueryCount(int queryCount) {
		this.queryCount = queryCount;
	}
	public int getUnqueryCount() {
		return unqueryCount;
	}
	public void setUnqueryCount(int unqueryCount) {
		this.unqueryCount = unqueryCount;
	}
	public String getLineStatus() {
		return lineStatus;
	}
	public void setLineStatus(String lineStatus) {
		this.lineStatus = lineStatus;
	}
	public PlanQueryVo() {super();
		ptype = -1 ;
		
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
	public int getPstatus() {
		return pstatus;
	}
	public void setPstatus(int pstatus) {
		this.pstatus = pstatus;
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
	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}
	public String getPlinename() {
		return plinename;
	}
	public void setPlinename(String plinename) {
		this.plinename = plinename;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
}
