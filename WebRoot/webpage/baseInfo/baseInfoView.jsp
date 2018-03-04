<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/tld.jsp" %>
<script type="text/javascript" charset="utf-8">
    var basis_equipmentAdd_searchForm;
    var basis_equipmentAdd_datagrid;
	$(function(){
		basis_equipmentAdd_searchForm=$('#basis_equipmentAdd_searchForm').form();
	    basis_equipmentAdd_datagrid= $('#basis_equipmentAdd_datagrid').datagrid({
			url:'${pageContext.request.contextPath}/baseInfo/baseInfoAction!baseInfoDatagrid.action',
			iconCls : 'icon-tip',
			pagination : true,
			pagePosition : 'bottom',
			fit : true,
			nowrap : false,
			autoRowHeight : false,
			border : false,
			idField : 'id',
			singleSelect : true,
				columns : [[
				    {field : 'id',title : '编号',width : 80,checkbox : true},
				    {title : '设备类型',field : 'btype',width : 120,formatter : function(val, rec) {
								if (val == '1') {
									return '铁塔、天馈线';
								}else{
									return '室内分布及WLAN';
								}
							}},
			        {title : '设备编号',field : 'bnumber',width : 100},
			        {title : '设备名称',field : 'bname',width : 150},
			        {title : '地市',field : 'bcity',width : 100},
			        {title : '区县',field : 'bregion',width : 120}
			    	//{title : '位置',field : 'baddress',width : 220}
			]],
			toolbar :'#basis_equipmentAdd_tb'
		});
	});
	//查询操作
	function baseinfo_search(){
		basis_equipmentAdd_datagrid.datagrid('load', sy.serializeObject(basis_equipmentAdd_searchForm));
	}
	//清空操作 
	function baseinfo_cleanSearch() {
		basis_equipmentAdd_datagrid.datagrid('load', {});
		basis_equipmentAdd_searchForm.find('input').val('');
		basis_equipmentAdd_searchForm.find('select').val('');
	}
	</script>
<div id="basis_equipmentAdd_div" class="easyui-layout" style="width: 650px; height: 560px;" fit="true">
	<div data-options="region:'north'" style="height: 60px;">
		<form id="basis_equipmentAdd_searchForm">
			<table class="tableForm datagrid-toolbar" style="width: 100%; height: 100%;">
				<tr>
				<th width="8%">
						类型：
					</th>
					<td>
						<select name="btype" style="width: 120px; height: 20px;">
							<option value="">请选择</option>
							 <option value="1">铁塔、天馈线</option>
				        	<option value="2">室内分布及WLAN</option>
						</select>
					</td>
					<th width="8%">
						编号：
					</th>
					<td width="8%">
						<input name="bnumber" style="width: 120px; height: 20px;" />
					</td>
					<th width="7%">
						名称：
					</th>
					<td>
						<input name="bname" style="width: 120px; height: 20px;" />
					</td>
					</tr>
					<tr>
					<th width="6%">
						地市：
					</th>
					<td>
						<input name="bcity" style="width: 120px; height: 20px;" />
					</td>
					<th width="6%">
						区县：
					</th>
					<td colspan="3">
						<input name="bregion" style="width: 120px; height: 20px;" />
						&nbsp;&nbsp;&nbsp;&nbsp;
						<a href="javascript:void(0);" iconCls="icon-search" class="easyui-linkbutton" onclick="baseinfo_search();">查询</a>
						<a href="javascript:void(0);" iconCls="icon-no" class="easyui-linkbutton" onclick="baseinfo_cleanSearch();">清空</a>
					</td>
					</tr>
			</table>
		</form>
	</div>
	<div data-options="region:'center',title:'基础信息'" border="false">
		<table id="basis_equipmentAdd_datagrid"></table>
	</div>
</div>
