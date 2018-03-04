package com.inspect.action.query;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;


import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;

import com.inspect.action.common.BaseAction;
import com.inspect.annotation.LogAnnotation;
import com.inspect.constant.Constant;
import com.inspect.model.baseinfo.TBaseInfo;
import com.inspect.model.basis.TEquipment;
import com.inspect.model.monitor.TInspectItemDetailReport;
import com.inspect.model.monitor.TInspectItemReport;
import com.inspect.service.InspectItemServiceI;
import com.inspect.service.InspectQueryServiceI;
import com.inspect.service.InspectUserServiceI;
import com.inspect.service.SystemServiceI;
import com.inspect.util.common.ExceptionUtil;
import com.inspect.util.common.FileUtil;
import com.inspect.util.common.StringUtils;
import com.inspect.vo.comon.Json;
import com.inspect.vo.monitor.InspectItemReportVo;
import com.inspect.vo.monitor.InspectReportImgVo;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 设备巡检信息查询业务流程控制Action
 * @version 1.0
 */
@Namespace("/query")
@Action(value="inspectmessageAction",results={
		@Result(name="inspectmessageList",location="/webpage/query/inspectmessageList.jsp"),
		@Result(name="inspectmessageAdd",location="/webpage/query/inspectmessageAdd.jsp"),
		@Result(name="inspectmessageView",location="/webpage/query/inspectmessageView.jsp"),
		@Result(name="inspectmessageImage",location="/webpage/query/inspectmessageImage.jsp"),    
		@Result(name="equtMsgView",location="/webpage/monitor/equtMsgView.jsp"),
		@Result(name="inspectmessageCompare",location="/webpage/query/inspectmessageCompare.jsp"),
		@Result(name="inspectmessageCheck",location="/webpage/query/inspectmessageCheck.jsp"),
		@Result(name="inspectmessageModifyView",location="/webpage/query/inspectmessageModifyView.jsp"),
		@Result(name="inspectmessageModifyEdit",location="/webpage/query/inspectmessageModifyEdit.jsp"),
		@Result(name="inspectmessageModifyList",location="/webpage/query/inspectmessageModifyList.jsp")
		})
public class InspectMessageAction extends BaseAction implements ModelDriven<InspectItemReportVo> {

	private static final long serialVersionUID = -5793219728219822288L;
	private static final Logger logger = Logger.getLogger(InspectMessageAction.class);

	@Resource
	private InspectQueryServiceI inspectQueryService;
	
	@Resource
	private InspectUserServiceI inspectUserService;
	
	@Resource
	private InspectItemServiceI inspectItemService;
	
	@Resource
	private SystemServiceI systemService;
	private InspectItemReportVo inspectmessageVo=new InspectItemReportVo();
	
	@Override
	public InspectItemReportVo getModel() {
		return inspectmessageVo;
	}
	
	public String inspectmessageAdd(){
		return "inspectmessageAdd";
	}
	
	//巡检数据查询页面
	public String inspectmessageList(){
		getRequestBean();
		return "inspectmessageList";
	}
	
	public void inspectmsgDatagrid(){
		int page =Integer.parseInt(ServletActionContext.getRequest().getParameter("page") );
		int rows =Integer.parseInt(ServletActionContext.getRequest().getParameter("rows") );
		String buf1="";
		if(!StringUtils.isEmpty(inspectmessageVo.getXename())){
			//通过设备名称找到设备主键id
			buf1=inspectItemService.getEquipmentByename(inspectmessageVo.getXename().trim(), queryEnterpriseByWhere());
		}
		Map<String, Object> map= inspectQueryService.findInspectInfoDatagrid(inspectmessageVo,page,rows,querySql(), buf1);
		writeJson(map);
	}
	
	
	//巡检数据查询详细页面
	public String inspectmessageView(){
		String mid=getRequest().getParameter("XMId");
		getRequest().setAttribute("XMessaheId",mid);
		TInspectItemReport msg=inspectQueryService.getItemReport(mid);
		if(!StringUtils.isEmpty(msg.getXimgname())){
			List<InspectReportImgVo> list=new ArrayList<InspectReportImgVo>();
			String[] imgs=msg.getXimgname().split(",");
			for(int i=0;i<imgs.length;i++){
				InspectReportImgVo imgvo=new InspectReportImgVo();
				imgvo.setImgid(i);
				imgvo.setImgname(imgs[i]);
				list.add(imgvo);
			}
			getRequest().setAttribute("ImgList",list);
		}
		TBaseInfo t=inspectItemService.getEntityById(TBaseInfo.class,inspectQueryService.getItemReport(mid).getXequid());
		getRequest().setAttribute("ReportMessage",t);
		getRequest().setAttribute("ReportMessageBase", msg);
		return "inspectmessageView";
	}
	

	
	//巡检图片页面
	public String inspectmessageImage(){
		String mid=getRequest().getParameter("XMId");
		getRequest().setAttribute("XMessaheId",mid);
		TInspectItemReport msg=inspectQueryService.getItemReport(mid);
		if(!StringUtils.isEmpty(msg.getXimgname())){
			List<InspectReportImgVo> list=new ArrayList<InspectReportImgVo>();
			String[] imgs=msg.getXimgname().split(",");
			for(int i=0;i<imgs.length;i++){
				InspectReportImgVo imgvo=new InspectReportImgVo();
				imgvo.setImgid(i);
				imgvo.setImgname(imgs[i]);
				list.add(imgvo);
			}
		/*	for(InspectReportImgVo imgvo:list){
				String name=Constant.IMG_PATH+File.separator+imgvo.getImgname();
				File f=new File(name);
				FileUtil.getThumbFile(f);
			}*/
			getRequest().setAttribute("ImgList",list);
			
		}
		
		return "inspectmessageImage";
	}

