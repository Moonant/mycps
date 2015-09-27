package net.bingyan.hustpass.module.account;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import net.bingyan.hustpass.API;
import net.bingyan.hustpass.R;
import net.bingyan.hustpass.http.RestHelper;
import net.bingyan.hustpass.ui.AccountActivity;
import net.bingyan.hustpass.ui.HomeActivity;
import net.bingyan.hustpass.ui.UserActivity;
import net.bingyan.hustpass.util.AppLog;
import net.bingyan.hustpass.util.Util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by ant on 14-8-15.
 */
public class AccountHustRegister implements AccountActivity.AccountActionListener {
    AppLog mLog = new AppLog(getClass());

    AccountActivity activity;
    EditText userET;
    EditText emailET;
    EditText pswET;
    EditText pswTwiceET;

    TextView userAlertTv;
    TextView emailAlertTv;
    TextView pswAlertTv;
    TextView pswTeiceAlertTv;

    String username;
    String email;
    String psw;
    String pswTemp;

    String yes;
    String no;

    API.HustOnlineService service;

    public AccountHustRegister(AccountActivity activity){
        this.activity = activity;
        init();
    }

    private void init(){
        activity.setContentView(R.layout.activity_account_hust_reg);
        activity.setTitle("通行证注册");

        userET = activity.initEditView(R.id.account_username);
        userAlertTv = (TextView)activity.findViewById(R.id.account_alert_username);
        emailET = activity.initEditView(R.id.account_email);
        emailAlertTv = (TextView)activity.findViewById(R.id.account_alert_email);
        pswET = activity.initEditView(R.id.account_psw);
        pswAlertTv = (TextView)activity.findViewById(R.id.account_alert_psw);
        pswTwiceET = activity.initEditView(R.id.account_psw_twice);
        pswTeiceAlertTv = (TextView)activity.findViewById(R.id.account_alert_psw_twice);

        activity.initBtnView(R.id.account_login);
        activity.initBtnView(R.id.account_reg);

        yes = activity.getString(R.string.yes);
        no = activity.getString(R.string.no);

        service = RestHelper.getService(API.HustOnlineService.HOST, API.HustOnlineService.class);
    }

    @Override
    public void onClick(int id) {

        if (id == R.id.account_reg){
            reg();
        }else if(id == R.id.account_login){
            login();
        }

    }

    private void reg(){
        checkPswTwice();
        if(TextUtils.isEmpty(username)||TextUtils.isEmpty(email)||TextUtils.isEmpty(psw)){
            Util.toast("请完善信息");
            return;
        }

        activity.showProgressBar();
        service.adduser(username,email,psw,new Callback<String>() {
            @Override
            public void success(String s, Response response) {
                activity.stopProgressBar();
                mLog.v("result:"+s);
                if(s.equals("True")||s.equals("true")){
                    Util.toast("注册完成，2小时内去邮箱激活帐号，才可使用");
                    MobclickAgent.onEvent(activity, "reg_success");

                    activity.startActivity(new Intent(activity, UserActivity.class));
                    activity.finish();
                }else {
                    Util.toast("注册失败。。。"+s);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                activity.stopProgressBar();
                Util.toast("网络问题，注册失败。。。"+error.getMessage());
                mLog.v(error.getMessage());
            }
        });
    }

    private void login(){
        Intent intent = new Intent(activity,AccountActivity.class);
        intent.putExtra(AccountActivity.STATE_TAG,AccountActivity.STATE_HUST_LOGIN);
        activity.startActivity(intent);
        activity.finish();
    }

    private void checkUserName(){
        final String user = userET.getText().toString().trim();

        if(TextUtils.isEmpty(user)){
            setAlertText(userET,userAlertTv,"不能为空");
            return;
        }

        Pattern pattern = Pattern.compile("[^\\x00-\\xff]"); // 无法连接指定网页 ， 继续
        Matcher matcher = pattern.matcher(user);

        if(matcher.find()){
            setAlertText(userET,userAlertTv,"不能包含中文字符");
            return ;
        }

        service.checkUsername(user,new Callback<String>() {
            @Override
            public void success(String s, Response response) {
                if(s.equals("1")){
                    setAlertText(userET,userAlertTv,"用户名已经被注册");
                }else {
                    clearAlert(userET,userAlertTv,"");
                    username = user;
                }
            }

            @Override
            public void failure(RetrofitError error) {
                setAlertText(userET,userAlertTv,"网络问题无法验证"+error.getMessage());
            }
        });
    }

    private void checkEmail(){
        final String emailTemp = emailET.getText().toString().trim();

        if(TextUtils.isEmpty(emailTemp)){
            setAlertText(emailET,emailAlertTv,"不能为空");
            return;
        }

        service.checkEmail(emailTemp, new Callback<String>() {
            @Override
            public void success(String s, Response response) {
                mLog.v("email response:"+response.toString());
//                response.toString();
                Util.toast("email response:"+response.toString());
                mLog.v("email:"+s);
                Util.toast("email:"+"邮箱可用");
                if(s.equals("True")||s.equals("true")){
                    setAlertText(emailET,emailAlertTv,"邮箱已经被注册"+s);
                }else {
                    clearAlert(emailET,emailAlertTv);
                    email = emailTemp;
                }
            }

            @Override
            public void failure(RetrofitError error) {
                setAlertText(emailET,emailAlertTv,"网络问题无法验证"+error.getMessage());
            }
        });
    }

    private void checkPsw(){
        String pswTemp = pswET.getText().toString().trim();

        if(TextUtils.isEmpty(pswTemp)){
            setAlertText(pswET,pswAlertTv,"不能为空");
            return;
        }

        clearAlert(pswET,pswAlertTv);
        this.pswTemp = pswTemp;
    }

    private void checkPswTwice(){
        String pswTwice = pswTwiceET.getText().toString().trim();

        if(TextUtils.isEmpty(pswTwice)){
            setAlertText(pswTwiceET,pswTeiceAlertTv,"不能为空");
            return;
        }

        if(pswTemp!=null&&pswTwice.equals(pswTemp)){
            clearAlert(pswTwiceET,pswTeiceAlertTv);
            psw = pswTwice;
        }else {
            setAlertText(pswTwiceET,pswTeiceAlertTv,"密码不一致");
        }
    }



    private void setAlertText(EditText et,TextView alertTv ,String msg){
        et.setBackgroundResource(R.drawable.app_edit_shape_red);
        alertTv.setText(no+msg);

        if(alertTv.getVisibility()!=View.VISIBLE) {
            alertTv.setVisibility(View.VISIBLE);
        }

    }
    private void clearAlert(EditText et,TextView alertTv ){
        et.setBackgroundResource(R.drawable.app_edit_bg_selector);
        alertTv.setText(yes);

        if(alertTv.getVisibility()!=View.VISIBLE) {
            alertTv.setVisibility(View.VISIBLE);
        }

    }
    private void clearAlert(EditText et,TextView alertTv ,String msg){
        et.setBackgroundResource(R.drawable.app_edit_bg_selector);
        alertTv.setText(yes+msg);

        if(alertTv.getVisibility()!=View.VISIBLE) {
            alertTv.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onLostFocus(int id) {
        switch (id) {
            case R.id.account_username:
                checkUserName();
                break;
            case R.id.account_email:
                checkEmail();
                break;
            case R.id.account_psw:
                checkPsw();
                break;
            case R.id.account_psw_twice:
                checkPswTwice();
                break;
        }
    }


}
