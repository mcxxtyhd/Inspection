<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/tld.jsp" %>
<!DOCTYPE HTML>
<html>
  <head>
    <title>抢修任务信息管理</title>
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
			url:'${pageContext.request.contextPath}/problem/repairAction!repairTaskDatagrid.action',
			iconCls : 'icon-tip',
			pagination : true,
			pagePosition : 'bottom',
			//rownumbers:true,
			fit : true,
			nowrap : false,
			autoRowHeight : false,
			border : false,
			idField : 'id',
			columns : [[
				    {title : '编号',field : 'id',width :60,checkbox:true},
				    {title : '类别',field : 'rcategory',width :50},
			        {title : '抢修内容',field : 'rcontent',width : 240},
			        {title : '维护队',field : 'rgname',width : 80},
			    	{title : '状态',field : 'rflag',width : 50,formatter : function(val, rec) {
								if (val == 1) {
									return '未处理';
								}else if(val == 2) {
									return '已下发';
								}else if(val == 3) {
									return '已回复';
								}else{
									return '';
								}
							}},
			    	{title : '回复信息',field : 'rdesc',width : 200},
			    	{title : '创建时间',field : 'rsenddate',width : 120},
			    	{title : '处理时间',field : 'rrepdate',width : 120},
			    	{title : '截止时间',field : 'rendtime',width : 120}
			]],
			rowStyler: function(index,row){
					if (row.rcomflag==0){
						return 'background-color:red;color:black;'; 
					}
				},
				
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
				text : '导出',
				iconCls : 'icon-edit',
				handler : function() {
					daochu();
				}
			}, '-', {
				text : '取消选中',
				iconCls : 'icon-undo',
				handler : function() {
					datagrid.datagrid('unselectAll');
				}
			},'-' ]
		});
			$("#entid").change(function(){
			var entid=$("#entid").val();
			var rgidSelect = $(".rgid").children("select");
			var plineidSelect = $(".plineid").children("select");
				$.ajax({
					url:'${pageContext.request.contextPath}/query/planAction!cascodeToLineAndGroup.action',
					data:  { entid: entid },
					dataType:"json",
					success:(function(json){
					if(json.flag==0){
						$("#rgid").empty();
							$("#rgid").append("<option value=''>请选择</option>").appendTo(rgidSelect);
							for (var i = 0; i < json.glist.length; i++) {
								$("#rgid").append("<option value=\""+json.glist[i].id+"\">"+json.glist[i].gname+"</option>");
							}
					}
					else{
						
					}
					})
				})
		})
	});
	
	function daochu() {
		 $.messager.show({
						title : '提示',
						msg : "正在导出，请耐心等待......",
						showType: 'slide',
						width:250,
						height:100,
         				timeout: 5000
					});
	//window.location.replace('${pageContext.request.contextPath}/summary/summaryFormAction!toExcel.action?flag=1&btype=1&entid='+$("#entid").val()+'&itaskid='+$("#itaskid").val()+'&xuid='+$("#xuid").val()+'&xgid='+$("#xgid").val()+'&xequid='+$("#xequid").val()+'&rpsdate='+$("#beginDate").val()+'&rpedate='+$("#dissucsdate").val());
		window.location.replace('${pageContext.request.contextPath}/problem/repairAction!repairTasktoExcel.action?entid='+$("#entid").val()+'&rcontent='+$("#rcontent").val()+'&rgid='+$("#rgid").val()+'&rflag='+$("#rflag").val());
	}
	//增加操作
	function append() {
		 var p = parent.sy.dialog({
			title : '抢修任务新增',
			iconCls : 'icon-tip',
			resizable: true,
			collapsible:true,
			href: '${pageContext.request.contextPath}/problem/repairAction!repairAdd.action',
			width : 450,
			height : 280,
			buttons : [ {
				text : '保存',
				iconCls : 'icon-ok',
				handler : function() {
					var f = p.find('form');
					var a = f.find('input[name="rendtime"]').val();
					if(a==""){
					  alert("截止日期不能为空");
					  return;
					}
					
					f.form('submit', {
						url :'${pageContext.request.contextPath}/problem/repairAction!addRepairTask.action',
						success : function(d) {
							var json = $.parseJSON(d);
							if (json.success) {
								datagrid.datagrid('reload');
								p.dialog('close');
							}
							$.messager.show({
								title : '提示',
								msg : json.msg
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
		        url:'${pageContext.request.contextPath}/problem/repairAction!isadmin.action',
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
							if(r){
									$.ajax({
										url:'${pageContext.request.contextPath}/problem/repairAction!removeRepairTask.action',
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
				title : '抢修任务修改',
				iconCls :'icon-tip',
				resizable: true,
			    collapsible:true,
				href : '${pageContext.request.contextPath}/problem/repairAction!repairEdit.action?Rid='+rows[0].id,
				width : 450,
				height : 350,
				buttons : [ {
					text : '保存',
					iconCls : 'icon-ok',
					handler : function() {
						var f = p.find('form');	
							var a = f.find('input[name="rendtime"]').val();
					if(a==""){
					  alert("截止日期不能为空");
					  return;
					}
						f.form('submit', {
							url : '${pageContext.request.contextPath}/problem/repairAction!editRepairTask.action',
							success : function(d) {
								var json = $.parseJSON(d);
								if (json.success) {
									datagrid.datagrid('unselectAll');
									datagrid.datagrid('reload');
									p.dialog('close');
								}
								$.messager.show({
								title : '提示',
								msg : json.msg
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
	}
	
	//清空操作 
	function cleanSearch() {
		datagrid.datagrid('load', {});
		searchForm.find('input').val('');
		searchForm.find('select').val('');
	}
	</script>
  </head>
  <body class="easyui-layout" fit="false">
   	<div region="north" border="false" title="查询条件" iconCls="icon-search" style="height:80px;overflow: hidden;" align="left">
		<form id="searchForm">
			<table class="tableForm datagrid-toolbar" style="width: 100%;height: 100%;">
			<tr>
			 <th width="8%">所属单位：</th>
				  <td width="8%">
				     <select name="entid" id="entid"  style="width:130px;height:20px;">
				        <option value="0">请选择</option>
				        <c:forEach items="${EnterpriseList}" var="ent">
				  		 <option value='<c:out value="${ent.id}"/>'><c:out value="${ent.entname}"/></option>
				  	    </c:forEach>
				      </select>
				  </td>
				   <th width="8%">抢修内容：</th>
				    <td>
				     <input name="rcontent"id="rcontent" style="width:130px;height:20px;" />
				   </td>  
				</tr>
				<tr>
				<th width="8%">维修队：</th>
				<td width="10%">
				  <select name="rgid"id="rgid" style="width:130px;height:20px;">
					        <option value="0">所有</option>
						    <c:forEach items="${GroupList}" var="group">
					  		 <option value='<c:out value="${group.id}"/>'><c:out value="${group.gname}"/></option>
					  	    </c:forEach>
					      </select></td>
			      <th width="10%">状态：</th>
				   <td >
				    <select name="rflag" id="rflag" style="width:130px;height:20px;">
				       <option value="0">请选择</option>
				       <option value="1">未处理</option>
				       <option value="2">已下发</option>
				       <option value="3">已回复</option>
				    </select>&nbsp;&nbsp;
					<a href="javascript:void(0);" iconCls="icon-search" class="easyui-linkbutton" onclick="_search();">查询</a>&nbsp;&nbsp;
					<a href="javascript:void(0);" iconCls="icon-no" class="easyui-linkbutton" onclick="cleanSearch();">清空</a>
					 </td>
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
