package com.inspect.model.system;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 单位持久化实体类
 * @author wzs
 * @version 1.0
 */
@Entity
@Table(name = "t_s_enterprise")
public class Enterprise implements Serializable {
	private static final long serialVersionUID = -4546900386030378174L;
	private int id;
	private String entname; // 单位名称
	private String entlinkperson;// 联系人
	private String entlinktel; // 联系电话
	private String entaddress; // 单位地址
	private String entdesc; // 单位描述
	public Enterprise() {
	}
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getEntname() {
		return entname;
	}
	public void setEntname(String entname) {
		this.entname = entname;
	}
	public String getEntlinkperson() {
		return entlinkperson;
	}
	public void setEntlinkperson(String entlinkperson) {
		this.entlinkperson = entlinkperson;
	}
	public String getEntlinktel() {
		return entlinktel;
	}
	public void setEntlinktel(String entlinktel) {
		this.entlinktel = entlinktel;
	}
	public String getEntaddress() {
		return entaddress;
	}
	public void setEntaddress(String entaddress) {
		this.entaddress = entaddress;
	}
	public String getEntdesc() {
		return entdesc;
	}
	public void setEntdesc(String entdesc) {
		this.entdesc = entdesc;
	}
}
