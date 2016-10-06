package com.silentgo.test.dao;

import java.util.Date;
import com.silentgo.core.db.Table;
import com.silentgo.core.db.TableModel;

@Table(value="sys_history_top",  primaryKey = "id")
public class SysHistoryTop extends TableModel {

	public Integer id;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String topicIds;

	public String getTopicIds() {
		return topicIds;
	}

	public void setTopicIds(String topicIds) {
		this.topicIds = topicIds;
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

