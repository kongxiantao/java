package com.th.spider.test;

import java.awt.Robot;
import java.awt.event.KeyEvent;

public class Spide {
    //IE
	private static void start(String url) throws Exception {
		Runtime.getRuntime().exec("explorer "+ url);
	}
	
	private static void stop(String url) throws Exception {
		Runtime.getRuntime().exec("taskkill /F /IM chrome.exe");
	}
	
	private static void run(String url){
		String osName = System.getProperty("os.name");
		if(url!=null && url.length() > 0){
			try {
				if (osName.startsWith("Windows")) {
					Robot robot = new Robot();
//					stop(url);
					robot.delay(3000);
					start(url);
					robot.keyRelease(KeyEvent.VK_F11);
					robot.delay(3000);
					//stop(url);
				} 
			} catch (Exception ex) {
				System.out.println("ä¯ÀÀÆ÷´ò¿ªÒì³£");
			}
		}
	}

	
	public static void main(String[] args) throws Exception{
		String url = "http://www.baidu.com";
		String url0 = "http://www.openv.com";
		System.out.println("============ start game ==================");
		Spide.run(url);
		Spide.run(url0);
		System.out.println("============ over game ==================");
	}
	
	

}
