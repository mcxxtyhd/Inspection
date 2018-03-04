package com.inspect.model.basis;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.inspect.model.BaseModel;

/**
 * 巡检点实体类
 * @author wzs
 */
@Entity
@Table(name="t_station")
public class TStation extends BaseModel implements Serializable {
	private static final long serialVersionUID = -530285980180365720L;
	
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
	private String stpic;// 图片路径


	public String getStpic() {
		return stpic;
	}

	public void setStpic(String stpic) {
		this.stpic = stpic;
	}

	public TStation() {
	}

	public TStation(String stnumber, String stname) {
		this.stnumber = stnumber;
		this.stname = stname;
	}

	public TStation(String stnumber,String stname,Double stantenna,Double sttower, Double stposx,Double stposy,
			String staddress,String stdate, String stnet,String stfre,String stshare,String stprocedure,String stvalidaty,String stpic) {
		
		this.stnumber = stnumber ; // 编号
		this.stname =stname; // 名称
		this.stantenna =stantenna; // 天线挂高
		this.sttower= sttower; // 铁塔高度
		this.stposx =stposx; // 经度
		this.stposy =stposy; // 纬度
		this.staddress= staddress;// 城市坐标
		this.stdate =stdate; // 基站建设时间
		this.stnet =stnet; // 网络信息
		this.stfre =stfre; // 频率信息
		this.stshare =stshare;// 共建共享
		this.stprocedure= stprocedure; //手续办理
		this.stvalidaty =stvalidaty;// 合法性
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
}
