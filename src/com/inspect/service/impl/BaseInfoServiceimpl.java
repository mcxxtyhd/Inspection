package com.inspect.service.impl;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.inspect.dao.BaseDaoI;
import com.inspect.model.baseinfo.TBaseInfo;
import com.inspect.model.baseinfo.TBaseInfoEquipment;
import com.inspect.model.basis.TEquipment;
import com.inspect.model.basis.TEquipmentProjectGroup;
import com.inspect.service.BaseInfoServiceI;
import com.inspect.util.common.QueryResult;
import com.inspect.util.common.StringUtils;
import com.inspect.vo.basis.TBaseInfoVo;
import com.inspect.vo.monitor.EquipLocateMap;
import com.inspect.vo.monitor.SearchLocateMap;


@Service("baseInfoService")
@Transactional(propagation = Propagation.REQUIRED,readOnly = false,rollbackFor = Exception.class)
//这个是Spring 的标注，是用标注定义来定义 Bean
//@Service 就相当于定义 bean, 并自动根据 bean 的类名生成一个首字母小写的 bean?
//在service类前加上@Transactional，声明这个service所有方法需要事务管理。每一个业务方法开始时都会打开一个事务。 
public class BaseInfoServiceimpl  implements BaseInfoServiceI{
	@Resource
	private BaseDaoI baseDao;    
	///d
	@Override
	public <T> T getEntityById(Class<T> paramClass, Serializable id) {
		return baseDao.getEntityById(paramClass, id);
	}
	
	@Override
	public void addBaseInfo(TBaseInfoVo baseInfoVo) {
		TBaseInfo bInfo=new TBaseInfo();
		BeanUtils.copyProperties(baseInfoVo,bInfo );
		baseDao.save(bInfo);
	}

	@Override
	public void editBaseInfo(TBaseInfoVo baseInfoVo) {
		TBaseInfo bInfo=baseDao.get(TBaseInfo.class, baseInfoVo.getId());
		BeanUtils.copyProperties(baseInfoVo, bInfo,new String[]{"id","entid","btype"});
		baseDao.update(bInfo);
		//修改关联设备信息
		Set<TBaseInfoEquipment> tbaseequipments=bInfo.getBaseinfoequipments();
		if(tbaseequipments!=null && tbaseequipments.size()>0){
			for(TBaseInfoEquipment tbe:tbaseequipments){
				TEquipment tequipmet=tbe.getTequipment();
				if(tequipmet!=null){
					tequipmet.setEnumber(bInfo.getBnumber());//设备编号
					tequipmet.setEname(bInfo.getBname());//设备名称
					tequipmet.setEposx(bInfo.getBposx());//经度
					tequipmet.setEposy(bInfo.getBposy());//纬度
					tequipmet.setEaddress(bInfo.getBaddress());//设备地址
					tequipmet.setEtype(bInfo.getBtype());//设备类型
					tequipmet.setEregion(bInfo.getBregion());//区县
					tequipmet.setEcity(bInfo.getBcity());//城市
					baseDao.update(tequipmet);
				}
			}
		}
	}

	@Override
	public Map<String, Object> findBaseInfoDatagrid(TBaseInfoVo baseInfoVo,int page, int rows, String qsql) {
		StringBuffer buf=new StringBuffer(qsql);
		Map<String, Object> map = new HashMap<String, Object>();
		if(!StringUtils.isEmpty(baseInfoVo.getBtype())){
			buf.append(" and btype ='").append(baseInfoVo.getBtype()).append("'");
		}
		if(baseInfoVo.getEntid()!=0){
			buf.append(" and entid=").append(baseInfoVo.getEntid());
		}
		if(!StringUtils.isEmpty(baseInfoVo.getBnumber())){
			buf.append(" and bnumber like '%").append(baseInfoVo.getBnumber().trim()).append("%'");
		}
		if(!StringUtils.isEmpty(baseInfoVo.getBname())){
			buf.append(" and bname like '%").append(baseInfoVo.getBname().trim()).append("%'");
		}
		if(!StringUtils.isEmpty(baseInfoVo.getBcity())){
			buf.append(" and bcity like '%").append(baseInfoVo.getBcity().trim()).append("%'");
		}
		if(!StringUtils.isEmpty(baseInfoVo.getBregion())){
			buf.append(" and bregion like '%").append(baseInfoVo.getBregion().trim()).append("%'");
		}
		if(!StringUtils.isEmpty(baseInfoVo.getBaddress())){
			buf.append(" and baddress like '%").append(baseInfoVo.getBaddress().trim()).append("%'");
		}
		buf.append(" order by id desc");
		QueryResult<TBaseInfo> queryResult = baseDao.getQueryResult(TBaseInfo.class, buf.toString(), (page - 1) * rows, rows,null, null);
		List<TBaseInfoVo> bInfoVoList=new ArrayList<TBaseInfoVo>();
		if (queryResult != null && queryResult.getResultList().size() > 0) {
			for (TBaseInfo bInfo : queryResult.getResultList()) {
				TBaseInfoVo bInfoVo = new TBaseInfoVo();
				//bInfo.setBaddress(bInfo.getBaddress().trim());
				//bInfo.setBname(bInfo.getBname().trim());
				BeanUtils.copyProperties(bInfo,bInfoVo);
				bInfoVoList.add(bInfoVo);
			}
		}
		map.put("total", queryResult.getTotalRecord());
		map.put("rows", bInfoVoList);
		return map;
	}

