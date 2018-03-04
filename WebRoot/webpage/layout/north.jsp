<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/tld.jsp" %>
<script type="text/javascript" src="${pageContext.request.contextPath}/jslib/common.js" charset="utf-8">
</script>
<script type="text/javascript" charset="utf-8">
	//用户信息
	function userInfo() {
	      var p = parent.sy.dialog({
			title : '用户信息',
			iconCls : 'icon-tip',
			resizable: true,
			collapsible:true,
			href: '${pageContext.request.contextPath}/system/userAction!userinfo.action',
			width : 450,
			height : 250,
			buttons : [{
			    text : '关闭',
			    iconCls : 'icon-cancel',
				handler : function(){
				p.dialog('close');
				}
			}]
		});
	}
	//修改密码
	function editPassword() {
	      var p = parent.sy.dialog({
			title : '修改密码',
			iconCls : 'icon-tip',
			resizable: true,
			collapsible:true,
			href: '${pageContext.request.contextPath}/system/userAction!changepassword.action',
			width : 500,
			height : 180,
			buttons : [ {
				text : '保存',
				iconCls : 'icon-ok',
				handler : function() {
					var f = p.find('form');
					f.form('submit', {
						url : '${pageContext.request.contextPath}/system/userAction!changeSavePassword.action',
						success : function(d) {
							var json = $.parseJSON(d);
							if (json.success) {
								p.dialog('close');
							}
							parent.sy.messagerShow({
								msg : json.msg,
								title : '提示'
							});
						  }
						});
				}
			} ,{
			    text : '取消',
			    iconCls : 'icon-cancel',
				handler : function(){
				p.dialog('close');
				}
			}]
		});
	}
	//注销系统
	function logout() {
	  $.messager.confirm('请确认', '确认要退出系统吗？', function(r) {
		   if (r) {
			 var url='${pageContext.request.contextPath}/system/loginAction!logout.action';
		     window.location.href = url;
		}
	  });
	}
</script>
<table width="100%" border="0" cellpadding="0" cellspacing="0" background="${pageContext.request.contextPath}/images/toubg.jpg">
    <tr>
      <td align="left" style="vertical-align:text-bottom" >
      <!-- <img src="${pageContext.request.contextPath}/plug-in/login/images/toplogo-main.png" width="550" height="52" alt="" >
      --><img src="${pageContext.request.contextPath}/images/topbg.jpg" >
     </td>
     <td align="right" nowrap>
      <table>
       <tr>
        <td valign="top" height="50">
         	欢迎您:<span id="lblBra">【${USER_SESSION.user.username}】</span> | <span id="clock"></span>
        </td>
       </tr>
       <tr>
       <td>
        <div style="position: absolute; right: 0px; bottom: 0px;">
          <a href="javascript:void(0);" class="easyui-menubutton" menu="#layout_north_kzmbMenu" iconCls="icon-help">控制面板</a>
          <a href="javascript:void(0);" class="easyui-menubutton" menu="#layout_north_zxMenu" iconCls="icon-back">注销</a>
        </div>
        <div id="layout_north_kzmbMenu" style="width: 100px; display: none;">
          <div onclick="userInfo();">用户信息</div>
          <div class="menu-sep"></div>
          <div onclick="editPassword();">修改密码</div>
          <div class="menu-sep"></div>
         <div>
			<span>更换主题</span>
			<div style="width: 120px;">
			<div style="width: 100px;" onclick="sy.changeTheme('cupertino');">cupertino</div>
			<div style="width: 100px;" onclick="sy.changeTheme('orange');">orange</div>
			<div style="width: 100px;" onclick="sy.changeTheme('green');">green</div>
			<div style="width: 100px;" onclick="sy.changeTheme('pink');">pink</div>
			<div style="width: 100px;" onclick="sy.changeTheme('gray');">gray</div>
			<div style="width: 100px;" onclick="sy.changeTheme('default');">default</div>
			</div>
		</div>
        </div>
        <div id="layout_north_zxMenu" style="width: 100px; display: none;">
         <div class="menu-sep"></div>
         <div onclick="logout();">退出系统</div>
        </div>
       </td>
       </tr>
      </table>
     </td>
     <td align="right">
      &nbsp;&nbsp;&nbsp;
     </td>
    </tr>
   </table>