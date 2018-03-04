<%@ page language="java" pageEncoding="UTF-8"%>

<script type="text/javascript">
//判断如果当前页面不为主框架，则将主框架进行跳转
	   var tagert_URL = "${pageContext.request.contextPath}/system/loginAction!login.action";
		if(self==top){
	    	window.location.href = tagert_URL;
	    }else{
	    	top.location.href = tagert_URL;
	    }
	
</script>

