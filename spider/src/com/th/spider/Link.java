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

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 链接
 * 
 * @author 王文成
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
