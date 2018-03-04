package com.inspect.model.system;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.inspect.model.BaseModel;
/**
 * 系统资源功能实体类
 * @author wzs
 * @version1.0
 */
@Entity
@Table(name="t_s_function")
public class TSFunction extends BaseModel implements Serializable {
	private static final long serialVersionUID = -7651806650003231630L;
	private TSFunction TSFunction;//父菜单
	private String functionName;//菜单名称
	private Short functionLevel;//菜单等级
	private String functionUrl;//菜单地址
	private Short functionIframe;//菜单地址打开方式
	private String functionOrder;//菜单排序
	private TSIcon TSIcon = new TSIcon();//菜单图标
	private List<TSFunction> TSFunctions = new ArrayList<TSFunction>();
	
	public TSFunction() {
		super();
	}
	
	public TSFunction(TSFunction tSFunction, String functionName, Short functionLevel, String functionUrl, Short functionIframe, String functionOrder, TSIcon tSIcon, List<TSFunction> tSFunctions) {
		super();
		TSFunction = tSFunction;
		this.functionName = functionName;
		this.functionLevel = functionLevel;
		this.functionUrl = functionUrl;
		this.functionIframe = functionIframe;
		this.functionOrder = functionOrder;
		TSIcon = tSIcon;
		TSFunctions = tSFunctions;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "iconid")
	public TSIcon getTSIcon() {
		return TSIcon;
	}
	public void setTSIcon(TSIcon tSIcon) {
		TSIcon = tSIcon;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parentfunctionid")
	public TSFunction getTSFunction() {
		return this.TSFunction;
	}

	public void setTSFunction(TSFunction TSFunction) {
		this.TSFunction = TSFunction;
	}

	@Column(name = "functionname", nullable = false, length = 50)
	public String getFunctionName() {
		return this.functionName;
	}

	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

	@Column(name = "functionlevel")
	public Short getFunctionLevel() {
		return this.functionLevel;
	}

	public void setFunctionLevel(Short functionLevel) {
		this.functionLevel = functionLevel;
	}
	@Column(name = "functionurl", length = 100)
	public String getFunctionUrl() {
		return this.functionUrl;
	}

	public void setFunctionUrl(String functionUrl) {
		this.functionUrl = functionUrl;
	}
	@Column(name = "functionorder")
	public String getFunctionOrder() {
		return functionOrder;
	}

	public void setFunctionOrder(String functionOrder) {
		this.functionOrder = functionOrder;
	}
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "TSFunction")
	public List<TSFunction> getTSFunctions() {
		return TSFunctions;
	}
	public void setTSFunctions(List<TSFunction> TSFunctions) {
		this.TSFunctions = TSFunctions;
	}
	@Column(name = "functioniframe")
	public Short getFunctionIframe() {
		return functionIframe;
	}
	public void setFunctionIframe(Short functionIframe) {
		this.functionIframe = functionIframe;
	}

}
