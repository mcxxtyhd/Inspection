package com.inspect.service;

import java.sql.Date;

import com.inspect.model.basis.TInspectUser;
import com.inspect.model.monitor.TTerminateStatusReport;
import com.inspect.vo.HttpVo.TermStatus;

public interface HttpServiceI {


	/**@author liao
	 *  添加终端状态信息（在登陆是添加）
	 */
	Integer addTerminateStatusReport (TTerminateStatusReport  term,TTerminateStatusReport  terminateStatusReport );

	TermStatus judgeValid(TTerminateStatusReport term,String imark);
	//终端登录时使用
	TermStatus judgeValid2(TTerminateStatusReport term,String imark);
	
}
