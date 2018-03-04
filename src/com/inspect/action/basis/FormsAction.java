package com.inspect.action.basis;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;

 
import com.inspect.action.common.BaseAction;
import com.inspect.annotation.LogAnnotation;
import com.inspect.dao.BaseDaoI;
import com.inspect.model.baseinfo.TBaseInfo;
import com.inspect.model.basis.TEnumParameter;
import com.inspect.model.basis.TEquipment;
import com.inspect.model.basis.TEquipmentProjectGroup;
import com.inspect.model.basis.TProject;
import com.inspect.model.basis.TProjectGroup;
import com.inspect.model.system.Forms;
import com.inspect.service.InspectItemServiceI;
import com.inspect.service.SystemServiceI;
import com.inspect.util.common.ExceptionUtil;
import com.inspect.util.common.StringUtils;
import com.inspect.util.excel.Eoip;
import com.inspect.util.excel.Eoiprule;
import com.inspect.vo.basis.EquipmentVo;
import com.inspect.vo.basis.FormsVo;
import com.inspect.vo.comon.Json;
import com.inspect.vo.summary.EquipmentSummaryVo;
import com.opensymphony.xwork2.ModelDriven;

import com.sptdi.formsbyid.DataRecord;
import com.sptdi.formsbyid.GetRemoteFile_Client_GoOn;
import com.sptdi.formsbyid.UnicomGForm;
import com.sptdi.formsbyid.UnicomWForm;
import com.sptdi.formsbyid.UnicomDForm;
import  com.sptdi.formsbyid.cover;

/**
 * 巡检设备业务流程控制Action
 * @version 1.0
 */
@Namespace("/basis")
@Action(value="formsAction",results={
		@Result(name="download",location="/webpage/basis/downloadui.jsp"),
		@Result(name="formsAdd",location="/webpage/basis/formsAdd.jsp"),
		@Result(name="baseFormsExcel",location="/webpage/basis/baseFormsExcel.jsp"),
		@Result(name="addmappic",location="/webpage/basis/formsAddmappic.jsp"),
		@Result(name="formsList",location="/webpage/basis/formsList.jsp")})
public class FormsAction extends BaseAction implements ModelDriven<FormsVo> {

	private static final long serialVersionUID = 6136954790875347573L;
	
	private static final Logger logger = Logger.getLogger(FormsAction.class);
	
	private InspectItemServiceI inspectItemService;
	
	private SystemServiceI systemService;
	
	private FormsVo formsVo=new FormsVo();

	public InspectItemServiceI getInspectItemService() {
		return inspectItemService;
	}
	@Resource
	private BaseDaoI baseDao;
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
	public FormsVo getModel() {
		return formsVo;
	}
	public String baseFormsExcel() {
		String type=getRequest().getParameter("type");
		getRequest().setAttribute("type",type);
		return "baseFormsExcel";
	}
	public void formsExel2db() throws Exception{
		FormsAction fe2d = new FormsAction();
		Json j=new Json();
		//List<Object> db = new ArrayList<Object>();
		
		int flag=Integer.parseInt(getRequest().getParameter("flag"));
		
		System.out.println("flag: " + flag);

		  //设置导入规则
		try {
//			 Eoiprule rule = new Eoiprule();
//			 
//			  rule.setSheetnumber((short)0);		//读第一个sheet
//			  /*
//			   * entity对象字段名与excle列值的映射
//			   * 名称,列值;名称,列值;名称,列值;
//			   */
//			  //室内
//				if(flag==8){
//					rule.setRowcontentspos((short)1);		//数据内容从第2行开始
//					//rule.setValidaterule("1,NotNull;6,0;7,0;10,0");
//					rule.setNametocol("bnumber,1;bname,2;bcity,3;bregion,4;baddress,5;bposx,6;bposy,7;bfactory,8;blevel,9;beqcount,10");	//映射三个字段，
//				}
//				//铁塔
//				else{
//					rule.setRowcontentspos((short)4);		//数据内容从第5行开始
//					//rule.setValidaterule("1,NotNull;6,Exception-Abandon;7,Exception-Abandon;10,0");
//					rule.setValidaterule("1,NotNull;6,0;7,0;10,0");
//					rule.setNametocol("bnumber,1;bcity,2;bregion,3;bname,4;baddress,5;bposx,6;bposy,7;bfactory,8;btowertype,9;bheight,10;btower,11");	//映射三个字段，
//				}
//			  Eoip eoip = new Eoip();
//			  eoip.setErule(rule);
			  
			  //指定导入的数据库表
			  
		//	  uploadFile();
			  //获得bean list，excle每行对应一个bean对象
			//  db = eoip.excel2db(mirror,formsVo.getExcelFile());
			 
			  //加入到数据库中
		  //int entid=getSessionUserName().getEntid();
			
			FormsVo mirror = new FormsVo();  
			
			System.out.println("(formsaction174)formsVo.getExcelFile()得到excel文件：" + formsVo.getExcelFile().getAbsolutePath());
			  
			List<String[]> list = fe2d.readexcle(formsVo.getExcelFile());
			String count1=fe2d.saveList(list);
			
				j.setMsg("导入成功！导入数据条数："+count1);
//				j.setMsg("导入成功！");
				j.setSuccess(true);
		} catch (Exception e) {
			// TODO: handle exception
			j.setMsg("导入失败！");
			j.setSuccess(true);
		}
		writeJson(j);
	}
	
	
	public String formsList(){
		System.out.println("formsList()");
		getRequest().setAttribute("FormsList",systemService.comboboxAdministrativedivision());
		return "formsList";
	}
 
