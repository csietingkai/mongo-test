package com.mkyong.web.controller;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mkyong.web.model.MongoInfo;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.client.gridfs.model.GridFSUploadOptions;

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
						info.setDepName(gridFSFile.getMetadata().getString("dep"));
						list.add(info);
				    }
				});
		mongoClient.close();
		return list;
	}
	
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public boolean upload(@RequestParam String depName, @RequestPart("file") MultipartFile file) {
		boolean result = true;
		
		MongoClient mongoClient = new MongoClient();
		MongoDatabase testDatabase = mongoClient.getDatabase("test");
		GridFSBucket gridFSBucket = GridFSBuckets.create(testDatabase, "files");
		try (InputStream fileInputStream = file.getInputStream()) {
			gridFSBucket.uploadFromStream(file.getOriginalFilename(), fileInputStream, 
					new GridFSUploadOptions().metadata(
							new Document().append("dep", depName)
					)
			);
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		} finally {
			mongoClient.close();
		}
		return result;
	}
	
	@RequestMapping(value = "/download", method = RequestMethod.GET)
	public void download(@RequestParam String id, @RequestParam String filename, HttpServletRequest request, HttpServletResponse response) {
		MongoClient mongoClient = new MongoClient();
		MongoDatabase testDatabase = mongoClient.getDatabase("test");
		GridFSBucket gridFSBucket = GridFSBuckets.create(testDatabase, "files");
		try {
			response.setHeader("Content-disposition", "attachment; filename*=UTF-8''" + URLEncoder.encode(filename, "UTF-8"));
			OutputStream out = response.getOutputStream();
			GridFSDownloadStream downloadStream = gridFSBucket.openDownloadStream(new ObjectId(id));
			byte[] buffer = new byte[1024];
	        int length = 0;
	        while ((length = downloadStream.read(buffer)) > 0) {
	            out.write(buffer, 0, length);
	        }
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			mongoClient.close();
		}
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	public boolean delete(@RequestParam String id) {
		boolean result = true;
		
		MongoClient mongoClient = new MongoClient();
		MongoDatabase testDatabase = mongoClient.getDatabase("test");
		GridFSBucket gridFSBucket = GridFSBuckets.create(testDatabase, "files");
		try {
			gridFSBucket.delete(new ObjectId(id));
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		} finally {
			mongoClient.close();
		}
		return result;
	}
}
