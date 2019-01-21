package com.chumbok.meme.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class ImageResourceConfiguration extends WebMvcConfigurerAdapter {

	@Value("${uploadedImagePath}")
	private String uploadedImagePath;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {

		registry.addResourceHandler("/image/**")
				.addResourceLocations("file:" + uploadedImagePath + "/")
				.setCachePeriod(3600);

		super.addResourceHandlers(registry);
	}
}