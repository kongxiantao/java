/* 
 * �������� 2011-5-3
 *
 * �ɶ���������˾
 * �绰��028-85425861 
 * ���棺028-85425861-8008 
 * �ʱࣺ610041 
 * ��ַ���ɶ������������·6�ŷ����������B��1001 
 * ��Ȩ����
 */
package com.th.spider;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * һ��СС������
 * 
 * @author ���ĳ�
 * @version 1.0
 * @since 2011-5-3
 */
public class Spider {
	
	private static final Log log = LogFactory.getLog(Spider.class);

	private Link link;

	private String pageId;

	public Spider(String pageId, Link link) {
		this.link = link;
		this.pageId = pageId;
	}

	public void find() throws Exception {
		Connection conn = ConnectionFactory.getConnection(link.getUrl(),5000);
		Document doc = conn.post();
		final String dirName = link.getName();
		//�ҵ���ŮͼƬ�������Ҫ��������HTML�е�ͼƬԪ��������
		Elements elements = doc.select("img[onclick*=zoom]");
		for (int i = 0; i < elements.size(); i++) {
			Element element = elements.get(i);
			final String src = element.attr("src");
			final String fileName = i + ".jpg";
			ThreadPool.execute(new Runnable() {
				@Override
				public void run() {
					FileSaver saver = new FileSaver(pageId, dirName, fileName, src);
					try {
						saver.save();
					} catch (Exception e) {
						log.error("�����ļ�ʧ��![src=" + src + "] >" + e.getMessage());
					}
				}
			});

		}
	}

	public static void main(String[] args) throws Exception {
		Link link = new Link("��Τ����۾�������ģ������ĸо� ", "http://www.36mn.com/thread-22340-1-1.html");
		Spider sp = new Spider("1", link);
		sp.find();
	}

}
