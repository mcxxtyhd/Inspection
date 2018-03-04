package com.inspect.model.system;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.inspect.model.BaseModel;
/**
 * 系统角色实体类
 * @author wzs
 * @version 1.0
 */
@Entity
@Table(name="t_s_role")
public class TSRole extends BaseModel implements Serializable {

	private static final long serialVersionUID = 2052972666247283348L;
	
	private String rolename;  //角色名称
	private String roledesc;  //角色描述
	public TSRole() {
		super();
	}
	public TSRole(String rolename, String roledesc) {
		super();
		this.rolename = rolename;
		this.roledesc = roledesc;
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
