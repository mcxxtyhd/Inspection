package com.inspect.action.basis;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;

import com.inspect.vo.comon.Json;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 获取Android端上传过来的信息
 * 
 * @author Administrator
 *
 */
//@Namespace("/upload")
//@Action(value="uploadAction",results={
//		@Result(name="docList",location="/webpage/forms/index.jsp")})
public class UploadMappicAction extends ActionSupport {
    // 上传文件域
    private File image;
    // 上传文件类型
    private String imageContentType;
    // 封装上传文件名
    private String imageFileName;
    // 接受依赖注入的属性
    private String savePath;
    private String stpic;
    @Override
    public String execute() {
    	try {
    	  System.out.println("execute()");
        HttpServletRequest request=ServletActionContext.getRequest();
        FileOutputStream fos = null;
        FileInputStream fis = null;
      
        int id = Integer.parseInt(request.getParameter("ID"));
		Json j=new Json();
	 
			//inspectItemService.editEquipment(equipmentVo);
			//上传图片
			//Forms forms=inspectItemService.getForms(getRequest().getParameter("ID"));  
			//String license =  forms.getLicense();
			
			DiskFileItemFactory factory = new DiskFileItemFactory(); // 目的：存放

			// 创建ServletFileUpload
			ServletFileUpload servletFileUpload = new ServletFileUpload(factory); // 解析器

			// 解析
			
				// 获得文件项列表 -- 每一个文件项相当于 一个上传表单组件
				List<FileItem> fileItems = servletFileUpload.parseRequest(request);
				System.out.println(fileItems.size());

				// 查找哪个是上传文件项
				for (FileItem fileItem : fileItems) {
					// 判断fileItem是上传文件
					if (!fileItem.isFormField()) {
						// 上传文件
						InputStream in = fileItem.getInputStream();
						String fileName = fileItem.getName();
						int index = fileName.lastIndexOf("\\");
						if (index != -1) {
							// 截取文件名
							fileName = fileName.substring(index + 1);
						}

						 String url =  "jdbc:mysql://127.0.0.1:3306/inspect",password = "123";
					        String user = "root";
					        Connection conn = null;
					        String license = null;
					        Class.forName("com.mysql.jdbc.Driver"); 
				        	conn = DriverManager.getConnection(url, user, password);
		 
				        	Statement stmt = conn.createStatement();
				        	
				        	String sql  = "Select * from unicominfo  where id = "+id;
				        	
				        	ResultSet rs1 = stmt.executeQuery(sql); 
				        	if(rs1.next()){
				        		 license = rs1.getString("license");
				        	}
				        	String sqlpic = "update t_report_message set mappic  = '" + fileName+"' where license = '"+license +"'";
				    		
				        	System.out.println(sqlpic);
				        	int rs = stmt.executeUpdate(sqlpic); 
				        	//防止查询空。	
				    		if(rs>0){
				    			System.out.println("添加平面图到数据库成功。");
				    		}
				        	
				    		
						FileOutputStream out = new FileOutputStream("D:\\"+ fileName);
						int temp;
						while ((temp = in.read()) != -1) {
							out.write(temp);
						}
						in.close();
						out.close();
					}
				}
				j.setMsg("添加平面图成功！");
				j.setSuccess(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
			
			
		
		
			return "SUCESS";
    }

    /**
     * 文件存放目录
     * 
     * @return
     */
    public String getSavePath() throws Exception{
        return ServletActionContext.getServletContext().getRealPath(savePath); 
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public File getImage() {
        return image;
    }

    public void setImage(File image) {
        this.image = image;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }

    private void close(FileOutputStream fos, FileInputStream fis) {
        if (fis != null) {
            try {
                fis.close();
                fis=null;
            } catch (IOException e) {
                System.out.println("FileInputStream关闭失败");
                e.printStackTrace();
            }
        }
        if (fos != null) {
            try {
                fos.close();
                fis=null;
            } catch (IOException e) {
                System.out.println("FileOutputStream关闭失败");
                e.printStackTrace();
            }
        }
    }

} 