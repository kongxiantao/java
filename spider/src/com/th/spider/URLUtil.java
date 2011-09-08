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
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

/**
 * ����˵��
 * 
 * @author ���ĳ�
 * @version 1.0
 * @since 2011-5-3
 */
public class URLUtil {

	private static final Log log = LogFactory.getLog(FileSaver.class);

	/**
	 * ȡ��������Դ
	 * 
	 * @param uri
	 * @return
	 * @throws Exception
	 */
	public static byte[] getByte(String uri) throws Exception {
		//ģ�����ͻ��ˣ�����һ���أ��Լ�ѡ��ɣ�
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
		log.error("��ȡ�ļ�ʧ�ܣ�[url=" + uri + "]");
		return new byte[0];
	}
}
