<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/tld.jsp" %>
<!DOCTYPE HTML>
<html>
  <head>
    <title>巡检点信息管理</title>
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
	//$("p") 选取 <p> 元素。$("p#demo") 选取所有 id="demo" 的 <p> 元素。
	//$("#intro")	id="intro" 的元素
	//在上面的例子中，当按钮的点击事件被触发时会调用一个函数：
    //$("button").click(function() {..some code... } )
    
    //.datagrid是个建表插件
	 datagrid = $('#datagrid').datagrid({
		    //url:'',
			url:'${pageContext.request.contextPath}/basis/docAction!docDatagrid.action',
			iconCls : 'icon-tip',  //在面板上通过一个CSS类显示16x16图标。
			pagination : true,  //是否显示分页工具栏
			pagePosition : 'bottom',
			//rownumbers:true,
			fit : true,   //是否自动适应父容器
			nowrap : false,  //是否包裹数据，默认为包裹数据显示在一行 
			autoRowHeight : false,
			selectOnCheck : true,
			border : false,   //设置面板是否具有边框
			idField : 'id',   //标识字段，或者说主键字段
			striped :true,   //设置是否让单元格显示条纹。默认false。
			fitColumns : true,
			//singleSelect : true,
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
				text : '取消选中',
				iconCls : 'icon-undo',
				handler : function() {
					datagrid.datagrid('unselectAll');
					$('div.datagrid-header-check input').attr("checked",false);
				}
			},'-',{
				text : '巡检点导入',
				iconCls : 'icon-add',
				handler : function() {
					pointexcel();
				}
			} ]
		});
	});
		//巡检点导入操作
	function pointexcel() {
		 var p = parent.sy.dialog({
			title : '请选择导入的文件',
			iconCls : 'icon-tip',
			resizable: true,
			collapsible:true,
			href:'${pageContext.request.contextPath}/baseInfo/baseInfoAction!baseInfoExcel.action?type=4',
			width : 400,
			height : 135,
			buttons : [ {
				text : '确定',
				iconCls : 'icon-ok',
				handler :
				 function() {
				 	$.messager.progress({
			             title: '温馨提示',
			             msg:  '正在导入数据...'
			         });
					var f = p.find('form');
					f.form('submit', {
						url :'${pageContext.request.contextPath}/basis/pointAction!testExel.action',
						success : function(d) {
							var json = $.parseJSON(d);
							if (json.success) {
								datagrid.datagrid('reload');
								p.dialog('close');
							}
							$.messager.progress('close');
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
				$.messager.progress('close');
				}
			}]
		});
	}
	//增加操作
	function append() {
		 var p = parent.sy.dialog({
			title : '巡检点信息新增',
			iconCls : 'icon-tip',
			resizable: true,
			collapsible:true,
			href: '${pageContext.request.contextPath}/basis/pointAction!pointAdd.action',
			width : 600,
			height : 500,
			buttons : [ {
				text : '保存',
				iconCls : 'icon-ok',
				handler : function() {
					var f = p.find('form');
					var a = f.find('select[name="poeIds"]').find("option");
						for(var i = 0; i < a.length; i++){
						    if(a[i].selected='fasle'){
								a[i].selected=true;// 默认选中
							}else{
								a[i].selected=true;// 默认选中
							}
						}
					f.form('submit', {
						url : '${pageContext.request.contextPath}/basis/pointAction!addPoint.action',
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
		        url:'${pageContext.request.contextPath}/basis/pointAction!isadmin.action',
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
							if (r) {
								for ( var i = 0; i < rows.length; i++) {
									ids.push(rows[i].id);
								}
								$.ajax({
									url:'${pageContext.request.contextPath}/basis/pointAction!deletePoint.action',
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
			//console.info(rows[0]);
			var p = parent.sy.dialog({
				title : '巡检点信息修改',
				iconCls :'icon-tip',
				resizable: true,
			    collapsible:true,
				href : '${pageContext.request.contextPath}/basis/pointAction!pointEdit.action?PID='+rows[0].id+'&flag=1',
				width : 600,
				height : 500,
				buttons : [ {
					text : '保存',
					iconCls : 'icon-ok',
					handler : function() {
						var f = p.find('form');
						var a = f.find('select[name="poeIds"]').find("option");
						for(var i = 0; i < a.length; i++){
						    if(a[i].selected='fasle'){
								a[i].selected=true;// 默认选中
							}else{
								a[i].selected=true;// 默认选中
							}
						}
						f.form('submit', {
							url : '${pageContext.request.contextPath}/basis/pointAction!editPoint.action',
							success : function(d) {
								var json = $.parseJSON(d);
								if (json.success) {
									datagrid.datagrid('unselectAll');
									datagrid.datagrid('reload');
									p.dialog('close');
								}
								parent.sy.messagerShow({
									msg : json.msg,
									title : '提示'
								});
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
					   f.form('load', {
						    id : rows[0].id,
							company : rows[0].operator,
							applyid : rows[0].applyid,
							baseid : rows[0].baseid,
							basename : rows[0].basename,
							baseaddress : rows[0].address
							)
					});
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
   	<div region="north" border="false" title="查询条件"  iconCls="icon-search" style="height:57px;overflow: hidden;" align="left">
		<form id="searchForm">
			<table class="tableForm datagrid-toolbar" style="width: 100%;height: 100%;">
				<tr>
				  <th width="8%">所属区县：</th>
				  <td width="10%">
				     <select name="administrativedivision" id="administrativedivision"  style="width:150px;height:20px;">
				        <option value="0">请选择</option>
				        <c:forEach items="${UnicomList}" var="ent">
				  		 <option value='<c:out value="${ent.administrativedivision}"/>'><c:out value="${ent.administrativedivision}"/></option>
				  	    </c:forEach>
				      </select>
				  </td>
				 <th width="8%">基站名称：</th>
				 <td width="10%">
				     <select name="basename" id="basename"  style="width:150px;height:20px;">
				        <option value="0">请选择</option>
				        <c:forEach items="${UnicomList}" var="ent">
				  		 <option value='<c:out value="${ent.basename}"/>'><c:out value="${ent.basename}"/></option>
				  	    </c:forEach>
				      </select>
				  </td>
				  
				<td>
				   <input name="poname" style="width:150px;height:20px;" />&nbsp;&nbsp;&nbsp;
					<a href="javascript:void(0);" iconCls="icon-search" class="easyui-linkbutton" onclick="_search();">查询</a>&nbsp;&nbsp;
					<a href="javascript:void(0);" iconCls="icon-no" class="easyui-linkbutton" onclick="cleanSearch();">清空</a>
				<tr>
				</tr>
			</table>
		</form>
	</div>
    <div region="center" border="false" >
		<table id="datagrid">
		  <thead>  
	        <tr>  
	            <th data-options="field : 'id',width : 80,checkbox:true">编号</th>  
	            <th data-options="field : 'company',width : 160">厂商</th> 
	            <th data-options="field : 'applyid',width:100">申请表编号</th>
	            <th data-options="field : 'baseid',width:680">基站编号</th>
	             <th data-options="field : 'basename',width:100">基站名称</th>
	            <th data-options="field : 'baseaddress',width:680">基站地址</th>
	        </tr>  
    		</thead>  
		</table>
	</div>
  </body>
</html>
