package com.inspect.util;



import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;


/***
 * 
 * This class is used for ...   
 * @author liux  
 * @version   
 *       1.0, 2013-4-22 下午01:22:13   
 * information :
 *		 DButil类----主要作用于，对数据库的链接及操作
 */
public class DBUtil {
	private String com,driver,sql,local,port,data,name,use,password;
	private Connection con;
	private static DBUtil util ;
	/**
	 * 构造方法，在构造自己的时候得到配置文件里面的与数据库有关的数据
	 */
	private DBUtil(){
		
		com = ConfigReadOrWrite.getInstance().readProperties( "connection.driver_class" ) ;
		driver = ConfigReadOrWrite.getInstance().readProperties( "jdbc_url" ) ;
		use = ConfigReadOrWrite.getInstance().readProperties( "jdbc_username" ) ;
		password = ConfigReadOrWrite.getInstance().readProperties( "jdbc_password" );
		con=this.connection();
	}
	
	public synchronized static DBUtil getInstanceUtil(){
		if( util == null ){
			util = new DBUtil() ;
		}
		return util ;
	}
	
	/**
	 * 得到管道并建立链接
	 * @return		Connection
	 */
	private Connection connection(){
		try {
			if( con == null || con.isClosed() ){
				StringBuffer url = new StringBuffer() ;
				//得到的是链接数据库的URL
				//sqlserver
				//String url1 =driver+":"+sql+"://"+local+":"+port+";database="+data+";user="+use+";password="+password;
				String url1 =driver +"&user="+use+"&password="+password;
				//jdbc:mysql://localhost:3306/sample_db?user=root&password=your_password
				//url.append(driver).append(":").append(sql).append("://").append(local) ;
				//url.append(":").append(port).append("/").append(data).append("?user=").append(use).append("&password=") ;
				//url.append(password).append("&useUnicode=true&characterEncoding=UTF-8") ;
				//com在构造自己的时候应景将数据加入进去----就是配置文件的驱动（指的是何种数据库）
				Class.forName(com);
				return DriverManager.getConnection( url.append(url1).toString() );
			}
		} catch (Exception e) {
			System.out.println( e.getMessage() );
			try
		    {
				  con.close();
				  util = null ;
		    }catch(Exception ex1){
		        System.out.println("数据库连接失败！");
		    }
		}
		return con ;
	}
	
