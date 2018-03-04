package com.inspect.vo.comon;

import com.inspect.model.system.TSUser;


/**
 * sessionInfo模型,封装登录用户信息
 */
public class SessionInfo implements java.io.Serializable {
	private static final long serialVersionUID = -8770042390932281763L;
	private String userId;// 用户ID
	private String loginName;// 用户登录名称
	private String loginPassword;// 登录密码
	private String roleIds;   //角色ID
	private String roleNames; //角色名称
	private String entid;//单位ID
	private String entName;//单位名称
	
	private TSUser user;

	  public TSUser getUser()
	  {
	    return this.user;
	  }

	  public void setUser(TSUser user)
	  {
	    this.user = user;
	  }

	public String getLoginName() {
		return loginName;
	}
	

	public String getLoginPassword() {
		return loginPassword;
	}

	public String getRoleIds() {
		return roleIds;
	}

	public String getRoleNames() {
		return roleNames;
	}

	public String getUserId() {
		return userId;
	}


	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public void setLoginPassword(String loginPassword) {
		this.loginPassword = loginPassword;
	}

	public void setRoleIds(String roleIds) {
		this.roleIds = roleIds;
	}

	public void setRoleNames(String roleNames) {
		this.roleNames = roleNames;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	@Override
	public String toString() {
		return loginName;
	}

	public String getEntid() {
		return entid;
	}

	public void setEntid(String entid) {
		this.entid = entid;
	}

	public String getEntName() {
		return entName;
	}

	public void setEntName(String entName) {
		this.entName = entName;
	}
}
