<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/tld.jsp"%>	
<%
String itaskid=request.getParameter("itaskid");
String bnumber=request.getParameter("bnumber");
System.out.println("ddddd="+itaskid+"      "+bnumber);
String aa="2";
System.out.println("summaryFormAction summaryFormDetailsDatagrid.action?itaskid="+itaskid+"&bnumber="+bnumber);
 %>
<script language="javascript">
var searchForm;
	var datagrid2;
	$(function(){
	 datagrid2 = $('#datagrid2').datagrid({
			url:'${pageContext.request.contextPath}/summary/summaryFormAction!summaryFormDetailsDatagrid.action?itaskid=${param.itaskid}&bnumber=${param.bnumber}',
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
			singleSelect : true
		});
	});
	</script>

		<table id="datagrid2" >
		  <thead>  
	        <tr>  
	            <th data-options="field : 'id',width : 120">编号</th>  
	            <th data-options="field : 'entid',width : 120">代维商编号</th>  
	            <th data-options="field : 'xproid',width : 100">巡检项编号</th>  
	            <th data-options="field : 'xproname',width : 100">巡检项名称</th>  
	            <th data-options="field : 'xmaxvalue',width : 100">最大值</th>  
	            <th data-options="field : 'xminvalue',width : 100">最小值</th>  
	            <th data-options="field : 'xvalue',width : 100">平均值</th>  
	            <th data-options="field : 'xreptime',width : 100">上报时间</th>  
	            <th data-options="field : 'xdesc',width : 100">描述</th>  
	            <th data-options="field : 'itaskid',width : 100">任务编号</th>  
	        </tr>  
	     
	      
    		</thead>  
		</table>

