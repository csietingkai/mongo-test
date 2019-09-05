package com.mkyong.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

@Configuration
public class FileConfig {
	
	@Bean(name="multipartResolver")
	public MultipartResolver getMultipartResolver(){
		CommonsMultipartResolver multipartResolver =new CommonsMultipartResolver();
		multipartResolver.setMaxUploadSizePerFile(100000000);//100M
		multipartResolver.setDefaultEncoding("UTF-8");
		multipartResolver.setResolveLazily(true);
		return multipartResolver;
	}
}