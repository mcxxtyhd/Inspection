package com.inspect.vo.comon;

import java.util.ArrayList;
import java.util.List;

/**
 * DataGrid 信息模型对象,用于前台展示列表信息
 */
public class DataGrid {

	private Long total = 0L; // 记录总数
	
	@SuppressWarnings("unchecked")
	private List rows = new ArrayList();// 结果集列表

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	@SuppressWarnings("unchecked")
	public List getRows() {
		return rows;
	}

	@SuppressWarnings("unchecked")
	public void setRows(List rows) {
		this.rows = rows;
	}
}
