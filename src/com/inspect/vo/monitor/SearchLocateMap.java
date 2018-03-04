package com.inspect.vo.monitor;

public class SearchLocateMap {
	
	private String lineName; //巡检线路
	private String groupName;  //巡检班组
	private String iuserName; //巡检人员
	private String startDate;  //开始时间
	private String endDate;    //巡检结束时间
	private String status;     //状态
	private int entId;
	private String rpEquipName; //设备名称
	private String rpEquipCity;		//巡检设备城市
	private String rpEquipRegion;		//巡检设备区县
	private String  rpEquipAddress;  //设备地址
	private String terminatenumber;//终端编号
	private  String btype;//设备类型（铁塔室内）
	
	public String getBtype() {
		return btype;
	}
	public void setBtype(String btype) {
		this.btype = btype;
	}
	public String getRpEquipName() {
		return rpEquipName;
	}
	public void setRpEquipName(String rpEquipName) {
		this.rpEquipName = rpEquipName;
	}
	public String getRpEquipCity() {
		return rpEquipCity;
	}
	public void setRpEquipCity(String rpEquipCity) {
		this.rpEquipCity = rpEquipCity;
	}
	public String getRpEquipRegion() {
		return rpEquipRegion;
	}
	public void setRpEquipRegion(String rpEquipRegion) {
		this.rpEquipRegion = rpEquipRegion;
	}
	public String getRpEquipAddress() {
		return rpEquipAddress;
	}
	public void setRpEquipAddress(String rpEquipAddress) {
		this.rpEquipAddress = rpEquipAddress;
	}
	public String getTerminatenumber() {
		return terminatenumber;
	}
	public void setTerminatenumber(String terminatenumber) {
		this.terminatenumber = terminatenumber;
	}
	public int getEntId() {
		return entId;
	}
	public void setEntId(int entId) {
		this.entId = entId;
	}
	public String getLineName() {
		return lineName;
	}
	public void setLineName(String lineName) {
		this.lineName = lineName;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getIuserName() {
		return iuserName;
	}
	public void setIuserName(String iuserName) {
		this.iuserName = iuserName;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	

}
