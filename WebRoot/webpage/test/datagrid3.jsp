<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
<title>Insert title here</title>
	<jsp:include page="/common/easyui.jsp"></jsp:include>
 
	
	<script>
		function load1(){
			using('calendar', function(){
				$('#cc').calendar({
					width:180,
					height:180
				});
			});
		}
		function load2(){
			using(['dialog','messager'], function(){
				$('#dd').dialog({
					title:'Dialog',
					width:300,
					height:200
				});
				$.messager.show({
					title:'info',
					msg:'dialog created'
				});
			});
		}
	</script>
</head>
<body>
	<h1>EasyLoader</h1>
	<a href="#" class="easyui-linkbutton" onclick="load1()">Load Calendar</a>
	<a href="#" class="easyui-linkbutton" onclick="load2()">Load Dialog</a>
	<div id="cc"></div>
	<div id="dd"></div>
</html>
