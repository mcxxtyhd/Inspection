<%@ page language="java" pageEncoding="UTF-8"%>
<div style="padding: 5px;overflow: hidden;">
	<form id="problemForm" method="post">
	        <table width="400px" height="130px" class="tableForm" >
			<tr>
					<th align="right">巡检项大类名称：</th>
					<td><input name="pgname"  style="width:220px;height:20px;"class="easyui-validatebox" required="true" missingMessage="巡检项大类名称不能为空!"/><font color="red">*</font></td>
				</tr>
				<tr>
					<th align="right">巡检项组描述：</th>
					<td><textarea name="pgdesc" style="height: 60px;width:320px;"></textarea></td>
				</tr>
			</table>
	</form>
</div>
