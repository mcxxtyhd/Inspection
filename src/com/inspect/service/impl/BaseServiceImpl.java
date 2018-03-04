package com.inspect.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.inspect.dao.BaseDaoI;
import com.inspect.model.system.Parameter;
import com.inspect.model.system.TLog;
import com.inspect.service.BaseServiceI;
import com.inspect.util.common.QueryResult;
import com.inspect.util.common.StringUtils;
import com.inspect.vo.system.PageLog;
import com.inspect.vo.system.ParameterVo;

@Service("baseService")
@Transactional(propagation = Propagation.REQUIRED,readOnly = false,rollbackFor = Exception.class)
public class BaseServiceImpl implements BaseServiceI {
	
	@Resource
	private BaseDaoI baseDao;

	public BaseDaoI getBaseDao() {
		return baseDao;
	}

	public void setBaseDao(BaseDaoI baseDao) {
		this.baseDao = baseDao;
	}

	@Override
	public void addParameter(ParameterVo parametervo) {
		Parameter parameter=new Parameter();
	    BeanUtils.copyProperties(parametervo,parameter);
	    parameter.setEntid(parametervo.getEntid());
	    baseDao.save(parameter);

	}

	public void editParameter(ParameterVo paramertervo) {
		Parameter parameter=baseDao.get(Parameter.class,paramertervo.getId());
	    BeanUtils.copyProperties(paramertervo,parameter,new String[]{"id","entid"});
	    baseDao.save(parameter);
	}
	
	@Override
	public  Parameter getParameter(String id) {
		if(StringUtils.isEmpty(String.valueOf(id))){
			return null;
		}
		return baseDao.get(Parameter.class,id);
	}

	@Override
	public void removeParameter(String ids) {
		if(!StringUtils.isEmpty(ids)){
			for(String id : ids.split(",")){
				baseDao.delete(Parameter.class,Integer.parseInt(id));
			}
		}
	}

	@Override
	public Map<String, Object> findParameterDatagrid(ParameterVo paramertervo, int page,int rows,String qsql) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer buf = new StringBuffer(qsql);
		if (!StringUtils.isEmpty(paramertervo.getPname())) {
			buf.append(" and pname like '%").append(paramertervo.getPname()).append("%'");
		}
		if (!StringUtils.isEmpty(paramertervo.getPvalue())) {
			buf.append(" and pvalue like '%").append(paramertervo.getPvalue()).append("%'");
		}
		if (paramertervo.getEntid()!=0) {
			buf.append(" and entid=").append(paramertervo.getEntid());
		}
		buf.append(" order by id desc");
		QueryResult<Parameter> queryResult = baseDao.getQueryResult(Parameter.class, buf.toString(), (page - 1) * rows, rows,null, null);
		map.put("total", queryResult.getTotalRecord());
		map.put("rows", queryResult.getResultList());
		return map;
	}
	
	public Map<String, Object> findLogDatagrid(PageLog pageLog, int page,int rows,String qsql) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer buf = new StringBuffer(qsql);
		if (!StringUtils.isEmpty(pageLog.getOpuser())) {
			buf.append(" and opuser like '%").append(pageLog.getOpuser().trim()).append("%'");
		}
		if (!StringUtils.isEmpty(pageLog.getOpstatus())) {
			buf.append(" and opstatus=").append(pageLog.getOpstatus());
		}
		if (!StringUtils.isEmpty(pageLog.getStartdate())){
			buf.append(" and optime >='").append(pageLog.getStartdate()).append("'");
		}
		if (!StringUtils.isEmpty(pageLog.getEnddate())){
			buf.append(" and optime <='").append(pageLog.getEnddate()).append("'");
		}
		if (!StringUtils.isEmpty(pageLog.getOpevent())){
			buf.append(" and opevent like '%").append(pageLog.getOpevent().trim()).append("%'");
		}
		buf.append(" order by optime desc");
		QueryResult<TLog> queryResult = baseDao.getQueryResult(TLog.class, buf.toString(), (page - 1) * rows, rows,null, null);
		List<PageLog> pvolist=new ArrayList<PageLog>();
		if (queryResult != null && queryResult.getResultList().size() > 0) {
			for (TLog tlog : queryResult.getResultList()) {
				PageLog pvo = new PageLog();
				BeanUtils.copyProperties(tlog,pvo,new String[]{"opstatus"});
				pvo.setOpstatus(String.valueOf(tlog.getOpstatus()));
				pvolist.add(pvo);
			}
		}
		map.put("total", queryResult.getTotalRecord());
		map.put("rows", pvolist);
		return map;
	}
	
	/**
	 * 添加日志
	 */
	public void addLog(TLog log) {
       baseDao.save(log);
	}
	
	/**
	 * 根据参数名称查询参数列表
	 */
	public List<Parameter> getParameListByParameName(String hql,String pname){
		List<Parameter> list=new ArrayList<Parameter>();
		StringBuffer buf=new StringBuffer("from Parameter where ").append(hql);
		if(!StringUtils.isEmpty(pname)){
			buf.append(" and pname ='").append(pname).append("'");
		}
		List<Parameter> l=baseDao.findByHqlAll(Parameter.class, buf.toString());
		if(l!=null && l.size()>0){
			for(Parameter a:l){
				Parameter p=new Parameter();
				p.setPvalue(a.getPvalue());
				p.setPname(a.getPname());
				list.add(p);
			}
		}
		return list;
	}
	
	/**
	 * 根据参数类型查询参数列表
	 * @param ptype
	 * @return
	 */
	@Override
	public List<Parameter> getParameListByParameType(String hql,String ptype){
		List<Parameter> list=new ArrayList<Parameter>();
//		StringBuffer buf=new StringBuffer("from Parameter where ").append(hql);
		StringBuffer buf=new StringBuffer("select distinct(pname) from Parameter where ").append(hql);
		if(!StringUtils.isEmpty(ptype)){
			buf.append(" and ptype ='").append(ptype).append("'");
		}
		List<String> l=baseDao.find(buf.toString());
		if(l!=null && l.size()>0){
			for(String a:l){
				Parameter p=new Parameter();
//			    p.setPvalue(a.getPvalue());
				p.setPname(a);
				list.add(p);
			}
		}
		return list;
	}


}
