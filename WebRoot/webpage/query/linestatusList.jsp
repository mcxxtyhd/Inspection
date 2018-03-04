<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/tld.jsp"%>
<!DOCTYPE HTML>
<html>
  <head>
    <title>巡检数据统计</title>
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
			url:'${pageContext.request.contextPath}/query/planQueryAction!tasksummaryDatagrid.action',
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
			singleSelect : true,
			columns : [[
			        {title : '任务名称',field : 'pname',width : 160},
			        {title : '巡检周期',field : 'pstartdate',width : 120},
			        {title : '维护队',field : 'groupname',width : 120},
			    	{title : '站点个数',field : 'pointCount',width : 100},
			    	{title : '已巡个数',field : 'queryCount',width : 100},
			    	{title : '未巡个数',field : 'unqueryCount',width : 100},
			    	{title : '问题上报个数',field : 'errqueryCount',width : 100},
			    	{title : '图片异常个数',field : 'picexcepnum',width : 100},
			    	{title : '合格率',field : 'picrate',width : 100},
			    	{title : '完成率',field : 'rate',width : 100}
			]]
		});
		$("#entid").change(function(){
			var entid=$("#entid").val();
			var pgidSelect = $(".pgid").children("select");
			var itaskidSelect = $(".itaskid").children("select");
				$.ajax({
					url:'${pageContext.request.contextPath}/query/planAction!cascodeToTaskAndGroup.action',
					data:  { entid: entid },
					dataType:"json",
					success:(function(json){
					if(json.flag==0){
						$("#pgid").empty();
							$("#pgid").append("<option value=''>请选择</option>").appendTo(pgidSelect);
							for (var i = 0; i < json.glist.length; i++) {
								$("#pgid").append("<option value=\""+json.glist[i].id+"\">"+json.glist[i].gname+"</option>");
							}
						$("#itaskid").empty();
						//alert(json.tlist.length);
						$("#itaskid").append("<option value=''> 请选择</option>").appendTo(itaskidSelect);
						for(var i=0;i<json.tlist.length;i++){
							$("#itaskid").append("<option value=\""+json.tlist[i].id+"\">"+json.tlist[i].pname+"</option>")
						}	
					}
					else{
						
					}
					})
				})
		})
	});
	
	//查询操作
	function _search() {
		$('#qyerytype').val("1");
		datagrid.datagrid('load', sy.serializeObject(searchForm));
	}
	
	//清空操作 
	function cleanSearch() {
		$('#qyerytype').val('');
		datagrid.datagrid('load', {});
		searchForm.find('select').val('');
		searchForm.find('input').val('');
	}
	
	</script>
  </head>
  
  <body class="easyui-layout" fit="false">
   	<div region="north" border="false" title="数据统计查询条件" iconCls="icon-search" style="height:80px;overflow: hidden;" align="left">
		<form id="searchForm">
		<input type="hidden" name="qyerytype" id="qyerytype"/>
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
				<th width="8%">任务名称：</th>
				      <td>
					       <select name="itaskid" id="itaskid"style="width:130px;height:20px;">
					        <option value="0">所有</option>
						    <c:forEach items="${TaskList}" var="task">
					  		 <option value='<c:out value="${task.id}"/>'><c:out value="${task.pname}"/></option>
					  	    </c:forEach>
					      </select>
				       </td>
				      
				   </tr>
				   <tr>
				    <th width="8%">维护队：</th>
				       <td>
					      <select name="pgid" id="pgid" style="width:130px;height:20px;">
					        <option value="0">所有</option>
						    <c:forEach items="${GroupList}" var="group">
					  		 <option value='<c:out value="${group.id}"/>'><c:out value="${group.gname}"/></option>
					  	    </c:forEach>
					      </select>
				       </td>
				    <th width="8%">巡检周期：</th>
				    <td colspan="5">
					<input class="Wdate" name="pstartdate" id="beginDate"  onfocus="WdatePicker({startDate:'%y-%M',dateFmt:'yyyy-MM ',alwaysUseStartDate:true})" style="width:130px;height:20px;"/>---
					<input class="Wdate" name="penddate" id="dissucsdate"  onfocus="WdatePicker({stopDate:'%y-%M',dateFmt:'yyyy-MM',alwaysUseStartDate:false,minDate:'#F{$dp.$D(\'beginDate\')}'})" style="width:130px;height:20px;"/>&nbsp;&nbsp;
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
