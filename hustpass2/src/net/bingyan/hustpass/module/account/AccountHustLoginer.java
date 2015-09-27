package net.bingyan.hustpass.module.account;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.widget.EditText;

import net.bingyan.hustpass.API;
import net.bingyan.hustpass.R;
import net.bingyan.hustpass.helper.AccountManager;
import net.bingyan.hustpass.http.RestHelper;
import net.bingyan.hustpass.ui.AccountActivity;
import net.bingyan.hustpass.ui.HomeActivity;
import net.bingyan.hustpass.ui.UserActivity;
import net.bingyan.hustpass.util.Pref;
import net.bingyan.hustpass.util.Util;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by ant on 14-8-15.
 */
public class AccountHustLoginer implements AccountActivity.AccountActionListener{
    AccountActivity activity;
    EditText userET;
    EditText pswET;

    API.HustOnlineService service;
    AccountManager.HustAccountManager hustAccountManager;

    public AccountHustLoginer(AccountActivity activity){
        this.activity = activity;
        activity.setTitle("登录");
        activity.setContentView(R.layout.activity_account_hust_login);
        service =  RestHelper.getService(API.HustOnlineService.HOST, API.HustOnlineService.class);
        hustAccountManager = AccountManager.getHustManger();

        userET = activity.initEditView(R.id.account_username);
        pswET = activity.initEditView(R.id.account_psw);
        activity.initBtnView(R.id.account_login);
        activity.initBtnView(R.id.account_reg);
    }

    @Override
    public void onClick(int id) {

        if(id == R.id.account_login){
            final String user = userET.getText().toString().trim();
            final String psw = pswET.getText().toString().trim();

            if(TextUtils.isEmpty(user)||TextUtils.isEmpty(psw)){
                Util.toast(R.string.account_input_empty_alert);
            }else {
                checkUserPsw(user, psw);
            }

        }else if(id == R.id.account_reg){
            Intent intent = new Intent(activity,AccountActivity.class);
            intent.putExtra(AccountActivity.STATE_TAG,AccountActivity.STATE_HUST_REG);
            activity.startActivity(intent);
            activity.finish();
        }

    }

    // （ 1 ）
    private void checkUserPsw(final  String user, final String psw){
        activity.showProgressBar();
        service.Checkuser(user, psw, new Callback<String>() {
            @Override
            public void success(String s, Response response) {

                if (s.equals("true") || s.equals("True")) {
                    getHustonlineId(user, psw);
                } else {
                    Util.toast("登录失败，帐号或密码错误");
                    activity.stopProgressBar();
                }

            }

            @Override
            public void failure(RetrofitError error) {
                activity.stopProgressBar();
                Util.toast("网络出错" + error.getMessage());
            }
        });
    }

    // （ 2 ）
    private void getHustonlineId(final  String user, final String psw){
        service.getHustonlineId(user,new Callback<String>() {
            @Override
            public void success(String s, Response response) {

                if(!(s.equals("false")||s.equals("False"))){
                    getUserName(user, psw, Long.parseLong(s));
                }else {
                    Util.toast("登录失败,获取id失败");
                    activity.stopProgressBar();
                }

            }

            @Override
            public void failure(RetrofitError error) {
                activity.stopProgressBar();
                Util.toast("网络出错" + error.getMessage());
            }
        });
    }

    // （ 3 ）
    private void getUserName(final  String user, final String psw, final long id){
        service.getUserName(user, new Callback<String>() {
            @Override
            public void success(String s, Response response) {

                if (!(s.equals("false") || s.equals("False"))) {
                    hustAccountManager.putEmailandPsw(user, psw);
                    hustAccountManager.putUid(id);
                    hustAccountManager.putUserName(s);
                    hustAccountManager.setIsEmailChecked(true);
                    Util.toast("登录成功，正在同步");
                    asynAccount(id, psw);  // 同步
                } else {
                    Util.toast("登录失败，获取用户名失败");
                    activity.stopProgressBar();
                }

            }

            @Override
            public void failure(RetrofitError error) {
                activity.stopProgressBar();
                Util.toast("网络出错" + error.getMessage());
            }
        });
    }

    // ( 4 )
    private void asynAccount(final long id, final String psw){
        API.HustpassService service = RestHelper.getService(API.HustpassService.HOST,API.HustpassService.class);
        service.getelecInfo(id, psw, new Callback<AccountBean.ElecAccountBean>() {
            @Override
            public void success(AccountBean.ElecAccountBean elecAccountBean, Response response) {
                AccountManager.getElecManger().updateBoundAccount(elecAccountBean.getAreaId(),elecAccountBean.getBuildingId(),elecAccountBean.getRoomId());
                hustAccountManager.setIsElecAsyned(true);
                add();
            }

            @Override
            public void failure(RetrofitError error) {
                add();
            }

            private void add(){
                addAsynState();
            }
        });

        service.getHubInfo(id, psw, new Callback<AccountBean.HubAccountBean>() {
            @Override
            public void success(AccountBean.HubAccountBean s, Response response) {
                AccountManager.getHubManger().updateBound(s.getHubId(),s.getHubPasswd());
                hustAccountManager.setIsHubAsyned(true);
                add();
            }

            @Override
            public void failure(RetrofitError error) {
                add();
            }

            private void add() {
                addAsynState();
            }
        });

        service.getwifiInfo(id, psw, new Callback<AccountBean.WifiAccountBean>() {
            @Override
            public void success(AccountBean.WifiAccountBean s, Response response) {
                AccountManager.getWifiManger().updateBound(s.getWifiId(),s.getWifiPasswd());
                hustAccountManager.setIsWifiAsyned(true);
                add();
            }

            @Override
            public void failure(RetrofitError error) {
                add();
            }

            private void add() {
                addAsynState();
            }
        });
    }


    private int state = 0;
    private void addAsynState(){
        state++;
        if(state==3){
            Util.toast("同步完成");
            activity.stopProgressBar();
            activity.startActivity(new Intent(activity, UserActivity.class));
            activity.finish();
        }
    }

    @Override
    public void onLostFocus(int id) {

    }
}
