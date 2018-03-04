package com.inspect.model.basis;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.inspect.model.BaseModel;

/**
 * 巡检设备与巡检项组关联实体类
 * @author wzs
 * @version 1.0
 */
@Entity
@Table(name="t_equipment_project_group")
public class TEquipmentProjectGroup extends BaseModel implements Serializable {

	private static final long serialVersionUID = -7920495201653598454L;

	private TEquipment tequipment; //设备信息
	private TProjectGroup tprojectgroup;//巡检项组信息
	public TEquipmentProjectGroup() {
		super();
	}
	public TEquipmentProjectGroup(TEquipment tequipment, TProjectGroup tprojectgroup) {
		super();
		this.tequipment = tequipment;
		this.tprojectgroup = tprojectgroup;
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
	@JoinColumn(name = "pgid")
	public TProjectGroup getTprojectgroup() {
		return tprojectgroup;
	}
	public void setTprojectgroup(TProjectGroup tprojectgroup) {
		this.tprojectgroup = tprojectgroup;
	}
	
	
}
