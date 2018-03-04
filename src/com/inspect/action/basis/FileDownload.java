package com.inspect.action.basis;

 

	import java.io.InputStream;


	import org.apache.struts2.ServletActionContext;


	import com.opensymphony.xwork2.ActionSupport;

	public class FileDownload extends ActionSupport{
		

		private String fileName;


		
		public String getFileName() {
			return fileName;
		}

		public void setFileName(String fileName) {
			this.fileName = fileName;
		}

		public InputStream getDownloadFile() throws Exception
		{
		 
			   this.fileName = "Forms.zip" ;
			    
			   return ServletActionContext.getServletContext().getResourceAsStream("表单.zip") ;
			 
		}
		
		@Override
		public String execute() throws Exception {
			
			return SUCCESS;
		}

	}
