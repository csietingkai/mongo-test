package com.mkyong.web.controller;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.bson.BsonObjectId;
import org.bson.BsonValue;
import org.bson.types.ObjectId;
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
@RequestMapping(value = "/mongo")
public class MongoController {
	
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public List<MongoInfo> listFiles() {
		List<MongoInfo> list = new ArrayList<MongoInfo>();

		MongoClient mongoClient = new MongoClient();
		MongoDatabase testDatabase = mongoClient.getDatabase("test");
		GridFSBucket gridFSBucket = GridFSBuckets.create(testDatabase, "files");
		gridFSBucket.find().forEach(
				new Block<GridFSFile>() {
					public void apply(final GridFSFile gridFSFile) {
				        MongoInfo info = new MongoInfo();
				        info.setId(gridFSFile.getId().asObjectId().getValue().toString());
				        info.setFilename(gridFSFile.getFilename());
						info.setLength(gridFSFile.getLength());
						info.setUploadDate(gridFSFile.getUploadDate());
						list.add(info);
				    }
				});
		mongoClient.close();
		return list;
	}
	
	@RequestMapping(value = "/upload", method = RequestMethod.PUT)
	public boolean upload(@RequestBody InputStream inputStream) {
		MongoClient mongoClient = new MongoClient();
		MongoDatabase testDatabase = mongoClient.getDatabase("test");
		GridFSBucket gridFSBucket = GridFSBuckets.create(testDatabase, "files");
		//gridFSBucket.uploadFromStream(filename, source);
		mongoClient.close();
		return false;
	}
}
