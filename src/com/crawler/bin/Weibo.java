package com.crawler.bin;

public class Weibo {
	private String sid;
	private String uid;
	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	private String text;
	private String source;
	private String image;
	private String geo_lat;
	private String geo_lon;
	private int timestamp;
	private int src;
	private String head;
	private String name;
	private String nick;

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}


	public String getGeo_lat() {
		return geo_lat;
	}

	public void setGeo_lat(String geo_lat) {
		this.geo_lat = geo_lat;
	}

	public String getGeo_lon() {
		return geo_lon;
	}

	public void setGeo_lon(String geo_lon) {
		this.geo_lon = geo_lon;
	}

	public int getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(int timestamp) {
		this.timestamp = timestamp;
	}

	public int getSrc() {
		return src;
	}

	public void setSrc(int src) {
		this.src = src;
	}

	public String getHead() {
		return head;
	}

	public void setHead(String head) {
		this.head = head;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String toString() {
		return "'" + sid + "','" +uid+"','"+ parse(source) + "','" + parse(image) + "','" + geo_lat
				+ "','" + geo_lon + "'," + timestamp + ",'" + parse(head) + "','"
				+ parse(name) + "','" + parse(nick) + "','" + parse(text) + "'," + src;
	}
	private String parse(String text){
		if (text.indexOf("'")!=-1) {
			text = text.replace("'", "%46");
		}
		return text;
	}
}
