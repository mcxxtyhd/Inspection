package com.inspect.model.system;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.inspect.model.BaseModel;

/**
 * 用户角色实体类
 * @author wzs
 * @version 1.0
 */
@Entity
@Table(name="t_s_role_user")
public class TSRoleUser extends BaseModel implements Serializable {

	private static final long serialVersionUID = 8000842639626971692L;
	
	private TSUser TSUser;
	private TSRole TSRole;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "userid")
	public TSUser getTSUser() {
		return this.TSUser;
	}

	public void setTSUser(TSUser TSUser) {
		this.TSUser = TSUser;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "roleid")
	public TSRole getTSRole() {
		return this.TSRole;
	}

	public void setTSRole(TSRole TSRole) {
		this.TSRole = TSRole;
	}


}
