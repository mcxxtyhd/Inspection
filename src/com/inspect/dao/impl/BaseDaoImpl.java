package com.inspect.dao.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.inspect.dao.AnnotatedHibernateDaoSupport;
import com.inspect.dao.BaseDaoI;
import com.inspect.model.BaseModel;
import com.inspect.util.common.QueryResult;
import com.inspect.util.system.CriteriaQuery;

/**
 * 基础数据操作实现类
 */
@Repository("baseDao")
public  class BaseDaoImpl extends AnnotatedHibernateDaoSupport implements BaseDaoI {
	
	public <T> T get(Class<T> type, Serializable id) {
		return type.cast(getHibernateTemplate().get(type, id));
	}
	
	@Override
	public <T> T hibernateSessionGet(Class<T> clazz, Serializable id){
		return (T) getHibernaSession().get(clazz, id) ;
	}
	
	@Override
	public void hibernateSessionSaveOrUpdate(Object entity){
		//getHibernaSession().saveOrUpdate(entity) ;
		getHibernaSession().update(entity) ;
	}
	
	//添加信息
	public void save(Object entity) {
		getHibernateTemplate().save(entity);		
		
	}
	//添加信息
	public void save1(Object entity) {
		 getHibernaSession().save(entity);		
		
	}
	
	//修改或添加信息
	public <T> void saveOrUpdateAll(Collection<T> data) {
		getHibernateTemplate().saveOrUpdateAll(data);
	}

	//修改信息
	public void update(Object entity) {
		getHibernateTemplate().update(entity);
	}
	
	//删除信息
	public <T> void delete(Class<T> type, Serializable id) {
		getHibernateTemplate().delete(getHibernateTemplate().get(type, id));
		
	}
	
	//批量增加信息
	public <T> void saveBatch(List<T> entitys) {
		for (int i = 0; i < entitys.size(); ++i)	    {
	      this.save(entitys.get(i));
	      if (i % 20 == 0){
	        getHibernateTemplate().flush();
	        getHibernateTemplate().clear();
	      }
	    }
	}
	
	@Override
	public Session getHibernaSession() {
		return getSession();
	}

	//根据hql语句查询信息
	@SuppressWarnings("unchecked")
	public <T> List<T> find(String hql) {
		return getHibernateTemplate().find(hql);
	}
	

