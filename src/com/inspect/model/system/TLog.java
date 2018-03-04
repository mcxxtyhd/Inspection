package com.inspect.model.system;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.inspect.model.BaseModel;
/**
 * 系统操作日志实体类对象
 * @author wzs
 * @version 1.0
 */
@Entity
@Table(name="t_log")
public class TLog extends BaseModel implements Serializable {

	private static final long serialVersionUID = -4975720711233062999L;
	
	private String opuser;  //操作人
	private String opevent;	 //操作事件
	private Date optime; //操作时间
	private Integer opstatus;   //操作状态 0：成功 1：失败
	private String optable; //操作表名
	public TLog() {
		super();
	}
	public String getOpuser() {
		return opuser;
	}
	public void setOpuser(String opuser) {
		this.opuser = opuser;
	}
	public String getOpevent() {
		return opevent;
	}
	public void setOpevent(String opevent) {
		this.opevent = opevent;
	}
	public Date getOptime() {
		return optime;
	}
	public void setOptime(Date optime) {
		this.optime = optime;
	}
	public Integer getOpstatus() {
		return opstatus;
	}
	public void setOpstatus(Integer opstatus) {
		this.opstatus = opstatus;
	}
	public String getOptable() {
		return optable;
	}
	public void setOptable(String optable) {
		this.optable = optable;
	}
}
