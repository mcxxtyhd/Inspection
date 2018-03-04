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
import com.inspect.model.basis.TNotice;
import com.inspect.service.VersionServiceI;
import com.inspect.util.common.DateUtils;
import com.inspect.util.common.QueryResult;
import com.inspect.util.common.StringUtils;
import com.inspect.vo.basis.VersionVo;
import com.inspect.model.basis.TVersion;
@Service("versionService")
@Transactional(propagation = Propagation.REQUIRED,readOnly = false,rollbackFor = Exception.class)
public class VersionServiceImpl implements VersionServiceI {
	
	@Resource
	private BaseDaoI baseDao;
	@Override
	public Map<String, Object> findNoticeDatagrid(VersionVo versionVo,
			int page, int rows, String qsql) {
		StringBuffer buf=new StringBuffer(qsql);
		Map<String, Object> map = new HashMap<String, Object>();
	
		if(!StringUtils.isEmpty(versionVo.getVnum())){
			buf.append(" and vnum like'%").append(versionVo.getVnum()).append("%'");
		}
		if(!StringUtils.isEmpty(versionVo.getVname())){
			buf.append(" and vname like'%").append(versionVo.getVname()).append("%'");
		}
		if(!StringUtils.isEmpty(versionVo.getVpublisher())){
			buf.append(" and vpublisher like'%").append(versionVo.getVpublisher()).append("%'");
		}
	/*	if(!StringUtils.isEmpty(versionVo.getVstartdate())){
			buf.append(" and vupdate >='").append(versionVo.getVstartdate()).append("'");
		}
		if(!StringUtils.isEmpty(versionVo.getVenddate())){
			buf.append(" and vupdate <='").append(versionVo.getVenddate()).append("'");
		}*/
		if(versionVo.getEntid()!=0){
			buf.append(" and entid=").append(versionVo.getEntid());
		}
		buf.append(" order by id desc");
		QueryResult<TVersion> queryResult = baseDao.getQueryResult(TVersion.class, buf.toString(), (page - 1) * rows, rows,null, null);
		List<VersionVo> vVoList=new ArrayList<VersionVo>();
		if (queryResult != null && queryResult.getResultList().size() > 0) {
			for (TVersion v : queryResult.getResultList()) {
				VersionVo vVo= new VersionVo();
				BeanUtils.copyProperties(v,vVo);
				vVoList.add(vVo);
			}
		}
		map.put("total", queryResult.getTotalRecord());
		map.put("rows", vVoList);
		return map;
	}
	@Override
	public void addVersion(VersionVo versionVo) {
		TVersion ver=new TVersion();
		BeanUtils.copyProperties(versionVo, ver);
		baseDao.save(ver);
	}
	@Override
	public void editVersion(VersionVo versionVo) {
		TVersion ver=(TVersion) baseDao.find1("from TVersion where id="+versionVo.getId());
		ver.setVupdate(versionVo.getVupdate());
		ver.setVpublisher(versionVo.getVpublisher());
		baseDao.save(ver);
		
	}
	@Override
	public void removeVersion(String ids) {
		for(String id:ids.split(",")){
			baseDao.delete(TVersion.class, Integer.parseInt(id));
		}
		
	}
	
	@Override
	public TVersion getVersion() {
	List<TVersion> vList= baseDao.find(" from TVersion order by id desc");
	TVersion ver = null;
	if(vList!=null&&vList.size()>0){
		ver=vList.get(0);
	}
	return ver;
	}
	@Override
	public boolean isExist(String vnum,int entid) {
		boolean flag=false;
		Long count=0L;
		if(entid!=0){
			 count=baseDao.count("select count(*) from TVersion where vnum='"+vnum+"' and entid="+entid);
		}
		else{
			count=baseDao.count("select count(*) from TVersion where vnum='"+vnum+"'");
		}
		if(count>0){
			flag=true;
		}
		return flag;
	}
	@Override
	public TVersion getRevertVersion() {
		List<TVersion> vList= baseDao.find(" from TVersion order by id desc");
		TVersion ver = null;
		if(vList!=null&&vList.size()>1){
			ver=vList.get(1);
		}
		return ver;
		}
	@Override
	public TVersion getVersion(int id) {
		TVersion ver=null;
		// TODO Auto-generated method stub
		 ver= (TVersion)baseDao.find1(" from TVersion where id="+id);
		return ver;
	}
}
