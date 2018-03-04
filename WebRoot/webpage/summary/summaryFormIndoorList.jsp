<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/tld.jsp" %>
<!DOCTYPE HTML>
<html>
  <head>
    <title>代维商信息管理</title>
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
	 datagrid = $('#datagrid').datagrid({
			url:'${pageContext.request.contextPath}/summary/summaryFormAction!summaryFormDatagrid.action?btype=2',
			iconCls : 'icon-tip',
			pagination : true,
			pagePosition : 'bottom',
			//rownumbers:true,
			fit : true,
			nowrap : false,
			autoRowHeight : false,
			selectOnCheck : true,
			border : false,
			idField : 'id',
			striped :true,
			//singleSelect : true,
			toolbar : [ '-', {
			text : '台帐导出',
			iconCls : 'icon-add',
			handler : function() {
				downloadfile1();
			}
			}, '-', {
			text : '维护寻检表导出',
			iconCls : 'icon-add',
			handler : function() {
				shineiInfoList();
			}
			}/*, '-', {
			text : '维护寻检表打印',
			iconCls : 'icon-add',
			handler : function() {
				shineiprint();
			}
			}
			*/
			/*, '-', {
			text : '部分导出',
			iconCls : 'icon-add',
			handler : function() {
				partExcel();
			}
			}*//*, '-', {
			text : '测试总账',
			iconCls : 'icon-add',
			handler : function() {
				downloadfile();
			}
			}*/,'-',{
			text : '取消选中',
			iconCls : 'icon-undo',
			handler : function() {
				datagrid.datagrid('unselectAll');
			}
			},'-']
		});
		$("#entid").change(function(){
			var entid=$("#entid").val();
			var xgidSelect = $(".xgid").children("select");
			var itaskidSelect = $(".itaskid").children("select");
				$.ajax({
					url:'${pageContext.request.contextPath}/query/planAction!cascodeToTaskAndGroup.action',
					data:  { entid: entid },
					dataType:"json",
					success:(function(json){
					if(json.flag==0){
						$("#xgid").empty();
							$("#xgid").append("<option value=''>请选择</option>").appendTo(xgidSelect);
							for (var i = 0; i < json.glist.length; i++) {
								$("#xgid").append("<option value=\""+json.glist[i].id+"\">"+json.glist[i].gname+"</option>");
							}
						$("#itaskid").empty();
					//	alert(json.tlist.length);
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
	
	
	function downloadfile() {
		window.location.replace('${pageContext.request.contextPath}/summary/summaryFormAction!toExcel.action?flag=2&btype=2&entid='+$("#entid").val()+'&itaskid='+$("#itaskid").val()+'&xuid='+$("#xuid").val()+'&xgid='+$("#xgid").val()+'&xequtnum='+$("#xequtnum").val()+'&rpsdate='+$("#beginDate").val()+'&rpedate='+$("#dissucsdate").val());
	}
	//查询操作
	function _search() {
		datagrid.datagrid('load', sy.serializeObject(searchForm));
		$('div.datagrid-header-check input').attr("checked",false);
	}
	
				//铁塔打印
	function shineiprint() {
		var taskids = [];
		var ids = [];
		var rows = datagrid.datagrid('getSelections');
		//alert(rows.length);
		if(rows.length>1){
		alert("同一时间只能打印一条记录");
		return;
		}
		if (rows.length > 0) {
			for ( var i = 0; i < rows.length; i++) {
			//alert("rows[i].itaskid="+rows[i].itaskid  +"      rows[i].bnumber="+rows[i].bnumber);
				taskids.push(rows[i].itaskid);
				ids.push(rows[i].id);
			}
			var url="${pageContext.request.contextPath}/summary/summaryFormAction!towerprint.action?flag=2&btype=2&ids="+ids+"&taskids="+taskids;
				window.open (url,'维护表打印','height=1000,width=1020,top=50,left=50,toolbar=yes,menubar=yes,scrollbars=yes, resizable=yes,location=no, status=no') ;
		} else {
			$.messager.alert('提示', '请选择要导出的记录！', 'error');
		}
	}
				//台帐导出最新
	function downloadfile1() {
	 $.messager.show({
						title : '提示',
						msg : "正在导出，请耐心等待......",
						showType: 'slide',
						width:250,
						height:100,
         						timeout: 0
					});
	//	alert( $("#entid").val() ) ;
	//	alert('${pageContext.request.contextPath}/summary/summaryFormAction!toExcel.action?flag=1&btype=1&entid='+$("#entid").val()+'&itaskid='+$("#itaskid").val()+'&xuid='+$("#xuid").val()+'&xgid='+$("#xgid").val()+'&xequid='+$("#xequid").val()+'&rpsdate='+$("#beginDate").val()+'&rpedate='+$("#dissucsdate").val()+'&bcity='+$("#bcity").val());
		window.location.replace('${pageContext.request.contextPath}/summary/summaryFormAction!toExcel1.action?flag=2&btype=2&entid='+$("#entid").val()+'&itaskid='+$("#itaskid").val()+'&ecity='+$("#ecity").val()+'&eregion='+$("#eregion").val()+'&xgid='+$("#xgid").val()+'&xequtnum='+$("#xequtnum").val()+'&rpsdate='+$("#beginDate").val()+'&rpedate='+$("#dissucsdate").val());
		//window.location.replace('${pageContext.request.contextPath}/summary/summaryFormAction!test.action');
	}
	//清空操作 
	function cleanSearch() {
		datagrid.datagrid('load', {});
		searchForm.find('input').val('');
		searchForm.find('select').val('');
		$('div.datagrid-header-check input').attr("checked",false);
	}
	
			//铁塔维护导出
	function shineiInfoList() {
		var taskids = [];
		var ids = [];
		var rows = datagrid.datagrid('getSelections');
		if (rows.length > 0) {
			for ( var i = 0; i < rows.length; i++) {
			//alert("rows[i].itaskid="+rows[i].itaskid  +"      rows[i].bnumber="+rows[i].bnumber);
				taskids.push(rows[i].itaskid);
				ids.push(rows[i].id);
			}
		window.location.replace('${pageContext.request.contextPath}/summary/summaryFormAction!InfoList.action?flag=2&btype=2&ids='+ids+'&taskids='+taskids);
		} else {
			$.messager.alert('提示', '请选择要导出的记录！', 'error');
		}
	}
	//部分导出操作
	function partExcel() {
		 var p = parent.sy.dialog({
			title : '导出部分信息',
			iconCls : 'icon-tip',
			resizable: true,
			collapsible:true,
			href: '${pageContext.request.contextPath}/summaryConfig/summaryConfigAction!excelEdit.action?xgid='+$("#xgid").val()+'&xequid='+$("#xequid").val()+'&sConfigFlag=2',
			width : 250,
			height : 400,
			buttons : [ {
				text : '保存',
				iconCls : 'icon-ok',
				handler : function() {
				alert('s');
				//var a = $('#summaryConfig_datagrid').datagrid('getSelections');
				//.info(a);
				
				
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
/*	function partExcel(){
	alert("d");
	window.location.replace('${pageContext.request.contextPath}/summaryConfig/summaryConfigAction!excelEdit.action');
	}*/
	</script>
  </head>
  
  <body class="easyui-layout" fit="false">
   	<div region="north" border="false" title="查询条件"  iconCls="icon-search" style="height:80px;overflow: hidden;" align="left">
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
				<th width="10%">任务名称：</th>
				      <td  width="10%">
					       <select name="itaskid" id="itaskid"   style="width:130px;height:20px;">
					        <option value="0">所有</option>
						    <c:forEach items="${TaskList}" var="task">
					  		 <option value='<c:out value="${task.id}"/>'><c:out value="${task.pname}"/></option>
					  	    </c:forEach>
					      </select>
				       </td>
				       <%--<th width="8%">巡检人：</th>
				       <td width="6%">
					       <select name="xuid"id="xuid" style="width:130px;height:20px;">
				        <option value="0">所有</option>
					    <c:forEach items="${InspectUserList}" var="user">
				  		 <option value='<c:out value="${user.id}"/>'><c:out value="${user.iuname}"/></option>
				  	    </c:forEach>
				      </select>
				       </td>
				       --%><th width="6%">地市：</th>
				<td  width="6%"><input name="ecity" id="ecity" style="width:130px;height:20px;" /></td>
					 <th width="6%">区县：</th>
					<td ><input name="eregion" id="eregion" style="width:130px;height:20px;" /></td>
				   </tr>
				<tr>
				  	<th width="8%">维护队：</th>
				 <td width="12%"><select name="xgid" id="xgid"  style="width:150px;">
				        <option value="0">所有</option>
					    <c:forEach items="${GroupList}" var="group">
				  		 <option value='<c:out value="${group.id}"/>'><c:out value="${group.gname}"/></option>
				  	    </c:forEach>
				      </select></td>
			  <th>物理编号：</th>
				   <td ><input name="xequtnum" id="xequtnum"style="width:130px;height:20px;" /></td>
				    <th width="8%">巡检周期：</th>
				    <td colspan="5">
					<input class="Wdate" name="rpsdate" id="beginDate"  onfocus="WdatePicker({startDate:'%y-%M',dateFmt:'yyyy-MM ',alwaysUseStartDate:true})" style="width:130px;height:20px;"/>--
					<input class="Wdate" name="rpedate" id="dissucsdate"  onfocus="WdatePicker({stopDate:'%y-%M',dateFmt:'yyyy-MM',alwaysUseStartDate:false,minDate:'#F{$dp.$D(\'beginDate\')}'})" style="width:130px;height:20px;"/>&nbsp;&nbsp;
					<a href="javascript:void(0);" iconCls="icon-search" class="easyui-linkbutton" onclick="_search();">查询</a>&nbsp;&nbsp;
					<a href="javascript:void(0);" iconCls="icon-no" class="easyui-linkbutton" onclick="cleanSearch();">清空</a></td>
				</tr>
			</table>
		</form>
	</div>
    <div region="center" border="false" >
		<table id="datagrid">
		  <thead>  
	        <tr>  
	        <th data-options="field : 'id',width : 120 ,checkbox : true"  >站点区域</th>
                <th data-options="field : 'bcity',width : 80">所属城市</th> 
                <th data-options="field : 'bregion',width : 80">所属区县</th> 
	            <th data-options="field : 'bnumber',width : 100">站点编号</th>  
	            <th data-options="field : 'pname',width : 200">任务名称</th>  
	            <th data-options="field : 'xreptime',width : 150">最近上报时间</th>  
	            <th data-options="field : 'xgname',width : 120">维护队</th>  
	            <th data-options="field : 'bname',width : 160">站点名称</th>  
	            <th data-options="field : 'baddress',width : 210">站点地址</th> 
	             <th data-options="field : 'bposx',width:120">经度</th>
	            <th data-options="field : 'bposy',width:120">纬度</th> 
	            <th data-options="field : 'bfactory',width : 100">集成厂家</th>  
	            <th data-options="field : 'xgname',width : 100">所属代维商</th> 
	           
	      
	   
	        </tr>  
    		</thead>  
		</table>
	</div>
  </body>
</html>
