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

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * ����
 * 
 * @author ���ĳ�
 * @version 1.0
 * @since 2011-5-3
 */
public class Link {
	
	private String url;

	private String name;

	public Link(String name, String url) {
		this.name = name;
		this.url = url;
	}


	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
