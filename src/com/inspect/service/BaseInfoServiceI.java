package com.inspect.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.inspect.model.baseinfo.TBaseInfo;
import com.inspect.model.basis.TEquipment;
import com.inspect.vo.basis.TBaseInfoVo;
import com.inspect.vo.monitor.EquipLocateMap;
import com.inspect.vo.monitor.LineLocateMap;
import com.inspect.vo.monitor.SearchLocateMap;


public interface BaseInfoServiceI {
	/**
	 *  添加巡检项对象
	 */
	void addBaseInfo(TBaseInfoVo baseInfoVo);
	/**
	 *  添加巡检项对象
	 */
	 int addBaseInfoList(List<TBaseInfo> bInfoList);
	
	/**
	 * 修改巡检项对象
	 */
	void editBaseInfo(TBaseInfoVo baseInfoVo);

	/**
	 * 删除巡检项对象
	 */
	void removeBaseInfo(String ids);

	/**
	 * 
	 */
	TBaseInfo getBaseInfoByBnum(String bnum,int entid);

	/**
	 * 查询巡检项对象 分页
	 */
	Map<String, Object> findBaseInfoDatagrid(TBaseInfoVo baseInfoVo,int page, int rows,String qsql);

	Long isExistBaseInfo(String bname,int entid);

	public <T> T getEntityById(Class<T> paramClass, Serializable id);
	String saveList(List<Object> db, int entid,int flag);
	List<EquipLocateMap> getLineLocateMap(SearchLocateMap smap);
	List<TBaseInfoVo> getRegionByCity(int entid, String bcity);
	
}
