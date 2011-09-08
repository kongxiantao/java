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

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * �б�ҳ��
 * @author ���ĳ�
 * @version 1.0
 * @since 2011-5-3
 */
public class ListPage {
	
	/**
	 * ҳ���е�ÿ����������
	 */
	private List<Link> links;
	
	/**
	 * ��ǰҳID
	 */
	private String pageId;
	
	public ListPage(String pageId,List<Link> links) {
		this.links = links;
		this.pageId = pageId;
	}
	
	public String getPageId() {
		return pageId;
	}

	public void setPageId(String pageId) {
		this.pageId = pageId;
	}

	public List<Link> getLinks() {
		return links;
	}

	public void setLinks(List<Link> links) {
		this.links = links;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