	/**
	 * 增删改都可以用此方法
	 * @param sql
	 * @param object
	 * @return	int
	 */
	public synchronized void  update(String sql,Object ... object ) {
		PreparedStatement prepared = null ;
		try {
			//得到建立的管道
			//con=this.connection();
			//得到链接到数据库的preparedstatement-----传入SQL语句
			prepared=con.prepareStatement(sql);
			//得到DAO层的数组----并开始加入到-------preparedstatement
			if(object != null){
				for(int sign=0;sign<object.length;sign++){
					prepared.setObject(sign+1,object[sign]);
				}
			}
			prepared.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				prepared.close() ;
			} catch (SQLException e) {}
		}
	}
	/**
	 * 关闭批处理语句
	 * @param pst
	 */
	public static void closePreparedStatment(PreparedStatement pst){
		try{
			if (pst != null) {
				pst.clearBatch();
				pst.close();
	        }
		}catch(Exception e){
		}		
	}
	/**
	 * 关闭结果集
	 * @param rs
	 */
	public static void closeResultSet(ResultSet rs){
		try{
			if (rs != null) {
				rs.close();
	        }
		}catch(Exception e){
			
		}		
	}
	/**
	 * 关闭Statement
	 * @param st
	 */
	public static void closeStatement(Statement st){
		try{
			if(st!=null){
				st.clearBatch();
				st.close();
			}
		}catch(Exception e){
			
		}
	}
	/**
	 * 关闭连接
	 * @param connection
	 */
	public static void closeConnection(Connection connection){
		try{
			if (connection != null) {
				connection.close();
	        }
		}catch(Exception e){
			
		}		
	}
	/**
	 * 关闭session
	 * @param session
	 */
	public static void closeSession(Session session){
		try{
			if (session != null) {
	            session.close();
	        }
		}catch(Exception e){
		}		
	}
	/**
	 * 回滚
	 * @param connection
	 */
	public static void rollback(Connection connection){
		try{
			if (connection != null) {
				connection.rollback();
	        }
		}catch(Exception e){
		}		
	}
	

	
	/**
	 * 查询的事件util方法
	 * @param sql
	 * @param object
	 * @return ResultSet
	 */
	public ResultSet select(String sql,Object ... object ){
		//得到建立的管道
		//con=this.connection();
		PreparedStatement perpared = null ; 
		ResultSet result = null ;
		try {
			//得到链接到数据库的preparedstatement-----传入SQL语句
			perpared=con.prepareStatement(sql);
			if(object != null){
				//得到DAO层的数组----并开始加入到-------preparedstatement
				for(int sign=0;sign<object.length;sign++){
					perpared.setObject(sign+1,object[sign]);
				}
			}
			result = perpared.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			/*try {
				perpared.close() ;
			} catch (SQLException e) {}*/
		}
		return result ;
	}
	
	/***
	 * 批量执行保存上传数据sql
	 * @param list
	 * @param sql
	 
	public void batchSave(List<Object> list,String sql){
		if(list != null){
			try {
				con.setAutoCommit(false);
				//得到链接到数据库的preparedstatement-----传入SQL语句
				PreparedStatement perpared = con.prepareStatement(sql) ;
				//得到DAO层的数组----并开始加入到-------preparedstatement
				for( ReaderData readerData : list ){
					perpared.setInt( 1, readerData.getVisitorID() ) ;
					perpared.setInt( 2, readerData.getCompanyID() ) ;
					perpared.setFloat( 3, readerData.getRssi() ) ;
					perpared.setTimestamp( 4, readerData.getRecordTime() ) ;
					perpared.addBatch();
				}
				perpared.executeBatch();   
				//Commit it 咽下去,到肚子(DB)里面
				con.commit();
				list = null ;
				System.gc() ;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}*/
	
	/***
	 * 此方法做两件事情，
	 * 第一如果档期那链接出错就返回错误，
	 * 第二件事情如果当前服务加载的时候链接错了，那么会将当前对象置空当前对象，因为是单例所以必须置空
	 * @return
	 * @author liux
	 */
	public synchronized boolean isConnect(){
		boolean remark = false ;
		if ( con != null  ) {
			try {
				if( ! con.isClosed() ){
					remark = true ;
				}
			} catch (SQLException e) {
				util = null ;
			}
		}else{
			util = null ;
		}
		return remark ;
	}
	
	
	
	public static void main(String[] args) {
		//	System.out.println( new Date().getTime() );
		//DBUtil utiel = new DBUtil();
		List<Object> list = new ArrayList<Object>();
		String sql = "select * from t_log " ;
		ResultSet result =  DBUtil.getInstanceUtil().select( sql ) ;
		try {
			while( result.next() ){
				System.out.println(result.getString("opevent"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//	System.out.println( "" );
		/*		int tagger = 0 ;
		ResultSet result =  DBUtil.getInstanceUtil().select(  "select triggerrelevanceid from t_eventtriggerterminfo where termid = ? " , 15 ) ;
		try {
			while( result.next() ){
				tagger += 1 ;
				int taggerTwo = 0 ;
				ResultSet resultTwo =  DBUtil.getInstanceUtil().select(  "select eventbusinessconfigid from t_eventbusinessconfiginfo where id = ? " , result.getInt(tagger) ) ;
				while( resultTwo.next() ){
					taggerTwo+= 1 ;
					int taggerThree = 0 ;
					ResultSet resultThree =  DBUtil.getInstanceUtil().select(  "select eventid from t_appeventbusinessinfo where id = ? " , resultTwo.getInt(taggerTwo) ) ;
					while( resultThree.next() ){
						taggerThree+= 1 ;
						list.add( resultThree.getObject( taggerThree ) ) ;
					}
					resultThree.close() ;
				}
				resultTwo.close() ;
			}
			result.close() ;
			
		} catch (SQLException e) {}
		for( Object o : list){
			System.out.println( o );
		}*/
		/*System.out.println( new Date().getTime() );
		StringBuffer sql = new StringBuffer( "select eventid from t_appeventbusinessinfo where id = " );
		sql.append( "( select eventbusinessconfigid from t_eventbusinessconfiginfo where id = ") ;
		sql.append(" ( select triggerrelevanceid from t_eventtriggerterminfo where termid = ? ))" ) ;
		ResultSet result =  DBUtil.getInstanceUtil().select(  sql.toString() , 15 ) ;
		try {
			while( result.next() ){
				list.add( result.getObject(1) ) ;
			}
			result.close() ;
		} catch (SQLException e) {}
		System.out.println( new Date().getTime() );*/
		//String v = ConfigReadOrWrite.getInstance().readProperties( "driverClassName" ) ;
		//System.out.println( v );
	}
}
