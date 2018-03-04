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
<%-- 联通测试服务器 http://123.232.122.71
<script type="text/javascript" 
  src="http://app.mapabc.com/apis?&t=flashmap&v=2.4.1&key=3edc1602c70903c144995d1381f5fbad3d9600834fada6e4bcc35ffa63d8ac3c796443493f46d403"></script>
--%>
<%-- 联通测试服务器 http://123.232.122.73
<script type="text/javascript" 
 src="http://app.mapabc.com/apis?&t=flashmap&v=2.4.1&key=d14814dc215ad779f12f88ac71b56b4a3dd6d09f3533b839aefa712581b6c254e8aed7e2ff4e855c"></script>
--%>

<title>线路监控</title>
<script type="text/javascript" charset="utf-8">

	$(document).ready(function(){
	$("#bcity").change(function(){
	var bcitySelect = $(".bcity").children("select");
	var bregionSelect = $(".bregion").children("select");
		var bcityValue = $(this).val();
		if (bcityValue != "") {
				$.ajax({
				type: "POST",
					url: '${pageContext.request.contextPath}/baseInfo/baseInfoAction!getRegionByCity.action',
					cache: false,
					 data:  { bcity: bcityValue },
					 dataType:"json",
					  success: function(json){
						if (json.length != 0) {
							$("#bregion").empty();
							$("#bregion").append("<option value=''>请选择区县</option>").appendTo(bregionSelect);
						//	alert(json[1].bregion+bregionSelect)
							for (var i = 0; i < json.length; i++) {
						//	$("<option value='" + json[i].bregion + "'>" + json[i].bregion + "</option>").appendTo(bregionSelect);
								$("#bregion").append("<option value=\""+json[i].bregion+"\">"+json[i].bregion+"</option>");
							}
						} 
						else{
						$("<option value=''>请选择区县</option>").appendTo(bregionSelect);
						}
					}
			
				});
		} else {
			//alert("请填写cheng");
			//3.如果值为空，那么第二个下拉框所在span要隐藏起来，第一个下拉框后面的指示图片也要隐藏
			//useridSelect.parent().hide();
			//groupidSelect.next().hide();
		}
	});

})










   var tietaImageUrl ='${tietaImageUrl}';
   var shineiImageUrl ='${shineiImageUrl}';
   
