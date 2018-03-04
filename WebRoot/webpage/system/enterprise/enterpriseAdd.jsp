<%@ page language="java" pageEncoding="UTF-8"%>
<div style="padding: 5px;overflow: hidden;">
	<form id="enterpriseForm" method="post">
			<table width="400px" height="220px" class="tableForm" >
				<tr>
					<th align="right">单位名称：</th>
					<td><input name="entname" class="easyui-validatebox" required="true" missingMessage="单位名称不能为空!"  style="width:220px;height:20px;"/><font color="red">*</font></td>
				</tr>
				<tr>
					<th align="right">联系人：</th>
					<td><input name="entlinkperson" class="easyui-validatebox" required="true" missingMessage="联系人不能为空!"  style="width:220px;height:20px;"/><font color="red">*</font></td>
				</tr>
				<tr>
					<th align="right">电话：</th>
					<td><input name="entlinktel" style="width:220px;height:20px;"/></td>
				</tr>
				<tr>
					<th align="right">地址：</th>
					<td><input name="entaddress" style="width:220px;height:20px;"/></td>
				</tr>
				<tr>
					<th align="right">描述：</th>
					<td>
					   <textarea name="entdesc" style="height: 60px;width:320px;"></textarea>
					</td>
				</tr>
			</table>
	</form>
</div>