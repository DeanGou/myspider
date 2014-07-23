package com.crawler;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Container {

	private static final Container container = new Container();

	private Container() {

	}

	public static Container getContainer() {
		return container;
	}

	private Map<String, String> context = new HashMap<String, String>();

	@SuppressWarnings("unchecked")
	public void addProperties(Properties properties) {
		Enumeration<String> enumeration = (Enumeration<String>) properties
				.propertyNames();
		while (enumeration.hasMoreElements()) {
			String key = enumeration.nextElement();
			context.put(key, properties.getProperty(key));
		}
	}

	public String getProperty(String key) {
		String str = context.get(key);
		if (str != null) {
			return str;
		}
		return null;
	}

	public Map<String, String> getMap() {
		return context;
	}

//	public void setProperty(String key, String value) {
//		context.put(key, value);
//	}
	
	
	public synchronized String getSinaFlog() {

		String flog = context.get("sina.flog");
		if (flog.equals("true")) {
			context.put("sinaFlog", "false");
			return "true";
		}
		return "false";
	}
	
	public synchronized void setSinaFlog(){
		context.put("sinaFlog", "true");
	}
	
	public synchronized void setQQFlog(){
		context.put("qqFlog", "true");
	}
	
	public synchronized String getQQFlog(){
		String flog = context.get("qq.flog");
		if (flog.equals("true")) {
			context.put("qqFlog", "false");
			return "true";
		}
		return null;
	}
	
	public String testFunction(){
		return "hello";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}


