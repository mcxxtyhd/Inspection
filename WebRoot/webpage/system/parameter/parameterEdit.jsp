<%@ page language="java" pageEncoding="UTF-8"%>
<div style="padding: 5px;overflow: hidden;">
	<form id="parameterForm" method="post">
	 <input name="id" type="hidden" />
			<table width="400px" height="180px" class="tableForm" >
			   <tr>
					<th align="right">参数名称：</th>
					<td><input name="pname" class="easyui-validatebox" readonly="readonly" required="true" missingMessage="参数名称不能为空!" style="width:220px;height:20px;"/><font color="red">*</font></td>
				</tr>
				<tr>
					<th align="right">参数值：</th>
					<td><input name="pvalue" class="easyui-validatebox" required="true" missingMessage="参数值不能为空!"  style="width:220px;height:20px;"/><font color="red">*</font></td>
				</tr>
				<tr>
					<th align="right">参数描述：</th>
					<td>
					   <textarea name="pdesc" style="height: 60px;width:320px;"></textarea>
					</td>
				</tr>
			</table>
	</form>
</div>