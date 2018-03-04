package com.inspect.vo.system;

import java.io.Serializable;

public class EnterpriseVo implements Serializable {

	private static final long serialVersionUID = -6163208320813510229L;
	private int id;
	private String entname; // 单位名称
	private String entlinkperson;// 联系人
	private String entlinktel; // 联系电话
	private String entaddress; // 单位地址
	private String entdesc; // 单位描述
	private String ids;
	private int page;// 当前页
	private int rows;// 每页显示记录数
	public String getEntaddress() {
		return entaddress;
	}
	public String getEntdesc() {
		return entdesc;
	}
	public String getEntlinkperson() {
		return entlinkperson;
	}
	public String getEntlinktel() {
		return entlinktel;
	}
	public String getEntname() {
		return entname;
	}
	public int getId() {
		return id;
	}
	public String getIds() {
		return ids;
	}
	public int getPage() {
		return page;
	}
	public int getRows() {
		return rows;
	}
	public void setEntaddress(String entaddress) {
		this.entaddress = entaddress;
	}
	public void setEntdesc(String entdesc) {
		this.entdesc = entdesc;
	}
	public void setEntlinkperson(String entlinkperson) {
		this.entlinkperson = entlinkperson;
	}
	public void setEntlinktel(String entlinktel) {
		this.entlinktel = entlinktel;
	}
	public void setEntname(String entname) {
		this.entname = entname;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
}
