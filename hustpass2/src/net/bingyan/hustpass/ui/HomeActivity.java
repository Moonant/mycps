package net.bingyan.hustpass.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import net.bingyan.hustpass.DaoHelper;
import net.bingyan.hustpass.Module;
import net.bingyan.hustpass.ModuleDao;
import net.bingyan.hustpass.MyApplication;
import net.bingyan.hustpass.R;
import net.bingyan.hustpass.Slide;
import net.bingyan.hustpass.SlideDao;
import net.bingyan.hustpass.helper.AccountManager;
import net.bingyan.hustpass.module.Modules;
import net.bingyan.hustpass.module.elec.ElecDetailActivity;
import net.bingyan.hustpass.module.elec.ElectricActivity;
import net.bingyan.hustpass.module.home.SlideImageFragment;
import net.bingyan.hustpass.module.recruit.RecruitCheckWifi;
import net.bingyan.hustpass.module.score.HubLoginActivity;
import net.bingyan.hustpass.module.score.ScoreActivity;
import net.bingyan.hustpass.provider.WifiWidgetProvider;
import net.bingyan.hustpass.service.WifiConnectService;
import net.bingyan.hustpass.ui.base.BaseActivity;
import net.bingyan.hustpass.ui.pref.PrefScorePswordActivity;
import net.bingyan.hustpass.util.AppLog;
import net.bingyan.hustpass.util.HustUtils;
import net.bingyan.hustpass.util.Pref;
import net.bingyan.hustpass.util.Util;
import net.bingyan.hustpass.widget.MoveAbleImageView;
import net.bingyan.hustpass.widget.pageIndicator.CirclePageIndicator;

import java.util.List;

public class HomeActivity extends BaseActivity {
    Runnable slideRunnable;
    Handler handler = new Handler();
    int SLIDE_SPACE_TIME = 4300;

    HustWifiStatusChangeReceiver mWifiReceiver = new HustWifiStatusChangeReceiver();
    MoveAbleImageView wifiView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDisplayActionbarBack(false);
        setContentView(R.layout.activity_home_activity2);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.postDelayed(slideRunnable, SLIDE_SPACE_TIME);
        registerReceiver(mWifiReceiver, new IntentFilter(WifiWidgetProvider.UPDATE_ACTION));

        //招新，启动扫描WIFI的服务
        SharedPreferences pref = MyApplication.getSharedPreferences();
        Intent service = new Intent(this, RecruitCheckWifi.class);
        int state = pref.getInt(Pref.RECRUIT_STATE, 0);
        if(state != 0 && state != 8) {
            startService(service);
        } else {
            stopService(service);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(slideRunnable);
        unregisterReceiver(mWifiReceiver);
    }

    private void initView() {
        ((TextView) findViewById(R.id.home_bottom_time)).setText(HustUtils.getHomeClasstime());
        initGridView();
        initSlideView();
        initWifiView();

    }

    private void initSlideView() {
        final ViewPager mPager = (ViewPager) findViewById(R.id.viewpager);

        SlideDao slideDao = ((MyApplication) getApplication()).getDaoSession().getSlideDao();
        List<Slide> slides = slideDao.queryBuilder().orderDesc(SlideDao.Properties.Id).limit(4).list();
        if (slides.isEmpty()) {
            slides = DaoHelper.initSlideDao(this, slideDao);
        }

        HomeSlideAdapter adapter = new HomeSlideAdapter(this.getSupportFragmentManager(), slides);
        mPager.setAdapter(adapter);
        final CirclePageIndicator mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);

