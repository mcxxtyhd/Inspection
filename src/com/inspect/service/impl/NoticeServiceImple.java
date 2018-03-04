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
import com.inspect.model.baseinfo.TBaseInfo;
import com.inspect.model.basis.TNotice;
import com.inspect.service.NoticeServiceI;
import com.inspect.util.common.DateUtils;
import com.inspect.util.common.QueryResult;
import com.inspect.util.common.StringUtils;
import com.inspect.vo.basis.NoticeVo;
import com.inspect.vo.basis.TBaseInfoVo;

@Service("noticeService")
@Transactional(propagation = Propagation.REQUIRED,readOnly = false,rollbackFor = Exception.class)
public class NoticeServiceImple implements NoticeServiceI {
	@Resource
	private BaseDaoI baseDao;
	
	@Override
	public Map<String, Object> findNoticeDatagrid(NoticeVo noticeVo, int page,
			int rows, String qsql) {
		StringBuffer buf=new StringBuffer(qsql);
		Map<String, Object> map = new HashMap<String, Object>();
		if(StringUtils.isNotEmpty(noticeVo.getNtype())){
			buf.append("and ntype like '%").append(noticeVo.getNtype()).append("%'");
		}
		if(StringUtils.isNotEmpty(noticeVo.getNcontent())){
			buf.append(" and ncontent like '%").append(noticeVo.getNcontent()).append("%'");
			}
		
		if(!StringUtils.isEmpty(noticeVo.getPublisher())){
			buf.append(" and publisher like '%").append(noticeVo.getPublisher()).append("%'");
		}
		if(!StringUtils.isEmpty(noticeVo.getNstarttime())){
			buf.append(" and nstarttime >='").append(noticeVo.getNstarttime()).append("'");
		}
		if(!StringUtils.isEmpty(noticeVo.getNendtime())){
			buf.append(" and nendtime <='").append(noticeVo.getNendtime()).append("'");
		}
		if(noticeVo.getEntid()!=0){
			buf.append(" and entid=").append(noticeVo.getEntid());
		}
		buf.append(" order by id desc");
		QueryResult<TNotice> queryResult = baseDao.getQueryResult(TNotice.class, buf.toString(), (page - 1) * rows, rows,null, null);
		List<NoticeVo> noticeVoList=new ArrayList<NoticeVo>();
		if (queryResult != null && queryResult.getResultList().size() > 0) {
			for (TNotice notice : queryResult.getResultList()) {
				NoticeVo noticeVo1 = new NoticeVo();
				BeanUtils.copyProperties(notice,noticeVo1);
				noticeVoList.add(noticeVo1);
			}
		}
		map.put("total", queryResult.getTotalRecord());
		map.put("rows", noticeVoList);
		return map;
	}
	/**
	 * 首页显示
	 * @param noticeVo
	 * @param page
	 * @param rows
	 * @param qsql
	 * @return
	 */
	@Override
	public Map<String, Object> findNoticeDatagrid1(NoticeVo noticeVo, int page,
			int rows, String qsql) {
		StringBuffer buf=new StringBuffer(qsql);
		String date=DateUtils.getFormatDayDate();
		Map<String, Object> map = new HashMap<String, Object>();
			buf.append(" and nstarttime <='").append(date).append("'");
			buf.append(" and nendtime >='").append(date).append("'");
		buf.append(" order by id desc");
		QueryResult<TNotice> queryResult = baseDao.getQueryResult(TNotice.class, buf.toString(), (page - 1) * rows, rows,null, null);
		List<NoticeVo> noticeVoList=new ArrayList<NoticeVo>();
		if (queryResult != null && queryResult.getResultList().size() > 0) {
			for (TNotice notice : queryResult.getResultList()) {
				NoticeVo noticeVo1 = new NoticeVo();
				BeanUtils.copyProperties(notice,noticeVo1);
				noticeVoList.add(noticeVo1);
			}
		}
		map.put("total", queryResult.getTotalRecord());
		map.put("rows", noticeVoList);
		return map;
	}
	@Override
	public void addNotice(NoticeVo noticeVo) {
		TNotice notice=new TNotice();
		BeanUtils.copyProperties(noticeVo,notice );
		baseDao.save(notice);
		
	}
	@Override
	public void editNotice(NoticeVo noticeVo){
		TNotice notice=baseDao.get(TNotice.class, noticeVo.getId());
		BeanUtils.copyProperties(noticeVo, notice,new String[]{"id","entid"});
		baseDao.update(notice);
	}

	@Override
	public void removeNotice(String ids) {
		// TODO Auto-generated method stub
		for(String id : ids.split(",")) {
				baseDao.delete(TNotice.class,Integer.parseInt(id));
		}
		
	}

}