	/**
	 * 加载巡检设备巡检详细信息
	 */
	public void msgInfoDatagrid(){
		int page =Integer.parseInt(ServletActionContext.getRequest().getParameter("page") );
		int rows =Integer.parseInt(ServletActionContext.getRequest().getParameter("rows") );
		String xmid=getRequest().getParameter("XMessageId");
		inspectmessageVo.setXmid(Integer.parseInt(xmid));
		
		Map<String, Object> map = inspectQueryService.findMessageInfoDatagrid(inspectmessageVo,page,rows,querySql());
		writeJson(map);
	}
	
	//巡检数据比对查询页面
	public String inspectmessageCompare(){
		getRequestBean();
		return "inspectmessageCompare";
	}
	// 巡检数据比对
	public void inspectmsgCompareDatagrid(){
		int page =Integer.parseInt(ServletActionContext.getRequest().getParameter("page") );
		int rows =Integer.parseInt(ServletActionContext.getRequest().getParameter("rows") );
		String buf="";
		if(!StringUtils.isEmpty(inspectmessageVo.getXename())){
			buf=inspectItemService.getEquipmentByename(inspectmessageVo.getXename().trim(), queryEnterpriseByWhere());
		}
		Map<String, Object> map= inspectQueryService.findInspectInfoCompareDatagrid1(inspectmessageVo,page,rows,querySql(),buf);
		writeJson(map);
	}
	
	//巡检数据核查查询页面
	public String inspectmessageCheck(){
		getRequestBean();
		return "inspectmessageCheck";
	}
	public void inspectmsgCheckDatagrid(){
		int page =Integer.parseInt(ServletActionContext.getRequest().getParameter("page") );
		int rows =Integer.parseInt(ServletActionContext.getRequest().getParameter("rows") );
		Map<String, Object> map= inspectQueryService.findInspectInfoCompareDatagrid(inspectmessageVo,page,rows,querySql());
		writeJson(map);
	}
	//巡检数据查询详细页面（用于修改）
	public String inspectmessageView1(){/*
		String mid=getRequest().getParameter("XMId");
		getRequest().setAttribute("XMessaheId",mid);
		TInspectItemReport msg=inspectQueryService.getItemReport(mid);
		if(!StringUtils.isEmpty(msg.getXimgname())){
			List<InspectReportImgVo> list=new ArrayList<InspectReportImgVo>();
			String[] imgs=msg.getXimgname().split(",");
			for(int i=0;i<imgs.length;i++){
				InspectReportImgVo imgvo=new InspectReportImgVo();
				imgvo.setImgid(i);
				imgvo.setImgname(imgs[i]);
				list.add(imgvo);
			}
			getRequest().setAttribute("ImgList",list);
		}
		TBaseInfo t=inspectItemService.getEntityById(TBaseInfo.class,inspectQueryService.getItemReport(mid).getXequid());
		getRequest().setAttribute("ReportMessage",t);
		getRequest().setAttribute("ReportMessageBase", msg);
		return "inspectmessageModifyView";
	*/
		return "";
		}
	public String inspectmessageModifyEdit(){
		String msgid=(String) getRequest().getParameter("msgid");
		String equid=(String) getRequest().getParameter("equid");
		getRequest().setAttribute("msgid", msgid);
//		TBaseInfo t=inspectItemService.getEntityById(TBaseInfo.class,equid);
//		getRequest().setAttribute("ReportMessage",t);
		return "inspectmessageModifyView";
	}

