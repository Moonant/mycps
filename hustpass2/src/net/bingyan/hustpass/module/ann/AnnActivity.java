package net.bingyan.hustpass.module.ann;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;

import net.bingyan.hustpass.R;
import net.bingyan.hustpass.adapter.XFragmentPagerAdapter;
import net.bingyan.hustpass.util.Pref;
import net.bingyan.hustpass.db.CacheDaoHelper;
import net.bingyan.hustpass.http.RestHelper;
import net.bingyan.hustpass.util.Util;
import net.bingyan.hustpass.widget.pageIndicator.TabPageIndicator;
import net.bingyan.hustpass.API;
import net.bingyan.hustpass.ui.base.BaseActivity;
import net.bingyan.hustpass.util.AppLog;
import net.bingyan.hustpass.widget.ActionbarNavigator;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class AnnActivity extends BaseActivity {
    ActionbarNavigator mNavigator;

    TabPageIndicator mPageIndicator;
    ViewPager mViewPager;
    AnnFragmentPagerAdapter mXFragmentPagerAdapter;

    int itemPositionNow = Pref.getPref().getInt(Pref.ANNOUNCEMENT_INDEX, 0);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple_empty_loading_view);
        init();
    }

    private AnnIndexBean getIndexCache() {
        return new CacheDaoHelper(getApplication()).getCache(AnnIndexBean.class);
    }

    private void putIndexCache(AnnIndexBean annIndexBean) {
        new CacheDaoHelper(getApplication()).putCache(annIndexBean);
    }

    private void getIndexHttp(Callback<AnnIndexBean> cb) {
        RestHelper.getService(API.AnnService.class).getIndex(cb);
    }


    private void init() {
        AnnIndexBean annIndexBean = getIndexCache();

        if (annIndexBean == null) {
            showProgressBar();
            getIndexHttp(new Callback<AnnIndexBean>() {
                @Override
                public void success(AnnIndexBean annIndexBean, Response response) {
                    stopProgressBar();
                    initContent(annIndexBean);
                    putIndexCache(annIndexBean);
                }

                @Override
                public void failure(RetrofitError error) {
                    stopProgressBar();
                    Util.toast("网络出错"+error.getMessage());
                }
            });
        } else {
            initContent(annIndexBean);
        }

    }

    private void initContent(AnnIndexBean annIndexBean) {
        setContentView(R.layout.activity_announcement);
        initNavigater(annIndexBean.getData());
        initViewPager(annIndexBean.getData().get(itemPositionNow));
    }

    private void initNavigater(final List<AnnIndexBean.Datum> data) {
        mNavigator = (ActionbarNavigator) findViewById(R.id.actionbar_navigiter);
        mNavigator.setVisibility(View.VISIBLE);
        List<String> items = new ArrayList<String>();

        for (AnnIndexBean.Datum item : data) {
            items.add(item.getDepName());
        }

        mNavigator.setData(items, itemPositionNow,"院系选择");

        mNavigator.setOnItemSelectedListener(new ActionbarNavigator.OnItemSelectedListener() {
            @Override
            public void onSelected(int i, View v) {
                Pref.getPref().edit().putInt(Pref.ANNOUNCEMENT_INDEX, i).commit();
                itemPositionNow = i;

                mLog.v("set changed");
                mXFragmentPagerAdapter.setData(data.get(i));
                mXFragmentPagerAdapter.notifyDataSetChanged();
            }
        });

    }

    private void initViewPager(AnnIndexBean.Datum data) {
        mPageIndicator = (TabPageIndicator) findViewById(R.id.page_indicator);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        mXFragmentPagerAdapter = new AnnFragmentPagerAdapter(getSupportFragmentManager(), data);

        mViewPager.setAdapter(mXFragmentPagerAdapter);
        mPageIndicator.setViewPager(mViewPager);
    }

    class AnnFragmentPagerAdapter extends XFragmentPagerAdapter {
        AnnIndexBean.Datum data;

        public AnnFragmentPagerAdapter(FragmentManager fm, AnnIndexBean.Datum data) {
            // TODO Auto-generated constructor stub
            super(fm, data.getCategory());
            this.data = data;
        }

        public void setData(AnnIndexBean.Datum data){
            this.data = data;
            super.setTitle(data.getCategory());
        }

        @Override
        public Fragment getItem(int arg0) {
            // TODO Auto-generated method stub
            return AnnFragment.newInstance(getBundle(arg0));
        }

        @Override
        public Bundle getBundle(int i) {
            // TODO Auto-generated method stub
            Bundle bundle = new Bundle();
            bundle.putString("title", data.getCategory().get(i));
            bundle.putString("department", data.getSimpName());
            return bundle;
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
            mLog.v("mPageIndicator change notify");
            mPageIndicator.notifyDataSetChanged();
        }

        @Override
        public int getItemPosition(Object object)   {
            if ( getCount() > 0) {
                mLog.v("return null");
                return POSITION_NONE;
            }
            return super.getItemPosition(object);
        }
    }

}