package com.inspect.util.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.inspect.action.webservice.PositionTcpServer;

public class OnlineListener implements ServletContextListener{

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		//System.out.println( "系统销毁" );
		PositionTcpServer.getInstance().endService();
		
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		//System.out.println( "系统启动" );
		new Thread(){
			public void run(){
				PositionTcpServer.getInstance().startService();
			}
		}.start() ;
	}

}
