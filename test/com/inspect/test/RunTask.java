package com.inspect.test;

import java.io.File;

import com.inspect.util.excel.ExcelToPdf;


public class RunTask {
 public static void main(String[] args) throws Exception {
	 
	    String path="D:/upload/inspect2";		
   	   jacobe2p(path,"560_1");
 }

 
	
	public static void jacobe2p(String rootpath,String excelname){
		 
	       String path=rootpath;	
	          try{
	             File file=new File(path);
	             
	             if(file.exists()){
	            	 
	                ExcelToPdf et=new ExcelToPdf(path);
	                
	                  et.listAllFile(excelname);
	                  
	             }else{
	            	 
	             System.out.println("Path Not Exist,Pls Comfirm: "+path);
	             
	             }
	       }catch(Exception ex){
	    	   
	             System.out.println("Pls Check Your Format,Format Must Be: java com/olive/util/RunTask Path(Exist Path) Frequency(Run Frequency,int)");
	            
	             ex.printStackTrace();
	       }
	 }
}
 