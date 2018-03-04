package com.inspect.model.system;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.inspect.model.BaseModel;

/**
 * 角色分配功能权限实体类
 * @author wzs
 * @version 1.0
 */
@Entity
@Table(name="t_s_role_function")
public class TSRoleFunction extends BaseModel implements Serializable {

	private static final long serialVersionUID = 4784320418982894173L;
	
	private TSFunction TSFunction;
	private TSRole TSRole;
	private String operation;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "functionid")
	public TSFunction getTSFunction() {
		return this.TSFunction;
	}

	public void setTSFunction(TSFunction TSFunction) {
		this.TSFunction = TSFunction;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "roleid")
	public TSRole getTSRole() {
		return this.TSRole;
	}

	public void setTSRole(TSRole TSRole) {
		this.TSRole = TSRole;
	}

	@Column(name = "operation", length = 100)
	public String getOperation() {
		return this.operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

}
