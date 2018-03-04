<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/tld.jsp" %>
<!DOCTYPE HTML>
<html>
  <head>
    <title>巡检设备信息管理</title>
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
	//$("#intro")	id="intro" 的元素
	 datagrid = $('#datagrid').datagrid({
			url:'${pageContext.request.contextPath}/basis/equipmentAction!equipmentDatagrid.action',
			iconCls : 'icon-tip',
			pagination : true,    //True 就会在 datagrid 的底部显示分页栏。
			pagePosition : 'bottom', 
			//rownumbers:true,
			fit : true,  //自动补全 
			nowrap : false, //True 就会把数据显示在一行里。
			autoRowHeight : false,
			
			//selectOnCheck：设置selected是否跟checkbox联动，如果为true的话，
			//用户勾选或者取消勾选复选框的时候会自动select或者unselect被勾选的行；
			//如果为false，用户勾选或者取消勾选checkbox的行为不会对select有任何影响.
			selectOnCheck : true,
			border : false,
			idField : 'id',  //标识字段。
			striped :true,  //隔行变色
			//singleSelect : true,
			//fitColumns : true,
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
				text : '设备巡检项导入',
				iconCls : 'icon-add',
				handler : function() {
					equipmentexcel();
				}
			}
			
			]
		});
	});
		//设备巡检项导入操作
		
		//datagrid有一个属性叫url，在进入页面后，它会通过ajax方式向后台发送请求，后台封装相应数据（JSON格式）再返回给前台即可显示
	function equipmentexcel() {
		 var p = parent.sy.dialog({
			title : '请选择导入的文件',
			iconCls : 'icon-tip',
			resizable: true,
			collapsible:true,
			href:'${pageContext.request.contextPath}/basis/equipmentAction!equipmentExcel.action?type=3',
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
						url :'${pageContext.request.contextPath}/basis/equipmentAction!testExel.action',
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
			title : '巡检设备信息新增',
			iconCls : 'icon-tip',
			resizable: true,
			collapsible:true,
			href: '${pageContext.request.contextPath}/basis/equipmentAction!equipmentAdd.action',
			width : 550,
			height :500,
			buttons : [ {
				text : '保存',
				iconCls : 'icon-ok',
				handler : function() {
					var f = p.find('form');
					var a = f.find('select[name="epids"]').find("option");
						for(var i = 0; i < a.length; i++){
						    if(a[i].selected='fasle'){
								a[i].selected=true;// 默认选中
							}else{
								a[i].selected=true;// 默认选中
							}
						}
					f.form('submit', {
						url : '${pageContext.request.contextPath}/basis/equipmentAction!addEquipment.action',
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
	//$ 是 JQuery 常用的一个回传函数，定义为 "选取" 英文是 selector 的缩写
	function remove() {
		var ids = [];
		var rows = datagrid.datagrid('getSelections');
		if (rows.length > 0) {
				for ( var i = 0; i < rows.length; i++) {
						ids.push(rows[i].id);
					}
			$.ajax({
				//async：要求为Boolean类型的参数，默认设置为true，所有请求均为异步请求。
			      // 如果需要发送同步请求，请将此选项设置为false。注意，同步请求将锁住浏览器，用户其他操作必须等
			     //  待请求完成才可以执行。

				async : false,
				//cache：要求为Boolean类型的参数，默认为true（当dataType为script时，默认为false）。 设置为false将不会从浏览器缓存中加载请求信息。
      			cache : false,
				//type: 要求为String类型的参数，请求方式（post或get）默认为get。
				type : "post",
				//url: 要求为String类型的参数，（默认为当前页地址）发送请求的地址。
		        url:'${pageContext.request.contextPath}/basis/equipmentAction!isadmin.action',
		        
		     /*   data: 要求为Object或String类型的参数，发送到服务器的数据。如果已经不是字符串，将自动转换为字符串格

		        式。get请求中将附加在url后。防止这种自动转换，可以查看processData选项。对象必须为key/value格

		        式，例如{foo1:"bar1",foo2:"bar2"}转换为&foo1=bar1&foo2=bar2。如果是数组，JQuery将自动为不同

		        值对应同一个名称。例如{foo:["bar1","bar2"]}转换为&foo=bar1&foo=bar2。*/
		        data : {
					ids : ids.join(',')
				},
				
				/*
				success：要求为Function类型的参数，请求成功后调用的回调函数，有两个参数。

		         (1)由服务器返回，并根据dataType参数进行处理后的数据。

		         (2)描述状态的字符串。
				*/
				success:function(data){
					var json = $.parseJSON(data);
				    if (json.success) {
				    	 $.messager.alert("提示", "含有总公司数据不能删除！","error");
					}
					else{
						$.messager.confirm('请确认', '您要删除当前选择的信息吗？', function(r) {
							if (r) {
								$.ajax({
									url:'${pageContext.request.contextPath}/basis/equipmentAction!deleteEquipment.action',
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
			//console.info(rows[0]);
			var p = parent.sy.dialog({
				title : '巡检设备信息修改',
				iconCls :'icon-tip',
				resizable: true,
			    collapsible:true,
				href : '${pageContext.request.contextPath}/basis/equipmentAction!equipmentEdit.action?EQID='+rows[0].id,
				width : 550,
				height : 500,
				buttons : [ {
					text : '保存',
					iconCls : 'icon-ok',
					handler : function() {
						var f = p.find('form');
						var a = f.find('select[name="epids"]').find("option");
						for(var i = 0; i < a.length; i++){
						    if(a[i].selected='fasle'){
								a[i].selected=true;// 默认选中
							}else{
								a[i].selected=true;// 默认选中
							}
						}
						f.form('submit', {
							url : '${pageContext.request.contextPath}/basis/equipmentAction!editEquipment.action',
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
				  <th width="10%">设备类型：</th>
				  <td width="10%">
				     <select name="etype" style="width:130px;height:20px;">
				        <option value="">请选择</option>
				        <option value="1">铁塔、天馈线</option>
				        <option value="2">室内分布及WLAN</option>
				      </select>
				  </td> 
				<th width="8%">设备编号：</th>
				<td width="10%"><input name="enumber" style="width:150px;height:20px;" /></td>
				 <th width="8%">设备名称：</th>
				<td>
				   <input name="ename" style="width:150px;height:20px;" />&nbsp;&nbsp;&nbsp;
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
	            <th data-options="field : 'enumber',width : 150">设备编号</th>  
	            <th data-options="field : 'ename',width : 200">设备名称</th> 
	            <%-- <th data-options="field : 'eaddress',width:200">地址</th>
	            <th data-options="field : 'eposx',width:120">经度</th> 
	            <th data-options="field : 'eposy',width:120">纬度</th>--%>
	            <th data-options="field : 'epids',width:100,hidden:true">项ID</th>
	            <th data-options="field : 'epnames',width:500">巡检项</th>
	        </tr>  
    		</thead>  
		</table>
	</div>
  </body>
</html>
