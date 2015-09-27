package net.bingyan.hustpass.module.account;

import android.app.ActionBar;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

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
 * Created by ant on 14-8-22.
 */
public class AccountElecBounder implements AccountActivity.AccountActionListener{
    AccountActivity activity;

    TextView slectArea;
    EditText building;
    EditText room;


    int[] areaId = { R.string.area_1, R.string.area_2, R.string.area_3,
            R.string.area_4 };

    PopupWindow popupWindow;
    AccountManager.ElecAccountManager elecAccountManager;

    public AccountElecBounder(AccountActivity activity){
        this.activity = activity;
        activity.setTitle("宿舍绑定");
        activity.setContentView(R.layout.activity_account_bound_elec);

        elecAccountManager = AccountManager.getElecManger();
        initView();
    }

    private void initView() {
        slectArea = (TextView) activity.findViewById(R.id.elec_slec_xiaoqu);
        building = (EditText) activity.findViewById(R.id.elec_text_building);
        room = (EditText) activity.findViewById(R.id.elec_text_room);

        activity.initBtnView(R.id.account_btn_cancle);
        activity.initBtnView(R.id.account_btn_bound);

//        if(elecAccountManager.isLogin()){
//            slectArea.setText(elecAccountManager.getArea());
//            building.setText(elecAccountManager.getBuilding());
//            room.setText(elecAccountManager.getRoom());
//        }

        slectArea.setOnClickListener(activity);

        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_elec_popwindow, null, false);
        view.findViewById(R.id.elec_area_1).setOnClickListener(activity);
        view.findViewById(R.id.elec_area_2).setOnClickListener(activity);
        view.findViewById(R.id.elec_area_3).setOnClickListener(activity);
        view.findViewById(R.id.elec_area_4).setOnClickListener(activity);

        popupWindow = new PopupWindow(view, ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.WRAP_CONTENT){
            @Override
            public void showAsDropDown(View anchor) {
                super.showAsDropDown(anchor);
                slectArea.setBackgroundResource(R.drawable.elec_area_text_bg);
            }

            @Override
            public void dismiss() {
                super.dismiss();
                slectArea.setBackgroundResource(R.drawable.app_edit_shape_grey);
            }
        };
        popupWindow.setBackgroundDrawable(activity.getResources().getDrawable(
                R.color.transparent));
        popupWindow.setFocusable(true);
    }

    private void bound(){
        String area = slectArea.getText().toString().trim();
        String buildingNum = building.getText().toString().trim();
        String roomNum = room.getText().toString().trim();
        if (area.equals("校区") || TextUtils.isEmpty(buildingNum) || TextUtils.isEmpty(roomNum)) {
            Util.toast("信息不完整");
            return;
        }

        elecAccountManager.updateBoundAccount(area, Integer.valueOf(buildingNum), roomNum);

        // asyn
        final AccountManager.HustAccountManager hustAccountManager = AccountManager.getHustManger();
        hustAccountManager.setIsElecAsyned(false);

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

        RestHelper.getService(API.HustpassService.HOST, API.HustpassService.class).updateelecInfo(area, Integer.valueOf(buildingNum), roomNum
                , hustAccountManager.getUid()
                , hustAccountManager.getPsw(), new Callback<Integer>() {
            @Override
            public void success(Integer s, Response response) {
                if (s == 1) {
                    Util.toast("同步成功");
                    MobclickAgent.onEvent(activity, "elec_bound_success");
                    hustAccountManager.setIsElecAsyned(true);
                    activity.finish();
                } else {
                    Util.toast("同步失败" + s);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Util.toast("网络或接口出错"+error.getMessage()+",已保存到本地，未同步");
                activity.stopProgressBar();
                activity.finish();
            }
        });


    }


    @Override
    public void onClick(int id) {
        // TODO Auto-generated method stub
        if(id!=R.id.elec_slec_xiaoqu)
            slectArea.setSelected(false);
        switch (id) {
            case R.id.elec_slec_xiaoqu:
                popupWindow.showAsDropDown(slectArea);
                slectArea.setSelected(true);
                break;

            case R.id.account_btn_cancle:
                activity.finish();
                break;
            case R.id.account_btn_bound:
                bound();
                break;

            case R.id.elec_area_1:
                slectArea.setText(activity.getString(areaId[0]));
                popupWindow.dismiss();
                break;
            case R.id.elec_area_2:
                slectArea.setText(activity.getString(areaId[1]));
                popupWindow.dismiss();
                break;
            case R.id.elec_area_3:
                slectArea.setText(activity.getString(areaId[2]));
                popupWindow.dismiss();
                break;
            case R.id.elec_area_4:
                slectArea.setText(activity.getString(areaId[3]));
                popupWindow.dismiss();
                break;
            default:
                break;
        }
    }

    @Override
    public void onLostFocus(int id) {

    }
}