package com.inspect.vo.system;

import java.io.Serializable;

import com.inspect.vo.comon.BaseVo;

public class PageRole extends BaseVo implements Serializable {

	private static final long serialVersionUID = 6079987329676685545L;
	
	private String rolename;  //角色名称
	private String roledesc;  //角色描述
	private String entname;
	public String getEntname() {
		return entname;
	}
	public void setEntname(String entname) {
		this.entname = entname;
	}
	public String getRolename() {
		return rolename;
	}
	public void setRolename(String rolename) {
		this.rolename = rolename;
	}
	public String getRoledesc() {
		return roledesc;
	}
	public void setRoledesc(String roledesc) {
		this.roledesc = roledesc;
	}

}
