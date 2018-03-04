<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/tld.jsp" %>
<!DOCTYPE HTML>
<html>
  <head>
    <title>日志管理</title>
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
			url:'${pageContext.request.contextPath}/system/logAction!logDatagrid.action',
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
			columns : [ [
				{title : '操作事件',field : 'opevent',width : 240},
				{title : '操作状态',field : 'opstatus',width : 100,
					formatter : function(val, rec) {
					if (val == 0) {
						return '成功';
					 }else{
						 return '失败';
					 }
					}},
				{title : '操作人',field : 'opuser',width : 120},
				{title : '操作时间',field : 'optime',width : 200}
				] ]
		});
	});
	
	//查询操作
	function _search() {
		datagrid.datagrid('load', sy.serializeObject(searchForm));
	}
	
	//清空操作 
	function cleanSearch() {
		datagrid.datagrid('unselectAll');
		datagrid.datagrid('load', {});
		searchForm.find('input').val('');
		searchForm.find('select').val('');
	}
	
	</script>
  </head>
  
  <body class="easyui-layout" fit="false">
   	<div region="north" border="false" title="查询条件"  iconCls="icon-search" style="height:106px;overflow: hidden;" align="left">
		<form id="searchForm">
			<table class="tableForm datagrid-toolbar" style="width: 100%;height: 100%;">
				<tr> 
				  <th width="8%">操作状态：</th>
				  <td width="10%">
				     <select name="opstatus" id="opstatus"  style="width:120px;height:20px;">
				        <option value="">请选择</option>
				         <option value="0">成功</option>
				         <option value="1">失败</option>
				      </select>
				  </td>
				   <th width="8%" style="width:120px;height:40px;" >操作时间：</th>
				    <td colspan="2"><input class="Wdate" name="startdate" onfocus="WdatePicker({isShowClear:true,readOnly:true})" style="width:120px;height:20px;"/>--至--
					<input class="Wdate" name="enddate" onfocus="WdatePicker({isShowClear:true,readOnly:true})" style="width:120px;height:20px;"/>&nbsp;&nbsp;&nbsp;
				</td>
			</tr>
			<tr>
				   <th width="8%">操作人：</th>
				  <td width="8%"><input name="opuser" style="width:120px;height:20px;" />
				  </td>
				  <th >操作事件：</th>
				  <td colspan="3" ><input name="opevent" style="width:120px;height:20px;" />
				  <a href="javascript:void(0);" iconCls="icon-search" class="easyui-linkbutton" onclick="_search();">查询</a>&nbsp;&nbsp;
					<a href="javascript:void(0);" iconCls="icon-no" class="easyui-linkbutton" onclick="cleanSearch();">清空</a>
					</td>
				</tr>
			</table>
		</form>
	</div>
    <div region="center" border="false" >
		<table id="datagrid"></table>
	</div>
  </body>
</html>
