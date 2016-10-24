package com.silentgo.test.dao;

import java.util.Date;
import com.silentgo.orm.base.annotation.Table;
import com.silentgo.orm.base.TableModel;

@Table(value="sys_area",  primaryKey = "id")
public class SysArea extends TableModel {

	public Integer id;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer parentId;

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String abbr;

	public String getAbbr() {
		return abbr;
	}

	public void setAbbr(String abbr) {
		this.abbr = abbr;
	}

	public String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String description;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String css;

	public String getCss() {
		return css;
	}

	public void setCss(String css) {
		this.css = css;
	}

	public String icon;

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Integer areaStatusId;

	public Integer getAreaStatusId() {
		return areaStatusId;
	}

	public void setAreaStatusId(Integer areaStatusId) {
		this.areaStatusId = areaStatusId;
	}

	public Date createTime;

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date updateTime;

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}


}

