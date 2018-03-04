package com.inspect.dao;

import java.io.Serializable;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.jdbc.core.JdbcTemplate;
import com.inspect.util.common.QueryResult;
import com.inspect.util.system.CriteriaQuery;

/**
 * 基础数据库操作类
 */
public interface BaseDaoI {

	/**
	 * 查询一个对象
	 * @param type
	 * @param id
	 * @return
	 */
	<T> T get(Class<T> type, Serializable id);
	
	<T> T getEntityById(Class<T> type, Serializable id) ;
     
	/**
	 * 删除对象
	 * @param type
	 * @param id
	 */
	<T> void delete(Class<T> type, Serializable id);

	/**
	 * 保存对象
	 * @param entity
	 */
	void save(Object entity);
	
	<T> void saveBatch(List<T> entitys) ;

	 void save1(Object entity);
	/**
	 * 更新对象
	 * @param entity
	 */
	void update(Object entity);
	
	<T> T hibernateSessionGet(Class<T> clazz, Serializable id) ;
	
	void hibernateSessionSaveOrUpdate(Object entity);

	/**
	 * 保存或更新对象
	 * @param entity
	 */
	void saveOrUpdate(Object entity);

	/**
	 * 批量保存
	 * @param data
	 * @return
	 */
	<T> void saveOrUpdateAll(Collection<T> data);

	/**
	 * 查询
	 * @param criteria
	 * @return
	 */
	<T> List<T> findByCriteria(DetachedCriteria criteria);
	
	<T> List<T> find(String hql);
	<T> Object find1(String hql);
	
	<T> List<T> find(String hql, List<Object> param);
	
    <T> List<T> find(String hql, Object[] param);
    
     <T> List<T> findByExample(Object exampleEntity);
     
     <T> List<T> find(Class<T> paramClass);
     
     <T> List<T> find(Class<T> type, String hql);
     
     <T> List<T> findByProperty(Class<T> entityClass, String propertyName,Object value);
    
     /**
 	 * 查找对象集合,带分页
 	 * 
 	 * @param hql
 	 * @param page
 	 *            当前页
 	 * @param rows
 	 *            每页显示记录数
 	 * @param param
 	 * @return 分页后的List<T>
 	 */
     <T> List<T> find(String hql, int page, int rows, Object... param);
 	
	/**
	 * 根据SQL语句查询获取查询对象
	 * @param type
	 * @param hql
	 * @return
	 */
	<T> T findByHql(Class<T> type, String hql);

	/**
	 * 根据SQL语句查询获取查询列表
	 * @param type
	 * @param hql
	 * @return
	 */
	<T> List<T> findByHqlAll(Class<T> type, String hql);

	/**
	 * 根据hql 语句查询，返回查询记录数
	 * @param hql
	 * @return
	 */
	int getTotalRecodeByHqlAll(String hql);

	int prepareCallment(String sql);
	
	Long count(String hql);
	
    Long count(String hql, Object[] param);

    Session getHibernaSession();

	/**
	 * Spring 的 JdbcTemplate
	 * @return 
	 */
	 JdbcTemplate getJdbcTemplate();

	/**
	 * 分页查询（注：只支持Hibernate 的Pojo 对象的查询
	 * @param className
	 *            Pojo
	 * @param wheleSql
	 *            where 语句
	 * @param firstIndex
	 *            开始索引
	 * @param maxResult
	 *            最大记录数
	 * @param queryParams
	 *            查询参数
	 * @param orderBy
	 *            排序
	 * @return
	 */
	 @SuppressWarnings("unchecked")
	<T> QueryResult<T> getQueryResult(Class className, String wheleSql, int firstIndex, int maxResult, Object[] queryParams, LinkedHashMap<String, String> orderBy);
	
    @SuppressWarnings("unchecked")
	<T> QueryResult<T> getQueryResultNotPage(Class className, String wheleSql,Object[] queryParams,LinkedHashMap<String, String> orderBy);
    
    /**
	 * 执行HQL语句
	 * @param hql
	 * @return 响应数目
	 */
	 Integer executeHql(String hql);
	 
	 Integer executeHql(String hql,Object[] param);
	 
	 <T> List<T> getListByCriteriaQuery(CriteriaQuery cq, Boolean ispage);
	 
	 String sum(String hql);
	 
	 <T> void deleteAllEntitie(Collection<T> entitys);

	 <T> QueryResult<T> getQueryResult11(Class className, String wheleSql,
			int firstIndex, int maxResult, Object[] queryParams,
			LinkedHashMap<String, String> orderBy);
	 
	 <T> boolean  isadmin(Class<T> type, String ids,int entid);
}
