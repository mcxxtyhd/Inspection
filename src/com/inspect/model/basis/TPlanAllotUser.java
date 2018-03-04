package com.inspect.model.basis;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.inspect.model.BaseModel;

/**
 * 巡检计划分配人员实体类
 * @author wzs
 */
//@Entity
//o@Table(name="t_plan_user")
public class TPlanAllotUser extends BaseModel implements Serializable {

	private static final long serialVersionUID = 4206075210161908751L;
	
	private int pid;
	private int uid;
	private int gid;
	public TPlanAllotUser() {
	}
	public TPlanAllotUser(int pid, int uid, int gid) {
		this.pid = pid;
		this.uid = uid;
		this.gid = gid;
	}
	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public int getGid() {
		return gid;
	}
	public void setGid(int gid) {
		this.gid = gid;
	}

}
