<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/tld.jsp" %>
<div style="padding: 5px;overflow: hidden;">
	<form id="inspectuserForm" method="post">
	 <input name="id" type="hidden" />
			<table width="400px" height="270px" class="tableForm" >
				<tr>
					<th align="right">巡检员登陆名：</th>
					<td><input name="iuname" readonly="readonly" class="easyui-validatebox" required="true" missingMessage="名称不能为空!" style="width:220px;height:20px;"/><font color="red">*</font></td>
				</tr>
				<tr>
					<th align="right">登录密码：</th>
					<td><input type="password" name="iupwd" class="easyui-validatebox" required="true" missingMessage="密码不能为空!" style="width:220px;height:20px;"/><font color="red">*</font></td>
				</tr>
				<tr>
					<th align="right">真实名称：</th>
					<td><input name="irealname" class="easyui-validatebox" required="true" missingMessage="真实名称不能为空!" style="width:220px;height:20px;"/><font color="red">*</font></td>
				</tr>
				
				<tr>
					<th align="right">手机号码：</th>
					<td><input name="iumobile" class="easyui-validatebox" required="true" missingMessage="手机号码不能为空!" style="width:220px;height:20px;"/><font color="red">*</font></td>
				</tr>
				<tr>
					<th align="right">性别：</th>
					<td>
					    <select name="iusex" class="easyui-validatebox" required="true" missingMessage="性别不能为空!" style="width:220px;height:20px;">
					      <option value="">请选择</option>
					      <option value="男">男</option>
					      <option value="女">女</option>
					    </select><font color="red">*</font>
					</td>
				</tr>
				<tr>
					<th align="right">所属维护队：</th>
					<td>
					  <select name="groupid" style="width:220px;height:20px;">
					      <option value="">请选择</option>
					      <c:forEach items="${GroupList}" var="g">
				  		  <option value='<c:out value="${g.id}"/>'><c:out value="${g.gname}"/></option>
				  	    </c:forEach>
					    </select>
					</td>
				</tr>
				<tr>
					<th align="right">备注：</th>
					<td>
					   <textarea name="iudesc" style="height: 60px;width:320px;"></textarea>
					</td>
				</tr>
			</table>
	</form>
</div>