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
 * 巡检班组实体类
 * @author wzs
 */
@Entity
@Table(name = "t_group")
public class TGroup extends BaseModel implements Serializable {
	private static final long serialVersionUID = -524452890613541807L;
	private String gnumber;// 班组编号
	private String gname; // 班组名称
	private String glinkperson; // 班组联系人
	private String glinktel; // 班组联系电话
	private String gdesc; // 班组描述
	private Set<TInspectUser> iusers= new HashSet<TInspectUser>(0);

	public TGroup() {
	}
	
	public TGroup(int id, String gname) {
		this.id = id;
		this.gname = gname;
	}
	
	public TGroup(String gnumber, String gname) {
		this.gnumber = gnumber;
		this.gname = gname;
	}

	public TGroup(String gnumber, String gname, String glinkperson,String glinktel, String gdesc,Set<TInspectUser> iusers) {
		this.gnumber = gnumber;
		this.gname = gname;
		this.glinkperson = glinkperson;
		this.glinktel = glinktel;
		this.gdesc = gdesc;
		this.iusers= iusers;
	}

	public String getGdesc() {
		return gdesc;
	}

	public void setGdesc(String gdesc) {
		this.gdesc = gdesc;
	}
	
	public String getGlinktel() {
		return glinktel;
	}

	public void setGlinktel(String glinktel) {
		this.glinktel = glinktel;
	}
	
	public String getGlinkperson() {
		return glinkperson;
	}

	public void setGlinkperson(String glinkperson) {
		this.glinkperson = glinkperson;
	}
	
	public String getGname() {
		return gname;
	}

	public void setGname(String gname) {
		this.gname = gname;
	}
	
	public String getGnumber() {
		return gnumber;
	}

	public void setGnumber(String gnumber) {
		this.gnumber = gnumber;
	}
	
	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY,mappedBy="tgroup")
	public Set<TInspectUser> getIusers() {
		return iusers;
	}

	public void setIusers(Set<TInspectUser> iusers) {
		this.iusers = iusers;
	}
}
