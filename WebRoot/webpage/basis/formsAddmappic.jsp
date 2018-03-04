<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/tld.jsp"%>
<script language="javascript">
 
	</script>
<div style="padding: 5px;overflow: hidden;">
	<form  id="addmppicForm" method="post"  enctype="multipart/form-data">
			<table width="530px" height="150px" class="tableForm" >
								<tr>
					<th align="right">申请编号：</th>
					<td><input name="license" style="width:320px;height:15px;" readonly="readonly"/></td>
				</tr>
				<tr>
					<th align="right">基站名称：</th>
					<td><input name="basename" style="width:320px;height:15px;" readonly="readonly"/></td>
				</tr>
				
								<tr>
					<th align="right">基站编号：</th>
					<td><input name="baseid" style="width:320px;height:15px;" readonly="readonly"/></td>
				</tr>
				
				<tr>
				<th align="right">平面图上传：</th>
				<td><input type="file" style="width:320px;height:20px;" name="myFile"/></td>
				</tr>
					
				 
		    </table>
			  
	</form>
</div>