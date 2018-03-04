<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/tld.jsp" %>
<!DOCTYPE HTML>
<html>
  <head>
    <title>巡检线路信息管理</title>
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
			//url:'${pageContext.request.contextPath}/basis/docAction!docDatagrid.action',
			url:'${pageContext.request.contextPath}/basis/lineAction!lineDatagrid.action',
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
			singleSelect : false,
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
			},'-',{
				text : '线路导入',
				iconCls : 'icon-add',
				handler : function() {
					lineexcel();
				}
			}  ]
		});
	});
		//巡检线导入操作
	function lineexcel() {
		 var p = parent.sy.dialog({
			title : '请选择导入的文件',
			iconCls : 'icon-tip',
			resizable: true,
			collapsible:true,
			href:'${pageContext.request.contextPath}/baseInfo/baseInfoAction!baseInfoExcel.action?type=5',
			width : 400,
			height : 135,
			buttons : [ {
				text : '确定',
				iconCls : 'icon-ok',
				handler :
				 function() {
			 /*	$.messager.show({
								title : '提示',
								msg : "正在导入，请耐心等待......",
								showType: 'slide',
								width:250,
								height:100,
           						timeout: 5000
							});
				*/
					$.messager.progress({
			             title: '温馨提示',
			             msg:  '正在导入数据...'
			         });			
					var f = p.find('form');
					f.form('submit', {
						url :'${pageContext.request.contextPath}/basis/lineAction!testExel.action',
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
							$.messager.progress('close');
							$('div.datagrid-header-check input').attr("checked",false);
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
				$.messager.progress('close');
				}
			}]
		});
	}
	//增加操作
	function append() {
		 var p = parent.sy.dialog({
			title : '线路信息新增',
			iconCls : 'icon-tip',
			resizable: true,
			collapsible:true,
			href: '${pageContext.request.contextPath}/basis/lineAction!lineAdd.action',
			width : 600,
			height : 500,
			buttons : [ {
				text : '保存',
				iconCls : 'icon-ok',
				handler : function() {
					var f = p.find('form');
					//var a = f.find('select[name="lorder"]').find("option:selected").length;
					var a = f.find('select[name="lpIds"]').find("option");
						for(var i = 0; i < a.length; i++){
						    if(a[i].selected='fasle'){
								a[i].selected=true;// 默认选中
							}else{
								a[i].selected=true;// 默认选中
							}
						}

					f.form('submit', {
						url : '${pageContext.request.contextPath}/basis/lineAction!addLine.action',
						success : function(d) {
							var json = $.parseJSON(d);
							if (json.success) {
								datagrid.datagrid('reload');
								p.dialog('close');
							}
							parent.sy.messagerAlert('提示', json.msg, 'info');
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
			for ( var i = 0; i < rows.length; i++) {
					ids.push(rows[i].id);
				}
			$.ajax({
				async : false,
				cache : false,
				type : "post",
		        url:'${pageContext.request.contextPath}/basis/lineAction!isadmin.action',
		        data : {
					ids : ids.join(',')
				},
				success:function(data){
					var json = $.parseJSON(data);
				    if (json.success) {
				    	 $.messager.alert("提示", "含有总公司数据不能删除！","error");
					}
					else{
						$.messager.confirm('请确认', '此操作会删除相关的计划和任务，您确定要删除吗？', function(r) {
						if (r) {
							$.ajax({
								url:'${pageContext.request.contextPath}/basis/lineAction!deleteLine.action',
								data :"ids="+ids,
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
		//console.info(rows[0]);
		if (rows.length == 1) {
			var p = parent.sy.dialog({
				title : '线路信息修改',
				iconCls :'icon-tip',
				resizable: true,
			    collapsible:true,
				href : '${pageContext.request.contextPath}/basis/lineAction!lineEdit.action?LID='+rows[0].id,
				width : 600,
				height : 500,
				buttons : [ {
					text : '保存',
					iconCls : 'icon-ok',
					handler : function() {
						var f = p.find('form');
					    var a = f.find('select[name="lpIds"]').find("option");
						for(var i = 0; i < a.length; i++){
						    if(a[i].selected='fasle'){
								a[i].selected=true;// 默认选中
							}else{
								a[i].selected=true;// 默认选中
							}
						}
						f.form('submit', {
							url : '${pageContext.request.contextPath}/basis/lineAction!editLine.action',
							success : function(d) {
								var json = $.parseJSON(d);
								if (json.success) {
									datagrid.datagrid('unselectAll');
									datagrid.datagrid('reload');
									p.dialog('close');
								}
								parent.sy.messagerAlert('提示', json.msg, 'info');
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
							lnumber : rows[0].lnumber,
							lname : rows[0].lname,
							ldesc : rows[0].ldesc,
							lorder : rows[0].lorder,
							lpIds : sy.getList(rows[0].lpIds)
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
   	<div region="north" border="false" title="查询条件" iconCls="icon-search" style="height:57px;overflow: hidden;" align="left">
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
				 <th width="8%">线路名称：</th>
				<td>
				   <input name="lname" style="width:150px;height:20px;" />&nbsp;&nbsp;&nbsp;
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
	            <th data-options="field : 'lname',width : 200">线路名称</th> 
	            <th data-options="field : 'lpIds',width:100,hidden:true">点ID</th>
	            <th data-options="field : 'lpNames',width:700">巡检点</th>
	        </tr>  
    		</thead>  
		</table>
	</div>
  </body>
</html>
