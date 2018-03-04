<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/tld.jsp"%>
<!DOCTYPE html>
  <jsp:include page="/common/easyui.jsp"></jsp:include>
    <!-- Link JScript-->
  <script type="text/javascript" src="${pageContext.request.contextPath}/plug-in/login/js/jquery-jrumble.js"></script>
  <script type="text/javascript" src="${pageContext.request.contextPath}/plug-in/login/js/jquery.tipsy.js"></script>
  <script type="text/javascript" src="${pageContext.request.contextPath}/plug-in/login/js/iphone.check.js"></script>
  <script type="text/javascript" src="${pageContext.request.contextPath}/plug-in/login/js/login.js"></script>
	  <script type="text/javascript">
	  //判断如果当前页面不为主框架，则将主框架进行跳转
	  	var tagert_URL = "${pageContext.request.contextPath}/system/loginAction!login.action";
	    if(self==top){
	    	window.location.href = tagert_URL;
	    }else{
	    	top.location.href = tagert_URL;
	    }
	  </script>
 </body>
</html>