
package com.inspect;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.transaction.support.TransactionSynchronizationManager;


/**
 * 
 * 可以作为所有测试类的Transactional基类，免去所有项目重复书写 note: 需要遵循这里的命名
 * 
 */
@ContextConfiguration(locations = {
		"/resources/spring.xml",
		"/resources/spring-hibernate.xml"})
public abstract class AbstractBaseTransactionalSpringContextTests extends	AbstractJUnit4SpringContextTests {

	@Autowired
	protected SessionFactory sessionFactory;

	private boolean singleSessionRequired = false;

	/**
	 * 当前事务管理器是否已有session绑定
	 */
	private boolean participated = true;

	@Before
	public void beforeTest() {
		SpringLocator.setApplicationContext(applicationContext);
		participated = true;
		if (singleSessionRequired) {
			if (!TransactionSynchronizationManager.hasResource(sessionFactory)) {
				participated = false;
				Session session = SessionFactoryUtils.getSession(
						sessionFactory, true);
				TransactionSynchronizationManager.bindResource(sessionFactory,
						new SessionHolder(session));
			}
		}
	}

	@After
	public void afterTest() {
		if (singleSessionRequired && !participated) {
			SessionHolder sessionHolder = (SessionHolder) TransactionSynchronizationManager
					.unbindResource(sessionFactory);
			SessionFactoryUtils.closeSession(sessionHolder.getSession());
		}
	}

	/**
	 * @param singleSessionRequired
	 *            the singleSessionRequired to set
	 */
	public void setSingleSessionRequired(boolean singleSessionRequired) {
		this.singleSessionRequired = singleSessionRequired;
	}

}
