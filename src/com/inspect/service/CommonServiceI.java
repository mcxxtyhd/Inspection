package com.inspect.service;

import java.util.List;
import java.util.Map;

import com.inspect.model.system.Enterprise;

/***
 * 
 * This class is used for ...   
 * @author liux  
 * @version   
 *       1.0, 2013-4-7 下午02:59:22   
 * information :
 *		 简单查询通用类，保存，修改，删除 : 返回值0为正确，1为错误
 */

public interface CommonServiceI<T>  {
	
	public void save(T entity); 
	
	public void delete(T entity) ; 
	
	public void deleteMuch(List<T> entity) ; 
	
	public void deleteMuch(T entity ,String ids) ; 
	
	public void update(T entity) ; 
	
	public void saveOrUpdate(T entity) ; 
	
	public T get( T entity , int id) ; 
	
	public T load( T entity , int id) ; 
	
	Map<String, Object> query(T entity ,int page, int rows,String hql , String totalSql);
	
	List<T> find( String entity ) ;
	
	List<T> find( T entity ,String hql ) ;
	
	Enterprise getEnterprise(Integer id ) ;
}
