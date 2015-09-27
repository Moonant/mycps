package net.bingyan.hustpass.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import net.bingyan.hustpass.helper.AccountManager;
import net.bingyan.hustpass.module.wifi.WifiActivity;
import net.bingyan.hustpass.module.wifi.WifiHelper;
import net.bingyan.hustpass.ui.AccountActivity;
import net.bingyan.hustpass.util.AppLog;
import net.bingyan.hustpass.util.Util;

/**
 * Created by ant on 14-8-14.
 */
public class WifiConnectService extends Service {
    AppLog mLog = new AppLog(getClass());
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        if(!WifiHelper.isWifiEnabled()){
            Util.toast("请打开wifi重试");
            return Service.START_NOT_STICKY;
        }

        if(!WifiHelper.isHustWireless()){
            Util.toast("请连接校园网wifi重试");
            return Service.START_NOT_STICKY;
        }

        AccountManager.WifiAccountManager manager = AccountManager.getWifiManger();

        boolean withuser = intent.getBooleanExtra("withuser",false);

        if(withuser){
            WifiHelper wifiHelper = new WifiHelper(this);
            wifiHelper.start(intent.getStringExtra("user"), intent.getStringExtra("psw"));
        }else {
            boolean temp = intent.getBooleanExtra("temp", false);
            if (temp) {

                if (!manager.isLogin()) {
                    goToLogin();
                    return Service.START_NOT_STICKY;
                }
                WifiHelper wifiHelper = new WifiHelper(this);
                wifiHelper.start(manager.getUid(), manager.getPsw());

            } else {

                if (!manager.isBound()) {
                    goToBound();
                    return Service.START_NOT_STICKY;
                }
                WifiHelper wifiHelper = new WifiHelper(this);
                wifiHelper.start(manager.getBoundUid(), manager.getBoundPsw());
                mLog.v(manager.getBoundUid() + ":" + manager.getBoundPsw());
            }
        }


        return Service.START_NOT_STICKY;
    }

    private void goToLogin() {
		Intent loginIntent = new Intent().setClass(this, WifiActivity.class);
		loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		loginIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		getApplication().startActivity(loginIntent);
    }

    private void goToBound(){
        Intent intent = new Intent(this, AccountActivity.class);
        intent.putExtra(AccountActivity.STATE_TAG,AccountActivity.STATE_WIFI_BOUND);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

}
