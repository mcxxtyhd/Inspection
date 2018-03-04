package com.inspect.test;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.inspect.AbstractBaseTransactionalSpringContextTests;
import com.inspect.dao.BaseDaoI;
import com.inspect.model.baseinfo.TBaseInfo;
import com.inspect.model.baseinfo.TBaseInfoEquipment;
import com.inspect.model.basis.TEquipment;
import com.inspect.model.monitor.TInspectItemDetailReport;
import com.inspect.model.monitor.TInspectItemReport;
import com.inspect.service.InspectItemServiceI;
import com.inspect.service.ProblemServiceI;
import com.inspect.service.SummaryServiceI;
import com.inspect.vo.problem.ProblemVo;

public class messageEquipidToBaseid extends
		AbstractBaseTransactionalSpringContextTests {

	@Autowired
	private BaseDaoI baseDao;

	@Resource
	private SummaryServiceI summaryService;

	@Resource
	private ProblemServiceI problemService;
	@Resource
	private InspectItemServiceI inspectItemService;

	/*
	 * private void amin() { // TODO Auto-generated method stub
	 * 
	 * TEquipment equipment=pointEquipment.getTequipment(); int baseid=0;
	 * if(equipment!=null){ Set<TBaseInfoEquipment> eSet=
	 * equipment.getBaseinfoequipments();
	 * 
	 * Iterator<TBaseInfoEquipment> beset=eSet.iterator(); if(beset.hasNext()){
	 * TBaseInfoEquipment be=(TBaseInfoEquipment)beset.next(); //将t_base_info装进来
	 * baseid=be.getTbaseinfo().getId(); //equipment.setId(equipid); } } }
	 */

	/**
	 * 巡检问题导出
	 */
//	@Test
	public void eidtobaseid() {

		List<TInspectItemReport> itemList = baseDao
				.find(" from TInspectItemReport");
		if (itemList != null && itemList.size() > 0) {
			for (TInspectItemReport item : itemList) {
				int eid = item.getXequid();
				TEquipment equip = (TEquipment) baseDao
						.find1(" from TEquipment where id=" + eid +" and entid="+item.getEntid());
				int baseid = 0;
				System.out.println(eid);
				if (equip != null) {
					Set<TBaseInfoEquipment> eSet = equip
							.getBaseinfoequipments();

					Iterator<TBaseInfoEquipment> beset = eSet.iterator();
					if (beset.hasNext()) {
						TBaseInfoEquipment be = (TBaseInfoEquipment) beset
								.next();
						// 将t_base_info装进来
						baseid = be.getTbaseinfo().getId();
						System.out.println(baseid);
						item.setXequid(baseid);
						baseDao.save(item);
					}
				}
			}
		}
	}
	

	/**
	 * 测试实体类
	 */
//	@Test
	public void testEntity() {
				TEquipment equip = (TEquipment) baseDao
						.find1(" from TEquipment where id=" + 109);
				System.out.println(equip.getEcity());
				equip.setEcity("潍坊啊哈哈");
			
			}

	/**
	 * 修改巡检值
	 */
	//@Test
	public void modifyProject() {
		String xequtnum="4413";
		int entid=11;
		String proname;
		String basename="F开发区附属医院病房楼";
		String hql="";
		
		if(entid==0){
			hql="from TBaseInfo  ir where  ir.bname like'%"+basename.trim()+"%'";
		}
		else{
			hql="from TBaseInfo  ir where  ir.bname like'%"+basename.trim()+"%' and ir.entid="+entid;
		}
		
		StringBuffer buf=new StringBuffer();
		List<TBaseInfo> tEquipmenlist=baseDao.find(hql);
		int i=1;
		//设备表主键id集合
		if(tEquipmenlist!=null&&tEquipmenlist.size()>0){
			for(TBaseInfo equipment:tEquipmenlist){
				if(i!=tEquipmenlist.size()){
				buf.append(equipment.getId()).append(",");
				}
				else{
					buf.append(equipment.getId());
				}
				i++;
			}
			
		}
		System.out.println(buf.toString());
	/*	List<TInspectItemReport> itemList = baseDao
		.find(" from TInspectItemReport where entid=11 and xequid=97");
		if (itemList != null && itemList.size() > 0) {
			for (TInspectItemReport item : itemList) {
				
				
			}
		}*/	
					
	}
		
	
}