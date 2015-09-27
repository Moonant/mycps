package net.bingyan.hustpass.provider;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import net.bingyan.hustpass.R;
import net.bingyan.hustpass.ui.HomeActivity;
import net.bingyan.hustpass.util.Pref;

public class ElecWidgetProvider extends AppWidgetProvider {

	public static final String UPDATE_ACTION = "hustpass2.elec.action.STATUS_UPDATED";

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		// TODO Auto-generated method stub
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		ComponentName thisWidget = new ComponentName(context,
				ElecWidgetProvider.class);
		int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
		float remain = Pref.getPref().getFloat(Pref.ELEC_REMAIN, -1);
		for (int appWidgetId : allWidgetIds) {
			RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
					R.layout.elec_widget);
			if (remain != -1) {
				remoteViews.setTextViewText(R.id.widget_elec_remain, ""
						+ remain);
				if (remain > 100) {
					remoteViews.setTextViewText(R.id.widget_elec_info,
							context.getString(R.string.elec_enough));
				} else {
					remoteViews.setTextViewText(R.id.widget_elec_info,
							context.getString(R.string.elec_not_enough));
				}
			}
			
			Intent intent = new Intent(context, HomeActivity.class);
			PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
					intent, PendingIntent.FLAG_UPDATE_CURRENT);
			remoteViews.setOnClickPendingIntent(R.id.widget, pendingIntent);
			appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
		}
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		super.onReceive(context, intent);
		AppWidgetManager appWidgetManager = AppWidgetManager
				.getInstance(context.getApplicationContext());
		int[] allWidgetIds = intent
				.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);
		onUpdate(context, appWidgetManager, allWidgetIds);
	}

}
