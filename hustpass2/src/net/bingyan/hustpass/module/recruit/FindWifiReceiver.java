package net.bingyan.hustpass.module.recruit;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;

import net.bingyan.hustpass.MyApplication;
import net.bingyan.hustpass.util.Pref;

import java.util.List;

/**
 * Created by jinge on 14-9-10.
 */
public class FindWifiReceiver extends BroadcastReceiver {

    public static final String BY_YY_WIFI = "bingyan_yunyuan";
    public static final String BY_QY_WIFI = "bingyan_qinyuan";
    public static final String BY_XJH_WIFI = "bingyan_xuanjianghui";
    public static final String BY_MS_WIFI = "bingyan_mianshi";

    @Override
    public void onReceive(Context context, Intent intent) {

        SharedPreferences pref = MyApplication.getSharedPreferences();
        if(pref.getInt(Pref.RECRUIT_STATE, 0) == 0 || pref.getInt(Pref.RECRUIT_STATE, 0) == 8) {
            return;
        }
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        List<ScanResult> results = wifiManager.getScanResults();
//        Log.d("wifi", results.size() + " " + results.toString());
        for (ScanResult r : results) {
            if (r.SSID.equals(BY_YY_WIFI)) {
                NotificationUtil.createNoti(context, NotificationUtil.TYPE_WIFI_YY);
                return;
            } else if (r.SSID.equals(BY_QY_WIFI)) {
                NotificationUtil.createNoti(context, NotificationUtil.TYPE_WIFI_QY);
                return;
            } else if (r.SSID.equals(BY_XJH_WIFI)) {
                NotificationUtil.createNoti(context, NotificationUtil.TYPE_WIFI_XJH);
                return;
            } else if (r.SSID.equals(BY_MS_WIFI)) {
                NotificationUtil.createNoti(context, NotificationUtil.TYPE_WIFI_MS);
                return;
            }
        }
        NotificationUtil.createNoti(context, NotificationUtil.TYPE_WIFI_OUT);
    }

}
