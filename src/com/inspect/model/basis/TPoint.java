package com.inspect.model.basis;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.inspect.model.BaseModel;

/**
 * 巡检点实体类
 * @author wzs
 */
@Entity
@Table(name="t_point")
public class TPoint extends BaseModel implements Serializable {
	private static final long serialVersionUID = -530285980180265720L;
	private String ponumber; //编号
	private String poname;	//名称
	private Double poposx;	//经度
	private Double poposy;	//纬度
	private String poaddress;//地址
	private String podesc;	//描述
	private Set<TPointEquipment> pointequipments=new HashSet<TPointEquipment>(0);//设备结合
	private Set<TLinePoint> linepoints=new HashSet<TLinePoint>(); //线路巡检点集合

	public TPoint() {
	}

	public TPoint(String ponumber, String poname) {
		this.ponumber = ponumber;
		this.poname = poname;
	}

	public TPoint(String ponumber, String poname, Double poposx, Double poposy,
			String poaddress, String podesc,Set<TPointEquipment> pointequipments,Set<TLinePoint> linepoints) {
		this.ponumber = ponumber;
		this.poname = poname;
		this.poposx = poposx;
		this.poposy = poposy;
		this.poaddress = poaddress;
		this.podesc = podesc;
		this.pointequipments = pointequipments;
		this.linepoints = linepoints;
	}

	public String getPonumber() {
		return ponumber;
	}

	public void setPonumber(String ponumber) {
		this.ponumber = ponumber;
	}

	public String getPoname() {
		return poname;
	}

	public void setPoname(String poname) {
		this.poname = poname;
	}

	public Double getPoposx() {
		return poposx;
	}

	public void setPoposx(Double poposx) {
		this.poposx = poposx;
	}

	public Double getPoposy() {
		return poposy;
	}

	public void setPoposy(Double poposy) {
		this.poposy = poposy;
	}

	public String getPoaddress() {
		return poaddress;
	}

	public void setPoaddress(String poaddress) {
		this.poaddress = poaddress;
	}

	public String getPodesc() {
		return podesc;
	}

	public void setPodesc(String podesc) {
		this.podesc = podesc;
	}

	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY,mappedBy="tpoint")
	public Set<TPointEquipment> getPointequipments() {
		return pointequipments;
	}

	public void setPointequipments(Set<TPointEquipment> pointequipments) {
		this.pointequipments = pointequipments;
	}
	
	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY,mappedBy="tpoint")
	public Set<TLinePoint> getLinepoints() {
		return linepoints;
	}

	public void setLinepoints(Set<TLinePoint> linepoints) {
		this.linepoints = linepoints;
	}
}
