package com.silentgo.lc4e.dao;

import com.silentgo.core.db.Table;
import com.silentgo.core.db.TableModel;

import java.util.Date;

@Table(value="sys_topic_attach",  primaryKey = "id")
public class SysTopicAttach extends TableModel {

	public Integer id;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer topicId;

	public Integer getTopicId() {
		return topicId;
	}

	public void setTopicId(Integer topicId) {
		this.topicId = topicId;
	}

	public Integer attachPath;

	public Integer getAttachPath() {
		return attachPath;
	}

	public void setAttachPath(Integer attachPath) {
		this.attachPath = attachPath;
	}

	public Integer userId;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
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