	//巡检数据修改页面
	public String inspectmessageModifyList(){
		getRequestBean();
		return "inspectmessageModifyList";
	}
	public void inspectmessageModifyDatagrid(){
		int page =Integer.parseInt(ServletActionContext.getRequest().getParameter("page") );
		int rows =Integer.parseInt(ServletActionContext.getRequest().getParameter("rows") );
		Map<String, Object> map= inspectQueryService.inspectmessageModifyDatagrid(inspectmessageVo,page,rows,queryEnterpriseByWhere());
		writeJson(map);
	}
	@LogAnnotation(event="修改巡检记录",tablename="t_report_message_detail")
	public void editinspectmessageModify(){

		Json j=new Json();
		//System.out.println(getRequest().getParameter("id"));
		try{
			inspectQueryService.editDetailReport(inspectmessageVo);
			j.setMsg("修改成功！");
			j.setSuccess(true);
		}catch(Exception e){
			setOperstatus(1);
			j.setMsg("修改失败！");
			logger.error(ExceptionUtil.getExceptionMessage(e));
		}
		writeJson(j);
	
/*		String mid=getRequest().getParameter("msgid");
		getRequest().setAttribute("XMessaheId",mid);
		TInspectItemReport msg=inspectQueryService.getItemReport(mid);
		if(!StringUtils.isEmpty(msg.getXimgname())){
			List<InspectReportImgVo> list=new ArrayList<InspectReportImgVo>();
			String[] imgs=msg.getXimgname().split(",");
			for(int i=0;i<imgs.length;i++){
				InspectReportImgVo imgvo=new InspectReportImgVo();
				imgvo.setImgid(i);
				imgvo.setImgname(imgs[i]);
				list.add(imgvo);
			}
			getRequest().setAttribute("ImgList",list);
		}
		TBaseInfo t=inspectItemService.getEntityById(TBaseInfo.class,inspectQueryService.getItemReport(mid).getXequid());
		getRequest().setAttribute("ReportMessage",t);
		getRequest().setAttribute("ReportMessageBase", msg);
		//return "inspectmessageModifyView";	
*/	}
	//巡检数据状态修改
	@LogAnnotation(event="上报巡检项值修改",tablename="t_report_message_detail")
	public void inspectmsgEditStatus(){
		Json j=new Json();
		try{
			TInspectItemReport rmsg=this.inspectQueryService.getItemReport(getRequest().getParameter("MsgId"));
			if(rmsg!=null){
				rmsg.setXstatus("1");
				j.setMsg("修改成功！");
				j.setSuccess(true);
				this.inspectQueryService.editReportMsgStatus(rmsg);
			}
		}catch(Exception e){
			setOperstatus(1);
			logger.error(ExceptionUtil.getExceptionMessage(e));    
			j.setMsg("修改失败");			
		}
		writeJson(j);
	}
	//用普通的方式显示页面，如果用easyri方式，则使用inspectmessageView方法
	public String inspectDetailInfo(){
		String xmid=getRequest().getParameter("XMId");
		inspectmessageVo.setXmid(Integer.parseInt(xmid));
		List<InspectItemReportVo> list = inspectQueryService.findInspectDetailInfo(inspectmessageVo);
		getRequest().setAttribute("inspectItemReportVolist", list);
		return "inspectmessageView";
	}
	
	public void writeImage1(){
		TInspectItemReport tInspectItemDetailReport = inspectQueryService.getItemReport("54") ;
		String[] imgs=tInspectItemDetailReport.getXimgname().split(",");
		for(int i=0;i<imgs.length;i++){
			InspectReportImgVo imgvo=new InspectReportImgVo();
			imgvo.setImgid(i);
			imgvo.setImgname(imgs[i]);
			//this.writeImage(imgs[i]);
		}
	}
	/**
	 * 读取图片信息
	 */
	public void writeImage(){
		String imagename = getRequest().getParameter("messgaeDetailIdString") ;
		String flag = getRequest().getParameter("flag") ;
		String pic_path="";
			pic_path = Constant.IMG_PATH + File.separator + imagename ;
		try {
			FileInputStream is = new FileInputStream( pic_path);
			 int i = is.available(); // 得到文件大小
			 byte data[] = new byte[i];
			 is.read(data); // 读数据
			 is.close();
			 getResponse().setContentType("image/*"); // 设置返回的文件类型
			 OutputStream toClient = getResponse().getOutputStream(); // 得到向客户端输出二进制数据的对象
			 toClient.write(data); // 输出数据
			 toClient.close();
		} catch ( Exception e ) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} 
	}
	
	public String inspectmessageList1(){
		//加载巡检员列表
		getRequest().setAttribute("GroupList",inspectUserService.getGroupList(queryEnterpriseByWhere()));
		getRequest().setAttribute("InspectUserList",inspectUserService.getInspectUserList(queryEnterpriseByWhere()));
		getRequest().setAttribute("EquipmentList",inspectItemService.getEquipmentList(getSessionUserName().getEntid(),"","a"));
		return "equtMsgView";
	}
