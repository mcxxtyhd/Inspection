<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/tld.jsp"%>
<!DOCTYPE HTML>
<html>
  <head>
    <title>巡检数据核查</title>
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
			url:'${pageContext.request.contextPath}/query/inspectmessageAction!inspectmsgDatagrid.action?qType=${QueryType}',
			iconCls : 'icon-tip',
			pagination : true,
			pagePosition : 'bottom',
			//rownumbers:true,
			fit : true,
			nowrap : false,
			autoRowHeight : false,
			selectOnCheck : false,
			checkOnSelect : false,
			border : false,
			idField : 'id',
			striped :true,
			singleSelect : true,
			columns : [[
			        {title : '任务名称',field : 'xtaskname',width : 150},
			        {title : '巡检周期',field : 'pstartdate',width : 120},
			    	{title : '设备名称',field : 'xequname',width : 150},
			    	{title : '维护队',field : 'xgname',width : 120},
			    	{title : '巡检时间',field : 'xreptime',width : 170},
			    	{title : '状态',field : 'xstatus',width : 100,formatter : function(val, rec) {
								if (val == '0') {
									return '正常';
								}else if(val == '1') {
									return '异常';
								}else{
									return '';
								}
							}},
			    	{title : '操作',field : 'caozuo',width : 150,formatter : function(value, row, index) {
					   return sy.fs('<a href="#"style="text-decoration:  none" onclick="editMsgInfo(\'{0}\');">【明细】</a> <a href="#" style="text-decoration:  none"onclick="editMsgStatus(\'{1}\');">【编辑】</a>',row.id,row.id);
					
				}},
			]]
			 //双击 事件 
			/*onClickRow : function(rowIndex, rowData) {
			var p = parent.sy.dialog({
				title : '巡检详细信息',
				iconCls :'icon-search',
				resizable: true,
			    collapsible:true,
				href:'${pageContext.request.contextPath}/query/inspectmessageAction!inspectmessageView.action?XMId='+rowData.id,
				width : 800,
				height : 520,
				buttons : [{
				    text : '关闭',
				    iconCls : 'icon-cancel',
					handler : function(){
					  p.dialog('close');
					}
			   }]
			  });
			}*/
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
	 //设备列表
	 	$('#xequid').combogrid({    
				    panelWidth:430,
				    panelHeight :300,
				    title :'设备信息',
				    editable:false,
				    idField:'id',    
				    textField:'ename', 
				    url:'${pageContext.request.contextPath}/basis/equipmentAction!equipmentInfoDatagrid.action',
				    pagination : true,
			        pagePosition : 'bottom',
			        pageSize :8,
			        pageList : [8,16,24,32],
				    fitColumns:true,   
				    columns:[[  
				    	{title : '设备编号',field : 'enumber',width : 120},
				        {title : '设备名称',field : 'ename',width : 220}
				    ]]    
				}); 
	});
	
	//巡检数据明细
	function editMsgInfo(mid){
		  var p = parent.sy.dialog({
				title : '巡检详细信息',
				iconCls :'icon-search',
				resizable: true,
			    collapsible:true,
				href:'${pageContext.request.contextPath}/query/inspectmessageAction!inspectmessageView.action?XMId='+mid,
				width : 980,
				height : 510,
				buttons : [{
				    text : '关闭',
				    iconCls : 'icon-cancel',
					handler : function(){
					  p.dialog('close');
					}
			   }]
		});
	}
	
	//修改设备巡检数据状态
	function editMsgStatus(mid){
		  $.messager.confirm('请确认', '确认要修改状态吗？', function(r) {
			 if (r) {
				$.ajax({
					async : false,
					cache : false,
					type : 'POST',
					url:'${pageContext.request.contextPath}/query/inspectmessageAction!inspectmsgEditStatus.action?MsgId='+mid,
					success : function(response) {
						var d = $.parseJSON(response);
						if (d.success) {
							datagrid.datagrid('reload');
						}
						$.messager.alert('提示',d.msg, 'info');
					   }
					});
				}
			});
	}
	
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
   	<div region="north" border="false" title="查询条件" iconCls="icon-search" style="height:78px;overflow: hidden;" align="left">
		<form id="searchForm">
		<input type="hidden" name="qyerytype" id="qyerytype"/>
			<table class="tableForm datagrid-toolbar" style="width:100%;height:100%;">
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
				      <td  width="10%">
					       <select name="itaskid" id="itaskid"style="width:130px;height:20px;">
					        <option value="0">所有</option>
						    <c:forEach items="${TaskList}" var="task">
					  		 <option value='<c:out value="${task.id}"/>'><c:out value="${task.pname}"/></option>
					  	    </c:forEach>
					      </select>
				       </td>
				       <th width="8%">维护队：</th>
				       <td width="8%">
					       <select name="xgid"id="pgid" style="width:130px;height:20px;">
					        <option value="0">所有</option>
						    <c:forEach items="${GroupList}" var="group">
					  		 <option value='<c:out value="${group.id}"/>'><c:out value="${group.gname}"/></option>
					  	    </c:forEach>
					      </select>
					 </td>
					  <th width="8%">状态：</th>
				       <td>
					       <select name="xstatus" style="width:130px;height:20px;">
					        <option value="">所有</option>
					        <option value="0">正常</option>
					        <option value="1">异常</option>
					      </select>
					 </td>
				</tr>
				<tr>
				 <th width="8%">设备名称：</th>
				 <td width="10%">
				 <input name="xename" style="width:140px;height:20px;" />
				 </td>
				  <th width="8%">巡检周期：</th>
				  <td>
				     <input name="pstartdate"  class="Wdate" onfocus="WdatePicker({startDate:'%y-%M',dateFmt:'yyyy-MM ',alwaysUseStartDate:true})" editable="false" style="width:130px;height:20px;" />
				   </td>    
					 <th width="8%">巡检时间：</th>
				     <td colspan="4">
				     <input class="Wdate" name="rpsdate" id="beginDate"  onfocus="WdatePicker({startDate:'%y-%M-%d',dateFmt:'yyyy-MM-dd ',alwaysUseStartDate:true})" style="width:130px;height:20px;"/>--
				     <input class="Wdate" name="rpedate" id="dissucsdate"  onfocus="WdatePicker({stopDate:'%y-%M-%d',dateFmt:'yyyy-MM-dd',alwaysUseStartDate:false,minDate:'#F{$dp.$D(\'beginDate\')}'})" style="width:130px;height:20px;"/>&nbsp;&nbsp;
					<a href="javascript:void(0);" iconCls="icon-search" class="easyui-linkbutton" onclick="_search();">查询</a>&nbsp;&nbsp;
					<a href="javascript:void(0);" iconCls="icon-no" class="easyui-linkbutton" onclick="cleanSearch();">清空</a>
				   </td>
				</tr>
			</table>
		</form>
	</div>
    <div region="center" border="false" >
		<table id="datagrid" ></table>
	</div>
  </body>
</html>
