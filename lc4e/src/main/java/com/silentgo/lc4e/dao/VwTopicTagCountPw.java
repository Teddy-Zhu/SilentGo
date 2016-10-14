package com.silentgo.lc4e.dao;

import com.silentgo.core.db.Table;
import com.silentgo.core.db.TableModel;

import java.math.BigDecimal;
import java.util.Date;

@Table("vw_topic_tag_count_pw")
public class VwTopicTagCountPw extends TableModel {

	public Integer id;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String areaAbbr;

	public String getAreaAbbr() {
		return areaAbbr;
	}

	public void setAreaAbbr(String areaAbbr) {
		this.areaAbbr = areaAbbr;
	}

	public String areaName;

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String title;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String author;

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public BigDecimal utPw;

	public BigDecimal getUtPw() {
		return utPw;
	}

	public void setUtPw(BigDecimal utPw) {
		this.utPw = utPw;
	}

	public Long count;

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	public Integer lastCommentId;

	public Integer getLastCommentId() {
		return lastCommentId;
	}

	public void setLastCommentId(Integer lastCommentId) {
		this.lastCommentId = lastCommentId;
	}

	public Integer curTagUser;

	public Integer getCurTagUser() {
		return curTagUser;
	}

	public void setCurTagUser(Integer curTagUser) {
		this.curTagUser = curTagUser;
	}

	public String authorAvatar;

	public String getAuthorAvatar() {
		return authorAvatar;
	}

	public void setAuthorAvatar(String authorAvatar) {
		this.authorAvatar = authorAvatar;
	}

	public Date pubTime;

	public Date getPubTime() {
		return pubTime;
	}

	public void setPubTime(Date pubTime) {
		this.pubTime = pubTime;
	}

	public Integer areaId;

	public Integer getAreaId() {
		return areaId;
	}

	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}

	public Integer authorId;

	public Integer getAuthorId() {
		return authorId;
	}

	public void setAuthorId(Integer authorId) {
		this.authorId = authorId;
	}


}

