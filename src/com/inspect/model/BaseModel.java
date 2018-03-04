package com.inspect.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * 该类为实体类的基类，定义子类的共有属性
 */
@MappedSuperclass
public class BaseModel {
	
	protected Integer id; //主键
	
	protected Integer entid; //单位ID
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getEntid() {
		return entid;
	}
	
	public void setEntid(Integer entid) {
		this.entid = entid;
	}


}
