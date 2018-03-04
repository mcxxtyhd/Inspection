<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/tld.jsp" %>
<div style="padding: 5px;overflow: hidden;">
	<form id="roleForm" method="post">
	 <input name="id" type="hidden" />
			<table width="350px" height="150px" class="tableForm" >
				<tr>
					<th align="right">角色名称：</th>
					<td><input name="rolename" class="easyui-validatebox" required="true" missingMessage="角色名称不能为空!" style="width:220px;height:20px;"/><font color="red">*</font></td>
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
					<th align="right">角色描述：</th>
					<td>
					   <textarea name="roledesc" style="height: 60px;width:280px;"></textarea>
					</td>
				</tr>
			</table>
	</form>
</div>