	@Override
	public void removeBaseInfo(String ids) {
		for(String id : ids.split(",")) {
			TBaseInfo tbf = baseDao.getEntityById(TBaseInfo.class, Integer.parseInt(id.trim()));
			Set<TBaseInfoEquipment> tbaseequipments = tbf.getBaseinfoequipments();
			if (tbaseequipments != null && tbaseequipments.size() > 0) {
				for (TBaseInfoEquipment tbe : tbaseequipments) {
					TEquipment tequipmet = tbe.getTequipment();
					try {
						if (tequipmet != null) {
							baseDao.executeHql("delete from TEquipmentProjectGroup t where t.tequipment = ?", new Object[] { tequipmet });
							baseDao.delete(TEquipment.class, tequipmet.getId());
						}
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					
				}
			}
			baseDao.delete(TBaseInfo.class,Integer.parseInt(id));
		}
	}

	@Override
	public Long isExistBaseInfo(String bname,int entid) {
		return baseDao.count("select count(*) from TBaseInfo t where t.bnumber = ? and t.entid=?", new Object[] { bname,entid });
	}

	public String saveListHibenate(List<Object> db,int entid,int flag){
		
		long s=System.currentTimeMillis();

		int count1=0;
		  for(int i=0;i<db.size();i++){
			  TBaseInfo bInfo= (TBaseInfo) db.get(i);
			  bInfo.setEntid(entid);
			  String hql="";
			  //flag为2时为室内，1为铁塔
			  if(flag==2){
				  bInfo.setBtype("2");
			  }
			  else{
				  bInfo.setBtype("1");
			  }
				  hql="select count(*) from TBaseInfo t where t.bnumber ='"+bInfo.getBnumber()+"'  and t.entid ="+entid+")";
			  
			  Long count=baseDao.count(hql);
			  if(count==0){
				  baseDao.save(db.get(i));
				  count1++;
			  }
		  }
			long e=System.currentTimeMillis();

		//	System.out.println("timespan: " + (e-s));
			
		  return String.valueOf(count1);
	}

	public String saveList(List<Object> db,int entid,int flag){
		
		long s=System.currentTimeMillis();
		
 	        String connectStr = "jdbc:mysql://localhost:3306/inspect";
	       //123.232.122.73  10.196.62.17 
		//String connectStr = "jdbc:mysql://123.232.122.73:3306/inspect?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&zeroDateTimeBehavior=convertToNull";
		
		//公司内网服务器
		//String connectStr = "jdbc:mysql://10.196.62.17:3306/inspect?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&zeroDateTimeBehavior=convertToNull";
	    
		//本地
		//String connectStr = "jdbc:mysql://localhost:3306/inspect?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&zeroDateTimeBehavior=convertToNull";
	        // connectStr += "?useServerPrepStmts=false&rewriteBatchedStatements=true";
	        String insert_sql = "insert ignore into t_base_info(entid,baddress,bdesc,beqcount,bfactory,blevel,bname,bnumber,bposx,bposy,bregion,btower,btype,btowertype,bcity,bwlnumber,bheight) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	        String charset = "utf-8";
	        Boolean debug = true;
	        String username = "root";
	        String password = "123";
	        Connection conn=null;
	        PreparedStatement prest=null;
		  try {  
		      Class.forName("com.mysql.jdbc.Driver");  
		       conn = DriverManager.getConnection(connectStr, username,password);
		      conn.setAutoCommit(false);  
		       prest = conn.prepareStatement(insert_sql,ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);  
		      for(int x = 0; x < db.size(); x++){  
				  TBaseInfo bInfo= (TBaseInfo) db.get(x);
				  
				  if(flag==2){
					  bInfo.setBtype("2");
				  }
				  else{
					  bInfo.setBtype("1");
				  }
				  
				  prest.setInt(1,entid);
				  prest.setString(2,bInfo.getBaddress());
				  prest.setString(3, bInfo.getBdesc());
				  prest.setString(4, bInfo.getBeqcount());
				  prest.setString(5, bInfo.getBfactory());
				  prest.setString(6, bInfo.getBlevel());
				  prest.setString(7, bInfo.getBname());
				  prest.setString(8, bInfo.getBnumber());
				  prest.setDouble(9, bInfo.getBposx());				  
				  prest.setDouble(10, bInfo.getBposy());
				  prest.setString(11, bInfo.getBregion());				  
				  prest.setString(12, bInfo.getBtower());				  
				  prest.setString(13, bInfo.getBtype());				  
				  prest.setString(14, bInfo.getBtowertype());	
				  prest.setString(15, bInfo.getBcity());	
				  prest.setString(16, bInfo.getBwlnumber());	
				  prest.setDouble(17, bInfo.getBheight());	
				  
				  
		          prest.addBatch();  
		      }  
		      prest.executeBatch();  
		     // conn.commit(); 
		      //conn.close();  
		      
		} catch (SQLException ex) {  
			ex.printStackTrace();
		   //Logger.getLogger(MyLogger.class.getName()).log(Level.SEVERE, null, ex);  
		} catch (ClassNotFoundException ex) {  
		     //Logger.getLogger(MyLogger.class.getName()).log(Level.SEVERE, null, ex);
			ex.printStackTrace();
		}finally{
			if(conn!=null){
		        try{    
		        	conn.commit(); 
		        	conn.close() ;    
		        }catch(SQLException e){    
		            e.printStackTrace() ;    
		        } 
			}
			if(prest!=null){
			  try{    
				  prest.close() ;    
		        }catch(SQLException e){    
		            e.printStackTrace() ;    
		        } 
			}
			
		}
		
		long e=System.currentTimeMillis();

	//	System.out.println("timespan: " + (e-s));
		return "" + db.size();
	}
	@Override
	public int addBaseInfoList(List<TBaseInfo> bInfoList) {
		int remark=0;
		try {
			if(bInfoList!=null&&bInfoList.size()>0){
				for(TBaseInfo bInfo:bInfoList){
					baseDao.save(bInfo);
					remark++;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return remark;
	
		
	}

	@Override
	public List<EquipLocateMap> getLineLocateMap(SearchLocateMap smap) {
				StringBuffer buf=new StringBuffer(" 1=1 ");
				
		if(!StringUtils.isNotEmpty(smap.getRpEquipRegion())){
					buf.append(" and 1=0 ");
		}
		if(StringUtils.isNotEmpty(smap.getRpEquipCity())){
					buf.append(" and bcity ='").append(smap.getRpEquipCity()).append("'");
		}
		if(StringUtils.isNotEmpty(smap.getRpEquipRegion())){
			buf.append(" and bregion ='").append(smap.getRpEquipRegion()).append("'");
		}
		if(StringUtils.isNotEmpty(smap.getRpEquipAddress())){
			buf.append(" and baddress like '%").append(smap.getRpEquipAddress().trim()).append("%'");
		}
		if(StringUtils.isNotEmpty(smap.getRpEquipName())){
			buf.append(" and bname like '%").append(smap.getRpEquipName().trim()).append("%'");
		}
		if(StringUtils.isNotEmpty(smap.getBtype())){
			buf.append(" and btype ='").append(smap.getBtype()).append("'");
		}
		if(smap.getEntId()!=0){
			buf.append(" and entid in(0,").append(smap.getEntId()).append(")");
		}
		String hql="from TBaseInfo where"+buf.toString();
		List<TBaseInfo>bsList =baseDao.find(hql);
		 List<EquipLocateMap> mapList=new ArrayList<EquipLocateMap>();
		if(bsList!=null&&bsList.size()>0){
			for(TBaseInfo binfo:bsList){
				EquipLocateMap map=new EquipLocateMap();
				map.setRpEquipCity(binfo.getBcity());
				map.setRpEquipRegion(binfo.getBregion());
				map.setRpEquipName(binfo.getBname());
				map.setRpEquipNumber(binfo.getBnumber());
				map.setRpEquipX(String.valueOf(binfo.getBposx()));
				map.setRpEquipY(String.valueOf(binfo.getBposy()));
				map.setRpEquipAddress(binfo.getBaddress());
				map.setRptype(binfo.getBtype());
				map.setId(binfo.getId());
				mapList.add(map);
			}
		}
	
		return mapList;
	}

	@Override
	public List<TBaseInfoVo> getRegionByCity(int entid, String bcity) {
		StringBuffer buf=new StringBuffer("from TBaseInfo ");
		if(entid!=0){
			buf.append("where entid in(0,"+entid+")");
		}
		else{
			buf.append(" where 1=1 ");
		}
		buf.append(" and bcity='"+bcity+"' group by bregion");
		List<TBaseInfo>  list=baseDao.find(buf.toString());
		List<TBaseInfoVo> bvolist =new ArrayList<TBaseInfoVo>();
		if(list!=null&&list.size()>0){
			for(TBaseInfo info:list){
				TBaseInfoVo bvo=new TBaseInfoVo();
				BeanUtils.copyProperties(info, bvo);
				bvolist.add(bvo);
			}
			
		}
		return bvolist;
	}

	@Override
	public TBaseInfo getBaseInfoByBnum(String bnum, int entid) {
			String hql="from TBaseInfo  ir where  ir.bnumber='"+bnum+"' and ir.entid="+entid;
			List<TBaseInfo> binfoList=baseDao.find(hql);
			if(binfoList!=null&&binfoList.size()>0){
				TBaseInfo binfo=new TBaseInfo();
				binfo=binfoList.get(0);
				return binfo;
			}
			return null;
	}
}
