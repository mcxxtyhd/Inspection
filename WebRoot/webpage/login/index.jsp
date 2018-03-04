<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
  <head>
    <title>巡检信息理系统</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<jsp:include page="/common/easyui.jsp"></jsp:include>
  </head>
  
 <body id="indexLayout" class="easyui-layout" fit="true" scroll="no">
    <div data-options="region:'north',split:'true',border:'false',href:'${pageContext.request.contextPath}/webpage/layout/north.jsp'" style="BACKGROUND:#E6E6FA;height:65px;padding:1px;overflow:hidden;"></div>
	<div data-options="region:'west',border:'false',href:'${pageContext.request.contextPath}/webpage/layout/west.jsp'" style="width: 180px;overflow: hidden;"></div>
	<div data-options="region:'center',border:'false',href:'${pageContext.request.contextPath}/webpage/layout/center.jsp'" style="overflow: hidden;"></div>
	<%--<div data-options="region:'south',border:'false',href:'${pageContext.request.contextPath}/layout/south.jsp'" style="height: 20px;overflow: hidden;"></div>--%>
</body>
</html>