        slideRunnable = new Runnable() {
            @Override
            public void run() {
                mIndicator.setCurrentItem((mPager.getCurrentItem() + 1) % 4);
                handler.postDelayed(slideRunnable, SLIDE_SPACE_TIME);
            }
        };
    }

    private void initGridView() {
        ModuleDao moduleDao = ((MyApplication) getApplication()).getDaoSession().getModuleDao();
        List<Module> modules = moduleDao.
                queryBuilder().
                orderDesc(ModuleDao.Properties.Frequency).
                list();
        if (modules.isEmpty()) {
            modules = Modules.initModuleDao(this, moduleDao);
        }

        GridView gridView = (GridView) findViewById(R.id.home_gridview);
        gridView.setFocusable(false);

        HomeGridViewAdapter adapter = new HomeGridViewAdapter(modules, moduleDao);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(adapter);
    }

    private void initWifiView() {
        wifiView = (MoveAbleImageView) findViewById(R.id.home_wifi_icon);
        wifiView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLog.v("btn click");
                Intent serviceIntent = new Intent(HomeActivity.this, WifiConnectService.class);
                startService(serviceIntent);
            }
        });
    }

    private void updateWifiStatus(int status) {
        int imgId;
        switch (status) {
            case WifiWidgetProvider.STATE_LOGOUT:
                imgId = R.drawable.home_wifi_logout;
                break;
            case WifiWidgetProvider.STATE_LOGIN:
                imgId = R.drawable.home_wifi_normal;
                break;
            case WifiWidgetProvider.STATE_LOADING:
                imgId = R.drawable.home_wifi_logout;
                break;
            default:
                imgId = R.drawable.home_wifi_logout;
                break;
        }
        wifiView.setImageResource(imgId);
    }

    boolean isBackPressedFirst = false;
    @Override
    public void onBackPressed() {
        if(isBackPressedFirst){
            super.onBackPressed();
        }else {
            isBackPressedFirst = true;
            Util.toast("再按一次退出程序",1400);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    isBackPressedFirst = false;
                }
            }, 1400);
        }
    }

    private Intent checkActivity(String n) {
        Intent intent = new Intent();

        if (n.equals(ElectricActivity.class.getName())) {
            AccountManager.ElecAccountManager manager = AccountManager.getElecManger();
            if (manager.isBound()) {
                Bundle bundle = new Bundle();
                bundle.putString("area", manager.getBoundArea());
                bundle.putInt("building", manager.getBoundBuilding());
                bundle.putString("room", manager.getBoundRoom());
                intent.putExtras(bundle);
                intent.setClass(this, ElecDetailActivity.class);
                return intent;
            }

        } else if (n.equals(ScoreActivity.class.getName())) {
            AccountManager.HubAccountManager manager = AccountManager.getHubManger();

            if(!manager.isBound()){
                intent.setClass(this, HubLoginActivity.class);
            }else if(manager.isScorePretected()){
                intent.putExtra("state",PrefScorePswordActivity.STATE_ENTER_PSWORD);
                intent.setClass(this, PrefScorePswordActivity.class);
            }else {
                intent.setClass(this, ScoreActivity.class);
            }
            return intent;
        }
        try {
            intent.setClass(this, Class.forName(n));
            return intent;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private class HustWifiStatusChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context arg0, Intent intent) {
            int loginStatus = intent.getIntExtra("loginStatus", 0);
            updateWifiStatus(loginStatus);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        MenuItem changeBtn = menu.add(5, 6, 7, "帐号管理");
        changeBtn.setIcon(R.drawable.home_action_icon_person)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        Intent intent = new Intent(this,UserActivity.class);
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }

    class HomeSlideAdapter extends FragmentPagerAdapter {
        List<Slide> slides;

        public HomeSlideAdapter(FragmentManager fm, List<Slide> slides) {
            super(fm);
            this.slides = slides;
        }

        @Override
        public Fragment getItem(int i) {
            Bundle bundle = new Bundle();
            bundle.putString("imgurl", slides.get(i).getImageurl());
            bundle.putString("url", slides.get(i).getSiteurl());
            return SlideImageFragment.newInstance(bundle);
        }

        @Override
        public int getCount() {
            return slides.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "";
        }
    }

    class HomeGridViewAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {
        List<Module> modules;
        ModuleDao moduleDao;

        public HomeGridViewAdapter(List<Module> modules, ModuleDao moduleDao) {
            this.modules = modules;
            this.moduleDao = moduleDao;
        }

        @Override
        public int getCount() {
            return modules.size();
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = View.inflate(HomeActivity.this, R.layout.activity_home_gridview_simple_item, null);
                ViewHolder viewHolder = new ViewHolder();
                viewHolder.imageView = (ImageView) view.findViewById(R.id.imageview);
                viewHolder.textView = (TextView) view.findViewById(R.id.textview);
                view.setTag(viewHolder);
            }
            ViewHolder viewHolder = (ViewHolder) view.getTag();
            viewHolder.imageView.setImageResource(Modules.moduleIconRes[modules.get(i).getIconid()]);
            viewHolder.textView.setText(modules.get(i).getName());
            return view;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public Object getItem(int i) {
            return modules.get(i);
        }

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            //update module frequency
            Module module = modules.get(i);
            module.setFrequency(module.getFrequency() + 1);
            moduleDao.update(module);

            //go to Module
//            String n = module.getClassname();
            String n = Modules.getClassNameByImgRes(module.getIconid());
            Intent intent = checkActivity(n);
            if (intent != null)
                startActivity(intent);
        }

        class ViewHolder {
            public ImageView imageView;
            public TextView textView;
        }
    }
}
