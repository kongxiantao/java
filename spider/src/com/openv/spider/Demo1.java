package com.openv.spider;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Demo1 {
	
	private static final Log log = LogFactory.getLog(Demo1.class);
	
	public static void main(String[] args) throws Exception{
		String home = "http://progit.org/book/zh/";
		
		String html = SpiderUtil.getHtmlContent(home);
		if( html != null ){
			Document doc = Jsoup.parse(html,SpiderUtil.pareCharset(html));
			if( doc != null ){
				Elements as = doc.select("a");
				for (int i = 0; i < as.size(); i++) {
					Element a = as.get(i);
					String href = a.attr("href");
					if(href.contains("ch") && href.contains(".html")){
						String content = SpiderUtil.getHtmlContent(home + href);
						log.info(home + href);	
						Document _doc = Jsoup.parse(content,SpiderUtil.pareCharset(content));
						if( _doc != null){
							Element div = _doc.getElementById("content");
							String htmlContent = div.html();
							string2File(htmlContent,href);
						}
					}
				}
			}
		}else{
			log.info("url没有抓到内容");
		}
	}
	
	private static String filePath = "D:/temp/gitBook/";
	
	public static boolean string2File(String content,String name) {
		
		String _content ="<!doctype html><html><head>"
		+"<meta charset=utf-8' /><meta http-equiv='X-UA-Compatible' content='IE=EmulateIE7' />"
		+"<title>"+name+"</title></head><body>"+content+"</body></html>";
		boolean flag = true;
		try {
			File file=new File(filePath+name);
			if( !file.exists() ){
				 file.createNewFile();
	        }
			FileWriter fw = new java.io.FileWriter(file, true);  
			PrintWriter pw = new java.io.PrintWriter(fw);  
			pw.println(_content);  
			pw.close();  
			fw.close();
			log.info(name+" 文件写入完成");
		} catch (IOException e) {
			log.info(name+" 文件写入异常");
			flag = false;
			return flag;
		} 
		return flag;
	}

}
