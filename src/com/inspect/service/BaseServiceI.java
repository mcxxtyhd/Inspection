package com.inspect.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.inspect.model.system.Parameter;
import com.inspect.model.system.TLog;
import com.inspect.vo.system.PageLog;
import com.inspect.vo.system.ParameterVo;

/**
 * 系统公共业务处理类
 */
public interface BaseServiceI {
	
	/**
	 *  添加参数对象
	 */
	void addParameter(ParameterVo parametervo);
	
	/**
	 *  添加日志对象
	 */
	void addLog(TLog log);
	
	/**
	 * 修改对象
	 */
	void editParameter(ParameterVo parametervo);

	/**
	 * 删除对象
	 */
	void removeParameter(String ids);

	/**
	 * 查询对象
	 */
	Parameter getParameter(String id);

	/**
	 * 查询对象 分页
	 */
	Map<String, Object> findParameterDatagrid(ParameterVo paramertervo,int page, int rows,String qsql);
	
	/**
	 * 根据参数名称查询参数列表
	 */
    List<Parameter> getParameListByParameName(String hql,String pname);
    
    /**
     * 系统日志查询列表
     * @param pageLog
     * @param page
     * @param rows
     * @param qsql
     * @return
     */
    Map<String, Object> findLogDatagrid(PageLog pageLog, int page,int rows,String qsql);

    /**
	 * 根据参数类型查询参数列表
	 */
    List<Parameter> getParameListByParameType(String hql,String ptype);

}
