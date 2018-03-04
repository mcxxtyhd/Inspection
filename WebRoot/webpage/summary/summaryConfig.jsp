<%@ page language="java" pageEncoding="UTF-8"%>
<%@include file="/common/tld.jsp"%> 
<script type="text/javascript" charset="utf-8">
	var summaryConfig_datagrid;
	$(function(){
	//alert(${param.sConfigFlag});
	//alert(${flag})
	 summaryConfig_datagrid = $('#summaryConfig_datagrid').datagrid({
			url:'${pageContext.request.contextPath}/summaryConfig/summaryConfigAction!editExcel.action?flag=${param.sConfigFlag}',
			iconCls : 'icon-tip',
			pagination : true,
			pagePosition : 'bottom',
			fit : true,
			nowrap : true,
			autoRowHeight : false,
			border : false,
			idField : 'id',
			
			columns : [ [
				    {title : '导出的列',field : 'id',width : 120,checkbox:true},
				    {title : '名称',field : 'sname',width : 120}
				    ] ],
			toolbar : [ '-',{
				text : '增加',
				iconCls : 'icon-add',
				handler : function() {
					toExcel();
				}
			}]
			
			
		});
	});
	
	
	//导出excel
	function toExcel() {
		var ids = [];
		var rows = summaryConfig_datagrid.datagrid('getSelections');
		if (rows.length > 0) {
			$.messager.confirm('请确认', '您要导出当前选择的列吗？', function(r) {
				if (r) {
					for ( var i = 0; i < rows.length; i++) {
						ids.push(rows[i].id);
					}
				
					$.ajax({
					
						url:'${pageContext.request.contextPath}/summary/summaryFormAction!toExcel1.action?xgid=${xgid}&xequid=${xequid}&flag=${param.sConfigFlag}',
						data : {
							ids : ids.join(',')
						},
						cache : false,
						dataType : "json",
						success : function(response) {
					//	alert(  response.msg ) ;
							if( response.success ){
								window.location.replace('${pageContext.request.contextPath}/summary/summaryFormAction!partExcel.action?path=' + response.msg ) ;
							}
						//console.info(response);
							//datagrid.datagrid('unselectAll');
							//datagrid.datagrid('reload');
							//$('div.datagrid-header-check input').attr("checked",false);
						}
					});
				}
			});
		} else {
			$.messager.alert('提示', '请选择要导出的列！', 'error');
		}
	}
	</script> 
  <table id="summaryConfig_datagrid"></table>
