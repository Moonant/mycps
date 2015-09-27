package net.bingyan.hustpass.module.recruit;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.umeng.fb.model.Reply;

import net.bingyan.hustpass.MyApplication;
import net.bingyan.hustpass.R;
import net.bingyan.hustpass.util.Pref;
import net.bingyan.hustpass.util.Util;

/**
 * Created by jinge on 14-9-9.
 */
public class PushReceiver extends BroadcastReceiver {

    SharedPreferences pref;

    @Override
    public void onReceive(Context context, Intent intent) {
        pref = MyApplication.getSharedPreferences();
        int state = pref.getInt(Pref.RECRUIT_STATE, 0);

        int type = 0;

        switch (state) {
            case 2:
                type = NotificationUtil.TYPE_PUSH_LY;
                break;
            case 3:
                type = NotificationUtil.TYPE_PUSH_XJH;
                break;
        }
        if(type != 0) {
            NotificationUtil.createNoti(context, type);
        }


    }
}
