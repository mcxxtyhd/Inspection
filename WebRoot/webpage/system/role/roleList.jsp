<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/tld.jsp" %>
<!DOCTYPE HTML>
<html>
  <head>
    <title>角色管理</title>
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
			url:'${pageContext.request.contextPath}/system/roleAction!roleDatagrid.action',
			pagination : true,
			pagePosition : 'bottom',
			pageSize : 15,
			pageList : [ 15, 30, 45, 60, 75 ],
			//rownumbers:true,
			fit : true,
			nowrap : false,
			autoRowHeight : false,
			selectOnCheck : true,
			border : false,
			idField : 'id',
			striped :true,
			singleSelect : true,
			columns:[[
					{title : '编号',field : 'id',width : 80,checkbox : true}, 
					{field:'rolename',title:'角色名称',width:150},
					{field:'entname',title:'所属单位',width:240},
					{field:'roledesc',title:'角色描述',width:220}
				]],
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
			},'-', {
				text : '权限设置',
				iconCls : 'icon-edit',
				handler : function() {
					setfunbyrole();
				}
			},'-', {
				text : '取消选中',
				iconCls : 'icon-undo',
				handler : function() {
					datagrid.datagrid('unselectAll');
				}
			},'-' ]
		});
	});
	
	//增加操作
	function append() {
		 var p = parent.sy.dialog({
			title : '角色新增',
			iconCls : 'icon-tip',
			resizable: true,
			collapsible:true,
			href: '${pageContext.request.contextPath}/system/roleAction!roleAdd.action',
			width : 450,
			height : 240,
			buttons : [ {
				text : '保存',
				iconCls : 'icon-ok',
				handler : function() {
					var f = p.find('form');
					f.form('submit', {
						url : '${pageContext.request.contextPath}/system/roleAction!addRole.action',
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
				}
			}]
		});
	}
	
	//删除操作
	function remove() {
		var ids = [];
		var rows = datagrid.datagrid('getSelections');
		if (rows.length > 0) {
			$.messager.confirm('请确认', '确认删除角色及分配的权限信息吗？', function(r) {
				if (r) {
					$.ajax({
						url:'${pageContext.request.contextPath}/system/roleAction!deleteRole.action?RoleId='+rows[0].id,
						cache : false,
						dataType : "json",
						success : function(response) {
							datagrid.datagrid('unselectAll');
							datagrid.datagrid('reload');
							$.messager.show({
								title : '提示',
								msg : response.msg
							});
						}
					});
					rowid = '';
					$("#function-panel").html("");//删除角色后，清空对应的权限
				}
			});
		} else {
			$.messager.alert('提示', '请选择要删除的记录！', 'error');
		}
	}
	
	//修改操作
	function edit() {
		var rows = datagrid.datagrid('getSelections');
		if (rows.length == 1) {
			var p = parent.sy.dialog({
				title : '角色修改',
				iconCls :'icon-tip',
				resizable: true,
			    collapsible:true,
				href : '${pageContext.request.contextPath}/system/roleAction!roleEdit.action',
				width : 450,
				height : 240,
				buttons : [ {
					text : '保存',
					iconCls : 'icon-ok',
					handler : function() {
						var f = p.find('form');
						f.form('submit', {
							url : '${pageContext.request.contextPath}/system/roleAction!editRole.action?RoleId='+rows[0].id,
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
					}
				} ,{
			    text : '取消',
			    iconCls : 'icon-cancel',
				handler : function(){
				  p.dialog('close');
				  datagrid.datagrid('unselectAll');
				}
			}],
				onLoad : function() {
					   var f = p.find('form');
					   f.form('load', {
						    id : rows[0].id,
							rolename : rows[0].rolename,
							roledesc : rows[0].roledesc,
							entid : rows[0].entid
							
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
	}
	
	//清空操作 
	function cleanSearch() {
		datagrid.datagrid('load', {});
		searchForm.find('input').val('');
		searchForm.find('select').val('');
	}
	
	//权限设置
	function setfunbyrole() {
		var ids = [];
		var rows = datagrid.datagrid('getSelections');
		if (rows.length > 0) {
			//系统权限设置
			 $("#function-panel").panel({
				title :rows[0].rolename+":系统权限",
				tools:[
					{iconCls:'icon-save',text :'保存',handler:function(){mysubmit();}},
					{iconCls : 'icon-reload',handler : function(){$('#functionid').tree('reload');}},
					{iconCls : 'icon-redo',handler : function(){
						var node = $('#functionid').tree('getSelected');
						if (node) {
							$('#functionid').tree('expandAll', node.target);
						} else {
							$('#functionid').tree('expandAll');
						}
				   }},
				   {iconCls : 'icon-undo',handler : function() {
							var node = $('#functionid').tree('getSelected');
							if (node) {
								$('#functionid').tree('collapseAll', node.target);
							} else {
								$('#functionid').tree('collapseAll');
							}
						}}]
			   }
		      );
		     $('#function-panel').panel("refresh", "${pageContext.request.contextPath}/system/roleAction!fun.action?roleId=" + rows[0].id);
		} else {
			$.messager.alert('提示', '请选择要编辑的记录！', 'error');
		}
	}
	</script>
  </head>
  
  <body class="easyui-layout" fit="false">
  <div region="north" border="false" title="查询条件"  iconCls="icon-search" style="height:56px;overflow: hidden;" align="left">
		<form id="searchForm">
			<table class="tableForm datagrid-toolbar" style="width: 100%;height: 100%;">
				<tr> 
				  <th width="8%">所属单位：</th>
				  <td width="10%">
				     <select name="entid" id="entid"  style="width:150px;height:20px;">
				        <option value="">请选择</option>
				        <c:forEach items="${EnterpriseList}" var="ent">
				  		 <option value='<c:out value="${ent.id}"/>'><c:out value="${ent.entname}"/></option>
				  	    </c:forEach>
				      </select>
				  </td>
                  <th width="8%">角色名称：</th>
				  <td><input name="rolename" style="width:150px;height:20px;" />&nbsp;&nbsp;&nbsp;
					<a href="javascript:void(0);" iconCls="icon-search" class="easyui-linkbutton" onclick="_search();">查询</a>&nbsp;&nbsp;
					<a href="javascript:void(0);" iconCls="icon-no" class="easyui-linkbutton" onclick="cleanSearch();">清空</a>
					</td>
				</tr>
			</table>
		</form>
	</div>
   <div region="center" border="false" >
		<table id="datagrid"></table>
	</div>
    <div region="east" style="width: 500px;" split="true">
      <div tools="#tt" class="easyui-panel" title="权限设置" style="padding: 10px;" fit="true" border="false" id="function-panel">
      </div>
    </div>
  </body>
</html>
