package com.inspect.action.summary;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

import com.inspect.action.common.BaseAction;
import com.inspect.annotation.LogAnnotation;
import com.inspect.constant.Constant;
import com.inspect.model.baseinfo.TBaseInfo;
import com.inspect.service.InspectItemServiceI;
import com.inspect.service.InspectQueryServiceI;
import com.inspect.service.InspectUserServiceI;
import com.inspect.service.SummaryServiceI;
import com.inspect.service.SystemServiceI;
import com.inspect.util.common.DateUtils;
import com.inspect.util.common.StringUtils;
import com.inspect.util.excel.Eoip;
import com.inspect.util.excel.Eoiprule;
import com.inspect.util.excel.Excel2Pdf;
import com.inspect.util.excel.ExcelToPdf;
import com.inspect.util.excel.JacobE2P;
import com.inspect.util.excel.PdfToImage;
import com.inspect.vo.comon.Json;
import com.inspect.vo.summary.PathVo;
import com.inspect.vo.summary.SummaryFormVo;
import com.opensymphony.xwork2.ModelDriven;

@Namespace("/summary")
@Action(value="summaryFormAction",results={
		@Result(name="summaryFormIndoorList",location="/webpage/summary/summaryFormIndoorList.jsp"),
		@Result(name="summaryFormTowerList",location="/webpage/summary/summaryFormTowerList.jsp"),
		@Result(name="summarySituaList",location="/webpage/summary/summarySituaList.jsp"),
		@Result(name="summaryprint",location="/webpage/summary/summaryprint.jsp"),
		@Result(name="summaryFormTowerView",location="/webpage/summary/summaryFormTowerView.jsp"),
		@Result(name="summaryFormIndoorView",location="/webpage/summary/summaryFormIndoorView.jsp")
		})
public class SummaryFormAction extends BaseAction implements ModelDriven<SummaryFormVo> {
	private static final long serialVersionUID = -5793219728219822288L;
	@Resource
	private InspectUserServiceI inspectUserService;
	
	@Resource
	private InspectItemServiceI inspectItemService;
	@Resource
	private InspectQueryServiceI inspectQueryService;
	
	@Resource
	private SystemServiceI systemService;
	@Resource
	private SummaryServiceI summaryService;
	
	private SummaryFormVo summaryFormvo=new SummaryFormVo();
	

	@Override
	public SummaryFormVo getModel() {
		// TODO Auto-generated method stub
		return summaryFormvo;
	}
	/**
	 * 室内查询统计内容
	 * @return
	 */
	public String summaryFormIndoorList(){
		getRequest().setAttribute("EnterpriseList",systemService.comboboxEnterprise(queryEnterpriseByWhere()));
		getRequest().setAttribute("TaskList",inspectItemService.getPlanTaskList(queryEnterpriseByWhere()));
		getRequest().setAttribute("GroupList",inspectUserService.getGroupList(queryEnterpriseByWhere()));
		getRequest().setAttribute("InspectUserList",inspectUserService.getInspectUserList(queryEnterpriseByWhere()));
		getRequest().setAttribute("EquipmentList",inspectItemService.getEquipmentList(getSessionUserName().getEntid(),"","a"));
		return "summaryFormIndoorList";
	}
	
	/**
	 * 铁塔查询统计内容
	 * @return
	 */
	public String summaryFormTowerList(){
		getRequest().setAttribute("EnterpriseList",systemService.comboboxEnterprise(queryEnterpriseByWhere()));
		getRequest().setAttribute("TaskList",inspectItemService.getPlanTaskList(queryEnterpriseByWhere()));
		getRequest().setAttribute("GroupList",inspectUserService.getGroupList(queryEnterpriseByWhere()));
	//	getRequest().setAttribute("InspectUserList",inspectUserService.getInspectUserList(queryEnterpriseByWhere()));
	//	getRequest().setAttribute("EquipmentList",inspectItemService.getEquipmentList(getSessionUserName().getEntid(),"","a"));
		return "summaryFormTowerList";
	}
	/**
	 * 通过条件查询室内或铁塔统计内容
	 */
	public void summaryFormDatagrid(){
	
		int page =Integer.parseInt(ServletActionContext.getRequest().getParameter("page") );
		int rows =Integer.parseInt(ServletActionContext.getRequest().getParameter("rows") );
		Map<String, Object> map = summaryService.findSummaryFormDatagrid(summaryFormvo,page,rows,querySql());
		writeJson(map);
	}
	
