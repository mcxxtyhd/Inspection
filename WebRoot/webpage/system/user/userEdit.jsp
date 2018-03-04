<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/tld.jsp" %>
<div style="padding: 5px;overflow: hidden;">
	<form id="userForm" method="post">
	 <input name="id" type="hidden" />
			<table width="400px" height="260px" class="tableForm" >
				<tr>
					<th align="right">用户名称：</th>
					<td><input name="username" readonly="readonly" class="easyui-validatebox" required="true" missingMessage="用户名不能为空!"  style="width:220px;height:20px;"/><font color="red">*</font></td>
				</tr>
				<tr>
					<th align="right">真实姓名：</th>
					<td><input name="realname" class="easyui-validatebox" required="true" missingMessage="真实姓名不能为空!"  style="width:220px;height:20px;"/><font color="red">*</font></td>
				</tr>
				<tr>
					<th align="right">所属角色：</th>
					<td>
					 <select name="roleid" id="roleid" style="width:220px;height:20px;" class="easyui-validatebox" required="true" missingMessage="角色不能为空!" >
					  <option value="">请选择</option>
				         <c:forEach items="${RoleList}" var="role">
				  		 <option value='<c:out value="${role.id}"/>'><c:out value="${role.rolename}"/></option>
				  	    </c:forEach>
					 </select><font color="red">*</font>
					</td>
				</tr>
				<tr>
					<th align="right">所属单位：</th>
					<td>
					 <select name="entid" name="entid" style="width:220px;height:20px;" class="easyui-validatebox" required="true" missingMessage="所属单位不能为空!" >
					    <option value="">请选择</option>
				        <c:forEach items="${EnterpriseList}" var="ent">
				  		<option value='<c:out value="${ent.id}"/>'><c:out value="${ent.entname}"/></option>
				  	   </c:forEach>
					 </select><font color="red">*</font>
					</td>
				</tr>
				<tr>
					<th align="right">性别：</th>
					<td>
					 <select name="sex" name="sex" style="width:220px;height:20px;" class="easyui-validatebox" required="true" missingMessage="性别不能为空!" >
					    <option value="">请选择</option>
					    <option value="男">男</option>
					    <option value="女">女</option>
					 </select><font color="red">*</font>
					</td>
				</tr>
				<tr>
					<th align="right">Email：</th>
					<td><input name="email" class="easyui-validatebox" data-options="validType:'email'" style="width:220px;height:20px;"></td>  
				</tr>
				<tr>
					<th align="right">手机：</th>
					<td><input name="mobile" style="width:220px;height:20px;"/></td>
				</tr>
				<tr>
					<th align="right">用户描述：</th>
					<td>
					   <textarea name="udesc" style="height: 60px;width:280px;"></textarea>
					</td>
				</tr>
			</table>
	</form>
</div>