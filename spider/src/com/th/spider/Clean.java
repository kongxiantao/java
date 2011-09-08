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
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 对创建的空目录和图片无效的目录进行清理删除
 * @author 王文成
 * @version 1.0
 * @since 2011-5-4
 */
public class Clean {

	private static final Log log = LogFactory.getLog(Clean.class);

	/**
	 * 删除空目录
	 * 
	 * @param dir
	 */
	public static boolean removeEmptyDir(File dir) {
		if (dir.listFiles().length == 0) {
			log.info("删除空的目录![" + dir.getAbsolutePath() + "]");
			dir.delete();
			return true;
		}
		return false;
	}

	/**
	 * 删除无效文件目录
	 * 
	 * @param dir
	 */
	public static void removeWrongFileDir(File dir) {
		Set<Long> sizeSet = new HashSet<Long>();
		File[] pics = dir.listFiles();
		for (File pic : pics) {
			sizeSet.add(pic.length());
		}
		if (sizeSet.size() == 1) {
			for (File pic : pics) {
				pic.delete();
				log.info("删除无效的文件![" + pic.getAbsolutePath() + "]");
			}
			log.info("删除无效文件目录![" + dir.getAbsolutePath() + "]");
			dir.delete();
		}
	}

	/**
	 * 删除目录
	 */
	public static void removeDir() {
		File root = new File(Config.getSaveDir());
		File[] pageDirs = root.listFiles();
		for (File pageDir : pageDirs) {
			File[] dirs = pageDir.listFiles();
			for (File dir : dirs) {
				if (!removeEmptyDir(dir)) {
					removeWrongFileDir(dir);
				}
			}
		}
	}

	public static void main(String[] args) {
		removeDir();
	}
}
