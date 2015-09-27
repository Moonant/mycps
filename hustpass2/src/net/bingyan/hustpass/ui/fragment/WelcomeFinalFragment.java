package net.bingyan.hustpass.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.actionbarsherlock.app.SherlockFragment;
import com.umeng.analytics.MobclickAgent;

import net.bingyan.hustpass.R;
import net.bingyan.hustpass.ui.AccountActivity;
import net.bingyan.hustpass.ui.HomeActivity;

import java.util.HashMap;

/**
 * Created by ant on 14-8-22.
 */
public class WelcomeFinalFragment extends SherlockFragment implements View.OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.welcome3_fragment, container, false);
        initRotateView(view);
        view.findViewById(R.id.account_login).setOnClickListener(this);
        view.findViewById(R.id.account_reg).setOnClickListener(this);
        view.findViewById(R.id.welcome_btn_enter_without_login).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getActivity(), AccountActivity.class);

        HashMap<String,String> map = new HashMap<String,String>();
        String type = "";
        switch (view.getId()) {
            case R.id.account_login:
                type = "login";
                break;
            case R.id.account_reg:
                type = "reg";
                break;
            case R.id.welcome_btn_enter_without_login:
                type = "enter";
                break;
        }
        map.put("type",type);
        MobclickAgent.onEvent(getActivity(), "welcome_login", map);

        switch (view.getId()) {
            case R.id.account_login:
                intent.putExtra(AccountActivity.STATE_TAG, AccountActivity.STATE_HUST_LOGIN);
                startActivity(intent);
//                getActivity().finish();
                break;
            case R.id.account_reg:
                intent.putExtra(AccountActivity.STATE_TAG, AccountActivity.STATE_HUST_REG);
                startActivity(intent);
//                getActivity().finish();
                break;
            case R.id.welcome_btn_enter_without_login:
                startActivity(new Intent(getActivity(), HomeActivity.class));
                getActivity().finish();
                break;
        }
    }

    private void initRotateView(View view) {
        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.welcome_wea);
        view.findViewById(R.id.welcome_wea).setAnimation(animation);
        animation = AnimationUtils.loadAnimation(getActivity(), R.anim.welcome_earth);
        view.findViewById(R.id.welcome_earth).setAnimation(animation);
        animation = AnimationUtils.loadAnimation(getActivity(), R.anim.welcome_woman);
        view.findViewById(R.id.welcome_woman).setAnimation(animation);
    }
}
