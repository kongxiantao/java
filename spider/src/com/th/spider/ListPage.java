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

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 列表页面
 * @author 王文成
 * @version 1.0
 * @since 2011-5-3
 */
public class ListPage {
	
	/**
	 * 页面中的每个帖子连接
	 */
	private List<Link> links;
	
	/**
	 * 当前页ID
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
