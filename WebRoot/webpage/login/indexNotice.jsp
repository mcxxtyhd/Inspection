<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/tld.jsp" %>
<script type="text/javascript" src="${pageContext.request.contextPath}/jslib/common.js" charset="utf-8">
</script>
   	<script type="text/javascript" charset="utf-8">
	var searchForm;
	var datagrid5;
	$(function(){
	 searchForm=$('#searchForm').form();
	 datagrid5 = $('#datagrid5').datagrid({
			url:'${pageContext.request.contextPath}/basis/noticeAction!noticeDatagrid1.action',
			iconCls : 'icon-tip',
			pagination : true,
			pagePosition : 'bottom',
			//rownumbers:true,
			fit : true,
			nowrap : false,
			autoRowHeight : false,
			border : false,
			idField : 'id',
			//singleSelect : true,
				columns : [[
			        {title : '公告名称',field : 'ntype',width : 120},
			        {title : '公告内容',field : 'ncontent',width : 240},
			    	{title : '开始日期',field : 'nstarttime',width : 100},
			    	{title : '截止日期',field : 'nendtime',width : 100},
			    	{title : '发表人',field : 'publisher',width : 100}
			]],
			toolbar : [ '-',{
				text : '在线人数：'+${count},
			}]
		});
		
		
	 datagrid2 = $('#datagrid2').datagrid({
			url:'${pageContext.request.contextPath}/query/planQueryAction!tasksummaryDatagrid1.action',
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
			singleSelect : true,
			columns : [[
					{title:'单位' ,field:'entname',width:100},
					{title:'合同站点数' ,field:'esum',width:100},
			    	{title : '计划巡检站点数',field : 'pointCount',width : 100},
					{title:'计划率' ,field:'planrate',width:100},
			    	{title : '已巡个数',field : 'queryCount',width : 100},
			    	{title : '未巡个数',field : 'unqueryCount',width : 100},
			    	{title : '问题上报个数',field : 'errqueryCount',width : 100},
			    	{title : '图片异常个数',field : 'picexcepnum',width : 100},
			    	{title : '合格率',field : 'picrate',width : 100},
			    	{title : '完成率',field : 'rate',width : 100} ,
			    	{title : '总完成率',field : 'totalrate',width : 80,
			    		//styler:'background-color:#ffee00;color:red;'
						 styler: function(value,row,index){
							//if (index =1){
							 return 'background-color:#ffee00;color:red;';
							//}
						}
			    	} 
			    	
			]],
			toolbar : [ '-',{
				text : '本季度巡检情况',
			}]
		});

});
	</script>
	  <div region="north"  style="width:1124px;height:150px;">
		 <table id="datagrid5" >
		 </table>
	   </div>
	 <div region="center"  style="width:1124px;height:330px;">
	  <table id="datagrid2" ></table>
	</div>
      
		 
