package com.inspect.vo.problem;

import com.inspect.vo.comon.BaseVo;

public class RepairTaskVo extends BaseVo {
	private String rcontent;// 抢修任务内容
	private int rgid;     //   维护队
	private int ruid;     //   维护队人员
	private int rflag;    //   抢修任务状态 1：未处理   2：已下发 3：已处理
	private String rflag1;    // 用于导出将数字变成对应文字  抢修任务状态 1：未处理   2：已下发 3：已处理
	private String rsenddate; // 任务下发时间
	private String rrepdate;//  任务抢修上报时间
	private String rternumber;//终端编号
	private String rdesc;// 抢修任务备注
	private String rgname;//维护队名称
	private String rcategory;//将修任务类别 有两种：抢修和优化
	private String rendtime; //将修时限，即在此日期之前完成
	private int rcomflag;//判断是否完成1表示未超时 0表示超时
	private int index;//用于紧急任务导出序号
	
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public String getRflag1() {
		return rflag1;
	}
	public void setRflag1(String rflag1) {
		this.rflag1 = rflag1;
	}
	public int getRcomflag() {
		return rcomflag;
	}
	public void setRcomflag(int rcomflag) {
		this.rcomflag = rcomflag;
	}
	public String getRcategory() {
		return rcategory;
	}
	public void setRcategory(String rcategory) {
		this.rcategory = rcategory;
	}
	public String getRendtime() {
		return rendtime;
	}
	public void setRendtime(String rendtime) {
		this.rendtime = rendtime;
	}
	public String getRgname() {
		return rgname;
	}
	public void setRgname(String rgname) {
		this.rgname = rgname;
	}
	public String getRuname() {
		return runame;
	}
	public void setRuname(String runame) {
		this.runame = runame;
	}
	private String runame;
	public String getRcontent() {
		return rcontent;
	}
	public void setRcontent(String rcontent) {
		this.rcontent = rcontent;
	}
	public int getRgid() {
		return rgid;
	}
	public void setRgid(int rgid) {
		this.rgid = rgid;
	}
	public int getRuid() {
		return ruid;
	}
	public void setRuid(int ruid) {
		this.ruid = ruid;
	}
	public int getRflag() {
		return rflag;
	}
	public void setRflag(int rflag) {
		this.rflag = rflag;
	}
	public String getRsenddate() {
		return rsenddate;
	}
	public void setRsenddate(String rsenddate) {
		this.rsenddate = rsenddate;
	}
	public String getRrepdate() {
		return rrepdate;
	}
	public void setRrepdate(String rrepdate) {
		this.rrepdate = rrepdate;
	}
	public String getRternumber() {
		return rternumber;
	}
	public void setRternumber(String rternumber) {
		this.rternumber = rternumber;
	}
	public String getRdesc() {
		return rdesc;
	}
	public void setRdesc(String rdesc) {
		this.rdesc = rdesc;
	}
}
