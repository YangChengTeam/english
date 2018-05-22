package com.yc.english.group.rong.models;

import com.yc.english.group.rong.util.GsonUtil;

import java.io.Serializable;

/**
 * 群组信息。
 */
public class GroupInfo implements Serializable {
	// 群组Id。
	String id;
	// 群组名称。
	String name;
	
	public GroupInfo(String id, String name) {
		this.id = id;
		this.name = name;
	}
	
	/**
	 * 设置id
	 *
	 */	
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * 获取id
	 *
	 * @return String
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * 设置name
	 *
	 */	
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * 获取name
	 *
	 * @return String
	 */
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return GsonUtil.toJson(this, GroupInfo.class);
	}
}
