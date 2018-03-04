package com.inspect.model.basis;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 巡检线路与巡检点关联实体类
 * @author wzs
 */
@Entity
@Table(name="t_line_point")
public class TLinePoint implements Serializable {

	private static final long serialVersionUID = 3189673412556820673L;

	private int id;
	private TLine tline;
	private TPoint tpoint;
	private int lporder;

	public TLinePoint() {
	}

	public TLinePoint(int id) {
		this.id = id;
	}
	
	public TLinePoint(int id, TLine tline, TPoint tpoint, int lporder) {
		this.id = id;
		this.tline = tline;
		this.tpoint = tpoint;
		this.lporder = lporder;
	}

	@Id
	@GeneratedValue
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="lineid")
	public TLine getTline() {
		return tline;
	}

	public void setTline(TLine tline) {
		this.tline = tline;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="pointid")
	public TPoint getTpoint() {
		return tpoint;
	}

	public void setTpoint(TPoint tpoint) {
		this.tpoint = tpoint;
	}

	public int getLporder() {
		return lporder;
	}

	public void setLporder(int lporder) {
		this.lporder = lporder;
	}
}
