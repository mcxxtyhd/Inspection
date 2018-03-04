package com.inspect.vo.monitor;

import java.io.Serializable;

import com.inspect.vo.comon.BaseVo;

public class InspectItemReportVo extends BaseVo implements Serializable {

	private static final long serialVersionUID = -8751547938824266344L;
	
	private int itaskid;   //任务id
	private int xlid; // 巡检线路id
	private int xpid; //点ID
	private Integer xequid; // 设备ID
	private int xgid;  //代维商Id
	private int xuid; // 巡检人ID
	private String xterid; // 巡检终端编号
	
	private String xtaskname;
	private String xlname;
	private String xequname;//设备名称
	private String xequtnum; // 设备编号
	private String xgname;
	private String xuname;
	private String pstartdate; //计划开始日期
	private String penddate;   //计划结束日期
	
	private Integer xproid; // 巡检项Id
	private String xproname; // 巡检项名称
	private String xptype; //类型
	private String xpenumvalue;//关联枚举值
	private int xpgid;//大类ID
	private String xpgname;//大类名称
	
	private int xeid;
	private String xename;
	private String xenumber;
	private String xmaxvalue;// 最大值
	private String xminvalue; // 最小值
	private String xpvalue;//默认值
	private int xmid; //巡检基础信息ID
	private String xreptime; // 上报时间
	private String xstatus; // 状态
	private String xstatusname;
	private String ximgname; //图评名称
    private String ximgurl;  //图评路径
	private String xvalue;// 巡检值
	private String xequtype;//设备类型
	private String xdesc; // 描述
	private String rpedate;//结束时间
	private String rpsdate;//开始时间
	
	private String xtype;
	private boolean flag;//是否异常，即图片上传数量是否满足平台所给数量或者是否存在异常问题上报;true表示异常，false表示正常
	
	private String bcity; //地市
	private String bregion;//区县
	private double bposx;     //经度
	private double bposy;    //纬度


	public String getBcity() {
		return bcity;
	}

	public void setBcity(String bcity) {
		this.bcity = bcity;
	}

	public String getBregion() {
		return bregion;
	}

	public void setBregion(String bregion) {
		this.bregion = bregion;
	}

	public double getBposx() {
		return bposx;
	}

	public void setBposx(double bposx) {
		this.bposx = bposx;
	}

	public double getBposy() {
		return bposy;
	}

