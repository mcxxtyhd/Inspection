<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="com.sptdi.forms.DataRecord"%>
<%@page import="com.sptdi.forms.UnicomGForm"%>
<%@page import="com.sptdi.forms.UnicomWForm"%>
<%@page import="com.sptdi.forms.UnicomDForm"%>
<%@page import="com.sptdi.forms.cover"%>
 
<%
    

	String name = request.getParameter("TrueName").toString();
   
	response.getWriter().println(name);
	 System.out.println("TrueName： " + name);
   
	
	
	DataRecord dr = new DataRecord();
	dr.create(name);

	UnicomGForm ug = new UnicomGForm();
	 ug.UGForm(name);

	UnicomDForm ud = new UnicomDForm();
	 ud.UDForm(name);

	UnicomWForm uw = new UnicomWForm();
	uw.UWForm(name);
	
	cover  c = new cover();
	c.create(name);
	
	System.out.println("报表已导出！");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
 
  </head>
  
  <body>
    <br>
  </body>
</html>
