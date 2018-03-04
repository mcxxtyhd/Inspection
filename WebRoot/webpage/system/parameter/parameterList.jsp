<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/tld.jsp" %>
<!DOCTYPE HTML>
<html>
  <head>
    <title>参数信息管理</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<jsp:include page="/common/easyui.jsp"></jsp:include>
	<script type="text/javascript" charset="utf-8">
	var searchForm;
	var datagrid;
	$(function(){
	 searchForm=$('#searchForm').form();
	 datagrid = $('#datagrid').datagrid({
			url:'${pageContext.request.contextPath}/system/parameterAction!parameterDatagrid.action',
			iconCls : 'icon-tip',
			pagination : true,
			pagePosition : 'bottom',
			pageSize : 15,
			pageList : [ 15, 30, 45, 60, 75 ],
			rownumbers:true,
			fit : true,
			nowrap : true,
			autoRowHeight : false,
			selectOnCheck : true,
			border : false,
			idField : 'id',
			striped :true,
			columns : [ [{
				    title : '编号',field : 'id',width : 80,checkbox : true},
				    {title : '参数名称',field : 'pname',width : 300},
				    {title : '参数值',field : 'pvalue',width : 200}] ],
			toolbar : [ '-',{
				text : '录入参数',
				iconCls : 'icon-add',
				handler : function() {
					append();
				}
			}, '-', {
				text : '删除参数',
				iconCls : 'icon-remove',
				handler : function() {
					remove();
				}
			}, '-', {
				text : '修改参数',
				iconCls : 'icon-edit',
				handler : function() {
					edit();
				}
			}, '-', {
				text : '取消选中',
				iconCls : 'icon-undo',
				handler : function() {
					datagrid.datagrid('unselectAll');
				}
			},'-' ],
			onLoadSuccess:function(){
			      	//指定列进行合并操作
			      	$(this).datagrid("autoMergeCells",['pname']);
			    }
		});
	});
	
	//增加操作
	function append() {
		 var p = parent.sy.dialog({
			title : '参数信息新增',
			iconCls : 'icon-tip',
			resizable: true,
			collapsible:true,
			href: '${pageContext.request.contextPath}/system/parameterAction!parameterAdd.action',
			width : 450,
			height : 270,
			buttons : [ {
				text : '保存',
				iconCls : 'icon-ok',
				handler : function() {
					var f = p.find('form');
					f.form('submit', {
						url : '${pageContext.request.contextPath}/system/parameterAction!addParameter.action',
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
				for ( var i = 0; i < rows.length; i++) {
					ids.push(rows[i].id);
				}
			$.ajax({
				async : false,
				cache : false,
				type : "post",
		        url:'${pageContext.request.contextPath}/system/parameterAction!isadmin.action',
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
									url:'${pageContext.request.contextPath}/system/parameterAction!deleteParameter.action',
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
					}
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
				title : '参数信息修改',
				iconCls :'icon-tip',
				resizable: true,
			    collapsible:true,
				href : '${pageContext.request.contextPath}/system/parameterAction!parameterEdit.action',
				width : 450,
				height : 270,
				buttons : [ {
					text : '保存',
					iconCls : 'icon-ok',
					handler : function() {
						var f = p.find('form');
						f.form('submit', {
							url : '${pageContext.request.contextPath}/system/parameterAction!editParameter.action',
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
					   f.form('load',rows[0]);
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
   	<div region="north" border="false" title="查询条件" iconCls="icon-search" style="height:57px;overflow: hidden;" align="left">
		<form id="searchForm">
			<table class="tableForm datagrid-toolbar" style="width: 100%;height: 100%;">
				<tr>
				<th width="10%">所属单位：</th>
				  <td width="10%">
				     <select name="entid" id="entid"  style="width:150px;height:20px;">
				        <option value="0">请选择</option>
				        <c:forEach items="${EnterpriseList}" var="ent">
				  		 <option value='<c:out value="${ent.id}"/>'><c:out value="${ent.entname}"/></option>
				  	    </c:forEach>
				      </select>
				  </td>
				<th width="10%">参数名称：</th>
				<td width="10%"><input name="pname" style="width:160px;height:20px;" /></td>
				<th width="10%">参数值：</th>
				<td><input name="pvalue" style="width:160px;height:20px;" />&nbsp;&nbsp;&nbsp;
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
