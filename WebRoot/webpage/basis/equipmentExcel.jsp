<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/tld.jsp" %>
<div style="padding: 5px;overflow: hidden;">
	<form id="docmentForm" method="post" enctype="multipart/form-data">
		<table width="300px" height="50px" class="tableForm" id="DocumentTable">
				<tr>
					<th align="right">导入文件：</th>
					<td colspan="3">
					<input type="file" name="excelFile" style="width:220px;height:20px;" class="easyui-validatebox" required="true" missingMessage="导入文件不能为空!"/>					
					</td>
					<td>
						 <a  href="${pageContext.request.contextPath}/excel/shebei.xls" style='color:red;'>模板下载</a>
					</td>	
				</tr>
			</table>
	</form>
</div>