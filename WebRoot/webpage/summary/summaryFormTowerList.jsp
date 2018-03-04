<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/tld.jsp" %>
<!DOCTYPE HTML>
<html>
  <head>
    <title>铁塔统计</title>
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
			url:'${pageContext.request.contextPath}/summary/summaryFormAction!summaryFormDatagrid.action?btype=1',
			iconCls : 'icon-tip',
			pagination : true,
			pagePosition : 'bottom',
			//rownumbers:true,
			fit : true,
			nowrap : true,
			autoRowHeight : false,
			selectOnCheck : true,
			border : false,
			idField : 'id',
			striped :true,
			singleSelect : false,
			columns : [[
			        {title : '编号',field : 'id',width : 120,checkbox:true},
			        {title : '所属城市',field : 'bcity',width : 80},
			        {title : '所属区县',field : 'bregion',width : 80},
			        {title : '物理编号',field : 'bnumber',width : 100},
			        {title : '任务名称',field : 'pname',width : 200},
			        {title : '最近上报时间',field : 'xreptime',width : 150},
			        {title : '维护队',field : 'xgname',width : 120},
			    	{title : '基站名称',field : 'bname',width : 160},
			    	{title : '基站位置',field : 'baddress',width : 210},
			    	{title : '经度',field : 'bposx',width : 120},
			    	{title : '纬度',field : 'bposy',width : 120},
			    	{title : '自建（共享）塔',field : 'btower',width : 120},
			
			]],
			toolbar : [ '-', {
			text : '台帐导出',
			iconCls : 'icon-add',
			handler : function() {
				downloadfile1();
			}
			}, '-', {
			text : '维护巡检表导出',
			iconCls : 'icon-add',
			handler : function() {
				towerInfoList();
			}
			}/*, '-', {
			text : '维护寻检表打印',
			iconCls : 'icon-add',
			handler : function() {
				towerprint();
			}
			}*//*, '-', {
			text : '配置文件导入',
			iconCls : 'icon-add',
			handler : function() {
				configexcel();
			}
			}*//*, '-', {
			text : '测试总账',
			iconCls : 'icon-add',
			handler : function() {
				downloadfile();
			}
			}*/
			,'-',{
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
	
	
	
			//配置文件导入
	function configexcel() {
		 var p = parent.sy.dialog({
			title : '请选择导入的文件',
			iconCls : 'icon-tip',
			resizable: true,
			collapsible:true,
			href:'${pageContext.request.contextPath}/baseInfo/baseInfoAction!baseInfoExcel.action',
			width : 400,
			height : 135,
			buttons : [ {
				text : '确定',
				iconCls : 'icon-ok',
				handler : function() {
				$.messager.show({
								title : '提示',
								msg : "正在导入，请稍后"
							});
					var f = p.find('form');
					f.form('submit', {
						url :'${pageContext.request.contextPath}/summary/summaryFormAction!testConfig.action?flag=1',
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
		//铁塔维护导出
	function towerInfoList() {
		var taskids = [];
		var ids = [];
		var rows = datagrid.datagrid('getSelections');
		if (rows.length > 0) {
			for ( var i = 0; i < rows.length; i++) {
			//alert("rows[i].itaskid="+rows[i].itaskid  +"      rows[i].bnumber="+rows[i].bnumber);
				taskids.push(rows[i].itaskid);
				ids.push(rows[i].id);
			}
	 
		window.location.replace('${pageContext.request.contextPath}/summary/summaryFormAction!InfoList.action?flag=1&btype=1&ids='+ids+'&taskids='+taskids);
		} else {
			$.messager.alert('提示', '请选择要导出的记录！', 'error');
		}
	}
	
					//巡检图片		
	function MsgImage1() {
	//alert("dd");
			//	window.location.replace('${pageContext.request.contextPath}/query/inspectmessageAction!inspectmessageImage.action?XMId='+rid);
	window.showModalDialog('${pageContext.request.contextPath}/problem/ProblemAction!test.action',"标题","dialogwidth:820px;dialogheight:960px;status=0;scroll=0;");
			}
			//铁塔打印
	function towerprint() {
		var taskids = [];
		var ids = [];
		var rows = datagrid.datagrid('getSelections');
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
			var url="${pageContext.request.contextPath}/summary/summaryFormAction!towerprint.action?flag=1&btype=1&ids="+ids+"&taskids="+taskids;
		//	window.showModalDialog(url,"标题","dialogwidth:820px;dialogheight:960px;dialogTop: 458px; dialogLeft: 166px; edge: Raised; center: Yes; help: Yes; resizable: Yes; status: Yes;menubar=Yes;");
			window.open (url,'维护表打印','height=900,width=1020,top=50,left=50,toolbar=yes,menubar=yes,scrollbars=yes, resizable=yes,location=no, status=no') ;
		} else {
			$.messager.alert('提示', '请选择要导出的记录！', 'error');
		}
	}
	
		//台帐导出
	/*function downloadfile() {
		alert( $("#entid").val() ) ;
		alert('${pageContext.request.contextPath}/summary/summaryFormAction!toExcel.action?flag=1&btype=1&entid='+$("#entid").val()+'&itaskid='+$("#itaskid").val()+'&xuid='+$("#xuid").val()+'&xgid='+$("#xgid").val()+'&xequid='+$("#xequid").val()+'&rpsdate='+$("#beginDate").val()+'&rpedate='+$("#dissucsdate").val()+'&bcity='+$("#bcity").val()+'&bregion='+$("#bregion").val());
		//window.location.replace('${pageContext.request.contextPath}/summary/summaryFormAction!toExcel.action?flag=1&btype=1&entid='+$("#entid").val()+'&itaskid='+$("#itaskid").val()+'&xuid='+$("#xuid").val()+'&bcity='+$("#bcity").val()+'&bregion='+$("#bregion").val()+'&xgid='+$("#xgid").val()+'&xequtnum='+$("#xequtnum").val()+'&rpsdate='+$("#beginDate").val()+'&rpedate='+$("#dissucsdate").val());
		//window.location.replace('${pageContext.request.contextPath}/summary/summaryFormAction!test.action');
	}*/
	
	
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
	/*var entid=$("#entid").val();
	if(entid==0){
		alert("请选择分公司");
		return ;
	}*/
		//alert( $("#entid").val() ) ;
		window.location.replace('${pageContext.request.contextPath}/summary/summaryFormAction!toExcel1.action?flag=1&btype=1&entid='+$("#entid").val()+'&itaskid='+$("#itaskid").val()+'&ecity='+$("#ecity").val()+'&eregion='+$("#eregion").val()+'&xgid='+$("#xgid").val()+'&xequtnum='+$("#xequtnum").val()+'&rpsdate='+$("#beginDate").val()+'&rpedate='+$("#dissucsdate").val());
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
   	<div region="north" border="false" title="查询条件"  iconCls="icon-search" style="height:75px;overflow: hidden;" align="left">
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
				       </td><%--
				       <th width="8%">巡检人：</th>
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
		</table>
	</div>
  </body>
</html>
