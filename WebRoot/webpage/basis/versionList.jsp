<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/tld.jsp" %>
<!DOCTYPE HTML>
<html>
  <head>
    <title>版本控制管理</title>
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
			url:'${pageContext.request.contextPath}/basis/versionAction!versionDatagrid.action',
			iconCls : 'icon-tip',
			pagination : true,
			pagePosition : 'bottom',
			//rownumbers:true,
			fit : true,
			nowrap : false,
			autoRowHeight : false,
			border : false,
			idField : 'id',
			//singleSelect : true,
				columns : [[
				    {field : 'id',title : '编号',width : 40,checkbox : true},
			        {title : '版本号',field : 'vnum',width : 100},
			        {title : '版本名称',field : 'vname',width : 280},
			    	{title : '版本描述',field : 'vdesc',width : 280},
			    	{title : '发布人',field : 'vpublisher',width : 150},
			    	{title : '更新时间',field : 'vupdate',width : 150},
			    	{title : '下载',field : 'caozuo',   width : 150,formatter : function(value, row, index) {
				   return sy.fs('<a href="#"style="text-decoration:  none"  onclick="downloadapk(\'{0}\');">【下载】</a>',row.id);
				}},
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
			} ]
		});
	});
	
	
		//下载操作
	function downloadapk(rid) {
					$.ajax({
						url:'${pageContext.request.contextPath}/basis/versionAction!isExistFile.action',
					    data:"id="+rid,
						cache : false,
						//dataType : "json",
						success : function(re) {
							var json = $.parseJSON(re);
							//alert(json.success);
							if (json.success) {
							window.location.replace('${pageContext.request.contextPath}/basis/versionAction!downFile.action?apkid='+rid);
							}
							else{
								datagrid.datagrid('unselectAll');
								datagrid.datagrid('reload');
								parent.sy.messagerShow({
								msg : json.msg,
								title : '提示'
							});
							}
					
						}
					});
				
				datagrid.datagrid('unselectAll');
				$('div.datagrid-header-check input').attr("checked",false);
		
	}
	
	 //下载apk
		function downloadapk1(rid) {
	//	alert('${pageContext.request.contextPath}/basis/versionAction!downFile.action?apkname='+rid);
			window.location.replace('${pageContext.request.contextPath}/basis/versionAction!downFile.action?apkid='+rid);
			}
	//增加操作
	function append() {
		 var p = parent.sy.dialog({
			title : '版本新增',
			iconCls : 'icon-tip',
			resizable: true,
			collapsible:true,
			href:'${pageContext.request.contextPath}/basis/versionAction!versionAdd.action',
			width : 520,
			height : 250,
			buttons : [ {
				text : '保存',
				iconCls : 'icon-ok',
				handler : function() {
					var f = p.find('form');
				
					f.form('submit', {
						url :'${pageContext.request.contextPath}/basis/versionAction!addVersion.action',
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
			for ( var i = 0; i < rows.length; i++) {
				ids.push(rows[i].id);
			}
			$.ajax({
				async : false,
				cache : false,
				type : "post",
		        url:'${pageContext.request.contextPath}/basis/versionAction!isadmin.action',
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
									url:'${pageContext.request.contextPath}/basis/versionAction!removeVersion.action',
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
							datagrid.datagrid('unselectAll');
							$('div.datagrid-header-check input').attr("checked",false);
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
				title : '版本修改',
				iconCls :'icon-tip',
				resizable: true,
			    collapsible:true,
				href : '${pageContext.request.contextPath}/basis/versionAction!versionEdit.action?versionId='+rows[0].id,
				width : 500,
			    height : 250,
				buttons : [ {
					text : '保存',
					iconCls : 'icon-ok',
					handler : function() {
						var f = p.find('form');	
					
						f.form('submit', {
							url : '${pageContext.request.contextPath}/basis/versionAction!editVersion.action',
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
		datagrid.datagrid('unselectAll');
		$('div.datagrid-header-check input').attr("checked",false);
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
   	<div region="north" border="false" title="查询条件" iconCls="icon-search" style="height:58px;overflow: hidden;" align="left">
		<form id="searchForm">
			<table class="tableForm datagrid-toolbar" style="width: 100%;height: 100%;">
			<tr>
			  <th width="8%">所属单位：</th>
				  <td width="6%">
				     <select name="entid" id="entid"  style="width:130px;height:20px;">
				        <option value="0">请选择</option>
				        <c:forEach items="${EnterpriseList}" var="ent">
				  		 <option value='<c:out value="${ent.id}"/>'><c:out value="${ent.entname}"/></option>
				  	    </c:forEach>
				      </select>
				</td>
		
				 
				<th width="8%">版本型号：</th>
				 <td width="10%">
				 <input name="vnum" style="width:130px;height:20px;" />
				 </td>
				 	 <th width="8%">版本名称：</th>
				 <td width="10%">
				 <input name="vname" style="width:140px;height:20px;" />
				 </td>
				  <th width="8%">发布人：</th>
				  <td >
				 <input name="vpublisher" style="width:100px;height:20px;" />
				 </td>
			 
				<td>
				   <a href="javascript:void(0);" iconCls="icon-search" class="easyui-linkbutton" onclick="_search();">查询</a>&nbsp;&nbsp;
					<a href="javascript:void(0);" iconCls="icon-no" class="easyui-linkbutton" onclick="cleanSearch();">清空</a>
					 </td>
				<tr>
				</tr>
				
			</table>
		</form>
	</div>
    <div region="center" border="false" >
		 <table id="datagrid">
		 </table>
	</div>
  </body>
</html>
