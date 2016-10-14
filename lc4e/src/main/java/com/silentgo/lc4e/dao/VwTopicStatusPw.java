package com.silentgo.lc4e.dao;

import com.silentgo.core.db.Table;
import com.silentgo.core.db.TableModel;

import java.math.BigDecimal;

@Table("vw_topic_status_pw")
public class VwTopicStatusPw extends TableModel {

	public Integer id;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public BigDecimal statusPw;

	public BigDecimal getStatusPw() {
		return statusPw;
	}

	public void setStatusPw(BigDecimal statusPw) {
		this.statusPw = statusPw;
	}


}

