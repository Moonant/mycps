package net.bingyan.hustpass.ui;

import net.bingyan.hustpass.R;
import net.bingyan.hustpass.module.recruit.openudid.OpenUDID_manager;
import net.bingyan.hustpass.ui.fragment.WelcomeFinalFragment;
import net.bingyan.hustpass.ui.fragment.WelcomeFragment;
import net.bingyan.hustpass.ui.base.BaseActivity;
import net.bingyan.hustpass.util.Pref;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ActionMode;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.umeng.update.UmengUpdateAgent;

public class WelcomeActivity extends SherlockFragmentActivity {
	WelcomeFragmentAdapter mAdapter;
	ViewPager mPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);

        SharedPreferences pref = Pref.getPref();
        pref.edit().putInt(Pref.INT_ELEC_ALARM, 10).putBoolean(Pref.IS_WIFI_AUTO_UPDATE, true)
                .putBoolean(Pref.IS_VERSION_AUTO_UPDATE, true).putInt(Pref.HOME_SIDE_NEWEST_ID, 35).apply();

		mAdapter = new WelcomeFragmentAdapter(getSupportFragmentManager());
		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);
	}

	class WelcomeFragmentAdapter extends FragmentPagerAdapter {
		int[] imgIds = { R.drawable.welcome1, R.drawable.welcome2, R.drawable.welcome3,R.drawable.welcome4};

		public WelcomeFragmentAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
            if(position==4){
                return new WelcomeFinalFragment();
            }

			Bundle bundle = new Bundle();
			bundle.putInt("imgId", imgIds[position]);
			return WelcomeFragment.newInstance(bundle);
		}

		@Override
		public int getCount() {
			return 5;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return "";
		}
	}

    @Override
    protected void onStop() {
        super.onStop();
    }
}
