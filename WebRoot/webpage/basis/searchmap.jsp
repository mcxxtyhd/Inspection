<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/tld.jsp"%>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="/common/easyui.jsp"></jsp:include>
<script type="text/javascript" 
  src="http://app.mapabc.com/apis?&t=ajaxmap&v=2.1.2&key=f6c97a7f64063cfee7c2dc2157847204d4dbf093773d423e46bbda566d70112d58af8751d9c186cb"></script>
</script>
  </head>
  
  <body>
 <div>地址<input type="text" size="30" value="上海市人民广场" id="address" name="address">
   <input type="button" value="查询" onClick="geocodeSearch()"> 
   <input type="button" value="获得经纬度" onClick="getCenterXY()" /> 
   <br /><!--结果<input type="text" size="50" name="address2" id="address2"/><br />-->
  <span id="result"></span> 
   </div>
   <!-- 在页面中添加一个用于装载地图的容器 -->
   <div id="mapabc" style="width:800px; height:400px;" ></div>
<!-- 使用Javascript语言，创建地图对象 -->
<script type="text/javascript"> 
//创建初始化设置 - 构建地图辅助类
  var mapoption = new MMapOptions();
   mapoption.zoom=16;//设置地图zoom级别
   mapoption.minZoomLevel = 4;//设置地图上可显示的最小缩放级别，默认为3  
   mapoption.maxZoomLevel = 17;//设置地图上可显示的最大缩放级别，默认为17,最大缩放级别小于等于20  
   mapoption.toolbar=MINI; //设置工具条 SMALL:小型  MINI:迷你  DEFAULT:默认 当有minZoomLevel和maxZoomLevel 变成小型
   mapoption.toolbarPos=new MPoint(0,0); //设置工具条位置
   //mapoption.toolbarUrl="http://code.mapabc.com/images/lan_1.png";//工具条URL，可以自定义工具条  
   
   mapoption.overviewMap =HIDE; //设置鹰眼(小地图)  可用 SHOW、HIDE、DEFAULT、ture、false 赋值
   mapoption.scale=0;//是否显示比例尺  
   mapoption.zoomBox = 1;//鼠标滚轮缩放和双击放大时是否有红框动画效果。 
   mapoption.center=new MLngLat(116.46036,39.91507)// ←googlemap |Google地球(116.454171538,39.9135834736)|百度(116.466938,39.920597);   //设置地图中心点  
   mapoption.returnCoordType = COORD_TYPE_OFFSET;//返回数字坐标
  
   var mapObj=new MMap("mapabc",mapoption);//创建地图对象 加载地图初始化


createTip("信息窗口标题","信息窗口内容",1,"113.642578","34.756493","mark101");//创建点
		//添加点击事件
		mapObj.addEventListener(mapObj,MOUSE_CLICK,function(param){
			createTip("","",1,param.eventX,param.eventY,"mark102");
		});


//创建点
	function createTip(title,content,i,x,y,id){
	 mapObj.removeOverlaysByType(MOverlay.TYPE_MARKER);
		var tipOption = new MTipOptions();//添加信息窗口 
		var markerOption = new MMarkerOptions();//构建一个名为markerOption的点选项对象。 
		  markerOption.imageUrl ="http://code.mapabc.com/images/apin/lan_"+(i)+".png";
		tipOption.title = title;//信息窗口标题 
		tipOption.content = "座标：<input type=text size=30 value='"+ x +","+ y +"' />";//信息窗口内容 
		tipOption.borderStyle.color= 0x0000ff; //信息框加框颜色
        tipOption.titleFillStyle.color=0x0000ff; //标题颜色
        tipOption.titleFontStyle.size=12; //标题字体大小
        tipOption.titleFontStyle.color= 0xffffff;//标题字体颜色
        tipOption.titleFontStyle.bold=true; ////标题字体粗体
        tipOption.fillStyle.color= 0x0000ff; //内容区北景色
        tipOption.contentFontStyle.color= 0x00ffff ; //内容区文本 颜色
        tipOption.alpha=0.6; //信息框的透明度1为不透明例0.8
		
		//markerOption.anchor = new MPoint(0, 0);//图片锚点BOTTOM_CENTER相对于标注位置的位置 
		//markerOption.imageAlign = BOTTOM_CENTER;//设置图片相对于加点经纬度坐标的位置。九宫格位置。默认BOTTOM_CENTER代表正下方 
		//markerOption.isEditable = false;//设置点是否为可编辑状态,rue，可以编辑；   false，不可编辑（默认） 
		markerOption.tipOption = tipOption;//设置点的信息窗口参数选项
		markerOption.canShowTip = true;//是否在地图中显示信息窗口，true，可以显示（默认）；false，不显示 
		//markerOption.rotation = "0";//设置图标旋转的角度 
		Mmarker = new MMarker(new MLngLat(x,y), markerOption);//通过经纬度坐标及参数选项确定标注信息 
		Mmarker.id = id;//对象编号，也是对象的唯一标识 
		mapObj.addOverlay(Mmarker, true);//向地图添加覆盖物 

	}


