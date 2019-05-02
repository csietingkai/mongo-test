package com.mkyong.web.model;

import java.util.Date;

import org.bson.BsonObjectId;
import org.bson.types.ObjectId;

import com.fasterxml.jackson.annotation.JsonView;
import com.mkyong.web.jsonview.Views;

public class MongoInfo {
	@JsonView(Views.Public.class)
	private String id;
	@JsonView(Views.Public.class)
	private long length;
	@JsonView(Views.Public.class)
	private Date uploadDate;
	@JsonView(Views.Public.class)
	private String filename;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public long getLength() {
		return length;
	}
	public void setLength(long length) {
		this.length = length;
	}
	public Date getUploadDate() {
		return uploadDate;
	}
	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	@Override
	public String toString() {
		return "MongoInfo: {" + this.id.toString() + ", " + this.length + ", " + this.uploadDate + ", " + this.filename + "}";
	}
}
