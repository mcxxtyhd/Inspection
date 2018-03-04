package com.inspect.vo.basis;

import java.io.File;
import java.io.Serializable;

import com.inspect.vo.comon.BaseVo;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="unicominfo")
public class FormsVo extends BaseVo implements Serializable{

	private static final long serialVersionUID = -5285248892667497154L;
	

	private String operator;
	private String license;
	private String datasheet;
	private String baseid;
	private String basename;
	private String administrativedivision;
	private String address;
	private String radius;
	private String longitute;
	private String latitude;
	private String cityx;
	private String cityy;
	private String high;
	private String startdate;
	private String recognitionbookid;
	private String network1;
	private String network1sectorid;
	private String network1sectornum;
	private String network1angle;
	private String  network1identificationcode;
	private String network1receiveangle;
	private String network1sendangle;
	private String network1loss;
	private String network1sendfreq;
	private String network1receivefreq;
	private String network1equipmentmodel;
	private String network1cmiitid;
	private String network1equipmentfactory;
	private String network1equipmentnum;
	private String network1transmitpower;
	private String network1powerunit;
	private String network1antennatype;
	private String network1antennamodel;
	private String network1polarization;
	private String network13db;
	private String network1dbi;
	private String network1dbiunit;
	private String network1antennafactory;
	private String network1antennahigh;
	private String network1startdate;
	private String network2;
	private String network2sectorid;
	private String network2sectornum;
	private String network2angle;
	private String  network2identificationcode;
	private String network2receiveangle;
	private String network2sendangle;
	private String network2loss;
	private String network2sendfreq;
	private String network2receivefreq;
	private String network2equipmentmodel;
	private String network2cmiitid;
	private String network2equipmentfactory;
	private String network2equipmentnum;
	private String network2transmitpower;
	private String network2powerunit;
	private String network2antennatype;
	private String network2antennamodel;
	private String network2polarization;
	private String network23db;
	private String network2dbi;
	private String network2dbiunit;
	private String network2antennafactory;
	private String network2antennahigh;
	private String network2startdate;
	private String network3;
	private String network3sectorid;
	private String network3angle;
	private String network3identificationcode;
	private String network3loss;
	private String network3sendfreq;
	private String network3receivefreq;
	private String network3equipmentmodel;
	private String network3cmiitid;
	private String network3equipmentfactory;
	private String network3transmitpower;
	private String network3powerunit;
	private String network3antennatype;
	private String network3antennamodel;
	private String network3polarization;
	private String network3dbi;
	private String network3dbiunit;
	private String network3antennafactory;
	private String network3antennahigh;
	private String network3startdate;
	private String network4;
	private String network4sectorid;
	private String network4sectornum;
	private String network4angle;
	private String  network4identificationcode;
	private String network4receiveangle;
	private String network4sendangle;
	private String network4loss;
	private String network4sendfreq;
	private String network4receivefreq;
	private String network4equipmentmodel;
	private String network4cmiitid;
	private String network4equipmentfactory;
	private String network4equipmentnum;
	private String network4transmitpower;
	private String network4powerunit;
	private String network4antennatype;
	private String network4antennamodel;
	private String network4polarization;
	private String network43db;
	private String network4dbi;
	private String network4dbiunit;
	private String network4antennafactory;
	private String network4antennahigh;
	private String network4startdate;
	private String printdate;
	private String validity;
	private String remark;
	
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

	
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getLicense() {
		return license;
	}
	public void setLicense(String license) {
		this.license = license;
	}
	public String getDatasheet() {
		return datasheet;
	}
	public void setDatasheet(String datasheet) {
		this.datasheet = datasheet;
	}
	public String getBaseid() {
		return baseid;
	}
	public void setBaseid(String baseid) {
		this.baseid = baseid;
	}
	public String getBasename() {
		return basename;
	}
	public void setBasename(String basename) {
		this.basename = basename;
	}
	public String getAdministrativedivision() {
		return administrativedivision;
	}
	public void setAdministrativedivision(String administrativedivision) {
		this.administrativedivision = administrativedivision;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getRadius() {
		return radius;
	}
	public void setRadius(String radius) {
		this.radius = radius;
	}
	public String getLongitute() {
		return longitute;
	}
	public void setLongitute(String longitute) {
		this.longitute = longitute;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getCityx() {
		return cityx;
	}
	public void setCityx(String cityx) {
		this.cityx = cityx;
	}
	public String getCityy() {
		return cityy;
	}
	public void setCityy(String cityy) {
		this.cityy = cityy;
	}
	public String getHigh() {
		return high;
	}
	public void setHigh(String high) {
		this.high = high;
	}
	public String getStartdate() {
		return startdate;
	}
	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}
	public String getRecognitionbookid() {
		return recognitionbookid;
	}
	public void setRecognitionbookid(String recognitionbookid) {
		this.recognitionbookid = recognitionbookid;
	}
	public String getNetwork1() {
		return network1;
	}
	public void setNetwork1(String network1) {
		this.network1 = network1;
	}
	public String getNetwork1sectorid() {
		return network1sectorid;
	}
	public void setNetwork1sectorid(String network1sectorid) {
		this.network1sectorid = network1sectorid;
	}
	public String getNetwork1sectornum() {
		return network1sectornum;
	}
	public void setNetwork1sectornum(String network1sectornum) {
		this.network1sectornum = network1sectornum;
	}
	public String getNetwork1angle() {
		return network1angle;
	}
	public void setNetwork1angle(String network1angle) {
		this.network1angle = network1angle;
	}
	public String getNetwork1identificationcode() {
		return network1identificationcode;
	}
	public void setNetwork1identificationcode(String network1identificationcode) {
		this.network1identificationcode = network1identificationcode;
	}
	public String getNetwork1receiveangle() {
		return network1receiveangle;
	}
	public void setNetwork1receiveangle(String network1receiveangle) {
		this.network1receiveangle = network1receiveangle;
	}
	public String getNetwork1sendangle() {
		return network1sendangle;
	}
	public void setNetwork1sendangle(String network1sendangle) {
		this.network1sendangle = network1sendangle;
	}
	public String getNetwork1loss() {
		return network1loss;
	}
	public void setNetwork1loss(String network1loss) {
		this.network1loss = network1loss;
	}
	public String getNetwork1sendfreq() {
		return network1sendfreq;
	}
	public void setNetwork1sendfreq(String network1sendfreq) {
		this.network1sendfreq = network1sendfreq;
	}
	public String getNetwork1receivefreq() {
		return network1receivefreq;
	}
	public void setNetwork1receivefreq(String network1receivefreq) {
		this.network1receivefreq = network1receivefreq;
	}
	public String getNetwork1equipmentmodel() {
		return network1equipmentmodel;
	}
	public void setNetwork1equipmentmodel(String network1equipmentmodel) {
		this.network1equipmentmodel = network1equipmentmodel;
	}
	public String getNetwork1cmiitid() {
		return network1cmiitid;
	}
	public void setNetwork1cmiitid(String network1cmiitid) {
		this.network1cmiitid = network1cmiitid;
	}
	public String getNetwork1equipmentfactory() {
		return network1equipmentfactory;
	}
	public void setNetwork1equipmentfactory(String network1equipmentfactory) {
		this.network1equipmentfactory = network1equipmentfactory;
	}
	public String getNetwork1equipmentnum() {
		return network1equipmentnum;
	}
	public void setNetwork1equipmentnum(String network1equipmentnum) {
		this.network1equipmentnum = network1equipmentnum;
	}
	public String getNetwork1transmitpower() {
		return network1transmitpower;
	}
	public void setNetwork1transmitpower(String network1transmitpower) {
		this.network1transmitpower = network1transmitpower;
	}
	public String getNetwork1powerunit() {
		return network1powerunit;
	}
	public void setNetwork1powerunit(String network1powerunit) {
		this.network1powerunit = network1powerunit;
	}
	public String getNetwork1antennatype() {
		return network1antennatype;
	}
	public void setNetwork1antennatype(String network1antennatype) {
		this.network1antennatype = network1antennatype;
	}
	public String getNetwork1antennamodel() {
		return network1antennamodel;
	}
	public void setNetwork1antennamodel(String network1antennamodel) {
		this.network1antennamodel = network1antennamodel;
	}
	public String getNetwork1polarization() {
		return network1polarization;
	}
	public void setNetwork1polarization(String network1polarization) {
		this.network1polarization = network1polarization;
	}
	public String getNetwork13db() {
		return network13db;
	}
	public void setNetwork13db(String network13db) {
		this.network13db = network13db;
	}
	public String getNetwork1dbi() {
		return network1dbi;
	}
	public void setNetwork1dbi(String network1dbi) {
		this.network1dbi = network1dbi;
	}
	public String getNetwork1dbiunit() {
		return network1dbiunit;
	}
	public void setNetwork1dbiunit(String network1dbiunit) {
		this.network1dbiunit = network1dbiunit;
	}
	public String getNetwork1antennafactory() {
		return network1antennafactory;
	}
	public void setNetwork1antennafactory(String network1antennafactory) {
		this.network1antennafactory = network1antennafactory;
	}
	public String getNetwork1antennahigh() {
		return network1antennahigh;
	}
	public void setNetwork1antennahigh(String network1antennahigh) {
		this.network1antennahigh = network1antennahigh;
	}
	public String getNetwork1startdate() {
		return network1startdate;
	}
	public void setNetwork1startdate(String network1startdate) {
		this.network1startdate = network1startdate;
	}
	public String getNetwork2() {
		return network2;
	}
	public void setNetwork2(String network2) {
		this.network2 = network2;
	}
	public String getNetwork2sectorid() {
		return network2sectorid;
	}
	public void setNetwork2sectorid(String network2sectorid) {
		this.network2sectorid = network2sectorid;
	}
	public String getNetwork2sectornum() {
		return network2sectornum;
	}
	public void setNetwork2sectornum(String network2sectornum) {
		this.network2sectornum = network2sectornum;
	}
	public String getNetwork2angle() {
		return network2angle;
	}
	public void setNetwork2angle(String network2angle) {
		this.network2angle = network2angle;
	}
	public String getNetwork2identificationcode() {
		return network2identificationcode;
	}
	public void setNetwork2identificationcode(String network2identificationcode) {
		this.network2identificationcode = network2identificationcode;
	}
	public String getNetwork2receiveangle() {
		return network2receiveangle;
	}
	public void setNetwork2receiveangle(String network2receiveangle) {
		this.network2receiveangle = network2receiveangle;
	}
	public String getNetwork2sendangle() {
		return network2sendangle;
	}
	public void setNetwork2sendangle(String network2sendangle) {
		this.network2sendangle = network2sendangle;
	}
	public String getNetwork2loss() {
		return network2loss;
	}
	public void setNetwork2loss(String network2loss) {
		this.network2loss = network2loss;
	}
	public String getNetwork2sendfreq() {
		return network2sendfreq;
	}
	public void setNetwork2sendfreq(String network2sendfreq) {
		this.network2sendfreq = network2sendfreq;
	}
	public String getNetwork2receivefreq() {
		return network2receivefreq;
	}
	public void setNetwork2receivefreq(String network2receivefreq) {
		this.network2receivefreq = network2receivefreq;
	}
	public String getNetwork2equipmentmodel() {
		return network2equipmentmodel;
	}
	public void setNetwork2equipmentmodel(String network2equipmentmodel) {
		this.network2equipmentmodel = network2equipmentmodel;
	}
	public String getNetwork2cmiitid() {
		return network2cmiitid;
	}
	public void setNetwork2cmiitid(String network2cmiitid) {
		this.network2cmiitid = network2cmiitid;
	}
	public String getNetwork2equipmentfactory() {
		return network2equipmentfactory;
	}
	public void setNetwork2equipmentfactory(String network2equipmentfactory) {
		this.network2equipmentfactory = network2equipmentfactory;
	}
	public String getNetwork2equipmentnum() {
		return network2equipmentnum;
	}
	public void setNetwork2equipmentnum(String network2equipmentnum) {
		this.network2equipmentnum = network2equipmentnum;
	}
	public String getNetwork2transmitpower() {
		return network2transmitpower;
	}
	public void setNetwork2transmitpower(String network2transmitpower) {
		this.network2transmitpower = network2transmitpower;
	}
	public String getNetwork2powerunit() {
		return network2powerunit;
	}
	public void setNetwork2powerunit(String network2powerunit) {
		this.network2powerunit = network2powerunit;
	}
	public String getNetwork2antennatype() {
		return network2antennatype;
	}
	public void setNetwork2antennatype(String network2antennatype) {
		this.network2antennatype = network2antennatype;
	}
	public String getNetwork2antennamodel() {
		return network2antennamodel;
	}
	public void setNetwork2antennamodel(String network2antennamodel) {
		this.network2antennamodel = network2antennamodel;
	}
	public String getNetwork2polarization() {
		return network2polarization;
	}
	public void setNetwork2polarization(String network2polarization) {
		this.network2polarization = network2polarization;
	}
	public String getNetwork23db() {
		return network23db;
	}
	public void setNetwork23db(String network23db) {
		this.network23db = network23db;
	}
	public String getNetwork2dbi() {
		return network2dbi;
	}
	public void setNetwork2dbi(String network2dbi) {
		this.network2dbi = network2dbi;
	}
	public String getNetwork2dbiunit() {
		return network2dbiunit;
	}
	public void setNetwork2dbiunit(String network2dbiunit) {
		this.network2dbiunit = network2dbiunit;
	}
	public String getNetwork2antennafactory() {
		return network2antennafactory;
	}
	public void setNetwork2antennafactory(String network2antennafactory) {
		this.network2antennafactory = network2antennafactory;
	}
	public String getNetwork2antennahigh() {
		return network2antennahigh;
	}
	public void setNetwork2antennahigh(String network2antennahigh) {
		this.network2antennahigh = network2antennahigh;
	}
	public String getNetwork2startdate() {
		return network2startdate;
	}
	public void setNetwork2startdate(String network2startdate) {
		this.network2startdate = network2startdate;
	}
	public String getNetwork3() {
		return network3;
	}
	public void setNetwork3(String network3) {
		this.network3 = network3;
	}
	public String getNetwork3sectorid() {
		return network3sectorid;
	}
	public void setNetwork3sectorid(String network3sectorid) {
		this.network3sectorid = network3sectorid;
	}
	public String getNetwork3angle() {
		return network3angle;
	}
	public void setNetwork3angle(String network3angle) {
		this.network3angle = network3angle;
	}
	public String getNetwork3identificationcode() {
		return network3identificationcode;
	}
	public void setNetwork3identificationcode(String network3identificationcode) {
		this.network3identificationcode = network3identificationcode;
	}
	public String getNetwork3loss() {
		return network3loss;
	}
	public void setNetwork3loss(String network3loss) {
		this.network3loss = network3loss;
	}
	public String getNetwork3sendfreq() {
		return network3sendfreq;
	}
	public void setNetwork3sendfreq(String network3sendfreq) {
		this.network3sendfreq = network3sendfreq;
	}
	public String getNetwork3receivefreq() {
		return network3receivefreq;
	}
	public void setNetwork3receivefreq(String network3receivefreq) {
		this.network3receivefreq = network3receivefreq;
	}
	public String getNetwork3equipmentmodel() {
		return network3equipmentmodel;
	}
	public void setNetwork3equipmentmodel(String network3equipmentmodel) {
		this.network3equipmentmodel = network3equipmentmodel;
	}
	public String getNetwork3cmiitid() {
		return network3cmiitid;
	}
	public void setNetwork3cmiitid(String network3cmiitid) {
		this.network3cmiitid = network3cmiitid;
	}
	public String getNetwork3equipmentfactory() {
		return network3equipmentfactory;
	}
	public void setNetwork3equipmentfactory(String network3equipmentfactory) {
		this.network3equipmentfactory = network3equipmentfactory;
	}
	public String getNetwork3transmitpower() {
		return network3transmitpower;
	}
	public void setNetwork3transmitpower(String network3transmitpower) {
		this.network3transmitpower = network3transmitpower;
	}
	public String getNetwork3powerunit() {
		return network3powerunit;
	}
	public void setNetwork3powerunit(String network3powerunit) {
		this.network3powerunit = network3powerunit;
	}
	public String getNetwork3antennatype() {
		return network3antennatype;
	}
	public void setNetwork3antennatype(String network3antennatype) {
		this.network3antennatype = network3antennatype;
	}
	public String getNetwork3antennamodel() {
		return network3antennamodel;
	}
	public void setNetwork3antennamodel(String network3antennamodel) {
		this.network3antennamodel = network3antennamodel;
	}
	public String getNetwork3polarization() {
		return network3polarization;
	}
	public void setNetwork3polarization(String network3polarization) {
		this.network3polarization = network3polarization;
	}
	public String getNetwork3dbi() {
		return network3dbi;
	}
	public void setNetwork3dbi(String network3dbi) {
		this.network3dbi = network3dbi;
	}
	public String getNetwork3dbiunit() {
		return network3dbiunit;
	}
	public void setNetwork3dbiunit(String network3dbiunit) {
		this.network3dbiunit = network3dbiunit;
	}
	public String getNetwork3antennafactory() {
		return network3antennafactory;
	}
	public void setNetwork3antennafactory(String network3antennafactory) {
		this.network3antennafactory = network3antennafactory;
	}
	public String getNetwork3antennahigh() {
		return network3antennahigh;
	}
	public void setNetwork3antennahigh(String network3antennahigh) {
		this.network3antennahigh = network3antennahigh;
	}
	public String getNetwork3startdate() {
		return network3startdate;
	}
	public void setNetwork3startdate(String network3startdate) {
		this.network3startdate = network3startdate;
	}
	public String getNetwork4() {
		return network4;
	}
	public void setNetwork4(String network4) {
		this.network4 = network4;
	}
	public String getNetwork4sectorid() {
		return network4sectorid;
	}
	public void setNetwork4sectorid(String network4sectorid) {
		this.network4sectorid = network4sectorid;
	}
	public String getNetwork4sectornum() {
		return network4sectornum;
	}
	public void setNetwork4sectornum(String network4sectornum) {
		this.network4sectornum = network4sectornum;
	}
	public String getNetwork4angle() {
		return network4angle;
	}
	public void setNetwork4angle(String network4angle) {
		this.network4angle = network4angle;
	}
	public String getNetwork4identificationcode() {
		return network4identificationcode;
	}
	public void setNetwork4identificationcode(String network4identificationcode) {
		this.network4identificationcode = network4identificationcode;
	}
	public String getNetwork4receiveangle() {
		return network4receiveangle;
	}
	public void setNetwork4receiveangle(String network4receiveangle) {
		this.network4receiveangle = network4receiveangle;
	}
	public String getNetwork4sendangle() {
		return network4sendangle;
	}
	public void setNetwork4sendangle(String network4sendangle) {
		this.network4sendangle = network4sendangle;
	}
	public String getNetwork4loss() {
		return network4loss;
	}
	public void setNetwork4loss(String network4loss) {
		this.network4loss = network4loss;
	}
	public String getNetwork4sendfreq() {
		return network4sendfreq;
	}
	public void setNetwork4sendfreq(String network4sendfreq) {
		this.network4sendfreq = network4sendfreq;
	}
	public String getNetwork4receivefreq() {
		return network4receivefreq;
	}
	public void setNetwork4receivefreq(String network4receivefreq) {
		this.network4receivefreq = network4receivefreq;
	}
	public String getNetwork4equipmentmodel() {
		return network4equipmentmodel;
	}
	public void setNetwork4equipmentmodel(String network4equipmentmodel) {
		this.network4equipmentmodel = network4equipmentmodel;
	}
	public String getNetwork4cmiitid() {
		return network4cmiitid;
	}
	public void setNetwork4cmiitid(String network4cmiitid) {
		this.network4cmiitid = network4cmiitid;
	}
	public String getNetwork4equipmentfactory() {
		return network4equipmentfactory;
	}
	public void setNetwork4equipmentfactory(String network4equipmentfactory) {
		this.network4equipmentfactory = network4equipmentfactory;
	}
	public String getNetwork4equipmentnum() {
		return network4equipmentnum;
	}
	public void setNetwork4equipmentnum(String network4equipmentnum) {
		this.network4equipmentnum = network4equipmentnum;
	}
	public String getNetwork4transmitpower() {
		return network4transmitpower;
	}
	public void setNetwork4transmitpower(String network4transmitpower) {
		this.network4transmitpower = network4transmitpower;
	}
	public String getNetwork4powerunit() {
		return network4powerunit;
	}
	public void setNetwork4powerunit(String network4powerunit) {
		this.network4powerunit = network4powerunit;
	}
	public String getNetwork4antennatype() {
		return network4antennatype;
	}
	public void setNetwork4antennatype(String network4antennatype) {
		this.network4antennatype = network4antennatype;
	}
	public String getNetwork4antennamodel() {
		return network4antennamodel;
	}
	public void setNetwork4antennamodel(String network4antennamodel) {
		this.network4antennamodel = network4antennamodel;
	}
	public String getNetwork4polarization() {
		return network4polarization;
	}
	public void setNetwork4polarization(String network4polarization) {
		this.network4polarization = network4polarization;
	}
	public String getNetwork43db() {
		return network43db;
	}
	public void setNetwork43db(String network43db) {
		this.network43db = network43db;
	}
	public String getNetwork4dbi() {
		return network4dbi;
	}
	public void setNetwork4dbi(String network4dbi) {
		this.network4dbi = network4dbi;
	}
	public String getNetwork4dbiunit() {
		return network4dbiunit;
	}
	public void setNetwork4dbiunit(String network4dbiunit) {
		this.network4dbiunit = network4dbiunit;
	}
	public String getNetwork4antennafactory() {
		return network4antennafactory;
	}
	public void setNetwork4antennafactory(String network4antennafactory) {
		this.network4antennafactory = network4antennafactory;
	}
	public String getNetwork4antennahigh() {
		return network4antennahigh;
	}
	public void setNetwork4antennahigh(String network4antennahigh) {
		this.network4antennahigh = network4antennahigh;
	}
	public String getNetwork4startdate() {
		return network4startdate;
	}
	public void setNetwork4startdate(String network4startdate) {
		this.network4startdate = network4startdate;
	}
	public String getPrintdate() {
		return printdate;
	}
	public void setPrintdate(String printdate) {
		this.printdate = printdate;
	}
	public String getValidity() {
		return validity;
	}
	public void setValidity(String validity) {
		this.validity = validity;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}

	
	
	
}
