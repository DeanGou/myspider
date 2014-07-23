package com.crawler.parse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import com.crawler.bin.User;
import com.crawler.bin.Weibo;
import com.crawler.connection.Storage;
import com.org.json.JSONArray;
import com.org.json.JSONException;
import com.org.json.JSONObject;

public class ParseSina {
	public  Storage storage = new Storage();
	public List<Weibo> startParse(String res) {
		try {
			JSONArray jsonArray = new JSONArray(res);
			int size = jsonArray.length();
			List<Weibo> list = new ArrayList<Weibo>(size);
			for (int i = 0; i < size; i++) {
				JSONObject object = jsonArray.getJSONObject(i);
				Weibo weibo = parseToWeiboSina(object);
				if (weibo==null||weibo.equals(null)) {
					continue;
				}
				JSONObject user = object.getJSONObject("user");
				if (storage.userExist(user.getString("id"),2)) {
					User sUser = parseToUserSina(user);
					storage.updateUser(sUser);
				}
				list.add(weibo);
			}
			return list;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	private  User parseToUserSina(JSONObject userInfo) {
		User user = new User();
		try {
			user.setSid(String.valueOf(userInfo.getLong("id")));
			user.setName(userInfo.getString("name"));
			user.setNick(userInfo.getString("screen_name"));
			user.setSex(getSex(userInfo.getString("gender")));
			user.setHead(userInfo.getString("profile_image_url"));
			user.setProvince(userInfo.getInt("province"));
			user.setCity(userInfo.getInt("city"));
			user.setLocation(userInfo.getString("location"));
			user.setDescription(userInfo.getString("description"));
			user.setFollowers_count(userInfo.getInt("followers_count"));
			user.setFriends_count(userInfo.getInt("friends_count"));
			user.setStatuses_count(userInfo.getInt("statuses_count"));
			user.setSrc(1);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return user;
	}

	private int getSex(String sex) {
		if (sex.equals("m")) {
			return 1;
		}
		if (sex.equals("f")) {
			return 2;
		}
		return 0;
	}

	private  Weibo parseToWeiboSina(JSONObject info) {
		Weibo weibo = new Weibo();
		try {
			String geo = info.getString("geo");
			if (geo==null||geo.equals("null")) {
				return null;
			}
			if (setGeo(weibo,geo)==0) {
				return null;
			}
			JSONObject user = info.getJSONObject("user");
			weibo.setUid(String.valueOf(user.getLong("id")));
			weibo.setHead(user.getString("profile_image_url"));
			weibo.setImage(info.getString("original_pic"));
			weibo.setName(user.getString("name"));
			weibo.setNick(user.getString("screen_name"));
			weibo.setSid(String.valueOf(info.getLong("id")));
			weibo.setSource(info.getString("source"));
			weibo.setSrc(1);
			weibo.setText(info.getString("text"));
			weibo.setTimestamp(parseDate(info.getString("created_at")));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return weibo;
	}

	private int setGeo(Weibo weibo,String geoInfo) {
		try {
			JSONObject geo = new JSONObject(geoInfo);
			String g = geo.getString("coordinates");
			int start = g.indexOf("[");
			int end = g.indexOf("]");
			String gg = g.substring(start+1, end);
			String[] geos = gg.split(",");
			if (geos[0].equals("0")) {
				return 0;
			}
			weibo.setGeo_lat(geos[0]);
			weibo.setGeo_lon(geos[1]);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return 1;
	}

	private  int parseDate(String time) {
		SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		try {
			return (int)(sdf.parse(time).getTime()/1000);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}
}
