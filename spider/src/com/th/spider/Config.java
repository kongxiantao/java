/* 
 * 创建日期 2011-5-4
 *
 * 成都天和软件公司
 * 电话：028-85425861 
 * 传真：028-85425861-8008 
 * 邮编：610041 
 * 地址：成都市武侯区航空路6号丰德万瑞中心B座1001 
 * 版权所有
 */
package com.th.spider;

import java.io.File;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 系统配置
 * 
 * @author 王文成
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
			throw new RuntimeException("配置文件读取错误![File=" + Constants.CONFIG_FILE + "]");
		}
		try {
			File saveDir = new File(getSaveDir());
			if( !saveDir.exists() ){
				saveDir.mkdir();
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
			throw new RuntimeException("文件目录创建错误![SaveDir=" + getSaveDir() + "]");
		}
	}
	
	/**
	 * 图片保存目录
	 * @return
	 */
	public static String getSaveDir(){
		return prop.getProperty("save.dir", "D:/girl");
	}
	
	/**
	 * 网站根路径
	 * @return
	 */
	public static String getUrlBase(){
		return prop.getProperty("url.base", "http://www.36mn.com/");
	}
	
	/**
	 * URL模板
	 * @return
	 */
	public static String getUrlTemplate(){
		return prop.getProperty("url.template", "http://www.36mn.com/forum-62-#page#.html");
	}
	
	/**
	 * 开始页
	 * @return
	 */
	public static int getPageStart(){
		String start = prop.getProperty("page.start", "1");
		return Integer.valueOf(start);
	}
	
	/**
	 * 结束页
	 * @return
	 */
	public static int getPageEnd(){
		String end = prop.getProperty("page.end", "1");
		return Integer.valueOf(end);
	}
	
	/**
	 * 线程池大小
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
