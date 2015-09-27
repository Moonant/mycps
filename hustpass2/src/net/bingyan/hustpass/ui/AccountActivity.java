package net.bingyan.hustpass.ui;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import net.bingyan.hustpass.module.account.AccountElecBounder;
import net.bingyan.hustpass.module.account.AccountHubBounder;
import net.bingyan.hustpass.module.account.AccountHustLoginer;
import net.bingyan.hustpass.module.account.AccountHustRegister;
import net.bingyan.hustpass.module.account.AccountWifiBounder;
import net.bingyan.hustpass.ui.base.BaseActivity;

/**
 * Created by ant on 14-8-15.
 */
public class AccountActivity extends BaseActivity implements View.OnFocusChangeListener {

    public static final int STATE_HUB_BOUND = 0;
    public static final int STATE_WIFI_BOUND = 1;
    public static final int STATE_HUST_REG = 3;
    public static final int STATE_HUST_LOGIN = 4;
    public static final int STATE_ELEC_LOGIN = 2;

    public static final String STATE_TAG = "Account_state_tag";

    AccountActionListener listener ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int state = getIntent().getIntExtra(STATE_TAG,STATE_HUST_REG);
        switch (state){
            case STATE_HUST_LOGIN:
                listener = new AccountHustLoginer(this);
                break;
            case STATE_HUST_REG:
                listener = new AccountHustRegister(this);
                break;
            case STATE_WIFI_BOUND:
                listener = new AccountWifiBounder(this);
                break;
            case STATE_HUB_BOUND:
                listener = new AccountHubBounder(this);
                break;
            case STATE_ELEC_LOGIN:
                listener = new AccountElecBounder(this);
                break;
        }
    }

    @Override
    public void onClick(View arg0) {
        listener.onClick(arg0.getId());
        super.onClick(arg0);
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        if(!b){
            listener.onLostFocus(view.getId());
        }
    }

    public EditText initEditView(int id){
        EditText editText = (EditText)findViewById(id);
        editText.setOnClickListener(this);
        editText.setOnFocusChangeListener(this);
        return editText;
    }

    public void initBtnView(int id){
        findViewById(id).setOnClickListener(this);
    }

    public interface AccountActionListener{
        public void onClick(int id);
        public void onLostFocus(int id);
    }
}
