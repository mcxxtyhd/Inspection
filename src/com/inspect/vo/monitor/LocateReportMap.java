package com.inspect.vo.monitor;

import java.io.Serializable;

/**
 * 巡检人员监控地图上显示信息对象类
 * @author wzs
 * @version 1.0
 */
public class LocateReportMap implements Serializable {

	private static final long serialVersionUID = 10617309543091916L;
	
	private String rpUserNumber;	//巡检员编号
	private String rpUserName;		//巡检员名称
	private String rpUserMobile;	//巡检员手机号
	private String rpGroupName;		//所属班组
	private String rpTerminalNumber; //巡检终端编号
	private String rpTerminalName;	//巡检终端名称
	private String rpLineName;		//巡检线路名称
	private double rpPosX;			//上报经度;
	private double rpPosY;			//上报纬度
	private String rpRepoetTime;	//上报时间
	private String rpLastTimestamp; //最后上报时间
	private String rpUserId;           //巡检上报的主键id
	private  String rpUserId1;          //巡检员信息表的主键id
	
	public String getRpUserId1() {
		return rpUserId1;
	}
	public void setRpUserId1(String rpUserId1) {
		this.rpUserId1 = rpUserId1;
	}
	public String getRpUserId() {
		return rpUserId;
	}
	public void setRpUserId(String rpUserId) {
		this.rpUserId = rpUserId;
	}
	private String rpLastUpdateTime;
	
	public String getRpLastUpdateTime() {
		return rpLastUpdateTime;
	}
	public void setRpLastUpdateTime(String rpLastUpdateTime) {
		this.rpLastUpdateTime = rpLastUpdateTime;
	}
	public String getRpUserNumber() {
		return rpUserNumber;
	}
	public void setRpUserNumber(String rpUserNumber) {
		this.rpUserNumber = rpUserNumber;
	}
	public String getRpUserName() {
		return rpUserName;
	}
	public void setRpUserName(String rpUserName) {
		this.rpUserName = rpUserName;
	}
	public String getRpUserMobile() {
		return rpUserMobile;
	}
	public void setRpUserMobile(String rpUserMobile) {
		this.rpUserMobile = rpUserMobile;
	}
	public String getRpGroupName() {
		return rpGroupName;
	}
	public void setRpGroupName(String rpGroupName) {
		this.rpGroupName = rpGroupName;
	}
	public String getRpTerminalNumber() {
		return rpTerminalNumber;
	}
	public void setRpTerminalNumber(String rpTerminalNumber) {
		this.rpTerminalNumber = rpTerminalNumber;
	}
	public String getRpTerminalName() {
		return rpTerminalName;
	}
	public void setRpTerminalName(String rpTerminalName) {
		this.rpTerminalName = rpTerminalName;
	}
	public String getRpLineName() {
		return rpLineName;
	}
	public void setRpLineName(String rpLineName) {
		this.rpLineName = rpLineName;
	}
	public double getRpPosX() {
		return rpPosX;
	}
	public void setRpPosX(double rpPosX) {
		this.rpPosX = rpPosX;
	}
	public double getRpPosY() {
		return rpPosY;
	}
	public void setRpPosY(double rpPosY) {
		this.rpPosY = rpPosY;
	}
	public String getRpRepoetTime() {
		return rpRepoetTime;
	}
	public void setRpRepoetTime(String rpRepoetTime) {
		this.rpRepoetTime = rpRepoetTime;
	}
	public String getRpLastTimestamp() {
		return rpLastTimestamp;
	}
	public void setRpLastTimestamp(String rpLastTimestamp) {
		this.rpLastTimestamp = rpLastTimestamp;
	}
}
