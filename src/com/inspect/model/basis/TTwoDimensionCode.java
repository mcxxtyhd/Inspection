package com.inspect.model.basis;

import java.io.Serializable;
import java.sql.Blob;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.inspect.model.BaseModel;

/**
 * 二维码实体类
 * @author wzs
 */
@Entity
@Table(name="t_dimension_code")
public class TTwoDimensionCode extends BaseModel implements Serializable {

	private static final long serialVersionUID = -9137120712935940174L;
	
	private String cname; //二维码标识
	private Blob cpicture;//二维码图片
	private String cdesc;  //二维码描述
	public TTwoDimensionCode() {
	}
	public TTwoDimensionCode(String cname) {
		this.cname = cname;
	}
	public TTwoDimensionCode(String cname, Blob cpicture, String cdesc) {
		this.cname = cname;
		this.cpicture = cpicture;
		this.cdesc = cdesc;
	}
	public String getCname() {
		return cname;
	}
	public void setCname(String cname) {
		this.cname = cname;
	}
	public Blob getCpicture() {
		return cpicture;
	}
	public void setCpicture(Blob cpicture) {
		this.cpicture = cpicture;
	}
	@Column(length=500)
	public String getCdesc() {
		return cdesc;
	}
	public void setCdesc(String cdesc) {
		this.cdesc = cdesc;
	}

}