	public void formsDatagrid(){
		
		System.out.println("formsDatagrid()");
		int page =Integer.parseInt(ServletActionContext.getRequest().getParameter("page") );
		int rows =Integer.parseInt(ServletActionContext.getRequest().getParameter("rows") );
		System.out.println("page: "+page+" , rows: " +rows);
		//System.out.println(formsVo.getBasename());
		Map<String, Object> map = inspectItemService.findFormsDatagrid(formsVo,page,rows,"");//querySql());
		//System.out.println(map);
		writeJson(map);
	}

	public String formsAdd(){
		return "formsAdd";
	}
	
	public void addForms(){
		Json j=new Json();
		try{
			Forms f=inspectItemService.getEntityById(Forms.class,Integer.parseInt(formsVo.getLicense()));
			long cnumber=inspectItemService.countFormsNumber(f.getLicense());
			if(cnumber>0){
				 j.setMsg("基站信息已存在,不能重复添加!");
				 setOperstatus(1);
			}else{
				inspectItemService.addForms(formsVo);
				j.setMsg("添加成功！");
				j.setSuccess(true);
			}
		}catch(Exception e){
			setOperstatus(1);
			j.setMsg("添加失败！");
			logger.error(ExceptionUtil.getExceptionMessage(e));
		}
		writeJson(j);
	}
	
