package com.inspect.vo.basis;

import com.inspect.vo.comon.BaseVo;

public class EnumParameterVo extends BaseVo {

	private String pname; //参数名称
	private String pvalue; //参数值
	private String ptype; //参数类型
	private String pflag; //参数值标识
	private String pdesc; //参数描述
	private String pcomname; //公司名称
	
	public String getPcomname() {
		return pcomname;
	}
	public void setPcomname(String pcomname) {
		this.pcomname = pcomname;
	}
	public String getPname() {
		return pname;
	}
	public void setPname(String pname) {
		this.pname = pname;
	}
	public String getPvalue() {
		return pvalue;
	}
	public void setPvalue(String pvalue) {
		this.pvalue = pvalue;
	}
	public String getPtype() {
		return ptype;
	}
	public void setPtype(String ptype) {
		this.ptype = ptype;
	}
	public String getPflag() {
		return pflag;
	}
	public void setPflag(String pflag) {
		this.pflag = pflag;
	}
	public String getPdesc() {
		return pdesc;
	}
	public void setPdesc(String pdesc) {
		this.pdesc = pdesc;
	}
}
