package net.bingyan.hustpass.module.wifi;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.umeng.analytics.MobclickAgent;

import net.bingyan.hustpass.MyApplication;
import net.bingyan.hustpass.R;
import net.bingyan.hustpass.provider.WifiWidgetProvider;
import net.bingyan.hustpass.service.WifiConnectService;
import net.bingyan.hustpass.util.AppLog;
import net.bingyan.hustpass.util.Pref;
import net.bingyan.hustpass.util.Util;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ant on 14-8-10.
 */
public class WifiHelper {
    AppLog mLog = new AppLog(getClass());
    OkHttpClient client ;
    Context context;

    public static boolean isLoading = false;

    public static final String[] HUSTWIFI_SSID = new String[] { "HUST_WIRELESS", "HUST-WIRELESS","HUST-WIRELESS-5.8G","\"HUST-WIRELESS-5.8G\"","\"HUST_WIRELESS\"","\"HUST-WIRELESS\"" };
    public static final int NOTI_ID = 003;

    static final public int WIFI_NORMAL = 0; // 网络连接正常，不需要登录
    static final public int WIFI_ERROR = 1 ; // 连接异常,不是校园网, 检查网络连接
    static final public int WIFI_SUCCESS = 2; // 登录成功
    static final public int WIFI_FAILED = 3; // 登录失败

    public WifiHelper(Context context){
        this.context = context;
        client = new OkHttpClient();
        client.setConnectTimeout(5 * 1000, TimeUnit.MILLISECONDS);
        client.setReadTimeout(5 * 1000, TimeUnit.MILLISECONDS);
    }

    public void start(final String uid, final  String pwd){

        if(isLoading){
            return;
        }

        MobclickAgent.onEvent(context, "wifi_login");

        Util.toast("校园网，登录中...");
        isLoading = true;
        updateHustWifiStatus(WifiWidgetProvider.STATE_LOADING);

        new Thread(new Runnable() {
            @Override
            public void run() {
                mLog.v("startConnect, Thread: "+ Thread.currentThread());

                try {
                    Message message = new Message();
                    int state = login(uid, pwd, message);
                    message.what = state;
                    handler.sendMessage(message);

                    isLoading = false;

                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }).start();
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WIFI_NORMAL:
                    Util.toast("网络连接正常，不需要登录");
                    break;
                case WIFI_ERROR:
                    Util.toast("连接异常, 不是校园网, 检查网络连接");
                    break;
                case WIFI_FAILED:
                    Util.toast((String)msg.obj);
                    break;
                case WIFI_SUCCESS:
                    Util.toast("登录成功");
                    break;
            }

            if(msg.what == WIFI_SUCCESS){
                updateHustWifiStatus(WifiWidgetProvider.STATE_LOGIN);
            }else {
                updateHustWifiStatus(WifiWidgetProvider.STATE_LOGOUT);
            }

            super.handleMessage(msg);
        }
    };


    private String getTestHtml() throws IOException{
        String url = "http://www2.hustonline.net/user/testemail";

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();

        return response.body().string();
    }



    public static boolean isHustWireless() {
        WifiManager wifiManager = (WifiManager)MyApplication.getInstance().getSystemService(Context.WIFI_SERVICE);
        String ssid = wifiManager.getConnectionInfo().getSSID();


        if (ssid != null) {
            for (String s : HUSTWIFI_SSID){
                if (ssid.equals(s)){
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean isWifiEnabled(){
        WifiManager wifiManager = (WifiManager)MyApplication.getInstance().getSystemService(Context.WIFI_SERVICE);
        return wifiManager.isWifiEnabled();
    }
    public static void createWifiNotification(Context context, int status) {
        Intent intent = new Intent(context, WifiConnectService.class);

        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent pendingIntent = PendingIntent.getService(context, 0,
                intent, PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                context)
                .setSmallIcon(
                        WifiWidgetProvider.STATE_LOGIN == status ? R.drawable.ic_wifi_on
                                : R.drawable.ic_wifi_off)
                .setContentTitle(
                        String.format("%s", "校园网 HUST-WIRELESS"))
                .setContentText(
                        WifiWidgetProvider.STATE_LOGIN == status ? "登录成功,点击重新登录"
                                : "点击登录").setOngoing(true).setAutoCancel(true);
        mBuilder.setContentIntent(pendingIntent);
        notificationManager.notify(NOTI_ID, mBuilder.build());
    }
//    String.format("%s - %s", "校园网", Pref.getPref()
//            .getString(Pref.WIFI_USER, "")))

    void updateHustWifiStatus(int flag) {
        Bundle bundle = new Bundle();
        bundle.putInt(WifiWidgetProvider.STATE_TAG, flag);
        Intent wifiWidgetIntent = new Intent(WifiWidgetProvider.UPDATE_ACTION);
        wifiWidgetIntent.putExtras(bundle);
        context.sendBroadcast(wifiWidgetIntent);
    }

    public int login(String uid, String pwd, Message message) throws IOException{



        // ( 1 )
        String html = getTestHtml();

        if(html.equals("False")){
            mLog.v("网络连接正常，不需要登录");
            return WIFI_NORMAL;
        }

        Pattern pattern = Pattern.compile("location.href=['\"](.*?)['\"]"); // 无法连接指定网页 ， 继续
        Matcher matcher = pattern.matcher(html);

        if(!matcher.find()){
            mLog.v("连接异常, 不是校园网, 检查网络连接");
            return WIFI_ERROR;
        }


        // ( 2 )
        String url = (matcher.group(1));  // 得到 登录界面 url
        mLog.v("登录界面1:"+url);

        Request request = new Request.Builder()
                .url(url)
                .header("User-Agent","Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.17 (KHTML, like Gecko) Chrome/24.0.1312.56 Safari/537.17")
                .build();

        Response response = client.newCall(request).execute();


        // ( 3 ) 模拟表单提交
        url = response.request().urlString();
        mLog.v("登录界面2:"+url);

//        String s1 = "HKchinaMobileLogin.html?";
//        String s2 = "userV2.do?method=login&param=true&";
//        url = url.replace(s1,s2);  // 表单提交 url

        url = url.replaceAll("eportal/.*\\?","eportal/userV2.do?method=login&param=true&");
        mLog.v("表单提交："+ url);

        MediaType JSON = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");
        String json = "username="+uid+"&pwd="+pwd+"&validcode=no_check&is_check=true";
        mLog.v("表单Data:"+json);

        RequestBody body = RequestBody.create(JSON, json);
        request = new Request.Builder()
                .url(url)
                .post(body)
                .header("User-Agent","Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.17 (KHTML, like Gecko) Chrome/24.0.1312.56 Safari/537.17")
                .build();

        response = client.newCall(request).execute();

        // ( 4 )
        pattern = Pattern.compile("function showAuthErrorMessage([\\s\\S]*?)showError\\('(.*?)'\\)");
        matcher = pattern.matcher(response.body().string());

        if(!matcher.find()){
            mLog.v("not found msg");
            return WIFI_SUCCESS;  // ( STATE 3 ) 没得到 正确的返回界面 XXXX  -->> 正确
        }

        String msg = matcher.group(2);

        if(msg.equals("")){
            mLog.v("msg ==  ");
            return WIFI_SUCCESS;  // ( STATE 4 )  登录成功?
        }

        message.obj = msg;
        mLog.v("failed:"+msg); // ( STATE 5 )  登录失败?

        return WIFI_FAILED;
    }

}
