<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%

//String name=request.getParameter("TrueName").toString();
//System.out.println("hhhhhhhhh"+name);
%>

<html>
<head><title>登记表</title></head>
<body>
<center>
<h1>打印巡检报表</h1>
<hr>
<form name="teacForm" action="docList2.jsp" method="post">
<table>
<tbody>
<tr>
<td><b>真实姓名</b></td>
<td><input type="text" name="TrueName" size="10"></td>
</tr>
<tr>
<td><b>身份证号</b></td>
<td><input type="text" name="PersonalCode" size="20"></td>
</tr>


<tr>
<td><b>职称</b></td>
<td>
<select name="Post">
<option checked value=""></option>
<option value="教授">教授</option>
<option value="副教授">副教授</option>
<option value="讲师">讲师</option>
<option value="助教">助教</option>
<option value="助读">助读</option>
</select>
</td>
</tr>
</tbody>
</table>
<p align="center">
<input type="button" value="提交" name="Submit">
<input type="reset" value="重填" name="Submit2">
</p>
</form>
</body>
</html>
