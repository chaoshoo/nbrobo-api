package com.net.nky.entity;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * office 科室表.
 * @author Ken
 * @version 2016年8月24日
 */
public class Office {
	@JsonIgnore
	private Integer id;
	private String code;
	private String name;
	private String pic;
	private String des;
	private String description;
	@JsonIgnore
	private Date createTime;

	public Office() {

	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return this.id;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCode() {
		return this.code;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getPic() {
		return this.pic;
	}

	public void setDes(String des) {
		this.des = des;
	}

	public String getDes() {
		return this.des;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getCreateTime() {
		return this.createTime;
	}
}
