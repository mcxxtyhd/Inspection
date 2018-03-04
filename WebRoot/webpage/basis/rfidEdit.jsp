<%@ page language="java" pageEncoding="UTF-8"%>
<div style="padding: 5px;overflow: hidden;">
	<form id="rfidForm" method="post">
	 <input name="id" type="hidden" />
			<table width="400px" height="140px" class="tableForm" >
				<tr>
					<th align="right">标识：</th>
					<td><input name="rname" class="easyui-validatebox" required="true" missingMessage="标识不能为空!"  style="width:220px;height:20px;"/><font color="red">*</font></td>
				</tr>
				<tr>
					<th align="right">描述：</th>
					<td>
					   <textarea name="rdesc" style="height: 90px;width:320px;"><font color="red">*</font></textarea>
					</td>
				</tr>
			</table>
	</form>
</div>