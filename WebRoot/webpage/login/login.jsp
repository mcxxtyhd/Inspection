<%@ page language="java"  pageEncoding="UTF-8"%>
<%@include file="/common/tld.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title></title>
  <jsp:include page="/common/easyui.jsp"></jsp:include>
  <link href="${pageContext.request.contextPath}/plug-in/login/css/zice.style.css" rel="stylesheet" type="text/css" />
  <link href="${pageContext.request.contextPath}/plug-in/login/css/buttons.css" rel="stylesheet" type="text/css" />
  <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/plug-in/login/css/tipsy.css" media="all" />
  <style type="text/css">
html {
	background-image: none;
}

label.iPhoneCheckLabelOn span {
	padding-left: 0px
}

#versionBar {
	background-color: #212121;
	position: fixed;
	width: 100%;
	height: 35px;
	bottom: 0;
	left: 0;
	text-align: center;
	line-height: 35px;
	z-index: 11;
	-webkit-box-shadow: black 0px 10px 10px -10px inset;
	-moz-box-shadow: black 0px 10px 10px -10px inset;
	box-shadow: black 0px 10px 10px -10px inset;
}

.copyright {
	text-align: center;
	font-size: 10px;
	color: #CCC;
}

.copyright a {
	color: #A31F1A;
	text-decoration: none
}

#login .logo {
	width: 500px;
	height: 51px;
}
</style>
 </head>
 <body>
  <div id="alertMessage"></div>
  <div id="successLogin"></div>
  <div class="text_success">
   <img src="${pageContext.request.contextPath}/plug-in/login/images/loader_green.gif" alt="Please wait" />
   <span>登陆成功!请稍后....</span>
  </div>
  <div id="login">
   <%--<div class="ribbon" style="background-image:url(${pageContext.request.contextPath}/plug-in/login/images/typelogin.png);"></div>--%>
   <div class="inner">
    <div class="logo">
     <img src="${pageContext.request.contextPath}/images/logo1.png"/>
    </div>
    <div class="formLogin">
     <form  name="formLogin" id="formLogin" action="${pageContext.request.contextPath}/system/loginAction!login.action" check="${pageContext.request.contextPath}/system/loginAction!checkuser.action" method="post">
      <div class="">
       <input class="userName" name="username" type="text" id="username" title="用户名" iscookie="true"   nullmsg="请输入用户名!"/>
      </div>
      <div class="">
       <input class="password" name="password" type="password" id="password" title="密码"  nullmsg="请输入密码!"/>
      </div>
      <div class="loginButton">
      <div style="float: right; padding: 3px 0; margin-right: -12px;">
        <div>
         <ul class="uibutton-group">
          <li>
           <a class="uibutton normal" href="#" id="but_login">登陆</a>
          </li>
          <li>
           <a class="uibutton normal" href="#" id="forgetpass">重置</a>
          </li>
         </ul>
        </div>
       </div>
       <div class="clear"></div>
      </div>
     </form>
    </div>
   </div>
   <div class="shadow"></div>
  </div>
  <!--Login div-->
  <div class="clear"></div>
  <div id="versionBar">
   <div class="copyright">
    &copy; 版权所有
    <span class="">(推荐使用IE8+,谷歌浏览器可以获得更快,更安全的页面响应速度)</span>
   </div>
  </div>
   <!-- Link JScript-->
  <script type="text/javascript" src="${pageContext.request.contextPath}/plug-in/login/js/jquery-jrumble.js"></script>
  <script type="text/javascript" src="${pageContext.request.contextPath}/plug-in/login/js/jquery.tipsy.js"></script>
  <script type="text/javascript" src="${pageContext.request.contextPath}/plug-in/login/js/iphone.check.js"></script>
  <script type="text/javascript" src="${pageContext.request.contextPath}/plug-in/login/js/login.js"></script>
 </body>
</html>