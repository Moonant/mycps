package net.bingyan.hustpass.module.score;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.bingyan.hustpass.R;
import net.bingyan.hustpass.adapter.XFragmentPagerAdapter;
import net.bingyan.hustpass.helper.AccountManager;
import net.bingyan.hustpass.util.Pref;
import net.bingyan.hustpass.util.Util;
import net.bingyan.hustpass.widget.pageIndicator.PageIndicator;
import net.bingyan.hustpass.ui.base.BaseActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class ScoreActivity extends BaseActivity {
    public String uid;
    public String psw;
    public ArrayList<String> terms = new ArrayList<String>();
    public String[] mTitle;

    ViewPager mViewPager;
    PageIndicator mPageIndicator;

    XFragmentPagerAdapter mXFragmentPagerAdapter;

    @Override
    public void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        AccountManager.HubAccountManager hubAccountManager = AccountManager.getHubManger();
        boolean temp = getIntent().getBooleanExtra("temp", false);

        if (temp) {
            uid = hubAccountManager.getUid();
            psw = hubAccountManager.getPsw();
        } else {
            uid = hubAccountManager.getBoundUid();
            psw = hubAccountManager.getBoundPsw();
        }

        setContentView(R.layout.activity_score);
        init();
    }

    private void init() {
        try {
            setTerms();
        } catch (Exception e) {
            Util.toast("输入信息有误");
            finish();
            return;
        }
        initView();
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mPageIndicator = (PageIndicator) findViewById(R.id.page_indicator);

        mXFragmentPagerAdapter = new XFragmentPagerAdapter(
                getSupportFragmentManager(), mTitle) {
            @Override
            public Bundle getBundle(int arg0) {
                // TODO Auto-generated method stub
                Bundle bundle = new Bundle();
                bundle.putString("term", terms.get(arg0));
                bundle.putString("uid", uid);
                bundle.putString("psw",psw);
                bundle.putInt("id", arg0);
                return bundle;
            }

            @Override
            public Fragment getItem(int arg0) {
                // TODO Auto-generated method stub
                return ScoreFragment.newInstance(getBundle(arg0));
            }
        };
        mViewPager.setAdapter(mXFragmentPagerAdapter);
        mPageIndicator.setViewPager(mViewPager);
        mViewPager.setCurrentItem(mTitle.length - 1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub

        MenuItem changeBtn = menu.add(1, 0, 0, "切换帐号");
        changeBtn.setIcon(R.drawable.score_change_account_icon)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        if (item.getGroupId() == 1 && item.getItemId() == 0) {
            startActivity(new Intent(this, HubLoginActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    public void setTerms() {
        int yearStart = Integer.parseInt(uid.substring(1, 5));
        int yearNow = Integer.parseInt(getStringDate().substring(0, 4));
        int monthNow = Integer.parseInt(getStringDate().substring(5, 7));
        List<String> title = new ArrayList<String>();
        for (int i = yearStart; i < yearNow; i++) {
            terms.add(i + "1");
            title.add("" + i + "年秋季");
            terms.add(i + "2");
            title.add("" + (i + 1) + "年春季");
        }
        if (monthNow > 9) {
            terms.add(yearNow + "1");
            title.add("" + yearNow + "年秋季");
        }
        mTitle = new String[title.size()];
        for (int i = 0; i < mTitle.length; i++) {
            mTitle[i] = title.get(i);
        }
    }

    public String getStringDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(new Date());
    }
}
