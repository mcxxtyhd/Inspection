package com.inspect.vo.basis;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.inspect.vo.comon.BaseVo;

public class PointVo extends BaseVo implements Serializable {
	private static final long serialVersionUID = 1833653540349756456L;
	private String ponumber; // 编号
	private String poname; // 名称
	private Double poposx; // 经度
	private Double poposy; // 纬度
	private String poaddress;// 地址
	private String podesc; // 描述
	private String poeIds; // 设备编号
	private String poeNames;// 设备名称
	private String eregion;//设备区县
	private int taskid;//任务id
	private String planname;//计划名称
//	private List<EquipmentVo> elist=new ArrayList<EquipmentVo>();
	private List<EquipmentVo> equipmentList=new ArrayList<EquipmentVo>(); //设备结合
	private  File   excelFile;
	private String excelFileFileName;
	private  String   excelFileContentType;
   
	public String getEregion() {
		return eregion;
	}

	public void setEregion(String eregion) {
		this.eregion = eregion;
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

	public int getTaskid() {
		return taskid;
	}

	public void setTaskid(int taskid) {
		this.taskid = taskid;
	}

	public String getPlanname() {
		return planname;
	}

	public void setPlanname(String planname) {
		this.planname = planname;
	}

	public String getPoaddress() {
		return poaddress;
	}

	public String getPodesc() {
		return podesc;
	}

	public String getPoeIds() {
		return poeIds;
	}

	public String getPoeNames() {
		return poeNames;
	}

	public String getPoname() {
		return poname;
	}

	public String getPonumber() {
		return ponumber;
	}

	public Double getPoposx() {
		return poposx;
	}

	public Double getPoposy() {
		return poposy;
	}

	public void setPoaddress(String poaddress) {
		this.poaddress = poaddress;
	}

	public void setPodesc(String podesc) {
		this.podesc = podesc;
	}

	public void setPoeIds(String poeIds) {
		this.poeIds = poeIds;
	}

	public void setPoeNames(String poeNames) {
		this.poeNames = poeNames;
	}

	public void setPoname(String poname) {
		this.poname = poname;
	}

	public void setPonumber(String ponumber) {
		this.ponumber = ponumber;
	}

	public void setPoposx(Double poposx) {
		this.poposx = poposx;
	}

	public void setPoposy(Double poposy) {
		this.poposy = poposy;
	}

	public List<EquipmentVo> getEquipmentList() {
		return equipmentList;
	}

	public void setEquipmentList(List<EquipmentVo> equipmentList) {
		this.equipmentList = equipmentList;
	}


	
}
