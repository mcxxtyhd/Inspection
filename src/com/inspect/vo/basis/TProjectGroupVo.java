package com.inspect.vo.basis;

import com.inspect.vo.comon.BaseVo;

/**
 * 巡检项组信息实体类
 * @author wzs
 */

public class TProjectGroupVo  extends BaseVo  {

	private String pgname;//巡检项组名称
	private String pgdesc;//巡检项组描述
private String pcomname; //公司名称
	
	public String getPcomname() {
		return pcomname;
	}
	public void setPcomname(String pcomname) {
		this.pcomname = pcomname;
	}
	public String getPgname() {
		return pgname;
	}
	public void setPgname(String pgname) {
		this.pgname = pgname;
	}
	public String getPgdesc() {
		return pgdesc;
	}
	public void setPgdesc(String pgdesc) {
		this.pgdesc = pgdesc;
	}
	

}
