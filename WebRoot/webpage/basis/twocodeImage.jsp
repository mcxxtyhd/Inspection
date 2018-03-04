<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/tld.jsp"%>
<script type="text/javascript">
<!--

</script>
<div style="padding: 5px; overflow: hidden;">
	<table width="400px" height="160px"  align="center">
		<tr align="center">
		<td></td>
			<td align="center" id="imagedatastream" >
				<c:if test="${TDC ne null}">
					<img src='${pageContext.request.contextPath}/basis/twocodeAction!getImage.action?tcdid=${TDC.id}' 
					title="二维码信息" height="160px" width="150px" border="0">
				</c:if>
			</td>
		</tr>
	</table>
</div>