package com.inspect.vo.problem;

import java.io.Serializable;

import com.inspect.vo.comon.BaseVo;

/**
 * 巡检问题信息实体类
 */

public class ProblemVo extends BaseVo implements Serializable {

	private static final long serialVersionUID = -8912588092074594740L;
	
	private String pronumber;//问题编号
	private String protype;	 //问题类型
	private String prodesc;  //问题描述
	private String procycle; //巡检周期
	private String prosite;  //巡检站点（baseinfo主键）
	private int iuserid; //问题提交人(所在组)
	private String ternumber;//终端编号
	private String createtime; //问题提交时间
	private String proremark;  //备注
	private int proitaskid;   //任务id
	private String proitaskname;   //任务名称
	private String iusername; //问题提交人
	private String prositename;  //巡检站点（baseinfo名字）
	private String prositenum;//巡检站点编号（baseinfo编号）
	private int index;//用于导出巡检问题序号
	
	
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public String getPrositenum() {
		return prositenum;
	}
	public void setPrositenum(String prositenum) {
		this.prositenum = prositenum;
	}
	public String getPrositename() {
		return prositename;
	}
	public void setPrositename(String prositename) {
		this.prositename = prositename;
	}
	public String getProitaskname() {
		return proitaskname;
	}
	public void setProitaskname(String proitaskname) {
		this.proitaskname = proitaskname;
	}
	public String getIusername() {
		return iusername;
	}
	public void setIusername(String iusername) {
		this.iusername = iusername;
	}
	public int getProitaskid() {
		return proitaskid;
	}
	public void setProitaskid(int proitaskid) {
		this.proitaskid = proitaskid;
	}
	public String getPronumber() {
		return pronumber;
	}
	public void setPronumber(String pronumber) {
		this.pronumber = pronumber;
	}
	public String getProtype() {
		return protype;
	}
	public void setProtype(String protype) {
		this.protype = protype;
	}
	public String getProdesc() {
		return prodesc;
	}
	public void setProdesc(String prodesc) {
		this.prodesc = prodesc;
	}
	public String getProcycle() {
		return procycle;
	}
	public void setProcycle(String procycle) {
		this.procycle = procycle;
	}
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
	public String getTernumber() {
		return ternumber;
	}
	public void setTernumber(String ternumber) {
		this.ternumber = ternumber;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public String getProremark() {
		return proremark;
	}
	public void setProremark(String proremark) {
		this.proremark = proremark;
	}
}
