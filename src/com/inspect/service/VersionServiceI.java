package com.inspect.service;

import java.util.Map;

import org.apache.tools.ant.filters.FixCrLfFilter.AddAsisRemove;

import com.inspect.model.basis.TVersion;
import com.inspect.vo.basis.VersionVo;

public interface VersionServiceI {
	/**
	 * 查询巡检项对象 分页
	 */
	Map<String, Object> findNoticeDatagrid(VersionVo versionVo,int page, int rows,String qsql);
	
	void addVersion(VersionVo versionVo);
	void editVersion(VersionVo version);
	void removeVersion(String ids);
	//获取最新的版本
	TVersion getVersion();
	
	//回滚至原来的版本
	TVersion getRevertVersion();
	//检查某版本号是否存在
	boolean isExist(String vnum,int entid);
	public TVersion getVersion(int id);

}