//  alert("tietaImageUrl="+tietaImageUrl+"shinei="+shineiImageUrl);
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
    mapoption.zoom = 20;//要加载的地图的缩放级别 
     mapoption.center = new MLngLat("fjuehjkltvNOKE", "holmmqnloFOGI")
  // mapoption.center = new MLngLat(121.486623, 31.23486);//要加载的地图的中心点经纬度坐标 
    mapoption.language = MConstants.MAP_CN;//设置地图类型，MAP_CN:中文地图（默认），MAP_EN:英文地图 
    mapoption.fullScreenButton = MConstants.SHOW;//设置是否显示全屏按钮，SHOW:显示（默认），HIDE:隐藏 
    mapoption.centerCross = MConstants.SHOW;//设置是否在地图上显示中心十字,SHOW:显示（默认），HIDE:隐藏 
    mapoption.requestNum=100;//设置地图切片请求并发数。默认100。 
    mapoption.isQuickInit=true;//设置是否快速显示地图，true显示，false不显示。
    mapObj = new MMap("map", mapoption); //地图初始化 
   // mapObj.addEventListener(mapObj,MConstants.TIP_OPEN,clickmenuitem);
  } 

  //加载地图
  $(document).ready(function(){ 
	 callShowMap();
  });
  
   //获取地图显示点信息
  function callShowMap(){
     mapInit();
     $.ajax({
        type:"POST",
        url:'${pageContext.request.contextPath}/monitor/monitorAction!showEquipLocateMap.action',
        async :true,
        dataType : "json",
		success:function(data){
	//	console.info(data);
           disposeSuccess(data,0);
		}
    });
   }
   
   //将后台数据在地图上显示
   function disposeSuccess(data,flag){
	 availableTerms =data.result;
	 if(availableTerms.length>0){
	// alert("availableTerms.length="+availableTerms.length);
	    for(var i=0;i<availableTerms.length;i++){
			var objjson = availableTerms[i];
			//添加标注点
		    addMarker(objjson);
		 }
		
	    //添加右键菜单
		//delayAddMenu();
	  }
	   if(availableTerms.length==0&&flag==1){
			alert("此区域没有设备");
		}
   }
   
   //添加 标注设备
   function addMarker(term){ 
    var markerOptions=new MMarkerOptions(); 
	markerOptions.picAgent=false; 
	markerOptions.canShowTip=true; 
	//正常
	//markerOptions.imageUrl='images/icon_grey.gif';
	//markerOptions.imageUrl='http://code.mapabc.com/images/lan_1.png';
	if(term.rptype=="2"){
	
	  markerOptions.imageUrl=shineiImageUrl;
	}
	if (term.rptype=="1"){
	
	  markerOptions.imageUrl=tietaImageUrl;
	}
	//点击设备获取设置的提示窗口信息内容
	var tip =setPointTipOptions(term); 
	markerOptions.tipOption=tip;
	//添加文本标签
	var labelOption = new MLabelOptions(); 
    labelOption.content="设备名称:"+term.rpEquipName; 
    markerOptions.labelOption=labelOption; 
  
	
	var location= new MLngLat(term.rpEquipX,term.rpEquipY);
	var marker=new MMarker(location, markerOptions); 
	marker.id=term.id; 
	mapObj.addOverlay(marker,true);//添加标注点
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
    //alert("编号:"+term.rpEquipNumber+"  名称:"+term.rpEquipName+"   当前坐标:"+term.rpEquipX+"，"+term.rpEquipY+"   地址:"+term.rpEquipAddress);
    content+="编号:"+term.rpEquipNumber+"<br/>";
    content+="名称:"+term.rpEquipName+"<br/>";
	content+="当前坐标:"+term.rpEquipX+"，"+term.rpEquipY+"<br/>";
	content+="地址:"+term.rpEquipAddress+"<br/>";
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
   
  
   
    //查询操作
	function _search() {
		var bcity=$('#bcity').val();
		var bregion=$('#bregion').val();
		var baddress=$('#baddress').val();
		var rpEquipName=$('#rpEquipName').val();
		var btype=$('#btype').val();
		if(bregion==""||bregion==null){
		  alert("请选择区县");
		  return;
		}
		//alert("bcity="+bcity+" bregion="+bregion+" baddress="+baddress);
		mapObj.removeAllOverlays();//删除地图对象上所有覆盖物
        $.ajax({
         type:"POST",
         url:'${pageContext.request.contextPath}/monitor/monitorAction!showEquipLocateMap.action',
         data:"bcity="+bcity+"&bregion="+bregion+"&baddress="+baddress+"&rpEquipName="+rpEquipName+"&btype="+btype,
         async :true,
         dataType : "json",
		 success:function(data){
		// console.info(data);
            disposeSuccess(data,1);
         
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

 <div region="east" split="true" border="false" title="查询条件" iconCls="icon-search" split="false" style="width:220px;height:490px;overflow:hidden;">
  <form id="searchForm">
			<table class="tableForm datagrid-toolbar">
				<tr>
				 <th width="25%">城市：</th>
				  <td><select name="bcity" id="bcity" style="width:100px;">
				        <option value="">所有</option>
					    <c:forEach items="${cityList}" var="city">
				  		  <option value='<c:out value="${city.bcity}"/>'><c:out value="${city.bcity}"/></option>
				  	    </c:forEach>
				      </select></td>
				  </tr>
				  <tr>
				    <th width="25%">区县<font color="red">*</font>：</th>
				    <td><select name="bregion" id="bregion"  style="width:100px;">
				       <!-- <option value="">所有</option>
					     <c:forEach items="${regionList}" var="region">
				  		<option value='<c:out value="${region.bregion}"/>'><c:out value="${region.bregion}"/></option>
				  	    </c:forEach>-->
				      </select></td>
				    </tr>
				   <tr>
				   <th width="25%">设备地址：</th>
			 		  <td width="6%"><input name="baddress"id="baddress"  style="width:130px;height:20px;" /></td>
				  </tr>
				  <tr>
				     <tr>
				   <th width="25%">设备名称：</th>
			 		  <td width="6%"><input name="rpEquipName"id="rpEquipName"  style="width:130px;height:20px;" /></td>
				  </tr>
				  <tr>
				 <th width="25%">设备类型：</th>
				  <td><select name="btype" id="btype" style="width:100px;">
				        <option value="">所有</option>
				  		  <option value='1'><c:out value="铁塔"/></option>
				  		  <option value='2'><c:out value="室内"/></option>
				      </select></td>
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

