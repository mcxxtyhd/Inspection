package com.sptdi.forms;

import java.io.*;
import java.util.*;

import java.sql.DriverManager;
import java.util.ResourceBundle;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;



import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.ObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;


public class UnicomWForm {
public static void UWForm(String str) {    
	String licensestr = str.trim();
    Configuration con=new Configuration();
    Connection conn = null;
    String url =  "jdbc:mysql://127.0.0.1:3306/inspect",password = "123";
    String user = "root";
	String sql;
	String operator = null;
	String license = null;
	String datasheet = null;
	String baseid = null;
	String basename = null;
	String administrativedivision = null;
	String address = null;
	String radius = null;
	String longitute = null;
	String latitude = null;
	String cityx = null;
	String cityy = null;
	String high = null;
	String startdate = null;
	String recognitionbookid = null;
	String network1 = null;
	String network1sectorid = null;
	String network1sectornum = null;
	String network1angle = null;
	String  network1identificationcode = null;
	String network1receiveangle = null;
	String network1sendangle = null;
	String network1loss = null;
	String network1sendfreq = null;
	String network1receivefreq = null;
	String network1equipmentmodel = null;
	String network1cmiitid = null;
	String network1equipmentfactory = null;
	String network1equipmentnum = null;
	String network1transmitpower = null;
	String network1powerunit = null;
	String network1antennatype = null;
	String network1antennamodel = null;
	String network1polarization = null;
	String network13db = null;
	String network1dbi = null;
	String network1dbiunit = null;
	String network1antennafactory = null;
	String network1antennahigh = null;
	String network1startdate = null;
	String network2 = null;
	String network2sectorid = null;
	String network2sectornum = null;
	String network2angle = null;
	String  network2identificationcode = null;
	String network2receiveangle = null;
	String network2sendangle = null;
	String network2loss = null;
	String network2sendfreq = null;
	String network2receivefreq = null;
	String network2equipmentmodel = null;
	String network2cmiitid = null;
	String network2equipmentfactory = null;
	String network2equipmentnum = null;
	String network2transmitpower = null;
	String network2powerunit = null;
	String network2antennatype = null;
	String network2antennamodel = null;
	String network2polarization = null;
	String network23db = null;
	String network2dbi = null;
	String network2dbiunit = null;
	String network2antennafactory = null;
	String network2antennahigh = null;
	String network2startdate = null;
	String network3 = null;
	String network3sectorid = null;
	String network3angle = null;
	String network3identificationcode = null;
	String network3loss = null;
	String network3sendfreq = null;
	String network3receivefreq = null;
	String network3equipmentmodel = null;
	String network3cmiitid = null;
	String network3equipmentfactory = null;
	String network3transmitpower = null;
	String network3powerunit = null;
	String network3antennatype = null;
	String network3antennamodel = null;
	String network3polarization = null;
	String network3dbi = null;
	String network3dbiunit = null;
	String network3antennafactory = null;
	String network3antennahigh = null;
	String network3startdate = null;
	String network4 = null;
	String network4sectorid = null;
	String network4sectornum = null;
	String network4angle = null;
	String network4identificationcode = null;
	String network4receiveangle = null;
	String network4sendangle = null;
	String network4loss = null;
	String network4sendfreq = null;
	String network4receivefreq = null;
	String network4equipmentmodel = null;
	String network4cmiitid = null;
	String network4equipmentfactory = null;
	String network4equipmentnum = null;
	String network4transmitpower = null;
	String network4powerunit = null;
	String network4antennatype = null;
	String network4antennamodel = null;
	String network4polarization = null;
	String network43db = null;
	String network4dbi = null;
	String network4dbiunit = null;
	String network4antennafactory = null;
	String network4antennahigh = null;
	String network4startdate = null;
	String printdate = null;
	String validity = null;
	String remark = null;

    try {
    	Class.forName("com.mysql.jdbc.Driver");// ��̬����mysql��
    	
    	conn = DriverManager.getConnection(url, user, password);
    	Statement stmt = conn.createStatement();
    	String tableid = "C0382";
    	sql = "select * from unicominfo where license like '"+licensestr+"'";
        //sql = "select * from unicominfo where license = '310020110005/C0382'";
		ResultSet rs = stmt.executeQuery(sql);// executeQuery�᷵�ؽ��ļ��ϣ����򷵻ؿ�ֵ
		
		
		while (rs.next()) {
			operator= rs.getString("operator");
			license= rs.getString("license");
			datasheet= rs.getString("datasheet");
			baseid= rs.getString("baseid");
			basename= rs.getString("basename");
			administrativedivision= rs.getString("administrativedivision");
			address= rs.getString("address");
			radius= rs.getString("radius");
			longitute= rs.getString("longitute");
			latitude= rs.getString("latitude");
			cityx= rs.getString("cityx");
			cityy= rs.getString("cityy");
			high= rs.getString("high");
			startdate= rs.getString("startdate");
			recognitionbookid= rs.getString("recognitionbookid");
			network1= rs.getString("network1");
			network1sectorid= rs.getString("network1sectorid");
			network1sectornum= rs.getString("network1sectornum");
			network1angle= rs.getString("network1angle");
			 network1identificationcode= rs.getString("network1identificationcode");
			network1receiveangle= rs.getString("network1receiveangle");
			network1sendangle= rs.getString("network1sendangle");
			network1loss= rs.getString("network1loss");
			network1sendfreq= rs.getString("network1sendfreq");
			network1receivefreq= rs.getString("network1receivefreq");
			network1equipmentmodel= rs.getString("network1equipmentmodel");
			network1cmiitid= rs.getString("network1cmiitid");
			network1equipmentfactory= rs.getString("network1equipmentfactory");
			network1equipmentnum= rs.getString("network1equipmentnum");
			network1transmitpower= rs.getString("network1transmitpower");
			network1powerunit= rs.getString("network1powerunit");
			network1antennatype= rs.getString("network1antennatype");
			network1antennamodel= rs.getString("network1antennamodel");
			network1polarization= rs.getString("network1polarization");
			network13db= rs.getString("network13db");
			network1dbi= rs.getString("network1dbi");
			network1dbiunit= rs.getString("network1dbiunit");
			network1antennafactory= rs.getString("network1antennafactory");
			network1antennahigh= rs.getString("network1antennahigh");
			network1startdate= rs.getString("network1startdate");
			network2= rs.getString("network2");
			network2sectorid= rs.getString("network2sectorid");
			network2sectornum= rs.getString("network2sectornum");
			network2angle= rs.getString("network2angle");
			 network2identificationcode= rs.getString("network2identificationcode");
			network2receiveangle= rs.getString("network2receiveangle");
			network2sendangle= rs.getString("network2sendangle");
			network2loss= rs.getString("network2loss");
			network2sendfreq= rs.getString("network2sendfreq");
			network2receivefreq= rs.getString("network2receivefreq");
			network2equipmentmodel= rs.getString("network2equipmentmodel");
			network2cmiitid= rs.getString("network2cmiitid");
			network2equipmentfactory= rs.getString("network2equipmentfactory");
			network2equipmentnum= rs.getString("network2equipmentnum");
			network2transmitpower= rs.getString("network2transmitpower");
			network2powerunit= rs.getString("network2powerunit");
			network2antennatype= rs.getString("network2antennatype");
			network2antennamodel= rs.getString("network2antennamodel");
			network2polarization= rs.getString("network2polarization");
			network23db= rs.getString("network23db");
			network2dbi= rs.getString("network2dbi");
			network2dbiunit= rs.getString("network2dbiunit");
			network2antennafactory= rs.getString("network2antennafactory");
			network2antennahigh= rs.getString("network2antennahigh");
			network2startdate= rs.getString("network2startdate");
			network3= rs.getString("network3");
			network3sectorid= rs.getString("network3sectorid");
			network3angle= rs.getString("network3angle");
			network3identificationcode= rs.getString("network3identificationcode");
			network3loss= rs.getString("network3loss");
			network3sendfreq= rs.getString("network3sendfreq");
			network3receivefreq= rs.getString("network3receivefreq");
			network3equipmentmodel= rs.getString("network3equipmentmodel");
			network3cmiitid= rs.getString("network3cmiitid");
			network3equipmentfactory= rs.getString("network3equipmentfactory");
			network3transmitpower= rs.getString("network3transmitpower");
			network3powerunit= rs.getString("network3powerunit");
			network3antennatype= rs.getString("network3antennatype");
			network3antennamodel= rs.getString("network3antennamodel");
			network3polarization= rs.getString("network3polarization");
			network3dbi= rs.getString("network3dbi");
			network3dbiunit= rs.getString("network3dbiunit");
			network3antennafactory= rs.getString("network3antennafactory");
			network3antennahigh= rs.getString("network3antennahigh");
			network3startdate= rs.getString("network3startdate");
			network4= rs.getString("network4");
			network4sectorid= rs.getString("network4sectorid");
			network4sectornum= rs.getString("network4sectornum");
			network4angle= rs.getString("network1angle");
			network4identificationcode= rs.getString("network4identificationcode");
			network4receiveangle= rs.getString("network4receiveangle");
			network4sendangle= rs.getString("network4sendangle");
			network4loss= rs.getString("network4loss");
			network4sendfreq= rs.getString("network4sendfreq");
			network4receivefreq= rs.getString("network4receivefreq");
			network4equipmentmodel= rs.getString("network4equipmentmodel");
			network4cmiitid= rs.getString("network4cmiitid");
			network4equipmentfactory= rs.getString("network4equipmentfactory");
			network4equipmentnum= rs.getString("network4equipmentnum");
			network4transmitpower= rs.getString("network4transmitpower");
			network4powerunit= rs.getString("network4powerunit");
			network4antennatype= rs.getString("network4antennatype");
			network4antennamodel= rs.getString("network4antennamodel");
			network4polarization= rs.getString("network4polarization");
			network43db= rs.getString("network43db");
			network4dbi= rs.getString("network4dbi");
			network4dbiunit= rs.getString("network4dbiunit");
			network4antennafactory= rs.getString("network4antennafactory");
			network4antennahigh= rs.getString("network4antennahigh");
			network4startdate= rs.getString("network4startdate");
			printdate= rs.getString("printdate");
			validity= rs.getString("validity");
			remark= rs.getString("remark");

		}
    	
        con.setDirectoryForTemplateLoading(new File("D:\\xj\\xjworkspace\\InspectForms\\InspectForms\\src\\com\\test\\"));//ָ������ģ���λ��
        con.setObjectWrapper(new DefaultObjectWrapper());//ָ�����ģ��ķ�ʽ
        con.setDefaultEncoding("utf-8");//����ģ���ȡ�ı��뷽ʽ�����ڴ�������
        Template template = con.getTemplate("WCDMA2003_1.xml");//ģ���ļ���������xml,ftl,html
        //System.out.println(template.getEncoding());
        template.setEncoding("utf-8");//����д��ģ��ı��뷽ʽ        
        Map root=new HashMap();//data���
    
        List wcdmadatas = new ArrayList();
        Map wdata = new HashMap();
        
        String applyid = license.split("/")[0];
        String applyid2 = license.split("/")[1];
        wdata.put("applyid2", applyid2);
        wdata.put("applyid", applyid);
        wdata.put("basename",basename);
        wdata.put("baseid", baseid);
        wdata.put("baseaddress", address);
        wdata.put("sectornum", "3");
        wdata.put("longitute",longitute);
        wdata.put("latitude", latitude);
        wdata.put("high", high);
        
        double radiuskm = (double)Integer.parseInt(radius)/1000;
        wdata.put("radius", radiuskm);
        
        String[] datestr = network3startdate.trim().split(" ");//[1];//.split("-");
        String[] datestr2 = datestr[0].split("-");
        String newdate = datestr2[0] + " 年" + datestr2[1] + " 月" + datestr2[2] + " 日" ;
        wdata.put("startdate",newdate);
        wdata.put("sector1","1");
        wdata.put("sector2","2");
        wdata.put("sector3","3");
        
        String[] angles  = network3angle.split(",");
        wdata.put("angle1",angles[0]);
        wdata.put("angle2",angles[1]);
        wdata.put("angle3",angles[2]);
        
        String[] sectorificationcodes  = network1identificationcode.split(",");
        if(sectorificationcodes.length>2){
        wdata.put("sectoridentificationcode1",sectorificationcodes[0]);
        wdata.put("sectoridentificationcode2",sectorificationcodes[1]);
        wdata.put("sectoridentificationcode3",sectorificationcodes[2]);
        }
        else{
            wdata.put("sectoridentificationcode1","");
            wdata.put("sectoridentificationcode2","");
            wdata.put("sectoridentificationcode3","");
        }
        
        wdata.put("receiveangle1","/");//network3receiveangle);
        wdata.put("receiveangle2","/");//network3receiveangle);
        wdata.put("receiveangle3","/");//network3receiveangle);
        wdata.put("sendangle1","/");//network3sendangle);
        wdata.put("sendangle2","/");//network3sendangle);
        wdata.put("sendangle3","/");//network3sendangle);
        
        String[] network3sendfreqs = network3sendfreq.split("-");
        
        wdata.put("sendstartfreq1",network3sendfreqs[0]);
        wdata.put("sendstartfreq2",network3sendfreqs[0]);
        wdata.put("sendstartfreq3",network3sendfreqs[0]);
        wdata.put("sendendfreq1",network3sendfreqs[1]);
        wdata.put("sendendfreq2",network3sendfreqs[1]);
        wdata.put("sendendfreq3",network3sendfreqs[1]);
        
        String[] network3receivefreqs = network3receivefreq.split("-");
        wdata.put("receivestartfreq1",network3receivefreqs[0]);
        wdata.put("receivestartfreq2",network3receivefreqs[0]);
        wdata.put("receivestartfreq3",network3receivefreqs[0]);
        wdata.put("receiveendfreq1",network3receivefreqs[1]);
        wdata.put("receiveendfreq2",network3receivefreqs[1]);
        wdata.put("receiveendfreq3",network3receivefreqs[1]);
        
        
        wdata.put("cmiitid1",network3cmiitid.split(":")[1]);
        wdata.put("equipmentmodel1",network3equipmentmodel);
        wdata.put("equipmentnum1","/");//network3equipmentnum);
        
        if(network1equipmentfactory.trim()==null||network1equipmentfactory.trim().length()==0){
        	network1equipmentfactory = "/";
        }
        wdata.put("equipmentfactory1",network3equipmentfactory);
                   
        wdata.put("transmitpower1",network3transmitpower);
        wdata.put("cmiitid2",network3cmiitid.split(":")[1]);
        wdata.put("equipmentmodel2",network3equipmentmodel);
        wdata.put("equipmentnum2","/");//network3equipmentnum);
        wdata.put("equipmentfactory2",network3equipmentfactory);
        wdata.put("transmitpower2",network3transmitpower);
        wdata.put("cmiitid3",network3cmiitid.split(":")[1]);
        wdata.put("equipmentmodel3",network3equipmentmodel);
        wdata.put("equipmentnum3","/");//network3equipmentnum);
        wdata.put("equipmentfactory3",network3equipmentfactory);
        wdata.put("transmitpower3",network3transmitpower);
        wdata.put("sector11","1");
        wdata.put("antennatype1",network3antennatype);
        wdata.put("antennamodel1",network3antennamodel);
        wdata.put("polarization1",network3polarization);
        wdata.put("angle3db1","");//network33db);
        wdata.put("dbi1",network3dbi);
        wdata.put("antennafactory1",network3antennafactory);
        wdata.put("antennahigh1",network3antennahigh);
        wdata.put("loss1",network3loss);
        wdata.put("sector22","2");
        wdata.put("antennatype2",network3antennatype);
        wdata.put("antennamodel2",network3antennamodel);
        wdata.put("polarization2",network3polarization);
        wdata.put("angle3db2","");//network33db);
        wdata.put("dbi2",network3dbi);
        wdata.put("antennafactory2",network3antennafactory);
        wdata.put("antennahigh2",network3antennahigh);
        wdata.put("loss2",network3loss);
        wdata.put("sector33","3");
        wdata.put("antennatype3",network3antennatype);
        wdata.put("antennamodel3",network3antennamodel);
        wdata.put("polarization3",network3polarization);
        wdata.put("angle3db3","");//network33db);
        wdata.put("dbi3",network3dbi);
        wdata.put("antennafactory3",network3antennafactory);
        wdata.put("antennahigh3",network3antennahigh);
        wdata.put("loss3",network3loss);
       
        
        
        
        wcdmadatas.add(wdata);
        root.put("wcdmadatas", wcdmadatas);
        


        Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("D:\\upload\\联通W网报表.doc")),"utf-8"));//����ļ������ 

        template.process(root, out);//��ģ��д���ļ���
        
        out.flush();
        out.close();
        
        System.out.println("联通W网报表导出成功！");
    } catch (IOException e) {
        // todo auto-generated catch block
        e.printStackTrace();
    }
    catch (TemplateException e) {
        // todo auto-generated catch block
        e.printStackTrace();
    } catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
}