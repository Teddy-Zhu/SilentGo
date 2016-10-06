package com.silentgo.test.dao;

import java.util.Date;
import com.silentgo.core.db.Table;
import com.silentgo.core.db.TableModel;

@Table(value="sys_area_status",  primaryKey = "id")
public class sysAreaStatus extends TableModel {

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

	public Integer pubtopic;

	public Integer getPubtopic() {
		return pubtopic;
	}

	public void setPubtopic(Integer pubtopic) {
		this.pubtopic = pubtopic;
	}

	public Integer pubcomment;

	public Integer getPubcomment() {
		return pubcomment;
	}

	public void setPubcomment(Integer pubcomment) {
		this.pubcomment = pubcomment;
	}

	public Date createtime;

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public Date updaettime;

	public Date getUpdaettime() {
		return updaettime;
	}

	public void setUpdaettime(Date updaettime) {
		this.updaettime = updaettime;
	}


}

