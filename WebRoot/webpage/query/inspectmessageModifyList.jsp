<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/tld.jsp" %>
<!DOCTYPE HTML>
<html>
  <head>
    <title>巡检信息</title>
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
			url:'${pageContext.request.contextPath}/query/inspectmessageAction!inspectmessageModifyDatagrid.action?qType=${QueryType}',
			iconCls : 'icon-tip',
			pagination : true,
			pagePosition : 'bottom',
			rownumbers:true,
			fit : true,
			nowrap : false,
			autoRowHeight : false,
			border : false,
			singleSelect : true,
			idField : 'id',
			striped :true,
			columns : [[
			        {title : '编号',field : 'id',width : 180,checkbox:true},
			        {title : '设备编号',field : 'xequtnum',width : 180},
			        {title : '设备名称',field : 'xequname',width : 140},
			        {title : '城市',field : 'bcity',width : 140},
			        {title : '区县',field : 'bregion',width : 140},
		    		{title : '操作',field : 'caozuo',width : 150,formatter : function(value, row, index) {
				   return sy.fs('<a href="#"style="text-decoration:  none"  onclick="edit(\'{0}\',\'{1}\');">【巡检值修改】</a>',row.id,row.xequid);
				}},
			]],
		});
	});
	
	
	//修改操作
	function edit(rid) {
	//alert(rid);
		var rows = datagrid.datagrid('getSelections');
			var p = parent.sy.dialog({
				title : '巡检问题修改',
				id:'editDialog',
				iconCls :'icon-tip',
				resizable: true,
			    collapsible:true,
		     	href:'${pageContext.request.contextPath}/query/inspectmessageAction!inspectmessageModifyEdit.action?msgid='+rid, 
				width : 780,
				height : 510,
				onLoad : function() {
								p.find('form').form('load',rows[0]);
							}
			});
		
	}
	
	//查询操作
	function _search() {
			$('#qyerytype').val("1");
		datagrid.datagrid('load', sy.serializeObject(searchForm));
		$('div.datagrid-header-check input').attr("checked",false);
	}
	
	//清空操作 
	function cleanSearch() {
		$('#qyerytype').val('');
		datagrid.datagrid('load', {});
		searchForm.find('input').val('');
		searchForm.find('select').val('');
	}
	</script>
  </head>
  <body class="easyui-layout" fit="false">
   	<div region="north" border="false" title="查询条件" iconCls="icon-search" style="height:60px;overflow: hidden;" align="left">
		<form id="searchForm">
		<input type="hidden" name="qyerytype" id="qyerytype"/>
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
			 <th width="10%">设备编号：</th>
				    <td width="6%">
				     <input name="xequtnum"   id="xequtnum" style="width:130px;height:20px;" />
				   </td>
				     <th width="10%">设备名称：</th>
					<td colspan="3" ><input name="xequname" id="xequname" style="width:130px;height:20px;" />
					&nbsp;&nbsp;
					<a href="javascript:void(0);" iconCls="icon-search" class="easyui-linkbutton" onclick="_search();">查询</a>&nbsp;&nbsp;
					<a href="javascript:void(0);" iconCls="icon-no" class="easyui-linkbutton" onclick="cleanSearch();">清空</a>
					 </td>
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
