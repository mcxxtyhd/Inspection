<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/tld.jsp" %>
<!DOCTYPE HTML>
<html>
  <head>
    <title>巡检问题信息管理</title>
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
			url:'${pageContext.request.contextPath}/problem/ProblemAction!problemDatagrid.action',
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
			}/*, '-', {
				text : '巡检数据id转换',
				iconCls : 'icon-edit',
				handler : function() {
					test("1");
				}
			}, '-', {
				text : '巡检问题id转换',
				iconCls : 'icon-edit',
				handler : function() {
					test("2");
				}
			}
			, '-', {
				text : '巡检员名称修改',
				iconCls : 'icon-edit',
				handler : function() {
					test1();
				}
			}*/
			,'-', {
				text : '取消选中',
				iconCls : 'icon-undo',
				handler : function() {
					datagrid.datagrid('unselectAll');
				}
			},'-' ]
		});
		$("#entid").change(function(){
			var entid=$("#entid").val();
			var iuseridSelect = $(".iuserid").children("select");
			var proitaskidSelect = $(".proitaskid").children("select");
				$.ajax({
					url:'${pageContext.request.contextPath}/query/planAction!cascodeToTaskAndGroup.action',
					data:  { entid: entid },
					dataType:"json",
					success:(function(json){
					if(json.flag==0){
						$("#iuserid").empty();
							$("#iuserid").append("<option value=''>请选择</option>").appendTo(iuseridSelect);
							for (var i = 0; i < json.glist.length; i++) {
								$("#iuserid").append("<option value=\""+json.glist[i].id+"\">"+json.glist[i].gname+"</option>");
							}
						$("#proitaskid").empty();
						//alert(json.tlist.length);
						$("#proitaskid").append("<option value=''> 请选择</option>").appendTo(proitaskidSelect);
						for(var i=0;i<json.tlist.length;i++){
							$("#proitaskid").append("<option value=\""+json.tlist[i].id+"\">"+json.tlist[i].pname+"</option>")
						}	
					}
					else{
						
					}
					})
				})
		})
	});
	function test1(){
	window.location.replace('${pageContext.request.contextPath}/basis/userAction!userList1.action');
	}
				
				//巡检图片		
	function test(flag1) {
	alert(flag1);
			$.messager.confirm('请确认', '您要删除当前选择的信息吗？', function(r) {
			if(r){
			
					$.ajax({
						url:'${pageContext.request.contextPath}/problem/ProblemAction!test.action?flag='+flag1,
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
			
		
	//window.showModalDialog('${pageContext.request.contextPath}/problem/ProblemAction!test.action',"标题","dialogwidth:820px;dialogheight:960px;status=0;scroll=0;");
			}
function daochu() {
	 $.messager.show({
						title : '提示',
						msg : "正在导出，请耐心等待......",
						showType: 'slide',
						width:250,
						height:100,
         				timeout: 5000
					});
	//alert($("#entid").val());
// alert('${pageContext.request.contextPath}/summary/summaryFormAction!toExcel.action?flag=1&btype=1&entid='+$("#entid").val()+'&itaskid='+$("#itaskid").val()+'&xuid='+$("#xuid").val()+'&xgid='+$("#xgid").val()+'&xequid='+$("#xequid").val()+'&rpsdate='+$("#beginDate").val()+'&rpedate='+$("#dissucsdate").val());
	    //     alert('${pageContext.request.contextPath}/problem/ProblemAction!problemtoExcel.action?entid='+$("#entid").val()+'&proitaskid='+$("#proitaskid").val()+'&iuserid='+$("#iuserid").val()+'&procycle='+$("#procycle").val()+'&prositenum='+$("#prositenum").val()+'&prosite='+$("#prosite").val()+'&protype='+$("#protype").val());
		window.location.replace('${pageContext.request.contextPath}/problem/ProblemAction!problemtoExcel.action?entid='+$("#entid").val()+'&proitaskid='+$("#proitaskid").val()+'&iuserid='+$("#iuserid").val()+'&procycle='+$("#procycle").val()+'&prositenum='+$("#prositenum").val()+'&prosite='+$("#prosite").val()+'&protype='+$("#protype").val());//
	}
	//增加操作
	function append() {
		 var p = parent.sy.dialog({
			title : '巡检问题新增',
			iconCls : 'icon-tip',
			resizable: true,
			collapsible:true,
			href: '${pageContext.request.contextPath}/problem/ProblemAction!problemAdd.action',
			width : 450,
			height : 430,
			buttons : [ {
				text : '保存',
				iconCls : 'icon-ok',
				handler : function() {
					var f = p.find('form');
					f.form('submit', {
						url :'${pageContext.request.contextPath}/problem/ProblemAction!addProblem.action',
						success : function(d) {
							var json = $.parseJSON(d);
							if (json.success) {
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
		        url:'${pageContext.request.contextPath}/problem/ProblemAction!isadmin.action',
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
									url:'${pageContext.request.contextPath}/problem/ProblemAction!removeProblem.action',
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
				title : '巡检问题修改',
				iconCls :'icon-tip',
				resizable: true,
			    collapsible:true,
				href : '${pageContext.request.contextPath}/problem/ProblemAction!problemEdit.action',
				width : 450,
				height : 430,
				buttons : [ {
					text : '保存',
					iconCls : 'icon-ok',
					handler : function() {
						var f = p.find('form');	
						f.form('submit', {
							url : '${pageContext.request.contextPath}/problem/ProblemAction!editProblem.action',
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
					       <select name="proitaskid" id="proitaskid" style="width:130px;height:20px;">
					        <option value="0">所有</option>
						    <c:forEach items="${TaskList}" var="task">
					  		 <option value='<c:out value="${task.id}"/>'><c:out value="${task.pname}"/></option>
					  	    </c:forEach>
					      </select>
				       </td>
				  <th width="6%">维护队：</th>
					<td width="6%">
				 		 <select name="iuserid" id="iuserid"style="width:130px;height:20px;">
					        <option value="0">所有</option>
						    <c:forEach items="${GroupList}" var="group">
					  		 <option value='<c:out value="${group.id}"/>'><c:out value="${group.gname}"/></option>
					  	    </c:forEach>
					      </select> 
					 </td> 
				 <th width="6%">巡检周期：</th>
				    <td>
				     <input name="procycle" id="procycle" class="Wdate" onfocus="WdatePicker({startDate:'%y-%M',dateFmt:'yyyy-MM ',alwaysUseStartDate:true})" editable="false" style="width:130px;height:20px;" />
				   </td> 
		</tr>
				<tr>
		  		    <th width="10%">设备编号：</th>
				    <td width="6%">
				     <input name="prositenum"   id="prositenum" style="width:130px;height:20px;" />
				   </td> 
				  <th width="10%">设备名称：</th>
					<td ><input name="prosite" id="prosite" style="width:130px;height:20px;" /></td>
				
			
					<th width="6%">问题类型：</th>
				<td colspan="3"><input name="protype" id="protype"style="width:130px;height:20px;" />
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
		 <thead> 
		 <tr>    
		         <th data-options="field : 'id',width : 80,checkbox:true">编号</th>  
		        <th data-options="field : 'proitaskname',width : 120">任务名称</th> 
		        <th data-options="field : 'procycle',width : 110">巡检周期</th> 
	            <th data-options="field : 'prositenum',width : 120">设备编号</th>  
	            <th data-options="field : 'prositename',width : 150">设备名称</th>  
	            <th data-options="field : 'protype',width : 120">问题类型</th> 
	            <th data-options="field : 'prodesc',width:170">问题描述</th> 
	            <th data-options="field : 'iusername',width : 150">维护队</th> 
	            <th data-options="field : 'ternumber',width : 150">终端编号</th> 
	            <th data-options="field : 'createtime',width : 170">提交时间</th> 
	            <!--<th data-options="field : 'test',width : 170" onclick="MsgImage1()">提交时间</th> 
	            
	        --></tr>  
		 </thead>
		 </table>
	</div>
  </body>
</html>
