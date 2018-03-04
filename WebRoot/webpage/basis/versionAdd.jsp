<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/tld.jsp" %>
<div style="padding: 5px;overflow: hidden;">
	<form id="docmentForm" method="post" enctype="multipart/form-data">
	<td><input name="vname" type="hidden" style="width:220px;height:20px;"/></td>
		<table width="500px" height="160px" class="tableForm" id="DocumentTable">
				<tr>
					<th align="right">版本号：</th>
					<td><input name="vnum" class="easyui-validatebox" required="true" missingMessage="版本不能为空!"  style="width:220px;height:20px;"/><font color="red">*</font></td>
				</tr>
					
				<tr>
					<th align="right" style="width: 100px">导入文件：</th>
					<td colspan="3">
					<input type="file" name="uploadFile" style="width:200px;height:20px;" class="easyui-validatebox" required="true" missingMessage="导入文件不能为空!"/>					
					</td>
				</tr>
				<tr>
					<th align="right">描述：</th>
					<td colspan="3">
					<textarea name="vdesc" style="height: 40px;width:420px;"></textarea>
					</td>
				</tr>
			</table>
	</form>
</div>