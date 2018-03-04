<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/tld.jsp" %>
<!DOCTYPE HTML>
<html>
  <head>
    <title>巡检内容信息管理</title>
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
			url:'${pageContext.request.contextPath}/basis/projectAction!projectDatagrid.action',
			iconCls : 'icon-tip',
			pagination : true,
			pagePosition : 'bottom',
			//rownumbers:true,
			fit : true,
			nowrap : false,
			border : false,
			idField : 'id',
			columns : [[
				    {field : 'id',title : '巡检项组编号',width : 80,checkbox : true},
			        {title : '大类名称',field : 'pgroupname',width : 200},
			    	{title : '小类名称',field : 'pname',width : 200},
			    	{title : '数据类型',field : 'ptype',width : 120,formatter : function(val, rec) {
								if (val == '0') {
									return '数字';
								}else if(val == '1') {
									return '枚举';
								}else if(val == '2') {
									return '字符串';
								}else{
									return '';
								}
							}},
					{title : '最大值',field : 'pmaxvalue',width : 100},
					{title : '最小值',field : 'pminvalue',width : 100},
					{title : '所属公司',field : 'pcomname',width : 350}
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
			}, '-', {
				text : '取消选中',
				iconCls : 'icon-undo',
				handler : function() {
					datagrid.datagrid('unselectAll');
				}
			},'-' ]
		});
	});
	function downloadfile() {
		//alert( $("#entid").val() ) ;
	//	window.location.replace('${pageContext.request.contextPath}/basis/projectAction!toTXT.action?pgroupid='+$("#pgroupid").val()+'&entid='+$("#entid").val()+'&pname='+$("#pname").val()+'&ptype='+$("#ptype").val();
	}
	//增加操作
	function append() {
		 var p = parent.sy.dialog({
			title : '巡检内容信息新增',
			iconCls : 'icon-tip',
			resizable: true,
			collapsible:true,
			href: '${pageContext.request.contextPath}/basis/projectAction!projectAdd.action',
			width : 500,
			height : 320,
			buttons : [ {
				text : '保存',
				iconCls : 'icon-ok',
				handler : function() {
					var f = p.find('form');
					var svalue = f.find('select[name="ptype"]').val();
					var bvalue = f.find('select[name="penumvalue"]').val();
					if(svalue==1&&bvalue==""){
					   parent.sy.messagerAlert('提示', '请选择关联枚举值！', 'error');
					   return;
					}
					f.form('submit', {
						url : '${pageContext.request.contextPath}/basis/projectAction!addProject.action',
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
			for ( var i = 0; i < rows.length; i++) {
						ids.push(rows[i].id);
					}
			$.ajax({
				async : false,
				cache : false,
				type : "post",
		        url:'${pageContext.request.contextPath}/basis/projectAction!isadmin.action',
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
									url:'${pageContext.request.contextPath}/basis/projectAction!deleteProject.action',
									data : {
										ids : ids.join(',')
									},
									cache : false,
									dataType : "json",
									success : function(response) {
										datagrid.datagrid('unselectAll');
										datagrid.datagrid('reload');
										$('div.datagrid-header-check input').attr("checked",false);
										$.messager.show({
											title : '提示',
											msg : response.msg
										});
									}
								});
								$('div.datagrid-header-check input').attr("checked",false);
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
			//console.info(rows[0]);
			var p = parent.sy.dialog({
				title : '巡检内容信息修改',
				iconCls :'icon-tip',
				resizable: true,
			    collapsible:true,
				href : '${pageContext.request.contextPath}/basis/projectAction!projectEdit.action?PID='+rows[0].id,
				width : 450,
				height : 340,
				buttons : [ {
					text : '保存',
					iconCls : 'icon-ok',
					handler : function() {
						var f = p.find('form');
						var svalue = f.find('select[name="ptype"]').val();
						var bvalue = f.find('select[name="penumvalue"]').val();
						if(svalue==1&&bvalue==""){
						   parent.sy.messagerAlert('提示', '请选择关联枚举值！', 'error');
						   return;
						}
						f.form('submit', {
							url : '${pageContext.request.contextPath}/basis/projectAction!editProject.action',
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
					   f.form('load', rows[0]);
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
		datagrid.datagrid('unselectAll');
	}
	
	//清空操作 
	function cleanSearch() {
		datagrid.datagrid('load', {});
		searchForm.find('input').val('');
		searchForm.find('select').val('');
		datagrid.datagrid('unselectAll');
	}
	
	</script>
  </head>
  
  <body class="easyui-layout" fit="false">
   	<div region="north" border="false" title="查询条件"  iconCls="icon-search" style="height:75px;overflow: hidden;" align="left">
		<form id="searchForm">
			<table class="tableForm datagrid-toolbar" style="width: 100%;height: 100%;">
				<tr>
				 <th width="8%">所属单位：</th>
				  <td width="10%">
				     <select name="entid" id="entid"  style="width:150px;height:20px;">
				        <option value="-1">请选择</option>
				        <c:forEach items="${EnterpriseList}" var="ent">
				  		 <option value='<c:out value="${ent.id}"/>'><c:out value="${ent.entname}"/></option>
				  	    </c:forEach>
				      </select>
				  </td>
			    	<th width="8%">大类名称：</th>
					<td>
					    <select name="pgroupid" style="width:150px;height:20px;">
				        <option value="">请选择</option>
				        <c:forEach items="${ProjectGroupList}" var="pgroup">
				  		 <option value='<c:out value="${pgroup.id}"/>'><c:out value="${pgroup.pgname}"/></option>
				  	    </c:forEach>
				      </select></td>
				 </tr>
				 <tr>
			    <th width="8%">小类名称：</th>
				<td width="8%">
				   <input name="pname" style="width:150px;height:20px;" /></td>
				<th width="8%">数据类型：</th>
				<td>
				   <select name="ptype" style="width:150px;height:20px;" >
				     <option value="">请选择</option>
				     <option value="0">数字</option>
				     <option value="1">枚举</option>
				     <option value="2">字符串</option>
				   </select>
					<a href="javascript:void(0);" iconCls="icon-search" class="easyui-linkbutton" onclick="_search();">查询</a>&nbsp;&nbsp;
					<a href="javascript:void(0);" iconCls="icon-no" class="easyui-linkbutton" onclick="cleanSearch();">清空</a>
				<tr>
				</tr>
			</table>
		</form>
	</div>
    <div region="center" border="false" >
		<table id="datagrid"></table>
	</div>
  </body>
</html>
