package com.inspect.util.system;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.Type;


public class CriteriaQuery implements Serializable {
	private static final long serialVersionUID = 1;
	  private int curPage;
	  private int pageSize;
	  private CriterionList criterionList;
	  private int isUseimage;
	  private DetachedCriteria detachedCriteria;
	  private static Map<String, Object> map;
	  private static Map<String, Object> ordermap;
	  private boolean flag;
	  private String field;
	  private Class entityClass;
	  private List reaults;
	  private int total;

	  public CriteriaQuery(){
	    this.curPage = 1;
	    this.pageSize = 10;
	    this.criterionList = new CriterionList();
	    this.isUseimage = 0;
	    this.flag = true;
	    this.field = "";
	  }

	  public List getReaults()
	  {
	    return this.reaults;
	  }

	  public void setReaults(List reaults)
	  {
	    this.reaults = reaults;
	  }

	  public int getTotal()
	  {
	    return this.total;
	  }

	  public void setTotal(int total)
	  {
	    this.total = total;
	  }
	  public Class getEntityClass()
	  {
	    return this.entityClass;
	  }

	  public void setEntityClass(Class entityClass)
	  {
	    this.entityClass = entityClass;
	  }

	  public CriteriaQuery(Class c){
	    this.curPage = 1;
	    this.pageSize = 10;
	    this.criterionList = new CriterionList();
	    this.isUseimage = 0;
	    this.flag = true;
	    this.field = "";
	    this.detachedCriteria = DetachedCriteria.forClass(c);
	    map = new HashMap();
	    ordermap = new HashMap();
	  }

	  public void add(Criterion c)
	  {
	    this.detachedCriteria.add(c);
	  }

	  public void add(){
	    for (int i = 0; i < getCriterionList().size(); ++i)
	      add(getCriterionList().getParas(i));
	  }
	  public void createCriteria(String name)
	  {
	    this.detachedCriteria.createCriteria(name);
	  }

	  public void createCriteria(String name, String value)
	  {
	    this.detachedCriteria.createCriteria(name, value);
	  }

	  public void createAlias(String name, String value)
	  {
	    this.detachedCriteria.createAlias(name, value);
	  }

	  public void setResultTransformer(Class class1)
	  {
	    this.detachedCriteria.setResultTransformer(Transformers.aliasToBean(class1));
	  }

	  public void setProjection(Property property)
	  {
	    this.detachedCriteria.setProjection(property);
	  }

	  public Criterion and(CriteriaQuery query, int source, int dest)
	  {
	    return Restrictions.and(query.getCriterionList().getParas(source), query.getCriterionList().getParas(dest));
	  }

	  public Criterion and(Criterion c, CriteriaQuery query, int souce)
	  {
	    return Restrictions.and(c, query.getCriterionList().getParas(souce));
	  }

	  public Criterion getOrCriterion(CriterionList list)
	  {
	    Criterion c1 = null;
	    Criterion c2 = null;
	    Criterion c3 = null;
	    c1 = list.getParas(0);
	    for (int i = 1; i < list.size(); ++i)
	    {
	      c2 = list.getParas(i);
	      c3 = getor(c1, c2);
	      c1 = c3;
	    }
	    return c3;
	  }

	  public Criterion getor(Criterion c1, Criterion c2)
	  {
	    return Restrictions.or(c1, c2);
	  }

	  public Criterion and(Criterion c1, Criterion c2)
	  {
	    return Restrictions.and(c1, c2);
	  }

	  public Criterion or(CriteriaQuery query, int source, int dest)
	  {
	    return Restrictions.or(query.getCriterionList().getParas(source), query.getCriterionList().getParas(dest));
	  }

	  public Criterion or(Criterion c, CriteriaQuery query, int source)
	  {
	    return Restrictions.or(c, query.getCriterionList().getParas(source));
	  }

	  public void or(Criterion c1, Criterion c2)
	  {
	    this.detachedCriteria.add(Restrictions.or(c1, c2));
	  }


	  public static Map<String, Object> getOrdermap()
	  {
	    return ordermap;
	  }

	  public static void setOrdermap(Map<String, Object> ordermap)
	  {
	    ordermap = ordermap;
	  }

	  public void eq(String keyname, Object keyvalue){
	    if ((keyvalue != null) && (keyvalue != "")){
	      this.criterionList.addPara(Restrictions.eq(keyname, keyvalue));
	      if (this.flag)
	        put(keyname, keyvalue);
	      this.flag = true;
	    }
	  }

	  public void notEq(String keyname, Object keyvalue)
	  {
	    if ((keyvalue != null) && (keyvalue != ""))
	    {
	      this.criterionList.addPara(Restrictions.ne(keyname, keyvalue));
	      if (this.flag)
	        put(keyname, keyvalue);
	      this.flag = true;
	    }
	  }

	  public void like(String keyname, Object keyvalue)
	  {
	    if ((keyvalue != null) && (keyvalue != ""))
	    {
	      this.criterionList.addPara(Restrictions.like(keyname, "%" + keyvalue + "%"));
	      if (this.flag)
	        put(keyname, keyvalue);
	      this.flag = true;
	    }
	  }

