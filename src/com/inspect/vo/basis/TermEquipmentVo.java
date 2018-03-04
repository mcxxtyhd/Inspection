package com.inspect.vo.basis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.inspect.vo.comon.BaseVo;

public class TermEquipmentVo  {
	private int id;//设备baseinfo表主键id
	private int eid;//equipment主键
	private String enumber; // 设备编号
	private String ename; // 设备名称
	private String etype; // 类型
	private String efactory;// 厂商
	private String eaddress; // 设备地址
	private Double eposx; // 经度
	private Double eposy; // 纬度
	private String ecity; //城市
	private String eregion;//区县
	private String xstatus;  //状态0表示正常 1表示异常，即审核不合格，需要重新巡检
	private boolean isInspectStatus;//此设备是否已经被巡检false表示未巡检，true表示已巡检
	private String tpgroupids;//巡检项组id集合（通过逗号“，”分割巡检项组id）
	
	
	
	public int getEid() {
		return eid;
	}
	public void setEid(int eid) {
		this.eid = eid;
	}
	public String getEcity() {
		return ecity;
	}
	public void setEcity(String ecity) {
		this.ecity = ecity;
	}
	public String getEregion() {
		return eregion;
	}
	public void setEregion(String eregion) {
		this.eregion = eregion;
	}
	public String getXstatus() {
		return xstatus;
	}
	public void setXstatus(String xstatus) {
		this.xstatus = xstatus;
	}
	public boolean isInspectStatus() {
		return isInspectStatus;
	}
	public void setInspectStatus(boolean isInspectStatus) {
		this.isInspectStatus = isInspectStatus;
	}
	public String getTpgroupids() {
		return tpgroupids;
	}
	public void setTpgroupids(String tpgroupids) {
		this.tpgroupids = tpgroupids;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getEnumber() {
		return enumber;
	}
	public void setEnumber(String enumber) {
		this.enumber = enumber;
	}
	public String getEname() {
		return ename;
	}
	public void setEname(String ename) {
		this.ename = ename;
	}
	public String getEtype() {
		return etype;
	}
	public void setEtype(String etype) {
		this.etype = etype;
	}
	public String getEfactory() {
		return efactory;
	}
	public void setEfactory(String efactory) {
		this.efactory = efactory;
	}
	public String getEaddress() {
		return eaddress;
	}
	public void setEaddress(String eaddress) {
		this.eaddress = eaddress;
	}
	public Double getEposx() {
		return eposx;
	}
	public void setEposx(Double eposx) {
		this.eposx = eposx;
	}
	public Double getEposy() {
		return eposy;
	}
	public void setEposy(Double eposy) {
		this.eposy = eposy;
	}




}