	public String formsAddmappic(){
		//已选巡检项列表
		Json j=new Json();
		Forms forms=inspectItemService.getForms(getRequest().getParameter("ID"));  
		String license =  forms.getLicense();
		String basename = forms.getBasename();
		String baseid = forms.getBaseid();
		getRequest().setAttribute("license",license);
		getRequest().setAttribute("basename",basename);
		getRequest().setAttribute("baseid",baseid);
		
//		j.setMsg("添加失败！");
//		writeJson(j);
		
		return "addmappic";
	}
	
	
	public void makeforms(){
		
		System.out.println("makeforms()");
		Json j=new Json();
		try{
			    String url =  "jdbc:mysql://127.0.0.1:3306/inspect",password = "123";
		        String user = "root";
		        Connection conn = null;
		        
		        Class.forName("com.mysql.jdbc.Driver"); 
	        	conn = DriverManager.getConnection(url, user, password);

	        	Statement stmt = conn.createStatement();
	        	String operatorname = null;
	        	String basename = null;
	        	String network1startdate = null;
	        	String network2startdate = null;
	        	String network3startdate = null;
				if(!StringUtils.isEmpty(formsVo.getIds())){
					for(String id : formsVo.getIds().split(","))
					{

              String sql = "select * from unicominfo where id  = "+id;
              ResultSet rs	= stmt.executeQuery(sql);	
              
              System.out.println(sql);
              if(rs.next()){
            	 operatorname = rs.getString("operator"); 
            	 basename = rs.getString("basename");
            	 network1startdate = rs.getString("network1startdate");
            	 network2startdate = rs.getString("network2startdate");
            	 network3startdate = rs.getString("network3startdate");
              }
              
              //2014-06-03 00:00:00.0
              SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM-dd HH:mm:ss" );
              Date date1 = sdf.parse(network1startdate);
              Date date2 = sdf.parse(network1startdate);
              Date date3 = sdf.parse(network1startdate);
              Date temp;
              if (date1.getTime() < date2.getTime()) {
            	  temp =date1;
              }else{
            	  temp =date2;
              }
              
              if (temp.getTime() > date3.getTime()) {
            	  temp =date3;
              }
              String year11 = "2011-12-31 00:00:00.0";
              String year12 = "2012-12-31 00:00:00.0";
              String year13 = "2013-12-31 00:00:00.0";
              Date date11 = sdf.parse(year11);
              Date date12 = sdf.parse(year12);
              Date date13 = sdf.parse(year13);
             
              long y11 = 0;
              long y12 = 0;
              long y13 = 0;
              String y="2013";

               y11 = date11.getTime() - temp.getTime();
               y12 = date12.getTime() - temp.getTime();
               y13 = date13.getTime() - temp.getTime();
 
               if(y11>0&&y12>00&&y13>0)
            	   y="2011";
               else if(y11<0&&y12>0&&y13>0)
            	   y="2012";
               else if(y11<0&&y12<0&&y13>0)
            	   y="2013";
               else if(y11<0&&y12<0&&y13<0)
           	       y="2013";
               
               System.out.println("合格报表年份文件： "+y+".docx");
            		  
        	 
               String oldPath ="C:\\inspectionsptdi\\unicom\\" +y+".doc";
               String newPath ="C:\\inspectionsptdi\\forms\\" +basename +"\\" +y+".doc";
               //环保承诺书
              FormsAction fa = new FormsAction();
              
            		  
               
               
               
                   int formsid = Integer.parseInt(id.trim());
					System.out.println("formsid: " + formsid);	
						
				if(operatorname.equals("联通"))	{
					
					 String oldPath2 ="C:\\inspectionsptdi\\unicom\\上海联通环保承诺书.doc";
		               String newPath2 = "C:\\inspectionsptdi\\forms\\" +basename +"\\上海联通环保承诺书.doc";
		               
					System.out.println("打印联通报表！");	
					
					 fa.copyFile(oldPath, newPath);
					 
	                 fa.copyFile(oldPath2,newPath2);	 
					 
				DataRecord dr = new DataRecord();
				dr.create(formsid);

				
				UnicomGForm ug = new UnicomGForm();
				 ug.UGForm(formsid);

				UnicomDForm ud = new UnicomDForm();
				 ud.UDForm(formsid);

				UnicomWForm uw = new UnicomWForm();
				uw.UWForm(formsid);
				
				cover  c = new cover();
				c.create(formsid);
				
				
			  
				
	 
				}
					else{
						System.out.println("打印电信报表！");
						cover  c = new cover();
						c.create(formsid);
						
						UnicomGForm ug = new UnicomGForm();
						 ug.UGForm(formsid);
						 
						DataRecord dr = new DataRecord();
						dr.create(formsid);
						
						UnicomDForm ud = new UnicomDForm();
						 ud.UDForm(formsid);
						 
						 
					}
					}
					
					/* 
					//压缩后的zip文件路径
					 
					String zipFilePath ="C:\\Program Files (x86)\\Apache Software Foundation\\Tomcat 6.0\\webapps\\inspectionsptdi\\表单.zip";
					  if(FileDelete.DeleteFolder(zipFilePath));
				    	  System.out.println("初始化压缩文件已删除！"); 
		
					String srcpath =  "c:\\inspectionsptdi\\forms\\";
					 File srcFile = new File(srcpath);

				        boolean zipok = false ;
				        if(srcFile.exists()) {

				        	zipok =  BuildFolderZip.zip(srcpath, zipFilePath);

				        }
					
				        if(zipok){
						       if(FileDelete.DeleteFolder(srcpath))
						    	  System.out.println("源文件已删除！"); 
						       
						       if(!srcFile.exists())
						    	   srcFile.mkdirs();
				        }else{
				        	  System.out.println("添加压缩文件失败！"); 
				        }
				        
				        */
				j.setSuccess(true);
				j.setMsg("导出报表成功！");
				
			}
		
		}catch(Exception e){
			setOperstatus(1);
			j.setMsg("导出报表失败！");
			logger.error(ExceptionUtil.getExceptionMessage(e));
		}
		writeJson(j);
	}

 

