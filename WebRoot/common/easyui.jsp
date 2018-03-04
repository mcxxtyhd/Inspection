<%@ page language="java" pageEncoding="UTF-8"%>
<%
	String easyuiThemeName = "default";
	Cookie cookies[] = request.getCookies();
	if (cookies != null && cookies.length > 0) {
		for (int i = 0; i < cookies.length; i++) {
			if (cookies[i].getName().equals("easyuiThemeName")) {
				easyuiThemeName = cookies[i].getValue();
				break;
			}
		}
	}
%>
<!-- jquery库 -->
<script type="text/javascript" src="${pageContext.request.contextPath}/jslib/jquery-easyui-1.3.1/jquery-1.8.0.min.js" charset="utf-8"></script>
<!-- jquery Cookie插件 -->
<script type="text/javascript" src="${pageContext.request.contextPath}/jslib/jquery.cookie.js" charset="utf-8"></script>

<!-- easyui相关库 -->
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jslib/jquery-easyui-1.3.1/themes/cupertino/easyui.css" rel="stylesheet" title="cupertino">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jslib/jquery-easyui-1.3.1/themes/green/easyui.css" rel="stylesheet" title="green">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jslib/jquery-easyui-1.3.1/themes/orange/easyui.css" rel="stylesheet" title="orange">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jslib/jquery-easyui-1.3.1/themes/default/easyui.css" rel="stylesheet" title="blue">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jslib/jquery-easyui-1.3.1/themes/pink/easyui.css" rel="stylesheet" title="pink">

<link id="easyuiTheme" rel="stylesheet" href="${pageContext.request.contextPath}/jslib/jquery-easyui-1.3.1/themes/<%=easyuiThemeName%>/easyui.css" type="text/css"></link>
<link rel="stylesheet" href="${pageContext.request.contextPath}/jslib/jquery-easyui-1.3.1/themes/icon.css" type="text/css"></link>


<script type="text/javascript" src="${pageContext.request.contextPath}/jslib/jquery-easyui-1.3.1/jquery.easyui.min.js" charset="utf-8"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jslib/jquery-easyui-1.3.1/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>

<!-- easyui插件库 -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/jslib/jquery-easyui-portal/portal.css" type="text/css"></link>
<script type="text/javascript" src="${pageContext.request.contextPath}/jslib/jquery-easyui-portal/jquery.portal.js" charset="utf-8"></script>

<!-- 自定义库 -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/syCss.css" type="text/css"></link>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/custom_form.css" type="text/css"></link>

<script type="text/javascript" src="${pageContext.request.contextPath}/jslib/easyuiUtil.js" charset="utf-8"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jslib/jquery-easyui-1.3.1/easyuiMergeUtils.js" charset="utf-8"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jslib/My97DatePicker/WdatePicker.js"></script>