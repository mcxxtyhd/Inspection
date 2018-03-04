package com.sptdi.formsbyid;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

public class  GetRemoteFile_Client_GoOn {
 
	private boolean FileExist(String pathAndFile){//确定文件是否已经下载，但没有下载完成
	    File file = new File(pathAndFile);
	    if (file.exists())
	      return true;
	    else
	      return false;
	  }
	private long FileSize(String pathAndFile){//确定已经下载了的文件大小
	    File file = new File(pathAndFile);
	    return file.length();
	  }
	private void FileRename(String fName,String nName){//将下载完全的文件更名，去掉.tp名
	    File file = new File(fName);
	    file.renameTo(new File(nName));
	    file.delete();
	  }
	public static void downloadfile(String args){
	    URL url = null;
	    HttpURLConnection urlc = null;
	    DataOutputStream dos = null;
	    BufferedInputStream bis = null;
	    FileOutputStream fos = null;
	    String localFile = "c:\\数据采集记录单.doc";//文件保存的地方及文件名，具体情况可以改
	    String localFile_bak = localFile + ".tp";//未下载完文件加.tp扩展名，以便于区别
	    GetRemoteFile_Client_GoOn gco = new  GetRemoteFile_Client_GoOn();
	    long fileSize = 0;
	    long start = System.currentTimeMillis();
	    int len = 0;
	    byte[] bt = new byte[1024];
	    RandomAccessFile raFile=null;
	    long TotalSize=0;//要下载的文件总大小
	    try{
	        //url路径不要有中文。
	    	//url = new URL("http://127.0.0.1:8080/inspectionsptdi/webpage/upload/a.doc");
	    	url = new URL("http://10.196.62.17:38888/inspectionsptdi/webpage/upload/a.doc");
	    	urlc = (HttpURLConnection) url.openConnection();
	      TotalSize=Long.parseLong(urlc.getHeaderField("Content-Length"));
	      System.out.println("下载文件大小为:"+TotalSize);
//	      urlc.disconnect();//先断开，下面再连接，否则下面会报已经连接的错误
	      urlc = (HttpURLConnection) url.openConnection();
	      //确定文件是否存在
	      if (gco.FileExist(localFile_bak)){//采用断点续传，这里的依据是看下载文件是否在本地有.tp有扩展名同名文件
	        System.out.println("文件续传中...");
	        fileSize = gco.FileSize(localFile_bak); //取得文件在小，以便确定随机写入的位置
	        System.out.println("fileSize:"+fileSize);
	        //设置User-Agent
	        //urlc.setRequestProperty("User-Agent","NetFox");
	        //设置断点续传的开始位置
	        urlc.setRequestProperty("RANGE", "bytes="+fileSize+"-");
	        //urlc.setRequestProperty("RANGE", "bytes="+fileSize);//这样写不行，不能少了这个"-".
	        //设置接受信息
	        urlc.setRequestProperty("Accept","image/gif,image/x-xbitmap,application/msword,*/*");        
	        raFile=new RandomAccessFile(localFile_bak,"rw");//随机方位读取
	        raFile.seek(fileSize);//定位指针到fileSize位置
	        bis = new BufferedInputStream(urlc.getInputStream());
	        while ((len = bis.read(bt)) > 0){//循环获取文件
	          raFile.write(bt, 0, len);
	        }
	        System.out.println("文件续传接收完毕！");        
	      }
	      else{//采用原始下载
	        fos = new FileOutputStream(localFile_bak); //没有下载完毕就将文件的扩展名命名.bak
	        dos = new DataOutputStream(fos);
	        bis = new BufferedInputStream(urlc.getInputStream());        
	        System.out.println("正在接收文件...");
	        int test=0;
	        while ((len = bis.read(bt)) > 0){//循环获取文件
	          dos.write(bt, 0, len);
	          test++;
	        }        
	      }      
	      System.out.println("共用时：" +  (System.currentTimeMillis() - start) +"毫秒");
	      if(bis!=null)
	        bis.close();
	      if(dos!=null)
	        dos.close();
	      if(fos!=null)
	        fos.close();
	      if(raFile!=null)
	        raFile.close();
	      System.out.println("localFile_bak:"+gco.FileSize(localFile_bak));
	      if(gco.FileSize(localFile_bak)==TotalSize){//下载完毕后，将文件重命名
	        gco.FileRename(localFile_bak,localFile);
	      }
	    //  System.exit(0);
	    }
	    catch (Exception e){
	      try{
	        if(bis!=null)
	          bis.close();
	        if(dos!=null)
	          dos.close();
	        if(fos!=null)
	          fos.close();
	        if(raFile!=null)
	          raFile.close();
	      }
	      catch (IOException f){
	        f.printStackTrace();
	      }
	      e.printStackTrace();
	    }
	   // System.exit(0);
	  }
}