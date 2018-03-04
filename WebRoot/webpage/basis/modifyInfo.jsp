<%@page import="com.inspect.action.basis.MyConverter"%>
<%@ page
 contentType="text/html;charset=gbk"
 import="java.io.*,javax.servlet.*,com.inspect.action.basis.DB,java.util.*"
 %>

   
  <%  
  System.out.println("oskKKKKKKKKKKKK");
  
  String stpic=request.getParameter("stpic").trim();
  System.out.println(stpic);
 // String stpic = "20141108151809";
  //File file=new File("C:\\Program Files (x86)\\Apache Software Foundation\\Tomcat 6.0\\webapps\\inspection\\image\\"+stpic);
  File file=new File("C:\\Program Files (x86)\\Apache Software Foundation\\Tomcat 6.0\\webapps\\inspection\\image\\"+stpic);
  String test[];
  test=file.list();
  
//  if(stpic.equals(0)){
 //      System.out.println("*****stpic.equals(0)********");
//	   out.println("0|0|");
 // }
  
  
 if(test== null){
      System.out.println("*****test == null********");
	  out.println("0|0|");
 }
 else if(test.length == 0){
      System.out.println("*****test.length == 0********");
	  out.println("0|0|");
 }
 else{
      System.out.println("*****test.length********"+test.length);
	  for(int i=0;i<test.length;i++)
	  {
	   System.out.println(test[i]);
	  }
	  
	  StringBuffer sb=new StringBuffer();
	  for(int i=0;i<test.length;i++)
	  {
	    sb.append(test[i]);
	    sb.append("|");
	  }
	  String s=sb.toString();
	  out.println(MyConverter.escape(s));
 }
%>