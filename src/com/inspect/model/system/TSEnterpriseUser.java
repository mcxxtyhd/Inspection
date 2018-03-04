package com.inspect.model.system;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 单位用户关联持久化实体类
 * @author wzs
 * @version 1.0
 */
@Entity
@Table(name = "t_s_enterprise_user")
public class TSEnterpriseUser implements Serializable {

	private static final long serialVersionUID = 386415981249545682L;
	
	private int id;
	private Enterprise tenterprise;
	private TSUser tsuser;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "entid")
	public Enterprise getTenterprise() {
		return tenterprise;
	}
	public void setTenterprise(Enterprise tenterprise) {
		this.tenterprise = tenterprise;
	}
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "userid")
	public TSUser getTsuser() {
		return tsuser;
	}
	public void setTsuser(TSUser tsuser) {
		this.tsuser = tsuser;
	}
}
