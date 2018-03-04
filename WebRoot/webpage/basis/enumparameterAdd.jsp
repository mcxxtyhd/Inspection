<%@ page language="java" pageEncoding="UTF-8"%>
<div style="padding: 5px;overflow: hidden;">
	<form id="parameterForm" method="post">
			<table width="400px" height="180px" class="tableForm" >
			<tr>
					<th align="right">枚举名称：</th>
					<td><input name="pname" class="easyui-validatebox" required="true" missingMessage="枚举名称不能为空!" style="width:220px;height:20px;"/><font color="red">*</font></td>
				</tr>
				<tr>
					<th align="right">枚举值：</th>
					<td><input name="pvalue" class="easyui-validatebox" required="true" missingMessage="枚举值不能为空!"  style="width:220px;height:20px;"/><font color="red">*</font></td>
				</tr>
				<tr>
					<th align="right">枚举标识：</th>
					<td><input name="pflag" style="width:220px;height:20px;"/></td>
				</tr>
				<%--<tr>
					<th align="right">参数类型：</th>
					<td>
					 <select name="ptype" style="width:220px;height:20px;">
					   <option value="">请选择</option>
					   <option value="0">字符串类型</option>
					   <option value="1">数据范围类型</option>
					   <option value="2">其它</option>
					 </select>
					</td>
				</tr>
				--%><tr>
					<th align="right">参数描述：</th>
					<td>
					   <textarea name="pdesc" style="height: 60px;width:320px;"></textarea>
					</td>
				</tr>
			</table>
	</form>
</div>