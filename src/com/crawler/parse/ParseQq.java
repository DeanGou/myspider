package com.crawler.parse;

import java.util.ArrayList;
import java.util.List;

import com.crawler.bin.User;
import com.crawler.bin.Weibo;
import com.crawler.connection.Storage;
import com.org.json.JSONArray;
import com.org.json.JSONException;
import com.org.json.JSONObject;

public class ParseQq {
	public  Storage storage = new Storage();
	public List<Weibo> startParse(String res) {
		List<Weibo> list = new ArrayList<Weibo>();
		String some = "";
		if (res.indexOf("\"image\":null")!=-1) {
			String [] sRes = res.split("\"image\":null");
			int size = sRes.length;
			for (int i = 0; i < size; i++) {
				if (i+1!=size) {
					some = some +sRes[i]+"\"image\":[\"\"]";
				}else{
					some = some + sRes[i];
				}
			}
		}
		try {
				JSONObject jsonObject = new JSONObject(some);
				JSONObject data = jsonObject.getJSONObject("data");
				JSONArray infos = data.getJSONArray("info");
				for (int i = 0; i < infos.length(); i++) {
					JSONObject info = infos.getJSONObject(i);
					Weibo weibo = parseToWeiboQq(info);
					if (weibo==null||weibo.equals(null)) {
						continue;
					}
					list.add(weibo);
				}
			return list;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void storageUser(User user){
		if (storage.userExist(user.getName(),1)) {
			storage.updateUser(user);
		}
	}

	private Weibo parseToWeiboQq(JSONObject info) {
		Weibo weibo = new Weibo();
		// weibo.setGeo_lat(geo_lat);
		// weibo.setGeo_lon(geo_lon);
		try {
			String geo = info.getString("geo");
//			System.out.println("geo is "+geo);
			String text = info.getString("text");
			if (text.equals("") || text == null) {
				text = info.getJSONObject("source").getString("text");
			}
			System.out.println(text);
			if (geo.equals(0)||geo==null||geo.equals("0")) {
				return null;
			}
			weibo.setHead(info.getString("head"));
			if (!(info.getJSONArray("image") == null || info.getJSONArray("image").equals(null))) {
				String image = info.getJSONArray("image").toString();
				image = image.replace("[\"", "").replace("\"]", "");
				weibo.setImage(image);
			}
			weibo.setName(info.getString("name"));
			weibo.setNick(info.getString("nick"));
			weibo.setSid(String.valueOf(info.getLong("id")));
			weibo.setSource(info.getString("from"));
			weibo.setSrc(1);
			weibo.setText(text);
			weibo.setTimestamp(info.getInt("timestamp"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return weibo;
	}

	public  User parseToUserQq(String res) {
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(res);
			JSONObject info = jsonObject.getJSONObject("data");
			User user = new User();
			user.setCity(info.getInt("city"));
			user.setDescription(info.getString("introduction"));
			user.setFollowers_count(info.getInt("fansnum"));
			user.setFriends_count(info.getInt("idolnum"));
			user.setHead(info.getString("head"));
			user.setLocation(info.getString("location"));
			user.setName(info.getString("name"));
			user.setNick(info.getString("nick"));
			user.setProvince(info.getInt("province"));
			user.setSex(info.getInt("sex"));
			user.setSrc(1);
			user.setStatuses_count(info.getInt("tweetnum"));
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return null;
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

}
