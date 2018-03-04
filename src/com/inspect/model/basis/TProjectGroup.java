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

/**
 * 巡检项组信息实体类
 * @author wzs
 */
@Entity
@Table(name="t_project_group")
public class TProjectGroup extends BaseModel implements Serializable {

	private static final long serialVersionUID = -3333806655212116652L;
	
	private String pgname;//巡检项组名称
	private String pgdesc;//巡检项组描述
	private Set<TProject> tprojects=new HashSet<TProject>(0);//巡检项集合
	private Set<TEquipmentProjectGroup> equipmentprojectgroups=new HashSet<TEquipmentProjectGroup>(0);//设备巡检项组集合
	public TProjectGroup() {
		super();
	}
	public TProjectGroup(String pgname, String pgdesc, Set<TProject> tprojects) {
		super();
		this.pgname = pgname;
		this.pgdesc = pgdesc;
		this.tprojects = tprojects;
	}
	public String getPgname() {
		return pgname;
	}
	public void setPgname(String pgname) {
		this.pgname = pgname;
	}
	@Column(length=100)
	public String getPgdesc() {
		return pgdesc;
	}
	public void setPgdesc(String pgdesc) {
		this.pgdesc = pgdesc;
	}
	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY,mappedBy="tprojectgroup")
	public Set<TProject> getTprojects() {
		return tprojects;
	}
	public void setTprojects(Set<TProject> tprojects) {
		this.tprojects = tprojects;
	}
	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY,mappedBy="tprojectgroup")
	public Set<TEquipmentProjectGroup> getEquipmentprojectgroups() {
		return equipmentprojectgroups;
	}
	public void setEquipmentprojectgroups(Set<TEquipmentProjectGroup> equipmentprojectgroups) {
		this.equipmentprojectgroups = equipmentprojectgroups;
	}
}
