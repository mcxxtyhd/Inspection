package com.inspect.model.problem;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.inspect.model.BaseModel;

/**
 * 抢修任务信息实体类
 */
@Entity
@Table(name = "t_repair_task")
public class TRepairTask extends BaseModel implements Serializable {

	private static final long serialVersionUID = -8251634746647846232L;
	
	private String rcontent;// 抢修任务内容
	private int rgid;     //   维护队
	private int ruid;     //   维护队人员
	private int rflag;    //   抢修任务状态 1：未处理   2：已下发 3：已处理
	private String rsenddate; // 任务下发时间
	private String rrepdate;//  任务抢修上报时间
	private String rternumber;//终端编号
	private String rdesc;// 抢修任务备注
	private String rcategory;//将修任务类别 有两种：抢修和优化
	private String rendtime; //将修时限，即在此日期之前完成
	
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
	public TRepairTask() {
		super();
	}
	public TRepairTask(String rcontent, int rgid, int ruid, int rflag, String rsenddate, String rrepdate, String rternumber, String rdesc) {
		super();
		this.rcontent = rcontent;
		this.rgid = rgid;
		this.ruid = ruid;
		this.rflag = rflag;
		this.rsenddate = rsenddate;
		this.rrepdate = rrepdate;
		this.rternumber = rternumber;
		this.rdesc = rdesc;
	}
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
	@Column(length=40)
	public String getRsenddate() {
		return rsenddate;
	}
	public void setRsenddate(String rsenddate) {
		this.rsenddate = rsenddate;
	}
	@Column(length=40)
	public String getRrepdate() {
		return rrepdate;
	}
	public void setRrepdate(String rrepdate) {
		this.rrepdate = rrepdate;
	}
	@Column(length=40)
	public String getRternumber() {
		return rternumber;
	}
	public void setRternumber(String rternumber) {
		this.rternumber = rternumber;
	}
	@Column(length=100)
	public String getRdesc() {
		return rdesc;
	}
	public void setRdesc(String rdesc) {
		this.rdesc = rdesc;
	}
}
