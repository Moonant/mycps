package net.bingyan.hustpass.http;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.util.concurrent.TimeUnit;

public class HttpUtil {

    public static String getString(String url){
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(5 * 1000, TimeUnit.MILLISECONDS);
        client.setReadTimeout(5 * 1000, TimeUnit.MILLISECONDS);
        Request request = new Request.Builder()
                .url(url)
                .build();

        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

//
//
//
//	/**
//	 * 只缓存字符串<br>
//	 * 不会设置上限，不会自动清除，<br>
//	 * 首页的缓存，讲座，新闻，院系通知
//	 * */
//	public static final String CACHE_ANNOUNCEMENT_INFO = "cache_announcement_info";
//	public static final String CACHE_ANNOUNCEMENT = "cache_announcement";
//	public static final String CACHE_FOODSHOP_LIST = "cache_food_list";
//	public static final String CACHE_HOME_NEWS = "cache_home_news";
//	public static final String CACHE_HOME_LECTURE = "cache_home_lecture";
//	public static final String CACHE_HOME_IKNOW = "cache_home_iknow";
//	public static final String CACHE_HOME_WEATHER = "cache_home_weather";
//	public static final String CACHE_HOME_SLIDE = "cache_home_slide";
//
//	public static final String CACHE_ClASSROOM = "cache_classroom";
//	public static final String CACHE_ELEC = "cache_elec";
//	public static String getCache(String k) {
//		String filePath = Util.urltoFilename(MyApplication.getAppContext()
//				.getCacheDir(), k);
//		File file = new File(filePath);
//		if (file.exists()) {
//			// return BitmapFactory.decodeFile(filePath);
//			String s;
//			String r = "";
//			try {
//				FileReader fr = new FileReader(file);
//				BufferedReader br = new BufferedReader(fr);
//				while ((s = br.readLine()) != null) {
//					r += s;
//				}
//				br.close();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			return r;
//		} else {
//			return null;
//		}
//	}
//
//	public static String getCache(String k, boolean time) {
//		String filePath = Util.urltoFilename(MyApplication.getAppContext()
//				.getCacheDir(), k);
//		File file = new File(filePath);
//
//		if (file.exists()) {
//			// return BitmapFactory.decodeFile(filePath);
//			Date now = new Date(java.lang.System.currentTimeMillis());
//			Date last = new Date(file.lastModified());
//			Log.i("Httpcache",""+now.getDate()+" "+last.getDate());
//			if (now.getDate() != last.getDate()) {
//				Log.i("HTTPcache", "http cache time out");
//				return null;
//			}
//			String s;
//			String r = "";
//			try {
//				FileReader fr = new FileReader(file);
//				BufferedReader br = new BufferedReader(fr);
//				while ((s = br.readLine()) != null) {
//					r += s;
//				}
//				br.close();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			return r;
//		} else {
//			return null;
//		}
//	}
//
//	public static void putCache(String k, String v) {
//		String filePath = Util.urltoFilename(MyApplication.getAppContext()
//				.getCacheDir(), k);
//		File file = new File(filePath);
//		if (!file.exists()) {
//			try {
//				file.createNewFile();
//				FileWriter fw = new FileWriter(file);
//				fw.write(v);
//				fw.close();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		} else {
//			try {
//				FileWriter fw = new FileWriter(file);
//				fw.write(v);
//				fw.close();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//	}

}
