package com.crawler;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.cralwer.contant.Constant;

public class StartUp {

	public void init() {
		initForConfig();
		startUp();
	}

	private void startUp() {
		long sina_e_time = 0;
		long sina_s_time = System.currentTimeMillis();
//		long qq_e_time = 0;
//		long qq_s_time = System.currentTimeMillis();
		while (true) {
			if (sina_s_time>=(sina_e_time+10000)) {
				if (Container.getContainer().getProperty("sina.crawl")
						.equals("true")) {
					if (Container.getContainer().getSinaFlog().equals("true")) {
						System.out.println("start crawler sina ");
						Downloader.getDownloader().submitCrawlSina();
//				sina_e_time = Long.valueOf( Container.getContainer().getProperty("sinaEndTime"));
						sina_e_time=System.currentTimeMillis();
					}
				}
			}
//			if (qq_s_time>=(qq_e_time+10000)) {
//				if (Container.getContainer().getProperty("qq.crawl")
//						.equals("true")) {
//					if (Container.getContainer().getQQFlog().equals("true")) {
//						System.out.println("start crawler qq ");
//						Downloader.getDownloader().submitCrawlQQ();
//					}
////					qq_e_time = Long.valueOf( Container.getContainer().getProperty("qqEndTime"));
//					qq_e_time = System.currentTimeMillis();
//				}
//			}
			sina_s_time = System.currentTimeMillis();
//			qq_s_time = System.currentTimeMillis();
		}
	}

//	private boolean getTimeFlog() {
//		long startTime = Long.valueOf(Container.getContainer().getProperty("startTime"));
//		long endTime = Long.valueOf(Container.getContainer().getProperty("endTime"));
//		if (startTime > endTime) {
//			return true;
//		}
//		return false;
//	}

	private void initForConfig() {
		Properties properties = load(Constant.CONFIG_FILE);
		Container.getContainer().addProperties(properties);
	}

	private Properties load(String configFile) {
		Properties properties = new Properties();
		InputStream in = getInputStream(configFile);
		try {
			properties.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return properties;
	}

	private InputStream getInputStream(String pathname) {
		InputStream in = StartUp.class.getClassLoader().getResourceAsStream(
				pathname);
		return in;
	}
}
