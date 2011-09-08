/* 
 * 创建日期 2011-5-5
 *
 * 成都天和软件公司
 * 电话：028-85425861 
 * 传真：028-85425861-8008 
 * 邮编：610041 
 * 地址：成都市武侯区航空路6号丰德万瑞中心B座1001 
 * 版权所有
 */
package com.th.spider;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 线程池 控制并发线程数
 * 
 * @author 王文成
 * @version 1.0
 * @since 2011-5-5
 */
public class ThreadPool {

	private static Executor executor;

	static {
		executor = Executors.newFixedThreadPool(Config.getThreadPoolSize());
	}

	public static void execute(Runnable run) {
		executor.execute(run);
	}
}
