<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/tld.jsp" %>
<!DOCTYPE HTML>
<html>
  <head>
    <title>基础数据管理</title>
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
			url:'${pageContext.request.contextPath}/baseInfo/baseInfoAction!baseInfoDatagrid.action',
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
				    {field : 'id',title : '编号',width : 80,checkbox : true},
				    {title : '设备类型',field : 'btype',width : 140,formatter : function(val, rec) {
								if (val == '1') {
									return '铁塔、天馈线';
								}else{
									return '室内分布及WLAN';
								}
							}},
			        {title : '设备编号',field : 'bnumber',width : 120},
			        {title : '设备名称',field : 'bname',width : 150},
			        {title : '地市',field : 'bcity',width : 120},
			        {title : '区县',field : 'bregion',width : 120},
			    	{title : '经度',field : 'bposx',width : 100},
			    	{title : '纬度',field : 'bposy',width : 100},
			    	{title : '地址',field : 'baddress',width : 250}
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
			},'-',{
				text : '室内导入',
				iconCls : 'icon-add',
				handler : function() {
					inexcel();
				}
			},  '-',{
				text : '铁塔导入',
				iconCls : 'icon-add',
				handler : function() {
					ydexcel();
				}
			}]
		});
	 var pager =	datagrid.datagrid('getPager'); // get the pager of datagrid
	 pager.pagination({
			/*buttons:[{
			iconCls:'icon-sum',
			text : '当前共有{total}个设备',
			}]*/
			displayMsg:'显示{from}到{to}  当前共有{total}个设备'
		}); 
	});
		//室内导入操作
	function inexcel(){
		 var p = parent.sy.dialog({
			title : '请选择导入的文件',
			iconCls : 'icon-tip',
			resizable: true,
			collapsible:true,
			href:'${pageContext.request.contextPath}/baseInfo/baseInfoAction!baseInfoExcel.action?type=2',
			width : 400,
			height : 135,
			buttons : [ {
				text : '确定',
				iconCls : 'icon-ok',
				handler :
				 function() {
				 	$.messager.show({
								title : '提示',
								msg : "正在导入，请耐心等待......",
								showType: 'slide',
								width:250,
								height:100,
           						timeout: 5000
							});
					var f = p.find('form');
					f.form('submit', {
						url :'${pageContext.request.contextPath}/baseInfo/baseInfoAction!testExel.action?flag=2',
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
							$('div.datagrid-header-check input').attr("checked",false);
						  }
						});
						p.dialog('close');
				datagrid.datagrid('unselectAll');
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
		//铁塔导入操作
	function ydexcel() {
		 var p = parent.sy.dialog({
			title : '请选择导入的文件',
			iconCls : 'icon-tip',
			resizable: true,
			collapsible:true,
			href:'${pageContext.request.contextPath}/baseInfo/baseInfoAction!baseInfoExcel.action?type=1',
			width : 400,
			height : 135,
			buttons : [ {
				text : '确定',
				iconCls : 'icon-ok',
				handler : function() {
				 	$.messager.show({
							title : '提示',
							msg : "正在导入，请耐心等待......",
							showType: 'slide',
							width:250,
							height:100,
          					timeout: 5000
						});
					var f = p.find('form');
					f.form('submit', {
						url :'${pageContext.request.contextPath}/baseInfo/baseInfoAction!testExel.action?flag=1',
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
				}
			}]
		});
	}
	//增加操作
	function append() {
		 var p = parent.sy.dialog({
			title : '基础信息新增',
			iconCls : 'icon-tip',
			resizable: true,
			collapsible:true,
			href:'${pageContext.request.contextPath}/baseInfo/baseInfoAction!baseInfoAdd.action',
			width : 500,
			height : 420,
			buttons : [ {
				text : '保存',
				iconCls : 'icon-ok',
				handler : function() {
					var f = p.find('form');
					f.form('submit', {
						url :'${pageContext.request.contextPath}/baseInfo/baseInfoAction!addBaseInfo.action',
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
		        url:'${pageContext.request.contextPath}/baseInfo/baseInfoAction!isadmin.action',
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
										url:'${pageContext.request.contextPath}/baseInfo/baseInfoAction!removeBaseInfo.action',
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
				title : '基础信息修改',
				iconCls :'icon-tip',
				resizable: true,
			    collapsible:true,
				href : '${pageContext.request.contextPath}/baseInfo/baseInfoAction!baseInfoEdit.action?BaseInfoId='+rows[0].id,
				width : 500,
				height : 430,
				buttons : [ {
					text : '保存',
					iconCls : 'icon-ok',
					handler : function() {
						var f = p.find('form');	
						f.form('submit', {
							url : '${pageContext.request.contextPath}/baseInfo/baseInfoAction!editBaseInfo.action',
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
   	<div region="north" border="false" title="查询条件" iconCls="icon-search" style="height:80px;overflow: hidden;" align="left">
		<form id="searchForm">
			<table class="tableForm datagrid-toolbar" style="width: 100%;height: 100%;">
			<tr>
			
			 <th width="10%">所属单位：</th>
				  <td width="10%">
				     <select name="entid" id="entid"  style="width:130px;height:20px;">
				        <option value="0">请选择</option>
				        <c:forEach items="${EnterpriseList}" var="ent">
				  		 <option value='<c:out value="${ent.id}"/>'><c:out value="${ent.entname}"/></option>
				  	    </c:forEach>
				      </select>
				  </td>
			 <th width="10%">设备类型：</th>
				  <td width="10%">
				     <select name="btype" style="width:130px;height:20px;">
				        <option value="">请选择</option>
				        <option value="1">铁塔、天馈线</option>
				        <option value="2">室内分布及WLAN</option>
				      </select>
			 </td>	  
			   <th width="10%">设备编号：</th>
			   <td width="6%"><input name="bnumber" style="width:130px;height:20px;" /></td>
			   <th width="10%">设备名称：</th>
			   <td><input name="bname" style="width:130px;height:20px;" /></td>
			   </tr>
			   <tr>
				<th width="6%">地市：</th>
				<td width="8%"><input name="bcity" style="width:130px;height:20px;" /></td>
			   <th width="6%">区县：</th>
			   <td><input name="bregion" style="width:130px;height:20px;" />
			   <th width="6%">地址：</th>
			   <td colspan="3"><input name="baddress" style="width:130px;height:20px;" />&nbsp;&nbsp;&nbsp;&nbsp;
					<a href="javascript:void(0);" iconCls="icon-search" class="easyui-linkbutton" onclick="_search();">查询</a>&nbsp;&nbsp;
					<a href="javascript:void(0);" iconCls="icon-no" class="easyui-linkbutton" onclick="cleanSearch();">清空</a>
	<span id="span"  ></span>
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
