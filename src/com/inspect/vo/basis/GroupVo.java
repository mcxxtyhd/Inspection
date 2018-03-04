package com.inspect.vo.basis;

import java.io.Serializable;

import com.inspect.vo.comon.BaseVo;

public class GroupVo extends BaseVo implements Serializable {

	private static final long serialVersionUID = 3768114255110674273L;

	private String gnumber;// 班组编号
	private String gname; // 班组名称
	private String glinkperson; // 班组联系人
	private String glinktel; // 班组联系电话
	private String gdesc; // 班组描述
	private String pcomname; //公司名称
	
	public String getPcomname() {
		return pcomname;
	}
	public void setPcomname(String pcomname) {
		this.pcomname = pcomname;
	}
	public String getGnumber() {
		return gnumber;
	}

	public void setGnumber(String gnumber) {
		this.gnumber = gnumber;
	}

	public String getGname() {
		return gname;
	}

	public void setGname(String gname) {
		this.gname = gname;
	}

	public String getGlinkperson() {
		return glinkperson;
	}

	public void setGlinkperson(String glinkperson) {
		this.glinkperson = glinkperson;
	}

	public String getGlinktel() {
		return glinktel;
	}

	public void setGlinktel(String glinktel) {
		this.glinktel = glinktel;
	}

	public String getGdesc() {
		return gdesc;
	}

	public void setGdesc(String gdesc) {
		this.gdesc = gdesc;
	}

}
