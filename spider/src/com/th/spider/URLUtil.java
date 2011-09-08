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
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

/**
 * 功能说明
 * 
 * @author 王文成
 * @version 1.0
 * @since 2011-5-3
 */
public class URLUtil {

	private static final Log log = LogFactory.getLog(FileSaver.class);

	/**
	 * 取得网络资源
	 * 
	 * @param uri
	 * @return
	 * @throws Exception
	 */
	public static byte[] getByte(String uri) throws Exception {
		//模拟多个客户端，还是一个呢？自己选择吧！
		HttpClient client = new DefaultHttpClient();
		client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,5000);
		HttpGet get = new HttpGet(uri);
		get.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,5000);
		try {
			HttpResponse resonse = client.execute(get);
			if (resonse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = resonse.getEntity();
				if (entity != null) {
					return EntityUtils.toByteArray(entity);
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		} finally {
			client.getConnectionManager().shutdown();
		}
		log.error("获取文件失败！[url=" + uri + "]");
		return new byte[0];
	}
}
