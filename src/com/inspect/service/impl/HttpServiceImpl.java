package com.inspect.service.impl;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.inspect.dao.BaseDaoI;
import com.inspect.model.basis.TInspectUser;
import com.inspect.model.monitor.TTerminateStatusReport;
import com.inspect.service.HttpServiceI;
import com.inspect.util.common.DateUtils;
import com.inspect.vo.HttpVo.TermStatus;

@Service("httpService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
public class HttpServiceImpl implements HttpServiceI {
	@Resource
	private BaseDaoI baseDao;

	public BaseDaoI getBaseDao() {
		return baseDao;
	}

	public void setBaseDao(BaseDaoI baseDao) {
		this.baseDao = baseDao;
	}
	


	@Override
	public Integer addTerminateStatusReport(TTerminateStatusReport term,
			TTerminateStatusReport terminateStatusReport) {
		int flag=0;
		try {
			//如果不是第一次登录此终端， 那term不会是null，旧的终端
			if(term!=null){
				//如果上次是退出异常，把上次没保存的也要保存。
				if(term.getFlag()==1){
					term.setFlag(0);
					baseDao.save1(term);
				}
			}
			baseDao.save1(terminateStatusReport);  //保存新的记录
		} catch (Exception e) {
			// TODO: handle exception
			flag=1;
		}
	return flag;
		
	}
	/**但登录不使用此方法
	 * 判断是否过期（超过一天，因为周期设为一天）或者标识码是否匹配（是否重新登录）
	 * @param term
	 * @return
	 * 用到此方法的接口有 退出，修改密码
	 */
	@Override
	public TermStatus judgeValid(TTerminateStatusReport term,String imark){

		TermStatus tStatus=new TermStatus();
		if(term==null){
			tStatus.setReason("此用户编号或终端编号备不存在");
			tStatus.setFlag(1);
			return tStatus;
		}
		//判断登录是否过期
		//如两个日期是同一天，在不过期，否则过期
		String date=DateUtils.getFormatDayDate();  //yyyy-MM-dd
		// rplogintime; // 登录时间  超过一天，算过期
		if(!date.equals(term.getRplogintime().substring(0,10))){
			term.setFlag(0);
			baseDao.update(term);
			tStatus.setReason("此用户信息过期，请重新登录");
			tStatus.setFlag(2);
			return tStatus;
		}
		//判断标志码是否一致
		if(!term.getRmark().equals(imark)){//0.9292865651198099
			term.setFlag(0);
			baseDao.update(term);
			tStatus.setReason("此用户已经被重复登录，若要继续操作，请重新登录");
			tStatus.setFlag(2);
			return tStatus;
		}
		tStatus.setFlag(0);
		return tStatus;
	}


	/**
	 * 判断是否过期（超过一天，因为周期设为一天）或者标识码是否匹配没用到
	 * @param term
	 * @return
	 */
	@Override
	public TermStatus judgeValid2(TTerminateStatusReport term,String imark){
		TermStatus tStatus=new TermStatus();
		
		//判断登录是否过期
		//如两个日期是同一天，在不过期，否则过期
		String date=DateUtils.getFormatDayDate();
		if(!date.equals(term.getRplogintime().substring(0,10))){}
		//判断标志码是否一致
		
		tStatus.setFlag(0);
		return tStatus;
	}

}
