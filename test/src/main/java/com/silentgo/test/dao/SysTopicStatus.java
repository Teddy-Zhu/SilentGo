package com.silentgo.test.dao;

import java.math.BigDecimal;
import java.util.Date;
import com.silentgo.core.db.Table;
import com.silentgo.core.db.TableModel;

@Table(value="sys_topic_status",  primaryKey = "id")
public class SysTopicStatus extends TableModel {

	public Integer id;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public BigDecimal pw;

	public BigDecimal getPw() {
		return pw;
	}

	public void setPw(BigDecimal pw) {
		this.pw = pw;
	}

	public String icon;

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
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