</script>
    
    
    
<!-- 使用Javascript语言，当前地图中心点坐标 -->
<script type="text/javascript"> 
function getCenterXY(){  
   var centXY = mapObj.getCenter();//获得地图中心点  
    var docTmp=parent.document.body;
    console.info(('#poposx').val());
    $(docTmp).find('#poposx').val(centXY.lngX);
    $(docTmp).find('#poposy').val(centXY.latY);
    $(docTmp).find('#mapSearchId').hide(); 
    //window.close();
}  
</script>  
<!-- 使用Javascript语言，当前地图视窗的像素大小 -->
<script type="text/javascript"> 
function getSize(){  
    var size = mapObj.getSize();//获取地图视野范围像素坐标的范围  
}  
</script>

<!-- 使用Javascript语言，地理编码（地址匹配） -->
<script type="text/javascript"> 
function geocodeSearch(){
var addressName = document.getElementById('address').value;
if(addressName== ""){
  alert("请输入地址！");
  document.getElementById('address').focus();
  return;
}else{
  var mls = new MGeoCodeSearch(); //初始化MGeoCodeSearch类的一个新实例
  var opt = new MGeoCodeSearchOptions(); //地址解析输入参数选项
  mls.setCallbackFunction(addressToGeoSearch_CallBack); //回调函数
  mls.addressToGeocode(addressName,opt); //地址解析，由地址得到经纬度坐标相关信息。
}
}

function addressToGeoSearch_CallBack(data){
var resultStr="";
 mapObj.removeOverlaysByType(MOverlay.TYPE_MARKER);
if(data.error_message != null){
  resultStr="查询异常！"+data.error_message;
}else{
switch(data.message){
case 'ok':
  var Mmarker = new Array();
  if(data.count==0){
   resultStr = "未查找到任何结果!<br />建议：<br />1.请确保所有字词拼写正确。<br />2.尝试不同的关键字。<br />3.尝试更宽泛的关键字。";
  }else{
   for (var i = 0; i < data.list.length; i++) {
    resultStr += "<span class=\"spoi\"><a href=\"javascript:var s=mapObj.setCenter(new MLngLat('"+ data.list[i].x +"','"+ data.list[i].y +"'));var t = mapObj.openOverlayTip('"+(i)+"');\">"+data.list[i].name+"</a></span>";
    var markerOption = new MMarkerOptions();
    markerOption.imageUrl ="http://code.mapabc.com/images/apin/lan_"+(i+1)+".png";
    //document.getElementById("address2").value =data.list[i].name;

    var ll = new MLngLat(data.list[i].x,data.list[i].y);
    
    var tipOption = new MTipOptions();
    tipOption.title=data.list[i].name; //标题 
    tipOption.content="座标：<input type=text size=30 value='"+ ll.latY +","+ ll.lngX +"' />"; //内容
   // document.getElementById("lngXY").value ="字符串坐标："+data.list[i].x+","+data.list[i].y ;

    tipOption.borderStyle.color= 0x0000ff; //信息框加框颜色
    tipOption.titleFillStyle.color=0x0000ff; //标题颜色
    tipOption.titleFontStyle.size=12; //标题字体大小
    tipOption.titleFontStyle.color= 0xffffff;//标题字体颜色
    tipOption.titleFontStyle.bold=true; ////标题字体粗体
    tipOption.fillStyle.color= 0x0000ff; //内容区北景色
    tipOption.contentFontStyle.color= 0x00ffff ; //内容区文本 颜色
    tipOption.alpha=0.6; //信息框的透明度1为不透明例0.8
    markerOption.tipOption = tipOption;
    markerOption.canShowTip=true; //是否显示tip
    
    Mmarker[i] = new MMarker(ll,markerOption);
    Mmarker[i].id=(i);
   }
   mapObj.addOverlays(Mmarker,true);
   mapObj.openOverlayTip("0");
  }
break;
case 'error':
  content='<div class=\"default\"><div class=\"default_title\">网络忙！请重新尝试！</div><div class=\"d_link\"><div class=\"d_right\"></div><div class=\"suggest\"><strong>建议：</strong><br />如果您刷新页后仍无法显示结果，请过几分钟后再次尝试或者与我们的服务人员联系。<br />Email：service@mapabc.com <br />电话：400 810 0080</div></div></span>错误信息："+data.message+"</div>';
break;
default:
  content='<div class=\"default\"><div class=\"default_title\">对不起！网络繁忙！请稍后重新尝试！</div><div class=\"d_link\"><div class=\"d_right\"></div><div class=\"suggest\"><strong>建议：</strong><br />如果您刷新页后仍无法显示结果，请过几分钟后再次尝试或者与我们的服务人员联系。<br />Email：service@mapabc.com <br />电话：400 810 0080</div></div></span>错误信息："+data.message+"</div>';
}
document.getElementById("result").innerHTML = resultStr;
}
}
</script>
  </body>
</html>
