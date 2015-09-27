package net.bingyan.hustpass.util;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.bingyan.hustpass.MyApplication;
import net.bingyan.hustpass.R;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.NotificationCompat;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.github.johnpersano.supertoasts.SuperCardToast;
import com.github.johnpersano.supertoasts.SuperToast;

public class Util {
	static String TAG = "Util";

	/**
	 * url 转换为文件名
	 * */
	public static String urltoFilename(File dir, String url) {
		try {
			return dir.getAbsolutePath() + File.separator
					+ URLEncoder.encode(url.replace("*", ""), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static String urltoFilename(String dirPath, String url) {
		try {
			return dirPath + File.separator
					+ URLEncoder.encode(url.replace("*", ""), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static boolean isEmpty(CharSequence cs) {
		return cs == null || cs.length() == 0;
	}

	/**
	 * 加密处理 <br>
	 * string :term + sid + "bingyan"
	 * */
	public static String shaString(String string) {
		return toHex(Hash("SHA-1", string.getBytes()));
	}

	public static String toHex(byte[] bytes) {
		if (bytes == null)
			return "";
		BigInteger bi = new BigInteger(1, bytes);
		return String.format("%0" + (bytes.length << 1) + "x", bi);
	}

	public static byte[] Hash(String algorithm, byte[] source) {
		try {
			java.security.MessageDigest md = java.security.MessageDigest
					.getInstance(algorithm);// "MD5","SHA-1","SHA-256","SHA-384","SHA-512"
			md.update(source);
			return md.digest();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String regexMatch(String regularExpression, CharSequence text) {
		Pattern p = Pattern.compile(regularExpression, Pattern.DOTALL);
		// Log.i(TAG,text.toString());
		Matcher m = p.matcher(text);
		if (m != null) {
			if (m.find())
				return m.group(1);
		}
		return null;
	}

	public static void toast(String s) {
        toast(s,Toast.LENGTH_SHORT);
	}

    public static void toast(String s, int time) {
        if(Thread.currentThread().getName().equals("main"))
        Toast.makeText(MyApplication.getAppContext(), s, time)
                .show();
    }

	public static void toast(int id) {
		String s = MyApplication.getAppContext().getResources().getString(id);
		toast(s);
	}



	public static boolean isWifiConnected() {
		ConnectivityManager connManager = (ConnectivityManager) MyApplication
				.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mWifi = connManager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		return mWifi.isConnected();

	}

	public static void createNotification(Context context, int id,
			String title, String content, Intent intent, boolean onGoing,
			boolean autoCancle) {
		createNotification(context, id, R.drawable.ic_launcher, title, content,
				intent, onGoing, autoCancle);
	}

	public static void createNotification(Context context, int id,
			int drawableId, String title, String content, Intent intent,
			boolean onGoing, boolean autoCancle) {
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
				intent, PendingIntent.FLAG_CANCEL_CURRENT);

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				context).setSmallIcon(drawableId).setContentTitle(title)
				.setContentText(content).setOngoing(onGoing)
				.setAutoCancel(autoCancle);
		mBuilder.setContentIntent(pendingIntent);

		notificationManager.notify(id, mBuilder.build());

	}

	public static void cancelNotification(Context context, int id) {
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancel(id);
	}

    /*********2.1************/

    public static int getSreenWidth(Context context){
        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        return displaymetrics.widthPixels;
    }
    public static int getSreenHeight(Context context){
        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        return displaymetrics.heightPixels;
    }

    public static String formatDateNoGMT(long timestamp, String format){
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
//        String date = sdf.format(new Date((int)percentX * 1000L));
        return new SimpleDateFormat(format).format(new Date(timestamp));
    }

    public static String formatDate(long timestamp, String dateFormat) {
        // example : "dd/MM/yyyy HH:mm:ss"
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
        return sdf.format(new java.util.Date(timestamp));
    }

    public static Date dateParse(String dateString,String dateFormat){
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        try {
            Date date =  sdf.parse(dateString);
            return date;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static String getPackageName(){
        return MyApplication.getInstance().getPackageName();
    }

    public  static int getVersionCode(){
        try {
            PackageInfo pinfo = MyApplication.getInstance().getPackageManager().getPackageInfo(getPackageName(), 0);
            return pinfo.versionCode;
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }

    }

    // View
    public static void superToast(Activity context,String msg){
        final SuperCardToast superCardToast;
        superCardToast = new SuperCardToast(context, SuperToast.Type.STANDARD);
        superCardToast.setAnimations(SuperToast.Animations.FADE);
        superCardToast.setDuration(SuperToast.Duration.MEDIUM);
        superCardToast.setBackground(SuperToast.Background.BLACK);
        superCardToast.setText(msg);
        superCardToast.show();

    }
 }
