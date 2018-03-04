<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/tld.jsp" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

  
 
    <body>
		<form  name="formLogin1" id="formLogin1" action="${pageContext.request.contextPath}/web/webAction!interterminalPicture.action" method="post" enctype="multipart/form-data">
			<table>
				<tr>
					<td><input type="file" name="uploadFile">  </td>
					<td><input type="submit" value="提交" /></td>

					
				</tr>
			</table>
		<form>
	</body>
  
</html>
