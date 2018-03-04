package com.inspect.vo.basis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.inspect.vo.comon.BaseVo;
import com.sun.org.apache.bcel.internal.generic.NEW;

public class TermProjectVo {

	private int id;
	

	private int pgroupid; //大类名称ID
	
	private String pgroupname; //大类名称
	
	private String pname; // 小类名称

	private String ptype; // 类型

	private String pstatus;// 状态

	private String pmaxvalue;//最大值
	
	private String pminvalue;//最小值
	
	private String penumvalue;//关联枚举值
	
	private String pvalue;//巡检值
	private List<TermEnumParameterVo> evList= new ArrayList<TermEnumParameterVo>();//枚举值里面的值的集合
	
	
	
	
	 
	public String getPvalue() {
		return pvalue;
	}

	public void setPvalue(String pvalue) {
		this.pvalue = pvalue;
	}

	public String getPmaxvalue() {
		return pmaxvalue;
	}

	public void setPmaxvalue(String pmaxvalue) {
		this.pmaxvalue = pmaxvalue;
	}

	public String getPminvalue() {
		return pminvalue;
	}

	public void setPminvalue(String pminvalue) {
		this.pminvalue = pminvalue;
	}

	public List<TermEnumParameterVo> getEvList() {
		return evList;
	}

	public void setEvList(List<TermEnumParameterVo> evList) {
		this.evList = evList;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	public int getPgroupid() {
		return pgroupid;
	}

	public void setPgroupid(int pgroupid) {
		this.pgroupid = pgroupid;
	}

	public String getPgroupname() {
		return pgroupname;
	}

	public void setPgroupname(String pgroupname) {
		this.pgroupname = pgroupname;
	}
	
	public String getPenumvalue() {
		return penumvalue;
	}

	public void setPenumvalue(String penumvalue) {
		this.penumvalue = penumvalue;
	}



	
	public String getPname() {
		return pname;
	}

	public String getPstatus() {
		return pstatus;
	}

	public String getPtype() {
		return ptype;
	}

	public void setPname(String pname) {
		this.pname = pname;
	}
	public void setPstatus(String pstatus) {
		this.pstatus = pstatus;
	}
	public void setPtype(String ptype) {
		this.ptype = ptype;
	}

}
