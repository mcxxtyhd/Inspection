package com.inspect.vo.monitor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.bcel.generic.NEW;

import com.inspect.model.BaseModel;
import com.inspect.model.monitor.TInspectItemDetailReport;
import com.inspect.model.monitor.TInspectItemRaltionReport;

/**
 * 巡检信息实时上报实体类
 * @author wzs
 */

public class TermInspectItemReportVo {

	protected Integer id; //主键
	protected Integer entid; //单位ID
	private int xtaskid; //任务id
	private int xlid;   //线路id
	private int xpid;  //巡检点id
	private int xequid; //设备ID
	private int xgid;  //巡检班组
	private int xuid; //巡检员
	private String xequtnum; //设备编号
	private String xterid; //巡检终端编号
	private String xreptime; //上报时间
	private String xstatus;  //状态
	private String ximgname; //图评名称
	private String ximgurl;  //图评路径
	private String xequtype;//设备类型
	private String imark;//标志码
	private String retrains;//重传标志
	private Set<TInspectItemDetailReport> inspectreportdetailmsgs=new HashSet<TInspectItemDetailReport>();
	


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
	public Set<TInspectItemDetailReport> getInspectreportdetailmsgs() {
		return inspectreportdetailmsgs;
	}
	public void setInspectreportdetailmsgs(
			Set<TInspectItemDetailReport> inspectreportdetailmsgs) {
		this.inspectreportdetailmsgs = inspectreportdetailmsgs;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getEntid() {
		return entid;
	}
	public void setEntid(Integer entid) {
		this.entid = entid;
	}
	public int getXtaskid() {
		return xtaskid;
	}
	public void setXtaskid(int xtaskid) {
		this.xtaskid = xtaskid;
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
	public String getXreptime() {
		return xreptime;
	}
	public void setXreptime(String xreptime) {
		this.xreptime = xreptime;
	}
	public String getXstatus() {
		return xstatus;
	}
	public void setXstatus(String xstatus) {
		this.xstatus = xstatus;
	}
	public String getXequtype() {
		return xequtype;
	}
	public void setXequtype(String xequtype) {
		this.xequtype = xequtype;
	}
	public String getXimgname() {
		return ximgname;
	}
	public void setXimgname(String ximgname) {
		this.ximgname = ximgname;
	}
	public String getXimgurl() {
		return ximgurl;
	}
	public void setXimgurl(String ximgurl) {
		this.ximgurl = ximgurl;
	}

}
