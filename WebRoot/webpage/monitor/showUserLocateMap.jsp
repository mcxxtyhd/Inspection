<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/tld.jsp"%>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="/common/easyui.jsp"></jsp:include>
<%-- 本地--%>
<script type="text/javascript" 
  src="http://app.mapabc.com/apis?&t=flashmap&v=2.4.1&key=f6c97a7f64063cfee7c2dc2157847204d4dbf093d1ba47fa56c99c3afd0ea845709b7d4775140f99"></script>

<%-- 公司服务器
<script type="text/javascript" 
  src="http://app.mapabc.com/apis?&t=flashmap&v=2.4.1&key=60bb7313f98ba8fa8ed165347e48ee637fc18509f2cb305008536963f5d760c60332207d710e2d86"></script>
--%>

<%-- 联通测试服务器 http://123.232.122.73
<script type="text/javascript" 
 src="http://app.mapabc.com/apis?&t=flashmap&v=2.4.1&key=d14814dc215ad779f12f88ac71b56b4a3dd6d09f3533b839aefa712581b6c254e8aed7e2ff4e855c"></script>
--%>
<title>人员监控</title>
<script type="text/javascript" charset="utf-8">
    var temid;
   var availableTerms;
   var availableTermsUsers;
   var menuArray = new Array();
   var mapObj=null; 
   var activeImageUrl ='${activeImageUrl}';
  //地图初始化
  function  mapInit(){
    var mapoption = new MMapOptions(); 
    mapoption.toolbar = MConstants.SMALL; //设置地图初始化工具条，ROUND:新版圆工具条  
    mapoption.toolbarPos=new MPoint(20,20); //设置工具条在地图上的显示位置 
    //mapoption.overviewMap = MConstants.SHOW; //设置鹰眼地图的状态，SHOW:显示，HIDE:隐藏（默认） 
    //mapoption.scale = MConstants.SHOW; //设置地图初始化比例尺状态，SHOW:显示（默认），HIDE:隐藏。 
    mapoption.zoom = 12;//要加载的地图的缩放级别  
    //mapoption.center = new MLngLat("fkoelopplrJOGE", "hjlijlnlnNOOI");//上海
    mapoption.center = new MLngLat("fjuehjkltvNOKE", "holmmqnloFOGI");//上海
   // mapoption.center = new MLngLat(121.486623, 31.23486);//要加载的地图的中心点经纬度坐标 
    mapoption.language = MConstants.MAP_CN;//设置地图类型，MAP_CN:中文地图（默认），MAP_EN:英文地图 
    mapoption.fullScreenButton = MConstants.SHOW;//设置是否显示全屏按钮，SHOW:显示（默认），HIDE:隐藏 
    mapoption.centerCross = MConstants.SHOW;//设置是否在地图上显示中心十字,SHOW:显示（默认），HIDE:隐藏 
    mapoption.requestNum=100;//设置地图切片请求并发数。默认100。 
    mapoption.isQuickInit=true;//设置是否快速显示地图，true显示，false不显示。
  //  mapoption.viewBounds=new MLngLatBounds(new MLngLat(47.34,20.23),new MLngLat(140.34,69.78));      //设置地图可显示的经纬度范围。
    mapoption.minZoomLevel=5;   //设置地图的最小缩放级别。
	mapoption.maxZoomLevel=20;  //设置地图的最大缩放级别。
    mapObj = new MMap("map", mapoption); //地图初始化 
    mapObj.addEventListener(mapObj,MConstants.MENUITEM_CLICK,clickmenuitem);
    mapObj.setWheelZoomEnabled (false); //设置禁止滚轮缩放地图 
    mapObj.setDBClickEnabled(false); //设置禁止双击放大地图 
  } 
  
  //单击右键菜单时触发该事件
  function clickmenuitem(param){
	 temid=param.overlayId; //获取覆盖物标注点的ID
  }
  //加载地图
  $(document).ready(function(){   
	 callShowMap();
	 
	 $("#rpgroupid").change(function(){
		var groupValue = $(this).val();
		if (groupValue != "") {
				$.ajax({
				type: "POST",
					url: '${pageContext.request.contextPath}/problem/ProblemAction!getUserBygroup.action',
					cache: false,
					 data:  { groupid: groupValue },
					 dataType:"json",
					  success: function(data){
						if (data.length != 0) {
							$("#rpuserid").empty();
							$("#rpuserid").append("<option value=''>请选择</option>");
							for (var i = 0; i < data.length; i++) {
								$("#rpuserid").append("<option value=\""+data[i].id+"\" selected>"+data[i].irealname+"</option>");
							}
						} 
						else{
						$("#rpuserid").empty();
							$("#rpuserid").append("<option value=''>请选择</option>");
						}
					}
				});
		} else {
			//3.如果值为空，那么第二个下拉框所在span要隐藏起来，第一个下拉框后面的指示图片也要隐藏
			useridSelect.parent().hide();
			groupidSelect.next().hide();
		}
	});
	
			
		$("#entid").change(function(){
			var entid=$("#entid").val();
			var pgidSelect = $(".rpgroupid").children("select");
			var plineidSelect = $(".rpuserid").children("select");
				$.ajax({
					url:'${pageContext.request.contextPath}/query/planAction!cascodeToLineAndGroup.action',
					data:  { entid: entid },
					dataType:"json",
					success:(function(json){
						if(json.flag==0){
							$("#rpgroupid").empty();
								$("#rpgroupid").append("<option value=''>请选择</option>").appendTo(pgidSelect);
								for (var i = 0; i < json.glist.length; i++) {
									$("#rpgroupid").append("<option value=\""+json.glist[i].id+"\">"+json.glist[i].gname+"</option>");
								}
								
								$("#rpuserid").empty();
								$("#rpuserid").append("<option value=''>请选择</option>").appendTo(plineidSelect);
						}
					})
				})
		})
  });
  
   //获取地图显示信息
  function callShowMap(){
     mapInit();
     $.ajax({
        type:"POST",
        url:'${pageContext.request.contextPath}/monitor/monitorAction!showUserLocateMap.action?QType=0',
        async :true,
        dataType : "json",
		success:function(data){
           disposeSuccess(data);
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
	//	delayAddMenu();
	  }
   }
   
   //添加 标注点
   function addMarker(term){ 
    var markerOptions=new MMarkerOptions(); 
	markerOptions.picAgent=false; 
	markerOptions.canShowTip=true; 
	markerOptions.imageUrl=activeImageUrl;
	markerOptions.imageUrl='http://code.mapabc.com/images/lan_1.png';
	//markerOptions.imageUrl='http://code.mapabc.com/images/car_03.png';
	//获取设置的提示窗口信息内容
	var tip =setLocateTipOptions(term); 
	markerOptions.tipOption=tip;
	//添加文本标签
	var labelOption = new MLabelOptions(); 
    labelOption.content="巡检员:"+term.rpUserName; 
    markerOptions.labelOption=labelOption; 
    var location;
    if(term.rpPosX>0){
    	var location= new MLngLat(term.rpPosX,term.rpPosY);
    }else{
    	location= new MLngLat("fjuehjkltvNOKE", "holmmqnloFOGI");
    	//location= new MLngLat("fkoelopplrJOGE", "hjlijlnlnNOOI");
    }
	var marker=new MMarker(location, markerOptions); 
	marker.id=term.rpTerminalNumber;//rpUserNumber rpTerminalNumber rpUserId
	mapObj.addOverlay(marker,true);//添加标注点
  }
   
    //菜单调用的方法
  function delayAddMenu(){
     if(availableTerms.length>0){
    	for(var i=0;i<availableTerms.length;i++){
		var term = availableTerms[i];
		  setTermMenu(term);
		}
     }
  	} 
    
    //添加菜单的逻辑
  function setTermMenu(term){
	   //添加人员轨迹回放菜单
	   addShowMoveMenu(term.rpTerminalNumber);
	}
    
    //添加轨迹回放菜单
  function addShowMoveMenu(markerId){
	var marker = mapObj.getOverlayById(markerId);
	for(var i=0;i<menuArray.length;i++){
		if(menuArray[i].id==("showMoveMenu:"+markerId)){
		  menuArray.splice(i,1);
		  break;
		}
	}
	var menuItem=new MMenuItem();    //定义一个菜单项 
	menuItem.menuType="singleMenu";        //定义菜单项类型为类型菜单 
	menuItem.overlayId= markerId;   //为类型菜单项绑定一类覆盖物 
	menuItem.order=2;        //设置菜单项的排列顺序 
	menuItem.menuText="开始轨迹回放";   //设置菜单项显示文字 
	menuItem.functionName= showUserMoveMenuFuntion;    //设置菜单项回调函数 showUserMoveMenuFuntion;
	menuItem.id="showMoveMenu:"+markerId;     //设置菜单项ID 
	menuItem.isEnabled=true;    //设置菜单项为可用状态 
	menuItem.isHaveSeparator = false;   //设置菜单项下方有分割条 
	var menuItem2=new MMenuItem(); 
	menuItem2.menuType="singleMenu"; 
    menuItem2.overlayId= markerId;   //为类型菜单项绑定一类覆盖物 
    menuItem2.order=3;        //设置菜单项的排列顺序 
    menuItem2.menuText="暂停轨迹回放"; 
    menuItem2.functionName=stopMoveMenuFuntion; 
    menuItem2.isEnabled=true; 
    menuItem2.isHaveSeparator=false; 
    menuItem2.id="showMoveMenu2"+markerId; 
    var menuItem3=new MMenuItem(); 
	menuItem3.menuType="singleMenu"; 
    menuItem3.overlayId= markerId;   //为类型菜单项绑定一类覆盖物 
    menuItem3.order=4;        //设置菜单项的排列顺序 
    menuItem3.menuText="删除轨迹"; 
    menuItem3.functionName=removeMoveMenuFuntion; 
    menuItem3.isEnabled=true; 
    menuItem3.isHaveSeparator=false; 
    menuItem3.id="showMoveMenu3"+markerId; 
	menuArray.push(menuItem);
	menuArray.push(menuItem2);
	menuArray.push(menuItem3);
	var array= menuArray.concat(); 
	mapObj.addMenuItems(menuArray);  //为一类覆盖物添加类型菜单 
 }
    function removeMoveMenuFuntion(){
    	mapObj.removeOverlaysByType(MOverlay.TYPE_POLYLINE); //先删除所有的标注。
    }
    
   //轨迹回放的回调函数
 function showUserMoveMenuFuntion(){
   $.ajax({
     type:"POST",
     url:'${pageContext.request.contextPath}/monitor/monitorAction!showTermMoveMap.action?RpTerminateId='+temid,
     async :true,
     dataType : "json",
     success:function(data){
		var termsdare =data.result;
		var marker = mapObj.getOverlayById(termsdare[0]);
		var term ;
		for(var i=0;i<availableTerms.length;i++){
		   var temp = availableTerms[i];
		   if(temp.rpTerminalNumber == marker.id){
				term = temp;
				break;
			 }
		 }
		var lineArr = new Array(); 
		if(temp!=null){
		  lineArr.push(new MLngLat(term.rpPosX, term.rpPosY));
		 }
		for(var i=1;i<termsdare.length;i++){
		  lineArr.push(new MLngLat((termsdare[i]),(termsdare[++i])));
		 }
		var linest = new MLineStyle(); 
		linest.alpha = 1; 			//设置线的透明度。
		linest.color = 0xFF3300 ; //设置线的颜色。
		linest.thickness = 3; 	 //设置线的宽度。;
		var lineopt  = new MLineOptions(); 
		lineopt.lineStyle = linest; 
		lineopt.canShowTip = false; 
		var PolylineAPI = new MPolyline(lineArr,lineopt);  //使用自定义的样式时用这个 
		PolylineAPI.id = "polyline101"; 
		mapObj.addOverlay(PolylineAPI)  ; 
		var arr=new Array(); 
		arr.push(termsdare[0]); 
		mapObj.setGPSFocus(MConstants.FOCUS,arr); 
		mapObj.markerMoveAlong(termsdare[0],lineArr,4); //标注点移动路线
		mapObj.startMoveAlong(termsdare[0],false); //开始移动 false:不循环移动 true:循环移动
	  }
    });
 }
   
   //暂停人员轨迹回放
   function stopMoveMenuFuntion(){
	 mapObj.pauseMoveAlong(temid);//暂停移动marker 
	}
   
   //标注点提示信息
  function setLocateTipOptions(term){
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
    content+="巡检员："+term.rpUserName+"<br/>";
    content+="巡检终端："+term.rpTerminalNumber+"<br/>";
    content+="终端号码："+term.rpUserMobile+"<br/>";
	content+="当前坐标："+term.rpPosX+"，"+term.rpPosY+"<br/>";
	content+="巡检任务："+term.rpLineName+"<br/>";
	content+="维护队："+term.rpGroupName+"<br/>";
	content+="上报时间："+term.rpRepoetTime+"<br/>";
	tip.content = content;
	return tip;
  }
   
    //查询操作
	function _search() {
		var uid=$('#rpuserid').val();
		var gid=$('#rpgroupid').val();
		var entid=$("#entid").val();
		var sdate = $('#searchForm').find('input[name=rpsdate]').val();
	   // var edate = $('#searchForm').find('input[name=rpedate]').val();
		mapObj.removeOverlaysByType(MOverlay.TYPE_MARKER); //先删除所有的标注。
		removeMoveMenuFuntion();
        $.ajax({
         type:"POST",
         url:'${pageContext.request.contextPath}/monitor/monitorAction!showUserLocateMap.action?QType=1',
         data:"RpGroupId="+ gid +"&RpUserId="+uid+"&Sdate="+sdate+"&entid="+entid,
         async :true,
         dataType : "json",
		 success:function(data){
        	// disposeSuccess(data);
        	//是否查看人员轨迹
            var str=document.getElementsByName("checkbox");
            if(str[0].checked == true){
          //  alert("guiji");
            	//线选择某个巡检员
              if(uid!=""){
              //添加上报位置信息点
             // console.info("data.rstUsers.length============"+data.rstUsers.length);
			 //   if(data.rstUsers.length>0){
				  disposeSuccessUsers(data);//标注每个上报坐标的信息,信息如下
				  showLineFuntionUser(uid,gid,sdate);//显示轨迹并开始移动
			    }
            //  }else{
            //	  parent.sy.messagerAlert('提示', '查看轨迹请选择巡检员信息！', 'error');
            //  }
            }else{
         //   alert("flase");
            	disposeSuccess(data);
            }
		 }
      });
	}
    
     //将后台数据在地图上显示
   function disposeSuccessUsers(data){
	 availableTermsUsers =data.rstUsers;
	 if(availableTermsUsers!=null){
		 if(availableTermsUsers.length>0){
		    for(var i=0;i<availableTermsUsers.length;i++){
				var obsonuser = availableTermsUsers[i];
				//添加标注点
			    addMarkerUser(obsonuser);
			 }
		  }
	  }
   }
     
       //添加 标注点
   function addMarkerUser(term){ 
    var markerOptions=new MMarkerOptions(); 
	markerOptions.picAgent=false; 
	markerOptions.canShowTip=true; 
//	markerOptions.imageUrl='http://code.mapabc.com/images/car_03.png';
	//获取设置的提示窗口信息内容
	var tip =setLocateTipOptions(term); 
	markerOptions.tipOption=tip;
	//添加文本标签
	var labelOption = new MLabelOptions(); 
    labelOption.content="巡检员:"+term.rpUserName; 
    markerOptions.labelOption=labelOption; 
  
	var location= new MLngLat(term.rpPosX,term.rpPosY);
	var marker=new MMarker(location, markerOptions); 
//	alert("term.rpUserId1   ="+term.rpUserId1)
	marker.id=term.rpUserId; 
	mapObj.addOverlay(marker,true);//添加标注点
    //使图标在第一段路线移动 
    //mapObj.markerMoveAlong(term.rpUserId,lineArr); 
    //mapObj.startMoveAlong(term.rpUserId,true); 
  }
  //user线路回调函数
 function showLineFuntionUser(uid,gid,sdate){
   $.ajax({
     type:"POST",
     url:'${pageContext.request.contextPath}/monitor/monitorAction!showDrawLineUserLocateMap.action',
     data:"RpGroupId="+ gid +"&RpUserId="+uid+"&Sdate="+sdate,
     async :true,
     dataType : "json",
     success:function(data){
		var termsdare =data.result;
		var marker = mapObj.getOverlayById(termsdare[0]);//marker为空
		var allover = new Array(); 
		var term ;
	/*	for(var i=0;i<availableTermsUsers.length;i++){
		   var temp = availableTermsUsers[i];
		 //  alert("temp.rpUserId="+temp.rpUserId)
	//	alert("temp.rpUserId="+temp.rpUserId);
	//	alert("marker.id="+marker);
		   if(temp.rpUserId == marker.id){
				term = temp;
				break;
			 }
		 }*/
		var lineArr = new Array(); 
		if(term!=null){
		  lineArr.push(new MLngLat(term.rpPosX, term.rpPosY));
		 }
		 else{
		   lineArr.push(new MLngLat(termsdare[1], termsdare[2]));
		 }
		for(var i=3;i<termsdare.length;i++){
	
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
		PolylineAPI.id = termsdare[0]; 
		allover.push(PolylineAPI);
		mapObj.addOverlays(allover,true)  ; 
	  /*添加小车*/ 
         var lnglat = new MLngLat(termsdare[1],termsdare[2]); 
        var busmarkerOption = new MMarkerOptions(); 
        busmarkerOption.imageUrl ="http://code.mapabc.com/images/car_03.png"; 
        busmarkerOption.picAgent = false; 
        busmarkerOption.imageAlign=5; 
        var busmarker = new MMarker(lnglat,busmarkerOption); 
        busmarker.id=termsdare[0]+1; 
        mapObj.addOverlay(busmarker,true);	
		 mapObj.setGPSFocus(MConstants.FOCUS,lnglat);//设置以车辆为中心，arr为车辆位置坐标
		mapObj.markerMoveAlong(termsdare[0]+1,lineArr,1); //标注点移动路线
		mapObj.startMoveAlong(termsdare[0]+1,false); //开始移动 false:不循环移动 true:循环移动
	  }
    });
 }
  
	//清空查询条件操作 
	function cleanSearch() {
		$('#searchForm').form().find('select').val('');
		$('#searchForm').form().find('input').val('');
		$("[name='checkbox']").removeAttr("checked");
		//removeMoveMenuFuntion();
	}
	//清空回放轨迹操作
	function cleanGj(){
		 if($("#clanceGJ").is(":checked")){
			removeMoveMenuFuntion();
		 }
	}
</script>
</head>
<body class="easyui-layout" fit="false">
  <div region="east" split="true" border="false" title="查询条件" iconCls="icon-search" split="false" style="width:190px;height:490px;overflow:hidden;">
  <form id="searchForm">
    <!--  <input type="hidden"  id="queryGJ" />-->
			<table class="tableForm datagrid-toolbar">
					<tr>
					 <th width="8%">所属单位：</th>
						 <td width="8%">
							     <select name="entid" id="entid"  style="width:130px;height:20px;">
							        <option value="0">请选择</option>
							        <c:forEach items="${EnterpriseList}" var="ent">
							  		 <option value='<c:out value="${ent.id}"/>'><c:out value="${ent.entname}"/></option>
							  	    </c:forEach>
							      </select>
						</td>
					</tr>
				   <tr>
				    <th width="8%">维护队：</th>
				    <td width="8%"><select name="rpgroupid" id="rpgroupid"  style="width:130px;height:20px;">
				        <option value="">请选择</option>
					  <!--   <c:forEach items="${GroupList}" var="group">
				  		 <option value='<c:out value="${group.id}"/>'><c:out value="${group.gname}"/></option>
				  	    </c:forEach>
				      </select>
				       -->
				      </td>
				   </tr>
				  <tr>
				    <th>巡检员：</th>
				    <td><select name="rpuserid" id="rpuserid"  style="width:130px;height:20px;">
				        <option value="">请选择</option>
					<!--     <c:forEach items="${InspectUserList}" var="user">
				  		 <option value='<c:out value="${user.id}"/>'><c:out value="${user.irealname}"/></option>
				  	    </c:forEach>
				      </select>
				       -->
				      </td>
				   </tr>
				   <tr>
				    <th>查询时间：</th>
				    <td><input class="Wdate" name="rpsdate" onfocus="WdatePicker({isShowClear:true,readOnly:true})" style="width:100px;height:20px;"/></td>
				    </tr>
				    <tr>
				    
				   <tr>
				    <td colspan="2" align="right" height="20">&nbsp;&nbsp;&nbsp;&nbsp;
				       <input type="checkbox" name="checkbox" id="queryGJ"  style="width:10px;overflow: hidden;" />查看轨迹
				    </td>
				  </tr>
				   
				    <tr height="10px"></tr>
				   <tr>
				   <td colspan="2" align="center">
					<a href="javascript:void(0);" iconCls="icon-search" class="easyui-linkbutton" onclick="_search();">查询</a>&nbsp;
					<a href="javascript:void(0);" iconCls="icon-no" class="easyui-linkbutton" onclick="cleanSearch();">清空</a>
				  </td>
				</tr>
			</table>
			<table class="tableForm datagrid-toolbar">
		  <tr>
		   <th>终端总数：</th>
		   <td width="80">${TerminalAllCount}</td>
		  </tr>
		  <tr>
		  <th>巡检员总数：</th>
		  <td>${UserAllCount}</td>
		  </tr>
		    <tr>
		    <th>今日在线终端：</th>
		  <td>${TerminalOnlineCount}</td>
		  </tr>
		  <tr>
		    <th>今日在线人数：</th>
		  <td>${UserOnlineCount}</td>
		  </tr>
		 </table>
		</form>
</div>
<div id="map" region="center" border="false" style="width:590px;overflow: hidden;"></div> 
</body> 
</html>

