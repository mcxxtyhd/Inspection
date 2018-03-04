package com.inspect.vo.basis;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.inspect.vo.comon.BaseVo;

public class LineVo extends BaseVo implements Serializable {

	private static final long serialVersionUID = 6741174509097942820L;

	private String lnumber; // 线路编号
	private String lname; // 线路名称
	private String ldesc; // 线路描述
	private int lorder; // 线路顺序 0:顺序 1:无序
	private String lpIds; // 巡检点ID
	private String lpNames;// 巡检点名称
	private List<PointVo> pointList=new ArrayList<PointVo>(); //线路巡检点集合
	private  File   excelFile;
	private String excelFileFileName;
	private  String   excelFileContentType;
	
	
	public File getExcelFile() {
		return excelFile;
	}

	public void setExcelFile(File excelFile) {
		this.excelFile = excelFile;
	}

	public String getExcelFileFileName() {
		return excelFileFileName;
	}

	public void setExcelFileFileName(String excelFileFileName) {
		this.excelFileFileName = excelFileFileName;
	}

	public String getExcelFileContentType() {
		return excelFileContentType;
	}

	public void setExcelFileContentType(String excelFileContentType) {
		this.excelFileContentType = excelFileContentType;
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

	public String getLpIds() {
		return lpIds;
	}

	public void setLpIds(String lpIds) {
		this.lpIds = lpIds;
	}

	public String getLpNames() {
		return lpNames;
	}

	public void setLpNames(String lpNames) {
		this.lpNames = lpNames;
	}

	public List<PointVo> getPointList() {
		return pointList;
	}

	public void setPointList(List<PointVo> pointList) {
		this.pointList = pointList;
	}


	

	
}
