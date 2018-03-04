package com.inspect.vo.basis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.inspect.vo.comon.BaseVo;

public class StationVo extends BaseVo implements Serializable {
	private static final long serialVersionUID = 1833653540349756427L;
	private String stnumber; // 编号
	private String stname; // 名称
	private Double stantenna; // 天线挂高
	private Double sttower; // 铁塔高度
	private Double stposx; // 经度
	private Double stposy; // 纬度
	private String staddress;// 城市坐标
	private String stdate; // 基站建设时间
	private String stnet; // 网络信息
	private String stfre; // 频率信息
	private String stshare;// 共建共享
	private String stprocedure; //手续办理
	private String stvalidaty;// 合法性
	private String stpic;// 合法性
	public String getStpic() {
		return stpic;
	}
	public void setStpic(String stpic) {
		this.stpic = stpic;
	}
	public String getStnumber() {
		return stnumber;
	}
	public void setStnumber(String stnumber) {
		this.stnumber = stnumber;
	}
	public String getStname() {
		return stname;
	}
	public void setStname(String stname) {
		this.stname = stname;
	}
	public Double getStantenna() {
		return stantenna;
	}
	public void setStantenna(Double stantenna) {
		this.stantenna = stantenna;
	}
	public Double getSttower() {
		return sttower;
	}
	public void setSttower(Double sttower) {
		this.sttower = sttower;
	}
	public Double getStposx() {
		return stposx;
	}
	public void setStposx(Double stposx) {
		this.stposx = stposx;
	}
	public Double getStposy() {
		return stposy;
	}
	public void setStposy(Double stposy) {
		this.stposy = stposy;
	}
	public String getStaddress() {
		return staddress;
	}
	public void setStaddress(String staddress) {
		this.staddress = staddress;
	}
	public String getStdate() {
		return stdate;
	}
	public void setStdate(String stdate) {
		this.stdate = stdate;
	}
	public String getStnet() {
		return stnet;
	}
	public void setStnet(String stnet) {
		this.stnet = stnet;
	}
	public String getStfre() {
		return stfre;
	}
	public void setStfre(String stfre) {
		this.stfre = stfre;
	}
	public String getStshare() {
		return stshare;
	}
	public void setStshare(String stshare) {
		this.stshare = stshare;
	}
	public String getStprocedure() {
		return stprocedure;
	}
	public void setStprocedure(String stprocedure) {
		this.stprocedure = stprocedure;
	}
	public String getStvalidaty() {
		return stvalidaty;
	}
	public void setStvalidaty(String stvalidaty) {
		this.stvalidaty = stvalidaty;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	
}