	//根据hql语句查询信息
	@SuppressWarnings("unchecked")
	public Object find1(String hql) {
		try{
		System.out.println("hql:"+hql);
		List list=getHibernateTemplate().find(hql);
		System.out.println("okkkkkkk");
		Object obj=null;
		if(list!=null&&list.size()>0){
			System.out.println("(basedaoImpl)list: "+list.size());
			obj=list.get(0);
		}
		else{
			System.out.println("(basedaoImpl)list: 空");
		}
		return obj;
		
		}catch(Exception e){
			System.out.println(e.getMessage());
			return null;
		}
	}
	//根据HQL语句进行高级查询
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> find(String hql, List<Object> param) {
		Query q = this.getSession().createQuery(hql);
		if (param != null && param.size() > 0) {
			for (int i = 0; i < param.size(); i++) {
				q.setParameter(i, param.get(i));
			}
		}
		return q.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> find(String hql, Object[] param) {
		Query q = this.getSession().createQuery(hql);
		if (param != null && param.length > 0) {
			for (int i = 0; i < param.length; i++) {
				q.setParameter(i, param[i]);
			}
		}
		return q.list();
	}
	
	  //查询
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> find(Class<T> paramClass) {
		  Criteria criteria =getHibernaSession().createCriteria(paramClass);
		  return criteria.list();
	}
   //查询
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> find(Class<T> type, String hql) {
		List data = getHibernateTemplate().find(hql);
//		if (null != data && data.size() > 0) {
//			return (List<T>) type.cast(data.get(0));
//		}
		return data;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> find(String hql, int page, int rows, Object... param) {
		Query q = this.getHibernaSession().createQuery(hql);
		if (param != null && param.length > 0) {
			for (int i = 0; i < param.length; i++) {
				q.setParameter(i, param[i]);
			}
		}
		return q.setFirstResult((page - 1) * rows).setMaxResults(rows).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> findByCriteria(DetachedCriteria criteria) {
		return getHibernateTemplate().findByCriteria(criteria);
	}
	
	@SuppressWarnings("unchecked")
	public <T> List<T> findByExample(Object exampleEntity){
		return getHibernateTemplate().findByExample(exampleEntity);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T findByHql(Class<T> type, String hql) {
		List data = findByHqlAll(type,hql);
		if (null != data && data.size() > 0) {
			return type.cast(data.get(0));
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> findByHqlAll(Class<T> type, String hql) {
		return getHibernateTemplate().find(hql);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> findByProperty(Class<T> entityClass, String propertyName,Object value) {
		 Assert.hasText(propertyName);
		 return createCriteria(entityClass, new Criterion[]{Restrictions.eq(propertyName,value)}).list();
	}
	
	private <T> Criteria createCriteria(Class<T> entityClass, Criterion[] criterions){
	    Criteria criteria = getHibernaSession().createCriteria(entityClass);
	    Criterion[] arrayOfCriterion = criterions;
	    byte b1 = 0;
	    byte b2 =(byte) arrayOfCriterion.length;
	    while (b1 < b2){
	      Criterion c = arrayOfCriterion[b1];
	      criteria.add(c);
	      ++b1;
	    }
	    return criteria;
	  }
	
	@Override
	public <T> T getEntityById(Class<T> type, Serializable id) {
		return type.cast(getHibernateTemplate().get(type, id));
	}

	@Override
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> QueryResult<T> getQueryResult(Class className, String wheleSql,int firstIndex, int maxResult, Object[] queryParams,LinkedHashMap<String, String> orderBy) {
		
		System.out.println("getQueryResult函数开始执行");
		
		QueryResult<T> queryResult = new QueryResult<T>();
		StringBuffer hql = new StringBuffer("from ").append(className.getSimpleName()).append(wheleSql == null ? " " : " where " + wheleSql);
		hql.append(buildOrderByql(orderBy));
		
		//测试
		System.out.println("getQueryResult数据库查询语句1："+hql.toString());
		//String str = "from TInspectItemReport";
		//Query query = getHibernaSession().createQuery(str);
		Query query = null;
		try {
			query = getHibernaSession().createQuery(hql.toString());
			  List list=query.list(); 
			    System.out.println("语句1查询后得到的list大小："+list.size());
	
		} catch (Exception e) {
			System.out.println("query执行失败！");
			e.printStackTrace();
		}
		 
		
		    
//		    Iterator iter=list.iterator(); 
//		    while(iter.hasNext()){ 
//		    	
//		     Object[] obj=(Object[]) iter.next(); 
//		     System.out.println(obj[0].toString());
//
//		    } 
		
		setQueryParams(query, queryParams);
		// 设置分页的索引与分页记录数
		if (firstIndex != -1 && maxResult != -1) {
			query.setFirstResult(firstIndex).setMaxResults(maxResult);
		}
		
		
		
		List<T> resultList = (List<T>)query.list();
		
		
		queryResult.setResultList(resultList);
		// 获取并设置分页数据的总记录数
		
		hql.delete(0, hql.length());
		
		hql.append("select count(*) from ").append(className.getSimpleName()).append(wheleSql == null ? " " : " where " + wheleSql);
		
		System.out.println("getQueryResult数据库查询语句2："+hql.toString());
		
		query = getHibernaSession().createQuery(hql.toString());
		setQueryParams(query, queryParams);
		
		queryResult.setTotalRecord((Long)query.uniqueResult());
		
		System.out.println("语句2查到结果数："+(Long)query.uniqueResult());
		
		System.out.println("返回结果集数："+queryResult.getResultList().size());
		return queryResult;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> QueryResult<T> getQueryResultNotPage(Class className,String wheleSql, Object[] queryParams,LinkedHashMap<String, String> orderBy) {
		QueryResult<T> queryResult = new QueryResult<T>();
		StringBuffer hql = new StringBuffer("from ").append(className.getSimpleName()).append(wheleSql == null ? " " : " where " + wheleSql);
		hql.append(buildOrderByql(orderBy));
		Query query = getHibernaSession().createQuery(hql.toString());
		setQueryParams(query, queryParams);
		List<T> resultList = (List<T>) query.list();
		queryResult.setResultList(resultList);
		// 获取并设置分页数据的总记录数
		hql.delete(0, hql.length());
		hql.append("select count(*) from ").append(className.getSimpleName()).append(wheleSql == null ? " " : " where " + wheleSql);
		query = getHibernaSession().createQuery(hql.toString());
		setQueryParams(query, queryParams);
		queryResult.setTotalRecord((Long)query.uniqueResult());
		return queryResult;
	}
	
	public Long count(String hql) {
		return (Long)getHibernaSession().createQuery(hql).uniqueResult();
	}
	
	public Long count(String hql, Object[] param) {
		Query q = this.getSession().createQuery(hql);
		if (param != null && param.length > 0) {
			for (int i = 0; i < param.length; i++) {
				q.setParameter(i, param[i]);
			}
		}
		return (Long) q.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public String sum(String hql) {
		List li = getSession().createQuery(hql).list();
		String sum = null ;
		if (li != null && !li.isEmpty()) {
			if (li.get(0) != null) {
				sum = li.get(0).toString() ;
			}
		}
		return sum ;
	}
	

	@SuppressWarnings("unchecked")
	@Override
	public int getTotalRecodeByHqlAll(String hql) {
		int resutl=0;
		if(StringUtils.isEmpty(hql)) return resutl;
		List list=this.getHibernaSession().createQuery(hql).list();
		if(list != null&& list.size()>0 ){
			resutl=list.size();
		}
		return resutl;
		//((Integer) getHibernateTemplate().iterate(hql).next()).intValue();
	}

	@Override
	public int prepareCallment(String sql) {
		return jdbcTemplate.update(sql);
	}

	public Integer executeHql(String hql){
		return getHibernaSession().createQuery(hql).executeUpdate();
	}

	@Override
	public Integer executeHql(String hql,Object[] param) {
		Query q = this.getSession().createQuery(hql);
		if (param != null && param.length > 0) {
			for (int i = 0; i < param.length; i++) {
				q.setParameter(i, param[i]);
			}
		}
		return q.executeUpdate();
	}
	/**
	 * 组装order by 语句
	 * @param orderBy  { "id":"desc", "name":"asc" }
	 * @return order by o.key1 desc,o.key2 asc,xxx desc
	 */
	private String buildOrderByql(LinkedHashMap<String, String> orderBy) {
		StringBuffer orderByql = new StringBuffer();
		if (orderBy != null && orderBy.size() > 0) {
			orderByql.append(" order by ");
			for (String key : orderBy.keySet()) {
				orderByql.append(key).append(" ").append(
						orderBy.get(key)).append(",");
			}
			orderByql.deleteCharAt(orderByql.length() - 1);
		}
		return orderByql.toString();
	}
	
	/**
	 * 设置查询条件的参数
	 * @param query  知道的查询对象
	 * @param queryParams  查询参数
	 */
	private void setQueryParams(Query query, Object[] queryParams) {
		if (queryParams != null && queryParams.length > 0) {
			for (int i = 0; i < queryParams.length; i++) {
				query.setParameter(i , queryParams[i]);
			}
		}
	}
	 @SuppressWarnings("unchecked")
		public <T> List<T> getListByCriteriaQuery(CriteriaQuery cq, Boolean ispage){
		    Criteria criteria = cq.getDetachedCriteria().getExecutableCriteria(getSession());
		    if (CriteriaQuery.getOrdermap() != null)
		      cq.setOrder(CriteriaQuery.getOrdermap());
		    if (ispage.booleanValue())
		      criteria.setMaxResults(cq.getPageSize());
		    return criteria.list();
		  }
	
		 @SuppressWarnings("unchecked")
		public <T> void deleteAllEntitie(Collection<T> entitys){
		    Iterator localIterator = entitys.iterator();
		    while (localIterator.hasNext()){
		      Object entity = localIterator.next();
		      getHibernateTemplate().delete(entity);
		      getHibernateTemplate().flush();
		    }
		}
		 
		 
			@Override
			@SuppressWarnings("unchecked")
			public <T> QueryResult<T> getQueryResult11(Class className, String wheleSql,int firstIndex, int maxResult, Object[] queryParams,LinkedHashMap<String, String> orderBy) {
				QueryResult<T> queryResult = new QueryResult<T>();
				StringBuffer hql = new StringBuffer("from ").append(className.getSimpleName()).append(" a ").append(wheleSql == null ? " " : " where " + wheleSql);
				//	System.out.println(hql);
				hql.append(buildOrderByql(orderBy));
				hql.append(" AND NOT EXISTS(SELECT 1 FROM TInspectItemReport WHERE a.xequid=xequid AND a.id<id) ");
				hql.append(" order by id desc");
				//	System.out.println(hql.toString());
				Query query = getHibernaSession().createQuery(hql.toString());
				setQueryParams(query, queryParams);
				// 设置分页的索引与分页记录数
				if (firstIndex != -1 && maxResult != -1) {
					query.setFirstResult(firstIndex).setMaxResults(maxResult);
				}
				List<T> resultList = (List<T>)query.list();
				queryResult.setResultList(resultList);
				// 获取并设置分页数据的总记录数
				hql.delete(0, hql.length());
				hql.append("select count(*) from ").append(className.getSimpleName()).append(" a ").append(wheleSql == null ? " " : " where " + wheleSql);
				hql.append(" AND NOT EXISTS(SELECT 1 FROM TInspectItemReport WHERE a.xequid=xequid AND a.id<id) ");
				hql.append(" order by id desc");
				//System.out.println(hql.toString());
				query = getHibernaSession().createQuery(hql.toString());
				setQueryParams(query, queryParams);
				queryResult.setTotalRecord((Long)query.uniqueResult());
				return queryResult;
			}
			/**
			 * 判断是否是总公司或者总管理员的
			 * 如果是，则不能删除
			 * true 表示其中至少有一个是总公司或者总管理员  ，不能删除        false 表示不是总公司或者总管理员，可以删除
			 * 
			 * entid 表示操作人所在公司
			 * 
			 * 
			 */
			public <T> boolean  isadmin(Class<T> paramClass, String ids,int entid){
				boolean flag=false;
				if(!StringUtils.isEmpty(ids)){
					for(String id : ids.split(",")){
						T t=this.getEntityById(paramClass, Integer.parseInt(id));
						if(((BaseModel) t).getEntid()==0&&entid!=0){
							flag=true;
							break;
						}
					}
				}
			return flag;
			}
}
