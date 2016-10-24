package com.silentgo.test.dao;

import java.util.Date;
import com.silentgo.orm.base.annotation.Table;
import com.silentgo.orm.base.TableModel;

@Table(value="sys_log",  primaryKey = "id")
public class SysLog extends TableModel {

	public Integer id;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer operateTypeId;

	public Integer getOperateTypeId() {
		return operateTypeId;
	}

	public void setOperateTypeId(Integer operateTypeId) {
		this.operateTypeId = operateTypeId;
	}

	public Integer userId;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String description;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String ip;

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String agant;

	public String getAgant() {
		return agant;
	}

	public void setAgant(String agant) {
		this.agant = agant;
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

