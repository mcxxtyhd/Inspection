package com.inspect.util.common;

import java.util.ArrayList;
import java.util.List;
import com.alibaba.fastjson.JSONObject;

/**
 * 项目中分页用的实体集合类
 * @version 1.0
 */
public class QueryResult<T> {

	/**
	 * 结果集列表
	 */
	private List<T> resultList = new ArrayList<T>();
	
	/**
	 * 记录总数
	 */
	private long totalRecord;
	
	/**
	 * JSON数据对象
	 */
	private JSONObject data ;
	
	public List<T> getResultList() {
		return resultList;
	}
	public void setResultList(List<T> resultList) {
		this.resultList = resultList;
	}
	public long getTotalRecord() {
		return totalRecord;
	}
	public void setTotalRecord(long totalRecord) {
		this.totalRecord = totalRecord;
	}
	public JSONObject getData() {
		return data;
	}
	public void setData(JSONObject data) {
		this.data = data;
	}
	
}
