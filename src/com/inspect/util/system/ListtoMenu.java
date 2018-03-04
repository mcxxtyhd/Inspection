package com.inspect.util.system;

import java.util.Iterator;
import java.util.List;

import com.inspect.model.system.TSFunction;

public class ListtoMenu {
	static int count = 0;

	@SuppressWarnings("unchecked")
	public static String getMenu(List<TSFunction> set, List<TSFunction> set1){
	    StringBuffer buffer = new StringBuffer();
	    buffer.append("{\"menus\":[");
	    Iterator localIterator = set.iterator();
	    while (localIterator.hasNext()){
	      TSFunction node = (TSFunction)localIterator.next();
	      String iconClas = "default";
	      if (node.getTSIcon() != null){
	    	  iconClas = node.getTSIcon().getIconClas(); 
	      }
	      buffer.append("{\"menuid\":\"" + node.getId() + "\",\"icon\":\"" + iconClas + "\"," + "\"menuname\":\"" + node.getFunctionName() + "\",\"menus\":[");
	      iterGet(set1, String.valueOf(node.getId()), buffer);
	      buffer.append("]},");
	    }
	    buffer.append("]}");
	    String tmp = buffer.toString();
	    tmp = tmp.replaceAll(",\n]", "\n]");
	    tmp = tmp.replaceAll(",]}", "]}");
	    return tmp;
	  }

	  @SuppressWarnings("unchecked")
	static void iterGet(List<TSFunction> set1, String pid, StringBuffer buffer)
	  {
	    Iterator localIterator = set1.iterator();
	    while (localIterator.hasNext())
	    {
	      TSFunction node = (TSFunction)localIterator.next();
	      count = count + 1;
	      if (node.getTSFunction().getId().equals(pid))
	      {
	        buffer.append("{\"menuid\":\"" + node.getId() + " \",\"icon\":\"" + node.getTSIcon().getIconClas() + "\"," + "\"menuname\":\"" + node.getFunctionName() + "\",\"url\":\"" + node.getFunctionUrl() + "\"");
	        if (count == set1.size())
	          buffer.append("}\n");
	        buffer.append("},\n");
	      }
	    }
	  }

	  @SuppressWarnings("unchecked")
	public static String getBootMenu(List<TSFunction> pFunctions, List<TSFunction> functions)
	  {
	    StringBuffer menuString = new StringBuffer();
	    menuString.append("<ul>");
	    Iterator localIterator1 = pFunctions.iterator();
	    while (localIterator1.hasNext())
	    {
	      TSFunction pFunction = (TSFunction)localIterator1.next();
	      menuString.append("<li><a href=\"#\"><span class=\"icon16 icomoon-icon-stats-up\"></span><b>" + pFunction.getFunctionName() + "</b></a>");
	      int submenusize = pFunction.getTSFunctions().size();
	      if (submenusize == 0)
	        menuString.append("</li>");
	      if (submenusize > 0)
	        menuString.append("<ul class=\"sub\">");
	      Iterator localIterator2 = functions.iterator();
	      while (localIterator2.hasNext())
	      {
	        TSFunction function = (TSFunction)localIterator2.next();
	        if (function.getTSFunction().getId().equals(pFunction.getId()))
	          menuString.append("<li><a href=\"" + function.getFunctionUrl() + "\" target=\"contentiframe\"><span class=\"icon16 icomoon-icon-file\"></span>" + function.getFunctionName() + "</a></li>");
	      }
	      if (submenusize > 0)
	        menuString.append("</ul></li>");
	    }
	    menuString.append("</ul>");
	    return menuString.toString();
	  }

	  @SuppressWarnings("unchecked")
	public static String getEasyuiMenu(List<TSFunction> pFunctions, List<TSFunction> functions)
	  {
	    StringBuffer menuString = new StringBuffer();
	    Iterator localIterator1 = pFunctions.iterator();
	    while (localIterator1.hasNext())
	    {
	      TSFunction pFunction = (TSFunction)localIterator1.next();
	      menuString.append("<div  title=\"" + pFunction.getFunctionName() + "\" iconCls=\"" + pFunction.getTSIcon().getIconClas() + "\">");
	      int submenusize = pFunction.getTSFunctions().size();
	      if (submenusize == 0)
	        menuString.append("</div>");
	      if (submenusize > 0)
	        menuString.append("<ul>");
	      Iterator localIterator2 = functions.iterator();
	      while (localIterator2.hasNext())
	      {
	        TSFunction function = (TSFunction)localIterator2.next();
	        if (function.getTSFunction().getId().equals(pFunction.getId()))
	        {
	          String icon = "folder";
	          if (function.getTSIcon() != null)
	            icon = function.getTSIcon().getIconClas();
	          menuString.append("<li><div onclick=\"addTab('" + function.getFunctionName() + "','" + function.getFunctionUrl() + "','" + icon + "')\"  title=\"" + function.getFunctionName() + "\" url=\"" + function.getFunctionUrl() + "\" iconCls=\"" + icon + "\"> <a class=\"" + function.getFunctionName() + "\" href=\"#\" > <span class=\"icon " + icon + "\" >&nbsp;</span> <span class=\"nav\" >" + function.getFunctionName() + "</span></a></div></li>");
	        }
	      }
	      if (submenusize > 0)
	        menuString.append("</ul></div>");
	    }
	    return menuString.toString();
	  }
	}
