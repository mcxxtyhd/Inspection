package com.inspect.util.excel;

import java.io.*;

import java.util.Calendar;
import java.util.Date;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

public class JacobE2P {
    private String path;
   // public static boolean runFlag=false;
    
 public JacobE2P(String path){
  this.path=path;
 }
 
 
 public void listAllFile(){
    String fileName="",appdex="";
    File temp=null;
    try{
     File [] list=new File(path).listFiles(new FileFilter(){
                public boolean accept(File pathname) {
                    boolean x = false;
                    if (pathname.getName().toLowerCase().endsWith(".xls")) {
                         x = true;
                    }
                    return x;
                }
           });
     // System.out.println((new Date()).toString()+"  Total Convert File : "+list.length);
     for(int i=0;i<list.length;i++){
      fileName=list[i].getName().substring(0, list[i].getName().indexOf("."));
      appdex=list[i].getName().substring(list[i].getName().indexOf("."));
      temp=new File(path+fileName+".pdf");
      if(temp.exists()){
      temp.renameTo(new File(path+fileName+"-"+getDateStr()+".pdf"));
      }
      
      ComThread.InitSTA();
      //saveExcelAsPdf(path+fileName+appdex,path+fileName+".pdf");
      String filePath = path+fileName+appdex;
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
             	for (int j = 1; j <= count; j++) {  
             		sheet = Dispatch.invoke(sheets, "Item", Dispatch.Get,new Object[] { new Integer(j) }, new int[1]).toDispatch(); 
             		
             		String sheetname = Dispatch.get(sheet, "name").toString();
             		
             		Dispatch.call(sheet, "Activate");	
             		Dispatch.call(sheet, "Select");
             		
             		outFile = path+fileName+sheetname+ j+".pdf";
             		 
             		Dispatch.invoke(excel,"SaveAs",Dispatch.Method,new Object[]{outFile,new Variant(57), new Variant(false),
                		     new Variant(57), new Variant(57),new Variant(false), new Variant(true),new Variant(57), new Variant(false),
                		     new Variant(true), new Variant(false) },new int[1]);
	             }
             
             
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
         }
      
     }
    }catch(Exception ex){
     ex.printStackTrace();
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