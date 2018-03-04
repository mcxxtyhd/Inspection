package com.inspect.util;

import org.hibernate.Hibernate;
import org.hibernate.dialect.MySQL5Dialect;

public class InspectDialect  extends MySQL5Dialect {    
	    public InspectDialect() {    
	        super();    
	        registerHibernateType(-1, Hibernate.STRING.getName());    
	    }    
	} 