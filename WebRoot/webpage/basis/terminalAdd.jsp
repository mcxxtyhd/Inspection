<%@ page language="java" pageEncoding="UTF-8"%>
<div style="padding: 5px;overflow: hidden;">
	<form id="terminalForm" method="post">
	        <table width="500px" height="250px" class="tableForm" >
				<tr>
					<th align="right">终端编号：</th>
					<td><input name="termno" class="easyui-validatebox" required="true" missingMessage="编号不能为空!"  style="width:220px;height:20px;"/><font color="red">*</font></td>
				</tr>
				<tr>
					<th align="right">终端名称：</th>
					<td><input name="tname" class="easyui-validatebox" required="true" missingMessage="终端名称不能为空!" style="width:220px;height:20px;"/><font color="red">*</font></td>
				</tr>
				<tr>
					<th align="right">终端型号：</th>
					<td><input name="tmodel"   style="width:220px;height:20px;"/></td>
				</tr>
				<tr>
					<th align="right">终端类型：</th>
					<td><input name="ttype" style="width:220px;height:20px;"/></td>
				</tr>
				<tr>
					<th align="right">终端号码：</th>
					<td><input name="ttelnumber" style="width:220px;height:20px;"/></td>
				</tr>
				<tr>
					<th align="right">终端厂商：</th>
					<td><input name="tvendor" style="width:220px;height:20px;"/></td>
				</tr>
				<tr>
					<th align="right">备注：</th>
					<td>
					   <textarea name="tnote" style="height: 60px;width:320px;"></textarea>
					</td>
				</tr>
			</table>
	</form>
</div>