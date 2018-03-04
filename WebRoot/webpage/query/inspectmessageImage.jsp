<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ include file="/common/tld.jsp"%>
<%@ page import="com.inspect.vo.monitor.InspectReportImgVo" %>
<%@ include file="/common/tld.jsp"%>
<%
	List<InspectReportImgVo> d=(List)request.getAttribute("ImgList");
//	System.out.println(d.size()+"00000000000000000000");
    int length=d.size()/4;

 %>
<!DOCTYPE HTML>
<html>
  <head>
    <title>巡检数据查询</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0"> 
  <script type="text/javascript">
  function downFileEdit(imgname){
  //alert(imgname);  
		window.location.replace('${pageContext.request.contextPath}/query/inspectmessageAction!downFile1.action?messgaeDetailIdString='+imgname);		
   }
   </script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/jslib/jquery-1.3.2.min.js"></script><%--
	<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.3.2/jquery.min.js"></script>
	--%><script type="text/javascript" src="${pageContext.request.contextPath}/jslib/jquery.magnifier.js"></script>
</head>
<body scroll="yes">
   	<table style="	overflow: auto;  ">
   	<%for(int i=0;i<=length;i++){ %>
 		 <tr>
			  <c:forEach   items="${ImgList}" var="vo" begin="<%=i*4%>" end="<%=3+i*4%>"   >
			  <div>
			 	 <td  align="center" ><div><img  class="magnify" style="width:195px; height:150px" src="${pageContext.request.contextPath}/query/inspectmessageAction!writeImage.action?messgaeDetailIdString=${vo.imgname}" /></div>
			 	 <div><a style="text-decoration:none"; href="${pageContext.request.contextPath}/query/inspectmessageAction!downFile1.action?messgaeDetailIdString=${vo.imgname}"  onclick="javascript:downFileEdit(${vo.imgname})"> ${vo.imgname}</a></div></td>
			 	</div> 	
			  </c:forEach>
	  	</tr>
	  	<%} %>
   	</table>
   </body>
</html>