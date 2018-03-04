<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
  <head>
    <title>单位信息管理</title>
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
			url:'${pageContext.request.contextPath}/system/enterpriseAction!entpriseDatagrid.action',
			//title : '单位信息列表',
			iconCls : 'icon-tip',
			pagination : true,
			pagePosition : 'bottom',
			//rownumbers:true,
			fit : true,
			nowrap : false,
			autoRowHeight : false,
			selectOnCheck : true,
			border : false,
			idField : 'id',
			striped :true,
			singleSelect : true,
			columns : [ [{
				    title : '编号',field : 'id',width : 80,checkbox : true
				}, {
					title : '单位名称',field : 'entname',width : 250
				}, {
					title : '联系人',field : 'entlinkperson',width : 150
				}, {
					title : '电话',field : 'entlinktel',width : 150
				}, {
					title : '地址',field : 'entaddress',width : 400
				}] ],
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
					editEnterprise();
				}
			}, '-', {
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
			title : '单位信息新增',
			iconCls : 'icon-tip',
			resizable: true,
			collapsible:true,
			href: '${pageContext.request.contextPath}/system/enterpriseAction!entpriseAdd.action',
			width : 450,
			height : 310,
			buttons : [ {
				text : '保存',
				iconCls : 'icon-ok',
				handler : function() {
					var f = p.find('form');
					f.form('submit', {
						url : '${pageContext.request.contextPath}/system/enterpriseAction!addEnterprise.action',
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
			$.messager.confirm('请确认', '您要删除当前选择的信息吗？', function(r) {
				if (r) {
					for ( var i = 0; i < rows.length; i++) {
						ids.push(rows[i].id);
					}
					$.ajax({
						url:'${pageContext.request.contextPath}/system/enterpriseAction!deleteEnterprise.action',
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
								msg : response.msg
							});
						}
					});
				}
			});
		} else {
			$.messager.alert('提示', '请选择要删除的记录！', 'error');
		}
	}
	
	//修改操作
	function editEnterprise() {
		var rows = datagrid.datagrid('getSelections');
		if (rows.length == 1) {
			var p = parent.sy.dialog({
				title : '单位信息修改',
				iconCls :'icon-tip',
				resizable: true,
			    collapsible:true,
				href : '${pageContext.request.contextPath}/system/enterpriseAction!entpriseEdit.action',
				width : 450,
				height : 310,
				buttons : [ {
					text : '保存',
					iconCls : 'icon-ok',
					handler : function() {
						var f = p.find('form');
						f.form('submit', {
							url : '${pageContext.request.contextPath}/system/enterpriseAction!editEnterprise.action',
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
							entname : rows[0].entname,
							entlinkperson : rows[0].entlinkperson,
							entlinktel : rows[0].entlinktel,
							entaddress : rows[0].entaddress,
							entdesc : rows[0].entdesc
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
	}
	
	</script>
  </head>
  
  <body class="easyui-layout" fit="false">
   	<div region="north" border="false" title="单位信息管理" iconCls="icon-search" style="height:56px;overflow: hidden;" align="left">
		<form id="searchForm">
			<table class="tableForm datagrid-toolbar" style="width: 100%;height: 100%;">
				<tr>
				<th width="8%">单位名称：</th>
				<td width="10%"><input name="entname" style="width:150px;height:20px;" /></td>
				<th width="7%">联系人：</th>
				<td><input name="entlinkperson" style="width:150px;height:20px;" />&nbsp;&nbsp;
					<a href="javascript:void(0);" iconCls="icon-search" class="easyui-linkbutton" onclick="_search();">查询</a>
					<a href="javascript:void(0);" iconCls="icon-no" class="easyui-linkbutton" onclick="cleanSearch();">清空</a>
				</td>
				</tr>
			</table>
		</form>
	</div>
    <div region="center" border="false">
		<table id="datagrid"></table>
	</div>
  </body>
</html>
