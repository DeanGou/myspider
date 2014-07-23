package com.crawler.bin;

public class User {
	private String sid;
	private String name;
	private String nick;
	private int sex;
	private String head;
	private int province;
	private int city;
	private String location;
	private String description;
	private int followers_count;
	private int friends_count;
	private int statuses_count;
	private int src;

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getHead() {
		return head;
	}

	public void setHead(String head) {
		this.head = head;
	}

	public int getProvince() {
		return province;
	}

	public void setProvince(int province) {
		this.province = province;
	}

	public int getCity() {
		return city;
	}

	public void setCity(int city) {
		this.city = city;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getFollowers_count() {
		return followers_count;
	}

	public void setFollowers_count(int followers_count) {
		this.followers_count = followers_count;
	}

	public int getFriends_count() {
		return friends_count;
	}

	public void setFriends_count(int friends_count) {
		this.friends_count = friends_count;
	}

	public int getStatuses_count() {
		return statuses_count;
	}

	public void setStatuses_count(int statuses_count) {
		this.statuses_count = statuses_count;
	}

	public int getSrc() {
		return src;
	}

	public void setSrc(int src) {
		this.src = src;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String toString() {

		return "'" + sid + "'," + sex + "," + province + "," + city + ",'"
				+ parse(location) + "','" +parse(description)+ "'," + followers_count + ","
				+ friends_count + "," + statuses_count + ",'" + parse(head) + "','"
				+ parse(name) + "','" + parse(nick) + "'," + src;
	}
	
	private String parse(String text){
		if (text.indexOf("'")!=-1) {
			text = text.replace("'", "%46");
		}
		return text;
	}
}
