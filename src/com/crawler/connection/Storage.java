package com.crawler.connection;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.crawler.bin.User;
import com.crawler.bin.Weibo;


public class Storage {
	private Connector conn = new Connector();
	
	public boolean userExist(String sid,int src){
		String sql="select * from weibo_user where sid = "+sid+" and src="+src;
		try {
			conn.open();
			ResultSet rs =  conn.query(sql);
			rs.last();
			int rowCount = rs.getRow();
			if (rowCount!=0) {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	public void updateUser(User user){
		int creatTime = (int) (System.currentTimeMillis()/1000);
		String sql = "insert into weibo_user (sid,sex,province,city,location,description,followers_count,friends_count,statuses_count,head,name,nick,src,creattime) values ("
				+ user.toString() + ","+creatTime+")";
//		System.out.println("sql----"+sql);
//		System.out.println("user info"+user.toString());
		try {
			conn.open();
			conn.executeInsert(sql);
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	public void updateWeibo(Weibo weibo){
//		return "'" + sid + "','" + source + "','" + image + "','" + geo_lat
//				+ "','" + geo_lon + "'," + timestamp + ",'" + head + "','"
//				+ name + "','" + nick + "','" + text + "'," + src;
		int creatTime = (int) (System.currentTimeMillis()/1000);
		String sql = "insert into weibo_info (sid,uid,source,image,geo_lat,geo_lon,timestamp,head,name,nick,text,src,creattime) values ("
				+ weibo.toString() +","+creatTime+ ")";
//		System.out.println("weibo info"+weibo.toString());
		try {
			conn.open();
			conn.executeInsert(sql);
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	

}
