package com.inspect.vo.basis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.inspect.model.BaseModel;
import com.sun.org.apache.bcel.internal.generic.NEW;

/**
 * 巡检项组信息实体类
 * @author wzs
 */

public class TermProjectGroupVo {
	protected Integer id; //主键
	private String pgname;//巡检项组名称
	private List<TermProjectVo> tprojectList=new ArrayList<TermProjectVo>();//巡检项集合
	
	public String getPgname() {
		return pgname;
	}
	public void setPgname(String pgname) {
		this.pgname = pgname;
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public List<TermProjectVo> getTprojectList() {
		return tprojectList;
	}
	public void setTprojectList(List<TermProjectVo> tprojectList) {
		this.tprojectList = tprojectList;
	}

	
}
