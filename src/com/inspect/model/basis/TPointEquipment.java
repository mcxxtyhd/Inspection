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
 * 巡检点与设备绑定关联实体类
 * @author wzs
 */
@Entity
@Table(name="t_point_equipment")
public class TPointEquipment implements Serializable {
	private static final long serialVersionUID = 5982299990486338568L;
	private int id;
	private TPoint tpoint;
	private TEquipment tequipment;

	public TPointEquipment() {
	}

	public TPointEquipment(int id) {
		this.id = id;
	}

	public TPointEquipment(int id, TPoint tpoint, TEquipment tequipment) {
		this.id = id;
		this.tpoint = tpoint;
		this.tequipment = tequipment;
	}

	@Id
	@GeneratedValue
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="pointid")
	public TPoint getTpoint() {
		return tpoint;
	}

	public void setTpoint(TPoint tpoint) {
		this.tpoint = tpoint;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="equipmentid")
	public TEquipment getTequipment() {
		return tequipment;
	}

	public void setTequipment(TEquipment tequipment) {
		this.tequipment = tequipment;
	}

}
