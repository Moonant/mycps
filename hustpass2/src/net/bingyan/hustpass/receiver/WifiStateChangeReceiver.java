package net.bingyan.hustpass.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import net.bingyan.hustpass.module.wifi.WifiHelper;
import net.bingyan.hustpass.provider.WifiWidgetProvider;
import net.bingyan.hustpass.service.WifiConnectService;

public class WifiStateChangeReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		// WIFI開關
		if (intent.getAction().equals(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION)) {
			boolean connected = intent.getBooleanExtra(WifiManager.EXTRA_SUPPLICANT_CONNECTED, false);
			// 手動斷開wifi

			if (!connected) {
				updateHustWifiStatus(context, WifiWidgetProvider.STATE_LOGOUT);
			}

		}

		else if (intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
			NetworkInfo netInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);

			if (netInfo.isConnected() && WifiHelper.isHustWireless()) {
                    Intent serviceIntent = new Intent(context, WifiConnectService.class);
                    context.startService(serviceIntent);
			} else {
				// 沒信號斷開wifi
				updateHustWifiStatus(context, WifiWidgetProvider.STATE_LOGOUT);
			}

		}

	}

    void updateHustWifiStatus(Context context, int flag) {
        Bundle bundle = new Bundle();
        bundle.putInt(WifiWidgetProvider.STATE_TAG, flag);
        Intent wifiWidgetIntent = new Intent(WifiWidgetProvider.UPDATE_ACTION);
        wifiWidgetIntent.putExtras(bundle);
        context.sendBroadcast(wifiWidgetIntent);
    }
}
