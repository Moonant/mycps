package net.bingyan.hustpass.module.recruit;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import net.bingyan.hustpass.MyApplication;
import net.bingyan.hustpass.service.WifiConnectService;

import java.util.List;
import java.util.concurrent.ThreadFactory;

public class RecruitCheckWifi extends Service {

    public static final String BY_SSID = "bingyan";

    WifiManager wifiManager;
    WifiManager.WifiLock wifiLock;
    boolean cheaking;

    public RecruitCheckWifi() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
        wifiLock = wifiManager.createWifiLock(WifiManager.WIFI_MODE_FULL, "hustpass_wifilock");
        wifiLock.acquire();
        cheaking = true;

        new MyThread().start();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(wifiLock.isHeld()) {
            wifiLock.release();
        }
        cheaking = false;
    }

    class MyThread extends Thread {
        @Override
        public void run() {
            super.run();
            while(cheaking) {
                wifiManager.startScan();
//                Log.d("check", "checking" + "  " + wifiManager.getScanResults().toString());
                Intent intent = new Intent("net.bingyan.wifi.scan");
                sendBroadcast(intent);
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