	 public static void downloadFile(URL theURL, String filePath,String filename) throws IOException {  

	     File dirFile = new File(filePath);
	        if(!dirFile.exists()){//文件路径不存在时，自动创建目录
	          dirFile.mkdir();
	        }
	   //从服务器上获取图片并保存
	      URLConnection  connection = theURL.openConnection();
	      InputStream in = connection.getInputStream();  
	      FileOutputStream os = new FileOutputStream(filePath+filename); 
	      byte[] buffer = new byte[4 * 1024];  
	      int read;  
	      while ((read = in.read(buffer)) > 0) {  
	          os.write(buffer, 0, read);  
	           }  
	        os.close();  
	        in.close();

	   }   
 
	 public String downloadui() throws Exception{
		//压缩后的zip文件路径
		 
			String zipFilePath ="C:\\Program Files (x86)\\Apache Software Foundation\\Tomcat 6.0\\webapps\\inspectionsptdi\\表单.zip";
			  //if(FileDelete.DeleteFolder(zipFilePath));
		    	//  System.out.println("初始化压缩文件已删除！"); 

			String srcpath =  "c:\\inspectionsptdi\\forms\\";
			 File srcFile = new File(srcpath);
			 File flist[] = srcFile.listFiles();  
			 int n = flist.length;
			 System.out.println("要压缩的文件数量：" + n);
			 if(n>0){
				        boolean zipok = false ;
				        if(srcFile.exists()) {
		
				        	zipok =  BuildFolderZip.zip(srcpath, zipFilePath);
		
				        }
					
				        if(zipok){
						       if(FileDelete.DeleteFolder(srcpath))
						    	  System.out.println("源文件已删除！"); 
						       
						       if(!srcFile.exists())
						    	   srcFile.mkdirs();
				        }else{
				        	  System.out.println("添加压缩文件失败！"); 
				        }
			  }
		 return "download";
		
		
	 }
	public void ismakeforms(){
		Json j=new Json();
		boolean flag=true;
		
		j.setSuccess(flag);
		writeJson(j);
	}
	
