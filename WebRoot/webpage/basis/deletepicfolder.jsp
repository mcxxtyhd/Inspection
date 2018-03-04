<%@ page
 contentType="text/html;charset=gbk"
 import="java.io.*,javax.servlet.*,com.inspect.action.basis.MyConverter,com.inspect.action.basis.DB,java.util.*"
 %>

   
  <%  
  System.out.println("deletefolderOkKKKKKKKKKKKK");
  
  String stpic=request.getParameter("stpic").trim();
  System.out.println("-->"+stpic);
  //File file=new File("C:\\Program Files (x86)\\Apache Software Foundation\\Tomcat 6.0\\webapps\\inspection\\image\\"+stpic);
  File file=new File("C:\\Program Files (x86)\\Apache Software Foundation\\Tomcat 6.0\\webapps\\inspection\\image\\"+stpic);
  
    StringBuffer sb=new StringBuffer();   
     
      if(file.exists())
      {                    //判断文件是否存在
		    if(file.isFile()){                    //判断是否是文件
		     file.delete();                       //delete()方法 你应该知道 是删除的意思;
		    }
		    else if(file.isDirectory())
		    {              //否则如果它是一个目录
		     File files[] = file.listFiles();               //声明目录下所有的文件 files[];
		     for(int i=0;i<files.length;i++){            //遍历目录下所有的文件
		      files[i].delete();             //把每个文件 用这个方法进行迭代
		     } 
		    } 
		    file.delete(); 
		    
		    sb.append("OK|OK|");
		    System.out.println("删除图片文件夹成功！");
		    
   	 }
     else{ 
            sb.append("NO|NO|");
     	    System.out.println("所删除的文件不存在！"); 
     } 
   
   
      
/*    if(file.exists()){
    boolean d = file.delete();

    if(d){
     System.out.print("删除图片文件夹成功！");
         sb.append("OK|OK|");
    }else{
     System.out.print("删除图片文件夹成功！！");
            sb.append("NG|NG|");
    }
   } 
   else{
    System.out.print("删除图片文件夹不存在！");
         sb.append("NO|NO|");
   
   }
 */

	  String s=sb.toString();
	  out.println(MyConverter.escape(s));

%>