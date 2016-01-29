/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.util.StringUtils;

/**
 *
 * @author afes
 */
public class JobUtil {
    protected static Logger log = LoggerFactory.getLogger(JobUtil.class);
	
	public static Resource getResource(final String resourcePath) {
		
		if (resourcePath == null) {
			return null;
		}
		
		Resource resource = null;
		
		String profile = System.getProperty("spring.profiles.active");			 
		if (!StringUtils.hasText(profile) || ("loc").equals(profile)) {
			resource = new ClassPathResource(resourcePath);
			return resource;
		} 
		
		try {
			resource = new UrlResource(resourcePath);
		} catch (Exception e) {			
			log.error("resource error : {}", e.toString());
			return null;
		} 
		
		return resource;
	}
}
