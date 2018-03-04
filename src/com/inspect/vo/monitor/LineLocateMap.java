package com.inspect.vo.monitor;

import java.util.List;

/**
 * 巡检线路监控地图上显示信息对象类
 * @author wzs
 * @version 1.0
 */
public class LineLocateMap  {

	
	private String rpLineName;		//巡检线路名称
	private String rpLineNumber;    //巡检线路编号
	
	private String rpPointNumber;   //巡检点编号
	private String rpPointName; 	//巡检点名称
	private String rpPointId;	   //巡检点ID
	
	private String rpUserNumber;	//巡检员编号
	private String rpUserName;		//巡检员名称
	private String rpGroupName;		//所属班组
	
	private double rpPosX;			//巡检点经度
	private double rpPosY;			//巡检点纬度
	private String rpRepoetTime;	//上报时间
	private String rpLastTimestamp; //最后上报时间
	
	private List<String> rpEquipments; //巡检设备
	
	private List<String> rpLineIds;//线路Id
	
	private int rpWarningFlag;
	
	public int getRpWarningFlag() {
		return rpWarningFlag;
	}
	public void setRpWarningFlag(int rpWarningFlag) {
		this.rpWarningFlag = rpWarningFlag;
	}
	public List<String> getRpLineIds() {
		return rpLineIds;
	}
	public void setRpLineIds(List<String> rpLineIds) {
		this.rpLineIds = rpLineIds;
	}
	public List<String> getRpEquipments() {
		return rpEquipments;
	}
	public void setRpEquipments(List<String> rpEquipments) {
		this.rpEquipments = rpEquipments;
	}
	public String getRpLineName() {
		return rpLineName;
	}
	public void setRpLineName(String rpLineName) {
		this.rpLineName = rpLineName;
	}
	public String getRpLineNumber() {
		return rpLineNumber;
	}
	public void setRpLineNumber(String rpLineNumber) {
		this.rpLineNumber = rpLineNumber;
	}
	public String getRpPointNumber() {
		return rpPointNumber;
	}
	public void setRpPointNumber(String rpPointNumber) {
		this.rpPointNumber = rpPointNumber;
	}
	public String getRpPointName() {
		return rpPointName;
	}
	public void setRpPointName(String rpPointName) {
		this.rpPointName = rpPointName;
	}
	public String getRpPointId() {
		return rpPointId;
	}
	public void setRpPointId(String rpPointId) {
		this.rpPointId = rpPointId;
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
	public String getRpGroupName() {
		return rpGroupName;
	}
	public void setRpGroupName(String rpGroupName) {
		this.rpGroupName = rpGroupName;
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
