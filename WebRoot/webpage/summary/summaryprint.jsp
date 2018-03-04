<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ include file="/common/tld.jsp"%>
<%@ page import="com.inspect.vo.summary.PathVo" %>
<%
List<PathVo> piclist=(List)request.getAttribute("piclist");
 %>
<html>
<head>
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
	factory.printing.Preview();
	document.getElementById("ptable").style.display="";
} 
/*** 打印 ***/
function printit(){ 	
	if (confirm('确定打印吗？')){
	factory.printing.header = "&b&b" ;    //页眉
    factory.printing.footer = "&b";    //页脚
    factory.printing.portrait = false;  //横竖排版
    factory.printing.leftMargin =10.00;   
	factory.printing.topMargin =10.00;   
	factory.printing.rightMargin =10.00;   
	factory.printing.bottomMargin =10.00; 
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
  <tr height="20px">
    <td>
    <object id="secmgr" viewastext style="display:none"
			classid="clsid:5445be81-b796-11d2-b931-002018654e2e"
			codebase="jslib/smsx.cab#Version=7,4,0,8">
			<param name="GUID" value="{[YOUR_LICENSE_GUID]}">
			<param name="Path" value="http://[your path]/sxlic.mlf">
			<param name="Revision" value="[your license rev]">
	</object>
    <%--
    <object id="factory" viewastext style="display:none"
   classid="clsid:1663ed61-23eb-11d2-b92f-008048fdd814"
   codebase="jslib/smsx.cab#Version=7,4,0,8">
</object>
    <OBJECT id="factory" style="display:none" codeBase="jslib/smsx.cab#VVersion=6,5,439,12" classid="clsid:1663ed61-23eb-11d2-b92f-008048fdd814"></OBJECT>   
<input type="button" name="button_print" class="ptbutton" value="打印" onClick="printit()"> 	--%>
	<!--<input type="button" name="button_setup" class="ptbutton" value="打印设置" onClick="printsetup()"> 
	--><input type="button" name="button_show" class="ptbutton" value="准备打印" onClick="printpreview()"> 
    </td>
  </tr>
</table>
  <%
    if(piclist!=null&&piclist.size()>0){
     for(PathVo pic:piclist){
 //  keyWord = java.net.URLEncoder.encode(pic,"UTF-8");
   %>
<img  style="width:100%; height:120%" src="${pageContext.request.contextPath}/summary/summaryFormAction!writeImage.action?pic=<%=pic.getPicname() %>" />
	</td>
	</tr>
	<%
	  }
	    }
	 %>
	 <script type="text/javascript">
	 factory.printing.header = "&b&b" ;    //页眉
        factory.printing.footer = "&b";    //页脚
         
        factory.printing.portrait = false;  //横竖排版
	 
	 </script>
</body>
</html>
