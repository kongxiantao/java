package com.th.spider.test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Exmaple3 {

	private static final Log log = LogFactory.getLog(Exmaple3.class);
	
	public static List<String> list = new ArrayList<String>();
	
	public static String URL = "http://oss.org.cn/kernel-book/";
	
	public static void go() throws Exception {
		Connection conn = Jsoup.connect(URL);
		Document doc = conn.post();
		Elements as = doc.select("a");
		for (int i = 0; i < as.size(); i++) {
			Element a = as.get(i);
			try {
				HtmlBook2epub.makeEpub(a.attr("href"),URL);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static List<String> getChapterIds() throws Exception {
		Connection conn = Jsoup.connect(URL);
		Document doc = conn.post();
		Elements as = doc.select("a");
		for (int i = 0; i < as.size(); i++) {
			Element a = as.get(i);
			try {
				list.add(URL+a.attr("href"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	

	public static String getContent(String name) throws Exception {
		String u = URL + name;
		if( name.getBytes().length   !=   name.length()){
			u = URL+java.net.URLEncoder.encode(name,"UTF-8");
		}
		Connection conn = Jsoup.connect(u);
		Document doc = conn.post();
		Elements contentDiv = doc.select("div[class=Section1]");
		log.info(URL+java.net.URLEncoder.encode(name,"UTF-8"));
		return contentDiv.get(0).html();
	}

	public static void write(String url, String content) throws Exception {
		String fileName = url.substring(url.lastIndexOf("/"));
		String filePath = "D:/深入分析Linux内核源码/" + fileName;
		String header = "<!doctype html><html><meta charset='gbk'/><head><title>"
				+ fileName + "</title></head><body>";
		String footer = "</body></html>";
		File log = new File(filePath);
		appendLog(log, header + content + footer);
	}

	public static void appendLog(File file, String newLog) {
		Scanner sc = null;
		PrintWriter pw = null;
		try {
			if (!file.exists())
			{
				File parentDir = new File(file.getParent());
				if (!parentDir.exists())
					parentDir.mkdirs();
				file.createNewFile();
			}
			sc = new Scanner(file);
			StringBuilder sb = new StringBuilder();
			while (sc.hasNextLine()){
				sb.append(sc.nextLine());
				sb.append("\r\n");
			}
			sc.close();
			pw = new PrintWriter(new FileWriter(file), true);
			pw.println(sb.toString());
			pw.println(newLog);
			pw.close();
		} catch (IOException ex) {
			log.info("文件写入异常");
		}
	}

	public static void main(String[] args) throws Exception {
		log.info("============ game start ====================");
		go();
		log.info("============ game over ====================");
	}
}
