package com.silentgo.test.dao;

import java.math.BigDecimal;
import java.util.Date;
import com.silentgo.orm.base.annotation.Table;
import com.silentgo.orm.base.TableModel;

@Table(value="user_tag",  primaryKey = "id")
public class UserTag extends TableModel {

	public Integer id;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer userId;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String tagId;

	public String getTagId() {
		return tagId;
	}

	public void setTagId(String tagId) {
		this.tagId = tagId;
	}

	public BigDecimal pw;

	public BigDecimal getPw() {
		return pw;
	}

	public void setPw(BigDecimal pw) {
		this.pw = pw;
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