	/**
	 *巡检情况统计 
	 */
	public String summarySituaList()
	{
		getRequest().setAttribute("SituaList",systemService.onlineCount(queryEnterpriseByWhere()));
		return "summarySituaList";
	}
	
	/**
	 * 总导出
	 */
	@LogAnnotation(event="台帐导出",tablename="t_report_message_detail")
	public void toExcel(){

		Json j=new Json();
		String flag = getRequest().getParameter("flag");
		String summanyname="";
		int  sConfigFlag=0;
		 String date=DateUtils.getFormatMinDate();
		 //  System.out.println(date);
		  String path="";
		//如果flag为2，则调用的文件名称为11.xls(室内),否则为22.xls（铁塔）
		if(flag!=null&&flag.equals("2")){
			path=Constant.MODEL_FILE_URL+File.separator+"室内分布及WLAN台账.xlsx";
			summanyname=date+"室内";
		}
		else{
			path=Constant.MODEL_FILE_URL+File.separator+"铁塔、天馈线台账（最新）.xlsx";//模版名称
			summanyname=date+"铁塔、天馈线";
		}
		try{
			String realPath = ServletActionContext.getServletContext().getRealPath("webpage/basis/upload/"+summanyname+".xls");
			String bcity=getRequest().getParameter("bcity");
			if(StringUtils.isNotEmpty(bcity)){
				String bcity1=new String(bcity.getBytes("ISO-8859-1"),"utf-8");
				summaryFormvo.setBcity(bcity1);
			}
			String btype = getRequest().getParameter("btype");//表示设备类型 铁塔和室内
			//	System.out.println(summaryFormvo.getEntid());
			//获取excel数据表的配置内容，1表示获取表里面全部内容
			HashMap<String, Integer> sconfigMap=null;// =summaryService.getSummaryConfigMap("", 1,sConfigFlag);
			//String filePath = summaryService.allSummary1(realPath,summaryFormvo,btype,querySql(),sconfigMap);
			String filePath = summaryService.allSummary(realPath,summaryFormvo,btype,queryEnterpriseByWhere(),sconfigMap);
			//System.out.println("1");
			if(filePath==null){
				downloadFile(  path ,false ,1 ) ;
				return ;
			}
			downloadFile( filePath ,true ,1  ) ;
			//System.out.println("dd");
		}catch(Exception e){
			downloadFile( path ,false ,1  ) ;
		}
	}
	/**
	 * 详情导出
	 */
	@LogAnnotation(event="维护导出",tablename="t_report_message_detail")
	public void InfoList(){

		Json j=new Json();
		try{
			String flag = getRequest().getParameter("flag");
			String summanyname="";
			int  sConfigFlag=0;
			//如果flag为2，则调用的文件名称为11.xls(室内),否则为22.xls（铁塔）
			if(flag!=null&&flag.equals("2")){
				sConfigFlag=2;
				summanyname="室内";
			}
			else{
				sConfigFlag=1;
				summanyname="铁塔、天馈线";
			}
			String realPath = ServletActionContext.getServletContext().getRealPath("webpage/basis/upload/"+summanyname+".xls");
		
			String btype = getRequest().getParameter("btype");//表示设备类型 铁塔和室内
			String ids = getRequest().getParameter("ids");//设备主键id集合
			String taskids = getRequest().getParameter("taskids");//任务id集合
			HashMap<String, String> map=new HashMap<String, String>();
			List<String>bnumberList=new ArrayList<String>();//存放设备主键id
			List<String>taskidList=new ArrayList<String>();//存放设备对应的任务id
			List<String>pathList=new ArrayList<String>();//用来保存解析好的临时文件名称
			if (!StringUtils.isEmpty(ids)) {
				for (String id : ids.split(",")) {
					bnumberList.add(id);
				}
			}
			if (!StringUtils.isEmpty(taskids)) {
				for (String taskid : taskids.split(",")) {
					taskidList.add(taskid);
				}
			}
			//获取excel数据表的配置内容，1表示获取表里面全部内容
			for(int i=0;i<taskidList.size();i++){
				String btaskid=taskidList.get(i);
				String id=bnumberList.get(i);
				String filePath = summaryService.baseinfoListSummary(realPath,btype,btaskid,id,i+1);
				//	System.out.println("000000000000000"+filePath);
				pathList.add(filePath);
			}
			//如果是选中一条记录，则导成excel格式
			if(pathList.size()==1){
				downloadFile(  pathList.get(0) ,true ,0 ) ;
			}
			//如果是选多条记录，则导成zip格式
			else{
				this.downAllDocument(pathList);
			}
			//System.out.println("dd");
		}catch(Exception e){
			setOperstatus(1);
			j.setMsg("导出失败！");
			System.out.println(e.getMessage());
			writeJson(j);
		}
	}
	/**
	 * /将xecel文件双传到临时文件中
	 * @param pathList
	 */
	public void downAllDocument(List<String>pathList){
		Json j=new Json();
		if(pathList==null&&pathList.size()==0){
			setOperstatus(1);
			j.setMsg("导出失败！");
			writeJson(j);
			return ;
		}
		
		List<File> FILESLIST=new ArrayList<File>(pathList.size());
		for(int i=0;i<pathList.size();i++){
			File f=new File(pathList.get(i));
			FILESLIST.add(f);
		}
		
		
		//生成的ZIP文件名为.zip   
        String tmpFileName = "巡检"+".zip";   
        String strZipPath=Constant.TEMP_FILE_URL+File.separator+tmpFileName; //文件批量下载zip附件存储路径
        byte[] buffer = new byte[1024];  
        try{
        	FileOutputStream dd=new FileOutputStream(strZipPath);
		    ZipOutputStream out = new ZipOutputStream(dd);   
            for (int i = 0; i < FILESLIST.size(); i++) {   
            	FileInputStream fis = new FileInputStream(FILESLIST.get(i));
				out.putNextEntry(new ZipEntry(FILESLIST.get(i).getName()));
				// 设置压缩文件内的字符编码，不然会变成乱码
				out.setEncoding("GBK");
				int len;
				// 读入需要下载的文件的内容，打包到zip文件
				while ((len = fis.read(buffer)) > 0) {
					out.write(buffer, 0, len);
				}
				out.closeEntry();
				fis.close();
            }
            out.close(); 
           this.downAllFile(tmpFileName); 
            File file=new File(strZipPath);
			if (file.exists()){
				file.delete();
			}
			//System.out.println("down================================");
	 } catch (Exception e) {   
        	setOperstatus(1);
        	e.getMessage();
        	System.out.println("文件下载出错："+e.getMessage());
        } 
	}
	 /**  
     * 将zip文件下载出来
     */  
    private void downAllFile(String fileName){   
        InputStream ins = null;
        BufferedInputStream bins = null;
        OutputStream outs = null;
        BufferedOutputStream bouts = null;
        String path = Constant.TEMP_FILE_URL+File.separator + fileName;   //存储路径
        try {
            File file = new File(path);   
    
            if (file.exists()) {   
                 ins = new FileInputStream(path);   
                 bins = new BufferedInputStream(ins);// 放到缓冲流里面   
                 outs = ServletActionContext.getResponse().getOutputStream();// 获取文件输出IO流   
                 bouts = new BufferedOutputStream(outs);   
                ServletActionContext.getResponse().setContentType("application/x-download");// 设置response内容的类型   
                ServletActionContext.getResponse().setHeader("Content-disposition","attachment;filename="+ URLEncoder.encode(fileName, "UTF-8"));// 设置头部信息   
                int bytesRead = 0;   
                byte[] buffer = new byte[8192];   
                // 开始向网络传输文件流   
                while ((bytesRead = bins.read(buffer, 0, 8192)) != -1) {   
                    bouts.write(buffer, 0, bytesRead);   
                }   
                bouts.flush();
                ins.close();   
                bins.close();   
                outs.close();   
                bouts.close();   
            } else {   
                bouts.flush();
                ins.close();   
                bins.close();   
                outs.close();   
                bouts.close();  
            	System.out.println("下载的文档文件不存在：");
            }   
        } catch (IOException e) { 
        	System.out.println("文件下载出错："+e.getMessage());
        } 
        finally{
            try {
				bouts.flush();
				 ins.close();   
	             bins.close();   
	             outs.close();   
	             bouts.close(); 
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
        }
    }   
	public void partExcel(){
		
		Json j=new Json();
		try {
			String filePath = getRequest().getParameter("path") ;
			String summanyname="巡检统计";
			downloadFile(  filePath ,true,0  ) ;
		} catch (RuntimeException e) {
			j.setMsg("导出失败！");
			writeJson(j);
		}
	}
	/**
	 *获取某此任务某设备下的巡检项信息
	 * @return
	 */
	public String summaryFormIndoorDetails(){
		getRequest().setAttribute("itaskid",getRequest().getParameter("itaskid") );
		getRequest().setAttribute("bnumber",getRequest().getParameter("bnumber") );
		getRequest().setAttribute("procycle",getRequest().getParameter("procycle") );
		return "summaryFormIndoorView";
	}
	
	/**
	 *获取某此任务某设备下的巡检项信息
	 * @return
	 */
	public String summaryFormTowerDetails(){
		getRequest().setAttribute("itaskid",getRequest().getParameter("itaskid") );
		getRequest().setAttribute("bnumber",getRequest().getParameter("bnumber") );
		getRequest().setAttribute("procycle",getRequest().getParameter("procycle") );
		return "summaryFormTowerView";
	}
	/**
	 * 	//获取某任务某设备下的巡检项信息
	 */
	public void summaryFormDetailsDatagrid(){
		int page =Integer.parseInt(ServletActionContext.getRequest().getParameter("page") );
		int rows =Integer.parseInt(ServletActionContext.getRequest().getParameter("rows") );
		//	System.out.println("itaskid="+getRequest().getParameter("itaskid"));
		//System.out.println("\t="+getRequest().getParameter("bnumber"));
		
		String bnumber=getRequest().getParameter("bnumber");
		String procycle=getRequest().getParameter("procycle");
		String itaskid=getRequest().getParameter("itaskid");
		if(procycle!=null&&procycle.equals("")){
			summaryFormvo.setProcycle(procycle);
		}
		if(itaskid!=null&&!itaskid.equals("")){
			summaryFormvo.setItaskid(Integer.parseInt(itaskid));
		}
		if(bnumber!=null&&!bnumber.equals("")){
			summaryFormvo.setBnumber(bnumber);
		}

		Map<String, Object> map = summaryService.findSummaryFormDetialsDatagrid(summaryFormvo,page,rows,querySql());
		writeJson(map);
	}
	

	/***
	 * 导出excel文件
	 * @param filePath   需要下载文件的路径
	 * @param isDelete   是否需要删除文件的路径下的文件
	 * @author liao
	 * flag     1表示总账导出   0表示非总账导出
	 */
	public void downloadFile(String filePath,boolean isDelete ,int flag){
		InputStream inStream = null;
		OutputStream os = null;
		try {
			// 下载本地文件
			//String fileName = (System.currentTimeMillis() + ".xls").toString(); // 文件的默认保存名
			// 读到流中
			//outputname采用filePath的动态名称（包含设备名称，此前的outoutname是一个固定的名称，后面不会加上设备名称）
			String newpath="";
			String outputName="";
			if(flag==1){
				outputName =  filePath.substring( filePath.toLowerCase().lastIndexOf(File.separator)+1,  filePath.toLowerCase( ).indexOf( ".xlsx" ) );
				newpath= toUtf8String( getRequest() , outputName + ".xlsx" ) ; //URLEncoder.encode(fileName, "utf-8");
			}
			else{
				outputName =  filePath.substring( filePath.toLowerCase().lastIndexOf(File.separator)+1,  filePath.toLowerCase( ).indexOf( ".xls" ) );
			    newpath = toUtf8String( getRequest() , outputName + ".xls" ) ; //URLEncoder.encode(fileName, "utf-8");
			}
			
			//filePath如果不存在则新建0504
			File filePath1 = new File(filePath);
			if(!filePath1.exists()){
				//filePath1.mkdirs();
				filePath1.createNewFile();
			}
			
			
			 inStream = new FileInputStream(filePath);// 文件的存放路径
		 
			
			
			// 设置输出的格式
			getResponse().reset();
			os = ServletActionContext.getResponse().getOutputStream();
			ServletActionContext.getResponse().setContentType("application/x-msdownload;charset=UTF-8");
			ServletActionContext.getResponse().setHeader("Content-Disposition", "attachment; filename=" + newpath);
			
			// 循环取出流中的数据
			byte[] b = new byte[100];
			int len;
			while ((len = inStream.read(b)) > 0) {
				os.write(b, 0, len);
				len = 0;
			}
			os.flush();
			os.close();
			inStream.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				if (os != null) {
					os.close();
					os = null;
				}
				if (inStream != null) {
					inStream.close();
					inStream = null;
				}
				if( isDelete ){
					File file = new File(filePath);
					if (file.isFile() && file.exists()) {
						file.delete();
					}
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}
	
	public void test(){
		summaryService.Test();
	}
	
	
	
	
	public void testConfig() throws Exception{
		Json j=new Json();
		List<Object> db = new ArrayList<Object>();
		
		int flag=Integer.parseInt(getRequest().getParameter("flag"));

		  //设置导入规则
		try {
			 Eoiprule rule = new Eoiprule();
			 
			  rule.setSheetnumber((short)0);		//读第一个sheet
			  /*
			   * entity对象字段名与excle列值的映射
			   * 名称,列值;名称,列值;名称,列值;
			   */
			  //室内
				if(flag==1){
					rule.setRowcontentspos((short)1);		//数据内容从第2行开始
					rule.setNametocol("sname,2;scell,6");	//映射三个字段，
				}
			  Eoip eoip = new Eoip();
			  eoip.setErule(rule);
			  
			  //指定导入的数据库表
			  TBaseInfo mirror = new TBaseInfo();  
			  uploadFile();
			  //获得bean list，excle每行对应一个bean对象
			  db = eoip.excel2db(mirror,summaryFormvo.getExcelFile());
			 
			  //加入到数据库中
			  int entid=getSessionUserName().getEntid();
			String count1=summaryService.saveConfigList(db,entid,flag);
				j.setMsg("导入成功！excel总行数："+eoip.getTotalExcelColumns()+",excel有效数目："+eoip.getTotalExcelDataLines()+",实际保存行数："+count1);
				j.setSuccess(true);
		} catch (Exception e) {
			// TODO: handle exception
			j.setMsg("导入失败！");
			j.setSuccess(true);
		}
		writeJson(j);
	}
	public void uploadFile() throws Exception {
		String realPath = ServletActionContext.getServletContext().getRealPath("webpage/upload");

		if (summaryFormvo.getExcelFile() != null) {
			File uploadfile = new File(new File(realPath),summaryFormvo.getExcelFileFileName());
			if (!uploadfile.getParentFile().exists()) {
				uploadfile.getParentFile().mkdir();
			}
			if (uploadfile.exists()) {
				uploadfile.delete();
			}
			FileUtils.copyFile(summaryFormvo.getExcelFile(),uploadfile);
		}
	}
	

	/**
	 * 铁塔打印
	 */
	@LogAnnotation(event="铁塔打印",tablename="t_report_message_detail")
	public String  towerprint(){
		List<PathVo> piclist=new ArrayList<PathVo>();
		Json j=new Json();
		try{
			String flag = getRequest().getParameter("flag");
			String summanyname="";
			int  sConfigFlag=0;
			//如果flag为2，则调用的文件名称为11.xls(室内),否则为22.xls（铁塔）
			if(flag!=null&&flag.equals("2")){
				sConfigFlag=2;
				summanyname="室内";
			}
			else{
				sConfigFlag=1;
				summanyname="铁塔、天馈线";
			}
			String realPath = ServletActionContext.getServletContext().getRealPath("webpage/basis/upload/"+summanyname+".xls");
		
			String btype = getRequest().getParameter("btype");//表示设备类型 铁塔和室内
			String ids = getRequest().getParameter("ids");//设备主键id集合
			String taskids = getRequest().getParameter("taskids");//任务id集合
			List<String>bnumberList=new ArrayList<String>();//存放设备主键id
			List<String>taskidList=new ArrayList<String>();//存放设备对应的任务id
			List<String>pathList=new ArrayList<String>();//用来保存解析好的临时文件名称
			if (!StringUtils.isEmpty(ids)) {
				for (String id : ids.split(",")) {
					bnumberList.add(id);
				}
			}
			if (!StringUtils.isEmpty(taskids)) {
				for (String taskid : taskids.split(",")) {
					taskidList.add(taskid);
				}
			}
			//获取excel数据表的配置内容，1表示获取表里面全部内容
			for(int i=0;i<taskidList.size();i++){
				String btaskid=taskidList.get(i);
				String id=bnumberList.get(i);
				String filePath = summaryService.baseinfoListSummary("pic",btype,btaskid,id,i+1);
				System.out.println(filePath);
				pathList.add(filePath);
				
			}
			//打印图片
			piclist=this.printPicyure(pathList);

		}catch(Exception e){
			setOperstatus(1);
			j.setMsg("导出失败！");
			writeJson(j);
		}
		getRequest().setAttribute("piclist", piclist);
		return "summaryprint";
	}
	
	/**
	 * 找出打印图片的路径
	 * @param list
	 * @return
	 */
	public List<PathVo> printPicyure(List<String> list){
		List<PathVo> piclist=new ArrayList<PathVo>();
		if(list!=null&&list.size()>0){
			for (int i = 0; i < list.size(); i++) {

				PathVo pa=new PathVo();
				//获取excel文件路径
				String ename=list.get(i);
				//获取文件名称
				String excelname=ename.substring(ename.lastIndexOf(File.separator)+1,ename.indexOf(".xls"));
				//文件所在目录（不包含文件名称）
				String rootpath=ename.substring(0,ename.lastIndexOf(File.separator));
				//生成PdF和图片
				jacobe2p(rootpath,excelname);
				pa.setPicname(excelname);
				piclist.add(pa);

				/*
				PathVo pa=new PathVo();
				//获取excel文件路径
				String ename=list.get(i);
				//获取文件名称
				String d0=ename.substring(ename.lastIndexOf(File.separator)+1,ename.indexOf(".xls"));
				//获取pdf文件路径
				String  d2=Constant.TEMP_FILE_URL+File.separator+d0+".pdf";
				//获取图片路径
				String  d3=d0;
				try
					{
						boolean result = Excel2Pdf.excel2Pdf( new File( ename ),
								new File( d2 ) );
						if ( result )
						{
							PdfToImage test = new PdfToImage( "jpg",d2, 150 );
							String[] images = test.getImageFiles( );
							System.out.println( Arrays.toString( images ) );
						}
						pa.setPicname(d3);
						piclist.add(pa);
					}
					catch ( Exception e )
					{
						e.printStackTrace( );
					}
					
					*/
					
			}
			
		}
	return piclist;
		
	}
	
	public void jacobe2p(String rootpath,String excelname){
		int switche = 0;
		
		
		 
	       String path=rootpath;	
	          try{
	             File file=new File(path);
	             
	             if(file.exists()){
	            	 
	            	 if(switche == 0){	//jacob
	                ExcelToPdf et=new ExcelToPdf(path);
	                
	                  et.listAllFile(excelname);
	            	 }
	            	 else{	//feeling
	            		 String fileprefix = rootpath+File.separator+excelname;
	            		
	            		 boolean result = Excel2Pdf.excel2Pdf( new File( fileprefix+".xls" ),
	            						new File(  fileprefix+".pdf" ) );
	            		 // System.out.println("convert:" +  fileprefix+".xls" );
	 					PdfToImage test = new PdfToImage( "jpg",fileprefix+".pdf", 300 );
						test.getImageFiles( );
	            	 }
	            		 
	                  
	             }else{
	            	 
	             System.out.println("Path Not Exist,Pls Comfirm: "+path);
	             
	             }
	       }catch(Exception ex){
	    	   
	             System.out.println("Pls Check Your Format,Format Must Be: java com/olive/util/RunTask Path(Exist Path) Frequency(Run Frequency,int)");
	            
	             ex.printStackTrace();
	       }
	 }
	/**
	 * 读取图片信息，以显示在页面上
	 * @throws UnsupportedEncodingException 
	 */
	public void writeImage() throws UnsupportedEncodingException{
		String pic = getRequest().getParameter("pic") ;
	
		//pic=new String(pic.getBytes("iso8859-1"),"utf-8");
		
		String pic_path = Constant.TEMP_FILE_URL + File.separator+pic+File.separator +"page1.jpg";
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
	/**
	 * 总账测试
	 */
	@LogAnnotation(event="台帐导出",tablename="t_report_message_detail")
	public void toExcel1(){
		Json j=new Json();
		 String flag = getRequest().getParameter("flag");
			String ecity=getRequest().getParameter("ecity");//城市
			String eregion=getRequest().getParameter("eregion");//区县
			String xequtnum=getRequest().getParameter("xequtnum");//设备编号
			try {
				if(StringUtils.isNotEmpty(ecity)){
					String ecity1;
				ecity1 = new String(ecity.getBytes("ISO-8859-1"),"utf-8");
				summaryFormvo.setEcity(ecity1);
				}
				if(StringUtils.isNotEmpty(eregion)){
					String eregion1=new String(eregion.getBytes("ISO-8859-1"),"utf-8");
					summaryFormvo.setEregion(eregion1);
				}
				if(StringUtils.isNotEmpty(xequtnum)){
					String xequtnum1=new String(xequtnum.getBytes("ISO-8859-1"),"utf-8");
					summaryFormvo.setXequtnum(xequtnum1);
				}
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		 String date=DateUtils.getFormatMinDate();
		 //System.out.println(date);
		 String path="";
		//如果flag为2，则调用的文件名称为11.xls(室内),否则为22.xls（铁塔）
		if(flag!=null&&flag.equals("2")){
			path=Constant.MODEL_FILE_URL+File.separator+"室内分布及WLAN台账.xlsx";
		}
		else{
			path=Constant.MODEL_FILE_URL+File.separator+"铁塔、天馈线台账（最新）.xlsx";//模版名称
		}
		//获取符合条件的base_info主键id集合
		long a=System.currentTimeMillis();
		//String ids=summaryService.getbids(summaryFormvo, querySql());
		String ids="";
		System.out.println("执行ids耗时 : "+(System.currentTimeMillis()-a)/1000f+" 秒 ");
		try{
			/*if(StringUtils.isEmpty(ids)){
				downloadFile( summanyname, path ,false  ) ;
				return;
			}*/
			String btype = getRequest().getParameter("btype");//表示设备类型 铁塔和室内
			//获取excel数据表的配置内容，1表示获取表里面全部内容
			HashMap<String, Integer> sconfigMap=null;// =summaryService.getSummaryConfigMap("", 1,sConfigFlag);
			//String filePath = summaryService.allSummary1(realPath,summaryFormvo,btype,querySql(),sconfigMap);
			String filePath = summaryService.allSummary1(ids,summaryFormvo,btype,queryEnterpriseByWhere(),sconfigMap);
		//	System.out.println("filePath="+filePath);
			if(filePath==null){
				downloadFile( path ,false ,1  ) ;
				return ;
			}
			downloadFile( filePath ,true ,1  ) ;
		}catch(Exception e){
			e.printStackTrace();
			downloadFile( path ,false,1   ) ;
		}
	}
	public void testid(){
		Json j=new Json();
		SummaryFormVo sfVo=new SummaryFormVo();
		String ids=summaryService.getbids(sfVo," 1=1 ");
		//http://localhost:8888/inspection/summary/summaryFormAction!testid
		Map map=new HashMap<String,String>();
		map.put("1", ids);
		writeJson(map);
	}
}