/**
 * 在地图中点击设备信息时调用此方法以获取巡检项的所有信息
 */
	public void inapectmsgDatagrid1(){
		int page =Integer.parseInt(ServletActionContext.getRequest().getParameter("page") );
		int rows =Integer.parseInt(ServletActionContext.getRequest().getParameter("rows") );
		String value = inspectmessageVo.getXequtnum() ;
		if( ! StringUtils.isEmpty( value ) ){
			String[] valueString = value.split(",") ;
			inspectmessageVo.setXequtnum( valueString.length > 1 ? valueString[ valueString.length - 1 ].trim() : value ) ; 
		}
		Map<String, Object> map = inspectQueryService.findInspectInfoDatagrid1(inspectmessageVo,page,rows,querySql());
		writeJson(map);
	}
	
	
	public void findProjectByEidDatagrid(){
		inspectmessageVo.setXequid(Integer.parseInt(getRequest().getParameter("equipmentId")));
		Map<String, Object> map= inspectQueryService.findProjectByEidDatagrid(inspectmessageVo,querySql());
		writeJson(map);
	}
	
	public void downFile1() throws UnsupportedEncodingException{
		String filename = getRequest().getParameter("messgaeDetailIdString") ;
	    filename = java.net.URLDecoder.decode(filename, "UTF-8");//解码关键！！！！ 
		InputStream is = null;
		OutputStream os = null;
		String newpath = URLEncoder.encode(filename, "utf-8");
		byte[] b = new byte[1024];
		int i = 0;
		//判断附件是否存在
		String path=Constant.IMG_PATH;
		File f =new File(path + File.separator+ filename);
		if(!f.exists()){
			logger.info("照片不存在:");
			return;
		}
		try {
			is = new FileInputStream(path + File.separator + filename);
			os = ServletActionContext.getResponse().getOutputStream();
			ServletActionContext.getResponse().setContentType("application/x-msdownload");
			ServletActionContext.getResponse().setHeader("Content-Disposition", "attachment; filename="+ newpath);
			while ((i = is.read(b)) != -1) {
				os.write(b, 0, i);
				i = 0;
			}
			os.flush();
		} catch (IOException e) {
			logger.info("图片不存在:"+e.getMessage());
		} finally {
			try {
				os.close();
				is.close();
			} catch (IOException e) {
				logger.info("图片读取异常!:"+e.getMessage());
			}
		}
  }
	public void documentDown()throws Exception{
		String imgname = getRequest().getParameter("ImgName");
		downFile(imgname);
	 }
	
	private void downFile(String filename)throws Exception{
	    filename = java.net.URLDecoder.decode(filename, "UTF-8");//解码关键！！！！ 
		InputStream is = null;
		OutputStream os = null;
		String newpath = URLEncoder.encode(filename, "utf-8");
		byte[] b = new byte[1024];
		int i = 0;
		//判断附件是否存在
		String path=Constant.IMG_PATH;
		File f =new File(path + File.separator+ filename);
		if(!f.exists()){
			logger.info("照片不存在:");
		}
		try {
			is = new FileInputStream(path + File.separator + filename);
			os = ServletActionContext.getResponse().getOutputStream();
			ServletActionContext.getResponse().setContentType("application/x-msdownload");
			ServletActionContext.getResponse().setHeader("Content-Disposition", "attachment; filename="+ newpath);
			while ((i = is.read(b)) != -1) {
				os.write(b, 0, i);
				i = 0;
			}
			os.flush();
		} catch (IOException e) {
			logger.info("图片不存在:"+e.getMessage());
		} finally {
			try {
				os.close();
				is.close();
			} catch (IOException e) {
				logger.info("图片读取异常!:"+e.getMessage());
			}
		}
  }
	
	
	private void getRequestBean(){
		getRequest().setAttribute("EnterpriseList",systemService.comboboxEnterprise(queryEnterpriseByWhere()));
		getRequest().setAttribute("TaskList",inspectItemService.getPlanTaskList(queryEnterpriseByWhere()));
		getRequest().setAttribute("GroupList",inspectUserService.getGroupList(queryEnterpriseByWhere()));
		getRequest().setAttribute("InspectUserList",inspectUserService.getInspectUserList(queryEnterpriseByWhere()));
		//getRequest().setAttribute("EquipmentList",inspectItemService.getEquipmentList(getSessionUserName().getEntid(),"","a"));
	}
}
