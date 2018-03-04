<%@ page language="java" pageEncoding="UTF-8"%>
<div style="padding: 5px;overflow: hidden;">
	<form id="groupForm" method="post">
	 <input name="id" type="hidden" />
			<table width="400px" height="150px" class="tableForm" >
				<tr>
					<th align="right">维护队名称：</th>
					<td><input name="gname" class="easyui-validatebox" required="true" missingMessage="维护队名称不能为空!" style="width:220px;height:20px;"/><font color="red">*</font></td>
				</tr>
				<tr>
					<th align="right">描述：</th>
					<td>
					   <textarea name="gdesc" style="height: 60px;width:320px;"></textarea>
					</td>
				</tr>
			</table>
	</form>
</div>