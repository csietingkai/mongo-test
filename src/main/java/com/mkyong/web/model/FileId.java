package com.mkyong.web.model;

import org.bson.BsonObjectId;

public class FileId {
	private BsonObjectId id;

	public BsonObjectId getId() {
		return id;
	}

	public void setId(BsonObjectId id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		return "FileId: {" + this.id.toString() + "}";
	}
}
