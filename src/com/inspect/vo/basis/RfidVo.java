package com.inspect.vo.basis;

import java.io.Serializable;

import com.inspect.vo.comon.BaseVo;

public class RfidVo extends BaseVo implements Serializable {

	private static final long serialVersionUID = -49768455500629844L;

	private String rname;
	private String rdesc;

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
