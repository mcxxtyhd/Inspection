package com.inspect.action.webservice;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import net.sf.json.JSONObject;

import org.apache.struts2.json.JSONUtil;

import com.inspect.model.monitor.TUserLocateReport;
import com.inspect.service.InspectMonitorServiceI;
import com.inspect.service.impl.InspectMonitorServiceImpl;
import com.inspect.util.common.SystemServerGetBean;


/***
 * 
 * This class is used for ...   
 * @author liao 
 * @version   
 * information :

 */
public class PositionTcpServer {
	
	protected ConcurrentHashMap<String,String > visitorAndcompanyMap = new ConcurrentHashMap<String, String>( );
	protected  ConcurrentHashMap<String, Socket > visitorAndSocketMap = new ConcurrentHashMap<String,Socket >( );
	private  Boolean isContinue ;
	private  ServerSocket serverSocket = null;  
	private  int port ;
	private StringBuffer className ;
	private static PositionTcpServer positionUdpServer ;

	private InspectMonitorServiceImpl inspectMonitorService= new InspectMonitorServiceImpl();
	InspectMonitorServiceI services = null ;
	private ExecutorService pool = null; 
	private PositionTcpServer( ){
		isContinue = true ;
		port = 8081 ;
		className = new StringBuffer();
		
		pool = Executors.newFixedThreadPool( 50 );  
	}
	
	public InspectMonitorServiceI getInspectMonitorService() {
		return inspectMonitorService;
	}



/**
 * @param
 * @return
 */
	public synchronized static PositionTcpServer getInstance( ){
		try {
			if( positionUdpServer == null ){
				positionUdpServer = new PositionTcpServer();
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			System.out.println("异常22----------------------------------------------");
		}
	
		return positionUdpServer ;
	}
	public static void main(String[] args) {
		PositionTcpServer positionTcpServer=PositionTcpServer.getInstance();
		positionTcpServer.startService();
		
	}
	
	/***
	 * 此方法给主进程使用
	 * @return
	 * @author  liao 
	 */
	public static boolean getPositionUdpServerIsNull(){
		return positionUdpServer == null ;
	}
	
	/**
	 * 返回当前对象
	 * @return
	 * @author  liao 
	 */
	public static PositionTcpServer getPositionUdpServer(){
		return positionUdpServer ;
		
	}
	

	
	/***
	 * 初始化监听端口
	 * @return 返回执行的参数
	 * @author  liao 
	 */
	public void init(){
		try {
		//	System.out.println("333333333333333");
			serverSocket = new ServerSocket( port  );  
			serverSocket.setSoTimeout(0);
		    isContinue = true ;
		    //启动一个循环线程，在一个时间端推送数据到手机端，这个时间是可以设置的。
			//System.out.println( "android服务启动........." );
		 } catch (Exception e) {
			
			 e.printStackTrace();
			 System.out.println(e.getMessage());
			 System.out.println("监听异常--------------------------------------------------------------");
		}  
	}

	public void endService(){
		isContinue = false ;
		try {
			serverSocket.close();
			System.out.println("终端服务关闭");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("终端服务关闭---异常-");
			e.printStackTrace();
		}
		
		
	}
	/***
     * 服务启动
     * @author  liao 
     */
	public void startService(){
		init() ;
		//返回日志到面板
		try {
			Thread.sleep(20 * 1000) ;
			this.receive();   
			services = (InspectMonitorServiceI) SystemServerGetBean.getInstance().returnClass( "inspectMonitorService" );
			System.out.println("d");
		} catch (Exception e1) {
			isContinue = false ;
			e1.printStackTrace();
			System.out.println(e1.getMessage());
			System.out.println("线程异常--------------------------------------------------------------");
			
		}finally{
			try {
				isContinue = true ;
				this.receive();   
			} catch (Exception e1) {
				System.out.println("线程异常2--------------------------------------------------------------");
			}
		}
				  
	}
	
	/***
	 * 
	 * 服务类
	 * @author  liao 
	 */
	public void receive()throws Exception{
		try {
			while ( isContinue ) {  
				for (int i = 0; i <20; i++) {
					System.out.println("在 监听等待------------------------------------------");
				}
				
				
				Socket client = serverSocket.accept();
				if( client != null ){
					//Runnable runable =  ;
					pool.execute(new PosServer( client ));//////////////////
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println(e.getMessage());
			System.out.println("有异常xianchen exception--------------------------------------------------------------");
		}
	
	}

    

	
    /***
     * @return 返回当前类名称
     * @author  liao 
     */
    protected StringBuffer getClassName(){
    	className.delete(0, className.length() ) ; 
    	className.append( this.getClass().getName() ).append("       ") ;
    	return className ;
    }
    


    /**
     * 经纬度线程处理
     * @author liao
     *
     */
class PosServer implements Runnable{
	
	private Socket client;
	TUserLocateReport ur = null ;
	String info = null  ;
	Date date = null ;
	JSONObject json = new JSONObject();
	SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	DataInputStream inputStream;  
	
	public PosServer(Socket client){
		this.client = client ;
	}
	
	public void run(){
		try {
			this.inputStream = new DataInputStream(client.getInputStream());
			 info = inputStream.readUTF();  
			 System.out.println("客户端说 " + info );   
			    ur=new TUserLocateReport();
			    ur.setEntid(1);
			    date=new Date();
				ur.setRptime(format.format(date));
			    if( info != null ){
			    	Object obj=JSONUtil.deserialize(info);
			    	HashMap objMap=(HashMap)obj;
			    	ur.setRpterminalid((String) objMap.get("rpterminalid"));
			    	//System.out.println("objMap.get()"+objMap.get("rplineid"));
			    	ur.setRplineid(Integer.parseInt((String)objMap.get("rplineid")));
			    	ur.setRpposx((Double.parseDouble((String)objMap.get("rpposx"))));
			    	ur.setRpposy((Double.parseDouble((String)objMap.get("rpposy"))));
			    	ur.setRpuserid(Integer.parseInt((String)objMap.get("rpuserid")));
			    	ur.setRpgroupid(Integer.parseInt((String)objMap.get("rpgroupid")));
					    	//System.out.println("333"+ur.toString());
					    	
					services.addTUserLocateReport( ur ) ;
						   /* Map<String,Object>  map =new HashMap<String, Object>();
							map.put("result",0);	*/
					client.close() ;
			}
		} catch (Exception e) {
			System.out.println("出现异常1" + e.getMessage() );
			System.out.println("出现异常1--------------------------------------------------------------");
			
		}
	}
	
	
}




}