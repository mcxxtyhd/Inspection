package com.inspect.action.basis;

import java.awt.print.Printable;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.junit.runner.Request;

import com.inspect.action.baseinfo.TBaseInfoAction;
import com.inspect.action.common.BaseAction;
import com.inspect.annotation.LogAnnotation;
import com.inspect.constant.Constant;
import com.inspect.dao.BaseDaoI;
import com.inspect.model.basis.TEnumParameter;
import com.inspect.model.basis.TVersion;
import com.inspect.service.NoticeServiceI;
import com.inspect.service.SystemServiceI;
import com.inspect.service.VersionServiceI;
import com.inspect.util.common.DateUtils;
import com.inspect.util.common.ExceptionUtil;
import com.inspect.util.common.StringUtils;
import com.inspect.vo.basis.NoticeVo;
import com.inspect.vo.basis.VersionVo;
import com.inspect.vo.comon.Json;
import com.itextpdf.text.pdf.PdfStructTreeController.returnType;
import com.opensymphony.xwork2.ModelDriven;
import com.sun.org.apache.bcel.internal.generic.NEW;
@Namespace("/basis")
@Action(value="versionAction",results={
		@Result(name="versionList",location="/webpage/basis/versionList.jsp"),
		@Result(name="versionAdd",location="/webpage/basis/versionAdd.jsp"),
		@Result(name="versionEdit",location="/webpage/basis/versionEdit.jsp")
		})
public class VersionAction  extends BaseAction implements ModelDriven<VersionVo>{
	
	private static final long serialVersionUID = -5864734147349411684L;
	private static final Logger logger = Logger.getLogger(TBaseInfoAction.class);
	@Resource
	private VersionServiceI versionService;
	@Resource
	private SystemServiceI systemService;
	private File uploadFile;// 得到上传的文件
    private String uploadFileContentType;// 得到文件的类型
    private String uploadFileFileName;// 得到文件的名称
    @Resource
	private BaseDaoI baseDao;
	public File getUploadFile() {
		return uploadFile;
	}



	public void setUploadFile(File uploadFile) {
		this.uploadFile = uploadFile;
	}



	public String getUploadFileContentType() {
		return uploadFileContentType;
	}



	public void setUploadFileContentType(String uploadFileContentType) {
		this.uploadFileContentType = uploadFileContentType;
	}



	public String getUploadFileFileName() {
		return uploadFileFileName;
	}



	public void setUploadFileFileName(String uploadFileFileName) {
		this.uploadFileFileName = uploadFileFileName;
	}



