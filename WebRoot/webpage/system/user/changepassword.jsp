<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/tld.jsp" %>
<!DOCTYPE HTML>
<html>
  <head>
    <title>修改密码</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<jsp:include page="/common/easyui.jsp"></jsp:include>
 </head>
 <body style="overflow-y: hidden" scroll="no">
  <form id="userForm" method="post">
    <input id="id" name="id" type="hidden" value="${user.id}">
    <table width="400px" height="100px" class="tableForm" >
				<tr>
					<th align="right">
						原始密码：
					</th>
					<td>
						<input type="password" name="oldpassword" class="easyui-validatebox" required="true" missingMessage="原密码不能为空!" style="width: 200px; height: 20px;" />
					</td>
				</tr>
				<tr>
					<th align="right">
						新密码：
					</th>
					<td>
						<input type="password" name="newpassword" class="easyui-validatebox" required="true" missingMessage="新密码不能为空!" style="width: 200px; height: 20px;" />
					</td>
				</tr>
				<tr>
					<th align="right">
						重复密码：
					</th>
					<td>
						<input type="password" name="checkpassword" class="easyui-validatebox" style="width: 200px; height: 20px;" data-options="required:'true',missingMessage:'请再次填写密码',validType:'eqPassword[\'#userForm input[name=newpassword]\']'" />
					</td>
				</tr>
			</table>
   </form>
 </body>