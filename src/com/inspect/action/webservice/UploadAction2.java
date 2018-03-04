package com.inspect.action.webservice;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import org.apache.struts2.ServletActionContext;

import com.alibaba.fastjson.JSON;
import com.inspect.vo.comon.Json;
import com.opensymphony.xwork2.ActionSupport;

public class UploadAction2 extends ActionSupport {
	
	
	// username属性用来封装用户名
	private String username;
	
	private String license;
	
	private String basename;
	
	public String getBasename() {
		return basename;
	}

	public void setBasename(String basename) {
		this.basename = basename;
	}

	public String getLicense() {
		return license;
	}

	public void setLicense(String license) {
		this.license = license;
	}

	// myFile属性用来封装上传的文件
	private File myFile;
	
	// myFileContentType属性用来封装上传文件的类型
	private String myFileContentType;

	// myFileFileName属性用来封装上传文件的文件名
	private String myFileFileName;

	
	//获得username值
	public String getUsername() {
		return username;
	}

	//设置username值
	public void setUsername(String username) {
		this.username = username;
	}

	//获得myFile值
	public File getMyFile() {
		return myFile;
	}

	//设置myFile值
	public void setMyFile(File myFile) {
		this.myFile = myFile;
	}

	//获得myFileContentType值
	public String getMyFileContentType() {
		return myFileContentType;
	}

	//设置myFileContentType值
	public void setMyFileContentType(String myFileContentType) {
		this.myFileContentType = myFileContentType;
	}

	//获得myFileFileName值
	public String getMyFileFileName() {
		return myFileFileName;
	}

	//设置myFileFileName值
	public void setMyFileFileName(String myFileFileName) {
		this.myFileFileName = myFileFileName;
	}

	public String execute() throws Exception {
		Json j=new Json();
		
		System.out.println("uploadaction2");
		System.out.println("license " + license);
		System.out.println("basename " + basename);
		System.out.println("myFile" + myFile);
		//基于myFile创建一个文件输入流
		InputStream is = new FileInputStream(myFile);
		
		// 设置上传文件目录
//C:\Program Files (x86)\Apache Software Foundation\Tomcat 6.0\webapps\inspection\image
		//String uploadPath = ServletActionContext.getServletContext().getRealPath("/image");
		
		String uploadPath = "c:\\inspectionsptdi\\upload\\inspect\\";
		
		System.out.println("uploadPath" + uploadPath);
		// 设置目标文件
		File toFile = new File(uploadPath, this.getMyFileFileName());
		
		// 创建一个输出流
		OutputStream os = new FileOutputStream(toFile);

		//设置缓存
		byte[] buffer = new byte[1024];

		int length = 0;

		//读取myFile文件输出到toFile文件中
		while ((length = is.read(buffer)) > 0) {
			os.write(buffer, 0, length);
		}
		System.out.println("上传用户"+username);
		System.out.println("上传文件名"+myFileFileName);
		System.out.println("上传文件类型"+myFileContentType);
		
		 String url =  "jdbc:mysql://127.0.0.1:3306/inspect",password = "123";
	        String user = "root";
	        Connection conn = null;
	        
	        Class.forName("com.mysql.jdbc.Driver"); 
     	conn = DriverManager.getConnection(url, user, password);

     	Statement stmt = conn.createStatement();
     	String sqlpic = "update t_report_message set mappic  = '" + myFileFileName+"' where xequtnum = '"+license +"'";
 		
     	System.out.println(sqlpic);
     	int rs = stmt.executeUpdate(sqlpic); 
     	if(rs>0){
     		System.out.println("插入数据库mappic成功！");
    		j.setMsg("添加平面图成功！");
    		j.setSuccess(true);
     	}else{
     		System.out.println("插入数据库mappic失败！");
     		j.setMsg("添加平面图失败，请检查任务是否建立！");
    		 
     	}
     	

	//	try {
			String json = JSON.toJSONStringWithDateFormat(j, "yyyy-MM-dd HH:mm:ss");
			System.out.println(json);
			//ServletActionContext.getResponse().setContentType("text/html;charset=utf-8");
			ServletActionContext.getResponse().getWriter().write(json);
			ServletActionContext.getResponse().getWriter().flush();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
		//关闭输入流
		is.close();
		
		//关闭输出流
		os.close();
		
		return SUCCESS;
	}

}
