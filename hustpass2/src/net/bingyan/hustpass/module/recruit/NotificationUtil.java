package net.bingyan.hustpass.module.recruit;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;

import net.bingyan.hustpass.MyApplication;
import net.bingyan.hustpass.R;
import net.bingyan.hustpass.util.Pref;

/**
 * Created by jinge on 14-9-10.
 */
public class NotificationUtil {

    public static final int TYPE_WIFI_YY = 1;
    public static final int TYPE_WIFI_QY = 2;
    public static final int TYPE_WIFI_XJH = 3;
    public static final int TYPE_WIFI_MS = 4;
    public static final int TYPE_WIFI_OUT = 5;
    public static final int TYPE_PUSH_LY = 6;
    public static final int TYPE_PUSH_XJH = 7;

    public static void createNoti(Context context, int type) {

        SharedPreferences pref = MyApplication.getSharedPreferences();
        String wifi = pref.getString(Pref.RECRUIT_WIFI, "null");
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        int drawableId = R.drawable.ic_launcher;
        int id = 0;
        String title = "";
        String content = "";

        switch(type) {
            case TYPE_WIFI_OUT:
                id = 10;

                if(wifi == null || wifi.trim().equals("") || wifi.equals("null")) {
                    return;
                }
                if(wifi.equals(FindWifiReceiver.BY_YY_WIFI)) {
                    title = "冰岩作坊";
                    content = "谢谢您对冰岩作坊的关注哦！";
                } else if (wifi.equals(FindWifiReceiver.BY_QY_WIFI)) {
                    title = "冰岩作坊";
                    content = "谢谢您对冰岩作坊的关注哦！";
                } else if (wifi.equals(FindWifiReceiver.BY_XJH_WIFI)) {
                    title = "冰岩作坊";
                    content = "谢谢您来参加我们的招新宣讲会哦！";
                }else if (wifi.equals(FindWifiReceiver.BY_MS_WIFI)) {
                    title = "冰岩作坊";
                    content = "谢谢您参加招新面试，静静等候结果通知吧！";
                }
                pref.edit().putString(Pref.RECRUIT_WIFI, "null").apply();
                break;
            case TYPE_WIFI_YY:
                if(!(wifi == null || wifi.trim().equals("") || wifi.equals("null"))) {
                    return;
                }
                id = 11;
                title = "冰岩作坊招新路演";
                content = "欢迎到韵苑路口参观冰岩作坊的招新路演哦！";
                pref.edit().putString(Pref.RECRUIT_WIFI, FindWifiReceiver.BY_YY_WIFI).apply();
                break;
            case TYPE_WIFI_QY:
                if(!(wifi == null || wifi.trim().equals("") || wifi.equals("null"))) {
                    return;
                }
                id = 12;
                title = "冰岩作坊招新路演";
                content = "欢迎到沁苑路口参观冰岩作坊的招新路演哦！";
                pref.edit().putString(Pref.RECRUIT_WIFI, FindWifiReceiver.BY_QY_WIFI).apply();
                break;
            case TYPE_WIFI_XJH:
                if(!(wifi == null || wifi.trim().equals("") || wifi.equals("null"))) {
                    return;
                }
                id = 13;
                title = "欢迎参加冰岩作坊招新宣讲会";
                content = "您的抽奖码为:";
                content += pref.getString(Pref.RECRUIT_LOTTERY_NUM, "");
                pref.edit().putString(Pref.RECRUIT_WIFI, FindWifiReceiver.BY_XJH_WIFI).apply();
                break;
            case TYPE_WIFI_MS:
                if(!(wifi == null || wifi.trim().equals("") || wifi.equals("null"))) {
                    return;
                }
                id = 14;
                title = "欢迎参加冰岩作坊的招新面试";
                content = "祝你好运哦！";
                pref.edit().putString(Pref.RECRUIT_WIFI, FindWifiReceiver.BY_MS_WIFI).apply();
                break;
            case TYPE_PUSH_LY:
                id = 15;
                title = "冰岩作坊招新路演中";
                content = "欢迎到韵苑路口和沁苑路口围观";
                break;
            case TYPE_PUSH_XJH:
                id = 16;
                title = "冰岩作坊";
                content = "今晚东九C102招新宣讲会，有现场抽奖哦";
                break;
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                context).setSmallIcon(R.drawable.ic_launcher).setContentTitle(title)
                .setContentText(content).setOngoing(false)
                .setAutoCancel(false)
                .setVibrate(new long[]{100, 500, 100, 500})
                .setDefaults(Notification.DEFAULT_SOUND);
        notificationManager.notify(id, builder.build());
    }

}
