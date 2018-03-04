package com.inspect.action.basis;


	import java.io.File;
	 import org.apache.tools.zip.ZipOutputStream; //�������ant.jar�Ҫ���ٷ�������
	import java.io.FileInputStream;
	 import java.io.FileOutputStream;
	 import java.util.zip.ZipInputStream;
	 import java.util.zip.ZipEntry;


	 public class BuildFolderZip {
	     public BuildFolderZip() {}

 
	     public static boolean zip(String inputFileName,String zipFileName) throws Exception {
 
	    	 
	    	 boolean flag = true;
	        System.out.println("新建压缩文件 :"+zipFileName);
	         zip(zipFileName, new File(inputFileName));
	         return flag;
	     }
 

	     private static boolean zip(String zipFileName, File inputFile) throws Exception {
	    	 boolean flag = true;
	         ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName));
	         zip(out, inputFile, "");
	         System.out.println("新建压缩文件完成！");
	         out.close();
	         return flag;
	     }


	     private static void zip(ZipOutputStream out, File f, String base) throws Exception {
	         if (f.isDirectory()) {
	            File[] fl = f.listFiles();
	            out.putNextEntry(new org.apache.tools.zip.ZipEntry(base + "/"));
	            base = base.length() == 0 ? "" : base + "/";
	            for (int i = 0; i < fl.length; i++) {
	            zip(out, fl[i], base + fl[i].getName());
	          }
	         }else {
	            out.putNextEntry(new org.apache.tools.zip.ZipEntry(base));
	            FileInputStream in = new FileInputStream(f);
	            int b;
	            System.out.println("正在压缩的文件名:"+base);
	            while ( (b = in.read()) != -1) {
	             out.write(b);
	          }
	          in.close();
	        }
	     }


	     public static void main(String [] temp){
	    	 BuildFolderZip book = new BuildFolderZip();
	         try {
	            book.zip("d:\\wx","d:\\wx.zip");//��Ҫѹ�����ļ���
	        }catch (Exception ex) {
	            ex.printStackTrace();
	        }
	     }

	} 
