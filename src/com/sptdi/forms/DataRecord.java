package com.sptdi.forms;


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
public class DataRecord {

    public void create(String str) throws Exception {
    	String licensestr = str.trim();
    	System.out.println("licensestr：" + licensestr);
        String url =  "jdbc:mysql://127.0.0.1:3306/inspect",password = "123";
        String user = "root";
        Connection conn = null;
        String sql = null;
        String address = null;
    	String longitute = null;
    	String latitude = null;
    	String high = null;
    	String network1antennahigh = null;
    	String network2antennahigh = null;
    	String network3antennahigh = null;
    	String antennahigh = null;
        
        try
        {
        	Class.forName("com.mysql.jdbc.Driver");// ��̬����mysql��
        	
        	conn = DriverManager.getConnection(url, user, password);
        	if(conn!=null){
        		System.out.println("��ݿ����ӳɹ���");
        	}
        	
        	Statement stmt = conn.createStatement();
        	//String tableid = "C0382";
            //sql = "select * from unicominfo where license = '310020140005/C0189'";
        	
 
        	sql = "select * from unicominfo where license = '"+licensestr+"'";
        	System.out.println("sql " + sql);
    		ResultSet rs = stmt.executeQuery(sql);// executeQuery�᷵�ؽ��ļ��ϣ����򷵻ؿ�ֵ
        	if(rs!=null){
        		System.out.println("���Ϊ�գ�");
        	}
    		
    		while (rs.next()) {
    			address= rs.getString("address");
    			longitute= rs.getString("longitute");
    			latitude= rs.getString("latitude");
    			high= rs.getString("high");
    			network1antennahigh= rs.getString("network1antennahigh");
    			network2antennahigh= rs.getString("network2antennahigh");
    			network3antennahigh= rs.getString("network3antennahigh");
    			
    		}
    		
    		
    		String sqlpic = "select ximgname from t_report_message where license = '"+licensestr+"'";
		String[] picnames = new String[10];
		String  pic11 = null;
		int j=0;
    	String filePath = "D:/xj/formstest/pics2";
	    File root = new File(filePath);
		File[] files = root.listFiles(); 
		for (File file : files) {
			if(!file.getName().endsWith(".png")){
				picnames[j] = filePath +"/"+file.getName();
				j++;
			}
			else{
				pic11 = filePath +"/"+file.getName();
			}
		}
		
		Date date = new Date();
		SimpleDateFormat sf  = new SimpleDateFormat("yyyy-MM-dd");
		String datestr = sf.format(date);
		System.out.println(datestr);
				
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("printyear", datestr.split("-")[0]);
        map.put("printmonth", datestr.split("-")[1]);
        map.put("printday", datestr.split("-")[2]);
        map.put("address", address);
        map.put("longitute",longitute);
        map.put("latitude", latitude);
        map.put("high", high);
        antennahigh = network1antennahigh.substring(0,2)+"/"+network2antennahigh.substring(0,2)+"/"+network3antennahigh.substring(0,2);
        map.put("antennahigh", antennahigh);
        System.out.println(antennahigh);
        
        map.put("image1", getImage(picnames[0]));
        map.put("image2", getImage(picnames[1]));
        map.put("image3", getImage(picnames[2]));
        map.put("image4", getImage(picnames[3]));
        map.put("image5", getImage(picnames[4]));
        map.put("image6", getImage(picnames[5]));
        map.put("image7", getImage(picnames[6]));
        map.put("image8", getImage(picnames[7]));
        map.put("image9", getImage(picnames[8]));
        map.put("image10", getImage(picnames[9]));
        map.put("image11", getImage(pic11));
 
        
        
        Configuration con=new Configuration();
       // Connection conn = null;
        con.setDirectoryForTemplateLoading(new File("D:\\xj\\xjworkspace\\InspectForms\\InspectForms\\src\\com\\test\\"));//ָ������ģ���λ��
        con.setObjectWrapper(new DefaultObjectWrapper());//ָ�����ģ��ķ�ʽ
        con.setDefaultEncoding("utf-8");//����ģ���ȡ�ı��뷽ʽ�����ڴ�������
        Template template = con.getTemplate("jld.xml");//ģ���ļ���������xml,ftl,html
        template.setEncoding("utf-8");//����д��ģ��ı��뷽ʽ        
      
 

        Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("D:\\upload\\数据采集记录单.doc")),"utf-8"));//����ļ������ 

       
        template.process(map, out);
        
        out.flush();
        out.close();
        
        System.out.println("数据采集记录单导出成功！");
        
        
     
    }catch(Exception e){
    	e.printStackTrace();
    }
    }
    private String getImage(String str) {
    	//System.out.println("getImage(String str) ");
        String imgFile = str;// "D:/xj/formstest/pics/��԰.png";
        InputStream in = null;
        byte[] data = null;
        try {
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);
    }
    
    private String getImageStr() {
        String imgFile = "D:/xj/formstest/pics/��԰.png";
        InputStream in = null;
        byte[] data = null;
        try {
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);
    }

    public static void main(String[] args) throws Exception {
        //new DataRecord().create();

    }
}