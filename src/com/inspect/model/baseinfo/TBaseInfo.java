package com.inspect.model.baseinfo;

import java.io.Serializable;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.inspect.model.BaseModel;
/**
 * 基础信息实体类
 */
@Entity
@Table(name="t_base_info")
public class TBaseInfo extends BaseModel implements Serializable {

	private static final long serialVersionUID = -4939100211560989647L;
	private String btype;   //基础设备类型
	private String bnumber;  //编号
	private String bname;   // 名称
	private String bcity; //城市
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
	private double bheight;
	private Set<TBaseInfoEquipment> baseinfoequipments=new HashSet<TBaseInfoEquipment>(0);
	public TBaseInfo() {
		super();
	}
	public TBaseInfo(String bnumber, String bname) {
		super();
		this.bnumber = bnumber;
		this.bname = bname;
	}
	
	public TBaseInfo(String btype, String bnumber, String bname, String bcity, String bregion, String baddress, double bposx, double bposy,double bheight, String btowertype, String btower, String bfactory, String blevel, String beqcount, String bwlnumber, String bdesc, Set<TBaseInfoEquipment> baseinfoequipments) {
		super();
		this.btype = btype;
		this.bnumber = bnumber;
		this.bname = bname;
		this.bcity = bcity;
		this.bregion = bregion;
		this.baddress = baddress;
		this.bposx = bposx;
		this.bposy = bposy;
		this.btowertype = btowertype;
		this.btower = btower;
		this.bfactory = bfactory;
		this.blevel = blevel;
		this.beqcount = beqcount;
		this.bwlnumber = bwlnumber;
		this.bdesc = bdesc;
		this.bheight = bheight;
		this.baseinfoequipments = baseinfoequipments;
	}
	

	public double getBheight() {
		return bheight;
	}
	public void setBheight(double bheight) {
		this.bheight = bheight;
	}
	@Column(length=40)
	public String getBtype() {
		return btype;
	}
	public void setBtype(String btype) {
		this.btype = btype;
	}
	@Column(length=40)
	public String getBnumber() {
		return bnumber;
	}
	public void setBnumber(String bnumber) {
		this.bnumber = bnumber;
	}
	@Column(length=100)
	public String getBname() {
		return bname;
	}
	public void setBname(String bname) {
		this.bname = bname;
	}
	@Column(length=50)
	public String getBcity() {
		return bcity;
	}
	public void setBcity(String bcity) {
		this.bcity = bcity;
	}
	@Column(length=50)
	public String getBregion() {
		return bregion;
	}
	public void setBregion(String bregion) {
		this.bregion = bregion;
	}
	@Column(length=150)
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
	@Column(length=40)
	public String getBtowertype() {
		return btowertype;
	}
	public void setBtowertype(String btowertype) {
		this.btowertype = btowertype;
	}
	@Column(length=40)
	public String getBfactory() {
		return bfactory;
	}
	public void setBfactory(String bfactory) {
		this.bfactory = bfactory;
	}
	@Column(length=40)
	public String getBlevel() {
		return blevel;
	}
	public void setBlevel(String blevel) {
		this.blevel = blevel;
	}
	@Column(length=40)
	public String getBeqcount() {
		return beqcount;
	}
	public void setBeqcount(String beqcount) {
		this.beqcount = beqcount;
	}
	@Column(length=50)
	public String getBtower() {
		return btower;
	}
	public void setBtower(String btower) {
		this.btower = btower;
	}
	public String getBwlnumber() {
		return bwlnumber;
	}
	@Column(length=50)
	public void setBwlnumber(String bwlnumber) {
		this.bwlnumber = bwlnumber;
	}
	@Column(length=100)
	public String getBdesc() {
		return bdesc;
	}
	public void setBdesc(String bdesc) {
		this.bdesc = bdesc;
	}
	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY,mappedBy="tbaseinfo")
	public Set<TBaseInfoEquipment> getBaseinfoequipments() {
		return baseinfoequipments;
	}
	public void setBaseinfoequipments(Set<TBaseInfoEquipment> baseinfoequipments) {
		this.baseinfoequipments = baseinfoequipments;
	}
}
