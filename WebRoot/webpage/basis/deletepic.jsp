<%@ page
 contentType="text/html;charset=gbk"
 import="java.io.*,javax.servlet.*,com.inspect.action.basis.MyConverter,com.inspect.action.basis.DB,java.util.*"
 %>

   
  <%  
  System.out.println("deleteOkKKKKKKKKKKKK");
  
  String stpic=request.getParameter("stpic").trim();
  String deletepicname = request.getParameter("deletepicname").trim();
  System.out.println("-->"+stpic);
  System.out.println("-->"+deletepicname);
 // String stpic = "20141108151809";
//  File file=new File("C:\\Program Files (x86)\\Apache Software Foundation\\Tomcat 6.0\\webapps\\inspection\\image\\"+stpic);
File file=new File("C:\\Program Files (x86)\\Apache Software Foundation\\Tomcat 6.0\\webapps\\inspection\\image\\"+stpic+"\\"+deletepicname);
  
    StringBuffer sb=new StringBuffer();   
      
   if(file.exists()){
    boolean d = file.delete();

    if(d){
     System.out.print("É¾³ý³É¹¦£¡");
         sb.append("OK");
	    sb.append("|");
    }else{
     System.out.print("É¾³ýÊ§°Ü£¡");
            sb.append("NG");
	    sb.append("|");
    }
   }
   else{
	   System.out.print("ÒªÉ¾³ýµÄÍ¼Æ¬²»´æÔÚ£¡");
   }
   
   System.out.println("stpic " + stpic);
   System.out.println("C:\\Program Files\\Apache Software Foundation\\Tomcat 6.0\\webapps\\inspection\\image\\"+stpic);
  
   File file1=new File("C:\\Program Files\\Apache Software Foundation\\Tomcat 6.0\\webapps\\inspection\\image\\"+stpic);
   String test[];
    test=file1.list();
  
    System.out.println("test " + test);
 if(test==null||test.length==0){
      System.out.println("*****test==null||test.length==0********");
        sb.append("0");
	    sb.append("|");
 }else{
 System.out.println("*****test != null********");
         sb.append("1");
	    sb.append("|");
 }

	  String s=sb.toString();
	  out.println(MyConverter.escape(s));

%>