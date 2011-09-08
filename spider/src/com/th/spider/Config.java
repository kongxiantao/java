/* 
 * �������� 2011-5-4
 *
 * �ɶ���������˾
 * �绰��028-85425861 
 * ���棺028-85425861-8008 
 * �ʱࣺ610041 
 * ��ַ���ɶ������������·6�ŷ����������B��1001 
 * ��Ȩ����
 */
package com.th.spider;

import java.io.File;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * ϵͳ����
 * 
 * @author ���ĳ�
 * @version 1.0
 * @since 2011-5-4
 */
public class Config {

	private static final Log log = LogFactory.getLog(Config.class);
	
	private static Properties prop = new Properties();
	
	static{
		try {
			InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(Constants.CONFIG_FILE);
			prop.load(in);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
			throw new RuntimeException("�����ļ���ȡ����![File=" + Constants.CONFIG_FILE + "]");
		}
		try {
			File saveDir = new File(getSaveDir());
			if( !saveDir.exists() ){
				saveDir.mkdir();
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
			throw new RuntimeException("�ļ�Ŀ¼��������![SaveDir=" + getSaveDir() + "]");
		}
	}
	
	/**
	 * ͼƬ����Ŀ¼
	 * @return
	 */
	public static String getSaveDir(){
		return prop.getProperty("save.dir", "D:/girl");
	}
	
	/**
	 * ��վ��·��
	 * @return
	 */
	public static String getUrlBase(){
		return prop.getProperty("url.base", "http://www.36mn.com/");
	}
	
	/**
	 * URLģ��
	 * @return
	 */
	public static String getUrlTemplate(){
		return prop.getProperty("url.template", "http://www.36mn.com/forum-62-#page#.html");
	}
	
	/**
	 * ��ʼҳ
	 * @return
	 */
	public static int getPageStart(){
		String start = prop.getProperty("page.start", "1");
		return Integer.valueOf(start);
	}
	
	/**
	 * ����ҳ
	 * @return
	 */
	public static int getPageEnd(){
		String end = prop.getProperty("page.end", "1");
		return Integer.valueOf(end);
	}
	
	/**
	 * �̳߳ش�С
	 * @return
	 */
	public static int getThreadPoolSize(){
		String size =  prop.getProperty("thread.pool.size", "50");
		return Integer.valueOf(size);
	}

	public static String asString() {
		return prop.toString();
	}
	
	public static void main(String[] args) {
		
	}
}
