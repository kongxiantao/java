/* 
 * �������� 2011-5-11
 *
 * �ɶ���������˾
 * �绰��028-85425861 
 * ���棺028-85425861-8008 
 * �ʱࣺ610041 
 * ��ַ���ɶ������������·6�ŷ����������B��1001 
 * ��Ȩ����
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
 * ��ȡ������̳��ͼƬ
 * 
 * @author ���ĳ�
 * @version 1.0
 * @since 2011-5-11
 */
public class Exmaple2 {

	private static final Log log = LogFactory.getLog(Exmaple1.class);
	
	/**
	 * ץȡͼƬ���Ŀ¼
	 */
	private static final String PIC_DIR = "D:/girl";

	/**
	 * ���ӳ�ʱ
	 */
	private static final int TIME_OUT = 10000;

	static void forum(String url) throws Exception {
		Connection conn = Jsoup.connect(url).timeout(TIME_OUT);
		Document doc = conn.post();
		//<a href="http://bbs.wed114.cn/thread-34918-1-1.html" onclick="atarget(this)" class="xst">���ҵ�������ɴ�ը� ֵ���Ƽ�~~~</a>
		Elements alinks = doc.select("a[class=xst]");
		for (int i = 0; i < alinks.size(); i++) {
			Element alink = alinks.get(i);
			// ȡ�����ӵ�����
			String name = alink.text();
			// ȡ�����ӵ�����
			String href = alink.attr("href");
			log.info("����ץȡ [name=" + name + "][href=" + href + "]");
			try {
				// ��������
				post(href);
			} catch (Exception e) {
				log.error(e);
			}
		}
	}
	
	/**
	 * ��������URL
	 * @param url
	 * @throws Exception
	 */
	static void post(String url) throws Exception {
		// JSOP��������
		Connection conn = Jsoup.connect(url);
		// ���󷵻������ĵ�����
		Document doc = conn.post();
		// ѡ������class=zoom ��img��ǩ����
		Elements imgs = doc.select("img[class=zoom]");
		// ѭ��ÿ��img��ǩ
		for (int i = 0; i < imgs.size(); i++) {
			// ���������̫����...
			Thread.sleep(500);
			Element img = imgs.get(i);
			// ȡ��ͼƬ�����ص�ַ
			final String picURL = doc.baseUri() + img.attr("file");
			log.info(picURL);
			new Thread( new Runnable(){
				@Override
				public void run() {
					// ����ͼƬ
					try {
						save(picURL);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}).start();
		}
	}

	/**
	 * ����ͼƬ
	 * 
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
	 * ��ȡͼƬ�ֽ���
	 * 
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
		// ��ʼץȡͼƬ
		forum("http://bbs.wed114.cn/forum-3-1.html");
	}
}
