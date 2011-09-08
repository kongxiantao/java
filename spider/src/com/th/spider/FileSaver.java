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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author ���ĳ�
 * @version 1.0
 * @since 2011-5-3
 */
public class FileSaver {

	private static final Log log = LogFactory.getLog(FileSaver.class);

	private String pageId;

	private String dirName;

	private String fileName;

	private String url;
	
	public FileSaver(String pageId, String dirName, String fileName, String url) {
		this.pageId = pageId;
		this.dirName = dirName.replace("*", "");
		this.fileName = fileName;
		this.url = url;
	}

	public void save() throws Exception {
		String dir = getDir();
		String file = dir + "/" + fileName;
		BufferedOutputStream out = null;
		byte[] bit = URLUtil.getByte(url);
		if (bit.length > 0) {
			try {
				out = new BufferedOutputStream(new FileOutputStream(file));
				out.write(bit);
				out.flush();
				log.info("Create File success! [" + file + "]");
			} finally {
				if (out != null)
					out.close();
			}
		}
	}

	private String getDir() {
		String pagePath = Config.getSaveDir() + "/" + pageId;
		File pageDir = new File(pagePath);
		if (!pageDir.exists()) {
			pageDir.mkdir();
		}
		String filePath = pagePath + "/" + dirName;
		File fileDir = new File(filePath);
		if (!fileDir.exists()) {
			fileDir.mkdir();
		}
		return filePath;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public static void main(String[] args) throws Exception {
	}
}
