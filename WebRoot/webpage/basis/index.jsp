<%@page language="java" import="java.util.*" pageEncoding="gbk"%>
<%@page contentType="text/html; charset=gbk"%> 
<%@page import="com.inspect.vo.basis.UnicomVo"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Statement"%>
<html>
<head><title>�ǼǱ�</title></head>
<body>
<center>
<h1>��ӡѲ�챨��</h1>
<hr>
<form name="teacForm" action="index.jsp" method="post">
<table>
<tbody>

<tr>

<td><b>��Ӫ��</b></td>
<td>
<select name="company">
<option checked value="��ͨ"></option>
<option value="��ͨ">��ͨ</option>
<option value="����">����</option>
</select>
</td>


<td><b>����</b></td>
<td>
<select name="area">
<option checked value="������"></option>
<option value="�����">�����</option>
<option value="������">������</option>
<option value="բ����">բ����</option>
<option value="�ֶ���">�ֶ���</option>
<option value="������">������</option>
<option value="��ɽ��">��ɽ��</option>
<option value="�ϻ���">�ϻ���</option>
<option value="������">������</option>
<option value="������">������</option>
<option value="������">������</option>
<option value="¬����">¬����</option>

 
</select>
</td>
</tr>
</tbody>
</table>
<p align="center">
<input type="submit" value="�ύ" name="Submit">
<input type="reset" value="����" name="Submit2">
</p>
</form>




    <% 
    if((request.getParameter("area"))!=null&&(request.getParameter("area")).length()!=0)
    {
    	
    String area0 = request.getParameter("area").toString();

    String area = new String(request.getParameter("area").getBytes("iso8859-1"), "gbk"); 
 
 
    System.out.println("������ "+area);
    
    String url =  "jdbc:mysql://127.0.0.1:3306/inspect",password = "123";
    String user = "root";
   
    Connection conn = null;
    String sql = null;
    String address = null;
	String longitute = null;
	String latitude = null;
	 Class.forName("com.mysql.jdbc.Driver");// ��̬����mysql����
	conn = DriverManager.getConnection(url, user, password);
	if(conn!=null){
		System.out.println("���ݿ����ӳɹ���");
	}
	
	Statement stmt = conn.createStatement();
	//String tableid = "C0382";
    //sql = "select * from unicominfo where license = '310020140005/C0189'";
	   if(area.contains("�ֶ�")){
		   sql = "select * from unicominfo where administrativedivision ='�ֶ�' or administrativedivision ='�ֶ�����'"; 
	    }
	   else{
	       sql = "select * from unicominfo where administrativedivision = '"+area+"'";
	   }
	ResultSet rs = stmt.executeQuery(sql);// executeQuery�᷵�ؽ���ļ��ϣ����򷵻ؿ�ֵ
	if(rs!=null){
		System.out.println("�������Ϊ�գ�");
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
        //�����б����ʵ�ʳ������Ǵ����ݿ��ȡ����Ϣ�� 
   
       
    %> 
    
	<table border="1">

		<tr>

			<th>��Ӫ��</th>
			
			<th>����</th>

			<th>������</th>

			<th>��վ���</th>

			<th>��վ����</th>
			
			<th>��վ��ַ</th>

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
	
		

            <td><input type="button" value="��������"
				onClick="javascript:window.open('recordtable.jsp?TrueName=<%=unicomvo.getLicense()%>')" />
			</td>
			
		
			
		</tr>

		<%
			}
		%>

	</table>
	
	<%} %>
	
	
	
</body>
</html>
