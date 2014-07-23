package com.crawler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import weibo4j.Paging;
import weibo4j.WeiboException;
import weibo4j.http.HttpClient;
import weibo4j.http.PostParameter;
import weibo4j.http.Response;

import com.cralwer.contant.Constant;
import com.crawler.bin.User;
import com.crawler.bin.Weibo;
import com.crawler.connection.Storage;
import com.crawler.parse.ParseQq;
import com.crawler.parse.ParseSina;
import com.mime.qweibo.OauthKey;
import com.mime.qweibo.QParameter;
import com.mime.qweibo.QWeiboRequest;
import com.org.json.JSONArray;
import com.org.json.JSONException;

public class Downloader {
	private ExecutorService executor;
	private HttpClient http;
	private String sinaAppKey = Container.getContainer().getProperty("sina.appKey");;
	private String sinaAppSecret = Container.getContainer().getProperty(
			"sina.appSecret");
	private String sinaTokenKey = Container.getContainer().getProperty(
			"sina.tokenKey");
	private String sinaTokenSecret = Container.getContainer().getProperty(
			"sian.tokenSecret");
	private static Downloader downloader = new Downloader();
	private String qqAppKey = Container.getContainer().getProperty("qq.appKey");
	private String qqAppSecret = Container.getContainer().getProperty("qq.appSecret");
	private String qqTokenKey = Container.getContainer().getProperty("qq.tokenKey");
	private String qqTokenSecret = Container.getContainer().getProperty("qq.tokenSecret");
	private ParseSina parseSina = new ParseSina();
	private ParseQq parseQq = new ParseQq();
	private Storage storage = new Storage();
	

	private Downloader() {

		executor = Executors.newFixedThreadPool(10);
		System.setProperty("weibo4j.oauth.consumerSecret", sinaAppSecret);
		System.setProperty("weibo4j.oauth.consumerKey", sinaAppKey);
		http = new HttpClient();
		http.setToken(sinaTokenKey, sinaTokenSecret);
	}

	public static Downloader getDownloader() {
		return downloader;
	}

	public void submitCrawlSina() {
		CrawlSina sina = new CrawlSina();
		//sina.start();
		executor.submit(sina);
		}

	public void submitCrawlQQ() {
		CrawlQQ qq = new CrawlQQ();
//		qq.start();
		executor.submit(qq);
	}

	private class CrawlSina extends Thread {

