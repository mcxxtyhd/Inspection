package com.inspect.vo.basis;

import java.io.Serializable;
import java.util.List;

import com.inspect.vo.comon.BaseVo;

public class ProjectVo extends BaseVo implements Serializable {

	private static final long serialVersionUID = -7536956369992767834L;

	private int pgroupid; //大类名称ID
	
	private String pgroupname; //大类名称
	
	private String pname; // 小类名称

	private String ptype; // 类型

	private String pstatus;// 状态

	private String pmaxvalue;// 最大值

	private String pminvalue;// 最小值
	
	private String pvalue;//默认值
	
	private String penumvalue;//关联枚举值
	
	private String pdesc; // 描述
	
	private String pimgstatus;//是否拍照
	
	private List evList;//枚举值里面的值的集合
	
	private int taskid ; //任务id
	
	private String pcomname; //公司名称
	
	public String getPcomname() {
		return pcomname;
	}
	public void setPcomname(String pcomname) {
		this.pcomname = pcomname;
	}
	 
	public int getTaskid() {
		return taskid;
	}

	public void setTaskid(int taskid) {
		this.taskid = taskid;
	}



	public List getEvList() {
		return evList;
	}

	public void setEvList(List evList) {
		this.evList = evList;
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

	public String getPimgstatus() {
		return pimgstatus;
	}

	public void setPimgstatus(String pimgstatus) {
		this.pimgstatus = pimgstatus;
	}

	public String getPvalue() {
		return pvalue;
	}

	public void setPvalue(String pvalue) {
		this.pvalue = pvalue;
	}
	
	public String getPdesc() {
		return pdesc;
	}

	public String getPmaxvalue() {
		return pmaxvalue;
	}

	public String getPminvalue() {
		return pminvalue;
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

	public void setPdesc(String pdesc) {
		this.pdesc = pdesc;
	}
	public void setPmaxvalue(String pmaxvalue) {
		this.pmaxvalue = pmaxvalue;
	}
	public void setPminvalue(String pminvalue) {
		this.pminvalue = pminvalue;
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
