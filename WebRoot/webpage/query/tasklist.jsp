<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/tld.jsp" %>
<!DOCTYPE HTML>
<html>
  <head>
    <title>任务信息管理</title>
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
			url:'${pageContext.request.contextPath}/query/plantaskAction!findPlanTaskList.action',
			iconCls : 'icon-tip',
			pagination : true,
			pagePosition : 'bottom',
			rownumbers:true,
			fit : true,
			nowrap : false,
			autoRowHeight : false,
			selectOnCheck : true,
			border : false,
			idField : 'id',
			striped :true,
			columns : [[
			        //{field : 'id',title : '编号',width : 80,checkbox : true},
			        {title : '计划名称',field : 'pname',width : 220},
			    	{title : '巡检周期',field : 'pstartdate',width : 170}, 
				    {title : '巡检线路',field : 'plinename',width : 200},
				    {title : '维护队',field : 'groupname',width : 200}
				    //{title : '巡检人',field : 'username',width : 130}, 
			]],
				rowStyler: function(index,row){
					if (row.pcomflag==0){
						return 'background-color:red;color:black;'; 
					}
				}
			/*,
			toolbar : [  '-', {
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
			},'-' ]*/
		});

	
	$("#entid").change(function(){
			var entid=$("#entid").val();
			var pgidSelect = $(".pgid").children("select");
			var plineidSelect = $(".plineid").children("select");
				$.ajax({
					url:'${pageContext.request.contextPath}/query/planAction!cascodeToLineAndGroup.action',
					data:  { entid: entid },
					dataType:"json",
					success:(function(json){
					if(json.flag==0){
						$("#pgid").empty();
							$("#pgid").append("<option value=''>请选择</option>").appendTo(pgidSelect);
							for (var i = 0; i < json.glist.length; i++) {
								$("#pgid").append("<option value=\""+json.glist[i].id+"\">"+json.glist[i].gname+"</option>");
							}
							
							$("#plineid").empty();
							$("#plineid").append("<option value=''>请选择</option>").appendTo(plineidSelect);
							for (var i = 0; i < json.llist.length; i++) {
								$("#plineid").append("<option value=\""+json.llist[i].id+"\">"+json.llist[i].lname+"</option>");
							}
							
					}
					else{
						//alert("选择分公司，有权限，可以修改");
					}
					})
				})
			
			
		})
	
		});

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
						url:'${pageContext.request.contextPath}/query/plantaskAction!deletePlanTask.action',
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
				title : '巡检任务信息修改',
				iconCls :'icon-tip',
				resizable: true,
			    collapsible:true,
				href : '${pageContext.request.contextPath}/query/plantaskAction!EditPlanTask.action',
				width : 500,
				height : 420,
				buttons : [ {
					text : '保存',
					iconCls : 'icon-ok',
					handler : function() {
						var f = p.find('form');					   
						f.form('submit', {
							url : '${pageContext.request.contextPath}/query/plantaskAction!PlanTaskEdit.action',
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
			<th width="10%">计划名称：</th>
				<td><input name="pname" style="width:130px;height:20px;" /></td>
			</tr>
				<tr>
				 <th width="10%"> 巡检线路：</th>
				 <td>
				    <select name="plineid" id="plineid" style="width:130px;height:20px;">
				     <option value="0">请选择</option>
					<c:forEach var="line" items="${requestScope.LineList}">
					<option value="${line.id}">${line.lname}</option>
					</c:forEach>
					</select>
				</td>
				
				<th width="10%">巡检周期：</th>
				<td width="5%"><input class="Wdate" style="width:130px;height:20px;" name="pstartdate" id="beginDate"  onfocus="WdatePicker({isShowClear:true,readOnly:true,dateFmt:'yyyy-MM'})"/></td>
				<th width="8%">维护队：</th>
				  <td>
				     <select name="pgid" id="pgid"  style="width:130px;height:20px;">
				     <option value="0">请选择</option>
					<c:forEach var="group" items="${GroupList}">
					<option value="${group.id}">${group.gname}</option>
					</c:forEach>
					</select>&nbsp;&nbsp;&nbsp;
					<a href="javascript:void(0);" iconCls="icon-search" class="easyui-linkbutton" onclick="_search();">查询</a>&nbsp;&nbsp;
					<a href="javascript:void(0);" iconCls="icon-no" class="easyui-linkbutton" onclick="cleanSearch();">清空</a>
					 </td>
				<tr>
				</tr>
			</table>
			</table>
		</form>
	</div>
    <div region="center" border="false" >
		 <table id="datagrid"></table>
	</div>
  </body>
</html>
