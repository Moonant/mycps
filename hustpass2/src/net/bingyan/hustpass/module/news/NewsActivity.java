package net.bingyan.hustpass.module.news;

import net.bingyan.hustpass.R;
import net.bingyan.hustpass.adapter.XFragmentPagerAdapter;
import net.bingyan.hustpass.API;
import net.bingyan.hustpass.widget.pageIndicator.TabPageIndicator;
import net.bingyan.hustpass.widget.pageIndicator.UnderlinePageIndicator;
import net.bingyan.hustpass.ui.base.BaseActivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

public class NewsActivity extends BaseActivity{

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
        TabPageIndicator mPageIndicator = (TabPageIndicator) findViewById(R.id.page_indicator);
//		UnderlinePageIndicator uIndicator = (UnderlinePageIndicator) findViewById(R.id.indicator);
        ViewPager mViewPager = (ViewPager) findViewById(R.id.viewpager);

		mViewPager.setAdapter(xAdapter);
		mPageIndicator.setViewPager(mViewPager);
//		uIndicator.setViewPager(mViewPager);
	}

    XFragmentPagerAdapter xAdapter = new XFragmentPagerAdapter(getSupportFragmentManager(),API.NewsService.title) {

        @Override
        public Fragment getItem(int arg0) {
            // TODO Auto-generated method stub
            return NewsFragment.newInstance(getBundle(arg0));
        }

        @Override
        public Bundle getBundle(int arg0) {
            // TODO Auto-generated method stub
            Bundle bundle = new Bundle();
            bundle.putInt("sort", API.NewsService.sort[arg0]);
            return bundle;
        }
    };

	
}
