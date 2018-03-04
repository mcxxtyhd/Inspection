<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/tld.jsp"%>	
<div style="padding: 5px;overflow: hidden;">
	<form id="problemForm" method="post">
	        <table width="450px" height="330px" class="tableForm" >
					<tr>
					<th align="right">设备编号：</th>
					<td>
					<select name="pronumber" style="width:220px;height:20px;">
					      <option value="">请选择</option>
					      <c:forEach items="${pronumberList}" var="g">
				  		  <option value='<c:out value="${g.id}"/>'><c:out value="${g.gname}"/></option>
				  	    </c:forEach>
					    </select>
					 </td>
				</tr>
				<tr>
					<th align="right">设备名称：</th>
					<td><input name="protype"  style="width:220px;height:20px;"class="easyui-validatebox" required="true" missingMessage="问题类型不能为空!"/><font color="red">*</font></td>
				</tr>
			
				<tr>
					<th align="right">巡检员：</th>
					<td><input name="prosite" style="width:220px;height:20px;"class="easyui-validatebox" required="true" missingMessage="巡检站点不能为空!" /><font color="red">*</font></td>
				</tr>
				<tr>
					<th align="right">所属代维商：</th>
					<td><input name="procycle" style="width:220px;height:20px;" /></td>
				</tr>
					<tr>
					<th align="right">终端编号：</th>
					<td><input name="ternumber" style="width:220px;height:20px;" /></td>
				</tr>
				<tr>
					<th align="right">巡检线路：</th>
					<td> <textarea name="prodesc" style="height: 60px;width:320px;"></textarea></td>
				</tr>
				<tr>
					<th align="right">上报时间：</th>
					<td> <textarea name="prodesc" style="height: 60px;width:320px;"></textarea></td>
				</tr>
			</table>
	</form>
</div>
