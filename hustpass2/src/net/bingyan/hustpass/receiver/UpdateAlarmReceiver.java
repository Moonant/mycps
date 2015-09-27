package net.bingyan.hustpass.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import net.bingyan.hustpass.helper.UpdateHelper;

public class UpdateAlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// 查成绩

		// 查电费，更新最新电费
        UpdateHelper.updateElec();

        //招新, 更新明天的招新状态
        UpdateHelper.updateRecruit();
	}
}