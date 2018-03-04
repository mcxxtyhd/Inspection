<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
response.setHeader("Pragma","No-cache");       
response.setHeader("Cache-Control","no-cache");     		    
response.setDateHeader("Expires",0);
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<base href="<%=basePath%>">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/print.css" title="pink">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/printapplay.css"  title="pink">
<script type="text/javascript">
/*** 打印页面设置  ***/
function printsetup(){ 
	// 打印页面设置 
	factory.printing.PageSetup();
} 
/*** 打印页面预览  ***/
function printpreview(){ 
	// 打印页面预览 
	document.getElementById("ptable").style.display="none";
	factory.printing.Preview()
	document.getElementById("ptable").style.display="";
} 
/*** 打印 ***/
function printit(){ 	
	if (confirm('确定打印吗？')){ 
		document.getElementById("ptable").style.display="none";
		factory.printing.Print(true); 
		document.getElementById("ptable").style.display="";
	} 
}
function setPrintBase() {   
	//默认横打
	factory.printing.portrait = false;   
	//设置打印页面边距
	factory.printing.header = "";    //页眉
	factory.printing.footer = "";     //页脚 
	factory.printing.leftMargin =10.00;   
	factory.printing.topMargin =10.00;   
	factory.printing.rightMargin =10.00;   
	factory.printing.bottomMargin =10.00;   
}  
 
</script>   
</head>
<body>
<table id="ptable" width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td>
    <OBJECT id="factory" style="display:none"  classid="clsid:1663ed61-23eb-11d2-b92f-008048fdd814"></OBJECT>   
	<input type="button" name="button_print" class="ptbutton" value="打印" onClick="printit()"> 

	<input type="button" name="button_show" class="ptbutton" value="打印预览" onClick="printpreview()"> 
    </td>
  </tr>
  <tr>
    <td>&nbsp;</td>
  </tr>
