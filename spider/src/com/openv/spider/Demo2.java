package com.openv.spider;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Demo2 {
	
	private static final Log log = LogFactory.getLog(Demo2.class);
	
	public static void main(String[] args) throws Exception{
		String url = "http://www.openv.com";
		List<String> aHrefs = SpiderUtil.urlToHrefs(url);
		for (int i = 0; i < aHrefs.size(); i++) {
			String aHref = aHrefs.get(i);
			String href = aHref.split("|")[1];
			log.info(aHref);
			
		}
	}

}
