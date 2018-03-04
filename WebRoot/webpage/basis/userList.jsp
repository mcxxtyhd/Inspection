<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/tld.jsp" %>
<!DOCTYPE HTML>
<html>
  <head>
    <title>巡检员信息管理</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<jsp:include page="/common/easyui.jsp"></jsp:include>
	<script type="text/javascript" charset="utf-8">
	var searchForm;
	var datagrid;
	$(function(){
	 searchForm=$('#searchForm').form();
	//datagrid 显示
	 datagrid = $('#datagrid').datagrid({
			url:'${pageContext.request.contextPath}/basis/userAction!userDatagrid.action',
			iconCls : 'icon-tip',
			pagination : true,
			pagePosition : 'bottom',
			//rownumbers:true,
			fit : true,
			nowrap : true,
			autoRowHeight : false,
			border : false,
			idField : 'id',
			striped :true,
			toolbar : [ '-',{
				text : '增加',
				iconCls : 'icon-add',
				handler : function() {
					append();
				}
			}, '-', {
				text : '删除',
				iconCls : 'icon-remove',
				handler : function() {
					remove();
				}
			}, '-', {
				text : '修改',
				iconCls : 'icon-edit',
				handler : function() {
					edit();
				}
			}, '-', {
				text : '取消选中',
				iconCls : 'icon-undo',
				handler : function() {
					datagrid.datagrid('unselectAll');
					$('div.datagrid-header-check input').attr("checked",false);
				}
			},'-' ]
		});
		$("#entid").change(function(){
			var entid=$("#entid").val();
			var pgidSelect = $(".pgid").children("select");
			var plineidSelect = $(".plineid").children("select");
				$.ajax({
					url:'${pageContext.request.contextPath}/query/planAction!cascodeToLineAndGroup.action',
					data:  { entid: entid },
					dataType:"json",
					success:(function(json){
					if(json.flag==0){
						$("#pgid").empty();
							$("#pgid").append("<option value=''>请选择</option>").appendTo(pgidSelect);
							for (var i = 0; i < json.glist.length; i++) {
								$("#pgid").append("<option value=\""+json.glist[i].id+"\">"+json.glist[i].gname+"</option>");
							}
					}
					else{
						
					}
					})
				})
		})
	});
	
	//增加操作
	function append() {
		 var p = parent.sy.dialog({
			title : '巡检员信息新增',
			iconCls : 'icon-tip',
			resizable: true,
			collapsible:true,
			href: '${pageContext.request.contextPath}/basis/userAction!userAdd.action',
			width : 450,
			height : 360,
			buttons : [ {
				text : '保存',
				iconCls : 'icon-ok',
				handler : function() {
					var f = p.find('form');
					f.form('submit', {
						url : '${pageContext.request.contextPath}/basis/userAction!addInspectUser.action',
						success : function(d) {
							var json = $.parseJSON(d);
							if (json.success) {
								datagrid.datagrid('reload');
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
				datagrid.datagrid('unselectAll');
				$('div.datagrid-header-check input').attr("checked",false);
				}
			}]
		});
	}
	
	//删除操作
	
	function remove() {
		var ids = [];
		var rows = datagrid.datagrid('getSelections');
		if (rows.length > 0) {
				//权限认证
			for ( var i = 0; i < rows.length; i++) {
						ids.push(rows[i].id);
					}
			$.ajax({
				async : false,
				cache : false,
				type : "post",
		        url:'${pageContext.request.contextPath}/basis/userAction!isadmin.action',
		        data : {
					ids : ids.join(',')
				},
				success:function(data){
					var json = $.parseJSON(data);
				    if (json.success) {
				    	 $.messager.alert("提示", "含有总公司数据不能删除！","error");
					}
					else{
					
						$.messager.confirm('请确认', '您要删除当前选择的信息吗？', function(r) {
						if (r) {
							
							$.ajax({
								url:'${pageContext.request.contextPath}/basis/userAction!deleteInspectUser.action',
								data : {
									ids : ids.join(',')
								},
								cache : false,
								dataType : "json",
								success : function(response) {
									datagrid.datagrid('unselectAll');
									datagrid.datagrid('reload');
									$.messager.show({
										title : '提示',
										msg : '删除成功！'
									});
								}
							});
						}
						datagrid.datagrid('unselectAll');
						$('div.datagrid-header-check input').attr("checked",false);
					});
				 }
			}
			});
		}
		 else {
			$.messager.alert('提示', '请选择要删除的记录！', 'error');
		}
	
	}
/*  此处删除巡检员信息时没有删除在计划表，任务表中的信息
	function remove() {
		var ids = [];
		var rows = datagrid.datagrid('getSelections');
		if (rows.length > 0) {
			$.messager.confirm('请确认', '您要删除当前选择的信息吗？', function(r) {
				if (r) {
					for ( var i = 0; i < rows.length; i++) {
						ids.push(rows[i].id);
					}
					$.ajax({
						url:'${pageContext.request.contextPath}/basis/userAction!deleteInspectUser.action',
						data : {
							ids : ids.join(',')
						},
						cache : false,
						dataType : "json",
						success : function(response) {
							datagrid.datagrid('unselectAll');
							datagrid.datagrid('reload');
							$.messager.show({
								title : '提示',
								msg : '删除成功！'
							});
						}
					});
					$('div.datagrid-header-check input').attr("checked",false);
				}
			});
		} else {
			$.messager.alert('提示', '请选择要删除的记录！', 'error');
		}
	}
	*/
	//修改操作
	function edit() {
		var rows = datagrid.datagrid('getSelections');
		if (rows.length == 1) {
			var p = parent.sy.dialog({
				title : '巡检员信息修改',
				iconCls :'icon-tip',
				resizable: true,
			    collapsible:true,
				href : '${pageContext.request.contextPath}/basis/userAction!userEdit.action',
				width : 450,
				height : 360,
				buttons : [ {
					text : '保存',
					iconCls : 'icon-ok',
					handler : function() {
						var f = p.find('form');
						f.form('submit', {
							url : '${pageContext.request.contextPath}/basis/userAction!editInspectUser.action',
							success : function(d) {
								var json = $.parseJSON(d);
								if (json.success) {
									datagrid.datagrid('unselectAll');
									datagrid.datagrid('reload');
									p.dialog('close');
								}
								parent.sy.messagerShow({
									msg : json.msg,
									title : '提示'
								});
							}
						});
						$('div.datagrid-header-check input').attr("checked",false);
					}
				} ,{
			    text : '取消',
			    iconCls : 'icon-cancel',
				handler : function(){
				  p.dialog('close');
				  datagrid.datagrid('unselectAll');
				  $('div.datagrid-header-check input').attr("checked",false);
				}
			}],
				onLoad : function() {
					   var f = p.find('form');
					   f.form('load', {
						    id : rows[0].id,
							iunumber : rows[0].iunumber,
							iuname : rows[0].iuname,
							irealname : rows[0].irealname,
							iupwd : rows[0].iupwd,
							iusex : rows[0].iusex,
							iumobile : rows[0].iumobile,
							iudesc : rows[0].iudesc,
							groupid : rows[0].groupid
					});
				}
			});
		} else if (rows.length > 1) {
			parent.sy.messagerAlert('提示', '同一时间只能编辑一条记录！', 'error');
		} else {
			parent.sy.messagerAlert('提示', '请选择要编辑的记录！', 'error');
		}
	}
	
	//查询操作
	function _search() {
		datagrid.datagrid('load', sy.serializeObject(searchForm));
		$('div.datagrid-header-check input').attr("checked",false);
	}
	
	//清空操作 
	function cleanSearch() {
		datagrid.datagrid('load', {});
		searchForm.find('input').val('');
		searchForm.find('select').val('');
		$('div.datagrid-header-check input').attr("checked",false);
	}
	</script>
  </head>
  <body class="easyui-layout" fit="false">
   	<div region="north" border="false" title="查询条件"  iconCls="icon-search" style="height:57px;overflow: hidden;" align="left">
		<form id="searchForm">
			<table class="tableForm datagrid-toolbar" style="width: 100%;height: 100%;">
				<tr>
				 <th width="8%">所属单位：</th>
				  <td width="10%">
				     <select name="entid" id="entid"  style="width:150px;height:20px;">
				        <option value="0">请选择</option>
				        <c:forEach items="${EnterpriseList}" var="ent">
				  		 <option value='<c:out value="${ent.id}"/>'><c:out value="${ent.entname}"/></option>
				  	    </c:forEach>
				      </select>
				  </td>
				 <th width="8%">维护队：</th>
				 <td width="8%"><select name="groupid" id="pgid"  style="width:150px;">
				        <option value="0">请选择</option>
					    <c:forEach items="${GroupList}" var="group">
				  		 <option value='<c:out value="${group.id}"/>'><c:out value="${group.gname}"/></option>
				  	    </c:forEach>
				      </select>
				 </td>
				 <th width="8%">真实姓名:</th>
				 <td width="8%"><input name="irealname"  style="width:130px;height:20px;"/> </td>
				
				 <th width="8%">巡检员登陆名：</th>
				<td colspan="3">
				   <input name="iuname" style="width:130px;height:20px;" />&nbsp;&nbsp;&nbsp;
					<a href="javascript:void(0);" iconCls="icon-search" class="easyui-linkbutton" onclick="_search();">查询</a>&nbsp;&nbsp;
					<a href="javascript:void(0);" iconCls="icon-no" class="easyui-linkbutton" onclick="cleanSearch();">清空</a>
				<tr>
				</tr>
			</table>
		</form>
	</div>
    <div region="center" border="false" >
		<table id="datagrid">
		  <thead>  
	        <tr>  
	            <th data-options="field : 'id',width : 80,checkbox:true">编号</th>  
	            <th data-options="field : 'iuname',width : 200">巡检员登录名</th> 
	             <th data-options="field : 'irealname',width:200">真实名称</th>
	            <th data-options="field : 'iusex',width:100">性别</th> 
	            <th data-options="field : 'iumobile',width:150">手机号</th> 
	            <th data-options="field : 'groupid',width:100,hidden:true">所属维护队ID</th>
	            <th data-options="field : 'groupname',width:250">所属维护队</th>
	             <th data-options="field : 'pcomname',width:250">所属公司</th>
	        </tr>  
    		</thead>  
		</table>
	</div>
  </body>
</html>
