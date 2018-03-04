package com.inspect.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.inspect.dao.BaseDaoI;
import com.inspect.model.system.Enterprise;
import com.inspect.service.CommonServiceI;

/***
 * 
 * This class is used for ...   
 * @author liux  
 * @version   
 *       1.0, 2013-4-7 下午03:04:22   
 * information :
 *		 简单查询通用类，保存，修改，删除 : 返回值0为正确，1为错误
 */
@Service("commonService")
@Transactional(propagation = Propagation.REQUIRED,readOnly = false,rollbackFor = Exception.class)
public  class CommonServiceImpl<T> implements CommonServiceI<T> {

	@Resource
	private BaseDaoI baseDao;

	@Override
	public void delete(T entity) {
		baseDao.getHibernaSession().delete(entity) ;
	}

	@Override
	public T get( T entity ,int id) {
		// TODO Auto-generated method stub
		return (T)baseDao.getHibernaSession().get( entity.getClass() , id ) ;
	}

	@Override
	public T load( T entity ,int id) {
		return (T)baseDao.getHibernaSession().load( entity.getClass() , id ) ;
	}

	@Override
	public void save(T entity) {
		baseDao.getHibernaSession().save(entity) ;
	}

	@Override
	public void update(T entity) {
		baseDao.getHibernaSession().update(entity) ;
	}
	
	public void saveOrUpdate(T entity) {
		baseDao.getHibernaSession().saveOrUpdate(entity) ;
	}

	@Override
	public Map<String, Object> query(T entity ,int page, int rows,
			String hql , String totalSql) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<T> list = new ArrayList<T>();
		StringBuffer buf = new StringBuffer( hql );
		//System.out.println(hql);
		list = baseDao.find( hql , page, rows , null ) ; 
		long total = baseDao.count( totalSql );
		
		map.put("total", total );
		map.put("come", (page - 1) * rows);
		map.put("to", rows * page);
		map.put("rows", list );
		return map;
	}

	@Override
	public List<T> find( String entity ) {
		List<T> list = new ArrayList<T>();
		StringBuffer hql = new StringBuffer(  );
		hql.append("from ").append( entity ) ;
		list =  baseDao.find( hql.toString() ) ;
		return baseDao.find( hql.toString() ) ;
	}
	
	public List<T> find( T entity ,String hql ) {
		List<T> list = new ArrayList<T>();
		list =  baseDao.find( hql.toString() ) ;
		return baseDao.find( hql.toString() ) ;
	}

	@Override
	public void deleteMuch(List<T> entityList) {
		if( entityList.isEmpty() ){
			for(int i=0 ;i<entityList.size() ;i++){
				baseDao.getHibernaSession().delete( entityList.get(i)) ;
			}
		}
		
	}

	@Override
	public void deleteMuch(T entity, String ids) {
		if( ! ids.isEmpty() ){
			for( int i=0;i<ids.split(",").length;i++ ){
				baseDao.delete( entity.getClass() , Integer.parseInt( ids.split(",")[i] ) ) ;
			}
		}	
	}
	
	public Enterprise getEnterprise(Integer id ) {
		return baseDao.get(Enterprise.class, id) ;
	}

	
	
}
