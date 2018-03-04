<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/tld.jsp"%>
<script type="text/javascript" charset="utf-8">
	var inspectmessageView_datagrid;
	var editRow = undefined;
	$(function(){
	   inspectmessageView_datagrid=$('#inspectmessageView_datagrid').datagrid({
			url:'${pageContext.request.contextPath}/query/inspectmessageAction!msgInfoDatagrid.action?XMessageId=${msgid}',
			iconCls : 'icon-tip',
			pagination : true,
			pagePosition : 'bottom',
			fit : true,
			nowrap : false,
			autoRowHeight : false,
			border : false,
			idField : 'id',
			columns : [ [
				    {title : '大类名称',field : 'xpgname',width : 150},
				    {title : '小类名称',field : 'xproname',width : 180},
				    {title : '数据类型',field : 'xptype',width : 80,formatter : function(val, rec) {
								if (val == '0') {
									return '数字';
								}else if(val == '1') {
									return '枚举';
								}else if(val == '2') {
									return '字符串';
								}else{
									return '';
								}
							}},
					{title : '巡检结果',field : 'xvalue',width : 180, editor :'text'},
					{title : '提交时间',field : 'xreptime',width : 150}
				    ] ],
			onClickCell : function(rowIndex, rowData) {
			//console.info(rowData);
			//console.log('代替alert哦'+rowData);
				if (editRow != undefined) {
					inspectmessageView_datagrid.datagrid('endEdit', editRow);
				}
				if (editRow == undefined) {
					inspectmessageView_datagrid.datagrid('beginEdit', rowIndex);
					editRow = rowIndex;
					inspectmessageView_datagrid.datagrid('unselectAll');
				}
			},
			onAfterEdit : function(rowIndex, rowData, changes) {
				//	console.info(rowData);
				var updated = inspectmessageView_datagrid.datagrid('getChanges', 'updated');
					
				if (updated.length < 1) {
					editRow = undefined;
					inspectmessageView_datagrid.datagrid('unselectAll');
					return;
				}
				var url = '';
				if (updated.length > 0) {
					url='${pageContext.request.contextPath}/query/inspectmessageAction!editinspectmessageModify.action';
				}
				$.ajax({
					url : url,
					data : rowData,
					dataType : 'json',
					success : function(r) {
						if (r.success) {
							inspectmessageView_datagrid.datagrid('acceptChanges');
							inspectmessageView_datagrid.datagrid('load');
						} else {
							inspectmessageView_datagrid.datagrid('rejectChanges');
							$.messager.alert('错误', r.msg, 'error');
						}
						editRow = undefined;
						inspectmessageView_datagrid.datagrid('unselectAll');
						
					}
				});
			}
		});
	 });
	 
	 	
	function updateOut(){
		if (editRow != undefined) {
			inspectmessageView_datagrid.datagrid('endEdit', editRow);
		   }
		    $('#inspectmessageView_datagrid').form('submit', {
			url : '${pageContext.request.contextPath}/query/inspectmessageAction!editinspectmessageModify.action',
			success : function(d) {
				var json = $.parseJSON(d);
				if (json.success) {
				 	inspectmessageView_datagrid.datagrid('unselectAll');
				 	sy.pageRefresh();
					$('#editDialog').dialog('close');
					parent.sy.messagerShow({
						msg : json.msg,
						title : '提示'
					});
				}
			}
			});
		}
	function cancelUpdateOut(){
		$('#editDialog').dialog('close');
		sy.pageRefresh();
		$('#inspectmessageView_datagrid').datagrid('unselectAll');
	}	
	</script>
	<div id="outbillAdd_add_div" class="easyui-layout" style="width:980px;height:510px;" fit="true">  
	   
	    <div data-options="region:'center',title:'巡检信息'"  border="false">
	   		<table width="760px" height="90px" id="inspectmessageView_datagrid"></table>
	   	</div>
	   	
	   	   <div data-options="region:'south'"  border="false" align="right" style="height:40px;">
      <a href="javascript:void(0);" iconCls="icon-ok" class="easyui-linkbutton" onclick="updateOut();">保存</a>&nbsp;&nbsp;
      <a href="javascript:void(0);" iconCls="icon-cancel" class="easyui-linkbutton" onclick="cancelUpdateOut();">取消</a>
    </div> 
   	</div>
