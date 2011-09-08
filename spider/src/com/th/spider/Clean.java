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
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * �Դ����Ŀ�Ŀ¼��ͼƬ��Ч��Ŀ¼��������ɾ��
 * @author ���ĳ�
 * @version 1.0
 * @since 2011-5-4
 */
public class Clean {

	private static final Log log = LogFactory.getLog(Clean.class);

	/**
	 * ɾ����Ŀ¼
	 * 
	 * @param dir
	 */
	public static boolean removeEmptyDir(File dir) {
		if (dir.listFiles().length == 0) {
			log.info("ɾ���յ�Ŀ¼![" + dir.getAbsolutePath() + "]");
			dir.delete();
			return true;
		}
		return false;
	}

	/**
	 * ɾ����Ч�ļ�Ŀ¼
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
				log.info("ɾ����Ч���ļ�![" + pic.getAbsolutePath() + "]");
			}
			log.info("ɾ����Ч�ļ�Ŀ¼![" + dir.getAbsolutePath() + "]");
			dir.delete();
		}
	}

	/**
	 * ɾ��Ŀ¼
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
