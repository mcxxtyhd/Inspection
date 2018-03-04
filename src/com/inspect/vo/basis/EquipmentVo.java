package com.inspect.vo.basis;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.inspect.vo.comon.BaseVo;

public class EquipmentVo extends BaseVo implements Serializable {

	private static final long serialVersionUID = -6019485774091976779L;
	
	private String enumber; // 设备编号
	private String ename; // 设备名称
	private String etype; // 类型
	private String efactory;// 厂商
	private String eaddress; // 设备地址
	private Double eposx; // 经度
	private Double eposy; // 纬度
	private String etwocodeid;// 二维码标识
	private String erfid; // RFID标识
	private String sdesc; // 设备描述
	private String epids; // 巡检项ID
	private String epnames;// 巡检项名称
	private String bregion;//所属区域
	private String blevel; //基站维护等级
	private String beqcount; // 有源设备数量
	private String btower;  //自建（共享）塔
	private String epgids;//巡检项组ID
	private String epgnames;//巡检项组名称
	private String epid;//设备ID;
	private int pointid;//巡检点id
	private int lineid;//巡检线路id
	private String ecity; //地市
	private String eregion;//区县
	private List<ProjectVo> projectList=new ArrayList<ProjectVo>();//巡检项集合
	private List<TermProjectVo> tprojectList=new ArrayList<TermProjectVo>();//巡检项集合,用于于终端上报与给终端传递任务
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

	public List<TermProjectVo> getTprojectList() {
		return tprojectList;
	}

	public void setTprojectList(List<TermProjectVo> tprojectList) {
		this.tprojectList = tprojectList;
	}

	public int getPointid() {
		return pointid;
	}

	public void setPointid(int pointid) {
		this.pointid = pointid;
	}

	public int getLineid() {
		return lineid;
	}

	public void setLineid(int lineid) {
		this.lineid = lineid;
	}

	public String getEpid() {
		return epid;
	}

	public void setEpid(String epid) {
		this.epid = epid;
	}


	public String getEpgids() {
		return epgids;
	}

	public void setEpgids(String epgids) {
		this.epgids = epgids;
	}

	public String getEpgnames() {
		return epgnames;
	}

	public void setEpgnames(String epgnames) {
		this.epgnames = epgnames;
	}
	public String getEaddress() {
		return eaddress;
	}

	public String getEfactory() {
		return efactory;
	}

	public String getEname() {
		return ename;
	}

	public String getEnumber() {
		return enumber;
	}

	public String getEpids() {
		return epids;
	}

	public String getEpnames() {
		return epnames;
	}

	public Double getEposx() {
		return eposx;
	}

	public Double getEposy() {
		return eposy;
	}

	public String getErfid() {
		return erfid;
	}

	public String getEtwocodeid() {
		return etwocodeid;
	}

	public String getEtype() {
		return etype;
	}

	public String getSdesc() {
		return sdesc;
	}

	public void setEaddress(String eaddress) {
		this.eaddress = eaddress;
	}

	public void setEfactory(String efactory) {
		this.efactory = efactory;
	}

	public void setEname(String ename) {
		this.ename = ename;
	}

	public void setEnumber(String enumber) {
		this.enumber = enumber;
	}

	public void setEpids(String epids) {
		this.epids = epids;
	}

	public void setEpnames(String epnames) {
		this.epnames = epnames;
	}

	public void setEposx(Double eposx) {
		this.eposx = eposx;
	}

	public void setEposy(Double eposy) {
		this.eposy = eposy;
	}

	public void setErfid(String erfid) {
		this.erfid = erfid;
	}

	public void setEtwocodeid(String etwocodeid) {
		this.etwocodeid = etwocodeid;
	}

	public void setEtype(String etype) {
		this.etype = etype;
	}

	public void setSdesc(String sdesc) {
		this.sdesc = sdesc;
	}

	public List<ProjectVo> getProjectList() {
		return projectList;
	}

	public void setProjectList(List<ProjectVo> projectList) {
		this.projectList = projectList;
	}
	public String getBregion() {
		return bregion;
	}

	public void setBregion(String bregion) {
		this.bregion = bregion;
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

	public String getEcity() {
		return ecity;
	}

	public void setEcity(String ecity) {
		this.ecity = ecity;
	}

	public String getEregion() {
		return eregion;
	}

	public void setEregion(String eregion) {
		this.eregion = eregion;
	}
	
}
