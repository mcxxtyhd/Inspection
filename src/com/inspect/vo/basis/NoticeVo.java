package com.inspect.vo.basis;

import java.io.Serializable;

import com.inspect.vo.comon.BaseVo;

public class NoticeVo extends BaseVo implements Serializable {

	private static final long serialVersionUID = 3932475718770709260L;
	private String nstarttime; // 开始时间
	private String nendtime;	//结束时间
	private String ntype;         //类型
	private String ncontent;   //公告内容
	private String publisher;  //发布人
	
	public String getPublisher() {
		return publisher;
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	public String getNstarttime() {
		return nstarttime;
	}
	public void setNstarttime(String nstarttime) {
		this.nstarttime = nstarttime;
	}
	public String getNendtime() {
		return nendtime;
	}
	public void setNendtime(String nendtime) {
		this.nendtime = nendtime;
	}

	public String getNtype() {
		return ntype;
	}
	public void setNtype(String ntype) {
		this.ntype = ntype;
	}
	public String getNcontent() {
		return ncontent;
	}
	public void setNcontent(String ncontent) {
		this.ncontent = ncontent;
	}
	
}
