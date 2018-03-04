package com.inspect.vo.comon;

/**
 * 数据传输对象基础属性类
 */
public class BaseVo {
	private int id;
	private String ids;
	private int entid; // 企业ID
	private int page;// 当前页
	private int rows;// 每页显示记录数
	private String q;// 搜索条件

	public int getEntid() {
		return entid;
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

	public String getQ() {
		return q;
	}

	public int getRows() {
		return rows;
	}

	public void setEntid(int entid) {
		this.entid = entid;
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

	public void setQ(String q) {
		this.q = q;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

}
