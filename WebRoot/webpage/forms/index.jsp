<%@page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page contentType="text/html; charset=utf-8"%> 
<%@page import="com.inspect.vo.basis.UnicomVo"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Statement"%>
<%@page import="com.sptdi.forms.DataRecord"%>
<%@page import="com.sptdi.forms.UnicomGForm"%>
<%@page import="com.sptdi.forms.UnicomWForm"%>
<%@page import="com.sptdi.forms.UnicomDForm"%>
<%@page import="com.sptdi.forms.cover"%>
<html>
<head><title>登记表</title></head>
<body>
<center>
<h1>打印巡检报表</h1>
<hr>
<form name="teacForm" action="#" method="post">
<table>
<tbody>

<tr>

<td><b>运营商</b></td>
<td>
<select name="company">
<option checked value="联通"></option>
<option value="联通">联通</option>
<option value="电信">电信</option>
</select>
</td>


<td><b>区县</b></td>
<td>
<select name="area">
<option checked value="青浦区"></option>
<option value="虹口区">虹口区</option>
<option value="闵行区">闵行区</option>
<option value="闸北区">闸北区</option>
<option value="浦东区">浦东区</option>
<option value="杨浦区">杨浦区</option>
<option value="金山区">金山区</option>
<option value="南汇区">南汇区</option>
<option value="青浦区">青浦区</option>
<option value="普陀区">普陀区</option>
<option value="奉贤区">奉贤区</option>
<option value="卢湾区">卢湾区</option>

 
</select>
</td>
</tr>
</tbody>
</table>
<p align="center">
<input type="submit" value="提交" name="Submit">
<input type="reset" value="重填" name="Submit2">
</p>
</form>






    <% 
    if((request.getParameter("area"))!=null&&(request.getParameter("area")).length()!=0)
    {
    	
    String area = request.getParameter("area").toString();

    //String area = new String(request.getParameter("area").getBytes("utf-8"), "gbk"); 
 
   // System.out.println("县区0： "+area0);
    System.out.println("县区： "+area);
    
    String url =  "jdbc:mysql://127.0.0.1:3306/inspect",password = "123";
    String user = "root";
   
    Connection conn = null;
    String sql = null;
    String address = null;
	String longitute = null;
	String latitude = null;
	 Class.forName("com.mysql.jdbc.Driver");// 动态加载mysql驱动
	conn = DriverManager.getConnection(url, user, password);
	if(conn!=null){
		System.out.println("数据库连接成功！");
	}
	
	Statement stmt = conn.createStatement();
	//String tableid = "C0382";
    //sql = "select * from unicominfo where license = '310020140005/C0189'";
	   if(area.contains("浦东")){
		   sql = "select * from unicominfo where administrativedivision ='浦东' or administrativedivision ='浦东新区'"; 
	    }
	   else{
	       sql = "select * from unicominfo where administrativedivision = '"+area+"'";
	   }
	ResultSet rs = stmt.executeQuery(sql);// executeQuery会返回结果的集合，否则返回空值
	if(rs!=null){
		System.out.println("结果集不为空！");
	}
	 List<UnicomVo> unicomvos = new ArrayList(); 
	while (rs.next()) {
		UnicomVo uvo = new UnicomVo();
		uvo.setOperator(rs.getString("operator"));
		uvo.setAdministrativedivision(rs.getString("administrativedivision"));
		uvo.setLicense(rs.getString("license"));
		uvo.setBaseid(rs.getString("baseid"));
		uvo.setBasename(rs.getString("basename"));
		uvo.setAddress(rs.getString("address"));
	
		unicomvos.add(uvo);
	}
        //构造列表对象，实际程序中是从数据库读取的信息。 
   
       
    %> 
    
	<table border="1">

		<tr>

			<th>运营商</th>
			
			<th>区县</th>

			<th>申请编号</th>

			<th>基站编号</th>

			<th>基站名称</th>
			
			<th>基站地址</th>

		</tr>

		<%
			for (UnicomVo unicomvo : unicomvos) {
		%>

		<tr>

			<td><%=unicomvo.getOperator()%></td>
			
			<td><%=unicomvo.getAdministrativedivision()%></td>

			<td><%=unicomvo.getLicense()%></td>

			<td><%=unicomvo.getBaseid()%></td>
			
			<td><%=unicomvo.getBasename()%></td>
			
         	<td><%=unicomvo.getAddress()%></td>
	
		

            <td id=<%=unicomvo.getLicense()%>>
			<a href="${pageContext.request.contextPath}/basis/docAction!docPrint.action?TrueName=<%=unicomvo.getLicense()%>">导出报表</a>
			</td>
			
		
			
		</tr>

		<%
			}
		%>

	</table>
	
	<%} %>
	
	
	
</body>
</html>
