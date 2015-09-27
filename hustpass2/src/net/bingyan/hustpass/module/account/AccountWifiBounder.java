package net.bingyan.hustpass.module.account;

import android.text.TextUtils;
import android.widget.EditText;

import com.umeng.analytics.MobclickAgent;

import net.bingyan.hustpass.API;
import net.bingyan.hustpass.R;
import net.bingyan.hustpass.helper.AccountManager;
import net.bingyan.hustpass.http.RestHelper;
import net.bingyan.hustpass.ui.AccountActivity;
import net.bingyan.hustpass.util.Util;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by ant on 14-8-15.
 */
public class AccountWifiBounder implements AccountActivity.AccountActionListener{
    AccountActivity activity;
    EditText userET;
    EditText pswET;

    public AccountWifiBounder(AccountActivity activity){
        this.activity = activity;
        activity.setTitle("校园网绑定");
        activity.setContentView(R.layout.activity_account_bound_wifi);
        init();
    }

    private void init(){
        userET = activity.initEditView(R.id.account_username);
        pswET = activity.initEditView(R.id.account_psw);

        activity.initBtnView(R.id.account_btn_cancle);
        activity.initBtnView(R.id.account_btn_bound);
    }

    private void bound(){
        String user = userET.getText().toString().trim();
        String psw = pswET.getText().toString().trim();

        if(TextUtils.isEmpty(user)||TextUtils.isEmpty(psw)){
            Util.toast(R.string.account_input_empty_alert);
            return;
        }

        AccountManager.WifiAccountManager wifiAccountManager = AccountManager.getWifiManger();
        wifiAccountManager.updateBound(user,psw);

        // asyn
        final AccountManager.HustAccountManager hustAccountManager = AccountManager.getHustManger();
        hustAccountManager.setIsWifiAsyned(false);

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

//        Util.toast(hustAccountManager.getUid()+hustAccountManager.getPsw());

        RestHelper.getService(API.HustpassService.HOST,API.HustpassService.class).updateWifiInfo(user,psw,hustAccountManager.getUid(),hustAccountManager.getPsw(),new Callback<Integer>() {
            @Override
            public void success(Integer s, Response response) {
                if(s==1){
                    Util.toast("同步成功");
                    MobclickAgent.onEvent(activity, "wifi_bound_success");
                    hustAccountManager.setIsWifiAsyned(true);
                    activity.finish();
                }else {
                    Util.toast("同步失败"+s);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Util.toast("网络或接口出错"+error.getMessage()+",已保存到本地，未同步");
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