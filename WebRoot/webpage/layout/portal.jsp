<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>无标题文档</title>
<jsp:include page="/common/easyui.jsp"></jsp:include>
<script type="text/javascript" src="${pageContext.request.contextPath}/plug-in/accordion/js/leftmenu.js" ></script>
<script type="text/javascript" charset="utf-8"><%--
	$(function(){
		$.ajax({
			type: "get",
			url: '<%=path%>/system/freeChartAction!putDocmentFileDate.action',
			success : function(d) {
				$.messager.show({
						title : '提示',
						msg : '删除成功！'
				});
			}
		})
	}


--%>


</script>

</head>

<body>
<div id="contentin">
   <div class="rightmain">
      <div class="lidiv">
         <div class="tai"><span><a href="#" class="btnx" onclick="addTab('文档管理','../repository/docmentAction!docmentList.action','pictures')"><font color="red">更多</font></a></span>规划文档</div>
         <ul>
			<c:forEach var="documentFile" items="${documentFileList}" varStatus="status" >
	            <c:choose> 
		         		<c:when test="${status.count % 2 > 0}">
		         			<c:if test="${documentFile.docfile==null}">
		         				<li class="li1"><a href="#" onclick="downdocmentfile(${documentFile.id})">${documentFile.docfile}</a></li>
		         			</c:if>
		         			<c:if test="${documentFile.docfile!=null}">
		         				<li><a href="#" onclick="downdocmentfile(${documentFile.id})">${documentFile.docfile}</a></li>
		         			</c:if>
		         		</c:when>
		         		<c:otherwise>
		         			<c:if test="${documentFile.docfile==null}">
		         				<li class="lib1"><a href="#" onclick="downdocmentfile(${documentFile.id})">${documentFile.docfile}</a></li>
		         			</c:if>
		         			<c:if test="${documentFile.docfile!=null}">
		         				<li class="lib"><a href="#" onclick="downdocmentfile(${documentFile.id})">${documentFile.docfile}</a></li>
		         			</c:if>
		         		</c:otherwise>
	         	</c:choose> 
         	</c:forEach>
         </ul>
      </div>
      <div class="lidiv">
         <div class="tai"><span><a href="#" onclick="addTab('资料管理','../repository/datumAction!datumList.action','pictures')" class="btnx"><font color="red">更多</font></a></span>规划资料</div>
         <ul>
         	<c:forEach var="datumFile" items="${datumFileList}" varStatus="status" >
         		<c:choose> 
	         		<c:when test="${status.count % 2 > 0}">
	         			<c:if test="${datumFile.datumfile==null}">
	         				<li class="li1"><a href="#" onclick="dowmdatumfile(${datumFile.id})" >${datumFile.datumfile}</a></li>
	         			</c:if>
	         			<c:if test="${datumFile.datumfile!=null}">
	         				<li><a href="#"  onclick="dowmdatumfile(${datumFile.id})" >${datumFile.datumfile}</a></li>
	         			</c:if>
	         		</c:when>
	         		<c:otherwise>
	         			<c:if test="${datumFile.datumfile==null}">
	         				<li class="lib1"><a href="#"  onclick="dowmdatumfile(${datumFile.id})"  >${datumFile.datumfile}</a></li>
	         			</c:if>
	         			<c:if test="${datumFile.datumfile!=null}">
	         				<li class="lib"><a href="#"  onclick="dowmdatumfile(${datumFile.id})"  >${datumFile.datumfile}</a></li>
	         			</c:if>
	         		</c:otherwise>
         		</c:choose>
         	</c:forEach>
         </ul>
      </div>
      <div class="xiandiv">
         <div class="tai"><span><a href="#" class="btnx"></a></span>数据统计</div>
         <div class="xboxmain">
            <div class="xbox" align="center"><p><img src="${pageContext.request.contextPath}/system/freeChartAction!putHistogramdate.action" /></p></div>
            <div class="xbox" align="center"><p><img src="${pageContext.request.contextPath}/system/freeChartAction!putHistogram1.action" /></p></div>
         </div>
      </div>
   </div>
</div>
</body>
</html>
