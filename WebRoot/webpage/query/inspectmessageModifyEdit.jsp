<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/tld.jsp" %>
<div style="padding: 5px;overflow: hidden;">
	<form id="problemForm" method="post">
	      <input type="hidden" name="id"/>
	       <table width="480px" height="330px" class="tableForm" >
				<tr>
					<th align="right">站点编号：</th>
					<td><input name="xequtnum" id="xequtnum" readonly="readonly"  style="width:250px;height:20px;"/></td>
				</tr>
				<tr>
					<th align="right">站点编号：</th>
					<td><input name="xproname" id="xproname"readonly="readonly"  style="width:250px;height:20px;"/></td>
				</tr>
				<tr>
					<th align="right">巡检值：</th>
					<td> <textarea name="xvalue" style="height: 60px;width:330px;"></textarea></td>
				</tr>
			
			</table>
	</form>
</div>