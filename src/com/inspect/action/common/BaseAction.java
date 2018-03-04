package com.inspect.action.common;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.alibaba.fastjson.JSON;
import com.inspect.model.system.TSRole;
import com.inspect.model.system.TSRoleUser;
import com.inspect.model.system.TSUser;
import com.inspect.service.SystemServiceI;
import com.inspect.vo.comon.SessionInfo;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

/**O
 * 基础ACTION,其他ACTION继承此ACTION来获得ActionSupport的功能
 */
public class BaseAction extends ActionSupport implements Preparable {
	
	private static final long serialVersionUID = 1L;
	
	public void prepare() throws Exception {}
	
	public HttpServletRequest getRequest() {
		return ServletActionContext.getRequest();
	}
	
	public HttpServletResponse getResponse() {
		return ServletActionContext.getResponse();
	}

	public HttpSession getSession() {
		return getRequest().getSession();
	}
	//操作标志
	private int operstatus=0;
	private int entid;
	private String operater;
	
	
	public int getEntid() {
		return entid;
	}

	public void setEntid(int entid) {
		this.entid = entid;
	}

	public String getOperater() {
		return operater;
	}

	public void setOperater(String operater) {
		this.operater = operater;
	}

	public int getOperstatus() {
		return operstatus;
	}

	protected void setOperstatus(int operstatus) {
		this.operstatus = operstatus;
	}

	/**
	 * 将对象转换成JSON字符串，并响应回前台
	 * @param object
	 */
	public void writeJson(Object object) {
		try {
			String json = JSON.toJSONStringWithDateFormat(object, "yyyy-MM-dd HH:mm:ss");
			//System.out.println(json);
			ServletActionContext.getResponse().setContentType("text/html;charset=utf-8");
			ServletActionContext.getResponse().getWriter().write(json);
			ServletActionContext.getResponse().getWriter().flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 根据不同浏览器将文件名中的汉字转为UTF8编码的串,以便下载时能正确显示另存的文件名.
	 * 
	 * @param s
	 *            原文件名
	 * @return 重新编码后的文件名
	 */
	public String toUtf8String(HttpServletRequest request, String s) {
		String agent = request.getHeader("User-Agent");
		try {
			boolean isFireFox = (agent != null && agent.toLowerCase().indexOf(
					"firefox") != -1);
			if (isFireFox) {
				s = new String(s.getBytes("UTF-8"), "ISO8859-1");
			} else {
				s = toUtf8String(s);
				if ((agent != null && agent.indexOf("MSIE") != -1)) {
					// see http://support.microsoft.com/default.aspx?kbid=816868
					if (s.length() > 150) {
						// 根据request的locale 得出可能的编码
						s = new String(s.getBytes("UTF-8"), "ISO8859-1");
					}
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return s;
	}
	public String toUtf8String(String s) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c >= 0 && c <= 255) {
				sb.append(c);
			} else {
				byte[] b;
				try {
					b = Character.toString(c).getBytes("utf-8");
				} catch (Exception ex) {
					//exceptionUtil.error( "将文件名中的汉字转为UTF8编码的串时错误，输入的字符串为：" + s );
					b = new byte[0];
				}
				for (int j = 0; j < b.length; j++) {
					int k = b[j];
					if (k < 0)
						k += 256;
					sb.append("%" + Integer.toHexString(k).toUpperCase());
				}
			}
		}
		return sb.toString();
	}
	/**
	 * 输出JSONObject对象
	 * @param jsonobject
	 * @throws IOException
	 */
	public String outData(JSONObject jsonobject) throws IOException {
		PrintWriter out = getWebWriter();
		out.write(jsonobject.toString());
		out.flush();
		out.close();
		return NONE;
	}
	
	protected static PrintWriter getWebWriter() throws IOException {
		ServletActionContext.getResponse().setContentType("text/html;charset=utf-8");
		ServletActionContext.getResponse().setHeader("Cache-Control", "no-cache");
		ServletActionContext.getResponse().setHeader("Pragma", "no-cache");
		ServletActionContext.getResponse().setDateHeader("Expires", 0);
		ServletActionContext.getResponse().setStatus(200);
		return ServletActionContext.getResponse().getWriter();
	}
	
	// 获取SessionInfo用户信息  tsuser系统用户实体类
	public final TSUser getSessionUserName() {
		HttpSession session = getRequest().getSession();
		session.setMaxInactiveInterval(-1);// 设置session 永远不会失效
		if (session.getAttributeNames().hasMoreElements()) {
			SessionInfo sessionInfo1 = (SessionInfo) session.getAttribute("USER_SESSION");
			if (sessionInfo1 != null) {
				return sessionInfo1.getUser();
			} else {
				return null;
			}
		}
		return null;
	}
	//返回标志位
	public  int queryEnterpriseByWhere(){
		TSUser user = this.getSessionUserName();
		int roleid = 0;
		int entterpriseid=0;  //标志位
		if(user!=null){
			SystemServiceI service = (SystemServiceI) WebApplicationContextUtils.getWebApplicationContext(ServletActionContext.getServletContext()).getBean("systemService");
			List<TSRoleUser> rUsers = service.findByProperty(TSRoleUser.class, "TSUser.id",user.getId());
			
			//在tsrole中查找tsuser.id =user.getId()的tsroleuser
			for (TSRoleUser ru : rUsers) {
				TSRole role = ru.getTSRole();
				roleid= role.getId();
			}
		}
        if(roleid==1){//系统管理员 查看所有
        	entterpriseid=0;
        }else{
        	entterpriseid=user.getEntid();//用户查看所属单位
        }
		return entterpriseid;
	}
	/**
	 * 本公司和总公司信息
	 * @return
	 */
	public String querySql(){
		StringBuffer buf=new StringBuffer();
		int entid=queryEnterpriseByWhere();
		if(entid==0){
			buf.append(" 1=1");
		}else{
			buf.append(" entid in (0,").append(entid).append(")");
		}
		return buf.toString();
	}
	/**
	 * 要么本公司信息，要么总公司信息
	 * @return
	 */
	public String querySql1(){
		StringBuffer buf=new StringBuffer();
		int entid=queryEnterpriseByWhere();
		if(entid==0){
			buf.append(" 1=1");
		}else{
			buf.append(" entid =").append(entid);
		}
		return buf.toString();
	}
}
