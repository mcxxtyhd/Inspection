package com.inspect.vo.basis;

import java.util.ArrayList;
import java.util.List;

public class CascodeVo  {
	private List<GroupVo> glist=new ArrayList<GroupVo>();
	
	private List<LineVo>  llist =new ArrayList<LineVo>();
	
	List<TPlanTaskVo> tlist=new ArrayList();
	
	private int flag=0;  //0  总公司需要修改 1 分公司，不用修改
	
	
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public List<GroupVo> getGlist() {
		return glist;
	}
	public void setGlist(List<GroupVo> glist) {
		this.glist = glist;
	}
	public List<LineVo> getLlist() {
		return llist;
	}
	public void setLlist(List<LineVo> llist) {
		this.llist = llist;
	}
	public List<TPlanTaskVo> getTlist() {
		return tlist;
	}
	public void setTlist(List<TPlanTaskVo> tlist) {
		this.tlist = tlist;
	}
	
}
