package com.inspect.vo.summary;

import java.io.File;
import java.io.Serializable;

import com.inspect.vo.comon.BaseVo;

public class SummaryFormVo extends BaseVo implements Serializable {

	private static final long serialVersionUID = -8751547938824266344L;

	private String bfixcycle;// 维修周期

	private String bnumber; // 基站编号
	private String bname; // 基站名称
	private String bregion;// 所属区域
	private String bcity;// 所属区县
	private String baddress;// 基站位置
	private double bposx; // 经度
	private double bposy; // 纬度
	private String btype; // 类型
	private String bfactory; // 集成厂家
	private String blevel; // 基站维护等级
	private String beqcount; // 有源设备数量
	private String btower; // 自建（共享）塔
	private String bdesc; // 描述
	private String rpedate;// 结束时间
	private String rpsdate;// 开始时间
	private String xdesc; // 描述
	private String pname; // 计划名称

	private int xeid;
	private String xename;
	private String xenumber;
	private int xequid; // 设备ID
	private String xequname;
	private String xequtnum; // 设备编号
	private int xgid;
	private String xgname;
	private int xlid; // 巡检线路
	private String xlname;
	private String xmaxvalue;// 最大值
	private int xmid;
	private String xminvalue; // 最小值
	private String xpid;
	private int xproid; // 巡检项Id
	private String xproname; // 巡检项名称
	private String xpvalue;// 默认值
	private String xreptime; // 上报时间
	private String xstatus; // 状态
	private String xstatusname;

	private String xterid; // 巡检终端编号
	private int xuid; // 巡检员
	private String xuname;
	private String ximgname; // 图评名称2
	private String ximgurl; // 图评路径2
	private int itaskid; // 任务id
	private String xvalue;// 默认值1
	private String procycle;// 巡检周期

	private String sname;
	private String scell;
	private int flag;
	private  File   excelFile;
	private String excelFileFileName;
	
	private  String   excelFileContentType;
	
	private String eregion;// 所属区域
	private String ecity;// 所属区县
	
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

	public File getExcelFile() {
		return excelFile;
	}

	public void setExcelFile(File excelFile) {
		this.excelFile = excelFile;
	}

	public String getExcelFileFileName() {
		return excelFileFileName;
	}

	public void setExcelFileFileName(String excelFileFileName) {
		this.excelFileFileName = excelFileFileName;
	}

	public String getExcelFileContentType() {
		return excelFileContentType;
	}

	public void setExcelFileContentType(String excelFileContentType) {
		this.excelFileContentType = excelFileContentType;
	}

	
	public String getBcity() {
		return bcity;
	}

	public void setBcity(String bcity) {
		this.bcity = bcity;
	}

	public String getSname() {
		return sname;
	}

	public void setSname(String sname) {
		this.sname = sname;
	}

	public String getScell() {
		return scell;
	}

	public void setScell(String scell) {
		this.scell = scell;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public String getPname() {
		return pname;
	}

	public void setPname(String pname) {
		this.pname = pname;
	}

	public String getProcycle() {
		return procycle;
	}

	public void setProcycle(String procycle) {
		this.procycle = procycle;
	}

	public String getBfixcycle() {
		return bfixcycle;
	}

	public void setBfixcycle(String bfixcycle) {
		this.bfixcycle = bfixcycle;
	}

	public String getBnumber() {
		return bnumber;
	}

	public void setBnumber(String bnumber) {
		this.bnumber = bnumber;
	}

	public String getBname() {
		return bname;
	}

	public void setBname(String bname) {
		this.bname = bname;
	}

	public String getBregion() {
		return bregion;
	}

	public void setBregion(String bregion) {
		this.bregion = bregion;
	}

	public String getBaddress() {
		return baddress;
	}

	public void setBaddress(String baddress) {
		this.baddress = baddress;
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

	public String getBtype() {
		return btype;
	}

	public void setBtype(String btype) {
		this.btype = btype;
	}

	public String getBfactory() {
		return bfactory;
	}

	public void setBfactory(String bfactory) {
		this.bfactory = bfactory;
	}

	public String getBlevel() {
		return blevel;
	}

	public void setBlevel(String blevel) {
		this.blevel = blevel;
	}

	public String getBeqcount() {
		return beqcount;
	}

	public void setBeqcount(String beqcount) {
		this.beqcount = beqcount;
	}

	public String getBtower() {
		return btower;
	}

	public void setBtower(String btower) {
		this.btower = btower;
	}

	public String getBdesc() {
		return bdesc;
	}

	public void setBdesc(String bdesc) {
		this.bdesc = bdesc;
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

	public int getXequid() {
		return xequid;
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

	public String getXpid() {
		return xpid;
	}

	public int getXproid() {
		return xproid;
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

	public void setXequid(int xequid) {
		this.xequid = xequid;
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

	public void setXpid(String xpid) {
		this.xpid = xpid;
	}

	public void setXproid(int xproid) {
		this.xproid = xproid;
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
