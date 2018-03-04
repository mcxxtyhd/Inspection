package com.inspect.model.system;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.inspect.model.BaseModel;

/**
 * 系统参数配置实体类
 * @author wzs
 * @version 1.0
 */
@Entity
@Table(name="t_parameter")
public class Parameter extends BaseModel implements Serializable {
	private static final long serialVersionUID = 8146460841718817838L;
	private String pname; //参数名称
	private String pvalue; //参数值
	private String ptype; //参数类型
	private String pdesc; //参数描述
	
	public Parameter() {}

	public Parameter(int id, String pname, String pvalue, String ptype) {
		this.id = id;
		this.pname = pname;
		this.pvalue = pvalue;
		this.ptype = ptype;
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

	public String getPdesc() {
		return pdesc;
	}

	public void setPdesc(String pdesc) {
		this.pdesc = pdesc;
	}
}