	public String versionList(){
		getRequest().setAttribute("EnterpriseList",systemService.comboboxEnterprise(queryEnterpriseByWhere()));
		return "versionList";
	}
	

	
	public String versionAdd() {
		return "versionAdd";
	}
	public String versionEdit() {
		return "versionEdit";
	}
	public void versionDatagrid(){
		int page =Integer.parseInt(ServletActionContext.getRequest().getParameter("page") );
		int rows =Integer.parseInt(ServletActionContext.getRequest().getParameter("rows") );
		Map<String, Object> map = versionService.findNoticeDatagrid(versionVo,page,rows,querySql());
		writeJson(map);
	}

	
  private VersionVo versionVo=new VersionVo();
	@Override
	public VersionVo getModel() {
		// TODO Auto-generated method stub
		return versionVo;
	}

	
	@LogAnnotation(event="添加版本",tablename="t_version")
	public void addVersion(){
		Json j=new Json();
		boolean flag=versionService.isExist(versionVo.getVnum(),queryEnterpriseByWhere());
		if(flag==true){
			j.setSuccess(false);
			j.setMsg("添加失败，此版本已存在");
			writeJson(j);
			return ;
		}
		   String fielPath=Constant.APK_PATH;
			String fileName=uploadFileFileName;
			fileName=DateUtils.getFormatMinDate()+fileName;
			if(uploadFileFileName==null){
				j.setMsg("添加失败");
				j.setSuccess(false);
				writeJson(j);
				return;
			}
			fileName=fileName.substring(fileName.lastIndexOf("\\")+1);
			String suffix=fileName.substring(fileName.lastIndexOf(".")+1);
			if(!"apk".equals(suffix)){
				j.setMsg("添加失败");
				j.setSuccess(false);
				writeJson(j);
				return;
			}
			File dir=new File(fielPath);
			File f=null;
	
				if(!dir.exists()){
					//生成目录
					dir.mkdirs();
				}
				//删除服务器已存在的文件
				f=new File(fielPath,fileName);
				if (f.exists()){
					f.delete();
				}
				BufferedOutputStream bos=null;
				BufferedInputStream bis=null;
				FileInputStream fis;
				FileOutputStream fos=null;
				try {
					fis = new FileInputStream(uploadFile);
					bis=new BufferedInputStream(fis);
					 fos=new FileOutputStream(new File(dir,fileName));
					bos=new BufferedOutputStream(fos);
				} catch (FileNotFoundException e) {
				/*	map.put("result",1);
					writeJson(map);
					e.printStackTrace();*/
					return;
				}
				
				byte[] buf=new byte[4096];
				int len=-1;
				try {
					while((len=bis.read(buf))!=-1){
						bos.write(buf,0,len);
						bos.flush();
					}
					versionVo.setVaddress("webpage"+File.separator+"upload"+File.separator+fileName);
					versionVo.setVupdate(DateUtils.getFormatMiniteDate());
					versionVo.setVname(fileName);
					versionVo.setVpublisher(getSessionUserName().getUsername());
					versionVo.setEntid(queryEnterpriseByWhere());
					versionService.addVersion(versionVo);
					
					j.setSuccess(true);
					j.setMsg("添加成功");
					
				} catch (IOException e) {
					e.printStackTrace();
					j.setMsg("添加失败");
					writeJson(j);
					return;
				}finally{
					try {
						bis.close() ;
						fis.close() ;
						bos.close() ;
						fos.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				writeJson(j);
	}
	@LogAnnotation(event="修改版本",tablename="t_version")
	public void editVersion(){
		Json j=new Json();
		try{	
			versionVo.setVupdate(DateUtils.getFormatMiniteDate());
			versionVo.setVpublisher(getSessionUserName().getUsername());
			versionService.editVersion(versionVo);
			j.setMsg("修改成功！");
			j.setSuccess(true);
			
		}catch(Exception e){
			setOperstatus(1);
			j.setMsg("修改失败！");
			logger.error(ExceptionUtil.getExceptionMessage(e));
		}
		writeJson(j);
	}
	
	@LogAnnotation(event="删除版本",tablename="t_version")
	public void removeVersion(){
		Json j=new Json();
		try{
			if(!StringUtils.isEmpty(versionVo.getIds())){
				String path="";
			//	 path=getRequest().getSession().getServletContext().getRealPath("/");
				path=Constant.APK_PATH+File.separator;
				//删除目录文件
				for(String id:versionVo.getIds().split(",")){
					TVersion ver=versionService.getVersion(Integer.parseInt(id));
					//System.out.println(path+ver.getVaddress());
					File f=new File(path+ver.getVaddress());
					if(f.exists()){
						f.delete();
					}
				}
				//删除数据库记录
				versionService.removeVersion(versionVo.getIds());
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
	
	public void isExistFile(){
		Json j=new Json();
		String apkid=getRequest().getParameter("id");
		TVersion ver=versionService.getVersion(Integer.parseInt(apkid));
		if(ver==null){
			j.setSuccess(false);
			writeJson(j);
			j.setMsg("不存在文件");
			return;
		}
		//判断附件是否存在
		String path=Constant.APK_PATH;
		File f =new File(path + File.separator+ ver.getVname());
		if(!f.exists()){
			j.setSuccess(false);
			j.setMsg("不存在文件");
			writeJson(j);
			return;
		}
		j.setSuccess(true);
		writeJson(j);
	}
	/**
	 * 下载apk
	 * @param filename
	 * @throws Exception
	 */
	@LogAnnotation(event="下载版本",tablename="t_version")
	public void downFile() throws Exception{
		Json j=new Json();
		String apkid=getRequest().getParameter("apkid");
		String filename="";
		TVersion ver=versionService.getVersion(Integer.parseInt(apkid));
		if(ver!=null){
			filename=ver.getVname();
		}
	    filename = java.net.URLDecoder.decode(filename, "UTF-8");//解码关键！！！！ 
		InputStream is = null;
		OutputStream os = null;
		String newpath = URLEncoder.encode(filename, "utf-8");
		
		//判断附件是否存在
		String path=Constant.APK_PATH;
		File f =new File(path + File.separator+ filename);
		if(!f.exists()){
			//writeJson("下载失败");
			return ;
			
		}
		try {
			is = new FileInputStream(path + File.separator + filename);
			os = ServletActionContext.getResponse().getOutputStream();
//			getResponse().setContentType("application/x-msdownload");
//			getResponse().setHeader("Content-Disposition", "attachment; filename="+ newpath);
			ServletActionContext.getResponse().setContentType("application/x-msdownload");
			ServletActionContext.getResponse().setHeader("Content-Disposition", "attachment; filename="+ newpath);
			byte[] b = new byte[1024];
			int i = 0;
			while ((i = is.read(b)) != -1) {
				os.write(b, 0, i);
				i = 0;
			}
			os.flush();
			j.setSuccess(true);
			j.setMsg("下载成功！");
		} catch (IOException e) {
			logger.info("apk不存在:"+e.getMessage());
			return ;
			
		} finally {}
  }

	public void isadmin(){
		Json j=new Json();
		boolean flag=false;
		String ids=getRequest().getParameter("ids");
		flag=baseDao.isadmin(TVersion.class,ids,queryEnterpriseByWhere());
		j.setSuccess(flag);
		writeJson(j);
	}
	
}