</table>
<tr><td><!--<img  style="width:180px; height:150px" src="${pageContext.request.contextPath}/images/imgtu2.jpg" /></td></tr>
--><center>
<logic:present name="tElecFormList">
<logic:iterate id="tElecForm" name="tElecFormList">
<table id="table" width="1050px" height="720px" border="0" cellspacing="0" cellpadding="0" class="table" style="border:1px solid #ffffff;">
 <tr>
  <td align="center" valign="middle">
  <table id="table" width="100%" height="600px" border="0" cellspacing="0" cellpadding="0" class="table">
  <tr>
    <td width="72%" height="100%" valign="bottom" style="padding:0px 0px;border:1px solid #ffffff;border-bottom:1px solid #000000;">
	<table id="toptable" width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td align="center" class="title1" height="45px" style="padding:5px 0px;">基站电费发票登记表</td>
      </tr>
    </table>
	<table id="centertable" width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td colspan="6" align="center" class="title2" height="30px" style="padding:5px 0px;">业主信息</td>
        </tr>
      <tr>
        <td colspan="2" class="title3" height="25px">场点名称（楼宇名称）</td>
        <td colspan="4">${tElecForm.locationName }</td>
        </tr>
      <tr>
        <td class="title3" width="100px" height="25px">场点地址</td>
        <td colspan="3">${tElecForm.address }</td>
        <td class="title3" width="120px">本次付费总金额</td>
        <td>${tElecForm.paidTotal }</td>
      </tr>
      <tr>
        <td class="title3" width="100px" height="25px">场点编号</td>
        <td width="140px">${tElecForm.locationCode }</td>
        <td class="title3" width="100px">站号</td>
        <td width="120px">${tElecForm.stationCode }</td>
        <td rowspan="2" valign="top" width="120px" class="title3">业务用途</td>
        <td rowspan="2" valign="top" width="180px">
		<logic:present name="tElecForm">
		 <logic:equal name="tElecForm" property="cdpd" value="1">CDPD</logic:equal> 
		 <logic:equal name="tElecForm" property="cdma" value="1">CDMA(大灵通)</logic:equal> 
		 <logic:equal name="tElecForm" property="phs" value="1">PHS</logic:equal> 
		 <logic:equal name="tElecForm" property="wlan" value="1">WLAN</logic:equal> 
		 <logic:equal name="tElecForm" property="tetra" value="1">TETRA</logic:equal> 
		 <logic:equal name="tElecForm" property="cdma2000" value="1">CDMA2000</logic:equal>
		</logic:present>			
		</td>
       </tr>
       <tr>
         <td class="title3" width="100px" height="25px">联系人</td>
         <td>${tElecForm.contactor }</td>
        <td class="title3" width="100px">联系电话</td>
        <td>${tElecForm.contactPhone }</td>
        </tr>
      <tr>
        <td class="title3" width="100px" height="25px">合同电费单价</td>
        <td>${tElecForm.dayPrice }</td>
        <td class="title3" width="100px">现行电费单价</td>
        <td>${tElecForm.curDayPrice } </td>
        <td class="title3" width="120px">是否第三方收款</td>
        <td>&nbsp;</td>
      </tr>
    </table>
    <table id="foottable" width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td colspan="9" align="center" class="title2" height="30px" style="padding:5px 0px;">抄表数据</td>
      </tr>
      <tr>
        <td width="100px" height="25px">&nbsp;</td>
        <td width="160px" class="title3">电表号</td>
        <td width="80px" class="title3">网络</td>
        <td width="100px" class="title3">电表类别</td>
        <td width="80px" class="title3">倍率</td>
        <td width="100px" class="title3">上次抄表数</td>
        <td width="100px" class="title3">本次抄表数</td>
        <td width="120px" class="title3">上次抄表时间</td>
        <td width="120px" class="title3">本次抄表时间</td>
      </tr>
	  <logic:present name="tElecForm" property="temForms">
	  <logic:iterate id="temForm" name="tElecForm" property="temForms" indexId="index">
	  <logic:present name="temForm">
	  <tr>
		<td width="100px" height="20px">电表${index+1}</td>
		<td width="160px">${temForm.meterCode }</td>
		<td width="80px">${temForm.network }</td>
		<td width="100px">${temForm.elecMode }</td>
		<td width="80px">${temForm.multiple }</td>
		<td width="100px">${temForm.lastNum }</td>
		<td width="100px">${temForm.thisNum }</td>
		<td width="120px">${temForm.lastTime }</td>
		<td width="120px">${temForm.thisTime }</td>
	  </tr>
	  </logic:present>
	  </logic:iterate>
	  </logic:present>
      <tr>
        <td rowspan="2" class="title3" style="border-bottom:1px solid #ffffff;">备注</td>
        <td colspan="6" rowspan="2" style="border-bottom:1px solid #ffffff;">${tElecForm.remarks }</td>
        <td colspan="2" class="title3">经办人  ： ${tElecForm.registrationUser }</td>
      </tr>
      <tr>
        <td colspan="2" class="title3" style="border-bottom:1px solid #ffffff;">日&nbsp;&nbsp;&nbsp;期  ：</td>
      </tr>
    </table>
    </td>
    <td width="28%" height="100%" align="left" valign="top">
      <span id="span">检查项目:</span>
      <ul id="ul">
      	<li>确认本表“业主信息”填写无误</li>
      	<li>确认电表抄见日、抄见数、电表号填写无误</li>
      	<li>确认发票抬头为“中国电信股份有限公司上海分公司”</li>
      	<li>确认发票有托收帐号</li>
      	<li>确认先行电费单价与合同单价不符的，要有《电费补充协议》</li>
      	<li>确认由业主委托第三方付费的，要有《第三方代收费用委托书》</li>
      	<li>确认经办人签名、填表日期无误</li>
      	<li>1-5项确认无误后，部门核查人在发背面签字</li>
      </ul>
      <span id="span">说明：</span>
	  <ul id="ul">
		<li>“业主信息”、“抄表数据”由直接负责基站维护的管理人员（即经办人）负责填写；</li>
		<li>电表的“表号”为每一个电表的唯一识别号，见于每一个电表表面；</li>
		<li>部门核查人对“检查项目”进行逐一确认并打钩后，应在发票背面签字确认；</li>
		<li>本表完成填写、核对后，连同发票一起上交，并统一付费；</li>
		<li>维护部门应复印本表保存备案，并在“基站电费缴费系统”使用前，做好电子记录（记录“场点名称”、“场点标号”、“本次抄见日期”和“发票金额”4项）；</li>	
	  </ul>
    </td>
  </tr>
  </table>
  </td>
 </tr>
</table>  
</logic:iterate>
</logic:present>  
</center>
</body>
</html>
