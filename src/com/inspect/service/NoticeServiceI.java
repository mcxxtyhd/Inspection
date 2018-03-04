package com.inspect.service;

import java.util.Map;

import com.inspect.vo.basis.NoticeVo;

public interface NoticeServiceI {
	/**
	 * 查询巡检项对象 分页
	 */
	Map<String, Object> findNoticeDatagrid(NoticeVo noticeVo,int page, int rows,String qsql);
	void addNotice(NoticeVo noticeVo);
	void editNotice(NoticeVo noticeVo);
	void removeNotice(String ids);
	/**在首页显示公告
	 */
	Map<String, Object> findNoticeDatagrid1(NoticeVo noticeVo, int page,
			int rows, String qsql);
}
