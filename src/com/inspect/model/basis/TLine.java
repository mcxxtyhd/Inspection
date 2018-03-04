package com.inspect.model.basis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.inspect.model.BaseModel;


/**
 * 巡检线路实体类
 * @author wzs
 */
@Entity
@Table(name="t_line")
public class TLine extends BaseModel implements Serializable {

	private static final long serialVersionUID = 7830436249652837797L;
	
	private String lnumber; //线路编号
	private String lname;	//线路名称
	private String ldesc;	//线路描述
	private int lorder;	//线路顺序  0:顺序 1:无序
//	private Set<TLinePoint> linepoints=new HashSet<TLinePoint>(); //线路巡检点集合
	private List<TLinePoint> linepoints=new ArrayList<TLinePoint>(); //线路巡检点集合
	
	public TLine() {
	}
	
	public TLine(String lnumber, String lname) {
		this.lnumber = lnumber;
		this.lname = lname;
	}
	public TLine(String lnumber, String lname, String ldesc, int lorder,List<TLinePoint> linepoints) {
		super();
		this.lnumber = lnumber;
		this.lname = lname;
		this.ldesc = ldesc;
		this.lorder = lorder;
		this.linepoints = linepoints;
	}
	public String getLnumber() {
		return lnumber;
	}
	public void setLnumber(String lnumber) {
		this.lnumber = lnumber;
	}
	public String getLname() {
		return lname;
	}
	public void setLname(String lname) {
		this.lname = lname;
	}
	public String getLdesc() {
		return ldesc;
	}
	public void setLdesc(String ldesc) {
		this.ldesc = ldesc;
	}
	public int getLorder() {
		return lorder;
	}
	public void setLorder(int lorder) {
		this.lorder = lorder;
	}
	
	/*@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY,mappedBy="tline")
	public Set<TLinePoint> getLinepoints() {
		return linepoints;
	}

	public void setLinepoints(Set<TLinePoint> linepoints) {
		this.linepoints = linepoints;
	}*/
	
	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY,mappedBy="tline")
	public List<TLinePoint> getLinepoints() {
		return linepoints;
	}

	public void setLinepoints(List<TLinePoint> linepoints) {
		this.linepoints = linepoints;
	}
	public String toString(){
		return "getEntid()="+this.getEntid()+" Ldesc="+this.getLdesc()+" Lname ="+this.getLname()+"  Lnumber="+this.getLnumber()
		        +"   Lorder ="+this.getLorder()+"   Linepoints="+this.getLinepoints();    
	}
}
