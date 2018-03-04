package com.inspect.vo.basis;

import com.inspect.vo.comon.BaseVo;

public class VersionVo  extends BaseVo{
	private String vnum; //版本型号
	private String vdesc;//版本描述
	private String vaddress;//版本上传下载地址
	private String vupdate;//版本更新时间
	private String vname;//版本名称
	private String vpublisher;//版本发布人
	private String vstartdate;//版本开始时间（用于查询）
	private String venddate;//版本结束时间（用于查询）
	
	public String getVpublisher() {
		return vpublisher;
	}
	public void setVpublisher(String vpublisher) {
		this.vpublisher = vpublisher;
	}
	public String getVnum() {
		return vnum;
	}
	public void setVnum(String vnum) {
		this.vnum = vnum;
	}
	public String getVdesc() {
		return vdesc;
	}
	public void setVdesc(String vdesc) {
		this.vdesc = vdesc;
	}
	public String getVaddress() {
		return vaddress;
	}
	public void setVaddress(String vaddress) {
		this.vaddress = vaddress;
	}
	public String getVupdate() {
		return vupdate;
	}
	public void setVupdate(String vupdate) {
		this.vupdate = vupdate;
	}
	public String getVname() {
		return vname;
	}
	public void setVname(String vname) {
		this.vname = vname;
	}
	public String getVstartdate() {
		return vstartdate;
	}
	public void setVstartdate(String vstartdate) {
		this.vstartdate = vstartdate;
	}
	public String getVenddate() {
		return venddate;
	}
	public void setVenddate(String venddate) {
		this.venddate = venddate;
	}
	
}
