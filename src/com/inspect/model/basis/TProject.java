package com.inspect.model.basis;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.inspect.model.BaseModel;

/**
 * 巡检项实体类
 * @author wzs
 */
@Entity
@Table(name="t_project")
public class TProject extends BaseModel implements Serializable {
	private static final long serialVersionUID = -5752887612338686581L;
	private String pname; //小类名称
	private String ptype; //类型
	private String pstatus;//状态
	private String pmaxvalue;//最大值
	private String pminvalue;//最小值
	private String penumvalue;//关联枚举值
	private String pdesc;	//描述
	private String pvalue;//默认值
	private String pimgstatus;//是否拍照
	private TProjectGroup tprojectgroup;//巡检项大类对象
	private Set<TEquipmentProject> equipmentprojects=new HashSet<TEquipmentProject>(0);
	public TProject() {
		super();
	}
	public TProject(String pname, String ptype, String pstatus, String pmaxvalue, String pminvalue, String penumvalue, String pdesc, String pvalue, String pimgstatus) {
		super();
		this.pname = pname;
		this.ptype = ptype;
		this.pstatus = pstatus;
		this.pmaxvalue = pmaxvalue;
		this.pminvalue = pminvalue;
		this.penumvalue = penumvalue;
		this.pdesc = pdesc;
		this.pvalue = pvalue;
		this.pimgstatus = pimgstatus;
	}
	@Column(length=200)
	public String getPname() {
		return pname;
	}
	public void setPname(String pname) {
		this.pname = pname;
	}
	@Column(length=40)
	public String getPtype() {
		return ptype;
	}
	public void setPtype(String ptype) {
		this.ptype = ptype;
	}
	@Column(length=40)
	public String getPstatus() {
		return pstatus;
	}
	public void setPstatus(String pstatus) {
		this.pstatus = pstatus;
	}
	@Column(length=40)
	public String getPmaxvalue() {
		return pmaxvalue;
	}
	public void setPmaxvalue(String pmaxvalue) {
		this.pmaxvalue = pmaxvalue;
	}
	@Column(length=40)
	public String getPminvalue() {
		return pminvalue;
	}
	public void setPminvalue(String pminvalue) {
		this.pminvalue = pminvalue;
	}
	@Column(length=40)
	public String getPenumvalue() {
		return penumvalue;
	}
	public void setPenumvalue(String penumvalue) {
		this.penumvalue = penumvalue;
	}
	@Column(length=100)
	public String getPdesc() {
		return pdesc;
	}
	public void setPdesc(String pdesc) {
		this.pdesc = pdesc;
	}
	public String getPvalue() {
		return pvalue;
	}
	public void setPvalue(String pvalue) {
		this.pvalue = pvalue;
	}
	public String getPimgstatus() {
		return pimgstatus;
	}
	public void setPimgstatus(String pimgstatus) {
		this.pimgstatus = pimgstatus;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="pgroupid",nullable=true)
	public TProjectGroup getTprojectgroup() {
		return tprojectgroup;
	}
	public void setTprojectgroup(TProjectGroup tprojectgroup) {
		this.tprojectgroup = tprojectgroup;
	}
	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY,mappedBy="tproject")
	public Set<TEquipmentProject> getEquipmentprojects() {
		return equipmentprojects;
	}
	public void setEquipmentprojects(Set<TEquipmentProject> equipmentprojects) {
		this.equipmentprojects = equipmentprojects;
	}
}
