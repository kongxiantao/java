package com.openv.spider;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

public class SpiderUtil {
	private static final Log log = LogFactory.getLog(SpiderUtil.class);
	
	private static String encoding = null;
	
	private static String filePath = "D:/javaData/";
	
	public SpiderUtil(){
		createDir();
	}
	
	public static void createDir(){
        File dir=new File(filePath);
        if(!dir.exists()){
        	 dir.mkdir();
        }
    }
	public static void deleDir(){
		// 的先递归删除该目录下的文件，才能删除该目录
        /*File dir=new File(filePath);
        if( dir.exists() && dir.isDirectory()){
        	 dir.delete();
        }*/
    }


	public static String getHtmlContent(String url) throws Exception{
		String htmlContent = null;
		try {
			GetMethod getMethod = new GetMethod(url);
			HttpClient client = new HttpClient();
			getPageCharset(url);
			if( encoding != null ){
				client.getParams().setContentCharset(encoding); 
				client.getHostConfiguration().getParams().setParameter(HttpMethodParams.USER_AGENT,"Mozilla/5.0 (X11; U; Linux i686; zh-CN; rv:1.9.1.2) Gecko/20090803 Fedora/3.5.2-2.fc11 Firefox/3.5.2");
				int statusCode = client.executeMethod(getMethod);
				if(statusCode == 200){
						htmlContent = getMethod.getResponseBodyAsString();
				}
			}
		} catch (Exception e) {e.printStackTrace();
			htmlContent = "" ;
			log.info("获取网页内容失败");
		}
		return htmlContent;
	}
	
	public static void getPageCharset (String url) throws Exception{
		try {
			GetMethod getMethod = new GetMethod(url);
			HttpClient client = new HttpClient();
			client.getHostConfiguration().getParams().setParameter(HttpMethodParams.USER_AGENT,"Mozilla/5.0 (X11; U; Linux i686; zh-CN; rv:1.9.1.2) Gecko/20090803 Fedora/3.5.2-2.fc11 Firefox/3.5.2");
			int statusCode = client.executeMethod(getMethod);
			String body = null ;
			if(statusCode == 200){
				if (getMethod.getResponseHeader("Content-Type") != null) {
					if (getMethod.getResponseHeader("Content-Type").getValue() != null) {
						encoding = getMethod.getResponseHeader("Content-Type").getValue().substring(getMethod.getResponseHeader("Content-Type").getValue().lastIndexOf("=") + 1,
										getMethod.getResponseHeader("Content-Type").getValue().length());
					}
				}
				if ("text/html".equals(encoding) || encoding == null){
					body = getMethod.getResponseBodyAsString();
					//新的编码获取方式
					encoding = pareCharset(body);
				}
			}
			if(encoding==null||encoding.toLowerCase().lastIndexOf("text")!=-1){
				encoding = getMethod.getRequestCharSet();
				client.getParams().setContentCharset(encoding); 
			}
		} catch (Exception e) {
			log.info("获取网页编码失败");
		}
	}
	
	public static String pareCharset(String body) throws ParserException{
		Parser parser = Parser.createParser(body,null);
		NodeFilter metaFilter = new NodeClassFilter(MetaTag.class); 
        NodeList meta = parser.extractAllNodesThatMatch(metaFilter);
        for(int i=0;i<meta.size();i++) { 
        	MetaTag   metaTag = (MetaTag)meta.elementAt(i); 
        	 if((metaTag.getAttribute("http-equiv")!=null)&&(metaTag.getAttribute("http-equiv").equalsIgnoreCase("content-type"))) { 
        		 String content = metaTag.getAttribute("content"); 
        		 if(null!= content) {
        			 int index = content.toLowerCase().indexOf("charset");
        			 if (index!=-1) { 
        				 content = content.substring(index+"charset".length()).trim(); 
                         if(content.startsWith("=")) { 
                                 content = content.substring(1).trim(); 
                                 index = content.indexOf(";"); 
                                 if(index!=-1) { 
                                     content = content.substring(0,index); 
                                 } 
                                 
                                 if(content.startsWith("\"")&&  content.endsWith("\"")&&(1<content.length())) { 
                                    content=content.substring(1,content.length()-1); 
                                 }
                                 
                                 if(content.startsWith("'")&& content.endsWith("'")&&(1<content.length())) { 
                                     content=content.substring(1,content.length()-1); 
                                 }
                              return content;
                         } 

                     }	 
                 }	 
             }else {
            	 if((metaTag.getAttribute("charset")!=null)) { 
            		 return metaTag.getAttribute("charset");
                 }
             }
        }
		return null;
	}

	public static void go(String url) throws Exception{
		
	}
	
	public static List<String> urlToHrefs(String url) throws Exception{
		List<String> list = new ArrayList<String>();
		log.info(url);
		String html = getHtmlContent(url);
		if( html != null ){
			Document doc = Jsoup.parse(html,encoding);
			if( doc != null ){
				Elements alinks = doc.select("a");
				for (int i = 0; i < alinks.size(); i++) {
					Element alink = alinks.get(i);
					String href = alink.attr("href");
					String data = alink.text();
					list.add(data+"|"+href);
				}
			}else{
				log.info("url="+url+" 转化为jsoup时失败");
			}
			
		}else{
			log.info("url="+url+" 没有抓到内容");
		}
		return list;
	}
	
	public static void write(String content) throws Exception{
		string2File(content,"url.txt");
	}
	
	public static boolean string2File(String content,String name) {
		boolean flag = true;
		try {
			File file=new File(filePath+name);
			if( !file.exists() ){
				 file.createNewFile();
	        }
			FileWriter fw = new java.io.FileWriter(file, true);  
			PrintWriter pw = new java.io.PrintWriter(fw);  
			pw.println(content);  
			pw.close();  
			fw.close();
		} catch (IOException e) {
			log.info(name+" 文件写入异常");
			flag = false;
			return flag;
		} 
		return flag;
	} 
	
	public static void main(String[] args)throws Exception {
//		String url = "http://www.sohu.com";//获取编码失败，获取编码的程序换是有问题啊
//		String url = "http://www.sina.com.cn";
		String url = "http://www.openv.com";
		log.info("=================== game start =======================");
		go(url);
		log.info("=================== game over =======================");
	}
	
}
