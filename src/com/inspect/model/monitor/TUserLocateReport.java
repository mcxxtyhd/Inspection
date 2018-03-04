package com.inspect.model.monitor;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 巡检人员实时上报位置信息实体类
 * @author wzs
 */
@Entity
@Table(name="t_user_locate_report")
public class TUserLocateReport implements Serializable {

	private static final long serialVersionUID = 6684776025972993006L;
	
    protected Long id; //主键
	protected Integer entid; //单位ID
	private int rpuserid;      //巡检员ID
	private String rpterminalid;  //巡检终端ID    
	private double rpposx;	  //上报经度
	private double rpposy;	  //上报纬度
	private String rptime;	 //上报时间
    private int rplineid;    //巡检线路ID
    private int rpgroupid;	 //所属班组ID
	public TUserLocateReport() {
	}
	public TUserLocateReport(int rpuserid, String rpterminalid, double rpposx,
			double rpposy) {
		this.rpuserid = rpuserid;
		this.rpterminalid = rpterminalid;
		this.rpposx = rpposx;
		this.rpposy = rpposy;
	}
	public TUserLocateReport(int rpuserid, String rpterminalid, double rpposx,
			double rpposy, String rptime, int rplineid, int rpgroupid) {
		this.rpuserid = rpuserid;
		this.rpterminalid = rpterminalid;
		this.rpposx = rpposx;
		this.rpposy = rpposy;
		this.rptime = rptime;
		this.rplineid = rplineid;
		this.rpgroupid = rpgroupid;
	}
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getEntid() {
		return entid;
	}
	
	public void setEntid(Integer entid) {
		this.entid = entid;
	}
	public int getRpuserid() {
		return rpuserid;
	}
	public void setRpuserid(int rpuserid) {
		this.rpuserid = rpuserid;
	}
	public String getRpterminalid() {
		return rpterminalid;
	}
	public void setRpterminalid(String rpterminalid) {
		this.rpterminalid = rpterminalid;
	}
	public double getRpposx() {
		return rpposx;
	}
	public void setRpposx(double rpposx) {
		this.rpposx = rpposx;
	}
	public double getRpposy() {
		return rpposy;
	}
	public void setRpposy(double rpposy) {
		this.rpposy = rpposy;
	}
	public String getRptime() {
		return rptime;
	}
	public void setRptime(String rptime) {
		this.rptime = rptime;
	}
	public int getRplineid() {
		return rplineid;
	}
	public void setRplineid(int rplineid) {
		this.rplineid = rplineid;
	}
	public int getRpgroupid() {
		return rpgroupid;
	}
	public void setRpgroupid(int rpgroupid) {
		this.rpgroupid = rpgroupid;
	}
}