	public void setBposy(double bposy) {
		this.bposy = bposy;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public String getXtype() {
		return xtype;
	}

	public void setXtype(String xtype) {
		this.xtype = xtype;
	}

	public Integer getXequid() {
		return xequid;
	}

	public void setXequid(Integer xequid) {
		this.xequid = xequid;
	}

	public Integer getXproid() {
		return xproid;
	}

	public void setXproid(Integer xproid) {
		this.xproid = xproid;
	}
	
	private String qyerytype;
	public String getQyerytype() {
		return qyerytype;
	}

	public void setQyerytype(String qyerytype) {
		this.qyerytype = qyerytype;
	}
	public String getXtaskname() {
		return xtaskname;
	}

	public void setXtaskname(String xtaskname) {
		this.xtaskname = xtaskname;
	}

	public String getPstartdate() {
		return pstartdate;
	}

	public void setPstartdate(String pstartdate) {
		this.pstartdate = pstartdate;
	}

	public String getPenddate() {
		return penddate;
	}

	public void setPenddate(String penddate) {
		this.penddate = penddate;
	}

	public int getXpid() {
		return xpid;
	}

	public void setXpid(int xpid) {
		this.xpid = xpid;
	}

	public String getXptype() {
		return xptype;
	}

	public void setXptype(String xptype) {
		this.xptype = xptype;
	}

	public String getXpenumvalue() {
		return xpenumvalue;
	}

	public void setXpenumvalue(String xpenumvalue) {
		this.xpenumvalue = xpenumvalue;
	}

	public int getXpgid() {
		return xpgid;
	}

	public void setXpgid(int xpgid) {
		this.xpgid = xpgid;
	}

	public String getXpgname() {
		return xpgname;
	}

	public void setXpgname(String xpgname) {
		this.xpgname = xpgname;
	}

	
	public String getXequtype() {
		return xequtype;
	}

	public void setXequtype(String xequtype) {
		this.xequtype = xequtype;
	}

	public int getItaskid() {
		return itaskid;
	}

	public void setItaskid(int itaskid) {
		this.itaskid = itaskid;
	}

	public String getXvalue() {
		return xvalue;
	}

	public void setXvalue(String xvalue) {
		this.xvalue = xvalue;
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

	public String getRpedate() {
		return rpedate;
	}
	
	public String getRpsdate() {
		return rpsdate;
	}
	
	public String getXdesc() {
		return xdesc;
	}

	public int getXeid() {
		return xeid;
	}

	public String getXename() {
		return xename;
	}

	public String getXenumber() {
		return xenumber;
	}

	public String getXequname() {
		return xequname;
	}

	public String getXequtnum() {
		return xequtnum;
	}

	public int getXgid() {
		return xgid;
	}

	public String getXgname() {
		return xgname;
	}

	public int getXlid() {
		return xlid;
	}

	public String getXlname() {
		return xlname;
	}

	public String getXmaxvalue() {
		return xmaxvalue;
	}

	public int getXmid() {
		return xmid;
	}

	public String getXminvalue() {
		return xminvalue;
	}


	public String getXproname() {
		return xproname;
	}

	public String getXpvalue() {
		return xpvalue;
	}

	public String getXreptime() {
		return xreptime;
	}

	public String getXstatus() {
		return xstatus;
	}

	public String getXstatusname() {
		return xstatusname;
	}

	public String getXterid() {
		return xterid;
	}

	public int getXuid() {
		return xuid;
	}

	public String getXuname() {
		return xuname;
	}

	public void setRpedate(String rpedate) {
		this.rpedate = rpedate;
	}

	public void setRpsdate(String rpsdate) {
		this.rpsdate = rpsdate;
	}

	public void setXdesc(String xdesc) {
		this.xdesc = xdesc;
	}

	public void setXeid(int xeid) {
		this.xeid = xeid;
	}

	public void setXename(String xename) {
		this.xename = xename;
	}

	public void setXenumber(String xenumber) {
		this.xenumber = xenumber;
	}

	public void setXequname(String xequname) {
		this.xequname = xequname;
	}

	public void setXequtnum(String xequtnum) {
		this.xequtnum = xequtnum;
	}

	public void setXgid(int xgid) {
		this.xgid = xgid;
	}

	public void setXgname(String xgname) {
		this.xgname = xgname;
	}

	public void setXlid(int xlid) {
		this.xlid = xlid;
	}

	public void setXlname(String xlname) {
		this.xlname = xlname;
	}

	public void setXmaxvalue(String xmaxvalue) {
		this.xmaxvalue = xmaxvalue;
	}

	public void setXmid(int xmid) {
		this.xmid = xmid;
	}

	public void setXminvalue(String xminvalue) {
		this.xminvalue = xminvalue;
	}
	public void setXproname(String xproname) {
		this.xproname = xproname;
	}

	public void setXpvalue(String xpvalue) {
		this.xpvalue = xpvalue;
	}

	public void setXreptime(String xreptime) {
		this.xreptime = xreptime;
	}

	public void setXstatus(String xstatus) {
		this.xstatus = xstatus;
	}
	
	public void setXstatusname(String xstatusname) {
		this.xstatusname = xstatusname;
	}

	public void setXterid(String xterid) {
		this.xterid = xterid;
	}

	public void setXuid(int xuid) {
		this.xuid = xuid;
	}

	public void setXuname(String xuname) {
		this.xuname = xuname;
	}

}
