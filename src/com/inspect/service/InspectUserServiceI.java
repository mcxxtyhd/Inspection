package com.inspect.service;

import java.util.List;
import java.util.Map;

import com.inspect.model.basis.TGroup;
import com.inspect.model.basis.TInspectUser;
import com.inspect.model.basis.TTerminal;
import com.inspect.model.monitor.TTerminateStatusReport;
import com.inspect.vo.basis.GroupVo;
import com.inspect.vo.basis.InspectUserVo;
import com.inspect.vo.basis.TerminalVo;


public interface InspectUserServiceI {
	
	/**
	 *  添加班组对象
	 */
	void addGroup(GroupVo groupvo);
	
	/**
	 * 修改班组对象
	 */
	void editGroup(GroupVo groupvo);

	/**
	 * 删除班组对象
	 */
	void removeGroup(String ids);

	/**
	 * 查询班组对象
	 */
	TGroup getGroup(String id);
	
	boolean isGroupHaveUsers(String Groupids);
	/**
	 * 查询班组对象 分页
	 */
	Map<String, Object> findGroupterDatagrid(GroupVo groupvo,int page, int rows,String qsql);
	
	 List<TInspectUser> findInspectUserByGroupid(String groupid);
	/**
	 *  添加巡检员对象
	 */
	void addInspectUser(InspectUserVo inspectuservo);
	
	/**
	 * 修改巡检员对象
	 */
	void editInspectUser(InspectUserVo inspectuservo);

	/**
	 * 删除巡检员对象
	 */
	void removeInspectUser(String ids);
	/**修改密码
	 * @author liao
	 * @param inspectUser
	 */
	void editInspectUserPwd(TInspectUser inspectUser);
	
	/**
	 * @author liao
	 * 通过用户id找到终端用户信息，修改密码时候用
	 */
//	TInspectUser getInspectUserByIunumber(String iunumber);
	
	/**
	 * 验证用户是否存在
	 * @param user
	 * @return
	 */
	TInspectUser checkTInspectUser(String iuname,String iupwd);
	
	/**
	 *@author liao
	 * 查询TTerminateStatusReport表中指定id最近登录的信息,用于退出终端
	 * @param user
	 * @return
	 */
	TTerminateStatusReport findTerminateStatusReport(String rpuserid);
	

	
	/**@author liao
	 *  添加终端状态信息（在登陆是添加）
	 */
	void addTerminateStatusReport (TTerminateStatusReport  terminateStatusReport );
	

	/**@author liao
	 *  删除终端状态信息
	 */
	void deleteTerminateStatusReport (String termid );
	/**@author liao
	 * 在TTerminateStatusReport中添加退出是的时间，修改状态
	 * @param terminateStatusReport
	 */
	void editTerminateStatusReport(TTerminateStatusReport  terminateStatusReport );
	
	

	/**
	 * 查询巡检员对象
	 */
	TInspectUser getInspectUser(String id);

	/**
	 * 查询巡检员对象 分页
	 */
	Map<String, Object> findInspectUserDatagrid(InspectUserVo inspectuservo,int page, int rows,String qsql);
	
	/**
	 * 查询班组列表
	 */
    List<TGroup> getGroupList(int entid);
    /**
	 * 查询班组列表
	 */
    List<TGroup> getGroupList1(int entid);
    
    
    /**
	 * 查询班组列表
	 */
    List<TInspectUser> getInspectUserList(int entid);
    
    /**
     * 查询班组下巡检员数量
     * @return
     */
    Long findcountBygid(int gid,int entid);
    
	/**
	 *  添加终端对象
	 */
	void addTerminal(TerminalVo terminalvo);
	/**
	 *  根据终端编号查找终端对象
	 */
	boolean getTerminalByTermid(String termid);
	
	
	/**
	 * 修改终端对象
	 */
	void editTerminal(TerminalVo terminalvo);
	/*
	 * 根据终端编号获取终端最后一条信息
	 */
	TTerminateStatusReport geTerminateStatusReport(String termid);
	/**
	 * 删除终端对象
	 */
	void removeTerminal(String ids);

	/**
	 * 查询终端对象
	 */
	TTerminal getTerminal(String id);

	
	/**
	 * 根据终端编号查询终端对象
	 */
	TTerminal getTerminalByTermno(String termno);
	/**
	 * 查询终端对象 分页
	 */
	Map<String, Object> findTerminalterDatagrid(TerminalVo terminalvo,int page, int rows,String qsql);

	Long getInspectUserCount(int eid);
    /**
     * 查询某企业下的终端数量
     */
	Long terminateTotalCount(int entid);

	Long count(String uname, int entid, int type);

	List<TInspectUser> getInspectUsers();

	Long count1(String termno, int entid, int type);



}
