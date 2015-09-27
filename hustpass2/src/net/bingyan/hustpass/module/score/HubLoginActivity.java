package net.bingyan.hustpass.module.score;

import net.bingyan.hustpass.API;
import net.bingyan.hustpass.R;
import net.bingyan.hustpass.helper.AccountManager;
import net.bingyan.hustpass.http.RestHelper;
import net.bingyan.hustpass.util.AppLog;
import net.bingyan.hustpass.ui.base.BaseActivity;
import net.bingyan.hustpass.util.Util;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class HubLoginActivity extends BaseActivity {
    AppLog mLog = new AppLog(getClass());
	EditText uidET;
	EditText psWordET;

    AccountManager.HubAccountManager hubAccountManager ;

	@Override
	public void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
        setContentView(R.layout.activity_hub_login);
        hubAccountManager = AccountManager.getHubManger();
        init();
	}

    @Override
    protected void onResume() {
        super.onResume();
//        Util.toast(R.string.account_alert_cannot_save_msg);
    }

    public void checkPsw(){
        final String user = uidET.getText().toString().trim().toUpperCase();
        final String psw = psWordET.getText().toString().trim();

//        if (psw.equals("adminpsw")) {
//            mLog.v(" get admin psw ");
//            hubAccountManager.updateAccount(user, psw);
//            Intent intent = new Intent(HubLoginActivity.this,ScoreActivity.class);
//            startActivity(intent);
//            this.finish();
//            return;
//        }

        final ProgressDialog progressDialog = ProgressDialog.show(HubLoginActivity.this, "", "验证中", true);
//        RestHelper.getService(API.HubService.CheckPswHOST,API.HubService.class).checkPsw(user,psw,new Callback<String>() {
//          RestHelper.getService(API.HubServiceV2.CheckPswHOST,API.HubServiceV2.class)
        (new ScoreHelper()).checkPsw(user,psw,new Callback<HubCheckBean>() {

            @Override
            public void success(HubCheckBean hubCheckBean, Response response) {
                progressDialog.dismiss();

                if (hubCheckBean != null && hubCheckBean.getCode()==0) {
                    hubAccountManager.updateAccount(user, psw);

//                    Bundle bundle = new Bundle();
//                    bundle.putString("uid", user);
//                    bundle.putString("psw", psw);
                    Intent intent = new Intent(HubLoginActivity.this,ScoreActivity.class);
                    intent.putExtra("temp",true);
//                    intent.putExtras(bundle);
                    startActivity(intent);
                    HubLoginActivity.this.finish();
                }else if(hubCheckBean != null ) {
                    Util.toast("帐号或密码错误");
                }else {
                    Util.toast("网络接口出错，请等待修复");
                }

            }

            @Override
            public void failure(RetrofitError error) {
                mLog.v(error.getUrl()+":"+error.getMessage());
                progressDialog.dismiss();
                Util.toast("网络错误"+error.getMessage());
            }
        });
    }

    private void init(){
        initView();
    }


	private void initView() {
		uidET = (EditText) findViewById(R.id.hub_login_num);
		psWordET = (EditText) findViewById(R.id.hub_login_psword);

        if (hubAccountManager.getUid() != null) {
			uidET.setText(hubAccountManager.getUid());
		}

        findViewById(R.id.hub_login_search).setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		super.onClick(arg0);
		if (arg0.getId() == R.id.hub_login_search) {
            checkPsw();
		}
	}
}
