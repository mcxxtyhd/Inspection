package com.inspect.model.basis;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 巡检设备与巡检项关联实体类
 * @author wzs
 * @version 1.0
 */
@Entity
@Table(name="t_equipment_project")
public class TEquipmentProject implements Serializable {
	private static final long serialVersionUID = -3097885983464006533L;
	private int id;
	private TEquipment tequipment; //设备信息
	private TProject tproject;  //巡检项信息

	public TEquipmentProject() {
	}

	public TEquipmentProject(int id) {
		this.id = id;
	}

	public TEquipmentProject(int id, TEquipment tequipment, TProject tproject) {
		this.id = id;
		this.tequipment = tequipment;
		this.tproject = tproject;
	}

	@Id
	@GeneratedValue
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "eid")
	public TEquipment getTequipment() {
		return tequipment;
	}

	public void setTequipment(TEquipment tequipment) {
		this.tequipment = tequipment;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pid")
	public TProject getTproject() {
		return tproject;
	}

	public void setTproject(TProject tproject) {
		this.tproject = tproject;
	}
}
