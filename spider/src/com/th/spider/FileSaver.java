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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author 王文成
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
