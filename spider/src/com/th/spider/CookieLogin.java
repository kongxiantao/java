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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Cookie��½ʵ��
 * @author ���ĳ�
 * @version 1.0
 * @since 2011-5-3
 */
public class CookieLogin {
	
	/**
	 *ģ���½
	 *�����Ҫ�õ�IEHTTPHeaders����׽��Щ��Ϣ�� 
	 */
	static void login()  throws Exception {
		Document doc = Jsoup.connect("/forum.php?mod=forumdisplay&fid=66")  .data("query", "Java")
		  .header("Accept", "*/*")
		  .header("Referer", "/forum.php?mod=forumdisplay&fid=66")
		  .header("Accept-Encoding", "gzip, deflate")
		  .header("If-Modified-Since", "Thu, 25 Nov 2010 06:51:00 GMT")
		  .header("If-None-Match", "1b0cb91-ee1-495db07517d00")
		  .userAgent("Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1; Trident/4.0; .NET CLR 2.0.50727; .NET CLR 3.0.04506.648; .NET CLR 3.5.21022; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)")
		  .header("Host", "www.xxxxxxx.com")
		  .header("Connection", "Keep-Alive")
		  .cookie("oI4T_2132_auth","37fcafwz%2B2XEIcBFkSIxTGqym7%2FkP%2Fm0gTXVRgl3bpA0KBbkYY8XE6fycTDuJsnVAn4g1So617eyU55xodShNt4")
		  .timeout(3000)
		  .post();
		System.out.println( doc.toString() );
	}
	
	public static void main(String[] args) throws Exception {
		login();
		
	}
}
