package com.inspect.model.baseinfo;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.inspect.model.BaseModel;
import com.inspect.model.basis.TEquipment;

/**
 * 基础信息与巡检项设备关联实体类
 * @author wzs
 * @version 1.0
 */
@Entity
@Table(name="t_base_info_equipment")
public class TBaseInfoEquipment extends BaseModel implements Serializable {

	private static final long serialVersionUID = 3152683979899892679L;
	
	private TBaseInfo tbaseinfo;//基础信息
	private TEquipment tequipment;//设备信息
	public TBaseInfoEquipment() {
		super();
	}
	public TBaseInfoEquipment(TBaseInfo tbaseinfo, TEquipment tequipment) {
		super();
		this.tbaseinfo = tbaseinfo;
		this.tequipment = tequipment;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bid")
	public TBaseInfo getTbaseinfo() {
		return tbaseinfo;
	}
	public void setTbaseinfo(TBaseInfo tbaseinfo) {
		this.tbaseinfo = tbaseinfo;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "eid")
	public TEquipment getTequipment() {
		return tequipment;
	}
	public void setTequipment(TEquipment tequipment) {
		this.tequipment = tequipment;
	}
	

}
