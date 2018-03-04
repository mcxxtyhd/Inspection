package com.inspect.vo.basis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.inspect.vo.comon.BaseVo;

public class TermPointVo  {
//	private int entid; // 企业ID
	//private int taskid;//任务id
	private int pointid;//巡检点id
	//private int lineid;//线路id
//	private String planname;//计划名称
	private List<TermEquipmentVo> equipmentList=new ArrayList<TermEquipmentVo>(); //设备结合
	
	public int getPointid() {
		return pointid;
	}
	public void setPointid(int pointid) {
		this.pointid = pointid;
	}

	public List<TermEquipmentVo> getEquipmentList() {
		return equipmentList;
	}
	public void setEquipmentList(List<TermEquipmentVo> equipmentList) {
		this.equipmentList = equipmentList;
	}
	
	
}
