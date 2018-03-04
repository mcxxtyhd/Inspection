package com.inspect.vo.basis;
import java.util.Comparator;

public class ComparatorPlanQueryVo implements Comparator{

 public int compare(Object arg0, Object arg1) {
	 PlanQueryVo pro=(PlanQueryVo)arg0;
	 PlanQueryVo pro1=(PlanQueryVo)arg1;
	 // return (pro1.getComrate()<pro.getComrate() ? -1 : (pro1.getComrate()==pro.getComrate() ? 0 : 1));
	 return (pro1.getTotrate()<pro.getTotrate() ? -1 : (pro1.getTotrate()==pro.getTotrate() ? 0 : 1));
		
 
 }
 
}

