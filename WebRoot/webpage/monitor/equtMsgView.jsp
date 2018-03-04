<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/tld.jsp"%>
   
  <script type="text/javascript" charset="utf-8">
	
	var searchForm ; 
	var datagrid ;
	var vl ;

	
	
		//查询操作
	function _search() {
//	alert("") ;
		datagrid.datagrid('load', sy.serializeObject(searchForm));
	}
	
	//清空操作 
	function cleanSearch() {
		datagrid.datagrid('load', {});
		searchForm.find('select').val('');
		searchForm.find('input').val('');
	}
	
	$(function(){
		searchForm=$('#searchForm').form();
	 	datagrid = $('#datagrid').datagrid({
			url:'${pageContext.request.contextPath}/query/inspectmessageAction!inapectmsgDatagrid1.action?xequtnum=${xequtnum}' ,
			iconCls : 'icon-tip',
			pagination : true,
			pagePosition : 'bottom',
			rownumbers:true,
			//fit : true,//作用是table的大小跟着上级页面的大小变化
			nowrap : false,
			autoRowHeight : false,
			selectOnCheck : true,
			border : false,
			idField : 'id',
			striped :true,
			singleSelect : true,
			width :1000,
			height :300,
		   iconCls : 'icon-cancel',
			handler : function(){
			  p.dialog('close');
			},
						//双击 事件 
			onClickRow : function(rowIndex, rowData) {
			if(!rowData.ximgname==""){
			}
			else{
			return;
			}
			console.info(rowIndex);
			console.info(rowData);
			var p = parent.sy.dialog({
				title : '设备巡检信息',
				iconCls :'icon-search',
				resizable: true,
			    collapsible:true,
			    href:'${pageContext.request.contextPath}/webpage/monitor/equtMsgPicture.jsp?messgaeDetailIdString='+rowData.id,
				width : 400,
				height : 300,
				buttons : [{
			    text : '关闭',
			    iconCls : 'icon-cancel',
				handler : function(){
				  p.dialog('close');
				}
			   }]
			  });
			}
		});
		
		
		
	
		
	});
	
	</script>
		<form id="searchForm">
			<table class="tableForm datagrid-toolbar" style="width:100%;height:100%;">
				<tr>
		<th width="8%">开始时间：</th>
				<td width="5%"><input class="Wdate" style="width:150px;height:20px;" name="rpsdate" id="beginDate"  onfocus="WdatePicker({startDate:'%y-%M-01',dateFmt:'yyyy-MM-dd ',alwaysUseStartDate:true})"/></td>
				 <th width="8%">结束时间：</th>
				<td>
				   <input class="Wdate" style="width:150px;height:20px;" name="rpedate" id="dissucsdate"  onfocus="WdatePicker({stopDate:'%y-%M-%d',dateFmt:'yyyy-MM-dd',alwaysUseStartDate:false,minDate:'#F{$dp.$D(\'beginDate\')}'})"/>&nbsp;&nbsp;&nbsp;</td>	
				   <td>	
				     
				       <th width="8%">巡检员：</th>
				       <td  ><select name="xuid" style="width:150px;">
				        <option value="0">所有</option>
					    <c:forEach items="${InspectUserList}" var="user">
				  		 <option value='<c:out value="${user.id}"/>'><c:out value="${user.iuname}"/></option>
				  	    </c:forEach>
				      </select>
				<!--	<th width="8%">巡检代维商：</th>
					 <td><select name="xgid" style="width:150px;">
				        <option value="0">所有</option>
					    <c:forEach items="${GroupList}" var="group">
				  		 <option value='<c:out value="${group.id}"/>'><c:out value="${group.gname}"/></option>
				  	    </c:forEach>
				      </select></td>  -->
				
					<a href="javascript:void(0);" iconCls="icon-search" class="easyui-linkbutton" onclick="_search();">查询</a>&nbsp;&nbsp;
					
					<a href="javascript:void(0);" iconCls="icon-no" class="easyui-linkbutton" onclick="cleanSearch();">清空</a>
				     </td>
				</tr>
			</table>
		</form>
	
		<table id="datagrid" style="width: 300px;height: 1000px">
		 <thead>  
	        <tr>  
	            <th data-options="field : 'xuname',width : 100">巡检员</th>  
	            <th data-options="field : 'xgname',width : 100">所属代维商</th> 
	            <th data-options="field : 'xlname',width:100">巡检线路</th>
	            <th data-options="field : 'xproname',width:100">巡检项名称</th>
	            <th data-options="field : 'xreptime',width:150">上报时间</th>
	            <th data-options="field : 'ximgname',width:130">图片名称</th>
	            <th data-options="field : 'xstatusname',width:100">状态</th>
	            <th data-options="field : 'xmaxvalue',width:60">最大值</th>
	            <th data-options="field : 'xminvalue',width:60">最小值</th>
	            <th data-options="field : 'xpvalue',width:60"">默认值</th>
	            
	        </tr>  
    		</thead>   
		</table>
</div>