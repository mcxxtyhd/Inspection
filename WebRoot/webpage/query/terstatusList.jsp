<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/tld.jsp"%>
<!DOCTYPE HTML>
<html>
  <head>
    <title>巡检终端状态查询</title>
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
			url:'${pageContext.request.contextPath}/query/terminalstatusAction!terstatusDatagrid.action',
			iconCls : 'icon-tip',
			pagination : true,
			pagePosition : 'bottom',
			//rownumbers:true,
			fit : true,
			nowrap : false,
			autoRowHeight : false,
			border : false,
			idField : 'id',
			striped :true,
			singleSelect : true
		});
	});
	
	//查询操作
	function _search() {
		datagrid.datagrid('load', sy.serializeObject(searchForm));
	}
	
	//清空操作 
	function cleanSearch() {
		datagrid.datagrid('load', {});
		searchForm.find('select').val('');
		searchForm.find('input').val('');
	}
	
	</script>
  </head>
  
  <body class="easyui-layout" fit="false">
   	<div region="north" border="false" title="查询条件" iconCls="icon-search" style="height:80px;overflow: hidden;" align="left">
		<form id="searchForm">
			<table class="tableForm datagrid-toolbar" style="width: 100%;height: 100%;">
				<tr>
				<th width="8%">维护队：</th>
				 <td width="12%"><select name="rpgroupid" id="rpgroupid"  style="width:150px;">
				        <option value="0">所有</option>
					    <c:forEach items="${GroupList}" var="group">
				  		 <option value='<c:out value="${group.id}"/>'><c:out value="${group.gname}"/></option>
				  	    </c:forEach>
				      </select></td>
				     <th width="8%">巡检员：</th>
				    <td width="12%"><select name="rpuserid" id="rpuserid"  style="width:150px;">
				        <option value="0">所有</option>
					    <c:forEach items="${InspectUserList}" var="user">
				  		 <option value='<c:out value="${user.id}"/>'><c:out value="${user.iuname}"/></option>
				  	    </c:forEach>
				      </select></td>
				     <th width="8%">终端编号：</th>
				     <td><input name="rpterminateid"  id="rpterminateid" style="width:170px;height:20px;" /></td>
				</tr>
				<tr>
				 <th>开始时间：</th>
				  <td ><input name="rpsdate" class="Wdate"  editable="false"onfocus="WdatePicker({startDate:'%y-%M-01',dateFmt:'yyyy-MM-dd ',alwaysUseStartDate:true})" style="width:160px;" /></td>
				    <th>结束时间：</th>
				   <td colspan="3"><input name="rpedate" class="Wdate" onfocus="WdatePicker({startDate:'%y-%M-01',dateFmt:'yyyy-MM-dd ',alwaysUseStartDate:true})" editable="false" style="width:160px;" />&nbsp;&nbsp;&nbsp;
					<a href="javascript:void(0);" iconCls="icon-search" class="easyui-linkbutton" onclick="_search();">查询</a>&nbsp;&nbsp;
					<a href="javascript:void(0);" iconCls="icon-no" class="easyui-linkbutton" onclick="cleanSearch();">清空</a>
				     </td>
				</tr>
			</table>
		</form>
	</div>
    <div region="center" border="false" >
		<table id="datagrid">
		  <thead>  
	        <tr>  
	           <th data-options="field : 'rpgroupname',width : 170">维护队</th> 
	            <th data-options="field : 'rpusername',width : 150">巡检员</th>  
	            <th data-options="field : 'rpterminateid',width:150">终端编号</th>
	            <th data-options="field : 'rplogintime',width:170">登录时间</th>
	            <th data-options="field : 'rplogouttime',width:170">注销时间</th>
	            <th data-options="field : 'flag',width : 150,formatter : function(val, rec) {
								if (val == '0') {
									return '离线';
								}else if(val == '1') {
									return '在线';
								}else{
									return '';
								}
							}">状态</th> 
	        </tr>  
    		</thead>  
		</table>
	</div>
  </body>
</html>
