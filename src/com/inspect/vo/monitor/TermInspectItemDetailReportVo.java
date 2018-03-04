package com.inspect.vo.monitor;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.inspect.model.monitor.TInspectItemRaltionReport;
import com.inspect.model.monitor.TInspectItemReport;
import com.inspect.vo.comon.BaseVo;

public class TermInspectItemDetailReportVo{


	private int id;
	private int entid; // 企业ID
	private int xtaskid; //任务id
	private int xproid; // 巡检项Id1
	private int xprogid;//巡检大类ID
	private String xproname; // 巡检项名称
	private String xtype;// 巡检类型
	private String xmaxvalue;// 最大值
	private String xminvalue; // 最小值
	private String penumvalue;//关联枚举值
	private String xvalue;// 结果值
	private String xstatus; // 状态
	private String xreptime; // 上报时间
	private String xdesc; // 描述
    private TermInspectItemReportVo termInspectItemReportVo;//巡检设备信息对象
    
	public TermInspectItemReportVo getTermInspectItemReportVo() {
		return termInspectItemReportVo;
	}
	public void setTermInspectItemReportVo(
			TermInspectItemReportVo termInspectItemReportVo) {
		this.termInspectItemReportVo = termInspectItemReportVo;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getEntid() {
		return entid;
	}
	public void setEntid(int entid) {
		this.entid = entid;
	}
	public int getXtaskid() {
		return xtaskid;
	}
	public void setXtaskid(int xtaskid) {
		this.xtaskid = xtaskid;
	}
	public int getXproid() {
		return xproid;
	}
	public void setXproid(int xproid) {
		this.xproid = xproid;
	}
	public int getXprogid() {
		return xprogid;
	}
	public void setXprogid(int xprogid) {
		this.xprogid = xprogid;
	}
	public String getXproname() {
		return xproname;
	}
	public void setXproname(String xproname) {
		this.xproname = xproname;
	}
	public String getXtype() {
		return xtype;
	}
	public void setXtype(String xtype) {
		this.xtype = xtype;
	}
	public String getXmaxvalue() {
		return xmaxvalue;
	}
	public void setXmaxvalue(String xmaxvalue) {
		this.xmaxvalue = xmaxvalue;
	}
	public String getXminvalue() {
		return xminvalue;
	}
	public void setXminvalue(String xminvalue) {
		this.xminvalue = xminvalue;
	}
	public String getPenumvalue() {
		return penumvalue;
	}
	public void setPenumvalue(String penumvalue) {
		this.penumvalue = penumvalue;
	}
	public String getXvalue() {
		return xvalue;
	}
	public void setXvalue(String xvalue) {
		this.xvalue = xvalue;
	}
	public String getXstatus() {
		return xstatus;
	}
	public void setXstatus(String xstatus) {
		this.xstatus = xstatus;
	}
	public String getXreptime() {
		return xreptime;
	}
	public void setXreptime(String xreptime) {
		this.xreptime = xreptime;
	}
	public String getXdesc() {
		return xdesc;
	}
	public void setXdesc(String xdesc) {
		this.xdesc = xdesc;
	}
    
	

}
