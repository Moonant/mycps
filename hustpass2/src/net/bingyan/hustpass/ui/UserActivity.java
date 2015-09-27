package net.bingyan.hustpass.ui;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.FeedbackAgent;

import net.bingyan.hustpass.R;
import net.bingyan.hustpass.helper.AccountManager;
import net.bingyan.hustpass.ui.base.BaseActivity;
import net.bingyan.hustpass.ui.pref.PrefActivity;

import java.io.File;
import java.util.HashMap;

/**
 * Created by ant on 14-8-16.
 */
public class UserActivity extends BaseActivity{

    ImageView hubBoundBtn;
    ImageView wifiBoundBtn;
    ImageView elecBoundBtn;

    ImageView userImg;
    File sdcardTempFile;

    TextView hubUid;
    TextView wifiUid;
    TextView elecUid;

    AccountManager.HubAccountManager hubAccountManager;
    AccountManager.WifiAccountManager wifiAccountManager;
    AccountManager.ElecAccountManager elecAccountManager;
    AccountManager.HustAccountManager hustAccountManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        hustAccountManager = AccountManager.getHustManger();
        hubAccountManager = AccountManager.getHubManger();
        wifiAccountManager = AccountManager.getWifiManger();
        elecAccountManager = AccountManager.getElecManger();

        init();
    }

    private void init(){
        initView();

        // image
//        sdcardTempFile = new File("/mnt/sdcard/hustpass/", "user_pic.jpg");
        sdcardTempFile = new File("/mnt/sdcard/", "user_pic.jpg");
        userImg = (ImageView) findViewById(R.id.user_activity_userimg);
        userImg.setOnClickListener(this);
        Bitmap bmp = BitmapFactory.decodeFile(sdcardTempFile.getAbsolutePath());
        if(bmp!=null) {
            userImg.setImageBitmap(bmp);
        }else {
            userImg.setImageResource(R.drawable.defoult_head);
        }

        // userName
        TextView userName = (TextView) findViewById(R.id.user_activity_username);
        if(hustAccountManager.isLogin()&&hustAccountManager.isEmailChecked()){
            userName.setText(hustAccountManager.getUserName());
            Resources resource = getBaseContext().getResources();
            ColorStateList csl = resource.getColorStateList(R.color.text_black);
            userName.setTextColor(csl);
        }else {
            userName.setOnClickListener(this);
        }
    }

    private void initView(){
        findViewById(R.id.user_btn_setting).setOnClickListener(this);
        findViewById(R.id.user_btn_feedback).setOnClickListener(this);

        hubBoundBtn = (ImageView) findViewById(R.id.account_btn_hub_bound);
        hubUid = (TextView) findViewById(R.id.account_tv_hub_uid);
        hubBoundBtn.setOnClickListener(this);
        setHubBoundView();

        wifiBoundBtn = (ImageView) findViewById(R.id.account_btn_wifi_bound);
        wifiUid = (TextView) findViewById(R.id.account_tv_wifi_uid);
        wifiBoundBtn.setOnClickListener(this);
        setWifiBoundView();

        elecBoundBtn = (ImageView) findViewById(R.id.account_btn_elec_bound);
        elecUid = (TextView) findViewById(R.id.account_tv_elec_uid);
        elecBoundBtn.setOnClickListener(this);
        setElecBoundView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setHubBoundView();
        setWifiBoundView();
        setElecBoundView();
    }

    private void setHubBoundView(){
        if(hubAccountManager.isBound()){
            hubBoundBtn.setImageResource(R.drawable.btn_account_to_bound);
            hubUid.setText(hubAccountManager.getBoundUid());
        }else {
            hubBoundBtn.setImageResource(R.drawable.btn_account_bounded);
            hubUid.setText("---------");
        }
    }

    private void setWifiBoundView(){
        if(wifiAccountManager.isBound()){
            wifiBoundBtn.setImageResource(R.drawable.btn_account_to_bound);
            wifiUid.setText(wifiAccountManager.getBoundUid());
        }else {
            wifiBoundBtn.setImageResource(R.drawable.btn_account_bounded);
            wifiUid.setText("---------");
        }
    }

    private void setElecBoundView(){
        if(elecAccountManager.isBound()){
            elecBoundBtn.setImageResource(R.drawable.btn_account_to_bound);
            elecUid.setText(elecAccountManager.getBoundRoom());
        }else {
            elecBoundBtn.setImageResource(R.drawable.btn_account_bounded);
        }
    }

    private void startBound(int state){
            Intent intent = new Intent(this, AccountActivity.class);
            intent.putExtra(AccountActivity.STATE_TAG,state);
            startActivity(intent);

        HashMap<String,String> map = new HashMap<String,String>();
        map.put("type",""+state);
        MobclickAgent.onEvent(this, "account_bound", map);

    }

    private void chooseImg(){

        int crop = 300;

        Intent intent = new Intent("android.intent.action.PICK");
        intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*");
        intent.putExtra("output", Uri.fromFile(sdcardTempFile));
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);// 裁剪框比例
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", crop);// 输出图片大小
        intent.putExtra("outputY", crop);
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100) {
            Bitmap bmp = BitmapFactory.decodeFile(sdcardTempFile.getAbsolutePath());
            userImg.setImageBitmap(bmp);
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        startActivity(new Intent(this,HomeActivity.class));
        this.finish();
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()){
            case R.id.user_btn_feedback:
                FeedbackAgent feedbackAgent = new FeedbackAgent(this);
                feedbackAgent.sync();
                feedbackAgent.startFeedbackActivity();
                break;
            case R.id.user_btn_setting:
                startActivity(new Intent(this, PrefActivity.class));
                break;
            case R.id.user_activity_username:

                if(!hustAccountManager.isLogin()){
                    Intent intent = new Intent(this, AccountActivity.class);
                    intent.putExtra(AccountActivity.STATE_TAG,AccountActivity.STATE_HUST_LOGIN);
                    startActivity(intent);
                    this.finish();
                }
                break;
            case R.id.user_activity_userimg:
                chooseImg();
                break;

            case R.id.account_btn_hub_bound:
                startBound(AccountActivity.STATE_HUB_BOUND);
                break;
            case R.id.account_btn_wifi_bound:
                startBound(AccountActivity.STATE_WIFI_BOUND);
                break;
            case R.id.account_btn_elec_bound:
                startBound(AccountActivity.STATE_ELEC_LOGIN);
                break;
        }

        super.onClick(arg0);
    }
}
