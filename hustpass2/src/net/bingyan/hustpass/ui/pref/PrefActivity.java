package net.bingyan.hustpass.ui.pref;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.update.UmengUpdateAgent;

import net.bingyan.hustpass.R;
import net.bingyan.hustpass.helper.AccountManager;
import net.bingyan.hustpass.module.recruit.RecruitLotteryActivity;
import net.bingyan.hustpass.ui.VersionInfoActivity;
import net.bingyan.hustpass.ui.base.BaseActivity;
import net.bingyan.hustpass.util.Pref;
import net.bingyan.hustpass.util.Util;

public class PrefActivity extends BaseActivity implements OnCheckedChangeListener {
    TextView mElecInfo;
    TextView mNetInfo;
    SharedPreferences mPref;
    View mScoreProtectView;
    boolean isScoreProtect;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        mPref = Pref.getPref();

        setContentView(R.layout.activity_pref);
        initView();
    }

    private void initView() {
        // TODO Auto-generated method stub
        mElecInfo = (TextView) findViewById(R.id.activity_pref_elec_info);
        mNetInfo = (TextView) findViewById(R.id.activity_pref_net_info);
        mScoreProtectView = findViewById(R.id.activity_pref_score_set_psword);

        mScoreProtectView.setOnClickListener(this);
        findViewById(R.id.activity_pref_elec).setOnClickListener(this);
        findViewById(R.id.activity_pref_net).setOnClickListener(this);
        findViewById(R.id.activity_pref_vaersion_info).setOnClickListener(this);
        findViewById(R.id.activity_pref_exit).setOnClickListener(this);

        initCheckBox();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isScoreProtect = mPref.getBoolean(Pref.IS_SCORE_PROTECT, false);

        setScoreProtectEnabledView(isScoreProtect);

        mLog.v("set listener");
        CheckBox box = (CheckBox) findViewById(R.id.activity_pref_score_protect);
        box.setOnCheckedChangeListener(this);
        box.setChecked(isScoreProtect);

        mElecInfo.setText("< " + mPref.getInt(Pref.INT_ELEC_ALARM, 0) + "度");
        mNetInfo.setText("< " + mPref.getInt(Pref.INT_NET_ALARM, 0) + "天");

        //招新抽奖号
        RelativeLayout recruit = (RelativeLayout) findViewById(R.id.recruit_lottery);
        recruit.setOnClickListener(this);
        String num = mPref.getString(Pref.RECRUIT_LOTTERY_NUM, "");
        TextView lotteryNum = (TextView)findViewById(R.id.recruit_lottery_num_pref);
        lotteryNum.setText(num);
        if (num != null && !num.trim().equals("") && num.length() == 6) {
            recruit.setVisibility(View.VISIBLE);
        } else {
            recruit.setVisibility(View.GONE);
        }
    }

    private void initCheckBox() {
        int[] checkboxId = {R.id.activity_pref_score_update_push
                , R.id.activity_pref_wifi_update
                , R.id.activity_pref_version_auto_update};
        boolean[] enabled = {mPref.getBoolean(Pref.IS_SCORE_PUSH, false)
                , mPref.getBoolean(Pref.IS_WIFI_AUTO_UPDATE, false)
                , mPref.getBoolean(Pref.IS_VERSION_AUTO_UPDATE, false)};

        for (int i = 0; i < checkboxId.length; i++) {
            CheckBox box = (CheckBox) findViewById(checkboxId[i]);
            box.setOnCheckedChangeListener(this);
            box.setChecked(enabled[i]);
        }
    }


    private void setScoreProtectEnabledView(boolean enabled) {
        if (!enabled) {
            ((TextView) mScoreProtectView
                    .findViewById(R.id.pref_set_score_textitem))
                    .setTextColor(Color.parseColor("#cecece"));
            ((ImageView) mScoreProtectView.findViewById(R.id.arrow_right))
                    .setImageDrawable(getResources().getDrawable(
                            R.drawable.pref_arrow_unabled));
        } else {
            ((TextView) mScoreProtectView
                    .findViewById(R.id.pref_set_score_textitem))
                    .setTextColor(Color.parseColor("#65827e"));
            ((ImageView) mScoreProtectView.findViewById(R.id.arrow_right))
                    .setImageDrawable(getResources().getDrawable(
                            R.drawable.pref_arrow));
        }
    }

    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        super.onClick(view);
        switch (view.getId()) {
            case R.id.activity_pref_score_set_psword:
                if (isScoreProtect) {
                    Intent intent = new Intent(this,
                            PrefScorePswordActivity.class);
                    intent.putExtra("state", PrefScorePswordActivity.STATE_CHANGE_PSWORD);
                    startActivity(intent);
                }
                break;
            case R.id.activity_pref_elec:
                startActivity(new Intent(this, PrefElecActivity.class));
                break;
            case R.id.activity_pref_net:
                startActivity(new Intent(this, PrefNetActivity.class));
                break;
            case R.id.activity_pref_vaersion_info:
                startActivity(new Intent(this, VersionInfoActivity.class));
                break;
            case R.id.activity_pref_exit:
                AccountManager.HustAccountManager hustAccountManager = AccountManager.getHustManger();
                hustAccountManager.putEmailandPsw(null, null);
                hustAccountManager.putUid(-1l);
                hustAccountManager.putUserName(null);
                hustAccountManager.setIsEmailChecked(true);
                Util.toast("退出登录成功");
                break;
            case R.id.recruit_lottery:
                startActivity(new Intent(this, RecruitLotteryActivity.class));
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        // TODO Auto-generated method stub
        switch (buttonView.getId()) {
            case R.id.activity_pref_score_protect:
                mLog.v("pref socre psw set");
                Intent intent = new Intent(this,
                        PrefScorePswordActivity.class);

                if (!isScoreProtect) {
                    intent.putExtra("state",
                            PrefScorePswordActivity.STATE_SET_PSWORD);
                } else {
                    intent.putExtra("state",
                            PrefScorePswordActivity.STATE_RM_PSWORD);
                }

                startActivity(intent);
                break;
            case R.id.activity_pref_score_update_push:
                mPref.edit().putBoolean(Pref.IS_SCORE_PUSH, isChecked).apply();
                break;
            case R.id.activity_pref_wifi_update:
                mPref.edit().putBoolean(Pref.IS_WIFI_AUTO_UPDATE, isChecked).apply();
                UmengUpdateAgent.setUpdateOnlyWifi(isChecked);
                break;
            case R.id.activity_pref_version_auto_update:
                mPref.edit().putBoolean(Pref.IS_VERSION_AUTO_UPDATE, isChecked).apply();
                break;
        }
    }
}
