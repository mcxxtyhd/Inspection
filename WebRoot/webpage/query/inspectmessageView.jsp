<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/tld.jsp"%>
<script type="text/javascript" charset="utf-8">
	var inspectmessageView_datagrid;
	$(function(){
	   inspectmessageView_datagrid=$('#inspectmessageView_datagrid').datagrid({
			url:'${pageContext.request.contextPath}/query/inspectmessageAction!msgInfoDatagrid.action?XMessageId=${XMessaheId}',
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
					{title : '巡检结果',field : 'xpvalue',width : 180},
					{title : '提交时间',field : 'xreptime',width : 150}
				    ] ]
		});
	 });
	function downFileEdit(imgname){  
		window.location.replace('${pageContext.request.contextPath}/query/inspectmessageAction!documentDown.action?ImgName='+imgname);		
   }
	</script>
	<div id="outbillAdd_add_div" class="easyui-layout" style="width:980px;height:500px;" fit="true">  
	    <div data-options="region:'west',title:'站点信息'" style="width:220px;">
		<table width="210px"  class="tableForm">
		    <tr>
				<th>设备编号：</th>
				<td>${ReportMessage.bnumber}</td>
			</tr>
			<tr>
				<th>设备名称：</th>
				<td>${ReportMessage.bname}</td>
			</tr>
			<tr>
				<th>设备地址：</th>
				<td>${ReportMessage.baddress}</td>
			</tr>
			<tr>
				<th>经度：</th>
				<td>${ReportMessage.bposx}</td>
			</tr>
			<tr>
				<th>纬度：</th>
				<td>${ReportMessage.bposy}</td>
			</tr>
			<tr>
				<th>巡检日期：</th>
				<td>${ReportMessageBase.xreptime}</td>
			</tr><!--
			<tr><th>图片信息</th><td></td></td></tr>
			<tr>
				<td colspan="2">
				 <table width="100%">
					<c:forEach items="${ImgList}" var="img">
					  <tr>
						<td>
						  <a href="#"onclick="downFileEdit('${img.imgname}')">${img.imgname}</a>															
					   </td>
					 </tr>
				 </c:forEach>
				</table>
				 </td>
			</tr>
		--></table>
	</div> 
	    <div data-options="region:'center',title:'巡检信息'"  border="false">
	   		<table id="inspectmessageView_datagrid"></table>
	   	</div>
   	</div>
