package com.inspect.model.basis;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;

import com.inspect.model.BaseModel;
/**
 * 
 * @author liao
 */
@Entity
@Table(name="t_summary_config")
public class TSummaryConfig extends BaseModel  implements Serializable{

	private static final long serialVersionUID = 8518113509653055912L;
	
    private String sname;
    private String scell;
    private int flag;//1表示铁塔总账，2表示室内总账，3表示铁塔详情，4表示室内详情
    
    
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
	public String getScell() {
		return scell;
	}
	public void setScell(String scell) {
		this.scell = scell;
	}

    
}
