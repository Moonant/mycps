package net.bingyan.hustpass.provider;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import net.bingyan.hustpass.R;
import net.bingyan.hustpass.module.wifi.WifiHelper;
import net.bingyan.hustpass.service.WifiConnectService;
import net.bingyan.hustpass.util.Util;

public class WifiWidgetProvider extends AppWidgetProvider {

    public static final String UPDATE_ACTION = "hustpass2.edunet.action.STATUS_UPDATED";

    public static final String STATE_TAG = "wifiStateTag";
    public static final int STATE_LOGOUT = 0;
    public static final int STATE_LOGIN = 1;
    public static final int STATE_LOADING = 2;

    private int loginStatus = 0;


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        ComponentName thisWidget = new ComponentName(context, WifiWidgetProvider.class);
        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        switch (loginStatus) {
            case STATE_LOGOUT:
                Util.cancelNotification(context, WifiHelper.NOTI_ID);
                break;
            case STATE_LOGIN:
                WifiHelper.createWifiNotification(context, WifiWidgetProvider.STATE_LOGIN);
                break;
        }
        for (int appWidgetId : allWidgetIds) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.wifi_widget);

            switch (loginStatus) {
                case STATE_LOGOUT:
                    remoteViews.setImageViewResource(R.id.btn_toggle,
                            R.drawable.widget_wifi_off);
                    break;
                case STATE_LOGIN:
                    remoteViews.setImageViewResource(R.id.btn_toggle,
                            R.drawable.widget_wifi_on);
                    break;
                case STATE_LOADING:
                    remoteViews.setImageViewResource(R.id.btn_toggle,
                            R.drawable.widget_wifi_off);
                    break;
            }

            Intent intent = new Intent(context, WifiConnectService.class);

            PendingIntent pendingIntent = PendingIntent.getService(context, 0,
                    intent, PendingIntent.FLAG_UPDATE_CURRENT);

            remoteViews.setOnClickPendingIntent(R.id.widget, pendingIntent);
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (UPDATE_ACTION.equals(action)) {
            loginStatus = intent.getIntExtra(STATE_TAG, STATE_LOGOUT);
            // update widget
            AppWidgetManager appWidgetManager = AppWidgetManager
                    .getInstance(context.getApplicationContext());
            int[] allWidgetIds = intent
                    .getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);
            onUpdate(context, appWidgetManager, allWidgetIds);
        } else {
            super.onReceive(context, intent);
        }
    }


}
