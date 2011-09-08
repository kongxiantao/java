package com.th.spider.test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Exmaple4 {

	private static final Log log = LogFactory.getLog(Exmaple4.class);

	public static void go(String url) throws Exception {
		Connection conn = Jsoup.connect(url);
		Document doc = conn.post();
		Elements as = doc.select("a");
		for (int i = 0; i < as.size(); i++) {
			Element a = as.get(i);
			try {
				getContent(a.attr("href"),url);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static String getContent(String name,String url) throws Exception {
		String u = url + name;
		if( name.getBytes().length   !=   name.length()){
			u = url+java.net.URLEncoder.encode(name,"UTF-8");
		}
		Connection conn = Jsoup.connect(u);
		log.info(url+java.net.URLEncoder.encode(name,"UTF-8"));
		Document doc = conn.post();
		Elements contentDiv = doc.select("div[class=Section1]");
		write(url+name, contentDiv.get(0).html());
		return null;
	}

	public static void write(String url, String content) throws Exception {
		String filePath = "D:/深入分析Linux内核源码.html";
		String header = "<!doctype html><html><meta charset='gbk'/><head><title>深入分析Linux内核源码</title></head><body>";
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
		String url = "http://oss.org.cn/kernel-book/";
		go(url);
		log.info("============ game over ====================");
	}
}
