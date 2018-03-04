<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/tld.jsp" %>
<div style="padding: 5px;overflow: hidden;">
	<form id="planForm" method="post">
	        <table width="400px;" height="240px" class="tableForm" >
				<tr>
					<th align="right">计划名称：</th>
					<td><input name="pname" class="easyui-validatebox" required="true" missingMessage="计划名称不能为空!" style="width:220px;height:20px;"/><font color="red">*</font></td>
				</tr>
				<tr>
					<th align="right">巡检周期：</th>
					<td><input class="Wdate" name="pstartdate" onfocus="WdatePicker({isShowClear:true,readOnly:true,dateFmt:'yyyy-MM'})" style="width:220px;height:20px;"/>
					 <font color="red">*</font></td>
				</tr>
				<tr>
					<th align="right">巡检线路：</th>
					<td>
					<select name="plineid" style="width:220px;height:20px;" class="easyui-validatebox" required="true" missingMessage="巡检线路不能为空!">
					    <option value="">请选择</option>
						<c:forEach var="line" items="${LineList}">
						<option value="${line.id}">${line.lname}</option>
						</c:forEach>
					</select><font color="red">*</font> </td>
				</tr>
				<tr>
					<th align="right">维护队：</th>
					<td>
					<select name="pgid" style="width:220px;height:20px;" class="easyui-validatebox" required="true" missingMessage="维护队不能为空!">
					    <option value="">请选择</option>
						<c:forEach var="group" items="${GroupList}">
						 <option value="${group.id}">${group.gname}</option>
						</c:forEach>
					</select><font color="red">*</font> </td>
				</tr>
				<tr>
					<th align="right">任务描述：</th>
					<td>
					   <textarea name="pdesc" style="height: 60px;width:320px;"></textarea>
					</td>
				</tr>
			</table>
	</form>
</div>