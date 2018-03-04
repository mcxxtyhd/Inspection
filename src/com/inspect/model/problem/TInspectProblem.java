package com.inspect.model.problem;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.inspect.model.BaseModel;

/**
 * 巡检问题信息实体类
 */
@Entity
@Table(name = "t_inspect_problem")
public class TInspectProblem extends BaseModel implements Serializable {

	private static final long serialVersionUID = -8912588092074594745L;
	private String protype;	 //问题类型
	private String prodesc;  //问题描述
	private String procycle; //巡检周期
	private String prosite;  //巡检站点（baseinfo主键id）
	private int iuserid; //问题提交人
	private String ternumber;//终端编号
	private String createtime; //问题提交时间
	private String proremark;  //备注
	private int proitaskid;   //任务id
	public int getProitaskid() {
		return proitaskid;
	}
	public void setProitaskid(int proitaskid) {
		this.proitaskid = proitaskid;
	}
	public TInspectProblem() {
		super();
	}
	@Column(length=20)
	public String getProtype() {
		return protype;
	}
	public void setProtype(String protype) {
		this.protype = protype;
	}
	@Column(length=100)
	public String getProdesc() {
		return prodesc;
	}
	public void setProdesc(String prodesc) {
		this.prodesc = prodesc;
	}
	@Column(length=20)
	public String getProcycle() {
		return procycle;
	}
	public void setProcycle(String procycle) {
		this.procycle = procycle;
	}
	@Column(length=20)
	public String getProsite() {
		return prosite;
	}
	public void setProsite(String prosite) {
		this.prosite = prosite;
	}
	public int getIuserid() {
		return iuserid;
	}
	public void setIuserid(int iuserid) {
		this.iuserid = iuserid;
	}
	@Column(length=20)
	public String getTernumber() {
		return ternumber;
	}
	public void setTernumber(String ternumber) {
		this.ternumber = ternumber;
	}
	@Column(length=20)
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	@Column(length=100)
	public String getProremark() {
		return proremark;
	}
	public void setProremark(String proremark) {
		this.proremark = proremark;
	}
}