	/**      * 复制单个文件      * 
	 *       @param oldPath String 原文件路径 如：c:/fqf.txt   
	 *       @param newPath String 复制后路径 如：f:/fqf.txt     
	 *       @return boolean    
	 *       
	      */   
	public  void copyFile(String oldPath, String newPath) {   
		
		System.out.println("拷贝到新文件： " + newPath);
		try {        
			int bytesum = 0;        
			int byteread = 0;        
			File oldfile = new File(oldPath);   
			File newfile = new File(newPath);
			if(!newfile.getParentFile().exists()){
				System.out.println("目标文件不存在，创建目标文件。");
				newfile.getParentFile().mkdirs();
				newfile.createNewFile();
			}
				
			if (oldfile.exists()) {
				System.out.println("源文件存在： " + oldPath);
				//文件存在时            
				InputStream inStream = new FileInputStream(oldPath);
				//读入原文件            
				FileOutputStream fs = new FileOutputStream(newPath);    
				byte[] buffer = new byte[1444];         
				int length;              
				while ( (byteread = inStream.read(buffer)) != -1) {        
					bytesum += byteread; 
					                 
					fs.write(buffer, 0, byteread);       
					}              
				inStream.close();          
				}     
			}        
		catch (Exception e)
		{    
			System.out.println("复制单个文件操作出错");    
			e.printStackTrace();     
			}   
		} 
				 //List<String[]> db
public String saveList(List<String[]> db){
		
		long s=System.currentTimeMillis();
		
 	        String connectStr = "jdbc:mysql://localhost:3306/inspect";
	        String insert_sql = "insert ignore into unicominfo(operator ,license ,datasheet ,baseid ,basename ,administrativedivision ,address ,radius ,longitute ,latitude ,cityx ,cityy ,high ,startdate ,recognitionbookid ,network1 ,network1sectorid ,network1sectornum ,network1angle , network1identificationcode ,network1receiveangle ,network1sendangle ,network1loss ,network1sendfreq ,network1receivefreq ,network1equipmentmodel ,network1cmiitid ,network1equipmentfactory ,network1equipmentnum ,network1transmitpower ,network1powerunit ,network1antennatype ,network1antennamodel ,network1polarization ,network13db ,network1dbi ,network1dbiunit ,network1antennafactory ,network1antennahigh ,network1startdate ,network2 ,network2sectorid ,network2sectornum ,network2angle , network2identificationcode ,network2receiveangle ,network2sendangle ,network2loss ,network2sendfreq ,network2receivefreq ,network2equipmentmodel ,network2cmiitid ,network2equipmentfactory ,network2equipmentnum ,network2transmitpower ,network2powerunit ,network2antennatype ,network2antennamodel ,network2polarization ,network23db ,network2dbi ,network2dbiunit ,network2antennafactory ,network2antennahigh ,network2startdate ,network3 ,network3sectorid ,network3angle ,network3identificationcode ,network3loss ,network3sendfreq ,network3receivefreq ,network3equipmentmodel ,network3cmiitid ,network3equipmentfactory ,network3transmitpower ,network3powerunit ,network3antennatype ,network3antennamodel ,network3polarization ,network3dbi ,network3dbiunit ,network3antennafactory ,network3antennahigh ,network3startdate ,network4 ,network4sectorid ,network4sectornum ,network4angle ,network4identificationcode ,network4receiveangle ,network4sendangle ,network4loss ,network4sendfreq ,network4receivefreq ,network4equipmentmodel ,network4cmiitid ,network4equipmentfactory ,network4equipmentnum ,network4transmitpower ,network4powerunit ,network4antennatype ,network4antennamodel ,network4polarization ,network43db ,network4dbi ,network4dbiunit ,network4antennafactory ,network4antennahigh ,network4startdate ,printdate ,validity ,remark) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	        String charset = "utf-8";
	        Boolean debug = true;
	        String username = "root";
	        String password = "123";
	        Connection conn=null;
	        PreparedStatement prest=null;
		  try {  
		      Class.forName("com.mysql.jdbc.Driver");  
		       conn = DriverManager.getConnection(connectStr, username,password);
		       conn.setAutoCommit(false);  
		       prest = conn.prepareStatement(insert_sql,ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);  
		      for(int x = 0; x < db.size(); x++){ 
		    	  
		    	  String[] info = new String[113];
					
		    	  for(int j=0;j<db.get(x).length;j++){
		    		     info[j] = db.get(x)[j];
						//System.out.print(list.get(i)[j] + " ");
					}
		    	  
 
		    	  prest.setString(1,info[0]);
		    	  prest.setString(2,info[1]);
		    	  prest.setString(3,info[2]);
		    	  prest.setString(4,info[3]);
		    	  prest.setString(5,info[4]);
		    	  prest.setString(6,info[5]);
		    	  prest.setString(7,info[6]);
		    	  prest.setString(8,info[7]);
		    	  prest.setString(9,info[8]);
		    	  prest.setString(10,info[9]);
		    	  prest.setString(11,info[10]);
		    	  prest.setString(12,info[11]);
		    	  prest.setString(13,info[12]);
		    	  prest.setString(14,info[13]);
		    	  prest.setString(15,info[14]);
		    	  prest.setString(16,info[15]);
		    	  prest.setString(17,info[16]);
		    	  prest.setString(18,info[17]);
		    	  prest.setString(19,info[18]);
		    	  prest.setString(20,info[19]);
		    	  prest.setString(21,info[20]);
		    	  prest.setString(22,info[21]);
		    	  prest.setString(23,info[22]);
		    	  prest.setString(24,info[23]);
		    	  prest.setString(25,info[24]);
		    	  prest.setString(26,info[25]);
		    	  prest.setString(27,info[26]);
		    	  prest.setString(28,info[27]);
		    	  prest.setString(29,info[28]);
		    	  prest.setString(30,info[29]);
		    	  prest.setString(31,info[30]);
		    	  prest.setString(32,info[31]);
		    	  prest.setString(33,info[32]);
		    	  prest.setString(34,info[33]);
		    	  prest.setString(35,info[34]);
		    	  prest.setString(36,info[35]);
		    	  prest.setString(37,info[36]);
		    	  prest.setString(38,info[37]);
		    	  prest.setString(39,info[38]);
		    	  prest.setString(40,info[39]);
		    	  prest.setString(41,info[40]);
		    	  prest.setString(42,info[41]);
		    	  prest.setString(43,info[42]);
		    	  prest.setString(44,info[43]);
		    	  prest.setString(45,info[44]);
		    	  prest.setString(46,info[45]);
		    	  prest.setString(47,info[46]);
		    	  prest.setString(48,info[47]);
		    	  prest.setString(49,info[48]);
		    	  prest.setString(50,info[49]);
		    	  prest.setString(51,info[50]);
		    	  prest.setString(52,info[51]);
		    	  prest.setString(53,info[52]);
		    	  prest.setString(54,info[53]);
		    	  prest.setString(55,info[54]);
		    	  prest.setString(56,info[55]);
		    	  prest.setString(57,info[56]);
		    	  prest.setString(58,info[57]);
		    	  prest.setString(59,info[58]);
		    	  prest.setString(60,info[59]);
		    	  prest.setString(61,info[60]);
		    	  prest.setString(62,info[61]);
		    	  prest.setString(63,info[62]);
		    	  prest.setString(64,info[63]);
		    	  prest.setString(65,info[64]);
		    	  prest.setString(66,info[65]);
		    	  prest.setString(67,info[66]);
		    	  prest.setString(68,info[67]);
		    	  prest.setString(69,info[68]);
		    	  prest.setString(70,info[69]);
		    	  prest.setString(71,info[70]);
		    	  prest.setString(72,info[71]);
		    	  prest.setString(73,info[72]);
		    	  prest.setString(74,info[73]);
		    	  prest.setString(75,info[74]);
		    	  prest.setString(76,info[75]);
		    	  prest.setString(77,info[76]);
		    	  prest.setString(78,info[77]);
		    	  prest.setString(79,info[78]);
		    	  prest.setString(80,info[79]);
		    	  prest.setString(81,info[80]);
		    	  prest.setString(82,info[81]);
		    	  prest.setString(83,info[82]);
		    	  prest.setString(84,info[83]);
		    	  prest.setString(85,info[84]);
		    	  prest.setString(86,info[85]);
		    	  prest.setString(87,info[86]);
		    	  prest.setString(88,info[87]);
		    	  prest.setString(89,info[88]);
		    	  prest.setString(90,info[89]);
		    	  prest.setString(91,info[90]);
		    	  prest.setString(92,info[91]);
		    	  prest.setString(93,info[92]);
		    	  prest.setString(94,info[93]);
		    	  prest.setString(95,info[94]);
		    	  prest.setString(96,info[95]);
		    	  prest.setString(97,info[96]);
		    	  prest.setString(98,info[97]);
		    	  prest.setString(99,info[98]);
		    	  prest.setString(100,info[99]);
		    	  prest.setString(101,info[100]);
		    	  prest.setString(102,info[101]);
		    	  prest.setString(103,info[102]);
		    	  prest.setString(104,info[103]);
		    	  prest.setString(105,info[104]);
		    	  prest.setString(106,info[105]);
		    	  prest.setString(107,info[106]);
		    	  prest.setString(108,info[107]);
		    	  prest.setString(109,info[108]);
		    	  prest.setString(110,info[109]);
		    	  prest.setString(111,info[110]);
		    	  prest.setString(112,info[111]);
		    	  prest.setString(113,info[112]);

				  
				  
		          prest.addBatch();  
		      }  
		      prest.executeBatch();  
		     // conn.commit(); 
		      //conn.close();  
		      
		} catch (SQLException ex) {  
			ex.printStackTrace();
		   //Logger.getLogger(MyLogger.class.getName()).log(Level.SEVERE, null, ex);  
		} catch (ClassNotFoundException ex) {  
		     //Logger.getLogger(MyLogger.class.getName()).log(Level.SEVERE, null, ex);
			ex.printStackTrace();
		}finally{
			if(conn!=null){
		        try{    
		        	conn.commit(); 
		        	conn.close() ;    
		        }catch(SQLException e){    
		            e.printStackTrace() ;    
		        } 
			}
			if(prest!=null){
			  try{    
				  prest.close() ;    
		        }catch(SQLException e){    
		            e.printStackTrace() ;    
		        } 
			}
			
		}
		
		long e=System.currentTimeMillis();

    	System.out.println("timespan: " + (e-s) +" 毫秒");
		return "" + db.size();
	}

public List<String[]> readexcle(File file) throws Exception {
	int maxcolnum = 0;

	Workbook wbs = null;

	try {
		wbs = WorkbookFactory.create(new FileInputStream(file));// .create(inp);

	} catch (Exception e) {
		e.printStackTrace();
	}

	Sheet st = wbs.getSheetAt(0);  //读取第几页
	int rnum = 2;     //从第几行开始读

	List<String[]> list = new ArrayList<String[]>();

	int totalExcelColumns = st.getLastRowNum();

	for (int j = rnum; j <= st.getLastRowNum(); j++) {
		Row row = st.getRow(j);
		if (row == null)
			continue;
		int iCellNum = row.getLastCellNum();

		// 如果erule没有设置最大列值，则在此得到该表格的最大列值
		//if (erule.getMaxcolnum() <= 0 && iCellNum > maxcolnum)
			maxcolnum = iCellNum;

		String[] srow = new String[iCellNum];
		for (int i = 0; i < iCellNum; i++) {
			Cell cell = row.getCell(i);
			srow[i] = getcellvalue(cell);
		}
		list.add(srow);
	}

//	if (erule.getMaxcolnum() <= 0)
//		erule.setMaxcolnum(maxcolnum);

	int totalExcelDataLines = list.size();

	return list;

}

// 将excel里面的格式转换为java里面的格式
public String getcellvalue(Cell cell) {
	String value = null;
	// 简单的查检列类型
	if (cell == null)
		return value;

	switch (cell.getCellType()) {
	case Cell.CELL_TYPE_STRING:// 字符串
		value = cell.getRichStringCellValue().getString();
		break;
	case Cell.CELL_TYPE_NUMERIC:// 数字
		// 判断是否是日期型数据
		if (DateUtil.isCellDateFormatted(cell)) {
			Date dTmp = cell.getDateCellValue();
			// 将成日期类型转化字符串
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			value = new String(df.format(dTmp));

		} else {

			CellStyle style = cell.getCellStyle();
			int idd = style.getDataFormat();

			// 2007年07月14日===189;2007年7月14日====185;2007-06-02====188
			if (idd == 185 || idd == 189 || idd == 188) {
				Date dTmp = cell.getDateCellValue();
				// 将成日期类型转化字符串
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				value = new String(df.format(dTmp));

			} else {
				Double d = new Double(cell.getNumericCellValue());
				value = new String(NumberFormat.getInstance().format(d).replace(",", ""));
			}
		}
		break;
	case Cell.CELL_TYPE_BLANK:
		value = "";
		break;
	case Cell.CELL_TYPE_FORMULA:
		value = String.valueOf(cell.getCellFormula());
		break;
	case Cell.CELL_TYPE_BOOLEAN:// boolean型值
		value = String.valueOf(cell.getBooleanCellValue());
		break;
	case Cell.CELL_TYPE_ERROR:
		value = String.valueOf(cell.getErrorCellValue());
		break;
	default:
		break;
	}
	return value;

}
}