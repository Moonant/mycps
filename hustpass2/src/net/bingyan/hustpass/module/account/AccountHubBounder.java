package net.bingyan.hustpass.module.account;

import android.content.Intent;
import android.text.TextUtils;
import android.widget.EditText;

import com.umeng.analytics.MobclickAgent;

import net.bingyan.hustpass.API;
import net.bingyan.hustpass.R;
import net.bingyan.hustpass.helper.AccountManager;
import net.bingyan.hustpass.http.RestHelper;
import net.bingyan.hustpass.module.score.HubCheckBean;
import net.bingyan.hustpass.module.score.ScoreActivity;
import net.bingyan.hustpass.module.score.ScoreHelper;
import net.bingyan.hustpass.ui.AccountActivity;
import net.bingyan.hustpass.util.AppLog;
import net.bingyan.hustpass.util.Util;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by ant on 14-8-15.
 */
public class AccountHubBounder implements AccountActivity.AccountActionListener{
    AppLog mLog = new AppLog(getClass());
    AccountActivity activity;
    EditText userET;
    EditText pswET;

    public AccountHubBounder(AccountActivity activity){
        this.activity = activity;
        activity.setTitle("Hub绑定");
        activity.setContentView(R.layout.activity_account_bound_hub);
        init();
    }

    private void init(){
        userET = activity.initEditView(R.id.account_username);
        pswET = activity.initEditView(R.id.account_psw);

        activity.initBtnView(R.id.account_btn_cancle);
        activity.initBtnView(R.id.account_btn_bound);
    }

    private void bound(){
        final String user = userET.getText().toString().trim();
        final String psw = pswET.getText().toString().trim();

        if(TextUtils.isEmpty(user)||TextUtils.isEmpty(psw)){
            Util.toast(R.string.account_input_empty_alert);
            return;
        }
        mLog.v("start check");

        activity.showProgressBar();

//        RestHelper.getService(API.HubService.CheckPswHOST,API.HubService.class).checkPsw(user,psw,new Callback<String>() {
//        RestHelper.getService(API.HubServiceV2.CheckPswHOST,API.HubServiceV2.class)
        (new ScoreHelper()).checkPsw(user, psw, new Callback<HubCheckBean>() {
                    @Override
                    public void success(HubCheckBean hubCheckBean, Response response) {
                        if (hubCheckBean != null && hubCheckBean.getCode() == 0) {
                            AccountManager.HubAccountManager hubAccountManager = AccountManager.getHubManger();
                            hubAccountManager.updateBound(user, psw);
                            asyn(user, psw);
                        } else if (hubCheckBean != null ) {
                            Util.toast("帐号或密码错误");
                            activity.stopProgressBar();
                        } else {
                            Util.toast("网络接口出错，请等待修复");
                            activity.stopProgressBar();
                        }

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        mLog.v("网络错误" + error.getUrl() + error.getMessage());
                        Util.toast("网络错误" + error.getMessage());
                        activity.stopProgressBar();
                    }
                });
    }

    private void asyn(String user, String psw){
        // asyn
        final AccountManager.HustAccountManager hustAccountManager = AccountManager.getHustManger();
        hustAccountManager.setIsHubAsyned(false);

        if(!hustAccountManager.isLogin()){
            Util.toast("已绑定,但华中大通行证未登录，无法同步");
            activity.finish();
            return;
        }

        if(!hustAccountManager.isEmailChecked()){
            Util.toast("已绑定,但华中大通行证未登录，无法同步");
            activity.finish();
            return;
        }

        mLog.v(user+"\n"+psw+"\n"+hustAccountManager.getUid()+"\n"+hustAccountManager.getPsw());
        API.HustpassService hustpassService = RestHelper.getService(API.HustpassService.HOST, API.HustpassService.class);

        hustpassService.updateHubInfo(user, psw, hustAccountManager.getUid(), hustAccountManager.getPsw(), new Callback<Integer>() {
                    @Override
                    public void success(Integer s, Response response) {
                        if (s == 1) {
                            Util.toast("同步成功");
                            MobclickAgent.onEvent(activity, "hub_bound_success");
                            hustAccountManager.setIsHubAsyned(true);
                            activity.finish();
                        } else {
                            Util.toast("同步失败" + s);
                        }
                        activity.stopProgressBar();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        error.printStackTrace();
//                        mLog.v("网络出错" + error.getUrl() + "\n" + error.getMessage() + "\n" + error.getBody() + "\n");
                        Util.toast("网络或接口出错"+error.getMessage()+",已保存到本地，未同步");
                        activity.stopProgressBar();
                        activity.finish();
                    }
                });
    }

    @Override
    public void onClick(int id) {
        if(id == R.id.account_btn_cancle){
            activity.finish();
        }else if(id == R.id.account_btn_bound){
            bound();
        }
    }

    @Override
    public void onLostFocus(int id) {

    }
}