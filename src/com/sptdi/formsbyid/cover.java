package com.sptdi.formsbyid;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.sql.DriverManager;
import java.util.ResourceBundle;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;
import com.lowagie.text.Image;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @Class name:    CreateDoc
 * @author: youfeng
 * @modified: 8/29/11
 */
public class cover
{

    public static void create(int id) 
 {
    	//String licensestr = str.trim();
        String url =  "jdbc:mysql://127.0.0.1:3306/inspect",password = "123";
        String user = "root";
        Connection conn = null;
        String sql = null;
        String applyid = null;
    	String basename = null;
    	String printdate = null;
    	 
        
        try
        {
        	Class.forName("com.mysql.jdbc.Driver");// ��̬����mysql��
        	
        	conn = DriverManager.getConnection(url, user, password);
        	Statement stmt = conn.createStatement();
        	//String tableid = "C0382";
           //sql = "select * from unicominfo where license = '310020140005/C0189'";
            sql = "select * from unicominfo where  id = "+id;
    		ResultSet rs = stmt.executeQuery(sql);// executeQuery�᷵�ؽ��ļ��ϣ����򷵻ؿ�ֵ
    		
    		
    		while (rs.next()) {
    			applyid= rs.getString("license");
    			basename= rs.getString("basename");
    			
    			
    		}
 
		
		Date date = new Date();
		SimpleDateFormat sf  = new SimpleDateFormat("yyyy-MM-dd");
		String datestr = sf.format(date);
		
		
		String year = datestr.split("-")[0];
	    String month = datestr.split("-")[1];
	    String day = datestr.split("-")[2];
	    String printdate2 = year +" 年" + month +" 月" + day + " 日" ;
	    
	    System.out.println(printdate2);
	    
        Map<String, Object> map = new HashMap<String, Object>();
   
        map.put("printdate", printdate2);
        map.put("applyid", applyid);
        map.put("basename",basename);
  
       

        
        
        Configuration con=new Configuration();
       // Connection conn = null;
        con.setDirectoryForTemplateLoading(new File("c:\\inspectionsptdi\\xml\\"));//ָ������ģ���λ��
        con.setObjectWrapper(new DefaultObjectWrapper());//ָ�����ģ��ķ�ʽ
        con.setDefaultEncoding("utf-8");//����ģ���ȡ�ı��뷽ʽ�����ڴ�������
        Template template = con.getTemplate("cover.xml");//ģ���ļ���������xml,ftl,html
        template.setEncoding("utf-8");//����д��ģ��ı��뷽ʽ        

        String  filepath = "c:\\inspectionsptdi\\forms\\"+basename+"\\封面.doc";
        
        CreateFileUtil.createFile(filepath);
 
        
        Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filepath),"utf-8"));//����ļ������ 

        
        template.process(map, out);
        
        out.flush();
        out.close();
        
        System.out.println("封面导出成功！");
        
        
     
    }catch(Exception e){
    	
    }
    }


    public static void main(String[] args) throws Exception {
        //cover.create();

    }
}