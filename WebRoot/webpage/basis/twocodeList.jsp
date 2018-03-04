<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/tld.jsp" %>
<!DOCTYPE HTML>
<html>
  <head>
    <title>二维码信息管理</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<jsp:include page="/common/easyui.jsp"></jsp:include>
	<style type='text/css'>
	@media screen{.printhide{display:block}} 
	@media print{.printhide{display:none}}
</style>
	<script type="text/javascript" charset="utf-8">
	var searchForm;
	var datagrid;
	$(function(){
	 searchForm=$('#searchForm').form();
	//datagrid 显示
	 datagrid = $('#datagrid').datagrid({
			url:'${pageContext.request.contextPath}/basis/twocodeAction!twocodeDatagrid.action',
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
				text : '生成二维码',
				iconCls : 'icon-help',
				handler : function() {
					createcode();
				}
				
			},'-',{
				text : '取消选中',
				iconCls : 'icon-undo',
				handler : function() {
					datagrid.datagrid('unselectAll');
					$('div.datagrid-header-check input').attr("checked",false);
				}
			}]
		});
	});
	
	//增加操作
	function append() {
		 var p = parent.sy.dialog({
			title : '二维码信息新增',
			iconCls : 'icon-tip',
			resizable: true,
			collapsible:true,
			href: '${pageContext.request.contextPath}/basis/twocodeAction!twocodeAdd.action',
			width : 450,
			height : 230,
			buttons : [ {
				text : '保存',
				iconCls : 'icon-ok',
				handler : function() {
					var f = p.find('form');
					f.form('submit', {
						url : '${pageContext.request.contextPath}/basis/twocodeAction!addTwoDimensionCode.action',
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
						url:'${pageContext.request.contextPath}/basis/twocodeAction!deleteTwoDimensionCode.action',
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
	function edit() {
		var rows = datagrid.datagrid('getSelections');
		if (rows.length == 1) {
			var p = parent.sy.dialog({
				title : '二维码信息修改',
				iconCls :'icon-tip',
				resizable: true,
			    collapsible:true,
				href : '${pageContext.request.contextPath}/basis/twocodeAction!twocodeEdit.action',
				width : 450,
				height : 230,
				buttons : [ {
					text : '保存',
					iconCls : 'icon-ok',
					handler : function() {
						var f = p.find('form');
						f.form('submit', {
							url : '${pageContext.request.contextPath}/basis/twocodeAction!editTwoDimensionCode.action',
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
							cname : rows[0].cname,
							cdesc : rows[0].cdesc,
							cpicture : rows[0].cpicture
					});
				}
			});
		} else if (rows.length > 1) {
			parent.sy.messagerAlert('提示', '同一时间只能编辑一条记录！', 'error');
		} else {
			parent.sy.messagerAlert('提示', '请选择要编辑的记录！', 'error');
		}
	}
	
	//生成二维码
	function createcode() {
		var rows = datagrid.datagrid('getSelections');
		if (rows.length == 1) {
			var p = parent.sy.dialog({
				title : '二维码信息',
				iconCls :'icon-tip',
				resizable: true,
			    collapsible:true,
				href : '${pageContext.request.contextPath}/basis/twocodeAction!twocodeImage.action?CodeID='+rows[0].id,
				width : 450,
				height : 260,
				buttons : [ {
					text : '打印',
					iconCls : 'icon-ok',
					handler : function() {
						var x=window.open("","gainover","width=360,height=350,top=40,left=30"); 
						x.document.write("<img src=${pageContext.request.contextPath}/basis/twocodeAction!getImage.action?tcdid='"+rows[0].id+"' width='150' height='160'></img>"); 
						x.print() ;
					}
				} ,{
			    text : '关闭',
			    iconCls : 'icon-cancel',
				handler : function(){
				  p.dialog('close');
				  datagrid.datagrid('unselectAll');
				  datagrid.datagrid('reload');
				  $('div.datagrid-header-check input').attr("checked",false);
				}
			}]
			});
		} else if (rows.length > 1) {
			parent.sy.messagerAlert('提示', '同一时间只能编辑一条记录！', 'error');
		} else {
			parent.sy.messagerAlert('提示', '请选择记录！', 'error');
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
   	<div region="north" class="printhide" border="false" title="查询条件"  iconCls="icon-search" style="height:57px;overflow: hidden;" align="left">
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
				 <th width="8%">二维码标识：</th>
				 <td>
				   <input name="cname" style="width:150px;height:20px;" />&nbsp;&nbsp;&nbsp;
					<a href="javascript:void(0);" iconCls="icon-search" class="easyui-linkbutton" onclick="_search();">查询</a>&nbsp;&nbsp;
					<a href="javascript:void(0);" iconCls="icon-no" class="easyui-linkbutton" onclick="cleanSearch();">清空</a>
				<tr>
				</tr>
			</table>
		</form>
	</div>
    <div region="center" border="false" class="printhide" >
		<table id="datagrid">
		  <thead>  
	        <tr>  
	            <th data-options="field : 'id',width : 80,checkbox:true">编号</th>  
	            <th data-options="field : 'cname',width : 200">二维码标识</th>  
	            <th data-options="field : 'cdesc',width : 400">描述</th> 
	        </tr>  
    		</thead>  
		</table>
	</div>
  </body>
</html>
