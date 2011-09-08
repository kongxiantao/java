/* 
 * 创建日期 2011-5-11
 *
 * 成都天和软件公司
 * 电话：028-85425861 
 * 传真：028-85425861-8008 
 * 邮编：610041 
 * 地址：成都市武侯区航空路6号丰德万瑞中心B座1001 
 * 版权所有
 */
package com.th.spider.test;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 单个帖子抓取
 * 
 * @author 王文成
 * @version 1.0
 * @since 2011-5-11
 */
public class Exmaple1 {

	private static final Log log = LogFactory.getLog(Exmaple1.class);
	
	/**
	 * 抓取图片存放目录
	 */
	private static final String PIC_DIR = "D:/girl";
	
	/**
	 * 链接超时
	 */
	private static final int TIME_OUT = 5000;
	
	/**
	 * 处理帖子URL
	 * @param url
	 * @throws Exception
	 */
	static void go(String url) throws Exception {
		// JSOP创建链接
		Connection conn = Jsoup.connect(url);
		// 请求返回整个文档对象
		Document doc = conn.post();
		// 选择所有class=zoom 的img标签对象
		Elements imgs = doc.select("img[class=zoom]");
		// 循环每个img标签
		for (int i = 0; i < imgs.size(); i++) {
			Element img = imgs.get(i);
			// 取得图片的下载地址
			String picURL = doc.baseUri() + img.attr("file");
			log.info(picURL);
			// 保存图片
			save(picURL);
		}
	}
	
	//<img src="static/image/common/none.gif" file="data/attachment/forum/201105/08/174412nz3jq4z90s33s2t0.jpg" width="770" class="zoom" onclick="zoom(this, this.src)" id="aimg_180565" onmouseover="showMenu({'ctrlid':this.id,'pos':'12'})" alt="img_src_29620.jpg" title="img_src_29620.jpg" />
	//doc.select("img[class=zoom]")
	/**
	 * 保存图片
	 * @param url
	 * @param i
	 * @throws Exception
	 */
	static void save(String url) throws Exception {
		String fileName = url.substring(url.lastIndexOf("/"));
		String filePath = PIC_DIR + "/" + fileName;
		BufferedOutputStream out = null;
		byte[] bit = getByte(url);
		if (bit.length > 0) {
			try {
				out = new BufferedOutputStream(new FileOutputStream(filePath));
				out.write(bit);
				out.flush();
				log.info("Create File success! [" + filePath + "]");
			} finally {
				if (out != null)
					out.close();
			}
		}
	}
	
	/**
	 * 获取图片字节流
	 * @param uri
	 * @return
	 * @throws Exception
	 */
	static byte[] getByte(String uri) throws Exception {
		HttpClient client = new DefaultHttpClient();
		client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, TIME_OUT);
		HttpGet get = new HttpGet(uri);
		get.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, TIME_OUT);
		try {
			HttpResponse resonse = client.execute(get);
			if (resonse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = resonse.getEntity();
				if (entity != null) {
					return EntityUtils.toByteArray(entity);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			client.getConnectionManager().shutdown();
		}
		return new byte[0];
	}

	public static void main(String[] args) throws Exception {
		// 开始抓取图片
		go("http://bbs.wed114.cn/thread-35586-1-1.html");
	}
}
