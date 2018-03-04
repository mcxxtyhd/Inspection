package com.inspect.model.basis;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.inspect.model.BaseModel;

@Entity
@Table(name = "t_terminal")
public class TTerminal extends BaseModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1167131543898634777L;

	private String termno;// 终端编号
	private String tname; // 终端名称
	private String tmodel; // 终端型号
	private String ttype; // 终端类型
	private String ttelnumber; // 终端号码
	private String tvendor;		//终端厂商
	private String tnote;		//备注

	public TTerminal() {
	}
	


	public TTerminal(String termno, String tname, String tmodel, String ttype,
			String ttelnumber, String tvendor, String tnote) {
		super();
		this.termno = termno;
		this.tname = tname;
		this.tmodel = tmodel;
		this.ttype = ttype;
		this.ttelnumber = ttelnumber;
		this.tvendor = tvendor;
		this.tnote = tnote;
	}



	public String getTermno() {
		return termno;
	}

	public void setTermno(String termno) {
		this.termno = termno;
	}

	public String getTname() {
		return tname;
	}

	public void setTname(String tname) {
		this.tname = tname;
	}

	public String getTmodel() {
		return tmodel;
	}

	public void setTmodel(String tmodel) {
		this.tmodel = tmodel;
	}

	public String getTtype() {
		return ttype;
	}

	public void setTtype(String ttype) {
		this.ttype = ttype;
	}



	public String getTtelnumber() {
		return ttelnumber;
	}



	public void setTtelnumber(String ttelnumber) {
		this.ttelnumber = ttelnumber;
	}



	public String getTvendor() {
		return tvendor;
	}

	public void setTvendor(String tvendor) {
		this.tvendor = tvendor;
	}

	public String getTnote() {
		return tnote;
	}

	public void setTnote(String tnote) {
		this.tnote = tnote;
	}

	

}
