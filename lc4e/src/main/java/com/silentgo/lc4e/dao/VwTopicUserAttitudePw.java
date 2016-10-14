package com.silentgo.lc4e.dao;

import com.silentgo.core.db.Table;
import com.silentgo.core.db.TableModel;

@Table("vw_topic_user_attitude_pw")
public class VwTopicUserAttitudePw extends TableModel {

	public Integer id;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Long utvaPw;

	public Long getUtvaPw() {
		return utvaPw;
	}

	public void setUtvaPw(Long utvaPw) {
		this.utvaPw = utvaPw;
	}


}

