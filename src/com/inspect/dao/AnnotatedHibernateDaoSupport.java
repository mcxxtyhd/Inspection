package com.inspect.dao;

import javax.annotation.Resource;
import javax.sql.DataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;


public abstract class AnnotatedHibernateDaoSupport extends HibernateDaoSupport {
	
	protected JdbcTemplate jdbcTemplate;
	
	@Autowired
	public final void setSessionFactoryExtend(SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}
	
	@Resource(name = "dataSource")
	public void setDataSource(DataSource dataSource) {
		if (this.jdbcTemplate == null || dataSource != this.jdbcTemplate.getDataSource()) {
			this.jdbcTemplate = createJdbcTemplate(dataSource);
			initTemplateConfig();
		}
	}

	/**
	 * 
	 */
	protected void initTemplateConfig() {}

	/**
	 * @param dataSource
	 * @return
	 */
	protected JdbcTemplate createJdbcTemplate(DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}

	public void saveOrUpdate(Object entity) {
		return;
	}

}
