<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/tld.jsp" %>
<!DOCTYPE HTML>
<html>
  <head>
    <title>巡检设备信息管理</title>
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
	//$("#intro")	id="intro" 的元素
	 datagrid = $('#datagrid').datagrid({
			url:'${pageContext.request.contextPath}/basis/formsAction!formsDatagrid.action',
			iconCls : 'icon-tip',
			pagination : true,   //显示分页  
			pagePosition : 'bottom',
			//rownumbers:true,
			fit : true,  //自动补全 
			nowrap : false,
			autoRowHeight : false,
			selectOnCheck : true,
			border : false,
			idField : 'id',
			striped :true,
			//singleSelect : true,
			//fitColumns : true,
			
			toolbar : [ 
			         
/*
			            '-',{
				text : '增加',
				iconCls : 'icon-add',
				handler : function() {
					append();
				}
			}, 
		
		       '-', {
				text : '删除',
				iconCls : 'icon-remove',
				handler : function() {
					remove();
				}
			}, 
			*/
		'-', {
				text : '下载报表',
				iconCls : 'icon-edit',
				handler : function() {
					downloadfile();
				}
			}, 
			 
			'-', {
				text : '添加平面图',
				iconCls : 'icon-edit',
				handler : function() {
					addmappic();
				}
			},'-', {
				text : '导出报表',
				iconCls : 'icon-remove',
				handler : function() {
					makeforms();
				}
			}, '-', {
				text : '取消选中',
				iconCls : 'icon-undo',
				handler : function() {
					datagrid.datagrid('unselectAll');
					$('div.datagrid-header-check input').attr("checked",false);
				}
			},  '-',{
				text : '基站信息导入',
				iconCls : 'icon-add',
				handler : function() {
					baseformsexcel();
				}
			}
			
			]
		});
	});
	function baseformsexcel() {
		 var p = parent.sy.dialog({
			title : '请选择导入的文件',
			iconCls : 'icon-tip',
			resizable: true,
			collapsible:true,
			href:'${pageContext.request.contextPath}/basis/formsAction!baseFormsExcel.action?type=8',
			width : 400,
			height : 135,
			buttons : [ {
				text : '确定',
				iconCls : 'icon-ok',
				handler : function() {
				 	$.messager.show({
							title : '提示',
							msg : "正在导入，请耐心等待......",
							showType: 'slide',
							width:250,
							height:100,
         					timeout: 5000
						});
					var f = p.find('form');
					f.form('submit', {
						url :'${pageContext.request.contextPath}/basis/formsAction!formsExel2db.action?flag=8',
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
	function makeforms() {
		var ids = [];
		var rows = datagrid.datagrid('getSelections');
		if (rows.length > 0) {
				for ( var i = 0; i < rows.length; i++) {
						ids.push(rows[i].id);
					}
			$.ajax({
				 
				async : false,
				//cache：要求为Boolean类型的参数，默认为true（当dataType为script时，默认为false）。 设置为false将不会从浏览器缓存中加载请求信息。
      			cache : false,
				//type: 要求为String类型的参数，请求方式（post或get）默认为get。
				type : "post",
				//url: 要求为String类型的参数，（默认为当前页地址）发送请求的地址。
		        url:'${pageContext.request.contextPath}/basis/formsAction!ismakeforms.action',
		  
		        data : {
					ids : ids.join(',')
				},
	 
				success:function(data){
					var json = $.parseJSON(data);
				    if (json.success) {
		 
						$.messager.confirm('请确认', '您要导出当前选择的基站信息吗？', function(r) {
							if (r) {
							  	$.messager.show({
									title : '提示',
									msg : "正在导出报表，请耐心等待......",
									showType: 'slide',
									width:250,
									height:100,
	           						timeout: 5000
								});
							  	
								$.ajax({
									url:'${pageContext.request.contextPath}/basis/formsAction!makeforms.action',
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
			$.messager.alert('提示', '请选择要导出的基站报表！', 'error');
		}
	}
	
	//修改操作
	function addmappic() {
		var rows = datagrid.datagrid('getSelections');
		if (rows.length == 1) {
			//console.info(rows[0]);
			var p = parent.sy.dialog({
				title : '添加基站平面图',
				iconCls :'icon-tip',
				resizable: true,
			    collapsible:true,
				href : '${pageContext.request.contextPath}/basis/formsAction!formsAddmappic.action?ID='+rows[0].id,
		
				width : 550,
				height : 500,
				buttons : [ {
					text : '上传',
					iconCls : 'icon-ok',
					handler : function() {
						var f = p.find('form');
						var a = f.find('select[name="epids"]').find("option");
						for(var i = 0; i < a.length; i++){
						    if(a[i].selected='fasle'){
								a[i].selected=true;// 默认选中
							}else{
								a[i].selected=true;// 默认选中
							}
						}
						f.form('submit', {
						url : '${pageContext.request.contextPath}/UploadAction2/UploadAction2.action',
						//success : function(d) d就是从URL test.json 获取（get）的 数据，其数据格式是：data：{username:值,conetent:值}		
						success : function(d) {
								var json = $.parseJSON(d);
								if (json.success) {
									datagrid.datagrid('unselectAll');
									datagrid.datagrid('reload');
									p.dialog('close');
								}else{
									p.dialog('close'); //失败把弹出窗关闭
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
					   f.form('load',rows[0]);
				}
			});
		} else if (rows.length > 1) {
			parent.sy.messagerAlert('提示', '同一时间只能添加一张平面图！', 'error');
		} else {
			parent.sy.messagerAlert('提示', '请选择要要添加平面图的基站！', 'error');
		}
	}
	
	//增加操作
	function append() {
		 var p = parent.sy.dialog({
			title : '基站信息新增',
			iconCls : 'icon-tip',
			resizable: true,
			collapsible:true,
			href: '${pageContext.request.contextPath}/basis/formsAction!formsAdd.action',
			width : 550,
			height :500,
			buttons : [ {
				text : '保存',
				iconCls : 'icon-ok',
				handler : function() {
					var f = p.find('form');
					var a = f.find('select[name="license"]').find("option");
						for(var i = 0; i < a.length; i++){
						    if(a[i].selected='fasle'){
								a[i].selected=true;// 默认选中
							}else{
								a[i].selected=true;// 默认选中
							}
						}
					f.form('submit', {
						url : '${pageContext.request.contextPath}/basis/formsAction!addForms.action',
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
	//下载报表
	function downloadfile() {
		 var p = parent.sy.dialog({
			title : '报表下载',
			iconCls : 'icon-tip',
			resizable: true,
			collapsible:true,
			href: '${pageContext.request.contextPath}/basis/formsAction!downloadui.action',
			width : 300,
			height :300,

		});
	}
	
	
	
	//查询操作
	function _search() {
		///*将searchForm表单内的元素序列化为对象，扩展Jquery的一个方法*/
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
   	<div region="north" border="false" title="查询条件" iconCls="icon-search" style="height:57px;overflow: hidden;" align="left">
		<form id="searchForm">
			<table class="tableForm datagrid-toolbar" style="width: 100%;height: 100%;">
				<tr>
				 
				  <th width="10%">运营商：</th>
				  <td width="10%">
				     <select name="operator" style="width:130px;height:20px;">
				        <option value="">请选择</option>
				        <option value="联通">联通</option>
				        <option value="电信">电信</option>
				
				      </select>
				  </td> 
				  
				  <th width="8%">区县：</th>
				  <td width="10%">
				    <select name="administrativedivision" id="administrativedivision"  style="width:150px;height:20px;">
				        <option value="">请选择</option>
				          <option value="虹口">虹口区</option>
				          <option value="闵行">闵行区</option>
				          <option value="闸北">闸北区</option>
				          <option value="宝山">宝山区</option>
				          <option value="浦东">浦东区</option>
				          <option value="杨浦">杨浦区</option>
				           <option value="静安">静安区</option>
				            <option value="长宁">长宁区</option>
				            <option value="徐汇">徐汇区</option>
				          <option value="金山">金山区</option>
				          <option value="南汇">南汇区</option>
				          <option value="青浦">青浦区</option>	
				          <option value="普陀">普陀区</option>
				          <option value="奉贤">奉贤区</option>	
				          <option value="卢湾">卢湾区</option>
				          <option value="黄浦">黄浦区</option>	
				          <option value="嘉定">嘉定区</option>
				          <option value="松江">松江区</option>	
				          <option value="崇明">崇明区</option>	
				           		          
				      </select>
				  </td>
 
				<th width="8%">申请编号：</th>
				<td width="10%"><input name="license" style="width:200px;height:20px;" /></td>
				 
				<td>
				    &nbsp;&nbsp;&nbsp;
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
	            <th data-options="field : 'id',width : 50,checkbox:true">编号</th>  
	            <th data-options="field : 'operator',width : 60">运营商</th>  
	            <th data-options="field : 'administrativedivision',width : 60">区县</th> 	            
	            <th data-options="field : 'license',width:200">申请编号</th> 
	            <th data-options="field : 'baseid',width:140">基站编号</th>
	            <th data-options="field : 'basename',width:100">基站名称</th>
	            <th data-options="field : 'address',width:320">地址</th>
	            <th data-options="field : 'remark',width:200">备注</th>
	        </tr>  
    		</thead>  
		</table>
	</div>
  </body>
</html>