	  public void gt(String keyname, Object keyvalue)
	  {
	    if ((keyvalue != null) && (keyvalue != ""))
	    {
	      this.criterionList.addPara(Restrictions.gt(keyname, keyvalue));
	      if (this.flag)
	        put(keyname, keyvalue);
	      this.flag = true;
	    }
	  }

	  public void lt(String keyname, Object keyvalue)
	  {
	    if ((keyvalue != null) && (keyvalue != ""))
	    {
	      this.criterionList.addPara(Restrictions.lt(keyname, keyvalue));
	      if (this.flag)
	        put(keyname, keyvalue);
	      this.flag = true;
	    }
	  }

	  public void le(String keyname, Object keyvalue)
	  {
	    if ((keyvalue != null) && (keyvalue != ""))
	    {
	      this.criterionList.addPara(Restrictions.le(keyname, keyvalue));
	      if (this.flag)
	        put(keyname, keyvalue);
	      this.flag = true;
	    }
	  }

	  public void ge(String keyname, Object keyvalue)
	  {
	    if ((keyvalue != null) && (keyvalue != ""))
	    {
	      this.criterionList.addPara(Restrictions.ge(keyname, keyvalue));
	      if (this.flag)
	        put(keyname, keyvalue);
	      this.flag = true;
	    }
	  }

	  public void in(String keyname, Object[] keyvalue)
	  {
	    if ((keyvalue != null) && (keyvalue[0] != ""))
	      this.criterionList.addPara(Restrictions.in(keyname, keyvalue));
	  }

	  public void isNull(String keyname)
	  {
	    this.criterionList.addPara(Restrictions.isNull(keyname));
	  }

	  public void isNotNull(String keyname)
	  {
	    this.criterionList.addPara(Restrictions.isNotNull(keyname));
	  }

	  public void put(String keyname, Object keyvalue)
	  {
	    if ((keyvalue != null) && (keyvalue != ""))
	      map.put(keyname, keyvalue);
	  }

	  public void between(String keyname, Object keyvalue1, Object keyvalue2)
	  {
	    Criterion c = null;
	    if ((!(keyvalue1.equals(null))) && (!(keyvalue2.equals(null))))
	      c = Restrictions.between(keyname, keyvalue1, keyvalue2);
	    else if (!(keyvalue1.equals(null)))
	      c = Restrictions.ge(keyname, keyvalue1);
	    else if (!(keyvalue2.equals(null)))
	      c = Restrictions.le(keyname, keyvalue2);
	    this.criterionList.add(c);
	  }

	  public void sql(String sql)
	  {
	    Restrictions.sqlRestriction(sql);
	  }

	  public void sql(String sql, Object[] objects, Type[] type)
	  {
	    Restrictions.sqlRestriction(sql, objects, type);
	  }

	  public void sql(String sql, Object objects, Type type)
	  {
	    Restrictions.sqlRestriction(sql, objects, type);
	  }

	  public Integer getCurPage()
	  {
	    return Integer.valueOf(this.curPage);
	  }

	  public void setCurPage(Integer curPage)
	  {
	    this.curPage = curPage.intValue();
	  }

	  public int getPageSize()
	  {
	    return this.pageSize;
	  }

	  public void setPageSize(int pageSize)
	  {
	    this.pageSize = pageSize;
	  }

	  public CriterionList getCriterionList()
	  {
	    return this.criterionList;
	  }

	  public void setCriterionList(CriterionList criterionList)
	  {
	    this.criterionList = criterionList;
	  }

	  public DetachedCriteria getDetachedCriteria()
	  {
	    return this.detachedCriteria;
	  }

	  public String getField()
	  {
	    return this.field;
	  }

	  public void setField(String field)
	  {
	    this.field = field;
	  }

	  public void setDetachedCriteria(DetachedCriteria detachedCriteria)
	  {
	    this.detachedCriteria = detachedCriteria;
	  }

	  public int getIsUseimage()
	  {
	    return this.isUseimage;
	  }

	  public void setIsUseimage(int isUseimage)
	  {
	    this.isUseimage = isUseimage;
	  }

	  public Map<String, Object> getMap()
	  {
	    return map;
	  }

	  public void setMap(Map<String, Object> map)
	  {
	    map = map;
	  }

	  public boolean isFlag()
	  {
	    return this.flag;
	  }

	  public void setFlag(boolean flag)
	  {
	    this.flag = flag;
	  }
	  
	  @SuppressWarnings("unchecked")
	public void setOrder(Map<String, Object> map)
	  {
	    Iterator localIterator = map.entrySet().iterator();
	    while (localIterator.hasNext())
	    {
	      Map.Entry entry = (Map.Entry)localIterator.next();
	      if ("asc".equals(entry.getValue()))
	        this.detachedCriteria.addOrder(Order.asc((String)entry.getKey()));
	      else
	        this.detachedCriteria.addOrder(Order.desc((String)entry.getKey()));
	    }
	  }
	}

