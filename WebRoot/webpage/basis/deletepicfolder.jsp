<%@ page
 contentType="text/html;charset=gbk"
 import="java.io.*,javax.servlet.*,com.inspect.action.basis.MyConverter,com.inspect.action.basis.DB,java.util.*"
 %>

   
  <%  
  System.out.println("deletefolderOkKKKKKKKKKKKK");
  
  String stpic=request.getParameter("stpic").trim();
  System.out.println("-->"+stpic);
  //File file=new File("C:\\Program Files (x86)\\Apache Software Foundation\\Tomcat 6.0\\webapps\\inspection\\image\\"+stpic);
  File file=new File("C:\\Program Files (x86)\\Apache Software Foundation\\Tomcat 6.0\\webapps\\inspection\\image\\"+stpic);
  
    StringBuffer sb=new StringBuffer();   
     
      if(file.exists())
      {                    //�ж��ļ��Ƿ����
		    if(file.isFile()){                    //�ж��Ƿ����ļ�
		     file.delete();                       //delete()���� ��Ӧ��֪�� ��ɾ������˼;
		    }
		    else if(file.isDirectory())
		    {              //�����������һ��Ŀ¼
		     File files[] = file.listFiles();               //����Ŀ¼�����е��ļ� files[];
		     for(int i=0;i<files.length;i++){            //����Ŀ¼�����е��ļ�
		      files[i].delete();             //��ÿ���ļ� ������������е���
		     } 
		    } 
		    file.delete(); 
		    
		    sb.append("OK|OK|");
		    System.out.println("ɾ��ͼƬ�ļ��гɹ���");
		    
   	 }
     else{ 
            sb.append("NO|NO|");
     	    System.out.println("��ɾ�����ļ������ڣ�"); 
     } 
   
   
      
/*    if(file.exists()){
    boolean d = file.delete();

    if(d){
     System.out.print("ɾ��ͼƬ�ļ��гɹ���");
         sb.append("OK|OK|");
    }else{
     System.out.print("ɾ��ͼƬ�ļ��гɹ�����");
            sb.append("NG|NG|");
    }
   } 
   else{
    System.out.print("ɾ��ͼƬ�ļ��в����ڣ�");
         sb.append("NO|NO|");
   
   }
 */

	  String s=sb.toString();
	  out.println(MyConverter.escape(s));

%>