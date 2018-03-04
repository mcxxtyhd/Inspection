package com.inspect.model.basis;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.inspect.model.BaseModel;

/**
 * RFID实体类
 * 
 * @author wzs
 */
@Entity
@Table(name = "t_rfid")
public class TRfid extends BaseModel implements Serializable {

	private static final long serialVersionUID = -632381127113766661L;

	private String rname;
	private String rdesc;

	public TRfid() {
	}

	public TRfid(String rname, String rdesc) {
		this.rname = rname;
		this.rdesc = rdesc;
	}

	public String getRname() {
		return rname;
	}

	public void setRname(String rname) {
		this.rname = rname;
	}

	public String getRdesc() {
		return rdesc;
	}

	public void setRdesc(String rdesc) {
		this.rdesc = rdesc;
	}

}
