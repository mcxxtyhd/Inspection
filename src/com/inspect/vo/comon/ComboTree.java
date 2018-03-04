package com.inspect.vo.comon;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class ComboTree  implements Serializable {
	private static final long serialVersionUID = 2802711051388027765L;
	private String id;
	private String text;// 树节点名称
	private String iconCls;// 前面的小图标样式
	private Boolean checked;// 是否勾选状态
	private Map<String, Object> attributes;// 其他参数
	private List<ComboTree> children;// 子节点
	private String state = "open";// 是否展开(open,closed)
	

	public ComboTree() {
		this.checked = Boolean.valueOf(false);
		this.state = "open";
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Boolean getChecked() {
		return this.checked;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}

	public Map<String, Object> getAttributes() {
		return this.attributes;
	}

	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	public List<ComboTree> getChildren() {
		return this.children;
	}

	public void setChildren(List<ComboTree> children) {
		this.children = children;
	}

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getIconCls() {
		return this.iconCls;
	}

	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}

}
