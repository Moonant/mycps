package net.bingyan.hustpass.ui.base;

import net.bingyan.hustpass.R;
import net.bingyan.hustpass.util.AppLog;
import net.bingyan.hustpass.util.Util;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.umeng.analytics.MobclickAgent;

import fr.castorflex.android.smoothprogressbar.ColorsShape;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import fr.castorflex.android.smoothprogressbar.SmoothProgressDrawable;

public abstract class BaseActivity extends SherlockFragmentActivity implements
		OnClickListener {
    public AppLog mLog = new AppLog(getClass());

    View actionbarBack;
    TextView actionbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActionBar2();

    }

    @Override
    protected void onResume() {

        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void setContentView(int layoutResId) {
        super.setContentView(layoutResId);
        if(mProgressBar==null) {
            ViewGroup contentView = (ViewGroup) getWindow().getDecorView().findViewById(android.R.id.content);
            ViewGroup viewGroup = (ViewGroup) View.inflate(this, R.layout.simple_smoothprogressbar, null);

            View content = contentView.getChildAt(0);
            contentView.removeView(content);
            viewGroup.addView(content);
            contentView.addView(viewGroup);
            mProgressBar = (SmoothProgressBar) viewGroup.findViewById(R.id.progressBar);
            mProgressBar.setVisibility(View.GONE);
        }
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    private void initActionBar2(){
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_actionbar_slid);
        actionbarBack = findViewById(R.id.actionbar_btn_back);
        actionbarBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        actionbarTitle = (TextView) findViewById(R.id.actionbar_title);
        actionbarTitle.setWidth(Util.getSreenWidth(this));
        CharSequence title = getTitle();
        if(TextUtils.isEmpty(title)){
            actionbarTitle.setVisibility(View.GONE);
        }else {
            actionbarTitle.setVisibility(View.VISIBLE);
            actionbarTitle.setText(title);
        }
    }

    private void initActionBar(){
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        getSupportActionBar().setDisplayShowCustomEnabled(true);

        ActionBar.LayoutParams params = new
                ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT);
        View customView = LayoutInflater.from(this).inflate(R.layout.custom_actionbar_slid,null);
//        getSupportActionBar().setCustomView(R.layout.custom_actionbar_slid);
        getSupportActionBar().setCustomView(customView, params);
        actionbarBack = findViewById(R.id.actionbar_btn_back);
        actionbarBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        actionbarTitle = (TextView) findViewById(R.id.actionbar_title);
//        Util.getSreenWidth(this);
        actionbarTitle.setWidth(Util.getSreenWidth(this));
        CharSequence title = getTitle();
        if(TextUtils.isEmpty(title)){
            actionbarTitle.setVisibility(View.GONE);
        }else {
            actionbarTitle.setVisibility(View.VISIBLE);
            actionbarTitle.setText(title);
        }
    }

    public void setDisplayActionbarBack(boolean display){
        if(!display){
            actionbarBack.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        if(TextUtils.isEmpty(title)){
            actionbarTitle.setVisibility(View.GONE);
        }else {
            actionbarTitle.setVisibility(View.VISIBLE);
            actionbarTitle.setText(title);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    SmoothProgressBar mProgressBar;
    int mProgressBarCount = 0;
    public void showProgressBar(){
        if(mProgressBar==null) {
            ViewGroup contentView = (ViewGroup) getWindow().getDecorView().findViewById(android.R.id.content);
            ViewGroup viewGroup = (ViewGroup) View.inflate(this, R.layout.simple_smoothprogressbar, null);

            View content = contentView.getChildAt(0);
            contentView.removeView(content);
            viewGroup.addView(content);
            contentView.addView(viewGroup);
            mProgressBar = (SmoothProgressBar) viewGroup.findViewById(R.id.progressBar);
        }


        mProgressBar.progressiveStart();
        mProgressBar.setVisibility(View.VISIBLE);
        mProgressBarCount++;
        mLog.v("show bar"+mProgressBarCount);
    }

    public void stopProgressBar(){
        mLog.v("stop bar"+mProgressBarCount);
        mProgressBarCount--;
        if(mProgressBar!=null&&mProgressBarCount<=0){
            mProgressBarCount=0;
            mProgressBar.progressiveStop();
            mProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View arg0) {
        if(arg0.getId()==android.R.id.home){
            onBackPressed();
        }
    }
}