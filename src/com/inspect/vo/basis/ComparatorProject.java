package com.inspect.vo.basis;
import java.util.Comparator;

import com.inspect.model.basis.TProject;

public class ComparatorProject implements Comparator{

 public int compare(Object arg0, Object arg1) {
	 TermProjectVo pro=(TermProjectVo)arg0;
	 TermProjectVo pro1=(TermProjectVo)arg1;
	  return (pro1.getId()<pro.getId() ? -1 : (pro1.getId()==pro.getId() ? 0 : 1));
	 }
 
}

