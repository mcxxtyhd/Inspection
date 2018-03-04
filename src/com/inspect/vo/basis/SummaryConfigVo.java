package com.inspect.vo.basis;

import java.io.Serializable;

import com.inspect.vo.comon.BaseVo;

public class SummaryConfigVo extends BaseVo implements Serializable{
	private static final long serialVersionUID = 8518113509653055922L;
	
    private String sname;
    private int scell;
    private int flag;//判断铁塔或室外
    
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public String getSname() {
		return sname;
	}
	public void setSname(String sname) {
		this.sname = sname;
	}
	public int getScell() {
		return scell;
	}
	public void setScell(int scell) {
		this.scell = scell;
	}
}
