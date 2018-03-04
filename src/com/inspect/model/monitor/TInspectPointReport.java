package com.inspect.model.monitor;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.inspect.model.BaseModel;

/**
 * 巡检点信息上报实体类
 * @author wzs
 */
@Entity
@Table(name = "t_report_point_message")
public class TInspectPointReport extends BaseModel implements Serializable {

	private static final long serialVersionUID = 7819827994130816225L;
	
	private int xlid; //巡检线路ID1
	private int xpid; //巡检点ID1
	private String xpnum; //巡检点编号
	private int xuid;	  //巡检员ＩＤ1
	private String xternum;//巡检终端编号1
	private double xposx;//上报经度
	private double xposy;//上报纬度
	private int flag;//巡检标识　０：已检　１：未巡检 
	private String xreptime; //巡检时间  
	private String ximage; 
	public TInspectPointReport() {
	}
	public TInspectPointReport(int xlid, int xpid, String xpnum, int xuid,
			String xternum, double xposx, double xposy, int flag,
			String xreptime, String ximage) {
		this.xlid = xlid;
		this.xpid = xpid;
		this.xpnum = xpnum;
		this.xuid = xuid;
		this.xternum = xternum;
		this.xposx = xposx;
		this.xposy = xposy;
		this.flag = flag;
		this.xreptime = xreptime;
		this.ximage = ximage;
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
	public String getXpnum() {
		return xpnum;
	}
	public void setXpnum(String xpnum) {
		this.xpnum = xpnum;
	}
	public int getXuid() {
		return xuid;
	}
	public void setXuid(int xuid) {
		this.xuid = xuid;
	}
	public String getXternum() {
		return xternum;
	}
	public void setXternum(String xternum) {
		this.xternum = xternum;
	}
	public double getXposx() {
		return xposx;
	}
	public void setXposx(double xposx) {
		this.xposx = xposx;
	}
	public double getXposy() {
		return xposy;
	}
	public void setXposy(double xposy) {
		this.xposy = xposy;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public String getXreptime() {
		return xreptime;
	}
	public void setXreptime(String xreptime) {
		this.xreptime = xreptime;
	}
	public String getXimage() {
		return ximage;
	}
	public void setXimage(String ximage) {
		this.ximage = ximage;
	}

}
