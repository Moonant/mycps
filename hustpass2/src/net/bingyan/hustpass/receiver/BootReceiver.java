package net.bingyan.hustpass.receiver;

import java.util.Calendar;
import java.util.TimeZone;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import net.bingyan.hustpass.module.recruit.PushReceiver;

public class BootReceiver extends BroadcastReceiver {
	
    @Override
    public void onReceive(Context context, Intent intent) {
        initAlarms(context);
    }
 
    public static void initAlarms(Context context) {
    	setUpdateAlarm(context);

        //招新路演和宣讲会的那天10点定时推送
        setPushAlarm(context);

    	context.sendBroadcast(new Intent(context, UpdateAlarmReceiver.class));
	}

	private static void setUpdateAlarm(Context context) {
        Calendar updateTime = Calendar.getInstance();
        updateTime.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        updateTime.set(Calendar.HOUR_OF_DAY, 20);
        updateTime.set(Calendar.MINUTE, 20);
        Intent receiver = new Intent(context, UpdateAlarmReceiver.class);
        PendingIntent alarmReceiver = PendingIntent.getBroadcast(context,
                0, receiver, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarms = (AlarmManager) context.getSystemService(
                Context.ALARM_SERVICE);
        alarms.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                updateTime.getTimeInMillis(),AlarmManager.INTERVAL_DAY, alarmReceiver);
                
    }

    private static void setPushAlarm(Context context) {
        Calendar updateTime = Calendar.getInstance();
        updateTime.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        updateTime.set(Calendar.HOUR_OF_DAY, 10);
        updateTime.set(Calendar.MINUTE, 20);
        Intent receiver = new Intent(context, PushReceiver.class);
        PendingIntent alarmReceiver = PendingIntent.getBroadcast(context,
                0, receiver, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarms = (AlarmManager) context.getSystemService(
                Context.ALARM_SERVICE);
        alarms.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                updateTime.getTimeInMillis(),AlarmManager.INTERVAL_DAY, alarmReceiver);
    }
    
}