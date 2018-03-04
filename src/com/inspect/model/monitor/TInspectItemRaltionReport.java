package com.inspect.model.monitor;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 巡检线路与巡检点关联实体类
 * @author wzs
 */
@Entity
@Table(name="t_report_message_relation")
public class TInspectItemRaltionReport implements Serializable {

	private static final long serialVersionUID = -1849556674283738532L;
	
	private int id;
	private TInspectItemReport tinspectitemreport;
	private TInspectItemDetailReport tinspectitemdetailrport;
	public TInspectItemRaltionReport() {
	}
	public TInspectItemRaltionReport(int id) {
		this.id = id;
	}
	public TInspectItemRaltionReport(int id,
			TInspectItemReport tinspectitemreport,
			TInspectItemDetailReport tinspectitemdetailrport) {
		this.id = id;
		this.tinspectitemreport = tinspectitemreport;
		this.tinspectitemdetailrport = tinspectitemdetailrport;
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
	@JoinColumn(name="xmid")
	public TInspectItemReport getTinspectitemreport() {
		return tinspectitemreport;
	}
	public void setTinspectitemreport(TInspectItemReport tinspectitemreport) {
		this.tinspectitemreport = tinspectitemreport;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="xdid")
	public TInspectItemDetailReport getTinspectitemdetailrport() {
		return tinspectitemdetailrport;
	}
	public void setTinspectitemdetailrport(TInspectItemDetailReport tinspectitemdetailrport) {
		this.tinspectitemdetailrport = tinspectitemdetailrport;
	}
}
