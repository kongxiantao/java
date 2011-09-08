/* 
 * �������� 2011-5-5
 *
 * �ɶ���������˾
 * �绰��028-85425861 
 * ���棺028-85425861-8008 
 * �ʱࣺ610041 
 * ��ַ���ɶ������������·6�ŷ����������B��1001 
 * ��Ȩ����
 */
package com.th.spider;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * �̳߳� ���Ʋ����߳���
 * 
 * @author ���ĳ�
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
