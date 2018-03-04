<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/tld.jsp"%>
<!DOCTYPE HTML>
<html>
  <head>
    <title>巡检数据查询</title>
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
			        {title : '任务名称',field : 'xtaskname',width : 180},
			        {title : '巡检周期',field : 'pstartdate',width : 140},
			    	{title : '设备名称',field : 'xequname',width : 180},
			    	{title : '维护队',field : 'xgname',width : 140},
			    	{title : '巡检时间',field : 'xreptime',width : 180},
		    		{title : '操作',field : 'caozuo',width : 150,formatter : function(value, row, index) {
				   return sy.fs('<a href="#"style="text-decoration:  none"  onclick="MsgInfo(\'{0}\');">【巡检详情】</a> <a href="#" style="text-decoration:  none" onclick="MsgImage1(\'{1}\');">【图片】</a>',row.id,row.id);
				}},
			]],
			rowStyler: function(index,row){
					if (row.flag==true){
						return 'background-color:red;color:black;'; 
					}
				}
		
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
	
		 //巡检详情
		function MsgInfo(rid) {
				var p = parent.sy.dialog({
				title : '巡检详细信息',
				iconCls :'icon-search',
				resizable: true,
			    collapsible:true,
				href:'${pageContext.request.contextPath}/query/inspectmessageAction!inspectmessageView.action?XMId='+rid,
				width : 980,
				height : 520,
				buttons : [{
				    text : '关闭',
				    iconCls : 'icon-cancel',
					handler : function(){
					  p.dialog('close');
					}
			   }]
			  });
			}
			
			
	//巡检图片		
	function MsgImage(rid) {
				var p = parent.sy.dialog({
				title : '巡检图片',
				iconCls :'icon-search',
				resizable: true,
			    collapsible:true,
				href:'${pageContext.request.contextPath}/query/inspectmessageAction!inspectmessageImage.action?XMId='+rid,
				width : 720,
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
			
			
				//巡检图片		
	function MsgImage1(rid) {
//	alert(rid);
			//	window.location.replace('${pageContext.request.contextPath}/query/inspectmessageAction!inspectmessageImage.action?XMId='+rid);
	window.open('${pageContext.request.contextPath}/query/inspectmessageAction!inspectmessageImage.action?XMId='+rid,"标题","dialogwidth:800px;dialogheight:650px;status=0;scroll=0;");
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
   	<div region="north" border="false" title="巡检问题查询条件" iconCls="icon-search" style="height:78px;overflow: hidden;" align="left">
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
					       <select name="itaskid"id="itaskid" style="width:130px;height:20px;">
					        <option value="0">所有</option>
						    <c:forEach items="${TaskList}" var="task">
					  		 <option value='<c:out value="${task.id}"/>'><c:out value="${task.pname}"/></option>
					  	    </c:forEach>
					      </select>
				       </td>
				       <th width="8%">维护队：</th>
				       <td>
					       <select name="xgid" id="pgid" style="width:130px;height:20px;">
					        <option value="0">所有</option>
						    <c:forEach items="${GroupList}" var="group">
					  		 <option value='<c:out value="${group.id}"/>'><c:out value="${group.gname}"/></option>
					  	    </c:forEach>
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
				     <input class="Wdate" name="rpsdate" id="beginDate"  onfocus="WdatePicker({startDate:'%y-%M-%d',dateFmt:'yyyy-MM-dd ',alwaysUseStartDate:true})" style="width:120px;height:20px;"/>--
				     <input class="Wdate" name="rpedate" id="dissucsdate"  onfocus="WdatePicker({stopDate:'%y-%M-%d',dateFmt:'yyyy-MM-dd',alwaysUseStartDate:false,minDate:'#F{$dp.$D(\'beginDate\')}'})" style="width:120px;height:20px;"/>&nbsp;&nbsp;
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
