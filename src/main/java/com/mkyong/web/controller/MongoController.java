package com.mkyong.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.mkyong.web.jsonview.Views;
import com.mkyong.web.model.FileId;
import com.mkyong.web.model.MongoInfo;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.client.model.Filters;

@RestController
public class MongoController {
	
	@JsonView(Views.Public.class)
	@RequestMapping(value = "/mongo/info", method=RequestMethod.POST)
	public MongoInfo getSearchResultViaAjax(@RequestBody FileId id) {
		MongoInfo info = new MongoInfo();
		info.setId(id.getId());
		
		if (this.isValidFileId(id)) {
			MongoClient mongoClient = new MongoClient();
			MongoDatabase testDatabase = mongoClient.getDatabase("test");
			GridFSBucket gridFSBucket = GridFSBuckets.create(testDatabase);
			gridFSBucket.find(Filters.eq("_id", id.getId())).forEach(
					new Block<GridFSFile>() {
						public void apply(final GridFSFile gridFSFile) {
							info.setFilename(gridFSFile.getFilename());
							info.setLength(gridFSFile.getLength());
							info.setUploadDate(gridFSFile.getUploadDate());
						}
					});
			mongoClient.close();
		}
		
		return info;
	}
	
	@RequestMapping(value = "/mongo/list", method = RequestMethod.POST)
	public List<MongoInfo> listFiles() {
		List<MongoInfo> list = new ArrayList<MongoInfo>();

		MongoClient mongoClient = new MongoClient();
		MongoDatabase testDatabase = mongoClient.getDatabase("test");
		GridFSBucket gridFSBucket = GridFSBuckets.create(testDatabase);
		gridFSBucket.find().forEach(
				  new Block<GridFSFile>() {
				    public void apply(final GridFSFile gridFSFile) {
				        MongoInfo info = new MongoInfo();
				        info.setId(gridFSFile.getId().toString());
				        info.setFilename(gridFSFile.getFilename());
						info.setLength(gridFSFile.getLength());
						info.setUploadDate(gridFSFile.getUploadDate());
						list.add(info);
				    }
				});
		
		return list;
	}
	
	private boolean isValidFileId(FileId fileid) {

		boolean valid = true;

		if (fileid == null) {
			valid = false;
		}

		if (StringUtils.isEmpty(fileid.getId())) {
			valid = false;
		}

		return valid;
	}
}
