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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * ����������
 * 
 * @author Fantasy
 * @version 1.0
 * @since 2011-5-3
 */
public class Main {

	private static final Log log = LogFactory.getLog(Main.class);
	
	/**
	 * ץȡ��ʼ
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		//�ӵ�1ҳץ����5ҳ��ͼƬ
		int start = Integer.valueOf(Config.getPageStart());
		int end = Integer.valueOf(Config.getPageEnd());
		log.info("�ļ�����·��[" + Config.getSaveDir() + "]");
		start(start,end);
	}
	
	/**
	 * ��ʼ
	 * @param pageStart
	 * @param pageEnd
	 * @throws Exception
	 */
	public static void start(int pageStart , int pageEnd) throws Exception {
		List<ListPage> pages = getPages(pageStart, pageEnd);
		for (ListPage page : pages) {
			final String pageId = page.getPageId();
			for (final Link link : page.getLinks()) {
				// ���������̫����... С�ı��������ܾ�
				Thread.sleep(500);
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							new Spider(pageId, link).find();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}).start();
			}
		}
	}

	/**
	 * ȡ���б�ҳ��
	 * 
	 * @param listUrl
	 * @return
	 * @throws Exception
	 */
	private static List<ListPage> getPages(int start, int end) throws Exception {
		List<ListPage> pages = new ArrayList<ListPage>();
		for (int i = start; i <= end; i++) {
			Thread.sleep(500);
			final String pageId = String.valueOf(i);
			String url = Config.getUrlTemplate().replace("#page#", pageId);
			ListPage page = getListPage(pageId, url);
			pages.add(page);
			log.info("\n" + page);
		}
		return pages;
	}

	/**
	 * ȡ���б�ҳ��
	 * 
	 * @param listUrl
	 * @return
	 * @throws Exception
	 */
	private static ListPage getListPage(String pageId, String listUrl) throws Exception {
		Connection conn = ConnectionFactory.getConnection(listUrl, 5000);
		Document doc = conn.post();
		Elements elements = doc.select("span[id*=thread] a");
		List<Link> links = new ArrayList<Link>();
		for (int i = 0; i < elements.size(); i++) {
			Element element = elements.get(i);
			String name = element.text();
			String href = Config.getUrlBase() + element.attr("href");
			links.add(new Link(name, href));
		}
		return new ListPage(pageId, links);
	}
}
