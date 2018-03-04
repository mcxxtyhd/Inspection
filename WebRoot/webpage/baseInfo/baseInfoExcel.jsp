<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/tld.jsp" %>
<div style="padding: 5px;overflow: hidden;">
	<form id="docmentForm" method="post" enctype="multipart/form-data">
		<table width="300px" height="50px" class="tableForm" id="DocumentTable">
				<tr>
					<th align="right">导入文件：</th>
					<td colspan="3">
					<input type="file" name="excelFile" style="width:220px;height:20px;" class="easyui-validatebox" />					
					</td>
					<td>
						<c:if test="${type=='1'}"><a  href="${pageContext.request.contextPath}/excel/tieta.xls" style='color:red;'>模板下载</a></c:if>
						<c:if test="${type=='2'}"> <a  href="${pageContext.request.contextPath}/excel/shinei.xls" style='color:red;'>模板下载</a></c:if>
						<c:if test="${type=='3'}"> <a  href="${pageContext.request.contextPath}/excel/shebei.xls" style='color:red;'>模板下载</a></c:if>
						<c:if test="${type=='4'}"> <a  href="${pageContext.request.contextPath}/excel/dian.xls" style='color:red;'>模板下载</a></c:if>
						<c:if test="${type=='5'}"> <a  href="${pageContext.request.contextPath}/excel/xian.xls" style='color:red;'>模板下载</a></c:if>
					</td>	
				</tr>	
			</table>
	</form>
</div>