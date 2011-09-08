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

import org.jsoup.Connection;
import org.jsoup.Jsoup;

/**
 * Connection��������
 * 
 * @author ���ĳ�
 * @version 1.0
 * @since 2011-5-3
 */
public class ConnectionFactory {

	public static Connection getConnection(String url) {
		return getConnection(url, 3000);
	}

	public static Connection getConnection(String url, int timeout) {
		Connection conn = Jsoup.connect(url)
		.timeout(timeout);
		return conn;
	}

	public static void main(String[] args) {
	}
}
