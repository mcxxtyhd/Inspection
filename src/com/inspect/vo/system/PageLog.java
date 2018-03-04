package com.inspect.vo.system;

import java.io.Serializable;
import java.util.Date;

import com.inspect.vo.comon.BaseVo;

public class PageLog extends BaseVo implements Serializable {

	private static final long serialVersionUID = -8473414953033832835L;

	private String opevent;	 //操作事件
	private String opstatus;   //操作状态 0：成功 1：失败
	private String optable; //操作表名
	private Date optime; //操作时间
	private String opuser;  //操作人
	private String startdate;
	private String enddate;
	public String getEnddate() {
		return enddate;
	}
	public String getOpevent() {
		return opevent;
	}
	public String getOpstatus() {
		return opstatus;
	}
	public String getOptable() {
		return optable;
	}
	public Date getOptime() {
		return optime;
	}
	public String getOpuser() {
		return opuser;
	}
	public String getStartdate() {
		return startdate;
	}
	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}
	public void setOpevent(String opevent) {
		this.opevent = opevent;
	}
	public void setOpstatus(String opstatus) {
		this.opstatus = opstatus;
	}
	public void setOptable(String optable) {
		this.optable = optable;
	}
	public void setOptime(Date optime) {
		this.optime = optime;
	}
	public void setOpuser(String opuser) {
		this.opuser = opuser;
	}
	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}
}
