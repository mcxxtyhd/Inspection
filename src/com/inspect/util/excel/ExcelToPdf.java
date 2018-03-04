package com.inspect.util.excel;


import java.io.*;


import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;


import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

public class ExcelToPdf {
    private String path;
   // public static boolean runFlag=false;
    
 public ExcelToPdf(String path){
  this.path=path;
 }
 
 
 public void listAllFile(String  excelname){
    String fileName="",appdex="";
    File temp=null;

     path=path+File.separator+excelname;
      appdex=".xls";
      //   System.out.println(path+".pdf");
      temp=new File(path+".pdf");//D:/upload/inspect2/11
      if(temp.exists()){
      temp.renameTo(new File(path+"-"+getDateStr()+".pdf"));
      }
     try {
   // 	 ComThread.startMainSTA();
    	  ComThread.InitSTA();
	} catch (Exception e) {
		// TODO: handle exception
		e.printStackTrace();
		//System.out.println(e.getMessage());
	}
    
      //saveExcelAsPdf(path+fileName+appdex,path+fileName+".pdf");
      //excel文件路径
      String filePath = path+appdex;
      String outFile = "";
      
      Dispatch sheet = null;  
      Dispatch sheets = null;  
      ActiveXComponent actcom=new ActiveXComponent("Excel.Application");
      try{
       //System.out.println((new Date()).toString()+"  start convert from : "+filePath+" to "+outFile);
       actcom.setProperty("Visible", new Variant(false));
       Dispatch excels=actcom.getProperty("Workbooks").toDispatch();
       
        Dispatch excel = Dispatch.invoke(excels,"Open",Dispatch.Method,  new Object[]{filePath,new Variant(false),new Variant(false)},  new int[9] ).toDispatch();
        
        sheets= Dispatch.get(excel, "Sheets").toDispatch();
             
             int count = Dispatch.get(sheets, "Count").getInt();  
            // System.out.println(count);
      
             		sheet = Dispatch.invoke(sheets, "Item", Dispatch.Get,new Object[] { new Integer(1) }, new int[1]).toDispatch(); 
             		
             	//	String sheetname = Dispatch.get(sheet, "name").toString();
             		
             		Dispatch.call(sheet, "Activate");	
             		Dispatch.call(sheet, "Select");
             		
             		outFile = path+".pdf";
             		 
             		Dispatch.invoke(excel,"SaveAs",Dispatch.Method,new Object[]{outFile,new Variant(57), new Variant(false),
                		     new Variant(57), new Variant(57),new Variant(false), new Variant(true),new Variant(57), new Variant(false),
                		     new Variant(true), new Variant(false) },new int[1]);
	             
             
             
             Dispatch.call(excel, "Close",new Variant(true));
             
             if(actcom!=null){
          	   
          	   actcom.invoke("Quit",new Variant[]{});
        
          	   actcom=null;
            }
             
             ComThread.Release();
             File ftemp=new File(filePath);
             ftemp.renameTo(new File(filePath));
             ftemp=new File(filePath);
             //temp.deleteOnExit();
             ftemp=null;
          //   System.out.println((new Date()).toString()+"  convert ok : "+filePath+" to "+outFile);
             
      }catch(Exception es){
             es.printStackTrace();
         	System.out.println(es.getMessage());
         }
      

		//获取excel文件路径
	//	String ename=list.get(i);
		//获取文件名称
		//String d0=ename.substring(ename.lastIndexOf(File.separator)+1,ename.indexOf(".xls"));
		//获取pdf文件路径
		String  d2=path+".pdf";
		
		//获取图片路径
	//	String  d3=d0;
		try
			{
		
				if ( true )
				{
					PdfToImage test = new PdfToImage( "jpg",d2, 300 );
					String[] images = test.getImageFiles( );
					//	System.out.println( Arrays.toString( images ) );
				}
				
			}
			catch ( Exception e )
			{
				e.printStackTrace( );
				System.out.println(e.getMessage());
			}
			
	
    
 }
 
 public String getDateStr(){
    Calendar cl=Calendar.getInstance();
    cl.setTime(new Date());
    String str=cl.get(Calendar.YEAR)+""+(cl.get(Calendar.MONTH)+1)+""
    +cl.get(Calendar.DATE)+""+cl.get(Calendar.HOUR)+""+cl.get(Calendar.MINUTE)+""
    +cl.get(Calendar.SECOND); 
    return str;
 }
 }
