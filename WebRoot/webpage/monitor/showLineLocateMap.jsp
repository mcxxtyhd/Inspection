<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/tld.jsp"%>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="/common/easyui.jsp"></jsp:include>
<%-- 本地--%>
<script type="text/javascript" 
  src="http://app.mapabc.com/apis?&t=flashmap&v=2.4&key=f6c97a7f64063cfee7c2dc2157847204d4dbf093d1ba47fa56c99c3afd0ea845709b7d4775140f99"></script>
<%-- 服务器
<script type="text/javascript" 
  src="http://app.mapabc.com/apis?&t=flashmap&v=2.4&key=60bb7313f98ba8fa8ed165347e48ee637fc18509f2cb305008536963f5d760c60332207d710e2d86"></script>
--%>
<title>线路监控</title>
<script type="text/javascript" charset="utf-8">
   var activeImageUrl ='${activeImageUrl}';
   var deactiveImageUrl ='${deactiveImageUrl}';
   var warnigImageUrl ='${warnigImageUrl}';
   var pointid;
   var availableTerms;
   var menuArray = new Array();
   var mapObj=null; 
   var searchForm;
   var datagrid;
  //地图初始化
  function  mapInit(){
    var mapoption = new MMapOptions(); 
    mapoption.toolbar = MConstants.SMALL; //设置地图初始化工具条，ROUND:新版圆工具条 
    mapoption.toolbarPos=new MPoint(20,20); //设置工具条在地图上的显示位置 
    //mapoption.overviewMap = MConstants.SHOW; //设置鹰眼地图的状态，SHOW:显示，HIDE:隐藏（默认） 
   // mapoption.scale = MConstants.SHOW; //设置地图初始化比例尺状态，SHOW:显示（默认），HIDE:隐藏。 
    mapoption.zoom = 15;//要加载的地图的缩放级别 
    mapoption.center = new MLngLat(121.486623, 31.23486);//要加载的地图的中心点经纬度坐标 
    mapoption.language = MConstants.MAP_CN;//设置地图类型，MAP_CN:中文地图（默认），MAP_EN:英文地图 
    mapoption.fullScreenButton = MConstants.SHOW;//设置是否显示全屏按钮，SHOW:显示（默认），HIDE:隐藏 
    mapoption.centerCross = MConstants.SHOW;//设置是否在地图上显示中心十字,SHOW:显示（默认），HIDE:隐藏 
    mapoption.requestNum=100;//设置地图切片请求并发数。默认100。 
    mapoption.isQuickInit=true;//设置是否快速显示地图，true显示，false不显示。
    mapObj = new MMap("map", mapoption); //地图初始化 
    mapObj.addEventListener(mapObj,MConstants.TIP_OPEN,clickmenuitem);
  } 
  
  //单击右键菜单时触发该事件
  function clickmenuitem(param){
	 pointid=param.overlayId; //获取覆盖物标注点的ID
	 //加载巡检点的设备消息
	 datagrid = $('#datagrid').datagrid({
			url:'${pageContext.request.contextPath}/monitor/monitorAction!equipmentLoadDatagrid.action?PointId='+pointid,
			title :'设备消息',                                                                                          
		    iconCls : 'icon-tip',
			rownumbers:true,
			fit : true,
			nowrap : false,
			autoRowHeight : false,
			selectOnCheck : true,
			border : false,
			idField : 'id',
			striped :true,
			singleSelect : true,
			columns : [ [ 
			  {title : '设备编号',field : 'xenumber',width : 70},
			  {title : '设备名称',field : 'xename',width :75}
			] ],
			//双击 事件 
			onClickRow : function(rowIndex, rowData) {
			var p = parent.sy.dialog({
				title : '设备巡检信息',
				iconCls :'icon-search',
				resizable: true,
			    collapsible:true,
			    href:'${pageContext.request.contextPath}/query/inspectmessageAction!inspectmessageList1.action?xequtnum='+rowData.xenumber,
				width : 1020,
				height : 420,
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
  }
  //加载地图
  $(document).ready(function(){ 
	 callShowMap();
  });
  
  $(function(){
	//datagrid 显示
	datagrid = $('#datagrid').datagrid({
		    title :'设备消息', 
			iconCls : 'icon-tip',
			//rownumbers:true,
			fit : true,
			nowrap : false,
			autoRowHeight : false,
			selectOnCheck : true,
			border : false,
			idField : 'id',
			striped :true,
			singleSelect : true,
			columns : [ [ 
			  {title : '设备编号',field : 'xenumber',width : 80},
			  {title : '设备名称',field : 'xename',width :85}
			] ]
		});
	});
	
  
   //获取地图显示点信息
  function callShowMap(){
     mapInit();
     $.ajax({
        type:"POST",
        url:'${pageContext.request.contextPath}/monitor/monitorAction!showLineLocateMap.action?SearchType=0',
        async :true,
        dataType : "json",
		success:function(data){
           disposeSuccess(data);
            if(data.rstLines.length>0){
              var ch = new Array;
			  ch = data.rstLines.split(",");
			  for(i=0;i<ch.length;i++){
			     showLineFuntion(ch[i]);//添加线
			   }
            }
		}
    });
   }
   
   //将后台数据在地图上显示
   function disposeSuccess(data){
	 availableTerms =data.result;
	 if(availableTerms.length>0){
	    for(var i=0;i<availableTerms.length;i++){
			var objjson = availableTerms[i];
			//添加标注点
		    addMarker(objjson);
		 }
	    //添加右键菜单
		//delayAddMenu();
	  }
   }
   
   //添加 标注点
   function addMarker(term){ 
    var markerOptions=new MMarkerOptions(); 
	markerOptions.picAgent=false; 
	markerOptions.canShowTip=true; 
	if(term.rpWarningFlag==0){//正常
	//markerOptions.imageUrl='http://code.mapabc.com/images/lan_1.png';
//	markerOptions.imageUrl='http://code.mapabc.com/images/car_03.png';
	  markerOptions.imageUrl=activeImageUrl;
	}else if(term.rpWarningFlag==1){//告警
		markerOptions.imageUrl=warnigImageUrl;
	}
	else{//处理
	   markerOptions.imageUrl=deactiveImageUrl;
	}
	//获取设置的提示窗口信息内容
	var tip =setPointTipOptions(term); 
	markerOptions.tipOption=tip;
	//添加文本标签
	var labelOption = new MLabelOptions(); 
    labelOption.content="巡检点:"+term.rpPointName; 
    markerOptions.labelOption=labelOption; 
  
	
	var location= new MLngLat(term.rpPosX,term.rpPosY);
	var marker=new MMarker(location, markerOptions); 
	marker.id=term.rpPointId; 
	mapObj.addOverlay(marker,true);//添加标注点
  }
   
   //线路回调函数
 function showLineFuntion(lineid){
   $.ajax({
     type:"POST",
     url:'${pageContext.request.contextPath}/monitor/monitorAction!showDrawLineLocateMap.action?LineId='+lineid,
     async :true,
     dataType : "json",
     success:function(data){
		var termsdare =data.result;
		var term ;
		var lineArr = new Array(); 
		for(var i=1;i<termsdare.length;i++){
		  lineArr.push(new MLngLat((termsdare[i]),(termsdare[++i])));
		 }
		var linest = new MLineStyle(); 
		linest.alpha = 1; 			//设置线的透明度。
		linest.color = 0x0000ff ; //设置线的颜色。0xFF3300  0x0000ff; 0x005890（蓝色）
		linest.thickness = 2; 	 //设置线的宽度
		var lineopt  = new MLineOptions(); 
		lineopt.lineStyle = linest; 
		lineopt.canShowTip = false; 
		var PolylineAPI = new MPolyline(lineArr,lineopt);  //使用自定义的样式时用这个 
		PolylineAPI.id = lineid; 
		mapObj.addOverlay(PolylineAPI)  ; 
		var arr=new Array(); 
		arr.push(termsdare[0]);
		mapObj.setGPSFocus(MConstants.FOCUS,arr); 
	  }
    });
 }

   //标注点提示信息
  function setPointTipOptions(term){
	var tip=new MTipOptions(); 			
	tip.hasShadow=true;//设置tip有阴影 				
	tip.roundRectSize=5;//设置tip圆边长度 				
	var borderStyle =new MLineStyle();//设置tip边框样式 				
	borderStyle.color=0x005890; 				
	borderStyle.thickness=2; 				
	tip.borderStyle=borderStyle; 		
	var titleFontStyle=new MFontStyle();//设置tip标题字体 
	titleFontStyle.color=0XFFFFFF; 
	titleFontStyle.size=12; 
	titleFontStyle.name="Arial"; 
	titleFontStyle.bold=true; 
	tip.titleFontStyle=titleFontStyle; 
	var titleFillStyle=new MFillStyle();//设置tip标题填充色 			
	titleFillStyle.color=0x007890; 				
	tip.titleFillStyle=titleFillStyle; 				
	var contentFontStyle=new MFontStyle();//设置tip内容字体 				
	contentFontStyle.color=0x000000; 				
	contentFontStyle.size=12; 				
	contentFontStyle.name="宋体"; 				
	contentFontStyle.bold=false; 				
	tip.contentFontStyle=contentFontStyle; 
	tip.isDimorphic=true;  // 设置点是否高亮显示。高亮显示与可编辑有冲突，只能设置一个，不能同时设置。
	var contentFillStyle=new MFillStyle();//设置tip内容填充色 
	contentFillStyle.color=0xFFFFFF; 
	tip.fillStyle=contentFillStyle; 
	tip.tipType=MConstants.HTML_BUBBLE_TIP;
	
	tip.title = "当前信息";
    var content= "";
    content+="编号:"+term.rpPointNumber+"<br/>";
    content+="名称:"+term.rpPointName+"<br/>";
	content+="当前坐标:"+term.rpPosX+"，"+term.rpPosY+"<br/>";
	content+="所属线路:"+term.rpLineName+"<br/>";
	//content+="巡检人:"+term.rpUserName+"<br/>";
	/*var params=term.rpEquipments;
	if(params.length>0){
		content+="巡检设备:"+"<br/>"
		for (i = 0 ;i<params.length;i++){
			content+='<a href="javascript:changeInfo('+"'"+params[i]+"'"+')">'+params[i]+'</a>'+"<br/>"; 
		}
	}*/
	tip.content = content;
	return tip;
  }
   
   function changeInfo(eid){
	    datagrid = $('#datagrid').datagrid({
			url:'${pageContext.request.contextPath}/monitor/monitorAction!equipmentDatagrid.action?Equnumber='+eid,
			iconCls : 'icon-tip',
			pagination : true,
			pagePosition : 'bottom',
			//rownumbers:true,
			fit : true,
			nowrap : false,
			autoRowHeight : false,
			selectOnCheck : true,
			border : false,
			idField : 'id',
			striped :true,
			singleSelect : true,
			columns : [ [ 
			  {title : '巡检项',field : 'xproname',width : 80},
			  {title : '状态',field : 'xstatusname',width :40},
			  {title : '巡检时间',field : 'xreptime',width :110}
			] ]
		});
   }
   
    //查询操作
	function _search() {
		var lid=$('#rplineid').val();
		var gid=$('#rpgroupid').val();
		var uid=$('#rpuserid').val();
	    var sdate = $('#searchForm').find('input[name=rpsdate]').val();
	    var edate = $('#searchForm').find('input[name=rpedate]').val();
		mapObj.removeAllOverlays();//删除地图对象上所有覆盖物
        $.ajax({
         type:"POST",
         url:'${pageContext.request.contextPath}/monitor/monitorAction!showLineLocateMap.action?SearchType=1',
         data:"LineId="+lid+"&Sdate="+sdate+"&Edate="+edate+"&RpGroupId="+gid+"&RpUserId="+uid,
         async :true,
         dataType : "json",
		 success:function(data){
            disposeSuccess(data);
            if(data.rstLines.length>0){
              var ch = new Array;
			  ch = data.rstLines.split(",");
			  for(i=0;i<ch.length;i++){
			     showLineFuntion(ch[i]);//重新添加线
			   }
            }
		}
      });
		//datagrid.datagrid('load', {});
	}
    
	//清空操作 
	function cleanSearch() {
		$('#searchForm').form().find('select').val('');
		$('#searchForm').form().find('input').val('');
		//datagrid.datagrid('load', {});
	}
</script>
</head>
<body class="easyui-layout" fit="false">

 <div region="east" split="true" border="false" title="查询条件" iconCls="icon-search" split="false" style="width:200px;height:490px;overflow:hidden;">
  <form id="searchForm">
			<table class="tableForm datagrid-toolbar">
				<tr>
				 <th>巡检线路：</th>
				  <td><select name="rplineid" id="rplineid" style="width:100px;">
				        <option value="">所有</option>
					    <c:forEach items="${LineList}" var="line">
				  		  <option value='<c:out value="${line.id}"/>'><c:out value="${line.lname}"/></option>
				  	    </c:forEach>
				      </select></td>
				  </tr>
				  <tr>
				    <th>巡检代维商：</th>
				    <td><select name="rpgroupid" id="rpgroupid"  style="width:100px;">
				        <option value="">所有</option>
					    <c:forEach items="${GroupList}" var="group">
				  		 <option value='<c:out value="${group.id}"/>'><c:out value="${group.gname}"/></option>
				  	    </c:forEach>
				      </select></td>
				    </tr>
				    <tr>
				    <th>巡检员：</th>
				    <td><select name="rpuserid" id="rpuserid"  style="width:100px;">
				        <option value="">所有</option>
					    <c:forEach items="${InspectUserList}" var="user">
				  		 <option value='<c:out value="${user.id}"/>'><c:out value="${user.iuname}"/></option>
				  	    </c:forEach>
				      </select></td>
				   </tr>
				   <tr>
				  <th>开始时间：</th>
				    <td><input class="Wdate" name="rpsdate" onfocus="WdatePicker({isShowClear:true,readOnly:true})" style="width:100px;height:20px;"/></td>
				  </tr>
				  <tr>
				    <th>结束时间：</th>
				      <td><input class="Wdate" name="rpedate" onfocus="WdatePicker({isShowClear:true,readOnly:true})" style="width:100px;height:20px;"/></td>
				  </tr>
				  <tr>
				   <td colspan="2" align="center">
					<a href="javascript:void(0);" iconCls="icon-search" class="easyui-linkbutton" onclick="_search();">查询</a>&nbsp;
					<a href="javascript:void(0);" iconCls="icon-no" class="easyui-linkbutton" onclick="cleanSearch();">清空</a>
				  </td>
				</tr>
			</table>
		</form>
		<div border="false" split="false" style="width:190px;height:310px;overflow:hidden;">
		<table id="datagrid"></table>
		</div>
</div>

<!-- <div region="east" split="true" border="false" title="巡检信息" split="false" style="width:210px;overflow:hidden;">
  <table id="datagrid"></table>
</div> -->

<div id="map" region="center" border="false" style="overflow: hidden;"></div> 
</body> 
</html>

