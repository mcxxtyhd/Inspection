package com.inspect.vo.basis;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.inspect.model.BaseModel;

/**
 * 枚举参数类实体类
 */

public class TermEnumParameterVo {

	private String pvalue; //参数值
	private String pflag; //参数值标识



	public String getPvalue() {
		return pvalue;
	}
	public void setPvalue(String pvalue) {
		this.pvalue = pvalue;
	}

	public String getPflag() {
		return pflag;
	}
	public void setPflag(String pflag) {
		this.pflag = pflag;
	}

	
}
