package com.inspect.vo.basis;

import java.io.File;
import java.io.Serializable;

import com.inspect.vo.comon.BaseVo;

public class TBaseInfoVo extends  BaseVo implements Serializable {

	private static final long serialVersionUID = -4939100211560989647L;
	private String btype;   //基础设备类型
	private String bnumber;  //编号
	private String bname;   // 名称
	private String bcity; //地市
	private String bregion;//区县
	private String baddress;//地址
	private double bposx;     //经度
	private double bposy;    //纬度
	private String btowertype;//铁塔类型
	private String btower;  //自建（共享）塔
	private String bfactory; // 集成厂家 
	private String blevel; //基站维护等级
	private String beqcount; // 有源设备数量
	private String bwlnumber;//物理编号
	private String bdesc;  // 描述
	private  File   excelFile;
	private double bheight;
	private String excelFileFileName;
	
	private  String   excelFileContentType;
	
	

	public double getBheight() {
		return bheight;
	}
	public void setBheight(double bheight) {
		this.bheight = bheight;
	}
	public File getExcelFile() {
		return excelFile;
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
	public void setExcelFile(File excelFile) {
		this.excelFile = excelFile;
	}
	public String getBtowertype() {
		return btowertype;
	}
	public String getBcity() {
		return bcity;
	}
	public void setBcity(String bcity) {
		this.bcity = bcity;
	}
	public String getBwlnumber() {
		return bwlnumber;
	}
	public void setBwlnumber(String bwlnumber) {
		this.bwlnumber = bwlnumber;
	}
	public void setBtowertype(String btowertype) {
		this.btowertype = btowertype;
	}
	public String getBnumber() {
		return bnumber;
	}
	public void setBnumber(String bnumber) {
		this.bnumber = bnumber;
	}
	public String getBname() {
		return bname;
	}
	public void setBname(String bname) {
		this.bname = bname;
	}
	public String getBregion() {
		return bregion;
	}
	public void setBregion(String bregion) {
		this.bregion = bregion;
	}
	public String getBaddress() {
		return baddress;
	}
	public void setBaddress(String baddress) {
		this.baddress = baddress;
	}
	public double getBposx() {
		return bposx;
	}
	public void setBposx(double bposx) {
		this.bposx = bposx;
	}
	public double getBposy() {
		return bposy;
	}
	public void setBposy(double bposy) {
		this.bposy = bposy;
	}
	public String getBtype() {
		return btype;
	}
	public void setBtype(String btype) {
		this.btype = btype;
	}
	public String getBfactory() {
		return bfactory;
	}
	public void setBfactory(String bfactory) {
		this.bfactory = bfactory;
	}
	public String getBlevel() {
		return blevel;
	}
	public void setBlevel(String blevel) {
		this.blevel = blevel;
	}
	public String getBeqcount() {
		return beqcount;
	}
	public void setBeqcount(String beqcount) {
		this.beqcount = beqcount;
	}
	public String getBtower() {
		return btower;
	}
	public void setBtower(String btower) {
		this.btower = btower;
	}
	public String getBdesc() {
		return bdesc;
	}
	public void setBdesc(String bdesc) {
		this.bdesc = bdesc;
	}
}
