package com.inspect.vo.basis;

import java.io.Serializable;
import java.sql.Blob;

import com.inspect.vo.comon.BaseVo;

public class TwoDimensionCodeVo extends BaseVo implements Serializable {

	private static final long serialVersionUID = -4156866936433534166L;
	
	private String cname; //二维码标识
	private Blob cpicture;//二维码图片
	private String cdesc;  //二维码描述
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
	public String getCdesc() {
		return cdesc;
	}
	public void setCdesc(String cdesc) {
		this.cdesc = cdesc;
	}

}
