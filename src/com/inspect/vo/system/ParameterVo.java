package com.inspect.vo.system;

import java.io.Serializable;

import com.inspect.vo.comon.BaseVo;

public class ParameterVo extends BaseVo implements Serializable {
	private static final long serialVersionUID = 8285860816072686776L;
	private String pname; // 参数名称
	private String pvalue; // 参数值
	private String ptype; // 参数类型
	private String pdesc; // 参数描述

	public String getPdesc() {
		return pdesc;
	}

	public String getPname() {
		return pname;
	}

	public String getPtype() {
		return ptype;
	}

	public String getPvalue() {
		return pvalue;
	}
	public void setPdesc(String pdesc) {
		this.pdesc = pdesc;
	}

	public void setPname(String pname) {
		this.pname = pname;
	}

	public void setPtype(String ptype) {
		this.ptype = ptype;
	}

	public void setPvalue(String pvalue) {
		this.pvalue = pvalue;
	}
}