		@Override
		public void run() {
			try {
				Paging page = new Paging();
				page.setCount(Integer.valueOf("200"));
				String res = sinaGet(Constant.SINA_URL, null, page, true).asString();
				if (goOrBack(res)) {
					List<Weibo> weibos = parseSina.startParse(res);
					int size = weibos.size();
					for (int i = 0; i < size; i++) {
						Weibo weibo = weibos.get(i);
						storage.updateWeibo(weibo);
//						if (i== (size-1)) {
//							Container.getContainer().getMap().put("sinaEndTime", String.valueOf(weibo.getTimestamp()));
//						}
					}
				}
			} catch (WeiboException e) {
				e.printStackTrace();
			}finally{
				Container.getContainer().setSinaFlog();
			}
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

		private boolean goOrBack(String res) {
			JSONArray jsonArray;
			try {
				jsonArray = new JSONArray(res);
				int size = jsonArray.length();
				if (size==0) {
					return false;
				}
				int startTime =  parseDate(jsonArray.getJSONObject(size-1).getString("created_at"));
				if (startTime>=Integer.valueOf(Container.getContainer().getProperty("endTime"))) {
					return true;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return false;
		}


		private Response sinaGet(String url, PostParameter[] params,
				Paging paging, boolean authenticate) throws WeiboException {
			if (null != paging) {
				List<PostParameter> pagingParams = new ArrayList<PostParameter>(
						4);
				if (-1 != paging.getMaxId()) {
					pagingParams.add(new PostParameter("max_id", String
							.valueOf(paging.getMaxId())));
				}
				if (-1 != paging.getSinceId()) {
					pagingParams.add(new PostParameter("since_id", String
							.valueOf(paging.getSinceId())));
				}
				if (-1 != paging.getPage()) {
					pagingParams.add(new PostParameter("page", String
							.valueOf(paging.getPage())));
				}
				if (-1 != paging.getCount()) {
					if (-1 != url.indexOf("search")) {
						// search api takes "rpp"
						pagingParams.add(new PostParameter("rpp", String
								.valueOf(paging.getCount())));
					} else {
						pagingParams.add(new PostParameter("count", String
								.valueOf(paging.getCount())));
					}
				}
				PostParameter[] newparams = null;
				PostParameter[] arrayPagingParams = pagingParams
						.toArray(new PostParameter[pagingParams.size()]);
				if (null != params) {
					newparams = new PostParameter[params.length
							+ pagingParams.size()];
					System.arraycopy(params, 0, newparams, 0, params.length);
					System.arraycopy(arrayPagingParams, 0, newparams,
							params.length, pagingParams.size());
				} else {
					if (0 != arrayPagingParams.length) {
						String encodedParams = HttpClient
								.encodeParameters(arrayPagingParams);
						if (-1 != url.indexOf("?")) {
							url += "&source=" + sinaAppKey + "&" + encodedParams;
						} else {
							url += "?source=" + sinaAppKey + "&" + encodedParams;
						}
					}
				}
				return get(url, newparams, authenticate);
			} else {
				return get(url, params, authenticate);
			}
		}

		private Response get(String url, PostParameter[] params,
				boolean authenticate) throws WeiboException {
			if (url.indexOf("?") == -1) {
				url += "?source=" + sinaAppKey;
			} else if (url.indexOf("source") == -1) {
				url += "&source=" + sinaAppKey;
			}
			if (null != params && params.length > 0) {
				url += "&" + HttpClient.encodeParameters(params);
			}
			return http.get(url, authenticate);
		}

	}

	private class CrawlQQ extends Thread {

		@Override
		public void run() {
			List<QParameter> parameters = new ArrayList<QParameter>();
			OauthKey oauthKey = new OauthKey();	
			oauthKey.customKey = qqAppKey;
			oauthKey.customSecrect = qqAppSecret;
			oauthKey.tokenKey = qqTokenKey;
			oauthKey.tokenSecrect = qqTokenSecret;


			parameters.add(new QParameter("format", "json"));
			parameters.add(new QParameter("pos", "0"));
			parameters.add(new QParameter("reqnum", String.valueOf(100)));
			QWeiboRequest request = new QWeiboRequest();
			try {
				String res = request.syncRequest(Constant.QQ_URL, "GET", oauthKey, parameters, null);
				List<Weibo> list = parseQq.startParse(res);
				int size = list.size();
				System.out.println("size is "+size);
//				for (int i = 0; i < size; i++) {
//					Weibo weibo = list.get(i);
//					User user = getQqUser(weibo.getName());
////					weibo.setUid(user.getSid());
//					parseQq.storageUser(user);
//					storage.updateWeibo(weibo);
////					if (i== (size-1)) {
////						Container.getContainer().getMap().put("qqEndTime", String.valueOf(weibo.getTimestamp()));
////					}
//				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		private User getQqUser(String name) {
			List<QParameter> parameters = new ArrayList<QParameter>();
			OauthKey oauthKey = new OauthKey();	
			oauthKey.customKey = qqAppKey;
			oauthKey.customSecrect = qqAppSecret;
			oauthKey.tokenKey = qqTokenKey;
			oauthKey.tokenSecrect = qqTokenSecret;


			parameters.add(new QParameter("format", "json"));
			parameters.add(new QParameter("name", name));
			QWeiboRequest request = new QWeiboRequest();
 			try {
				String res = request.syncRequest(Constant.QQ_USERURL, "GET", oauthKey, parameters, null);
//				parseQq.parseToUserQq(res);
				return parseQq.parseToUserQq(res);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

	}

}
