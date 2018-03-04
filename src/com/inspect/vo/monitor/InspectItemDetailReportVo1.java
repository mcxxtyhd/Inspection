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

public class InspectItemDetailReportVo1{


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
	private int xlid;   //线路id
	private int xpid;  //巡检点id
	private int xgid;  //巡检班组
	private int xuid; //巡检员
	private int xequid; //设备ID
	private String xequtnum; //设备编号
	private String xterid; //巡检终端编号
	private String xequtype;//设备类型
	private String imark;//标志码
	private String retrains;//重传标志
	private String eregion;// 所属区域
	private String ecity;// 所属区县
	private  int  psize;//设备下巡检项总数
	
	public int getPsize() {
		return psize;
	}
	public void setPsize(int psize) {
		this.psize = psize;
	}
	public String getEregion() {
		return eregion;
	}
	public void setEregion(String eregion) {
		this.eregion = eregion;
	}
	public String getEcity() {
		return ecity;
	}
	public void setEcity(String ecity) {
		this.ecity = ecity;
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

	public int getXlid() {
		return xlid;
	}
	public void setXlid(int xlid) {
		this.xlid = xlid;
	}
	public int getXpid() {
		return xpid;
	}
	public void setXpid(int xpid) {
		this.xpid = xpid;
	}
	public int getXequid() {
		return xequid;
	}
	public void setXequid(int xequid) {
		this.xequid = xequid;
	}
	public int getXgid() {
		return xgid;
	}
	public void setXgid(int xgid) {
		this.xgid = xgid;
	}
	public int getXuid() {
		return xuid;
	}
	public void setXuid(int xuid) {
		this.xuid = xuid;
	}
	public String getXequtnum() {
		return xequtnum;
	}
	public void setXequtnum(String xequtnum) {
		this.xequtnum = xequtnum;
	}
	public String getXterid() {
		return xterid;
	}
	public void setXterid(String xterid) {
		this.xterid = xterid;
	}

	public String getXequtype() {
		return xequtype;
	}
	public void setXequtype(String xequtype) {
		this.xequtype = xequtype;
	}
	public String getImark() {
		return imark;
	}
	public void setImark(String imark) {
		this.imark = imark;
	}
	public String getRetrains() {
		return retrains;
	}
	public void setRetrains(String retrains) {
		this.retrains = retrains;
	}
   

}
