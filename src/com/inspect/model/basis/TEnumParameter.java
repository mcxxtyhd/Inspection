package com.inspect.model.basis;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.inspect.model.BaseModel;

/**
 * 枚举参数类实体类
 */
@Entity
@Table(name = "t_enum_parameter")
public class TEnumParameter extends BaseModel implements Serializable {

	private static final long serialVersionUID = 1557126749909636514L;

	private String pname; //参数名称
	private String pvalue; //参数值       //中文
	private String ptype; //参数类型    
	private String pflag; //参数值标识  //数字
	private String pdesc; //参数描述
	public TEnumParameter() {
		super();
	}
	public TEnumParameter(String pname) {
		super();
		this.pname = pname;
	}
	public TEnumParameter(String pname, String pvalue, String pflag) {
		super();
		this.pname = pname;
		this.pvalue = pvalue;
		this.pflag = pflag;
	}
	public TEnumParameter(String pname, String pvalue, String ptype, String pflag, String pdesc) {
		super();
		this.pname = pname;
		this.pvalue = pvalue;
		this.ptype = ptype;
		this.pflag = pflag;
		this.pdesc = pdesc;
	}
	@Column(length=40)
	public String getPname() {
		return pname;
	}
	public void setPname(String pname) {
		this.pname = pname;
	}
	@Column(length=40)
	public String getPvalue() {
		return pvalue;
	}
	public void setPvalue(String pvalue) {
		this.pvalue = pvalue;
	}
	@Column(length=40)
	public String getPtype() {
		return ptype;
	}
	public void setPtype(String ptype) {
		this.ptype = ptype;
	}
	@Column(length=40)
	public String getPflag() {
		return pflag;
	}
	public void setPflag(String pflag) {
		this.pflag = pflag;
	}
	@Column(length=100)
	public String getPdesc() {
		return pdesc;
	}
	public void setPdesc(String pdesc) {
		this.pdesc = pdesc;
	}
}
