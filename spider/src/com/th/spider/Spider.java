/* 
 * 创建日期 2011-5-3
 *
 * 成都天和软件公司
 * 电话：028-85425861 
 * 传真：028-85425861-8008 
 * 邮编：610041 
 * 地址：成都市武侯区航空路6号丰德万瑞中心B座1001 
 * 版权所有
 */
package com.th.spider;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 一个小小的爬虫
 * 
 * @author 王文成
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
		//找到美女图片，这个需要分析返回HTML中的图片元素特征。
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
						log.error("保存文件失败![src=" + src + "] >" + e.getMessage());
					}
				}
			});

		}
	}

	public static void main(String[] args) throws Exception {
		Link link = new Link("林韦茹大眼睛，好身材，闪亮的感觉 ", "http://www.36mn.com/thread-22340-1-1.html");
		Spider sp = new Spider("1", link);
		sp.find();
	}

}
