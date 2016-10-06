package com.silentgo.test.dao;

import java.util.Date;
import com.silentgo.core.db.Table;
import com.silentgo.core.db.TableModel;

@Table(value="sys_area_status",  primaryKey = "id")
public class SysAreaStatus extends TableModel {

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

	public String description;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer visible;

	public Integer getVisible() {
		return visible;
	}

	public void setVisible(Integer visible) {
		this.visible = visible;
	}

	public Integer close;

	public Integer getClose() {
		return close;
	}

	public void setClose(Integer close) {
		this.close = close;
	}

	public Integer move;

	public Integer getMove() {
		return move;
	}

	public void setMove(Integer move) {
		this.move = move;
	}

	public Integer pubTopic;

	public Integer getPubTopic() {
		return pubTopic;
	}

	public void setPubTopic(Integer pubTopic) {
		this.pubTopic = pubTopic;
	}

	public Integer pubComment;

	public Integer getPubComment() {
		return pubComment;
	}

	public void setPubComment(Integer pubComment) {
		this.pubComment = pubComment;
	}

	public Date createTime;

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date updaetTime;

	public Date getUpdaetTime() {
		return updaetTime;
	}

	public void setUpdaetTime(Date updaetTime) {
		this.updaetTime = updaetTime;
	}


}

