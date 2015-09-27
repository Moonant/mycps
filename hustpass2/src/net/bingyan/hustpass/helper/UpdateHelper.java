package net.bingyan.hustpass.helper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import net.bingyan.hustpass.API;
import net.bingyan.hustpass.MyApplication;
import net.bingyan.hustpass.R;
import net.bingyan.hustpass.Slide;
import net.bingyan.hustpass.SlideDao;
import net.bingyan.hustpass.db.CacheDaoHelper;
import net.bingyan.hustpass.http.RestHelper;
import net.bingyan.hustpass.module.elec.ElecBean;
import net.bingyan.hustpass.module.home.SlidesBean;
import net.bingyan.hustpass.module.recruit.RecruitActivityBean;
import net.bingyan.hustpass.module.recruit.RecruitCheckWifi;
import net.bingyan.hustpass.module.recruit.RecruitLotteryBean;
import net.bingyan.hustpass.module.recruit.openudid.OpenUDID_manager;
import net.bingyan.hustpass.ui.HomeActivity;
import net.bingyan.hustpass.util.AppLog;
import net.bingyan.hustpass.util.Pref;
import net.bingyan.hustpass.util.Util;

import java.util.Date;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by ant on 14-8-14.
 */
public class UpdateHelper {
    static AppLog mLog = new AppLog("UpdateHelper");

    static public void updateElec() {
        final AccountManager.ElecAccountManager manager = AccountManager.getElecManger();
        if (!manager.isLogin()) {
            return;
        }

        RestHelper.getService(API.ElecService.class).getElec(manager.getBoundArea(), manager.getBoundBuilding(), manager.getBoundRoom(), new Callback<ElecBean>() {
            @Override
            public void success(ElecBean elecBean, Response response) {
                try {
                    if (elecBean.getState().equals("success")) {
                        float remain = Float.parseFloat(elecBean.getRemain());
                        manager.updateRemain(remain);
                        if (remain <= Pref.getPref().getInt(Pref.INT_ELEC_ALARM, 10)) {
                            startElecAlert(remain);
                        }
                        CacheDaoHelper.getInstance().putCache(elecBean, manager.getBoundArea() + manager.getBoundBuilding() + manager.getBoundRoom());
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    static public void updateHomeSlide() {
        RestHelper.getService(API.AppService.slideHOST, API.AppService.class).getSlides(new Callback<SlidesBean>() {
            @Override
            public void success(SlidesBean slidesBean, Response response) {
                mLog.v(response.getUrl());
                updateSlideDao(slidesBean);
            }

            @Override
            public void failure(RetrofitError error) {
                mLog.v(error.getUrl());
            }
        });
    }

    static private void updateSlideDao(SlidesBean slidesBean) {
        SlideDao slideDao = MyApplication.getInstance().getDaoSession().getSlideDao();
        List<Slide> slides = slideDao.queryBuilder().orderDesc(SlideDao.Properties.Id).limit(1).list();
        int maxDaoId = slides.isEmpty() ? -1 : slides.get(0).getId().intValue();
        for (SlidesBean.Datum data : slidesBean.getData()) {
            if (Integer.parseInt(data.getId()) > maxDaoId) {
                Slide slide = new Slide();
                slide.setId(Long.parseLong(data.getId()));
                slide.setImageurl(data.getImageurl());
                slide.setSiteurl(data.getSiteurl());
                slideDao.insert(slide);
            }
        }
    }

    static private void startElecAlert(float remain) {
        Intent intent = new Intent(MyApplication.getAppContext(), HomeActivity.class);
        Util.createNotification(MyApplication.getAppContext(), 001, R.drawable.notifi_elec, "电费不足",
                "剩余电量：" + remain, intent, false, false);
    }

    //更新招新状态
    static public void updateRecruit() {
        final SharedPreferences pref = MyApplication.getSharedPreferences();
        long now = System.currentTimeMillis();
        RestHelper.getService(API.RecruitService.class).getState(now + "", new Callback<RecruitActivityBean>() {
            @Override
            public void success(RecruitActivityBean recruitActivityBean, Response response) {
                pref.edit().putInt(Pref.RECRUIT_STATE, recruitActivityBean.getFirst()).apply();
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    //招新获取抽奖码
    static public void getLottery() {
        if (!OpenUDID_manager.isInitialized()) {
            return;
        }
        String deviceId = OpenUDID_manager.getOpenUDID();
        final SharedPreferences pref = MyApplication.getSharedPreferences();
        String num = pref.getString(Pref.RECRUIT_LOTTERY_NUM, "");
        if (num != null && !num.trim().equals("") && num.length() == 6) {
            return;
        }
        RestHelper.getService(API.RecruitService.class).lottery(deviceId, new Callback<RecruitLotteryBean>() {
            @Override
            public void success(RecruitLotteryBean recruitLotteryBean, Response response) {
                if (recruitLotteryBean.getCode() == 0) {
                    pref.edit().putString(Pref.RECRUIT_LOTTERY_NUM, recruitLotteryBean.getString()).commit();
                }
            }

            @Override
            public void failure(RetrofitError error) {
            }
        });
    }
}
