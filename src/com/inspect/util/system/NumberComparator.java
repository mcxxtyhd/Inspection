package com.inspect.util.system;

import java.util.Comparator;

import com.inspect.model.system.TSFunction;

public class NumberComparator implements Comparator<Object> {

	private boolean ignoreCase;

	  public NumberComparator()
	  {
	    this.ignoreCase = true;
	  }

	  public NumberComparator(boolean ignoreCase)
	  {
	    this.ignoreCase = true;
	    this.ignoreCase = ignoreCase;
	  }

	  public int compare(Object obj1, Object obj2)
	  {
	    String o1 = "";
	    String o2 = "";
	    if (this.ignoreCase){
	      TSFunction c1 = (TSFunction)obj1;
	      TSFunction c2 = (TSFunction)obj2;
	      o1 = c1.getFunctionOrder();
	      o2 = c2.getFunctionOrder();
	    }
	    if ((o1 != null) && (o2 != null)){
	    	 for (int i = 0; i < o1.length(); ++i)
		      {
		        if ((i == o1.length()) && (i < o2.length()))
		          return -1;
		        if ((i == o2.length()) && (i < o1.length()))
		          return 1;
		        char ch1 = o1.charAt(i);
		        char ch2 = o2.charAt(i);
		        if ((ch1 >= '0') && (ch2 <= '9'))
		        {
		          int i1 = getNumber(o1.substring(i));
		          int i2 = getNumber(o2.substring(i));
		          if (i1 == i2)
		            continue;
		          return (i1 - i2);
		        }
		        if (ch1 != ch2)
		          return (ch1 - ch2);
		      }
	    }
	     
	    return 0;
	  }

	  private int getNumber(String str)
	  {
	    int num = 2147483647;
	    int bits = 0;
	    for (int i = 0; i < str.length(); ++i)
	    {
	      if ((str.charAt(i) >= '0') && (str.charAt(i) <= '9'))
//	        break;
	      ++bits;
	      continue;
//	      break;
	    }
	    if (bits > 0)
	      num = Integer.parseInt(str.substring(0, bits));
	    return num;
	  }
}
