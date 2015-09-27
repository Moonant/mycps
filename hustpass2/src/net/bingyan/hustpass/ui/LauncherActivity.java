package net.bingyan.hustpass.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.umeng.fb.FeedbackAgent;
import com.umeng.update.UmengUpdateAgent;

import net.bingyan.hustpass.R;
import net.bingyan.hustpass.helper.UpdateHelper;
import net.bingyan.hustpass.module.recruit.openudid.OpenUDID_manager;
import net.bingyan.hustpass.util.AppLog;
import net.bingyan.hustpass.util.Pref;
import net.bingyan.hustpass.util.Util;

public class LauncherActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        UmengUpdateAgent.setUpdateCheckConfig(false);
        setContentView(R.layout.activity_launcher);

        FeedbackAgent agent = new FeedbackAgent(this);
        agent.sync();


        //获取设备唯一标识
        if(!OpenUDID_manager.isInitialized()) {
            OpenUDID_manager.sync(this);
        }
        //招新状态更新，获取抽奖码
        UpdateHelper.getLottery();
        UpdateHelper.updateRecruit();

        checkVersion();
    }

    private void checkVersion(){
        int versionNumber = Util.getVersionCode();
        int old_code = Pref.getPref().getInt("version_code",0);

        if(versionNumber > old_code ){
            Pref.getPref().edit().putInt("version_code",versionNumber).apply();
            startActivity(new Intent(this, WelcomeActivity.class));
            this.finish();

        }else {
            UpdateHelper.updateElec(); // 检查 电费
            UpdateHelper.updateHomeSlide();  // 更新 幻灯
            new Handler().postDelayed(new Loading(), 800); // 进入首页

        }

    }

    class Loading implements Runnable {
        @Override
        public void run() {
            // TODO Auto-generated method stub

            //检查更新
            if (Pref.getPref().getBoolean(Pref.IS_VERSION_AUTO_UPDATE, true)) {
                UmengUpdateAgent.update(LauncherActivity.this);
            }

            startActivity(new Intent(LauncherActivity.this, HomeActivity.class));
            LauncherActivity.this.finish();
        }
    }

}
