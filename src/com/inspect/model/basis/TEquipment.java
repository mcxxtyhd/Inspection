package com.inspect.model.basis;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.inspect.model.BaseModel;
import com.inspect.model.baseinfo.TBaseInfoEquipment;

/**
 * 巡检设备实体类
 */
@Entity
@Table(name = "t_equipment")
public class TEquipment extends BaseModel implements Serializable {
	private static final long serialVersionUID = 1981056370952269100L;
	private String enumber; //设备编号
	private String ename;	//设备名称
	private String etype;	//类型
	private String efactory;//厂商
	private String eaddress; //设备地址
	private Double eposx;	//经度
	private Double eposy;	//纬度
	private String etwocodeid;//二维码标识
	private String erfid;	//RFID标识
	private String sdesc;	//设备描述
	private String ecity; //城市
	private String eregion;//区县
	private Set<TEquipmentProject> equipmentprojects=new HashSet<TEquipmentProject>(0);//设备巡检项集合
	private Set<TPointEquipment> pointequipments=new HashSet<TPointEquipment>(0);//巡检点设备结合
	private Set<TEquipmentProjectGroup> equipmentprojectgroups=new HashSet<TEquipmentProjectGroup>(0);//设备巡检项组集合
	private Set<TBaseInfoEquipment> baseinfoequipments=new HashSet<TBaseInfoEquipment>(0);
	public TEquipment() {
		super();
	}
	public TEquipment(String enumber, String ename) {
		super();
		this.enumber = enumber;
		this.ename = ename;
	}
	@Column(length=40)
	public String getEnumber() {
		return enumber;
	}
	public void setEnumber(String enumber) {
		this.enumber = enumber;
	}
	@Column(length=100)
	public String getEname() {
		return ename;
	}
	public void setEname(String ename) {
		this.ename = ename;
	}
	@Column(length=40)
	public String getEtype() {
		return etype;
	}
	public void setEtype(String etype) {
		this.etype = etype;
	}
	@Column(length=100)
	public String getEfactory() {
		return efactory;
	}
	public void setEfactory(String efactory) {
		this.efactory = efactory;
	}
	@Column(length=100)
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
	@Column(length=40)
	public String getEtwocodeid() {
		return etwocodeid;
	}
	public void setEtwocodeid(String etwocodeid) {
		this.etwocodeid = etwocodeid;
	}
	@Column(length=40)
	public String getErfid() {
		return erfid;
	}
	public void setErfid(String erfid) {
		this.erfid = erfid;
	}
	@Column(length=100)
	public String getSdesc() {
		return sdesc;
	}
	public void setSdesc(String sdesc) {
		this.sdesc = sdesc;
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
	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY,mappedBy="tequipment")
	public Set<TEquipmentProject> getEquipmentprojects() {
		return equipmentprojects;
	}

	public void setEquipmentprojects(Set<TEquipmentProject> equipmentprojects) {
		this.equipmentprojects = equipmentprojects;
	}
	
	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY,mappedBy="tequipment")
	public Set<TPointEquipment> getPointequipments() {
		return pointequipments;
	}
 
	public void setPointequipments(Set<TPointEquipment> pointequipments) {
		this.pointequipments = pointequipments;
	}
	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY,mappedBy="tequipment")
	public Set<TEquipmentProjectGroup> getEquipmentprojectgroups() {
		return equipmentprojectgroups;
	}
	public void setEquipmentprojectgroups(Set<TEquipmentProjectGroup> equipmentprojectgroups) {
		this.equipmentprojectgroups = equipmentprojectgroups;
	}
	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY,mappedBy="tequipment")
	public Set<TBaseInfoEquipment> getBaseinfoequipments() {
		return baseinfoequipments;
	}
	public void setBaseinfoequipments(Set<TBaseInfoEquipment> baseinfoequipments) {
		this.baseinfoequipments = baseinfoequipments;
	}
}
