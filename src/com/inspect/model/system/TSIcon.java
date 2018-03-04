package com.inspect.model.system;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.inspect.model.BaseModel;

@Entity
@Table(name="t_s_icon")
public class TSIcon extends BaseModel implements Serializable {
	private static final long serialVersionUID = -3706622845188885755L;
	private String iconName;
	private Short iconType;
	private String iconPath;
	private byte[] iconContent;
	private String iconClas;
	private String extend;
	
	public TSIcon() {
		super();
	}

	public TSIcon(String iconName, java.lang.Short iconType, String iconPath, byte[] iconContent, String iconClas, String extend) {
		super();
		this.iconName = iconName;
		this.iconType = iconType;
		this.iconPath = iconPath;
		this.iconContent = iconContent;
		this.iconClas = iconClas;
		this.extend = extend;
	}

	@Column(name = "name", nullable = false, length = 100)
	public String getIconName() {
		return this.iconName;
	}

	public void setIconName(String iconName) {
		this.iconName = iconName;
	}

	@Column(name = "type")
	public Short getIconType() {
		return this.iconType;
	}

	public void setIconType(Short iconType) {
		this.iconType = iconType;
	}

	@Column(name = "path", length = 300)
	public String getIconPath() {
		return this.iconPath;
	}

	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
	}
	@Column(name = "iconclas", length = 200)
	public String getIconClas() {
		return iconClas;
	}
	public void setIconClas(String iconClas) {
		this.iconClas = iconClas;
	}

	public void setIconContent(byte[] iconContent) {
		this.iconContent = iconContent;
	}
	@Column(name = "content")
	public byte[] getIconContent() {
		return iconContent;
	}
	@Column(name = "extend")
	public String getExtend() {
		return extend;
	}

	public void setExtend(String extend) {
		this.extend = extend;
	}

}
