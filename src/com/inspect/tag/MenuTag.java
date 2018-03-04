package com.inspect.tag;

import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import com.inspect.model.system.TSFunction;
import com.inspect.util.system.ListtoMenu;

public class MenuTag extends TagSupport {
	  private static final long serialVersionUID = 5768191573897192226L;
	  protected String style;
	  protected List<TSFunction> parentFun;
	  protected List<TSFunction> childFun;

	  public MenuTag(){
	    this.style = "easyui";
	  }

	  public void setParentFun(List<TSFunction> parentFun){
	    this.parentFun = parentFun;
	  }

	  public void setChildFun(List<TSFunction> childFun){
	    this.childFun = childFun;
	  }

	  public int doStartTag()throws JspTagException{
	    return 6;
	  }

	  public int doEndTag()throws JspTagException{
	    JspWriter out;
	    try
	    {
	      out = this.pageContext.getOut();
	      out.print(end().toString());
	    }
	    catch (IOException e)
	    {
	      e.printStackTrace();
	    }
	    return 6;
	  }

	  public StringBuffer end(){
	    StringBuffer sb = new StringBuffer();
	    if (this.style.equals("easyui"))
	    {
	      sb.append("<div id=\"nav\" class=\"easyui-accordion\" fit=\"true\" border=\"false\">");
	      sb.append(ListtoMenu.getEasyuiMenu(this.parentFun, this.childFun));
	      sb.append("</div>");
	    }
	    if (this.style.equals("bootstrap"))
	      sb.append(ListtoMenu.getBootMenu(this.parentFun, this.childFun));
	    if (this.style.equals("json"))
	    {
	      sb.append("<script type=\"text/javascript\">");
	      sb.append("var _menus=" + ListtoMenu.getMenu(this.parentFun, this.childFun));
	      sb.append("</script>");
	    }
	    return sb;
	  }

	  public void setStyle(String style)
	  {
	    this.style = style;
	  }
	}