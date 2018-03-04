package com.inspect.vo.monitor;

import java.util.List;

/**
 * 巡检线路监控地图上显示信息对象类
 * @author wzs
 * @version 1.0
 */
public class EquipLocateMap  {
	private Integer id; //主键
	private String rpEquipNumber;		//巡检设备编号
	private String rpEquipCity;		//巡检设备城市
	private String rpEquipRegion;		//巡检设备区县
	private String rpEquipX;		//巡检设备经度
	private String rpEquipY;		//巡检设备纬度
	private String rpEquipName;     //巡检设备名称
	private String  rpEquipAddress;  //设备地址
	private String rptype;   //基础设备类型
	
	public String getRptype() {
		return rptype;
	}
	public void setRptype(String rptype) {
		this.rptype = rptype;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getRpEquipNumber() {
		return rpEquipNumber;
	}
	public void setRpEquipNumber(String rpEquipNumber) {
		this.rpEquipNumber = rpEquipNumber;
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
	public String getRpEquipX() {
		return rpEquipX;
	}
	public void setRpEquipX(String rpEquipX) {
		this.rpEquipX = rpEquipX;
	}
	public String getRpEquipY() {
		return rpEquipY;
	}
	public void setRpEquipY(String rpEquipY) {
		this.rpEquipY = rpEquipY;
	}
	public String getRpEquipName() {
		return rpEquipName;
	}
	public void setRpEquipName(String rpEquipName) {
		this.rpEquipName = rpEquipName;
	}
	public String getRpEquipAddress() {
		return rpEquipAddress;
	}
	public void setRpEquipAddress(String rpEquipAddress) {
		this.rpEquipAddress = rpEquipAddress;
	}
	
	
}
