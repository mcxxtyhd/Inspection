package com.inspect.model.basis;

import java.io.Serializable;


import javax.persistence.Entity;
import javax.persistence.Table;

import com.inspect.model.BaseModel;
@Entity
@Table(name="t_version")
public class TVersion extends BaseModel implements Serializable {

	private static final long serialVersionUID = -4444996703464327823L;
	private String vnum; //版本名称
	private String vdesc;//版本描述
	private String vaddress;//版本上传下载地址
	private String vupdate;//版本更新时间
	private String vname;//版本名称
	private String vpublisher;//版本发布人
	
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
	
	
}
