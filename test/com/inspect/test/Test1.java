package com.inspect.test;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import org.junit.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.inspect.AbstractBaseTransactionalSpringContextTests;
import com.inspect.dao.BaseDaoI;
import com.inspect.model.basis.TPoint;
import com.inspect.model.basis.TPointEquipment;
import com.inspect.model.system.TSUser;
import com.inspect.service.InspectItemServiceI;
import com.inspect.service.ProblemServiceI;
import com.inspect.service.SummaryServiceI;
import com.inspect.util.common.StringUtils;
import com.inspect.vo.basis.PointVo;

public class Test1  extends AbstractBaseTransactionalSpringContextTests {

	@Autowired
	private BaseDaoI baseDao;
	
	@Resource
	private SummaryServiceI summaryService;
	
	@Resource
	private ProblemServiceI problemService;
	@Resource
	private InspectItemServiceI inspectItemService;
	
	public List<PointVo> getPointList(String region,String poname, String eids,String qtype,int entid) {
		StringBuffer hql=new StringBuffer("from TPoint ");
		if(entid!=0){
			hql.append("where entid in(0,"+entid+")");
		}
		else{
			hql.append("where 1=1 ");
		}
		if(qtype.equals("u")){//修改
			if(eids!="" && !eids.equals("")){
				hql.append(" and id not in ("+eids +" )");
			}
		}
		if(StringUtils.isNotEmpty(poname)){
			hql.append(" and poname like '%"+poname.trim()+"%'");
		}
		hql.append("order by id asc");
		List<TPoint> pList=baseDao.findByHqlAll(TPoint.class, hql.toString());
		List<PointVo> pVoList=new ArrayList<PointVo>();
		//巡检点集合
		if(StringUtils.isNotEmpty(region)){
			if(pList!=null&&pList.size()>0){
				for(int i=0;i<pList.size();i++){
					Set<TPointEquipment> pe=pList.get(i).getPointequipments();
					Iterator iter=pe.iterator();
					//找出符合区县条件巡检点
					//之后判断一下巡检点下面没有设备时，是否报错
					while(iter.hasNext()){
						TPointEquipment pe1=(TPointEquipment) iter.next();
						//在巡检点下Equipment中存在对应区县，则将此巡检点加入，然后查询下一个巡检点；
						if(region.equals(pe1.getTequipment().getEregion())){
							PointVo pVo=new PointVo();
							BeanUtils.copyProperties(pList.get(i), pVo);
							pVoList.add(pVo);
							break;
						}
					}
				}
			}
		}
		else{
			if(pList!=null&&pList.size()>0){
				for(int i=0;i<pList.size();i++){
					Set<TPointEquipment> pe=pList.get(i).getPointequipments();
					Iterator iter=pe.iterator();
					//找出符合区县条件巡检点
					//之后判断一下巡检点下面没有设备时，是否报错
					while(iter.hasNext()){
						TPointEquipment pe1=(TPointEquipment) iter.next();
							PointVo pVo=new PointVo();
							BeanUtils.copyProperties(pList.get(i), pVo);
							pVoList.add(pVo);
							break;
					}
				}
			}
		}
	System.out.println(pVoList.size());
		return pVoList;
	}
	@Test
	public  void getregion (){
		String hql=" from  TPoint a where  a.id=a.pointequipments.pointid ";
		try {
			List l=baseDao.find(hql);
			System.out.println("ddddd"+l.size());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	
	}
}
