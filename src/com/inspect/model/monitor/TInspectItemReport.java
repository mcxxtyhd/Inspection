package com.inspect.model.monitor;

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
import javax.persistence.Transient;

import com.inspect.model.BaseModel;

/**
 * 巡检信息实时上报实体类
 * @author wzs
 */
@Entity
@Table(name = "t_report_message")
public class TInspectItemReport extends BaseModel implements Serializable {
	private static final long serialVersionUID = 7028075888838051326L;

	private int xtaskid; //任务id
	private int xlid;   //线路id
	private int xpid;  //巡检点id
	private int xequid; //baseinfo表中的ID
	private int xgid;  //巡检班组
	private int xuid; //巡检员
	private String xequtnum; //设备编号
	private String xterid; //巡检终端编号
	private String xreptime; //上报时间
	private String xstatus;  //状态0表示正常 1表示异常，即审核不合格，需要重新巡检
	private String ximgname; //图评名称
	private String ximgurl;  //图评路径
	private String xequtype;//设备类型
	private String eregion;// 所属区域
	private String ecity;// 所属区县
	private int lastmonthflag;//所有巡检项是否巡检完成  0未完成  1完成
	private List<TInspectItemDetailReport> inspectreportdetailmsgs=new ArrayList<TInspectItemDetailReport>();//巡检信息详细集合
	private List<TInspectItemRaltionReport> inspectmsgdetailreports=new ArrayList<TInspectItemRaltionReport>();
	
	//和baseinf的关联数据：
	@Transient
	public String btype;   //基础设备类型
	@Transient
	public String bname;   // 名称
	@Transient
	public String bcity; //地市
	@Transient
	public String bregion;//区县
	@Transient
	public String baddress;//地址
	@Transient
	public double bposx;     //经度
	@Transient
	public double bposy;    //纬度
	@Transient
	public String btowertype;//铁塔类型
	@Transient
	public String btower;  //自建（共享）塔
	@Transient
	public String bfactory; // 集成厂家 
	@Transient
	public String blevel; //基站维护等级
	@Transient
	public String beqcount; // 有源设备数量
	@Transient
	public String bwlnumber;//物理编号
	@Transient
	public String bdesc;  // 描述
	@Transient
	public double bheight;//塔高
	@Transient
	public String entname; //公司名称
	@Transient
	public String iuname; //巡检人名称
	
	public TInspectItemReport() {
		super();
	}
	public TInspectItemReport(int xtaskid, int xequid, String xequtnum, String xterid) {
		super();
		this.xtaskid = xtaskid;
		this.xequid = xequid;
		this.xequtnum = xequtnum;
		this.xterid = xterid;
	}
	public TInspectItemReport(int xtaskid, int xlid, int xpid, int xequid, int xgid, int xuid, String xequtnum, String xterid, String xreptime, String xstatus, String ximgname, String ximgurl, List<TInspectItemDetailReport> inspectreportdetailmsgs, List<TInspectItemRaltionReport> inspectmsgdetailreports) {
		super();
		this.xtaskid = xtaskid;
		this.xlid = xlid;
		this.xpid = xpid;
		this.xequid = xequid;
		this.xgid = xgid;
		this.xuid = xuid;
		this.xequtnum = xequtnum;
		this.xterid = xterid;
		this.xreptime = xreptime;
		this.xstatus = xstatus;
		this.ximgname = ximgname;
		this.ximgurl = ximgurl;
		this.inspectreportdetailmsgs = inspectreportdetailmsgs;
		this.inspectmsgdetailreports = inspectmsgdetailreports;
	}
	
	public int getLastmonthflag() {
		return lastmonthflag;
	}
	public void setLastmonthflag(int lastmonthflag) {
		this.lastmonthflag = lastmonthflag;
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
	@Column(length=50)
	public String getXequtnum() {
		return xequtnum;
	}
	public void setXequtnum(String xequtnum) {
		this.xequtnum = xequtnum;
	}
	@Column(length=50)
	public String getXterid() {
		return xterid;
	}
	public void setXterid(String xterid) {
		this.xterid = xterid;
	}
	@Column(length=40)
	public String getXreptime() {
		return xreptime;
	}
	public void setXreptime(String xreptime) {
		this.xreptime = xreptime;
	}
	@Column(length=40)
	public String getXstatus() {
		return xstatus;
	}
	public void setXstatus(String xstatus) {
		this.xstatus = xstatus;
	}
	@Column(length=40)
	public String getXequtype() {
		return xequtype;
	}
	public void setXequtype(String xequtype) {
		this.xequtype = xequtype;
	}
	@Column(length=100)
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
	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY,mappedBy="tinspectitemreport")
	public List<TInspectItemDetailReport> getInspectreportdetailmsgs() {
		return inspectreportdetailmsgs;
	}
	public void setInspectreportdetailmsgs(List<TInspectItemDetailReport> inspectreportdetailmsgs) {
		this.inspectreportdetailmsgs = inspectreportdetailmsgs;
	}
	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY,mappedBy="tinspectitemreport")
	public List<TInspectItemRaltionReport> getInspectmsgdetailreports() {
		return inspectmsgdetailreports;
	}
	public void setInspectmsgdetailreports(List<TInspectItemRaltionReport> inspectmsgdetailreports) {
		this.inspectmsgdetailreports = inspectmsgdetailreports;
	}
	
}
