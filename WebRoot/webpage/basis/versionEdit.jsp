<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/tld.jsp" %>
<div style="padding: 5px;overflow: hidden;">
	<form id="docmentForm" method="post" enctype="multipart/form-data">
		 <input type="hidden" name="id" >
		 <td><input name="vname" type="hidden" style="width:220px;height:20px;"/></td>
		<table width="300px" height="150px" class="tableForm" id="DocumentTable">
				<tr>
					<th align="right">型号：</th>
					<td><input name="vnum"  readonly="readonly" style="width:220px;height:20px;"/></td>
				</tr>
				<tr>
					<th align="right">描述：</th>
					<td colspan="3">
					<textarea name="vdesc" style="height: 50px;width:420px;"></textarea>
					</td>
				</tr>
			</table>
	</form>
</div>