package com.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.inspect.dao.BaseDaoI;
import javax.annotation.Resource;

import org.apache.struts2.ServletActionContext;
import org.hibernate.SQLQuery;

import com.inspect.action.common.BaseAction;
import com.inspect.dao.BaseDaoI;
import com.inspect.service.InspectItemServiceI;
import com.inspect.vo.basis.UnicomVo;

public class DocDatagrid extends BaseAction{
	private static InspectItemServiceI inspectItemService ;
	private static BaseDaoI baseDao;
	public InspectItemServiceI getInspectItemService() {
		return inspectItemService;
	}

	@Resource
	public void setInspectItemService(InspectItemServiceI inspectItemService) {
		this.inspectItemService = inspectItemService;
	}
	public static Map<String, Object> findUnicomDatagrid(UnicomVo unicomvo, int page,
			int rows, String qsql) {
		Map<String, Object> map = new HashMap<String, Object>();

		StringBuffer buf = new StringBuffer();

			
		buf.append(" select  id,company,applyid,baseid,basename,baseaddress from unicominfo ");
		
		String sql=buf.toString()+ "LIMIT "+(page - 1) * rows+","+rows ;
		//System.out.println(buf.toString());
		SQLQuery pgquery = baseDao.getHibernaSession().createSQLQuery(sql);
		List<Object[]> lvolist = pgquery.list();

		List<UnicomVo> list = toUnicomVoObj(lvolist);
		System.out.println("inspectitemserviceimpl++++: "+ list.size());
		
		
		SQLQuery listsum = baseDao.getHibernaSession().createSQLQuery(buf.toString());
		
		System.out.println("listsum.list().size(): "+listsum.list().size());
		if(list==null){
			list=new ArrayList<UnicomVo>();
		}
		map.put("total", listsum.list().size());
		map.put("rows", list);
		return map;
	}
	
	
	
	public static void main(String args[]){
		System.out.println("dddddddd");
		UnicomVo unicomVo=new UnicomVo();
		
		System.out.println("docDatagrid()");
		int page =2;//Integer.parseInt(ServletActionContext.getRequest().getParameter("page") );
		int rows =2;//Integer.parseInt(ServletActionContext.getRequest().getParameter("rows") );
		Map<String, Object> map = DocDatagrid.findUnicomDatagrid(unicomVo,page,rows,"");
		
		
		System.out.println("100docactionUnicommap "+map);
		
		//writeJson(map);
	}
}
