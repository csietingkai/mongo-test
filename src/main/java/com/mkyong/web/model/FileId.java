package com.mkyong.web.model;

public class FileId {
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		return "FileId: {" + this.id + "}";
	}
}
