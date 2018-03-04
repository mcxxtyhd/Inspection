package com.inspect.model.monitor;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inspect.model.BaseModel;

/**
 * 巡检信息实时上报详细实体类
 * @author wzs
 */
@Entity
@Table(name = "t_report_message_detail")
public class TInspectItemDetailReport extends BaseModel implements Serializable {

	private static final long serialVersionUID = 3492811656748850540L;

	private int xtaskid; //任务id
	private int xproid; // 巡检项Id1
	private int xprogid;//巡检大类ID
	private String xproname; // 巡检项名称
	private String xtype;// 巡检类型(枚举，布尔等等)
	private String xmaxvalue;// 最大值
	private String xminvalue; // 最小值
	private String penumvalue;//关联枚举值
	private String xvalue;// 结果值
	private String xstatus; // 状态
	private String xreptime; // 上报时间
	private String xdesc; // 描述
	private String ximgname; //图评名称
    private String ximgurl;  //图评路径
    
    private TInspectItemReport tinspectitemreport;//巡检设备信息对象
    
	private Set<TInspectItemRaltionReport> inspectmsgdetailreports=new HashSet<TInspectItemRaltionReport>();
	
	@Transient
	public int msgid; //message表的主键
	
	public TInspectItemDetailReport() {
		super();
	}

	public TInspectItemDetailReport(int xtaskid, int xproid, int xprogid) {
		super();
		this.xtaskid = xtaskid;
		this.xproid = xproid;
		this.xprogid = xprogid;
	}

	public TInspectItemDetailReport(int xtaskid, int xproid, int xprogid, String xproname, String xtype, String xmaxvalue, String xminvalue, String penumvalue, String xvalue, String xstatus, String xreptime, String xdesc, String ximgname, String ximgurl, TInspectItemReport tinspectitemreport, Set<TInspectItemRaltionReport> inspectmsgdetailreports) {
		super();
		this.xtaskid = xtaskid;
		this.xproid = xproid;
		this.xprogid = xprogid;
		this.xproname = xproname;
		this.xtype = xtype;
		this.xmaxvalue = xmaxvalue;
		this.xminvalue = xminvalue;
		this.penumvalue = penumvalue;
		this.xvalue = xvalue;
		this.xstatus = xstatus;
		this.xreptime = xreptime;
		this.xdesc = xdesc;
		this.ximgname = ximgname;
		this.ximgurl = ximgurl;
		this.tinspectitemreport = tinspectitemreport;
		this.inspectmsgdetailreports = inspectmsgdetailreports;
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
    @Column(length=100)
	public String getXproname() {
		return xproname;
	}

	public void setXproname(String xproname) {
		this.xproname = xproname;
	}

	@Column(length=40)
	public String getXtype() {
		return xtype;
	}

	public void setXtype(String xtype) {
		this.xtype = xtype;
	}

	@Column(length=40)
	public String getXmaxvalue() {
		return xmaxvalue;
	}

	public void setXmaxvalue(String xmaxvalue) {
		this.xmaxvalue = xmaxvalue;
	}
	@Column(length=40)
	public String getXminvalue() {
		return xminvalue;
	}

	public void setXminvalue(String xminvalue) {
		this.xminvalue = xminvalue;
	}
	@Column(length=50)
	public String getPenumvalue() {
		return penumvalue;
	}

	public void setPenumvalue(String penumvalue) {
		this.penumvalue = penumvalue;
	}
	@Column(length=100)
	public String getXvalue() {
		return xvalue;
	}

	public void setXvalue(String xvalue) {
		this.xvalue = xvalue;
	}
	@Column(length=40)
	public String getXstatus() {
		return xstatus;
	}

	public void setXstatus(String xstatus) {
		this.xstatus = xstatus;
	}
	@Column(length=40)
	public String getXreptime() {
		return xreptime;
	}

	public void setXreptime(String xreptime) {
		this.xreptime = xreptime;
	}
	@Column(length=100)
	public String getXdesc() {
		return xdesc;
	}

	public void setXdesc(String xdesc) {
		this.xdesc = xdesc;
	}
	@Column(length=50)
	public String getXimgname() {
		return ximgname;
	}

	public void setXimgname(String ximgname) {
		this.ximgname = ximgname;
	}
	@Column(length=100)
	public String getXimgurl() {
		return ximgurl;
	}

	public void setXimgurl(String ximgurl) {
		this.ximgurl = ximgurl;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="msgid",nullable=true)
	public TInspectItemReport getTinspectitemreport() {
		return tinspectitemreport;
	}

	public void setTinspectitemreport(TInspectItemReport tinspectitemreport) {
		this.tinspectitemreport = tinspectitemreport;
	}

	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY,mappedBy="tinspectitemdetailrport")
	public Set<TInspectItemRaltionReport> getInspectmsgdetailreports() {
		return inspectmsgdetailreports;
	}

	public void setInspectmsgdetailreports(Set<TInspectItemRaltionReport> inspectmsgdetailreports) {
		this.inspectmsgdetailreports = inspectmsgdetailreports;
	}
}
