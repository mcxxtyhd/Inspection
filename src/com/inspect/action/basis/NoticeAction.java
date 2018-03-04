package com.inspect.action.basis;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;

import com.inspect.action.baseinfo.TBaseInfoAction;
import com.inspect.action.common.BaseAction;
import com.inspect.annotation.LogAnnotation;
import com.inspect.dao.BaseDaoI;
import com.inspect.model.basis.TEnumParameter;
import com.inspect.model.basis.TNotice;
import com.inspect.service.NoticeServiceI;
import com.inspect.service.SystemServiceI;
import com.inspect.util.common.ExceptionUtil;
import com.inspect.util.common.StringUtils;
import com.inspect.vo.basis.NoticeVo;
import com.inspect.vo.comon.Json;
import com.opensymphony.xwork2.ModelDriven;
import com.sun.org.apache.bcel.internal.generic.NEW;
@Namespace("/basis")
@Action(value="noticeAction",results={
		@Result(name="noticeList",location="/webpage/basis/noticeList.jsp"),
		@Result(name="indexNotice",location="/webpage/login/indexNotice.jsp"),
		@Result(name="noticeAdd",location="/webpage/basis/noticeAdd.jsp"),
		@Result(name="noticeEdit",location="/webpage/basis/noticeEdit.jsp"),
		@Result(name="noticeView",location="/webpage/basis/noticeView.jsp")})
public class NoticeAction  extends BaseAction implements ModelDriven<NoticeVo>{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5864734147349411684L;
	private static final Logger logger = Logger.getLogger(TBaseInfoAction.class);
	@Resource
	private NoticeServiceI noticeService;
	@Resource
	private BaseDaoI baseDao;
	@Resource
	private SystemServiceI systemService;
	public String noticeList(){
		getRequest().setAttribute("EnterpriseList",systemService.comboboxEnterprise(queryEnterpriseByWhere()));
		return "noticeList";
	}
	
	public String indexNotice(){
		getRequest().setAttribute("count",systemService.onlineCount(queryEnterpriseByWhere()));
		return "indexNotice";
	}
	
	public String noticeAdd() {
		return "noticeAdd";
	}
	public String noticeEdit() {
		return "noticeEdit";
	}
	public void noticeDatagrid(){
		int page =Integer.parseInt(ServletActionContext.getRequest().getParameter("page") );
		int rows =Integer.parseInt(ServletActionContext.getRequest().getParameter("rows") );
		Map<String, Object> map = noticeService.findNoticeDatagrid(noticeVo,page,rows,querySql());
		writeJson(map);
	}
	public void noticeDatagrid1(){
		int page =Integer.parseInt(ServletActionContext.getRequest().getParameter("page") );
		int rows =Integer.parseInt(ServletActionContext.getRequest().getParameter("rows") );
		Map<String, Object> map = noticeService.findNoticeDatagrid1(noticeVo,page,rows,querySql());
		writeJson(map);
	}
	
  private NoticeVo noticeVo=new NoticeVo();
	@Override
	public NoticeVo getModel() {
		// TODO Auto-generated method stub
		return noticeVo;
	}

	
	@LogAnnotation(event="添加公告",tablename="t_notice")
	public void addNotice(){
		Json j=new Json();
		try{	noticeVo.setPublisher(getSessionUserName().getUsername());
				noticeVo.setEntid(getSessionUserName().getEntid());
				noticeService.addNotice(noticeVo);
				j.setMsg("添加成功！");
				j.setSuccess(true);
			
		}catch(Exception e){
			setOperstatus(1);
			j.setMsg("添加失败！");
			logger.error(ExceptionUtil.getExceptionMessage(e));
		}
		writeJson(j);
	}
	@LogAnnotation(event="修改公告",tablename="t_notice")
	public void editNotice(){
		Json j=new Json();
		try{	noticeVo.setPublisher(getSessionUserName().getUsername());
				noticeService.editNotice(noticeVo);
				j.setMsg("修改成功！");
				j.setSuccess(true);
			
		}catch(Exception e){
			setOperstatus(1);
			j.setMsg("修改失败！");
			logger.error(ExceptionUtil.getExceptionMessage(e));
		}
		writeJson(j);
	}
	
	@LogAnnotation(event="删除公告",tablename="t_notice")
	public void removeNotice(){
		Json j=new Json();
		try{
			if(!StringUtils.isEmpty(noticeVo.getIds())){
				noticeService.removeNotice(noticeVo.getIds());
				j.setSuccess(true);
				j.setMsg("删除成功！");
			 }
			}	
		catch(Exception e){
			setOperstatus(1);
			j.setMsg("删除失败！");
			logger.error(ExceptionUtil.getExceptionMessage(e));
		}
		writeJson(j);
	}

	public void isadmin(){
		Json j=new Json();
		boolean flag=false;
		String ids=getRequest().getParameter("ids");
		flag=baseDao.isadmin(TNotice.class,ids,queryEnterpriseByWhere());
		j.setSuccess(flag);
		writeJson(j);
	}
}
