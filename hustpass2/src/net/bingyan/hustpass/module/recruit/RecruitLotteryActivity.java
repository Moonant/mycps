package net.bingyan.hustpass.module.recruit;

import android.app.Activity;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import net.bingyan.hustpass.MyApplication;
import net.bingyan.hustpass.R;
import net.bingyan.hustpass.util.Pref;

public class RecruitLotteryActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recruit_lottery);

        Button button = (Button) findViewById(R.id.recruit_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecruitLotteryActivity.this.finish();
            }
        });

        TextView lotteryNum = (TextView) findViewById(R.id.recruit_lottery_num);
        lotteryNum.setText(MyApplication.getSharedPreferences().getString(Pref.RECRUIT_LOTTERY_NUM, ""));

    }

}
