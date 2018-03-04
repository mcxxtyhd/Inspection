<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/tld.jsp"%>

<div style="padding: 5px;overflow: hidden;">
	<form id="pointForm" method="post">
	 <input name="id" type="hidden" />
			<table width="400px" height="200px" class="tableForm" >
			<tr>
					<th width="20%" align="right">公告名称：</th>
					<td><input name="ntype" class="easyui-validatebox" required="true" missingMessage="维护队名称不能为空!" style="width:220px;height:20px;"/><font color="red">*</font></td>
				</tr>
				<tr>
				<th width="20%">开始日期：</th>
				     <td colspan="4">
				     <input class="Wdate" name="nstarttime" id="beginDate"  onfocus="WdatePicker({startDate:'%y-%M-%d',dateFmt:'yyyy-MM-dd ',alwaysUseStartDate:true})" style="width:120px;height:20px;"/>
				     <font color="red">*</font>
				     </td>
				</tr>
				<tr>
				     <th width="20%">截止日期：</th>
				     <td colspan="4">
				     <input class="Wdate" name="nendtime" id="dissucsdate"  onfocus="WdatePicker({stopDate:'%y-%M-%d',dateFmt:'yyyy-MM-dd',alwaysUseStartDate:false,minDate:'#F{$dp.$D(\'beginDate\')}'})" style="width:120px;height:20px;"/>
					<font color="red">*</font>
					 </td>
				</tr>
				<tr>
					<th width="20%"align="right">公告内容：</th>
					<td>
					   <textarea name="ncontent" style="height: 90px;width:320px;"></textarea>
					</td>
				</tr>
		</table>
	</form>
</div>