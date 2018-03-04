<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/tld.jsp"%>
<!DOCTYPE html >
<html>
 <head>
  <title>智能巡检管理系统</title>
  <jsp:include page="/common/easyui.jsp"></jsp:include>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/plug-in/accordion/css/icons.css" type="text/css"></link>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/plug-in/accordion/css/accordion.css" type="text/css"></link>
  <script type="text/javascript" src="${pageContext.request.contextPath}/plug-in/accordion/js/leftmenu.js"></script>
  <style type="text/css">
	a {
		color: Black;
		text-decoration: none;
	}
	
	a:hover {
		color: black;
		text-decoration: none;
	}
</style>
 </head>
 <body id="indexLayout" class="easyui-layout" style="overflow-y: hidden" scroll="no" fit="true">
  <!-- 顶部-->
  <div data-options="region:'north',split:'false',border:'false',href:'${pageContext.request.contextPath}/webpage/layout/north.jsp'" style="BACKGROUND:#E6E6FA;height:77px;padding:1px;overflow:hidden;"></div>
  <!-- 左侧-->
  <div region="west" split="true" href="${pageContext.request.contextPath}/system/loginAction!left.action" title="功能菜单" style="width: 170px; padding: 1px;">
  </div>
  <!-- 中间-->
  <div id="mainPanle" region="center" style="overflow: hidden;">
   <div id="maintabs" class="easyui-tabs" fit="true" border="false">
    <div class="easyui-tab" title="首页" href="${pageContext.request.contextPath}/basis/noticeAction!indexNotice.action" style="padding:2px; overflow: hidden;">
    </div>
   </div>
  </div>
  <!-- 底部 -->
  <div region="south" border="false" style="height: 20px; overflow: hidden;">
    <div align="center" style="color: #CC99FF; padding-top: 2px">
    &copy; 版权所有(推荐火狐浏览器，获得更快响应速度)
   </div>
  </div>
  <div id="mm" class="easyui-menu" style="width:150px;">
        <div id="mm-tabclose">关闭</div>
        <div id="mm-tabcloseall">全部关闭</div> 
        <div id="mm-tabcloseother">除此之外全部关闭</div>
        <div class="menu-sep"></div>
        <div id="mm-tabcloseright">当前页右侧全部关闭</div>
        <div id="mm-tabcloseleft">当前页左侧全部关闭</div>
</div>
 </body>
</html>