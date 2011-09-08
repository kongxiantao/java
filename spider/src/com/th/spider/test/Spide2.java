package com.th.spider.test;


import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.MetaTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.th.spider.test.Exmaple1;

public class Spide2 {

	private static final Log log = LogFactory.getLog(Exmaple1.class);
	
	private static String encoding = null;

	public static void write(String content,String title,String path) {
		try {
			String header = "<!doctype html><html><meta charset='gbk'/><head><title>"+title+"</title></head><body>";
			String footer = "</body></html>";
			String _content = header + content + footer ;
			FileWriter fw = new FileWriter("d:/temp/"+path+"/"+title+".html", true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(_content);
			bw.close();
			fw.close();
			log.info("文件"+title+"写入成功");
		} catch (IOException e) {
			log.info("文件"+title+"写入失败");
		}

	}
	
	
	/*public static void save(String url,String title,String path) throws Exception {
		BufferedOutputStream out = null;
		try {
			byte[] bit = getByte(url);
			out = new BufferedOutputStream(new FileOutputStream("d:/temp/"+path+"/"+title+".txt", true));
			out.write(bit);
			out.flush();
			log.info("文件"+title+"写入成功");
		} finally {
			if (out != null){
				out.close();
			}
				
		}
	}
	public static byte[] getByte(String uri) throws Exception {
		DefaultHttpClient client = new DefaultHttpClient();
		client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,5000);
		HttpGet get = new HttpGet(uri);
		get.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,5000);
		try {
			HttpResponse resonse = client.execute(get);
			if (resonse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = resonse.getEntity();
				if (entity != null) {
					return EntityUtils.toByteArray(entity);
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		} finally {
			client.getConnectionManager().shutdown();
		}
		log.info(uri+"获取失败");
		return new byte[0];
	}*/

	public static String getHtmlContent(String url) throws Exception {
		String htmlContent = null;
		try {
			GetMethod getMethod = new GetMethod(url);
			HttpClient client = new HttpClient();
			client.getParams().setContentCharset("utf-8");
			client.getHostConfiguration()
					.getParams()
					.setParameter(
							HttpMethodParams.USER_AGENT,
							"Mozilla/5.0 (X11; U; Linux i686; zh-CN; rv:1.9.1.2) Gecko/20090803 Fedora/3.5.2-2.fc11 Firefox/3.5.2");

			int statusCode = client.executeMethod(getMethod);
			if (statusCode == 200) {
				htmlContent = getMethod.getResponseBodyAsString();
			}
		} catch (Exception e) {
			e.printStackTrace();
			htmlContent = "";
			log.info("获取网页内容失败");
		}
		return htmlContent;
	}
	
	public static String getPageCharset(String url) {
		try {
			GetMethod getMethod = new GetMethod(url);
			HttpClient client = new HttpClient();
			client.getHostConfiguration()
					.getParams()
					.setParameter(
							HttpMethodParams.USER_AGENT,
							"Mozilla/5.0 (X11; U; Linux i686; zh-CN; rv:1.9.1.2) Gecko/20090803 Fedora/3.5.2-2.fc11 Firefox/3.5.2");

			int statusCode = client.executeMethod(getMethod);
			String body = null;
			if (statusCode == 200) {
				if (getMethod.getResponseHeader("Content-Type") != null) {
					if (getMethod.getResponseHeader("Content-Type").getValue() != null) {
						encoding = getMethod
								.getResponseHeader("Content-Type")
								.getValue()
								.substring(
										getMethod
												.getResponseHeader(
														"Content-Type")
												.getValue().lastIndexOf("=") + 1,
										getMethod
												.getResponseHeader(
														"Content-Type")
												.getValue().length());
					}
				}
				if ("text/html".equals(encoding) || encoding == null) {
					body = getMethod.getResponseBodyAsString();
					// 新的编码获取方式
					encoding = pareCharset(body);
				}
			}
			if (encoding == null
					|| encoding.toLowerCase().lastIndexOf("text") != -1) {
				encoding = getMethod.getRequestCharSet();
				client.getParams().setContentCharset(encoding);
			}
		} catch (Exception e) {
			log.info("获取网页编码失败");
		}
		return encoding;
	}

	public static String pareCharset(String body) throws ParserException {
		Parser parser = Parser.createParser(body, null);
		NodeFilter metaFilter = new NodeClassFilter(MetaTag.class);
		NodeList meta = parser.extractAllNodesThatMatch(metaFilter);
		for (int i = 0; i < meta.size(); i++) {
			MetaTag metaTag = (MetaTag) meta.elementAt(i);
			if ((metaTag.getAttribute("http-equiv") != null)
					&& (metaTag.getAttribute("http-equiv")
							.equalsIgnoreCase("content-type"))) {
				String content = metaTag.getAttribute("content");
				if (null != content) {
					int index = content.toLowerCase().indexOf("charset");
					if (index != -1) {
						content = content.substring(index + "charset".length())
								.trim();
						if (content.startsWith("=")) {
							content = content.substring(1).trim();
							index = content.indexOf(";");
							if (index != -1) {
								content = content.substring(0, index);
							}
							if (content.startsWith("\"")
									&& content.endsWith("\"")
									&& (1 < content.length())) {
								content = content.substring(1,
										content.length() - 1);
							}
							if (content.startsWith("'")
									&& content.endsWith("'")
									&& (1 < content.length())) {
								content = content.substring(1,
										content.length() - 1);
							}
							return content;
						}

					}
				}
			} else {
				if ((metaTag.getAttribute("charset") != null)) {
					return metaTag.getAttribute("charset");
				}
			}
		}
		return null;
	}

	
	public static void getPageContent(String url,int page) throws Exception{
		String content = getHtmlContent(url);	
		if(content != null){
			Document doc = Jsoup.parse(content, getPageCharset(url)); 
			Elements alinks = doc.select("a[rel=bookmark]");
			for (int i = 0; i < alinks.size(); i++) {
				Element alink = alinks.get(i);	
				String href = alink.attr("href");
				String title = alink.text();
				log.info("获取第"+page+"页的第"+i+"篇文章开始");
				log.info(href);
				String htmlContent = getHtmlContent(href);
	        	if( htmlContent != null ){
	        		Document _doc = Jsoup.parse(htmlContent, "utf-8");  
		            Element elem = _doc.getElementById("body");
		            write(elem.html(),title,"johnResig");  
	        	}else{
	        		log.info(href+" 获取失败");
	        	}
				log.info("获取第"+page+"页的第"+i+"篇文章结束");
			}
		}
	}
	
	
	public static void go() throws Exception{
		for (int i = 0; i < 65; i++) {
			String url = "http://ejohn.org/category/blog/page/"+i+"/";
			log.info("获取第"+i+"页的内容开始");
			getPageContent(url,i);
			log.info("获取第"+i+"页的内容结束");
		}
	}
	
	public static void main(String[] args) throws Exception {
		log.info("game start");
		go();
//		String url = "http://ejohn.org/blog/next-steps-in-2011/";
//		save(url,"next-steps-in-2011","johnResig");
		/*String htmlContent = getHtmlContent(url);
    	if( htmlContent != null ){
    		Document _doc = Jsoup.parse(htmlContent, "utf-8");  
            Element elem = _doc.getElementById("body");
            write(elem.html(),"next-steps-in-2011","johnResig"); 
    	}else{
    		log.info(url+" 获取失败");
    	}*/
    	
		log.info("game over");
	}
}
