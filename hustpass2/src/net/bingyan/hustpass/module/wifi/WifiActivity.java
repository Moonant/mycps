package net.bingyan.hustpass.module.wifi;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import net.bingyan.hustpass.R;
import net.bingyan.hustpass.helper.AccountManager;
import net.bingyan.hustpass.service.WifiConnectService;
import net.bingyan.hustpass.ui.base.BaseActivity;
import net.bingyan.hustpass.util.AppLog;
import net.bingyan.hustpass.util.Util;

public class WifiActivity extends BaseActivity {

	EditText userET;
	EditText pswordET;

    AccountManager.WifiAccountManager manager;

	@Override
	public void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
        manager = AccountManager.getWifiManger();

        initViewLogin();


	}


    private void initViewLogin(){
        setContentView(R.layout.activity_wifi_login);
        userET = (EditText) findViewById(R.id.wifi_login_user);
        pswordET = (EditText) findViewById(R.id.wifi_login_psword);
        if(manager.isBound()){
            userET.setText(manager.getBoundUid());
            pswordET.setText(manager.getBoundPsw());
        }

        findViewById(R.id.wifi_login_btn).setOnClickListener(this);
    }

	private void login() {
		String user = userET.getText().toString().trim();
		String psword = pswordET.getText().toString().trim();
		if (!TextUtils.isEmpty(user) && !TextUtils.isEmpty(psword)) {

            Intent intent = new Intent(this, WifiConnectService.class);
            intent.putExtra("withuser",true);
            intent.putExtra("user",user);
            intent.putExtra("psw",psword);
            startService(intent);

		}else {
            Util.toast(R.string.account_input_empty_alert);
        }
	}

    private void connectWifi(boolean temp){
        mLog.v("connect");
        Intent serviceIntent = new Intent(this, WifiConnectService.class);
        serviceIntent.putExtra("temp",temp);

        startService(serviceIntent);
    }

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

        if (arg0.getId() == R.id.wifi_login_btn) {
    			login();

			return;
		}

        super.onClick(arg0);
	}
}
