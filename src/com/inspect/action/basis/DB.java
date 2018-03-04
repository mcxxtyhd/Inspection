package com.inspect.action.basis;
import javax.naming.*;
import java.sql.*;
import javax.sql.*;
import java.util.*;
public class DB
{
	private static Connection con=null;//����Connection����
	private static Statement stat=null;//����Statement����
	private static ResultSet rs=null;//����ResultSet����
//*****************************��ݿ����Ӻ͹رղ���*************************
	public static Connection getCon(){//�õ���ݿ����ӵķ���	
		
/*		try{			
			 Context initial = new InitialContext();//�õ�����������
			 DataSource ds = //�õ�DataSource����
				    (DataSource)initial.lookup("java:comp/env/jdbc/jiudian");
			 con = ds.getConnection();//�õ���ݿ�����
		}
		catch(Exception e)
		{e.printStackTrace();}
		return con;//������ݿ�����
*/		
		
		try{
		Class.forName("com.mysql.jdbc.Driver");
		
		String url="jdbc:mysql://localhost/hotel";
		con = DriverManager.getConnection(url,"root","123");
		}catch(ClassNotFoundException e){
			e.printStackTrace();
			}catch(SQLException e1){
				e1.printStackTrace();
				}
		return con;
	}
	public static void closeCon(){//�ر���ݿ����ӷ���
		try	{
			  if(rs!=null){rs.close();}
			  if(stat!=null){stat.close();}
			  if(con!=null){con.close();}
		}
		catch(Exception e)
		{e.printStackTrace();}
	}
//*******************�Է���Ĳ���******************************************
	public static Vector<String[]> getGroup(){
		Vector<String[]> v =new Vector<String[]>();//����������������
		try{
			 con = DB.getCon();//�õ���ݿ�����
			 stat = con.createStatement();//����������
			 String sql = "select gName,gImg,gDetail,gId,gOrderDet from rgroup";
			 rs = stat.executeQuery(sql);
			 while(rs.next()){//������õ�������Ϣ		    
			    String group[] = new String[5];
			    for(int i=0;i<group.length;i++){
			      group[i] = //����Ϣ��ӵ�����
			    		  new String(rs.getString(i+1));
			    		  //new String(rs.getString(i+1).getBytes("iso8859-1"),"gbk");
			    }			
				v.add(group);//����Ϣ������ӵ����ص�������
			}
		}
		catch(Exception e)
		{e.printStackTrace();}
		finally
		{DB.closeCon();}	
		return v;
	}
	public static Vector<String> getGroupInfo(int gId){
		Vector<String> v =new Vector<String>();//����������Ϣ����	
		try{
			 con = DB.getCon();//�õ���ݿ�����
			 stat = con.createStatement();//����������
			 String sql = "select gId,gName,gOrderDet,gImg,gDetail from"+
			  				" rgroup where gId="+gId;
			 rs = stat.executeQuery(sql);//ִ��SQL��ѯ
			 if(rs.next()){//�������Ϣ��ӵ�����������			  
				v.add(new String(rs.getString(1)));
				v.add(new String(rs.getString(2)));
				v.add(new String(rs.getString(3)));
				v.add(new String(rs.getString(4)));
				v.add(new String(rs.getString(5)));								
			 }
		}
		catch(Exception e){e.printStackTrace();}
		finally	{DB.closeCon();}//�ر���ݿ�����
		return v;//���ط�����Ϣ
	}
	public static String getOrderDet(int gId)
	{
		String msg="";
		try
		{
			con=DB.getCon();
			stat=con.createStatement();
			String sql="select gOrderDet from rgroup where gId="+gId;
			rs=stat.executeQuery(sql);
			if(rs.next())
			{
				msg=new String(rs.getString(1));
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			DB.closeCon();
		}
		return msg;
	}
	
//******************��ҳ����**************************************************
public static int getTotal(int span,int group){
	int result=0;//��ʼ������ҳ��
	String sql = "";//����sql����
	try{
		con = DB.getCon();
		stat = con.createStatement();
		//�õ���ؼ�¼��������
		if(group==0)//0������з���
		{sql = "select count(*) from resource";}
		else{
			sql = "select count(*) from resource "+"where rgroup='"+group+"'";
		}
		rs = stat.executeQuery(sql);//ִ��sql���			
	    rs.next();
	    int rows=rs.getInt(1);//�õ���¼����
	    result=rows/span+((rows%span==0)?0:1);//�������ҳ��
	}
	catch(Exception e){e.printStackTrace();}
	finally{DB.closeCon();}//�ر���ݿ�����		
	return result;//���ؽ��
}
public static Vector<String[]> getResource(String rlevel)
	{
	   System.out.println("in getresource*******");
		
	   Vector<String[]> v=new Vector<String[]>();
		
		String sql;
		try
		{
			con=DB.getCon();
			stat=con.createStatement();
			//String rlevell = new String(rlevel.getBytes("gbk"),"iso8859-1");
			String rlevell = new String(rlevel);
			sql="select rgid,rlevel,rmoney,rstatus from resource where rlevel='"+rlevell+"'";
			rs=stat.executeQuery(sql);
            while(rs.next()){//������õ�������Ϣ		
            	
            	System.out.println("in getresource rs*******");
            	
			    String group[] = new String[4];
			    for(int i=0;i<group.length;i++){
			      group[i] = new String(rs.getString(i+1)); //����Ϣ��ӵ�����
			      
			      System.out.println("******getresourcedb*****"+group[i]);
			    }			
				v.add(group);//����Ϣ������ӵ����ص�������
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			DB.closeCon();
		}
		return  v;
	}
	public static Vector<String[]> getResource1()
	{
		Vector<String[]> v=new Vector<String[]>();
		try
		{
			con=DB.getCon();
			stat=con.createStatement();
			String sql="select rgroup,rgid,rlevel,rmoney,rstatus from resource";
			rs=stat.executeQuery(sql);
            while(rs.next()){//������õ�������Ϣ		    
			    String group[] = new String[4];
			    for(int i=0;i<group.length;i++){
			      group[i] = //����Ϣ��ӵ�����
			    	new String(rs.getString(i+2));
			    }			
				v.add(group);//����Ϣ������ӵ����ص�������
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			DB.closeCon();
		}
		return  v;
	}
	
public static Vector<String[]> getPageContent(int page,int span,int group){
	Vector<String[]> v = new Vector<String[]>();//����������������
	String sql = "";//����sql�������
	int startRow = 	(page-1)*span;//�������ʼ��¼����
	try{
		con = DB.getCon();
		stat = con.createStatement();
		if(group==0){//group����Ϊ�㣬���Ƕ����еķ�����з�ҳ��ʾ
			sql = "select rgid,rlevel,rmoney,rdetail,rstatus,rid,gName from "+
			       "resource,rgroup where resource.rgroup=rgroup.gId order "+
			        "by rgroup, rgid, rid";
		}
		else{//�Ծ��������з�ҳ��ʾ
			sql = "select rgid,rlevel,rmoney,rdetail,rstatus,rid,gName "+
			 	   "from resource,rgroup where resource.rgroup=rgroup.gId "+
			 	   "and rgroup='"+group+"' order by rgid";
		}
		rs = stat.executeQuery(sql);//ִ��sql��䣬�õ����
		if(startRow!=0)//�����ʵ�в��ǵ�����
		{rs.absolute(startRow);}//����������ʼ��
		int c=0;//���ƶ�ȡ�ļ�¼����
		while(c<span&&rs.next()){//����ʵ�ж�ÿҳ��ʾ�ļ�¼����
			String s[] = new String[7];
			for(int i=0;i<s.length;i++){
		      s[i] = //��������Ϣ��ӵ�����
		    	new String(rs.getString(i+1));
		    }							
			v.add(s);//��������ӵ���������
			c++;
		}
	}
	catch(Exception e){e.printStackTrace();}
	finally{DB.closeCon();}//�ر���ݿ�����	
	return v;//���ؽ��
}
//*******************�õ�ĳ�ű�ĳһ�е����ֵ����1***************************
	public static int getId(String table,String row){//�õ�һ��������ID+1ֵ
		int id = 0;
		try	{
			con = DB.getCon();//�õ���ݿ�����
			stat = con.createStatement();//����������
			rs = stat.executeQuery("select count(*) from "+table);
			rs.next();
			if(rs.getInt(1)==0)	{ id = 1; }//������û�м�¼����id��Ϊ1
			else{
				rs = stat.executeQuery("select max("+row+") from "+table);
				rs.next();
				id = Integer.parseInt(rs.getString(1))+1;//����ֵ��һ
			}						
		}
		catch(Exception e){e.printStackTrace();}
		finally	{DB.closeCon();}//�ر���ݿ�����
		return id;//���ؽ��
	}
//********************ĳ����¼�Ƿ����**************************************
	public static boolean isExist(String sqla){//�鿴������¼�Ƿ����
		boolean flag = false;			
		try{			
			String sql = new String(sqla.getBytes("gbk"),"iso8859-1");//ת��	
			con = DB.getCon();//�õ���ݿ�����
			stat = con.createStatement();//����������
			rs = stat.executeQuery(sql);//ִ�в�ѯ
			if(rs.next()) {flag = true;}
		}
		catch(Exception e)	{e.printStackTrace();}
		finally	{DB.closeCon();}//�ر���ݿ�����		
		return flag;//���ؽ��
	}
//*********************������ݿ�*****************************************
	public static int update(String sqla){
		int changedCount=0;
		try{
			String sql = new String(sqla.getBytes(),"iso8859-1");//ת��
			con = DB.getCon();//�õ���ݿ�����
			stat = con.createStatement();//����������
			changedCount = stat.executeUpdate(sql);//���и���
		}
		catch(Exception e)	{e.printStackTrace();}
		finally {DB.closeCon();}//�ر���ݿ����� 		
		return changedCount;//���ظ��¼�¼����
	}
	public static boolean update(String sqla,String sqlb){//������
		boolean b = false;//��¼�Ƿ���³ɹ�
		try{
			con = DB.getCon();//�õ���ݿ�����
			con.setAutoCommit(false);//��ֹ�Զ��ύ����ʼһ������
			stat = con.createStatement();
			String sql = new String(sqla.getBytes(),"iso8859-1");//ת��
			stat.executeUpdate(sql);//ִ�и���
			sql = new String(sqlb.getBytes(),"iso8859-1");//ת��
			stat.executeUpdate(sql);//ִ�и���			
			con.commit();//�������ύ			
			con.setAutoCommit(true);//�ָ��Զ��ύģʽ
			b = true;//���ø��³ɹ�
		}
		catch(Exception e){
			e.printStackTrace();
			try{
				con.rollback();//�������⣬����ع�
				b = false;
			}
			catch(Exception ea){ea.printStackTrace();}
		}
		finally{DB.closeCon();}//�ر���ݿ�����
		return b;//���ظ��³ɹ�����ʧ�ܱ�־
	}
		public static boolean updatea(String sqla)//����ӵ�
	{
		boolean b=false;
		try{
			String sql = new String(sqla.getBytes(),"iso8859-1");//ת��
			con = DB.getCon();//�õ���ݿ�����
			con.setAutoCommit(false);//��ֹ�Զ��ύ����ʼһ������
			stat = con.createStatement();//����������
			stat.executeUpdate(sql);//���и���
			con.commit();//�������ύ			
			con.setAutoCommit(true);//�ָ��Զ��ύģʽ
			b = true;//���ø��³ɹ�
		}
		catch(Exception e){
			e.printStackTrace();
			try{
				con.rollback();//�������⣬����ع�
				b = false;
			}
			catch(Exception ea){ea.printStackTrace();}
		}
		finally{DB.closeCon();}//�ر���ݿ�����
		return b;//���ظ��³ɹ�����ʧ�ܱ�־
	}
//********************���һ��SQL�õ���ݿ�����Ϣ****************************
	public static String getInfo(String sqla){
		String Info=null;
		try{			
			String sql = new String(sqla.getBytes(),"iso8859-1");//SQLת��
			con = DB.getCon();//�õ���ݿ�����
			stat = con.createStatement();//����������
			rs = stat.executeQuery(sql);//ִ�в�ѯ
			if(rs.next())
			{Info=new String(rs.getString(1));}
		}
		catch(Exception e)	{e.printStackTrace();}
		finally {DB.closeCon();}		
		return Info;
	}
	public static String getDetail(String rgid)
	{
		String s=null;
		try
		{
			String sql="select rdetail from resource where rgid="+rgid;
			con=DB.getCon();
			stat=con.createStatement();
			rs=stat.executeQuery(sql);
			if(rs.next())
			{
				s=new String(rs.getString(1));
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			DB.closeCon();
		}
		return s;
	}
	
//***********************�õ��û�����ϸ��Ϣ********************************
	public static Vector<String> getUserInfo(String uname1){
		Vector<String> userInfo=new Vector<String>();
		try{
			String uname = new String(uname1.getBytes("gbk"),"iso8859-1");//ת��
			con = DB.getCon();//�õ���ݿ�����
			stat = con.createStatement();//����������
			rs = stat.executeQuery("select pwd,telNum,realName,gender,"+
									"email from user where uname='"+uname+"'");
			if(rs.next()){//���û���Ϣ��ӵ�������
			    userInfo.add(new String(rs.getString(1)));
				userInfo.add(new String(rs.getString(2)));
				userInfo.add(new String(rs.getString(3)));
				userInfo.add(new String(rs.getString(4)));
				userInfo.add(new String(rs.getString(5)));
			}
		}
		catch(Exception e) {e.printStackTrace();}
		finally	{DB.closeCon();}//�ر���ݿ�����		
		return userInfo;//�����û���Ϣ
	}
//******************�õ���Դ����ϸ��Ϣ***************************
	public static Vector<String[]> getResInfo(String sqla){
		Vector<String []> v = new Vector<String[]>();
		try{
			con = DB.getCon();//�õ���ݿ�����
			stat = con.createStatement();//����������			
			String sql = new String(sqla.getBytes(),"iso8859-1");//ת��
			rs = stat.executeQuery(sql);//ִ�в�ѯ
			while(rs.next()){
				String s[] = new String[8];
				for(int i=0;i<s.length-1;i++){//����Դ��Ϣ��ӵ�����
					s[i] = new String(rs.getString(i+1));
				}				
				v.add(s);//��������ӵ�����������
			}
			for(String s[]:v){//��ݷ���ID�õ�������				
				String sqlb = "select gName from rgroup where gId='"+s[5]+"'";
				rs = stat.executeQuery(sqlb);
				rs.next();//����α������һλ
				s[7] = new String(rs.getString(1));
			}
		}
		catch(Exception e){e.printStackTrace();}
		finally{DB.closeCon();}//�ر���ݿ�����
		return v;//���ز�ѯ���
	}
	
//*****************�õ�����Ա��ϸ��Ϣ********************************
	public static Vector<String[]> getAdminInfo(){
		Vector<String[]> v = new Vector<String[]>();
		try{
			con = DB.getCon();//�õ���ݿ�����
			stat = con.createStatement();//����������
			rs = stat.executeQuery("select adname,adlevel from adinfo");//ִ�в�ѯ
			while(rs.next()){
				String s[] = new String[2];
				s[0] = new String(rs.getString(1));
				s[1] = new String(rs.getString(2));				
				v.add(s);
			}
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
		finally{DB.closeCon();}		
		return v;
	}
		public static boolean isDelete(String rid1){
		boolean b = false;
		int count=0;
		try{
			String orid=new String(rid1.getBytes("iso8859-1"),"gbk");
			System.out.println(orid);
			String sqla="delete from olist where oid="+orid;
			String sqlb="delete from oinfo where orid="+orid;
		    count+=stat.executeUpdate(sqla);
		    count+=stat.executeUpdate(sqlb);
		    if(count==2){b=true;}
		}
		catch(Exception e){e.printStackTrace();}
		finally{DB.closeCon();}
		return b;
	}
}