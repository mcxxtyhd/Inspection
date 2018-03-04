package com.inspect.action.basis;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Blob;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.hibernate.Hibernate;

import com.inspect.action.common.BaseAction;
import com.inspect.annotation.LogAnnotation;
import com.inspect.model.basis.TTwoDimensionCode;
import com.inspect.service.InspectItemServiceI;
import com.inspect.service.SystemServiceI;
import com.inspect.util.common.ExceptionUtil;
import com.inspect.util.common.StringUtils;
import com.inspect.util.common.TwoDimensionCode;
import com.inspect.vo.basis.TwoDimensionCodeVo;
import com.inspect.vo.comon.Json;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 二维码业务流程控制Action
 * @version 1.0
 */
@Namespace("/basis")
@Action(value="twocodeAction",results={
		@Result(name="twocodeList",location="/webpage/basis/twocodeList.jsp"),
		@Result(name="twocodeAdd",location="/webpage/basis/twocodeAdd.jsp"),
		@Result(name="twocodeEdit",location="/webpage/basis/twocodeEdit.jsp"),
		@Result(name="twocodeImage",location="/webpage/basis/twocodeImage.jsp")})
public class TwoDimensionCodeAction extends BaseAction implements ModelDriven<TwoDimensionCodeVo> {

	private static final long serialVersionUID = -3950042619264756446L;

	private static final Logger logger = Logger.getLogger(TwoDimensionCodeAction.class);
	
	private InspectItemServiceI inspectItemService;
	
	private SystemServiceI systemService;
	
	private TwoDimensionCodeVo twodimensioncodeVo=new TwoDimensionCodeVo();
	
	public InspectItemServiceI getInspectItemService() {
		return inspectItemService;
	}
	@Resource
	public void setInspectItemService(InspectItemServiceI inspectItemService) {
		this.inspectItemService = inspectItemService;
	}
	
	public SystemServiceI getSystemService() {
		return systemService;
	}

	@Resource
	public void setSystemService(SystemServiceI systemService) {
		this.systemService = systemService;
	}

	@Override
	public TwoDimensionCodeVo getModel() {
		return twodimensioncodeVo;
	}
	
	public String twocodeList(){
		getRequest().setAttribute("EnterpriseList",systemService.comboboxEnterprise(queryEnterpriseByWhere()));
		return "twocodeList";
	}
	
	public String twocodeAdd(){
		return "twocodeAdd";
	}
	
	public String twocodeEdit(){
		return "twocodeEdit";
	}
	
	public void twocodeDatagrid(){
		int page =Integer.parseInt(ServletActionContext.getRequest().getParameter("page") );
		int rows =Integer.parseInt(ServletActionContext.getRequest().getParameter("rows") );
		Map<String, Object> map = inspectItemService.findTwoDimensionCodeDatagrid(twodimensioncodeVo,page,rows,querySql());
		writeJson(map);
	}
	
	@LogAnnotation(event="添加二维码",tablename="t_dimension_code")
	public void addTwoDimensionCode(){
		Json j=new Json();
		try{
			twodimensioncodeVo.setEntid(getSessionUserName().getEntid());
			
			 //保存二维码
//			  String imgPath=ServletActionContext.getServletContext().getRealPath("/basis/upload/Michael_QRCode.png");
//			  File uploadfile=new File(imgPath);
//			  if(!uploadfile.getParentFile().exists()){
//					uploadfile.getParentFile().mkdir();
//				}
//			  String encoderContent=twodimensioncodeVo.getCname()+"||"+ twodimensioncodeVo.getCdesc();
//			  TwoDimensionCode handler = new TwoDimensionCode();  
//		      handler.encoderQRCode(encoderContent, imgPath, "png");
//		      FileInputStream fin = new FileInputStream(imgPath);// File 转 InputStream
//	          Blob blob =(Blob)Hibernate.createBlob(fin);//InputStream 转 Blob
		      //保存结束
//		      twodimensioncodeVo.setCpicture(blob);
		      
			inspectItemService.addTwoDimensionCode(twodimensioncodeVo);
			j.setMsg("添加成功！");
			j.setSuccess(true);
		}catch(Exception e){
			setOperstatus(1);
			j.setMsg("添加失败！");
			logger.error(ExceptionUtil.getExceptionMessage(e));
		}
		writeJson(j);
	}
	
	@LogAnnotation(event="修改二维码",tablename="t_dimension_code")
	public void editTwoDimensionCode(){
		Json j=new Json();
		try{
			inspectItemService.editTwoDimensionCode(twodimensioncodeVo);
			j.setMsg("修改成功！");
			j.setSuccess(true);
		}catch(Exception e){
			setOperstatus(1);
			j.setMsg("修改失败！");
			logger.error(ExceptionUtil.getExceptionMessage(e));
		}
		writeJson(j);
	}
	@LogAnnotation(event="删除二维码",tablename="t_dimension_code")
	public void deleteTwoDimensionCode(){
		Json j=new Json();
		try{
			if(!StringUtils.isEmpty(twodimensioncodeVo.getIds())){
				inspectItemService.removeTwoDimensionCode(twodimensioncodeVo.getIds());
				j.setSuccess(true);
				j.setMsg("删除成功！");
			}
		}catch(Exception e){
			setOperstatus(1);
			j.setMsg("删除失败！");
			logger.error(ExceptionUtil.getExceptionMessage(e));
		}
		writeJson(j);
	}

	public void twodimensioncodeList(){
		List<TTwoDimensionCode> but=inspectItemService.getTwoDimensionCodeList(getSessionUserName().getEntid());
		writeJson(but);
	}
	
	public String twocodeImage(){
		String cid=getRequest().getParameter("CodeID");
		TTwoDimensionCode dcode=inspectItemService.getTwoDimensionCode(cid);
		 //生成二维码二维码
		  String imgPath=ServletActionContext.getServletContext().getRealPath("/webpage/basis/upload/Michael_QRCode.png");
		  File uploadfile=new File(imgPath);
		  if(!uploadfile.getParentFile().exists()){
				uploadfile.getParentFile().mkdir();
			}
		  String encoderContent=dcode.getCname()+"||"+ dcode.getCdesc();
		  TwoDimensionCode handler = new TwoDimensionCode();  
	      handler.encoderQRCode(encoderContent, imgPath, "png");
	      //生成二维码二维码结束
		getRequest().setAttribute("TDC",dcode);
		return "twocodeImage";
	}
	
	/**
	 * 将图片输出
	 */
	 public String getImage() throws Exception {
		    HttpServletResponse response=ServletActionContext.getResponse();
		    InputStream in=null;
		    String imgPath=ServletActionContext.getServletContext().getRealPath("/webpage/basis/upload/Michael_QRCode.png");
		    FileInputStream fin = new FileInputStream(imgPath);// File 转 InputStream
		    Blob blob =(Blob)Hibernate.createBlob(fin);//InputStream 转 Blob
		    in=blob.getBinaryStream();
		    if(in!=null){
		    	response.setContentType("image/gif");
		    	response.setHeader("Pragma","No-cache");
		    	response.setHeader("Cache-Control","no-cache");
		    	response.setDateHeader("Expires", 0); 
		    	int size=in.available();
			    byte[] image=new byte[size];
				in.read(image);
				ServletOutputStream out=response.getOutputStream();
			    out.write(image);
			    out.flush();
			    out.close();
		    }
		    //删除图片
//		     File uploadfile=new File(imgPath);
//		      if(uploadfile.exists()){
//				uploadfile.delete();
//			 }
		    return null;
		 }
	
	
}
